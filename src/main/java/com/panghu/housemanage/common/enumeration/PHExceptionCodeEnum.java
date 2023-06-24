package com.panghu.housemanage.common.enumeration;

import lombok.Getter;

@Getter
public enum PHExceptionCodeEnum {

    SERVICE_ERROR(500,"服务端异常，请联系管理员查看错误日志"),
    DATA_NOT_FOUND(10001,"已找不到该记录，请刷新页面后再尝试");

    private final int code;

    private final String msg;


    PHExceptionCodeEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }
}
