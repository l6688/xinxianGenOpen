package com.xinxian.generator.service;

import com.xinxian.generator.framework.utils.GenAddTypeUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenAddType
 * @Description 生成代码增加类型
 * @Author lmy
 * @Date 2023/4/22 23:10
 */
public class GenAddType {

    /**
     * 增加生成类型
     * 1.执行该方法
     * 2.补充template
     * 3.补充目标地址 GeneratorUtils.getResultPath方法 & GeneratorProject.this.abstractPathMap
     * 4.补充组生成规则 GenFunctionConstant
     *
     * @param args {@link String[]}
     */
    public static void main(String[] args) {
        /* 配置信息 */
        List<Map<String, String>> configList = Lists.newArrayList();
        Map<String, String> configMap = new HashMap<>();
        configMap.put("${desc}", "测试");
        configMap.put("${value}", "test");
        configList.add(configMap);
        GenAddTypeUtils.doAddType(configList);
    }
}
