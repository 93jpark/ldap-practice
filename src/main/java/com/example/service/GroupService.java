package com.example.service;

import com.example.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final LdapTemplate template;

    public List<UserInfo> findDetailByGroup(String group) {
        if (!existsByGroup(group)) {
            throw new UsernameNotFoundException("group not found");
        }

        Name name = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
        Filter filter = new EqualsFilter("objectClass", "person");
        try {
            List<UserInfo> userInfoList =
                    template.search(
                            name,
                            filter.encode(),
                            new AbstractContextMapper<UserInfo>() {
                                @Override
                                protected UserInfo doMapFromContext(DirContextOperations ctx) {
                                    String email = ctx.getStringAttribute("uid");
                                    String name = ctx.getStringAttribute("cn");
                                    String group = LdapUtils.getStringValue(ctx.getDn(), "ou").toUpperCase();
                                    return UserInfo.builder().email(email).name(name).group(group).build();
                                }
                            });
            return userInfoList;
        } catch (NameNotFoundException e) {
            return new ArrayList<>();
        }
    }


    public boolean existsByGroup(String group) {
        Name name = LdapNameBuilder.newInstance().add("ou", group.toLowerCase()).build();
        try {
            template.lookup(
                    name,
                    new AbstractContextMapper<String>() {
                        @Override
                        protected String doMapFromContext(DirContextOperations ctx) {
                            return ctx.getStringAttribute("ou");
                        }
                    });
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public List<String> findAll() {
        Filter filter = new EqualsFilter("objectClass", "organizationalUnit");
        try {
            List<String> groupList =
                    template.search(
                            LdapUtils.emptyLdapName(),
                            filter.encode(),
                            new AbstractContextMapper<String>() {
                                @Override
                                protected String doMapFromContext(DirContextOperations ctx) {
                                    return ctx.getStringAttribute("ou").toUpperCase();
                                }
                            });
            return groupList;
        } catch (NameNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
