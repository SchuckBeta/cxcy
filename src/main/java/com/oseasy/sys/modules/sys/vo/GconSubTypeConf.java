/**
  * Copyright 2017 bejson.com 
  */
package com.oseasy.sys.modules.sys.vo;

/**
 * Auto-generated: 2017-11-08 8:48:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class GconSubTypeConf{

    /**
	 * 
	 */
	private String subType;//大赛类型，字典值
    private PersonNumConf personNumConf;//申报人数配置


    public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public void setPersonNumConf(PersonNumConf personNumConf) {
         this.personNumConf = personNumConf;
     }
     public PersonNumConf getPersonNumConf() {
         return personNumConf;
     }

}