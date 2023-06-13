package com.panghu.housemanage.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Long id;
    private String number;
    private String description;
    private String status;
    private Long memberId;

    public Room setStatus(String status) {
        this.status = status;
        return this;
    }
}
