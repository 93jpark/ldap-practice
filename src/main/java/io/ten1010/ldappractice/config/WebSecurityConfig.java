package io.ten1010.ldappractice.config;

import io.ten1010.ldappractice.auth.PlaintextPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.ppolicy.PasswordPolicyAwareContextSource;
import org.springframework.security.ldap.server.UnboundIdContainer;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LdapAuthenticationProvider provider) throws Exception {
        http
                .authenticationProvider(provider)
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();
        return http.build();
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() throws Exception {
        LdapAuthenticationProvider provider = new LdapAuthenticationProvider(ldapAuthenticator());
        return provider;
    }

    @Bean
    public LdapContextSource ldapContextSource() throws Exception {
        PasswordPolicyAwareContextSource ctx = new PasswordPolicyAwareContextSource("ldap://localhost:389");
        ctx.setBase("dc=ten1010,dc=io");
        ctx.setUserDn("cn=admin,dc=ten1010,dc=io");
        ctx.setPassword("admin");
        return ctx;
    }

    @Bean
    public LdapAuthenticator ldapAuthenticator() throws Exception {
        BindAuthenticator authenticator = new BindAuthenticator(ldapContextSource());
        authenticator.setUserDnPatterns(new String[] {"uid={0},ou=people"});
        return authenticator;
    }

    ContextSource contextSource(UnboundIdContainer container) {
        return new DefaultSpringSecurityContextSource("ldap://localhost:389/dc=ten1010,dc=io");
    }

    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},ou=people");
        return factory.createAuthenticationManager();
    }

    /*
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
     */
}
