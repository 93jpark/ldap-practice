package io.ten1010.ldappractice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String homeController() {
        return "Authenticated!";
    }

    @GetMapping("/auth")
    public String authValidate() {
        return "Authorization";
    }
}
