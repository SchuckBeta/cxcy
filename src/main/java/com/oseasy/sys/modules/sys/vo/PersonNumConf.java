/**
  * Copyright 2017 bejson.com 
  */
package com.oseasy.sys.modules.sys.vo;

/**
 * Auto-generated: 2017-11-07 16:22:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PersonNumConf {

    /**
	 * 
	 */
	private String teamNumOnOff;//团队人数限制开关，1-限制，0不限制
    private ConfRange teamNum;//团队人数范围
    private String schoolTeacherNumOnOff;//校园导师人数限制开关，1-限制，0不限制
    private ConfRange schoolTeacherNum;//校园导师人数范围
    private String enTeacherNumOnOff;//企业导师人数限制开关，1-限制，0不限制
    private ConfRange enTeacherNum;//企业导师人数范围
	public String getTeamNumOnOff() {
		return teamNumOnOff;
	}
	public void setTeamNumOnOff(String teamNumOnOff) {
		this.teamNumOnOff = teamNumOnOff;
	}
	public ConfRange getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(ConfRange teamNum) {
		this.teamNum = teamNum;
	}
	public String getSchoolTeacherNumOnOff() {
		return schoolTeacherNumOnOff;
	}
	public void setSchoolTeacherNumOnOff(String schoolTeacherNumOnOff) {
		this.schoolTeacherNumOnOff = schoolTeacherNumOnOff;
	}
	public ConfRange getSchoolTeacherNum() {
		return schoolTeacherNum;
	}
	public void setSchoolTeacherNum(ConfRange schoolTeacherNum) {
		this.schoolTeacherNum = schoolTeacherNum;
	}
	public String getEnTeacherNumOnOff() {
		return enTeacherNumOnOff;
	}
	public void setEnTeacherNumOnOff(String enTeacherNumOnOff) {
		this.enTeacherNumOnOff = enTeacherNumOnOff;
	}
	public ConfRange getEnTeacherNum() {
		return enTeacherNum;
	}
	public void setEnTeacherNum(ConfRange enTeacherNum) {
		this.enTeacherNum = enTeacherNum;
	}

}