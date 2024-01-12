package com.xinxian.generator.framework.dto.enums;

import java.util.Objects;

/**
 * 转化类型枚举
 *
 * @author lmy
 * @date 2023/7/27 3:18 PM
 **/
public enum CastTypeEnum {

    /**
     * 无
     */
    NOT_HAVE(1, "无"),

    /**
     * 对象
     */
    OBJECT(2, "对象"),
    /**
     * 对象列表
     */
    OBJECT_LIST(3, "对象列表"),
    /**
     * 逗号分隔
     */
    COMMA_SEPARATED(4, "逗号分隔"),
    /**
     * 时间格式化
     */
    TIME_FORMAT(5, "时间格式化"),

    ;


    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    public static CastTypeEnum toEnum(Integer value) {
        for (CastTypeEnum valueType : CastTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static CastTypeEnum getInstance(String desc) {
        for (CastTypeEnum valueType : CastTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    CastTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getValue() {
        return value;
    }
    }
