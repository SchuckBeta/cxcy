/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process
 * @Description [[_ActYwSten_]]文件
 * @date 2017年6月2日 上午8:42:52
 *
 */

package com.oseasy.act.modules.actyw.tool.process;

import com.oseasy.act.modules.actyw.tool.process.vo.RtSvl;
import com.oseasy.act.modules.actyw.tool.process.vo.SnPropertyPackages;
import com.oseasy.act.modules.actyw.tool.process.vo.SnRules;
import com.oseasy.act.modules.actyw.tool.process.vo.SnStencils;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.util.common.utils.json.JsonAliUtils;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * 工作流元素集.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:42:52
 *
 */
public class ActYwSten {
  /**
   * title : 流程编辑器.
   * namespace : http://b3mn.org/stencilset/bpmn2.0#
   * description : BPMN流程编辑器
   * propertyPackages : 包属性
   * rules : 规则
   */

  private String title;
  private String namespace;
  private String description;
  private SnRules rules;
  private List<SnPropertyPackages> propertyPackages;
  private List<SnStencils> stencils;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SnRules getRules() {
    return rules;
  }

  public void setRules(SnRules rules) {
    this.rules = rules;
  }

  public List<SnPropertyPackages> getPropertyPackages() {
    return propertyPackages;
  }

  public void setPropertyPackages(List<SnPropertyPackages> propertyPackages) {
    this.propertyPackages = propertyPackages;
  }

  public List<SnStencils> getStencils() {
    return stencils;
  }

  public void setStencils(List<SnStencils> stencils) {
    this.stencils = stencils;
  }

  /**
   * 根据json生成SnStencils对象列表.
   * @author chenhao
   * @return List
   */
  public static List<SnStencils> genActYwStenStencils() {
      return genActYwSten().getStencils();
  }
  /**
   * 根据json生成Rule对象.
   * @author chenhao
   * @return List
   */
  public static SnRules genActYwStenRules() {
    return genActYwSten().getRules();
  }

  /**
   * 根据json生成对象.
   * @author chenhao
   * @param jsons json
   * @return ActYwResult
   */
  public static ActYwSten genActYwSten() {
    try {
      return genActYwStens(readJson(RtSvl.RtModelVal.STENCILSET_JSON)).get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 读取json文件 .
   * @author chenhao
   * @param path 路径
   */
  public static String readJson(String path) throws Exception {
    InputStream stencilsetStream = StenType.class.getClassLoader().getResourceAsStream(path);
    try {
      return IOUtils.toString(stencilsetStream, "utf-8");
    } catch (Exception e) {
      throw new Exception("Error while loading json", e);
    }
  }

  /**
   * 根据json生成对象.
   * @author chenhao
   * @param json json
   * @return List
   */
  public static List<ActYwSten> genActYwStens(String json) {
    return (List<ActYwSten>) JsonAliUtils.toBean("[" + json + "]", ActYwSten.class);
  }
}
