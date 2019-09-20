package com.oseasy.pie.modules.impdata.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGtime;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pie.modules.expdata.entity.ExpInfo;
import com.oseasy.pie.modules.expdata.service.ExpInfoService;
import com.oseasy.pie.modules.impdata.service.ImpExpService;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

@Controller
public class ImpExpController extends BaseController {
	@Autowired
	private ExpInfoService expInfoService;
	@Autowired
	private ImpExpService impExpService;
    @Autowired
    private ActYwGtimeService actYwGtimeService;
	@Autowired
	private ProModelMdService proModelMdService;
	@Autowired
	private ActTaskService actTaskService;
	@RequestMapping(value = "${adminPath}/proprojectmd/getExpCloseInfo")
	@ResponseBody
	public ExpInfo getExpCloseInfo(HttpServletRequest request, HttpServletResponse response) {
		return expInfoService.getExpInfoByType(ExpType.MdExpclose.getValue());
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/getExpMidInfo")
	@ResponseBody
	public ExpInfo getExpMidInfo(HttpServletRequest request, HttpServletResponse response) {
		return expInfoService.getExpInfoByType(ExpType.MdExpmid.getValue());
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/getExpApprovalInfo")
	@ResponseBody
	public ExpInfo getExpApprovalInfo(HttpServletRequest request, HttpServletResponse response) {
		return expInfoService.getExpInfoByType(ExpType.MdExpapproval.getValue());
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/getExpInfo")
	@ResponseBody
	public ExpInfo getExpInfo(HttpServletRequest request, HttpServletResponse response) {
		String eid=request.getParameter("eid");
		return expInfoService.getExpInfo(eid);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expAll")
	public void expAll(HttpServletRequest request, HttpServletResponse response) {
		impExpService.expAll(request,response);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expClosePlus")
	@ResponseBody
	public JSONObject expClosePlus(HttpServletRequest request, HttpServletResponse response) {
		return impExpService.expClosePlus(request,response);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expClose")
	public void expClose(HttpServletRequest request, HttpServletResponse response) {
		impExpService.expClose(request,response);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expMidPlus")
	@ResponseBody
	public JSONObject expMidPlus(HttpServletRequest request, HttpServletResponse response) {
		return impExpService.expMidPlus(request,response);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expMid")
	public void expMid(HttpServletRequest request, HttpServletResponse response) {
		impExpService.expMid(request,response);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expApprovalPlus")
	@ResponseBody
	public JSONObject expApprovalPlus(HttpServletRequest request, HttpServletResponse response) {
		return impExpService.expApprovalPlus(request,response);
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/expApproval")
	public void expApproval(HttpServletRequest request, HttpServletResponse response) {
		impExpService.expApproval(request,response);
	}

	/**
     * 自定义流程民大查询导出.
     * @param request
     * @param response
     * @return JSONObject
     */
	@RequestMapping(value = "${adminPath}/proprojectmd/expQuery")
    public void expQuery(HttpServletRequest request, HttpServletResponse response) {
        impExpService.expQuery(request,response);
    }

	/**
	 * 自定义流程民大节点导出.
	 * @param request
	 * @param response
	 * @return JSONObject
	 */
    @RequestMapping(value = "${adminPath}/proprojectmd/expPlus")
    @ResponseBody
    public JSONObject expPlus(HttpServletRequest request, HttpServletResponse response) {
        ItOper impVo = new ItOper(request);
        if (StringUtil.isNotEmpty(impVo.getReqParam().getType())) {// 民大立项
            impVo.getReqParam().setIsMdOld(true);
            if ((ExpType.MdExpapproval.getIdx()).equals(impVo.getReqParam().getType())) {// 民大立项
                return impExpService.expApprovalPlus(request,response);
            }else if ((ExpType.MdExpmid.getIdx()).equals(impVo.getReqParam().getType())) {// 民大中期
                return impExpService.expMidPlus(request,response);
            }else if ((ExpType.MdExpclose.getIdx()).equals(impVo.getReqParam().getType())) {// 民大结项
                return impExpService.expClosePlus(request,response);
            }
        }else{
            if(StringUtil.isNotEmpty(impVo.getActywId()) && StringUtil.isNotEmpty(impVo.getReqParam().getGnodeId())){
                ActYwGtime gtime = actYwGtimeService.getTimeByYnodeId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
                if((gtime != null) && (gtime.getHasTpl()) && StringUtil.isNotEmpty(gtime.getExcelTplPath())){
                    impVo.getReqParam().setIsMdOld(true);
                    if ((gtime.getExcelTplPath()).contains(ExpType.MdExpapproval.getKey())) {// 民大立项
                        return impExpService.expApprovalPlus(request,response, impVo);
                    }else if ((gtime.getExcelTplPath()).contains(ExpType.MdExpmid.getKey())) {// 民大中期
                        return impExpService.expMidPlus(request,response, impVo);
                    }else if((gtime.getExcelTplPath()).contains(ExpType.MdExpclose.getKey())){// 民大结项
                        return impExpService.expClosePlus(request,response, impVo);
                    }
                }else{
                    logger.error("请在流程管理，配置导出Excel模板！");
                }
            }else{
                impVo.getReqParam().setIsMdOld(false);
                return impExpService.expQuery(request,response);
            }
        }
        return null;
    }

	@RequestMapping(value = "${adminPath}/proprojectmd/checkExpAll")
	@ResponseBody
	public JSONObject checkExpAll(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
//		String actywId=request.getParameter("actywId");
		List<String> pids=proModelMdService.getAllPromodelMd();
		if (pids!=null&&pids.size()>0) {
			js.put("ret", "1");
		}else{
			js.put("ret", "0");
		}
		return js;
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/checkExpApproval")
	@ResponseBody
	public JSONObject checkExpApproval(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
		String gnodeId=request.getParameter("gnodeId");
		String actywId=request.getParameter("actywId");
		List<String> pids=actTaskService.getAllTodoId(actywId,gnodeId);
		if (pids!=null&&pids.size()>0) {
			js.put("ret", "1");
		}else{
			js.put("ret", "0");
		}
		return js;
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/checkExpMid")
	@ResponseBody
	public JSONObject checkExpMid(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
		String gnodeId=request.getParameter("gnodeId");
		String actywId=request.getParameter("actywId");
		List<String> pids=actTaskService.getAllTodoId(actywId,gnodeId);
		if (pids!=null&&pids.size()>0) {
			js.put("ret", "1");
		}else{
			js.put("ret", "0");
		}
		return js;
	}
	@RequestMapping(value = "${adminPath}/proprojectmd/checkExpClose")
	@ResponseBody
	public JSONObject checkExpClose(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
        String gnodeId = request.getParameter(ActYwGroup.JK_GNODE_ID);
        String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		List<String> pids=actTaskService.getAllTodoId(actywId,gnodeId);
		if (pids!=null&&pids.size()>0) {
			js.put("ret", "1");
		}else{
			js.put("ret", "0");
		}
		return js;
	}


    /**
     * 自定义流程民大导出.
     * @param request
     * @param response
     * @return JSONObject
     */
    @RequestMapping(value = "${adminPath}/proprojectmd/checkExp")
    @ResponseBody
    public JSONObject checkExp(HttpServletRequest request, HttpServletResponse response) {
        ItOper impVo = new ItOper(request);
        JSONObject js=new JSONObject();
        List<String> pids = Lists.newArrayList();
        if (StringUtil.isNotEmpty(impVo.getReqParam().getType())) {// 民大立项
            if ((ExpType.MdExpapproval.getIdx()).equals(impVo.getReqParam().getType())) {// 民大立项
                pids = actTaskService.getAllTodoId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
            }else if ((ExpType.MdExpmid.getIdx()).equals(impVo.getReqParam().getType())) {// 民大中期
                pids = actTaskService.getAllTodoId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
            }else if ((ExpType.MdExpclose.getIdx()).equals(impVo.getReqParam().getType())) {// 民大结项
                pids = actTaskService.getAllTodoId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
            }
        }else{
            if(StringUtil.isNotEmpty(impVo.getActywId()) && StringUtil.isNotEmpty(impVo.getReqParam().getGnodeId())){
                ActYwGtime gtime = actYwGtimeService.getTimeByYnodeId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
                if((gtime == null) || (!gtime.getHasTpl()) || StringUtil.isNotEmpty(gtime.getExcelTplPath())){
                    if((gtime.getExcelTplPath()).contains(ExpType.MdExpapproval.getKey())){// 民大立项
                        pids = actTaskService.getAllTodoId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
                    }else if ((gtime.getExcelTplPath()).contains(ExpType.MdExpmid.getKey())) {// 民大中期
                        pids = actTaskService.getAllTodoId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
                    }else if ((gtime.getExcelTplPath()).contains(ExpType.MdExpclose.getKey())) {// 民大结项
                        pids = actTaskService.getAllTodoId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
                    }
                }
            }
        }

        if (StringUtil.checkNotEmpty(pids)) {
            js.put("ret", "1");
        }else{
            js.put("ret", "0");
        }
        return js;
    }

    /**************************************************************************************
     * 自定义导出数据包括（自定义大赛\自定义项目\铜陵学院项目\互联网+大赛\）.
     * @param request
     * @param response
     */
	@ResponseBody
    @RequestMapping(value = "${adminPath}/exp/expData/{actywId}")
    public JSONObject exportProject(@PathVariable String actywId, HttpServletRequest request, HttpServletResponse response){
        return impExpService.expData(actywId, new ItReqParam(request, actywId), request, response);
    }

	/**
	 * 自定义导出数据和附件包括（自定义大赛\自定义项目\铜陵学院项目\互联网+大赛）.
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "${adminPath}/exp/expByGnode")
	public JSONObject expByGnode(HttpServletRequest request, HttpServletResponse response) {
	    JSONObject jo = impExpService.expDataByGnode(new ItReqParam(request), request, response);
	    if(jo == null){
	        JSONObject js = new JSONObject();
	        js.put("ret", 0);
	        js.put("msg", "");
	    }
		return jo;
	}

    @ResponseBody
	@RequestMapping(value = "${adminPath}/exp/getExpGnode")
	public ExpInfo getExpGnode(HttpServletRequest request, HttpServletResponse response) {
		return expInfoService.getExpInfoByType(request.getParameter(ActYwGroup.JK_GNODE_ID));
	}

    @ResponseBody
	@RequestMapping(value = "${adminPath}/exp/getGnodeExpInfo")
	public ExpInfo getGnodeExpInfo(HttpServletRequest request, HttpServletResponse response) {
		String eid = request.getParameter(ExpInfo.JK_EXPINFO_ID);
		ExpInfo expInfo = expInfoService.getExpInfo(eid);
		if(expInfo== null){
		    expInfo = new ExpInfo(eid);
		}
		return expInfo;
	}

    /**************************************************************************************
     * 大创项目导出数据.
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "${adminPath}/exp/expPdData/{actywId}")
    public JSONObject expPdData(@PathVariable String actywId, HttpServletRequest request, HttpServletResponse response){
        return impExpService.expPdData(actywId, new ItReqParam(request, actywId), request, response);
    }

    /**
     * 大创项目导出数据和附件.
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "${adminPath}/exp/expPdByGnode")
    public JSONObject expPdByGnode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jo = impExpService.expPdDataByGnode(new ItReqParam(request), request, response);
        if(jo == null){
            JSONObject js = new JSONObject();
            js.put("ret", 0);
            js.put("msg", "");
        }
        return jo;
    }

    @ResponseBody
    @RequestMapping(value = "${adminPath}/exp/getExpPdGnode")
    public ExpInfo getExpPdGnode(HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(request.getParameter(ActYwGroup.JK_GNODE_ID))){
            return expInfoService.getExpInfoByType(ProjectNodeVo.gnodeFiles().getId());
        }else{
            return expInfoService.getExpInfoByType(request.getParameter(ActYwGroup.JK_GNODE_ID));
        }
    }

    @ResponseBody
    @RequestMapping(value = "${adminPath}/exp/getGnodeExpPdInfo")
    public ExpInfo getGnodeExpPdInfo(HttpServletRequest request, HttpServletResponse response) {
        String eid=request.getParameter("eid");
        ExpInfo expInfo = expInfoService.getExpInfo(eid);
        if(expInfo== null){
            expInfo = new ExpInfo(eid);
        }
        return expInfo;
    }

}