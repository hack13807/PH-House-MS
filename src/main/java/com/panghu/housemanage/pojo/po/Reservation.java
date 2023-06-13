package com.panghu.housemanage.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class Reservation {
    private int reservationId;
    private int userId;
    private int bookId;
    private Date deadline;
}
