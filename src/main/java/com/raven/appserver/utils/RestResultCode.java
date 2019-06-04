package com.raven.appserver.utils;

public enum RestResultCode {
    /* common */
    COMMON_SUCCESS(10000, "Success!"),
    COMMON_SERVER_ERROR(10001, "Server Failed!"),
    COMMON_NOT_IMPLEMENT(10002, "not implement"),
    COMMON_INVALID_PARAMETER(10003, "invalid parameter."),
    COMMON_NOT_FOUND(10004, "not found"),
    COMMON_METHOD_NOT_SUPPORT(10005, "http method not support."),

    /* user */
    USER_INVALID_PASSWORD(10101, "Invalid Password"),
    USER_INVALID_USERNAME(10102, "Invalid Username"),
    USER_USER_NOT_FOUND(10103, "User not found."),
    USER_NAME_PWD_NOT_MATCH(10104, "Username and Password not match."),
    USER_USER_DISABLED(10105, "User disabled."),
    USER_USER_NOT_LOGIN(10106, "User not login."),
    USER_USER_NAME_EXIST(10107, "User name exists already."),
    USER_USER_NOT_AUTHORIZED(10108, "User not authorized."),
    USER_USER_NOT_PERMITTED(10109, "User not permitted."),

    /* upload */
    UPLOAD_FILE_EMPTY(10201, "File empty."),
    UPLOAD_FILE_UPLOAD_ERROR(10202, "File upload error."),
    ;

    private int code;
    private String msg;

    RestResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(int code) {
        for (RestResultCode r : RestResultCode.values()) {
            if (r.getCode() == code) {
                return r.getMsg();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean success() {
        return getCode() == COMMON_SUCCESS.getCode();
    }
}
