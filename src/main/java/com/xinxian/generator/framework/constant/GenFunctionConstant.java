package com.xinxian.generator.framework.constant;

import com.xinxian.generator.enums.GenFunctionEnum;
import com.xinxian.generator.enums.GenTypeEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenFunctionConstant
 * @Description 生成功能常量
 * @Author lmy
 * @Date 2023/5/3 23:34
 */
public class GenFunctionConstant {

    /* class,import,attribute,method,call,param,content  */
    private static final String importFastjson = "import com.alibaba.fastjson.JSON;\n";
    private static final String importHttpCommonUtils = "import ${packageBase}.util.HttpCommonUtils;\n";
    private static final String attributeApiHost = "    private final String ${shortEntityName}ApiHost = \"https://api.xx.com\";\n";
    private static final String attributePath = "    private final String path${entityName} = \"/api/v1/xx/xx\";\n";
    private static final String methodList = "list";
    private static final String methodDetail = "detail";
    private static final String methodAdd = "add";
    private static final String methodUpdate = "update";
    private static final String methodDeleteById = "deleteById";
    private static final String methodBatchUpdate = "batchUpdate";
    private static final String methodDeleteByIds = "deleteByIds";
    private static final String methodConvertToResult = "convertTo${entityName}Result";
    private static final String methodConvertToListResult = "convertToList${entityName}Result";
    private static final String methodSetProperty = "setProperty";
    private static final String methodListByPage = "listByPage";
    private static final String methodCheckPageParam = "checkPageParam";
    private static final String methodSyncEntityName = "sync${entityName}";
    private static final String callUpdateByPrimaryKeySelective = ".updateByPrimaryKeySelective(";
    private static final String setCreateTime = "        record.setCreateTime(DateUtil.date());\n";
    private static final String setCreateUserId = "        record.setCreateUserId(record.getUpdateUserId());\n";
    private static final String setcreateBy = "        record.setcreateBy(record.getupdateBy());\n";
    private static final String setStatus = "        record.setIsDelete(0);\n";
    private static final String updateUserId = ", updateUserId";
    private static final String updateUserIdDefinition = ", Long updateUserId";
    private static final String setIsOwner = "        if (updateUserId != null) {\n            result.setIsOwn(Objects.equals(updateUserId, result.getCreateUserId()));\n        }\n";
    private static final String updateUserIdComments = "     * @param updateUserId {@link Long}\n";
    private static final String getUpdateUserId = ", param.getUpdateUserId()";
    private static final String convertToList = "        List<${entityName}> records = ${entityName}Converter.convertToList${entityName}(params);\n";
    private static final Pair pairClassParam2Entity = Pair.of("${entityName}Param", "${entityName}");
    private static final Pair pairImportParam2Entity = Pair.of("import ${packageBase}.bean.view.${entityName}Param;", "import ${packageBase}.bean.po.${entityName};");
    private static final Pair pairParamParams2Records = Pair.of("params", "records");
    private static final Pair pairSyncTime = Pair.of("record.setUpdateTime(DateUtil.date());", "record.setSyncTime(DateUtil.date());");
    private static final Pair pairIgnoreOwner = Pair.of("convertTo${entityName}Result(record, null", "convertTo${entityName}Result(record");


    /* 功能和删除生成类型映射 */
    public static Map<String, List<String>> deleteGenTypeMap = new HashMap() {
        {
            put(GenFunctionEnum.WEB.name(), GenTypeEnum.ADD_FIELD_SQL.getValue());
            put(GenFunctionEnum.FACADE.name(), Arrays.asList(GenTypeEnum.ADD_FIELD_SQL.getValue(), GenTypeEnum.VO.getValue()));
            put(GenFunctionEnum.THRID_API_SYNC.name(), Arrays.asList(GenTypeEnum.CONTROLLER.getValue(), GenTypeEnum.ADD_FIELD_SQL.getValue(), GenTypeEnum.VO.getValue()));
        }
    };

    /* 功能和删除方法映射 */
    public static Map<String, List<String>> deleteMethodMap = new HashMap() {
        {
        }
    };

    /* 功能和删除内容映射 */
    public static Map<String, List<String>> deleteContentMap = new HashMap() {
        {
            put(GenFunctionEnum.THRID_API_SYNC.name() + GenTypeEnum.SERVICE_IMPL.name(), Arrays.asList(setCreateTime, setCreateUserId, setcreateBy, setStatus));
        }
    };

    /* 功能和替换内容映射 */
    public static Map<String, List<Pair<String, String>>> replaceContentPairMap = new HashMap() {
        {
            put(GenFunctionEnum.THRID_API_SYNC.name() + GenTypeEnum.SERVICE_IMPL.name(), Arrays.asList(pairSyncTime));
        }
    };
}
