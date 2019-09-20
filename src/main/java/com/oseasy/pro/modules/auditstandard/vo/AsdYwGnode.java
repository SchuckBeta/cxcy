package com.oseasy.pro.modules.auditstandard.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;

/**
 * 项目流程节点与标准关联对象.
 * @author chenhao
 *
 */
public class AsdYwGnode extends ActYwGnode{
  private String auditStandardName;//标准名称
  private String auditStandardId;//标准id

  public AsdYwGnode() {
    super();
  }

  public AsdYwGnode(ActYwGnode gnode) {
    super();
  }

  public String getAuditStandardName() {
		return auditStandardName;
	}

	public void setAuditStandardName(String auditStandardName) {
		this.auditStandardName = auditStandardName;
	}

	public String getAuditStandardId() {
		return auditStandardId;
	}

	public void setAuditStandardId(String auditStandardId) {
		this.auditStandardId = auditStandardId;
	}

  @JsonIgnore
  public static void sortList2(List<AsdYwGnode> list, List<AsdYwGnode> sourcelist, String parentId, boolean cascade) {
    for (int i = 0; i < sourcelist.size(); i++) {
      AsdYwGnode e = sourcelist.get(i);
      if (e.getParent() != null && e.getParent().getId() != null
          && e.getParent().getId().equals(parentId)) {
        list.add(e);
        if (cascade) {
          // 判断是否还有子节点, 有则继续获取子节点
          for (int j = 0; j < sourcelist.size(); j++) {
            AsdYwGnode child = sourcelist.get(j);
            if (child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
              sortList2(list, sourcelist, e.getId(), true);
              break;
            }
          }
        }
      }
    }
  }
}
