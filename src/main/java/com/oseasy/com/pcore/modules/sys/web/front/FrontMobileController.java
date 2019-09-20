package com.oseasy.com.pcore.modules.sys.web.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.mqserver.common.utils.sms.SMSUtilAlidayu;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.vo.SysValCode;

/**
 * @author  zhangzheng
 * 发送短信验证码，验证短信验证码
 */
@Controller
@RequestMapping(value = "${frontPath}/mobile")
public class FrontMobileController extends BaseController {
	/**
	 * 发送短信验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sendMobileValidateCode")
	public Boolean sendMobileValidateCode(HttpServletRequest request) {
		String mobile = request.getParameter(SysValCode.F_MOBILE.getPkey());
		//发送短信 ，方法返回验证码
//		String code =  String.format("%06d", rand.nextInt(1000000));
		String code =  SMSUtilAlidayu.sendSms(mobile);
		//短信验证码保存到session
		SysValCode.putVerCode(SysValCode.F_MOBILE, request, code);
		return true;
	}

	/**
	 * 验证短信验证码   true:验证通过    false：验证不通过
	 * @return
	 */
	@RequestMapping("checkMobileValidateCode")
	@ResponseBody
	public Boolean validateCode(HttpServletRequest request,String yzm) {
		//session  获取验证码
		//String code = (String)request.getSession().getAttribute(MobilefrontController.mobileValidateCode);
		String code = SysValCode.getVerCode(SysValCode.F_MOBILE, request);


		// 比较验证码
		if (SysValCode.checkVerCode(SysValCode.F_MOBILE, request, yzm)) {
			return true;
		}
		return false;
	}
}
