package com.oseasy.com.pcore.modules.sys.web;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.mqserver.common.utils.sms.SMSUtilAlidayu;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.vo.SysValCode;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.Msg;

/**
 * 注册Controller
 * @author zhang
 * @version 2017-3-22
 */

@Controller
@RequestMapping(value = "${frontPath}/register")
public class RegisterController extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(RegisterController.class);
    public static final String REG_TYPE = "regType";
    public static final String USER_TYPE = "userType";
    /**
     * 短信验证码Key.
     */
    private static final String HQYAZHENMA = "hqyazhenma";
    //注册类型，手机注册或者学号工号注册
    private final static String REG_TYPE_MOBILE = "mobile";
    public final static String REG_TYPE_NO = "no";

    public static final String YANZHENGMA = "yanzhengma";
    public static final String VALIDATE_CODE = "validateCode";
    public static final String YZMA = "yzma";

    @Autowired
    private UserService userService;
    @Autowired
    private CoreService coreService;

    /**
     * 通过手机验证用户是否已存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/validatePhone")
    public Boolean validatePhone(HttpServletRequest request) {
        String mobile = request.getParameter("mobile");
        User user = new User();
        user.setMobile(mobile);
        user.setDelFlag("0");
        user = userService.getByMobile(user);
        if (user != null) {
            return false;
        }
        return true;
    }

    /**
     * 获取短信验证码
     *
     * @param request mobile
     */
    @ResponseBody
    @RequestMapping(value = "/getVerificationCode", method = RequestMethod.GET)
    public String getVerificationCode(HttpServletRequest request) {
        String mobile = request.getParameter(SysValCode.F_MOBILE.getPkey());
        if(StringUtil.isEmpty(mobile)){
            return "手机号参数错误";
        }
        String sm = SMSUtilAlidayu.sendSms(mobile);
        if(StringUtil.isNotEmpty(sm)){
            SysValCode.putVerCode(SysValCode.F_MOBILE, request, sm);
            return "验证码发送完成";
        }
        return "验证码发送失败";
    }

    /**
     * 验证验证码是否输入正确
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "validateYZM")
    public Boolean validateYZM(HttpServletRequest request) {
        String sruyazhengma = request.getParameter(SysValCode.F_MOBILE.getPkey());
        if(StringUtil.isEmpty(sruyazhengma)){
            return false;
        }

        String cvcode = SysValCode.getVerCode(SysValCode.F_MOBILE, request);
        if(StringUtil.isEmpty(cvcode)){
            return false;
        }
        return sruyazhengma.equals(cvcode);
    }

    /**
     * 从缓存或Session中获取验证码
     * @param request HttpServletRequest
     * @param sruyazhengma String
     * @return String
     */
    public static String getVerCode(HttpServletRequest request, String sruyazhengma) {
        String hqyazhengma = (String) CacheUtils.get(SysValCode.VKEY + request.getSession().getId());
        if(StringUtil.isEmpty(hqyazhengma)){
            hqyazhengma = (String) CacheUtils.get(HQYAZHENMA + request.getSession().getId(), HQYAZHENMA + request.getSession().getId());
        }else if(StringUtil.isNotEmptys(hqyazhengma)){
            logger.info("VerCode get key:" + SysValCode.VKEY + request.getSession().getId()  + " , " + SysValCode.VKEY + request.getSession().getId() + " = " + hqyazhengma + "-->"+(hqyazhengma).equals(sruyazhengma));
        }
        if(StringUtil.isEmpty(hqyazhengma)){
            hqyazhengma = (String) CacheUtils.get(YANZHENGMA + request.getSession().getId(), YANZHENGMA + request.getSession().getId());
        }else if(StringUtil.isNotEmptys(hqyazhengma)){
            logger.info("VerCode get key:" + HQYAZHENMA + request.getSession().getId()  + " , " + HQYAZHENMA + request.getSession().getId() + " = " + hqyazhengma + "-->"+(hqyazhengma).equals(sruyazhengma));
        }

        if(StringUtil.isEmpty(hqyazhengma)){
            hqyazhengma = (String) CacheUtils.get(VALIDATE_CODE + request.getSession().getId(), VALIDATE_CODE + request.getSession().getId());
        }else if(StringUtil.isNotEmptys(hqyazhengma)){
            String yzm = YANZHENGMA;
            logger.info("VerCode get key:" + YANZHENGMA + request.getSession().getId()  + " , " + yzm + request.getSession().getId() + " = " + hqyazhengma + "-->"+(hqyazhengma).equals(sruyazhengma));
        }

        return hqyazhengma;
    }

    /**
     * 获取请求中的验证码.
     * @param request HttpServletRequest
     * @return String
     */
    public static String getVreqCode(HttpServletRequest request) {
        String sruyazhengma = request.getParameter(VALIDATE_CODE);
        if(StringUtil.isEmpty(sruyazhengma)){
            sruyazhengma = request.getParameter(YANZHENGMA);
        }
        if(StringUtil.isEmpty(sruyazhengma)){
            sruyazhengma = request.getParameter(HQYAZHENMA);
        }
        return sruyazhengma;
    }

    @RequestMapping(value = "/studentregistersuccessful")
    public String studentregistersuccessful(Model model, String userType) {
        model.addAttribute(USER_TYPE, userType);
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "registerSuccessful";
    }

    @RequestMapping(value = "/registerFail")
    public String registerFail(Model model,String userType, String msg) {
        model.addAttribute(USER_TYPE, userType);
        model.addAttribute(CoreJkey.JK_MSG, msg);
        model.addAttribute(CoreJkey.JK_URL, CoreSval.getFrontPath() + Retype.F_REGISTER.getUrl());
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "registerFail";
    }

    @RequestMapping(value = "/studentExpanSion")
    public String studentExpanSion() {
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "studentregistersuccess";
    }

    /**
     * 校验验证码
     * @param request
     * @param regType 注册类型，手机：mobile, 学号/工号：no
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/checkCode")
    @ResponseBody
    public Boolean checkCode(HttpServletRequest request, String regType, String code) {
        return SysValCode.checkVerCode(SysValCode.F_LOGIN, request, code);
    }


    /**
     * 校验登录名唯一性
     * @param loginName
     * @param userId
     * @return
     */
    @RequestMapping(value = "/checkLoginNameUnique")
    @ResponseBody
    public Boolean checkLoginNameUnique(String loginName, String userId) {
        return coreService.checkLoginNameUnique(loginName, userId);
    }

    /**
     * 校验学号/工号唯一性
     * @param no
     * @param userId
     * @param userType
     * @return
     */
    @RequestMapping(value = "/checkNoUnique")
    @ResponseBody
    public Boolean checkNoUnique(String no, String userId, String userType) {
        return coreService.checkNoUnique(no, userId);
    }

    @RequestMapping(value = "/checkMobileUnique")
    @ResponseBody
    public Msg checkMobileUnique(String mobile, String userId) {
        if (coreService.checkMobileUnique(mobile, userId)) {
            return Msg.ok("手机号可使用");
        } else {
            return Msg.error("手机号已存在");
        }
    }
}
