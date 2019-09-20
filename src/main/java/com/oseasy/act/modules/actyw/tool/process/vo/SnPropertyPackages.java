/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnPropertyPackages_]]文件
 * @date 2017年6月2日 上午8:55:17
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 自定义流程包.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:55:17
 *
 */
public class SnPropertyPackages {
  /**
   * name : process_idpackage properties :
   * [{"id":"process_id","type":"String","title":"流程标识","value":"process","description":"Unique
   * identifier of the process definition.","popular":true}]
   */

  private String name;
  private List<SnProperties> properties;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SnProperties> getProperties() {
    return properties;
  }

  public void setProperties(List<SnProperties> properties) {
    this.properties = properties;
  }
}
