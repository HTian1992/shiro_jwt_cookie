package com.bestwehotel.shiro.jwt.model;

import com.bestwehotel.shiro.exceptions.InvalidJwtTokenException;
import com.bestwehotel.shiro.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 原始Access Jwt Token对象
 *
 * @author lizehao
 */
public class RawAccessJwtToken implements JwtToken {
    private static Logger log = LoggerFactory.getLogger(RawAccessJwtToken.class);

    private String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * 解析Token
     *
     * @throws InvalidJwtTokenException
     * @throws JwtExpiredTokenException
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error("无效的Refresh Token", ex);
            throw new InvalidJwtTokenException("无效的Refresh Token");
        } catch (ExpiredJwtException expiredEx) {
            log.error("Refresh Token已过期", expiredEx);
            throw new JwtExpiredTokenException("Refresh Token已过期");
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
