package com.oseasy.pie.modules.iep.vo;

import java.util.List;

/**
 * 模板步骤.
 * @author chenhao
 *
 */
public enum TplStep {
  TS1(1, "第1步"),
  TS2(2, "第2步"),
  TS3(3, "第3步"),
  TS4(4, "第4步");
    public static final String TPL_STEP = "tplStep";
    public static final String TPL_STEPS = "tplSteps";

  private Integer key;
  private String name;

  private TplStep(Integer key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplStep
   */
  public static TplStep getByKey(Integer key) {
    if ((key != null)) {
      TplStep[] entitys = TplStep.values();
      for (TplStep entity : entitys) {
        if ((key == entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

  /**
   * 检查是否存在 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplStep
   */
  public static Boolean checkHas(List<TplStep> fstypes, Integer key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return TplStep
   */
  public static TplStep checkRet(List<TplStep> fstypes, Integer key) {
      for (TplStep fstype : fstypes) {
          if ((fstype.getKey() == key)) {
              return fstype;
          }
      }
      return null;
  }

  public Integer getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
  }
}
