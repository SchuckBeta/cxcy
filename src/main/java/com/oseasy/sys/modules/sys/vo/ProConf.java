/**
  * Copyright 2017 bejson.com 
  */
package com.oseasy.sys.modules.sys.vo;
import java.util.List;

/**
 * Auto-generated: 2017-11-07 16:22:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ProConf {

    /**
	 * 
	 */
	private String aOnOff;//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
    private String bOnOff;//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
    private String cOnOff;//是否允许同一个学生用同一个项目申报不同类型的项目,1-允许，0-不允许
    private List<ProSubTypeConf> proSubTypeConf;//不同项目类型的配置，大创、小创之类
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
	public String getcOnOff() {
		return cOnOff;
	}
	public void setcOnOff(String cOnOff) {
		this.cOnOff = cOnOff;
	}
	public List<ProSubTypeConf> getProSubTypeConf() {
		return proSubTypeConf;
	}
	public void setProSubTypeConf(List<ProSubTypeConf> proSubTypeConf) {
		this.proSubTypeConf = proSubTypeConf;
	}

    

}