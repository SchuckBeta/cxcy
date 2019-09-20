/**
 *
 */
package com.oseasy.com.pcore.modules.sys.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Role;

/**
 * 角色DAO接口


 */
@MyBatisDao
public interface
RoleDao extends CrudDao<Role> {
	@Override
	@FindListByTenant
	public List<Role> findList(Role entity);

	public List<Role> findListNtenant(Role entity);
	@FindListByTenant
	public List<Role> findRoles(Role entity);
	@FindListByTenant
	public List<Role> findByOffice(Role entity);

	@Override
	@InsertByTenant
	public int insert(Role entity);
	@InsertByTenant
	public int insertPL(@Param("entitys") List<Role> entitys);
	@Override
	@FindListByTenant
	public List<Role> findAllList(Role entity);
	public Integer getRoleUserCount(String roleid);
	public Role getByName(Role role);

	public Role getByEnname(Role role);

	@FindListByTenant
	public Role getByRid(Role role);

	/**
	 * 获取当前租户类型开户的初始角色（rinit = true）.
	 * @param role 角色
	 * @return Role
	 */
	@FindListByTenant
	public Role getByRtype(Role role);
	/**
	 * 获取当前租户类型开户的初始角色（rinit = true）,含有菜单.
	 * @param role 角色
	 * @return Role
	 */
	@FindListByTenant
	public Role getByRtmenu(Role role);

	/**
	 * 获取省租户类型开户的初始角色
	 * @param role
	 * @return
	 */
	Role getByRtypeOfProvince(Role role);

	/**
	 * 获取当前租户所有该类型角色（包含初始和用户自己新建的）.
	 * @param role 角色
	 * @return List
	 */
	@FindListByTenant
	public List<Role> findListByRtype(Role role);

	/**
	 * 获取当前租户所有的的初始角色（rinit = true）.
	 * @param role 角色
	 * @return List
	 */
	@FindListByTenant
	public List<Role> findListByRinit(Role role);

	/**
	 * 删除所有角色
	 * @param entity
	 * @return
	 */
	public void deleteWLByOffice(Role entity);
	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);
	public int deleteRoleMenus(Role role);

	public int insertRoleMenu(Role role);

	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);

	public List<Role> findListByUserId(@Param(value = "userId") String userId);

	public Role getNamebyId(String id);

	@FindListByTenant
	public Integer checkRoleName(Role role);

	public Integer checkRoleEnName(Role role);

    public int checkRoleByUid(@Param(value = "uid") String uid, @Param(value = "rid") String rid);
	public List<Role> findListByIds(@Param("ids") List<String> ids);
	public List<Role> findListByUserIds(@Param("ids") List<String> ids);

	List<String> getRoleNameByGnodeId(@Param(value = "gnodeId") String gnodeId);
}
