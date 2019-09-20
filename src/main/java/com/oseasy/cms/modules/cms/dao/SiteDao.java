/**
 * 
 */
package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 站点DAO接口

 * @version 2013-8-23
 */
@MyBatisDao
public interface SiteDao extends CrudDao<Site> {

	@Override
	@FindListByTenant
	public List<Site> findList(Site entity);
	@Override
	@InsertByTenant
	public int insert(Site entity);

	String getLastId();

	int getFirstSite();

	void siteChange(Site site);

	void changeAutoSite(String id);

	@FindListByTenant
	Site getAutoSite(Site site);

	String getInsertId();
	Site getAutoSite();
}
