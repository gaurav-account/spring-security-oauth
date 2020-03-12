package com.baeldung.newstack;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;


public class CustomClaimsValidator implements OAuth2TokenValidator<Jwt> {
	
	 private static final String DOMAIN = "@baeldung.com";
	 private static final String PREFERRED_USERNAME = "preferred_username";
	 private static final OAuth2Error error = new OAuth2Error("invalid_token", "Invalid Email Domain", null);

	    public OAuth2TokenValidatorResult validate(Jwt jwt) {
	        String preferredUsername = (String) jwt.getClaims().get(PREFERRED_USERNAME);
	        if (preferredUsername.endsWith(DOMAIN)) {
	            return OAuth2TokenValidatorResult.success();
	        }else{
	            return OAuth2TokenValidatorResult.failure(error);
	        }
	    }
}
