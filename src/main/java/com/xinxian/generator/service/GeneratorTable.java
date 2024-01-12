package com.xinxian.generator.service;

import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.framework.utils.GeneratorTableUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GeneratorGetData
 * @Description 自动生成优化
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
public class GeneratorTable extends GeneratorProject {

    /* 配置项 */
    public String tableName = "tgd_template_info";
    public String entityName = "TgdTemplateInfo";
    public String slaveEntityName = "TgdTemplateFieldInfo";
    public String entityDesc = "模板";
    public String bizClassName = "TgdTemplateManage";
    public String requestMapping = "/getdata/inner/v1/templateManage";
    /* 选填 */
    public String controllerType = "";

    public Map<String, String> tableConfigMap = null;

    public GeneratorTable() {
    }

    public GeneratorTable(Map<String, String> configMap) {
        super(configMap);
        setProperties(configMap);
    }

    public GeneratorTable(String projectName, Map<String, String> configMap) {
        super(projectName);
        setProperties(configMap);
    }

    private void setProperties(Map<String, String> configMap) {
        this.tableName = configMap.get("${tableName}");
        this.entityName = configMap.get("${entityName}");
        this.entityDesc = configMap.get("${entityDesc}");
        this.bizClassName = configMap.get("${bizClassName}");
        this.requestMapping = configMap.get("${requestMapping}");
        this.slaveEntityName = configMap.get("${slaveEntityName}");
        if (tableName == null) {
            return;
        }
        this.tableConfigMap = new HashMap() {
            {
                for (Entry<String, String> entry : configMap.entrySet()) {
                    if (entry.getKey().startsWith("${")) {
                        put(entry.getKey(), entry.getValue());
                    }
                }
                Map<String, String> projectMap = GeneratorProject.abstractPathMap;
                put("${shortEntityName}", StrUtil.lowerFirst(entityName));
                put("${entityNameUpper}", StrUtil.toUnderlineCase(entityName).toUpperCase());
                put("${entityEnumName}", entityName.replace("Info", "").replace("Table", ""));
                put("${shortClassName}", StrUtil.lowerFirst(bizClassName));
                put("${entityBasePath}", projectMap.get(GenTypeEnum.ENTITY.getValue()));
                put("${entityPath}", packageBase + ".bean.po." + entityName);
                put("${namespace}", packageBase + ".core.mapper." + entityName + "Mapper");
                put("${entityInputPath}", GeneratorTableUtils.getEntityInputPath(entityInputPath, entityName));
                put("${mapperXmlInputPath}", GeneratorTableUtils.getMapperXmlInputPath(entityName, abstractPathMap));
                /* front config */
                put("${controllerPath}", requestMapping.substring(requestMapping.substring(1).indexOf("/") + 2));
                put("${controllerEndPath}", requestMapping.substring(requestMapping.lastIndexOf("/") + 1));
                put("${upperControllerEndPath}", StrUtil.upperFirst(String.valueOf(get("${controllerEndPath}"))));
                put("${controllerType}", controllerType);
                /* configMap */
                put("${mainEntityName}", StrUtil.upperFirst(StrUtil.toCamelCase(configMap.get("${mainTable}"), '_')));
                put("${shortMainEntityName}", StrUtil.toCamelCase(configMap.get("${mainTable}"), '_'));
                put("${slaveEntityName}", slaveEntityName);
                put("${shortSlaveEntityName}", StrUtil.lowerFirst(slaveEntityName));
            }
        };
    }
}