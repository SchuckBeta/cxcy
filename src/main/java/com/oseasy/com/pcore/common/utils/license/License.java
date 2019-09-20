package com.oseasy.com.pcore.common.utils.license;

import com.oseasy.com.pcore.common.config.CoreSval;

import net.sf.json.JSONArray;

/**
 * Created by victor on 2017/3/22.
 */
public class License {
    public static final String MONTH_0 = "0";
    public static final String PRODUCT_TYPE_SY = "0";
    public static final String PRODUCT_TYPE_ZS = "1";
    private String productName;//产品名称
    private String productId;//产品ID
    private String month;//有效期时长,0 代表无限期
    private JSONArray machineCode;//机器码
    private String expiredDate; //有效期开始日期
    private String client_name;//客户端名称
    private String productType;//产品类型,试用期0，正式1
    private String product_key;//产品标识
    private String valid;//是否有效,0-无效，1-有效
    private String modules;//菜单功能

	public String getModules() {
		return modules;
	}
	public void setModules(String modules) {
		this.modules = modules;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getProduct_key() {
		return product_key;
	}
	public void setProduct_key(String product_key) {
		this.product_key = product_key;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public JSONArray getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(JSONArray machineCode) {
		this.machineCode = machineCode;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * 是否开启校验.
	 * @return
	 */
    public static boolean isOpen() {
//        return true;
        if(!CoreSval.getLicense()){
            return true;
        }
        return false;
    }
}
