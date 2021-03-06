/**
 * .
 */

package com.oseasy.pro.modules.promodel.tool.process.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.oseasy.act.modules.actyw.tool.process.impl.Fpparam;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.pro.modules.promodel.entity.ProModel;

/**
 * 默认表单参数初始化，ProModel 必填.
 * @author chenhao
 *
 */
public class FppMr extends Fpparam{

    @Override
    public Boolean init(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        return super.init(model, request, response, objs);
    }

    @Override
    public Boolean initSysAttachment(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        ProModel proModel = (ProModel) objs[0];
        SysAttachmentService sysAttachmentService = (SysAttachmentService) objs[1];
        if((proModel == null) || (sysAttachmentService == null)){
            return true;
        }

        SysAttachment sa=new SysAttachment();
        sa.setUid(proModel.getId());
        sa.setType(FileTypeEnum.S11);
        List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
        model.addAttribute("sysAttachments", fileListMap);
        return super.initSysAttachment(model, request, response, objs);
    }

}
