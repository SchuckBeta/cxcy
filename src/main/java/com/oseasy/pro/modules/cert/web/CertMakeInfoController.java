package com.oseasy.pro.modules.cert.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.cert.entity.CertMakeInfo;
import com.oseasy.pro.modules.cert.service.CertMakeInfoService;
import com.oseasy.pro.modules.cert.service.CertMakeService;
import com.oseasy.pro.modules.cert.service.SysCertService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 下发证书进度信息Controller.
 * @author 奔波儿灞
 * @version 2018-03-02
 */
@Controller
public class CertMakeInfoController extends BaseController {
	public final static Logger logger = Logger.getLogger(CertMakeInfoController.class);
	@Autowired
	private CertMakeService certMakeService;
	@Autowired
	private CertMakeInfoService certMakeInfoService;
	@Autowired
	private SysCertService sysCertService;
//	@RequiresPermissions("cert:sysCert:make")
	@RequestMapping(value = {"${adminPath}/certMake/list"})
	public String list(String actywId,String referrer, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("certList", sysCertService.getCertListWithFlow(actywId));
		String reqreferer = referrer;
		if(StringUtil.isEmpty(referrer)){
			reqreferer=request.getHeader("referer");
		}else{
			try {
				reqreferer=URLDecoder.decode(StringEscapeUtils.unescapeHtml4(reqreferer), "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
			}
		}
		model.addAttribute("actywId", actywId);
		model.addAttribute("referrer", reqreferer);
		CertMakeInfo certMakeInfo =new CertMakeInfo();
		try {
			model.addAttribute("encodereferrer", URLEncoder.encode(URLEncoder.encode(reqreferer, "utf-8"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		Page<CertMakeInfo> page = certMakeInfoService.findPage(new Page<CertMakeInfo>(request, response), certMakeInfo);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.CERT.k()) + "certMakeList";
	}

	@RequestMapping(value = {"${adminPath}/certMake/getCertMakeList"}, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getCertMakeList(HttpServletRequest request, HttpServletResponse response){
		CertMakeInfo certMakeInfo =new CertMakeInfo();
		Page<CertMakeInfo> page = certMakeInfoService.findPage(new Page<CertMakeInfo>(request, response), certMakeInfo);
		return ApiResult.success(page);
	}


	@RequestMapping(value = {"${adminPath}/certMake/delCert"}, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult delCert(CertMakeInfo certMakeInfo,String referrer, HttpServletRequest request, HttpServletResponse response){
		try {
			certMakeInfoService.delete(certMakeInfo);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequiresPermissions("cert:sysCert:make")
	@RequestMapping(value = "${adminPath}/certMake/delete")
	public String delete(CertMakeInfo certMakeInfo,String referrer, RedirectAttributes redirectAttributes) {
		certMakeInfoService.delete(certMakeInfo);
		addMessage(redirectAttributes, "删除成功");
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/certMake/list/?repage&referrer="+referrer;
	}
	@RequestMapping(value = "${adminPath}/certMake/getCertMakeInfo")
	@ResponseBody
	public CertMakeInfo getCertMakeInfo(HttpServletRequest request, HttpServletResponse response) {
		String id=request.getParameter("id");
		return certMakeInfoService.getCertMakeInfo(id);
	}
	@RequestMapping(value = "${adminPath}/certMake/doCertMake")
	@ResponseBody
	@RequiresPermissions("cert:sysCert:make")
	public JSONObject doCertMake(String actywId,String certid) {
		try {
			return certMakeService.doCertMake(actywId, certid);
		} catch (Exception e) {
			JSONObject obj = new JSONObject();
			obj.put("msg", "证书下发出错,系统异常");
			obj.put("ret", 0);
			return obj;
		}
	}
}