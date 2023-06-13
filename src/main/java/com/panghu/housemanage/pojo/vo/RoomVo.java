package com.panghu.housemanage.pojo.vo;

import com.panghu.housemanage.pojo.po.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomVo {
    private Long id;
    private String number;
    private String description;
    private String status;
    private String memberName;

    public RoomVo setStatus(String status) {
        this.status = status;
        return this;
    }
}
