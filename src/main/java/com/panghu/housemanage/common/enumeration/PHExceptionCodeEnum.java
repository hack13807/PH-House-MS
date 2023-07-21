package com.panghu.housemanage.common.enumeration;

import lombok.Getter;

@Getter
public enum PHExceptionCodeEnum {

    SERVICE_ERROR(500,"服务端异常，请联系PangHu管理员查看错误日志"),
    DATA_NOT_FOUND(10001,"已找不到该记录，请刷新页面后再尝试"),
    ROOM_INUSE(10002,"租客未退租，请确定以下租客退租后重新禁用"),
    MEMBER_RENTING(10002,"租客未退租，请确定以下租客退租后重新删除"),
    OPT_TYPE_NOT_FOUNG(10003,"该操作未定义，请联系管理员查看后台日志"),
    EFFECTIVE_STATUS_NOT_FOUNG(10004,"生效状态未定义，请联系管理员查看后台日志"),
    UNIQUE_MEMBER(10005,"系统已存在该身份证号的租客"),
    UNIQUE_LEASE(10006,"系统已存在相同房间相同租客的租约"),
    UNIQUE_ROOM(10007,"系统已存在该房号，如被禁用，请启用");

    private final int code;

    private final String msg;


    PHExceptionCodeEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

}
