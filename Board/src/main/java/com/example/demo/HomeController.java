package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";  // home.html 파일을 반환
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";  // join.html 파일을 반환
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // login.html 파일을 반환
    }
}
