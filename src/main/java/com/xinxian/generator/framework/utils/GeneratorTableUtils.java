package com.xinxian.generator.framework.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.bean.*;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.service.GeneratorProject;
import com.xinxian.generator.service.GeneratorTable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName GeneratorGetData
 * @Description 自动生成优化
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
public class GeneratorTableUtils {

    /* 配置项 */
    public static List<String> notRelationType3SlaveRemoveMethods = Arrays.asList("selectByMainTableId", "deleteByMainTableId", "selectByMainTableIds");

    /* 非配置项 */
    private static Map<String, String> importPackageSuffixMap = new HashMap() {
        {
            put("", "dao.entity");
            put("VO", "dto.vo");
            put("Param", "dto.params");
            put("Result", "dto.results");
        }
    };

    /**
     * 执行生成类
     *
     * @param tableConfigMap {@link Map<String, String>}
     * @param generator      {@link GeneratorTable}
     * @return {@link Map< String, String>}
     */
    public static Map<String, String> getConfig(Map<String, String> tableConfigMap, GeneratorTable generator) {
        Map<String, String> configMap = new HashMap<>();
        configMap.putAll(tableConfigMap);
        configMap.putAll(generator.getProjectConfigMap());
        if (GeneratorTableUtils.getTableConfigMap(generator.tableConfigMap) != null && (tableConfigMap == null || !tableConfigMap.containsKey("genCreateItemType"))) {
            configMap.putAll(GeneratorTableUtils.getTableConfigMap(generator.tableConfigMap));
            addPropertyInfo(configMap, GeneratorUtils.getFieldPropertyMap(configMap, GenConstant.keyAddFieldMap));
        }
        /* allParamMap */
        GeneratorConfigUtils.mergeProperty(GenConstant.keyAllParamMap, configMap, configMap.get(GenConstant.keyParamMap));
        GeneratorConfigUtils.mergeProperty(GenConstant.keyAllParamMap, configMap, configMap.get(GenConstant.keyPropertyMap));
        /* allResultMap */
        GeneratorConfigUtils.mergeProperty(GenConstant.keyAllResultMap, configMap, configMap.get(GenConstant.keyResultMap));
        GeneratorConfigUtils.mergeProperty(GenConstant.keyAllResultMap, configMap, configMap.get(GenConstant.keyPropertyMap));
        return configMap;
    }

    /**
     * 初始化参数
     *
     * @param propertiesMap {@link }
     */
    public static void initParam(Map<String, String> propertiesMap, Map<String, String> initMap) {
        /* 处理生成时间 */
        initGenDate(propertiesMap, initMap);
        /* 前端特殊处理 */
        initFrontParam(propertiesMap);
        /* 属性关联参数初始化 */
        initPropertyUniqueField(propertiesMap);
        /* 导包 */
        initImportPackageParam(propertiesMap);
        /* notRelationList */
        initStaticParam(propertiesMap);
    }

    /**
     * 处理生成时间
     *
     * @param propertiesMap {@link Map<String, String>}
     */
    private static void initGenDate(Map<String, String> propertiesMap, Map<String, String> initMap) {
        if (initMap.containsKey(GeneratorMainUtils.initKeyGenDate)) {
            propertiesMap.put("${date}", initMap.get(GeneratorMainUtils.initKeyGenDate));
        }
    }

    /**
     * 静态参数动态赋值
     *
     * @param propertiesMap {@link Map<String, String>}
     */
    private static void initStaticParam(Map<String, String> propertiesMap) {
//        GeneratorBizImplUtils.notRelationType3MethodList = GeneratorUtils.doGenerator(GeneratorBizImplUtils.notRelationType3MethodList, propertiesMap);
    }

    /**
     * 增加导包
     *
     * @param propertiesMap {@link Map<String, String>}
     */
    private static void initImportPackageParam(Map<String, String> propertiesMap) {
        String packageBase = propertiesMap.get("${packageBase}");
        doInitImportPackageParam(propertiesMap.get("${entityName}"), packageBase);
        if (propertiesMap.containsKey("${slaveEntityName}")) {
            doInitImportPackageParam(propertiesMap.get("${slaveEntityName}"), packageBase);
        }
    }

    /**
     * 执行增加导包
     *
     * @param entityName  {@link String}
     * @param packageBase {@link String}
     */
    private static void doInitImportPackageParam(String entityName, String packageBase) {
        for (Map.Entry<String, String> entry : importPackageSuffixMap.entrySet()) {
            if (StringUtils.isEmpty(entityName)) {
                continue;
            }
            String className = entityName + entry.getKey();
            GeneratorUtils.importMap.put(String.format(" %s ", className), String.format("import %s.%s.%s;\n", packageBase, entry.getValue(), className));
            GeneratorUtils.importMap.put(String.format(" List<%s> ", className), String.format("import %s.%s.%s;\n", packageBase, entry.getValue(), className));
        }
    }

    /**
     * 属性关联参数初始化
     *
     * @param configMap {@link Map}
     */
    private static void initPropertyUniqueField(Map<String, String> configMap) {
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
        if (StringUtils.isNotEmpty(configMap.get("${uniqueFields}"))) {
            addUniqueFieldsDesc(configMap, propertyMap);
        }
    }

    /**
     * 增加uniqueFieldsDesc
     */
    private static void addUniqueFieldsDesc(Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        List<String> uniqueFieldList = JSON.parseArray(configMap.get("${uniqueFields}"), String.class);
        ArrayList<String> uniqueFieldDescList = Lists.newArrayList();
        for (String field : uniqueFieldList) {
            EntityProperty entityProperty = propertyMap.get(field);
            if (entityProperty != null) {
                uniqueFieldDescList.add(entityProperty.getDesc());
            }
        }
        configMap.put("${uniqueFieldsDesc}", StrUtil.join("、", uniqueFieldDescList));
    }

    /**
     * 初始化前端参数
     *
     * @param propertiesMap {@link }
     */
    private static void initFrontParam(Map<String, String> propertiesMap) {
        if (GeneratorProject.specialFrontApi.contains(propertiesMap.get("${projectName}"))) {
            propertiesMap.put("${apiBase}", propertiesMap.getOrDefault("${controllerEndPath}", ""));
            propertiesMap.put("${controllerPath}", "");
        }
    }

    /**
     * 执行生成类
     *
     * @param configMap {@link Map<String, String>}
     * @return
     */
    public static void addPropertyInfo(Map<String, String> configMap, LinkedHashMap<String, EntityProperty> addFieldMap) {
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.createPropertyMap(configMap);
        if (addFieldMap != null) {
            propertyMap.putAll(addFieldMap);
        }
        configMap.put("propertyMap", JSON.toJSONString(propertyMap));
        String hasDateProperty = "false";
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            if (GeneratorUtils.commonFieldSet.contains(entry.getKey())) {
                continue;
            }
            EntityProperty value = entry.getValue();
            if (Objects.equals(GenConstant.JDBC_TYPE_TIMESTAMP, value.getJdbcType())) {
                hasDateProperty = "true";
            }
        }
        configMap.put(GenConstant.CONFIG_MAP_HAS_DATE_KEY, hasDateProperty);
    }

    /**
     * 表配置信息
     *
     * @param genTableConfig {@link GenTableConfig}
     * @return {@link Map<String, String>}
     */
    public static Map<String, String> getTableConfig(GenTableConfig genTableConfig) {
        HashMap<String, String> tableConfigMap = new HashMap();
        /* 表参数，${做模板替换，其他的是传递到tableConfigMap处理 */
        tableConfigMap.put("${tableName}", genTableConfig.getTableName());
        tableConfigMap.put("${entityName}", genTableConfig.getEntityName());
        tableConfigMap.put("${entityDesc}", genTableConfig.getEntityDesc());
        tableConfigMap.put("queryFieldList", CollectionUtils.isEmpty(genTableConfig.getQueryFieldList()) ? null : JSON.toJSONString(genTableConfig.getQueryFieldList()));
        if (CollectionUtils.isNotEmpty(genTableConfig.getQueryFieldList())) {
            GenQueryField queryField = genTableConfig.getQueryFieldList().get(0);
            tableConfigMap.put("${queryField}", queryField.getQueryField());
        }
        tableConfigMap.put("${bizClassName}", genTableConfig.getBizClassName());
        tableConfigMap.put("${requestMapping}", genTableConfig.getRequestMapping());
        tableConfigMap.put("controllerType", genTableConfig.getControllerType());
        tableConfigMap.put("${checkParamFields}", CollectionUtils.isEmpty(genTableConfig.getCheckParamFields()) ? null : JSON.toJSONString(genTableConfig.getCheckParamFields()));
        /* 关系表 */
        tableConfigMap.put("${relationType}", "1");
        tableConfigMap.put("relationTableList", null);
        if (CollectionUtils.isNotEmpty(genTableConfig.getRelationTableList())) {
            tableConfigMap.put("relationTableList", JSON.toJSONString(genTableConfig.getRelationTableList()));
            GenRelationTable relationTable = genTableConfig.getRelationTableList().get(0);
            addRelationTableConfigMap(relationTable, tableConfigMap);
        }
        /* 唯一约束 */
        tableConfigMap.put("uniqueFieldList", genTableConfig.getUniqueFieldList() != null ? JSON.toJSONString(genTableConfig.getUniqueFieldList()) : null);
        return MapUtils.defaultNullValue(tableConfigMap);
    }

    /**
     * 处理关系表
     *
     * @param relationTable {@link GenRelationTable}
     * @param configMap     {@link Map<String,String>}
     * @return {@link String}
     */
    public static void addRelationTableConfigMap(GenRelationTable relationTable, Map<String, String> configMap) {
        configMap.put("${relationType}", relationTable.getRelationType());
        configMap.put("${mainTable}", relationTable.getMainTable());
        configMap.put("${slaveTable}", relationTable.getSlaveTable());
        configMap.put("${slaveRelationFieldColumn}", relationTable.getSlaveRelationField() != null ? StrUtil.toUnderlineCase(relationTable.getSlaveRelationField()) : null);
        configMap.put("mainRelationField", relationTable.getSlaveTable());
        configMap.put("${slaveEntityDesc}", relationTable.getSlaveEntityDesc());
        configMap.put("${mainEntityDesc}", relationTable.getMainEntityDesc());
        configMap.put("${checkSlaveParamFields}", CollectionUtils.isEmpty(relationTable.getCheckParamFields()) ? null : JSON.toJSONString(relationTable.getCheckParamFields()));
        if (!GeneratorUtils.notRelationTable(configMap)) {
            String slaveRelationFieldCamelCase = relationTable.getSlaveRelationField() != null ? StrUtil.toCamelCase(relationTable.getSlaveRelationField(), '_') : "";
            configMap.put("${slaveRelationField}", slaveRelationFieldCamelCase);
            configMap.put("${slaveRelationFieldUpperFirst}", StrUtil.upperFirst(slaveRelationFieldCamelCase));
            String shortSlaveEntityName = relationTable.getSlaveTable() != null ? StrUtil.toCamelCase(relationTable.getSlaveTable(), '_') : "";
            String slaveEntityName = StrUtil.upperFirst(shortSlaveEntityName);
            configMap.put("${slaveEntityName}", slaveEntityName);
            configMap.put("${shortSlaveEntityName}", shortSlaveEntityName);
            configMap.put("${contentEntityName}", GeneratorUtils.relationType2Main(configMap) ? StringUtils.isNotBlank(slaveEntityName) ? slaveEntityName : "CommonContent" : null);
        }
    }

    /**
     * 处理唯一约束
     *
     * @param uniqueField {@link GenUniqueField}
     * @param configMap   {@link Map<String,String>}
     * @return {@link String}
     */
    public static void addUniqueFieldConfigMap(GenUniqueField uniqueField, Map<String, String> configMap) {
        configMap.put("${uniqueFields}", uniqueField.getUniqueFields() == null ? null : JSON.toJSONString(uniqueField.getUniqueFields()));
        configMap.put("${checkUniqueType}", (uniqueField.getCheckUniqueType() == null || uniqueField.getUniqueFields() == null) ? null : String.valueOf(uniqueField.getCheckUniqueType()));
        List<String> uniqueFieldList = uniqueField.getUniqueFields();
        configMap.put("${uniqueLastField}", uniqueFieldList.get(uniqueFieldList.size() - 1));
        List<String> underlineCaseUniqueFields = uniqueFieldList.stream().map(r -> StrUtil.toUnderlineCase(r)).collect(Collectors.toList());
        configMap.put("${uniqueFieldsUpper}", StrUtil.join("_", underlineCaseUniqueFields).toUpperCase());
        List<String> uniqueFieldsUpperFirst = uniqueFieldList.stream().map(r -> StrUtil.upperFirst(r)).collect(Collectors.toList());
        configMap.put("${uniqueFieldsUpperFirst}", JSON.toJSONString(uniqueFieldsUpperFirst));
        configMap.put("${uniqueFieldsUpperFirstCombine}", StrUtil.join("", uniqueFieldsUpperFirst));
        initPropertyUniqueField(configMap);
    }

    /**
     * 获取表配置
     *
     * @return
     */
    public static Map<String, String> getTableConfigMap(Map<String, String> tableConfigMap) {
        return MapUtils.defaultNullValue(tableConfigMap);
    }

    /**
     * 获取实体地址
     *
     * @return
     */
    public static String getEntityInputPath(String entityInputPath, String entityName) {
        return entityInputPath + entityName + ".java";
    }

    /**
     * 获取mapperXml地址
     *
     * @return
     */
    public static String getMapperXmlInputPath(String entityName, Map<String, String> abstractPathMap) {
        return GeneratorUtils.getResultPath(GenTypeEnum.MAPPER_XML.getValue(), entityName, abstractPathMap);
    }
}