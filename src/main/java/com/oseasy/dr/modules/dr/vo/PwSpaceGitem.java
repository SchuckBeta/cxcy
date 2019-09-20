package com.oseasy.dr.modules.dr.vo;

public class PwSpaceGitem{
	private String id;//场地空间
	private String spaceId;//场地id
    private String dname;//门名称
    private String eptId;//设备id
    private String eptNo;//设备编号
    private String drNo;//设备端口号
    private String sel;//是否选中该门1-选中，0-未选中
    private String canSel;//是否允许选择（去除两条规则选择同一个门的情况）
    private String groupName;//预警规则名称

	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEptNo() {
		return eptNo;
	}
	public void setEptNo(String eptNo) {
		this.eptNo = eptNo;
	}
	public String getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getEptId() {
		return eptId;
	}
	public void setEptId(String eptId) {
		this.eptId = eptId;
	}
	public String getDrNo() {
		return drNo;
	}
	public void setDrNo(String drNo) {
		this.drNo = drNo;
	}
	public String getSel() {
		return sel;
	}
	public void setSel(String sel) {
		this.sel = sel;
	}
    public String getCanSel() {
        return canSel;
    }
    public void setCanSel(String canSel) {
        this.canSel = canSel;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}