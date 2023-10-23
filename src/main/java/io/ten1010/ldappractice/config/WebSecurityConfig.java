package io.ten1010.ldappractice.config;

import io.ten1010.ldappractice.auth.PlaintextPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {


    @Value("${spring.ldap.urls}")
    private String ldapURLs;

    @Value("${spring.ldap.port}")
    private String ldapPort;

//    @Value("${spring.ldap.base")
//    private String ldapBase;

//    @Value("${spring.ldap.username}")
//    private String username;

//    @Value("${spring.ldap.password")
//    private String password;

    @Value("${spring.ldap.userDnPattern}")
    private String userDnPattern;

//    @Value("${spring.ldap.userSearchBase")
//    private String userSearchBase;

//    @Value("${spring.ldap.userSearchFilter")
//    private String userSearchFilter;

    @Value("${spring.ldap.groupSearchBase}")
    private String groupSearchBase;

//    @Value("${spring.ldap.groupSearchFilter")
//    private String groupSearchFilter;

    @Value("${spring.ldap.managerDn}")
    private String managerDn;

    @Value("${spring.ldap.managerPassword}")
    private String managerPassword;

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
        auth
                .ldapAuthentication()
                .userDnPatterns(userDnPattern)
//                .userSearchBase(userSearchBase)
//                .userSearchFilter(userSearchFilter)
                .groupSearchBase(groupSearchBase)
//                .groupSearchFilter(groupSearchFilter)
                .contextSource()
//                .url("ldap://localhost:389/dc=ten1010,dc=io")
                .url(ldapURLs)
//                .port(Integer.parseInt(ldapPort))
//                .root("dc=ten1010,dc=io") // root only for embedded server
                .managerDn(managerDn)
                .managerPassword(managerPassword)
//                .root(ldapBase)
                .and()
                .passwordCompare()
                .passwordEncoder(new PlaintextPasswordEncoder())
                .passwordAttribute("userPassword");
    }
}
