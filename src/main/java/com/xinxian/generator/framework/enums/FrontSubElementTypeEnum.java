package com.xinxian.generator.framework.enums;


import java.util.Objects;

/**
 * 前端子要素类枚举
 *
 * @author lmy
 * @date 2023/4/9 3:18 PM
 **/
public enum FrontSubElementTypeEnum {

    /**
     * 不可用
     */
    DISABLED(1, "不可用"),

    /**
     * 不可用条件
     */
    DISABLED_CONDITION(2, "不可用条件"),

    /**
     * 标签宽度
     */
    LABEL_WIDTH(3, "标签宽度"),

    ;

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    public static FrontSubElementTypeEnum toEnum(Integer value) {
        for (FrontSubElementTypeEnum valueType : FrontSubElementTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static FrontSubElementTypeEnum getInstance(String desc) {
        for (FrontSubElementTypeEnum valueType : FrontSubElementTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    FrontSubElementTypeEnum(Integer value, String desc) {
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