package com.mycompany.myapp.security.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

//FIX for @Value - @Component
public class JwtGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    //CUSTOM START - fix for @Value failing to inject
    //    @Value("${spring.security.oauth2.client.registration.oidc.client-id}")
    private String clientId;

    public JwtGrantedAuthorityConverter(String clientId) {
        this.clientId = clientId;
    }
    //CUSTOM END

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return this.extractAuthorityFromClaims(jwt.getClaims());
    }

    public List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
        return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
    }

    @SuppressWarnings("unchecked")
    private Collection<String> getRolesFromClaims(Map<String, Object> claims) {
        Map<String, Object> resourceAccessClaim = (Map<String, Object>) claims.getOrDefault("resource_access", new HashMap<>());
        if (resourceAccessClaim.containsKey(this.clientId)) {
            return (Collection<String>) ((Map) resourceAccessClaim.get(this.clientId)).getOrDefault("roles", new ArrayList<>());
        } else {
            return new ArrayList<>();
        }
    }

    private List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
