package com.oseasy.pro.common.utils.image;

import java.util.Arrays;
import java.util.List;

/**
 * 水印资源类型.
 * @author chenhao
 *
 */
public enum WaterType {
  WT_Z_MAIN(WaterRelType.WRT_Z.getKey() + WaterResType.WRT_MAIN.getKey(), WaterRelType.WRT_Z, WaterResType.WRT_MAIN, WaterRelType.WRT_Z.getName() + WaterResType.WRT_MAIN.getName()),
  WT_Z_ICON(WaterRelType.WRT_Z.getKey() + WaterResType.WRT_ICON.getKey(), WaterRelType.WRT_Z, WaterResType.WRT_ICON, WaterRelType.WRT_Z.getName() + WaterResType.WRT_ICON.getName()),
  WT_Z_BICON(WaterRelType.WRT_Z.getKey() + WaterResType.WRT_BICON.getKey(), WaterRelType.WRT_Z, WaterResType.WRT_BICON, WaterRelType.WRT_Z.getName() + WaterResType.WRT_BICON.getName()),
  WT_Z_TEXT(WaterRelType.WRT_Z.getKey() + WaterResType.WRT_TEXT.getKey(), WaterRelType.WRT_Z, WaterResType.WRT_TEXT, WaterRelType.WRT_Z.getName() + WaterResType.WRT_TEXT.getName()),
  WT_Z_BTEXT(WaterRelType.WRT_Z.getKey() + WaterResType.WRT_BTEXT.getKey(), WaterRelType.WRT_Z, WaterResType.WRT_BTEXT, WaterRelType.WRT_Z.getName() + WaterResType.WRT_BTEXT.getName()),

  WT_F_MAIN(WaterRelType.WRT_F.getKey() + WaterResType.WRT_MAIN.getKey(), WaterRelType.WRT_F, WaterResType.WRT_MAIN, WaterRelType.WRT_F.getName() + WaterResType.WRT_MAIN.getName()),
  WT_F_ICON(WaterRelType.WRT_F.getKey() + WaterResType.WRT_ICON.getKey(), WaterRelType.WRT_F, WaterResType.WRT_ICON, WaterRelType.WRT_F.getName() + WaterResType.WRT_ICON.getName()),
  WT_F_BICON(WaterRelType.WRT_F.getKey() + WaterResType.WRT_BICON.getKey(), WaterRelType.WRT_F, WaterResType.WRT_BICON, WaterRelType.WRT_F.getName() + WaterResType.WRT_BICON.getName()),
  WT_F_TEXT(WaterRelType.WRT_F.getKey() + WaterResType.WRT_TEXT.getKey(), WaterRelType.WRT_F, WaterResType.WRT_TEXT, WaterRelType.WRT_F.getName() + WaterResType.WRT_TEXT.getName()),
  WT_F_BTEXT(WaterRelType.WRT_F.getKey() + WaterResType.WRT_BTEXT.getKey(), WaterRelType.WRT_F, WaterResType.WRT_BTEXT, WaterRelType.WRT_F.getName() + WaterResType.WRT_BTEXT.getName());

  private String key;
  private WaterRelType relType;
  private WaterResType resType;
  private String name;

  private WaterType(String key, WaterRelType relType, WaterResType resType, String name) {
    this.key = key;
    this.name = name;
    this.relType = relType;
    this.resType = resType;
  }

  /**
   * 根据key获取枚举 .
   * @author chenhao
   * @param key
   *          枚举标识
   * @return IWaterType
   */
  public static WaterType getByKey(String key) {
    if ((key != null)) {
      WaterType[] entitys = WaterType.values();
      for (WaterType entity : entitys) {
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
   * @return List<WaterType>
   */
  public static List<WaterType> getList() {
    return Arrays.asList(WaterType.values());
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public WaterRelType getRelType() {
    return relType;
  }

  public WaterResType getResType() {
    return resType;
  }
}
