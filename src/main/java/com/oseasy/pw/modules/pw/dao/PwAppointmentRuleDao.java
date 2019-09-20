package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwAppointmentRule;
import java.util.List;
/**
 * 预约规则DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwAppointmentRuleDao extends CrudDao<PwAppointmentRule> {
	@FindListByTenant
	PwAppointmentRule getPwAppointmentRule();

	@Override
    @FindListByTenant
    public List<PwAppointmentRule> findList(PwAppointmentRule entity);


	@Override
	@InsertByTenant
	public int insert(PwAppointmentRule entity);
}