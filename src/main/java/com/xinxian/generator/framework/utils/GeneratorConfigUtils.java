package com.xinxian.generator.framework.utils;

import com.alibaba.fastjson.JSON;
import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.converter.GenTableConverter;
import com.xinxian.generator.framework.dto.vo.EntityPropertyVO;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GeneratorConfig
 * @Description 自动生成配置
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
public interface GeneratorConfigUtils {

    public static final String commonContentField = "content";
    public static final String commonContentTable = "common_content";
    public static final String commonContentIdField = "contentId";

    /**
     * FixedEmailLog配置信息
     *
     * @return
     */
    public static void mergeProperty(String key, Map<String, String> tableConfig, String mapStr) {
        if (StringUtils.isNotEmpty(mapStr)) {
            LinkedHashMap<String, EntityProperty> map = GeneratorUtils.getFieldOrDefaultPropertyMap(tableConfig, key);
            List<EntityPropertyVO> entityPropertyList = GenTableConverter.converterToListEntityPropertyVO(mapStr);
            for (EntityPropertyVO entityPropertyVO : entityPropertyList) {
                map.put(entityPropertyVO.getProperty(), GenTableConverter.convertToEntityProperty(entityPropertyVO));
            }
            tableConfig.put(key, JSON.toJSONString(map));
        }
    }
}