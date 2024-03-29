package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ReceiptTypeEnum implements IEnum<Integer> {
    DEFAULT(0,"默认"),
    RENT(1,"房租"),
    UTILITY(2,"水电"),
    DEPOSIT(3,"押金"),
    MAINTAIN(4,"维修更换"),
    OTHER(5,"其它");

    @EnumValue
    private final int code;

    @JsonValue
    private final String msg;


    ReceiptTypeEnum(int code , String msg){
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

        for(ReceiptTypeEnum typeEnum: ReceiptTypeEnum.values()){
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
    public static ReceiptTypeEnum getTypeEnumBytypeId(int value){
        for(ReceiptTypeEnum topType : values()){
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
