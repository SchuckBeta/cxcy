package com.oseasy.com.pcore.common.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.excel.fieldtype.RoleListType;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.util.List;

/**
 * 角色判断实体类
 * @version 2014-05-16
 */
public abstract class UserEntity<T> extends DataEntity<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String uuid;

	protected List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	public UserEntity() {
	}

	public UserEntity(String id) {
		super(id);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonIgnore
	@ExcelField(title = "拥有角色", align = 1, sort = 800, fieldType = RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	/**
	 * 校验是否未当前平台下的租户的超级管理员.
	 * @return boolean
	 */
	public static boolean checkCurpnSuper(User user) {
		if(user == null){
			return false;
		}
		String curType = CoreSval.getCurrpntplTenantByType(user);
		if((Sval.EmPn.NCENTER).equals(curType)){
			return UserEntity.isSuper(user) || UserEntity.isAdmin(user);
		}else if((Sval.EmPn.NPROVINCE).equals(curType)){
			return UserEntity.isNprAdmin(user);
		}else if((Sval.EmPn.NSCHOOL).equals(curType)){
			return UserEntity.isNscAdmin(user);
		}
		return false;
	}
	/****************************************************************************************
	 * 运营平台
	 ****************************************************************************************/
	/**
	 * 是否为超级管理员
	 * @param user UserEntity
	 * @return boolean
     */
	public static boolean isSuper(UserEntity user) {
		if(UserEntity.isSupUser(user.getId())){
			return true;
		}

		if(StringUtil.checkNotEmpty(user.getRoleList())){
			IRole cur;
			for (int i = 0; i < user.getRoleList().size(); i++) {
				cur  = (IRole) user.getRoleList().get(i);
				if(UserEntity.isSuperRole(cur.getRtype())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 用户是否为运营中心内置超级管理员.
	 * @param userId 用户ID
	 * @return boolean
	 */
	public static boolean isSupUser(String userId) {
		return (StringUtil.isNotEmpty(userId) && (CoreIds.NCE_SYS_USER_SUPER.getId()).equals(userId));
	}

	/**
	 * 用户是否为运营中心超级管理员.
	 * @param roleId 角色ID
	 * @return boolean
	 */
	public static boolean isSuperRole(String rtype) {
		return StringUtil.isNotEmpty(rtype) && (CoreSval.Rtype.SUPER_SC.getKey()).equals(rtype);
//		return (StringUtil.isNotEmpty(rtype) && (CoreIds.NCE_SYS_ROLE_SUPER.getId()).equals(rtype));
	}

	/**
	 * 用户是否为运营中心系统管理员.
	 * @param user UserEntity
	 * @return boolean
     */
	public static boolean isAdmin(UserEntity user) {
		if(StringUtil.checkNotEmpty(user.getRoleList())){
			IRole cur;
			for (int i = 0; i < user.getRoleList().size(); i++) {
				cur  = (IRole) user.getRoleList().get(i);
				if(UserEntity.isAdmin(cur.getRtype())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 角色是否为运营中心系统管理员.
	 * @param roleId 角色ID
	 * @return boolean
	 */
	public static boolean isAdmin(String rtype) {
		return StringUtil.isNotEmpty(rtype) && (CoreSval.Rtype.ADMIN_SYS_SC.getKey()).equals(rtype);
//		return StringUtil.isNotEmpty(rtype) && (CoreIds.NCE_SYS_ROLE_ADMIN.getId()).equals(rtype);
	}


	/**
	 * 用户是否为运营中心运营管理员.
	 * @param user UserEntity
	 * @return boolean
	 */
	public static boolean isAdmyw(UserEntity user) {
		if(StringUtil.checkNotEmpty(user.getRoleList())){
			IRole cur;
			for (int i = 0; i < user.getRoleList().size(); i++) {
				cur  = (IRole) user.getRoleList().get(i);
				if(UserEntity.isAdmyw(cur.getRtype())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 角色是否为运营中心系统运营管理员.
	 * @param roleId 用户ID
	 * @return boolean
	 */
	public static boolean isAdmyw(String rtype) {
		return StringUtil.isNotEmpty(rtype) && (CoreSval.Rtype.ADMIN_YW_SC.getKey()).equals(rtype);
//		return StringUtil.isNotEmpty(rtype) && (CoreIds.NCE_SYS_ROLE_ADMYW.getId()).equals(rtype);
	}
	/****************************************************************************************
	 * 省平台
	 ****************************************************************************************/
	/**
	 * 是否为省机构管理员.
	 * @return boolean
	 */
	public static boolean isNprAdmin(String rtype) {
		return StringUtil.isNotEmpty(rtype) && (CoreSval.Rtype.ADMIN_PN.getKey()).equals(rtype);
//		return StringUtil.isNotEmpty(rtype) && (CoreIds.NPR_SYS_ROLE_ADMIN.getId()).equals(rtype);
	}

	/**
	 * 用户是否为省机构系统管理员.
	 * @param user UserEntity
	 * @return boolean
	 */
	public static boolean isNprAdmin(UserEntity user) {
		if(StringUtil.checkNotEmpty(user.getRoleList())){
			IRole cur;
			for (int i = 0; i < user.getRoleList().size(); i++) {
				cur  = (IRole) user.getRoleList().get(i);
				if(UserEntity.isNprAdmin(cur.getRtype())){
					return true;
				}
			}
		}
		return false;
	}

	/****************************************************************************************
	 * 校平台
	 ****************************************************************************************/
	/**
	 * 是否为校机构管理员.
	 * @return boolean
	 */
	public static boolean isNscAdmin(String rtype) {
		return StringUtil.isNotEmpty(rtype) && (CoreSval.Rtype.ADMIN_SN.getKey()).equals(rtype) ;
//		return StringUtil.isNotEmpty(rtype) && (CoreIds.NSC_SYS_ROLE_ADMIN.getId()).equals(rtype) ;
	}
	/**
	 * 用户是否为校机构系统管理员.
	 * @param user UserEntity
	 * @return boolean
	 */
	public static boolean isNscAdmin(UserEntity user) {
		if(StringUtil.checkNotEmpty(user.getRoleList())){
			IRole cur;
			for (int i = 0; i < user.getRoleList().size(); i++) {
				cur  = (IRole) user.getRoleList().get(i);
				if(UserEntity.isNscAdmin(cur.getRtype())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据用户和模板角色列表获取给当前用户赋角色
	 * @return User
	 */
	public static boolean checkRole(UserEntity user, List<String> rtypes) {
		if(StringUtil.checkEmpty(rtypes)){
			return false;
		}

		if(StringUtil.checkNotEmpty(user.getRoleList())){
			IRole cur;
			for (int i = 0; i < user.getRoleList().size(); i++) {
				cur  = (IRole) user.getRoleList().get(i);
				if((rtypes).contains(cur.getRtype())){
					return true;
				}
			}
		}
		return false;
	}
}
