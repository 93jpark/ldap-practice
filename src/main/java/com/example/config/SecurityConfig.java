package com.example.config;

import com.example.auth.CustomLdapAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLdapAuthenticationProvider customLdapAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                    .and()
                .formLogin();
        http.authenticationProvider(customLdapAuthenticationProvider);
        return http.build();
    }

    @Bean
    public AuthenticationManager config(AuthenticationManagerBuilder auth) throws Exception {
        return auth.authenticationProvider(customLdapAuthenticationProvider).build();
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource context = new LdapContextSource();
        context.setUrl("ldap://localhost:389");
        context.setBase("dc=ten1010,dc=io");
        context.setUserDn("cn=admin,dc=ten1010,dc=io");
        context.setPassword("admin");
//        context.setPooled(true);
        return context;
    }

    @Bean
    public LdapTemplate template() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        uid=joonwoo.park@ten1010.io,ou=people,dc=ten1010,dc=io
        auth.authenticationProvider(customLdapAuthenticationProvider).build();
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .userSearchBase("ou=people")
                .userSearchFilter("uid={0}")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("uniqueMember={0}")
                .contextSource(contextSource())
                .passwordCompare()
                .passwordEncoder(new PlaintextPasswordEncoder())
                .passwordAttribute("userPassword");
    }


}
