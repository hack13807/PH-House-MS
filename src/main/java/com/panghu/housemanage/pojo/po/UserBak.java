package com.panghu.housemanage.pojo.po;

import lombok.Data;

@Data
public class UserBak {
    private int userId;
    private String userName;
    private String passWd;
    private String rname;
    private String phone;
    private String email;

    public UserBak(String userName, String passWd) {
        this.userName = userName;
        this.passWd = passWd;
    }

    public UserBak() {}
}
