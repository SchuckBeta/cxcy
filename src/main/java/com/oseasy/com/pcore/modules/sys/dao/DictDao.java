/**
 *
 */
package com.oseasy.com.pcore.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindDictByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;

/**
 * 字典DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {
	@Override
	@FindDictByTenant
	public Dict get(String id);
	@FindDictByTenant
	@Override
	public int insert(Dict entity);

	@Override
	@FindDictByTenant
	public int update(Dict entity);
	@Override
	@FindDictByTenant
	public List<Dict> findList(Dict entity);
	@Override
	@FindDictByTenant
	public List<Dict> findAllList(Dict entity);
	@FindDictByTenant
	public List<Map<String, String>> getDictListPlus(Map<String,Object> param);
	@FindDictByTenant
	public int getDictListPlusCount(Map<String,Object> param);
	@FindDictByTenant
	public List<Map<String, String>> getDictTypeListPlus();
	@FindDictByTenant
	public List<String> findTypeList(Dict dict);
	@FindDictByTenant
	public List<Dict> getAllData();
	@FindDictByTenant
	public int getDictTypeCountByCdn(Map<String,String> param);
	@FindDictByTenant
	public int getDictCountByCdn(Map<String,String> param);

	public void delDictType(String id);
	@FindDictByTenant
	public Dict getByValue(String type);
	@FindDictByTenant
	public Dict getByValue(Dict dict);
	void edtBackDoorDict(Dict dict);
	public void delDictByTenantId(Dict dict);

	public void runBySqlString(Dict dict);
}
