/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 节点审核查看类型.
 * @author chenhao
 *
 */
public enum AmapVstatus {
    EDIT_VIEW("10", true, "审核查看"),
    EDIT("20", true, "审核"),
    VIEW("30", true, "查看");

    private String id;//标识
    private Boolean enable;//显示
    private String name;//名称

    private AmapVstatus(String id, Boolean enable, String name) {
      this.id = id;
      this.name = name;
      this.enable = enable;
    }

    /**
     * 根据id获取AmapStatus .
     * @author chenhao
     * @param id id惟一标识
     * @return AmapStatus
     */
    public static AmapVstatus getById(String id) {
      AmapVstatus[] entitys = AmapVstatus.values();
      for (AmapVstatus entity : entitys) {
        if ((id).equals(entity.getId())) {
          return entity;
        }
      }
      return null;
    }

    /**
     * 获取AmapStatus.
     * @return List
     */
    public static List<AmapVstatus> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<AmapVstatus> enty = Lists.newArrayList();
        AmapVstatus[] entitys = AmapVstatus.values();
        for (AmapVstatus entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<AmapVstatus> getAll() {
        return getAll(true);
    }

    public String getId() {
        return id;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getName() {
        return name;
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
