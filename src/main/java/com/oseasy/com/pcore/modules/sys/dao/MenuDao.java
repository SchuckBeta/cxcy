/**
 *
 */
package com.oseasy.com.pcore.modules.sys.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.modules.sys.entity.User;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Menu;

/**
 * 菜单DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {
	@Override
	@FindListByTenant
	public List<Menu> findList(Menu entity);
	@Override
	@InsertByTenant
	public int insert(Menu entity);
	@Override
	@FindListByTenant
	public List<Menu> findAllList(Menu entity);

	//登录之后的当前人的授权菜单list
	@FindListByTenant
	public List<Menu> findAllListIndex(Menu entity);

	public List<Menu> findRoleMenuByParentIdsLike(@Param("rids") List<String> rids, @Param("menu") Menu menu);

	public List<Menu> findByParentIdsLike(Menu menu);

	@FindListByTenant
	public List<Menu> findByUserId(Menu menu);

	public List<Menu> findByRoleId(String roleId);

	public int updateParentIds(Menu menu);

	public int updateSort(Menu menu);

	public Menu getMenuByName(String name);

	public Integer checkMenuName(Menu menu);

	/**
	 * 根据当前租户获取根菜单.
	 * @return Menu
	 */
	@FindListByTenant
	public Menu getRoot(Menu entity);

	@FindListByTenant
	public List<Menu> findListByHref(Menu entity);

	@FindListByTenant
	public List<Menu> findLlistByLver(Menu entity);

	/**
	 * 根据ID获取菜单，不考虑删除状态.
	 * @param id 唯一标识
	 * @return Menu
	 */
	@FindListByTenant
	public Menu getById(Menu entity);

	/**
	 * 根据ID获取菜单，不考虑删除状态.
	 * @param rid 唯一标识
	 * @return Menu
	 */
	@FindListByTenant
	public Menu getByRid(Menu entity);

	@FindListByTenant
	Menu getByLtype(Menu entity);

	@FindListByTenant
	Integer getParentLtypeById(String id);

	Menu getMenuByHref(@Param("href") String href);

	void updateIsShow(Menu menu);

	@FindListByTenant
	List<Menu> findListByTenant(Menu entity);
	List<Menu> findListByRtenant(@Param("rids") List<String> rids, @Param("menu")  Menu menu);

	/**
	 * 删除所有子集菜单
	 * @param entity User
	 * @return
	 */
	public void deleteWLByTenant(Menu entity);

	/**
	 * 删除所有子集菜单
	 * @param entity Menu
	 * @return
	 */
	public void deleteWLByPtenant(Menu entity);

	/**
	 * 删除所有子集菜单
	 *不含当前菜单.
	 * @param entity Menu
     */
	public void deleteWLByPidTenant(Menu entity);

	@FindListByTenant
	public Menu findById(String id);

	@FindListByTenant
	public List<Menu> findListByIds(Menu entity);



//	@FindListByTenant
//	@Override
//	public Menu get(Menu menu);
}
