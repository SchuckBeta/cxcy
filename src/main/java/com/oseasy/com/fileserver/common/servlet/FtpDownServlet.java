package com.oseasy.com.fileserver.common.servlet;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.net.ftp.FTPClient;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.Encodes;

/**
 *

 * @version 2014-06-25
 */

public class FtpDownServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("ppppppppppppppppppp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//String type= (String)request.getParameter("type");//
		//String name=(String)request.getParameter("name");//
		//需要返回的fileName
		String fileName = null;
		//判断是否为文件流
		//boolean isMultipart =  ServletFileUpload.isMultipartContent(request);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
/*		String rootPath=getClass().getResource("/").getFile().toString();
		String realPath = "E:\\f\\load";*/
		Map param = new HashMap();
		JSONObject obj = new JSONObject();
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem item : items) {
				//其他参数
			    if (item.isFormField()) {
			        param.put(item.getFieldName(), item.getString("utf-8"));//如果你页面编码是utf-8的
			    }
			}
			String type= (String)param.get("type");//
			String name=(String)param.get("name");//
			for(FileItem item : items) {
				String filetype = item.getContentType();
				if (filetype == null) {
					continue;
				}
				//文件参数
				fileName = item.getName();

				/*File dir = new File(realPath);
				File f = new File(dir, fileName);
				if (f.exists()) {
					f.delete();
				}
				f.createNewFile();
				//保存
				item.write(f);*/
				//得到文件名后缀，用id到名称保存。.
				String[] index=fileName.split("\\.");
				String filename=index[0];

				String suffix=index[1];
				String attId=IdGen.uuid();
				String ftpPath=type+"/"+DateUtil.getDate();

				filename=Encodes.urlEncode(filename);
				//编码后名称
				String saveFileName=filename+"."+suffix;
				//判断是否有同名文件
				boolean isSame=FtpUtil.checkName("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH+ftpPath, saveFileName);
				if (isSame) {
					//流形式保存
					boolean res=FtpUtil.uploadInputSteam(item.getInputStream(),"/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH+ftpPath,saveFileName);
					if (res) {
						obj.put("state",1);//上传成功
						obj.put("fileName", fileName);
						obj.put("arrUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH+ftpPath+"/"+saveFileName);
					}else{
						obj.put("state", 2);
						obj.put("url", fileName);
						obj.put("url", fileName);
					}
				}else{

					obj.put("state",3);//文件同名
				}
				//sysAttachmentService.save(sysAttachment);
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			obj.put("state", 2);
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("state", 2);
		}
		//String storepath=""
       // t.upload("192.168.0.105","ftponly",2121,"os-easy","/tool/oseasy/gcontest",realPath+"\\"+fileName,fileName);
		//返回结果
		response.getWriter().print(obj.toString());
	}
}
