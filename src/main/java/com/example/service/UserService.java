package com.example.service;

import com.example.auth.LdapUser;
import com.example.dto.LoginForm;
import com.example.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.naming.Name;
import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final String ROLE_PREFIX = "ROLE_";
    private final GroupService groupService;
    private final LdapTemplate template;

    public boolean authenticates(String email, String rawPassword) {
        LdapQuery query = LdapQueryBuilder.query().where("uid").is(email);

        List<LoginForm> user =
                template.search(
                        query,
                        new AbstractContextMapper<LoginForm>() {
                            @Override
                            protected LoginForm doMapFromContext(DirContextOperations ctx) {
                                String email = ctx.getStringAttribute("uid");
                                byte[] bytes = (byte[]) ctx.getObjectAttribute("userPassword");
                                String password = new String(bytes);
                                return LoginForm.builder().email(email).password(password).build();
                            }
                        });

        if (user.size() != 1) {
            throw new UsernameNotFoundException("user not found");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, user.get(0).getPassword());
    }

    public UserInfo findByEmail(String email) {
        LdapQuery query = LdapQueryBuilder.query().where("uid").is(email);

        List<UserInfo> user =
                template.search(
                        query,
                        new AbstractContextMapper<UserInfo>() {
                            @Override
                            protected UserInfo doMapFromContext(DirContextOperations ctx) {
                                String email = ctx.getStringAttribute("uid");
                                String name = ctx.getStringAttribute("cn");
                                // need to figure out below sentence.
                                String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                                return UserInfo.builder().email(email).name(name).group(group).build();
                            }
                        });
        if (user.size() != 0) {
            throw new UsernameNotFoundException("user not found");
        }
        return user.get(0);
    }

    public Optional<UserInfo> findByGroupAndEmail(String group, String email) {
        Name ldapName = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).add("uid", email).build();
        UserInfo user =
                template.lookup(
                        ldapName,
                        new AbstractContextMapper<UserInfo>() {
                            @Override
                            protected UserInfo doMapFromContext(DirContextOperations ctx) {
                                String email = ctx.getStringAttribute("uid");
                                String name = ctx.getStringAttribute("cn");
                                String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                                return UserInfo.builder().email(email).name(name).group(group).build();
                            }
                        }
                );
        return Optional.of(user);
    }

    public List<UserInfo> findAll() {
        LdapQuery query = LdapQueryBuilder.query().where("objectClass").is("person");
        try {
            List<UserInfo> userList =
                    template.search(
                            query,
                            new AbstractContextMapper<UserInfo>() {
                                @Override
                                protected UserInfo doMapFromContext(DirContextOperations ctx) {
                                    String email = ctx.getStringAttribute("uid");
                                    String name = ctx.getStringAttribute("cn");
                                    String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                                    return UserInfo.builder().email(email).name(name).group(group).build();
                                }
                            });
            return userList;
        } catch (org.springframework.ldap.NameNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public UserDetails loadUserByEmail(String email) {
        LdapQuery query = LdapQueryBuilder.query().where("uid").is(email);
        List<LdapUser> user =
                template.search(
                        query,
                        new AbstractContextMapper<LdapUser>() {
                            @Override
                            protected LdapUser doMapFromContext(DirContextOperations ctx) {
                                String email = ctx.getStringAttribute("uid");
                                String name = ctx.getStringAttribute("cn");
                                List<GrantedAuthority> authorities = new ArrayList<>();
                                authorities.add(
                                        new SimpleGrantedAuthority(
                                                ROLE_PREFIX + LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase()));
                                return LdapUser.builder().email(email).name(name).authorities(authorities).build();
                            }
                        });
        if (user.size() != 1) {
            throw new UsernameNotFoundException(email);
        }
        return user.get(0);
    }
}
