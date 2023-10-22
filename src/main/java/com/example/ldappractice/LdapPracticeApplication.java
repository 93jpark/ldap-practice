package com.example.ldappractice;

import com.example.config.PlaintextPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Properties;

@SpringBootApplication//(exclude= {UserDetailsServiceAutoConfiguration.class})
public class LdapPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LdapPracticeApplication.class, args);

//        testConnection();

    }

    private static void testConnection() {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
//        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=ten1010,dc=io");
//        env.put(Context.SECURITY_CREDENTIALS, "admin");
        env.put(Context.SECURITY_PRINCIPAL, " uid=joonwoo.park@ten1010.io,ou=people,dc=ten1010,dc=io");
        env.put(Context.SECURITY_CREDENTIALS, "joonwoo");
        try {
            DirContext connection = new InitialDirContext(env);
            System.out.println("Hello, " + connection);
            System.out.println("Login success!");
            connection.close();
        } catch (AuthenticationException e) {
            System.out.println("Auth Exception");
            System.out.println(e.getMessage());
        } catch (NamingException e) {
            System.out.println("Naming Exception");
            e.printStackTrace();
        }
    }

}
