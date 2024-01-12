package com.xinxian.generator.utils;

import cn.hutool.core.io.FileUtil;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.enums.DataTypeEnum;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GeneratorUtils;

import java.util.*;

/**
 * @ClassName GeneratorTestUtils
 * @Description 自动生成测试工具包
 * @Author lmy
 * @Date 2023/08/04 10:08
 */
public class GeneratorTestUtils {

    private static final Integer defaultValue = 99;
    private static final Integer updateDefaultValue = 100;
    private static final String updateTemplate = "param.set{{upperFirstProperty}}(\"%s\");";
    private static final String updateSlaveTemplate = "param.get${entityName}List().get(0).set{{upperFirstProperty}}(\"s\");\n";
    private static final String updateSlaveLocation = "        ApiResult<Boolean> result = ${shortClassName}Controller.update(request, param);\n";
    private static final String basicTestInjectEnd = "    /* BasicTestInjectEnd */";
    private static final String basicTestExecEnd = "        /* BasicTestExecEnd */";
    private static final String basicTestInjectTemplate = "    @Autowired\n    private ${entityName}Test ${shortEntityName}Test;\n";
    private static final String basicTestExecTemplate = "//        ${shortEntityName}Test.execTest();\n";
    private static final String basicTestTemplateName = "BasicTestTemplate";
    private static final String setRelationIdTemplate = "                param.set${slaveRelationFieldUpperFirst}(item.get${slaveRelationFieldUpperFirst}());";
    private static final String setRelationIdLocation = "param.setId(item.getId());\n";
    private static final String compareIgnoreSet = "private Set<String> compareIgnoreSet = new HashSet<>();";
    private static final String compareIgnoreSetTemplate = "private Set<String> compareIgnoreSet = new HashSet<>(Arrays.asList(${compareIgnoreField}));";
    private static final String importTemplate = "import ${packageBase}.dto.vo.${entityName}VO;\n";
    private static final String importLocation = "import com.xinxian.user.passport.facade.common.util.BaseParamUtils;\n";
    private static final String setParamSlaveTemplate = ".${shortEntityName}List(get${entityName}VOList())\n";
    private static final String setParamTemplate = ".${shortEntityName}List(null)\n";
    private static final String doCompareLocation = "        Assert.assertTrue(msg, same);\n";
    private static final String doCompareTemplate =
            "       for (int i = 0; i < param.get${entityName}List().size(); i++) {\n" +
                    "            Boolean sameSlave = compareParamAndResult(param.get${entityName}List().get(i), result.getData().get${entityName}List().get(i), null);\n" +
                    "            Assert.assertTrue(msg, sameSlave);\n" +
                    "        }\n";
    private static final String compareSlaveLocation = "\n    /**\n     * 比较参数和明细\n";
    private static final String compareSlaveTemplate =
            "    /**\n" +
                    "     * 比较Slave入参和结果\n" +
                    "     *\n" +
                    "     * @param param  {@link ${entityName}VO}\n" +
                    "     * @param result {@link ${entityName}VO}\n" +
                    "     * @return {@link Boolean}\n" +
                    "     */\n" +
                    "    private Boolean compareParamAndResult(${entityName}VO param, ${entityName}VO result, Set<String> ignoreSet) {\n" +
                    "        Boolean same = false;\n" +
                    "        try {\n" +
                    "            same = BeanUtils.compareObjectValue(param, result, ignoreSet);\n" +
                    "        } catch (IllegalAccessException e) {\n" +
                    "            throw new RuntimeException(e);\n" +
                    "        }\n" +
                    "        return same;\n" +
                    "    }\n\n";
    private static final String setRelation3IdTemplate = "                ${mainEntityName}Result detail = ${shortMainEntityName}Controller.detail(request, param).getData();\n" +
            "                param.set${entityName}List(detail.get${entityName}List());\n" +
            "                param.set${entityName}IdList(detail.get${entityName}IdList());\n";
    private static final String getParamTemplate = "    /**\n" +
            "     * 获取子表参数列表\n" +
            "     *\n" +
            "     * @return {@link List<${entityName}VO>}\n" +
            "     */\n" +
            "    private List<${entityName}VO> get${entityName}VOList() {\n" +
            "        List<${entityName}VO> list = Lists.newArrayList();\n" +
            "        list.add(get${entityName}VO());\n" +
            "        log.info(\"关联表3子表param列表：\" + JSON.toJSONString(list));\n" +
            "        return list;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 获取子表参数\n" +
            "     *\n" +
            "     * @return {@link ${entityName}VO}\n" +
            "     */\n" +
            "    private ${entityName}VO get${entityName}VO() {\n" +
            "        ${entityName}VO param = ${entityName}VO.builder()\n" +
            "${buildParam}" +
            "                .build();\n" +
            "        log.info(\"关联表3子表param：\" + JSON.toJSONString(param));\n" +
            "        return param;\n" +
            "    }\n\n";
    private static final String getParamLocation = "    /**\n     * 测试${mainEntityDesc}增加\n     */\n";

    /* 参数转实体模板map */
    public static Map<String, String> paramTemplateMap = new HashMap() {
        {
            put(DataTypeEnum.ENUM_TYPE.getKeyword() + "_" + DataTypeEnum.CHAR_TYPE.getKeyword(), "                .{{property}}(\"{{firstEnum}}\")\n");
            put(DataTypeEnum.ENUM_TYPE.getKeyword() + "_" + DataTypeEnum.NUMBER_TYPE.getKeyword(), "                .{{property}}({{firstEnum}})\n");
            put(DataTypeEnum.LONG_NUMBER_TYPE.getKeyword(), String.format("                .{{property}}(%sL)\n", defaultValue));
            put(DataTypeEnum.DATE_TYPE.getKeyword(), "                .{{property}}(DateUtil.date())\n");
            put(DataTypeEnum.BOOLEAN_TYPE.getKeyword(), "                .{{property}}(true)\n");
            put(DataTypeEnum.CHAR_TYPE.getKeyword(), String.format("                .{{property}}(\"%s\")\n", defaultValue));
            put(DataTypeEnum.FLOAT_TYPE.getKeyword(), String.format("                .{{property}}(\"%s.9\")\n", defaultValue));
            put(DataTypeEnum.OBJECT_TYPE.getKeyword(), "                .{{property}}(null)\n");
            put(DataTypeEnum.LIST_TYPE.getKeyword(), "                .{{property}}(null)\n");
            put(DataTypeEnum.NUMBER_TYPE.getKeyword(), String.format("                .{{property}}(%s)\n", defaultValue));
        }
    };

    /**
     * 前置处理
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String,String>}
     * @return {@link String}
     */
    public static String beforeProcess(String genText, Map<String, String> configMap) {
        return genText;
    }

    /**
     * 后置处理
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String,String>}
     * @return {@link String}
     */
    public static String afterProcess(String genText, Map<String, String> configMap) {
        processBasicTest(configMap);
        LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.getFieldOrDefaultPropertyMap(configMap, GenConstant.keyAllParamMap);
        genText = processCompareIgnoreSet(genText, configMap, propertyMap);
        genText = processBuildParam(genText, configMap, propertyMap);
        genText = processUpdateParam(genText, configMap, propertyMap);
        genText = processRelationId(genText, configMap, propertyMap);
        genText = processRelation3Slave(genText, configMap, propertyMap);
        return genText;
    }

    /**
     * 处理关系表3子表
     *
     * @param genText     {@link String}
     * @param configMap   {@link Map< String, String>}
     * @param propertyMap {@link LinkedHashMap< String, EntityProperty>}
     * @return {@link String}
     */
    private static String processRelation3Slave(String genText, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        if (!GeneratorUtils.relationType3Slave(configMap)) {
            return genText;
        }
        /* 引入包 */
        genText = GeneratorUtils.appendHead(genText, importLocation, importTemplate, configMap);
        /* 设置参数 */
        genText = GeneratorUtils.replaceTemplate(genText, setParamTemplate, setParamSlaveTemplate, configMap);
        /* 获取参数 */
        String relation3SlaveParam = getRelation3SlaveParam(configMap, propertyMap);
        /* 修改参数 */
        String updateParam = getUpdateParam(configMap, propertyMap, updateSlaveTemplate);
        genText = GeneratorUtils.replaceTemplateAndAppendHead(genText, updateSlaveLocation, updateParam, configMap);
        /* 比较Slave入参和结果 */
        genText = GeneratorUtils.appendHead(genText, compareSlaveLocation, compareSlaveTemplate, configMap);
        /* 执行比较 */
        genText = GeneratorUtils.appendHead(genText, doCompareLocation, doCompareTemplate, configMap);
        return genText;
    }

    /**
     * 获取关系表3参数集合
     *
     * @param configMap   {@link Map<String,String>}
     * @param propertyMap {@link LinkedHashMap<String,EntityProperty>}
     * @return {@link String}
     */
    private static String getRelation3SlaveParam(Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        String buildParamField = getBuildParamField(configMap, propertyMap);
        String param = getParamTemplate.replace("${buildParam}", buildParamField);
        return GeneratorUtils.processTemplate(param, configMap);
    }

    /**
     * 处理比较忽略集合
     *
     * @param genText     {@link String}
     * @param configMap   {@link Map<String,String>}
     * @param propertyMap {@link LinkedHashMap<String,EntityProperty>}
     * @return {@link String}
     */
    private static String processCompareIgnoreSet(String genText, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        if (GeneratorUtils.relationMain(configMap)) {
            String compareIgnoreField = GeneratorUtils.relationType2Main(configMap) ? "\"${slaveRelationField}\"" : "\"${shortSlaveEntityName}List\", \"${shortSlaveEntityName}IdList\"";
            String ignoreSet = compareIgnoreSetTemplate.replace("${compareIgnoreField}", compareIgnoreField);
            genText = genText.replace(compareIgnoreSet, GeneratorUtils.processTemplate(ignoreSet, configMap));
        }
        return genText;
    }

    /**
     * 处理关系表id
     *
     * @param genText     {@link String}
     * @param configMap   {@link Map<String,String>}
     * @param propertyMap {@link LinkedHashMap< String,EntityProperty>}
     * @return {@link String}
     */
    private static String processRelationId(String genText, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        if (GeneratorUtils.relationType2Main(configMap)) {
            String setRelationId = GeneratorUtils.processTemplate(setRelationIdTemplate, configMap);
            genText = genText.replace(GenConstant.keySetRelationId, setRelationId);
        } else if (GeneratorUtils.relationType3Slave(configMap)) {
            genText = GeneratorUtils.appendTail(genText, setRelationIdLocation, setRelation3IdTemplate, configMap);
        } else {
            genText = genText.replace(GenConstant.keySetRelationId + GenConstant.ENTER, "");
        }
        return genText;
    }

    /**
     * 处理基础测试
     * 目标地址存在优先用目标地址的
     * 判断是否已存在当前类
     *
     * @param configMap {@link Map<String,String>}
     */
    private static void processBasicTest(Map<String, String> configMap) {
        String resultPath = GeneratorUtils.replaceClassName(configMap.get("resultPath"), "BasicTest.java");
        String text = null;
        /* 跳过关系表3子表 */
        if (GeneratorUtils.relationType3Slave(configMap)) {
            return;
        }
        if (FileUtil.exist(resultPath)) {
            text = FileUtil.readUtf8String(resultPath);
        }
        /* 模板替换 */
        String basicTestInjectText = GeneratorUtils.processTemplate(basicTestInjectTemplate, configMap);
        String basicTestExecText = GeneratorUtils.processTemplate(basicTestExecTemplate, configMap);
        if (!text.contains(basicTestInjectText)) {
            text = text.replace(basicTestInjectEnd, basicTestInjectText + basicTestInjectEnd);
        }
        if (!text.contains(basicTestExecText)) {
            text = text.replace(basicTestExecEnd, basicTestExecText + basicTestExecEnd);
        }
        /* 写文件-输出 */
        FileUtil.writeUtf8String(text, resultPath);
    }

    /**
     * 处理BuildParam
     */
    public static String processBuildParam(String text, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        String entityName = configMap.get("${entityName}");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("${entityName}Param param = ${entityName}Param.builder()\n".replace("${entityName}", entityName));
        /* 获取参数字段 */
        String buildParamField = getBuildParamField(configMap, propertyMap);
        stringBuilder.append(buildParamField);
        stringBuilder.append("                .build();");
        String buildParam = stringBuilder.toString();
        text = text.replace("${buildParam}", buildParam);
        return text;
    }

    /**
     * 获取参数字段
     */
    public static String getBuildParamField(Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            if (GenConstant.BASE_TEST_ADD_IGNORE_SET.contains(name)) {
                continue;
            }
            /* 关系2子表id&&关系3子表中的主表id */
            if ((GeneratorUtils.relationType2Main(configMap) || GeneratorUtils.relationType3Slave(configMap)) && Objects.equals(entry.getKey(), configMap.get("${slaveRelationField}"))) {
                continue;
            }
            EntityProperty entityProperty = entry.getValue();
            String dataType = GeneratorUtils.getDataType(entityProperty);
            String fieldItem = GeneratorUtils.replaceProperty(paramTemplateMap.get(dataType), entityProperty);
            stringBuilder.append(fieldItem);
        }
        if (!GeneratorUtils.relationType3Slave(configMap)) {
            stringBuilder.append("                .page(0)\n");
            stringBuilder.append("                .pageSize(0)\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 处理UpdateParam
     * 优先处理非枚举的string类型
     */
    public static String processUpdateParam(String text, Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap) {
        String updateParam = getUpdateParam(configMap, propertyMap, updateTemplate);
        text = text.replace("${updateParam}", updateParam);
        return text;
    }

    /**
     * 处理UpdateParam
     * 优先处理非枚举的string类型
     */
    public static String getUpdateParam(Map<String, String> configMap, LinkedHashMap<String, EntityProperty> propertyMap, String template) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> uniqueFieldList = GeneratorUtils.getUniqueFieldList(configMap);
        for (Map.Entry<String, EntityProperty> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            if (GenConstant.BASE_TEST_ADD_IGNORE_SET.contains(name)) {
                continue;
            }
            EntityProperty entityProperty = entry.getValue();
            String dataType = GeneratorUtils.getDataType(entityProperty);
            if (!Objects.equals(dataType, DataTypeEnum.CHAR_TYPE.getKeyword())) {
                continue;
            }
            if (uniqueFieldList.contains(entityProperty.getProperty())) {
                continue;
            }
            String fieldItem = GeneratorUtils.replaceProperty(String.format(template, updateDefaultValue), entityProperty);
            stringBuilder.append(fieldItem);
            break;
        }
        return stringBuilder.toString();
    }
}
