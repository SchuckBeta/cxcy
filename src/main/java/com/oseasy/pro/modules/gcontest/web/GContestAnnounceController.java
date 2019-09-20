package com.oseasy.pro.modules.gcontest.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.gcontest.entity.GContestAnnounce;
import com.oseasy.pro.modules.gcontest.service.GContestAnnounceService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大赛通告表Controller
 * @author zdk
 * @version 2017-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/gcontest/gContestAnnounce")
public class GContestAnnounceController extends BaseController {

	@Autowired
	private GContestAnnounceService gContestAnnounceService;

	@Autowired
	private SysAttachmentService sysAttachmentService;

	@Autowired
	private FtpService ftpService;


	@ModelAttribute
	public GContestAnnounce get(@RequestParam(required=false) String id) {
		GContestAnnounce entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = gContestAnnounceService.get(id);
		}
		if (entity == null) {
			entity = new GContestAnnounce();
		}
		return entity;
	}



	@RequiresPermissions("gcontest:gContestAnnounce:view")
	@RequestMapping(value = {"list", ""})
	public String list(GContestAnnounce gContestAnnounce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GContestAnnounce> page = gContestAnnounceService.findPage(new Page<GContestAnnounce>(request, response), gContestAnnounce);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAnnounceList";
	}

	@RequiresPermissions("gcontest:gContestAnnounce:view")
	@RequestMapping(value = "form")
	public String form(GContestAnnounce gContestAnnounce, Model model,HttpServletRequest request) {
		SysAttachment sysAttachment=new SysAttachment();
		if (gContestAnnounce.getId()!=null) {
			sysAttachment.setUid(gContestAnnounce.getId());
			List<SysAttachment> sysAttachments=sysAttachmentService.findList(sysAttachment);
			model.addAttribute("sysAttachments", sysAttachments);
		}
		String operationType = request.getParameter("operationType");
    if (StringUtil.isNotEmpty(operationType)) {
			model.addAttribute("operationType", operationType);
		}
		model.addAttribute("gContestAnnounce", gContestAnnounce);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAnnounceForm";
	}


	@RequestMapping(value = "formEdit")
	public String formEdit(GContestAnnounce gContestAnnounce, Model model,HttpServletRequest request) {
		SysAttachment sysAttachment=new SysAttachment();
		if (gContestAnnounce.getId()!=null) {
			sysAttachment.setUid(gContestAnnounce.getId());
			List<SysAttachment> sysAttachments=sysAttachmentService.findList(sysAttachment);
			model.addAttribute("sysAttachments", sysAttachments);
		}
		String operationType = request.getParameter("operationType");

    if (StringUtil.isNotEmpty(operationType)) {
			model.addAttribute("operationType", operationType);
		}
		model.addAttribute("gContestAnnounce", gContestAnnounce);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAnnounceFormEdit";
	}

	@RequiresPermissions("gcontest:gContestAnnounce:edit")
	@RequestMapping(value = "save")
	public String save(GContestAnnounce gContestAnnounce, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, gContestAnnounce)) {
			return form(gContestAnnounce, model,request);
		}

		//gContestAnnounceService.saveGContestAnnounce(gContestAnnounce);
		gContestAnnounceService.save(gContestAnnounce);
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
				sysAttachment.setUid(gContestAnnounce.getId());
				sysAttachment.setType(FileTypeEnum.S3);
				sysAttachment.setName(arrNames[i]);
				sysAttachment.setUrl(arrUrl[i]);
				sysAttachment.setSuffix(arrNames[i].substring(arrNames[i].lastIndexOf(".")+1));
				sysAttachmentService.save(sysAttachment);
			}
		}
	//	addMessage(redirectAttributes, "保存大赛通告表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContestAnnounce/?repage";
	}

	@RequiresPermissions("gcontest:gContestAnnounce:edit")
	@RequestMapping(value = "delete")
	public String delete(GContestAnnounce gContestAnnounce, RedirectAttributes redirectAttributes) {
		gContestAnnounceService.delete(gContestAnnounce);
	//	addMessage(redirectAttributes, "删除大赛通告表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContestAnnounce/?repage";
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
		@RequestMapping(value="validateName")
		@ResponseBody
		public String validateName(String name) {
		//	String name = request.getParameter("name");
			if (gContestAnnounceService.getGContestByName(name)!=null) {
				return "1";
			}
			return "0";

		}
		@RequestMapping(value="publish")
		@ResponseBody
		public String publish(String type) {
			GContestAnnounce gContestAnnounce = new GContestAnnounce();
			gContestAnnounce.setType(type);
			List<GContestAnnounce> list = gContestAnnounceService.findList(gContestAnnounce);
			for (GContestAnnounce gContestAnnounce2 : list) {
				String status = gContestAnnounce2.getStatus();
				if ("1".equals(status)) {
					return "1";
				}else if ("0".equals(status)) {
					return "2";
				}else {
					continue;
				}
			}
			return "-1";
		}

}