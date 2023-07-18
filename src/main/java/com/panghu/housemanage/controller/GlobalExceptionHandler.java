package com.panghu.housemanage.controller;

import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.PHResp;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author PangHu
 * @date 2023/06/24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(PHServiceException.class)
    public <T> PHResp<T> serviceExceptionHandler(PHServiceException e) {
        return PHResp.error(e.getCode(), e.getMsg(), (T) e.getDetail());
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> PHResp<T> globalException(Exception e) {
        return PHResp.error(PHExceptionCodeEnum.SERVICE_ERROR.getCode(), PHExceptionCodeEnum.SERVICE_ERROR.getMsg(), (T) e);
    }

}
