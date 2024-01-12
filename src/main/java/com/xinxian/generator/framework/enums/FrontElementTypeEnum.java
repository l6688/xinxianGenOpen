package com.xinxian.generator.framework.enums;


import java.util.Objects;

/**
 * 前端要素类枚举
 *
 * @author lmy
 * @date 2023/4/9 3:18 PM
 **/
public enum FrontElementTypeEnum {

    /**
     * 表单项
     */
    FORM_ITEM(1, "表单项", "${el-form-item}"),

    /**
     * 列表
     */
    TABLE_COLUMN(2, "列表", "${el-table-column}"),

    /**
     * 格式方法
     */
    FORMAT_METHOD(3, "格式方法", "${onFormatMethod}"),

    ;

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    /**
     * key
     */
    private final String key;

    public static FrontElementTypeEnum toEnum(Integer value) {
        for (FrontElementTypeEnum valueType : FrontElementTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static FrontElementTypeEnum getInstance(String desc) {
        for (FrontElementTypeEnum valueType : FrontElementTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    public static FrontElementTypeEnum getInstanceByKey(String key) {
        for (FrontElementTypeEnum valueType : FrontElementTypeEnum.values()) {
            if (Objects.equals(valueType.getKey(), key)) {
                return valueType;
            }
        }
        return null;
    }

    FrontElementTypeEnum(Integer value, String desc, String key) {
        this.value = value;
        this.desc = desc;
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}