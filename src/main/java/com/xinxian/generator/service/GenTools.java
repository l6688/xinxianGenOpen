package com.xinxian.generator.service;

import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.constant.GenConfigConstant;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.constant.GenToolsConstant;
import com.xinxian.generator.framework.utils.GeneratorUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenSmallTools
 * @Description 生成工具-自动生成模板
 * @Author lmy
 * @Date 2023/5/14 05:37
 */
public class GenTools {

    public static final String functionQueryField = "queryField";
    public static final String functionRelationTable = "relationTable";
    public static final String functionUniqueField = "uniqueField";
    public static final String functionCheckParam = "checkParam";
    public static final String functionGeneratorConfig = "generatorConfig";
    private static final List<String> templateQueryFieldList = Arrays.asList(GenToolsConstant.TEMPLATE_UTILS_CLASS,
            GenToolsConstant.TEMPLATE_PROPERTY, GenConstant.GEN_CONSTANT_CLASS, GenToolsConstant.TEMPLATE_PROPERTY_KEY,
            GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_REMOVE_REPLACE_KEY, GenToolsConstant.TEMPLATE_TEMPLATE_CLASS,
            GenToolsConstant.TEMPLATE_REPLACE_KEY, GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_ONE_TO_MANY_INPUT,
            GenToolsConstant.TEMPLATE_ONE_TO_MANY_PROCESS
    );
    private static final List<String> templateRelationTableList = Arrays.asList(GenToolsConstant.TEMPLATE_UTILS_CLASS,
            GenToolsConstant.TEMPLATE_PROPERTY, GenConstant.GEN_CONSTANT_CLASS, GenToolsConstant.TEMPLATE_PROPERTY_KEY,
            GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_PUT_MAP_RELATION, GenToolsConstant.TEMPLATE_REMOVE_REPLACE_KEY, GenToolsConstant.TEMPLATE_TEMPLATE_CLASS,
            GenToolsConstant.TEMPLATE_REPLACE_KEY, GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_ONE_TO_MANY_INPUT_RELATION,
            GenToolsConstant.TEMPLATE_ONE_TO_MANY_PROCESS_RELATION
    );
    private static final List<String> templateUniqueFieldList = Arrays.asList(GenToolsConstant.TEMPLATE_UTILS_CLASS,
            GenToolsConstant.TEMPLATE_PROPERTY, GenConstant.GEN_CONSTANT_CLASS, GenToolsConstant.TEMPLATE_PROPERTY_KEY,
            GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_REMOVE_REPLACE_KEY, GenToolsConstant.TEMPLATE_TEMPLATE_CLASS,
            GenToolsConstant.TEMPLATE_REPLACE_KEY, GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_ONE_TO_MANY_INPUT_UNIQUE,
            GenToolsConstant.TEMPLATE_BUILDER, GenToolsConstant.TEMPLATE_ONE_TO_MANY_PROCESS_UNIQUE, GenToolsConstant.TEMPLATE_ONE_TO_MANY_REPLACE_UNIQUE
    );
    private static final List<String> templateGeneratorConfigList = Arrays.asList(GenToolsConstant.TEMPLATE_UTILS_CLASS,
            GenToolsConstant.TEMPLATE_PROPERTY, GenConstant.GEN_CONSTANT_CLASS, GenToolsConstant.TEMPLATE_PROPERTY_KEY,
            GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_REMOVE_REPLACE_KEY, GenToolsConstant.TEMPLATE_TEMPLATE_CLASS,
            GenToolsConstant.TEMPLATE_REPLACE_KEY, GenToolsConstant.TEMPLATE_UTILS_CLASS, GenToolsConstant.TEMPLATE_ONE_TO_MANY_INPUT_UNIQUE,
            GenToolsConstant.TEMPLATE_BUILDER, GenToolsConstant.TEMPLATE_ONE_TO_MANY_PROCESS_UNIQUE, GenToolsConstant.TEMPLATE_ONE_TO_MANY_REPLACE_UNIQUE
    );

    /* 功能和模版集合映射 */
    private static Map<String, List<String>> functionTemplateMap = new HashMap() {
        {
            put(functionQueryField, templateQueryFieldList);
            put(functionRelationTable, templateRelationTableList);
            put(functionUniqueField, templateUniqueFieldList);
            put(functionCheckParam, templateQueryFieldList);
            put(functionGeneratorConfig, templateGeneratorConfigList);
        }
    };


    /**
     * 主入口
     * 1. 一变多
     *
     * @param args {@link String[]}
     */
    public static void main(String[] args) {
        Map<String, String> configMap = getConfigMap();
        //一变多
        oneToMany(configMap);
        //处理及输出
        GeneratorUtils.processByTemplate(configMap);
    }

    /**
     * 获取配置map
     *
     * @return {@link Map<String,String>}
     */
    private static Map<String, String> getConfigMap() {
        Map map = new HashMap();
        map.put(GenConstant.KEY_OUTPUT_PATH, GenConfigConstant.GEN_RESOURCE_PATH + "/efficiency/genToolsResult.txt");
        return map;
    }


    /**
     * 一变多的处理
     * 1. 原代码改为模板，写到对应Utils,并改为{{property}}，这种格式的变量
     * 2. 原代码处替换为一个变量，key写到GenConstant中
     * 3. 修改不存在时的替换为新变量
     * 4. 获取属性，转为propertyMap
     * 5. 处理对应变量，会拼接item，不用在外层循环
     *
     * @param configMap {@link Map<String,String>}
     */
    private static void oneToMany(Map<String, String> configMap) {
        /* 设置变量 */
        configMap.put(GenConstant.KEY_GEN_TYPE_METHOD, "oneToMany");
        String function = functionGeneratorConfig;
        String method = "table";
        String resourceCode =
                "        <table tableName=\"{{tableName}}\" domainObjectName=\"{{entityName}}\"\n" +
                        "               enableCountByExample=\"false\"\n" +
                        "               enableUpdateByExample=\"false\"\n" +
                        "               enableDeleteByExample=\"false\"\n" +
                        "               enableSelectByExample=\"false\"\n" +
                        "               selectByExampleQueryId=\"true\"\n" +
                        "               enableSelectByPrimaryKey=\"true\"\n" +
                        "               enableUpdateByPrimaryKey=\"true\"\n" +
                        "               enableDeleteByPrimaryKey=\"true\">\n" +
                        "            <generatedKey column=\"id\"  sqlStatement=\"JDBC\"/>\n" +
                        "        </table>";
        /* 选模板 */
        StringBuilder stringBuilder = new StringBuilder();
        List<String> templateList = functionTemplateMap.get(function);
        for (String template : templateList) {
            stringBuilder.append(template);
        }
        configMap.put(GenConstant.KEY_TEMPLATE, stringBuilder.toString());
        /* 写入config */
        configMap.put(GenToolsConstant.KEY_FUNCTION, function);
        configMap.put(GenToolsConstant.KEY_UPPER_FIRST_FUNCTION, StrUtil.upperFirst(function));
        configMap.put(GenToolsConstant.KEY_METHOD, method);
        configMap.put(GenToolsConstant.KEY_UPPER_FIRST_METHOD, StrUtil.upperFirst(method));
        configMap.put(GenToolsConstant.KEY_RESOURCE_CODE, resourceCode
                        /* queryField */
//                .replace(String.format("${%s}", function), "{{property}}")
//                .replace(String.format("${%sUpperFirst}", function), "{{upperFirstProperty}}")
                        /* relationTable */
//                .replace("${slaveEntityName}", "{{upperFirstProperty}}")
//                .replace("${shortSlaveEntityName}", "{{property}}")
//                .replace("${contentEntityName}", "{{upperFirstProperty}}")
//                .replace("${shortEntityName}", "{{property}}")
//                .replace("${slaveEntityDesc}", "{{desc}}")
//                .replace("slaveRecord", "{{property}}")
//                .replace("ContentId", "{{upperFirstRelationField}}")
//                .replace("records", "{{property}}List")
//                .replace("content", "{{property}}")
//                .replace("Content", "{{upperFirstProperty}}")
                        /* all */
                        .replace("\"", "\\\"")
                        .replace("\n", "")
        );
    }
}
