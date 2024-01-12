package com.xinxian.generator.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.bean.GenQueryField;
import com.xinxian.generator.framework.bean.GenTableConfig;
import com.xinxian.generator.framework.bean.GenUniqueField;
import com.xinxian.generator.framework.enums.CheckUniqueTypeEnum;
import com.xinxian.generator.framework.service.IGeneratorConfig;
import com.xinxian.generator.framework.utils.GeneratorTableUtils;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName GeneratorConfig
 * @Description 自动生成配置
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
public class GeneratorConfig implements IGeneratorConfig {

    @Override
    public void setCommonResult(LinkedHashMap<String, EntityProperty> resultMap) {
        resultMap.put("isOwner", EntityProperty.builder().property("isOwner").desc("自己创建").type(GeneratorUtils.typeBoolean).build());
    }

    @Override
    public void setCommonParam(LinkedHashMap<String, EntityProperty> paramMap) {
        paramMap.put("updateUserEmail", EntityProperty.builder().property("updateUserEmail").desc("更新人邮箱").type(GeneratorUtils.typeString).build());
        paramMap.put("updateUserDept", EntityProperty.builder().property("updateUserDept").desc("更新人部门名称").type(GeneratorUtils.typeString).build());
        paramMap.put("updateCompany", EntityProperty.builder().property("updateCompany").desc("更新人公司").type(GeneratorUtils.typeString).build());
        paramMap.put("page", EntityProperty.builder().property("page").desc("页码下标").type(GeneratorUtils.typeInteger).build());
        paramMap.put("pageSize", EntityProperty.builder().property("pageSize").desc("每页条数").type(GeneratorUtils.typeInteger).build());
    }

    @Override
    public void setCommonEntity(LinkedHashMap<String, EntityProperty> entityMap) {
    }

    /**
     * MarketCandles其他信息
     *
     * @return {@link Map < String, String>}
     */
    public static Map<String, String> getMarketCandlesOtherInfo() {
        String tableName = "market_candles";
        String apiResult = "{\n" +
                "            \"open\":\"2.34517\",\n" +
                "            \"high\":\"2.34771\",\n" +
                "            \"low\":\"2.34214\",\n" +
                "            \"close\":\"2.34555\",\n" +
                "            \"quoteVol\":\"189631.101357091\",\n" +
                "            \"baseVol\":\"80862.6823\",\n" +
                "            \"usdtVol\":\"189631.101357091\",\n" +
                "            \"ts\":\"1622091360000\"\n" +
                "        }";
        Map<@Nullable String, @Nullable String> otherInfoMap = Maps.newHashMap();
        otherInfoMap.put("apiResult", apiResult);
        otherInfoMap.put("${tableName}", tableName);
        otherInfoMap.put("${entityDesc}", "现货币对OHLC价格");
        otherInfoMap.put("${entityName}", StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
        return otherInfoMap;
    }

    /**
     * 测试使用-relationType:1
     * NoticeInfoTable配置信息
     *
     * @return
     */
    public Map<String, String> getNoticeInfoTable() {
        GenTableConfig genTableConfig = GenTableConfig.builder().tableName("notice_info_table").entityName("NoticeInfoTable").entityDesc("通知信息").bizClassName("NoticeInfoTable").requestMapping("/noticeInfoTable").controllerType(null).checkParamFields(Arrays.asList("platId", "type", "noticeTitle", "content")).build();
        GenQueryField queryField = GenQueryField.builder().queryField("noticeTitle").queryFieldDesc("消息标题").build();
        GenUniqueField uniqueField = GenUniqueField.builder().uniqueFields(Arrays.asList("noticeTitle")).checkUniqueType(CheckUniqueTypeEnum.NOTIFICATION.getValue()).build();
        GenUniqueField uniqueField2 = GenUniqueField.builder().uniqueFields(Arrays.asList("version", "releaseDate")).checkUniqueType(CheckUniqueTypeEnum.NOTIFICATION.getValue()).build();
        genTableConfig.setQueryFieldList(Arrays.asList(queryField));
        genTableConfig.setUniqueFieldList(Arrays.asList(uniqueField2, uniqueField));
        Map<String, String> tableConfig = GeneratorTableUtils.getTableConfig(genTableConfig);
        addCommonProperty(tableConfig);
        /* entityMap */
        LinkedHashMap<String, EntityProperty> entityMap = GeneratorUtils.getFieldOrDefaultPropertyMap(tableConfig, "${entityMap}");
        entityMap.put("types", EntityProperty.builder().property("types").desc("类型 1-通知 2-公告 3-进度").type(GeneratorUtils.typeListInteger).build());
        tableConfig.put("${entityMap}", JSON.toJSONString(entityMap));
        /* paramMap */
        LinkedHashMap<String, EntityProperty> paramMap = GeneratorUtils.getFieldOrDefaultPropertyMap(tableConfig, "${paramMap}");
        paramMap.put("types", EntityProperty.builder().property("types").desc("类型 1-通知 2-公告 3-进度").type(GeneratorUtils.typeListInteger).build());
        tableConfig.put("${paramMap}", JSON.toJSONString(paramMap));
        /* resultMap */
        LinkedHashMap<String, EntityProperty> resultMap = GeneratorUtils.getFieldOrDefaultPropertyMap(tableConfig, "${resultMap}");
        resultMap.put("unReadNum", EntityProperty.builder().property("unReadNum").desc("未读数量").type(GeneratorUtils.typeLong).build());
        resultMap.put("popup", EntityProperty.builder().property("popup").desc("弹出").type(GeneratorUtils.typeBoolean).build());
        resultMap.put("contentList", EntityProperty.builder().property("contentList").desc("内容列表").type(GeneratorUtils.typeListString).build());
        tableConfig.put("${resultMap}", JSON.toJSONString(resultMap));
        return tableConfig;
    }
}