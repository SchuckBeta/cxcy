package com.oseasy.pw.modules.pw.vo;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum PwEnterAuditEnum {
	 DSH("0","待审核")
	,SHTG("1","审核通过")
	,SHBTG("2","审核不通过")
	;
	private String value;//code
	private String name;//名称

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private PwEnterAuditEnum(String value, String name) {
		this.value=value;
		this.name=name;

	}
	public static PwEnterAuditEnum getByValue(String value) {
		if (value!=null) {
			for(PwEnterAuditEnum e: PwEnterAuditEnum.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
		}
		return null;
	}
	//获得审核枚举
	public static List<PwEnterAuditEnum> getByKeys() {
		PwEnterAuditEnum[] entitys = PwEnterAuditEnum.values();
		List<PwEnterAuditEnum> pwEnterStatuss = Lists.newArrayList();
		for (PwEnterAuditEnum entity : entitys) {
			pwEnterStatuss.add(entity);
		}
		return pwEnterStatuss;
	}

	public static List<Object> getPwEnterAuditList(){
		PwEnterAuditEnum[] entitys = PwEnterAuditEnum.values();
		List<Object> pwEnterStatuss = Lists.newArrayList();
		for(PwEnterAuditEnum entity : entitys){
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("label", entity.getName());
			hashMap.put("value", entity.getValue());
			pwEnterStatuss.add(hashMap);
		}
		return pwEnterStatuss;
	}

    //根据审核值转换成 审核表存储值
    public static String getAuditValue(String atype) {
        if ((PassNot.PASS.getKey()).equals(atype)) {
            return PwEnterAuditEnum.SHTG.getValue();
        }
        return PwEnterAuditEnum.SHBTG.getValue();
    }
}
