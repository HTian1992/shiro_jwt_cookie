package com.bestwehotel.shiro.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

/**
 * Access Jwt Token对象
 *
 * @author lizehao
 */
public final class AccessJwtToken implements JwtToken {
    private final String rawToken;
    @JsonIgnore
    private Claims claims;

    protected AccessJwtToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }

    public String getJti() {
        return claims.getId();
    }
}
