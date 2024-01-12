package com.xinxian.generator.service;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.enums.GenFunctionEnum;
import com.xinxian.generator.enums.PlatInfoEnum;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.enums.SupportRequirementEnum;
import com.xinxian.generator.framework.utils.GenFunctionUtils;
import com.xinxian.generator.framework.utils.GeneratorMainUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @ClassName GeneratorGetData
 * @Description 自动生成优化
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
@Slf4j
public class GeneratorMain {

    /* 时间map */
    {
        GeneratorMainUtils.dateMap.put("xx", "2023/06/30 17:40");
    }

    ;

    /**
     * 1.新表生成代码
     * 2.增加字段
     *
     * @param args {@link String[]}
     */
    public static void main(String[] args) {
        Map<String, String> configMap = new HashMap<>();
        /* 执行复制类和生成类二选一 */
        configMap.put("copyEntity", "false");
        genNewTable(configMap);
//        genAddField();
    }

    /**
     * TgdTemplateInfo增加字段
     * genAddFieldTgdTemplateInfo
     */
    public static void genAddField() {
        String projectName = PlatInfoEnum.KNOWLEDGE.getProjectName();
        ArrayList<String> tableList = new ArrayList<>();
        ArrayList<String> genClassList = new ArrayList<>(GeneratorMainUtils.genAddField);
        tableList.add("testTable");
        /* addFieldMap */
        LinkedHashMap<String, EntityProperty> addFieldMap = new LinkedHashMap<>();
        /* 状态模板 */
        /* tableFieldMap */
        Map<String, Map<String, String>> tableFieldMap = new HashMap<>();
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("${addFieldMap}", JSON.toJSONString(addFieldMap));
        tableFieldMap.put("testTable", fieldMap);
        HashMap<String, String> otherInfo = new HashMap<>();
        otherInfo.put("${supportRequirement}", SupportRequirementEnum.ADD_FIELD.getValue());
        GeneratorMainUtils.doGeneratorClass(projectName, tableList, genClassList, tableFieldMap, otherInfo);
    }

    /**
     * 新表生成代码：
     * <p>
     * 1.建表
     * 2.项目中增加自动生成代码
     * 3.提交代码
     * 4.放开tk，覆盖entity
     * 5.tableList增加表
     * 6.复制entity到本项目对应entity文件夹,可通过执行genNewTable实现
     * 7.GeneratorConfig.getXXX 配置信息
     */
    public static void genNewTable(Map<String, String> configMap) {
        String projectName = PlatInfoEnum.KNOWLEDGE.getProjectName();
        HashMap<String, String> otherInfo = new HashMap<>();
        otherInfo.put(GenFunctionUtils.keyGenFunctionList, JSON.toJSONString(Arrays.asList(GenFunctionEnum.WEB.getValue())));
        otherInfo.putAll(configMap);
        otherInfo.put("${supportRequirement}", SupportRequirementEnum.GEN_CODE.getValue());
        ArrayList<String> genClassList = new ArrayList<>(GeneratorProject.genTypes);
        /* 生成表 */
        ArrayList<String> tableList = new ArrayList<>();
        tableList.add("testTable");
        GeneratorMainUtils.doGeneratorClass(projectName, tableList, genClassList, null, otherInfo);
    }
}