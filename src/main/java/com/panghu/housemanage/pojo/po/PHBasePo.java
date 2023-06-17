package com.panghu.housemanage.pojo.po;

import lombok.Data;

/**
 * PO基类，所有ResponseBody对象继承此类
 */
@Data
public abstract class PHBasePo {
    private int offset;
    private int limit;
}
