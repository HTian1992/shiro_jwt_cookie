package com.bestwehotel.shiro.jwt.verifier;

import com.bestwehotel.shiro.config.JwtProperties;
import com.bestwehotel.shiro.jwt.emun.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Token验证器
 *
 * @author lizehao
 */
public class TokenVerifier {

    /***
     * 判断用户是否存在修改权限，状态等操作，存在则 fasle: 校验失败->需要用户重新登录
     * @param userId 用户userId
     * @param issuedTime token签发时间
     * @return true-校验通过, fasle-校验失败
     * */
    public boolean verify(String userId,long issuedTime){
        return true;
    }

    /**
     * 校验jti，如果用户点击注销登录，会把jti加入黑名单
     * */
    public boolean jtiVerify(String userId,String jti){
        return true;
    }

}
