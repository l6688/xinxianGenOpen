package com.xinxian.generator.utils;

import com.xinxian.generator.framework.utils.GeneratorTableUtils;
import com.xinxian.generator.service.GeneratorTable;
import com.xinxian.generator.framework.utils.GeneratorUtils;

import java.util.Map;

/**
 * @ClassName GeneratorServiceUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorServiceUtils {

    /**
     * 前置处理
     */
    public static String beforeProcess(String template, Map<String, String> configMap) {
        template = processRelationTable(template, configMap);
        return template;
    }

    /**
     * 生成完后的处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        String text = generatorText;
        text = GeneratorUtils.addMainEntityImport(configMap, text);
        return text;
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
            text = GeneratorUtils.removeAbstractMethod(text, method);
        }
        return text;
    }
}
