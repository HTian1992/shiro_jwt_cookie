package com.bestwehotel.shiro.exceptions;

import com.bestwehotel.shiro.jwt.model.JwtToken;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Token过期错误类
 *
 * @author lizehao
 */
public class JwtExpiredTokenException extends AuthenticationException {

    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
