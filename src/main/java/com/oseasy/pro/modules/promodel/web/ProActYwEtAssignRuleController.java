package com.oseasy.pro.modules.promodel.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.service.ActYwEtAssignRuleService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActYwEtAssignRuleService;
import com.oseasy.pro.modules.promodel.service.ProBackTeacherExpansionService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 专家指派规则Controller.
 * @author zy
 * @version 2019-01-03
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actywAssignRule")
public class ProActYwEtAssignRuleController extends BaseController {
    private String PROTYPE="proType";
    @Autowired
    private ActYwEtAssignRuleService entityService;
    @Autowired
    private ProBackTeacherExpansionService proBackTeacherExpansionService;
	@Autowired
	private ProActYwEtAssignRuleService proActYwEtAssignRuleService;
	@Autowired
    private ProModelService proModelService;

    //项目分配状态列表
    @RequestMapping(value = "ajaxGetProAssginList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxGetProAssginList(Page<ProModel> page,ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
        try {
            page = proModelService.getAssginProList(page, actYwEtAssignRule);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //项目分配状态列表
    @RequestMapping(value = "ajaxProvGetProAssginList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxProvGetProAssginList(Page<ProModel> page,ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
        try {
            page = proModelService.getProvAssginProList(page, actYwEtAssignRule);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //得到查询属性
    @RequestMapping(value = "ajaxGetQuery", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxGetQuery(HttpServletRequest request, HttpServletResponse response){
        try {
            //根据菜单传值 判断是大赛还是项目
            String proType=request.getParameter(PROTYPE);
            Map<String,Object> map=entityService.findQueryList(proType);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = {"xmTaskList", ""})
    public String taskList(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ActYwEtAssignRule> page = entityService.findPage(new Page<ActYwEtAssignRule>(request, response), entity);
        model.addAttribute(Page.PAGE, page);
        model.addAttribute(PROTYPE, ProSval.PRO_TYPE_PROJECT);
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "taskList";
    }

    @RequestMapping(value = {"xmProvTaskList", ""})
    public String xmProvTaskList(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ActYwEtAssignRule> page = entityService.findPage(new Page<ActYwEtAssignRule>(request, response), entity);
        model.addAttribute(Page.PAGE, page);
        model.addAttribute(PROTYPE, ProSval.PRO_TYPE_PROJECT);
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "taskProvList";
    }

    @RequestMapping(value = {"dsTaskList", ""})
    public String dsTaskList(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ActYwEtAssignRule> page = entityService.findPage(new Page<ActYwEtAssignRule>(request, response), entity);
        model.addAttribute(Page.PAGE, page);
        model.addAttribute(PROTYPE,ProSval.PRO_TYPE_GCONTEST);
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "taskList";
    }

    @RequestMapping(value = {"dsProvTaskList", ""})
    public String dsProvTaskList(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ActYwEtAssignRule> page = entityService.findPage(new Page<ActYwEtAssignRule>(request, response), entity);
        model.addAttribute(Page.PAGE, page);
        model.addAttribute(PROTYPE,ProSval.PRO_TYPE_GCONTEST);
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "taskProvList";
    }

	//校自动 手动分配专家审核
	@RequestMapping(value = "ajaxGetManuallyAssgin" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxGetManuallyAssgin(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request) {
		try {
			proActYwEtAssignRuleService.ajaxGetManuallyAssgin(actYwEtAssignRule);
			return ApiResult.success(actYwEtAssignRule);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

    //省自动 手动分配专家审核
    @RequestMapping(value = "ajaxGetProvManuallyAssgin" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetProvManuallyAssgin(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request) {
        try {
            proActYwEtAssignRuleService.ajaxGetProvManuallyAssgin(actYwEtAssignRule);
            return ApiResult.success(actYwEtAssignRule);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	//清除分配任务
	@RequestMapping(value = "delProTask", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult delProTask(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
		try {
			//分配专家
			proActYwEtAssignRuleService.clearAssignUserTask(actYwEtAssignRule);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


    //清除省级分配任务
   	@RequestMapping(value = "delProvProTask", method = RequestMethod.POST, produces = "application/json")
   	@ResponseBody
   	public ApiResult delProvProTask(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
   		try {
   			//分配专家
   			proActYwEtAssignRuleService.clearProvAssignUserTask(actYwEtAssignRule);
   			return ApiResult.success();
   		}catch (Exception e){
   			logger.error(e.getMessage());
   			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
   		}
   	}


    //手动分配专家 专家列表
    @RequestMapping(value = "ajaxGetManuallyAssginList" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetManuallyAssginList(Page page,ActYwEtAssignTaskVo actYwEtAssignTaskVo,HttpServletRequest request) {
        try {
            actYwEtAssignTaskVo.setPage(page);
            List<BackTeacherExpansion> backTeacherExpansionList= proBackTeacherExpansionService.getUserToDoTaskList(actYwEtAssignTaskVo);
            page.setList(backTeacherExpansionList);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //得到专家任务数列表
    @RequestMapping(value = "ajaxGetExpertAssginNumList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetExpertAssginNumList(Page page,ActYwEtAssignTaskVo actYwEtAssignTaskVo, HttpServletRequest request, HttpServletResponse response){
        try {
            actYwEtAssignTaskVo.setPage(page);
            List<BackTeacherExpansion> list= proBackTeacherExpansionService.getUserTaskList(actYwEtAssignTaskVo);
            page.setList(list);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //手动分配专家 专家不分页列表
    @RequestMapping(value = "ajaxGetManuallyList" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetManuallyList(ActYwEtAssignTaskVo actYwEtAssignTaskVo,HttpServletRequest request) {
        try {
            List<BackTeacherExpansion> backTeacherExpansionList= proBackTeacherExpansionService.getUserToDoTaskList(actYwEtAssignTaskVo);
            return ApiResult.success(backTeacherExpansionList);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


	//校自动 保存规则并分配任务
	@RequestMapping(value = "ajaxAssginTask", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxAssginTask(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
		try {
			return proActYwEtAssignRuleService.assginTaskByRule(actYwEtAssignRule);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


    //省自动 保存规则并分配任务
   	@RequestMapping(value = "ajaxProvAssginTask", method = RequestMethod.POST, produces = "application/json")
   	@ResponseBody
   	public ApiResult ajaxProvAssginTask(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
   		try {
   			return proActYwEtAssignRuleService.assginProvTaskByRule(actYwEtAssignRule);
   		}catch (Exception e){
   			logger.error(ExceptionUtil.getStackTrace(e));
   			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
   		}
   	}
}