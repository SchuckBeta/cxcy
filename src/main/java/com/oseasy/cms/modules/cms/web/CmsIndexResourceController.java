package com.oseasy.cms.modules.cms.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsIndexResource;
import com.oseasy.cms.modules.cms.service.CmsIndexResourceService;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 资源Controller
 * @author daichanggeng
 * @version 2017-04-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsIndexResource")
public class CmsIndexResourceController extends BaseController {

	@Autowired
	private CmsIndexResourceService cmsIndexResourceService;

	@Autowired
	private SysAttachmentService sysAttachmentService;

	@Autowired
	private FtpService ftpService;


	@ModelAttribute
	public CmsIndexResource get(@RequestParam(required=false) String id) {
		CmsIndexResource entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = cmsIndexResourceService.get(id);
		}
		if (entity == null) {
			entity = new CmsIndexResource();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsIndexResource:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsIndexResource cmsIndexResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsIndexResource> page = cmsIndexResourceService.findPage(new Page<CmsIndexResource>(request, response), cmsIndexResource);
		model.addAttribute("page", page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsIndexResourceList";
	}


	@RequiresPermissions("cms:cmsIndexResource:view")
	@RequestMapping(value = "form")
	public String form(CmsIndexResource cmsIndexResource, Model model) {

		SysAttachment sysAttachment=new SysAttachment();
		if (cmsIndexResource.getId() != null) {
			sysAttachment.setUid(cmsIndexResource.getId());
			List<SysAttachment> sysAttachments=sysAttachmentService.findList(sysAttachment);
			model.addAttribute("sysAttachments", sysAttachments);
			if (StringUtil.isNotEmpty(cmsIndexResource.getContent())) {
				cmsIndexResource.setContent(cmsIndexResource.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
				cmsIndexResource.setContent(StringEscapeUtils.unescapeHtml4(cmsIndexResource.getContent()));
			}
		}

		model.addAttribute("cmsIndexResource", cmsIndexResource);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsIndexResourceForm";
	}

	@RequiresPermissions("cms:cmsIndexResource:edit")
	@RequestMapping(value = "save")
	public String save(CmsIndexResource cmsIndexResource,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsIndexResource)) {
			return form(cmsIndexResource, model);
		}
		if (StringUtil.isNotEmpty(cmsIndexResource.getContent()))cmsIndexResource.setContent(cmsIndexResource.getContent().replaceAll(FtpUtil.FTP_HTTPURL,FtpUtil.FTP_MARKER));
		cmsIndexResourceService.save(cmsIndexResource);

		String[] arrUrl= request.getParameterValues("arrUrl");
		String[] arrNames= request.getParameterValues("arrName");

		if (arrUrl!=null&&arrUrl.length>0) {
			for(int i=0;i<arrUrl.length;i++) {
				 try {
					String moveEnd=FtpUtil.moveFile( arrUrl[i]);
					arrUrl[i]=moveEnd;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SysAttachment sysAttachment=new SysAttachment();
				sysAttachment.setUid(cmsIndexResource.getId());
				sysAttachment.setType(FileTypeEnum.S4);
				sysAttachment.setName(arrNames[i]);
				sysAttachment.setUrl(arrUrl[i]);
				sysAttachment.setSuffix(arrNames[i].substring(arrNames[i].lastIndexOf(".")+1));
				sysAttachmentService.save(sysAttachment);
			}
		}

		addMessage(redirectAttributes, "保存资源管理成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsIndexResource/?repage";
	}

	@RequiresPermissions("cms:cmsIndexResource:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsIndexResource cmsIndexResource, RedirectAttributes redirectAttributes) {
		cmsIndexResourceService.delete(cmsIndexResource);
		addMessage(redirectAttributes, "删除资源管理成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsIndexResource/?repage";
	}

			//删除文件
	@RequestMapping(value = {"delload"})
	@ResponseBody
	public JSONObject delload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//path ftp上文件 目录
		//String path=(String)request.getParameter("path");//
		//fileName ftp上文件名
		String fileName=(String)request.getParameter("fileName");
		boolean ftpdel=ftpService.del(fileName);
		/*FtpUtil ftpUtil = new FtpUtil();
		FTPClient ftpClient=ftpUtil.getftpClient();*/
		//ftpUtil.remove(ftpClient, fileName.substring(0,fileName.lastIndexOf("/")+1), fileName.substring(fileName.lastIndexOf("/")+1));
		JSONObject obj = new JSONObject();
		SysAttachment sysAttachment = new SysAttachment();
		sysAttachment = sysAttachmentService.getByUrl(fileName);
		sysAttachmentService.delete(sysAttachment);
		if (ftpdel) {
			obj.put("state",1);//删除成功
		}else{
			obj.put("state", 2);
			obj.put("msg", "文件太大");
		}
		//response.getWriter().print(obj.toString());
		return obj;
		//downloadFile(ftpClient, response, fileName.substring(fileName.lastIndexOf("/")+1), fileName.substring(0,fileName.lastIndexOf("/")+1));
	}
}