package com.oseasy.pro.common.utils.image;

import java.util.Arrays;
import java.util.List;

/**
 * 水印资源文件类型.
 * @author chenhao
 *
 */
public enum WaterResFileType {
  WRFT_TEXT("1", "文本"),
  WRFT_IMG("2", "图片");

  private String key;
  private String name;

  private WaterResFileType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key
   *          枚举标识
   * @return IWaterResType
   */
  public static WaterResFileType getByKey(String key) {
    if ((key != null)) {
      WaterResFileType[] entitys = WaterResFileType.values();
      for (WaterResFileType entity : entitys) {
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
   * @return List<WaterResFileType>
   */
  public static List<WaterResFileType> getList() {
    return Arrays.asList(WaterResFileType.values());
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}
