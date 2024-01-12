package com.xinxian.generator.framework.dto.vo;

/**
 * @ClassName EntityContentVO
 * @Description 需求分析实体VO
 * @Author lmy
 * @Date 2023/7/16 10:29
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityContentVO {

    /**
     * 库
     */
    private String database;

    /**
     * 名称
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * sql
     */
    private String sql;

    /**
     * 实体子项内容集合
     */
    private List<EntityPropertyVO> itemList;

    /**
     * 关系类型: RelationType
     */
    private String relationType;

    /**
     * 关系字段
     */
    private String relationField;

    /**
     * 是否主表
     */
    private Boolean mainTable;

    /**
     * key集合
     */
    private List<KeyVO> keyList;

    /**
     * KeyVO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class KeyVO {

        /**
         * 类型
         */
        private String type;

        /**
         * 名称
         */
        private String name;

        /**
         * 字段集合
         */
        private List<String> fieldList;
    }
}


