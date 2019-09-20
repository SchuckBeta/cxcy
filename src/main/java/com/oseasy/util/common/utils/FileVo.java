package com.oseasy.util.common.utils;

import java.io.File;
import java.util.List;

public class FileVo {
  public static final int SUCCESS = 1;//全部成功
  public static final int SUC_FAIL = 2;//部分成功
  public static final int FAIL = 0;//全部失败

  private Integer status;//状态
  private List<File> suFiles;//成功文件
  private List<File> faFiles;//失败文件
  public FileVo() {
    super();
  }

  public FileVo(Integer status) {
    super();
    this.status = status;
  }

  public FileVo(Integer status, List<File> suFiles) {
    super();
    this.status = status;
    this.suFiles = suFiles;
  }

  public FileVo(List<File> suFiles, List<File> faFiles) {
    super();
    this.status = SUC_FAIL;
    this.suFiles = suFiles;
    this.faFiles = faFiles;
  }

  public FileVo(Integer status, List<File> suFiles, List<File> faFiles) {
    super();
    this.status = status;
    this.suFiles = suFiles;
    this.faFiles = faFiles;
  }

  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }
  public List<File> getSuFiles() {
    return suFiles;
  }
  public void setSuFiles(List<File> suFiles) {
    this.suFiles = suFiles;
  }
  public List<File> getFaFiles() {
    return faFiles;
  }
  public void setFaFiles(List<File> faFiles) {
    this.faFiles = faFiles;
  }
}
