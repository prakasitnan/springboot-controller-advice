package com.github.prakasitnan.springbootcontrolleradvice.controller;

import com.github.prakasitnan.springbootcontrolleradvice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String view(Model model) {

        model.addAttribute("users", userService.findAllUsers());
        return "pages/user/view";
    }


    @GetMapping("/{userId}")
    public String update(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userId);
        return "pages/user/manage";
    }
}
