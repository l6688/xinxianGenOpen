package com.xinxian.generator.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenUniqueField;
import com.xinxian.generator.framework.enums.FrontComponentTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GenFrontUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import com.xinxian.generator.framework.utils.StrUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName GeneratorEnumUtils
 * @Description 自动生成工具包
 * @Author lmy
 * @Date 2023/04/19 23:00
 */
public class GeneratorEnumUtils {

    public static final String itemTemplate =
            "    /**\n" +
                    "     * {{itemLabel}}\n" +
                    "     */\n" +
                    "    {{upperItemLabel}}(${symbol}{{itemValue}}${symbol}, \"{{itemLabel}}\"),\n\n";
    private static final String queryFieldNotNullTemplate = "    ${queryFieldUpper}_CANNOT_NULL(\"{{codeNum}}\", \"{{desc}}不能为空\"),\n";
    private static final String duplicateTemplate = "    FIELD_${uniqueFieldsUpper}_DUPLICATE(\"{{codeNum}}\", \"${uniqueFieldsDesc}字段重复\"),\n";
    private static final String enumItemRegex = "([0-9a-zA-Z_]*)\\(([\"0-9a-zA-Z]*),(\\s)*\"(.*)\"\\);";
    private static Integer maxCodeNum = 10001;
    private static Set<String> nameSet = new HashSet<>();

    /**
     * 前置处理
     */
    public static String beforeProcess(String genText, Map<String, String> configMap) {
        return genText;
    }

    /**
     * 后置处理
     */
    public static String afterProcess(String genText, Map<String, String> configMap) {
        /* 生成枚举类 */
        genEnumClass(genText, configMap);
        /* 增加错误枚举 */
        String errorCode = genErrorCode(configMap);
        return errorCode;
    }

    /**
     * 增加错误枚举
     * 输入，处理，输出
     *
     * @param configMap {@link Map<String, String>}
     */
    private static String genErrorCode(Map<String, String> configMap) {
        /* 输入 */
        String sourcePath = configMap.get("${errorCodeEnumPath}");
        String source = FileUtil.readString(sourcePath, Charset.defaultCharset());
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
        /* 处理:解析，处理queryField，处理uniqueField */
        String lastCodeEnum = getLastCodeEnumAndMaxCodeNum(source);
        StringBuilder queryFieldStringBuilder = new StringBuilder();
        StringBuilder uniqueFieldStringBuilder = new StringBuilder();
        List<String> checkParamFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyCheckParamFields, String.class);
        if (CollectionUtils.isNotEmpty(checkParamFieldList)) {
            for (String item : checkParamFieldList) {
                item = removeSpecialCharacter(item);
                configMap.put("${queryField}", item);
                configMap.put("${queryFieldUpper}", StrUtil.toUnderlineCase(item).toUpperCase());
                queryFieldStringBuilder.append(getGenItemErrorCode("${queryFieldUpper}", "${queryField}", configMap, queryFieldNotNullTemplate, propertyMap));
            }
        }
        List<GenUniqueField> uniqueFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyUniqueFieldList, GenUniqueField.class);
        if (CollectionUtils.isNotEmpty(uniqueFieldList)) {
            for (GenUniqueField item : uniqueFieldList) {
                List<String> underlineCaseUniqueFields = item.getUniqueFields().stream().map(r -> StrUtil.toUnderlineCase(r)).collect(Collectors.toList());
                configMap.put("${uniqueFieldsUpper}", StrUtil.join("_", underlineCaseUniqueFields).toUpperCase());
                configMap.put("${uniqueLastField}", item.getUniqueFields().get(item.getUniqueFields().size() - 1));
                uniqueFieldStringBuilder.append(getGenItemErrorCode("${uniqueFieldsUpper}", "${uniqueLastField}", configMap, duplicateTemplate, propertyMap));
            }
        }
        /* 输出 */
        String genText = source;
        if (!queryFieldStringBuilder.toString().isEmpty() || !uniqueFieldStringBuilder.toString().isEmpty()) {
            genText = source.replace(lastCodeEnum, lastCodeEnum + queryFieldStringBuilder + uniqueFieldStringBuilder);
        }
        configMap.put("resultPath", configMap.get("${errorCodeEnumPath}"));
        return genText;
    }

    /**
     * 去除特殊字符
     *
     * @param text {@link String}
     * @return {@link String}
     */
    private static String removeSpecialCharacter(String text) {
        String newText = text.replace("，逗号分隔", "");
        newText = newText.replace("逗号分隔", "");
        return newText;
    }

    /**
     * 解析并获取最后一个错误枚举
     *
     * @param source {@link String}
     * @return {@link String}
     */
    public static String getLastCodeEnumAndMaxCodeNum(String source) {
        String lastCodeEnum = "";
        List<Map<Integer, String>> matchMapList = StrUtils.getMatchMapList(source, enumItemRegex, false, 4);
        for (int i = 0; i < matchMapList.size(); i++) {
            Map<Integer, String> currentMap = matchMapList.get(i);
            nameSet.add(currentMap.get(1));
            if (i == matchMapList.size() - 1) {
                lastCodeEnum = currentMap.get(0);
                maxCodeNum = Integer.valueOf(currentMap.get(2).replace("\"", ""));
            }
        }
        return lastCodeEnum;
    }

    /**
     * 获取最后一个错误枚举
     *
     * @param source {@link String}
     * @return {@link String}
     */
    public static String getLastCodeEnum(String source) {
        String lastCodeEnum = "";
        List<Map<Integer, String>> matchMapList = StrUtils.getMatchMapList(source, enumItemRegex, false, 4);
        for (int i = 0; i < matchMapList.size(); i++) {
            Map<Integer, String> currentMap = matchMapList.get(i);
            nameSet.add(currentMap.get(1));
            if (i == matchMapList.size() - 1) {
                lastCodeEnum = currentMap.get(0);
            }
        }
        return lastCodeEnum;
    }

    /**
     * 生成错误提示
     *
     * @param replaceKey  {@link String}
     * @param fieldKey    {@link String}
     * @param configMap   {@link Map<String, String>}
     * @param propertyMap {@link LinkedHashMap<String, EntityProperty>}
     * @return {@link String}
     */
    public static String getGenItemErrorCode(String replaceKey, String fieldKey, Map<String, String> configMap, String template,
                                             LinkedHashMap<String, EntityProperty> propertyMap) {
        String replaceKeyStr = configMap.get(replaceKey);
        if (StringUtils.isNotEmpty(replaceKeyStr)) {
            String field = configMap.get(fieldKey);
            HashMap<String, String> itemConfigMap = new HashMap<>();
            itemConfigMap.put("{{codeNum}}", String.valueOf(++maxCodeNum));
            String result = GeneratorUtils.replaceAllTemplate(template, configMap, propertyMap.get(field), itemConfigMap);
            List<Map<Integer, String>> resultMatchMapList = StrUtils.getMatchMapList(result, enumItemRegex, false, 4);
            if (CollectionUtils.isNotEmpty(resultMatchMapList) && !nameSet.contains(resultMatchMapList.get(0).get(1))) {
                nameSet.add(resultMatchMapList.get(0).get(1));
                return result;
            } else {
                --maxCodeNum;
            }
        }
        return "";
    }

    /**
     * 生成枚举类
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String, String>}
     */
    private static void genEnumClass(String genText, Map<String, String> configMap) {
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String genItemText = getEnumClass(genText, entry);
            if (StringUtils.isNotEmpty(genItemText)) {
                /* 写文件-输出 */
                String resultPath = GeneratorUtils.replaceItemTemplate(configMap.get("resultPath"), entry.getValue(), null);
                FileUtil.writeUtf8String(genItemText, resultPath);
            }
        }
    }

    /**
     * 获取生成的枚举类
     *
     * @param genText {@link String}
     * @param entry   {@link  Map.Entry<String, EntityProperty>}
     * @return {@link String}
     */
    private static String getEnumClass(String genText, Map.Entry<String, EntityProperty> entry) {
        FrontComponentTypeEnum componentTypeEnum = GenFrontUtils.getFrontComponentTypeEnum(entry.getValue(), null);
        if (Objects.equals(componentTypeEnum, FrontComponentTypeEnum.SELECT)) {
            EntityProperty entityProperty = entry.getValue();
            String itemEnum = GenFrontUtils.genExpansionContentInSelect(itemTemplate, entityProperty, false);
            HashMap<String, String> map = new HashMap<>();
            map.put("${itemEnum}", itemEnum);
            map.put("${symbol}", GeneratorUtils.isNumeric(entityProperty.getType()) ? "" : "\"");
            genText = GeneratorUtils.replaceItemTemplate(genText, entityProperty, map);
            return genText;
        }
        return null;
    }
}