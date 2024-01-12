package com.xinxian.generator.utils;

import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import com.xinxian.generator.framework.dto.entity.GenTable;

import java.util.List;
import java.util.Map;

/**
 * @ClassName GeneratorGeneratorConfigUtils
 * @Description 自动生成生成配置工具包
 * @Author lmy
 * @Date 2023/06/11 16:06
 */
public class GeneratorGeneratorConfigUtils {

    private static final String generatorConfigTableTemplate = "        <table tableName=\"${tableName}\" domainObjectName=\"${entityName}\"\n               enableCountByExample=\"false\" \n               enableUpdateByExample=\"false\"\n               enableDeleteByExample=\"false\"\n               enableSelectByExample=\"false\"\n               selectByExampleQueryId=\"true\"\n               enableSelectByPrimaryKey=\"true\"\n               enableUpdateByPrimaryKey=\"true\"\n               enableDeleteByPrimaryKey=\"true\">\n            <generatedKey column=\"id\"  sqlStatement=\"JDBC\"/>\n        </table>\n";


    /**
     * 前置处理
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    public static String beforeProcess(String genText, Map<String, String> configMap) {
        return genText;
    }

    /**
     * 后置处理
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    public static String afterProcess(String genText, Map<String, String> configMap) {
        List<GenTable> genTableList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyGeneratorConfigList, GenTable.class);
        String generatorConfigTable = GeneratorUtils.genByTemplateAndGenTableList(genTableList, generatorConfigTableTemplate);
        genText = genText.replace(GenConstant.keyGeneratorConfigTable, generatorConfigTable);
        return genText;
    }
}
