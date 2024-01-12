package com.xinxian.generator.framework.utils;

import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @ClassName MapUtils
 * @Description map工具类
 * @Author lmy
 * @Date 2023/1/24 09:05
 */
public class MapUtils {

    /**
     * 获取项目配置
     *
     * @return
     */
    public static Map<String, String> defaultNullValue(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue("");
            }
        }
        return map;
    }

    /**
     * 展开map的值
     * 把Map<String, List<ServiceTemplate>>转为Map<String, ServiceTemplate>
     *
     * @param map {@link Map}
     * @return {@link Map}
     */
    public static Map<String, Object> flatMapValues(Map<String, List<Map.Entry<String, Object>>> map) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, List<Map.Entry<String, Object>>> entryList : map.entrySet()) {
            String key = entryList.getKey();
            List<Map.Entry<String, Object>> list = entryList.getValue();
            for (Map.Entry<String, Object> entry : list) {
                if (Objects.equals("DateTime", key)) {
                    result.put(entry.getKey(), DateUtil.date(Long.valueOf(entry.getValue().toString())));
                } else if (Objects.equals("ZonedDateTime", key)) {
                    result.put(entry.getKey(), LocalDateTime.ofEpochSecond(Long.parseLong(entry.getValue().toString())/1000, 0, ZoneOffset.ofHours(8)).atZone(ZoneId.systemDefault()));
                } else if (Objects.equals("Long", key)) {
                    result.put(entry.getKey(), Long.valueOf(entry.getValue().toString()));
                } else if (Objects.equals("Double", key)) {
                    result.put(entry.getKey(), Double.valueOf(entry.getValue().toString()));
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * 按照类型分组
     * 把Map<String, ServiceTemplate>转为Map<String, List<ServiceTemplate>>
     *
     * @param map {@link Map}
     * @return {@link Map}
     */
    public static <T> Map<String, List<Map.Entry<String, T>>> groupByType(Map<String, T> map) {
        Map<String, List<Map.Entry<String, T>>> result = new HashMap<>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String key = entry.getValue().getClass().getSimpleName();
            if (result.containsKey(key)) {
                result.get(key).add(entry);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(entry);
                result.put(key, arrayList);
            }
        }
        return result;
    }
}
