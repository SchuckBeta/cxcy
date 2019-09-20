/**
 *
 */
package com.oseasy.scr.modules.scr.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.scr.modules.scr.service.ScrProModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwFormService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.apply.IAengine;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.apply.IAstatus;
import com.oseasy.act.modules.actyw.tool.apply.IConfig;
import com.oseasy.act.modules.actyw.tool.apply.IForm;
import com.oseasy.act.modules.actyw.tool.apply.IFrorm;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowAuparam;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowAutype;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormFtype;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormType;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
//import com.oseasy.pro.modules.promodel.entity.ProModel;
//import com.oseasy.pro.modules.promodel.service.ProActService;
//import com.oseasy.pro.modules.promodel.service.ProModelService;
//import com.oseasy.pro.modules.workflow.IWorkFlow;
//import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.tool.apply.ScrAengine;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 内容管理Controller
 *
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cmss")
public class CmsScoreController extends BaseController {
	public static final String CMS_FORM_ADMIN = CoreSval.getAdminPath() + "/cms/form/";
	public static final String OPEN_TIME_LIMIT = CoreSval.getConfig("openTimeLimit");
	@Autowired
	private ActYwFormService actYwFormService;
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private ScrProModelService scrProModelService;
//	@Autowired
//	private ProActService proActService;
//	public static final String SKILLCREDITID=CoreSval.getScoreSkillId();
	public  String SKILLCREDITID="12345";

    /**
     * 跳转表单审核页面.
     * @param request 请求
     * @param response 响应
     * @param model 模型
     * @return String
     */
    @RequestMapping(value = "aform")
    public String aform(HttpServletRequest request, HttpServletResponse response, Model model) {
        ScrAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.GNODE);
        if(!engine.isinit()){
            return CorePages.ERROR_MISS.getIdxUrl();
        }

        /**
         * 判断当前结点是否为第一个审核节点.
         */
        ActYwGnode firstGnode = scrProModelService.getStartNextGnode(engine.apply().iactYw());
        if((firstGnode != null) && (firstGnode.getId()).equals(engine.apply().getIgnodeId())){
            engine.apply().getIamap().setIsfirst(true);
        }

        /**
         * 根据gnodeId得到下一个节点是否为网关，是否需要网关状态.
         */
        List<IAstatus> iastatuss = scrProModelService.getActYwStatus(engine.apply().getIgnodeId());
        if (StringUtil.checkNotEmpty(iastatuss)) {
            engine.apply().getIamap().setIastatuss(iastatuss);
        }

        model = IApply.imodel(engine, model, request, response, Arrays.asList(new FlowAuparam[]{FlowAuparam.ALL}));
		model.addAttribute("scrId", request.getParameter("id"));
		model.addAttribute("SKILLCREDITID", SKILLCREDITID);
        if ((engine.apply().iactYw() == null) || (engine.apply().ignode() == null) || (engine.apply().ignode().igforms() == null)) {
            return CorePages.ERROR_MISS.getIdxUrl();
        }

        IFrorm frorm = IFrorm.gformFst(engine.apply().ignode().igforms(), FormStyleType.FST_FORM);
        if (frorm == null) {
            return CorePages.ERROR_MISS.getIdxUrl();
        }
        return frorm.iform().ipath();
    }

    /**
     *审核提交接口 .
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param model 模型
     * @return String
     */
    @RequestMapping(value = "asave")
    public String asave(HttpServletRequest request, HttpServletResponse response, Model model) {
        return null;
    }

    /**
     * 流程查看接口 .
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param model 模型
     * @return String
     */
    @RequestMapping(value = "vform")
    public String vform(HttpServletRequest request, HttpServletResponse response, Model model) {
         ScrAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.GNODE);
         if(!engine.isinit()){
             return CorePages.ERROR_MISS.getIdxUrl();
         }

         /**
          * 判断当前结点是否为第一个审核节点.
          */
         model = IApply.imodel(engine, model, request, response);
         if ((engine.apply().iactYw() == null) || (engine.apply().iactYw().group() == null)) {
             return CorePages.ERROR_MISS.getIdxUrl();
         }

         String tpath = FormType.genTpath(engine.apply().iactYw(), FormFtype.VIEW, FormClientType.FST_ADMIN);
         if (StringUtil.isEmpty(tpath)) {
             return CorePages.ERROR_MISS.getIdxUrl();
         }
		model.addAttribute("scrId", request.getParameter("id"));
		model.addAttribute("SKILLCREDITID", SKILLCREDITID);
         return tpath;
    }

    /**
     * 跳转表单列表页面.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ApiResult
     */
	@RequestMapping(value = "lform/{iform}")
	public String lform(@PathVariable String iform, HttpServletRequest request, HttpServletResponse response, Model model) {
		ActYwForm actYwForm = actYwFormService.get(iform);
		if ((actYwForm == null) || StringUtils.isEmpty(actYwForm.getPath())) {
			return CorePages.ERROR_MISS.getIdxUrl();
		}

		ScrAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.GNODE);
		if(!engine.isinit()){
		    return CorePages.ERROR_MISS.getIdxUrl();
		}

		if (engine.apply().iactYw() == null) {
            return CorePages.ERROR_MISS.getIdxUrl();
        }
        model = IApply.imodel(engine, model, request, response, Arrays.asList(new FlowAuparam[]{FlowAuparam.LIST_AUDIT}));
        model.addAttribute(IForm.ILFORM_ID, iform);
		if (engine.apply().iactYw().config() != null) {
			//后台审核结果
			showBankMessage(engine.apply().iactYw().config(), model);
		}
		return actYwForm.getPath();
	}

	/**
	 * 审核列表页面获取列表数据.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ApiResult
	 */
   	@ResponseBody
    @RequestMapping(value = "lform/ajaxList")
	public ApiResult lformAjaxList(HttpServletRequest request, HttpServletResponse response) {
   	    IAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.GNODE);
        if(!engine.isinit()){
            return ApiResult.failed(0, "初始化失败！");
        }

        engine = IApply.iobj(engine);
        if (engine.apply().iactYw() == null) {
            return ApiResult.failed(0, "流程不能为空");
        }
		return ApiResult.success(engine.appser().ifindPage(engine));
	}

   	/**
   	 * 审核列表页面删除数据.
   	 * @param request HttpServletRequest
   	 * @param response HttpServletResponse
   	 * @return ApiResult
   	 */
   	@ResponseBody
   	@RequestMapping(value = "lform/ajaxDelpl")
   	public ApiResult lformAjaxDelpl(HttpServletRequest request, HttpServletResponse response) {
   	    IAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.FLOW);
   	    if(!engine.isinit()){
   	        return ApiResult.failed(0, "初始化失败！");
   	    }

   	    engine = IApply.iobj(engine);
   	    if (engine.apply().iactYw() == null) {
   	        return ApiResult.failed(0, "流程不能为空");
   	    }

   	    String ids = request.getParameter(CoreJkey.JK_IDS);
   	    if (StringUtil.isEmpty(ids)) {
   	        return ApiResult.failed(0, "IDS不能为空");
   	    }

   	    engine.appser().idelPl(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
   	    return ApiResult.success("删除成功");
   	}

   	/**
   	 * 查询表单页面.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param model Model
     * @return String
   	 */
   	@RequestMapping(value = "qform")
    public String qform(HttpServletRequest request, HttpServletResponse response, Model model) {
       	 ScrAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.GNODE);
         if(!engine.isinit()){
             return CorePages.ERROR_MISS.getIdxUrl();
         }

         /**
          * 判断当前结点是否为第一个审核节点.
          */
         model = IApply.imodel(engine, model, request, response);
         if ((engine.apply().iactYw() == null) || (engine.apply().iactYw().group() == null)) {
             return CorePages.ERROR_MISS.getIdxUrl();
         }

         String tpath = FormType.genTpath(engine.apply().iactYw(), FormFtype.QUERY, FormClientType.FST_ADMIN);
         if (StringUtil.isEmpty(tpath)) {
             return CorePages.ERROR_MISS.getIdxUrl();
         }
         return tpath;
    }

    @ResponseBody
    @RequestMapping(value = "qform/ajaxList")
    public ApiResult qformAjaxList(HttpServletRequest request, HttpServletResponse response) {
        IAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.GNODE);
        if(!engine.isinit()){
            return ApiResult.failed(0, "初始化失败！");
        }

        engine = IApply.iobj(engine);
        if (engine.apply().iactYw() == null) {
            return ApiResult.failed(0, "流程不能为空");
        }
        return ApiResult.success(engine.appser().ifindQPage(engine));
    }


   	/**
   	 * 申请表单页面.
   	 * TODO 待实现生成统一的申请页面
   	 * @param request HttpServletRequest
   	 * @param response HttpServletResponse
   	 * @param model Model
   	 * @return String
   	 */
   	@RequestMapping(value = "start")
   	public String start(HttpServletRequest request, HttpServletResponse response, Model model) {
   	    IAengine engine = new ScrAengine(new ScoRapply(), request, FlowAutype.FLOWTYPE);
   	    if(!engine.isinit()){
   	        return CorePages.ERROR_MISS.getIdxUrl();
   	    }

   	    /**
   	     * 判断当前结点是否为第一个审核节点.
   	     */
   	    model = IApply.imodel(engine, model, request, response);
   	    if ((engine.apply().iactYw() == null) || (engine.apply().iactYw().group() == null)) {
   	        return CorePages.ERROR_MISS.getIdxUrl();
   	    }

   	    String tpath = FormType.genTpath(engine.apply().iactYw(), FormFtype.START, FormClientType.FST_ADMIN);
   	    if (StringUtil.isEmpty(tpath)) {
   	        return CorePages.ERROR_MISS.getIdxUrl();
   	    }
   	    return tpath;
   	}

   	/****************************************************************************************************************
   	 *
   	 ***************************************************************************************************************/
	@RequestMapping(value = "form/{template}/")
	public String modelFormMiss(@PathVariable String template, HttpServletRequest request, HttpServletResponse response, Model model) {
		return CorePages.ERROR_MISS.getIdxUrl();
	}



	private void showBankMessage(IConfig config, Model model) {}
}
