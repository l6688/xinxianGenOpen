package com.xinxian.generator.utils;

import cn.hutool.core.io.FileUtil;

import java.util.Map;

/**
 * @ClassName GeneratorFrontRouterUtils
 * @Description
 * @Author lmy
 * @Date 2023/1/14 19:31
 */
public class GeneratorFrontRouterUtils {

    private static final String dictChildStr = "meta: { title: \"字典\", icon: \"dict\", affix: true },\n        children: [\n";

    private static String routerEndStr = "export default router;";


    /**
     * 后处理
     */
    public static String afterProcess(String generatorText, Map<String, String> configMap) {
        String resultPath = configMap.get("resultPath");
        String sourceText = FileUtil.readUtf8String(resultPath);
        if (sourceText.contains(generatorText)) {
            return sourceText;
        }
        sourceText = sourceText.replace(dictChildStr, dictChildStr + generatorText + ",\n");
        return sourceText;
    }
}
