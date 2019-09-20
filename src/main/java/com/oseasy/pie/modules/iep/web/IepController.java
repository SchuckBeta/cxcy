package com.oseasy.pie.modules.iep.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.service.IepService;
import com.oseasy.pie.modules.iep.service.IepTplService;
import com.oseasy.pie.modules.iep.tool.impl.IeYws;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 模板导入导出Controller.
 * @author chenhao
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/iep/ie")
public class IepController extends BaseController {
	@Autowired
	private IepService iepService;
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

	@RequestMapping(value = "downTpl")
    public void downTpl(IepTpl entity, HttpServletRequest request, HttpServletResponse response) {
        iepService.downTpl(entity, new IeYws(entity, request, response));
    }

	@ResponseBody
	@RequestMapping(value = "uploadTpl")
	public ApiTstatus<?> uploadTpl(HttpServletRequest request, HttpServletResponse response) {
	    return iepService.uploadTpl(request, response);
	}
	@RequestMapping(value = "exportTpl")
    public void exportTpl(IepTpl entity, HttpServletRequest request, HttpServletResponse response) {
        iepService.exportTpl(entity, new IeYws(entity, request, response));
    }

	@RequestMapping(value = "downSdata")
    public void downSdata(String id, HttpServletRequest request, HttpServletResponse response) {
        iepService.downSdata(id, request, response);
    }

	@RequestMapping(value = "downEdata")
	public void downEdata(String id, HttpServletRequest request, HttpServletResponse response) {
	    iepService.downEdata(id, request, response);
	}

	@ResponseBody
	@RequestMapping(value = "delRecord")
	public ApiTstatus<?> delRecord(String id, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		return iepService.delRecord(id, request, response);
//	    Rtstatus<?> rtstatus =  iepService.delRecord(id, request, response);
//	    if (rtstatus.getStatus()) {
//            return (String) rtstatus.getDatas();
//        }
//	    return "";
	}

	/**
     * 获取列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxIepInfos")
    public ApiTstatus<?> ajaxIepInfos(IepTpl entity, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtil.isEmpty(entity.getId())) {
                entity.setId(request.getParameter("id"));
            }
            if (StringUtil.isEmpty(entity.getId())) {
                return new ApiTstatus<Object>(true, "参数有误,模板ID不能为空");
            }
            return iepService.findListByIep(new IeYws(entity, request, response));
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ApiTstatus<Object>(false, "获取数据失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxIepInfo", method = RequestMethod.POST, produces = "application/json")
    public ApiTstatus<?> getImpInfoList(@RequestBody List<ImpInfo> infos, HttpServletRequest request, HttpServletResponse response){
        try {
            return iepService.getByIep(infos, request, response);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ApiTstatus<>(false, "请求失败");
        }
    }

}