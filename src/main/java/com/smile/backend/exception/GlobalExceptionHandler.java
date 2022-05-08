package com.smile.backend.exception;

import com.smile.backend.utils.Result;
import com.smile.backend.utils.ResultEnum;
import com.smile.backend.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param req HttpServletRequest
     * @param e   BizException
     * @return Result
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest req, BizException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultResponse.getFailResult(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param req HttpServletRequest
     * @param e   BizException
     * @return Result
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e) {
        logger.error("发生空指针异常！原因是: ", e);
        return ResultResponse.getFailResult(ResultEnum.BAD_REQUEST);
    }

    /**
     * 处理其他异常
     *
     * @param req HttpServletRequest
     * @param e   BizException
     * @return Result
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("服务器异常！原因是: ", e);
        return ResultResponse.getFailResult(ResultEnum.SERVER_ERROR);
    }
}
