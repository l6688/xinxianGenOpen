package com.xinxian.generator.utils;

import com.xinxian.generator.framework.bean.EntityProperty;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.enums.JavaTypeEnum;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import com.xinxian.generator.framework.converter.RequirementsAnalysisTaskConverter;
import com.xinxian.generator.framework.dto.vo.EntityContentVO;
import com.xinxian.generator.framework.dto.vo.EntityPropertyVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName GeneratorAddFieldSqlUtils
 * @Description 自动生成增加字段sql工具包
 * @Author lmy
 * @Date 2023/05/27 23:05
 */
public class GeneratorAddFieldSqlUtils {

    public static final String addFieldItemTemplate = "    ${column} ${jdbcType}(${jdbcLength}) ${default} COMMENT '${desc}',\n";
    private static final String nullDefault = "DEFAULT NULL";
    private static final String notNullStr = "NOT NULL ";
    private static final String addFieldTemplate = "ALTER TABLE ${database}.${tableName} ADD ${column} ${jdbcType}(${jdbcLength}) ${default} COMMENT '${desc}';\n";
    public static final String keyTemplate = "    ,KEY idx_${all_key_name} (${all_key}) USING BTREE\n";
    public static final String addEntityTemplate = "  CREATE TABLE ${database}.${tableName} (\n" +
            "    id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
            "${sqlItemList}" +
            "    create_time datetime(3) NOT NULL COMMENT '创建时间',\n" +
            "    create_user_id bigint(100) NOT NULL COMMENT '创建人id',\n" +
            "    update_time datetime(3) DEFAULT NULL COMMENT '最后更新时间',\n" +
            "    update_user_id bigint(100) NOT NULL COMMENT '更新人id',\n" +
            "    create_by varchar(100) NOT NULL COMMENT '创建人',\n" +
            "    update_by varchar(100) DEFAULT NULL COMMENT '更新人',\n" +
            "    status int(11) NOT NULL DEFAULT '1' COMMENT '0删除 1正常',\n" +
            "    PRIMARY KEY (id) USING BTREE\n" +
            "${keyList}" +
            ") DEFAULT CHARSET=utf8mb4 COMMENT='${comment}';\n";

    /**
     * 前置处理
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    public static String beforeProcess(String genText, Map<String, String> configMap) {
        return genText;
    }

    /**
     * 后置处理
     *
     * @param genText   {@link String}
     * @param configMap {@link Map<String, String>}
     * @return {@link String}
     */
    public static String afterProcess(String genText, Map<String, String> configMap) {
        String text = GeneratorUtils.doGenerator(addFieldTemplate, configMap);
        String generatorText = processAddField(text, configMap);
        return genText.replace(GenConstant.keyAddFieldSql, generatorText);
    }

    /**
     * 处理增加字段sql
     *
     * @param text      {@link String}
     * @param configMap {@link Map}
     * @return {@link String}
     */
    public static String processAddField(String text, Map<String, String> configMap) {
        LinkedHashMap<String, EntityProperty> addFieldPropertyMap = GeneratorUtils.getFieldPropertyMap(configMap, "${addFieldMap}");
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, EntityProperty> entry : addFieldPropertyMap.entrySet()) {
            EntityProperty property = entry.getValue();
            String sqlItem = genAddFieldSqlItem(property, text);
            stringBuilder.append(sqlItem);
        }
        return stringBuilder.toString();
    }

    /**
     * 生成增加字段sql
     *
     * @param property {@link EntityProperty}
     * @param text     {@link String}
     * @return {@link String}
     */
    public static String genAddFieldSqlItem(EntityProperty property, String text) {
        Boolean notNull = property.getNotNull() != null ? property.getNotNull() : StringUtils.isNotEmpty(property.getDefaultValue());
        String defaultValue = getDefaultValue(property.getJdbcType(), property.getDefaultValue());
        String column = GenConstant.sqlKeyWordSet.contains(property.getColumn().toUpperCase()) ? "`" + property.getColumn() + "`" : property.getColumn();
        String notNullAndDefaultStr = getNotNullAndDefault(property.getJdbcType(), defaultValue, notNull);
        String jdbcType = Objects.equals(property.getJdbcType(), "INTEGER") ? "int" : property.getJdbcType();
        String sql = text.replace("${property}", property.getProperty()).replace("${jdbcType}", jdbcType)
                .replace("${column}", column)
                .replace("${jdbcLength}", property.getLength()).replace("${desc}", property.getDesc())
                .replace("${default}", notNullAndDefaultStr);
        return sql;
    }

    /**
     * 获取默认值
     *
     * @param jdbcType     {@link String}
     * @param defaultValue {@link String}
     * @return {@link String}
     */
    private static String getNotNullAndDefault(String jdbcType, String defaultValue, Boolean notNull) {
        String notNullString = notNull ? notNullStr : "";
        String defaultStr = defaultValue == null ? nullDefault : "DEFAULT '${defaultValue}'".replace("${defaultValue}", defaultValue);
        if (Objects.equals(jdbcType, JavaTypeEnum.DATE.getJdbcType())) {
            if (StringUtils.isEmpty(defaultValue)) {
                defaultStr = nullDefault;
            }
        }
        if (Objects.equals(notNullString, notNullStr) && Objects.equals(defaultStr, nullDefault)) {
            defaultStr = "";
        }
        return notNullString + defaultStr;
    }

    /**
     * 获取默认值
     *
     * @param jdbcType     {@link String}
     * @param defaultValue {@link String}
     * @return {@link String}
     */
    private static String getDefaultValue(String jdbcType, String defaultValue) {
        String result = defaultValue;
        if (Objects.equals(jdbcType, "INTEGER") && StringUtils.isEmpty(defaultValue)) {
            result = "0";
        } else if (Objects.equals(jdbcType, "BIGINT") && StringUtils.isEmpty(defaultValue)) {
            result = "0";
        } else if (Objects.equals(jdbcType, JavaTypeEnum.BYTE.getJdbcType()) && StringUtils.isEmpty(defaultValue)) {
            result = "0";
        }
        return result;
    }

    /**
     * 生成增加字段sql
     *
     * @param param {@link EntityContentVO}
     * @return {@link String}
     */
    public static String genAddEntitySql(EntityContentVO param) {
        if (param == null || CollectionUtils.isEmpty(param.getItemList())) {
            return null;
        }
        String sql = GeneratorAddFieldSqlUtils.addEntityTemplate;
        String sqlItemList = getSqlItemList(param);
        String keyList = getKeyList(param);
        sql = sql.replace("${sqlItemList}", sqlItemList)
                .replace("${keyList}", keyList)
                .replace("${database}", param.getDatabase())
                .replace("${tableName}", param.getName())
                .replace("${comment}", param.getComment());
        return sql;
    }

    /**
     * 获取sql子项集合
     *
     * @param param {@link EntityContentVO}
     * @return {@link String}
     */
    public static String getSqlItemList(EntityContentVO param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (EntityPropertyVO entityPropertyVO : param.getItemList()) {
            EntityProperty entityProperty = RequirementsAnalysisTaskConverter.converterToEntityProperty(entityPropertyVO);
            String sqlItem = GeneratorAddFieldSqlUtils.genAddFieldSqlItem(entityProperty, GeneratorAddFieldSqlUtils.addFieldItemTemplate);
            stringBuilder.append(sqlItem);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取key集合
     *
     * @param param {@link EntityContentVO}
     * @return {@link String}
     */
    public static String getKeyList(EntityContentVO param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (EntityContentVO.KeyVO keyVO : param.getKeyList()) {
            String keyItem = GeneratorAddFieldSqlUtils.genKeyItem(keyVO, GeneratorAddFieldSqlUtils.keyTemplate);
            stringBuilder.append(keyItem);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取key子项
     *
     * @param keyVO       {@link EntityContentVO.KeyVO}
     * @param keyTemplate {@link String}
     * @return {@link String}
     */
    public static String genKeyItem(EntityContentVO.KeyVO keyVO, String keyTemplate) {
        if (CollectionUtils.isEmpty(keyVO.getFieldList())) {
            return null;
        }
        String all_key = StringUtils.join(keyVO.getFieldList(), ',');
        String all_key_name = StringUtils.isNotEmpty(keyVO.getName()) ? keyVO.getName() : StringUtils.join(keyVO.getFieldList(), '_');
        return keyTemplate.replace("${all_key}", all_key).replace("${all_key_name}", all_key_name);
    }

}
