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
public class GeneratorParamUtils {

    /**
     * 后置处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        String text = GeneratorUtils.commonEntityProcess(generatorText, configMap, GeneratorUtils.ignoreFieldEntitySet, GenTypeEnum.PARAM);
        text = text.replace(" private Date createTime;", " private String createTime;");
        text = text.replace(" private Date updateTime;", " private String updateTime;");
        text = text.replace("import java.util.Date;", "");
        text = GeneratorUtils.addImport(text);
        return text;
    }
}
