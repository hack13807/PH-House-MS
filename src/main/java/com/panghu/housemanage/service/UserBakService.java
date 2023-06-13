package com.panghu.housemanage.service;

import com.panghu.housemanage.pojo.po.UserBak;


public interface UserBakService {
    UserBak checkUser(UserBak user);

    UserBak checkManager(UserBak user);
}
