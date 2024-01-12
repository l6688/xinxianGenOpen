package com.xinxian.generator.framework.enums;


import java.util.Objects;

/**
 * 前端组件大类枚举
 *
 * @author lmy
 * @date 2023/4/9 3:18 PM
 **/
public enum FrontComponentTypeEnum {

    /**
     * 输入
     */
    INPUT(1, "输入"),

    /**
     * 选择器
     */
    SELECT(2, "选择器"),

    /**
     * 时间
     */
    TIME(3, "时间"),

    ;
    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    public static FrontComponentTypeEnum toEnum(Integer value) {
        for (FrontComponentTypeEnum valueType : FrontComponentTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static FrontComponentTypeEnum getInstance(String desc) {
        for (FrontComponentTypeEnum valueType : FrontComponentTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    FrontComponentTypeEnum(Integer value, String desc) {
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