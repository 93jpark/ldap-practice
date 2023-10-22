package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.ldap.url}")
    private String ldapURL;

    @Value("${spring.ldap.port}")
    private String ldapPort;

    @Value("${spring.ldap.userDnPattern}")
    private String userDnPattern;

    @Value("${spring.ldap.groupSearchBase}")
    private String groupSearchBase;

    @Value("${spring.ldap.groupSearchFilter}")
    private String groupSearchFilter;

    @Value("${spring.ldap.managerDn}")
    private String managerDn;

    @Value("${spring.ldap.managerPassword}")
    private String managerPassword;



    /*
    This example follow bind authentication.
    So, LDAP server doesn't provide user password rather it verifies the user password and give a response to the web application.
    This provides as an additional layer of security as passwords are not transferred backend between web application and LDAP server.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/", "/home", "/unsecuregreetings")
//                .permitAll().anyRequest().fullyAuthenticated())
//                .formLogin((form) -> form.loginPage("/login").permitAll()).logout((logout) -> logout.permitAll());

        http.authorizeHttpRequests((requests)->requests.requestMatchers("/", "/home", "/unsecuregreetings").permitAll().anyRequest()
                .fullyAuthenticated()).formLogin();

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userDnPatterns(userDnPattern)
                .groupSearchBase(groupSearchBase)
                .groupSearchFilter(groupSearchFilter)
                .contextSource()
                .url(ldapURL)
                .port(Integer.parseInt(ldapPort))
                .managerDn(managerDn)
                .managerPassword(managerPassword);
    }


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
