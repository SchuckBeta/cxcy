package com.oseasy.pro.modules.tpl.vo.impl;

import java.io.Serializable;

import com.oseasy.pro.modules.tpl.vo.IMparam;

/**
 * Created by Administrator on 2017/9/27 0027.
 */
public class Mteam implements IMparam, Serializable {
	private String tplFileName;

	@Override
	public String getTplFileName() {
		return tplFileName;
	}

	@Override
	public void setTplFileName(String tplFileName) {
		this.tplFileName = tplFileName;
	}

	public static Mteam init(Mteam param) {
	   if (param == null) {
		 param = new Mteam();
	   }
		//TODO 初始化模板参数
	   return param;
	 }
}
