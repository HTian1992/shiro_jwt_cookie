package com.bestwehotel.shiro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt相关配置
 *
 */
@Configuration
@ConfigurationProperties(prefix = "app.shiro.jwt")
public class JwtProperties {

    //维护用户状态，里面保存的是用户信息/权限/状态被变更的最新时间。用于与token签发时间进行比较判断token有效性
    public final static String USER_STATUS_CACHE_KEY = "user:status:";

    //token黑名单
    public final static String LOGIN_LIMIT = "login:blacklist:";

    public final static String AUTH_HEADER = "Authorization";

    public final static String JWT_TOKEN_HEADER_PREFIX = "Bearer ";

    //存放在cookie中的token值的 name
    public final static String TOKEN_COOKIE_NAME = "auth_token";

    /**
     * Token过期时间 单位为分钟
     */
    private Integer tokenExpirationTime;

    /**
     * Token签发者
     */
    private String tokenIssuer;

    /**
     * 签名Key
     */
    private String tokenSigningKey;

    /**
     * Refresh Token过期时间 单位为分钟
     */
    private Integer refreshTokenExpTime;

    /**
     * 为了让开发同事方便，仅非开发环境需要强制设置cookie的domain
     * */
    private boolean needSetDomain;

    /**
     * 存放在cookie中的domain信息，httpOnly = true;
     * */
    private String tokenCookieDomain;

    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public String getTokenCookieDomain() {
        return tokenCookieDomain;
    }

    public void setTokenCookieDomain(String tokenCookieDomain) {
        this.tokenCookieDomain = tokenCookieDomain;
    }

    public boolean isNeedSetDomain() {
        return needSetDomain;
    }

    public void setNeedSetDomain(boolean needSetDomain) {
        this.needSetDomain = needSetDomain;
    }
}
