package com.panghu.housemanage.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowDetail {
    private int borrowId;
    private String bname;
    private String userName;
    private Date btime;
    private Date deadline;
    private Date rtime;
}
