package com.oseasy.com.fileserver.modules.vsftp.service;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.DateUtil;

import net.sf.json.JSONObject;

/**
 * 大赛信息Service
 * @author zy
 * @version 2017-03-11
 */
@Service
@Transactional(readOnly = true)
public class FtpService{

	public void downloadFile(String fileName,HttpServletResponse response) {
		FtpUtil.downloadFile(response,
				fileName.substring(fileName.lastIndexOf("/")+1),
				fileName.substring(0,fileName.lastIndexOf("/")+1));
	}

	public JSONObject upLoadForUeditor(String type,HttpServletRequest request) {
		JSONObject obj = new JSONObject();
		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获得第1张图片（根据前台的name名称得到上传的文件）
			MultipartFile imgFile1 = multipartRequest.getFile("upfile");
			//文件名
			String urlFileName=imgFile1.getOriginalFilename();
			//得到文件名后缀，用id到名称保存。.
			String filename=urlFileName.substring(0,urlFileName.lastIndexOf("."));
			String suffix=urlFileName.substring(urlFileName.lastIndexOf(".")+1);

			InputStream is=imgFile1.getInputStream();
			filename=IdGen.uuid();
			String saveFileName=filename+"."+suffix;
			String ftpPath=type+"/"+DateUtil.getDate("yyyy-MM-dd")+"/";
			boolean res=FtpUtil.uploadInputSteam(is, "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH +ftpPath,saveFileName);
			if (res) {
				obj.put("state","SUCCESS");//上传成功
				obj.put("type", "."+suffix);
				obj.put("url", FtpUtil.ftpImgUrl( "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH +ftpPath+saveFileName));
			}else{
				obj.put("state", 2);
				obj.put("msg", "上传失败");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return obj;
	}
	public JSONObject uploadFile(String type,HttpServletRequest request) {
		JSONObject obj = new JSONObject();
		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

			// 获得第1张图片（根据前台的name名称得到上传的文件）
			MultipartFile imgFile1 = multipartRequest.getFile("fileName");
			//文件名
			String urlFileName=imgFile1.getOriginalFilename();
			//得到文件名后缀，用id到名称保存。.
			String filename=urlFileName.substring(0,urlFileName.lastIndexOf("."));
			String suffix=urlFileName.substring(urlFileName.lastIndexOf(".")+1);

			InputStream is=imgFile1.getInputStream();
			filename=IdGen.uuid();
			String saveFileName=filename+"."+suffix;
			String ftpPath=type+"/"+DateUtil.getDate("yyyy-MM-dd")+"/";

			boolean res=FtpUtil.uploadInputSteam(is, "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH+ftpPath,saveFileName);
			if (res) {
				obj.put("state",1);//上传成功
				obj.put("fileName", urlFileName);
				obj.put("arrUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH +ftpPath+saveFileName);
				obj.put("httpUrl", FtpUtil.ftpImgUrl((String)obj.get("arrUrl")));
			}else{
				obj.put("state", 2);
				obj.put("fileName", urlFileName);
				obj.put("msg", "上传失败");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return obj;
	}

	public void downloadUrlFile(String url,String name,HttpServletResponse response) {
		FtpUtil.downloadUrlFile( response, name,
				url.substring(url.lastIndexOf("/")+1),
				url.substring(0,url.lastIndexOf("/")+1));
	}

	public boolean del(String fileName) {
		FTPClient ftpClient;
		boolean ftpdel=false;
		try {
			ftpdel=	FtpUtil.remove( fileName.substring(0,fileName.lastIndexOf("/")+1), fileName.substring(fileName.lastIndexOf("/")+1));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ftpdel;
	}



}