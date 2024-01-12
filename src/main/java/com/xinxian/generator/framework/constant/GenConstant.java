package com.xinxian.generator.framework.constant;

import com.xinxian.generator.constant.GenConfigConstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName GenConstant
 * @Description gen常量
 * @Author lmy
 * @Date 2023/2/11 22:37
 */
public class GenConstant {
    public static final String JDBC_TYPE_TIMESTAMP = "TIMESTAMP";
    public static final String CONFIG_MAP_HAS_DATE_KEY = "hasDateProperty";
    public static final String ENTER = "\n";
    public static final String KEY_INPUT_PATH = "inputPath";
    public static final String KEY_OUTPUT_PATH = "outputPath";
    public static final String KEY_UPPER_FIRST_GEN_TYPE = "${upperFirstGenType}";
    public static final String KEY_GEN_TYPE_METHOD = "genTypeMethod";
    public static final String KEY_TEMPLATE = "template";
    public static final String GEN_CONSTANT_CLASS = "\nGenConstant:\n";
    public static final String keyIndexExpansionHeaderSubElement = "indexExpansionHeaderSubElement";
    public static final String keyQueryFieldList = "queryFieldList";
    public static final String keyRelationTableList = "relationTableList";
    public static final String keyUniqueFieldList = "uniqueFieldList";
    public static final String keyCheckParamFields = "${checkParamFields}";
    public static final String keyTemplateMap = "templateMap";
    public static final String keyQueryFieldFilterForm = "${queryFieldFilterForm}";
    public static final String keyQueryFieldOnResetDebounce = "${queryFieldOnResetDebounce}";
    public static final String keyQueryFieldSetCondition = "${queryFieldSetCondition}";
    public static final String keyRelationTableImport = "${relationTableImport}";
    public static final String keyRelationTableProperty = "${relationTableProperty}";
    public static final String keyRelationTableNeedDefine = "${relationTableNeedDefine}";
    public static final String keyRelationTableNeedDefineList = "${relationTableNeedDefineList}";
    public static final String keyRelationTableNeedCall = "${relationTableNeedCall}";
    public static final String keyRelationTableAdd = "${relationTableAdd}";
    public static final String keyRelationTable2Add = "${relationTable2Add}";
    public static final String keyRelationTableUpdate = "${relationTableUpdate}";
    public static final String keyRelationTableDelete = "${relationTableDelete}";
    public static final String keyRelationTableDescribe = "${relationTableDescribe}";
    public static final String keyRelationTableParamDefine = "${relationTableParamDefine}";
    public static final String keyRelationTableCallGetSlaveMap = "${relationTableCallGetSlaveMap}";
    public static final String keyRelationTableCallGetSlaveMapList = "${relationTableCallGetSlaveMapList}";
    public static final String keyRelationTableMethodSetSlave = "${relationTableMethodSetSlave}";
    public static final String keyRelationTableCallSetSlave = "${relationTableCallSetSlave}";
    public static final String keyRelationTableDescribeSetProperty = "${relationTableDescribeSetProperty}";
    public static final String keyRelationTableParamSetProperty = "${relationTableParamSetProperty}";
    public static final String keyRelationTableCallSetProperty = "${relationTableCallSetProperty}";
    public static final String keyRelationTableMethodGetSlave = "${relationTableMethodGetSlave}";
    public static final String keyUniqueFieldDelete = "${uniqueFieldDelete}";
    public static final String keyUniqueFieldCallCheckUniqueCombine = "${uniqueFieldCallCheckUniqueCombine}";
    public static final String keyUniqueFieldMethodCheckUniqueCombine = "${uniqueFieldMethodCheckUniqueCombine}";
    public static final String keyUniqueFieldCheckBatchUnique = "${uniqueFieldCheckBatchUnique}";
    public static final String keyAddFieldSql = "${addFieldSql}";
    public static final String keyParamMap = "${paramMap}";
    public static final String keyResultMap = "${resultMap}";
    public static final String keyPropertyMap = "propertyMap";
    public static final String keyAllParamMap = "${allParamMap}";
    public static final String keyAllResultMap = "${allResultMap}";
    public static final String keyGenTableConfig = "genTableConfig";
    public static final String keyGenTable = "genTable";
    public static final String keySetRelationId = "${setRelationId}";
    public static final String keyGeneratorConfigTable = "${genConfigTable}";
    public static String keyGeneratorConfigList = "${generatorConfigList}";
    public static String keyAddFieldMap = "${addFieldMap}";
    public static final Set<String> sqlKeyWordSet = new HashSet(Arrays.asList("ADD", "ALL", "ALTER", "AND", "AS", "ASC", "AUTO_INCREMENT", "AVG", "BETWEEN", "BIGINT", "BINARY", "BIT", "BY", "CASE", "CAST", "CHAR", "CHECK", "COLLATE", "COLUMN", "CONSTRAINT", "COUNT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "DATABASE", "DATEADD", "DATEDIFF", "DAY", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DESCRIBE", "DISTINCT", "DROP", "EXISTS", "ELSE", "END", "ENUM", "ESCAPE", "EXCEPT", "EXECUTE", "EXISTS", "FALSE", "FETCH", "FLOAT", "FOREIGN", "FROM", "FULL OUTER JOIN", "GROUP BY", "HAVING", "IN", "INDEX", "INNER JOIN", "INSERT INTO", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "JOIN", "KEY", "LEFT JOIN", "LIKE", "LIMIT", "MAX", "MIN", "NATURAL JOIN", "NOT", "NULL", "NUMERIC", "ON", "OR", "ORDER BY", "OUTER JOIN", "PRIMARY", "PROCEDURE", "REAL", "REFERENCES", "RIGHT JOIN", "ROLLBACK", "ROW_NUMBER", "ROWS", "SELECT", "SET", "SMALLINT", "SUM", "TABLE", "TEMPORARY", "TEXT", "THEN", "TIME", "TIMESTAMP", "TOP", "TRUNCATE", "TRUE", "UNION", "UNIQUE", "UPDATE", "VALUES", "VARCHAR", "VIEW", "WHEN", "WHERE"));
    /* genProject */
    public static final String keyProjectName = "projectName";
    public static final String keyProjectDesc = "projectDesc";
    public static final String keyProjectPrefix = "projectPrefix";
    public static final String keyProjectPathName = "projectPath";
    public static final String keyBackendProject = "xin-backend-project";
    public static final String keyBackendProjectPath = "xin.backend.project";
    public static final String keyBackendProjectUnderline = "xin_backend_project";
    public static final String keyBackendProjectSlash = "xin/backend/project";
    public static final String keyBackendProjectSpacing = "xin Backend Project";
    public static final String keyUpperBackendProject = "XinBackendProject";
    public static final String keyUpperShortBackendProject = "BackendProject";
    public static final String keyShortBackendProjectSlash = "backend/project";
    public static final String keyShortBackendProjectCamel = "backendProject";
    public static final String keyBackendApiPath = "backendApiPath";
    public static final String keyProjectAuthor = "author";
    public static final String keyProjectDate = "projectDate";
    public static final String keyProjectPath = "projectPath";
    public static final String keyProjectSourcePath = "projectSourcePath";
    public static final String keyFrontProjectSourcePath = "frontProjectSourcePath";
    public static final String keyDeleteOldProject = "deleteOldProject";
    public static final String keyFrontNewProjectName = "front-platform-web";
    public static final String keyFrontProjectDesc = "frontProjectDesc";
    public static final String keyFrontProjectShortName = "front_platform";
    public static final String keyFrontProjectDetailDesc = "frontProjectDetailDesc";
    public static final String keyNextPlatformValue = "nextPlatformValue";
    public static final String keyGenBackendSwitch = "genBackendSwitch";
    public static final String keyGenFrontSwitch = "genBackendSwitch";
    public static final Set<String> BASE_TEST_ADD_IGNORE_SET = new HashSet<>(Arrays.asList("id", "updateUserEmail", "updateUserDept", "updateCompany", "createUserId", "updateUserId", "createBy", "updateBy", "createTime", "updateTime","isDelete","page", "pageSize"));
    public static final Set<String> PAGE_SET = new HashSet<>(Arrays.asList("page", "pageSize"));
}
