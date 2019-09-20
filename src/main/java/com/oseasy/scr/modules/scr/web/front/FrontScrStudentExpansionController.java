package com.oseasy.scr.modules.scr.web.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyCert;
import com.oseasy.scr.modules.scr.service.ScoRapplyCertService;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontStudentExpansion")
public class FrontScrStudentExpansionController extends BaseController {
    @Autowired
    private ScoRapplyCertService scoRapplyCertService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private ScoRapplyService scoRapplyService;

  //获取技能证书
    @ResponseBody
    @RequestMapping(value="ajaxGetUserCertById")
    public ApiResult ajaxGetUserCertById(String userId) {
        try{
            ScoRapply scoRapply = new ScoRapply();
            scoRapply.setUser(new User(userId));
            scoRapply.setStatus(ScoRstatus.SRS_PASS.getKey());
            List<ScoRapplyCert> list = scoRapplyCertService.findCertByUserList(scoRapply);
            //获取附件
            for(ScoRapplyCert scoRapplyCert : list){
                //获取附件
                SysAttachment sysAttachment=new SysAttachment();
                sysAttachment.setUid(scoRapplyCert.getId());
                List<SysAttachment> sysAttachmentList=sysAttachmentService.getFiles(sysAttachment);
                scoRapplyCert.setSysAttachmentList(sysAttachmentList);
            }
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }
    //获取双创学分
    @ResponseBody
    @RequestMapping(value="ajaxGetCreditById")
    public ApiResult ajaxGetCreditById(String userId, HttpServletRequest request, HttpServletResponse response) {
        ScoQuery scoQuery = new ScoQuery();
        scoQuery.setUserId(userId);
        scoQuery.setStatus(ScoRstatus.SRS_PASS.getKey());
        Page<ScoQuery>  page = scoRapplyService.findCreditPage(new Page<ScoQuery>(request, response),scoQuery);
        return ApiResult.success(page);
    }
}