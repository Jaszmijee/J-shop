package com.example.jshop.configuration;

import com.example.jshop.security.AuthenticationProcessingFilter;
import com.example.jshop.security.AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationProvider authProvider;
    @Lazy
    private final AuthenticationManager authManager;

   /* @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProcessingFilter authenticationProcessingFilter() {
        val filter = new AuthenticationProcessingFilter("/auth/login");
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationManager(authManager);
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // prettier-ignore
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/v1/j-shop/admin/*").hasAuthority("managerGroup")
                        .antMatchers("/v1/j-shop/customer/*").hasAuthority("clientGroup")
                        .antMatchers("/v1/j-shop/cart/pay/login").authenticated()
                        .antMatchers("/v1/j-shop/cart/finalize/login").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin().disable()
                .oauth2Login().and()
                .build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        val authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .password()
                        .refreshToken()
                        .build();

        val authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

        return authorizedClientManager;
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
        return authorizeRequest -> {
            val contextAttributes = new HashMap<String, Object>();
            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, authorizeRequest.getPrincipal().getPrincipal());
            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, authorizeRequest.getPrincipal().getCredentials());
            return contextAttributes;
        };
    }
}
