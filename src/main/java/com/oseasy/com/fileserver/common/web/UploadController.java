package com.oseasy.com.fileserver.common.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.com.fileserver.common.service.UoloadFtpService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.util.common.utils.Encodes;

@Controller
@RequestMapping("upload")
public class UploadController {
	@Autowired
	private UoloadFtpService uoloadFtpService;
	@Autowired
	private UserService userService;


    //上传到ftp目录
    @RequestMapping( "/ftpfileUpload" )
    public void ftpfileUpload(@RequestParam ("file") MultipartFile fileUpload,String userId,HttpServletRequest request,HttpServletResponse response) throws IOException{
      String img64=	Encodes.encodeBase64(fileUpload.getBytes());
      //System.err.println("img64="+img64);
	  JSONObject obj = new JSONObject();
      SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddhhmmss" );
      String fileName = sFormat.format(Calendar.getInstance().getTime())+ new Random().nextInt(1000);
      String originalFilename = fileUpload.getOriginalFilename();
      fileName += originalFilename.substring(originalFilename.lastIndexOf("." ));

      InputStream inputStream = null ;


       try{
          inputStream = fileUpload.getInputStream();
          uoloadFtpService.ftpUplod(fileName, obj, inputStream,userId);
          inputStream.close();
          obj.put("img64", img64);
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }catch (IOException e) {
          e.printStackTrace();
      }
       response.getWriter().print(obj.toString());

    }

    //删除指定文件
	@RequestMapping(value = "delImg")
	public void delImg(String userId, Model model,HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject json=new JSONObject();
		User user= userService.findUserById(userId);
		if (user!=null) {
			if (user.getPhoto()!=null&&!user.getPhoto().equals("")) {
				boolean bl= uoloadFtpService.delFtpImg(user.getPhoto(),user.getId());
				json.put("success", bl);
			}else{
				json.put("success", false);
			}
		}else{
			json.put("success", false);
		}
		System.err.println("json="+json.toString());
		 response.getWriter().print(json.toString());
	}


}
