package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 民大项目立项审核导入错误数据表Entity.
 * @author 9527
 * @version 2017-09-22
 */
public class ProMdApprovalError extends DataEntity<ProMdApprovalError> {

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String proCategory;		// 申报类型
	private String level;		// 申报级别
	private String pNumber;		// 项目编号
	private String pName;		// 项目名称
	private String leaderName;		// 负责人-姓名
	private String no;		// 负责人-学号
	private String mobile;		// 负责人-手机
	private String proSource;		// 项目来源
	private String sourceProjectName;		// 来源项目名称
	private String sourceProjectType;		// 来源项目类别
	private String teachers1;		// 导师-姓名
	private String teachers2;		// 导师-职称
	private String teachers3;		// 导师-学历
	private String teachers4;		// 导师-工号
	private String rufu;		// 是否申请入孵
	private String members1;		// 成员-姓名
	private String members2;		// 成员-学号
	private String members3;		// 成员-手机
	private String result;		// 评审结果
	private String proModelMdId;		// 项目id
	private String gnodeid;		// 节点
	private String sheetIndex;//所在sheet
	public ProMdApprovalError() {
		super();
	}

	public ProMdApprovalError(String id) {
		super(id);
	}

	
	public String getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(String sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=128, message="申报类型长度必须介于 0 和 128 之间")
	public String getProCategory() {
		return proCategory;
	}

	public void setProCategory(String proCategory) {
		if (proCategory!=null&&proCategory.length()>128) {
			return;
		}
		this.proCategory = proCategory;
	}

	@Length(min=0, max=128, message="申报级别长度必须介于 0 和 128 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		if (level!=null&&level.length()>128) {
			return;
		}
		this.level = level;
	}

	@Length(min=0, max=128, message="项目编号长度必须介于 0 和 128 之间")
	public String getPNumber() {
		return pNumber;
	}

	public void setPNumber(String pNumber) {
		if (pNumber!=null&&pNumber.length()>128) {
			return;
		}
		this.pNumber = pNumber;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getPName() {
		return pName;
	}

	public void setPName(String pName) {
		if (pName!=null&&pName.length()>128) {
			return;
		}
		this.pName = pName;
	}

	@Length(min=0, max=128, message="负责人-姓名长度必须介于 0 和 128 之间")
	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		if (leaderName!=null&&leaderName.length()>128) {
			return;
		}
		this.leaderName = leaderName;
	}

	@Length(min=0, max=128, message="负责人-学号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		if (no!=null&&no.length()>128) {
			return;
		}
		this.no = no;
	}

	@Length(min=0, max=128, message="负责人-手机长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		if (mobile!=null&&mobile.length()>128) {
			return;
		}
		this.mobile = mobile;
	}

	@Length(min=0, max=128, message="项目来源长度必须介于 0 和 128 之间")
	public String getProSource() {
		return proSource;
	}

	public void setProSource(String proSource) {
		if (proSource!=null&&proSource.length()>128) {
			return;
		}
		this.proSource = proSource;
	}

	@Length(min=0, max=128, message="来源项目名称长度必须介于 0 和 128 之间")
	public String getSourceProjectName() {
		return sourceProjectName;
	}

	public void setSourceProjectName(String sourceProjectName) {
		if (sourceProjectName!=null&&sourceProjectName.length()>128) {
			return;
		}
		this.sourceProjectName = sourceProjectName;
	}

	@Length(min=0, max=128, message="来源项目类别长度必须介于 0 和 128 之间")
	public String getSourceProjectType() {
		return sourceProjectType;
	}

	public void setSourceProjectType(String sourceProjectType) {
		if (sourceProjectType!=null&&sourceProjectType.length()>128) {
			return;
		}
		this.sourceProjectType = sourceProjectType;
	}

	@Length(min=0, max=128, message="导师-姓名长度必须介于 0 和 128 之间")
	public String getTeachers1() {
		return teachers1;
	}

	public void setTeachers1(String teachers1) {
		if (teachers1!=null&&teachers1.length()>128) {
			return;
		}
		this.teachers1 = teachers1;
	}

	@Length(min=0, max=128, message="导师-职称长度必须介于 0 和 128 之间")
	public String getTeachers2() {
		return teachers2;
	}

	public void setTeachers2(String teachers2) {
		if (teachers2!=null&&teachers2.length()>128) {
			return;
		}
		this.teachers2 = teachers2;
	}

	@Length(min=0, max=128, message="导师-学历长度必须介于 0 和 128 之间")
	public String getTeachers3() {
		return teachers3;
	}

	public void setTeachers3(String teachers3) {
		if (teachers3!=null&&teachers3.length()>128) {
			return;
		}
		this.teachers3 = teachers3;
	}

	@Length(min=0, max=128, message="导师-工号长度必须介于 0 和 128 之间")
	public String getTeachers4() {
		return teachers4;
	}

	public void setTeachers4(String teachers4) {
		if (teachers4!=null&&teachers4.length()>128) {
			return;
		}
		this.teachers4 = teachers4;
	}

	@Length(min=0, max=128, message="是否申请入孵长度必须介于 0 和 128 之间")
	public String getRufu() {
		return rufu;
	}

	public void setRufu(String rufu) {
		if (rufu!=null&&rufu.length()>128) {
			return;
		}
		this.rufu = rufu;
	}

	@Length(min=0, max=256, message="成员-姓名长度必须介于 0 和 256 之间")
	public String getMembers1() {
		return members1;
	}

	public void setMembers1(String members1) {
		if (members1!=null&&members1.length()>256) {
			return;
		}
		this.members1 = members1;
	}

	@Length(min=0, max=256, message="成员-学号长度必须介于 0 和 256 之间")
	public String getMembers2() {
		return members2;
	}

	public void setMembers2(String members2) {
		if (members2!=null&&members2.length()>256) {
			return;
		}
		this.members2 = members2;
	}

	@Length(min=0, max=256, message="成员-手机长度必须介于 0 和 256 之间")
	public String getMembers3() {
		return members3;
	}

	public void setMembers3(String members3) {
		if (members3!=null&&members3.length()>256) {
			return;
		}
		this.members3 = members3;
	}

	@Length(min=0, max=12, message="评审结果长度必须介于 0 和 12 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		if (result!=null&&result.length()>12) {
			return;
		}
		this.result = result;
	}

	@Length(min=0, max=64, message="项目id长度必须介于 0 和 64 之间")
	public String getProModelMdId() {
		return proModelMdId;
	}

	public void setProModelMdId(String proModelMdId) {
		if (proModelMdId!=null&&proModelMdId.length()>64) {
			return;
		}
		this.proModelMdId = proModelMdId;
	}

	@Length(min=0, max=64, message="节点长度必须介于 0 和 64 之间")
	public String getGnodeid() {
		return gnodeid;
	}

	public void setGnodeid(String gnodeid) {
		if (gnodeid!=null&&gnodeid.length()>64) {
			return;
		}
		this.gnodeid = gnodeid;
	}

}