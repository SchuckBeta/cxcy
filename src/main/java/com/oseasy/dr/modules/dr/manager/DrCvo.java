package com.oseasy.dr.modules.dr.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;

public class DrCvo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long version;//版本
	private DrCard card;//卡
	private Long curTime;//当前时间戳
	private Boolean hasExpire;//是否过期
	private Boolean hasFinish;//是否完成
	private List<String> etpIds;
	private Map<String, List<DrCardErspace>> etpCerspaces;

	private final static long MAX_EXPIRE_TIME = 60 * 1;//最大响应时间 3分钟

	public DrCvo() {
		super();
		this.hasExpire = false;
		this.hasFinish = false;
		this.etpIds = Lists.newArrayList();
		this.etpCerspaces = Maps.newHashMap();
	}

	public Boolean getHasExpire() {
		return hasExpire;
	}

	public Boolean checkHasExpire(Long localCurTime) {
	    if(!this.hasExpire){
	        if(((localCurTime - this.getCurTime()) >= MAX_EXPIRE_TIME)){
	            this.hasExpire = true;
	        }
	    }
	    return hasExpire;
	}

	public Boolean getHasFinish() {
		if(!this.hasFinish){
			if((this.etpCerspaces.size() == this.etpIds.size())){
				this.hasFinish = true;
			}
		}
		return hasFinish;
	}

	public void setHasExpire(Boolean hasExpire) {
		this.hasExpire = hasExpire;
	}

	public void setHasFinish(Boolean hasFinish) {
		this.hasFinish = hasFinish;
	}

	public Long getCurTime() {
		return curTime;
	}

	public void setCurTime(Long curTime) {
		this.curTime = curTime;
	}

	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public DrCard getCard() {
		return card;
	}
	public void setCard(DrCard card) {
		this.card = card;
	}
	public List<String> getEtpIds() {
		return etpIds;
	}

	public void setEtpIds(List<String> etpIds) {
		this.etpIds = etpIds;
	}

	public Map<String, List<DrCardErspace>> getEtpCerspaces() {
		return etpCerspaces;
	}
	public void setEtpCerspaces(Map<String, List<DrCardErspace>> etpCerspaces) {
		this.etpCerspaces = etpCerspaces;
	}

	/**
	 * 获取所有的授权.
	 * @param cvo
	 * @return List
	 */
	public static List<DrCardErspace> convert(DrCvo cvo) {
		List<DrCardErspace> drcerspace = Lists.newArrayList();
		 Map<String, List<DrCardErspace>>  maps = cvo.getEtpCerspaces();
		for (String eid : cvo.getEtpIds()) {
			drcerspace.addAll(maps.get(eid));
		}
		return drcerspace;
	}
}
