package com.xinxian.generator.framework.service;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenRelationTable;
import com.xinxian.generator.framework.bean.GenTableConfig;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GeneratorConfigUtils;
import com.xinxian.generator.framework.utils.GeneratorTableUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import com.xinxian.generator.framework.dto.entity.GenTable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GeneratorConfig
 * @Description 自动生成配置
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
public interface IGeneratorConfig {

    /**
     * 生成代码业务使用-GenTableColumn配置信息
     *
     * @return
     */
    default Map<String, String> getGenTableColumn(Map<String, String> configMap) {
        String genTableConfigStr = configMap.get("genTableConfig");
        GenTableConfig genTableConfig = JSON.parseObject(genTableConfigStr, GenTableConfig.class);
        Map<String, String> tableConfig = GeneratorTableUtils.getTableConfig(genTableConfig);
        addCommonProperty(tableConfig);
        String genTableStr = configMap.get("genTable");
        GenTable genTable = JSON.parseObject(genTableStr, GenTable.class);
        /* entityMap */
        GeneratorConfigUtils.mergeProperty("${entityMap}", tableConfig, genTable.getEntityMap());
        /* paramMap */
        GeneratorConfigUtils.mergeProperty("${paramMap}", tableConfig, genTable.getParamMap());
        /* resultMap */
        GeneratorConfigUtils.mergeProperty("${resultMap}", tableConfig, genTable.getResultMap());
        /* convertMap */
        GeneratorConfigUtils.mergeProperty("${convertMap}", tableConfig, genTable.getConvertMap());
        return tableConfig;
    }

    /**
     * 获取通用的返回结果
     *
     * @return
     */
    default void addCommonProperty(Map<String, String> tableConfig) {
        /* entityMap */
        LinkedHashMap<String, EntityProperty> entityMap = getCommonEntity(tableConfig);
        tableConfig.put("${entityMap}", JSON.toJSONString(entityMap));
        /* paramMap */
        LinkedHashMap<String, EntityProperty> paramMap = getCommonParam(tableConfig);
        tableConfig.put("${paramMap}", JSON.toJSONString(paramMap));
        /* resultMap */
        LinkedHashMap<String, EntityProperty> resultMap = getCommonResult(tableConfig);
        tableConfig.put("${resultMap}", JSON.toJSONString(resultMap));
    }

    /**
     * 获取通用的返回结果
     *
     * @return
     */
    default LinkedHashMap<String, EntityProperty> getCommonResult(Map<String, String> tableConfig) {
        /* resultMap */
        LinkedHashMap<String, EntityProperty> resultMap = new LinkedHashMap<>();
        setCommonResult(resultMap);
        if (GeneratorUtils.relationType3Main(tableConfig)) {
            addMainEntity(resultMap, tableConfig);
        }
        addCommonEntity(resultMap, tableConfig);
        return resultMap;
    }

    /**
     * 获取通用的属性
     *
     * @return
     */
    default LinkedHashMap<String, EntityProperty> getCommonEntity(Map<String, String> tableConfig) {
        /* entityMap */
        LinkedHashMap<String, EntityProperty> resultMap = new LinkedHashMap<>();
        if (GeneratorUtils.relationType3Slave(tableConfig)) {
            addSlaveEntity(resultMap, tableConfig);
        }
        addCommonEntity(resultMap, tableConfig);
        setCommonEntity(resultMap);
        return resultMap;
    }

    /**
     * 获取通用的参数
     * 包含关系表的处理
     *
     * @return
     */
    default LinkedHashMap<String, EntityProperty> getCommonParam(Map<String, String> tableConfig) {
        /* resultMap */
        LinkedHashMap<String, EntityProperty> resultMap = new LinkedHashMap<>();
        setCommonParam(resultMap);
        if (GeneratorUtils.relationType3Main(tableConfig)) {
            addMainEntity(resultMap, tableConfig);
        }
        if (GeneratorUtils.relationType3Slave(tableConfig)) {
            addSlaveEntity(resultMap, tableConfig);
        }
        addCommonEntity(resultMap, tableConfig);
        return resultMap;
    }

    default void addCommonEntity(LinkedHashMap<String, EntityProperty> resultMap, Map<String, String> tableConfig) {
        if (GeneratorUtils.relationType2Main(tableConfig)) {
            addCommonContentEntity(resultMap, tableConfig);
        }
    }

    default void addCommonContentEntity(LinkedHashMap<String, EntityProperty> resultMap, Map<String, String> tableConfig) {
        resultMap.put(GeneratorConfigUtils.commonContentField, EntityProperty.builder().property(GeneratorConfigUtils.commonContentField).desc("内容").type(GeneratorUtils.typeString).jdbcType(GeneratorUtils.jdbcTypeVarchar).column(GeneratorConfigUtils.commonContentField).build());
    }

    default void addSlaveEntity(LinkedHashMap<String, EntityProperty> resultMap, Map<String, String> tableConfig) {
        String slaveRelationField = tableConfig.get("${slaveRelationField}") + "s";
        resultMap.put(slaveRelationField, EntityProperty.builder().property(slaveRelationField).desc(tableConfig.get("${mainEntityDesc}") + "id列表").type(GeneratorUtils.typeListLong).build());
    }

    default void addMainEntity(LinkedHashMap<String, EntityProperty> resultMap, Map<String, String> tableConfig) {
        List<GenRelationTable> relationTableList = GeneratorUtils.getListFromConfig(tableConfig, GenConstant.keyRelationTableList, GenRelationTable.class);
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.relationTableConverterToProperty(relationTableList);
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            EntityProperty entityProperty = entry.getValue();
            String shortSlaveEntityName = entityProperty.getProperty();
            String slaveEntityNameLowerFirst = shortSlaveEntityName + "List";
            String slaveEntityIdLIst = shortSlaveEntityName + "IdList";
            resultMap.put(slaveEntityNameLowerFirst, EntityProperty.builder().property(slaveEntityNameLowerFirst).desc(entityProperty.getDesc() + "列表").type(String.format("List<%s%s>", entityProperty.getType(), "VO")).build());
            resultMap.put(slaveEntityIdLIst, EntityProperty.builder().property(slaveEntityIdLIst).desc(entityProperty.getDesc() + "id列表").type(GeneratorUtils.typeListLong).build());
        }
    }

    void setCommonParam(LinkedHashMap<String, EntityProperty> paramMap);

    void setCommonEntity(LinkedHashMap<String, EntityProperty> entityMap);

    void setCommonResult(LinkedHashMap<String, EntityProperty> resultMap);
}