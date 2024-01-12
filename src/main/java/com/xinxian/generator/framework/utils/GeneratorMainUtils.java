package com.xinxian.generator.framework.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xinxian.generator.constant.GenConfigConstant;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.enums.PlatInfoEnum;
import com.xinxian.generator.framework.bean.GenTableConfig;
import com.xinxian.generator.framework.constant.GenConstant;
import com.xinxian.generator.framework.enums.SupportRequirementEnum;
import com.xinxian.generator.service.GeneratorConfig;
import com.xinxian.generator.service.GeneratorProject;
import com.xinxian.generator.service.GeneratorTable;
import com.xinxian.generator.framework.converter.GenTableConverter;
import com.xinxian.generator.framework.dto.entity.GenTable;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName GeneratorGetData
 * @Description 自动生成优化
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
@Slf4j
public class GeneratorMainUtils {

    public static final String initKeyGenDate = "genDate";
    public static final String copyEntity = "true";


    /* 初始化map */
    private static Map<String, String> initMap = new HashMap();

    /* 时间map */
    public static Map<String, String> dateMap = new HashMap() {
        {
            /* 测试 */
            put("notice_info_table", "2023/04/21 16:04");
            put("notice_info_relation2", "2023/04/21 16:04");
            put("common_content", "2023/04/21 16:04");
            put("relation3_main", "2023/04/21 16:04");
            put("relation3_slave", "2023/04/21 16:04");
        }
    };

    public static final List<String> genAddField = new ArrayList() {
        {
            add(GenTypeEnum.ENTITY.getValue());
            add(GenTypeEnum.MAPPER_XML.getValue());
            add(GenTypeEnum.PARAM.getValue());
            add(GenTypeEnum.CONVERTER.getValue());
            add(GenTypeEnum.ADD_FIELD_SQL.getValue());
        }
    };

    public static final List<String> relationTableUnNeedGenTypes = new ArrayList() {
        {
            add(GenTypeEnum.CONTROLLER.getValue());
            add(GenTypeEnum.PARAM.getValue());
        }
    };

    /**
     * 新表生成代码：
     */
    public static void genNewTable(Map<String, String> configMap, GenTable genTable) {
        String projectName = genTable.getProjectName();
        HashMap<String, String> otherInfo = new HashMap<>();
        otherInfo.put(GenFunctionUtils.keyGenFunctionList, StrUtils.listStrToJsonStr(genTable.getGenFunctionList()));
        otherInfo.putAll(configMap);
        otherInfo.put("${supportRequirement}", SupportRequirementEnum.GEN_CODE.getValue());
        dateMap.put(genTable.getTableName(), genTable.getGenDate());
        ArrayList<String> genClassList = new ArrayList<>(StrUtils.strToList(genTable.getGenClassList()));
        /* 生成表 */
        ArrayList<String> tableList = new ArrayList<>();
        tableList.add(genTable.getTableName());
        doGeneratorClass(projectName, tableList, genClassList, null, otherInfo);
    }

    /**
     * 增加字段：
     */
    public static void genAddField(GenTable genTable) {
        String projectName = genTable.getProjectName();
        String tableName = genTable.getTableName();
        ArrayList<String> tableList = new ArrayList<>();
        ArrayList<String> genClassList = new ArrayList<>(genAddField);
        tableList.add(tableName);
        /* tableFieldMap */
        Map<String, Map<String, String>> tableFieldMap = new HashMap<>();
        Map<String, String> otherConfigMap = new HashMap<>();
        setInfoByGenTable(genTable, otherConfigMap);
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("${addFieldMap}", genTable.getAddFieldMap());
        otherConfigMap.put("${supportRequirement}", SupportRequirementEnum.ADD_FIELD.getValue());
        tableFieldMap.put(tableName, fieldMap);
        doGeneratorClass(projectName, tableList, genClassList, tableFieldMap, otherConfigMap);
    }

    /**
     * 通过生成表设置信息
     *
     * @param genTable       {@link GenTable}
     * @param otherConfigMap {@link Map<String,String>}
     */
    public static void setInfoByGenTable(GenTable genTable, Map<String, String> otherConfigMap) {
        otherConfigMap.put("${entityName}", genTable.getEntityName());
        otherConfigMap.put("${tableName}", genTable.getTableName());
        otherConfigMap.put("${entityDesc}", genTable.getEntityDesc());
        otherConfigMap.put("${bizClassName}", genTable.getBizClassName());
        otherConfigMap.put("${requestMapping}", genTable.getRequestMapping());
        dateMap.put(genTable.getTableName(), genTable.getGenDate());
        GenTableConfig genTableConfig = GenTableConverter.convertToGenTableConfig(genTable);
        otherConfigMap.put(GenConstant.keyGenTableConfig, JSON.toJSONString(genTableConfig));
        otherConfigMap.put(GenConstant.keyGenTable, JSON.toJSONString(genTable));
        otherConfigMap.put(GenConstant.keyAddFieldMap, genTable.getAddFieldMap());
    }

    /**
     * 生成配置
     */
    public static void setCommonConfig(Map<String, String> configMap, String projectName) {
        configMap.putAll(GeneratorProject.getConfig(projectName));
        GeneratorProject generatorProject = new GeneratorProject(configMap);
        configMap.putAll(generatorProject.getProjectConfigMap());
    }

    /**
     * 执行生成类
     *
     * @param projectName
     * @param tableList
     * @param genClassList
     */
    public static void doGeneratorClass(String projectName, List<String> tableList, List<String> genClassList, Map<String, Map<String, String>> tableFieldMap, Map<String, String> otherConfigMap) {
        initMap.put(initKeyGenDate, dateMap.getOrDefault(tableList.get(0), DateUtil.format(DateUtil.date(), GenConfigConstant.COMMENT_DATE_FORMAT)));
        Map<String, String> projectConfigMap = GeneratorProject.getConfig(projectName);
        for (String tableName : tableList) {
            Map<String, String> propertiesMap = new HashMap<>();
            propertiesMap.putAll(projectConfigMap);
            propertiesMap.putAll(getTableProperties(tableName, otherConfigMap));
            if (otherConfigMap != null) {
                propertiesMap.putAll(otherConfigMap);
            }
            if (Objects.equals(propertiesMap.get("copyEntity"), copyEntity)) {
                copyEntity(propertiesMap);
            }
            if (tableFieldMap != null) {
                propertiesMap.putAll(tableFieldMap.get(tableName));
            }
            List<String> genList = processRelationTable(genClassList, propertiesMap);
            GenFunctionUtils.processDeleteGenType(genList, otherConfigMap);
            checkEntityFile(propertiesMap, projectName);
            GeneratorTable generator = new GeneratorTable(projectName, propertiesMap);
            Map<String, String> configMap = GeneratorTableUtils.getConfig(propertiesMap, generator);
            GeneratorTableUtils.initParam(configMap, initMap);
            for (String genType : genList) {
                GeneratorUtils.processByTemplate(configMap, GenTypeEnum.toEnum(genType));
            }
        }
    }

    /**
     * 1.检查生成项目的对应entity文件,不存在则提示生成
     * 2.检查本项目对应entity文件是否存在，不存在同步，并提示重启项目
     *
     * @param configMap   {@link Map<String, String>}
     * @param projectName {@link String}
     */
    private static void checkEntityFile(Map<String, String> configMap, String projectName) {
        String supportRequirement = configMap.get("${supportRequirement}");
        if (!Objects.equals(SupportRequirementEnum.GEN_CODE.getValue(), supportRequirement)) {
            return;
        }
        GeneratorTable currentGenerator = new GeneratorTable(PlatInfoEnum.KNOWLEDGE.getProjectName(), configMap);
        /* 检查生成项目的对应entity文件, 初始化abstractPathMap */
        GeneratorTable generator = new GeneratorTable(projectName, configMap);
        String entityPath = GeneratorUtils.getResultPath(GenTypeEnum.ENTITY, GeneratorProject.abstractPathMap, configMap);
        File file = new File(entityPath);
        if (!file.exists()) {
            throw new RuntimeException("缺少entity文件，需手动生成");
        }
        /* 检查本项目对应entity文件是否存在 */
        String currentEntityPath = entityPath.replace(generator.serviceBasePath, currentGenerator.serviceBasePath);
        File currentFile = new File(currentEntityPath);
        if (!currentFile.exists()) {
            FileUtil.copy(file.toPath(), currentFile.toPath());
            if (!file.exists()) {
                throw new RuntimeException("entity文件已同步，需重启项目");
            }
        }
    }

    /**
     * 复制entity到本项目对应entity文件夹
     *
     * @param configMap {@link Map<String,String>}
     */
    private static void copyEntity(Map<String, String> configMap) {
        GeneratorProject generatorProject = new GeneratorProject(configMap);
        configMap.putAll(generatorProject.getProjectConfigMap());
        /* 同步entity */
        String classPath = GenConfigConstant.GEN_PROJECT_SERVICE_PATH + GeneratorUtils.commonPath + GeneratorUtils.getClassPath(configMap).replace(".", "/") + ".java";
        String resultPath = GeneratorUtils.getResultPath(GenTypeEnum.ENTITY, generatorProject.abstractPathMap, configMap);
        if (FileUtil.exist(classPath) && !FileUtil.exist(resultPath)) {
            String source = FileUtil.readUtf8String(classPath);
            FileUtil.writeUtf8String(source, resultPath);
            log.info("copyEntity succeed,entityName:{}", configMap.get("${entityName}"));
        }
        /* 同步xml */
        String xmlPath = GenConfigConstant.GEN_PROJECT_SERVICE_PATH + GeneratorUtils.resourcePath + "/" +
                generatorProject.packageBasePath + "/dao/mapper/" + configMap.get("${entityName}") + "Mapper.xml";
        String xmlResultPath = GeneratorUtils.getResultPath(GenTypeEnum.MAPPER_XML, generatorProject.abstractPathMap, configMap);
        if (FileUtil.exist(xmlPath) && !FileUtil.exist(xmlResultPath)) {
            String source = FileUtil.readUtf8String(xmlPath);
            FileUtil.writeUtf8String(source, xmlResultPath);
            log.info("copyEntity succeed,entityName:{}", configMap.get("${entityName}"));
        }
    }

    /**
     * 处理关系表
     *
     * @param genClassList  {@link List}
     * @param propertiesMap {@link Map}
     */
    public static List<String> processRelationTable(List<String> genClassList, Map<String, String> propertiesMap) {
        List<String> genList = ListUtils.copy(genClassList, String.class);
        if (GeneratorUtils.relationType3Slave(propertiesMap)) {
            if ((genList.contains(GenTypeEnum.PARAM.getValue())) && !genList.contains(GenTypeEnum.VO.getValue())) {
                genList.add(GenTypeEnum.VO.getValue());
            }
            for (String unNeedGenType : relationTableUnNeedGenTypes) {
                genList.remove(unNeedGenType);
            }
        } else {
            if (genList.contains(GenTypeEnum.VO.getValue())) {
                genList.remove(GenTypeEnum.VO.getValue());
            }
        }
        return genList;
    }

    /**
     * 获取表配置信息
     *
     * @param tableName {@link String}
     * @return
     */
    public static Map<String, String> getTableProperties(String tableName, Map<String, String> configMap) {
        GeneratorConfig generatorConfig = new GeneratorConfig();
        if (configMap != null && configMap.containsKey("genTableConfig")) {
            return generatorConfig.getGenTableColumn(configMap);
        }
        try {
            Method method = generatorConfig.getClass().getDeclaredMethod("get" + StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
            return (Map<String, String>) method.invoke(generatorConfig);
        } catch (Exception e) {
            log.warn("GeneratorMain getTableProperties param is :{}, no table config", tableName);
        }
        return new HashMap<>();
    }
}