package com.oseasy.com.pcore.modules.authorize.enums;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval;

import java.util.List;

/**
 * @author: QM
 * @date: 2019/5/23 17:08
 * @description: 栏目
 */
public enum TenantCategory {
	/**
	 * 内容管理系统
	 */
	S0(54,"网站内容管理系统"),
	/**
	 * 用户及流程管理系统
	 */
	S1(55,"基础数据管理系统"),
	/**
	 * 双创人才库
	 */
	S2(53,"双创人才管理系统"),
	/**
	 * 双创项目申报系统
	 */
	S3(51,"双创项目管理系统"),
	/**
	 * 双创大赛管理系统
	 */
	S4(52,"双创大赛管理系统"),
	/**
	 * 双创学分认定系统
	 */
	S5(56,"双创学分认定系统"),
	/**
	 * 企业孵化基地管理
	 */
	S6(57,"孵化基地管理系统"),
	/**
	 * 大数据分析系统
	 */
	S7(58,"双创大数据分析系统"),
	/**
	 * 云数据支撑系统
	 */
	S8(59,"云数据支撑系统"),

	/**
	 * 省平台管理系统
	 */
	S9(60,"湖北省大学生创新创业训练计划"),

	S10(61,"湖北省创新创业导师库"),


	S11(62,"\"互联网+\" 大赛湖北省赛"),

	S12(63,"湖北省创新创业教育大数据");

	/**
	 * 索引
	 */
	private int id;
	/**
	 * type
	 */
	private String type;

	TenantCategory(int id, String type) {
		this.id = id;
		this.type = type;
	}

	public static TenantCategory getById(Integer id) {
		TenantCategory[] entitys = TenantCategory.values();
		for (TenantCategory entity : entitys) {
			if ((id).equals(entity.getId())) {
				return entity;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static List<Integer> filters(String type){
		return filters(CoreSval.Rtype.getByKey(type));
	}
	public static List<Integer> filters(CoreSval.Rtype rtype){
		List<Integer> ids = Lists.newArrayList();
		if((CoreSval.Rtype.SUPER_SC).equals(rtype)){
		}else if((CoreSval.Rtype.ADMIN_SYS_SC).equals(rtype)){
		}else if((CoreSval.Rtype.ADMIN_YW_SC).equals(rtype)){
		}else if((CoreSval.Rtype.ADMIN_PN).equals(rtype)){
		}else if((CoreSval.Rtype.ADMIN_SN).equals(rtype)){
			ids.add(TenantCategory.S0.getId());
			ids.add(TenantCategory.S1.getId());
			ids.add(TenantCategory.S2.getId());
			ids.add(TenantCategory.S3.getId());
			ids.add(TenantCategory.S4.getId());
			ids.add(TenantCategory.S5.getId());
			ids.add(TenantCategory.S6.getId());
			ids.add(TenantCategory.S7.getId());
			ids.add(TenantCategory.S8.getId());
			ids.add(TenantCategory.S9.getId());
			ids.add(TenantCategory.S10.getId());
			ids.add(TenantCategory.S11.getId());
			ids.add(TenantCategory.S12.getId());
		}else if((CoreSval.Rtype.MINISTER).equals(rtype)){
			ids.add(TenantCategory.S1.getId());
			ids.add(TenantCategory.S2.getId());
			ids.add(TenantCategory.S3.getId());
			ids.add(TenantCategory.S4.getId());
		}else if((CoreSval.Rtype.EXPORT).equals(rtype)){
			ids.add(TenantCategory.S1.getId());
			ids.add(TenantCategory.S2.getId());
			ids.add(TenantCategory.S3.getId());
			ids.add(TenantCategory.S4.getId());
		}else if((CoreSval.Rtype.TEACHER).equals(rtype)){
		}else if((CoreSval.Rtype.STUDENT).equals(rtype)){
		}else if((CoreSval.Rtype.OTHER).equals(rtype)){
		}
		return ids;
	}
	
	@Override
	public String toString() {
		return "TenantCategory{" +
				"id=" + id +
				", type='" + type + '\'' +
				'}';
	}
}
