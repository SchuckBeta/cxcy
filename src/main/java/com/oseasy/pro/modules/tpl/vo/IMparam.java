package com.oseasy.pro.modules.tpl.vo;

import java.util.Date;

public interface IMparam {
  public static final String WORD_JSON = "/json/word";
  /**
   * 获取Tpl文件名.
   * @return
   */
  public String getTplFileName();

  /**
   * 设置Tpl文件名.
   * @return
   */
  public void setTplFileName(String fileName);
}