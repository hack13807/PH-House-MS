package com.panghu.housemanage.common.enumeration;

public enum RoomStatusEnum {
    UNUSED(0,"待出租"),
    NORMAL(1,"出租中"),
    PENDING(2,"待处理");

    RoomStatusEnum(int code , String value){
        this.code = code ;
        this.value = value ;
    }

    private int code;
    private String value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 通过code获取value
     * @param code
     * @return
     */
    public static String getValueByCode(int code){

        for(RoomStatusEnum typeEnum:RoomStatusEnum.values()){
            if(typeEnum.getCode() == code){
                return typeEnum.getValue();
            }
        }
        return "";
    }

    /**
     * 通过code获取实例
     * @param code
     * @return
     */
    public static RoomStatusEnum getTypeEnumBytypeId(int code){
        for(RoomStatusEnum topType : values()){
            if(topType.getCode() == code){
                return topType;
            }
        }
        return null;
    }
}
