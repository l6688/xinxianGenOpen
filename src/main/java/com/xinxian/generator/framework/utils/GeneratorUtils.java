package com.xinxian.generator.framework.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xinxian.generator.constant.GenConfigConstant;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenQueryField;
import com.xinxian.generator.framework.bean.GenRelationTable;
import com.xinxian.generator.framework.bean.GenUniqueField;
import com.xinxian.generator.framework.enums.DataTypeEnum;
import com.xinxian.generator.framework.enums.FrontElementTypeEnum;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.enums.JavaTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.service.GeneratorProject;
import com.xinxian.generator.framework.dto.entity.GenTable;
import com.xinxian.generator.framework.dto.enums.CastTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName GeneratorUtils
 * @Description 自动生成工具类
 * @Author lmy
 * @Date 2023/1/17 21:14
 */
@Slf4j
public class GeneratorUtils {

    public static final String commonPath = "/src/main/java/";
    public static final String resourcePath = "/src/main/resources";
    public static final String commonTestPath = "/src/test/java/";
    public static final String frontApiPath = "/src/api/";
    public static final String frontViewsPath = "/src/views/";
    public static final String frontRouterPath = "/src/router/";
    public static final String jdbcTypeVarchar = "VARCHAR";
    public static final String jdbcTypeBigint = "BIGINT";
    public static final String jdbcTypeInteger = "INTEGER";
    public static final String jdbcTypeTinyint = "TINYINT";
    public static final String jdbcTypeTimestamp = "TIMESTAMP";
    public static final String typeString = "String";
    public static final String typeLong = "Long";
    public static final String typeInteger = "Integer";
    public static final String typeByte = "Byte";
    public static final String typeDate = "Date";
    public static final String typeBoolean = "Boolean";
    public static final String typeListString = "List<String>";
    public static final String typeListLong = "List<Long>";
    public static final String typeListInteger = "List<Integer>";
    public static final String typeList = "List<%s%s>";
    public static final String entityPropertyType = "type";
    public static final Set numericSet = new HashSet(Arrays.asList(typeLong, typeInteger, typeByte));
    public static final Set commonFieldSet = new HashSet(Arrays.asList("createUserId", "updateUserId", "updateBy", "isDelete"));
    public static final Set ignoreFieldEntitySet = new HashSet(Arrays.asList("isDelete"));
    public static final Set ignoreResultFieldSet = new HashSet(Arrays.asList("updateUserId", "isDelete"));
    public static final Set abstractMethodGenTypeSet = new HashSet(Arrays.asList(GenTypeEnum.SERVICE.name()));
    public static final Set ignoreVoFieldSet = new HashSet(Arrays.asList("isDelete"));

    private static final Pattern entityPattern = Pattern.compile("\\/\\*\\*\n     \\* (.*)\n     \\*\\/\n    private (.*) (.*);");
    private static final Pattern propertyPattern = Pattern.compile("<(.*)column=\"(.*)\" property=\"(.*)\" jdbcType=\"(.*)\"(.*)/>");
    private static final Pattern columnAnnotationPattern = Pattern.compile("(.*)(@Column\\(name = \"(.*)\"\\))\n");

    public static final Set<GenTypeEnum> hasSlaveTemplateSet = new HashSet<>(Arrays.asList(GenTypeEnum.CONVERTER));
    public static final Set<String> genTableConfigListFieldSet = new HashSet<>(Arrays.asList("queryFieldList", "relationTableList", "uniqueFieldList"));

    private static final List<Pair<String, String>> xmlMethodStartRegexList = Arrays.asList(
            Pair.of("(    <insert id=\"%s\")", "(    </insert>\n)"),
            Pair.of("(    <delete id=\"%s\")", "(    </delete>\n)"),
            Pair.of("(    <update id=\"%s\")", "(    </update>\n)"),
            Pair.of("(    <select id=\"%s\")", "(    </select>\n)"));

    private static final List<String> mapperMethodBeginList = Arrays.asList("int", "void", "${entityName}", "List<${entityName}>", "Long", "Integer", "Set<String>", "List<String>", "Set<Long>", "List<Long>", "Set<Integer>", "List<Integer>");

    private static final String classAnnotation =
            "/**\n" +
                    " * @ClassName ${entityName}${classType}\n" +
                    " * @Description ${entityDesc}${classTypeDesc}\n" +
                    " * @Author ${author}\n" +
                    " * @Date ${date}\n" +
                    " */";
    public static final String fieldDefine =
            "    /**\n" +
                    "     * ${desc}\n" +
                    "     */\n" +
                    "    private ${type} ${property};\n\n";

    public static final Map<String, String> importMap = new HashMap() {
        {
            put(" List<", "import java.util.List;\n");
            put(" Date ", "import java.util.Date;\n");
            put(" BigDecimal ", "import java.math.BigDecimal;\n");
        }
    };

    private static final Map jdbcTypeMap = new HashMap() {
        {
            put(jdbcTypeVarchar, "varchar");
            put(jdbcTypeBigint, "bigint");
            put(jdbcTypeInteger, "int");
            put(jdbcTypeTimestamp, "datetime");
        }
    };

    private static final Map<String, String> simpleNameMap = new HashMap() {
        {
            put("Byte", "Integer");
        }
    };

    /* 单表 */
    public static final Integer relationType1 = 1;
    /* 内容关系表 */
    public static final Integer relationType2 = 2;
    /* 关系表 */
    public static final Integer relationType3 = 3;

    /**
     * 类注释修改
     */
    public static String getClassAnnotation(Map<String, String> configMap, String classType, String classTypeDesc) {
        String text = classAnnotation;
        text = text.replace("${entityName}", configMap.get("${entityName}"));
        text = text.replace("${entityDesc}", configMap.get("${entityDesc}"));
        text = text.replace("${author}", configMap.get("${author}"));
        text = text.replace("${date}", configMap.get("${date}"));
        text = text.replace("${classType}", classType);
        text = text.replace("${classTypeDesc}", classTypeDesc);
        return text;
    }

    /**
     * 处理模板
     *
     * @param generatorText {@link String}
     * @param configMap     {@link Map<String, String>}
     * @return
     */
    public static String processTemplate(String generatorText, Map<String, String> configMap) {
        generatorText = doGenerator(generatorText, configMap);
        return generatorText;
    }

    /**
     * 模板替换
     *
     * @param template  {@link String}
     * @param configMap {@link Map<String,String>}
     * @return {@link String}
     */
    public static String doGenerator(String template, Map<String, String> configMap) {
        String generatorText = template;
        for (Map.Entry<String, String> mapEntry : configMap.entrySet()) {
            generatorText = generatorText.replace(mapEntry.getKey(), mapEntry.getValue());
        }
        return generatorText;
    }

    /**
     * 在前面追加
     * 在text中的location前追加appendTemplate
     *
     * @param text           {@link String}
     * @param location       {@link String}
     * @param appendTemplate {@link String}
     * @param configMap      {@link Map< String, String>}
     * @return {@link String}
     */
    public static String appendHead(String text, String location, String appendTemplate, Map<String, String> configMap) {
        String genText = GeneratorUtils.doGenerator(appendTemplate, configMap);
        if (!text.contains(genText + location)) {
            text = text.replace(location, genText + location);
        }
        return text;
    }

    /**
     * 在后面追加
     * 在text中的location后追加appendTemplate
     *
     * @param text           {@link String}
     * @param location       {@link String}
     * @param appendTemplate {@link String}
     * @param configMap      {@link Map< String, String>}
     * @return {@link String}
     */
    public static String appendTail(String text, String location, String appendTemplate, Map<String, String> configMap) {
        String genText = GeneratorUtils.doGenerator(appendTemplate, configMap);
        if (!text.contains(location + genText)) {
            text = text.replace(location, location + genText);
        }
        return text;
    }

    /**
     * 在前面追加
     * 在text中的location前追加appendTemplate
     * locationTemplate和appendTemplate都要替换
     *
     * @param text             {@link String}
     * @param locationTemplate {@link String}
     * @param appendTemplate   {@link String}
     * @param configMap        {@link Map< String, String>}
     * @return {@link String}
     */
    public static String replaceTemplateAndAppendHead(String text, String locationTemplate, String appendTemplate, Map<String, String> configMap) {
        locationTemplate = GeneratorUtils.doGenerator(locationTemplate, configMap);
        text = appendHead(text, locationTemplate, appendTemplate, configMap);
        return text;
    }

    /**
     * 模版替换
     *
     * @param text        {@link String}
     * @param oldTemplate {@link String}
     * @param newTemplate {@link String}
     * @param configMap   {@link Map< String, String>}
     * @return {@link String}
     */
    public static String replaceTemplate(String text, String oldTemplate, String newTemplate, Map<String, String> configMap) {
        oldTemplate = GeneratorUtils.doGenerator(oldTemplate, configMap);
        newTemplate = GeneratorUtils.doGenerator(newTemplate, configMap);
        if (!text.contains(newTemplate)) {
            text = text.replace(oldTemplate, newTemplate);
        }
        return text;
    }

    /**
     * 模板替换
     *
     * @param templateList {@link List<String>}
     * @param configMap    {@link Map<String,String>}
     * @return {@link String}
     */
    public static List<String> doGenerator(List<String> templateList, Map<String, String> configMap) {
        return templateList.stream().map(item -> {
            return item.replace(item, doGenerator(item, configMap));
        }).collect(Collectors.toList());
    }

    /**
     * 执行获取类
     */
    public static Class getClass(Map<String, String> configMap) throws ClassNotFoundException {
        Class clazz = doGetClass(configMap);
//        Class clazz = getOtherProjectClass(configMap);
        return clazz;
    }

    /**
     * 执行获取类
     */
    public static Class doGetClass(Map<String, String> configMap) throws ClassNotFoundException {
        String classPath = getClassPath(configMap);
        Class clazz = Class.forName(classPath);
        return clazz;
    }

    /**
     * 执行获取类
     */
    public static String getClassPath(Map<String, String> configMap) {
        String path = configMap.get("${packageBase}");
        String entityName = configMap.get("${entityName}");
        return path + ".dao.entity." + entityName;
    }

    /**
     * 获取其他项目内的类
     */
    public static Class getOtherProjectClass(Map<String, String> configMap) {
        String classPath = configMap.get("${entityInputPath}");
        Class clazz = DynamicLoader.getClass(classPath);
        return clazz;
    }

    /**
     * 获取模板源地址
     */
    public static String getTemplateSourcePath(GenTypeEnum genTypeEnum, Map<String, String> configMap) {
        String templateBasePath = configMap.get("${templateBasePath}");
        String sourcePath = templateBasePath + StrUtil.upperFirst(genTypeEnum.getValue()) + "Template";
        /* 关系表的模板 */
        if (GeneratorUtils.relationType3Slave(configMap) && hasSlaveTemplateSet.contains(genTypeEnum)) {
            return sourcePath + "Slave";
        }
        return sourcePath;
    }

    /**
     * 获取返回地址
     */
    public static String getResultPath(String genType, String name, Map<String, String> abstractPathMap) {
        String suffix = "";
        switch (genType) {
            case "sql":
                suffix = ".sql";
                break;
            case "mapperXml":
                suffix = ".xml";
                break;
            default:
                suffix = ".java";
                break;
        }
        String upperGenType = StrUtil.upperFirst(genType);
        if (Objects.equals(genType, GenTypeEnum.MAPPER_XML.getValue())) {
            upperGenType = upperGenType.replace("MapperXml", "Mapper");
        }
        if (Objects.equals(genType, GenTypeEnum.VO.getValue())) {
            upperGenType = upperGenType.toUpperCase();
        }
        String resultPath = abstractPathMap.get(genType) + name + upperGenType + suffix;
        return resultPath;
    }

    /**
     * 获取返回地址
     * GeneratorUtils.getResultPath方法
     */
    public static String getResultPath(GenTypeEnum genTypeEnum, Map<String, String> abstractPathMap, Map<String, String> configMap) {
        String suffix = ".java";
        String genType = genTypeEnum.getValue();
        String name = configMap.get("${entityName}");
        String upperGenType = StrUtil.upperFirst(genType);
        String controllerType = "";
        String controllerEndPath = "";
        switch (genTypeEnum) {
            case CONTROLLER:
                name = configMap.get("${bizClassName}");
                break;
            case ENTITY:
                upperGenType = "";
                break;
            case MAPPER_XML:
                suffix = ".xml";
                upperGenType = upperGenType.replace("MapperXml", "Mapper");
                break;
            case PARAM:
            case VO:
                upperGenType = "View";
                break;
            case ADD_FIELD_SQL:
                suffix = ".sql";
                break;
            case ENUM:
                name = "{{upperFirstProperty}}";
                break;
        }
        String resultPath = abstractPathMap.get(genType) + name + upperGenType + suffix;
        if (GenTypeEnum.frontClass(genTypeEnum)) {
            resultPath = abstractPathMap.get(genType) + controllerType + controllerEndPath + suffix;
        }
        return resultPath;
    }

    /**
     * 获取单行配置map
     *
     * @param mapKey   {@link String}
     * @param template {@link String}
     * @return {@link String}
     */
    public static Map<String, String> getOneLinePartConfigMap(String mapKey, String template, String separator) {
        Map<String, String> configMap = new HashMap();
        configMap.put("lineSize", null);
        configMap.put("lineSeparator", null);
        configMap.put("separator", separator);
        configMap.put("mapKey", mapKey);
        configMap.put("start", null);
        configMap.put("end", null);
        configMap.put("template", template);
        return configMap;
    }

    /**
     * 处理通用item
     *
     * @param text         {@link String}
     * @param propertyMap  {@link LinkedHashMap<String,EntityProperty>}
     * @param mapKey       {@link String}
     * @param frontType    {@link Integer}
     * @param omitFieldSet {@link Set<String>}
     * @return {@link String}
     */
    public static String processCommonItem(String text, LinkedHashMap<String, EntityProperty> propertyMap, String mapKey, Integer frontType, Set<String> omitFieldSet, Map<String, String> configMap) {
        Map<String, String> partConfigMap = new HashMap();
        partConfigMap.put("lineSize", null);
        partConfigMap.put("lineSeparator", null);
        partConfigMap.put("separator", "\n");
        partConfigMap.put("mapKey", mapKey);
        partConfigMap.put("start", null);
        partConfigMap.put("end", null);
        partConfigMap.put("template", "");
        partConfigMap.putAll(configMap);
        text = GeneratorUtils.processPart(text, propertyMap, frontType, partConfigMap, omitFieldSet);
        return text;
    }

    /**
     * 处理通用item
     *
     * @param text         {@link String}
     * @param propertyMap  {@link LinkedHashMap<String,EntityProperty>}
     * @param mapKey       {@link String}
     * @param omitFieldSet {@link Set<String>}
     * @return {@link String}
     */
    public static String processCommonItem(String text, LinkedHashMap<String, EntityProperty> propertyMap, String mapKey, Set<String> omitFieldSet, Map<String, String> configMap, String template, String separator) {
        Map<String, String> partConfigMap = new HashMap();
        partConfigMap.put("lineSize", null);
        partConfigMap.put("lineSeparator", null);
        partConfigMap.put("separator", separator);
        partConfigMap.put("mapKey", mapKey);
        partConfigMap.put("start", null);
        partConfigMap.put("end", null);
        partConfigMap.put("template", template);
        partConfigMap.put(GenConstant.keyTemplateMap, configMap.get(GenConstant.keyTemplateMap));
        partConfigMap.putAll(configMap);
        text = GeneratorUtils.processPart(text, propertyMap, null, partConfigMap, omitFieldSet);
        return text;
    }

    /**
     * 处理通用item
     *
     * @param text         {@link String}
     * @param propertyMap  {@link LinkedHashMap<String,EntityProperty>}
     * @param mapKey       {@link String}
     * @param omitFieldSet {@link Set<String>}
     * @return {@link String}
     */
    public static String processCommonItem(String text, LinkedHashMap<String, EntityProperty> propertyMap, String mapKey, Set<String> omitFieldSet, Map<String, String> configMap, String template) {
        return processCommonItem(text, propertyMap, mapKey, omitFieldSet, configMap, template, GenConstant.ENTER);
    }

    /**
     * 处理部分
     *
     * @param text        {@link String}
     * @param propertyMap {@link Map<String,    EntityProperty   >}
     * @param frontType   {@link Integer}
     * @return {@link String}
     */
    public static String processPart(String text, LinkedHashMap<String, EntityProperty> propertyMap, Integer frontType, Map<String, String> configMap, Set<String> omitFieldSet) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        int partSize = getPartSize(propertyMap, omitFieldSet);
        if (configMap.get("start") != null) {
            stringBuilder.append(configMap.get("start"));
        }
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            if (omitFieldSet != null && !omitFieldSet.isEmpty() && omitFieldSet.contains(name)) {
                continue;
            }
            EntityProperty property = propertyMap.get(name);
            if (configMap.get("lineSize") != null && index % Integer.valueOf(configMap.get("lineSize")) == 0 && index != 0) {
                stringBuilder.append(configMap.get("lineSeparator"));
            }
            String part = getTemplate(configMap, property, frontType);
            if (StringUtils.isEmpty(part)) {
                continue;
            }
            part = replaceProperty(part, property);
            if (index != partSize && configMap.get("separator") != null && StringUtils.isNotEmpty(part)) {
                part = part + configMap.get("separator");
            }
            stringBuilder.append(part);
            index++;
        }
        if (configMap.get("end") != null) {
            stringBuilder.append(configMap.get("end"));
        }
        text = text.replace(configMap.get("mapKey"), stringBuilder.toString());
        return text;
    }

    /**
     * 处理部分
     *
     * @param text     {@link String}
     * @param property {@link EntityProperty}
     * @return {@link String}
     */
    public static String replaceProperty(String text, EntityProperty property) {
        return text.replace("{{column}}", property.getColumn() == null ? "" : property.getColumn())
                .replace("{{property}}", property.getProperty())
                .replace("{{upperProperty}}", StrUtil.toUnderlineCase(property.getProperty()).toUpperCase())
                .replace("{{upperFirstProperty}}", StrUtil.upperFirst(property.getProperty()))
                .replace("{{jdbcType}}", property.getJdbcType() == null ? "" : property.getJdbcType())
                .replace("{{type}}", property.getType() == null ? "" : property.getType())
                .replace("{{desc}}", GeneratorUtils.getFormatDesc(property))
                .replace("{{frontWidth}}", property.getFrontWidth() == null ? "" : property.getFrontWidth())
                .replace("{{relationField}}", property.getRelationField() == null ? "" : property.getRelationField())
                .replace("{{upperFirstRelationField}}", property.getRelationField() == null ? "" : StrUtil.upperFirst(property.getRelationField()))
                .replace("{{tableName}}", StrUtil.upperFirst(property.getProperty()))
                .replace("{{customObject}}", property.getCustomObject() == null ? "" : property.getCustomObject())
                .replace("{{firstEnum}}", GeneratorUtils.getFirstEnum(property) == null ? "" : GeneratorUtils.getFirstEnum(property))
                ;
    }

    /**
     * 获取首个枚举值
     * 申请状态 1-未申请  2-申请中 3-已通过 4-未通过
     *
     * @param property {@link EntityProperty}
     * @return {@link String}
     */
    private static String getFirstEnum(EntityProperty property) {
        String result = null;
        String desc = property.getDesc() != null ? property.getDesc().trim() : "";
        if (getIsEnum(desc, property.getType())) {
            String[] array = desc.split(" ");
            String enumPart = array[1];
            desc.split(" ");
            if (desc.contains("-")) {
                result = enumPart.split("-")[0].replace("(", "");
            } else if (desc.contains(":")) {
                result = enumPart.split(":")[0].replace("(", "");
            }
        }
        return result;
    }

    /**
     * 获取模板
     *
     * @param configMap {@link Map<String,String>}
     * @param property  {@link EntityProperty}
     * @param frontType {@link Integer}
     * @return {@link String}
     */
    private static String getTemplate(Map<String, String> configMap, EntityProperty property, Integer frontType) {
        String template = configMap.get("template");
        String mapKey = configMap.get("mapKey");
        if (FrontElementTypeEnum.getInstanceByKey(mapKey) != null) {
            template = GenFrontUtils.getComponentTemplate(property, frontType, configMap, mapKey);
        }
        if (StringUtils.isEmpty(configMap.get("template")) && configMap.containsKey(GenConstant.keyTemplateMap)) {
            Map<String, String> map = JSON.parseObject(configMap.get(GenConstant.keyTemplateMap), Map.class);
            template = map.get(property.getRelationType() + mapKey);
        }
        return template;
    }

    /**
     * 获取生成模板块长度
     *
     * @param propertyMap  {@link Map<String,EntityProperty>}
     * @param omitFieldSet {@link Set<String>}
     * @return {@link int}
     */
    private static int getPartSize(LinkedHashMap<String, EntityProperty> propertyMap, Set<String> omitFieldSet) {
        if (propertyMap == null) {
            return 0;
        }
        int size = propertyMap.size() - 1;
        if (omitFieldSet != null && !omitFieldSet.isEmpty()) {
            size = size - omitFieldSet.size();
        }
        return size;
    }

    public static LinkedHashMap<String, EntityProperty> createPropertyMap(Map<String, String> configMap) {
        String mapperXmlPath = configMap.get("${mapperXmlInputPath}");
        LinkedHashMap<String, EntityProperty> propertyMap = getMapperXmlPropertyMap(mapperXmlPath);
        setOtherInfoFromEntity(propertyMap, configMap);
        return propertyMap;
    }

    private static void setOtherInfoFromEntity(Map<String, EntityProperty> propertyMap, Map<String, String> configMap) {
        setType(propertyMap, configMap);
        setDesc(propertyMap, configMap);
    }

    private static void setType(Map<String, EntityProperty> propertyMap, Map<String, String> configMap) {
        try {
            Class clazz = GeneratorUtils.getClass(configMap);
            for (Field field : clazz.getDeclaredFields()) {
                String property = field.getName();
                EntityProperty entityProperty = propertyMap.get(property);
                if (entityProperty != null) {
                    String simpleName = field.getType().getSimpleName();
                    entityProperty.setType(simpleNameMap.getOrDefault(simpleName, simpleName));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setDesc(Map<String, EntityProperty> propertyMap, Map<String, String> configMap) {
        String entityPath = configMap.get("${entityInputPath}");
        if (StringUtils.isEmpty(entityPath)) {
            return;
        }
        String text = FileUtil.readUtf8String(entityPath);
        text = processField(text);
        text = processFieldAnnotation(text);
        Matcher matcher = entityPattern.matcher(text);
        int matcherStart = 0;
        while (matcher.find(matcherStart)) {
            String desc = matcher.group(1);
            String property = matcher.group(3);
            EntityProperty entityProperty = propertyMap.get(property);
            if (entityProperty != null) {
                entityProperty.setDesc(desc);
            }
            matcherStart = matcher.end();
        }
    }

    /**
     * 处理字段注释
     *
     * @param text {@link String}
     * @return {@link String}
     */
    private static String processFieldAnnotation(String text) {
        text = text.replace("@Id", "");
        text = text.replace("@GeneratedValue(generator = \"JDBC\")", "");
        Matcher matcher = columnAnnotationPattern.matcher(text);
        int matcher_start = 0;
        while (matcher.find(matcher_start)) {
            String columnAnnotationPattern = matcher.group(2);
            text = text.replace(columnAnnotationPattern, "");
            text = text.replace("     */\n    \n", "     */\n");
            matcher_start = matcher.end();
        }
        return text;
    }

    /**
     * 处理字段
     *
     * @param text {@link String}
     * @return {@link String}
     */
    private static String processField(String text) {
        text = text.replace("private Byte ", "private Integer ");
        return text;
    }

    private static LinkedHashMap<String, EntityProperty> getMapperXmlPropertyMap(String mapperXmlPath) {
        String mapperXmlText = FileUtil.readUtf8String(mapperXmlPath);
        Matcher matcher = propertyPattern.matcher(mapperXmlText);
        int matcherStart = 0;
        LinkedHashMap<String, EntityProperty> propertyMap = new LinkedHashMap<>();
        while (matcher.find(matcherStart)) {
            String column = matcher.group(2);
            String property = matcher.group(3);
            String jdbcType = matcher.group(4);
            EntityProperty build = EntityProperty.builder().column(column).property(property).jdbcType(jdbcType).build();
            propertyMap.put(property, build);
            matcherStart = matcher.end();
        }
        return propertyMap;
    }

    /**
     * 正则匹配并替换
     *
     * @param text        {@link String}
     * @param pattern     {@link Pattern}
     * @param replacement {@link String}
     * @return {@link String}
     */
    public static String replace(String text, Pattern pattern, String replacement) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        String matchText = matcher.group(1);
        text = text.replace(matchText, replacement);
        return text;
    }

    /**
     * 正则匹配并替换
     *
     * @param text    {@link String}
     * @param pattern {@link Pattern}
     * @return {@link String}
     */
    public static String replaceEmpty(String text, Pattern pattern) {
        return replace(text, pattern, "");
    }

    /**
     * 处理结尾空行
     */
    public static String processEndEmptyLine(String sourceText, String endStr) {
        Pattern endPattern = Pattern.compile(String.format("(%s(\n)*)", endStr));
        Matcher endMatcher = endPattern.matcher(sourceText);
        if (endMatcher.find()) {
            String endtext = endMatcher.group(1);
            sourceText = sourceText.replace(endtext, endStr);
        }
        return sourceText;
    }

    /**
     * 实体类通用处理
     *
     * @param text {@link String}
     * @return {@link String}
     */
    public static String otherCommonEntityProcess(String text) {
        text = addImport(text);
        return text;
    }

    /**
     * 通用实体处理
     *
     * @param generatorText {@link String}
     * @param configMap     {@link Map<String,String>}
     * @param omitSet       {@link Set<String>}
     * @param genTypeEnum   {@link GenTypeEnum}
     * @return {@link String}
     */
    public static String commonEntityProcess(String generatorText, Map<String, String> configMap, Set<String> omitSet, GenTypeEnum genTypeEnum) {
        LinkedHashMap<String, EntityProperty> fieldPropertyMap = GeneratorUtils.getFieldPropertyMap(configMap, "propertyMap");
        LinkedHashMap<String, EntityProperty> convertMap = GeneratorUtils.getFieldPropertyMap(configMap, "${convertMap}");
        String expansionPoint = getEntityExpansionPoint(configMap, GenTypeEnum.getAddFieldKey(genTypeEnum), genTypeEnum);
        generatorText = generatorText.replace("${expansionPoint}\n", expansionPoint);
        String entityField = getPropertyTemplate(fieldPropertyMap, GeneratorUtils.fieldDefine, omitSet, genTypeEnum, convertMap);
        generatorText = generatorText.replace("${entityField}", entityField);
        generatorText = otherCommonEntityProcess(generatorText);
        return generatorText;
    }

    /**
     * 获取实体拓展点
     *
     * @param configMap   {@link Map<String, String>}
     * @param addFieldKey {@link String}
     * @return {@link String}
     */
    private static String getEntityExpansionPoint(Map<String, String> configMap, String addFieldKey, GenTypeEnum genTypeEnum) {
        LinkedHashMap<String, EntityProperty> fieldPropertyMap = getFieldPropertyMap(configMap, addFieldKey);
        if (fieldPropertyMap == null) {
            return "";
        }
        return getPropertyTemplate(fieldPropertyMap, fieldDefine, null, genTypeEnum, null);
    }

    /**
     * 引入包
     */
    public static String addImport(String text) {
        for (Map.Entry<String, String> importEntry : importMap.entrySet()) {
            text = addImportByKey(text, importEntry.getKey(), importEntry.getValue(), "import lombok.NoArgsConstructor;\n");
        }
        return text;
    }

    /**
     * 引入包
     */
    public static String addMainEntityImport(Map<String, String> configMap, String text) {
        String mainEntityName = configMap.get("${mainEntityName}");
        if (StringUtils.isEmpty(mainEntityName)) {
            return text;
        }
        String keyWord = String.format(" %s ", mainEntityName);
        String value = String.format("import %s%s;\n", configMap.get("${entityBasePath}"), mainEntityName);
        String prevImport = String.format("import %s;\n", configMap.get("${entityPath}"));
        text = addImportByKey(text, keyWord, value, prevImport);
        return text;
    }

    /**
     * 引入包
     *
     * @param text       {@link String}
     * @param keyWord    {@link String}
     * @param value      {@link String}
     * @param prevImport {@link String}
     * @return {@link String}
     */
    public static String addImportByKey(String text, String keyWord, String value, String prevImport) {
        if (value != null && text.contains(keyWord) && !text.contains(value) && !text.contains(String.format("public class%s", keyWord))) {
            return text.replace(prevImport, prevImport + value);
        }
        return text;
    }

    /**
     * 获取属性map
     */
    public static LinkedHashMap<String, EntityProperty> getPropertyMap(Map<String, String> configMap) {
        LinkedHashMap<String, EntityProperty> propertyMap = JSON.parseObject(configMap.get("propertyMap"), new TypeReference<LinkedHashMap<String, EntityProperty>>() {
        });
        return propertyMap;
    }

    /**
     * 获取属性map
     */
    public static LinkedHashMap<String, EntityProperty> getPropertyMapComplete(Map<String, String> configMap) {
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getPropertyMap(configMap);
        GeneratorUtils.processRelationField(propertyMap, configMap, false);
        return propertyMap;
    }

    /**
     * 获取新增字段的属性map
     */
    public static LinkedHashMap<String, EntityProperty> getFieldPropertyMap(Map<String, String> configMap, String key) {
        if (StringUtils.isEmpty(configMap.get(key))) {
            return new LinkedHashMap();
        }
        LinkedHashMap<String, EntityProperty> addFieldMap = JSON.parseObject(configMap.get(key), new TypeReference<LinkedHashMap<String, EntityProperty>>() {
        });
        return addFieldMap;
    }

    /**
     * 获取字段的属性map,没有返回空map
     *
     * @param tableConfig {@link Map<String, String>}
     * @param key         {@link String}
     * @return {@link LinkedHashMap< String, EntityProperty>}
     */
    public static LinkedHashMap<String, EntityProperty> getFieldOrDefaultPropertyMap(Map<String, String> tableConfig, String key) {
        LinkedHashMap<String, EntityProperty> propertyMap = new LinkedHashMap<>();
        if (tableConfig.containsKey(key)) {
            propertyMap = getFieldPropertyMap(tableConfig, key);
        }
        return propertyMap;
    }

    public static <T> List<T> getListFromConfig(Map<String, String> configMap, String key, Class clazz) {
        String value = configMap == null ? null : configMap.get(key);
        return value == null ? null : JSON.parseArray(value, clazz);
    }

    /**
     * 通过模板，拼装属性
     *
     * @param fieldPropertyMap {@link LinkedHashMap}
     * @param template         {@link String}
     * @return {@link String}
     */
    public static String getPropertyTemplate(LinkedHashMap<String, EntityProperty> fieldPropertyMap, String template, Set<String> omitSet, GenTypeEnum genTypeEnum, LinkedHashMap<String, EntityProperty> convertMap) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, EntityProperty> entry : fieldPropertyMap.entrySet()) {
                EntityProperty property = entry.getValue();
                if (omitSet != null && omitSet.contains(property.getProperty())) {
                    continue;
                }
                String templateStr = template;
                for (Field field : EntityProperty.class.getDeclaredFields()) {
                    String name = field.getName();
                    field.setAccessible(true);
                    String value = String.valueOf(field.get(property));
                    value = castSourceType(name, value, genTypeEnum, property, convertMap);
                    templateStr = templateStr.replace(String.format("${%s}", name), value);
                }
                stringBuilder.append(templateStr);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 转为原始类型
     *
     * @param name        {@link String}
     * @param value       {@link String}
     * @param genTypeEnum {@link GenTypeEnum}
     * @param property    {@link EntityProperty}
     * @return {@link String}
     */
    public static String castSourceType(String name, String value, GenTypeEnum genTypeEnum, EntityProperty property, LinkedHashMap<String, EntityProperty> convertMap) {
        String sourceType = property.getSourceType();
        /* convertMap的转换 */
        if (convertMap != null && convertMap.containsKey(property.getProperty()) && sourceType == null) {
            EntityProperty convertProperty = convertMap.get(property.getProperty());
            sourceType = convertProperty.getSourceType();
        }
        if (sourceType != null && Objects.equals(name, entityPropertyType) && (Objects.equals(genTypeEnum, GenTypeEnum.PARAM))) {
            value = sourceType;
            if (Objects.equals(CastTypeEnum.OBJECT.getValue(), property.getCastType()) && property.getCustomObject() != null) {
                value = property.getCustomObject();
            }
            if (Objects.equals(CastTypeEnum.OBJECT_LIST.getValue(), property.getCastType()) && Objects.equals(JavaTypeEnum.LIST_OBJECT.getValue(), property.getSourceType())) {
                value = String.format(value, property.getCustomObject(), "");
            }
        }
        return value;
    }

    /**
     * 执行去除方法
     * 方法如替换失败
     * 1.不是/**或者@Override开头
     * 2.结尾不是endStr，有看不到的特殊字符
     *
     * @param text         {@link String}
     * @param method       {@link String}
     * @param pair         {@link Pair<Character, Character>}
     * @param beginStrList {@link List<String>}
     * @param endStr       {@link String}
     * @return {@link String}
     */
    public static String doRemoveMethod(String text, String method, Pair<Character, Character> pair, List<String> beginStrList, String startStr, String endStr) {
        String methodPart = String.format(" %s(", method);
        if (text.contains(methodPart)) {
            int index = text.indexOf(methodPart) + methodPart.length() - 1;
            String frontPart = text.substring(0, index);
            int beginIndex = 0;
            for (String beginStr : beginStrList) {
                int candidateIndex = frontPart.lastIndexOf(beginStr);
                beginIndex = candidateIndex >= beginIndex ? candidateIndex : beginIndex;
            }
            String backPart = text.substring(index);
            int paramIndex = 0;
            /* 跳过参数 */
            if (Objects.equals(pair.getLeft(), '{')) {
                paramIndex = StrUtils.getEndIndex(backPart, '(', ')');
                backPart = backPart.substring(paramIndex);
            }
            int backIndex = index + paramIndex + StrUtils.getEndIndex(backPart, pair.getLeft(), pair.getRight());
            String methodContent = startStr + text.substring(beginIndex, backIndex) + endStr;
            text = text.replace(methodContent, "");
        }
        return text;
    }

    /**
     * 去除mapper抽象方法
     *
     * @param text   {@link String}
     * @param method {@link String}
     * @return {@link String}
     */
    public static String removeMapperMethod(String text, String method) {
        return doRemoveMethod(text, method, Pair.of('(', ')'), mapperMethodBeginList, "    ", ";\n\n");
    }

    /**
     * 去除抽象方法
     *
     * @param text   {@link String}
     * @param method {@link String}
     * @return {@link String}
     */
    public static String removeAbstractMethod(String text, String method) {
        return doRemoveMethod(text, method, Pair.of('(', ')'), Arrays.asList("    /**"), "", ";\n\n");
    }

    /**
     * 替换method中的参数并去除方法,在替换后使用
     * 后置替换方法
     *
     * @param text      {@link String}
     * @param method    {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    public static String replaceParamAndRemoveMethod(String text, String method, Map<String, String> configMap) {
        method = doGenerator(method, configMap);
        return removeMethod(text, method);
    }

    /**
     * 去除方法,在替换后使用
     *
     * @param text   {@link String}
     * @param method {@link String}
     * @return {@link String}
     */
    public static String removeMethod(String text, String method) {
        return doRemoveMethod(text, method, Pair.of('{', '}'), Arrays.asList("    /**", "    @Override"), "", "\n\n");
    }

    /**
     * 去除方法
     *
     * @param text   {@link String}
     * @param method {@link String}
     * @return {@link String}
     */
    public static String removeXmlMethod(String text, String method) {
        String xmlMethodContent = getXmlMethod(text, method);
        if (StringUtils.isNotEmpty(xmlMethodContent)) {
            text = text.replace(xmlMethodContent, "");
        }
        return text;
    }

    /**
     * 去除方法
     *
     * @param text   {@link String}
     * @param method {@link String}
     * @return {@link String}
     */
    public static String getXmlMethod(String text, String method) {
        List<Pair<String, String>> methodRegexList = Lists.newArrayList();
        for (Pair<String, String> pair : xmlMethodStartRegexList) {
            methodRegexList.add(Pair.of(String.format(pair.getLeft(), method), pair.getRight()));
        }
        return StrUtils.getFirstRegexContentByPairList(text, methodRegexList);
    }

    /**
     * 去除内容
     *
     * @param text        {@link String}
     * @param contentList {@link List<String>}
     * @return {@link String}
     */
    public static String removeContent(String text, List<String> contentList) {
        for (String content : contentList) {
            text = text.replace(content, "");
        }
        return text;
    }


    public static boolean relationType2Main(Map<String, String> configMap) {
        if (relationType2(configMap) && !relationSlave(configMap)) {
            return true;
        }
        return false;
    }

    private static boolean relationType2(Map<String, String> configMap) {
        Integer relationType = Integer.valueOf(configMap.getOrDefault("${relationType}", "1"));
        if (Objects.equals(relationType, 2)) {
            return true;
        }
        return false;
    }

    /**
     * 关系表type3的子表
     *
     * @param configMap {@link }
     * @return {@link boolean}
     */
    public static boolean relationType3Slave(Map<String, String> configMap) {
        if (GeneratorUtils.relationType(configMap, GeneratorUtils.relationType3) && relationSlave(configMap)) {
            return true;
        }
        return false;
    }

    /**
     * 关系表type3的主表
     *
     * @param configMap {@link }
     * @return {@link boolean}
     */
    public static boolean relationType3Main(Map<String, String> configMap) {
        if (GeneratorUtils.relationType(configMap, GeneratorUtils.relationType3) && relationMain(configMap)) {
            return true;
        }
        return false;
    }

    /**
     * 关系表
     *
     * @param configMap         {@link Map}
     * @param queryRelationType {@link Integer}
     * @return {@link boolean}
     */
    public static boolean relationType(Map<String, String> configMap, Integer queryRelationType) {
        Integer relationType = Integer.valueOf(configMap.getOrDefault("${relationType}", "1"));
        if (Objects.equals(relationType, queryRelationType)) {
            return true;
        }
        return false;
    }

    /**
     * 关系表子表
     *
     * @param configMap {@link }
     * @return {@link boolean}
     */
    public static boolean relationSlave(Map<String, String> configMap) {
        if (Objects.equals(configMap.get("${tableName}"), configMap.get("${slaveTable}"))) {
            return true;
        }
        return false;
    }

    /**
     * 关系表主表
     *
     * @param configMap {@link }
     * @return {@link boolean}
     */
    public static boolean relationMain(Map<String, String> configMap) {
        if (Objects.equals(configMap.get("${tableName}"), configMap.get("${mainTable}"))) {
            return true;
        }
        return false;
    }

    /**
     * 关系表
     *
     * @param configMap {@link }
     * @return {@link boolean}
     */
    public static boolean notRelationTable(Map<String, String> configMap) {
        Integer relationType = Integer.valueOf(configMap.getOrDefault("${relationType}", "1"));
        if (Objects.equals(relationType, 1)) {
            return true;
        }
        return false;
    }

    /**
     * 增加关系表字段
     *
     * @param configMap {@link LinkedHashMap<String, EntityProperty>}
     * @param configMap {@link Map<String, String>}
     * @return {@link LinkedHashMap< String, EntityProperty>}
     */
    public static void processRelationField(LinkedHashMap<String, EntityProperty> propertyMap, Map<String, String> configMap, Boolean needRemove) {
        if (relationType(configMap, relationType2)) {
            EntityProperty entityProperty = getFieldPropertyMap(configMap, "${paramMap}").get(GeneratorConfigUtils.commonContentField);
            propertyMap.put(GeneratorConfigUtils.commonContentField, entityProperty);
            if (needRemove) {
                propertyMap.remove(GeneratorConfigUtils.commonContentIdField);
            }
        }
    }

    /**
     * 通过模板生成代码
     *
     * @param configMap   {@link Map}
     * @param genTypeEnum {@link GenTypeEnum}
     */
    public static void processByTemplate(Map<String, String> configMap, GenTypeEnum genTypeEnum) {
        /* 读取模板-输入 */
        String sourcePath = GeneratorUtils.getTemplateSourcePath(genTypeEnum, configMap);
        String resultPath = GeneratorUtils.getResultPath(genTypeEnum, GeneratorProject.abstractPathMap, configMap);
        configMap.put("resultPath", resultPath);
        configMap.put("genTypeEnum", genTypeEnum.name());
        String generatorText = FileUtils.readFileByChars(sourcePath);
        /* 前置处理 */
        generatorText = GeneratorUtils.beforeOrAfterProcess(genTypeEnum.getValue(), "beforeProcess", generatorText, configMap);
        /* 模板替换 */
        generatorText = GeneratorUtils.processTemplate(generatorText, configMap);
        /* 后置处理 */
        generatorText = GeneratorUtils.beforeOrAfterProcess(genTypeEnum.getValue(), "afterProcess", generatorText, configMap);
        /* 写文件-输出 */
        if (StringUtils.isNotEmpty(generatorText)) {
            FileUtil.writeUtf8String(generatorText, configMap.get("resultPath"));
        }
        log.info("{}_{}代码生成成功", configMap.get("${entityName}"), genTypeEnum.getValue());
    }

    /**
     * 通过模板生成小工具代码
     *
     * @param configMap {@link Map}
     */
    public static void processByTemplate(Map<String, String> configMap) {
        /* 读取模板-输入 */
        String resultPath = configMap.get(GenConstant.KEY_OUTPUT_PATH);
        String genText = configMap.get(GenConstant.KEY_TEMPLATE);
        /* 模板替换 */
        genText = GeneratorUtils.processTemplate(genText, configMap);
        /* 写文件-输出 */
        FileUtil.writeUtf8String(genText, resultPath);
        log.info("{}代码生成成功", configMap.get(GenConstant.KEY_GEN_TYPE_METHOD));
    }

    public static String beforeOrAfterProcess(String genType, String methodName, String generatorText, Map<String, String> configMap) {
        String resultText = generatorText;
        String className = String.format(GenConfigConstant.GEN_PACKAGE_NAME + ".utils.Generator%sUtils", StrUtil.upperFirst(genType));
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName, String.class, Map.class);
            method.setAccessible(true);
            resultText = (String) method.invoke(clazz, generatorText, configMap);
        } catch (ClassNotFoundException e) {
            log.warn("beforeOrAfterProcess_{}_ClassNotFoundException", genType);
        } catch (NoSuchMethodException e) {
            log.warn("beforeOrAfterProcess_{}_{}_NoSuchMethodException", genType, methodName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultText;
    }

    /**
     * 获取忽略的set
     *
     * @param configMap   {@link Map}
     * @param genTypeEnum {@link GenTypeEnum}
     * @return {@link boolean}
     */
    public static Set<String> getIgnoreSet(Map<String, String> configMap, GenTypeEnum genTypeEnum) {
        Set<String> ignoreSet = commonFieldSet;
        if (Objects.equals(genTypeEnum, GenTypeEnum.ENTITY)) {
            ignoreSet = null;
        }
        if (Objects.equals(genTypeEnum, GenTypeEnum.PARAM)) {
            ignoreSet = ignoreResultFieldSet;
        }
        return ignoreSet;
    }

    public static String formatDesc(String desc) {
        return desc.replace("（", " ").replace("(", " ");
    }

    public static String getFormatDesc(EntityProperty entityProperty) {
        if (StringUtils.isEmpty(entityProperty.getDesc())) {
            return "";
        }
        if (needFormatDesc(entityProperty)) {
            return formatDesc(entityProperty.getDesc()).split(" ")[0];
        }
        return entityProperty.getDesc();
    }

    /**
     * 是否需要格式化描述
     *
     * @param entityProperty {@link EntityProperty}
     * @return {@link String}
     */
    private static Boolean needFormatDesc(EntityProperty entityProperty) {
        if (entityProperty != null && (Objects.equals(entityProperty.getType(), GeneratorUtils.typeInteger) || Objects.equals(entityProperty.getType(), GeneratorUtils.typeByte) || Objects.equals(entityProperty.getType(), GeneratorUtils.typeString))
                && (entityProperty.getDesc().contains("1-") || entityProperty.getDesc().contains("1:"))) {
            return true;
        }
        return false;
    }

    /**
     * 下拉组件方法
     *
     * @param type {@link String}
     * @return {@link Boolean}
     */
    public static Boolean isNumeric(String type) {
        if (numericSet.contains(type)) {
            return true;
        }
        return false;
    }

    /**
     * 处理数字类型，去掉引号
     *
     * @param text {@link String}
     * @return {@link String}
     */
    public static String processNumeric(String text, String key) {
        return text.replace(String.format("\"%s\"", key), key);
    }

    /**
     * 替换子项模板
     *
     * @param template       {@link String}
     * @param entityProperty {@link EntityProperty}
     * @param itemConfigMap  {@link Map<String, String>}
     * @return {@link String}
     */
    public static String replaceItemTemplate(String template, EntityProperty entityProperty, Map<String, String> itemConfigMap) {
        if (itemConfigMap != null) {
            for (Map.Entry<String, String> entry : itemConfigMap.entrySet()) {
                template = template.replace(entry.getKey(), entry.getValue());
            }
        }
        if (entityProperty != null) {
            template = template.replace("{{desc}}", GeneratorUtils.getFormatDesc(entityProperty));
            template = template.replace("{{upperFirstProperty}}", StrUtil.upperFirst(entityProperty.getProperty()));
            template = template.replace("{{type}}", entityProperty.getType() == null ? "" : entityProperty.getType());
        }
        return template;
    }

    /**
     * 替换模板和子项模板
     *
     * @param template       {@link String}
     * @param configMap      {@link Map<String,String>}
     * @param entityProperty {@link EntityProperty}
     * @param itemConfigMap  {@link Map<String,String>}
     * @return {@link String}
     */
    public static String replaceAllTemplate(String template, Map<String, String> configMap, EntityProperty entityProperty, Map<String, String> itemConfigMap) {
        template = doGenerator(template, configMap);
        template = replaceItemTemplate(template, entityProperty, itemConfigMap);
        return template;
    }

    /**
     * 删除无用的数据
     *
     * @param text              {@link String}
     * @param deleteMethodList  {@link List<String>}
     * @param deleteContentList {@link List<String>}
     * @param replacePairList   {@link List<Pair<String, String>>}
     * @return {@link String}
     */
    public static String deleteUnUsedTextAndMethod(String text, List<String> deleteMethodList, List<String> deleteContentList, List<Pair<String, String>> replacePairList) {
        if (CollectionUtils.isNotEmpty(deleteContentList)) {
            for (String str : deleteContentList) {
                text = text.replace(str, "");
            }
        }
        if (CollectionUtils.isNotEmpty(replacePairList)) {
            for (Pair<String, String> pair : replacePairList) {
                text = text.replace(pair.getLeft(), pair.getRight());
            }
        }
        if (CollectionUtils.isNotEmpty(deleteMethodList)) {
            for (String method : deleteMethodList) {
                text = GeneratorUtils.removeMethod(text, method);
                text = GeneratorUtils.removeAbstractMethod(text, method);
            }
        }
        return text;
    }

    /**
     * 删除无用的数据
     * 如果方法没被删除，考虑1.抽象方法，xml方法 2.不是/** overwrite开头 3.方法结尾不是\n\n,注意空行和空行后是否有空格
     *
     * @param text              {@link String}
     * @param genTypeEnum       {@link String}
     * @param deleteMethodList  {@link List<String>}
     * @param deleteContentList {@link List<String>}
     * @param replacePairList   {@link List<Pair<String, String>>}
     * @return {@link String}
     */
    public static String deleteUnUsedTextAndMethod(String text, String genTypeEnum, List<String> deleteMethodList, List<String> deleteContentList, List<Pair<String, String>> replacePairList) {
        if (CollectionUtils.isNotEmpty(deleteContentList)) {
            for (String str : deleteContentList) {
                text = text.replace(str, "");
            }
        }
        if (CollectionUtils.isNotEmpty(replacePairList)) {
            for (Pair<String, String> pair : replacePairList) {
                text = text.replace(pair.getLeft(), pair.getRight());
            }
        }
        if (CollectionUtils.isNotEmpty(deleteMethodList)) {
            for (String method : deleteMethodList) {
                if (abstractMethodGenTypeSet.contains(genTypeEnum)) {
                    text = GeneratorUtils.removeAbstractMethod(text, method);
                } else {
                    text = GeneratorUtils.removeMethod(text, method);
                }
            }
        }
        return text;
    }

    /**
     * 查询字段转为EntityProperty
     *
     * @param queryFieldList {@link List<GenQueryField>}
     * @return {@link LinkedHashMap<String,EntityProperty>}
     */
    public static LinkedHashMap<String, EntityProperty> queryFieldConverterToProperty(List<GenQueryField> queryFieldList) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (GenQueryField queryField : queryFieldList) {
            String frontWidth = getFrontWidth(queryField.getQueryFieldDesc());
            EntityProperty entityProperty = EntityProperty.builder().property(queryField.getQueryField()).desc(queryField.getQueryFieldDesc()).frontWidth(frontWidth).build();
            linkedHashMap.put(queryField.getQueryField(), entityProperty);
        }
        return linkedHashMap;
    }

    /**
     * 关系表转为EntityProperty
     *
     * @param relationTableList {@link List<GenRelationTable>}
     * @return {@link LinkedHashMap<String,EntityProperty>}
     */
    public static LinkedHashMap<String, EntityProperty> relationTableConverterToProperty(List<GenRelationTable> relationTableList) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (GenRelationTable relationTable : relationTableList) {
            String shortSlaveEntityName = relationTable.getSlaveTable() != null ? StrUtil.toCamelCase(relationTable.getSlaveTable(), '_') : "";
            String relationField = relationTable.getSlaveRelationField() != null ? StrUtil.toCamelCase(relationTable.getSlaveRelationField(), '_') : "";
            /* 根据关系类生成EntityProperty */
            if (Objects.equals(relationTable.getRelationType(), String.valueOf(GeneratorUtils.relationType2))) {
                String property = relationTable.getSlaveRelationField().replace("Id", "");
                linkedHashMap.put(property, EntityProperty.builder().property(property).desc(relationTable.getSlaveEntityDesc()).type(StrUtil.upperFirst(shortSlaveEntityName)).relationType(relationTable.getRelationType()).relationField(relationField).build());
            } else if (Objects.equals(relationTable.getRelationType(), String.valueOf(GeneratorUtils.relationType3))) {
                linkedHashMap.put(shortSlaveEntityName, EntityProperty.builder().property(shortSlaveEntityName).desc(relationTable.getSlaveEntityDesc()).type(StrUtil.upperFirst(shortSlaveEntityName)).relationType(relationTable.getRelationType()).relationField(relationField).build());
            }
        }
        return linkedHashMap;
    }

    /**
     * 唯一字段转为EntityProperty
     *
     * @param uniqueFieldList {@link List<GenUniqueField>}
     * @return {@link LinkedHashMap<String,EntityProperty>}
     */
    public static LinkedHashMap<String, EntityProperty> uniqueFieldConverterToProperty(List<GenUniqueField> uniqueFieldList) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (GenUniqueField uniqueField : uniqueFieldList) {
            if (uniqueField == null || CollectionUtils.isEmpty(uniqueField.getUniqueFields())) {
                continue;
            }
            for (String field : uniqueField.getUniqueFields()) {
                EntityProperty entityProperty = EntityProperty.builder().property(field).checkUniqueType(uniqueField.getCheckUniqueType()).build();
                linkedHashMap.put(field, entityProperty);
            }
        }
        return linkedHashMap;
    }

    /**
     * 字段列表转为EntityProperty
     *
     * @param fieldList {@link List<String>}
     * @return {@link LinkedHashMap<String,EntityProperty>}
     */
    public static LinkedHashMap<String, EntityProperty> fieldListConverterToProperty(List<String> fieldList) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (String field : fieldList) {
            EntityProperty entityProperty = EntityProperty.builder().property(field).build();
            linkedHashMap.put(field, entityProperty);
        }
        return linkedHashMap;
    }

    /**
     * 汉字长度20，其他10
     *
     * @param queryFieldDesc {@link String}
     * @return {@link String}
     */
    private static String getFrontWidth(String queryFieldDesc) {
        if (StringUtils.isEmpty(queryFieldDesc)) {
            return "0";
        }
        Integer frontWidth = 15;
        char[] chars = queryFieldDesc.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            frontWidth += 10;
            if (StrUtils.isChinese(String.valueOf(chars[i]))) {
                frontWidth += 10;
            }
        }
        return String.valueOf(frontWidth);
    }

    /**
     * 通过模板和生成表列表生成
     *
     * @param genTableList
     * @param template
     * @return
     */
    public static String genByTemplateAndGenTableList(List<GenTable> genTableList, String template) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GenTable genTable : genTableList) {
            stringBuilder.append(genByTemplateAndGenTable(genTable, template));
        }
        return stringBuilder.toString();
    }


    /**
     * 通过模板和生成表生成
     *
     * @param genTable
     * @param template
     * @return
     */
    public static String genByTemplateAndGenTable(GenTable genTable, String template) {
        return template
                .replace("${tableName}", genTable.getTableName())
                .replace("${entityName}", genTable.getEntityName())
                .replace("${entityDesc}", genTable.getEntityDesc())
                .replace("${bizClassName}", genTable.getBizClassName())
                .replace("${requestMapping}", genTable.getRequestMapping())
                .replace("${controllerType}", genTable.getControllerType());
    }

    /**
     * 判断是否枚举
     *
     * @param desc {@link String}
     * @param type {@link String}
     * @return {@link Boolean}
     */
    public static Boolean getIsEnum(String desc, String type) {
        if (StringUtils.isEmpty(desc)) {
            return false;
        }
        if ((Objects.equals(type, GeneratorUtils.typeInteger) || Objects.equals(type, GeneratorUtils.typeByte) || Objects.equals(type, GeneratorUtils.typeString))
                && (desc.contains("1-") || desc.contains("1:"))) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否时间类型
     *
     * @param property {@link String}
     * @return {@link Boolean}
     */
    public static Boolean getIsTime(String property) {
        if (StringUtils.isEmpty(property)) {
            return false;
        }
        if (property.endsWith("Date") || property.endsWith("Time")) {
            return true;
        }
        return false;
    }

    /**
     * 获取类型 DataTypeEnum
     *
     * @param entityProperty {@link EntityProperty}
     * @return {@link Boolean}
     */
    public static String getDataType(EntityProperty entityProperty) {
        String result = null;
        if (getIsEnum(entityProperty.getDesc(), entityProperty.getType())) {
            result = DataTypeEnum.ENUM_TYPE.getKeyword();
            if (Objects.equals(entityProperty.getType(), JavaTypeEnum.STRING.getValue())) {
                result = result + "_" + DataTypeEnum.CHAR_TYPE.getKeyword();
            } else {
                result = result + "_" + DataTypeEnum.NUMBER_TYPE.getKeyword();
            }
        } else if (getIsTime(entityProperty.getProperty())) {
            result = DataTypeEnum.DATE_TYPE.getKeyword();
        } else if (Objects.equals(entityProperty.getType(), JavaTypeEnum.STRING.getValue())) {
            result = DataTypeEnum.CHAR_TYPE.getKeyword();
        } else if (getIsList(entityProperty.getType())) {
            result = DataTypeEnum.LIST_TYPE.getKeyword();
        } else if (Objects.equals(entityProperty.getType(), JavaTypeEnum.LONG.getValue())) {
            result = DataTypeEnum.LONG_NUMBER_TYPE.getKeyword();
        } else if (Objects.equals(entityProperty.getType(), JavaTypeEnum.BOOLEAN.getValue())) {
            result = DataTypeEnum.BOOLEAN_TYPE.getKeyword();
        } else if (Objects.equals(entityProperty.getType(), JavaTypeEnum.INTEGER.getValue())
                || Objects.equals(entityProperty.getType(), JavaTypeEnum.BYTE.getValue())) {
            result = DataTypeEnum.NUMBER_TYPE.getKeyword();
        } else if (Objects.equals(entityProperty.getType(), JavaTypeEnum.DOUBLE.getValue())) {
            result = DataTypeEnum.FLOAT_TYPE.getKeyword();
        } else {
            result = DataTypeEnum.OBJECT_TYPE.getKeyword();
        }
        return result;
    }

    /**
     * 判断是否集合类型
     *
     * @param property {@link String}
     * @return {@link boolean}
     */
    private static boolean getIsList(String property) {
        if (StringUtils.isEmpty(property)) {
            return false;
        }
        if (property.startsWith("List<")) {
            return true;
        }
        return false;
    }

    /**
     * 获取唯一字段列表
     *
     * @param configMap {@link Map<String, String>}
     * @return {@link List<String>}
     */
    public static List<String> getUniqueFieldList(Map<String, String> configMap) {
        List<String> uniqueFieldList = Lists.newArrayList();
        String uniqueFieldListStr = configMap.get(GenConstant.keyUniqueFieldList);
        if (StringUtils.isNotEmpty(uniqueFieldListStr)) {
            List<GenUniqueField> genUniqueFields = JSON.parseArray(uniqueFieldListStr, GenUniqueField.class);
            for (GenUniqueField genUniqueField : genUniqueFields) {
                uniqueFieldList.addAll(genUniqueField.getUniqueFields());
            }
        }
        return uniqueFieldList;
    }

    /**
     * 替换类名
     *
     * @param path      {@link String}
     * @param className {@link String}
     * @return {@link String}
     */
    public static String replaceClassName(String path, String className) {
        return path.substring(0, path.lastIndexOf("/") + 1) + className;
    }
}
