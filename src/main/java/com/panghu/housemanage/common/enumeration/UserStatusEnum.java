package com.panghu.housemanage.common.enumeration;

public enum UserStatusEnum {
    ENABLE(0, "启用"),
    DISABILE(1, "禁用");

    UserStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
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
     *
     * @param code
     * @return
     */
    public static String getValueByCode(int code) {

        for (UserStatusEnum typeEnum : UserStatusEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum.getValue();
            }
        }
        return "";
    }

    /**
     * 通过code获取实例
     *
     * @param code
     * @return
     */
    public static UserStatusEnum getTypeEnumBytypeId(int code) {
        for (UserStatusEnum topType : values()) {
            if (topType.getCode() == code) {
                return topType;
            }
        }
        return null;
    }
}
