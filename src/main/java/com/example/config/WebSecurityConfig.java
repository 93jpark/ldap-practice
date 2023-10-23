package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    /*
    This example follow bind authentication.
    So, LDAP server doesn't provide user password rather it verifies the user password and give a response to the web application.
    This provides as an additional layer of security as passwords are not transferred backend between web application and LDAP server.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("CHECKPOINT");
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=ten1010,dc=io")
                .and()
                .passwordCompare()
                .passwordEncoder(new PlaintextPasswordEncoder())
                .passwordAttribute("userPassword");
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                    .anyRequest().fullyAuthenticated()
//                    .and()
//                .formLogin();
//
//        return http.build();
//    }
//
//    @Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
//                    .userDnPatterns("uid={0},ou=people")
//    //                .userSearchBase(userSearchBase)
//    //                .userSearchFilter(userSearchFilter)
//                    .groupSearchBase("ou=groups")
//                    .contextSource()
//                        .url("ldap://localhost:8389/dc=springframework,dc=org")
//    //                .port(Integer.parseInt(ldapPort))
//    //                .root(ldapRoot)
//                        .and()
//                    .passwordCompare()
//                        .passwordEncoder(new BCryptPasswordEncoder())
//                        .passwordAttribute("userPassword");
//    }


    /*
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

     */


}
