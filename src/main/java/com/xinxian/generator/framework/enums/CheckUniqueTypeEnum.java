package com.xinxian.generator.framework.enums;


import java.util.Objects;

/**
 * 检查唯一约束的方式
 *
 * @author lmy
 * @date 2022/4/1 5:28 PM
 **/
public enum CheckUniqueTypeEnum {

    /**
     * 异常提示
     */
    NOTIFICATION(1, "异常提示"),

    /**
     * 删除之前数据
     */
    DELETE(2, "删除之前数据"),

    ;
    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    public static CheckUniqueTypeEnum toEnum(Integer value) {
        for (CheckUniqueTypeEnum valueType : CheckUniqueTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static CheckUniqueTypeEnum getInstance(String desc) {
        for (CheckUniqueTypeEnum valueType : CheckUniqueTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    CheckUniqueTypeEnum(Integer value, String desc) {
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
