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
public class GeneratorResultUtils {

    /**
     * 后置处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        generatorText = GeneratorUtils.commonEntityProcess(generatorText, configMap, GeneratorUtils.ignoreResultFieldSet, GenTypeEnum.PARAM);
        generatorText = generatorText.replace(" private Date createTime;", " private String createTime;");
        generatorText = generatorText.replace(" private Date updateTime;", " private String updateTime;");
        generatorText = generatorText.replace("import java.util.Date;", "");
        generatorText = GeneratorUtils.addImport(generatorText);
        return generatorText;
    }
}
