package com.oseasy.cms.modules.cms.enums;

public enum FrontRouteEnum {
    FRONTBASIC("basic","默认"),
    FRONTR01("R01","模板1路由");

    private String route;//对应文件路径
    private String name;//名称

    FrontRouteEnum(String route, String name) {
        this.route = route;
        this.name = name;
    }

    public static FrontRouteEnum getByRoute(String route) {
        if (route!=null) {
            for(FrontRouteEnum e:FrontRouteEnum.values()) {
                if (e.route.equals(route)) {
                    return e;
                }
            }
        }
        return null;
    }
    public String getRoute() {
        return route;
    }

    public String getName() {
        return name;
    }
}
