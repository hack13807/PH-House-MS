package com.panghu.housemanage.common.exception;

import com.panghu.housemanage.common.enumeration.ExceptionEnum;
import lombok.Data;

/**
 * @Author jacklightChen
 * @Email 1797079433@qq.com
 * @createTime 2019/4/8
 * @proverb please find the joy in your life
 **/
@Data
public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }
}
