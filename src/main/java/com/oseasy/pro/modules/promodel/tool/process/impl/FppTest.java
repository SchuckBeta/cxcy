/**
 * .
 */

package com.oseasy.pro.modules.promodel.tool.process.impl;

import com.oseasy.act.modules.actyw.tool.process.impl.Fpparam;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.pro.modules.promodel.entity.ProModel;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通用表单参数初始化，ProModel，SysAttachmentService 必填.
 * @author chenhao
 *
 */
public class FppTest extends Fpparam {

    @Override
    public Boolean init(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        return super.init(model, request, response, objs);
    }

    @Override
    public Boolean initSysAttachment(Model model, HttpServletRequest request, HttpServletResponse response,
                                     Object... objs) {
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
        model.addAttribute("proModel",proModel);
        return super.initSysAttachment(model, request, response, objs);
    }

}
