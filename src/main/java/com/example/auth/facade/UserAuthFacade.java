package com.example.auth.facade;

import org.springframework.security.core.Authentication;

public interface UserAuthFacade {
    Authentication getAuthentication();
}
