package com.xinxian.generator.utils;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.framework.bean.GenUniqueField;
import com.xinxian.generator.framework.enums.CheckUniqueTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GeneratorTableUtils;
import com.xinxian.generator.service.GeneratorTable;
import com.xinxian.generator.framework.utils.GenFunctionUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @ClassName GeneratorServiceImplUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorServiceImplUtils {

    private static List<String> notUniqueFieldMethodList = Lists.newArrayList();
    private static List<String> notUniqueFieldList = Lists.newArrayList();
    private static List<String> notUniqueFieldDeleteMethodList = Arrays.asList("deleteDuplicateUniqueKey");
    private static List<String> notUniqueFieldDeleteList = Arrays.asList("        mapper.deleteDuplicateUniqueKey(records, records.get(0));\n", "        deleteDuplicateUniqueKey(Arrays.asList(record));\n", "        deleteDuplicateUniqueKey(records);\n");
    private static final String uniqueFieldCallCheckUniqueCombineTemplate = "        check${uniqueFieldsUpperFirstCombine}Unique(record);\n";
    private static final String uniqueFieldMethodCheckUniqueCombineTemplate = "    /**\n     * ${uniqueFieldsDesc}唯一约束检查\n     *\n     * @param record {@link ${entityName}}\n     * @return\n     */\n    private void check${uniqueFieldsUpperFirstCombine}Unique(${entityName} record) {\n        ${entityName} condition = new ${entityName}();\n${subCheckUniqueContent}\n        List<${entityName}> conditionList = selectByCondition(condition);\n        if (CollectionUtils.isNotEmpty(conditionList)) {\n            for (${entityName} indb : conditionList) {\n                if (${doCheckUniqueContent}) {\n                    if (record.getId() == null || !indb.getId().equals(record.getId())) {\n                        throw new ApiResultException(${errorCodeEnum}.FIELD_${uniqueFieldsUpper}_DUPLICATE);\n                    }\n                }\n            }\n        }\n    }\n\n";
    private static final String uniqueFieldCheckBatchUniqueTemplate = "        /* ${uniqueFieldsDesc}当前批次唯一约束检查 */\n        List<String> notNull${uniqueFieldsUpperFirstCombine}List = records.stream().filter(r -> ${checkBatchUniqueContent}).map(r -> StrUtil.join(\"_\", ${checkBatchUniqueContent2})).collect(Collectors.toList());\n        List<String> distinct${uniqueFieldsUpperFirstCombine}List = notNull${uniqueFieldsUpperFirstCombine}List.stream().distinct().collect(Collectors.toList());\n        if (notNull${uniqueFieldsUpperFirstCombine}List.size() != distinct${uniqueFieldsUpperFirstCombine}List.size()) {\n, throw new ApiResultException(${errorCodeEnum}.FIELD_${uniqueFieldsUpper}_DUPLICATE);\n}\n";
    public static List<Pair<String, String>> notUniqueFieldPairList = Arrays.asList(
            Pair.of("    private ${entityName} insertPrepare(${entityName} record, Boolean checkUnique) {",
                    "    private ${entityName} insertPrepare(${entityName} record) {"),
            Pair.of("insertPrepare(record, true);",
                    "insertPrepare(record);"),
            Pair.of("insertPrepare(record, false);",
                    "insertPrepare(record);"));
    /* 模板map，key为替换的关键字，value：pair左侧为模板，右侧为分隔符 */
    private static Map<String, Pair<String, String>> checkUniqueTemplateMap = new HashMap() {
        {
            put("${subCheckUniqueContent}", Pair.of("        condition.set${uniqueFieldsUpperFirstItem}(record.get${uniqueFieldsUpperFirstItem}());", "\n"));
            put("${doCheckUniqueContent}", Pair.of("indb.get${uniqueFieldsUpperFirstItem}().equals(record.get${uniqueFieldsUpperFirstItem}())", " && "));
            put("${checkBatchUniqueContent}", Pair.of("r.get${uniqueFieldsUpperFirstItem}() != null", " && "));
            put("${checkBatchUniqueContent2}", Pair.of("r.get${uniqueFieldsUpperFirstItem}()", " + \"-\" + "));
        }
    };

    static {
        notUniqueFieldList.addAll(Arrays.asList("        if(checkUnique){\n            checkUnique(record);\n        }\n", "        doCheckUnique(record, condition, ${errorCodeEnum}.FIELD_${queryFieldUpper}_DUPLICATE);\n", "        doCheckUnique(record, condition, UserPassportErrorCodeEnum.FIELD_REVIEWER_ID_DUPLICATE);\n", "        checkBatchUnique(records);\n"
                , "${uniqueFieldsUpperFirstCombine}", "${subCheckUniqueContent}\n", "FIELD_${uniqueFieldsUpper}_DUPLICATE", "${doCheckUniqueContent}", "${uniqueFieldCallCheckUniqueCombine}","        checkUnique(record);\n"));
        notUniqueFieldMethodList.addAll(Arrays.asList("checkUnique", "check${uniqueFieldsUpperFirstCombine}Unique", "doCheckUnique", "checkBatchUnique"));
        notUniqueFieldList.addAll(notUniqueFieldDeleteList);
        notUniqueFieldMethodList.addAll(notUniqueFieldDeleteMethodList);
        notUniqueFieldList.add(GenConstant.keyUniqueFieldCallCheckUniqueCombine + GenConstant.ENTER);
        notUniqueFieldList.add(GenConstant.keyUniqueFieldMethodCheckUniqueCombine + GenConstant.ENTER);
        notUniqueFieldList.add(GenConstant.keyUniqueFieldCheckBatchUnique + GenConstant.ENTER);
    }


    /**
     * 前置处理
     */
    public static String beforeProcess(String template, Map<String, String> configMap) {
        /* 处理唯一约束 */
        template = processUniqueFields(template, configMap);
        /* 根据功能删除无用代码 */
        template = GenFunctionUtils.processUnUsed(template, configMap);
        return template;
    }

    /**
     * 后置处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        generatorText = processRelationTable(generatorText, configMap);
        /* 增加主表的import */
        generatorText = GeneratorUtils.addMainEntityImport(configMap, generatorText);
        return generatorText;
    }

    /**
     * 处理关系表
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    private static String processRelationTable(String text, Map<String, String> configMap) {
        if (GeneratorUtils.relationType3Slave(configMap)) {
            return text;
        }
        for (String method : GeneratorTableUtils.notRelationType3SlaveRemoveMethods) {
            text = GeneratorUtils.removeMethod(text, method);
        }
        return text;
    }

    /**
     * 处理唯一约束
     *
     * @param text      {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    private static String processUniqueFields(String text, Map<String, String> configMap) {
        /* 不存在时处理 */
        if (StringUtils.isEmpty(configMap.get(GenConstant.keyUniqueFieldList))) {
            text = GeneratorUtils.deleteUnUsedTextAndMethod(text, notUniqueFieldMethodList, notUniqueFieldList, notUniqueFieldPairList);
            return text;
        }
        List<GenUniqueField> genUniqueFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyUniqueFieldList, GenUniqueField.class);
        StringBuilder callCheckUniqueCombine = new StringBuilder();
        StringBuilder methodCheckUniqueCombine = new StringBuilder();
        StringBuilder checkBatchUnique = new StringBuilder();
        for (GenUniqueField uniqueField : genUniqueFieldList) {
            GeneratorTableUtils.addUniqueFieldConfigMap(uniqueField, configMap);
            /* processCallCheckUniqueCombine */
            callCheckUniqueCombine.append(GeneratorUtils.processTemplate(uniqueFieldCallCheckUniqueCombineTemplate, configMap));
            /* processMethodCheckUniqueCombine */
            methodCheckUniqueCombine.append(processCheckUniqueContent(GeneratorUtils.processTemplate(uniqueFieldMethodCheckUniqueCombineTemplate, configMap), configMap));
            /* processCheckBatchUnique */
            checkBatchUnique.append(processCheckUniqueContent(GeneratorUtils.processTemplate(uniqueFieldCheckBatchUniqueTemplate, configMap), configMap));
        }
        text = text.replace(GenConstant.keyUniqueFieldCallCheckUniqueCombine, callCheckUniqueCombine.toString());
        text = text.replace(GenConstant.keyUniqueFieldMethodCheckUniqueCombine, methodCheckUniqueCombine.toString());
        text = text.replace(GenConstant.keyUniqueFieldCheckBatchUnique, checkBatchUnique.toString());
        /* 去掉deleteDuplicateUniqueKey */
        if (!Objects.equals(genUniqueFieldList.get(0).getCheckUniqueType(), String.valueOf(CheckUniqueTypeEnum.DELETE.getValue()))) {
            text = GeneratorUtils.deleteUnUsedTextAndMethod(text, notUniqueFieldDeleteMethodList, notUniqueFieldDeleteList, null);
        }
        return text;
    }

    private static String processCheckUniqueContent(String text, Map<String, String> configMap) {
        String uniqueFieldsUpperFirst = configMap.get("${uniqueFieldsUpperFirst}");
        List<String> uniqueFieldsUpperFirstList = JSON.parseArray(uniqueFieldsUpperFirst, String.class);
        StringBuilder subCheckUniqueContent = new StringBuilder();
        StringBuilder doCheckUniqueContent = new StringBuilder();
        StringBuilder checkBatchUniqueContent = new StringBuilder();
        StringBuilder checkBatchUniqueContent2 = new StringBuilder();
        for (int i = 0; i < uniqueFieldsUpperFirstList.size(); i++) {
            String field = uniqueFieldsUpperFirstList.get(i);
            if (!Objects.equals(i, 0)) {
                subCheckUniqueContent.append(checkUniqueTemplateMap.get("${subCheckUniqueContent}").getRight());
                doCheckUniqueContent.append(checkUniqueTemplateMap.get("${doCheckUniqueContent}").getRight());
                checkBatchUniqueContent.append(checkUniqueTemplateMap.get("${checkBatchUniqueContent}").getRight());
                checkBatchUniqueContent2.append(checkUniqueTemplateMap.get("${checkBatchUniqueContent2}").getRight());
            }
            subCheckUniqueContent.append(checkUniqueTemplateMap.get("${subCheckUniqueContent}").getLeft().replace("${uniqueFieldsUpperFirstItem}", field));
            doCheckUniqueContent.append(checkUniqueTemplateMap.get("${doCheckUniqueContent}").getLeft().replace("${uniqueFieldsUpperFirstItem}", field));
            checkBatchUniqueContent.append(checkUniqueTemplateMap.get("${checkBatchUniqueContent}").getLeft().replace("${uniqueFieldsUpperFirstItem}", field));
            checkBatchUniqueContent2.append(checkUniqueTemplateMap.get("${checkBatchUniqueContent2}").getLeft().replace("${uniqueFieldsUpperFirstItem}", field));
        }
        text = text.replace("${subCheckUniqueContent}", subCheckUniqueContent.toString());
        text = text.replace("${doCheckUniqueContent}", doCheckUniqueContent.toString());
        text = text.replace("${checkBatchUniqueContent}", checkBatchUniqueContent.toString());
        text = text.replace("${checkBatchUniqueContent2}", checkBatchUniqueContent2.toString());
        return text;
    }
}
