package com.oseasy.pro.modules.project.vo;

import java.util.List;

import com.oseasy.pro.modules.project.entity.ProMid;
import com.oseasy.pro.modules.project.entity.ProProgress;
import com.oseasy.pro.modules.project.entity.ProSituation;

/**
 * 项目申报Vo
 * @author 9527
 * @version 2017-03-11
 */
public class ProMidVo  {
	private ProMid proMid;
	private List<ProSituation> situation;
	private List<ProProgress> sroProgress;
	public ProMid getProMid() {
		return proMid;
	}
	public void setProMid(ProMid proMid) {
		this.proMid = proMid;
	}
	public List<ProSituation> getSituation() {
		return situation;
	}
	public void setSituation(List<ProSituation> situation) {
		this.situation = situation;
	}
	public List<ProProgress> getSroProgress() {
		return sroProgress;
	}
	public void setSroProgress(List<ProProgress> sroProgress) {
		this.sroProgress = sroProgress;
	}
	
	
}