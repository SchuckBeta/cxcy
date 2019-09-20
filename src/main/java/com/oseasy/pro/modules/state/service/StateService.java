package com.oseasy.pro.modules.state.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.fileserver.modules.attachment.dao.SysAttachmentDao;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.dao.ProjectDeclareDao;
import com.oseasy.pro.modules.project.dao.ProjectPlanDao;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.project.enums.ProjectStatusEnum;
import com.oseasy.pro.modules.project.service.ProjectAuditInfoService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.state.vo.MidVo;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * Created by zhangzheng on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class StateService {

	@Autowired
	private ActTaskService actTaskService;
    @Autowired
    ProjectDeclareDao projectDeclareDao;
    @Autowired
    ProjectPlanDao projectPlanDao;
    @Autowired
    SysAttachmentService sysAttachmentService;
    @Autowired
    SysAttachmentDao sysAttachmentDao;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProjectAuditInfoService projectAuditInfoService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserService userService;
    public static String procDefKey="state_project_audit";

    /**
     *
     * 根据分数获得中期评级流程中合格和不合格数
     * @param score
     * @return
     */
    public Map<String,List<MidVo>> getNoByScore(int score) {
         Map<String,List<MidVo>> map= Maps.newHashMap();

        TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
				actTaskService.getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
        todoTaskQuery.processDefinitionKey(procDefKey);
        todoTaskQuery.processVariableValueGreaterThanOrEqual("scoreInt",score);
        todoTaskQuery.processVariableValueGreaterThanOrEqual("scorePoint",0);
        todoTaskQuery.taskDefinitionKeyLike("%middleRating%"); //中期评级步骤

        List<Task> todoList = todoTaskQuery.list();
        List<MidVo> voList = Lists.newArrayList();
        for (Task task : todoList) {
            MidVo midVo=new MidVo();
            System.out.println((String)task.getProcessVariables().get("id"));
            midVo.setBussinessId((String)task.getProcessVariables().get("id"));
            System.out.println(task.getId());
            midVo.setTaskId(task.getId());
            voList.add(midVo);
        }
        map.put("pass",voList);
        TaskQuery todoTaskQuery2 = //taskService.createTaskQuery().taskAssignee(userId)
				actTaskService.getTaskQueryByAssignee()
						.active().includeProcessVariables().orderByTaskCreateTime().desc();
        todoTaskQuery2.processDefinitionKey(procDefKey);
        todoTaskQuery2.processVariableValueLessThan("scoreInt",score);
        todoTaskQuery2.taskDefinitionKeyLike("%middleRating%"); //中期评级步骤

        List<Task> todoList2 = todoTaskQuery2.list();
        List<MidVo> voList2 = Lists.newArrayList();
        for (Task task : todoList2) {
            MidVo midVo=new MidVo();
            System.out.println((String)task.getProcessVariables().get("id"));
            midVo.setBussinessId((String)task.getProcessVariables().get("id"));
            System.out.println(task.getId());
            midVo.setTaskId(task.getId());
            voList2.add(midVo);
        }
        map.put("failed",voList2);


        return map;
    }




    /**
     * 保存项目变更
     * 1先保存主表信息
     * 2保存子表 任务分工 信息
     * 3附件信息
     * 4团队信息
     * 5评审信息
     */
    @Transactional(readOnly = false)
    public void saveEdit(ProjectDeclare projectDeclare) {
       // 1先保存主表信息
        projectDeclare.preUpdate();
        projectDeclareDao.update(projectDeclare);
       //2保存子表 任务分工 信息
        projectPlanDao.deleteByProjectId(projectDeclare.getId());
        int sort=1;
        for(ProjectPlan plan:projectDeclare.getPlanList()) {
            plan.setSort(sort+"");
            plan.setProjectId(projectDeclare.getId());
            plan.preInsert();
            projectPlanDao.insert(plan);
            sort++;
        }

    }
    @Transactional(readOnly = false)
    public JSONObject saveModify(ProjectDeclare projectDeclare,String modifyPros) {
    	JSONObject js=new JSONObject();
    	List<TeamUserHistory> stus=projectDeclare.getStudentList();
		List<TeamUserHistory> teas=projectDeclare.getTeacherList();
		String actywId=projectDeclare.getActywId();
		String teamId=projectDeclare.getTeamId();
		String proId=projectDeclare.getId();
		String category=projectDeclare.getType();
		js=commonService.checkProjectOnModify(stus,teas,proId, actywId,category, teamId,projectDeclare.getYear());
		if ("0".equals(js.getString("ret"))) {
			return js;
		}
    	saveEdit(projectDeclare);
    	commonService.disposeTeamUserHistoryForModify(stus, teas, actywId, teamId, proId);
        sysAttachmentService.saveByVo(projectDeclare.getAttachMentEntity(),projectDeclare.getId(),FileTypeEnum.S0,FileStepEnum.S100);
        //流程变更
        if (StringUtil.isNotEmpty(projectDeclare.getToGnodeId())) {
        	projectDeclareChangeAct(projectDeclare.getId(), projectDeclare.getToGnodeId());
        }
//        //变更学分信息
//        ProjectDeclare pd=projectDeclareService.getScoreConfigure(projectDeclare.getId());
//        if(pd != null){
//            if (pd!=null&&StringUtil.isNotEmpty(pd.getLevel())&&StringUtil.isNotEmpty(pd.getFinalResult())) {
//                projectDeclareService.saveScore(projectDeclare.getId());
//            }
//            js.put("msg", "保存成功");
//        }
        return js;
    }

	//查询大创项目可以变更节点
	public List<ProjectStatusEnum> getProjectDeclareChangeGnode(ProjectDeclare projectDeclare) {
		List<ProjectStatusEnum> newList=new ArrayList<ProjectStatusEnum>();
		if(projectDeclare.getStatus()!=null){
			int statusNum=Integer.parseInt(projectDeclare.getStatus());
			if(statusNum==8 || statusNum==9){
				return newList;
			}
			for(int i=1;i<statusNum;i++){
				//打回 完结 失败状态除掉
				if(i==4 ){
					continue;
				}
				if("3".equals(projectDeclare.getLevel()) || "4".equals(projectDeclare.getLevel()) ){
					if(i==2){
						continue;
					}
				}
				String status=String.valueOf(i);
				newList.add(ProjectStatusEnum.getByValue(status));
			}
		}
		return newList;
	}
	//将项目变更到相应的节点
	//大创 项目id
	// status 大创 项目变更状态
	@Transactional(readOnly = false)
	public void projectDeclareChangeAct(String projectDeclareId,String status) {
		if(status!=null && projectDeclareId!=null){
			ProjectDeclare projectDeclare=projectDeclareService.get(projectDeclareId);
			String taskId = actTaskService.getTaskidByProcInsId(projectDeclare.getProcInsId());
			if(taskId==null){
				return;
			}
			String ret="";
			if("1".equals(status)){
				//流程变更节点 set1立项学院秘书审核
				List<String> claims = userService.getCollegeSecs(projectDeclare.getCreateBy().getId());
				ret=actTaskService.rollBackFlow(taskId, "collegeSec", claims,  "set1");

			}else if("2".equals(status)){
				//流程变更节点 set2立项学校管理员审核
				List<String> claims = userService.getSchoolSecs();
				ret=actTaskService.rollBackFlow(taskId, "schoolSec", claims,  "set2");
			}else if("3".equals(status)){
				//流程变更节点 middle1中期学生提交报告
				List<String> claims = new 	ArrayList<String>();
				claims.add(projectDeclare.getCreateBy().getId());
				ret=actTaskService.rollBackFlow(taskId, "apply",claims ,  "middle1");
			}else if("5".equals(status)){
				//流程变更节点 根据项目级别走不同的分支
				//中期专家打分
				if("3".equals(projectDeclare.getLevel()) || "4".equals(projectDeclare.getLevel()) ){
					List<String> claims = userService.getCollegeExperts(projectDeclare.getCreateBy().getId());
					ret=actTaskService.rollBackFlow(taskId, "collegeExperts", claims,  "middleScore3");
				}else {
					List<String> claims = userService.getSchoolExperts();
					ret=actTaskService.rollBackFlow(taskId, "schoolExperts", claims,  "middleScore2");
				}
			}else if("6".equals(status)){
				//流程变更节点 close1结项学生提交报告
				List<String> claims = new 	ArrayList<String>();
				claims.add(projectDeclare.getCreateBy().getId());
				ret=actTaskService.rollBackFlow(taskId, "apply",claims ,  "close1");
			}else if("7".equals(status)){
				//流程变更节点 根据项目级别走不同的分支
				//结项专家打分
				if("3".equals(projectDeclare.getLevel()) || "4".equals(projectDeclare.getLevel()) ){
					List<String> claims = userService.getCollegeExperts(projectDeclare.getCreateBy().getId());
					ret=actTaskService.rollBackFlow(taskId, "collegeExperts", claims,  "closeScore3");
				}else {
					List<String> claims = userService.getSchoolExperts();
					ret=actTaskService.rollBackFlow(taskId, "schoolExperts", claims,  "closeScore2");
				}
			}
			if("SUCCESS".equals(ret)){
				projectDeclare.setStatus(status);
				projectDeclareService.updateStatus(projectDeclare);
			}
		}

	}


}
