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
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 企业账号
     */
    @ExcelProperty(value = "企业账号")
    private String id;

    /**
     * 身份
     */
    @ExcelProperty(value = "身份")
    private String identity;

    /**
     * 就诊量
     */
    @ExcelProperty(value = "门诊就诊量")
    private Integer visits;

    /**
     * 看诊最多日看诊量
     */
    @ExcelProperty(value = "看诊最多日看诊量")
    private Integer maxVisit;

    /**
     * 看诊最多日期
     */
    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty(value = "看诊最多日期")
    private String maxVisitDate;

    /**
     * 住院患者治疗量
     */
    @ExcelProperty(value = "住院患者治疗量")
    private Integer healing;

    /**
     * 手术量
     */
    @ExcelProperty(value = "手术量")
    private Integer operation;

    /**
     * 手术总时长
     */
    @ExcelProperty(value = "手术总时长（小时）")
    private String timeOperations;

    /**
     * 手术最多搭档
     */
    @ExcelProperty(value = "手术最多搭档")
    private String partner;

    /**
     * 手术时长最长日期
     */
    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty(value = "手术时长最长日期")
    private String maxDate;

    /**
     * 手术时长最长时间
     */
    @ExcelProperty(value = "手术时长最长时间（分钟）")
    private String maxTimeOperations;

    /**
     * 手术最长结束时间
     */
    @DateTimeFormat("yyyy年MM月dd日HHmm")
    @ExcelProperty(value = "手术最长结束时间")
    private String latestTimeOperations;

    /**
     * 医生生日
     */
    @DateTimeFormat("mm月dd日")
    @ExcelProperty(value = "医生生日")
    private String birthDate;

    /**
     * 生日当天看诊量
     */
    @ExcelProperty(value = "生日当天看诊量")
    private Integer birthVisits;

    /**
     * 生日当天手术量
     */
    @ExcelProperty(value = "生日当天手术量")
    private Integer birthOperations;

    /**
     * 线上诊疗人次（互联网门诊）
     */
    @ExcelProperty(value = "线上诊疗人次（互联网门诊）")
    private Integer onlineVisits;
}
