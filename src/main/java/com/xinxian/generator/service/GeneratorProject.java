package com.xinxian.generator.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xinxian.generator.constant.GenConfigConstant;
import com.xinxian.generator.enums.GenTypeEnum;
import com.xinxian.generator.enums.PlatInfoEnum;
import com.xinxian.generator.framework.utils.GeneratorUtils;
import com.xinxian.generator.framework.utils.MapUtils;

import java.util.*;

/**
 * @ClassName GeneratorGetData
 * @Description 自动生成优化
 * @Author lmy
 * @Date 2023/1/13 23:05
 */
public class GeneratorProject {

    /* 配置项 */
    public PlatInfoEnum platform = PlatInfoEnum.KNOWLEDGE;
    public PlatInfoEnum userPlatform = PlatInfoEnum.KNOWLEDGE;
    /* 非配置项 */
    public static final String githubPath = GenConfigConstant.CODE_BASE_PATH;
    public static final String newprojectPath = githubPath + "new/";
    public static String codeBasePath = GenConfigConstant.BACKEND_PROJECT_BASE_PATH;
    public static String baseExecProjectPath = codeBasePath + "/xinxian/src/main/java/";
    public static List<String> specialFrontApi = Arrays.asList(PlatInfoEnum.KNOWLEDGE.getProjectName());
    public static Map<String, String> abstractPathMap = null;
    public static final Map<String, String> errorCodeEnumMap = new HashMap() {
        {
            put(PlatInfoEnum.KNOWLEDGE.getProjectName(), "EnumResultMsg");
        }
    };
    public static Map<String, HashMap<String, String>> projectMap = new HashMap() {
        {
            for (PlatInfoEnum platInfoEnum : PlatInfoEnum.values()) {
                HashMap platformMap = new HashMap();
                platformMap.put("projectName", platInfoEnum.getProjectName());
                platformMap.put("projectShortName", platInfoEnum.getName());
                platformMap.put("projectPath", codeBasePath + platInfoEnum.getProjectName() + "/");
                platformMap.put("frontProjectName", platInfoEnum.getFrontProjectName());
                platformMap.put("apiBase", platInfoEnum.getApiBase());
                platformMap.put("database", platInfoEnum.getDatabase());
                platformMap.put("projectDesc", platInfoEnum.getDesc());
                platformMap.put("application", StrUtil.upperFirst(StrUtil.toCamelCase(platInfoEnum.getProjectName(), '-')));
                put(platInfoEnum.getProjectName(), platformMap);
            }
        }
    };
    public static final List<String> genTypes = new ArrayList() {
        {
            for (GenTypeEnum genTypeEnum : GenTypeEnum.values()) {
                add(genTypeEnum.getValue());
            }
        }
    };
    public String projectName = platform.getProjectName();
    public String frontProjectName = platform.getFrontProjectName();
    public String apiBase = platform.getApiBase();
    public String database = platform.getDatabase();
    /* 项目名称 */
    public String projectPath = codeBasePath + projectName + "/";
    public String dtoName = "bean";
    public String restName = "web";
    public String facadeName = projectName + "-facade";
    public String serviceName = "core";

    public String entityPathName = "bean";
    public String jobName = projectName + "-job";
    public String packageBase = "com.jinke.kb";
    public String userPackageBase = "com.jinke.kb";
    /* 模块路径 */
    public String packageBasePath = packageBase.replace(".", "/");
    public String serviceBasePath = projectPath + serviceName + GeneratorUtils.commonPath;
    public String frontPath = GenConfigConstant.FRONT_PROJECT_BASE_PATH + frontProjectName;
    public String servicePath = serviceBasePath + packageBasePath;
    public String dtoPath = projectPath + dtoName + GeneratorUtils.commonPath + packageBasePath;
    public String restPath = projectPath + restName + GeneratorUtils.commonPath + packageBasePath;
    public String facadePath = projectPath + facadeName + GeneratorUtils.commonPath + packageBasePath;
    public String jobPath = projectPath + jobName + GeneratorUtils.commonPath + packageBasePath + "/job/task/";
    public String restTestPath = projectPath + restName + GeneratorUtils.commonTestPath + packageBasePath;

    /* 输入类路径 */
    public String entityInputPath = servicePath + "/dao/entity/";
    public String resourcesPath = String.format("%s/src/main/resources", projectPath + serviceName);
    public String mapperXmlInputPath = String.format("%s/mapper/", resourcesPath);

    /* 输出类路径 */
    public String entityAbstractPath = entityInputPath;
    public String mapperXmlAbstractPath = mapperXmlInputPath;
    public String generatorConfigAbstractPath = String.format("%s/%s/dao/generator/", resourcesPath, packageBasePath);
    public String mapperAbstractPath = servicePath + "/core/mapper/";
    public String controllerPath = restPath + "/rest/controller/";
    public String controllerAbstractPath = controllerPath;
    public String restTestAbstractPath = restTestPath + "/test/";
    public String facadeControllerAbstractPath = controllerPath + "feign/";
    public String facadeClientPath = facadePath + "/facade/inner/";
    public String facadeFallbackPath = facadePath + "/facade/fallback/";
    public String paramAbstractPath = dtoPath + "/bean/view/";
    public String resultAbstractPath = dtoPath + "/dto/results/";
    public String enumAbstractPath = dtoPath + "/bean/enums";
    public String voAbstractPath = dtoPath + "/dto/vo/";
    public String bizAbstractPath = servicePath + "/biz/";
    public String bizImplAbstractPath = servicePath + "/biz/impl/";
    public String serviceAbstractPath = servicePath + "/core/service/";
    public String serviceImplAbstractPath = servicePath + "/core/service/imp/";
    public String converterAbstractPath = servicePath + "/core/converter/";
    public String frontApiAbstractPath = frontPath + GeneratorUtils.frontApiPath;
    public String frontIndexAbstractPath = frontPath + GeneratorUtils.frontViewsPath;
    public String frontCreateAbstractPath = frontPath + GeneratorUtils.frontViewsPath;
    public String frontRouterAbstractPath = frontPath + GeneratorUtils.frontRouterPath;
    public String sqlAbstractPath = resourcesPath + "/sql/";
    public Map<String, String> projectConfigMap = new HashMap<>();

    public GeneratorProject() {
        new GeneratorProject(PlatInfoEnum.KNOWLEDGE.getProjectName());
    }

    public GeneratorProject(PlatInfoEnum platform) {
        new GeneratorProject(platform.getProjectName());
    }

    public GeneratorProject(Map<String, String> configMap) {
        this.projectName = configMap.get("projectName");
        this.frontProjectName = configMap.get("frontProjectName");
        this.apiBase = configMap.get("apiBase");
        this.database = configMap.get("database");
        setProperties();
    }

    public GeneratorProject(String projectName) {
        HashMap<String, String> projectConfigMap = projectMap.get(projectName);
        this.projectName = projectConfigMap.get("projectName");
        this.frontProjectName = projectConfigMap.get("frontProjectName");
        this.apiBase = projectConfigMap.get("apiBase");
        this.database = projectConfigMap.get("database");
        setProperties();
    }

    /* 增加类型时setProperties地址也要更新 */
    public void setProperties() {
        /* 自定义 */
        if (projectName.startsWith(GenConfigConstant.PROJECT_PREFIX)) {
            codeBasePath = GenConfigConstant.BACKEND_PROJECT_BASE_PATH;
        } else if (!codeBasePath.contains(githubPath)) {
            codeBasePath = githubPath;
        }
        /* 通用 */
        this.projectPath = codeBasePath + projectName + "/";
        this.dtoName = "bean";
        this.restName = "web";
        this.facadeName = projectName + "-facade";
        this.serviceName = "core";
        this.jobName = projectName + "-job";
        this.packageBase = "com.jinke.kb";
        this.userPackageBase = "com.jinke.kb";
        if (!projectName.startsWith(GenConfigConstant.PROJECT_PREFIX)) {
            this.userPackageBase = "com." + userPlatform.getProjectName().replace("-", ".");
        }
        this.packageBasePath = packageBase.replace(".", "/");
        this.serviceBasePath = projectPath + serviceName + GeneratorUtils.commonPath;
        this.servicePath = serviceBasePath + packageBasePath;
        this.dtoPath = projectPath + dtoName + GeneratorUtils.commonPath + packageBasePath;
        this.restPath = projectPath + restName + GeneratorUtils.commonPath + packageBasePath;
        this.facadePath = projectPath + facadeName + GeneratorUtils.commonPath + packageBasePath;
        this.jobPath = projectPath + jobName + GeneratorUtils.commonPath + packageBasePath + "/job/task/";
        this.restTestPath = projectPath + restName + GeneratorUtils.commonTestPath + packageBasePath;

        /* 输入类路径 */
        this.entityInputPath = projectPath + entityPathName + GeneratorUtils.commonPath + packageBasePath + "/bean/po/";
        this.resourcesPath = String.format("%s/src/main/resources", projectPath + serviceName);
        this.mapperXmlInputPath = String.format("%s/mapper/", resourcesPath);

        /* 输出类路径 */
        this.entityAbstractPath = entityInputPath;
        this.mapperXmlAbstractPath = mapperXmlInputPath;
        this.generatorConfigAbstractPath = String.format("%s/dao/generator/", resourcesPath);
        this.mapperAbstractPath = servicePath + "/core/mapper/";
        this.controllerPath = restPath + "/web/controller/";
        this.controllerAbstractPath = controllerPath;
        this.restTestAbstractPath = restTestPath + "/test/";
        this.facadeControllerAbstractPath = controllerPath + "feign/";
        this.facadeClientPath = facadePath + "/facade/inner/";
        this.facadeFallbackPath = facadePath + "/facade/fallback/";
        this.paramAbstractPath = dtoPath + "/bean/view/";
        this.resultAbstractPath = dtoPath + "/bean/results/";
        this.enumAbstractPath = dtoPath + "/bean/enums/";
        this.voAbstractPath = dtoPath + "/bean/view/";
        this.bizAbstractPath = servicePath + "/biz/";
        this.bizImplAbstractPath = servicePath + "/biz/impl/";
        this.serviceAbstractPath = servicePath + "/core/service/";
        this.serviceImplAbstractPath = servicePath + "/core/service/imp/";
        this.converterAbstractPath = servicePath + "/core/converter/";
        if (!projectName.startsWith(GenConfigConstant.PROJECT_PREFIX) && codeBasePath.contains(githubPath)) {
            this.frontPath = codeBasePath + frontProjectName;
        }
        this.frontApiAbstractPath = frontPath + GeneratorUtils.frontApiPath;
        this.frontIndexAbstractPath = frontPath + GeneratorUtils.frontViewsPath;
        this.frontCreateAbstractPath = frontPath + GeneratorUtils.frontViewsPath;
        this.frontRouterAbstractPath = frontPath + GeneratorUtils.frontRouterPath;
        this.sqlAbstractPath = resourcesPath + "/sql/";

        /* GeneratorProject.this.abstractPathMap */
        this.abstractPathMap = new HashMap() {
            {
                put(GenTypeEnum.ENTITY.getValue(), entityAbstractPath);
                put(GenTypeEnum.MAPPER_XML.getValue(), mapperXmlAbstractPath);
                put(GenTypeEnum.MAPPER.getValue(), mapperAbstractPath);
                put(GenTypeEnum.CONTROLLER.getValue(), controllerAbstractPath);
                put(GenTypeEnum.PARAM.getValue(), paramAbstractPath);
                put(GenTypeEnum.VO.getValue(), voAbstractPath);
                put(GenTypeEnum.SERVICE.getValue(), serviceAbstractPath);
                put(GenTypeEnum.SERVICE_IMPL.getValue(), serviceImplAbstractPath);
                put(GenTypeEnum.CONVERTER.getValue(), converterAbstractPath);
                put(GenTypeEnum.ENUM.getValue(), enumAbstractPath);
                put(GenTypeEnum.ADD_FIELD_SQL.getValue(), sqlAbstractPath);
            }
        };


        this.projectConfigMap = new HashMap() {
            {
                put("${date}", DateUtil.format(DateUtil.date(), GenConfigConstant.COMMENT_DATE_FORMAT));
                put("${author}", GenConfigConstant.AUTHOR);
                put("${packageBase}", packageBase);
                put("${packageBasePath}", packageBase.replace(".", "/"));
                put("${projectName}", projectName);
                put("${userPackageBase}", userPackageBase);
                put("${templateBasePath}", GenConfigConstant.GEN_TEMPLATE_BASE_PATH + "/");
                put("${baseParamPath}", String.format("%s/dto/view/BaseParam.java", dtoPath));
                put("${apiBase}", apiBase);
                put("${database}", database);
                put("${shortProjectPath}", apiBase.replace("/", ""));
                put("${errorCodeEnum}", errorCodeEnumMap.get(projectName));
                put("${errorCodeEnumPath}", dtoPath + "/bean/base/" + errorCodeEnumMap.get(projectName) + ".java");
                PlatInfoEnum platInfoEnum = PlatInfoEnum.getInstanceByProjectName(projectName);
                if (platInfoEnum != null) {
                    put("${platId}", String.valueOf(platInfoEnum.getValue()));
                    put("${platIdName}", platInfoEnum.getName());
                    put("${platIdEnum}", platInfoEnum.name());
                    put("${feignClientValue}", StrUtil.toCamelCase(platInfoEnum.getProjectName().substring(platInfoEnum.getProjectName().indexOf("-") + 1), '-'));
                }
            }
        };
    }

    /**
     * 获取项目配置
     *
     * @return
     */
    public Map<String, String> getProjectConfigMap() {
        return MapUtils.defaultNullValue(projectConfigMap);
    }

    /**
     * 获取配置
     *
     * @return
     */
    public static Map<String, String> getConfig(String projectName) {
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : projectMap.get(projectName).entrySet()) {
            if (!entry.getKey().startsWith("${")) {
                map.put(String.format("${%s}", entry.getKey()), entry.getValue());
            }
        }
        map.putAll(projectMap.get(projectName));
        return map;
    }
}