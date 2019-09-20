package com.oseasy.act.modules.actyw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.dao.ActYwEtAssignRuleDao;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 专家指派规则Service.
 * @author zy
 * @version 2019-01-03
 */
@Service
@Transactional(readOnly = true)
public class ActYwEtAssignRuleService extends CrudService<ActYwEtAssignRuleDao, ActYwEtAssignRule> {
	@Autowired
	ActYwGassignService actYwGassignService;
	@Autowired
	ActYwGnodeService actYwGnodeService;
	@Autowired
	ActTaskService actTaskService;
	@Autowired
	ActYwService actYwService;
	@Autowired
	ActYwEtAuditNumService actYwEtAuditNumService;

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

	public ActYwEtAssignTaskVo ajaxOther(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		ActYwGnode actYwGnode=actYwGnodeService.get(actYwEtAssignTaskVo.getGnodeId());
		if(actYwGnode.getIsDelegate()){
			//得到当前节点数据
			CompletableFuture<Void> voidFuture = CompletableFuture.runAsync(() -> actYwGassignService.getDelegateToDoNumByGnodeId(actYwEtAssignTaskVo), ThreadUtils.newFixedThreadPool());
			//得到当前历史数据
			CompletableFuture<Integer> hasNumFuture = CompletableFuture.supplyAsync(() -> actYwGassignService.getAuditNumByGnodeId(actYwEtAssignTaskVo),ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(voidFuture,hasNumFuture).join();
			Integer hasNum = null;
			try {
				hasNum = hasNumFuture.get();
			} catch (InterruptedException e) {
				logger.error("查询线程中断,ajaxOther");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.error("执行异常,ajaxOther");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
			//得到当前节点待指派数据
//			Long todoNum=
//			//得到当前节点数据
//			Long auditNum=actYwGassignService.getProToDoNum(actYwEtAssignTaskVo);
//			actYwGassignService.getDelegateToDoNumByGnodeId(actYwEtAssignTaskVo);
			//得到当前历史数据
//			int hasNum=actYwGassignService.getAuditNumByGnodeId(actYwEtAssignTaskVo);
//			actYwEtAssignTaskVo.setTodoNum(String.valueOf(todoNum));
//			actYwEtAssignTaskVo.setToauditNum(String.valueOf(auditNum-todoNum));
			actYwEtAssignTaskVo.setHasNum(String.valueOf(hasNum));
		}else{
			//得到当前节点待指派数据
			CompletableFuture<Long> todoNumFuture = CompletableFuture.supplyAsync(() -> actYwGassignService.getToDoNumByGnodeId(actYwEtAssignTaskVo), ThreadUtils.newFixedThreadPool());
			//得到当前节点数据
			CompletableFuture<Long> auditNumFuture = CompletableFuture.supplyAsync(() -> actYwGassignService.getProToDoNum(actYwEtAssignTaskVo),ThreadUtils.newFixedThreadPool());
			//得到当前历史数据
			CompletableFuture<Integer> hasNumFuture = CompletableFuture.supplyAsync(() -> actYwGassignService.getAuditNumByGnodeId(actYwEtAssignTaskVo),ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(todoNumFuture,auditNumFuture,hasNumFuture).join();
			Long todoNum = null;
			Long auditNum = null;
			Integer hasNum = null;
			try {
				todoNum = todoNumFuture.get();
				auditNum = auditNumFuture.get();
				hasNum = hasNumFuture.get();
			} catch (InterruptedException e) {
				logger.error("查询线程中断,ajaxOther");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.error("执行异常,ajaxOther");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
//			//得到当前节点待指派数据
//			Long todoNum=actYwGassignService.getToDoNumByGnodeId(actYwEtAssignTaskVo);
//			//得到当前节点数据
//			Long auditNum=actYwGassignService.getProToDoNum(actYwEtAssignTaskVo);
//			//得到当前历史数据
//			int hasNum=actYwGassignService.getAuditNumByGnodeId(actYwEtAssignTaskVo);
			actYwEtAssignTaskVo.setTodoNum(String.valueOf(todoNum));
			actYwEtAssignTaskVo.setToauditNum(String.valueOf(auditNum-todoNum));
			actYwEtAssignTaskVo.setHasNum(String.valueOf(hasNum));
		}
		return actYwEtAssignTaskVo;
	}

	public ActYwEtAssignTaskVo ajaxUserTask(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		//得到待审核数据
		Long todoNum=actYwGassignService.getToDoNum(actYwEtAssignTaskVo);
		//得到已经审核数据
		String hasNum=actYwGassignService.getHasNum(actYwEtAssignTaskVo);
		actYwEtAssignTaskVo.setTodoNum(String.valueOf(todoNum));
		actYwEtAssignTaskVo.setHasNum(hasNum);
		return actYwEtAssignTaskVo;
	}


	@Transactional(readOnly = false)
	public List<String> getExpertByRule(ActYwEtAssignRule actYwEtAssignRule, String proId, List<String> expertList) {
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
		}
		return assignList;
	}


	public Map<String,Object> findQueryList(String proType) {
		List<ActYwEtAssignTaskVo> list=dao.findQueryList(proType);
		Map<String,List<ActYwEtAssignTaskVo>> map=new HashMap<String,List<ActYwEtAssignTaskVo>>();
		for(ActYwEtAssignTaskVo actYwEtAssignTaskVo: list){
			if(StringUtil.checkNotEmpty(map.get(actYwEtAssignTaskVo.getActywId()))){
				List<ActYwEtAssignTaskVo> indexList=map.get(actYwEtAssignTaskVo.getActywId());
				indexList.add(actYwEtAssignTaskVo);
			}else{
				List<ActYwEtAssignTaskVo> indexList=new ArrayList<ActYwEtAssignTaskVo>();
				indexList.add(actYwEtAssignTaskVo);
				map.put(actYwEtAssignTaskVo.getActywId(),indexList);
			}
		}
		List<ActYwEtAssignTaskVo> voList=new ArrayList<ActYwEtAssignTaskVo>();
		for (ActYwEtAssignTaskVo actYwEtAssignTaskVo: list){
			if(!checkContain(voList,actYwEtAssignTaskVo.getActywId())){
				ActYwEtAssignTaskVo actYwEtAssignTaskVoIndex=new ActYwEtAssignTaskVo();
				actYwEtAssignTaskVoIndex.setActywId(actYwEtAssignTaskVo.getActywId());
				actYwEtAssignTaskVoIndex.setActywName(actYwEtAssignTaskVo.getActywName());
				voList.add(actYwEtAssignTaskVoIndex);
			}
		}
		Map<String,Object>  endMap= new HashMap<>();
		endMap.put("proTypeNode",map);
		endMap.put("projectTypes",voList);
		return endMap;
	}

	private boolean checkContain(List<ActYwEtAssignTaskVo> voList, String actywId) {
		for(ActYwEtAssignTaskVo actYwEtAssignTaskVo:voList){
			if(actYwEtAssignTaskVo.getActywId().equals(actywId)){
				return true;
			}
		}
		return false;
	}

	public ActYwEtAssignRule getByEntity(ActYwEtAssignRule entity) {
		ActYwEtAssignRule actYwEtAssignRule=dao.getActYwEtAssignRuleByActywIdAndGnodeId(entity.getActywId(),entity.getGnodeId());
		if(actYwEtAssignRule!=null){
			return  actYwEtAssignRule;
		}
		return entity;
	}

}