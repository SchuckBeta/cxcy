package com.oseasy.pro.modules.cert.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.entity.SysCert;
import com.oseasy.pro.modules.cert.entity.SysCertFlow;
import com.oseasy.pro.modules.cert.entity.SysCertPage;
import com.oseasy.pro.modules.cert.enums.ColEnum;
import com.oseasy.pro.modules.cert.service.SysCertFlowService;
import com.oseasy.pro.modules.cert.service.SysCertPageService;
import com.oseasy.pro.modules.cert.service.SysCertService;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;
import com.oseasy.pro.modules.cert.vo.SysCertPageInsVo;
import com.oseasy.pro.modules.cert.vo.SysCertVo;
import com.oseasy.util.common.exception.UtilRunException;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 证书模板Controller.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Controller
public class SysCertController extends BaseController {
	@Autowired
	private SysCertPageService sysCertPageService;
	@Autowired
	private SysCertService sysCertService;
	@Autowired
	private SysCertFlowService sysCertFlowService;
	@ModelAttribute
	public SysCert get(@RequestParam(required=false) String id) {
		SysCert entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysCertService.get(id);
		}
		if (entity == null){
			entity = new SysCert();
		}
		return entity;
	}
	//证书模板分页查询
	@RequiresPermissions("cert:sysCert:view")
	@RequestMapping(value = {"${adminPath}/cert/list"})
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysCertVo> page = sysCertService.findPageVo(new Page<SysCertVo>(request, response), new SysCertVo());
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.CERT.k()) + "sysCertList";
	}

	//列表接口
	@RequestMapping(value="a/cert/certList")
	@ResponseBody
	public ApiResult certList(HttpServletRequest request, HttpServletResponse response, Model model){
		Page<SysCertVo> page = sysCertService.findPageVo(new Page<SysCertVo>(request, response), new SysCertVo());
		return ApiResult.success(page);
	}


	//根据项目id修改证书名称
	@RequestMapping(value="a/cert/editSysCertName")
	@ResponseBody
	public ApiResult editSysCertName(SysCertVo cert,HttpServletRequest request, HttpServletResponse response){
		sysCertService.editSysCertName(cert);
		return ApiResult.success();
	}

	//根据关联项目id取消关联节点
	@RequestMapping(value="a/cert/clearSysCertFlow")
	@ResponseBody
	public ApiResult clearCert(SysCertFlow scf,HttpServletRequest request, HttpServletResponse response){
		sysCertFlowService.deleteByFlow(scf);
		return ApiResult.success();
	}

	//根据id获取关联节点
	@RequestMapping(value="a/cert/SysCertFlowVo")
	@ResponseBody
	public ApiResult getSysCertFlowVo(String  id,HttpServletRequest request, HttpServletResponse response){
		SysCertFlowVo scf = sysCertFlowService.getCertFlowVo(id);
		return ApiResult.success(scf);
	}

	//证书模板全部查询
	@RequiresPermissions("cert:sysCert:view")
	@RequestMapping(value = {"${adminPath}/cert/allList"})
	public String allList(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SysCertVo> list = sysCertService.findAllList();
		model.addAttribute("list", list);
		String url=ProSval.path.vms(ProEmskey.CERT.k()) + "sysCertList";
		return url;
	}
	//证书模板编辑页面
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = {"${adminPath}/cert/form"})
	public String form(HttpServletRequest request, HttpServletResponse response, Model model) {
		String certid=request.getParameter("certid");
		String certpageid=request.getParameter("certpageid");
		if(StringUtil.isNotEmpty(certid)){
			SysCert sc=sysCertService.get(certid);
			if(sc!=null){
				model.addAttribute("certname", sc.getName());
			}
		}
		String certpagename = request.getParameter("certpagename");;
		if(StringUtil.isNotEmpty(certpageid)){
			SysCertPage scp=sysCertPageService.get(certpageid);
			if(scp!=null){
				certpagename = scp.getName();
			}
			if(StringUtil.isEmpty(certpagename)){
				certpagename = IdGen.uuid();
			}
		}
		model.addAttribute("certpagename", certpagename);
		model.addAttribute("certid", certid);
		model.addAttribute("certpageid", certpageid);
		JSONArray jsonArray = new JSONArray();
        for (ColEnum e : ColEnum.values()) {
            JSONObject object = new JSONObject();
            object.put("value", e.getValue());
            object.put("name", e.getName());
            jsonArray.add(object);
        }
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}

		model.addAttribute("colsJson", jsonArray.toString());
		return ProSval.path.vms(ProEmskey.CERT.k()) + "sysCertForm";
	}
	//编辑证书模板页面时获取数据
	@RequiresPermissions("cert:sysCert:view")
	@RequestMapping(value = "${adminPath}/cert/getCertPage")
	@ResponseBody
	public CertPage getCertPage(String pageid) {
		return sysCertService.getCertPage(pageid);
	}
	@RequestMapping(value = "${adminPath}/cert/getCertIns")
	@ResponseBody
	public List<SysCertPageInsVo> getCertInsAdmin(String certinsid) {
		return sysCertService.getCertIns(certinsid);
	}
	@RequestMapping(value = "${frontPath}/cert/getCertIns")
	@ResponseBody
	public List<SysCertPageInsVo> getCertInsFront(String certinsid) {
		return sysCertService.getCertIns(certinsid);
	}
	//预览证书模板页面数据
	@RequiresPermissions("cert:sysCert:view")
	@RequestMapping(value = "${adminPath}/cert/preview",method=RequestMethod.POST)
	public void preview(String data,HttpServletResponse response) {
		try {
			String jsonstr=StringEscapeUtils.unescapeHtml4(data);
			sysCertService.preview(JSONObject.fromObject(jsonstr), response);
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	}
	//保存证书模板页面数据
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/savePageName",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject savePageName(String pageid,String pagename) {
		JSONObject js=new JSONObject();
		try {
			sysCertService.savePageName(pageid, pagename);
			js.put("ret", 1);
			js.put("msg", "保存成功");
			return js;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "保存失败，系统错误");
			return js;
		}
	}
	//保存证书模板页面数据
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/save",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject save(@RequestBody JSONObject requestBody) {
		JSONObject js=new JSONObject();
		try {
			Map<String, String> map=sysCertService.saveCert(requestBody);
			js.put("ret", 1);
			js.put("msg", "保存成功");
			js.put("certid", map.get("certid"));
			js.put("certpageid", map.get("certpageid"));
			return js;
		} catch (UtilRunException e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", e.getMessage());
			return js;
		}catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "保存失败，系统错误");
			return js;
		}
	}
	//删除证书模板
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/deleteAll",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteAll(String ids) {
		JSONObject js=new JSONObject();
		if(StringUtil.isEmpty(ids)){
			js.put("ret", 0);
			js.put("msg", "删除失败，参数错误");
			return js;
		}
		try {
			int c=sysCertService.deleteAll(ids);
			if(c!=0){
				js.put("ret", 1);
				js.put("msg", "删除成功");
			}else{
				js.put("ret", 0);
				js.put("msg", "删除失败，不能删除已发布的证书");
			}
			return js;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "删除失败，系统错误");
			return js;
		}
	}
	//删除证书模板页
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/deleteCertPage",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteCertPage(String id) {
		JSONObject js=new JSONObject();
		if(StringUtil.isEmpty(id)){
			js.put("ret", 0);
			js.put("msg", "删除失败，参数错误");
			return js;
		}
		SysCertPage scp=sysCertPageService.get(id);
		if(scp==null){
			js.put("ret", 0);
			js.put("msg", "数据已被删除");
			return js;
		}
		SysCert sc=sysCertService.get(scp.getCertId());
		if(sc==null){
			js.put("ret", 0);
			js.put("msg", "数据已被删除");
			return js;
		}
		if(Const.YES.equals(sc.getReleases())){
			js.put("ret", 0);
			js.put("msg", "删除失败，不能删除已发布的证书");
			return js;
		}
		try {
			sysCertService.deleteCertPage(id);
			js.put("ret", 1);
			js.put("msg", "删除成功");
			return js;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "删除失败，系统错误");
			return js;
		}
	}
	//保存证书模板关联
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/saveCertFlow",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject saveCertFlow(SysCertFlowVo vo) {
		JSONObject js=new JSONObject();
		try {
			if(StringUtil.isEmpty(vo.getFlow())||StringUtil.isEmpty(vo.getNode())||StringUtil.isEmpty(vo.getCertId())){
				js.put("ret", 0);
				js.put("msg", "添加失败，参数错误");
				return js;
			}else if(sysCertFlowService.findByCdn(vo.getFlow(), vo.getNode())>0){
				js.put("ret", 0);
				js.put("msg", "添加失败，该节点已关联证书");
				return js;
			}else{
				SysCertFlow a=new SysCertFlow();
				a.setCertId(vo.getCertId());
				a.setFlow(vo.getFlow());
				a.setNode(vo.getNode());
				sysCertFlowService.save(a);
				js.put("sysCertFlowId",a.getId());
				js.put("ret", 1);
				js.put("msg", "添加成功");
				return js;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "添加失败，系统错误");
			return js;
		}

	}
	//删除证书模板关联
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/delCertFlow")
	@ResponseBody
	public JSONObject delCertFlow(String id) {
		JSONObject js=new JSONObject();
		try {
			sysCertFlowService.deleteWL(new SysCertFlow(id));
			js.put("ret", 1);
			js.put("msg", "删除成功");
			return js;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "删除失败，系统错误");
			return js;
		}

	}
	//发布证书模板
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/release")
	@ResponseBody
	public JSONObject release(String id) {
		JSONObject js=new JSONObject();
		try {
			sysCertService.release(id);
			js.put("ret", 1);
			js.put("msg", "发布成功");
			return js;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "发布失败，系统错误");
			return js;
		}

	}
	//取消发布证书模板
	@RequiresPermissions("cert:sysCert:edit")
	@RequestMapping(value = "${adminPath}/cert/unrelease")
	@ResponseBody
	public JSONObject unrelease(String id) {
		JSONObject js=new JSONObject();
		try {
			sysCertService.unrelease(id);
			js.put("ret", 1);
			js.put("msg", "取消发布成功");
			return js;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", 0);
			js.put("msg", "取消失败，系统错误");
			return js;
		}

	}
}