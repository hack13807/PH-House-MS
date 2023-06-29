package com.panghu.housemanage.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomStatusEnum implements IEnum<Integer> {
    DELETE(0,"禁用"),
    INUSE(1,"出租中"),
    UNUSED(2,"待租"),
    PENDING(3,"待处理");

    @EnumValue
    private final int code;

    @JsonValue
    private final String msg;

    RoomStatusEnum(int code , String msg){
        this.code = code ;
        this.msg = msg ;
    }

    /**
     * 通过code获取value
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(RoomStatusEnum typeEnum: RoomStatusEnum.values()){
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
    public static RoomStatusEnum getTypeEnumBytypeId(int code){
        for(RoomStatusEnum topType : values()){
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
