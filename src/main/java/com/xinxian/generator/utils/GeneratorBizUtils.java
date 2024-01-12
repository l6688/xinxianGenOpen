package com.xinxian.generator.utils;

import com.xinxian.generator.framework.utils.GenFunctionUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;

import java.util.Map;

/**
 * @ClassName GeneratorServiceUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorBizUtils {

    /**
     * 前置处理
     */
    public static String beforeProcess(String template, Map<String, String> configMap) {
        /* 根据功能删除无用代码 */
        template = GenFunctionUtils.processUnUsed(template, configMap);
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
}
