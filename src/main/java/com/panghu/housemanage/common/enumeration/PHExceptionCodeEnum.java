package com.panghu.housemanage.common.enumeration;

import lombok.Getter;

@Getter
public enum PHExceptionCodeEnum {

    SERVICE_ERROR(500,"服务端异常，请联系管理员查看错误日志"),
    DATA_NOT_FOUND(10001,"已找不到该记录，请刷新页面后再尝试"),
    ROOM_INUSE(10002,"租客未退租，请确定以下租客退租后重新禁用"),
    MEMBER_RENTING(10002,"租客未退租，请确定以下租客退租后重新删除");

    private final int code;

    private final String msg;


    PHExceptionCodeEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

}
