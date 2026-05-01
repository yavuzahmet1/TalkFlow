package com.yavuzahmet.talkflow.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    @Nullable
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        // Fetching default Spring Security authorities (e.g., SCOPE_)
        Collection<GrantedAuthority> defaultAuthorities = new JwtGrantedAuthoritiesConverter().convert(source);

        // Fetch Keycloak roles
        Collection<? extends GrantedAuthority> keycloakAuthorities = extractResourceRoles(source);

        // Safely merging the lists.
        var authorities = Stream.concat(
                defaultAuthorities != null ? defaultAuthorities.stream() : Stream.empty(),
                keycloakAuthorities.stream()).collect(Collectors.toSet());

        return new JwtAuthenticationToken(source, authorities);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        // SECURITY WALL 1: If resource_access is missing in the token, return an empty
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        Map<String, List<String>> account = (Map<String, List<String>>) resourceAccess.get("account");

        // SAFETY CHECK 2: Return empty set if 'account' or 'roles' is null
        if (account == null || account.get("roles") == null) {
            return Collections.emptySet();
        }

        List<String> roles = account.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(Collectors.toSet());
    }
}