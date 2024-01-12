package com.xinxian.generator.enums;

import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName GenTypeEnum
 * @Description 生成类型枚举
 * @Author lmy
 * @Date 2023/1/21 07:00
 */
public enum GenTypeEnum {

    /**
     * 实体
     */
    ENTITY("entity", "实体"),

    /**
     * mapper映射xml
     */
    MAPPER_XML("mapperXml", "mapper映射xml"),

    /**
     * mapper映射
     */
    MAPPER("mapper", "mapper映射"),

    /**
     * 控制器
     */
    CONTROLLER("controller", "控制器"),

    /**
     * 参数
     */
    PARAM("param", "参数"),

    /**
     * 服务
     */
    SERVICE("service", "服务"),

    /**
     * 服务实现
     */
    SERVICE_IMPL("serviceImpl", "服务实现"),

    /**
     * 转换器
     */
    CONVERTER("converter", "转换器"),

    /**
     * VO
     */
    VO("vo", "VO"),

    /**
     * 枚举
     */
    ENUM("enum", "枚举"),

    /**
     * 增加字段sql
     */
    ADD_FIELD_SQL("addFieldSql", "增加字段sql"),

    /**
     * 测试
     */
    //TEST("test", "测试"),

    ;

    /**
     * 值
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    public static GenTypeEnum toEnum(String value) {
        for (GenTypeEnum valueType : GenTypeEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static GenTypeEnum getInstance(String desc) {
        for (GenTypeEnum valueType : GenTypeEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    GenTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static boolean isItem(GenTypeEnum genTypeEnum) {
        if (Objects.equals(genTypeEnum, ENUM)) {
            return true;
        }
        return false;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    public static Boolean frontClass(GenTypeEnum genTypeEnum) {
        return false;
    }

    public static String getAddFieldKey(GenTypeEnum genTypeEnum) {
        String addFieldKey = "";
        switch (genTypeEnum) {
            case ENTITY:
                addFieldKey = "${entityMap}";
                break;
            case PARAM:
            case VO:
                addFieldKey = "${paramMap}";
                break;
            default:
                break;
        }
        return addFieldKey;
    }

    public static List<String> getValueList() {
        List<String> valueList = Lists.newArrayList();
        for (GenTypeEnum valueType : GenTypeEnum.values()) {
            valueList.add(valueType.getValue());
        }
        return valueList;
    }
}
