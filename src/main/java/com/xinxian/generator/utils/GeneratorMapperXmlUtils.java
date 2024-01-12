package com.xinxian.generator.utils;

import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenQueryField;
import com.xinxian.generator.framework.bean.GenUniqueField;
import com.xinxian.generator.framework.enums.CheckUniqueTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName MybatisEntityUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorMapperXmlUtils {

    public static List<String> notUniqueFieldList = Lists.newArrayList();
    private static final Set omitFieldSet = new HashSet(Arrays.asList("id"));
    private static final Set omitSelectByConditionSet = new HashSet(Arrays.asList("id", "createTime", "updateTime"));
    private static final String selectMapKey = "${selectByCondition}";
    private static final String backQuote = "`";
    private static final String underline = "_";
    private static final String uniqueFieldDeleteTemplate = "        {{property}} = concat({{property}},DATE_FORMAT(now(),'%Y%m%d%H%i%S')),";
    private static final String slaveQueryMainIdsTemplate =
            "            <if test=\"${slaveRelationField}s != null and ${slaveRelationField}s.size() > 0\">\n" +
                    "                and ${slaveRelationFieldColumn} in\n" +
                    "                <foreach collection=\"${slaveRelationField}s\" item=\"record\" index=\"index\" separator=\",\" open=\"(\" close=\")\">\n" +
                    "                    #{record}\n" +
                    "                </foreach>\n" +
                    "            </if>";

    static {
        notUniqueFieldList.add(GenConstant.keyUniqueFieldDelete + GenConstant.ENTER);
    }

    /**
     * 处理MapperXml
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
        try {
            Class clazz = GeneratorUtils.getClass(configMap);
            generatorText = processResultMap(generatorText, propertyMap, clazz);
            generatorText = processBaseColumnList(generatorText, propertyMap, clazz);
            generatorText = processInsertField(generatorText, propertyMap, clazz);
            generatorText = processInsertValue(generatorText, propertyMap, clazz);
            generatorText = processUpdateByCondition(generatorText, propertyMap, clazz);
            generatorText = processUpdateByPrimaryKey(generatorText, propertyMap, clazz);
            generatorText = processBatchUpdate(generatorText, propertyMap, clazz);
            generatorText = processBatchUpdateSelective(generatorText, propertyMap, clazz);
            generatorText = processUpdateOnDuplicate(generatorText, propertyMap, clazz);
            generatorText = processFieldByCondition(generatorText, propertyMap, clazz);
            generatorText = processFieldContentByCondition(generatorText, propertyMap, clazz);
            generatorText = processSelectByCondition(generatorText, propertyMap, clazz, configMap);
            return generatorText;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 前置处理
     *
     * @param template  {@link String}
     * @param configMap {@link Map}
     * @return {@link String}
     */
    public static String beforeProcess(String template, Map<String, String> configMap) {
        template = processUniqueFields(template, configMap);
        return template;
    }

    /**
     * 处理唯一约束
     *
     * @param template  {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    private static String processUniqueFields(String template, Map<String, String> configMap) {
        /* 不存在时处理 */
        if (StringUtils.isEmpty(configMap.get(GenConstant.keyUniqueFieldList))) {
            template = GeneratorUtils.deleteUnUsedTextAndMethod(template, null, notUniqueFieldList, null);
            template = GeneratorUtils.removeXmlMethod(template, "deleteDuplicateUniqueKey");
            return template;
        }
        List<GenUniqueField> uniqueFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyUniqueFieldList, GenUniqueField.class);
        /* 取出每组最后一个字段 */
        List<String> lastUniqueFieldList = Lists.newArrayList();
        HashSet<String> set = new HashSet<>();
        for (GenUniqueField uniqueField : uniqueFieldList) {
            List<String> uniqueFields = uniqueField.getUniqueFields();
            String value = StrUtil.toUnderlineCase(uniqueFields.get(uniqueFields.size() - 1));
            if (!set.contains(value)) {
                lastUniqueFieldList.add(value);
                set.add(value);
            }
        }
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.fieldListConverterToProperty(lastUniqueFieldList);
        /* 处理uniqueFieldDelete */
        template = GeneratorUtils.processCommonItem(template, propertyMap, GenConstant.keyUniqueFieldDelete, null, configMap, uniqueFieldDeleteTemplate);
        /* deleteDuplicateUniqueKey */
        if (!Objects.equals(uniqueFieldList.get(0).getCheckUniqueType(), String.valueOf(CheckUniqueTypeEnum.DELETE.getValue()))) {
            template = GeneratorUtils.removeXmlMethod(template, "deleteDuplicateUniqueKey");
        } else {
            template = processDeleteDuplicateUniqueKey(template, uniqueFieldList);
        }
        return template;
    }

    /**
     * 处理resultMap
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processResultMap(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            EntityProperty property = propertyMap.get(name);
            String part = "";
            if ("id".equals(name)) {
                part = String.format("<id column=\"%s\" property=\"%s\" jdbcType=\"%s\"/>\n", property.getColumn(), property.getProperty(), property.getJdbcType());
            } else {
                part = String.format("        <result column=\"%s\" property=\"%s\" jdbcType=\"%s\"/>\n", property.getColumn(), property.getProperty(), property.getJdbcType());
            }
            stringBuilder.append(part);
        }
        String baseResultMap = stringBuilder.toString();
        text = text.replace("${baseResultMap}", baseResultMap);
        text = text.replace("\n    </resultMap>", "    </resultMap>");
        return text;
    }

    /**
     * 处理BaseColumnList
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processBaseColumnList(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", "9");
        configMap.put("lineSeparator", "\n        ");
        configMap.put("omitField", "false");
        configMap.put("separator", ", ");
        configMap.put("mapKey", "${Base_Column_List}");
        configMap.put("template", "{{column}}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("prefix", null);
        configMap.put("suffix", null);
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理InsertField
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processInsertField(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", "3");
        configMap.put("lineSeparator", "\n        ");
        configMap.put("omitField", "true");
        configMap.put("separator", ", ");
        configMap.put("mapKey", "${insert_field}");
        configMap.put("template", "{{column}}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("prefix", null);
        configMap.put("suffix", null);
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理InsertValue
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processInsertValue(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", "3");
        configMap.put("lineSeparator", "\n        ");
        configMap.put("omitField", "true");
        configMap.put("separator", ", ");
        configMap.put("mapKey", "${insert_value}");
        configMap.put("template", "#{record.{{property}},jdbcType={{jdbcType}}}");
        configMap.put("start", null);
        configMap.put("end", null);
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理UpdateByCondition
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processUpdateByCondition(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", "\n");
        configMap.put("mapKey", "${updateByCondition}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template",
                "            <if test=\"{{property}} != null\">\n" +
                        "                {{column}} = #{{{property}},jdbcType={{jdbcType}}},\n" +
                        "            </if>");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理UpdateByPrimaryKey
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processUpdateByPrimaryKey(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", ",\n");
        configMap.put("mapKey", "${updateByPrimaryKey}");
        configMap.put("start", "set\n");
        configMap.put("end", null);
        configMap.put("template", "        {{column}} = #{{{property}},jdbcType={{jdbcType}}}");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理BatchUpdate
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processBatchUpdate(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", "\n");
        configMap.put("mapKey", "${batchUpdate}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template",
                "            <trim prefix=\"{{column}} = case\" suffix=\"end,\">\n" +
                "                <foreach collection=\"records\" index=\"index\" item=\"item\">\n" +
                "                    when id = #{item.id} then #{item.{{property}}}\n" +
                "                </foreach>\n" +
                "            </trim>");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理BatchUpdate
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processBatchUpdateSelective(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", "\n");
        configMap.put("mapKey", "${batchUpdateSelective}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template",
                "            <trim prefix=\"{{column}} = case\" suffix=\"end,\">\n" +
                        "                <foreach collection=\"records\" index=\"index\" item=\"item\">\n" +
                        "                    <if test=\"item.{{property}} != null\">\n" +
                        "                        when id = #{item.id} then\n" +
                        "                        #{item.{{property}}}\n" +
                        "                    </if>\n" +
                        "                </foreach>\n" +
                        "            </trim>");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理updateOnDuplicate
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processUpdateOnDuplicate(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", ",\n");
        configMap.put("mapKey", "${updateOnDuplicate}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template", "        {{column}} = #{{{property}},jdbcType={{jdbcType}}}");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理FieldByCondition
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processFieldByCondition(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", ",\n");
        configMap.put("mapKey", "${fieldByCondition}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template",
                "                <if test=\"item.{{property}} != null\">\n" +
                "                    {{column}},\n" +
                "                </if>");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理FieldByCondition
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processFieldContentByCondition(String text, Map<String, EntityProperty> propertyMap, Class clazz) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", ",\n");
        configMap.put("mapKey", "${fieldContentByCondition}");
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template",
                "                <if test=\"item.{{property}} != null\">\n" +
                        "                    #{item.{{column}},\n" +
                        "                </if>");
        text = processPart(text, propertyMap, clazz, configMap, null);
        return text;
    }

    /**
     * 处理SelectByCondition
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    public static String processSelectByCondition(String text, Map<String, EntityProperty> propertyMap, Class clazz, Map<String, String> replaceMap) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("omitField", "true");
        configMap.put("separator", "\n");
        configMap.put("mapKey", selectMapKey);
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template", "            <if test=\"{{property}} != null \">and {{column}} = #{{{property}}}</if>");
        configMap.put("stringTemplate", "            <if test=\"{{property}} != null  and {{property}} != ''\">and {{column}} = #{{{property}}}</if>");
        configMap.put("stringQueryTemplate",
                "            <if test=\"{{property}} != null  and {{property}} != ''\">\n" +
                        "                and {{column}} like concat('%', #{{{property}}}, '%')\n" +
                        "            </if>");
        configMap.put("listTemplate",
                "            <if test=\"{{property}} != null and {{property}}.size() > 0\">\n" +
                        "                and {{column}} in\n" +
                        "                <foreach collection=\"{{property}}\" item=\"record\" index=\"index\" separator=\",\" open=\"(\" close=\")\">\n" +
                        "                    #{record}\n" +
                        "                </foreach>\n" +
                        "            </if>");
        text = processPart(text, propertyMap, clazz, configMap, replaceMap);
        return text;
    }

    /**
     * 增加子表中对主表id列表的查询
     *
     * @param text      {@link String}
     * @param configMap {@link Map}
     * @return {@link String}
     */
    private static String addSlavePart(String text, Map<String, String> configMap) {
        if (!GeneratorUtils.relationType3Slave(configMap)) {
            return text;
        }
        String slaveQueryMainIdsText = GeneratorUtils.doGenerator(slaveQueryMainIdsTemplate, configMap);
        return String.format("%s\n%s", text, slaveQueryMainIdsText);
    }

    /**
     * 处理DeleteDuplicateUniqueKey
     *
     * @param text            {@link String}
     * @param uniqueFieldList {@link List<GenUniqueField>}
     * @return {@link String}
     */
    public static String processDeleteDuplicateUniqueKey(String text, List<GenUniqueField> uniqueFieldList) {
        ArrayList<String> newList = Lists.newArrayList();
        for (String field : uniqueFieldList.get(0).getUniqueFields()) {
            newList.add(String.format("%s = #{record.%s}", StrUtil.toUnderlineCase(field), field));
        }
        text = text.replace("#{deleteDuplicateUniqueKey}", String.join(" and ", newList));
        return text;
    }


    /**
     * 处理部分
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param clazz       {@link Class}
     * @return {@link String}
     */
    private static String processPart(String text, Map<String, EntityProperty> propertyMap, Class clazz, Map<String, String> configMap, Map<String, String> replaceMap) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        Boolean omitField = configMap.get("omitField") == null ? false : Boolean.valueOf(configMap.get("omitField"));
        int partSize = getPartSize(propertyMap, omitField, configMap);
        if (configMap.get("start") != null) {
            stringBuilder.append(configMap.get("start"));
        }
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            //1.普通忽略字段 2.selectByCondition的忽略字段
            if (omitField && ((omitFieldSet.contains(name) ||
                    (selectMapKey.equals(configMap.get("mapKey")) && omitSelectByConditionSet.contains(name))))) {
                continue;
            }
            EntityProperty property = propertyMap.get(name);
            if (configMap.get("lineSize") != null && index % Integer.valueOf(configMap.get("lineSize")) == 0 && index != 0) {
                stringBuilder.append(configMap.get("lineSeparator"));
            }
            String part = configMap.get("template");
            if (selectMapKey.equals(configMap.get("mapKey")) && "VARCHAR".equals(property.getJdbcType())) {
                part = configMap.get("stringTemplate");
                if (StringUtils.isNotEmpty(replaceMap.get(GenConstant.keyQueryFieldList))) {
                    List<GenQueryField> queryFieldList = GeneratorUtils.getListFromConfig(replaceMap, GenConstant.keyQueryFieldList, GenQueryField.class);
                    Set<String> queryFieldSet = queryFieldList.stream().map(GenQueryField::getQueryField).collect(Collectors.toSet());
                    if (queryFieldSet.contains(name)) {
                        part = configMap.get("stringQueryTemplate");
                    }
                }
            }
            if (selectMapKey.equals(configMap.get("mapKey")) && "ARRAY".equals(property.getJdbcType())) {
                part = configMap.get("listTemplate");
            }
            String column = property.getColumn();
            if (GenConstant.sqlKeyWordSet.contains(property.getColumn().toUpperCase())) {
                column = backQuote + column + backQuote;
            }
            part = part.replace("{{column}}", column).replace("{{property}}", property.getProperty()).replace("{{jdbcType}}", property.getJdbcType());
            if (configMap.get("prefix") != null) {
                if (!Objects.equals(configMap.get("prefix"), backQuote) ||
                        (Objects.equals(configMap.get("prefix"), backQuote) && !property.getColumn().contains(underline))) {
                    part = configMap.get("prefix") + part;
                }
            }
            if (configMap.get("suffix") != null) {
                if (!Objects.equals(configMap.get("suffix"), backQuote) ||
                        (Objects.equals(configMap.get("suffix"), backQuote) && !property.getColumn().contains(underline))) {
                    part = part + configMap.get("suffix");
                }
            }
            if (index != partSize && configMap.get("separator") != null) {
                part = part + configMap.get("separator");
            }
            stringBuilder.append(part);
            index++;
        }
        if (configMap.get("end") != null) {
            stringBuilder.append(configMap.get("end"));
        }
        String genText = stringBuilder.toString();
        if (selectMapKey.equals(configMap.get("mapKey")) && replaceMap != null) {
            genText = addSlavePart(genText, replaceMap);
        }
        text = text.replace(configMap.get("mapKey"), genText);
        return text;
    }

    /**
     * 获取生成模板块长度
     *
     * @param propertyMap {@link Map<String,  EntityProperty >}
     * @param omitField   {@link Boolean}
     * @param configMap   {@link Map<String, String>}
     * @return {@link int}
     */
    private static int getPartSize(Map<String, EntityProperty> propertyMap, Boolean omitField, Map<String, String> configMap) {
        int size = propertyMap.size() - 1;
        if (omitField) {
            if (selectMapKey.equals(configMap.get("mapKey"))) {
                size = size - omitSelectByConditionSet.size();
            } else {
                size = size - omitFieldSet.size();
            }
        }
        return size;
    }
}
