package com.xinxian.generator.framework.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenTable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 实体类名称
     */
    private String entityName;

    /**
     * 实体类描述
     */
    private String entityDesc;

    /**
     * 生成业务名
     */
    private String bizClassName;

    /**
     * 请求地址
     */
    private String requestMapping;

    /**
     * 控制器类型
     */
    private String controllerType;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 生成类列表，逗号分隔
     */
    private String genClassList;

    /**
     * 生成功能列表，逗号分隔
     */
    private String genFunctionList;

    /**
     * 检查参数字段，逗号分隔
     */
    private String checkParamFields;

    /**
     * 前端查询
     */
    private String queryFieldList;

    /**
     * 关系表
     */
    private String relationTableList;

    /**
     * 唯一约束
     */
    private String uniqueFieldList;

    /**
     * 字段集合
     */
    private String entityPropertyList;

    /**
     * 建表语句
     */
    private String createSql;

    /**
     * 是否删除旧表
     */
    private String needDeleteTable;

    /**
     * 代码生成日期
     */
    private String genDate;

    /**
     * 增加的实体映射
     */
    private String entityMap;

    /**
     * 增加的字段映射
     */
    private String addFieldMap;

    /**
     * 增加的参数映射
     */
    private String paramMap;

    /**
     * 增加的结果映射
     */
    private String resultMap;

    /**
     * 增加的结果映射
     */
    private String convertMap;

    /**
     * 前端组件
     */
    private String frontComponent;

    /**
     * 支持需求 逗号分隔
     */
    private String supportRequirement;

    /**
     * api结果
     */
    private String apiResult;

    /**
     * 三方生成类型 1-通过JSON 2-通过EXCEL
     */
    private String genCreateItemType;

    /**
     * 是否测试表 1-是 2-否
     */
    private Integer isTest;

    /**
     * 作者
     */
    private String author;

    /**
     * 其它生成选项
     */
    private String options;

    /**
     * 生成包路径
     */
    private String packageName;

    /**
     * 生成模块名
     */
    private String moduleName;

    /**
     * 生成路径（不填默认项目路径）
     */
    private String genPath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 最后更新时间
     */
    private Date updateTime;

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

    /**
     * 0删除 1正常
     */
    private Integer status;


}