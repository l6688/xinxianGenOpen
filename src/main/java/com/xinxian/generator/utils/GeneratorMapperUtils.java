package com.xinxian.generator.utils;


import com.xinxian.generator.framework.bean.GenUniqueField;
import com.xinxian.generator.framework.enums.CheckUniqueTypeEnum;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName GeneratorMapperUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorMapperUtils {

    /**
     * 前置处理
     */
    public static String beforeProcess(String template, Map<String, String> configMap) {
        template = processUniqueFields(template, configMap);
        return template;
    }

    /**
     * 处理唯一约束
     *
     * @param template  {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    private static String processUniqueFields(String template, Map<String, String> configMap) {
        /* 不存在时处理 */
        if (StringUtils.isEmpty(configMap.get(GenConstant.keyUniqueFieldList))) {
            template = GeneratorUtils.removeMapperMethod(template, "deleteDuplicateUniqueKey");
            return template;
        }
        List<GenUniqueField> uniqueFieldList = GeneratorUtils.getListFromConfig(configMap, GenConstant.keyUniqueFieldList, GenUniqueField.class);
        /* 去掉deleteDuplicateUniqueKey */
        if (!Objects.equals(uniqueFieldList.get(0).getCheckUniqueType(), String.valueOf(CheckUniqueTypeEnum.DELETE.getValue()))) {
            template = GeneratorUtils.removeMapperMethod(template, "deleteDuplicateUniqueKey");
        }
        return template;
    }
}
