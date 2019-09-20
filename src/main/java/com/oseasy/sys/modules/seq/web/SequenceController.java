package com.oseasy.sys.modules.seq.web;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.seq.entity.Sequence;
import com.oseasy.sys.modules.seq.service.SequenceService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 序列表Controller.
 * @author zy
 * @version 2018-10-08
 */
@Controller
@RequestMapping(value = "${adminPath}/seq/sequence")
public class SequenceController extends BaseController {

	@Autowired
	private SequenceService entityService;

	@ModelAttribute
	public Sequence get(@RequestParam(required=false) String id) {
		Sequence entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new Sequence();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(Sequence entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Sequence> page = entityService.findPage(new Page<Sequence>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return SysSval.path.vms(SysEmskey.SEQ.k()) + "sequenceList";
	}

	@RequestMapping(value = "form")
	public String form(Sequence entity, Model model) {
		model.addAttribute("sequence", entity);
		return SysSval.path.vms(SysEmskey.SEQ.k()) + "sequenceForm";
	}


	@RequestMapping(value = "save")
	public String save(Sequence entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存序列表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/seq/sequence/?repage";
	}


	@RequestMapping(value = "delete")
	public String delete(Sequence entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除序列表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/seq/sequence/?repage";
	}

	@ResponseBody
   	@RequestMapping(value = "/addSeq", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
   	public String addSeq(Sequence seq,HttpServletRequest request, HttpServletResponse response, String name ) {
       	String seqName=request.getParameter("seqName");
       	String startNum=request.getParameter("startNum");
       	String updateNum=request.getParameter("updateNum");
       	Map<String,Object> param=new HashMap<String,Object>();
       	param.put("seqName",seqName);
       	param.put("startNum", startNum);
       	param.put("updateNum", updateNum);
		entityService.save(seq);
		return SysSval.path.vms(SysEmskey.SEQ.k()) + "testSeqPage";
   	}

//   	@ResponseBody
   	@RequestMapping(value = "/getNextSeq", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
   	public String getNextSeq(HttpServletRequest request, HttpServletResponse response, String name ) {
		String num=entityService.nextSequence(name);
       	return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/seq/sequence/?repage";
	}
}