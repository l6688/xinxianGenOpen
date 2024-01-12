package com.xinxian.generator.framework.utils;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.enums.GenFunctionEnum;
import com.xinxian.generator.framework.constant.GenFunctionConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName GenFunctionUtils
 * @Description 生成功能工具包
 * @Author lmy
 * @Date 2023/5/4 16:25
 */
public class GenFunctionUtils {

    public static final String keyGenFunctionList = "genFunctionList";

    /**
     * 执行处理未使用的
     *
     * @param template  {@link String}
     * @param configMap {@link Map< String, String>}
     * @return {@link String}
     */
    public static String doProcessUnUsed(String template, Map<String, String> configMap) {
        String genTypeEnum = configMap.get("genTypeEnum");
        List<String> deleteMethodList = getUnNeedList(configMap, GenFunctionConstant.deleteMethodMap, genTypeEnum);
        List<String> deleteContentList = getUnNeedList(configMap, GenFunctionConstant.deleteContentMap, genTypeEnum);
        List<Pair<String, String>> replaceContentList = getUnNeedPairList(configMap, GenFunctionConstant.replaceContentPairMap, genTypeEnum);
        template = GeneratorUtils.deleteUnUsedTextAndMethod(template, genTypeEnum, deleteMethodList, deleteContentList, replaceContentList);
        return template;
    }

    /**
     * 处理删除生成类型
     *
     * @param genList   {@link List< String>}
     * @param configMap {@link Map< String, String>}
     */
    public static void processDeleteGenType(List<String> genList, Map<String, String> configMap) {
        if (configMap == null) {
            return;
        }
        List<String> unNeedGenTypeList = getUnNeedList(configMap, GenFunctionConstant.deleteGenTypeMap, "");
        if (CollectionUtils.isNotEmpty(unNeedGenTypeList)) {
            for (String unNeedGenType : unNeedGenTypeList) {
                genList.remove(unNeedGenType);
            }
        }
    }

    /**
     * 获取不需要的集合
     * 求交集，在map中有不存在的直接返回null
     *
     * @param configMap {@link Map< String, String>}
     * @param map       {@link Map< String, List< String>>}
     * @return {@link List< String>}
     */
    private static List<String> getUnNeedList(Map<String, String> configMap, Map<String, List<String>> map, String genTypeEnum) {
        return Lists.newArrayList();
    }

    /**
     * 获取不需要的集合
     *
     * @param configMap {@link Map< String, String>}
     * @param map       {@link Map< String, List< String>>}
     * @return {@link List< String>}
     */
    private static List<Pair<String, String>> getUnNeedPairList(Map<String, String> configMap, Map<String, List<Pair<String, String>>> map, String genTypeEnum) {
        List<Integer> genFunctionList = GeneratorUtils.getListFromConfig(configMap, keyGenFunctionList, Integer.class);
        List<Pair> resultList = JSON.parseArray(JSON.toJSONString(map.get(GenFunctionEnum.toEnum(genFunctionList.remove(0)).name() + genTypeEnum)), Pair.class);
        for (Integer genFunctionValue : genFunctionList) {
            List<Pair<String, String>> list = map.get(GenFunctionEnum.toEnum(genFunctionValue).name() + genTypeEnum);
            if (CollectionUtils.isNotEmpty(list)) {
                HashSet<Pair> set = new HashSet<>(list);
                resultList.removeIf(item -> !set.contains(item));
            }
        }
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        return resultList.stream().map(r -> Pair.of(String.valueOf(r.getLeft()), String.valueOf(r.getRight()))).collect(Collectors.toList());
    }

    /**
     * 处理未使用的
     *
     * @param template  {@link String}
     * @param configMap {@link Map< String, String>}
     * @return {@link String}
     */
    public static String processUnUsed(String template, Map<String, String> configMap) {
        List<Integer> genFunctionList = GeneratorUtils.getListFromConfig(configMap, keyGenFunctionList, Integer.class);
        if (CollectionUtils.isNotEmpty(genFunctionList)) {
            template = GenFunctionUtils.doProcessUnUsed(template, configMap);
        }
        return template;
    }


}
