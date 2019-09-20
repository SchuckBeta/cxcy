/**
 *
 */
package com.oseasy.com.fileserver.common.service;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.Encodes;

/**
 * Service上传
 *
 * @version 2014-05-16
 */

@Transactional(readOnly = true)
public  class UoloadFtpService {

	@Autowired
	private UserDao userDao;
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	 //上传ftp
	@Transactional(readOnly = false)
    public void ftpUplod(String fileName, JSONObject obj,InputStream inputStream,String userId) {
    	try {
    		String ftpId=IdGen.uuid();
    		//得到文件名后缀，用id到名称保存。.
    		String filename=fileName.substring(0,fileName.lastIndexOf("."));
    		String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
    		//String ftpPath=suffix+"/"+DateUtils.getDate("yyyy-MM-dd")+"/"+ftpId;
    		String ftpPath=userId+"/";//+DateUtils.getDate("yyyy-MM-dd");
    		filename=Encodes.urlEncode(filename);
    		//编码后名称
    		String saveFileName=filename+"."+suffix;
    		//判断是否有同名文件
    		boolean isSame=FtpUtil.checkName("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/sys/" +ftpPath, saveFileName);
    		if (isSame) {
    			//流形式保存
    			boolean res=FtpUtil.uploadInputSteam(inputStream, "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/sys/" +ftpPath,saveFileName);
    			this.syncDb("/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/sys/" +ftpPath+"/"+saveFileName, userId);
    			if (res) {
    				obj.put("state",1);//上传成功
    				obj.put("fileName", fileName);
    				obj.put("arrUrl", "/"+ TenantConfig.getCacheTenant()+Global.REMOTEPATH  + "/temp/" +ftpPath+"/"+saveFileName);
    				obj.put("ftpId", ftpId);
    			}else{
    				obj.put("state", 2);
    				obj.put("fileName", fileName);
    				obj.put("msg", "上传失败");
    			}
    		}else{
    			obj.put("state",3);//文件同名
    			obj.put("msg", "不能上传相同文件名");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    //同步数据库
	@Transactional(readOnly = false)
    public void syncDb(String imgurl,String userId) {
    	User user=new User();
    	user.setId(userId);
    	user.setPhoto(imgurl);
    	//user.setIsNewRecord(false);
    	userDao.updateUserInfo(user);
    }

	/**
	 * @param ftpUrl
	 *
	 * @return 返回  <Img src="?" />
	 */
	@Transactional(readOnly = false)
	public String downFtpByBase64(String ftpUrl) {
		try {
			FTPClient ftpClient=FtpUtil.getftpClient();
			InputStream in = null;
			in=ftpClient.retrieveFileStream(ftpUrl);
			byte[] bytes = null;
			bytes = input2byte(in);
			String img64= Encodes.encodeBase64(bytes);
			in.close();
			ftpUrl=ftpUrl.substring(ftpUrl.lastIndexOf(".")+1);
	        img64="data:image/"+ftpUrl+";base64,"+img64;

			return img64;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	//删除指定文件
	@Transactional(readOnly = false)
	public  boolean delFtpImg(String urlImg,String userId) {
		try {
			this.syncDb("", userId);
			FTPClient ftpClient=FtpUtil.getftpClient();
			return ftpClient.deleteFile(urlImg);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] input2byte(InputStream inStream)throws IOException{
	        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
	        byte[] buff = new byte[100];
	        int rc = 0;
	        while ((rc = inStream.read(buff, 0, 100)) > 0)
	        {
	            swapStream.write(buff, 0, rc);
	        }
	        byte[] in2b = swapStream.toByteArray();

	        swapStream.close();

	        return in2b;
	    }
}
