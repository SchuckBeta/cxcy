/**
  * Copyright 2017 bejson.com 
  */
package com.oseasy.sys.modules.sys.vo;
import java.util.List;

/**
 * Auto-generated: 2017-11-08 8:48:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ProSubTypeConf {

    /**
	 * 
	 */
	private String subType;//项目类型，字典值
    private List<LowTypeConf> lowTypeConf;//不同项目类别配置，创新创业、创新实践
    

    public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public void setLowTypeConf(List<LowTypeConf> lowTypeConf) {
         this.lowTypeConf = lowTypeConf;
     }
     public List<LowTypeConf> getLowTypeConf() {
         return lowTypeConf;
     }

}