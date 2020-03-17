package com.baeldung.newstack;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import java.util.Collection;


public class CustomAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
	private String DOMAIN = "@baeldung.com";
	private static final String PREFERRED_USERNAME = "preferred_username";

    public CustomAuthoritiesExtractor() {
    }

    public CustomAuthoritiesExtractor(String superUserDomain) {
        super();
        this.DOMAIN = superUserDomain;
    }

    public Collection<GrantedAuthority> convert(Jwt jwt) {
        JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);
        String preferredUsername = (String) jwt.getClaims().get(PREFERRED_USERNAME);

        if (preferredUsername.endsWith(DOMAIN)) {
            authorities.add(new SimpleGrantedAuthority("SUPERUSER"));
        }
        return authorities;
    }
}
