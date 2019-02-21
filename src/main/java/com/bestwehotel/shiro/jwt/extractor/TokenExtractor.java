package com.bestwehotel.shiro.jwt.extractor;

import com.bestwehotel.shiro.config.JwtProperties;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Token提取器
 *
 * @author lizehao
 */
@Component
public class TokenExtractor {

    public String extract(String header) {
        if (StringUtils.isEmpty(header)) {
            throw new AuthenticationException("认证头信息不能为空");
        }

        if (header.length() < JwtProperties.JWT_TOKEN_HEADER_PREFIX.length()) {
            throw new AuthenticationException("头信息大小不合法");
        }

        return header.substring(JwtProperties.JWT_TOKEN_HEADER_PREFIX.length(), header.length());
    }
}
