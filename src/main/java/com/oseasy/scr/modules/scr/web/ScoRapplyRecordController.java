package com.oseasy.scr.modules.scr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.scr.modules.scr.service.ScoRapplyRecordService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分申请记录Controller.
 * @author chenh
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRapplyRecord")
public class ScoRapplyRecordController extends BaseController {

	@Autowired
	private ScoRapplyRecordService entityService;

	@ModelAttribute
	public ScoRapplyRecord get(@RequestParam(required=false) String id) {
		ScoRapplyRecord entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRapplyRecord();
		}
		return entity;
	}

	@RequiresPermissions("scr:scoRapplyRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(ScoRapplyRecord entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoRapplyRecord> page = entityService.findPage(new Page<ScoRapplyRecord>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyRecordList";
	}

	@RequiresPermissions("scr:scoRapplyRecord:view")
	@RequestMapping(value = "form")
	public String form(ScoRapplyRecord entity, Model model) {
		model.addAttribute("scoRapplyRecord", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyRecordForm";
	}

	@RequiresPermissions("scr:scoRapplyRecord:edit")
	@RequestMapping(value = "save")
	public String save(ScoRapplyRecord entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存学分申请记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapplyRecord/?repage";
	}

	@RequiresPermissions("scr:scoRapplyRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(ScoRapplyRecord entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除学分申请记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapplyRecord/?repage";
	}

    /**
     * 获取审核记录列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(ScoRapplyRecord entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<ScoRapplyRecord> pag = new Page<ScoRapplyRecord>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(ScoRapplyRecord.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE);
                pag.setOrderByType(Page.ORDER_DESC);
            }
            return ApiResult.success(entityService.findPage(new Page<ScoRapplyRecord>(request, response), entity));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}