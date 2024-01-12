package com.xinxian.generator.utils;

import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import com.xinxian.generator.framework.dto.enums.CastTypeEnum;

import java.util.*;

/**
 * @ClassName MybatisEntityUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorConverterUtils {

    private static final Set omitFieldSet = new HashSet(Arrays.asList("createTime", "createUserId", "updateTime", "createBy", "isDelete"));
    private static final Set mainEntityFieldSet = new HashSet(Arrays.asList("updateUserId", "updateBy"));
    /* 参数转实体模板map */
    public static Map<String, String> param2EntityTemplateMap = new HashMap() {
        {
            put(CastTypeEnum.NOT_HAVE.getValue(), "                .{{property}}(param.get{{upperFirstProperty}}())\n");
            put(CastTypeEnum.OBJECT.getValue(), "                .{{property}}(param.get{{upperFirstProperty}}() != null ? JSON.toJSONString(param.get{{upperFirstProperty}}()) : null)\n");
            put(CastTypeEnum.OBJECT_LIST.getValue(), "                .{{property}}(StringUtil.listCastString(param.get{{upperFirstProperty}}()))\n");
            put(CastTypeEnum.COMMA_SEPARATED.getValue(), "                .{{property}}(StringUtil.commaSeparatedParamCastString(param.get{{upperFirstProperty}}()))\n");
            put(CastTypeEnum.TIME_FORMAT.getValue(), "                .{{property}}(param.get{{upperFirstProperty}}() != null ? DateUtils.getDateByString(param.get{{upperFirstProperty}}()) : null)\n");
        }
    };
    /* 实体转结果模板map */
    public static Map<String, String> entity2ResultTemplateMap = new HashMap() {
        {
            put(CastTypeEnum.NOT_HAVE.getValue(), "                .{{property}}(record.get{{upperFirstProperty}}())\n");
            put(CastTypeEnum.OBJECT.getValue(), "                .{{property}}(record.get{{upperFirstProperty}}() != null ? JSON.parseObject(record.get{{upperFirstProperty}}(), {{customObject}}.class) : null)\n");
            put(CastTypeEnum.OBJECT_LIST.getValue(), "                .{{property}}(record.get{{upperFirstProperty}}() != null ? JSON.parseArray(record.get{{upperFirstProperty}}(), {{customObject}}.class) : null)\n");
            put(CastTypeEnum.COMMA_SEPARATED.getValue(), "                .{{property}}(StringUtils.isNotEmpty(record.get{{upperFirstProperty}}()) ? Arrays.asList(record.get{{upperFirstProperty}}().split(\",\")) : null)\n");
            put(CastTypeEnum.TIME_FORMAT.getValue(), "                .{{property}}(DateUtils.format(DateUtils.YYYY_MM_DD_HH_MM_SS, record.get{{upperFirstProperty}}()))\n");
        }
    };


    /**
     * 处理Converter
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        String slaveRelationField = configMap.get("${slaveRelationField}");
        Integer relationType = Integer.valueOf(configMap.get("${relationType}"));
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
        LinkedHashMap<String, EntityProperty> convertMap = GeneratorUtils.getFieldPropertyMap(configMap, "${convertMap}");
        generatorText = processNewEntity(generatorText, configMap, propertyMap, slaveRelationField, relationType, convertMap);
        Set<String> ignoreSet = GeneratorUtils.getIgnoreSet(configMap, GenTypeEnum.PARAM);
        generatorText = processNewResultEntity(generatorText, configMap, propertyMap, slaveRelationField, relationType, ignoreSet, convertMap);
        return generatorText;
    }


    /**
     * 处理NewEntity
     */
    public static String processNewEntity(String text, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap, String slaveRelationField, Integer relationType, LinkedHashMap<String, EntityProperty> convertMap) {
        String entityName = configMap.get("${entityName}");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("${entityName}.builder()\n".replace("${entityName}", entityName));
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            if (omitFieldSet.contains(name)) {
                continue;
            }
            EntityProperty entityProperty = entry.getValue();
            Integer castType = entityProperty.getCastType() == null ? CastTypeEnum.NOT_HAVE.getValue() : entityProperty.getCastType();
            /* convertMap */
            if (convertMap != null && convertMap.containsKey(entityProperty.getProperty()) && convertMap.get(entityProperty.getProperty()).getCastType() != null) {
                castType = convertMap.get(entityProperty.getProperty()).getCastType();
            }
            String setField = GeneratorUtils.replaceProperty(param2EntityTemplateMap.get(castType), entityProperty);
            String getName = Objects.equals(name, slaveRelationField) ? "Id" : StrUtil.upperFirst(name);
            if (GeneratorUtils.relationType3Slave(configMap) && (mainEntityFieldSet.contains(name) || Objects.equals(name, slaveRelationField))) {
                setField = String.format("                .%s(mainEntity != null ? mainEntity.get%s() : param.get%s())\n", name, getName, StrUtil.upperFirst(name));
            }
            stringBuilder.append(setField);
        }
        stringBuilder.append("                .build();");
        String newEntity = stringBuilder.toString();
        text = text.replace("${newEntity}", newEntity);
        return text;
    }

    /**
     * 处理processNewResultEntity
     */
    public static String processNewResultEntity(String text, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap, String slaveRelationField, Integer relationType, Set<String> ignoreSet, LinkedHashMap<String, EntityProperty> convertMap) {
        String entityName = configMap.get("${entityName}");
        StringBuilder stringBuilder = new StringBuilder();
        String entityType = GeneratorUtils.relationType3Slave(configMap) ? "View" : "View";
        stringBuilder.append("${entityName}${entityType}.builder()\n".replace("${entityName}", entityName).replace("${entityType}", entityType));
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            if (ignoreSet != null && ignoreSet.contains(name)) {
                continue;
            }
            EntityProperty entityProperty = entry.getValue();
            Integer castType = entityProperty.getCastType() == null ? CastTypeEnum.NOT_HAVE.getValue() : entityProperty.getCastType();
            /* convertMap */
            if (convertMap != null && convertMap.containsKey(entityProperty.getProperty()) && convertMap.get(entityProperty.getProperty()).getCastType() != null) {
                castType = convertMap.get(entityProperty.getProperty()).getCastType();
            }
            String setField = GeneratorUtils.replaceProperty(entity2ResultTemplateMap.get(castType), entityProperty);
            if ("createTime".equals(name) || "updateTime".equals(name)) {
                setField = String.format("                .%s(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, record.get%s()))\n", name, StrUtil.upperFirst(name));
            } else if (GeneratorUtils.relationType3Slave(configMap) && Objects.equals(name, slaveRelationField)) {
                String setName = Objects.equals(name, slaveRelationField) ? "Id" : StrUtil.upperFirst(name);
                setField = String.format("                .%s(mainEntityResult != null ? mainEntityResult.get%s() : record.get%s())\n", name, setName, StrUtil.upperFirst(name));
            }
            stringBuilder.append(setField);
        }
        stringBuilder.append("                .build();");
        String newResultEntity = stringBuilder.toString();
        text = text.replace("${newEntity}", newResultEntity);
        text = text.replace("${newResultEntity}", newResultEntity);
        return text;
    }
}
