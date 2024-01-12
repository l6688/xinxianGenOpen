package com.xinxian.generator.framework.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ListUtils
 * @Description list工具包
 * @Author lmy
 * @Date 2023/4/4 15:06
 */
public class ListUtils {

    /**
     * 复制集合
     *
     * @param list {@link List<T>}
     * @return {@link List<T>}
     */
    public static <T> List<T> copy(List<T> list, Class<T> clazz) {
        return JSON.parseArray(JSON.toJSONString(list), clazz);
    }

    /**
     * 集合合并
     *
     * @param list {@link List<T>}
     * @return {@link List<T>}
     */
    public static <T> List<T> join(List<T> list, List<T> otherList) {
        ArrayList<T> mergeList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            mergeList.addAll(list);
        }
        if (CollectionUtils.isNotEmpty(otherList)) {
            mergeList.addAll(otherList);
        }
        return mergeList;
    }
}
