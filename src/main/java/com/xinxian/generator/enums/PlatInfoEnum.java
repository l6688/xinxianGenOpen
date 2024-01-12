package com.xinxian.generator.enums;


import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 平台信息
 *
 * @author lmy
 * @date 2022/8/17 5:28 PM
 **/
public enum PlatInfoEnum {

    /**
     * 知识库
     */
    KNOWLEDGE(1, "知识库", "knowledge", "ai-cv-knowledge-base-adm-app", "knowledge", "knowledge-web", "knowledge_base_db_new"),
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
     * 平台简称
     */
    private final String name;

    /**
     * 平台名称
     */
    private final String projectName;

    /**
     * 平台api名称
     */
    private final String apiBase;

    /**
     * 前端名称
     */
    private final String frontProjectName;

    /**
     * 数据库
     */
    private final String database;


    public static PlatInfoEnum toEnum(Integer value) {
        for (PlatInfoEnum valueType : PlatInfoEnum.values()) {
            if (valueType.getValue().equals(value)) {
                return valueType;
            }
        }
        return null;
    }

    public static PlatInfoEnum getInstance(String desc) {
        for (PlatInfoEnum valueType : PlatInfoEnum.values()) {
            if (Objects.equals(valueType.getDesc(), desc)) {
                return valueType;
            }
        }
        return null;
    }

    public static PlatInfoEnum getInstanceByProjectName(String projectName) {
        for (PlatInfoEnum valueType : PlatInfoEnum.values()) {
            if (Objects.equals(valueType.getProjectName(), projectName)) {
                return valueType;
            }
        }
        return null;
    }

    public static String allDesc() {
        return String.join("，", Arrays.stream(PlatInfoEnum.values()).map(PlatInfoEnum::getDesc).collect(Collectors.toList()));
    }

    PlatInfoEnum(Integer value, String desc, String name, String projectName, String apiBase, String frontProjectName, String database) {
        this.value = value;
        this.desc = desc;
        this.name = name;
        this.projectName = projectName;
        this.apiBase = apiBase;
        this.frontProjectName = frontProjectName;
        this.database = database;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getApiBase() {
        return apiBase;
    }

    public String getFrontProjectName() {
        return frontProjectName;
    }

    public String getDatabase() {
        return database;
    }
}
