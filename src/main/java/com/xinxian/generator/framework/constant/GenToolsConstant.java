package com.xinxian.generator.framework.constant;

/**
 * @ClassName GenSmallToolsConstant
 * @Description gen小工具常量
 * @Author lmy
 * @Date 2023/2/11 22:37
 */
public class GenToolsConstant {

    public static final String KEY_FUNCTION = "${function}";
    public static final String KEY_METHOD = "${method}";
    public static final String KEY_UPPER_FIRST_FUNCTION = "${upperFirstFunction}";
    public static final String KEY_UPPER_FIRST_METHOD = "${upperFirstMethod}";
    public static final String KEY_RESOURCE_CODE = "${resourceCode}";
    public static final String TEMPLATE_UTILS_CLASS = "\nGenerator${upperFirstGenType}Utils:\n";
    public static final String TEMPLATE_TEMPLATE_CLASS = "\n${upperFirstGenType}Template:\n";
    public static final String TEMPLATE_PROPERTY = "    private static final String ${function}${upperFirstMethod}Template = \"${resourceCode}\";\n";
    public static final String TEMPLATE_PROPERTY_KEY = "    public static final String key${upperFirstFunction}${upperFirstMethod} = \"${${function}${upperFirstMethod}}\";\n";
    public static final String TEMPLATE_REPLACE_KEY = "${${function}${upperFirstMethod}}\n";
    public static final String TEMPLATE_PUT_MAP_RELATION = "            put(GeneratorUtils.relationType2 + GenConstant.key${upperFirstFunction}${upperFirstMethod}, ${function}${upperFirstMethod}Template);\n" +
            "            put(GeneratorUtils.relationType3 + GenConstant.key${upperFirstFunction}${upperFirstMethod}, ${function}${upperFirstMethod}Template);\n";
    public static final String TEMPLATE_REMOVE_REPLACE_KEY = "        not${upperFirstFunction}List.add(GenConstant.key${upperFirstFunction}${upperFirstMethod} + GenConstant.ENTER);\n";

    public static final String TEMPLATE_ONE_TO_MANY_INPUT = "            List<Gen${upperFirstFunction}> ${function}List = GeneratorUtils.getListFromConfig(configMap, GenConstant.key${upperFirstFunction}List, Gen${upperFirstFunction}.class);\n" +
            "            LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.converterToEntityProperty(${function}List);\n";
    public static final String TEMPLATE_ONE_TO_MANY_INPUT_RELATION = "            List<Gen${upperFirstFunction}> ${function}List = GeneratorUtils.getListFromConfig(configMap, GenConstant.key${upperFirstFunction}List, Gen${upperFirstFunction}.class);\n" +
            "            LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.relationConverterToEntityProperty(${function}List);\n";
    public static final String TEMPLATE_ONE_TO_MANY_INPUT_UNIQUE = "            List<Gen${upperFirstFunction}> ${function}List = GeneratorUtils.getListFromConfig(configMap, GenConstant.key${upperFirstFunction}List, Gen${upperFirstFunction}.class);\n" +
            "            LinkedHashMap<String, EntityProperty> propertyMap = GeneratorUtils.uniqueFieldConverterToProperty(${function}List);\n";
    public static final String TEMPLATE_ONE_TO_MANY_PROCESS = "            /* process${upperFirstMethod} */\n" +
            "            text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.key${upperFirstFunction}${upperFirstMethod}, null, configMap, ${function}${upperFirstMethod}Template);\n";
    public static final String TEMPLATE_ONE_TO_MANY_PROCESS_RELATION = "            /* process${upperFirstMethod} */\n" +
            "            text = GeneratorUtils.processCommonItem(text, propertyMap, GenConstant.key${upperFirstFunction}${upperFirstMethod}, null, configMap, null);\n";
    public static final String TEMPLATE_ONE_TO_MANY_PROCESS_UNIQUE = "            /* process${upperFirstMethod} */\n" +
            "            ${method}.append(processCheckUniqueContent(GeneratorUtils.processTemplate(${function}${upperFirstMethod}Template, configMap), configMap));\n\n";
    public static final String TEMPLATE_BUILDER = "            StringBuilder ${method} = new StringBuilder();\n";
    public static final String TEMPLATE_ONE_TO_MANY_REPLACE_UNIQUE = "            text = text.replace(GenConstant.key${upperFirstFunction}${upperFirstMethod}, ${method}.toString());";


}
