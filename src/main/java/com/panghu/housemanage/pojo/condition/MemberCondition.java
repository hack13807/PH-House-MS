package com.panghu.housemanage.pojo.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCondition extends PHBaseCondition {

    private String memberName;
    private String roomNo;

    public MemberCondition(int pn){
        super(pn);
    }
}
