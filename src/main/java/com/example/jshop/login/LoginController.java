package com.example.jshop.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/j-shop/")
public class LoginController {


    @GetMapping(path = "log-in")
    public String login(HttpServletRequest request) {
        return "You are logged in";
    }
}

