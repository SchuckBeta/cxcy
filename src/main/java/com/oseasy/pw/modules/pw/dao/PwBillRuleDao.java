package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwBillRule;

/**
 * 费用规则DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwBillRuleDao extends CrudDao<PwBillRule> {

}