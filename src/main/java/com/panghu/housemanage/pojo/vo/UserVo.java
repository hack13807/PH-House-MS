package com.panghu.housemanage.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserVo {
    private Long userId;
    private String userNumber;
    private String userName;
    private String phone;
    private String userRole;
    private String userStatus;
}
