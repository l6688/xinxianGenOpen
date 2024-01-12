package com.xinxian.generator.framework.dto.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "gen_table_column")
public class GenTableColumn {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 代码生成业务表id
     */
    @Column(name = "gen_table_id")
    private Long genTableId;

    /**
     * 列名称
     */
    @Column(name = "column_name")
    private String columnName;

    /**
     * 列描述
     */
    @Column(name = "column_comment")
    private String columnComment;

    /**
     * 列类型
     */
    @Column(name = "column_type")
    private String columnType;

    /**
     * JAVA类型
     */
    @Column(name = "java_type")
    private String javaType;

    /**
     * JAVA字段名
     */
    @Column(name = "java_field")
    private String javaField;

    /**
     * 是否主键（1是）
     */
    @Column(name = "is_pk")
    private String isPk;

    /**
     * 是否自增（1是）
     */
    @Column(name = "is_increment")
    private String isIncrement;

    /**
     * 是否必填（1是）
     */
    @Column(name = "is_required")
    private String isRequired;

    /**
     * 是否为插入字段（1是）
     */
    @Column(name = "is_insert")
    private String isInsert;

    /**
     * 是否编辑字段（1是）
     */
    @Column(name = "is_edit")
    private String isEdit;

    /**
     * 是否列表字段（1是）
     */
    @Column(name = "is_list")
    private String isList;

    /**
     * 是否查询字段（1是）
     */
    @Column(name = "is_query")
    private String isQuery;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @Column(name = "query_type")
    private String queryType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Column(name = "html_type")
    private String htmlType;

    /**
     * 字典类型
     */
    @Column(name = "dict_type")
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 最后更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人id
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 更新人
     */
    @Column(name = "update_user_name")
    private String updateUserName;

    /**
     * 0删除 1正常
     */
    private Integer status;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取代码生成业务表id
     *
     * @return gen_table_id - 代码生成业务表id
     */
    public Long getGenTableId() {
        return genTableId;
    }

    /**
     * 设置代码生成业务表id
     *
     * @param genTableId 代码生成业务表id
     */
    public void setGenTableId(Long genTableId) {
        this.genTableId = genTableId;
    }

    /**
     * 获取列名称
     *
     * @return column_name - 列名称
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置列名称
     *
     * @param columnName 列名称
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    /**
     * 获取列描述
     *
     * @return column_comment - 列描述
     */
    public String getColumnComment() {
        return columnComment;
    }

    /**
     * 设置列描述
     *
     * @param columnComment 列描述
     */
    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment == null ? null : columnComment.trim();
    }

    /**
     * 获取列类型
     *
     * @return column_type - 列类型
     */
    public String getColumnType() {
        return columnType;
    }

    /**
     * 设置列类型
     *
     * @param columnType 列类型
     */
    public void setColumnType(String columnType) {
        this.columnType = columnType == null ? null : columnType.trim();
    }

    /**
     * 获取JAVA类型
     *
     * @return java_type - JAVA类型
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * 设置JAVA类型
     *
     * @param javaType JAVA类型
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType == null ? null : javaType.trim();
    }

    /**
     * 获取JAVA字段名
     *
     * @return java_field - JAVA字段名
     */
    public String getJavaField() {
        return javaField;
    }

    /**
     * 设置JAVA字段名
     *
     * @param javaField JAVA字段名
     */
    public void setJavaField(String javaField) {
        this.javaField = javaField == null ? null : javaField.trim();
    }

    /**
     * 获取是否主键（1是）
     *
     * @return is_pk - 是否主键（1是）
     */
    public String getIsPk() {
        return isPk;
    }

    /**
     * 设置是否主键（1是）
     *
     * @param isPk 是否主键（1是）
     */
    public void setIsPk(String isPk) {
        this.isPk = isPk == null ? null : isPk.trim();
    }

    /**
     * 获取是否自增（1是）
     *
     * @return is_increment - 是否自增（1是）
     */
    public String getIsIncrement() {
        return isIncrement;
    }

    /**
     * 设置是否自增（1是）
     *
     * @param isIncrement 是否自增（1是）
     */
    public void setIsIncrement(String isIncrement) {
        this.isIncrement = isIncrement == null ? null : isIncrement.trim();
    }

    /**
     * 获取是否必填（1是）
     *
     * @return is_required - 是否必填（1是）
     */
    public String getIsRequired() {
        return isRequired;
    }

    /**
     * 设置是否必填（1是）
     *
     * @param isRequired 是否必填（1是）
     */
    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired == null ? null : isRequired.trim();
    }

    /**
     * 获取是否为插入字段（1是）
     *
     * @return is_insert - 是否为插入字段（1是）
     */
    public String getIsInsert() {
        return isInsert;
    }

    /**
     * 设置是否为插入字段（1是）
     *
     * @param isInsert 是否为插入字段（1是）
     */
    public void setIsInsert(String isInsert) {
        this.isInsert = isInsert == null ? null : isInsert.trim();
    }

    /**
     * 获取是否编辑字段（1是）
     *
     * @return is_edit - 是否编辑字段（1是）
     */
    public String getIsEdit() {
        return isEdit;
    }

    /**
     * 设置是否编辑字段（1是）
     *
     * @param isEdit 是否编辑字段（1是）
     */
    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit == null ? null : isEdit.trim();
    }

    /**
     * 获取是否列表字段（1是）
     *
     * @return is_list - 是否列表字段（1是）
     */
    public String getIsList() {
        return isList;
    }

    /**
     * 设置是否列表字段（1是）
     *
     * @param isList 是否列表字段（1是）
     */
    public void setIsList(String isList) {
        this.isList = isList == null ? null : isList.trim();
    }

    /**
     * 获取是否查询字段（1是）
     *
     * @return is_query - 是否查询字段（1是）
     */
    public String getIsQuery() {
        return isQuery;
    }

    /**
     * 设置是否查询字段（1是）
     *
     * @param isQuery 是否查询字段（1是）
     */
    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery == null ? null : isQuery.trim();
    }

    /**
     * 获取查询方式（等于、不等于、大于、小于、范围）
     *
     * @return query_type - 查询方式（等于、不等于、大于、小于、范围）
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * 设置查询方式（等于、不等于、大于、小于、范围）
     *
     * @param queryType 查询方式（等于、不等于、大于、小于、范围）
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType == null ? null : queryType.trim();
    }

    /**
     * 获取显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     *
     * @return html_type - 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    public String getHtmlType() {
        return htmlType;
    }

    /**
     * 设置显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     *
     * @param htmlType 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType == null ? null : htmlType.trim();
    }

    /**
     * 获取字典类型
     *
     * @return dict_type - 字典类型
     */
    public String getDictType() {
        return dictType;
    }

    /**
     * 设置字典类型
     *
     * @param dictType 字典类型
     */
    public void setDictType(String dictType) {
        this.dictType = dictType == null ? null : dictType.trim();
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人id
     *
     * @return create_user_id - 创建人id
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人id
     *
     * @param createUserId 创建人id
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取最后更新时间
     *
     * @return update_time - 最后更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后更新时间
     *
     * @param updateTime 最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取更新人id
     *
     * @return update_user_id - 更新人id
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /**
     * 设置更新人id
     *
     * @param updateUserId 更新人id
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * 获取创建人
     *
     * @return create_user_name - 创建人
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * 设置创建人
     *
     * @param createUserName 创建人
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    /**
     * 获取更新人
     *
     * @return update_user_name - 更新人
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * 设置更新人
     *
     * @param updateUserName 更新人
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
    }

    /**
     * 获取0删除 1正常
     *
     * @return status - 0删除 1正常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0删除 1正常
     *
     * @param status 0删除 1正常
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}