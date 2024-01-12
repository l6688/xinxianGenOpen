package com.xinxian.generator.framework.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.xinxian.generator.framework.bean.*;
import com.xinxian.generator.framework.dto.entity.GenTable;
import com.xinxian.generator.framework.dto.params.GenTableParam;
import com.xinxian.generator.framework.dto.results.GenTableResult;
import com.xinxian.generator.framework.dto.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName GenTableConverter
 * @Description 代码生成业务表转换器
 * @Author lmy
 * @Date 2023/05/28 15:12
 */
public class GenTableConverter {

    public static final String needDeleteTable = "1";
    public static final String doNotNeedDeleteTable = "2";
    public static final Integer mapTypeEntity = 1;
    public static final Integer mapTypeAddField = 2;
    public static final Integer mapTypeParam = 3;
    public static final Integer mapTypeResult = 4;


    /**
     * 转为代码生成业务表列表
     *
     * @param params {@link List<GenTableParam>}
     * @return {@link List<GenTable>}
     */
    public static List<GenTable> convertToListGenTable(List<GenTableParam> params) {
        List<GenTable> results = Lists.newArrayList();
        if (CollectionUtils.isEmpty(params)) {
            return results;
        }
        return params.stream().map(GenTableConverter::convertToGenTable).collect(Collectors.toList());
    }

    /**
     * 转为代码生成业务表
     *
     * @param param {@link GenTableParam}
     * @return {@link GenTable}
     */
    public static GenTable convertToGenTable(GenTableParam param) {
        if (param == null) {
            return null;
        }
        GenTable result = GenTable.builder()
                .id(param.getId())
                .tableName(param.getTableName())
                .entityName(param.getEntityName())
                .entityDesc(param.getEntityDesc())
                .bizClassName(param.getBizClassName())
                .requestMapping(param.getRequestMapping())
                .controllerType(param.getControllerType())
                .projectName(param.getProjectName())
                .genClassList(param.getGenClassList())
                .genFunctionList(param.getGenFunctionList())
                .checkParamFields(getCheckParamFields(param.getCheckParamFieldList()))
                .queryFieldList(getQueryFieldList(param.getQueryFields()))
                .uniqueFieldList(getUniqueFieldList(param.getUniqueFields()))
                .entityPropertyList(param.getEntityPropertyList())
                .createSql(param.getCreateSql())
                .needDeleteTable(param.getNeedDeleteTable())
                .genDate(param.getGenDate())
                .entityMap(converterToMapStr(param.getAddEntityList()))
                .addFieldMap(converterToMapStr(param.getAddFieldList()))
                .paramMap(converterToMapStr(param.getAddParamList()))
                .resultMap(converterToMapStr(param.getAddResultList()))
                .convertMap(converterToMapStr(param.getAddConvertList()))
                .frontComponent(param.getFrontComponent())
                .supportRequirement(param.getSupportRequirement())
                .apiResult(param.getApiResult())
                .genCreateItemType(param.getGenCreateItemType())
                .isTest(param.getIsTest())
                .author(param.getAuthor())
                .options(param.getOptions())
                .packageName(param.getPackageName())
                .moduleName(param.getModuleName())
                .genPath(param.getGenPath())
                .updateUserId(param.getUpdateUserId())
                .updateUserName(param.getUpdateUserName())
                .build();
        return result;
    }

    private static String getUniqueFieldList(List<GenUniqueFieldVO> uniqueFields) {
        if (uniqueFields == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(uniqueFields)) {
            return "";
        }
        uniqueFields.stream().forEach(r -> r.setUniqueFields(Arrays.asList(r.getUniqueFieldsStr().split(","))));
        return JSON.toJSONString(uniqueFields);
    }

    private static String getQueryFieldList(List<GenQueryFieldVO> queryFields) {
        if (queryFields == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(queryFields)) {
            return "";
        }
        return JSON.toJSONString(queryFields);
    }

    /**
     * 获取CheckParamFields
     *
     * @param checkParamFieldList {@link List<String>}
     * @return {@link String}
     */
    private static String getCheckParamFields(List<GenCheckParamFieldVO> checkParamFieldList) {
        if (checkParamFieldList == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(checkParamFieldList)) {
            return "";
        }
        return StringUtils.join(checkParamFieldList.stream().map(GenCheckParamFieldVO::getField).collect(Collectors.toList()), ",");
    }

    /**
     * 转为代码生成业务表返回值列表
     *
     * @param list {@link List<GenTable>}
     * @return {@link List<GenTableResult>}
     */
    public static List<GenTableResult> convertToListGenTableResult(List<GenTable> list) {
        List<GenTableResult> results = Lists.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return results;
        }
        return list.stream().map(GenTableConverter::convertToGenTableResult).collect(Collectors.toList());
    }

    /**
     * 转为代码生成业务表返回值
     *
     * @param record {@link GenTable}
     * @return {@link GenTableResult}
     */
    public static GenTableResult convertToGenTableResult(GenTable record) {
        if (record == null) {
            return null;
        }
        GenTableResult result = GenTableResult.builder()
                .id(record.getId())
                .tableName(record.getTableName())
                .entityName(record.getEntityName())
                .entityDesc(record.getEntityDesc())
                .bizClassName(record.getBizClassName())
                .requestMapping(record.getRequestMapping())
                .controllerType(record.getControllerType())
                .projectName(record.getProjectName())
                .genClassList(record.getGenClassList())
                .genFunctionList(record.getGenFunctionList())
                .checkParamFields(record.getCheckParamFields())
                .queryFieldList(record.getQueryFieldList())
                .relationTableList(record.getRelationTableList())
                .uniqueFieldList(record.getUniqueFieldList())
                .entityPropertyList(record.getEntityPropertyList())
                .createSql(record.getCreateSql())
                .needDeleteTable(record.getNeedDeleteTable())
                .genDate(record.getGenDate())
                .entityMap(record.getEntityMap())
                .addFieldMap(record.getAddFieldMap())
                .paramMap(record.getParamMap())
                .resultMap(record.getResultMap())
                .convertMap(record.getConvertMap())
                .frontComponent(record.getFrontComponent())
                .supportRequirement(record.getSupportRequirement())
                .apiResult(record.getApiResult())
                .genCreateItemType(record.getGenCreateItemType())
                .isTest(record.getIsTest())
                .author(record.getAuthor())
                .options(record.getOptions())
                .packageName(record.getPackageName())
                .moduleName(record.getModuleName())
                .genPath(record.getGenPath())
                .createUserId(record.getCreateUserId())
                .createUserName(record.getCreateUserName())
                .updateUserName(record.getUpdateUserName())
                .addEntityList(converterToListEntityPropertyVO(record.getEntityMap()))
                .addFieldList(converterToListEntityPropertyVO(record.getAddFieldMap()))
                .addParamList(converterToListEntityPropertyVO(record.getParamMap()))
                .addResultList(converterToListEntityPropertyVO(record.getResultMap()))
                .addConvertList(converterToListEntityPropertyVO(record.getConvertMap()))
                .checkParamFieldList(getCheckParamFieldList(record.getCheckParamFields()))
                .queryFields(getQueryFields(record.getQueryFieldList()))
                .relationTables(getRelationTables(record.getRelationTableList()))
                .uniqueFields(getUniqueFields(record.getUniqueFieldList()))
                .build();
        return result;
    }

    private static List<GenUniqueFieldVO> getUniqueFields(String uniqueFieldList) {
        if (StringUtils.isEmpty(uniqueFieldList)) {
            return null;
        }
        List<GenUniqueFieldVO> genUniqueFieldVOS = JSON.parseArray(uniqueFieldList, GenUniqueFieldVO.class);
        genUniqueFieldVOS.stream().forEach(r -> r.setUniqueFields(Arrays.asList(r.getUniqueFieldsStr().split(","))));
        return genUniqueFieldVOS;
    }

    private static List<GenRelationTableVO> getRelationTables(String relationTableList) {
        if (StringUtils.isEmpty(relationTableList)) {
            return null;
        }
        return JSON.parseArray(relationTableList, GenRelationTableVO.class);
    }

    /**
     * 获取QueryFields
     *
     * @param queryFieldList {@link String}
     * @return {@link List<GenQueryFieldVO>}
     */
    private static List<GenQueryFieldVO> getQueryFields(String queryFieldList) {
        if (StringUtils.isEmpty(queryFieldList)) {
            return null;
        }
        return JSON.parseArray(queryFieldList, GenQueryFieldVO.class);
    }

    /**
     * 获取checkParamFieldList
     *
     * @param checkParamFields {@link String}
     * @return {@link List<String>}
     */
    private static List<GenCheckParamFieldVO> getCheckParamFieldList(String checkParamFields) {
        if (StringUtils.isEmpty(checkParamFields)) {
            return null;
        }
        List<GenCheckParamFieldVO> results = Lists.newArrayList();
        for (String field : checkParamFields.split(",")) {
            results.add(GenCheckParamFieldVO.builder().field(field).build());
        }
        return results;
    }

    /**
     * 转为生成表配置
     *
     * @param genTable {@link GenTable}
     * @return {@link GenTableConfig}
     */
    public static GenTableConfig convertToGenTableConfig(GenTable genTable) {
        if (genTable == null) {
            return null;
        }
        GenTableConfig genTableConfig = GenTableConfig.builder()
                .tableName(genTable.getTableName())
                .entityName(genTable.getEntityName())
                .entityDesc(genTable.getEntityDesc())
                .bizClassName(genTable.getBizClassName())
                .requestMapping(genTable.getRequestMapping())
                .controllerType(genTable.getControllerType())
                .checkParamFields(StringUtils.isEmpty(genTable.getCheckParamFields()) ? null : Arrays.asList(genTable.getCheckParamFields().split(",")))
                .queryFieldList(StringUtils.isEmpty(genTable.getQueryFieldList()) ? null : JSON.parseArray(genTable.getQueryFieldList(), GenQueryField.class))
                .relationTableList(StringUtils.isEmpty(genTable.getRelationTableList()) ? null : JSON.parseArray(genTable.getRelationTableList(), GenRelationTable.class))
                .uniqueFieldList(StringUtils.isEmpty(genTable.getUniqueFieldList()) ? null : JSON.parseArray(genTable.getUniqueFieldList(), GenUniqueField.class))
                .entityPropertyList(StringUtils.isEmpty(genTable.getEntityPropertyList()) ? null : JSON.parseArray(genTable.getEntityPropertyList(), EntityProperty.class))
                .addEntityList(converterToListEntityPropertyVO(genTable.getEntityMap()))
                .addFieldList(converterToListEntityPropertyVO(genTable.getAddFieldMap()))
                .addParamList(converterToListEntityPropertyVO(genTable.getParamMap()))
                .addResultList(converterToListEntityPropertyVO(genTable.getResultMap()))
                .addConvertList(converterToListEntityPropertyVO(genTable.getConvertMap()))
                .build();
        return genTableConfig;
    }

    /**
     * 转为map
     *
     * @param entityPropertyList {@link List<  EntityPropertyVO  >}
     * @return {@link String}
     */
    public static String converterToMapStr(List<EntityPropertyVO> entityPropertyList) {
        if (entityPropertyList == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(entityPropertyList)) {
            return "";
        }
        LinkedHashMap<String, EntityProperty> linkedHashMap = new LinkedHashMap<>();
        for (EntityPropertyVO entityPropertyVO : entityPropertyList) {
            linkedHashMap.put(entityPropertyVO.getProperty(), convertToEntityProperty(entityPropertyVO));
        }
        return JSON.toJSONString(linkedHashMap);
    }

    /**
     * 转为map
     *
     * @param mapStr {@link String}
     * @return {@link List<EntityPropertyVO>}
     */
    public static List<EntityPropertyVO> converterToListEntityPropertyVO(String mapStr) {
        if (StringUtils.isEmpty(mapStr)) {
            return null;
        }
        LinkedHashMap<String, EntityProperty> propertyMap = JSON.parseObject(mapStr, new TypeReference<LinkedHashMap<String, EntityProperty>>() {
        });
        List<EntityPropertyVO> resultList = Lists.newArrayList();
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            EntityPropertyVO entityPropertyVO = convertToEntityPropertyVO(entry.getValue());
            resultList.add(entityPropertyVO);
        }
        return resultList;
    }

    /**
     * 转为EntityProperty
     *
     * @param entityPropertyVO {@link EntityPropertyVO}
     * @return {@link EntityProperty}
     */
    public static EntityProperty convertToEntityProperty(EntityPropertyVO entityPropertyVO) {
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
                .castType(entityPropertyVO.getCastType())
                .sourceType(entityPropertyVO.getSourceType())
                .customObject(entityPropertyVO.getCustomObject())
                .build();
        return entityProperty;
    }

    /**
     * 转为EntityPropertyVO
     *
     * @param entityProperty {@link EntityProperty}
     * @return {@link EntityPropertyVO}
     */
    public static EntityPropertyVO convertToEntityPropertyVO(EntityProperty entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        EntityPropertyVO entityPropertyVO = EntityPropertyVO.builder()
                .column(entityProperty.getColumn())
                .property(entityProperty.getProperty())
                .jdbcType(entityProperty.getJdbcType())
                .desc(entityProperty.getDesc())
                .type(entityProperty.getType())
                .length(entityProperty.getLength())
                .notNull(entityProperty.getNotNull())
                .defaultValue(entityProperty.getDefaultValue())
                .frontWidth(entityProperty.getFrontWidth())
                .relationType(entityProperty.getRelationType())
                .relationField(entityProperty.getRelationField())
                .checkUniqueType(entityProperty.getCheckUniqueType())
                .castType(entityProperty.getCastType())
                .sourceType(entityProperty.getSourceType())
                .customObject(entityProperty.getCustomObject())
                .build();
        return entityPropertyVO;
    }

    /**
     * 转为GenQueryField
     *
     * @param genQueryFieldVO {@link GenQueryFieldVO}
     * @return {@link GenQueryField}
     */
    public static GenQueryField convertToEntityProperty(GenQueryFieldVO genQueryFieldVO) {
        if (genQueryFieldVO == null) {
            return null;
        }
        GenQueryField genQueryField = GenQueryField.builder()
                .queryField(genQueryFieldVO.getQueryField())
                .queryFieldDesc(genQueryFieldVO.getQueryFieldDesc())
                .build();
        return genQueryField;
    }

    /**
     * 转为GenRelationTableVO
     *
     * @param genQueryField {@link GenQueryField}
     * @return {@link GenQueryFieldVO}
     */
    public static GenQueryFieldVO convertToGenQueryFieldVO(GenQueryField genQueryField) {
        if (genQueryField == null) {
            return null;
        }
        GenQueryFieldVO genQueryFieldVO = GenQueryFieldVO.builder()
                .queryField(genQueryField.getQueryField())
                .queryFieldDesc(genQueryField.getQueryFieldDesc())
                .build();
        return genQueryFieldVO;
    }

    /**
     * 转为GenRelationTable
     *
     * @param genRelationTableVO {@link GenRelationTableVO}
     * @return {@link GenRelationTable}
     */
    public static GenRelationTable convertToGenRelationTable(GenRelationTableVO genRelationTableVO) {
        if (genRelationTableVO == null) {
            return null;
        }
        GenRelationTable genRelationTable = GenRelationTable.builder()
                .relationType(genRelationTableVO.getRelationType())
                .mainTable(genRelationTableVO.getMainTable())
                .slaveTable(genRelationTableVO.getSlaveTable())
                .slaveRelationField(genRelationTableVO.getSlaveRelationField())
                .slaveEntityDesc(genRelationTableVO.getSlaveEntityDesc())
                .mainEntityDesc(genRelationTableVO.getMainEntityDesc())
                .checkParamFields(genRelationTableVO.getCheckParamFields())
                .build();
        return genRelationTable;
    }

    /**
     * 转为GenRelationTableVO
     *
     * @param genRelationTable {@link GenRelationTable}
     * @return {@link GenRelationTableVO}
     */
    public static GenRelationTableVO convertToGenRelationTableVO(GenRelationTable genRelationTable) {
        if (genRelationTable == null) {
            return null;
        }
        GenRelationTableVO genRelationTableVO = GenRelationTableVO.builder()
                .relationType(genRelationTable.getRelationType())
                .mainTable(genRelationTable.getMainTable())
                .slaveTable(genRelationTable.getSlaveTable())
                .slaveRelationField(genRelationTable.getSlaveRelationField())
                .slaveEntityDesc(genRelationTable.getSlaveEntityDesc())
                .mainEntityDesc(genRelationTable.getMainEntityDesc())
                .checkParamFields(genRelationTable.getCheckParamFields())
                .build();
        return genRelationTableVO;
    }

    /**
     * 转为GenUniqueField
     *
     * @param genUniqueFieldVO {@link GenUniqueFieldVO}
     * @return {@link GenUniqueField}
     */
    public static GenUniqueField convertToGenUniqueField(GenUniqueFieldVO genUniqueFieldVO) {
        if (genUniqueFieldVO == null) {
            return null;
        }
        GenUniqueField result = GenUniqueField.builder()
                .checkUniqueType(genUniqueFieldVO.getCheckUniqueType())
                .uniqueFields(genUniqueFieldVO.getUniqueFields())
                .build();
        return result;
    }

    /**
     * 转为GenUniqueFieldVO
     *
     * @param genUniqueField {@link GenUniqueField}
     * @return {@link GenUniqueFieldVO}
     */
    public static GenUniqueFieldVO convertToGenUniqueFieldVO(GenUniqueField genUniqueField) {
        if (genUniqueField == null) {
            return null;
        }
        GenUniqueFieldVO vo = GenUniqueFieldVO.builder()
                .checkUniqueType(genUniqueField.getCheckUniqueType())
                .uniqueFields(genUniqueField.getUniqueFields())
                .build();
        return vo;
    }


}
