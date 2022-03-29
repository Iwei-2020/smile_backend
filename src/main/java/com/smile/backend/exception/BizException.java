package com.smile.backend.exception;

import com.smile.backend.utils.ResultEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected int errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.errorCode = resultEnum.getCode();
        this.errorMsg = resultEnum.getMsg();
    }

    public BizException(ResultEnum resultEnum, Throwable cause) {
        super(resultEnum.getMsg(), cause);
        this.errorCode = resultEnum.getCode();
        this.errorMsg = resultEnum.getMsg();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BizException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(int errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
