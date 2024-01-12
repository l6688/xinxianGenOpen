package com.xinxian.generator.framework.dto.vo;

/**
 * @ClassName MapperXmlProperty
 * @Description mapper.xml属性
 * @Author lmy
 * @Date 2023/1/18 12:48
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityPropertyVO {

    private String column;

    private String property;

    private String jdbcType;

    private String desc;

    private String type;

    private String length;

    private Boolean notNull;

    private String defaultValue;

    private String frontWidth;

    private String relationType;

    private String relationField;

    private Integer checkUniqueType;

    /**
     * sql子项
     */
    private String sqlItem;

    /**
     * 转换类型 CastTypeEnum
     */
    private Integer castType;

    /**
     * 源类型
     */
    private String sourceType;

    /**
     * 自定义对象
     */
    private String customObject;

}
