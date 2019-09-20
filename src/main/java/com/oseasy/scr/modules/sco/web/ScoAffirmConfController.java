package com.oseasy.scr.modules.sco.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.entity.ScoAffirmConf;
import com.oseasy.scr.modules.sco.service.ScoAffirmConfService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 学分认定配置Controller.
 * @author 9527
 * @version 2017-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/sco/scoAffirmConf")
public class ScoAffirmConfController extends BaseController {

	@Autowired
	private ScoAffirmConfService scoAffirmConfService;
  @Autowired
  private ActYwService actYwService;

	@ModelAttribute
	public ScoAffirmConf get(@RequestParam(required=false) String id) {
		ScoAffirmConf entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = scoAffirmConfService.get(id);
		}
		if (entity == null) {
			entity = new ScoAffirmConf();
		}
		return entity;
	}
	@RequiresPermissions("sco:scoAffirmConf:view")
	@RequestMapping(value = {"list", ""})
	public String list(ScoAffirmConf scoAffirmConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ScoAffirmConf> list= scoAffirmConfService.findAll();
		Map<String,List<ScoAffirmConf>> map=new LinkedHashMap<String,List<ScoAffirmConf>>();
		if (list!=null) {
			for(ScoAffirmConf sc:list) {
				if (map.get(sc.getType())==null) {
					List<ScoAffirmConf> temlist=new ArrayList<ScoAffirmConf>();
					temlist.add(sc);
					map.put(sc.getType(), temlist);
				}else{
					map.get(sc.getType()).add(sc);
				}
			}
		}

		List<ActYw> actYws = actYwService.findListByDeploy(FlowType.FWT_SCORE);
		model.addAttribute("actYws", actYws);
		model.addAttribute("map",map);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoAffirmConfList";
	}
	/*@RequiresPermissions("sco:scoAffirmConf:view")
	@RequestMapping(value = {"list"})
	public String list(ScoAffirmConf scoAffirmConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoAffirmConf> page = scoAffirmConfService.findPage(new Page<ScoAffirmConf>(request, response), scoAffirmConf);
		model.addAttribute("page", page);
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoAffirmConfList";
	}*/

	@RequiresPermissions("sco:scoAffirmConf:view")
	@RequestMapping(value = "form")
	public String form(ScoAffirmConf scoAffirmConf, Model model) {
		model.addAttribute("scoAffirmConf", scoAffirmConf);
		Set<String> pset=scoAffirmConfService.getTypeSetData("1,");
		Set<String> gset=scoAffirmConfService.getTypeSetData("7,");
		if (pset!=null&&pset.size()>0) {
			List<Dict> l2=scoAffirmConfService.getDictForScoAffirm(pset,"project_style");
			model.addAttribute("project_style", l2);
		}
		if (gset!=null&&gset.size()>0) {
			List<Dict> l2=scoAffirmConfService.getDictForScoAffirm(gset,"competition_type");
			model.addAttribute("competition_type", l2);
		}
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoAffirmConfForm";
	}

	@RequiresPermissions("sco:scoAffirmConf:edit")
	@RequestMapping(value = "save")
	public String save(ScoAffirmConf scoAffirmConf, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, scoAffirmConf)) {
			return form(scoAffirmConf, model);
		}
		if (scoAffirmConfService.check(scoAffirmConf.getId(), null,
				scoAffirmConf.getItem(), scoAffirmConf.getCategory(), scoAffirmConf.getSubdivision())>0) {
			model.addAttribute("message", "保存失败,存在相同类型的认定项目");
			return form(scoAffirmConf, model);
		}
		scoAffirmConfService.save(scoAffirmConf);
		addMessage(redirectAttributes, "保存学分认定配置成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sco/scoAffirmConf/?repage";
	}

	@RequiresPermissions("sco:scoAffirmConf:edit")
	@RequestMapping(value = "delete")
	public String delete(ScoAffirmConf scoAffirmConf, RedirectAttributes redirectAttributes) {
		scoAffirmConfService.delete(scoAffirmConf);
		addMessage(redirectAttributes, "删除学分认定配置成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sco/scoAffirmConf/?repage";
	}
	@RequestMapping(value = "check")
	@ResponseBody
	public boolean check(String id,String type,String item,String category,String subdivision) {
		if (scoAffirmConfService.check(id, null, item, category, subdivision)>0) {
			return false;
		}
		return true;
	}
	@RequestMapping(value = "updateProc")
	@ResponseBody
	public JSONObject updateProc(HttpServletRequest request) {
		String id=request.getParameter("id");
		String proc=request.getParameter("proc");
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		js.put("msg", "设置成功");
		try {
			scoAffirmConfService.updateProc(id,proc);
		} catch (Exception e) {
			js.put("ret", "0");
			js.put("msg", "设置失败");
		}
		return js;
	}
	@RequestMapping(value = "getSetData")
	@ResponseBody
	public Set<String> getSetData(String item,String category,HttpServletRequest request) {
		if ("1,".equals(item)&&"1".equals(category)) {//大创项目
			List<Dict> l=DictUtils.getDictList(FlowPcategoryType.PCT_XM.getKey());
			Set<String> set =new HashSet<String>();
			if (l!=null&&l.size()>0) {
				for(Dict d:l) {
					set.add(d.getValue());
				}
			}
			return set;
		}
		if ("7,".equals(item)&&"1".equals(category)) {//互联网+大赛
			List<Dict> l=DictUtils.getDictList("competition_net_type");
			Set<String> set =new HashSet<String>();
			if (l!=null&&l.size()>0) {
				for(Dict d:l) {
					set.add(d.getValue());
				}
			}
			return set;
		}
		return scoAffirmConfService.getSetData(item, category);
	}
}