package com.xinxian.generator.utils;


import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.utils.GeneratorUtils;

import java.util.Map;

/**
 * @ClassName GeneratorResultUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorEntityUtils {

    /**
     * 后置处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        return GeneratorUtils.commonEntityProcess(generatorText, configMap, null, GenTypeEnum.ENTITY);
    }
}
