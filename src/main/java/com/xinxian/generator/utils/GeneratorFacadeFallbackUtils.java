package com.xinxian.generator.utils;

import java.util.Map;

/**
 * @ClassName GeneratorFacadeClientUtils
 * @Description 自动生成工具包
 * @Author lmy
 * @Date 2023/04/22 11:00
 */
public class GeneratorFacadeFallbackUtils {

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