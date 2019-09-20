package com.oseasy.pro.modules.project.enums;

import com.google.common.collect.Lists;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;
import java.util.Map;

public enum ProjectStatusEnum {
	 S0("0", ProjectNodeVo.P_START_ID, ProjectNodeVo.PN_START_ID, "未提交")
	,S1("1", ProjectNodeVo.PNODE_START_ID, ProjectNodeVo.PNNODE_START_ID, "待学院立项审核")
	,S2("2", ProjectNodeVo.PNODE_START_ID, ProjectNodeVo.PNNODE_START_ID, "待学校立项审核")
	,S3("3", ProjectNodeVo.PNODE_MIDDLE_ID, ProjectNodeVo.PNNODE_MIDDLE_ID, "待提交中期报告")
	,S4("4", ProjectNodeVo.PNODE_MIDDLE_ID, ProjectNodeVo.PNNODE_MIDDLE_ID, "待修改中期报告")
	,S5("5", ProjectNodeVo.PNODE_MIDDLE_ID, ProjectNodeVo.PNNODE_MIDDLE_ID, "待中期检查")
	,S6("6", ProjectNodeVo.PNODE_CLOSE_ID, ProjectNodeVo.PNNODE_CLOSE_ID, "待提交结项报告")
	,S7("7", ProjectNodeVo.PNODE_CLOSE_ID, ProjectNodeVo.PNNODE_CLOSE_ID, "待结项审核")
	,S8("8", ProjectNodeVo.P_END_ID, ProjectNodeVo.PN_END_ID, "项目终止")
	,S9("9", ProjectNodeVo.P_END_ID, ProjectNodeVo.PN_END_ID, "项目结项")
	;

	//(0未提交，1待学院立项审核，2待学校立项审核，3.待提交中期报告，4待修改中期报告,5待中期检查,6待提交结项报告,7.待结项审核，8项目终止)
	private String value;
  private String gnodeId;
  private String nodeId;
	private String name;

  public String getNodeId() {
    return nodeId;
  }

  public String getGnodeId() {
    return gnodeId;
  }

  public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private ProjectStatusEnum(String value, String gnodeId, String nodeId, String name) {
		this.value=value;
		this.gnodeId=gnodeId;
		this.nodeId=nodeId;
		this.name=name;
	}
	public static String getNameByValue(String value) {
    if (StringUtil.isNotEmpty(value)) {
			for(ProjectStatusEnum e:ProjectStatusEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}

	public static ProjectStatusEnum getByValue(String value) {
	  if (StringUtil.isNotEmpty(value)) {
	    for(ProjectStatusEnum e:ProjectStatusEnum.values()) {
	      if ((e.value).equals(value)) {
	        return e;
	      }
	    }
	  }
	  return null;
	}

	public static List<ProjectStatusEnum> getAll() {
		ProjectStatusEnum[] entitys = ProjectStatusEnum.values();
		List<ProjectStatusEnum> projectStatusEnums = Lists.newArrayList();
		for (ProjectStatusEnum entity : entitys) {
			projectStatusEnums.add(entity);
		}
		return projectStatusEnums;
	}
}
