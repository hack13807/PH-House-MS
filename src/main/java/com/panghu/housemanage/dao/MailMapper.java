package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.po.MailDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailMapper {
    // 还书提醒
    List<MailDetail> returnReminder();

    List<MailDetail> resReminder();
}
