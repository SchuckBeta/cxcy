package com.oseasy.com.fileserver.vsftp.web;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 附件信息表Controller
 * @author zy
 * @version 2017-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/")
public class FtpBackController extends BaseController {

	@Autowired
	private FtpService ftpService;

	@Autowired
	private SysAttachmentService sysAttachmentService;

	public static final String FTP_FILE_SIZE = CoreSval.getConfig("ftp.fileSize");

	@ResponseBody
	@RequestMapping(value="/urlLoad/file")
	public JSONObject urlLoad(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		// 转型为MultipartHttpRequest(重点的所在)
		String type= (String)request.getParameter("type");//
		String ftpId=(String)request.getParameter("ftpId");//临时文件id
		String imgWidth= (String)request.getParameter("imgWidth");//
		String imgHeight=(String)request.getParameter("imgHeight");//临时文件id
		if (ftpId.isEmpty()) {
			ftpId=IdGen.uuid();
		}
		JSONObject obj = new JSONObject();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile imgFile1 = multipartRequest.getFile("fileName");
		// 获得第1张图片（根据前台的name名称得到上传的文件）
		if (StringUtil.isNotEmpty(imgWidth) && StringUtil.isNotEmpty(imgHeight)) {
			BufferedImage bi =ImageIO.read(imgFile1.getInputStream());
      if (!(StringUtil.exeDivision(imgWidth, bi.getWidth()) && StringUtil.exeDivision(imgHeight, bi.getHeight()))) {
				obj.put("state",4);//文件同名
				obj.put("msg", "上传文件规格不符合");
				return obj;
			}
		}


		long m=imgFile1.getSize();
		//上传文件过大 超过10m
		if (m>Integer.valueOf(FTP_FILE_SIZE)*1024*1024) {
			obj.put("state",4);//文件同名
			obj.put("msg", "上传文件过大，超过100m");
			return obj;
		}

		//文件名
		String urlFileName=imgFile1.getOriginalFilename();//
		//得到文件名后缀，用id到名称保存。.
		String filename=urlFileName.substring(0,urlFileName.lastIndexOf("."));
		String suffix=urlFileName.substring(urlFileName.lastIndexOf(".")+1);

		String ftpPath=type+"/"+DateUtil.getDate("yyyy-MM-dd")+"/"+ftpId;
		InputStream is=imgFile1.getInputStream();

		filename=IdGen.uuid();
		String saveFileName=filename+"."+suffix;
		FTPClient ftpClient=FtpUtil.getftpClient();

		//判断是否有同名文件
		boolean isSame=FtpUtil.checkName(  "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath, saveFileName);
		if (isSame) {
			//流形式保存
			boolean res=FtpUtil.uploadInputSteam( is, "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath,saveFileName);
			if (res) {
				obj.put("state",1);//上传成功
				obj.put("fileName", urlFileName);
				obj.put("arrUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath+"/"+saveFileName);
				obj.put("ftpId", ftpId);
			}else{
				obj.put("state", 2);
				obj.put("fileName", urlFileName);
				obj.put("msg", "上传失败");
			}
		}else{
			obj.put("state",3);//文件同名
			obj.put("msg", "不能上传相同文件名");
		}
		//response.getWriter().print(obj.toString());
		return obj;
	}

	//测试上传文件到正式目录
	@ResponseBody
	@RequestMapping(value="/urlLoad/urlRenzhengLoad")
	public JSONObject urlRenzhengLoad(HttpServletRequest request,HttpServletResponse response) throws Exception {

		JSONObject obj = new JSONObject();
		String type= (String)request.getParameter("type");//
		//String urlName= (String)request.getParameter("urlName");//
		obj=ftpService.uploadFile(type, request);

		//response.getWriter().print(obj.toString());
		return obj;
	}

	@ModelAttribute
	public SysAttachment get(@RequestParam(required=false) String id) {
		SysAttachment entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = sysAttachmentService.get(id);
		}
		if (entity == null) {
			entity = new SysAttachment();
		}
		return entity;
	}

	//下载文件
	@RequestMapping(value = {"download"})
	public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//path ftp上文件 目录
		//String path=(String)request.getParameter("path");//
		//fileName ftp上文件名
		response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data;charset=UTF-8");
		//String url=(String)request.getParameter("url");//
		String fileName=(String)request.getParameter("fileName");//
		ftpService.downloadFile(fileName,response);




	}


	//下载文件
	@RequestMapping(value = {"loadUrl"})
	public void loadUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//path ftp上文件 目录
		//String path=(String)request.getParameter("path");//
		//fileName ftp上文件名
		response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data;charset=UTF-8");
		String url=(String)request.getParameter("url");//
		String fileName=(String)request.getParameter("fileName");//
		fileName=URLDecoder.decode(fileName,"UTF-8");
		ftpService.downloadUrlFile(url,fileName,response);
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
		/*
		FTPClient ftpClient=FtpUtil.getftpClient();*/
		//FtpUtil.remove(  fileName.substring(0,fileName.lastIndexOf("/")+1), fileName.substring(fileName.lastIndexOf("/")+1));
		JSONObject obj = new JSONObject();
		if (ftpdel) {
			obj.put("state",1);//删除成功
		}else{
			obj.put("state", 2);
			obj.put("msg", "文件太大");
		}
		//response.getWriter().print(obj.toString());
		return obj;
		//downloadFile(  response, fileName.substring(fileName.lastIndexOf("/")+1), fileName.substring(0,fileName.lastIndexOf("/")+1));
	}
}