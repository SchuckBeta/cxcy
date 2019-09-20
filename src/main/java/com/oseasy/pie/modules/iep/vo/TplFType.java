package com.oseasy.pie.modules.iep.vo;

import java.util.List;

/**
 * 模板文件类型.
 * @author chenhao
 *
 */
public enum TplFType {
  EXCEL_XLS("10", "Excel.xls", ".xls"), EXCEL_XLSX("20", "Excel.xlsx", ".xlsx"), WORD("30", "Word.doc", ".doc"), ZIP("90", "Word.zip", ".zip");
    public static final String TPL_FTYPE = "tplFtype";
    public static final String TPL_FTYPES = "tplFtypes";

  private String key;
  private String name;
  private String postfix;

  private TplFType(String key, String name, String postfix) {
    this.key = key;
    this.name = name;
    this.postfix = postfix;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplFType
   */
  public static TplFType getByKey(String key) {
    if ((key != null)) {
      TplFType[] entitys = TplFType.values();
      for (TplFType entity : entitys) {
        if ((key).equals(entity.getKey())) {
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
   * @return TplFType
   */
  public static Boolean checkHas(List<TplFType> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return TplFType
   */
  public static TplFType checkRet(List<TplFType> fstypes, String key) {
      for (TplFType fstype : fstypes) {
          if ((fstype.getKey()).equals(key)) {
              return fstype;
          }
      }
      return null;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPostfix() {
    return postfix;
}

@Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"postfix\":\"" + this.postfix + "\"}";
  }
}
