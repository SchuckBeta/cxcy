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
public class LowTypeConf{

    /**
	 * 
	 */
	private String lowType;//项目类别,字典值
    private PersonNumConf personNumConf;//申报人数配置
    

    public String getLowType() {
		return lowType;
	}
	public void setLowType(String lowType) {
		this.lowType = lowType;
	}
	public void setPersonNumConf(PersonNumConf personNumConf) {
         this.personNumConf = personNumConf;
     }
     public PersonNumConf getPersonNumConf() {
         return personNumConf;
     }

}