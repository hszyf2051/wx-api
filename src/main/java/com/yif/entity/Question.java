package com.yif.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yif
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Question对象", description="题目表")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    @ExcelProperty(value = "题目ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目类型 1单选  2多选 3判断 4主观题  5音频题  6视频题
     */
    @ExcelProperty(value = "题目类型")
    private Integer quesType;

    /**
     * 1普通,2中等  3困难
     */
    @ExcelProperty(value = "题目等级")
    private Integer level;

    /**
     * 题目图片
     */
    @ExcelProperty(value = "题目图片")
    private String image;

    /**
     * 题目内容
     */
    @ExcelProperty(value = "题目内容")
    private String content;

    /**
     * 题目备注
     */
    @ExcelProperty(value = "题目备注")
    private String remark;

    /**
     * 整题解析
     */
    @ExcelProperty(value = "题目分析")
    private String analysis;


}
