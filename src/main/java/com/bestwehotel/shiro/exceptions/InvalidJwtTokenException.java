package com.bestwehotel.shiro.exceptions;

/**
 * 无效Token错误类
 *
 * @author lizehao
 */
public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException(String msg) {
        super(msg);
    }
}
