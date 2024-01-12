package com.xinxian.generator.framework.bean;

import com.xinxian.generator.framework.dto.vo.EntityPropertyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName GenTableConfig
 * @Description 表配置类
 * @Author lmy
 * @Date 2023/5/8 22:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenTableConfig {

    /* base info */
    private String tableName;
    private String entityName;
    private String entityDesc;
    private String bizClassName;
    private String requestMapping;
    private String controllerType;
    private List<String> checkParamFields;

    /* 前端查询 */
    List<GenQueryField> queryFieldList;

    /* 关系表 */
    List<GenRelationTable> relationTableList;

    /* 唯一约束 */
    List<GenUniqueField> uniqueFieldList;

    /* 字段集合 */
    List<EntityProperty> entityPropertyList;

    /**
     * 增加的实体映射
     */
    private List<EntityPropertyVO> addEntityList;

    /**
     * 增加的字段列表
     */
    private List<EntityPropertyVO> addFieldList;

    /**
     * 增加的参数映射
     */
    private List<EntityPropertyVO> addParamList;

    /**
     * 增加的结果映射
     */
    private List<EntityPropertyVO> addResultList;

    /**
     * 增加的转化映射
     */
    private List<EntityPropertyVO> addConvertList;

}
