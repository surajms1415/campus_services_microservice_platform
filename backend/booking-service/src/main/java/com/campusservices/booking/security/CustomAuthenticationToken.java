package com.campusservices.booking.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final Long userId;
    private final String token;

    public CustomAuthenticationToken(String email, Long userId, Collection<? extends GrantedAuthority> authorities, String token) {
        super(authorities);
        this.email = email;
        this.userId = userId;
        this.token = token;
        setAuthenticated(true);
    }
    @Override public Object getCredentials() { return token; }
    @Override public Object getPrincipal() { return email; }
    public Long getUserId() { return userId; }
    public String getToken() { return token; }
}
