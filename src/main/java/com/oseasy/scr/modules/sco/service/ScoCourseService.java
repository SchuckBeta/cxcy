package com.oseasy.scr.modules.sco.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.scr.modules.sco.dao.ScoAffirmCriterionCouseDao;
import com.oseasy.scr.modules.sco.dao.ScoCourseDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirmCriterionCouse;
import com.oseasy.scr.modules.sco.entity.ScoCourse;
import com.oseasy.scr.modules.sco.vo.ScoCourseVo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分课程Service.
 * @author 张正
 * @version 2017-07-13
 */
@Service
@Transactional(readOnly = true)
public class ScoCourseService extends CrudService<ScoCourseDao, ScoCourse> {
	@Autowired
	private ScoAffirmCriterionCouseDao scoAffirmCriterionCouseDao;
	@Autowired
	private ScoAffirmCriterionCouseService scoAffirmCriterionCouseService;
	
	public int checkName(String id,String name) {
		return dao.checkName(id,name);
	}
	public int checkCode(String id,String code) {
		return dao.checkCode(id,code);
	}
	public ScoCourse get(String id) {
		return super.get(id);
	}

	public List<ScoCourse> findList(ScoCourse scoCourse) {
		return super.findList(scoCourse);
	}

	public Page<ScoCourse> findPage(Page<ScoCourse> page, ScoCourse scoCourse) {
		return super.findPage(page, scoCourse);
	}

	@Transactional(readOnly = false)
	public void save(ScoCourse scoCourse) {
		boolean isNew=true;
		if (StringUtil.isNotEmpty(scoCourse.getId())) {
			isNew=false;
		}
		super.save(scoCourse);
		if (isNew) {
			ScoAffirmCriterionCouse sacc1=new ScoAffirmCriterionCouse();
			sacc1.setForeignId(scoCourse.getId());
			sacc1.setParentId("0");
			sacc1.setStart(scoCourse.getPlanTime());
			sacc1.setEnd(scoCourse.getPlanTime());
			sacc1.setSort("0");
			scoAffirmCriterionCouseService.saveDefault(sacc1);
			ScoAffirmCriterionCouse sacc2=new ScoAffirmCriterionCouse();
			sacc2.setForeignId(scoCourse.getId());
			sacc2.setParentId(sacc1.getId());
			sacc2.setStart(scoCourse.getOverScore());
			sacc2.setEnd("100");
			sacc2.setScore(scoCourse.getPlanScore());
			sacc2.setSort("0");
			scoAffirmCriterionCouseService.saveDefault(sacc2);
		}
	}

	@Transactional(readOnly = false)
	public void delete(ScoCourse scoCourse) {
		super.delete(scoCourse);
		scoAffirmCriterionCouseDao.delByFid(scoCourse.getId());
	}

	//根据课程名或者课程代码查询学分课程
	public List<ScoCourse> findListByNameOrCode(String keyword) {
		return dao.findListByNameOrCode(keyword);
	}

	public List<ScoCourse> findCourseList(ScoCourse scoCourse) {
		return dao.findCourseList(scoCourse);
	}

	public Page<ScoCourseVo> findScoCourseVoPage(Page<ScoCourseVo> scoCourseVoPage, ScoCourseVo scoCourseVo) {

		scoCourseVo.setPage(scoCourseVoPage);
		scoCourseVoPage.setList(dao.findscoCourseVoList(scoCourseVo));
		return scoCourseVoPage;
	}

}