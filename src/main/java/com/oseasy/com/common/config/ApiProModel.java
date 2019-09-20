package com.oseasy.com.common.config;

import static com.oseasy.com.common.config.ApiConst.INNER_ERROR;

/**
 * @author: QM
 * @date: 2019/4/27 23:04
 * @description: 项目申报
 */
public enum ApiProModel {
	PRO_TEACHER_FORBID(501,"导师不能申报项目"),
	PRO_PERSON_PERFECT(502,"个人信息未完善,立即完善个人信息？"),
	PRO_STUDENT_NO(503,"未找到学生信息"),
	PRO_STUDENT_GRADUATE(504,"您已经毕业,不能申报项目"),
	PRO_CONFIG_NO(505,"无项目申报配置信息,请联系管理员"),
	PRO_CONFIG_NO_FIND(506,"未找到项目申报配置信息,请联系管理员"),
	PRO_DECLARE_ERROR(507,"项目申报配置信息有误,请联系管理员"),
	PRO_STUDENT_PARTICIPATION(508,"你已参加大赛"),
	PRO_STUDENT_PARTICIPATION_OTHER(509,"你已申报其他类型的项目"),
	PRO_STUDENT_PARTICIPATION_THIS(510,"你已申报该类型的项目"),
	PRO_STUDENT_UNCOMMITTED(511,"你有未提交的该类型项目"),
	PRO_TUTOR_FORBID(512,"导师不能申报大赛"),
	PRO_MEGAGAME_ERROR(513,"大赛申报配置信息有误,请联系管理员"),
	PRO_MEGAGAME_UNCOMMITTED(514,"你有未提交的该类型大赛"),
	PRO_NAME_MUST(515,"保存失败，项目名称为必填项"),
	PRO_NAME_EXIST(516,"保存失败，该项目名称已经存在"),
	PRO_AUDIT_BEGIN(517,"该项目已经开始审核,不能撤销"),
	PRO_REVERT_ERROR(518,"撤销异常");


	private Integer code;
	private String msg;
	ApiProModel(Integer code,String msg){
		this.code = code;
		this.msg = msg;
	}

	public java.lang.Integer getCode() {
		return code;
	}

	public void setCode(java.lang.Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



	public static String getErrMsg(Integer code) {
		for(ApiProModel cmsApiConst : ApiProModel.values()){
			if(code.equals(cmsApiConst.getCode())){
				return cmsApiConst.getMsg();
			}
		}
		return INNER_ERROR.getMsg();
	}
}
