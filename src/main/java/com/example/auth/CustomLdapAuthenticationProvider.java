package com.example.auth;

import com.example.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.AuthenticationNotSupportedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLdapAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        boolean isAuthenticated = userService.authenticates(email, password);
        if (!isAuthenticated) {
            throw new AuthenticationException("인증할 수 없는 사용자") {
            };
        } else {
            LdapUser user = (LdapUser) userService.loadUserByEmail(email);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
