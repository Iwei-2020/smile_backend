package com.smile.backend.utils;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(200, "200 Success"), //成功
    BAD_REQUEST(400, "400 Bad Request"), //失败
    UNAUTHORIZED(401, "401 Unauthorized"), //未认证（签名错误）
    NOT_FOUND(404, "404 Not Found"), //接口不存在
    SERVER_ERROR(500, "500 Server Error"); //服务器内部错误

    private final int code;

    private final String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
