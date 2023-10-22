package com.example.controller;

import com.example.auth.facade.UserAuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LDAPRestController {

    @Autowired
    private UserAuthFacade userAuthFacade;

    @GetMapping("/securegreetings")
    public String secureGreetings() {
        String msg = null;
        String role = null;
        Authentication authentication = userAuthFacade.getAuthentication();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String grantedAuthority = grantedAuthorities.get(0).getAuthority();
        if ("ROLE_USER".equals(grantedAuthority)) {
            role = "User";
        } else if ("ROLE_MANAGER".equals(grantedAuthority)) {
            role = "Manager";
        } else {
            role = "undefined";
        }
        msg = "Welcome and Thanks! " + authentication.getName() + " (" + role + ")";
        return msg;
    }
}
