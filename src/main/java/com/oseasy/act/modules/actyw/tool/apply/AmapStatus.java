/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 审核节点ACT状态类型.
 * @author chenhao
 *
 */
public enum AmapStatus {
    TODO("10", true, "待办", "待{}审核"),
    END("20", true, "完成", "审核通过"),
    NONE("90", true, "无", "数据异常无显示");

    private String id;//标识
    private Boolean enable;//显示
    private String name;//名称
    private String regx;//显示状态名称

    private AmapStatus(String id, Boolean enable, String name, String regx) {
      this.id = id;
      this.name = name;
      this.enable = enable;
      this.regx = regx;
    }

    /**
     * 根据id获取AmapStatus .
     * @author chenhao
     * @param id id惟一标识
     * @return AmapStatus
     */
    public static AmapStatus getById(String id) {
      AmapStatus[] entitys = AmapStatus.values();
      for (AmapStatus entity : entitys) {
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
    public static List<AmapStatus> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<AmapStatus> enty = Lists.newArrayList();
        AmapStatus[] entitys = AmapStatus.values();
        for (AmapStatus entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<AmapStatus> getAll() {
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

    public String getRegx() {
        return regx;
    }

    @SuppressWarnings("unused")
    @Override
    public String toString() {
        if(this != null){
            return "{\"id\":\"" + this.id + "\",\"name\":\"" + this.name + "\",\"regx\":\"" + this.regx + "\"}";
        }
        return super.toString();
    }
}
