package com.xinxian.generator.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GenConfigConstant
 * @Description 生成配置常量
 * @Author lmy
 * @Date 2023/10/3 09:38
 */
public class GenConfigConstant {
    public static final String CODE_BASE_PATH = "D:/lmy/git/open/";
    public static final String GEN_PROJECT_BASE_PATH = CODE_BASE_PATH + "xinxianOpen";
    public static final String GEN_PROJECT_NAME = "xinxian-user-passport";
    public static final String GEN_PROJECT_PATH = GEN_PROJECT_BASE_PATH;
    public static final String BACKEND_PROJECT_BASE_PATH = CODE_BASE_PATH;
    public static final String FRONT_PROJECT_BASE_PATH = CODE_BASE_PATH;
    public static final String GEN_MODULE_BASE_NAME = "xinxian-user-passport";
    public static final String PROJECT_PREFIX = "xin";
    public static final String AUTHOR = "lmy";
    public static final String COMMENT_DATE_FORMAT = "yyyy/MM/dd HH:mm";
    public static final String GEN_PACKAGE_NAME = "com.xinxian.generator";
    public static final String GEN_PROJECT_NAME_SERVICE = GEN_MODULE_BASE_NAME + "-service";
    public static final String GEN_PROJECT_SERVICE_PATH = GEN_PROJECT_PATH + "/";
    public static final String GEN_PATH = GEN_PROJECT_SERVICE_PATH + "/src/main/java/com/xinxian/generator";
    public static final String GEN_RESOURCE_PATH = GEN_PROJECT_SERVICE_PATH + "/src/main/resources";
    public static final String GEN_TEMPLATE_BASE_PATH = GEN_RESOURCE_PATH + "/generator_template";
    public static final String GEN_ENUMS_PATH = GEN_PATH + "/enums";
    public static final String GEN_UTILS_PATH = GEN_PATH + "/utils";

}
