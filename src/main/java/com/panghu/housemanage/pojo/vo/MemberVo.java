package com.panghu.housemanage.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo {
    private Long id;
    private String name;
    private String phone;
    private String idCard;
    private String room;
    private String status;

    public MemberVo setStatus(String status) {
        this.status = status;
        return this;
    }
}
