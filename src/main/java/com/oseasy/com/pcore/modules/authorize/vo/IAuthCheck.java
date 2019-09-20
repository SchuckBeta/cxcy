package com.oseasy.com.pcore.modules.authorize.vo;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;

/**
 * @author: QM
 * @date: 2019/5/29 10:23
 * @description:
 */
public interface IAuthCheck {
	static boolean runcheck(AuthCtype atype, String id){
		 Class authClzz = null;
		 IAuthCheck service = null;
		 try {
			 authClzz = Class.forName(CoreSval.achecks.get(atype));
			 service = (IAuthCheck)SpringContextHolder.getBean(authClzz);
		 } catch (Exception e) {
			 System.out.println("IAuthCheck-服务类加载失败： --> authClzz = " + authClzz + " --> service = " + service);
			 return false;
		 }

		 if((authClzz == null) || (service == null)){
			 return false;
		 }
		 Integer ltype = service.checkRltype(id);
		/**
		 * ltype为空的数据不校验.
		 */
		 if((ltype == null)){
			return true;
		 }
		return service.checked(ltype);
	}



	/**
	 * 根据ID获取类型标识.
	 * @param id
	 * @return
	 */
	public Integer checkRltype(String id);

	/**
	 * 根据类型标识校验授权.
	 * @param id
	 * @return
	 */
	public boolean checked(Integer id);



	/**
	 * 授权校验类型.
	 */
	public enum AuthCtype {
		MENU("菜单校验"),
		CTEGROY("栏目校验");

		private String key;//系统
		AuthCtype(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}
}
