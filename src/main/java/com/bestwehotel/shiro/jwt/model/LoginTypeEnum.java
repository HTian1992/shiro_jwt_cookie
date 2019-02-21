package com.bestwehotel.shiro.jwt.model;

/**
 * @author lzh
 * @since 2018-12-06
 */
public enum LoginTypeEnum {

    //1-店长，2-店员(默认)，3-集团用户，4-投资人，7-高校代理，8-企业拓展代理，9-铂物馆供应商，10-旅游分销商
    LOGIN_DISTRIBUTION("1", "旅游分销商"),
    LOGIN_OPERATE("2", "平台运营");

    private String code;
    private String name;

    LoginTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
