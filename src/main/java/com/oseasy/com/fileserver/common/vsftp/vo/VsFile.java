package com.oseasy.com.fileserver.common.vsftp.vo;

import java.io.File;

public class VsFile {
  private String remotePath;//远程路径
  private String rfileName;//远程文件名
  private String localPath;//本地路径
  private String lfileName;//本地文件名
//  private File file;//本地文件名

  public String getRemotePath() {
    return remotePath;
  }
  public void setRemotePath(String remotePath) {
    this.remotePath = remotePath;
  }
  public String getRfileName() {
    return rfileName;
  }
  public void setRfileName(String rfileName) {
    this.rfileName = rfileName;
  }
  public String getLocalPath() {
    return localPath;
  }
  public void setLocalPath(String localPath) {
    this.localPath = localPath;
  }
  public String getLfileName() {
    return lfileName;
  }
  public void setLfileName(String lfileName) {
    this.lfileName = lfileName;
  }
//  public File getFile() {
//    return file;
//  }
//  public void setFile(File file) {
//    this.file = file;
//  }
}
