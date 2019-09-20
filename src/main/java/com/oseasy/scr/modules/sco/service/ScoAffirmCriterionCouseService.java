package com.oseasy.scr.modules.sco.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.scr.modules.sco.dao.ScoAffirmCriterionCouseDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirmCriterionCouse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 课程学分认定标准Service.
 * @author 9527
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoAffirmCriterionCouseService extends CrudService<ScoAffirmCriterionCouseDao, ScoAffirmCriterionCouse> {
	public List<ScoAffirmCriterionCouse> findListByFid(String fid) {
		return dao.findListByFid(fid);
	}

	public List<ScoAffirmCriterionCouse> findListByFidCouseNum(String fid) {
		return dao.findListByFidCouseNum(fid);
	}

	public List<ScoAffirmCriterionCouse> findListByParentId(String parentId) {
		return dao.findListByParentId(parentId);
	}

	public ScoAffirmCriterionCouse get(String id) {
		return super.get(id);
	}

	public List<ScoAffirmCriterionCouse> findList(ScoAffirmCriterionCouse scoAffirmCriterionCouse) {
		return super.findList(scoAffirmCriterionCouse);
	}

	public Page<ScoAffirmCriterionCouse> findPage(Page<ScoAffirmCriterionCouse> page, ScoAffirmCriterionCouse scoAffirmCriterionCouse) {
		return super.findPage(page, scoAffirmCriterionCouse);
	}
	@Transactional(readOnly = false)
	public void saveDefault(ScoAffirmCriterionCouse scoAffirmCriterionCouse) {
		super.save(scoAffirmCriterionCouse);
	}
	@Transactional(readOnly = false)
	public void save(ScoAffirmCriterionCouse scoAffirmCriterionCouse) {
		dao.delByFid(scoAffirmCriterionCouse.getForeignId());
		JSONArray js=JSONArray.fromObject(scoAffirmCriterionCouse.getDataJson());
		for(int i=0;i<js.size();i++) {
			JSONObject jo=js.getJSONObject(i);
			JSONObject couse=jo.getJSONObject("couse");
			ScoAffirmCriterionCouse psac=new ScoAffirmCriterionCouse();
			psac.setForeignId(scoAffirmCriterionCouse.getForeignId());
			psac.setParentId("0");
			psac.setStart(couse.getString("start"));
			psac.setEnd(couse.getString("end"));
			psac.setSort(i+"");
			super.save(psac);
			JSONArray scores=jo.getJSONArray("scores");
			for(int j=0;j<scores.size();j++) {
				JSONObject score=scores.getJSONObject(j);
				ScoAffirmCriterionCouse sac=new ScoAffirmCriterionCouse();
				sac.setParentId(psac.getId());
				sac.setStart(score.getString("start"));
				sac.setEnd(score.getString("end"));
				sac.setScore(score.getString("score"));
				sac.setForeignId(scoAffirmCriterionCouse.getForeignId());
				sac.setSort(j+"");
				super.save(sac);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(ScoAffirmCriterionCouse scoAffirmCriterionCouse) {
		super.delete(scoAffirmCriterionCouse);
	}

}