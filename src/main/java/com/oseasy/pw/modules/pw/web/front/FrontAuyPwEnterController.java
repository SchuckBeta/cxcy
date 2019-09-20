package com.oseasy.pw.modules.pw.web.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.auy.modules.pw.service.AuyPwEnterService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttachmentVo;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.exception.NoTeamException;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.PwEnterVo;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * 入驻申报Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${frontPath}/pw/pwEnter")
public class FrontAuyPwEnterController extends BaseController {
    @Autowired
    private PwEnterService pwEnterService;
    @Autowired
    private AuyPwEnterService auyPwEnterService;

    /**
     * 保存入驻企业基本信息.
     *
     * @return ActYwRstatus 结果状态
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/ajaxSaveAll", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<PwEnter> ajaxSaveAll(@RequestBody JSONObject ppeVo) {
        ApiTstatus<PwEnter> rstatus = new ApiTstatus<PwEnter>(false, "保存失败(申报信息保存失败)！");
        PwEnterVo peVo = null;
        try {
            Map<String, Class> classMap = new HashMap<String, Class>();
            classMap.put("pwCompany", PwCompany.class);
            classMap.put("tfiles", SysAttachmentVo.class);
            classMap.put("cfiles", SysAttachmentVo.class);
            classMap.put("pfiles", SysAttachmentVo.class);
            classMap.put("projectFiles", SysAttachmentVo.class);
            peVo = (PwEnterVo) JSONObject.toBean(ppeVo, PwEnterVo.class, classMap);
        } catch (Exception e) {
            rstatus = new ApiTstatus<PwEnter>(false, "入驻申请参数格式异常");
            logger.error(rstatus.getMsg(), e);
        }

        if (peVo == null) {
            return new ApiTstatus<PwEnter>(false, "入驻申请参数格式异常");
        }

        try {
            rstatus = auyPwEnterService.saveEnterApply(peVo);
        } catch (NoTeamException e) {
            rstatus = new ApiTstatus<PwEnter>(false, "保存失败(没有团队)！", new PwEnter(peVo.getEid()));
            logger.error(rstatus.getMsg(), e);
        }

        if(rstatus != null){
            PwEnter curPwEnter = rstatus.getDatas();
            if (curPwEnter != null) {
                if (curPwEnter.getEcompany() != null) {
                    //curPwEnter.getEcompany().setPwEnter(null);
                }
//                if (curPwEnter.getEproject() != null) {
                   // curPwEnter.getEproject().setPwEnter(null);
//                }
                if (curPwEnter.getEteam() != null) {
                    //curPwEnter.getEteam().setPwEnter(null);
                }
                rstatus.setDatas(curPwEnter);
            }
        }
        return rstatus;
    }

    //获得关联项目接口
    @RequestMapping(value = "ajaxGetProjects" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetProjects(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ProModel> proModels = pwEnterService.getProjects();
            return ApiResult.success(proModels);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}