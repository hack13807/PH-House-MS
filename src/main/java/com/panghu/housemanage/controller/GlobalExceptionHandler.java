package com.panghu.housemanage.controller;

import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.PHResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * 全局异常处理器
 *
 * @author PangHu
 * @date 2023/06/24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(PHServiceException.class)
    public <T> PHResp<T> handleServiceException(PHServiceException e, HttpServletRequest request) {
        logger.warn("业务异常: {} {}, 错误码: {}, 错误信息: {}", 
            request.getMethod(), request.getRequestURI(), e.getCode(), e.getMsg());
        return PHResp.error(e.getCode(), e.getMsg(), (T) e.getDetail());
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public PHResp<String> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.error("请求的资源不存在: {} {}", request.getMethod(), request.getRequestURI());
        return PHResp.error(404, "请求的资源不存在");
    }

    /**
     * 处理连接超时异常
     */
    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class, ResourceAccessException.class})
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public PHResp<String> handleTimeoutException(Exception ex, HttpServletRequest request) {
        logger.error("请求超时: {} {}, 原因: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return PHResp.error(504, "请求超时，请稍后重试");
    }

    /**
     * 处理REST客户端异常
     */
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public PHResp<String> handleRestClientException(RestClientException ex, HttpServletRequest request) {
        logger.error("外部API调用失败: {} {}, 原因: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return PHResp.error(502, "外部服务暂时不可用");
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public PHResp<String> handleException(Exception ex, HttpServletRequest request) {
        logger.error("系统异常: {} {}", request.getMethod(), request.getRequestURI(), ex);
        logger.error("异常堆栈信息: {}", Arrays.toString(ex.getStackTrace()));
        return PHResp.error(PHExceptionCodeEnum.SERVICE_ERROR.getCode(), 
                          PHExceptionCodeEnum.SERVICE_ERROR.getMsg());
    }
}
