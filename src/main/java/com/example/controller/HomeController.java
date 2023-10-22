package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page!";
    }

    @PostMapping("/unsecuregreetings")
    public String unsecureGreetings(@RequestParam(name="email", required = true) String email, Model model) {
        model.addAttribute("email", email);
        return "unsecuregreetings";
    }

}
