package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.panghu.housemanage.common.exception.PHServiceException;
import lombok.Getter;

@Getter
public enum EffectiveEnum implements IEnum<Integer> {
    UN_EFFECTIVE(0,"已失效"),
    EFFECTIVE(1,"生效中");

    @EnumValue
    private final int code;

    @JsonValue
    private final String msg;

    EffectiveEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

    /**
     * 通过code获取value
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(EffectiveEnum typeEnum: EffectiveEnum.values()){
            if(typeEnum.getCode() == code){
                return typeEnum.getMsg();
            }
        }
        return "";
    }

    /**
     * 通过value获取实例
     * @param code
     * @return
     */
    public static EffectiveEnum getTypeEnumBytypeId(int code){
        for(EffectiveEnum topType : values()){
            if(topType.code == code){
                return topType;
            }
        }
        throw new PHServiceException(PHExceptionCodeEnum.EFFECTIVE_STATUS_NOT_FOUNG, String.valueOf(code));
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
