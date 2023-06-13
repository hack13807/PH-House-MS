package com.panghu.housemanage.enumeration;

public enum MemberStatusEnum {
    RENTING(0,"租住中"),
    SURRENDER(1,"已退租");

    MemberStatusEnum(int code , String value){
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

        for(MemberStatusEnum typeEnum: MemberStatusEnum.values()){
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
    public static MemberStatusEnum getTypeEnumBytypeId(int code){
        for(MemberStatusEnum topType : values()){
            if(topType.getCode() == code){
                return topType;
            }
        }
        return null;
    }
}
