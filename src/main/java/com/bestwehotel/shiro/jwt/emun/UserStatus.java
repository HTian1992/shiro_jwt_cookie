package com.bestwehotel.shiro.jwt.emun;

/**
 *
 * @author lizehao
 */
public enum UserStatus {
    //可用的
    AVAILABLE(0),
    //更新密码
    UPDATE_PASSWORD(1),
    //更新权限
    UPDATE_PERMISSION(2),
    //启用用户
    ENABLE_ACCOUNT(3),
    //禁用用户
    DISABLE_ACCOUNT(4);

    private int userStatus;

    UserStatus(int userStatus){
        this.userStatus = userStatus;
    }

    public int flag(){
        return userStatus;
    }
}
