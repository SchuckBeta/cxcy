package com.oseasy.act.modules.actyw.tool.process.vo;

import com.oseasy.util.common.utils.StringUtil;

public class RtPxFormPropertie {
  private String id;
  private String name;//名称
  private String type;//表单类型
  private String readable;//true
  private String writable;//true
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getReadable() {
    if (StringUtil.isEmpty(this.readable)) {
      this.readable = "true";
    }
    return readable;
  }
  public void setReadable(String readable) {
    this.readable = readable;
  }
  public String getWritable() {
    if (StringUtil.isEmpty(this.writable)) {
      this.writable = "true";
    }
    return writable;
  }
  public void setWritable(String writable) {
    this.writable = writable;
  }
}
