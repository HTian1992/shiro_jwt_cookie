package com.bestwehotel.shiro;

import com.bestwehotel.shiro.config.JwtProperties;
import com.bestwehotel.shiro.exceptions.InvalidJwtTokenException;
import com.bestwehotel.shiro.jwt.model.RawAccessJwtToken;
import com.bestwehotel.shiro.jwt.model.UserContext;
import com.bestwehotel.shiro.jwt.verifier.TokenVerifier;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class AuthServerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServerUtils.class);

    private final JwtProperties jwtProperties;

    private TokenVerifier tokenVerifier;

    @Autowired
    public AuthServerUtils(final JwtProperties jwtProperties,TokenVerifier tokenVerifier){
        this.jwtProperties = jwtProperties;
        this.tokenVerifier = tokenVerifier;
    }

    //parseRawToken
    public UserContext parseRawToken(String authToken) {
        RawAccessJwtToken token = new RawAccessJwtToken(authToken);
        Claims body = token.parseClaims(jwtProperties.getTokenSigningKey()).getBody();
        String userId = body.getSubject();
        String jti = body.getId();
        if(!tokenVerifier.jtiVerify(userId,jti)){
            throw new InvalidJwtTokenException("用户已注销登录,token失效");
        }
        Date iat = body.get("iat",Date.class);
        //校验用户状态/密码/权限等是否被修改了，被修改则校验失败->返回fasle
        if(!tokenVerifier.verify(userId,iat.getTime())){
            throw new InvalidJwtTokenException("用户信息已变更,token失效");
        }
        String username = body.get("username", String.class);
        String usertype = body.get("usertype", String.class);
        String companyId = body.get("companyId", String.class);
        return UserContext.create(jti, companyId, userId, usertype, username, body.get("scopes", List.class));
    }

}
