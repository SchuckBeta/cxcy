/**
 *
 */
package com.oseasy.com.pcore.modules.sys.utils;

import java.util.List;

import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;

import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户工具类


 */
public class UserUtils {
	public static final String maxUploadSize = CoreSval.getConfig("web.maxUploadSize");
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	public static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static OaNotifyDao oaNotifyDao = SpringContextHolder.getBean(OaNotifyDao.class);

	public static final String CACHE_OANOTIFY_LIST = "oaNotifyList";

    /*************************************************************
     * 公共处理的方法.
     *************************************************************/
	/**
     * 清除当前用户缓存
     */
    public static void clearCache() {
//        CoreUtils.removeCache(CACHE_OANOTIFY_LIST);
        CoreUtils.clearCache();
    }

    /**获取上传文件最大大小限制
     * @return
     */
    public static String getMaxUpFileSize(){
        return maxUploadSize;
    }

    /**
     * 获取专业名称
     * @return
     */
    public static String getProfessional(String id) {
        Office office = officeDao.get(id);
        if (office!=null&&"3".equals(office.getGrade())) {
            return office.getName();
        }
        return null;
    }

    //得到学院
    public static List<Office> findColleges() {
        return  officeDao.findColleges();
    }

    //根据学院id 得到其下面的专业
    public static List<Office> findProfessionals(String parentId) {
        return  officeDao.findProfessionals(parentId);
    }
    /*************************************************************
     * 公共未处理的方法.
     *************************************************************/


	/**
	 * 获取所有角色列表
	 * @return
	 */
	public static List<Role> getAllRoleList() {
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)
				JedisUtils.hashGet(RedisEnum.ROLE.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST);
		if (roleList == null) {
			roleList = roleDao.findAllList(new Role());
			JedisUtils.hashSetKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST, roleList);
//			CoreUtils.putCache(CoreUtils.CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}

	//检查用户是否有某个角色
	/*public static boolean checkHasRole(String userid,RoleBizTypeEnum rolebiztype) {
		if(StringUtil.isEmpty(userid)||rolebiztype==null){
			return false;
		}
		List<Role> rs=roleDao.findListByUserId(userid);
		if(rs==null||rs.size()==0){
			return false;
		}
		for(Role r:rs){
			if(rolebiztype.getValue().equals(r.getBizType())){
				return true;
			}
		}
		return false;
	}*/

	/**has rolelist
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id) {
	User user = (User)
			JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_ID_ + id);
//			CacheUtils.get(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + id);
		if (user ==  null) {
			user = userDao.get(id);
			if (user == null) {
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_ID_ + id, user);
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);

		}
		return user;
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName) {
		User user = (User)
				JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_LOGIN_NAME_ + loginName);

//				CacheUtils.get(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null) {
			user = userDao.getByLoginName(new User(null, loginName));
			if (user == null) {
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_ID_ + user.getId(), user);
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);

//			CacheUtils.put(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + user.getId(), user);
//			CacheUtils.put(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}

	public static User getByLoginNameOrNo(String loginNameOrNo) {
		User user = (User)
				JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_LOGIN_NAME_ + loginNameOrNo);
		if (user == null) {
			user = userDao.getByLoginNameOrNo(loginNameOrNo,null);
			if (user == null) {
				return null;
			}
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_ID_ + user.getId(), user);
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);

		}
		return user;
	}


	public static boolean isExistNo(String no) {
		User	user = userDao.getByLoginNameOrNo(no,null);
		if (user == null) {
			return false;
		}
		return true;
	}
	/**
	 * 根据手机号获取用户
	 * @param mobile
	 * @return 取不到返回null
	 */
	public static boolean isExistMobile(String mobile) {
		User p=new User();
		p.setMobile(mobile);
		User	user = userDao.getByMobile(p);
		if (user == null) {
			return false;
		}
		return true;
	}
	public static User getByMobile(String mobile) {
		User p=new User();
		p.setMobile(mobile);
		User	user = userDao.getByMobile(p);
		if (user == null) {
			return null;
		}
		user.setRoleList(roleDao.findList(new Role(user)));
		return user;
	}
	public static User getByMobile(String mobile,String id) {
		User p=new User();
		p.setMobile(mobile);
		p.setId(id);
		User	user = userDao.getByMobileWithId(p);
		if (user == null) {
			return null;
		}
		user.setRoleList(roleDao.findList(new Role(user)));
		return user;
	}

    /**
     * 检查当前是否登录.
     * @return 取不到返回 new User()
     */
	public static Boolean checkToLogin() {
	    return checkToLogin(getUser());
	}
    public static Boolean checkToLogin(User curUser) {
        if((curUser == null) || StringUtil.isEmpty(curUser.getId())){
            return true;
        }
	    return false;
	}

	/**
	 * 获取当前用户 (has rolelist)
	 * @return 取不到返回 new User()
	 */
	public static User getUser() {
		if((CoreUtils.getUser() == null) || StringUtil.isEmpty(CoreUtils.getUser().getId())){
			return new User();
		}
	    return CoreUtils.getUser();
	}

	public static User getUserTenant() {
		if((CoreUtils.getUserTenant() == null) || StringUtil.isEmpty(CoreUtils.getUserTenant().getId())){
			return new User();
		}
		return CoreUtils.getUserTenant();
	}

	public static String getUserId() {
	    return getUser().getId();
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList() {
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)
				JedisUtils.hashGet(RedisEnum.ROLE.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST);
//				CoreUtils.getCache(CoreUtils.CACHE_ROLE_LIST);
		if (roleList == null) {
			User user = getUser();
			if (user.getAdmin()) {
				roleList = roleDao.findAllList(new Role());
			}else{
				Role role = new Role();
				//role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao.findList(role);
			}
			JedisUtils.hashSetKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST, roleList);

//			CoreUtils.putCache(CoreUtils.CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}

    public static SimpleAuthorizationInfo getSimpleAuthorizationInfo(SimpleAuthorizationInfo info) {
        List<Menu> list = CoreUtils.getMenuList();
        for (Menu menu : list) {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                // 添加基于Permission的权限信息
                for (String permission : StringUtils.split(menu.getPermission(),",")) {
                    info.addStringPermission(permission);
                }
            }
        }
        return info;
    }


	public static Office getAdminOffice() {
		Office office = (Office)CacheUtils.get(CoreUtils.CACHE_OFFICE, CoreIds.NCE_SYS_OFFICE_TOP.getId());
		if (office == null) {
			office = officeDao.get(CoreIds.NCE_SYS_OFFICE_TOP.getId());
			if (office!=null)CacheUtils.put(CoreUtils.CACHE_OFFICE, CoreIds.NCE_SYS_OFFICE_TOP.getId(),office);
		}
		return office;
	}

	public static List<Office> getOfficeListFront() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)
				JedisUtils.hashGet(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),"officeListFront");

		if (officeList == null) {
			/*if (user.isAdmin()) {*/
				officeList = officeDao.findAllList(new Office());
			/*}else{
				Office office = new Office();
			//	office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}*/
			JedisUtils.hashSetKey(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),"officeListFront", officeList);
		}
		return officeList;
	}



	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	// ============== User Cache ==============
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = CoreUtils.getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static List<OaNotify> getOaNotifyList(OaNotify oaNotify) {
		@SuppressWarnings("unchecked")
		List<OaNotify> oaNotifyList = (List<OaNotify>)
				JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CACHE_OANOTIFY_LIST);
//				CoreUtils.getCache(CACHE_OANOTIFY_LIST);
		if (oaNotifyList == null) {
			oaNotifyList = oaNotifyDao.findList(oaNotify);
			JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CACHE_OANOTIFY_LIST, oaNotifyList);
		}
		return oaNotifyList;
	}

	/**
	 * 跳转登录页面.
	 * @return String
	 */
	public static String toLogin() {
	  return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/toLogin";
	}

	public static String getCheckParam(User user){
		String checkStr = "";
		String loginName = user.getLoginName();
		String no = user.getNo();
		if(StringUtil.isNotEmpty(loginName)){
			checkStr = loginName;
		}
		if(StringUtil.isNotEmpty(no)){
			checkStr = no;
		};
		return checkStr;
	}

//	public static Boolean isCompleteUserInfo(User user, StudentExpansion studentExpansion){
//		Boolean isComplete = true;
//		List<String> userKeys = new ArrayList<>();
//		userKeys.add("getMobile");
//		userKeys.add("email");
//		userKeys.add("idType");
//		userKeys.add("idNumber");
//		for (String keyItem: userKeys){
//			if(StringUtil.isEmpty(user.getIdType())){
//				isComplete = false;
//				break;
//			}
//		}
//		return  isComplete;
//	}

	/****************************************************************************************
	 * 运营平台
	 ****************************************************************************************/
    /**
     * 是否为运营中心超级管理员.
     * @return boolean
     */
    public static boolean isSuper(){
        return isSuper(UserUtils.getUser());
    }

    /**
     * 是否为运营中心超级管理员.
     * @return boolean
     */
    public static boolean isSuper(User user){
        String roleIds = StringUtil.listIdToStr(user.getRoleList());
        return (roleIds != null) && (roleIds).contains(CoreIds.NCE_SYS_ROLE_SUPER.getId());
    }

    /**
     * 是否为运营中心系统管理员.
     * @return boolean
     */
    public static boolean isAdminSys(){
        return isAdminSys(UserUtils.getUser());
    }

    /**
     * 是否为运营中心系统管理员.
     * @return boolean
     */
    public static boolean isAdminSys(User user){
        String roleIds = StringUtil.listIdToStr(user.getRoleList());
        return (roleIds != null) && (roleIds).contains(CoreIds.NCE_SYS_ROLE_ADMIN.getId());
    }

	/****************************************************************************************
	 * 省平台
	 ****************************************************************************************/
	/**
	 * 是否为省机构管理员.
	 * @return boolean
	 */
	public static boolean isNprAdminOffice(){
		return isNprAdminOffice(UserUtils.getUser());
	}

	/**
	 * 是否为省机构管理员.
	 * @return boolean
	 */
	public static boolean isNprAdminOffice(User user){
		String roleIds = StringUtil.listIdToStr(user.getRoleList());
		return (roleIds != null) && (roleIds).contains(CoreIds.NPR_SYS_ROLE_ADMIN.getId());
	}

	/****************************************************************************************
	 * 校平台
	 ****************************************************************************************/
	/**
	 * 是否为校机构管理员.
	 * @return boolean
	 */
	public static boolean isNscAdminOffice(){
		return isNscAdminOffice(UserUtils.getUser());
	}

	/**
	 * 是否为校机构管理员.
	 * @return boolean
	 */
	public static boolean isNscAdminOffice(User user){
		String roleIds = StringUtil.listIdToStr(user.getRoleList());
		return (roleIds != null) && (roleIds).contains(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
	}

    /**
     * 当前用户是否为管理员（含超级管理员）.
     * @return boolean
     */
    public static boolean isAdm(){
        return isAdm(UserUtils.getUser());
    }

    /**
     * 当前用户是否为管理员（含超级管理员）.
     * @return boolean
     */
    public static boolean isAdm(User user){
        return isAdminOrSuperAdmin(user);
    }

    /**
     * 该用户是否为管理员（含超级管理员）.
     * @return boolean
     */
    public static boolean isAdminOrSuperAdmin(User user){
        List<Role> roles =roleDao.findList(new Role(user));
        boolean isAdmin=false;
        if(StringUtil.checkNotEmpty(roles)){
            for(Role role:roles){
				if(role != null &&
						((CoreSval.Rtype.SUPER_SC.getKey()).equals(role.getRtype())
								|| (CoreSval.Rtype.ADMIN_SYS_SC.getKey()).equals(role.getRtype())
								|| (CoreSval.Rtype.ADMIN_YW_SC.getKey()).equals(role.getRtype())
								|| (CoreSval.Rtype.ADMIN_SN.getKey()).equals(role.getRtype())
								|| (CoreSval.Rtype.ADMIN_PN.getKey()).equals(role.getRtype())
						)){

//				if(role != null &&
//						(CoreIds.NSC_SYS_ROLE_ADMIN.getId().equals(role.getId())
//								|| CoreIds.NCE_SYS_ROLE_SUPER.getId().equals(role.getId())
//								|| CoreIds.NCE_SYS_ROLE_ADMIN.getId().equals(role.getId())
//								|| CoreIds.NCE_SYS_ROLE_ADMYW.getId().equals(role.getId())
//								|| CoreIds.NPR_SYS_ROLE_ADMIN.getId().equals(role.getId())
//						)){
                    isAdmin=true;
                    break;
                }
            }
        }
        return isAdmin;
    }
}
