package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrCardRuleDao;
import com.oseasy.dr.modules.dr.entity.DrCardRule;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁预警Service.
 * @author zy
 * @version 2018-04-13
 */
@Service
@Transactional(readOnly = true)
public class DrCardRuleService extends CrudService<DrCardRuleDao, DrCardRule> {

	public DrCardRule get(String id) {
		return super.get(id);
	}

	public List<DrCardRule> findList(DrCardRule drCardRule) {
		return super.findList(drCardRule);
	}

	public Page<DrCardRule> findPage(Page<DrCardRule> page, DrCardRule drCardRule) {
		return super.findPage(page, drCardRule);
	}

	@Transactional(readOnly = false)
	public void save(DrCardRule drCardRule) {
		super.save(drCardRule);
	}

	@Transactional(readOnly = false)
	public void delete(DrCardRule drCardRule) {
		super.delete(drCardRule);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardRule drCardRule) {
  	  dao.deleteWL(drCardRule);
  	}

	public DrCardRule getDrCardRule() {
		return dao.getDrCardRule();
	}

	@Transactional(readOnly = false)
	public void saveDrCardRule(DrCardRule drCardRule) {
		if(drCardRule.getIsWarm()!=null && drCardRule.getIsWarm().equals("on")){
			drCardRule.setIsWarm("1");
		}else{
			drCardRule.setIsWarm("0");
		}
		if(drCardRule.getIsEnter()!=null && drCardRule.getIsEnter().equals("on")){
			drCardRule.setIsEnter("1");
		}else{
			drCardRule.setIsEnter("0");
		}
		if(drCardRule.getIsOut()!=null && drCardRule.getIsOut().equals("on")){
			drCardRule.setIsOut("1");
		}else{
			drCardRule.setIsOut("0");
		}
		save(drCardRule);
	}

    @Transactional(readOnly = false)
    public void saveDrCrule(DrCardRule drCardRule) {
        if(StringUtil.isEmpty(drCardRule.getIsEnter())){
            drCardRule.setIsEnter(Const.NO);
        }
        if(StringUtil.isEmpty(drCardRule.getIsOut())){
            drCardRule.setIsOut(Const.NO);
        }
        save(drCardRule);
    }
}