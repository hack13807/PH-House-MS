package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum LeaseTypeEnum implements IEnum<Integer> {
    DEFAULT(0,"默认", ""),
    DAILY(1,"按日租", "天"),
    MONTHLY(2,"按月租", "月"),
    YEARLY(3,"按年租", "年");

    @EnumValue
    private final int code;

    @JsonValue
    private final String msg;

    private final String unit;

    LeaseTypeEnum(int code , String msg, String unit){
        this.code = code ;
        this.msg = msg ;
        this.unit = unit;
    }

    /**
     * 通过code获取value
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(LeaseTypeEnum typeEnum: LeaseTypeEnum.values()){
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
    public static LeaseTypeEnum getTypeEnumBytypeId(int value){
        for(LeaseTypeEnum topType : values()){
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
