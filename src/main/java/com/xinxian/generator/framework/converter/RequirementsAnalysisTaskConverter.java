package com.xinxian.generator.framework.converter;

import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.dto.vo.EntityPropertyVO;

/**
 * @ClassName RequirementsAnalysisTaskConverter
 * @Description 需求分析任务转换器
 * @Author lmy
 * @Date 2023/07/12 01:10
 */
public class RequirementsAnalysisTaskConverter {

    /**
     * 转为EntityProperty
     *
     * @param entityPropertyVO {@link EntityPropertyVO}
     * @return {@link EntityProperty}
     */
    public static EntityProperty converterToEntityProperty(EntityPropertyVO entityPropertyVO) {
        if (entityPropertyVO == null) {
            return null;
        }
        EntityProperty entityProperty = EntityProperty.builder()
                .column(entityPropertyVO.getColumn())
                .property(entityPropertyVO.getProperty())
                .jdbcType(entityPropertyVO.getJdbcType())
                .desc(entityPropertyVO.getDesc())
                .type(entityPropertyVO.getType())
                .length(entityPropertyVO.getLength())
                .notNull(entityPropertyVO.getNotNull())
                .defaultValue(entityPropertyVO.getDefaultValue())
                .frontWidth(entityPropertyVO.getFrontWidth())
                .relationType(entityPropertyVO.getRelationType())
                .relationField(entityPropertyVO.getRelationField())
                .checkUniqueType(entityPropertyVO.getCheckUniqueType())
                .build();
        return entityProperty;
    }

}
