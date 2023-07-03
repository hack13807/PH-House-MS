package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum MemberStatusEnum implements IEnum<Integer> {
    NEW(0,"新增"),
    RENTING(1,"租住中"),
    SURRENDER(2,"已退租");

    @EnumValue
    private final int code;

    @JsonValue
    private final String msg;

    MemberStatusEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

    /**
     * 通过code获取value
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(MemberStatusEnum typeEnum: MemberStatusEnum.values()){
            if(typeEnum.getValue() == code){
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
    public static MemberStatusEnum getTypeEnumBytypeId(int code){
        for(MemberStatusEnum topType : values()){
            if(topType.code == code){
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
