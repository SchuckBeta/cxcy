package com.oseasy.com.pcore.modules.authorize.enums;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;

/**
 * @author: QM
 * @date: 2019/5/22 15:23
 * @description: 菜单,租户初始化
 */
public enum TenantMenu {
	/**
	 * 内容管理系统
	 */
	S0(54, "网站内容管理系统"),
	/**
	 * 用户及流程管理系统
	 */
	S1(55, "基础数据管理系统"),
	/**
	 * 双创人才库
	 */
	S2(53, "双创人才管理系统"),
	/**
	 * 双创项目申报系统
	 */
	S3(51, "双创项目管理系统"),
	/**
	 * 双创大赛管理系统
	 */
	S4(52, "双创大赛管理系统"),
	/**
	 * 双创学分认定系统
	 */
	S5(56, "双创学分认定系统"),
	/**
	 * 企业孵化基地管理
	 */
	S6(57, "孵化基地管理系统"),
	/**
	 * 大数据分析系统
	 */
	S7(58, "双创大数据分析系统"),
	/**
	 * 云数据支撑系统
	 */
	S8(59, "云数据支撑系统"),

	/**
	 * 省平台管理系统
	 */
	S9(60, "湖北省大学生创新创业训练计划"),

	S10(61, "湖北省创新创业导师库"),


	S11(62, "\"互联网+\" 大赛湖北省赛"),

	S12(63, "湖北省创新创业教育大数据");

	/**
	 * 索引
	 */
	private int id;
	/**
	 * type
	 */
	private String type;

	TenantMenu(int id, String type) {
		this.id = id;
		this.type = type;
	}

	public static TenantMenu getById(Integer id) {
		TenantMenu[] entitys = TenantMenu.values();
		for (TenantMenu entity : entitys) {
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

	public static List<Integer> filters(String type) {
		return filters(CoreSval.Rtype.getByKey(type));
	}

	public static List<Integer> filters(CoreSval.Rtype rtype) {
		List<Integer> ids = Lists.newArrayList();
		if ((CoreSval.Rtype.SUPER_SC).equals(rtype)) {
		} else if ((CoreSval.Rtype.ADMIN_SYS_SC).equals(rtype)) {
		} else if ((CoreSval.Rtype.ADMIN_YW_SC).equals(rtype)) {
		} else if ((CoreSval.Rtype.ADMIN_PN).equals(rtype)) {
		} else if ((CoreSval.Rtype.ADMIN_SN).equals(rtype)) {
			ids.add(TenantMenu.S0.getId());
			ids.add(TenantMenu.S1.getId());
			ids.add(TenantMenu.S2.getId());
			ids.add(TenantMenu.S3.getId());
			ids.add(TenantMenu.S4.getId());
			ids.add(TenantMenu.S5.getId());
			ids.add(TenantMenu.S6.getId());
			ids.add(TenantMenu.S7.getId());
			ids.add(TenantMenu.S8.getId());
			ids.add(TenantMenu.S9.getId());
			ids.add(TenantMenu.S10.getId());
			ids.add(TenantMenu.S11.getId());
			ids.add(TenantMenu.S12.getId());
		} else if ((CoreSval.Rtype.MINISTER).equals(rtype)) {
//			ids.add(TenantMenu.S1.getId());
//			ids.add(TenantMenu.S2.getId());
//			ids.add(TenantMenu.S3.getId());
//			ids.add(TenantMenu.S4.getId());
		} else if ((CoreSval.Rtype.EXPORT).equals(rtype)) {
//			ids.add(TenantMenu.S1.getId());
//			ids.add(TenantMenu.S2.getId());
//			ids.add(TenantMenu.S3.getId());
//			ids.add(TenantMenu.S4.getId());
		} else if ((CoreSval.Rtype.TEACHER).equals(rtype)) {
		} else if ((CoreSval.Rtype.STUDENT).equals(rtype)) {
		} else if ((CoreSval.Rtype.OTHER).equals(rtype)) {
		}
		return ids;
	}

	/**
	 * 运营过滤菜单列表.
	 * @param menus
	 * @return
     */
	public static List<Menu> yymenufilters(List<Menu> menus) {
		List<Integer> ltypeFilters = Lists.newArrayList();
		List<String> hrefFilters = Lists.newArrayList();
		if(CoreSval.isChangeTcvo(Sval.EmPn.NPROVINCE)){
			ltypeFilters.add(TenantMenu.S2.getId());
			ltypeFilters.add(TenantMenu.S3.getId());
			ltypeFilters.add(TenantMenu.S4.getId());
//			hrefFilters.add("/sys/backTeacherExpansion/toProvinceTeacherList");
		}else if(CoreSval.isChangeTcvo(Sval.EmPn.NSCHOOL)){
		}else if(CoreSval.isChangeTcvo(Sval.EmPn.NCENTER)){
		}
		return menuFilters(menus, ltypeFilters, hrefFilters);
	}

	/**
	 * 全局根据菜单父类型或Href过滤菜单列表.
	 * @param menus        List
	 * @param ltypeFilters List
	 * @param hrefFilters  List
	 * @return List
	 */
	public static List<Menu> menuFilters(List<Menu> menus, List<Integer> ltypeFilters, List<String> hrefFilters) {
		boolean hasHref = false;
		boolean hasLtype = false;
		List<Menu> targets = Lists.newArrayList();
		if (StringUtil.checkNotEmpty(ltypeFilters)) {
			hasLtype = true;
		}

		if (StringUtil.checkNotEmpty(hrefFilters)) {
			hasHref = true;
		}

		for (Menu cur : menus) {
			if (hasLtype && (cur.getLtype() != null) && (ltypeFilters).contains(cur.getLtype())) {
				continue;
			}

			if (hasHref && StringUtil.isNotEmpty(cur.getHref()) && (hrefFilters).contains(cur.getHref())) {
				continue;
			}

			targets.add(cur);
		}
		return targets;
	}


	@Override
	public String toString() {
		return "TenantMenu{" +
				"id=" + id +
				", type='" + type + '\'' +
				'}';
	}
}
