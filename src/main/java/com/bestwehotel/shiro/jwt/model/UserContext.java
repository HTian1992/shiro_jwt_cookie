package com.bestwehotel.shiro.jwt.model;

import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户上下文对象
 *
 * @author lizehao
 */
public class UserContext {

    //用户所属组织(公司)id
    private final String companyId;
    //用户类型 1-分销商 2-运营
    private final String usertype;
    //用户id
    private final String userId;
    //用户名称
    private final String username;
    //设置jti refresh_token
    private final String jti;
    //用户shiro权限集合
    private final List<String> authorities;

    private UserContext(String jti,String companyId,String userId,String usertype,String username, List<String> authorities) {
        this.userId = userId;
        this.usertype = usertype;
        this.username = username;
        this.authorities = authorities;
        this.jti = jti;
        this.companyId = companyId;
    }

    public static UserContext create(String jti,String companyId,String userId,String usertype,String username, List<String> authorities) {
        if (StringUtils.isEmpty(userId)||StringUtils.isEmpty(username)) throw new IllegalArgumentException("用户信息为空");
        return new UserContext(jti,companyId,userId,usertype,username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public String getUserId() {
        return userId;
    }

    public String getJti() {
        return jti;
    }

    public String getCompanyId() {
        return companyId;
    }

    /**
     * 获得当前登录用户上下文
     * */
    public static UserContext getCurrentUser(){
        return (UserContext) SecurityUtils.getSubject().getPrincipal();
    }

    public String getUsertype() {
        return usertype;
    }

    /**
     * 判断账号类型，为运营账号返回true
     * */
    public static boolean isOperateUser(){
        String usertype = getCurrentUser().getUsertype();
        if(LoginTypeEnum.LOGIN_OPERATE.code().equals(usertype)){
            return true;
        }
        return false;
    }

    /**
     * 判断账号类型，为分销商账号返回true
     * */
    public static boolean isDistributionUser(){
        String usertype = getCurrentUser().getUsertype();
        if(LoginTypeEnum.LOGIN_DISTRIBUTION.code().equals(usertype)){
            return true;
        }
        return false;
    }
}
