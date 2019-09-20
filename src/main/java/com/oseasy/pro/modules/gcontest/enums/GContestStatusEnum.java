package com.oseasy.pro.modules.gcontest.enums;

import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.util.common.utils.StringUtil;

public enum GContestStatusEnum {
	 S0("0", GContestNodeVo.G_START_ID, GContestNodeVo.GN_START_ID,"未提交")
	,S1("1", GContestNodeVo.GNODE_FIRST_ID, GContestNodeVo.GNNODE_FIRST_ID, "待学院专家评分")
	,S2("2", GContestNodeVo.GNODE_SECOND_ID, GContestNodeVo.GNNODE_SECOND_ID, "待学院秘书审核")
	,S3("3", GContestNodeVo.GNODE_THREE_ID, GContestNodeVo.GNNODE_THREE_ID, "待学校专家评分")
	,S4("4", GContestNodeVo.GNODE_FOUR_ID, GContestNodeVo.GNNODE_FOUR_ID, "待学校管理员审核")
	,S5("5", GContestNodeVo.GNODE_FIVE_ID, GContestNodeVo.GNNODE_FIVE_ID, "待学校管理员路演审核")
	,S6("6", GContestNodeVo.GNODE_SIX_ID, GContestNodeVo.GNNODE_SIX_ID, "待学校管理员评级")
	,S7("7", GContestNodeVo.G_END_ID, GContestNodeVo.GN_END_ID, "校赛评级结束")
	,S8("8", GContestNodeVo.G_END_ID, GContestNodeVo.GN_END_ID, "校赛未通过")
	,S9("9", GContestNodeVo.G_END_ID, GContestNodeVo.GN_END_ID, "院赛未通过")
	;

	private String value;
	private String gnodeId;
	private String nodeId;
	private String name;

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

	public String getNodeId() {
    return nodeId;
  }

  public String getGnodeId() {
    return gnodeId;
  }

  private GContestStatusEnum(String value, String gnodeId, String nodeId, String name) {
		this.value=value;
		this.gnodeId=gnodeId;
		this.nodeId=nodeId;
		this.name=name;
	}
	public static String getNameByValue(String value) {
    if (StringUtil.isNotEmpty(value)) {
			for(GContestStatusEnum e:GContestStatusEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}

  public static GContestStatusEnum getByValue(String value) {
    if (StringUtil.isNotEmpty(value)) {
      for(GContestStatusEnum e:GContestStatusEnum.values()) {
        if ((e.value).equals(value)) {
          return e;
        }
      }
    }
    return null;
  }
}
