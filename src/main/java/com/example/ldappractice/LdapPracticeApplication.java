package com.example.ldappractice;

import com.example.config.PlaintextPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class LdapPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LdapPracticeApplication.class, args);
//        testConnection();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("inspect beans provided by spring boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }

    private static void testConnection() {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:8389");
//        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=ten1010,dc=io");
//        env.put(Context.SECURITY_CREDENTIALS, "admin");
        env.put(Context.SECURITY_PRINCIPAL, " uid=bob,ou=people,dc=springframework,dc=org");
        env.put(Context.SECURITY_CREDENTIALS, "bobspassword");
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
