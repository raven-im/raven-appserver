package com.tim.appserver.common;

import com.tim.appserver.utils.RestResultCode;
import java.sql.SQLException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理 处理控制器抛出的异常 Created by zhouxiaoxiao on 17/4/19.
 */
@ControllerAdvice
public class ExceptionController {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(BaseException.class)
    public @ResponseBody RestResult handleValidationException(BaseException baseException) {
        log.error(baseException.getMessage());
        return new RestResult().setRspCode(RestResultCode.COMMON_SERVER_ERROR.getCode())
            .setRspMsg(baseException.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public @ResponseBody RestResult handleSQLException(SQLException sqlException) {
        log.error(sqlException.getMessage());
        sqlException.printStackTrace();
        return RestResult.failure();
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public @ResponseBody RestResult handleAuthenticatedException(UnauthenticatedException exception) {
        log.error(exception.getMessage());
        return RestResult.generate(RestResultCode.USER_USER_NOT_LOGIN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody RestResult handleAuthorizedException(UnauthorizedException exception) {
        log.error(exception.getMessage());
        return RestResult.generate(RestResultCode.USER_USER_NOT_AUTHORIZED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody RestResult handleMethodNotSupportException(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage());
        return RestResult.generate(RestResultCode.COMMON_METHOD_NOT_SUPPORT);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody RestResult handleException(Exception exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
        return RestResult.failure();
    }
}
