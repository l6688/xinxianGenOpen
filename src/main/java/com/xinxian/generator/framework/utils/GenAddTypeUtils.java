package com.xinxian.generator.framework.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.constant.GenConfigConstant;
import com.xinxian.generator.utils.GeneratorEnumUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenAddType
 * @Description 生成代码增加类型
 * @Author lmy
 * @Date 2023/4/22 23:10
 */
public class GenAddTypeUtils {

    private static final String genTypeEnumPath = GenConfigConstant.GEN_ENUMS_PATH + "/GenTypeEnum.java";
    private static final String genUtilsTemplatePath = GenConfigConstant.GEN_TEMPLATE_BASE_PATH + "/GenUtilsTemplate";
    private static final String genTypeEnumTemplate = "\n" +
            "    /**\n" +
            "     * ${desc}\n" +
            "     */\n" +
            "    ${name}(\"${value}\", \"${desc}\"),\n";


    /**
     * 增加生成类型
     * 1.执行该方法
     * 2.补充template
     * 3.补充目标地址 GeneratorUtils.getResultPath方法 & GeneratorProject.this.abstractPathMap
     * 4.补充组生成规则 GenFunctionConstant
     *
     * @param configList {@link List<Map<String, String>>}
     */
    public static void doAddType(List<Map<String, String>> configList) {
        autoAddConfigInfo(configList);
        /* GenTypeEnum */
        addGenTypeEnum(configList);
        /* 模板 */
        addGenTemplate(configList);
        /* utils */
        addGenUtils(configList);
    }

    /**
     * 增加模板
     *
     * @param configList {@link List<Map<String, String>> configList}
     */
    private static void addGenTemplate(List<Map<String, String>> configList) {
        for (Map<String, String> configMap : configList) {
            FileUtil.writeUtf8String("", GenConfigConstant.GEN_TEMPLATE_BASE_PATH + String.format("/%sTemplate", configMap.get("${genType}")));
        }
    }

    /**
     * 增加工具类
     *
     * @param configList {@link List<Map<String, String>> configList}
     */
    private static void addGenUtils(List<Map<String, String>> configList) {
        for (Map<String, String> configMap : configList) {
            String source = FileUtil.readUtf8String(genUtilsTemplatePath);
            source = GeneratorUtils.doGenerator(source, configMap);
            FileUtil.writeUtf8String(source, GenConfigConstant.GEN_UTILS_PATH + String.format("/Generator%sUtils.java", configMap.get("${genType}")));
        }
    }

    /**
     * 增加枚举类型
     *
     * @param configList {@link List<Map<String, String>>}
     */
    private static void addGenTypeEnum(List<Map<String, String>> configList) {
        String source = FileUtil.readUtf8String(genTypeEnumPath);
        /* 处理:解析，处理queryField，处理uniqueField */
        GeneratorEnumUtils generatorEnumUtils = new GeneratorEnumUtils();
        String lastCodeEnum = generatorEnumUtils.getLastCodeEnum(source);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map<String, String> configMap : configList) {
            String value = configMap.get("${value}");
            configMap.put("${name}", StrUtil.toUnderlineCase(value).toUpperCase());
            String genItemText = generatorEnumUtils.getGenItemErrorCode("${value}", null, configMap, genTypeEnumTemplate, new LinkedHashMap<>());
            stringBuilder.append(genItemText);
        }
        /* 输出 */
        String genText = source.replace(lastCodeEnum, lastCodeEnum + stringBuilder);
        FileUtil.writeUtf8String(genText, genTypeEnumPath);
    }

    /**
     * 自动增加补充配置信息
     *
     * @param configList {@link List<Map<String, String>>}
     */
    private static void autoAddConfigInfo(List<Map<String, String>> configList) {
        for (Map<String, String> configMap : configList) {
            String value = configMap.get("${value}");
            configMap.put("${name}", StrUtil.toUnderlineCase(value).toUpperCase());
            configMap.put("${genType}", StrUtil.upperFirst(value));
            configMap.put("${date}", DateUtil.format(DateUtil.date(), GenConfigConstant.COMMENT_DATE_FORMAT));
            configMap.put("${author}", GenConfigConstant.AUTHOR);
        }
    }
}
