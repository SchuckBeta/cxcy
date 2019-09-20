package com.oseasy.com.pcore.modules.authorize.enums;

import com.oseasy.util.common.utils.StringUtil;

public enum MenuEnum {
	/*报备系统授权列表索引,对应模块菜单id,*/
	S0(0,"31","",""),//内容管理系统
	S1(1,"2","",""),//用户及流程管理系统
	S2(2,"14b1fd32fc0545258d703cb39916b677","",",881b0b797c2745c5947fb22662221b19,"),//双创人才库
	S3(3,"5474e38a3c8a46f590939df6a453d5f8",",d9e340db5b7940cabb2ab8b0cca1b670,",",c5c65c9a80a849cfbe4a05741b78d902,"),//双创项目申报系统
	S4(4,"85c6095f275540b9980dde2b06d77382",",6bc42269b41540a4ab036b75cba3e54c,",",448e7bc14f3c477fa31a7c47fe016c12,"),//双创大赛管理系统
	S5(5,"810ac9622bc449dfbc0c8f3ceb1f6b73","",",10194,"),//双创学分认定系统
	S6(6,"dac585634aff46cfad306c2c4e95609c","",",10189,"),//企业孵化基地管理
	S7(7,"67d3263ec2b74597a90855df2ab3aed3","",""),//大数据分析系统
	S8(8,"45daeb14f0c847ada4e4ba0610db9f9c","","")//云数据支撑系统
	;
	private int index;//报备系统授权列表索引
	private String id;//开创啦系统模块菜单id
	private String mids;//系统模块菜单子菜单id串
	private String cids;//前台栏目id串




	private MenuEnum(int index, String id, String mids, String cids) {
		this.index = index;
		this.id = id;
		this.mids = mids;
		this.cids = cids;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMids() {
		return mids;
	}

	public void setMids(String mids) {
		this.mids = mids;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	public static String getIdByIndex(int index) {
		for(MenuEnum e:MenuEnum.values()) {
			if (e.index==index) {
				return e.id;
			}
		}
		return null;
	}
	public static int getIndexById(String id) {
		if (StringUtil.isEmpty(id)) {
			return -1;
		}
		for(MenuEnum e:MenuEnum.values()) {
			if (e.id.equals(id)) {
				return e.index;
			}
		}
		return -1;
	}
	public static int getIndexByChildMenuId(String id) {
		if (StringUtil.isEmpty(id)) {
			return -1;
		}
		id=","+id+",";
		for(MenuEnum e:MenuEnum.values()) {
			if (e.mids.contains(id)) {
				return e.index;
			}
		}
		return -1;
	}
	public static int getIndexByCategoryId(String id) {
		if (StringUtil.isEmpty(id)) {
			return -1;
		}
		id=","+id+",";
		for(MenuEnum e:MenuEnum.values()) {
			if (e.cids.contains(id)) {
				return e.index;
			}
		}
		return -1;
	}
}
