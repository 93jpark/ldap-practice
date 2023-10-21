package com.example.repository;

import com.example.domain.Person;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import javax.naming.directory.Attributes;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class PersonRepoImpl {

    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List<String> getAllPersonNames() {
        return ldapTemplate.search(
            query().where("objectclass").is("person"),
            new AttributesMapper<String>() {
                public String mapFromAttributes(Attributes attrs)
                        throws NamingException {
                    return (String) attrs.get("cn").get();
                }
            });
    }

    private class PersonAttributesMapper implements AttributesMapper<Person> {
        public Person mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
            Person person = new Person();
            person.setFullName((String)attrs.get("cn").get());
            person.setLastName((String)attrs.get("sn").get());
            person.setDescription((String)attrs.get("description").get());
            return person;
        }
    }

}
