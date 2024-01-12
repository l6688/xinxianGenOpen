package com.xinxian.generator.framework.enums;

/**
 * @author lmy
 * @ClassName DataTypeEnum
 * @Description 字段数据类型枚举
 * @Date 2022/9/16 15:10
 */
public enum DataTypeEnum {

    ENUM_TYPE(1, "枚举型", "enum"),
    LONG_NUMBER_TYPE(2, "长整型", "bigint"),
    DATE_TYPE(3, "时间戳", "datetime"),
    BOOLEAN_TYPE(4, "布尔型", "boolean"),
    CHAR_TYPE(5, "字符型", "varchar"),
    FLOAT_TYPE(6, "浮点型", "decimal"),
    OBJECT_TYPE(7, "对象型", "object"),
    LIST_TYPE(8, "数组型", "array"),
    NUMBER_TYPE(9, "数值型", "integer"),
    ;


    DataTypeEnum(Integer code, String desc, String keyword) {
        this.code = code;
        this.desc = desc;
        this.keyword = keyword;
    }

    public static DataTypeEnum getInstance(String operator) {
        DataTypeEnum[] values = DataTypeEnum.values();
        for (DataTypeEnum value : values) {
            if (value.getDesc().contains(operator)) {
                return value;
            }
        }
        return null;
    }

    public static DataTypeEnum getInstanceByCode(Integer code) {
        DataTypeEnum[] values = DataTypeEnum.values();
        for (DataTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static DataTypeEnum getByKeyword(String dataType) {
        DataTypeEnum[] values = DataTypeEnum.values();
        for (DataTypeEnum value : values) {
            if (dataType.contains(value.getKeyword())) {
                return value;
            }
        }
        return null;
    }

    private Integer code;

    private String desc;

    private String keyword;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getKeyword() {
        return keyword;
    }
}
