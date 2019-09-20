/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtModel_]]文件
 * @date 2017年6月6日 上午8:40:22
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流程模型参数类.
 *
 * @author chenhao
 * @date 2017年6月6日 上午8:40:22
 *
 */
public class RtModel {
  /**
   * 模型名称.
   */
  private String name;

  /**
   * 模型标识.
   */
  private String key;

  /**
   * 模型说明.
   */
  private String description;

  /**
   * 模型类目.
   */
  private String category;

  /**
   * 模型流程结构json.
   */
  private String jsonXml;

  /**
   * 模型流程结构svg.
   */
  private String svgXml;

  public RtModel() {
    super();
  }

  public RtModel(String name, String key, String description, String category, String jsonXml,
      String svgXml) {
    super();
    this.name = name;
    this.key = key;
    this.description = description;
    this.category = category;
    this.jsonXml = jsonXml;
    this.svgXml = svgXml;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getJsonXml() {
    return jsonXml;
  }

  public void setJsonXml(String jsonXml) {
    this.jsonXml = jsonXml;
  }

  public String getSvgXml() {
    return svgXml;
  }

  public void setSvgXml(String svgXml) {
    this.svgXml = svgXml;
  }
}
