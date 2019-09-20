/**
 *
 */
package com.oseasy.com.pcore.modules.sys.entity;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.OfficeEntity;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * 机构Entity


 */
public class Office extends OfficeEntity<Office> {

	private static final long serialVersionUID = 1L;
	public static final String GRADE = "grade";
	private Area area;		// 归属区域
	private String code; 	// 机构编码(机构惟一标识)
	private String rid; 	// 模板角色ID
	private String name; 	// 机构名称
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String useable;//是否可用
	private User primaryPerson;//主负责人
	private User deputyPerson;//副负责人
	private List<String> childDeptList;//快速添加子部门

	private String cityCode;         //城市编号
	private String schoolCode;        //学校编号
	private List<Office> children; //子级
	private String regval;// 正则规则(名称|区域编码|城市编码)

	public Office() {
		super();
		this.type = "2";
	}

	public Office(String id) {
		super(id);
	}

	public Office(Office parent) {
		super();
		this.parent = parent;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getRegval() {
		return regval;
	}

	public void setRegval(String regval) {
		this.regval = regval;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public User getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(User primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public User getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(User deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

//	@JsonBackReference
//	@NotNull
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@Override
	public String getParentIds() {
		if(StringUtil.isEmpty(this.parentIds) && (this.parent != null) && StringUtil.isNotEmpty(this.parent.getParentIds()) && StringUtil.isNotEmpty(this.parent.getId())){
			this.parentIds = this.parent.getParentIds() + this.parent.getId() + StringUtil.DOTH;
		}
		return parentIds;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
//
//	@Length(min=1, max=100)
	public String getName() {
	return name;
	}
//
	public void setName(String name) {
		this.name = name;
	}
//
//	public Integer getSort() {
//		return sort;
//	}
//
//	public void setSort(Integer sort) {
//		this.sort = sort;
//	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

//	public String getParentId() {
//		return parent != null && parent.getId() != null ? parent.getId() : "0";
//	}


	public List<Office> getChildren() {
		return children;
	}

	public void setChildren(List<Office> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}