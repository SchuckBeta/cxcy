package com.oseasy.cms.modules.cms.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 栏目模块枚举类
 * Created by liangjie on 2018/9/21.
 */
public enum CategoryModel {
    ARTICLEMODEL("article","0000000275","文章"),
    PROJECTMODEL("project","0000000276","基地/项目/大赛详情展示"),
    HOTMODEL("hot","0000000277","大赛热点"),
    LINKMODEL("link","0000000278","链接")
    ;

    public static String ARTICLEMODELPARAM = "0000000275";
    public static String PROJECTMODELPARAM = "0000000276";
    public static String HOTMODELPARAM = "0000000277";
    public static String LINKMODELPARAM = "0000000278";

    private String modelParam;
    private String modelCode;
    private String modelName;


    CategoryModel(String modelParam,String modelCode,String modelName){
        this.modelParam = modelParam;
        this.modelCode = modelCode;
        this.modelName = modelName;
    }

    public static List<CategoryModel> getCategoryModelList(){
        List<CategoryModel> categoryModelList = new ArrayList();
        for(CategoryModel c : CategoryModel.values()){
            categoryModelList.add(c);
        }
        return  categoryModelList;
    }

    public static CategoryModel getCategoryModel(String modelParam){
        if(modelParam != null){
            for(CategoryModel c : CategoryModel.values()){
                if(c.modelParam.equals(modelParam)){
                    return c;
                }
            }
        }
        return null;
    }


    public String getModelParam() {
        return modelParam;
    }

    public void setModelParam(String modelParam) {
        this.modelParam = modelParam;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
