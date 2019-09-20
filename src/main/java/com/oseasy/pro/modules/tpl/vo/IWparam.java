package com.oseasy.pro.modules.tpl.vo;

import java.util.Date;

public interface IWparam {
  public static final String WORD_JSON = "/json/word";
  /** 文件名前缀. */
  public static final String FILE_TPL_PREFIX = "大学生";

  /**
   * 获取下载文件模板前缀.
   * @return String
   */
  public static String getFileTplPreFix() {
    return (new Date().getYear() + 1901) + FILE_TPL_PREFIX;
  }

  /**
   * 获取文件名.
   * @return
   */
  public String getFileName();

  /**
   * 获取Tpl文件名.
   * @return
   */
  public String getTplFileName();

  /**
   * 设置文件名.
   * @return
   */
  public void setFileName(String fileName);
  /**
   * 设置Tpl文件名.
   * @return
   */
  public void setTplFileName(String fileName);
}