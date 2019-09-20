package com.oseasy.pie.modules.iep.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pie.common.config.PieSval;
import com.oseasy.pie.common.config.PieSval.PieEmskey;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.service.IepTplService;
import com.oseasy.pie.modules.iep.vo.TplFType;
import com.oseasy.pie.modules.iep.vo.TplFnType;
import com.oseasy.pie.modules.iep.vo.TplLevel;
import com.oseasy.pie.modules.iep.vo.TplOperType;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 模板导入导出Controller.
 * @author chenhao
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/iep/iepTpl")
public class IepTplController extends BaseController {

	@Autowired
	private IepTplService entityService;

	@ModelAttribute
	public IepTpl get(@RequestParam(required=false) String id) {
		IepTpl entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new IepTpl();
		}
		return entity;
	}

	@RequiresPermissions("iep:iepTpl:view")
	@RequestMapping(value = {"list", ""})
	public String list(IepTpl entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<IepTpl> list = entityService.findList(entity);
		model.addAttribute("list", list);
		model.addAttribute(TplType.TPL_TYPES, Arrays.asList(TplType.values()));
		model.addAttribute(TplOperType.TPL_OPERTYPES, Arrays.asList(TplOperType.values()));
		model.addAttribute(TplFType.TPL_FTYPES, Arrays.asList(TplFType.values()));
		model.addAttribute(TplFnType.TPL_FNTYPES, Arrays.asList(TplFnType.values()));
		model.addAttribute(TplLevel.TPL_LEVELS, Arrays.asList(TplLevel.values()));
		return PieSval.path.vms(PieEmskey.IEP.k()) + "iepTplList";
	}

	@ResponseBody
	@RequestMapping(value = {"ajaxIepTpl"})
	public IepTpl ajaxIepTpl(IepTpl entity, HttpServletRequest request, HttpServletResponse response, Model model) {
	    List<IepTpl> entitys = entityService.findTreeById(entity);
	    IepTpl.treeList(entity, entitys, true);
	    return entity;
	}

	@RequestMapping(value = {"stepList"})
	public String stepList(IepTpl entity, HttpServletRequest request, HttpServletResponse response, Model model) {
	    List<IepTpl> entitys = entityService.findTreeById(entity);
	    IepTpl.treeList(entity, entitys, true);
	    model.addAttribute(IepTpl.IEPTPL, entity);
		model.addAttribute(IActYw.IACTYW_ID, request.getParameter(IActYw.IACTYW_ID));
		model.addAttribute(IGnode.IGNODE_ID, request.getParameter(IGnode.IGNODE_ID));
		model.addAttribute(TplOperType.TPL_OPERTYPE, request.getParameter(TplOperType.TPL_OPERTYPE));
	    return PieSval.path.vms(PieEmskey.IEP.k()) + "iepTplStepList";
	}

	@RequiresPermissions("iep:iepTpl:view")
	@RequestMapping(value = "form")
	public String form(IepTpl entity, Model model) {
		if (entity.getParent()!=null && StringUtil.isNotBlank(entity.getParent().getId())){
			entity.setParent(entityService.get(entity.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtil.isBlank(entity.getId())){
				IepTpl entityChild = new IepTpl();
				entityChild.setParent(new IepTpl(entity.getParent().getId()));
				List<IepTpl> list = entityService.findList(entity);
				if (list.size() > 0){
					entity.setSort(list.get(list.size()-1).getSort());
					if (entity.getSort() != null){
						entity.setSort(entity.getSort() + 30);
					}
				}
			}
		}
		if (entity.getSort() == null){
			entity.setSort(30);
		}
		model.addAttribute("iepTpl", entity);
        return PieSval.path.vms(PieEmskey.IEP.k()) + "iepTplForm";
	}

	@RequiresPermissions("iep:iepTpl:edit")
	@RequestMapping(value = "save")
	public String save(IepTpl entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存模板导入导出成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/iep/iepTpl/?repage";
	}

	@RequiresPermissions("iep:iepTpl:edit")
	@RequestMapping(value = "delete")
	public String delete(IepTpl entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除模板导入导出成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/iep/iepTpl/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<IepTpl> list = entityService.findList(new IepTpl());
		for (int i=0; i<list.size(); i++){
			IepTpl e = list.get(i);
			if (StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

}