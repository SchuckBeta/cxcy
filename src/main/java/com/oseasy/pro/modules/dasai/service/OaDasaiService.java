/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.pro.modules.dasai.service;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.dasai.dao.OaDasaiDao;
import com.oseasy.pro.modules.dasai.entity.OaDasai;
import com.oseasy.util.common.utils.StringUtil;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大赛测试Service
 * @author zhangzheng
 * @version 2017-02-24
 */
@Service
@Transactional(readOnly = true)
public class OaDasaiService extends CrudService<OaDasaiDao, OaDasai> {

	public OaDasai get(String id) {
		return super.get(id);
	}
	
	public List<OaDasai> findList(OaDasai oaDasai) {
		return super.findList(oaDasai);
	}
	
	public Page<OaDasai> findPage(Page<OaDasai> page, OaDasai oaDasai) {
		return super.findPage(page, oaDasai);
	}

	@Autowired
	ActTaskService actTaskService;

	@Autowired
	UserService userService;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	IdentityService identityService;

	@Autowired
	ActDao actDao;


	@Transactional(readOnly = false)
	public void save(OaDasai oaDasai) {
		//保存表单
		oaDasai.setState("1");  //审核中
		oaDasai.preInsert();
		dao.insert(oaDasai);
		//启动大赛工作流
		String roleName=actTaskService.getStartNextRoleName("multi_task");  //从工作流中查询 下一步的角色集合
		List<String> roles=userService.getRolesByName(roleName);
		Map<String,Object> vars=new HashMap<String,Object>();
		vars.put(roleName+"s",roles);
		vars.put("projectName",oaDasai.getProjectName());

		String key="multi_task";
		String userId = UserUtils.getUser().getLoginName();
		vars.put("sumbitter",userId);
		identityService.setAuthenticatedUserId(userId);
		ProcessInstance procIns=runtimeService.startProcessInstanceByKeyAndTenantId(key, "oa_dasai"+":"+oaDasai.getId(),vars, TenantConfig.getCacheTenant());

		//流程id返写业务表
		Act act = new Act();
		act.setBusinessTable("oa_dasai");// 业务表名
		act.setBusinessId(oaDasai.getId());	// 业务表ID
		act.setProcInsId(procIns.getId());
		actDao.updateProcInsIdByBusinessId(act);

	//	actTaskService.startProcess("multi_task","oa_dasai",oaDasai.getId(),oaDasai.getProjectName(),null);
	}

	@Transactional(readOnly = false)
	public void saveAudit1(OaDasai oaDasai) {
		//完成工作流
		String userId = UserUtils.getUser().getLoginName();
		String comment=userId+" 的评分："+oaDasai.getScore();

		Map<String,Object> vars=new HashMap<String,Object>();
		vars.put("projectName",oaDasai.getProjectName());

		System.out.println("key::::::"+oaDasai.getAct().getTaskDefKey());
		String roleName=actTaskService.getNextRoleName(oaDasai.getAct().getTaskDefKey(),"multi_task");  //查找下一个环节的 roleName
		if (StringUtil.isNotBlank(roleName)) {
			List<String> roles=userService.getRolesByName(roleName);
			vars.put(roleName+"s",roles);
		}

		actTaskService.complete(oaDasai.getAct().getTaskId(), oaDasai.getAct().getProcInsId(), comment, oaDasai.getProjectName(), vars);

		//查询本用户任务环节是否完成，改变业务表对应的状态
		String taskDefKey = oaDasai.getAct().getTaskDefKey(); 	// 环节编号

		if (actTaskService.isMultiFinished("multi_task",oaDasai.getAct().getTaskDefKey(),oaDasai.getAct().getProcInsId())) { //如果当前任务环节完成了
			//处理平均分 查询子表，取平均
			oaDasai.setScore(93);  //先随便弄了个
			//处理状态
			if (StringUtil.isBlank(roleName)) {  //如果没有了审核人了，说明审核结束了
				oaDasai.setState("2");
			}
			dao.updateStateAndScore(oaDasai);
		}


	}


	@Transactional(readOnly = false)
	public void saveAudit2(OaDasai oaDasai) {
		//完成工作流
		String userId = UserUtils.getUser().getLoginName();
		String comment=userId+" 的评分："+oaDasai.getScore();
		Map<String,Object> vars=new HashMap<String,Object>();
		vars.put("projectName",oaDasai.getProjectName());

		String roleName=actTaskService.getNextRoleName(oaDasai.getAct().getTaskDefKey(),"multi_task");  //查找下一个环节的 roleName
		if (StringUtil.isNotBlank(roleName)) {
			List<String> roles=userService.getRolesByName(roleName);
			vars.put(roleName+"s",roles);
		}



		actTaskService.complete(oaDasai.getAct().getTaskId(), oaDasai.getAct().getProcInsId(), comment, oaDasai.getProjectName(), vars);

		//查询本用户任务环节是否完成，改变业务表对应的状态
		String taskDefKey = oaDasai.getAct().getTaskDefKey(); 	// 环节编号

		if (actTaskService.isMultiFinished("multi_task",oaDasai.getAct().getTaskDefKey(),oaDasai.getAct().getProcInsId())) { //如果当前任务环节完成了
			//处理平均分 查询子表，取平均
			oaDasai.setScore(93);  //先随便弄了个
			//处理状态
			if (StringUtil.isBlank(roleName)) {  //如果没有了审核人了，说明审核结束了
				oaDasai.setState("2");
			}
			dao.updateStateAndScore(oaDasai);
		}




	}

	
	@Transactional(readOnly = false)
	public void delete(OaDasai oaDasai) {
		super.delete(oaDasai);
	}
	
}