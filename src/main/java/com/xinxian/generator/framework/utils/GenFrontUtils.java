package com.xinxian.generator.framework.utils;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.enums.CreateOptionSubHeaderEnum;
import com.xinxian.generator.framework.enums.FrontComponentTypeEnum;
import com.xinxian.generator.framework.enums.FrontElementTypeEnum;
import com.xinxian.generator.framework.enums.FrontSubElementTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.constant.GenFrontConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.xinxian.generator.framework.enums.FrontComponentTypeEnum.*;

/**
 * @ClassName GenFrontUtils
 * @Description 前端工具类
 * @Author lmy
 * @Date 2023/4/9 15:42
 */
public class GenFrontUtils {

    public static final Integer frontTypeCreate = 1;
    public static final Integer frontTypeIndex = 2;
    public static String horizontalLine = "-";
    public static String colon = ":";
    public static Integer timeGroupValueFormat = 1;
    private static final Set<String> textareaPropertySet = new HashSet(Arrays.asList("content", "remark"));

    /**
     * 获取前端组件模板
     *
     * @param entityProperty {@link }
     * @return {@link String}
     */
    public static String getComponentTemplate(EntityProperty entityProperty, Integer frontType, Map<String, String> configMap, String mapKey) {
        FrontComponentTypeEnum componentTypeEnum = getFrontComponentTypeEnum(entityProperty, frontType);
        String template = "";
        /* create */
        if (Objects.equals(frontType, frontTypeCreate)) {
            template = getCreateComponentTemplate(entityProperty, componentTypeEnum, configMap, mapKey);
            /* index */
        } else if (Objects.equals(frontType, frontTypeIndex)) {
            template = getIndexComponentTemplate(entityProperty, componentTypeEnum, configMap, mapKey);
        }
        return template;
    }

    /**
     * 获取create前端组件模板
     *
     * @param entityProperty {@link }
     * @return {@link String}
     */
    public static String getCreateComponentTemplate(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum, Map<String, String> configMap, String mapKey) {
        /* 获取模板 */
        String template = GenFrontConstant.createTemplateMap.get(componentTypeEnum + mapKey);
        /* 前置处理 拓展值 */
        String expansionHeader = getCreateExpansionHeader(entityProperty, componentTypeEnum);
        String expansionSubHeader = getCreateExpansionSubHeader(entityProperty, componentTypeEnum, configMap);
        String expansionContent = getCreateExpansionContent(entityProperty, componentTypeEnum);
        /* 模板替换 */
        template = template.replace("${expansionHeader}", expansionHeader);
        template = template.replace("${expansionSubHeader}", expansionSubHeader);
        template = template.replace("${expansionContent}", expansionContent);
        /* 后置处理 */
        template = createAfterProcess(template, entityProperty, componentTypeEnum);
        return template;
    }

    /**
     * 获取index前端组件模板
     *
     * @param entityProperty {@link }
     * @return {@link String}
     */
    public static String getIndexComponentTemplate(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum, Map<String, String> configMap, String mapKey) {
        /* 获取模板 */
        String template = GenFrontConstant.indexTemplateMap.get(componentTypeEnum + mapKey);
        if (StringUtils.isEmpty(template)) {
            return "";
        }
        /* 前置处理 拓展值 */
        String expansionHeader = getIndexExpansionHeader(entityProperty, componentTypeEnum, configMap);
        String expansionSubHeader = getIndexExpansionSubHeader(entityProperty, componentTypeEnum, configMap);
        String expansionContent = getIndexExpansionContent(entityProperty, componentTypeEnum);
        /* 模板替换 */
        template = template.replace("${expansionHeader}", expansionHeader);
        template = template.replace("${expansionSubHeader}", expansionSubHeader);
        template = template.replace("${expansionContent}", expansionContent);
        /* 后置处理 */
        template = indexAfterProcess(template, entityProperty, componentTypeEnum);
        return template;
    }

    /**
     * 获取index拓展子头
     *
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @param configMap         {@link Map<String,String>}
     * @return {@link String}
     */
    private static String getIndexExpansionSubHeader(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum, Map<String, String> configMap) {
        String genText = "";
        switch (componentTypeEnum) {
            case INPUT:
                break;
            case TIME:
                break;
            default:
                break;
        }
        return genText;
    }

    /**
     * 后置处理
     *
     * @param template          {@link String}
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String createAfterProcess(String template, EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum) {
        template = commonAfterProcess(template, entityProperty, componentTypeEnum);
        return template;
    }

    /**
     * 后置处理
     *
     * @param template          {@link String}
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String indexAfterProcess(String template, EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum) {
        template = commonAfterProcess(template, entityProperty, componentTypeEnum);
        return template;
    }

    /**
     * 通用后置处理
     *
     * @param template          {@link String}
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String commonAfterProcess(String template, EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum) {
        return template;
    }

    /**
     * 获取create拓展头
     *
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String getCreateExpansionHeader(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum) {
        String result = "";
        return result;
    }

    /**
     * 获取index拓展头
     *
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String getIndexExpansionHeader(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum, Map<String, String> configMap) {
        String mapKey = configMap.get("mapKey");
        FrontElementTypeEnum frontElementTypeEnum = FrontElementTypeEnum.getInstanceByKey(mapKey);
        String indexExpansionHeaderSubElement = configMap.get(GenConstant.keyIndexExpansionHeaderSubElement);
        String result = "";
        if (StringUtils.isNotEmpty(indexExpansionHeaderSubElement)) {
            List<Integer> subElementList = JSON.parseArray(indexExpansionHeaderSubElement, Integer.class);
            for (Integer item : subElementList) {
                FrontSubElementTypeEnum frontSubElementTypeEnum = FrontSubElementTypeEnum.toEnum(item);
                result += GenFrontConstant.otherInfoMap.getOrDefault(componentTypeEnum + frontElementTypeEnum.name() + frontSubElementTypeEnum.name(), "");
            }
        }
        return result;
    }

    /**
     * 获取create拓展子头
     *
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String getCreateExpansionSubHeader(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum, Map<String, String> configMap) {
        String genText = "";
        switch (componentTypeEnum) {
            case INPUT:
                genText = genCreateOptionSubHeaderInput(configMap, entityProperty);
                break;
            case TIME:
                genText = genCreateOptionSubHeaderTime(configMap, entityProperty);
                break;
            default:
                break;
        }
        return genText;
    }

    /**
     * 生成创建文本子头可选项
     *
     * @param configMap      {@link Map}
     * @param entityProperty {@link EntityProperty}
     * @return {@link String}
     */
    private static String genCreateOptionSubHeaderInput(Map<String, String> configMap, EntityProperty entityProperty) {
        String optionSubHeader = configMap.get("createOptionSubHeader");
        Map<String, List<String>> optionSubHeaderMap = JSON.parseObject(optionSubHeader, Map.class);
        List<String> optionList = optionSubHeaderMap == null ? null : optionSubHeaderMap.get(entityProperty.getProperty());
        /* 默认必选值处理 */
        String genText = genCreateOptionSubHeaderInputRequire(optionList, entityProperty);
        StringBuilder stringBuilder = new StringBuilder(genText);
        /* 可选项处理 */
        if (CollectionUtils.isNotEmpty(optionList)) {
            for (String optionKey : optionList) {
                CreateOptionSubHeaderEnum createOptionSubHeaderEnum = CreateOptionSubHeaderEnum.toEnum(optionKey);
                if (StringUtils.isNotBlank(createOptionSubHeaderEnum.getContent())) {
                    stringBuilder.append(createOptionSubHeaderEnum.getContent());
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 文本默认必选值处理
     *
     * @param optionList     {@link List}
     * @param entityProperty {@link EntityProperty}
     * @return {@link String}
     */
    private static String genCreateOptionSubHeaderInputRequire(List<String> optionList, EntityProperty entityProperty) {
        StringBuilder stringBuilder = new StringBuilder();
        /* 文本格式化默认值处理 */
        List<CreateOptionSubHeaderEnum> requireOptionSubHeaderEnums = CreateOptionSubHeaderEnum.getRequireByType(INPUT.getValue());
        Map<Integer, List<CreateOptionSubHeaderEnum>> groupMap = requireOptionSubHeaderEnums.stream().collect(Collectors.groupingBy(CreateOptionSubHeaderEnum::getGroupId));
        for (Map.Entry<Integer, List<CreateOptionSubHeaderEnum>> entry : groupMap.entrySet()) {
            List<String> valueList = entry.getValue().stream().map(CreateOptionSubHeaderEnum::getValue).collect(Collectors.toList());
            /* 有交集，即必选项有值 */
            if (CollectionUtils.isNotEmpty(optionList) && CollectionUtils.isNotEmpty(ListUtils.intersection(optionList, valueList))) {
                continue;
            }
            /* 默认使用第一个 */
            stringBuilder.append(entry.getValue().get(0).getContent());
        }
        /* 大文本 */
        if (textareaPropertySet.contains(entityProperty.getProperty())) {
            stringBuilder.append(CreateOptionSubHeaderEnum.INPUT_TEXTAREA.getContent());
        }
        return stringBuilder.toString();
    }

    /**
     * 生成创建时间子头可选项
     *
     * @param configMap      {@link Map<String, String>}
     * @param entityProperty {@link EntityProperty}
     * @return {@link String}
     */
    private static String genCreateOptionSubHeaderTime(Map<String, String> configMap, EntityProperty entityProperty) {
        String optionSubHeader = configMap.get("createOptionSubHeader");
        Map<String, List<String>> optionSubHeaderMap = JSON.parseObject(optionSubHeader, Map.class);
        List<String> optionList = optionSubHeaderMap == null ? null : optionSubHeaderMap.get(entityProperty.getProperty());
        /* 默认必选值处理 */
        String genText = genCreateOptionSubHeaderTimeRequire(optionList, entityProperty);
        StringBuilder stringBuilder = new StringBuilder(genText);
        /* 可选项处理 */
        if (CollectionUtils.isNotEmpty(optionList)) {
            for (String optionKey : optionList) {
                CreateOptionSubHeaderEnum createOptionSubHeaderEnum = CreateOptionSubHeaderEnum.toEnum(optionKey);
                if (StringUtils.isNotBlank(createOptionSubHeaderEnum.getContent())) {
                    stringBuilder.append(createOptionSubHeaderEnum.getContent());
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 默认必选值处理
     *
     * @param optionList     {@link List<String>}
     * @param entityProperty {@link EntityProperty}
     * @return {@link String}
     */
    private static String genCreateOptionSubHeaderTimeRequire(List<String> optionList, EntityProperty entityProperty) {
        StringBuilder stringBuilder = new StringBuilder();
        /* 时间格式化默认值处理 */
        List<CreateOptionSubHeaderEnum> requireOptionSubHeaderEnums = CreateOptionSubHeaderEnum.getRequireByType(TIME.getValue());
        Map<Integer, List<CreateOptionSubHeaderEnum>> groupMap = requireOptionSubHeaderEnums.stream().collect(Collectors.groupingBy(CreateOptionSubHeaderEnum::getGroupId));
        for (Map.Entry<Integer, List<CreateOptionSubHeaderEnum>> entry : groupMap.entrySet()) {
            List<String> valueList = entry.getValue().stream().map(CreateOptionSubHeaderEnum::getValue).collect(Collectors.toList());
            /* 有交集，即必选项有值 */
            if (CollectionUtils.isNotEmpty(optionList) && CollectionUtils.isNotEmpty(ListUtils.intersection(optionList, valueList))) {
                continue;
            }
            if (Objects.equals(entry.getKey(), timeGroupValueFormat)) {
                if ((entityProperty.getProperty().endsWith("Date"))) {
                    stringBuilder.append(CreateOptionSubHeaderEnum.TIME_DATE_VALUE_FORMAT.getContent());
                } else {
                    stringBuilder.append(CreateOptionSubHeaderEnum.TIME_DATE_TIME_VALUE_FORMAT.getContent());
                }
            } else {
                /* 默认使用第一个 */
                stringBuilder.append(entry.getValue().get(0).getContent());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取拓展内容
     *
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String getCreateExpansionContent(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum) {
        String template = GenFrontConstant.createContentTemplateMap.getOrDefault(componentTypeEnum, "");
        if (StringUtils.isEmpty(template)) {
            return "";
        }
        String genText = "";
        switch (componentTypeEnum) {
            case SELECT:
                genText = genExpansionContentInSelect(template, entityProperty, false);
                break;
            default:
                break;
        }
        return genText;
    }

    /**
     * 获取拓展内容
     *
     * @param entityProperty    {@link EntityProperty}
     * @param componentTypeEnum {@link FrontComponentTypeEnum}
     * @return {@link String}
     */
    private static String getIndexExpansionContent(EntityProperty entityProperty, FrontComponentTypeEnum componentTypeEnum) {
        String template = GenFrontConstant.indexContentTemplateMap.getOrDefault(componentTypeEnum, "");
        if (StringUtils.isEmpty(template)) {
            return "";
        }
        String genText = "";
        switch (componentTypeEnum) {
            case SELECT:
                genText = genExpansionContentInSelect(template, entityProperty, true);
                break;
            default:
                break;
        }
        return genText;
    }

    /**
     * 下拉组件内容
     *
     * @param template       {@link String}
     * @param entityProperty {@link EntityProperty}
     * @return {@link String}
     */
    public static String genExpansionContentInSelect(String template, EntityProperty entityProperty, Boolean processNumeric) {
        List<Pair<String, String>> descItemList = getDescEnumList(entityProperty.getDesc());
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<String, String> labelAndValue : descItemList) {
            if (processNumeric && GeneratorUtils.isNumeric(entityProperty.getType())) {
                template = GeneratorUtils.processNumeric(template, "{{itemValue}}");
            }
            String replace = replaceLabelAndValue(template, labelAndValue);
            stringBuilder.append(replace);
        }
        return stringBuilder.toString();
    }

    /**
     * 替换标签和值
     *
     * @param template      {@link String}
     * @param labelAndValue {@link Pair<String, String>}
     * @return {@link String}
     */
    public static String replaceLabelAndValue(String template, Pair<String, String> labelAndValue) {
        String upperItemLabel = labelAndValue.getLeft().toUpperCase();
        if (StrUtils.isChinese(labelAndValue.getLeft())) {
            upperItemLabel = StrUtils.toPinyinUnderlineCase(labelAndValue.getLeft().replace("/", ""));
        }
        String replace = template.replace("{{itemLabel}}", labelAndValue.getLeft()).replace("{{itemValue}}", labelAndValue.getRight())
                .replace("{{upperItemLabel}}", upperItemLabel);
        return replace;
    }

    /**
     * 获取枚举名称和内容集合
     *
     * @param desc {@link String}
     * @return {@link List<String>}
     */
    private static List<Pair<String, String>> getDescEnumList(String desc) {
        String[] descItem = GeneratorUtils.formatDesc(desc).split(" ");
        List<Pair<String, String>> labelAndValueList = Lists.newArrayList();
        if (desc.contains("1" + horizontalLine)) {
            labelAndValueList = getLabelAndValueList(descItem, horizontalLine);
        } else if (desc.contains("1" + colon)) {
            labelAndValueList = getLabelAndValueList(descItem, colon);
        }
        return labelAndValueList;
    }

    /**
     * 获取枚举名称和内容集合
     *
     * @param descItem {@link String[]}
     * @return {@link List<String>}
     */
    private static List<Pair<String, String>> getLabelAndValueList(String[] descItem, String symbol) {
        List<Pair<String, String>> labelAndValueList = Lists.newArrayList();
        for (int i = 1; i < descItem.length; i++) {
            String item = descItem[i];
            if (!item.contains(symbol)) {
                continue;
            }
            String[] labelAndValue = item.split(symbol);
            labelAndValueList.add(Pair.of(labelAndValue[1].replace(",", "").replace(")", "").replace("）", "").trim(), labelAndValue[0].trim()));
        }
        return labelAndValueList;
    }

    /**
     * 获取对应的前端组件
     * frontType:1-create 2-index
     *
     * @param entityProperty {@link EntityProperty}
     * @return {@link FrontComponentTypeEnum}
     */
    public static FrontComponentTypeEnum getFrontComponentTypeEnum(EntityProperty entityProperty, Integer frontType) {
        /* 默认文本 */
        FrontComponentTypeEnum componentTypeEnum = INPUT;
        String property = entityProperty.getProperty();
        String type = entityProperty.getType();
        String desc = entityProperty.getDesc();
        /* create&index */
        if (GeneratorUtils.getIsTime(property)) {
            componentTypeEnum = TIME;
        }
        if (GeneratorUtils.getIsEnum(desc, type)) {
            componentTypeEnum = SELECT;
        }
        return componentTypeEnum;
    }

    /**
     * 处理格式化检查
     *
     * @param generatorText {@link }
     * @param configMap     {@link }
     * @return {@link String}
     */
    public static String processFormatCheck(String generatorText, Map<String, String> configMap) {
        /* 连续空行 */
        generatorText = generatorText.replace("\n\n\n", "\n\n");
        return generatorText;
    }
}
