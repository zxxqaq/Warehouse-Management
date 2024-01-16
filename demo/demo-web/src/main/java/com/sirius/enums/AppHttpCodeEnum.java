package com.sirius.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),

    FILE_TYPE_ERROR (506,"文件类型错误"),

    REQUIRE_PASSWORD(507, "必需填写密码"),

    REQUIRE_NICKNAME(509, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误");



    int code;
    String msg;

    private AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}