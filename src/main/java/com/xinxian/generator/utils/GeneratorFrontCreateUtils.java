package com.xinxian.generator.utils;

import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.utils.GenFrontUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;

import java.util.*;

/**
 * @ClassName GeneratorServiceImplUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorFrontCreateUtils {

    private static final Set<String> omitFieldSet = new HashSet(Arrays.asList("id", "createUserId", "updateUserId", "createTime", "updateTime", "createBy", "updateBy", "status"));
    private static final Set<String> queryFormOmitFieldSet = new HashSet(Arrays.asList("createUserId", "updateUserId", "createTime", "updateTime", "createBy", "updateBy", "status"));

    /**
     * 后处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        try {
            LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
            LinkedHashMap<String, EntityProperty> allPropertyMap = GeneratorUtils.getPropertyMapComplete(configMap);
            GeneratorUtils.processRelationField(propertyMap, configMap, true);
            Class clazz = GeneratorUtils.getClass(configMap);
            generatorText = processElFormItem(generatorText, propertyMap, clazz);
            generatorText = processElFormRules(generatorText, propertyMap, clazz);
            generatorText = processQueryForm(generatorText, propertyMap, clazz);
            generatorText = processAssignQueryForm(generatorText, allPropertyMap, clazz);
            generatorText = GenFrontUtils.processFormatCheck(generatorText, configMap);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return generatorText;
    }

    /**
     * 处理processElFormItem
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,EntityProperty>}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processElFormItem(String text, LinkedHashMap<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = GeneratorUtils.getOneLinePartConfigMap("${el-form-item}", null, "\n");
        text = GeneratorUtils.processPart(text, propertyMap, GenFrontUtils.frontTypeCreate, configMap, omitFieldSet);
        return text;
    }

    /**
     * 处理ElFormRules
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,EntityProperty>}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processElFormRules(String text, LinkedHashMap<String, EntityProperty> propertyMap, Class clazz) {
        String template = "    {{property}}: [{ required: true, message: \"请输入{{desc}}\", trigger: \"blur\" }]";
        Map<String, String> configMap = GeneratorUtils.getOneLinePartConfigMap("${elFormRules}", template, ",\n");
        text = GeneratorUtils.processPart(text, propertyMap, GenFrontUtils.frontTypeCreate, configMap, omitFieldSet);
        return text;
    }

    /**
     * 处理QueryForm
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,EntityProperty>}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processQueryForm(String text, LinkedHashMap<String, EntityProperty> propertyMap, Class clazz) {
        String template = "    {{property}}: \"\"";
        Map<String, String> configMap = GeneratorUtils.getOneLinePartConfigMap("${queryForm}", template, ",\n");
        text = GeneratorUtils.processPart(text, propertyMap, GenFrontUtils.frontTypeCreate, configMap, queryFormOmitFieldSet);
        return text;
    }

    /**
     * 处理AssignQueryForm
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,EntityProperty>}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processAssignQueryForm(String text, LinkedHashMap<String, EntityProperty> propertyMap, Class clazz) {
        String template = "        {{property}}: data.{{property}}";
        Map<String, String> configMap = GeneratorUtils.getOneLinePartConfigMap("${assignQueryForm}", template, ",\n");
        text = GeneratorUtils.processPart(text, propertyMap, GenFrontUtils.frontTypeCreate, configMap, queryFormOmitFieldSet);
        return text;
    }
}
