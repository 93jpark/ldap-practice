package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @PostMapping("/unsecuregreetings")
    public String unsecureGreetings(@RequestParam(name="email", required = true) String email, Model model) {
        model.addAttribute("email", email);
        return "unsecuregreetings";
    }
}
