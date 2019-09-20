/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnProperties_]]文件
 * @date 2017年6月2日 上午8:56:58
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 自定义流程.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:56:58
 *
 */
public class SnProperties {
  /**
   * id : process_id type : String title : 流程标识 value : process description : Unique identifier of
   * the process definition. popular : true
   */

  private String id;
  private String type;
  private String title;
  private String value;
  private String description;
  private boolean popular;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPopular() {
    return popular;
  }

  public void setPopular(boolean popular) {
    this.popular = popular;
  }
}
