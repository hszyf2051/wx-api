package com.yif.param;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Yif
 */
@Data
public class WrongMsg {
    @ExcelProperty(value = "错误id")
    private String id;
}
