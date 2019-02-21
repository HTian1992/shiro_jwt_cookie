package com.bestwehotel.shiro;

import com.bestwehotel.shiro.jwt.model.UserContext;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ShiroRealm
 * @author lizehao
 * */
public class ShiroRealm extends AuthorizingRealm {

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * 该方法的调用时机为需授权资源被访问时
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        UserContext userContext = (UserContext) principalCollection.getPrimaryPrincipal();
        //String userId = body.getSubject();
        //添加用户凭证，即token的权限部分
        List<String> scopes = userContext.getAuthorities();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户的角色信息
        info.setRoles(getRolesName());
        //用户权限信息
        info.addStringPermissions(scopes);
        return info;
    }

    /**
     * 登录认证(token验证)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }

    /***
     * 查询当前用户所具有的角色
     */
    private Set<String> getRolesName() {
        Set<String> setList = new HashSet<>();
        return setList;
    }

    /**
     * 查询当前用户所具有的权限
     **/
    private List<String> getPermissionsNames() {
        List<String> list = new ArrayList<>();
        return list;
    }

}
