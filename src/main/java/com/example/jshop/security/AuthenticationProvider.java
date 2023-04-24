package com.example.jshop.security;

import camundajar.impl.com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    @Lazy
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        val authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal(authentication)
                .build();
        val authorize = authorizedClientManager.authorize(authorizeRequest);

        val tokenContent = new String(Base64.getUrlDecoder().decode(authorize.getAccessToken().getTokenValue().split("\\.")[1]));
        val gson = new Gson();
        val content = gson.fromJson(tokenContent, TokenContent.class);

        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                content.getMember_of_group().stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
