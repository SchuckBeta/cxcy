package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwCompany;

import java.util.List;

/**
 * 入驻企业DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwCompanyDao extends CrudDao<PwCompany> {

	PwCompany getByEid(String eid);

	List<PwCompany> findListByPwCompany(PwCompany pwCompany);
}