package com.xinxian.generator.enums;

import java.util.Objects;

/**
 * @ClassName GenFunctionEnum
 * @Description 生成功能枚举
 * @Author lmy
 * @Date 2023/5/3 23:22
 */
public enum GenFunctionEnum {

    /**
     * web
     */
    WEB(1, "web"),

    /**
     * facade
     */
    FACADE(2, "facade"),

    /**
     * 增加字段
     */
    ADD_FIELD(3, "addField"),

    /**
     * 三方Api创建表
     */
    THRID_API_CREATE(4, "thridApiCreate"),

    /**
     * 三方API同步
     */
    THRID_API_SYNC(5, "thridApiSync"),

    /**
     * 同步库
     */
    SYNC_DB(6, "syncDB"),

    ;
    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    public static GenFunctionEnum toEnum(Integer value) {
        for (GenFunctionEnum valueType : GenFunctionEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static GenFunctionEnum getInstance(String desc) {
        for (GenFunctionEnum valueType : GenFunctionEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    GenFunctionEnum(Integer value, String desc) {
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
