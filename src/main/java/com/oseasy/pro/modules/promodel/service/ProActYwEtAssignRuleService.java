package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.dao.ActYwEtAssignRuleDao;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwEtAuditNumService;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 专家指派规则Service.
 * @author zy
 * @version 2019-01-03
 */
@Service
@Transactional(readOnly = true)
public class ProActYwEtAssignRuleService extends CrudService<ActYwEtAssignRuleDao, ActYwEtAssignRule> {
	@Autowired
	ProActYwGassignService proActYwGassignService;
	@Autowired
	BackTeacherExpansionService backTeacherExpansionService;
	@Autowired
	ActYwGnodeService actYwGnodeService;
	@Autowired
	ActTaskService actTaskService;
	@Autowired
	ActYwService actYwService;
	@Autowired
	ActYwGassignService actYwGassignService;
	@Autowired
	ActYwEtAuditNumService actYwEtAuditNumService;

	@Autowired
	private CoreService coreService;

	public ActYwEtAssignRule get(String id) {
		return super.get(id);
	}

	public List<ActYwEtAssignRule> findList(ActYwEtAssignRule entity) {
		return super.findList(entity);
	}

	public Page<ActYwEtAssignRule> findPage(Page<ActYwEtAssignRule> page, ActYwEtAssignRule entity) {
		return super.findPage(page, entity);
	}

	public Page<ActYwEtAssignTaskVo> findActYwEtAssignTaskVoPage(Page<ActYwEtAssignTaskVo> page, ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		actYwEtAssignTaskVo.setPage(page);
		page.setList(dao.findActYwEtAssignTaskVoList(actYwEtAssignTaskVo));
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(ActYwEtAssignRule entity) {
	    if(entity.getIsNewRecord()){
	        if(StringUtil.isEmpty(entity.getAtype())){
	            entity.setAtype(EarAtype.ZP.getKey());
	        }
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ActYwEtAssignRule> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ActYwEtAssignRule> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ActYwEtAssignRule entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ActYwEtAssignRule entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwEtAssignRule entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ActYwEtAssignRule entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	public ActYwEtAssignRule getActYwEtAssignRuleByActywIdAndGnodeId(String actywId ,String gnodeId) {
		return dao.getActYwEtAssignRuleByActywIdAndGnodeId(actywId,gnodeId);
	}

	@Transactional(readOnly = false)
	public ApiResult assginTaskByRule(ActYwEtAssignRule actYwEtAssignRule) {
		//设定需要的审核人数
		int gradeSpeEachTotal=Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
		List<String> promodelIdList = proActYwGassignService.getProList(actYwEtAssignRule);
		if(StringUtil.checkEmpty(promodelIdList)){
			return ApiResult.success(actYwEtAssignRule,"保存成功");
		}
		actYwEtAssignRule.setProIdList(promodelIdList);
		//清除当前任务
		clearAssignUserTask(actYwEtAssignRule);
		//重新分配任务
		for(String proId:promodelIdList){
			boolean ret=false;
			if(ActYwEtAssignRule.autoRole.equals(actYwEtAssignRule.getAuditRole())){
				//0是全部专家
				List<String> expertList = null;
				Role role = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
				if (role != null){
					expertList = backTeacherExpansionService.findAllExpertListById(role.getId());
				}else{
					expertList=backTeacherExpansionService.findAllExpertList();
				}
				if(expertList.size()<gradeSpeEachTotal){
					return ApiResult.failed(actYwEtAssignRule,ApiConst.CODE_LESSUSER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_LESSUSER_ERROR));
				}
				ret=assignExpertByRule(actYwEtAssignRule, proId, expertList);
			}else{//1为根据学院找专家
				//找到的专家
//				List<String> expertList=backTeacherExpansionService.findCollegeExpertListByPro(proId);
				List<String> expertList=backTeacherExpansionService.findExpertListByType(actYwEtAssignRule.getAuditRole());
				if(expertList.size()<gradeSpeEachTotal){
					return ApiResult.failed(actYwEtAssignRule,ApiConst.CODE_LESSUSER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_LESSUSER_ERROR));
				}
				ret=assignExpertByRule(actYwEtAssignRule,  proId, expertList);
			}
			if(!ret){

				return ApiResult.failed(actYwEtAssignRule,ApiConst.CODE_ASSIGN_ERROR,ApiConst.getErrMsg(ApiConst.CODE_ASSIGN_ERROR));
			}
		}

		save(actYwEtAssignRule);
		return ApiResult.success(actYwEtAssignRule);
	}

	@Transactional(readOnly = false)
	public ApiResult assginProvTaskByRule(ActYwEtAssignRule actYwEtAssignRule) {
		//设定需要的审核人数
		int gradeSpeEachTotal=Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
		List<String> promodelIdList = proActYwGassignService.getProvProList(actYwEtAssignRule);
		if(StringUtil.checkEmpty(promodelIdList)){
			return ApiResult.success(actYwEtAssignRule,"保存成功");
		}
		actYwEtAssignRule.setProIdList(promodelIdList);
		//清除当前任务
		clearProvAssignUserTask(actYwEtAssignRule);
		//重新分配任务
		for(String proId:promodelIdList){
			boolean ret=false;
			if(ActYwEtAssignRule.autoRole.equals(actYwEtAssignRule.getAuditRole())){
				//0是全部专家
//				List<String> expertList=backTeacherExpansionService.findAllExpertList();
				Role role = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
				List<String> expertList=backTeacherExpansionService.findAllExpertListById(role.getId());
				if(expertList.size()<gradeSpeEachTotal){
					return ApiResult.failed(actYwEtAssignRule,ApiConst.CODE_LESSUSER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_LESSUSER_ERROR));
				}
				ret=assignProvExpertByRule(actYwEtAssignRule, proId, expertList);
			}else{//1为根据学院找专家
				//找到的专家
//				List<String> expertList=backTeacherExpansionService.findCollegeExpertListByPro(proId);
				List<String> expertList=backTeacherExpansionService.findExpertListByType(actYwEtAssignRule.getAuditRole());
				if(expertList.size()<gradeSpeEachTotal){
					return ApiResult.failed(actYwEtAssignRule,ApiConst.CODE_LESSUSER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_LESSUSER_ERROR));
				}
				ret=assignProvExpertByRule(actYwEtAssignRule,  proId, expertList);
			}
			if(!ret){

				return ApiResult.failed(actYwEtAssignRule,ApiConst.CODE_ASSIGN_ERROR,ApiConst.getErrMsg(ApiConst.CODE_ASSIGN_ERROR));
			}
		}

		save(actYwEtAssignRule);
		return ApiResult.success(actYwEtAssignRule);
	}

	@Transactional(readOnly = false)
	public Boolean assignExpertByRule(ActYwEtAssignRule actYwEtAssignRule, String proId, List<String> expertList) {
			ArrayList<String> indexList=new ArrayList<String>();
			//设定需要的审核人数
			int gradeSpeEachTotal=Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
			//设定每个人最大审核数
			int gradeCancelTotal=Integer.valueOf(actYwEtAssignRule.getAuditMax());
			List<String> assignList=new ArrayList<String>();
			if(ActYwEtAssignRule.auditNum.equals(actYwEtAssignRule.getAuditUserNum())){
				assignList.addAll(expertList);
			}else{
				indexList.addAll(expertList);
				//根据算法 分配出实际专家
				assignList=actYwEtAuditNumService.getArrayList(actYwEtAssignRule,gradeSpeEachTotal, gradeCancelTotal, indexList, proId);
				//assignList= LotteryUtil.getArrayList(gradeSpeEachTotal, gradeCancelTotal, indexList, proId);
			}

			if(StringUtil.checkEmpty(assignList)){
				return false;
	//			return ApiResult.failed(ApiConst.CODE_ASSIGN_ERROR,ApiConst.getErrMsg(ApiConst.CODE_ASSIGN_ERROR));
			}
			ActYwGassign actYwGassign = new ActYwGassign();
			actYwGassign.setYwId(actYwEtAssignRule.getActywId());
			actYwGassign.setGnodeId(actYwEtAssignRule.getGnodeId());
	        //actYwGassign = ActYwGassign.initType(actYwGassign, actYwGassignService.getGnode(actYwGassign));
			ActYwGnode gnode=actYwGnodeService.get(actYwEtAssignRule.getGnodeId());
			//设置指派点 删除旧的指派
			boolean res=proActYwGassignService.saveProModelAssignByList(proId,actYwGassign,assignList);
			if(res){
				List<ActYwGassign> insertActYwGassignList=new ArrayList<ActYwGassign>();
				for(int j=0;j<assignList.size();j++){
					String userId=assignList.get(j);
					ActYwGassign actYwGassignIndex =new ActYwGassign();
					actYwGassignIndex.setId(IdGen.uuid());
					actYwGassignIndex.setYwId(actYwGassign.getYwId());
					actYwGassignIndex.setPromodelId(proId);
					actYwGassignIndex.setRevUserId(userId);
					if(gnode.getIsDelegate()){
						actYwGassignIndex.setType(EarAtype.WP.getKey());
					}else if(gnode.getIsAssign()){
						actYwGassignIndex.setType(EarAtype.ZP.getKey());
					}
					actYwGassignIndex.setGnodeId(actYwGassign.getGnodeId());
					actYwGassignIndex.setYwId(actYwGassign.getYwId());
					actYwGassignIndex.setAssignUserId(UserUtils.getUserId());
					insertActYwGassignList.add(actYwGassignIndex);
				}
				actYwGassignService.insertPl(insertActYwGassignList);

			}
			return true;
		}

	@Transactional(readOnly = false)
	public Boolean assignProvExpertByRule(ActYwEtAssignRule actYwEtAssignRule, String proId, List<String> expertList) {
			ArrayList<String> indexList=new ArrayList<String>();
			//设定需要的审核人数
			int gradeSpeEachTotal=Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
			//设定每个人最大审核数
			int gradeCancelTotal=Integer.valueOf(actYwEtAssignRule.getAuditMax());
			List<String> assignList=new ArrayList<String>();
			if(ActYwEtAssignRule.auditNum.equals(actYwEtAssignRule.getAuditUserNum())){
				assignList.addAll(expertList);
			}else{
				indexList.addAll(expertList);
				//根据算法 分配出实际专家
				assignList=actYwEtAuditNumService.getArrayList(actYwEtAssignRule,gradeSpeEachTotal, gradeCancelTotal, indexList, proId);
				//assignList= LotteryUtil.getArrayList(gradeSpeEachTotal, gradeCancelTotal, indexList, proId);
			}

			if(StringUtil.checkEmpty(assignList)){
				return false;
	//			return ApiResult.failed(ApiConst.CODE_ASSIGN_ERROR,ApiConst.getErrMsg(ApiConst.CODE_ASSIGN_ERROR));
			}
			ActYwGassign actYwGassign = new ActYwGassign();
			actYwGassign.setYwId(actYwEtAssignRule.getActywId());
			actYwGassign.setGnodeId(actYwEtAssignRule.getGnodeId());
	        //actYwGassign = ActYwGassign.initType(actYwGassign, actYwGassignService.getGnode(actYwGassign));
			ActYwGnode gnode=actYwGnodeService.get(actYwEtAssignRule.getGnodeId());
			//设置指派点 删除旧的指派
//			boolean res=proActYwGassignService.saveProModelAssignByList(proId,actYwGassign,assignList);
			boolean res=proActYwGassignService.saveProvProModelAssignByList(proId,actYwGassign,assignList);
			if(res){
				List<ActYwGassign> insertActYwGassignList=new ArrayList<ActYwGassign>();
				for(int j=0;j<assignList.size();j++){
					String userId=assignList.get(j);
					ActYwGassign actYwGassignIndex =new ActYwGassign();
					actYwGassignIndex.setId(IdGen.uuid());
					actYwGassignIndex.setYwId(actYwGassign.getYwId());
					actYwGassignIndex.setPromodelId(proId);
					actYwGassignIndex.setRevUserId(userId);
					if(gnode.getIsDelegate()){
						actYwGassignIndex.setType(EarAtype.WP.getKey());
					}else if(gnode.getIsAssign()){
						actYwGassignIndex.setType(EarAtype.ZP.getKey());
					}
					actYwGassignIndex.setGnodeId(actYwGassign.getGnodeId());
					actYwGassignIndex.setYwId(actYwGassign.getYwId());
					actYwGassignIndex.setAssignUserId(UserUtils.getUserId());
					insertActYwGassignList.add(actYwGassignIndex);
				}
				actYwGassignService.insertPl(insertActYwGassignList);

			}
			return true;
		}


	//清除专家分配任务
	@Transactional(readOnly = false)
	public void clearAssignUserTask(ActYwEtAssignRule actYwEtAssignRule) {
		proActYwGassignService.clearAssignUserTask(actYwEtAssignRule);
	}

	//清除专家分配任务
	@Transactional(readOnly = false)
	public void clearProvAssignUserTask(ActYwEtAssignRule actYwEtAssignRule) {
		proActYwGassignService.clearProvAssignUserTask(actYwEtAssignRule);
	}

	@Transactional(readOnly = false)
	public void ajaxGetManuallyAssgin(ActYwEtAssignRule actYwEtAssignRule) {
		save(actYwEtAssignRule);
		proActYwGassignService.ajaxGetManuallyAssgin(actYwEtAssignRule);
	}


	@Transactional(readOnly = false)
	public void ajaxGetProvManuallyAssgin(ActYwEtAssignRule actYwEtAssignRule) {
		save(actYwEtAssignRule);
		proActYwGassignService.ajaxGetProvManuallyAssgin(actYwEtAssignRule);
	}
}