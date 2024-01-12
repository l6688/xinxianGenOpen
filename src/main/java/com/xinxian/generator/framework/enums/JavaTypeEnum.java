package com.xinxian.generator.framework.enums;


import java.util.Objects;

/**
 * JavaTypeEnum
 *
 * @author lmy
 * @date 2022/8/17 5:28 PM
 **/
public enum JavaTypeEnum {

    /**
     * 字符串
     */
    STRING("String", "字符串String", "VARCHAR"),

    /**
     * 长整型
     */
    LONG("Long", "长整型Long", "BIGINT"),

    /**
     * 整型
     */
    INTEGER("Integer", "整型Integer", "INTEGER"),

    /**
     * 短整型
     */
    BYTE("Byte", "短整型Byte", "TINYINT"),

    /**
     * 时间
     */
    DATE("Date", "时间Date", "DATETIME"),

    /**
     * 布尔
     */
    BOOLEAN("Boolean", "布尔Boolean", "TINYINT"),

    /**
     * 浮点型
     */
    DOUBLE("Double", "浮点型Double", "DOUBLE"),

    /**
     * 自定义对象
     */
    OBJECT("%s", "自定义对象", ""),

    /**
     * 字符集合
     */
    LIST_STRING("List<String>", "字符集合List<String>", ""),

    /**
     * 长整型集合
     */
    LIST_LONG("List<Long>", "长整型集合List<Long>", ""),

    /**
     * 整型集合
     */
    LIST_INTEGER("List<Integer>", "整型集合List<Integer>", ""),

    /**
     * 自定义对象集合
     */
    LIST_OBJECT("List<%s%s>", "自定义对象集合List<%s%s>", ""),

    ;

    /**
     * 值
     */
    private final String value;


    /**
     * 描述
     */
    private final String desc;

    /**
     * jdbc类型
     */
    private final String jdbcType;


    public static JavaTypeEnum toEnum(String value) {
        for (JavaTypeEnum valueType : JavaTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static JavaTypeEnum getInstance(String desc) {
        for (JavaTypeEnum valueType : JavaTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    public static JavaTypeEnum getInstanceByJdbcType(String jdbcType) {
        for (JavaTypeEnum valueType : JavaTypeEnum.values()) {
            if (Objects.equals(valueType.getJdbcType(), jdbcType)) {
                return valueType;
            }
        }
        return null;
    }

    JavaTypeEnum(String value, String desc, String jdbcType) {
        this.value = value;
        this.desc = desc;
        this.jdbcType = jdbcType;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    public String getJdbcType() {
        return jdbcType;
    }
}
