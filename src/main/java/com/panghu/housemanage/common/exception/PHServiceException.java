package com.panghu.housemanage.common.exception;

import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import lombok.Getter;

/**
 * 业务自定义异常
 *
 * @author PangHu
 * @date 2023/06/24
 */
@Getter
public class PHServiceException extends RuntimeException {
    private final int code;
    private final String msg;
    private final String detail;

    public PHServiceException(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
        this.detail = null;
    }
    public PHServiceException(int code, String msg, String detail) {
        super();
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }
    public PHServiceException(PHExceptionCodeEnum exceCodeEnum, String detail) {
        super();
        this.code = exceCodeEnum.getCode();
        this.msg = exceCodeEnum.getMsg();
        this.detail = detail;
    }
}
