/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnStencils_]]文件
 * @date 2017年6月2日 上午8:58:55
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 流程节点.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:58:55
 *
 */
public class SnStencils {
  /**
   * type : node id : BPMNDiagram title : BPMN-Diagram description : A BPMN 2.0 diagram. view :
   * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
   * <svg xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:oryx=
   * "http://www.b3mn.org/oryx" xmlns:xlink="http://www.w3.org/1999/xlink" width="800" height="600"
   * version="1.0"> <defs></defs> <g pointer-events="fill" >
   * <polygon stroke="black" fill="black" stroke-width="1" points="0,0 0,590 9,599 799,599 799,9
   * 790,0" stroke-linecap="butt" stroke-linejoin="miter" stroke-miterlimit="10" />
   * <rect id="diagramcanvas" oryx:resize="vertical horizontal" x="0" y="0" width="790" height="590"
   * stroke="black" stroke-width="2" fill="white" />
   * <text font-size="22" id="diagramtext" x="400" y="25" oryx:align="top center" stroke=
   * "#373e48"></text> </g> </svg> icon : diagram.png groups : ["Diagram"] mayBeRoot : true hide :
   * true propertyPackages :
   * ["process_idpackage","namepackage","documentationpackage","process_authorpackage","process_versionpackage",
   * "process_namespacepackage","executionlistenerspackage","eventlistenerspackage","signaldefinitionspackage","messagedefinitionspackage"]
   * hiddenPropertyPackages : [] roles : [] layout : [{"type":"layout.bpmn2_0.pool"}]
   */

  private String type;
  private String id;
  private String title;
  private String description;
  private String view;
  private String icon;
  private boolean mayBeRoot;
  private boolean hide;
  private List<String> groups;
  private List<String> propertyPackages;
  private List<?> hiddenPropertyPackages;
  private List<?> roles;
  private List<SnLayout> layout;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public boolean isMayBeRoot() {
    return mayBeRoot;
  }

  public void setMayBeRoot(boolean mayBeRoot) {
    this.mayBeRoot = mayBeRoot;
  }

  public boolean isHide() {
    return hide;
  }

  public void setHide(boolean hide) {
    this.hide = hide;
  }

  public List<String> getGroups() {
    return groups;
  }

  public void setGroups(List<String> groups) {
    this.groups = groups;
  }

  public List<String> getPropertyPackages() {
    return propertyPackages;
  }

  public void setPropertyPackages(List<String> propertyPackages) {
    this.propertyPackages = propertyPackages;
  }

  public List<?> getHiddenPropertyPackages() {
    return hiddenPropertyPackages;
  }

  public void setHiddenPropertyPackages(List<?> hiddenPropertyPackages) {
    this.hiddenPropertyPackages = hiddenPropertyPackages;
  }

  public List<?> getRoles() {
    return roles;
  }

  public void setRoles(List<?> roles) {
    this.roles = roles;
  }

  public List<SnLayout> getLayout() {
    return layout;
  }

  public void setLayout(List<SnLayout> layout) {
    this.layout = layout;
  }
}
