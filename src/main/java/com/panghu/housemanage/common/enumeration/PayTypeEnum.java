package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PayTypeEnum implements IEnum<Integer> {
    DEFAULT(0,"默认"),
    WECHAT(1,"微信支付"),
    CASH(2,"现金支付");

    @EnumValue
    private final int code;

    @JsonValue
    private final String msg;


    PayTypeEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

    /**
     * 通过code获取value
     *
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(PayTypeEnum typeEnum: PayTypeEnum.values()){
            if(typeEnum.getValue() == code){
                return typeEnum.getMsg();
            }
        }
        return "";
    }

    /**
     * 通过value获取实例
     * @param value
     * @return
     */
    public static PayTypeEnum getTypeEnumBytypeId(int value){
        for(PayTypeEnum topType : values()){
            if(topType.getValue() == value){
                return topType;
            }
        }
        return null;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
