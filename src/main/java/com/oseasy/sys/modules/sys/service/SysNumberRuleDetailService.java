package com.oseasy.sys.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.sys.modules.sys.dao.SysNumberRuleDetailDao;
import com.oseasy.sys.modules.sys.entity.SysNumberRuleDetail;

/**
 * 编号规则详情表Service.
 * @author 李志超
 * @version 2018-05-17
 */
@Service
@Transactional(readOnly = true)
public class SysNumberRuleDetailService extends CrudService<SysNumberRuleDetailDao, SysNumberRuleDetail> {

	public SysNumberRuleDetail get(String id) {
		return super.get(id);
	}

	public List<SysNumberRuleDetail> findList(SysNumberRuleDetail sysNumberRuleDetail) {
		return super.findList(sysNumberRuleDetail);
	}

	public Page<SysNumberRuleDetail> findPage(Page<SysNumberRuleDetail> page, SysNumberRuleDetail sysNumberRuleDetail) {
		return super.findPage(page, sysNumberRuleDetail);
	}

	@Transactional(readOnly = false)
	public void batchSave(List<SysNumberRuleDetail> sysNumberRuleDetailList) {
		dao.batchSave(sysNumberRuleDetailList);
	}

	@Transactional(readOnly = false)
	public void save(SysNumberRuleDetail sysNumberRuleDetail) {
		super.save(sysNumberRuleDetail);
	}

	@Transactional(readOnly = false)
	public void delete(SysNumberRuleDetail sysNumberRuleDetail) {
		super.delete(sysNumberRuleDetail);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysNumberRuleDetail sysNumberRuleDetail) {
  	  dao.deleteWL(sysNumberRuleDetail);
  	}

	@Transactional(readOnly = false)
	public void deleteByRuleId(String ruleId) {
		dao.deleteByRuleId(ruleId);
	}

	public List<SysNumberRuleDetail> findSysNumberRuleDetailList(String ruleId) {
		return dao.findSysNumberRuleDetailList(ruleId);
	}

	public SysNumberRuleDetail getBySysNumberRule(String sysNumberRuleId, String ruleType) {
		return dao.getBySysNumberRule(sysNumberRuleId,ruleType);
	}
}