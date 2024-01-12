package com.xinxian.generator.framework.enums;


import java.util.Objects;

/**
 * 支持需求枚举
 *
 * @author lmy
 * @date 2022/4/1 5:28 PM
 **/
public enum SupportRequirementEnum {

    /**
     * 生成代码
     */
    GEN_CODE("1", "生成代码"),

    /**
     * 增加字段
     */
    ADD_FIELD("2", "增加字段"),

    /**
     * 三方建表
     */
    THRID_CREATE("3", "三方建表"),

    ;
    /**
     * 值
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    public static SupportRequirementEnum toEnum(String value) {
        for (SupportRequirementEnum valueType : SupportRequirementEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static SupportRequirementEnum getInstance(String desc) {
        for (SupportRequirementEnum valueType : SupportRequirementEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    SupportRequirementEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }
}
