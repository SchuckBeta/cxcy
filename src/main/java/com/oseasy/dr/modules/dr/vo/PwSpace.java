package com.oseasy.dr.modules.dr.vo;

import java.util.List;


public class PwSpace  {
    private String sid;//场地id
    private String sname;//场地名称
    private String pid;//场地父id
    private String type;//场地type
    private List<PwSpaceDoor> doors;//场地拥有的门
    private List<PwSpaceGitem> gitems;//场地拥有的门
	public String getSid() {
		return sid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List<PwSpaceDoor> getDoors() {
		return doors;
	}
	public void setDoors(List<PwSpaceDoor> doors) {
		this.doors = doors;
	}

    public List<PwSpaceGitem> getGitems() {
        return gitems;
    }

    public void setGitems(List<PwSpaceGitem> gitems) {
        this.gitems = gitems;
    }
}