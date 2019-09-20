/**
 * .
 */

package com.oseasy.pro.modules.promodel.tool.process.impl;

import com.oseasy.act.modules.actyw.tool.process.impl.Fpparam;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.pro.modules.promodel.entity.ProModel;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 互联网+大赛表单参数初始化，ProModel，SysAttachmentService，ProModelMdService 必填.
 * @author chenhao
 */
public class GppGzsmxx extends Fpparam{

    @Override
    public Boolean init(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        return super.init(model, request, response, objs);
    }

    @Override
    public Boolean initSysAttachment(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        ProModel proModel = (ProModel) objs[0];
        SysAttachmentService sysAttachmentService = (SysAttachmentService) objs[1];
        if((proModel == null) || (sysAttachmentService == null) ){
            return true;
        }
//        model.addAttribute("proModel",proModel);
        SysAttachment sa=new SysAttachment();
        sa.setUid(proModel.getId());
        List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
        model.addAttribute("sysAttachments", fileListMap);
        return super.initSysAttachment(model, request, response, objs);
    }

}
