package com.xinxian.generator.framework.constant;

import com.xinxian.generator.framework.enums.FrontComponentTypeEnum;
import com.xinxian.generator.framework.enums.FrontElementTypeEnum;
import com.xinxian.generator.framework.enums.FrontSubElementTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GenFrontConstant
 * @Description 前端常量类
 * @Author lmy
 * @Date 2023/4/9 15:42
 */
public class GenFrontConstant {

    /**
     * Component
     */
    /* 前端组建类型和创建模板map */
    public static Map<String, String> createTemplateMap = new HashMap() {
        {
            put(FrontComponentTypeEnum.INPUT + FrontElementTypeEnum.FORM_ITEM.getKey(),
                    "                <el-form-item label=\"{{desc}}\" prop=\"{{property}}\" ${expansionHeader}>\n" +
                            "                    <el-input v-model=\"queryForm.{{property}}\" placeholder=\"{{desc}}\" clearable ${expansionSubHeader}></el-input>\n" +
                            "                </el-form-item>");
            put(FrontComponentTypeEnum.SELECT + FrontElementTypeEnum.FORM_ITEM.getKey(),
                    "                <el-form-item label=\"{{desc}}\" prop=\"{{property}}\" ${expansionHeader}>\n" +
                            "                    <el-select v-model=\"queryForm.{{property}}\" placeholder=\"请选择{{desc}}\" ${expansionSubHeader}>\n" +
                            "${expansionContent}" +
                            "                    </el-select>\n" +
                            "                </el-form-item>");
            put(FrontComponentTypeEnum.TIME + FrontElementTypeEnum.FORM_ITEM.getKey(),
                    "                <el-form-item label=\"{{desc}}\" prop=\"{{property}}\" ${expansionHeader}>\n" +
                            "                    <el-date-picker v-model=\"queryForm.{{property}}\" type=\"date\" placeholder=\"{{desc}}\" ${expansionSubHeader}/>\n" +
                            "                </el-form-item>");
        }
    };

    /* 前端组建类型和创建内容模板map */
    public static Map<String, String> createContentTemplateMap = new HashMap() {
        {
            put(FrontComponentTypeEnum.SELECT, "                        <el-option label=\"{{itemLabel}}\" value=\"{{itemValue}}\"/>\n");
        }
    };

    /* 前端组建类型和创建内容模板map */
    public static Map<String, String> indexContentTemplateMap = new HashMap() {
        {
            put(FrontComponentTypeEnum.SELECT,
                    "    case \"{{itemValue}}\":\n" +
                            "        return \"{{itemLabel}}\";\n");
        }
    };

    /* 前端组建类型和索引模板map */
    public static Map<String, String> indexTemplateMap = new HashMap() {
        {
            put(FrontComponentTypeEnum.INPUT + FrontElementTypeEnum.FORM_ITEM.getKey(),
                    "                <el-form-item label=\"{{desc}}\" prop=\"{{property}}\" ${expansionHeader}>\n" +
                            "                    <el-input v-model.trim=\"filterForm.{{property}}\" placeholder=\"{{desc}}\" clearable ${expansionSubHeader}></el-input>\n" +
                            "                </el-form-item>");
            put(FrontComponentTypeEnum.INPUT + FrontElementTypeEnum.TABLE_COLUMN.getKey(), "        <el-table-column label=\"{{desc}}\" align=\"center\" prop=\"{{property}}\" ${expansionHeader}/>");
            put(FrontComponentTypeEnum.TIME + FrontElementTypeEnum.TABLE_COLUMN.getKey(), "        <el-table-column label=\"{{desc}}\" align=\"center\" prop=\"{{property}}\" :formatter=\"timeFormatter({{property}})\" ${expansionHeader}/>");
            put(FrontComponentTypeEnum.SELECT + FrontElementTypeEnum.TABLE_COLUMN.getKey(),
                    "                <el-table-column label=\"{{desc}}\" align=\"center\" prop=\"{{property}}\" ${expansionHeader}>\n" +
                            "                    <template #default=\"scoped\">\n" +
                            "                        {{ onFormat{{upperFirstProperty}}(scoped.row.{{property}}) }}\n" +
                            "                    </template>\n" +
                            "                </el-table-column>");
            put(FrontComponentTypeEnum.SELECT + FrontElementTypeEnum.FORMAT_METHOD.getKey(),
                    "/**\n" +
                            " * 格式化{{desc}}\n" +
                            " * @param {*} param\n" +
                            " */\n" +
                            "function onFormat{{upperFirstProperty}}(param) {\n" +
                            "    console.log(param);\n" +
                            "    switch (param) {\n" +
                            "${expansionContent}" +
                            "    }\n" +
                            "}\n");
        }
    };

    /* 其他补充信息 */
    public static Map<String, String> otherInfoMap = new HashMap() {
        {
            put(FrontComponentTypeEnum.INPUT + FrontElementTypeEnum.FORM_ITEM.name() + FrontSubElementTypeEnum.DISABLED.name(), " disabled ");
            put(FrontComponentTypeEnum.INPUT + FrontElementTypeEnum.FORM_ITEM.name() + FrontSubElementTypeEnum.DISABLED_CONDITION.name(), " :disabled=\"queryForm.{{property}} !== 1\" ");
            put(FrontComponentTypeEnum.INPUT + FrontElementTypeEnum.FORM_ITEM.name() + FrontSubElementTypeEnum.LABEL_WIDTH.name(), " label-width=\"{{frontWidth}}px\" ");
        }
    };
}
