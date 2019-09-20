package com.oseasy.scr.modules.scr.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.scr.modules.scr.dao.ScoRuleDao;
import com.oseasy.scr.modules.scr.dao.ScoRuleDetailDao;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetail;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetailMould;
import com.oseasy.scr.modules.scr.entity.ScoRulePb;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分规则Service.
 * @author chenh
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class ScoRuleService extends TreeService<ScoRuleDao, ScoRule> {

	@Autowired
	private ScoRuleDetailDao scoRuleDetailDao;
	@Autowired
	private ScoRuleDetailService scoRuleDetailService;
	@Autowired
	private ScoRulePbService scoRulePbService;
	@Autowired
	private ScoRapplyService scoRapplyService;


	public ScoRule get(String id) {
		return super.get(id);
	}

	public List<ScoRule> findList(ScoRule entity) {
		return super.findList(entity);
	}

	public List<ScoRule> findByParentIds(ScoRule entity) {
		return dao.findByParentIds(entity);
	}

	public List<ScoRule> findByParentIdsLike(ScoRule entity) {
		return dao.findByParentIdsLike(entity);
	}

	public Page<ScoRule> findPage(Page<ScoRule> page, ScoRule entity) {
		return super.findPage(page, entity);
	}
	public ScoRule findScoRule(ScoRule entity){
		return dao.findScoRule(entity);
	}

	public List<ScoRule> findTreeList(ScoRule entity){
		List<ScoRule> ss =dao.scoRuleSingleList(entity);
		return buildTee(ss,entity);
	}
	//构建树
	public List<ScoRule> buildTee(List<ScoRule> scoRuleList,ScoRule entity){
		List<ScoRule> list = Lists.newArrayList();
		for(ScoRule scoRule : scoRuleList){
			if(CoreSval.getScoreRootId().equals(scoRule.getId())){
				list.add(scoRule);
			}
			for(ScoRule scoR : scoRuleList){
				if(scoR.getParent().getId().equals(scoRule.getId())){
					if(scoRule.getChildren() == null){
						scoRule.setChildren(new ArrayList<Object>());
					}
					scoRule.getChildren().add(scoR);
					//排序正序
					//Collections.sort(scoRule.getChildren(), Comparator.comparing(ScoRule::getSort));
					sort(scoRule.getChildren(),"sort","asc");
				}else{
					//没有子类别时查找标准
					ScoRuleDetail scoRuleDetail = new ScoRuleDetail();
					scoRuleDetail.setRule(scoR);
					if(scoR.getPage()!=null){
						scoRuleDetail.getPage().setOrderBy(entity.getPage().getOrderBy());
						scoRuleDetail.getPage().setOrderByType(entity.getPage().getOrderByType());
					}
					List<ScoRuleDetail> scoRuleDetails = scoRuleDetailDao.findList(scoRuleDetail);
					if(null != scoRuleDetails && scoRuleDetails.size() >0){
						if(scoR.getChildren() == null){
							//排序
							Collections.sort(scoRuleDetails, Comparator.comparing(ScoRuleDetail::getSort));
							/*Collections.sort(scoRuleDetails, new Comparator<ScoRuleDetail>(){
								public int compare(ScoRuleDetail o1, ScoRuleDetail o2) {
									return Integer.valueOf(o1.getSort())-Integer.valueOf(o2.getSort());
								}});*/
							scoR.setChildren(scoRuleDetails);
						}
					}
				}
				}
			}
		return list;
	}

	/**
	 * list中不同实体 根据相同字段排序
	 * @param targetList 要排序的对象
	 * @param sortField 用哪个字段排序
	 * @param sortMode 倒序desc还是正序asc
	 */
	@SuppressWarnings("unchecked")
	public static void sort(List<Object> targetList, String sortField, String sortMode) {
		//使用集合的sort方法  ，并且自定义一个排序的比较器
		Collections.sort(targetList, new Comparator<Object>() {
			//匿名内部类，重写compare方法
			public int compare(Object obj1, Object obj2) {
				int result = 0;
				try {
					//首字母转大写
					String newStr = sortField.substring(0, 1).toUpperCase()+sortField.replaceFirst("\\w","");
					//获取需要排序字段的“get方法名”
					String methodStr = "get"+newStr;
					/**	API文档：：
					 *  getMethod(String name, Class<?>... parameterTypes)
					 *  返回一个 Method 对象，它反映此 Class 对象所表示的类或接口的指定公共成员方法。
					 */
					Method method1 = obj1.getClass().getMethod(methodStr,null);
					Method method2 = obj2.getClass().getMethod(methodStr, null);
					Object returnObj1 = method1.invoke((obj1), null);
					Object returnObj2 = method2.invoke((obj2), null);
					result = (Integer)returnObj1 - (Integer)returnObj2;
				} catch (Exception e) {
					throw new RuntimeException();
				}
				if ("desc".equals(sortMode)) {
					// 倒序
					result = -result;
				}
				return result;
			}
		});
	}


	@Transactional(readOnly = false)
	public void deleteScoRule(ScoRule entity){
		//删除自己
		delete(entity);
		ScoRuleDetail sc = new ScoRuleDetail();
		sc.setRule(entity);
		scoRuleDetailService.deleteByRid(sc);
		ScoRulePb scoRulePb = new ScoRulePb();
		scoRulePb.setRule(entity);
		scoRulePbService.deleteByRid(scoRulePb);
		ScoRule entitys = new ScoRule();
		entitys.setParentIds(entity.getId());
		List<ScoRule> list = findByParentIds(entitys);
		if(list != null && list.size() > 0){
			//删除子类
			deleteByParentIds(entity);
			for(ScoRule scoRule : list){
				//删除标准
				sc.setRule(scoRule);
				scoRuleDetailService.deleteByRid(sc);
				//删除配比
				scoRulePb.setRule(scoRule);
				scoRulePbService.deleteByRid(scoRulePb);
			}
		}
	}
	//判断当前选中的类别下有没有标准被认定
	public boolean scoRuleInRapply(ScoRule entity){
		List<ScoRapply> scoRapplyList = scoRapplyService.scoRuleInRapply(entity);
		if(scoRapplyList != null && scoRapplyList.size() >0){
			return true;
		}else{
			return  false;
		}
	}

	@Transactional(readOnly = false)
	public void save(ScoRule entity) {
		ScoRuleDetailMould sc = entity.getScoRuleDetailMould();
		if(null == sc.getLevel()){
			sc.setLevel(0);
		}
		if(null == sc.getIsLimitm()){
			sc.setIsLimitm(Const.NO);
		}
		if(null == sc.getIsHalf()){
			sc.setIsHalf(Const.NO);
		}
		if(null == sc.getHalfRemarks()){
			sc.setHalfRemarks("");
		}
		if(null == sc.getCondType()){
			sc.setCondType(Const.NO);
		}
		if(null == sc.getJoinType() || StringUtil.isEmpty(sc.getJoinType())){
			sc.setJoinType(Const.NO);
		}
		if(StringUtil.isEmpty(sc.getJoinMax())){
			sc.setJoinMax(null);
		}
		if(null == sc.getIsLowSco() || sc.getIsLowSco().equals(Const.NO)){
			sc.setIsLowSco(Const.NO);
			sc.setLowSco(null);
			sc.setLowScoMax(null);
		}
		if(null == sc.getIsJoin() || sc.getIsJoin().equals(Const.NO)){
			sc.setIsJoin(Const.NO);
		}
		if(entity.getId()!= null && StringUtil.isNotEmpty(entity.getId())){
			//修改标准模板
			dao.updateScoRuleDetailMould(entity.getScoRuleDetailMould());
			ScoRuleDetail scoRuleDetail = new ScoRuleDetail();
			scoRuleDetail.setRule(entity);
			//修改该类别下的标准计算分值规则
			scoRuleDetailService.updateMaxOrSumByRid(scoRuleDetail);
			super.save(entity);
		}else{
			super.save(entity);
			//保存标准模板
			sc.setId(IdGen.uuid());
			sc.setRid(entity.getId());
			dao.insertScoRuleDetailMould(sc);
			if(entity.getIsPb()){
				//继承配比
				ScoRulePb scoRulePb = new ScoRulePb();
				scoRulePb.setRule(entity.getParent());
				List<ScoRulePb> list = scoRulePbService.findList(scoRulePb);
				if(list != null && list.size() > 0){
					for(ScoRulePb scoPb : list){
						scoPb.setRule(entity);
					}
					scoRulePbService.insertPL(list);
				}
			}

		}

	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRule> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRule> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRule entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void updateSort(ScoRule entity) {
		dao.updateSort(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRule entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRule entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRule entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	@Transactional(readOnly = false)
	public void deleteByParentIds(ScoRule entity){
		dao.deleteByParentIds(entity);
	}

	public List<ScoRule> scoRuleSingleList(ScoRule entity){
		return dao.scoRuleSingleList(entity);
	}
	public List<ScoRule> ajaxValiScrRuleName(ScoRule entity){
		return dao.ajaxValiScrRuleName(entity);
	}
	public ScoRule findScoRuleSingleDetail(ScoRule entity){return dao.findScoRuleSingleDetail(entity);}
}