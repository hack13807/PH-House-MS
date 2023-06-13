package com.panghu.housemanage.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;
    private String name;
    private String phone;
    private String idCard;
    private Long roomId;
    private String status;

    public Member setStatus(String status) {
        this.status = status;
        return this;
    }
}
