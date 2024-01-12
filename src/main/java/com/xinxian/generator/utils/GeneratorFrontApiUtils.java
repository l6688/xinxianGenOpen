package com.xinxian.generator.utils;


import java.util.Map;

/**
 * @ClassName GeneratorFrontApiUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorFrontApiUtils {

    /**
     * 后置处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        generatorText = generatorText.replace("${base}//", "${base}/");
        return generatorText;
    }
}
