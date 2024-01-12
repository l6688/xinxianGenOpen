package com.xinxian.generator.utils;

import java.util.Map;

/**
 * @ClassName GeneratorSqlUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorSqlUtils {

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
        return genText;
    }

}
