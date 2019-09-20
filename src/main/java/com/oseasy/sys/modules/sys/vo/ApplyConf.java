/**
  * Copyright 2017 bejson.com 
  */
package com.oseasy.sys.modules.sys.vo;

/**
 * Auto-generated: 2017-11-07 16:22:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ApplyConf {

    /**
	 * 
	 */
	private String aOnOff;//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
    private String bOnOff;//是否允许同一个学生用同一个项目既申报项目又参加大赛，1-允许，0-不允许
    private ProConf proConf;//双创项目配置
    private GconConf gconConf;//双创大赛配置
	public String getaOnOff() {
		return aOnOff;
	}
	public void setaOnOff(String aOnOff) {
		this.aOnOff = aOnOff;
	}
	public String getbOnOff() {
		return bOnOff;
	}
	public void setbOnOff(String bOnOff) {
		this.bOnOff = bOnOff;
	}
	public ProConf getProConf() {
		return proConf;
	}
	public void setProConf(ProConf proConf) {
		this.proConf = proConf;
	}
	public GconConf getGconConf() {
		return gconConf;
	}
	public void setGconConf(GconConf gconConf) {
		this.gconConf = gconConf;
	}
    

}