package com.baeldung.newstack.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import com.baeldung.newstack.CustomAuthoritiesExtractor;
import com.baeldung.newstack.CustomClaimsValidator;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String jwtIssuerUri;

    @Override
    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeRequests()
              .antMatchers(HttpMethod.GET, "/user/info", "/api/projects/**")
                .hasAuthority("SCOPE_read")
              .antMatchers(HttpMethod.POST, "/api/projects")
                //.hasAuthority("SCOPE_write")
               .hasAuthority("SUPERUSER")
              .anyRequest()
                .authenticated()
            .and()
              .oauth2ResourceServer()
                //.jwt();
              .jwt().jwtAuthenticationConverter(customGrantedAuthoritiesExtractor());
    }//@formatter:on
    
    @Bean
    JwtDecoder jwtDecoder() {
    	
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(jwtIssuerUri);
        OAuth2TokenValidator<Jwt> claimsValidator = new CustomClaimsValidator();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(jwtIssuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, claimsValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

    Converter<Jwt, AbstractAuthenticationToken> customGrantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesExtractor());
        return jwtAuthenticationConverter;
    }

}