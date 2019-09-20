/**
 * .
 */

package com.oseasy.pie.modules.impdata.enums;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 机构等级.
 * @author chenhao
 *
 */
public enum OffiGrade {
    OG_1("1", true, "一级"),
    OG_2("2", true, "二级"),
    OG_3("3", true, "三级"),
    OG_4("4", true, "四级");

    private String id;//标识
    private Boolean enable;//显示
    private String name;//名称

    private OffiGrade(String id, Boolean enable, String name) {
      this.id = id;
      this.name = name;
      this.enable = enable;
    }

    /**
     * 根据id获取OfficeGrade .
     * @author chenhao
     * @param id id惟一标识
     * @return OfficeGrade
     */
    public static OffiGrade getById(String id) {
      OffiGrade[] entitys = OffiGrade.values();
      for (OffiGrade entity : entitys) {
        if ((id).equals(entity.getId())) {
          return entity;
        }
      }
      return null;
    }

    /**
     * 获取OfficeGrade.
     * @return List
     */
    public static List<OffiGrade> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<OffiGrade> enty = Lists.newArrayList();
        OffiGrade[] entitys = OffiGrade.values();
        for (OffiGrade entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<OffiGrade> getAll() {
        return getAll(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    @Override
    public String toString() {
        if(this != null){
            return "{\"id\":\"" + this.id + "\",\"name\":\"" + this.name + "\"}";
        }
        return super.toString();
    }
}
