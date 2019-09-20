/**
 * 正则类型.
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 正则类型：1、eq;2、区间.
 * @author chenhao
 *
 */
public enum RegType {
    RT_EQ("1", true, "审核"),
    RT_GE("2", true, "评分"),
    RT_OT("999", true, "其它");

    private String id;//标识
    private Boolean enable;//显示
    private String name;//名称

    private RegType(String id, Boolean enable, String name) {
      this.id = id;
      this.name = name;
      this.enable = enable;
    }

    /**
     * 根据id获取RegType .
     * @author chenhao
     * @param id id惟一标识
     * @return RegType
     */
    public static RegType getById(String id) {
      RegType[] entitys = RegType.values();
      for (RegType entity : entitys) {
        if ((id).equals(entity.getId())) {
          return entity;
        }
      }
      return null;
    }

    /**
     * 获取RegType.
     * @return List
     */
    public static List<RegType> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<RegType> enty = Lists.newArrayList();
        RegType[] entitys = RegType.values();
        for (RegType entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<RegType> getAll() {
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
