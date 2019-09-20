package com.oseasy.scr.modules.sco.vo;

/**
 * 学分类型枚举.
 * @author chenhao
 */
public enum ScoType {
  ST_INNOVATE(1, "创业学分"),
  ST_BUSINESS(2, "创新学分"),
  ST_COURSE(3, "课程学分"),
  ST_CREDIT(4, "素质学分"),
  ST_SKILL(5, "技能学分");

  private Integer key;
  private String remarks;

  private ScoType(Integer key, String remarks) {
    this.key = key;
    this.remarks = remarks;
  }

  /**
   * 根据关键字获取枚举 .
   *
   * @author chenhao
   * @param key
   *          关键字
   * @return ScoType
   */
  public static ScoType getByKey(Integer key) {
    ScoType[] entitys = ScoType.values();
    for (ScoType entity : entitys) {
      if ((key).equals(entity.getKey())) {
        return entity;
      }
    }
    return null;
  }

  public Integer getKey() {
    return key;
  }
  public void setKey(Integer key) {
    this.key = key;
  }
  public String getRemarks() {
    return remarks;
  }
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}
