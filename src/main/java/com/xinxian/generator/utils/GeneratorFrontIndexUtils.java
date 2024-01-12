package com.xinxian.generator.utils;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenQueryField;
import com.xinxian.generator.framework.enums.FrontElementTypeEnum;
import com.xinxian.generator.framework.enums.FrontSubElementTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GenFrontUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @ClassName GeneratorServiceImplUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorFrontIndexUtils {

    private static final Set<String> omitFieldSet = new HashSet(Arrays.asList("id", "createUserId", "updateUserId", "updateTime", "updateBy", "status"));
    private static final Pattern elFromPattern = Pattern.compile("(      <el-form([\\s\\S]*)      </el-form>\n)");
    private static final String queryFieldFilterFormTemplate = "    {{property}}: \"\"";
    private static final String queryFieldOnResetDebounceTemplate = "    filterForm.{{property}} = \"\";";
    private static final String queryFieldMethodOnResetDebounceTemplate =
                    "/**\n" +
                    " * 【通用】重置按钮 触发\n" +
                    " */\n" +
                    "function onResetDebounce() {\n" +
                    "    paginationOption.pageSize = 20;\n" +
                    "    paginationOption.page = 1;\n" +
                    "    refurbishData();\n" +
                    "}";


    /**
     * 后处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        try {
            LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
            GeneratorUtils.processRelationField(propertyMap, configMap, true);
            Class clazz = GeneratorUtils.getClass(configMap);
            generatorText = processElTableColumn(generatorText, propertyMap, clazz);
            generatorText = processOnFormatMethod(generatorText, propertyMap);
            generatorText = processQueryField(generatorText, configMap);
            generatorText = GenFrontUtils.processFormatCheck(generatorText, configMap);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return generatorText;
    }

    /**
     * 处理格式化方法
     *
     * @param text        {@link String}
     * @param propertyMap {@link LinkedHashMap<String, EntityProperty>}
     * @return {@link String}
     */
    private static String processOnFormatMethod(String text, LinkedHashMap<String, EntityProperty> propertyMap) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("separator", "\n");
        configMap.put("mapKey", "${onFormatMethod}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template", "        <el-table-column label=\"{{desc}}\" align=\"center\" prop=\"{{property}}\" />");
        text = GeneratorUtils.processPart(text, propertyMap, GenFrontUtils.frontTypeIndex, configMap, omitFieldSet);
        return text;
    }

    /**
     * 处理ElTableColumn
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,EntityProperty>}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processElTableColumn(String text, LinkedHashMap<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("separator", "\n");
        configMap.put("mapKey", "${el-table-column}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template", "        <el-table-column label=\"{{desc}}\" align=\"center\" prop=\"{{property}}\" />");
        text = GeneratorUtils.processPart(text, propertyMap, GenFrontUtils.frontTypeIndex, configMap, omitFieldSet);
        return text;
    }

    /**
     * 处理QueryField
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String,String>}
     * @return {@link String}
     */
    public static String processQueryField(String text, Map<String, String> configMap) {
        if (StringUtils.isEmpty(configMap.get("queryFieldList"))) {
            text = GeneratorUtils.replaceEmpty(text, elFromPattern);
            text = text.replace("    : \"\"\n", "");
            text = text.replace("const onSearchDebounce = _.debounce(onSearchClick, 500);\n", "");
            text = text.replace("import _ from \"lodash\";\n", "");
            text = text.replace(FrontElementTypeEnum.FORM_ITEM.getKey() + "\n", "");
            text = text.replace(GenConstant.keyQueryFieldFilterForm + GenConstant.ENTER, "");
            text = text.replace(GenConstant.keyQueryFieldOnResetDebounce + GenConstant.ENTER, "");
            text = text.replace(queryFieldMethodOnResetDebounceTemplate + GenConstant.ENTER, "");
        } else {
            /* processQueryFieldFormItem */
            List<GenQueryField> queryFieldList = JSON.parseArray(configMap.get("queryFieldList"), GenQueryField.class);
            LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.queryFieldConverterToProperty(queryFieldList);
            text = processQueryFieldFormItem(text, configMap, propertyMap);
            /* processFilterForm */
            text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyQueryFieldFilterForm, null, configMap, queryFieldFilterFormTemplate, "," + GenConstant.ENTER);
            /* processOnResetDebounce */
            text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.keyQueryFieldOnResetDebounce, null, configMap, queryFieldOnResetDebounceTemplate);
        }
        return text;
    }

    /**
     * 处理QueryField
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String,String>}
     * @return {@link String}
     */
    public static String processQueryFieldFormItem(String text, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        configMap.put(GenConstant.keyIndexExpansionHeaderSubElement, JSON.toJSONString(Arrays.asList(FrontSubElementTypeEnum.LABEL_WIDTH.getValue())));
        text = GeneratorUtils.processCommonItem(text, propertyMap, FrontElementTypeEnum.FORM_ITEM.getKey(), GenFrontUtils.frontTypeIndex, omitFieldSet, configMap);
        configMap.remove(GenConstant.keyIndexExpansionHeaderSubElement);
        return text;
    }
}
