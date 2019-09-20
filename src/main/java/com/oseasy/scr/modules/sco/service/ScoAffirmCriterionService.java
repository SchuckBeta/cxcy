package com.oseasy.scr.modules.sco.service;

import java.util.List;

import com.oseasy.scr.modules.sco.dao.ScoAffirmCriterionDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirmCriterion;
import com.oseasy.scr.modules.sco.vo.ScoAffrimCriterionVo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 学分认定标准Service.
 * @author 9527
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoAffirmCriterionService extends CrudService<ScoAffirmCriterionDao, ScoAffirmCriterion> {
	public List<ScoAffirmCriterion> findListByConfid(String confid) {
		return dao.findListByConfid(confid);
	}

	public ScoAffirmCriterion get(String id) {
		return super.get(id);
	}

	public List<ScoAffirmCriterion> findList(ScoAffirmCriterion scoAffirmCriterion) {
		return super.findList(scoAffirmCriterion);
	}

	public Page<ScoAffirmCriterion> findPage(Page<ScoAffirmCriterion> page, ScoAffirmCriterion scoAffirmCriterion) {
		return super.findPage(page, scoAffirmCriterion);
	}

	@Transactional(readOnly = false)
	public void save(ScoAffirmCriterion scoAffirmCriterion) {
		dao.delByConfid(scoAffirmCriterion.getAffirmConfId());
		JSONArray js=JSONArray.fromObject(scoAffirmCriterion.getDataJson());
		for(int i=0;i<js.size();i++) {
			JSONObject jo=js.getJSONObject(i);
			ScoAffirmCriterion sac=new ScoAffirmCriterion();
			sac.setAffirmConfId(scoAffirmCriterion.getAffirmConfId());
			sac.setCategory(jo.getString("category"));
			sac.setResult(jo.getString("result"));
			sac.setScore(jo.getString("score"));
			super.save(sac);
		}
	}

	@Transactional(readOnly = false)
	public void delete(ScoAffirmCriterion scoAffirmCriterion) {
		super.delete(scoAffirmCriterion);
	}

	public ScoAffrimCriterionVo findCriter(ScoAffrimCriterionVo scoAffrimCriterionVo) {
		return dao.findCriter(scoAffrimCriterionVo);
	}

}