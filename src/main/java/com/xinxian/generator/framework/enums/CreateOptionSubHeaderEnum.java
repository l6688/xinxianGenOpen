package com.xinxian.generator.framework.enums;


import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 前端创建SubHeader可选项枚举
 *
 * @author lmy
 * @date 2023/4/9 3:18 PM
 **/
public enum CreateOptionSubHeaderEnum {

    /**
     * 大文本
     */
    INPUT_TEXTAREA("1_1_1","大文本", FrontComponentTypeEnum.INPUT.getValue(), 1,false," type=\"textarea\" "),

    /**
     * 不能选
     */
    INPUT_CANNOT_SELECT("1_2_1","不能选",FrontComponentTypeEnum.INPUT.getValue(), 2,false," disabled "),

    /**
     * 限制长度
     */
    INPUT_LIMIT_LENGTH("1_3_1","限制长度",FrontComponentTypeEnum.INPUT.getValue(), 3,false," maxlength=\"100\" "),

    /**
     * 展示限制的长度
     */
    INPUT_DISPLAY_LIMIT_LENGTH("1_4_1","展示限制的长度",FrontComponentTypeEnum.INPUT.getValue(), 4,false," show-word-limit "),

    /**
     * 日期-格式化
     */
    TIME_DATE_VALUE_FORMAT("3_1_1","日期",FrontComponentTypeEnum.TIME.getValue(), 1,true," value-format=\"YYYY-MM-DD\" "),

    /**
     * 时间-格式化
     */
    TIME_TIME_VALUE_FORMAT("3_1_2","时间",FrontComponentTypeEnum.TIME.getValue(), 1,true," value-format=\"HH:mm:ss\" "),

    /**
     * 日期时间-格式化
     */
    TIME_DATE_TIME_VALUE_FORMAT("3_1_3","日期时间",FrontComponentTypeEnum.TIME.getValue(), 1,true," value-format=\"YYYY-MM-DD HH:mm:ss\" "),;

    /**
     * 值：大类_组_序号
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 分组号
     */
    private final Integer groupId;

    /**
     * 必选
     */
    private final Boolean require;

    /**
     * 内容
     */
    private final String content;

    public static CreateOptionSubHeaderEnum toEnum(String value) {
        for (CreateOptionSubHeaderEnum valueType : CreateOptionSubHeaderEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static CreateOptionSubHeaderEnum getInstance(String desc) {
        for (CreateOptionSubHeaderEnum valueType : CreateOptionSubHeaderEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    public static List<CreateOptionSubHeaderEnum> getRequireByType(Integer type) {
        ArrayList<CreateOptionSubHeaderEnum> results = Lists.newArrayList();
        for (CreateOptionSubHeaderEnum valueType : CreateOptionSubHeaderEnum.values()) {
            if (Objects.equals(valueType.getType(), type) && valueType.getRequire()) {
                results.add(valueType);
            }
        }
        return results;
    }

    CreateOptionSubHeaderEnum(String value, String desc, Integer type, Integer groupId, Boolean require, String content) {
        this.value = value;
        this.desc = desc;
        this.type = type;
        this.groupId = groupId;
        this.require = require;
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    public Integer getType() {
        return type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Boolean getRequire() {
        return require;
    }

    public String getContent() {
        return content;
    }

    }