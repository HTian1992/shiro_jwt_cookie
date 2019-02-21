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
@Component
public class TokenVerifierBack {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenVerifierBack.class);

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenVerifierBack(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /***
     * 判断用户是否存在修改权限，状态等操作，存在则 fasle: 校验失败->需要用户重新登录
     * @param userId 用户userId
     * @param issuedTime token签发时间
     * @return true-校验通过, fasle-校验失败
     * */
    public boolean verify(String userId,long issuedTime) {
        try {
            String userStatusFlag = redisTemplate.opsForValue().get(JwtProperties.USER_STATUS_CACHE_KEY + userId);
            if(null == userStatusFlag){
                //LOGGER.info("校验用户状态通过，userId-{}",userId);
                return true;
            }
            long updateTime = new Long(userStatusFlag);
            // token签发时间(用户登录时间) 与 用户信息被更新时间，比较：
            // 1-签发时间在用户信息被更新时间前，则校验失败，重新登录
            // 2-签发时间在用户信息被更新时间后，则校验通过，可访问资源
            long timeLong = (issuedTime - updateTime)/1000;
            if(timeLong > 0){
                LOGGER.info("校验用户状态通过，userId-{}，timeLong = {}",userId,timeLong);
                return true;
            }
            LOGGER.info("校验用户状态失败，userId-{}，timeLong = {}",userId,timeLong);
        }catch (Exception e){
            LOGGER.error("校验用户状态异常-{}",e);
        }
        return false;
    }

    /**
     * 校验jti，如果用户点击注销登录，会把jti加入黑名单
     * */
    public boolean jtiVerify(String userId,String jti){
        String jtiFlag =  redisTemplate.opsForValue().get(JwtProperties.LOGIN_LIMIT + jti);
        if(null == jtiFlag){
            //LOGGER.info("jti校验通过,userId = {}, jti = {}",userId,jti);
            return true;
        }
        LOGGER.error("jti校验失败,userId = {}, jti = {}",userId,jti);
        return false;
    }

    /**
     * 用户状态或者权限被修改,添加重新登录的redis缓存标志
     * */
    public void pushStatus2Cache(String userId, UserStatus updateType) {
        //初始化用户状态标识
        switch (updateType) {
            case AVAILABLE:
                //do nothings
                break;
            case UPDATE_PASSWORD:
                LOGGER.info("更新用户密码");
                redisTemplate.opsForValue().set(JwtProperties.USER_STATUS_CACHE_KEY+userId,String.valueOf(System.currentTimeMillis()));
                break;
            case ENABLE_ACCOUNT:
                //do nothings
                break;
            case DISABLE_ACCOUNT:
                LOGGER.info("禁用用户");
                redisTemplate.opsForValue().set(JwtProperties.USER_STATUS_CACHE_KEY+userId,String.valueOf(System.currentTimeMillis()));
                break;
            case UPDATE_PERMISSION:
                LOGGER.info("更新用户权限");
                redisTemplate.opsForValue().set(JwtProperties.USER_STATUS_CACHE_KEY+userId,String.valueOf(System.currentTimeMillis()));
                break;
            default:
                //do nothings
                break;
        }
    }

//    public boolean refreshTokenVerify(String jti) {
//        //暂不实现，如果是有效期很长的refresh token，则可以使用黑名单机制，手动吊销refresh token,然后在此处判断refresh token的jti是否在黑名单中。
//        return true;
//    }

}
