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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.DateUtil;


/**
 *

 * @version 2014-06-25
 */

public class FtpUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(FtpUpServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("ppppppppppppppppppp");
	}

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
		String agent=request.getHeader("User-Agent").toLowerCase();

		Map<String, String> param = new HashMap<String, String>();
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
			String ftpId=(String)param.get("ftpId");//临时文件id
			if (ftpId.isEmpty()) {
				ftpId=IdGen.uuid();
			}
			for(FileItem item : items) {
				String filetype = item.getContentType();
				if (filetype == null) {
					continue;
				}
				//文件参数
				fileName = item.getName();
				//ie ie11上传路径为全路径
				if (agent.indexOf("msie")>0) {
					fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
				}else if ((agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0)) {
					fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
				}
				/*File dir = new File(realPath);
				File f = new File(dir, fileName);
				if (f.exists()) {
					f.delete();
				}
				f.createNewFile();
				//保存
				item.write(f);*/
				logger.debug("上传文件路径："+fileName);
				int m=item.getInputStream().available();
				//上传文件过大 超过100m
				if (m>100*1024*1024) {
					obj.put("state",4);//文件同名
					obj.put("msg", "上传文件过大，超过100m");
				}else{
					//得到文件名后缀，用id到名称保存。.
					String filename=fileName.substring(0,fileName.lastIndexOf("."));
					String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
							//index[1];
					//String attId=IdGen.uuid();
					String ftpPath=type+"/"+DateUtil.getDate("yyyy-MM-dd")+"/"+ftpId;

					filename=IdGen.uuid();
					//编码后名称
					String saveFileName=filename+"."+suffix;
					//判断是否有同名文件
					boolean isSame=FtpUtil.checkName( "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath, saveFileName);
					if (isSame) {
						//流形式保存
						boolean res=FtpUtil.uploadInputSteam(item.getInputStream(),
								"/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath,saveFileName);
						if (res) {
							obj.put("state",1);//上传成功
							obj.put("fileName", fileName);
							obj.put("arrUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath+"/"+saveFileName);
							obj.put("ftpId", ftpId);
							obj.put("msg", "上传成功");
						}else{
							obj.put("state", 2);
							obj.put("fileName", fileName);
							obj.put("msg", "上传失败");
						}
					}else{
						obj.put("state",3);//文件同名
						obj.put("msg", "不能上传相同文件名");
					}
				}
				//sysAttachmentService.save(sysAttachment);
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			obj.put("state", 2);
			obj.put("msg", "异常：FileUploadException");
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("state", 2);
			obj.put("msg", "异常：Exception");
		}
		//String storepath=""
       // t.upload("192.168.0.105","ftponly",2121,"os-easy","/tool/oseasy/gcontest",realPath+"\\"+fileName,fileName);
		//返回结果
		response.getWriter().print(obj.toString());
	}

	public String getBrowserName(String agent) {
		if (agent.indexOf("msie 7")>0) {
			return "ie7";
		}else if (agent.indexOf("msie 8")>0) {
			return "ie8";
		}else if (agent.indexOf("msie 9")>0) {
			return "ie9";
		}else if (agent.indexOf("msie 10")>0) {
			return "ie10";
		}else if (agent.indexOf("msie")>0) {
			return "ie";
		}else if (agent.indexOf("opera")>0) {
			return "opera";
		}else if (agent.indexOf("opera")>0) {
			return "opera";
		}else if (agent.indexOf("firefox")>0) {
			return "firefox";
		}else if (agent.indexOf("webkit")>0) {
			return "webkit";
		}else if (agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0) {
			return "ie11";
		}else{
			return "Others";
		}
	}
}
