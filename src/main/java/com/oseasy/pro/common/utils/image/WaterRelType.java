package com.oseasy.pro.common.utils.image;

import java.util.Arrays;
import java.util.List;

/**
 *
 * 水印资源种类.
 * @author chenhao
 *
 */
public enum WaterRelType {
  WRT_Z("1", "正面-封面"),
  WRT_F("2", "反面-内容页");

  private String key;
  private String name;

  private WaterRelType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key
   *          枚举标识
   * @return IWaterRelType
   */
  public static WaterRelType getByKey(String key) {
    if ((key != null)) {
      WaterRelType[] entitys = WaterRelType.values();
      for (WaterRelType entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

  /**
   * 获取枚举 .
   * @author chenhao
   * @return List<WaterRelType>
   */
  public static List<WaterRelType> getList() {
    return Arrays.asList(WaterRelType.values());
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}
