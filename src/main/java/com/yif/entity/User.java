package com.yif.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author admin
 */
@Data
public class User {

    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "部门编号")
    private String department;

    @ExcelProperty(value = "用户名")
    private String userid;

}
