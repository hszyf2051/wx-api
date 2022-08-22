package com.yif.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

/**
 * @author admin
 */
@Data
public class Doctor {
    /**
     * 姓名
     */
    @ExcelProperty(index = 0)
    private String name;

    /**
     * 企业账号
     */
    @ExcelProperty(index = 1)
    private String id;

    /**
     * 就诊量
     */
    @ExcelProperty(index = 2)
    private Integer visits;

    /**
     * 看诊最多日看诊量
     */
    @ExcelProperty(index = 3)
    private Integer maxVisit;

    /**
     * 看诊最多日期
     */
    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty(index = 4)
    private String maxVisitDate;

    /**
     * 住院患者治疗量
     */
    @ExcelProperty(index = 5)
    private Integer healing;

    /**
     * 手术量
     */
    @ExcelProperty(index = 6)
    private Integer operation;

    /**
     * 手术总时长
     */
    @ExcelProperty(index = 7)
    private String timeOperations;

    /**
     * 手术最多搭档
     */
    @ExcelProperty(index = 8)
    private String partner;

    /**
     * 手术时长最长日期
     */
    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty(index = 9)
    private String maxDate;

    /**
     * 手术时长最长时间（分钟）
     */
    @ExcelProperty(index = 10)
    private String maxTimeOperations;

    /**
     * 手术最长结束时间
     */
    @DateTimeFormat("yyyy年MM月dd日HH:mm:ss")
    @ExcelProperty(index = 11)
    private String latestTimeOperations;

    /**
     * 医生生日
     */
    @DateTimeFormat("mm月dd日")
    @ExcelProperty(index = 12)
    private String birthDate;

    /**
     * 生日当天看诊量
     */
    @ExcelProperty(index = 13)
    private Integer birthVisits;

    /**
     * 生日当天手术量
     */
    @ExcelProperty(index = 14)
    private Integer birthOperations;

    /**
     * 线上诊疗人次（互联网门诊）
     */
    @ExcelProperty(index = 15)
    private Integer onlineVisits;
}
