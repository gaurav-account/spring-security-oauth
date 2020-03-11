package com.baeldung.resource;

import java.util.Map;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

public class CustomClaimVerifier implements JwtClaimsSetVerifier {
    @Override
    public void verify(Map<String, Object> claims) throws InvalidTokenException {
        final String username = (String) claims.get("user_name");
       
        if ((username == null) || (username.length() == 0) || (!username.endsWith("@baeldung.com"))) {
            throw new InvalidTokenException("Invalid User Name!");
        }
    }
}
