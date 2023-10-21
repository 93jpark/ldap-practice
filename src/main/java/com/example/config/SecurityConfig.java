package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.AuthenticationSource;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.SecurityFilterChain;

import javax.naming.ldap.LdapContext;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                    .and()
                .formLogin();
        http.authenticationProvider(ldapAuthenticationProvider());
        return http.build();
    }

    @Bean
    LdapAuthenticationProvider ldapAuthenticationProvider() {
        LdapAuthenticationProvider provider = new LdapAuthenticationProvider(authenticator());

        return provider;
    }

    public LdapAuthenticationProvider
    @Bean
    BindAuthenticator authenticator() {
        FilterBasedLdapUserSearch search = new FilterBasedLdapUserSearch(
                "ou=people,dc=ten1010,dc=io",
                "(uid={0})", contextSource()
        );
        BindAuthenticator authenticator = new BindAuthenticator(contextSource());
        authenticator.setUserSearch(search);
        return authenticator;
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        DefaultSpringSecurityContextSource dsCtx = new DefaultSpringSecurityContextSource("ldap://localhost:389/dc=ten1010,dc=io");
        dsCtx.setUserDn("cn=admin,dc=ten1010,dc=io");
        dsCtx.setPassword("admin");
        return dsCtx;
    }

    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        uid=joonwoo.park@ten1010.io,ou=people,dc=ten1010,dc=io
        auth
                .ldapAuthentication()
//                .userDnPatterns("uid={0},ou=people")
//                .groupSearchBase("ou=people,dc=ten1010,dc=io")

                    .userDnPatterns("uid={0},ou=people")
//                    .groupSearchBase("ou=people")
                .contextSource()
                    .url("ldap://localhost:389/dc=ten1010,dc=io")
//                    .port(389)
//                    .managerDn("cn=admin,dc=ten1010,dc=io")
//                    .managerPassword("admin")
                .and()
                    .passwordCompare()
                    .passwordEncoder(new PlaintextPasswordEncoder())
                    .passwordAttribute("userPassword");

    }


}
