package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MemberOptTypeEnum {
    MODIFY(1,"修改"),
    TERMINATE(2,"退租");

    private final int code;

    private final String msg;

    MemberOptTypeEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

    /**
     * 通过code获取value
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(MemberOptTypeEnum typeEnum: MemberOptTypeEnum.values()){
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
    public static MemberOptTypeEnum getTypeEnumBytypeId(int code){
        for(MemberOptTypeEnum topType : values()){
            if(topType.code == code){
                return topType;
            }
        }
        return null;
    }
}
