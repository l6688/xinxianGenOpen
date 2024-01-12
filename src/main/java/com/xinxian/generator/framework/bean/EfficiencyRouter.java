package com.xinxian.generator.framework.bean;

import lombok.Data;

import java.util.List;

/**
 * @ClassName EfficiencyRouter
 * @Description 效率提升生成路由sql实例
 * @Author lmy
 * @Date 2023/2/2 21:47
 */
@Data
public class EfficiencyRouter {
    private String path;
    private String component;
    private Boolean hidden;
    private Boolean alwaysShow;
    private Meta meta;
    private String name;
    private String menuType;
    private String permissions;
    private Boolean hasPermission;
    private String redirect;
    private Boolean rolePermission;
    private List<EfficiencyRouter> children;
    private Long id;
    private String operationPermission;
    private String parentLevel;


    @Data
    public class Meta {
        private String icon;
        private Boolean noCache;
        private String title;
        private String activeMenu;
        private String link;
    }
}
