package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwRenewalRuleDao;
import com.oseasy.pw.modules.pw.entity.PwRenewalRule;
import com.oseasy.pw.modules.pw.vo.SvalPw;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 续期配置Service.
 * @author zy
 * @version 2018-01-04
 */
@Service
@Transactional(readOnly = true)
public class PwRenewalRuleService extends CrudService<PwRenewalRuleDao, PwRenewalRule> {

	public PwRenewalRule get(String id) {
		return super.get(id);
	}

	public List<PwRenewalRule> findList(PwRenewalRule pwRenewalRule) {
		return super.findList(pwRenewalRule);
	}

	public Page<PwRenewalRule> findPage(Page<PwRenewalRule> page, PwRenewalRule pwRenewalRule) {
		return super.findPage(page, pwRenewalRule);
	}

	@Transactional(readOnly = false)
	public void save(PwRenewalRule pwRenewalRule) {
	  if(StringUtil.isNotEmpty(pwRenewalRule.getId())){
	    SvalPw.removeCache(pwRenewalRule.getId());
	  }
		super.save(pwRenewalRule);
		SvalPw.putCache(null, pwRenewalRule);
	}

	@Transactional(readOnly = false)
	public void delete(PwRenewalRule pwRenewalRule) {
	  if(StringUtil.isNotEmpty(pwRenewalRule.getId())){
      SvalPw.removeCache(pwRenewalRule.getId());
    }
		super.delete(pwRenewalRule);
	}

	@Transactional(readOnly = false)
	public void deleteWL(PwRenewalRule pwRenewalRule) {
	  if(StringUtil.isNotEmpty(pwRenewalRule.getId())){
      SvalPw.removeCache(pwRenewalRule.getId());
    }
	  dao.deleteWL(pwRenewalRule);
	}

	public PwRenewalRule getPwRenewalRule() {
		return dao.getPwRenewalRule();
	}
}