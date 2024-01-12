package com.xinxian.generator.framework.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName GenTableColumnVO
 * @Description 代码生成业务表字段VO
 * @Author lmy
 * @Date 2023/05/28 15:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenTableColumnVO {

    /**
     * 更新人邮箱
     */
    private String updateUserEmail;

    /**
     * 更新人部门名称
     */
    private String updateUserDept;

    /**
     * 页码下标
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 代码生成业务表id列表
     */
    private List<Long> genTableIds;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 代码生成业务表id
     */
    private Long genTableId;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    private String javaField;

    /**
     * 是否主键（1是）
     */
    private String isPk;

    /**
     * 是否自增（1是）
     */
    private String isIncrement;

    /**
     * 是否必填（1是）
     */
    private String isRequired;

    /**
     * 是否为插入字段（1是）
     */
    private String isInsert;

    /**
     * 是否编辑字段（1是）
     */
    private String isEdit;

    /**
     * 是否列表字段（1是）
     */
    private String isList;

    /**
     * 是否查询字段（1是）
     */
    private String isQuery;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    private String htmlType;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 最后更新时间
     */
    private String updateTime;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 更新人
     */
    private String updateUserName;


}