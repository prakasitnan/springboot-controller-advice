package com.github.prakasitnan.springbootcontrolleradvice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login(){
        return "pages/login";
    }

    @GetMapping("/home")
    public String home(){
        return "pages/home";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(){
        return "pages/admin";
    }

    @GetMapping("/user/profile")
    public String profile() {
        return "pages/profile";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
