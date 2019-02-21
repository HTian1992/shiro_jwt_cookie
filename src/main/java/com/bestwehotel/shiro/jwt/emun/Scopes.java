package com.bestwehotel.shiro.jwt.emun;

/**
 * Token Scope 用于区分是否为Refresh Token
 *
 * @author lizehao
 */
public enum Scopes {
    REFRESH_TOKEN;

    public String authority() {
        return this.name();
    }
}
