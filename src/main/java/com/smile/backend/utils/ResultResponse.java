package com.smile.backend.utils;

/**
 * 响应结果封装
 */

public class ResultResponse {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public ResultResponse() {
    }

    public static Result getSuccessResult() {
        return new Result(ResultEnum.SUCCESS.getCode(), DEFAULT_SUCCESS_MESSAGE, null);
    }

    public static Result getSuccessResult(Object data) {
        return new Result(ResultEnum.SUCCESS.getCode(), DEFAULT_SUCCESS_MESSAGE, data);

    }
    public static Result getSuccessResult(String msg, Object data) {
        return new Result(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static Result getFailResult(String message) {
        return new Result(ResultEnum.BAD_REQUEST.getCode(), message, null);
    }


    public static Result getFailResult(ResultEnum resultEnum) {
        return new Result(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static Result getFailResult(int code, String msg) {
        return new Result(code, msg, null);
    }
}


