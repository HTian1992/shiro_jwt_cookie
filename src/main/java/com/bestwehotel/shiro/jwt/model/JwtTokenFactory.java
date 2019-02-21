package com.bestwehotel.shiro.jwt.model;


import com.bestwehotel.shiro.config.JwtProperties;
import com.bestwehotel.shiro.jwt.emun.Scopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Token生产工厂类
 *
 * @author lizehao
 */
@Component
public class JwtTokenFactory {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtTokenFactory(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 创建JWT Token
     * @return AccessJwtToken
     */
    public AccessJwtToken createAccessJwtToken(String jti,String companyId,String userId,String usertype,String username, List<String> authorities) {
        if (StringUtils.isEmpty(userId))
            throw new IllegalArgumentException("用户名为空，无法创建Token。");

        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("username",username);
        claims.put("usertype",usertype);
        claims.put("scopes", authorities);
        claims.put("companyId",companyId);
        LocalDateTime currentDateTime = LocalDateTime.now();

        //生成token
        String token = Jwts.builder()
                // 自定义属性
                .setClaims(claims)
                // 签发者
                .setIssuer(jwtProperties.getTokenIssuer())
                //设置jti,方便吊销refresh_token
                .setId(jti)
                // 签发时间
                .setIssuedAt(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                // 过期时间
                .setExpiration(Date.from(currentDateTime.plusMinutes(jwtProperties.getTokenExpirationTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .compressWith(CompressionCodecs.GZIP)
                // 签名算法以及密匙
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }

    /**
     * 创建Refresh Token
     *
     * @return JwtToken
     */
    public JwtToken createRefreshToken(String jti,String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("用户名为空，无法创建Refresh Token。");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userId);
        //设定Refresh Token标识
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtProperties.getTokenIssuer())
                //设置JTI 方便吊销Refresh Token
                .setId(jti)
                .setIssuedAt(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentDateTime.plusMinutes(jwtProperties.getRefreshTokenExpTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .compressWith(CompressionCodecs.GZIP)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }
}
