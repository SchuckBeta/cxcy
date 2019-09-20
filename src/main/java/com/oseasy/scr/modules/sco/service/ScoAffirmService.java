package com.oseasy.scr.modules.sco.service;

import java.util.List;

import com.oseasy.scr.modules.sco.dao.ScoAffirmDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirm;
import com.oseasy.scr.modules.sco.vo.ScoCourseVo;
import com.oseasy.scr.modules.sco.vo.ScoProjectVo;
import com.oseasy.scr.modules.sco.vo.ScoRatioVo;
import com.oseasy.scr.modules.sco.vo.ScoTeamRatioVo;
import com.oseasy.util.common.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 创新、创业、素质学分认定表Service.
 * @author chenhao
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoAffirmService extends CrudService<ScoAffirmDao, ScoAffirm> {
	@Autowired
	ScoAllotRatioService scoAllotRatioService;

	public ScoAffirm get(String id) {
		return super.get(id);
	}

	public List<ScoAffirm> findList(ScoAffirm scoAffirm) {
		return super.findList(scoAffirm);
	}

	public Page<ScoAffirm> findPage(Page<ScoAffirm> page, ScoAffirm scoAffirm) {
		return super.findPage(page, scoAffirm);
	}

	@Transactional(readOnly = false)
	public void save(ScoAffirm scoAffirm) {
		super.save(scoAffirm);
	}

	@Transactional(readOnly = false)
	public void delete(ScoAffirm scoAffirm) {
		super.delete(scoAffirm);
	}

	public Page<ScoProjectVo> findInnovationPage(Page<ScoProjectVo> page, ScoProjectVo scoProjectVo) {
		scoProjectVo.setPage(page);
		page.setList(dao.findInnovationList(scoProjectVo));
		return page;
	}

	public Page<ScoProjectVo> findBusinessPage(Page<ScoProjectVo> page, ScoProjectVo scoProjectVo) {
		scoProjectVo.setPage(page);
		page.setList(dao.findBusinessList(scoProjectVo));
		return page;
	}

	public Page<ScoProjectVo> findQualityList(Page<ScoProjectVo> page, ScoProjectVo scoProjectVo) {
		scoProjectVo.setPage(page);
		page.setList(dao.findQualityList(scoProjectVo));
		return page;
	}

//	public ScoAffirm findProjectScore(ScoAffirm scoAffirm) {
//		return dao.findProjectScore(scoAffirm);
//	}
//	//后台查询素质学分列表
//	public Page<ScoProjectVo> findScoGontestVoPage(Page<ScoProjectVo> page, ScoProjectVo scoProjectVo) {
//
//		scoProjectVo.setPage(page);
//		List<ScoProjectVo> scoProjectVoList=dao.findScoGontestVoList(scoProjectVo);
//		if (scoProjectVoList.size()>0) {
//			for(int i=0;i<scoProjectVoList.size();i++) {
//				ScoProjectVo scoProjectVoindex=scoProjectVoList.get(i);
//				if (scoProjectVoindex!=null) {
//					if (scoProjectVoindex.getUserNum()!=null) {
//						String ratioResult=findGcontestRatio(Integer.valueOf(scoProjectVoindex.getUserNum()));
//						scoProjectVoindex.setRatioResult(ratioResult);
//					}
//					//todo  学分配比
//					else{
//
//					}
//				}
//			}
//		}
//		page.setList(scoProjectVoList);
//		return page;
//	}
//	//后台查询创业学分列表
//	public Page<ScoProjectVo> findScoProjectCreateVoPage(Page<com.oseasy.scr.modules.sco.vo.ScoProjectVo> page, com.oseasy.scr.modules.sco.vo.ScoProjectVo scoProjectVo) {
//		scoProjectVo.setPage(page);
//		List<ScoProjectVo> scoProjectVoList=dao.findScoProjectCreateVoPage(scoProjectVo);
//		if (scoProjectVoList.size()>0) {
//			for(int i=0;i<scoProjectVoList.size();i++) {
//				ScoProjectVo scoProjectVoindex=scoProjectVoList.get(i);
//				if (scoProjectVoindex!=null) {
//					if (scoProjectVoindex.getUserNum()!=null) {
//						String ratioResult = findRatio(scoProjectVoindex.getProjectDeclare().getType(), Integer.valueOf(scoProjectVoindex.getUserNum()));
//						scoProjectVoindex.setRatioResult(ratioResult);
//					}
//				}
//			}
//		}
//		page.setList(scoProjectVoList);
//
//		return page;
//	}
//	//后台查询创业学分列表
//	public Page<ScoProjectVo> findScoProjectStartVoPage(Page<com.oseasy.scr.modules.sco.vo.ScoProjectVo> page, com.oseasy.scr.modules.sco.vo.ScoProjectVo scoProjectVo) {
//
//		scoProjectVo.setPage(page);
//		List<ScoProjectVo> scoProjectVoList=dao.findScoProjectStartVoPage(scoProjectVo);
//		if (scoProjectVoList.size()>0) {
//			for(int i=0;i<scoProjectVoList.size();i++) {
//				ScoProjectVo scoProjectVoindex=scoProjectVoList.get(i);
//				if (scoProjectVoindex.getUserNum()!=null) {
//					String ratioResult = findRatio(scoProjectVoindex.getProjectDeclare().getType(), Integer.valueOf(scoProjectVoindex.getUserNum()));
//					scoProjectVoindex.setRatioResult(ratioResult);
//				}
//				//todo  学分配比
//				else {}
//			}
//		}
//		page.setList(scoProjectVoList);
//		return page;
//	}

	public String findRatio(String type,int snumber ) {
		ScoRatioVo scoRatioVo = new ScoRatioVo();
		if (StringUtil.equals(type,"1")||StringUtil.equals(type,"2")) { //创新训练、创业训练
			scoRatioVo.setType("0000000123"); //设置查询的学分类型（创新学分）
		}
		if (StringUtil.equals(type,"3")) { //创业实践
			scoRatioVo.setType("0000000124"); //设置查询的学分类型（创业学分）
		}
		scoRatioVo.setItem("0000000128"); //双创项目
		scoRatioVo.setCategory("1"); //大学生创新创业训练项目
		scoRatioVo.setSubdivision(type);
		scoRatioVo.setNumber(snumber);
		ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
		if (ratioResult!=null) {
			return ratioResult.getRatio();
		}else{
			return "";
		}
	}

	public String findGcontestRatio(int snumber ) {
		ScoRatioVo scoRatioVo = new ScoRatioVo();
		scoRatioVo.setType("0000000125"); //设置查询的学分类型（素质学分）
		scoRatioVo.setItem("0000000129"); //双创大赛
		scoRatioVo.setCategory("1"); //互联网+大赛
		scoRatioVo.setNumber(snumber);
		ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
		if (ratioResult!=null) {
			return ratioResult.getRatio();
		}else{
			return "";
		}
	}

	public List<ScoProjectVo> getScoGradeQuality(String id) {
		List<ScoProjectVo> proVo=dao.getScoGradeQuality(id);
		if (proVo.size()>0 && proVo.get(0).getWeightTotal()==0) {
			for(ScoProjectVo scoProjectVo:proVo ) {
				scoProjectVo.setWeightTotal(1);
				scoProjectVo.setWeightVal(1);
			}
		}
		return proVo;
		//return dao.getScoGradeQuality(id);
	}


	public  List<ScoProjectVo>  getScoGradeCreate(String id) {
		List<ScoProjectVo> proVo=dao.getScoGradeProject(id);
		if (proVo.size()>0 && proVo.get(0).getWeightTotal()==0) {
			for(ScoProjectVo scoProjectVo:proVo ) {
				scoProjectVo.setWeightTotal(1);
				scoProjectVo.setWeightVal(1);
			}
		}
		return proVo;
	}


	public  List<ScoProjectVo>  getScoGradeStart(String id) {
		List<ScoProjectVo> proVo=dao.getScoGradeProject(id);
		if (proVo.size()>0 && proVo.get(0).getWeightTotal()==0) {
			for(ScoProjectVo scoProjectVo:proVo ) {
				scoProjectVo.setWeightTotal(1);
				scoProjectVo.setWeightVal(1);
			}
		}
		return proVo;
	}
}