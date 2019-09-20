package com.oseasy.scr.modules.scr.service;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.modules.scr.dao.ScoRsumDao;
import com.oseasy.scr.modules.scr.dao.ScoRuleDao;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyPb;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;
import com.oseasy.scr.modules.scr.entity.ScoRset;
import com.oseasy.scr.modules.scr.entity.ScoRsum;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.vo.ScoAuditVo;
import com.oseasy.scr.modules.scr.vo.ScoMemberVo;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分汇总Service.
 * @author chenh
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class ScoRsumService extends CrudService<ScoRsumDao, ScoRsum> {
	@Autowired
	private ScoRuleDao scoRuleDao;
	@Autowired
	private ScoRsetService entityService;
	@Autowired
	private ScoRapplyPbService scoRapplyPbService;
	@Autowired
	private ScoRapplyRecordService scoRapplyRecordService;
	@Autowired
	private ScoRapplyService scoRapplyService;

	public ScoRsum get(String id) {
		return super.get(id);
	}

	public List<ScoRsum> findList(ScoRsum entity) {
		return super.findList(entity);
	}

    public List<ScoRsum> findListByAppId(ScoRsum scoRsum) {
        return dao.findListByAppId(scoRsum);
    }

    public List<ScoRsum> findListByAppIds(ScoRsum scoRsum) {
        return dao.findListByAppIds(scoRsum);
    }

	public List<ScoRsum> updateValList(ScoRsum entity){
		return dao.updateValList(entity);
	}

	public Page<ScoRsum> findPage(Page<ScoRsum> page, ScoRsum entity) {
		return super.findPage(page, entity);
	}

	public Page findScoRsumPage(Page page, ScoRsum entity) {
		entity.setPage(page);
		ScoRule scoRule = new ScoRule();
		scoRule.setType("2");
		List<ScoRule> list = Lists.newArrayList();
		ScoRule r = new ScoRule();
		r.setName("创新创业课程");
		list.add(r);
		list.addAll(scoRuleDao.scoRuleSingleList(scoRule));
		entity.setEntitys(list);
		List<Map<String,Double>> map = dao.findScoSumList(entity);
		for(Map<String,Double> myMapTmp : map){
			//个人总分超过最大分数限制时，只显示最大分数限制的值
			List<ScoRset> scoRsetList = entityService.findList(new ScoRset());
			if(scoRsetList.size() > 0 && scoRsetList != null){
				ScoRset scoRset = scoRsetList.get(0);
				if(scoRset.getSnumlimit() != null && myMapTmp.get("总分") >= scoRset.getSnumlimit()){
					myMapTmp.put("总分",Double.valueOf(scoRset.getSnumlimit().toString()));
				}
			}

			Map<String, Object> myMap = new LinkedHashMap<String, Object>();
			List<String> keyList = Lists.newArrayList();
			Iterator<String> it =myMapTmp.keySet().iterator();
			while(it.hasNext()){
				keyList.add(it.next());
			}
			Collections.sort(keyList);
			Iterator<String> it2 = keyList.iterator();
			while(it2.hasNext()){
				String key = it2.next();
				myMap.put(key, myMapTmp.get(key));
			}
		}

		page.setList(map);
		return page;
	}
	public List<Map<String,Double>> findScoSumList(ScoRsum scoRsum){
		ScoRule scoRule = new ScoRule();
		scoRule.setType("2");
		List<ScoRule> list = Lists.newArrayList();
		ScoRule r = new ScoRule();
		r.setName("创新创业课程");
		list.add(r);
		list.addAll(scoRuleDao.scoRuleSingleList(scoRule));
		scoRsum.setEntitys(list);
		return dao.findScoSumList(scoRsum);
	}

	@Transactional(readOnly = false)
	public void save(ScoRsum entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}
	public ScoRsum findRdetailPersonalSum(ScoRapplyValid scoRsum){
		return  dao.findRdetailPersonalSum(scoRsum);
	}
	@Transactional(readOnly = false)
	public void updateVal(ScoRsum entity){
		dao.updateVal(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRsum> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRsum> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRsum entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRsum entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRsum entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRsum entity) {
  	  dao.deleteWLPL(entity);
  	}
  	@Transactional(readOnly = false)
  	public void deleteWLByAppId(ScoRsum entity) {
  	    dao.deleteWLByAppId(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	@Transactional(readOnly = false)
	public void deleteByAppId(ScoQuery scoQuery){
		ScoRapply scoRapply = new ScoRapply(scoQuery.getId());
		ScoRsum scoRsum = new ScoRsum();
		scoRsum.setApply(scoRapply);
		dao.deleteByAppId(scoRsum);
	}

    @Transactional(readOnly = false)
    public void deleteWLByAppId(String appId) {
        ScoRsum scoRsum = new ScoRsum();
        scoRsum.setApply(new ScoRapply(appId));
        dao.deleteWLByAppId(scoRsum);
    }

	@Transactional(readOnly = false)
	public void ajaxupdateVal(ScoRsum entity) {
		//获取申请对象.
		ScoRapply scoRapply = scoRapplyService.get(entity.getScoRsumList().get(0).getApply().getId());
		Double score = 0.0;
		List<ScoMemberVo> scoMemberVoList = Lists.newArrayList();
		for(ScoRsum scoRsum : entity.getScoRsumList()){
			ScoRapplyPb scoRapplyPb = new ScoRapplyPb();
			scoRapplyPb.setApply(scoRsum.getApply());
			scoRapplyPb.setRdetail(scoRapply.getRdetail());
			scoRapplyPb.setUser(scoRsum.getUser());
			List<ScoRapplyPb> scoRapplyPbs = scoRapplyPbService.findList(scoRapplyPb);
			ScoMemberVo scoMemberVo = new ScoMemberVo();
			scoMemberVo.setUid(scoRsum.getUser().getId());
			scoMemberVo.setScore(scoRsum.getVal());
			if(StringUtil.checkNotEmpty(scoRapplyPbs)){
				scoMemberVo.setRate(scoRapplyPbs.get(0).getVal());
			}else{
				scoMemberVo.setRate(1);
			}
			scoMemberVoList.add(scoMemberVo);
			score += scoRsum.getVal();
		}
		ScoAuditVo scoaVo = new ScoAuditVo();
		scoaVo.setId(scoRapply.getId());
		scoaVo.setIgnodeId(scoRapply.getIgnodeId());
		scoaVo.setIsHalf(Const.NO);
		scoaVo.setAtype(CoreSval.PassNot.PASS.getKey());
		scoaVo.setScore(score);//个人，取传过来的，团队，不变
		scoaVo.setMembers(scoMemberVoList);
		scoaVo.setAppIds(scoRapply.getAppIds());
		//更改申请状态.
		if((CoreSval.PassNot.PASS.getKey()).equals(scoaVo.getAtype())){
			scoRapply.setStatus(ScoRstatus.SRS_PASS.getKey());
		}else if((CoreSval.PassNot.NOT.getKey()).equals(scoaVo.getAtype())){
			scoRapply.setStatus(ScoRstatus.SRS_NOPASS.getKey());
		}else{
			scoRapply.setStatus(ScoRstatus.SRS_DSH.getKey());
		}
		//计算最终得分情况
		scoaVo = ScoAuditVo.countScore(scoaVo, scoRapply);
		//保存、更新数据
		scoRapply.setAutDate(DateUtil.newDate());
		scoRapply.setAutBy(UserUtils.getUser());
		scoRapplyService.save(scoRapply);
		scoRapply.setTval(scoaVo.getScore());
		if((CoreSval.PassNot.PASS.getKey()).equals(scoaVo.getAtype())){
			scoRapply.setTcval(scoaVo.getTcval());
		}
		scoRapply.setAppIds(scoaVo.getAppIds());
		scoRapplyService.save(scoRapply);
		scoRapplyRecordService.save(ScoRapplyRecord.convert(scoRapply, scoaVo.getRemarks()));
		deleteWLByAppId(new ScoRsum(scoRapply));

			/**
			 * 是否需要处理相同项目(只支持单个项目).
			 * 规则：相同项目，取最高分，最高分相等，不处理
			 */
			boolean needDpro = false;
			boolean needDealPro = false;
			List<ScoRsum> odscoRsums = Lists.newArrayList();
			if(StringUtil.isNotEmpty(scoaVo.getAppId())){
				ScoRapply srapply = scoRapplyService.getGtRapply(scoRapply, odscoRsums, scoaVo);
				//判断总分数是否大于旧的
				if(srapply != null){
					if((Const.YES).equals(srapply.getIsFirst())){
						scoRapplyService.save(srapply);
					}

					if((scoRapply.getTcval() > srapply.getTcval())){
						/**
						 * 如果当前申请大于历史申请，需要处理历史数据标记为isPro=1,新增的总分isPro=0;
						 * 如果当前申请小于历史申请，需要处理历史数据标记为isPro=0,新增的总分isPro=1;
						 */
						needDpro = false;
						needDealPro = true;
					}else if((scoRapply.getTcval() == srapply.getTcval())){
						/**
						 * 如果当前申请等于历史申请,且申请ID一致是，需要处理历史数据标记为isPro=1,新增的总分isPro=0;
						 */
						if((scoRapply.getId()).equals(srapply.getId())){
							needDpro = false;
							needDealPro = true;
						}else{
							needDpro = true;
							needDealPro = false;
						}
					}else{
						//needDpro = true;
						//needDealPro = false;
						needDpro = true;
						needDealPro = false;
					}
				}

				//总分表无数据不做处理
				if(StringUtil.checkEmpty(odscoRsums)){
					//needDealPro = false;
				}

				if(needDealPro){
					for (ScoRsum scoRsum : odscoRsums) {
						scoRsum.setIsSpro(Const.YES);
					}
					updatePL(odscoRsums);
				}else{
					for (ScoRsum scoRsum : odscoRsums) {
						if(scoRsum.getApply().getId().equals(srapply.getId())){
							scoRsum.setIsSpro(Const.NO);
						}
					}
					updatePL(odscoRsums);
				}
			}

			deleteWLByAppId(scoRapply.getId());
			if(StringUtil.checkNotEmpty(scoaVo.getMembers())){
				scoRapplyPbService.deleteWLByApplyId(scoRapply.getId());
				List<ScoRsum> scoRsums = ScoRsum.convert(scoaVo, scoRapply, needDpro);
				insertPL(scoRsums);
				scoRapplyPbService.insertPL(ScoRapplyPb.convert(scoRsums, scoaVo, scoRapply));
			}else{
				save(ScoRsum.convert(scoRapply, scoRapply.getUser(), scoaVo.getScore(), needDpro));
			}

	}

}