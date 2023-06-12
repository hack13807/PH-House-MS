package com.panghu.housemanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String number;
    private String name;
    private String phone;
    private String pwd;
    private Long roleId;
    private String status;
}
