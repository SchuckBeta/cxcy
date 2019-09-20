package com.oseasy.com.fileserver.modules.vsftp.service;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.common.vsftp.config.Global;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional(readOnly = true)
public class UeditorUploadService {

	private Logger logger = LoggerFactory.getLogger(UeditorUploadService.class);
	@Autowired
	private SysAttachmentService sysAttachmentService;

	/**
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public JSONObject uploadTempBiz(HttpServletRequest request) {
		return sysAttachmentService.uploadTempBiz(request);
	}

	/**
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public JSONObject uploadTempIndex(HttpServletRequest request) {
		return sysAttachmentService.uploadTempIndex(request);
	}

	public JSONObject uploadTempFtp(HttpServletRequest request) {
		return sysAttachmentService.uploadTempFtp(request);
	}
	/**
	 * 从ftp文件中copy到临时目录

	 * @return
	 * @throws IOException
	 */
	public JSONObject copyFile(SysAttachment syAttachment) {
		InputStream in = null;
		JSONObject obj = new JSONObject();
		try {
			String urlFileName = syAttachment.getName();
			// 得到文件名后缀，用id到名称保存。.
			String filename = urlFileName.substring(0, urlFileName.lastIndexOf(".")); // 文件名
			String suffix = syAttachment.getSuffix();
			String ftpId = IdGen.uuid();
			String saveFileName = ftpId + "." + suffix;
			String folder ="ueditor";
			String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
			String remotePath = Global.REMOTEPATH + "/temp/" + ftpPath;
			boolean res = VsftpUtils.copyFile(syAttachment.getFileName(),syAttachment.getRemotePath(),saveFileName,remotePath);
			//boolean res = VsftpUtils.uploadFile(remotePath, saveFileName, in);
			if (res) {
				obj.put("state", "SUCCESS");//复制成功
				obj.put("original", filename);
				obj.put("title", urlFileName);
				obj.put("type", suffix);
				obj.put("name", filename);
				String param = "?fielTitle=" + urlFileName + "&fielType=" + suffix;
				obj.put("ftpUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH + "/temp/" + ftpPath + "/" + saveFileName );
				obj.put("url", FtpUtil.ftpImgUrl("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH + "/temp/" + ftpPath + "/" + saveFileName) + param);
			}
			return obj;
		} catch (Exception e) {
			logger.error(e.getMessage());
			obj.put("state", "FAIL");// 复制失败
			return obj;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 上传到正式目录，成功返回SysAttachment 失败返回null
	 *
	 * @param request
	 *            传参upfile 文件input的name;folder 文件子目录
	 * @return
	 */
	public SysAttachment upload(HttpServletRequest request) {
		InputStream in = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile imgFile1 = multipartRequest.getFile("upfile"); // 文件
			String urlFileName = imgFile1.getOriginalFilename();
			// 得到文件名后缀，用id到名称保存。.
			String suffix = urlFileName.substring(urlFileName.lastIndexOf(".") + 1);
			String ftpId = IdGen.uuid();
			String saveFileName = ftpId + "." + suffix;
			String folder = request.getParameter("folder");
			if (StringUtil.isBlank(folder)) {
				folder = "default";
			}
			String ftpPath = folder + "/" + DateUtil.getDate("yyyy-MM-dd");
			long size = imgFile1.getSize();
			String remotePath = Global.REMOTEPATH+ ftpPath;
			in = imgFile1.getInputStream();
			VsftpUtils.uploadFile(remotePath, saveFileName, in);
			SysAttachment sa = new SysAttachment();
			sa.setSize(size + "");
			sa.setName(urlFileName);
			sa.setSuffix(suffix);
			sa.setUrl("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH+ ftpPath + "/" + saveFileName);
			return sa;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

	}

	@Transactional(readOnly = false)
	public boolean delFile(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String url =request.getParameter("url");
		return sysAttachmentService.delFile(id, url);
	}

	public JSONObject uploadPwTemp(HttpServletRequest request, HttpServletResponse response) {
		return sysAttachmentService.uploadPwTempBiz(request);
	}
}
