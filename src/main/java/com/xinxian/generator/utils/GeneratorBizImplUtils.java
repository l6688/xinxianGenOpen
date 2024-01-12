package com.xinxian.generator.utils;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenQueryField;
import com.xinxian.generator.framework.bean.GenRelationTable;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GenFunctionUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @ClassName GeneratorBizImplUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorBizImplUtils {

    public static List<String> notRelationTableList = Lists.newArrayList();
    public static List<String> notRelationType3List = Lists.newArrayList();
    public static List<String> notRelationType2List = Lists.newArrayList();
    public static List<String> notQueryFieldList = Lists.newArrayList();
    public static List<String> notCheckParamList = Lists.newArrayList();
    public static List<String> notQueryFieldMethodList = Arrays.asList();
    public static List<String> notCheckParamMethodList = Arrays.asList("checkAddParam");
    public static List<String> notRelationType2MethodList = Arrays.asList();
    public static List<String> notRelationType3MethodList = Arrays.asList();
    public static List<Pair<String, String>> notRelationType3PairList = Arrays.asList();
    private static final String queryFieldSetConditionTemplate = "        condition.set{{upperFirstProperty}}(StringUtil.convertQueryField(param.get{{upperFirstProperty}}()));";
    public static List<Pair<String, String>> queryFieldTypeChangePairList = Arrays.asList(Pair.of(queryFieldSetConditionTemplate,
            "        condition.set{{upperFirstProperty}}(param.get{{upperFirstProperty}}());"));
    private static final String relationTable2ImportTemplate = "import ${packageBase}.dao.entity.{{type}};\nimport ${packageBase}.service.{{type}}Service;";
    private static final String relationTable3ImportTemplate = "import ${packageBase}.converter.{{type}}Converter;\nimport ${packageBase}.dao.entity.{{type}};\nimport ${packageBase}.service.{{type}}Service;";
    private static final String relationTablePropertyTemplate = "    private final {{type}}Service {{property}}Service;";
    private static final String relationTableNeedDefineTemplate = "        Boolean need{{upperFirstProperty}} = true;";
    private static final String relationTable2NeedDefineListTemplate = "        Boolean need{{upperFirstProperty}} = true;";
    private static final String relationTable3NeedDefineListTemplate = "        Boolean need{{upperFirstProperty}} = false;";
    private static final String relationTableNeedCallTemplate = ", need{{upperFirstProperty}}";
    private static final String relationTable2AddTemplate = "        /* 增加{{desc}} */\n        {{type}} {{property}} = get{{upperFirstProperty}}(param);\n        {{property}}Service.insert({{property}});\n        record.set{{upperFirstRelationField}}({{property}}.getId());";
    private static final String relationTable3AddTemplate = "        /* 增加{{desc}} */\n        List<{{type}}> {{property}}List = {{type}}Converter.convertToList{{type}}(param.get{{upperFirstProperty}}List(), record);\n        {{property}}Service.batchInsert({{property}}List);";
    private static final String relationTable2UpdateTemplate = "        /* 修改{{desc}} */\n        {{type}} {{property}} = get{{upperFirstProperty}}(param);\n        {{property}}Service.updateByPrimaryKeySelective({{property}});";
    private static final String relationTable3UpdateTemplate = "        /* 修改{{desc}} */\n        List<{{upperFirstProperty}}> {{property}}List = {{upperFirstProperty}}Converter.convertToList{{upperFirstProperty}}(param.get{{upperFirstProperty}}List(), record);\n        {{upperFirstProperty}}Converter.operateAll({{property}}List, {{property}}Service, param);";
    private static final String relationTable2DeleteTemplate = "        /* 删除{{desc}} */\n        {{type}} {{property}} = get{{upperFirstProperty}}(param);\n        {{property}}Service.deleteByPrimaryKey({{property}});";
    private static final String relationTable3DeleteTemplate = "        /* 删除{{desc}} */\n        {{property}}Service.deleteByMainTableId(param.getId(), record);";
    private static final String relationTableDescribeTemplate = "     * @param need{{upperFirstProperty}}    {@link Boolean}";
    private static final String relationTableParamDefineTemplate = ", Boolean need{{upperFirstProperty}}";
    private static final String relationTableCallGetSlaveMapTemplate = "        Map<Long, List<{{upperFirstProperty}}>> {{property}}Map = get{{upperFirstProperty}}Map(Arrays.asList(record), need{{upperFirstProperty}});";
    private static final String relationTableCallGetSlaveMapListTemplate = "        Map<Long, List<{{upperFirstProperty}}>> {{property}}Map = get{{upperFirstProperty}}Map(records, need{{upperFirstProperty}});";
    private static final String relationTable2MethodSetSlaveTemplate = "    /**\n     * 设置{{desc}}\n     *\n     * @param result {@link ${entityName}Result}\n     * @return\n     */\n    private void set{{upperFirstProperty}}(${entityName}Result result) {\n        {{type}} {{property}} = {{property}}Service.selectByPrimaryKey(result.get{{upperFirstRelationField}}());\n        result.set{{upperFirstProperty}}({{property}}.get{{upperFirstProperty}}());\n    }\n";
    private static final String relationTable3MethodSetSlaveTemplate = "    /**\n     * 设置{{desc}}\n     *\n     * @param result {@link ${entityName}Result}\n     * @param {{property}}Map {@link Map<Long,List<{{upperFirstProperty}}>>}\n     * @return\n     */\n    private void set{{upperFirstProperty}}(${entityName}Result result, Map<Long, List<{{upperFirstProperty}}>> {{property}}Map) {\n        List<{{upperFirstProperty}}> {{property}}List = {{property}}Map.get(result.getId());\n        result.set{{upperFirstProperty}}List({{upperFirstProperty}}Converter.convertToList{{upperFirstProperty}}VO({{property}}List, result));\n    }\n";
    private static final String relationTable2CallSetSlaveTemplate = "        if (need{{upperFirstProperty}}) {\n            set{{upperFirstProperty}}(result);\n        }";
    private static final String relationTable3CallSetSlaveTemplate = "        if ({{property}}Map != null && !{{property}}Map.isEmpty()) {\n            set{{upperFirstProperty}}(result, {{property}}Map);\n        }";
    private static final String relationTable2DescribeSetPropertyTemplate = "     * @param need{{upperFirstProperty}}    {@link Boolean}";
    private static final String relationTable3DescribeSetPropertyTemplate = "     * @param {{property}}Map    {@link Map<Long, List<{{upperFirstProperty}}>>}";
    private static final String relationTable2ParamSetPropertyTemplate = ", Boolean need{{upperFirstProperty}}";
    private static final String relationTable3ParamSetPropertyTemplate = ", Map<Long, List<{{upperFirstProperty}}>> {{property}}Map";
    private static final String relationTable2CallSetPropertyTemplate = ", need{{upperFirstProperty}}";
    private static final String relationTable3CallSetPropertyTemplate = ", {{property}}Map";
    private static final String relationTable2MethodGetSlaveTemplate = "    /**\n     * 获取{{desc}}\n     *\n     * @param param {@link ${entityName}Param}\n     * @return {@link {{type}}}\n     */\n    private {{type}} get{{upperFirstProperty}}(${entityName}Param param) {\n        {{type}} {{property}} = {{type}}.builder()\n                .id(param.get{{upperFirstRelationField}}())\n                .{{property}}Type(CommonContentTypeEnum.${entityNameUpper}.getValue())\n                .updateUserId(param.getUpdateUserId())\n                .updateBy(param.getupdateBy())\n                .{{property}}(param.get{{upperFirstProperty}}())\n                .build();\n        return {{property}};\n    }\n";
    private static final String relationTable3MethodGetSlaveTemplate = "    /**\n     * 获取主表id和{{desc}}的map\n     *\n     * @param {{property}}List   {@link List<${entityName}>}\n     * @param need{{upperFirstProperty}}    {@link Boolean}\n     * @return {@link Map< Long,List<{{upperFirstProperty}}>>}\n     */\n    private Map<Long, List<{{upperFirstProperty}}>> get{{upperFirstProperty}}Map(List<${entityName}> {{property}}List, Boolean need{{upperFirstProperty}}) {\n        List<Long> ids = CollectionUtils.emptyIfNull({{property}}List).stream().filter(r -> r != null).map(${entityName}::getId).collect(Collectors.toList());\n        Map<Long, List<{{upperFirstProperty}}>> {{property}}Map = null;\n        if (need{{upperFirstProperty}}) {\n            List<{{upperFirstProperty}}> slaveEntityList = {{property}}Service.selectByMainTableIds(ids);\n            if (CollectionUtils.isNotEmpty(slaveEntityList)) {\n                {{property}}Map = slaveEntityList.stream().collect(Collectors.groupingBy({{upperFirstProperty}}::get{{upperFirstRelationField}}));\n            }\n        }\n        return {{property}}Map;\n    }\n";
    private static final String checkParamFieldsTemplate = "        ApiPreconditions.assertTrue(param.get{{upperFirstProperty}}() == null, ${errorCodeEnum}.{{upperProperty}}_CANNOT_NULL);";

    /* relationAndTemplate */
    private static Map<String, String> relationAndTemplateMap = new HashMap() {
        {
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableImport, relationTable2ImportTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableImport, relationTable3ImportTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableProperty, relationTablePropertyTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableProperty, relationTablePropertyTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableNeedDefine, relationTableNeedDefineTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableNeedDefine, relationTableNeedDefineTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableNeedDefineList, relationTable2NeedDefineListTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableNeedDefineList, relationTable3NeedDefineListTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableNeedCall, relationTableNeedCallTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableNeedCall, relationTableNeedCallTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTable2Add, relationTable2AddTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableAdd, relationTable3AddTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableUpdate, relationTable2UpdateTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableUpdate, relationTable3UpdateTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableDelete, relationTable2DeleteTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableDelete, relationTable3DeleteTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableDescribe, relationTableDescribeTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableDescribe, relationTableDescribeTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableParamDefine, relationTableParamDefineTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableParamDefine, relationTableParamDefineTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableCallGetSlaveMap, "");
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableCallGetSlaveMap, relationTableCallGetSlaveMapTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableCallGetSlaveMapList, "");
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableCallGetSlaveMapList, relationTableCallGetSlaveMapListTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableMethodSetSlave, relationTable2MethodSetSlaveTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableMethodSetSlave, relationTable3MethodSetSlaveTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableCallSetSlave, relationTable2CallSetSlaveTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableCallSetSlave, relationTable3CallSetSlaveTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableDescribeSetProperty, relationTable2DescribeSetPropertyTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableDescribeSetProperty, relationTable3DescribeSetPropertyTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableParamSetProperty, relationTable2ParamSetPropertyTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableParamSetProperty, relationTable3ParamSetPropertyTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableCallSetProperty, relationTable2CallSetPropertyTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableCallSetProperty, relationTable3CallSetPropertyTemplate);
            put(GeneratorUtils.relationType2 + GenConstant.keyRelationTableMethodGetSlave, relationTable2MethodGetSlaveTemplate);
            put(GeneratorUtils.relationType3 + GenConstant.keyRelationTableMethodGetSlave, relationTable3MethodGetSlaveTemplate);
        }
    };

    /* 方法调用要在字段前替换 */
    static {
        notRelationTableList.add(GenConstant.keyRelationTableImport + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableProperty + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableNeedDefine + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableNeedDefineList + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableNeedCall);
        notRelationTableList.add(GenConstant.keyRelationTableAdd + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTable2Add + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableUpdate + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableDelete + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableDescribe + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableParamDefine);
        notRelationTableList.add(GenConstant.keyRelationTableCallGetSlaveMap + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableCallGetSlaveMapList + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableMethodSetSlave + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableCallSetSlave + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableDescribeSetProperty + GenConstant.ENTER);
        notRelationTableList.add(GenConstant.keyRelationTableParamSetProperty);
        notRelationTableList.add(GenConstant.keyRelationTableCallSetProperty);
        notRelationTableList.add(GenConstant.keyRelationTableMethodGetSlave + GenConstant.ENTER);
        /* relationType2 content */
        notRelationType2List.add("import ${packageBase}.dto.enums.CommonContentTypeEnum;" + GenConstant.ENTER);
        notRelationType2List.add(GenConstant.keyRelationTable2Add + GenConstant.ENTER);
        /* relationType3 content */
        notRelationType3List.add(GenConstant.keyRelationTableCallGetSlaveMap + GenConstant.ENTER);
        notRelationType3List.add(GenConstant.keyRelationTableCallGetSlaveMapList + GenConstant.ENTER);
        notRelationType3List.add(GenConstant.keyRelationTableAdd + GenConstant.ENTER);
        notRelationTableList.addAll(notRelationType2List);
        notRelationTableList.addAll(notRelationType3List);
        /* 没有关键字查询 */
        notQueryFieldList.add("${queryFieldSetCondition}\n");
        /* 没有检查参数 */
        notCheckParamList.add("        checkAddParam(param);\n");
        notCheckParamList.add("        params.stream().forEach(param -> checkAddParam(param));\n");
        notCheckParamList.add(GenConstant.keyCheckParamFields + GenConstant.ENTER);

    }

    /**
     * 生成前的处理   beforeOrAfterProcess调用
     */
    public static String beforeProcess(String text, Map<String, String> configMap) {
        text = processQueryField(text, configMap);
        text = processCheckParamFields(text, configMap);
        /* 关系表 */
        if (GeneratorUtils.notRelationTable(configMap)) {
            text = GeneratorUtils.deleteUnUsedTextAndMethod(text, null, notRelationTableList, null);
        } else {
            if (!GeneratorUtils.relationType(configMap, GeneratorUtils.relationType2)) {
                text = GeneratorUtils.deleteUnUsedTextAndMethod(text, notRelationType2MethodList, notRelationType2List, null);
            }
            if (!GeneratorUtils.relationType(configMap, GeneratorUtils.relationType3)) {
                text = GeneratorUtils.deleteUnUsedTextAndMethod(text, notRelationType3MethodList, notRelationType3List, notRelationType3PairList);
            }
            text = processRelationTable(text, configMap);
        }
        /* 根据功能删除无用代码 */
        text = GenFunctionUtils.processUnUsed(text, configMap);
        return text;
    }

    /**
     * 处理关系表
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String,String>}
     * @return {@link String}
     */
    private static String processRelationTable(String text, Map<String, String> configMap) {
        configMap.put(GenConstant.keyTemplateMap, JSON.toJSONString(relationAndTemplateMap));
        List<GenRelationTable> relationTableList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyRelationTableList, GenRelationTable.class);
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.relationTableConverterToProperty(relationTableList);
        /* processImport */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableImport, null, configMap, null);
        /* processProperty */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableProperty, null, configMap, null);
        /* processNeedDefine */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableNeedDefine, null, configMap, null);
        /* processNeedDefineList */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableNeedDefineList, null, configMap, null);
        /* processNeedCall */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableNeedCall, null, configMap, null, "");
        /* processAdd */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableAdd, null, configMap, null);
        /* processAddRelation2 */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTable2Add, null, configMap, null);
        /* processUpdate */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableUpdate, null, configMap, null);
        /* processDelete */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableDelete, null, configMap, null);
        /* processDescribe */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableDescribe, null, configMap, null);
        /* processParamDefine */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableParamDefine, null, configMap, null, "");
        /* processCallGetSlaveMap */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableCallGetSlaveMap, null, configMap, null);
        /* processCallGetSlaveMapList */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableCallGetSlaveMapList, null, configMap, null);
        /* processMethodSetSlave */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableMethodSetSlave, null, configMap, null);
        /* processCallSetSlave */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableCallSetSlave, null, configMap, null);
        /* processDescribeSetProperty */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableDescribeSetProperty, null, configMap, null);
        /* processParamSetProperty */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableParamSetProperty, null, configMap, null, "");
        /* processCallSetProperty */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableCallSetProperty, null, configMap, null, "");
        /* processMethodGetSlave */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyRelationTableMethodGetSlave, null, configMap, null);
        return text;
    }

    /**
     * 生成完后的处理   beforeOrAfterProcess调用
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        String text = generatorText;
        /* 唯一 */
        if (StringUtils.isNotEmpty(configMap.get("${uniqueFields}"))) {
            text = processUnique(text, configMap);
        }
        return text;
    }

    /**
     * 处理前端查询
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    private static String processQueryField(String text, Map<String, String> configMap) {
        /* 不存在时处理 */
        if (StringUtils.isEmpty(configMap.get(GenConstant.keyQueryFieldList))) {
            text = GeneratorUtils.deleteUnUsedTextAndMethod(text, notQueryFieldMethodList, notQueryFieldList, null);
            return text;
        }
        List<GenQueryField> queryFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyQueryFieldList, GenQueryField.class);
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.queryFieldConverterToProperty(queryFieldList);
        /* processSetCondition */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyQueryFieldSetCondition, null, configMap, queryFieldSetConditionTemplate);
        /* processSetCondition数字类型特殊处理 */
        for (GenQueryField genQueryField : queryFieldList) {
            String queryField = genQueryField.getQueryField();
            LinkedHashMap<String, EntityProperty> entityPropertyMap = GeneratorUtils.getPropertyMap(configMap);
            if (entityPropertyMap.get(queryField) != null && !Objects.equals(entityPropertyMap.get(queryField).getType(), GeneratorUtils.typeString)) {
                for (Pair<String, String> pair : queryFieldTypeChangePairList) {
                    List<Pair<String, String>> replacePairList = Arrays.asList(Pair.of(GeneratorUtils.replaceProperty(pair.getLeft(), entityPropertyMap.get(queryField)), GeneratorUtils.replaceProperty(pair.getRight(), entityPropertyMap.get(queryField))));
                    text = GeneratorUtils.deleteUnUsedTextAndMethod(text, null, null, replacePairList);
                }
            }
        }
        return text;
    }

    /**
     * 处理检查参数
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    private static String processCheckParamFields(String text, Map<String, String> configMap) {
        /* 不存在时处理 */
        if (StringUtils.isEmpty(configMap.get(GenConstant.keyCheckParamFields))) {
            text = GeneratorUtils.deleteUnUsedTextAndMethod(text, notCheckParamMethodList, notCheckParamList, null);
            return text;
        }
        List<String> checkParamFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyCheckParamFields, String.class);
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.fieldListConverterToProperty(checkParamFieldList);
        /* processCheckParamFields */
        text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyCheckParamFields, null, configMap, checkParamFieldsTemplate);
        return text;
    }


    /**
     * 删除唯一检查
     *
     * @param text {@link String}
     * @return {@link String}
     */
    private static String processUnique(String text, Map<String, String> configMap) {
        text = text.replace("record.set(param.get());\n", "");
        text = text.replace(String.format("ApiPreconditions.assertTrue(StringUtils.isEmpty(param.get()), %s.TEMPLATE__CANNOT_NULL);", configMap.get("${errorCodeEnum}")), "return;");
        return text;
    }
}
