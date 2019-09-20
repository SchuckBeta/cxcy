package com.oseasy.pie.modules.impdata.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.common.config.PieSval;
import com.oseasy.pie.common.config.PieSval.PieEmskey;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.PmgMemsError;
import com.oseasy.pie.modules.impdata.entity.PmgTeasError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.enums.BackUserColEnum;
import com.oseasy.pie.modules.impdata.enums.GcontestColEnum;
import com.oseasy.pie.modules.impdata.enums.OfficeColEnum;
import com.oseasy.pie.modules.impdata.enums.ProModelColEnum;
import com.oseasy.pie.modules.impdata.enums.ProjectColEnum;
import com.oseasy.pie.modules.impdata.enums.ProjectHsColEnum;
import com.oseasy.pie.modules.impdata.enums.ProjectMdApprovalColEnum;
import com.oseasy.pie.modules.impdata.enums.ProjectMdCloseColEnum;
import com.oseasy.pie.modules.impdata.enums.ProjectMdMidColEnum;
import com.oseasy.pie.modules.impdata.enums.StudentColEnum;
import com.oseasy.pie.modules.impdata.enums.TeacherColEnum;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.BackUserErrorService;
import com.oseasy.pie.modules.impdata.service.GcontestErrorService;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.ImpExpService;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;
import com.oseasy.pie.modules.impdata.service.ImpInfoService;
import com.oseasy.pie.modules.impdata.service.OfficeErrorService;
import com.oseasy.pie.modules.impdata.service.PmgMemsErrorService;
import com.oseasy.pie.modules.impdata.service.PmgTeasErrorService;
import com.oseasy.pie.modules.impdata.service.ProMdApprovalErrorService;
import com.oseasy.pie.modules.impdata.service.ProMdCloseErrorService;
import com.oseasy.pie.modules.impdata.service.ProMdMidErrorService;
import com.oseasy.pie.modules.impdata.service.ProModelErrorService;
import com.oseasy.pie.modules.impdata.service.ProModelGcontestErrorService;
import com.oseasy.pie.modules.impdata.service.ProjectErrorService;
import com.oseasy.pie.modules.impdata.service.ProjectHsErrorService;
import com.oseasy.pie.modules.impdata.service.StudentErrorService;
import com.oseasy.pie.modules.impdata.service.TeacherErrorService;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.pie.modules.impdata.tool.IitTplData;
import com.oseasy.pie.modules.impdata.tool.data.TplGcontestGJ;
import com.oseasy.pie.modules.impdata.tool.down.ItDnBackUser;
import com.oseasy.pie.modules.impdata.tool.down.ItDnConStu;
import com.oseasy.pie.modules.impdata.tool.down.ItDnHlwGcontest;
import com.oseasy.pie.modules.impdata.tool.down.ItDnOrg;
import com.oseasy.pie.modules.impdata.tool.down.ItDnProModelGcontest;
import com.oseasy.pie.modules.impdata.tool.down.ItDnProModelPro;
import com.oseasy.pie.modules.impdata.tool.down.ItDnProjectClose;
import com.oseasy.pie.modules.impdata.tool.down.ItDnProjectHs;
import com.oseasy.pie.modules.impdata.tool.down.ItDnStudent;
import com.oseasy.pie.modules.impdata.tool.down.ItDnTeacher;
import com.oseasy.pie.modules.impdata.tool.engine.ItDrEngine;
import com.oseasy.pie.modules.impdata.tool.engine.ItEngine;
import com.oseasy.pie.modules.impdata.tool.param.ItParam;
import com.oseasy.pie.modules.impdata.tool.param.ItParamDrCard;
import com.oseasy.pie.modules.impdata.tool.service.ItDrService;
import com.oseasy.pie.modules.impdata.tool.service.ItService;
import com.oseasy.pie.modules.impdata.tool.tpl.IitAbsTpl;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXsheet;
import com.oseasy.pie.modules.impdata.vo.ImpDataVo;
import com.oseasy.pie.modules.impdata.vo.ImpDataVo.Builder;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 导入数据Controller
 *
 * @author 9527
 * @version 2017-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/impdata")
public class ImpDataController extends BaseController {
    private static final String TYPE = "type";
    private static final String REFERER = "referer";
    private static final String REFERRER = "referrer";
    private static final String ENCODEREFERRER = "encodereferrer";

    public final static Logger logger = Logger.getLogger(ImpDataController.class);
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private ImpDataService impDataService;
	@Autowired
	private ImpInfoService impInfoService;
	@Autowired
	private StudentErrorService studentErrorService;
	@Autowired
	private TeacherErrorService teacherErrorService;
	@Autowired
	private BackUserErrorService backUserErrorService;
	@Autowired
	private OfficeErrorService officeErrorService;
	@Autowired
	private ProjectErrorService projectErrorService;
	@Autowired
	private ImpInfoErrmsgService impInfoErrmsgService;
	@Autowired
	private ProMdApprovalErrorService proMdApprovalErrorService;
	@Autowired
	private ProMdMidErrorService proMdMidErrorService;
	@Autowired
	private ProMdCloseErrorService proMdCloseErrorService;
	@Autowired
	private ProjectHsErrorService projectHsErrorService;
	@Autowired
	private GcontestErrorService gcontestErrorService;
	@Autowired
	private ProModelErrorService proModelErrorService;
	@Autowired
	private ActYwGnodeService actYwGnodeService;
	@Autowired
	private ProModelGcontestErrorService proModelGcontestErrorService;
	@Autowired
	private PmgMemsErrorService pmgMemsErrorService;
	@Autowired
	private PmgTeasErrorService pmgTeasErrorService;
	@Autowired
	private ItService itService;
	@Autowired
	private ItDrService itDrService;
	@ModelAttribute
	public ImpInfo get(@RequestParam(required=false) String id) {
		ImpInfo entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = impInfoService.get(id);
		}
		if (entity == null) {
			entity = new ImpInfo();
		}
		return entity;
	}
	@RequestMapping(value = { "list", "" })
	public String list(ImpInfo impInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		User user = UserUtils.getUser();
//		param.put("userid", user.getId());
//		Page<Map<String, String>> page = impInfoService.getList(new Page<Map<String, String>>(request, response),
//				param);
//		model.addAttribute(Page.PAGE, page);
		return PieSval.path.vms(PieEmskey.IMPDATA.k()) + "impdataList";
	}

	@RequestMapping(value="getImpdataList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getImpdataList(HttpServletRequest request, HttpServletResponse response){
	    try {
            Map<String, Object> param = new HashMap<String, Object>();
            User user = UserUtils.getUser();
            param.put("userid", user.getId());
            Page<Map<String, String>> page = impInfoService.getList(new Page<Map<String, String>>(request, response),
                    param);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	@RequestMapping(value = { "promodellist"})
	public String promodellist(ImpInfo impInfo, String actywId, String gnodeId, String isTrue, String tplType, String referrer, HttpServletRequest request, HttpServletResponse response, Model model) {
	    getProModelList(impInfo, actywId, gnodeId, isTrue, tplType, referrer, request, response, model);
		return PieSval.path.vms(PieEmskey.IMPDATA.k()) + "impProModelList";
	}
    /*自定义大赛导入*/
    @RequestMapping(value = { "promodelgcontestlist"})
    public String promodelgcontestlist(ImpInfo impInfo, String actywId, String gnodeId, String isTrue, String tplType, String referrer, HttpServletRequest request, HttpServletResponse response, Model model) {
        getProModelList(impInfo, actywId, gnodeId, isTrue, tplType, referrer, request, response, model);
        return PieSval.path.vms(PieEmskey.IMPDATA.k()) + "impProModelGcontestList";
    }

    @RequestMapping(value = "getImpProModelList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> getImpProModelList(ImpInfo impInfo, String actywId, String gnodeId, String isTrue,String referrer, HttpServletRequest request, HttpServletResponse response, Model model){
        return ajaxGetImpProModelList(impInfo, actywId, gnodeId, isTrue, referrer, request, response);
    }

    /**
     * 处理Request 的REFERER参数和转码参数.
     * @param referrer
     * @param request
     * @return
     */
    private Builder dealReferrer(String referrer, HttpServletRequest request) {
        Builder builder = new ImpDataVo.Builder();
        builder.referrer(referrer);
        if (StringUtil.isEmpty(referrer)) {
            builder.referrer(request.getHeader(REFERER));
        }else{
            try {
                builder.referrer(URLDecoder.decode(StringEscapeUtils.unescapeHtml4(referrer), StringUtil.UTF_8));
            } catch (UnsupportedEncodingException e) {
                logger.error(e);
            }
        }

        try {
            builder.referer(builder.getReferrer());
            builder.encodereferrer(URLEncoder.encode(URLEncoder.encode(builder.getReferrer(), StringUtil.UTF_8), StringUtil.UTF_8));
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return builder;
    }

    /**
     * 同步加载导入信息.
     * @param impInfo
     * @param actywId
     * @param gnodeId
     * @param isTrue
     * @param referrer
     * @param request
     * @param response
     * @param model
     */
    private void getProModelList(ImpInfo impInfo, String actywId, String gnodeId, String isTrue, String tplType, String referrer,
            HttpServletRequest request, HttpServletResponse response, Model model) {
        Builder builder = dealReferrer(referrer, request);
        builder.actywid(actywId);
        ImpDataVo dataVo = builder.build();
        model.addAttribute(REFERRER, dataVo.getReferrer());
        model.addAttribute(ENCODEREFERRER, dataVo.getEncodereferrer());

		impInfo.setActywid(actywId);
		Page<ImpInfo> page = impInfoService.getProModelList(new Page<ImpInfo>(request, response), impInfo);
		model.addAttribute(Page.PAGE, page);
		model.addAttribute(ActYwGroup.JK_ACTYW_ID, actywId);
		model.addAttribute(ActYwGroup.JK_GNODE_ID, gnodeId);
		model.addAttribute(CoreJkey.JK_IS_TRUE, isTrue);
		if(StringUtil.isNotEmpty(tplType)){
		    //国家级模板导入
	        model.addAttribute(TplType.TPL_TYPE, tplType);
		}else{
		    //双创模板导入
	        model.addAttribute(TplType.TPL_TYPE, 0);
		}
    }

    /**
     * 异步加载导入信息.
     * @param impInfo
     * @param actywId
     * @param gnodeId
     * @param isTrue
     * @param referrer
     * @param request
     * @param response
     * @return
     */
    private ApiTstatus<HashMap<String, Object>> ajaxGetImpProModelList(ImpInfo impInfo, String actywId, String gnodeId, String isTrue, String referrer,
            HttpServletRequest request, HttpServletResponse response) {
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus<HashMap<String, Object>>(false, "没有数据");
        HashMap<String, Object> hashMap = new HashMap<>();
        Builder builder = dealReferrer(referrer, request);
        builder.actywid(actywId);
        ImpDataVo dataVo = builder.build();
        hashMap.put(REFERRER, dataVo.getReferrer());
        hashMap.put(ENCODEREFERRER, dataVo.getEncodereferrer());

        impInfo.setActywid(actywId);
        Page<ImpInfo> page = impInfoService.getProModelList(new Page<ImpInfo>(request, response), impInfo);
        hashMap.put(Page.PAGE, page);
        hashMap.put(ActYwGroup.JK_ACTYW_ID, actywId);
        hashMap.put(ActYwGroup.JK_GNODE_ID, gnodeId);
        hashMap.put(CoreJkey.JK_IS_TRUE, isTrue);
        actYwRstatus.setMsg("查询成功");
        actYwRstatus.setStatus(true);
        actYwRstatus.setDatas(hashMap);
        return actYwRstatus;
    }

	@RequestMapping(value = { "mdlist"})
	public String mdlist(ImpInfo impInfo, String type, String actywId, String gnodeId, String referrer, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Builder builder = dealReferrer(referrer, request);
        builder.type(type);
        builder.actywid(actywId);
        builder.gnodeId(gnodeId);
        ImpDataVo dataVo = builder.build();
        model.addAttribute(TYPE, dataVo.getType());
        model.addAttribute(REFERRER, dataVo.getReferrer());
        model.addAttribute(ENCODEREFERRER, dataVo.getEncodereferrer());
        model.addAttribute(ActYwGroup.JK_GNODE_ID, dataVo.getGnodeId());
        model.addAttribute(ActYwGroup.JK_ACTYW_ID, dataVo.getActywid());

		Map<String, Object> param = new HashMap<String, Object>();
		param.put(TYPE, dataVo.getType());
		Page<Map<String, String>> page = impInfoService.getMdList(new Page<Map<String, String>>(request, response), param);
		model.addAttribute(Page.PAGE, page);
		return PieSval.path.vms(PieEmskey.IMPDATA.k()) + "proprojectmd/impdataList";
	}

	/***************************************************************************************
	/*门禁卡导入导出.
	/***************************************************************************************
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "downDrcardTemplate")
    public void downDrcardTemplate(HttpServletRequest request, HttpServletResponse response) {
        itDrService.downTpl(null, response, SpringContextHolder.getWebPath(request, ItDrService.TPL_EXCEL_DR_ROOT), null);
    }

    /*门禁卡导入*/
    @RequestMapping(value = { "impDrcardList"})
    public String drCardImpList(ImpInfo impInfo, String isTrue, String referrer, HttpServletRequest request, HttpServletResponse response, Model model) {
        Builder builder = dealReferrer(referrer, request);
        ImpDataVo dataVo = builder.build();
        model.addAttribute(REFERRER, dataVo.getReferrer());
        model.addAttribute(ENCODEREFERRER, dataVo.getEncodereferrer());
        Page<ImpInfo> page = impInfoService.getDrCardList(new Page<ImpInfo>(request, response), impInfo);
        model.addAttribute(Page.PAGE, page);
        return PieSval.path.vms(PieEmskey.IMPDATA.k()) + "impDrcardList";

    }

    @RequestMapping(value = "getImpDrcardList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> getImpDrcardList(ImpInfo impInfo, String referrer, HttpServletRequest request, HttpServletResponse response, Model model){
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus<HashMap<String, Object>>(false, "没有数据");
        HashMap<String, Object> hashMap = new HashMap<>();
        Builder builder = dealReferrer(referrer, request);
        ImpDataVo dataVo = builder.build();
        hashMap.put(REFERRER, dataVo.getReferrer());
        hashMap.put(ENCODEREFERRER, dataVo.getEncodereferrer());
        impInfo.setImpTpye((ExpType.DrCard.getIdx()));
        Page<ImpInfo> page = impInfoService.getDrCardList(new Page<ImpInfo>(request, response), impInfo);
        hashMap.put(Page.PAGE, page);
        actYwRstatus.setMsg("查询成功");
        actYwRstatus.setStatus(true);
        actYwRstatus.setDatas(hashMap);
        return actYwRstatus;
    }
    /**
     * 自定义项目导入数据，导入附件.
     * @param request
     * @param response
     * @return JSONObject
     */
    @RequestMapping(value = "importDrcardData")
    @ResponseBody
    @RequiresPermissions("sys:user:import")
    public JSONObject importDrcardData(HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        ItOper oper = new ItOper(request);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 读取上传的文件内容
        List<MultipartFile> imgFiles = multipartRequest.getFiles(FileUtil.FILE_NAME);
        if (imgFiles == null||imgFiles.size()==0) {
            obj.put(CoreJkey.JK_RET, "0");
            obj.put(CoreJkey.JK_MSG, "上传失败");
            return obj;
        }
        try {
            boolean hasExcel=false;
            for (MultipartFile imgFile : imgFiles) {
                String fname=imgFile.getOriginalFilename().toLowerCase();
                if(fname.endsWith(FileUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX)){
                    hasExcel=true;
                    break;
                }
            }
            ItDrEngine engine = new ItDrEngine(itDrService);
            ItParamDrCard param = new ItParamDrCard();
            param.setOper(ItOper.check(oper));
            param.setTpl(new ItTplXsheet());
            engine.run(param, imgFiles, request);
            if(hasExcel){
                obj.put(CoreJkey.JK_RET, "1");
                obj.put(CoreJkey.JK_MSG, "上传成功");
            }else{
                obj.put(CoreJkey.JK_RET, "2");
                obj.put(CoreJkey.JK_MSG, "上传成功");
            }
        }catch(ImpDataException w) {
            obj.put(CoreJkey.JK_RET, "0");
            obj.put(CoreJkey.JK_MSG, w.getMessage());
            logger.error("导入出错", w);
            return obj;
        }catch (POIXMLException e) {
            obj.put(CoreJkey.JK_RET, "0");
            obj.put(CoreJkey.JK_MSG, "请选择正确的文件");
            logger.error("导入出错", e);
            return obj;
        }catch (Exception e) {
            obj.put(CoreJkey.JK_RET, "0");
            obj.put(CoreJkey.JK_MSG, "导入出错");
            logger.error("导入出错", e);
            return obj;
        }
        return obj;
    }

    @RequestMapping(value = "expDrcardData")
    public void expDrcardData(ImpInfo impInfo, HttpServletRequest request, HttpServletResponse response) {
        itDrService.expErrorData(impInfo, null, null, request, response);
    }

    @RequestMapping(value = "deleteDrcardProCt", method = RequestMethod.DELETE,  produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<?> deleteDrcardProCt(ImpInfo impInfo, RedirectAttributes redirectAttributes) {
        return itDrService.deleteImpInfo(impInfo);
    }

    /****************************************************************************************/

	@RequestMapping(value = "delete")
	public String delete(ImpInfo impInfo, String referrer,String actywId ,String gnodeId, String tplType, RedirectAttributes redirectAttributes) {
		impInfoService.delete(impInfo);
		addMessage(redirectAttributes, "删除成功");
        String url = null;
		String type=impInfo.getImpTpye();
		if ((ExpType.MdExpapproval.getIdx()).equals(type)
				||(ExpType.MdExpmid.getIdx()).equals(type)
				||(ExpType.MdExpclose.getIdx()).equals(type)) {
			url = "/impdata/mdlist/?repage&type="+type+"&referrer="+referrer+"&actywId="+actywId + "&gnodeId="+gnodeId;
		}else if((ExpType.PmProject.getIdx()).equals(type)){
            url = "/impdata/promodellist/?repage&referrer="+referrer+"&actywId="+actywId + "&gnodeId="+gnodeId;
		}else if((ExpType.PmGcontest.getIdx()).equals(type)){
            url = "/impdata/promodelgcontestlist/?repage&referrer="+referrer+"&actywId="+actywId + "&gnodeId="+gnodeId;
		}else if((ExpType.DrCard.getIdx()).equals(type)){
		    url = "/impdata/impDrcardList/?repage&referrer="+referrer;
		}else {
		    url = "/impdata/?repage&actywId="+actywId + "&gnodeId="+gnodeId;
		}

		if(StringUtil.isNotEmpty(tplType)){
            url += "&tplType="+tplType;
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + url;
	}


	@RequestMapping(value = "deleteProCt", method = RequestMethod.DELETE,  produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiTstatus deleteProCt(ImpInfo impInfo, String referrer,String actywId ,String gnodeId, RedirectAttributes redirectAttributes) {
		ApiTstatus actYwRstatus = new ApiTstatus(false, "删除失败");
		impInfoService.delete(impInfo);
		actYwRstatus.setStatus(true);
		actYwRstatus.setMsg("删除成功");
		return actYwRstatus;
	}

	@RequestMapping(value = "getImpInfo")
	@ResponseBody
	public ImpInfo getImpInfo(HttpServletRequest request, HttpServletResponse response) {
		String id=request.getParameter("id");
		return impInfoService.getImpInfo(id);
	}

	@RequestMapping(value="getImpInfoList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getImpInfoList(@RequestBody List<ImpInfo> list){
	    try {
//            List<ImpInfo> impInfos = new ArrayList<>();
            HashMap<String, ImpInfo> hashMap = new HashMap<>();
            for(ImpInfo impInfo : list){
                hashMap.put(impInfo.getId(), impInfoService.getImpInfo(impInfo.getId()));
//                impInfos.add(impInfoService.getImpInfo(impInfo.getId()));
            }
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	/*自定义大赛导入*/
	@RequestMapping(value = "importProModelGcontestData")
	@ResponseBody
	@RequiresPermissions("sys:user:import")
	public JSONObject importProModelGcontestData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		ItOper oper = new ItOper(request);
		ActYw ay = actYwService.get(oper.getActywId());
		if(ay==null){
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "导入失败，未找到项目配置信息");
			return obj;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 读取上传的文件内容
		List<MultipartFile> imgFile1 = multipartRequest.getFiles(FileUtil.FILE_NAME);
		if (imgFile1 == null||imgFile1.size()==0) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "上传失败");
			return obj;
		}

		try {
			boolean hasExcel=false;
			for (MultipartFile imgFile : imgFile1) {
				String fname=imgFile.getOriginalFilename().toLowerCase();
                if(fname.endsWith(FileUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX)){
					hasExcel=true;
					break;
				}
			}

            oper.setFirst(actYwGnodeService.getFirstTaskByYwid(oper.getActywId()));
            ItOper impVo = ItOper.check(oper);
            impDataService.importFlowData(ay, imgFile1, request, impVo);
			if(hasExcel){
				obj.put(CoreJkey.JK_RET, "1");
				obj.put(CoreJkey.JK_MSG, "上传成功");
			}else{
				obj.put(CoreJkey.JK_RET, "2");
				obj.put(CoreJkey.JK_MSG, "上传成功");
			}
		}catch(ImpDataException w) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, w.getMessage());
			logger.error("导入出错", w);
			return obj;
		}catch (POIXMLException e) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "请选择正确的文件");
			logger.error("导入出错", e);
			return obj;
		}catch (Exception e) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "导入出错");
			logger.error("导入出错", e);
			return obj;
		}
		return obj;
	}

	/**
	 * 自定义项目导入数据，导入附件.
	 * @param request
	 * @param response
	 * @return JSONObject
	 */
	@RequestMapping(value = "importProModelData")
	@ResponseBody
	@RequiresPermissions("sys:user:import")
	public JSONObject importProModelData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		ItOper oper = new ItOper(request);
		ActYw ay=actYwService.get(oper.getActywId());
		if(ay==null){
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "导入失败，未找到项目配置信息");
			return obj;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 读取上传的文件内容
		List<MultipartFile> imgFile1 = multipartRequest.getFiles(FileUtil.FILE_NAME);
		if (imgFile1 == null||imgFile1.size()==0) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "上传失败");
			return obj;
		}
		try {
		    boolean hasExcel=false;
            for (MultipartFile imgFile : imgFile1) {
                String fname=imgFile.getOriginalFilename().toLowerCase();
                if(fname.endsWith(FileUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX)){
                    hasExcel=true;
                    break;
                }
            }
            ItEngine engine = new ItEngine(itService);
            ItParam param = new ItParam();
            param.setActYw(ay);
            oper.setFirst(actYwGnodeService.getFirstTaskByYwid(oper.getActywId()));
            param.setOper(ItOper.check(oper));
            param.setTpl(new ItTplXsheet());
            engine.run(param, imgFile1, request);
            if(hasExcel){
                obj.put(CoreJkey.JK_RET, "1");
                obj.put(CoreJkey.JK_MSG, "上传成功");
            }else{
                obj.put(CoreJkey.JK_RET, "2");
                obj.put(CoreJkey.JK_MSG, "上传成功");
            }
		}catch(ImpDataException w) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, w.getMessage());
			logger.error("导入出错", w);
			return obj;
		}catch (POIXMLException e) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "请选择正确的文件");
			logger.error("导入出错", e);
			return obj;
		}catch (Exception e) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "导入出错");
			logger.error("导入出错", e);
			return obj;
		}
		return obj;
	}

	/*民大项目数据导入*/
	@RequestMapping(value = "importMdData")
	@ResponseBody
	@RequiresPermissions("sys:user:import")
	public JSONObject importMdData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
        ItOper oper = new ItOper(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 读取上传的文件内容
		List<MultipartFile> imgFile1 = multipartRequest.getFiles(FileUtil.FILE_NAME);
		if (imgFile1 == null||imgFile1.size()==0) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "上传失败");
			return obj;
		} else {
			impDataService.importMdData(imgFile1, request, oper);
			obj.put(CoreJkey.JK_RET, "1");
			obj.put(CoreJkey.JK_MSG, "上传成功");
		}
		return obj;
	}
	/*数据导入*/
	@RequestMapping(value = "importData")
	@ResponseBody
	@RequiresPermissions("sys:user:import")
	public JSONObject importData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 读取上传的文件内容
		MultipartFile imgFile1 = multipartRequest.getFile(FileUtil.FILE_NAME);
		if (imgFile1 == null) {
			obj.put(CoreJkey.JK_RET, "0");
			obj.put(CoreJkey.JK_MSG, "上传失败");
			return obj;
		} else {
			try {
				impDataService.importData(imgFile1, request);
			}catch(ImpDataException w) {
				obj.put(CoreJkey.JK_RET, "0");
				obj.put(CoreJkey.JK_MSG, w.getMessage());
				logger.error("导入出错", w);
				return obj;
			}catch (POIXMLException e) {
				obj.put(CoreJkey.JK_RET, "0");
				obj.put(CoreJkey.JK_MSG, "请选择正确的文件");
				logger.error("导入出错", e);
				return obj;
			}catch (Exception e) {
				obj.put(CoreJkey.JK_RET, "0");
				obj.put(CoreJkey.JK_MSG, "导入出错");
				logger.error("导入出错", e);
				return obj;
			}
			obj.put(CoreJkey.JK_RET, "1");
			obj.put(CoreJkey.JK_MSG, "上传成功");
		}
		return obj;
	}

	/*自定义大赛模板导出*/
	@RequestMapping(value = "downProModelGcontestTemplate")
	public void downProModelGcontestTemplate(HttpServletRequest request, HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			out= response.getOutputStream();
			String type = request.getParameter(TplType.TPL_TYPE);
			File fi = null;
			// excel模板路径
            String fileName = null;
            String fileName2 = null;

		    // excel模板路径
            fileName = "大赛信息导入.xlsx";
            fileName2 = "promodel_gcontest_data_template.xlsx";
            fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + fileName2);
            String headStr = "attachment; filename=\"" + new String(fileName .getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
			fs = new FileInputStream(fi);
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			IitDownTpl downTpl = new ItDnProModelGcontest();
			downTpl.setHead(sheet);
            downTpl.setBody(wb, sheet);
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	@RequestMapping(value = "downProModelTemplate")
	public void downProModelTemplate(HttpServletRequest request, HttpServletResponse response) {
        String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
        Boolean isNew = (Const.YES.equals((request.getParameter(CoreJkey.JK_IS_TRUE)))?true:false);
        /**
         * 兼容旧的代码.
         */
        if(!isNew){
            FileInputStream fs = null;
            OutputStream out = null;
            try {
                out= response.getOutputStream();

                // excel模板路径
                ActYw actYw = actYwService.get(actywId);
                fs = PieExcelUtils.setExcelHeader(response, IitTpl.getTplByYwName(request, null, actYw));
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                XSSFSheet sheet = wb.getSheetAt(0);

                FlowProjectType fpType =  actYw.getFptype();
                if((fpType == null)){
                    logger.warn("模板类型未定义，请检查参数！");
                    return;
                }

                // excel模板路径
                IitDownTpl downTpl = null;
                if((FlowProjectType.PMT_XM).equals(fpType)){
                    downTpl = new ItDnProModelPro(actYw);
                    downTpl.setHead(sheet);
                    downTpl.setBody(wb, sheet);
                }else if((FlowProjectType.PMT_DASAI).equals(fpType)){
                    downTpl = new ItDnProModelGcontest(actYw);
                    downTpl.setHead(sheet);
                    downTpl.setBody(wb, sheet);
                }else{
                    logger.info("当前下载的模板类型未定义！");
                }
                out = response.getOutputStream();
                wb.write(out);
            } catch (Exception e) {
                logger.error(e);
            } finally {
                try {
                    if (out!=null)out.close();
                    if (fs!=null)fs.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }else{
            if(StringUtil.isEmpty(actywId)){
                logger.warn("业务流程为空！");
                return;
            }
            ActYw actyw = actYwService.get(actywId);
            if(actyw != null){
                logger.warn("业务流程不存在["+actywId+"]！");
            }
            itService.downTpl(actyw, response, SpringContextHolder.getWebPath(request, PieSval.ROOT_IMP), null);
        }
	}
	/*下载模板*/
	@RequestMapping(value = "downTemplate")
	public void downTemplate(HttpServletRequest request, HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			out= response.getOutputStream();

			// excel模板路径
			String type=request.getParameter(CoreJkey.JK_TYPE);
			ExpType expType = ExpType.getByIdx(type);
			if(expType == null){
			    expType = ExpType.Stu;
			    return;
			}

			String headStr = "attachment; filename=\"" + new String(expType.getDownfname() .getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + expType.getTplname());
			fs = new FileInputStream(fi);
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			IitDownTpl downTpl = null;
			if ((ExpType.Stu).equals(expType)) {
			    downTpl = new ItDnStudent();
			    downTpl.setHead(sheet);
			    downTpl.setBody(wb, sheet);
			}else if ((ExpType.Tea).equals(expType)) {
                downTpl = new ItDnTeacher();
                downTpl.setHead(sheet);
                downTpl.setBody(wb, sheet);
			}else if ((ExpType.BackUser).equals(expType)) {
				downTpl = new ItDnBackUser();
                downTpl.setHead(sheet);
                downTpl.setBody(wb, sheet);
			}else if ((ExpType.Org.getIdx()).equals(type)) {
			    downTpl = new ItDnOrg();
			    downTpl.setHead(sheet);
                downTpl.setBody(wb, sheet);
			}else if ((ExpType.DcProjectClose).equals(expType)) {
                downTpl = new ItDnProjectClose();
                downTpl.setHead(sheet);
                downTpl.setBody(wb, sheet);
			}else if ((ExpType.PmProjectHsmid).equals(expType)) {
				downTpl = new ItDnProjectHs();
                downTpl.setHead(sheet);
                downTpl.setBody(wb, sheet);
			}else if ((ExpType.HlwGcontest).equals(expType)) {
				downTpl = new ItDnHlwGcontest();
                downTpl.setHead(sheet);
                downTpl.setBody(wb, sheet);
			}else if ((ExpType.ConStu).equals(expType)) {
				downTpl = new ItDnConStu();
				downTpl.setHead(sheet);
				downTpl.setBody(wb, sheet);
			}
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	/**
	 * 下载导入结果模板.
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "expProModelData")
	public void expProModelData(HttpServletRequest request, HttpServletResponse response) {
	    String id = request.getParameter("id");
	    String ywid = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		ActYw actYw = actYwService.get(ywid);
        String fileName = "项目信息导入.xlsx";
        IitAbsTpl tpl = null;
		if(actYw != null){
		    if((FormTheme.F_TLXY).equals(actYw.getFtheme())){
		        itService.expErrorData(new ImpInfo(id), "/tlxy/tlxy_project", "项目信息导入", request, response);
		    }else{
		        tpl = new IitAbsTpl(SpringContextHolder.getWebPath(request, ExpType.TPL_ROOT_STATICEXCELTEMPLATE), fileName, "promodel_data_template.xlsx");
		        proModelExp(id, tpl, request, response);
		    }
		}else{
		    tpl = new IitAbsTpl(SpringContextHolder.getWebPath(request, ExpType.TPL_ROOT_STATICEXCELTEMPLATE), fileName, "promodel_data_template.xlsx");
	        proModelExp(id, tpl, request, response);
		}
	}

	@RequestMapping(value = "expProModelGcontestData")
	public void expProModelGcontestData(HttpServletRequest request, HttpServletResponse response) {
		String id=request.getParameter("id");
		proModelGcontestExp(id, request, response);
	}
	/*错误数据的导出*/
	@RequestMapping(value = "expData")
	public void expData(HttpServletRequest request, HttpServletResponse response) {
		String id=request.getParameter(CoreJkey.JK_ID);
		String type = request.getParameter(CoreJkey.JK_TYPE);
        ExpType expType = ExpType.getByIdx(type);
        if(expType == null){
            return;
        }

		if ((ExpType.Stu).equals(expType)) {
			studentExp(id,request, response);
		}else if ((ExpType.Tea).equals(expType)) {
			teacherExp(id, request, response);
		}else if ((ExpType.BackUser).equals(expType)) {
			backUserExp(id, request, response);
		}else if ((ExpType.Org).equals(expType)) {
			orgExp(id, request, response);
		}else if ((ExpType.DcProjectClose).equals(expType)) {
			projectExp(id, request, response);
		}else if ((ExpType.MdExpapproval).equals(expType)) {
			projectMdApprovalExp(id, request, response);
		}else if ((ExpType.MdExpmid).equals(expType)) {
			projectMdMidExp(id, request, response);
		}else if ((ExpType.MdExpclose.getKey()).equals(type)) {
			projectMdCloseExp(id, request, response);
		}else if ((ExpType.PmProjectHsmid).equals(expType)) {
			projectHsExp(id, request, response);
		}else if ((ExpType.HlwGcontest).equals(expType)) {
			gcontestExp(id, request, response);
		}else if ((ExpType.ConStu).equals(expType)) {
			constuExp(id, request, response);
		}
	}

	private void setMdApprovalExcelHead(ImpInfo ii,XSSFSheet sheet,String sindx) {
		if (ii!=null) {
			JSONObject js=JSONObject.fromObject(ii.getMsg());
			sheet.getRow(0).getCell(0).setCellValue(js.getString("title"+sindx));
			sheet.getRow(1).getCell(0).setCellValue(js.getString("oname"));
		}
	}
	private void setMdMidExcelHead(ImpInfo ii,XSSFSheet sheet) {
		if (ii!=null) {
			JSONObject js=JSONObject.fromObject(ii.getMsg());
			sheet.getRow(0).getCell(0).setCellValue(js.getString("title0"));
			sheet.getRow(1).getCell(0).setCellValue(js.getString("oname"));
		}
	}
	private void setMdCloseExcelHead(ImpInfo ii,XSSFSheet sheet) {
		if (ii!=null) {
			JSONObject js=JSONObject.fromObject(ii.getMsg());
			sheet.getRow(0).getCell(0).setCellValue(js.getString("title0"));
			sheet.getRow(1).getCell(0).setCellValue(js.getString("oname"));
		}
	}
	private void studentExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.Stu.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.Stu.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=studentErrorService.getListByImpId(id);
			ItDnStudent dnUser = new ItDnStudent();
			dnUser.setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=StudentColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	private void constuExp(String id,HttpServletRequest request,HttpServletResponse response) {
			String rootpath = request.getSession().getServletContext().getRealPath("/");
			FileInputStream fs = null;
			OutputStream out = null;
			try {
				// excel模板路径
				String headStr = "attachment; filename=\"" + new String(ExpType.ConStu.getDownfname().getBytes(), "ISO-8859-1") + "\"";
				response.setContentType("APPLICATION/OCTET-STREAM");
				response.setHeader("Content-Disposition", headStr);
				File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.ConStu.getTplPath());
				fs = new FileInputStream(fi);
				// 读取了模板内所有sheet内容
				XSSFWorkbook wb = new XSSFWorkbook(fs);
				XSSFSheet sheet = wb.getSheetAt(0);
				CreationHelper factory = wb.getCreationHelper();
				ClientAnchor anchor = factory.createClientAnchor();
				Drawing drawing = sheet.createDrawingPatriarch();
				List<Map<String, String>> list=studentErrorService.getListByImpId(id);
				ItDnStudent dnUser = new ItDnStudent();
				dnUser.setHead(sheet);
				if (!list.isEmpty()) {
					Map<String, Integer> rowIndex=new HashMap<String, Integer>();
					// 在相应的单元格进行赋值
					for(int i=0;i<list.size();i++) {
						Map<String, String> map=list.get(i);
						XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
						rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
						for(String key:map.keySet()) {
							Integer cindex=StudentColEnum.getValueByName(key);
							if (cindex!=null) {
								XSSFCell cell = row.createCell(cindex);
								cell.setCellValue(map.get(key));
							}
						}
					}
					List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
					if (!errlist.isEmpty()) {
						for(Map<String, String> errmap:errlist) {
							Comment comment0 = drawing.createCellComment(anchor);
							RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
							XSSFFont commentFormatter = wb.createFont();
							str0.applyFont(commentFormatter);
							comment0.setString(str0);
							if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
								sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
							}else{
								sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
							}
						}
					}
				}
				out = response.getOutputStream();
				wb.write(out);
			} catch (Exception e) {
				logger.error(e);
			} finally {
				try {
					if (out!=null)out.close();
					if (fs!=null)fs.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}


	private void teacherExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.Tea.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.Tea.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=teacherErrorService.getListByImpId(id);
			//setTeacherExcelHead(sheet);
			new ItDnTeacher().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=TeacherColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void backUserExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.BackUser.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.BackUser.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=backUserErrorService.getListByImpId(id);
			new ItDnBackUser().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=BackUserColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void orgExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.Org.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.Org.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=officeErrorService.getListByImpId(id);
			//setOrgExcelHead(sheet);
			new ItDnOrg().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=OfficeColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void projectHsExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String fileName = "项目信息导入.xlsx";
			String headStr = "attachment; filename=\"" + new String(fileName .getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.PmProjectHsmid.getTplname());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=projectHsErrorService.getListByImpId(id);
			new ItDnProjectHs().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=ProjectHsColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		}	catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void proModelExp(String id, IitAbsTpl tpl, HttpServletRequest request,HttpServletResponse response) {
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			fs = PieExcelUtils.setExcelHeader(response, tpl);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=proModelErrorService.getListByImpId(id);
			new ItDnProModelPro().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=ProModelColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		}	catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	private void expDataError(String id, HttpServletRequest request, HttpServletResponse response) {
	    ItOper oper = new ItOper(request);
        ActYw ay = actYwService.get(oper.getActywId());
//        if(ay==null){
//            obj.put(CoreJkey.JK_RET, "0");
//            obj.put(CoreJkey.JK_MSG, "导入失败，未找到项目配置信息");
//            return obj;
//        }

	    IitTplData itTplData = null;
        ItOper impVo = ItOper.check(oper);
        //处理项目信息
        if(impVo.getIsImpFirstData() || impVo.getIsImpHisData()){
            if((FlowProjectType.PMT_DASAI).equals(ay.getFptype())){
                if((TplType.GJ.getKey()).equals(impVo.getReqParam().getTplType())){
                    itTplData = new TplGcontestGJ();
                }else{
                    proModelGcontestExp(id, request, response);
                }
            }else if((FlowProjectType.PMT_XM).equals(ay.getFptype())){
                //TODO CHENHAO
            }else{
                //TODO CHENHAO
            }

            if(itTplData != null){
                itTplData.expDataError(id, ay, request, impVo);
            }
        }
	}
    private void proModelGcontestExp(String id,HttpServletRequest request,HttpServletResponse response) {
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String fileName = "大赛信息导入.xlsx";
            fs = PieExcelUtils.setExcelHeader(response, new IitAbsTpl(SpringContextHolder.getWebPath(request, ExpType.TPL_ROOT_STATICEXCELTEMPLATE), fileName, "promodel_gcontest_data_template_error.xlsx"));
//			String headStr = "attachment; filename=\"" + new String(fileName .getBytes(), "ISO-8859-1") + "\"";
//			response.setContentType("APPLICATION/OCTET-STREAM");
//			response.setHeader("Content-Disposition", headStr);
//			File fi = new File(rootpath + ExpTypeEnum.TPL_ROOT_STATICEXCELTEMPLATE + "promodel_gcontest_data_template.xlsx");
//			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet0 = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet0.createDrawingPatriarch();
			new ItDnProModelGcontest().setHead(sheet0);
			Map<String, ProModelGcontestError> m1 = new LinkedHashMap<String, ProModelGcontestError>();
			Map<String, List<PmgMemsError>> m2 = new HashMap<String, List<PmgMemsError>>();
			Map<String, List<PmgTeasError>> m3 = new HashMap<String, List<PmgTeasError>>();
			getPromodelGcontestExpData(id, m1, m2, m3);
			if (!m1.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值

				int row0 = 5;//当前项目起始行
				int prorows = 1;//每个项目占几行
				for (String pid : m1.keySet()) {
					//算项目行数
					List<PmgMemsError> l2 = m2.get(pid);
					if (l2 != null && l2.size() > prorows) {
						prorows = l2.size();
					}
					List<PmgTeasError> l3 = m3.get(pid);
					if (l3 != null && l3.size() > prorows) {
						prorows = l3.size();
					}

					//创建行
					creatRow(sheet0, row0, prorows);

					rowIndex.put(pid, row0);

					XSSFRow row = sheet0.getRow(row0);
					ProModelGcontestError ged = m1.get(pid);
					row.createCell(0).setCellValue(ged.getName());
					row.createCell(1).setCellValue(ged.getYear());
					row.createCell(2).setCellValue(ged.getStage());
					row.createCell(3).setCellValue(ged.getType());
					row.createCell(4).setCellValue(ged.getGroups());
					row.createCell(5).setCellValue(ged.getIntroduction());
					row.createCell(6).setCellValue(ged.getHasfile());
					row.createCell(7).setCellValue(ged.getLeader());
					row.createCell(8).setCellValue(ged.getNo());
					row.createCell(9).setCellValue(ged.getProfes());
					row.createCell(10).setCellValue(ged.getEnter());
					row.createCell(11).setCellValue(ged.getOut());
					row.createCell(12).setCellValue(ged.getXueli());
					row.createCell(13).setCellValue(ged.getIdnum());
					row.createCell(14).setCellValue(ged.getMobile());
					row.createCell(15).setCellValue(ged.getEmail());

					if (l2 != null && l2.size() > 0) {
						for (int i = 0; i < l2.size(); i++) {
							XSSFRow row2 = sheet0.getRow(row0 + i);
							PmgMemsError gse = l2.get(i);
							rowIndex.put(pid+gse.getId(), row0 + i);
							row2.createCell(16).setCellValue(gse.getName());
							row2.createCell(17).setCellValue(gse.getNo());
							row2.createCell(18).setCellValue(gse.getProfes());
							row2.createCell(19).setCellValue(gse.getEnter());
							row2.createCell(20).setCellValue(gse.getOut());
							row2.createCell(21).setCellValue(gse.getXueli());
							row2.createCell(22).setCellValue(gse.getIdnum());
							row2.createCell(23).setCellValue(gse.getMobile());
							row2.createCell(24).setCellValue(gse.getEmail());

						}
					}
					if (l3 != null && l3.size() > 0) {
						for (int i = 0; i < l3.size(); i++) {
							XSSFRow row3 = sheet0.getRow(row0 + i);
							PmgTeasError gse = l3.get(i);
							rowIndex.put(pid+gse.getId(), row0 + i);
							row3.createCell(25).setCellValue(gse.getName());
							row3.createCell(26).setCellValue(gse.getNo());
							row3.createCell(27).setCellValue(gse.getOffice());
							row3.createCell(28).setCellValue(gse.getMobile());
							row3.createCell(29).setCellValue(gse.getEmail());
							row3.createCell(30).setCellValue(gse.getZhicheng());
						}
					}
                    mergedRowCell(sheet0, row0, prorows);
					row0 = row0 + prorows;
					prorows=1;
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if(StringUtil.isEmpty(errmap.get(ImpInfoErrmsg.DATA_SUB_ID))){
							if (sheet0.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
								sheet0.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
							}else{
								sheet0.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
							}
						}else{
							if (sheet0.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID)+errmap.get(ImpInfoErrmsg.DATA_SUB_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
								sheet0.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID)+errmap.get(ImpInfoErrmsg.DATA_SUB_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
							}else{
								sheet0.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID)+errmap.get(ImpInfoErrmsg.DATA_SUB_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
							}
						}
					}
				}

			}
            out = response.getOutputStream();
            wb.write(out);
        }   catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("下载错误信息失败，错误文档生成报错！");
        } finally {
            try {
                if (out!=null)out.close();
                if (fs!=null)fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("下载错误信息失败，错误文档生成流关闭报错！");
                logger.error(e);
            }
        }
	}
	private void creatRow(XSSFSheet sheet0, int start, int count) {
		for (int i = 0; i < count; i++) {
			sheet0.createRow(start + i);
		}
	}
	private void mergedRowCell(XSSFSheet sheet0, int start, int count) {
	      if(count < 2){
	          return;
	      }
		// 合并单元格
		for (int i = 0; i < 16; i++) {
			CellRangeAddress cra = new CellRangeAddress(start, start + count - 1, i, i); // 起始行, 终止行, 起始列, 终止列
			sheet0.addMergedRegion(cra);
		}
	}
	private void getPromodelGcontestExpData(String impid,Map<String, ProModelGcontestError> m1, Map<String, List<PmgMemsError>> m2, Map<String, List<PmgTeasError>> m3){
		List<ProModelGcontestError> l1 = proModelGcontestErrorService.getListByImpId(impid);
		if (l1 == null || l1.size() == 0) {
			return;
		}
		for (ProModelGcontestError o : l1) {
			m1.put(o.getId(), o);
		}
		List<PmgMemsError> l2 = pmgMemsErrorService.getListByImpId(impid);
		if (l2 != null && l2.size() != 0) {
			for (PmgMemsError o : l2) {
				List<PmgMemsError> teml = m2.get(o.getPmgeId());
				if (teml == null) {
					teml = new ArrayList<PmgMemsError>();
					m2.put(o.getPmgeId(), teml);
				}
				teml.add(o);
			}
		}
		List<PmgTeasError> l3 = pmgTeasErrorService.getListByImpId(impid);
		if (l3 != null && l3.size() != 0) {
			for (PmgTeasError o : l3) {
				List<PmgTeasError> teml = m3.get(o.getPmgeId());
				if (teml == null) {
					teml = new ArrayList<PmgTeasError>();
					m3.put(o.getPmgeId(), teml);
				}
				teml.add(o);
			}
		}
	}
	private void gcontestExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.HlwGcontest.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.HlwGcontest.getTplname());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=gcontestErrorService.getListByImpId(id);
			//setGcontestExcelHead(sheet);
			new ItDnHlwGcontest().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=GcontestColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		}	catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void projectExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String fileName = "项目信息导入.xlsx";
			String headStr = "attachment; filename=\"" + new String(ExpType.DcProjectClose.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.DcProjectClose.getTplname());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0);
			CreationHelper factory = wb.getCreationHelper();
			ClientAnchor anchor = factory.createClientAnchor();
			Drawing drawing = sheet.createDrawingPatriarch();
			List<Map<String, String>> list=projectErrorService.getListByImpId(id);
			//setProjectExcelHead(sheet);
			new ItDnProjectClose().setHead(sheet);
			if (!list.isEmpty()) {
				Map<String, Integer> rowIndex=new HashMap<String, Integer>();
				// 在相应的单元格进行赋值
				for(int i=0;i<list.size();i++) {
					Map<String, String> map=list.get(i);
					XSSFRow row=sheet.createRow(i+1+ImpDataService.descHeadRow);
					rowIndex.put(map.get("id"), i+1+ImpDataService.descHeadRow);
					for(String key:map.keySet()) {
						Integer cindex=ProjectColEnum.getValueByName(key);
						if (cindex!=null) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}
					}
				}
				List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
				if (!errlist.isEmpty()) {
					for(Map<String, String> errmap:errlist) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void projectMdApprovalExpSheet(XSSFWorkbook wb,String sheetindx,String id) {
		XSSFSheet sheet = wb.getSheetAt(Integer.parseInt(sheetindx));

		XSSFCellStyle rowStyle = wb.createCellStyle();
		rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框

		CreationHelper factory = wb.getCreationHelper();
		ClientAnchor anchor = factory.createClientAnchor();
		Drawing drawing = sheet.createDrawingPatriarch();
		List<Map<String, String>> list=proMdApprovalErrorService.getListByImpId(id,sheetindx);
		ImpInfo ii=impInfoService.get(id);
		setMdApprovalExcelHead(ii,sheet,sheetindx);
		int row0=ImpExpService.approval_sheet0_head+2;
		int row1=ImpExpService.approval_sheet1_head+2;
		Map<String, Integer> rowIndex=null;
		if (!list.isEmpty()) {
			rowIndex=new HashMap<String, Integer>();
			int rowh=0;
			if ("0".equals(sheetindx)) {
				rowh=ImpExpService.approval_sheet0_head+2;
			}else if ("1".equals(sheetindx)) {
				rowh=ImpExpService.approval_sheet1_head+2;
			}
			// 在相应的单元格进行赋值
			for(int i=0;i<list.size();i++) {
				int rowinx=0;
				if ("0".equals(sheetindx)) {
					rowinx=row0-ImpExpService.approval_sheet0_head-1;
					row0++;
				}else if ("1".equals(sheetindx)) {
					rowinx=row1-ImpExpService.approval_sheet1_head-1;
					row1++;
				}
				Map<String, String> map=list.get(i);
				XSSFRow row=sheet.createRow(i+rowh);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(rowinx+"");
				rowIndex.put(map.get("id"), i+4);
				for(String key:map.keySet()) {
					Integer cindex=ProjectMdApprovalColEnum.getValueByName(key);
					if (cindex!=null) {
						if (cindex!=8) {
							XSSFCell cell = row.createCell(cindex);
							cell.setCellValue(map.get(key));
						}else{
							if ("A".equals(map.get(key))) {
								XSSFCell cell = row.createCell(8);
								cell.setCellValue("√");
							}else if ("B".equals(map.get(key))) {
								XSSFCell cell = row.createCell(9);
								cell.setCellValue("√");
							}else if ("C".equals(map.get(key))) {
								XSSFCell cell = row.createCell(10);
								cell.setCellValue("√");
							}
						}
					}
				}
				//设置样式
				for(int m=0;m<=23;m++) {
					if (row.getCell(m)==null) {
						row.createCell(m).setCellStyle(rowStyle);
					}else{
						row.getCell(m).setCellStyle(rowStyle);
					}
				}
			}
			List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
			if (!errlist.isEmpty()) {
				for(Map<String, String> errmap:errlist) {
					Integer ri=rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID));
					if (ri!=null) {
						Comment comment0 = drawing.createCellComment(anchor);
						RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
						XSSFFont commentFormatter = wb.createFont();
						str0.applyFont(commentFormatter);
						comment0.setString(str0);
						if (sheet.getRow(ri).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
							sheet.getRow(ri).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}else{
							sheet.getRow(ri).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
						}
					}
				}
			}
		}
		//尾部
		XSSFCellStyle cellStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("仿宋_GB2312");
		font.setFontHeightInPoints((short) 14);//设置字体大小
		cellStyle.setFont(font);
		if ("0".equals(sheetindx)) {
			if (ii!=null) {
				JSONObject js=JSONObject.fromObject(ii.getMsg());
				sheet.createRow(row0).createCell(0).setCellValue(js.getString("tel0"));
				sheet.getRow(row0).getCell(0).setCellStyle(cellStyle);
			}
			for(int k=1;k<ImpExpService.approval_sheet0_foot.length;k++) {
				sheet.createRow(row0+k).createCell(0).setCellValue(ImpExpService.approval_sheet0_foot[k]);
			}
		}else if ("1".equals(sheetindx)) {
			if (ii!=null) {
				JSONObject js=JSONObject.fromObject(ii.getMsg());
				sheet.createRow(row1).createCell(0).setCellValue(js.getString("tel1"));
				sheet.getRow(row1).getCell(0).setCellStyle(cellStyle);
			}
			for(int k=1;k<ImpExpService.approval_sheet1_foot.length;k++) {
				sheet.createRow(row1+k).createCell(0).setCellValue(ImpExpService.approval_sheet1_foot[k]);
			}
		}
	}
	private void projectMdApprovalExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.MdExpapproval.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpapproval.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			projectMdApprovalExpSheet(wb, "0",id);
			projectMdApprovalExpSheet(wb, "1",id);
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void projectMdMidExpSheet(XSSFWorkbook wb,String id) {
		XSSFSheet sheet = wb.getSheetAt(0);

		XSSFCellStyle rowStyle = wb.createCellStyle();
		rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框

		CreationHelper factory = wb.getCreationHelper();
		ClientAnchor anchor = factory.createClientAnchor();
		Drawing drawing = sheet.createDrawingPatriarch();
		List<Map<String, String>> list=proMdMidErrorService.getListByImpId(id);
		ImpInfo ii=impInfoService.get(id);
		setMdMidExcelHead(ii,sheet);
		int row0=ImpExpService.mid_sheet0_head+2;
		Map<String, Integer> rowIndex=null;
		if (!list.isEmpty()) {
			rowIndex=new HashMap<String, Integer>();
			int rowh=ImpExpService.mid_sheet0_head+2;
			// 在相应的单元格进行赋值
			for(int i=0;i<list.size();i++) {
				int rowinx=row0-ImpExpService.mid_sheet0_head-1;
				row0++;
				Map<String, String> map=list.get(i);
				XSSFRow row=sheet.createRow(i+rowh);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(rowinx+"");
				rowIndex.put(map.get("id"), i+4);
				for(String key:map.keySet()) {
					Integer cindex=ProjectMdMidColEnum.getValueByName(key);
					if (cindex!=null) {
						XSSFCell cell = row.createCell(cindex);
						cell.setCellValue(map.get(key));
					}
				}
				//设置样式
				for(int m=0;m<=11;m++) {
					if (row.getCell(m)==null) {
						row.createCell(m).setCellStyle(rowStyle);
					}else{
						row.getCell(m).setCellStyle(rowStyle);
					}
				}
			}
			List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
			if (!errlist.isEmpty()) {
				for(Map<String, String> errmap:errlist) {
					Comment comment0 = drawing.createCellComment(anchor);
					RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
					XSSFFont commentFormatter = wb.createFont();
					str0.applyFont(commentFormatter);
					comment0.setString(str0);
					if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
						sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
					}else{
						sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
					}
				}
			}
		}
		//尾部
		XSSFCellStyle cellStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("仿宋_GB2312");
		font.setFontHeightInPoints((short) 14);//设置字体大小
		cellStyle.setFont(font);
		if (ii!=null) {
			JSONObject js=JSONObject.fromObject(ii.getMsg());
			sheet.createRow(row0).createCell(0).setCellValue(js.getString("tel0"));
			sheet.getRow(row0).getCell(0).setCellStyle(cellStyle);
		}
		for(int k=1;k<ImpExpService.mid_sheet0_foot.length;k++) {
			sheet.createRow(row0+k).createCell(0).setCellValue(ImpExpService.mid_sheet0_foot[k]);
		}
	}
	private void projectMdMidExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.MdExpmid.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpmid.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			projectMdMidExpSheet(wb,id);
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	private void projectMdCloseExpSheet(XSSFWorkbook wb,String id) {
		XSSFSheet sheet = wb.getSheetAt(0);

		XSSFCellStyle rowStyle = wb.createCellStyle();
		rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框

		CreationHelper factory = wb.getCreationHelper();
		ClientAnchor anchor = factory.createClientAnchor();
		Drawing drawing = sheet.createDrawingPatriarch();
		List<Map<String, String>> list=proMdCloseErrorService.getListByImpId(id);
		ImpInfo ii=impInfoService.get(id);
		setMdCloseExcelHead(ii,sheet);
		int row0=ImpExpService.close_sheet0_head+2;
		Map<String, Integer> rowIndex=null;
		if (!list.isEmpty()) {
			rowIndex=new HashMap<String, Integer>();
			int rowh=ImpExpService.close_sheet0_head+2;
			// 在相应的单元格进行赋值
			for(int i=0;i<list.size();i++) {
				int rowinx=row0-ImpExpService.close_sheet0_head-1;
				row0++;
				Map<String, String> map=list.get(i);
				XSSFRow row=sheet.createRow(i+rowh);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(rowinx+"");
				rowIndex.put(map.get("id"), i+4);
				for(String key:map.keySet()) {
					Integer cindex=ProjectMdCloseColEnum.getValueByName(key);
					if (cindex!=null) {
						XSSFCell cell = row.createCell(cindex);
						cell.setCellValue(map.get(key));
					}
				}
				//设置样式
				for(int m=0;m<=14;m++) {
					if (row.getCell(m)==null) {
						row.createCell(m).setCellStyle(rowStyle);
					}else{
						row.getCell(m).setCellStyle(rowStyle);
					}
				}
			}
			List<Map<String, String>> errlist=impInfoErrmsgService.getListByImpId(id);
			if (!errlist.isEmpty()) {
				for(Map<String, String> errmap:errlist) {
					Comment comment0 = drawing.createCellComment(anchor);
					RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
					XSSFFont commentFormatter = wb.createFont();
					str0.applyFont(commentFormatter);
					comment0.setString(str0);
					if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
						sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
					}else{
						sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
					}
				}
			}
		}
		//尾部
		XSSFCellStyle cellStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("仿宋_GB2312");
		font.setFontHeightInPoints((short) 14);//设置字体大小
		cellStyle.setFont(font);
		if (ii!=null) {
			JSONObject js=JSONObject.fromObject(ii.getMsg());
			sheet.createRow(row0).createCell(0).setCellValue(js.getString("tel0"));
			sheet.getRow(row0).getCell(0).setCellStyle(cellStyle);
		}
		for(int k=1;k<ImpExpService.close_sheet0_foot.length;k++) {
			sheet.createRow(row0+k).createCell(0).setCellValue(ImpExpService.close_sheet0_foot[k]);
		}
	}
	private void projectMdCloseExp(String id,HttpServletRequest request,HttpServletResponse response) {
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		FileInputStream fs = null;
		OutputStream out = null;
		try {
			// excel模板路径
			String headStr = "attachment; filename=\"" + new String(ExpType.MdExpclose.getDownfname().getBytes(), "ISO-8859-1") + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", headStr);
			File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpclose.getTplPath());
			fs = new FileInputStream(fi);
			// 读取了模板内所有sheet内容
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			projectMdCloseExpSheet(wb,id);
			out = response.getOutputStream();
			wb.write(out);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (out!=null)out.close();
				if (fs!=null)fs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

    /**
     * 获取目标类型数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxExpTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxExpTypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String idx) {
        if(isAll == null){
            isAll = false;
        }

        //查询所有
        if(isAll){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(ExpType.values()).toString()));
        }

        if(StringUtil.isEmpty(idx)){
            ExpType expTypeEnum = ExpType.getByIdx(idx);
            if(expTypeEnum == null){
                return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
            }
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(expTypeEnum).toString()));
        }
        return new ApiTstatus<String>(false, "查询失败,参数不正确！");
    }
}