package com.yif.param;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Yif
 */
@Data
public class RightMsg {
    @ExcelProperty(value = "企业账号")
    private String id;
}
