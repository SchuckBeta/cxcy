/**
 * .
 */

package com.oseasy.pie.modules.impdata.enums;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 机构类型.
 * @author chenhao
 *
 */
public enum OffiType {
    OT_1("1", true, "公司"),
    OT_2("2", true, "部门"),
    OT_3("3", true, "小组"),
    OT_4("4", true, "学院"),
    OT_5("5", true, "专业"),
    ;

    private String id;//标识
    private Boolean enable;//显示
    private String name;//名称

    private OffiType(String id, Boolean enable, String name) {
      this.id = id;
      this.name = name;
      this.enable = enable;
    }

    /**
     * 根据id获取OfficeType .
     * @author chenhao
     * @param id id惟一标识
     * @return OfficeType
     */
    public static OffiType getById(String id) {
      OffiType[] entitys = OffiType.values();
      for (OffiType entity : entitys) {
        if ((id).equals(entity.getId())) {
          return entity;
        }
      }
      return null;
    }

    /**
     * 获取OfficeType.
     * @return List
     */
    public static List<OffiType> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<OffiType> enty = Lists.newArrayList();
        OffiType[] entitys = OffiType.values();
        for (OffiType entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<OffiType> getAll() {
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
