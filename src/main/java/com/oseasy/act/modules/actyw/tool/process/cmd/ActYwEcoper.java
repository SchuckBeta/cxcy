/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.cmd;

/**
 * .
 * @author chenhao
 *
 */
public enum ActYwEcoper {
    ECR_ADD("add","新增"),
    ECR_UPDATE("update","修改"),
    ECR_DEL("del","删除"),
    ECR_OTR("other","其它"),
    ;

    private String key;
    private String remark;

    private ActYwEcoper(String key, String remark) {
      this.key = key;
      this.remark = remark;
    }

    /**
     * 根据关键字获取枚举 .
     * @author chenhao
     * @param key 关键字
     * @return ActYwEcoper
     */
    public static ActYwEcoper getByKey(String key) {
      ActYwEcoper[] entitys = ActYwEcoper.values();
      for (ActYwEcoper entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
      return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
