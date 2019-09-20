package com.oseasy.pro.common.utils.image;

import java.util.Arrays;
import java.util.List;

/**
 * 水印资源类别.
 * @author chenhao
 */
public enum WaterResType {
  WRT_MAIN("1", "主图", WaterResFileType.WRFT_IMG),
  WRT_ICON("2", "公章水印", WaterResFileType.WRFT_IMG),
  WRT_BICON("3", "背景水印图标", WaterResFileType.WRFT_IMG),
  WRT_BTEXT("4", "背景水印文本", WaterResFileType.WRFT_TEXT),
  WRT_TEXT("5", "内容参数", WaterResFileType.WRFT_TEXT);

  private String key;
  private String name;
  private WaterResFileType resFileType;

  private WaterResType(String key, String name, WaterResFileType resFileType) {
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
  public static WaterResType getByKey(String key) {
    if ((key != null)) {
      WaterResType[] entitys = WaterResType.values();
      for (WaterResType entity : entitys) {
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
   * @return List<WaterResType>
   */
  public static List<WaterResType> getList() {
    return Arrays.asList(WaterResType.values());
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public WaterResFileType getResFileType() {
    return resFileType;
  }
}
