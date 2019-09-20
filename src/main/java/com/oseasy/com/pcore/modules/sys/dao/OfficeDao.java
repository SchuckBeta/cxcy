/**
 *
 */
package com.oseasy.com.pcore.modules.sys.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.TreeEntity;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;

/**
 * 机构DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	@Override
	@FindListByTenant
	public List<Office> findAllList(Office entity);
	@Override
	@FindListByTenant
	public List<Office> findList(Office entity);

//	@FindListByTenant
	public List<Office> findListByTenant(Office entity);
//	@FindListByTenant
	public List<Office> findAllListByTenant(Office entity);
	@Override
	@InsertByTenant
	public int insert(Office entity);
	@InsertByTenant
	public int insertPL(@Param("entitys") List<Office> entitys);

	/**
	 * 根据当前租户获取根机构.
	 * @return Menu
	 */
	@FindListByTenant
	public Office getRoot(Office entity);

	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
//	@FindListByTenant
	public List<Office> findByParentIds(Office entity);
	/**
	 * 删除所有子节点
	 * @param entity
	 * @return
	 */
	public void deleteWLByParentIds(Office entity);

	String selelctParentId(@Param("id") String id);

	List<Office> findProfessionByParentIdsLike(Office office);

	List<Office> findProfessionByParentIds(@Param("officeIds") String officeIds);

	@FindListByTenant
	List<Office> findColleges();

	List<Office> findProfessionals(String parentId);
	Office getSchool();
	Office getOrgByName(@Param("oname") String oname);
	Office getOfficeByName(@Param("oname") String oname);
	Office getProfessionalByName(@Param("oname") String oname,@Param("pname") String pname);
	Office getProfessionalByPName(@Param("pname") String pname);
	int checkNameByParent(@Param("parentId") String parentId, @Param("name") String name);

	void updateSpace(Office office);

	int checkByNameAndId(@Param("id") String id, @Param("parentId") String parentId,  @Param("name")String name);

	@FindListByTenant
	String selectOfficeIdByParentId(@Param("parentId") String parentId);
}
