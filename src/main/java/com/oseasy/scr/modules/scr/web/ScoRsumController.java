package com.oseasy.scr.modules.scr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRsum;
import com.oseasy.scr.modules.scr.service.ScoRsumService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分汇总Controller.
 * @author chenh
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRsum")
public class ScoRsumController extends BaseController {

	@Autowired
	private ScoRsumService entityService;

	@ModelAttribute
	public ScoRsum get(@RequestParam(required=false) String id) {
		ScoRsum entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRsum();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list() {
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRsumList";
	}

	@RequestMapping(value = "listPage", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult listPage(ScoRsum entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
		Page page = entityService.findScoRsumPage(new Page(request, response), entity);
		return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}
}