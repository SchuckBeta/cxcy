package com.oseasy.com.mqserver.common.utils.sms;

import java.util.List;
import java.util.Random;

import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oseasy.com.mqserver.common.utils.sms.impl.SendOeyParam;
import com.oseasy.com.mqserver.common.utils.sms.impl.SendParam;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * api:
 * https://api.alidayu.com/doc2/apiDetail?spm=a3142.8260425.1999205497.20.j8eFPJ&apiId=25450
 */
public class SMSUtilAlidayu {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SMSUtilAlidayu.class);
    public static final String AILIDAYU_SMS_SECRET = CoreSval.getConfig("ailidayu.sms.secret");
    public static final String AILIDAYU_SMS_APPKEY = CoreSval.getConfig("ailidayu.sms.appkey");
    public static final String AILIDAYU_SMS_URL = CoreSval.getConfig("ailidayu.sms.url");
    //校验码模板
    public static final String AILIDAYU_SMS_TemplateCode = CoreSval.getConfig("ailidayu.sms.templatecode");
    public static final String AILIDAYU_SMS_SIGN = CoreSval.getConfig("ailidayu.sms.sign");

//    邀请加入团队短信模板
    public static final String AILIDAYU_SMS_TemplateInvite = CoreSval.getConfig("ailidayu.sms.template.invite");

//    申请加入团队短信模板
    public static final String AILIDAYU_SMS_TemplateApply = CoreSval.getConfig("ailidayu.sms.template.apply");


    public static Random rand = new Random();

    /**
     * 发送短信，签名为开窗啦.
     * @param tel 电话号码
     * @return String
     */
    public static String sendSms(String tel) {
      return sendSms(tel, AILIDAYU_SMS_SIGN);
    }

    /**
     * 发送短信.
     * @param tel 电话号码
     * @param sign 签名，默认值，开创啦
     * @return String
     */
    public static String sendSms(String tel, String sign) {
        TaobaoClient client = new DefaultTaobaoClient(AILIDAYU_SMS_URL, AILIDAYU_SMS_APPKEY, AILIDAYU_SMS_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        if (StringUtil.isEmpty(sign)) {
          req.setSmsFreeSignName(AILIDAYU_SMS_SIGN);
        }else{
          req.setSmsFreeSignName(sign);
        }
        req.setRecNum(tel);
        req.setSmsTemplateCode(AILIDAYU_SMS_TemplateCode);

        String content = String.format("%06d", rand.nextInt(1000000));
        req.setSmsParamString("{\"code\":\""+content+"\"}");
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        }
        if (rsp != null) {
          if ((rsp.getResult() != null) && rsp.getResult().getSuccess()) {
            return content;
          }
          logger.error("短信发送失败:原因【"+rsp.getBody() +"】");
        }
        return null;
    }

    /**
     * 单个号码发送短信（发送相同的内容）.
     * @param tel 手机号
     * @param template 模板
     * @param parm 模板参数
     * @return SMSState
     */
    public static SMSState sendSmsTemplate(String tel, String template, ISendParam parm) throws  Exception{
      return sendSmsTemplate(tel, AILIDAYU_SMS_SIGN, template, parm);
    }

    /**
     * 单个号码发送短信（发送相同的内容）.
     * @param tel 手机号
     * @param sign 签名，默认值，开创啦
     * @param template 模板
     * @param parm 模板参数
     * @return SMSState
     */
    public static SMSState sendSmsTemplate(String tel, String sign, String template, ISendParam parm) throws  Exception{
        TaobaoClient client = new DefaultTaobaoClient(AILIDAYU_SMS_URL, AILIDAYU_SMS_APPKEY, AILIDAYU_SMS_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        if (StringUtil.isEmpty(sign)) {
          req.setSmsFreeSignName(AILIDAYU_SMS_SIGN);
        }else{
          req.setSmsFreeSignName(sign);
        }
        req.setRecNum(tel);
        req.setSmsTemplateCode(template);
        req.setSmsParamString(JSON.toJSONString(parm));
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
//            System.out.println(rsp.getBody());
        } catch (Exception e) {
//            logger.error(ExceptionUtil.getStackTrace(e));
            throw new ApiException(e);
        }
        if (rsp != null) {
            if ((rsp.getResult() != null) && rsp.getResult().getSuccess()) {
                return SMSState.SUCCESS ;
            }
            logger.error("短信发送失败:原因【" + rsp.getBody() + "】");
        }
        return SMSState.FAIL101;
    }

    /**
     * 批量发送短信（发送相同的内容）.
     * @param tel 手机号(多个号码以,分隔)
     * @param template 模板
     * @param parm 模板参数
     * @return SMSState
     */
    public static SMSState sendSmsBath(String tel, String template, SendParam parm) throws  Exception{
        return sendSmsTemplate(tel, template, parm);
    }


    /**
     * 批量发送短信（发送相同、不同的内容）.
     * @param params 手机号(多个号码以,分隔)
     * @param template 模板
     * @return SmsRstate
     */
    public static SmsRstate sendSmsBath(List<SmsAlidayuParam<? extends ISendParam>> params, String template) throws  Exception{
      return sendSmsBath(params, template, null);
    }

    /**
     * 批量发送短信（发送相同、不同的内容）.
     * @param params 手机号(多个号码以,分隔)
     * @param sign 签名，默认值，开创啦
     * @param template 模板
     * @return SmsRstate
     */
    public static SmsRstate sendSmsBath(List<SmsAlidayuParam<? extends ISendParam>> params, String template, String sign) throws  Exception{
      SmsRstate smsRstate = new SmsRstate();
      List<SmsAlidayuRstate> sucstates = Lists.newArrayList();
      List<SmsAlidayuRstate> falstates = Lists.newArrayList();
      for (SmsAlidayuParam<? extends ISendParam> smsAlidayu : params) {
        SMSState smsState = null;
        if (StringUtil.isEmpty(sign)) {
          smsState = sendSmsTemplate(smsAlidayu.getTels(), template, smsAlidayu.getParam());
        }else{
          smsState = sendSmsTemplate(smsAlidayu.getTels(), sign, template, smsAlidayu.getParam());
        }
        if ((smsState).equals(SMSState.SUCCESS)) {
          sucstates.add(new SmsAlidayuRstate(smsAlidayu.getTels(), smsState));
        }else if ((smsState).equals(SMSState.FAIL101)) {
          falstates.add(new SmsAlidayuRstate(smsAlidayu.getTels(), smsState));
        }
      }
      smsRstate.setSucstates(sucstates);
      smsRstate.setFailstates(falstates);
      return SmsRstate.validate(smsRstate);
    }

    /**
     * OEY 批量发送短信（发送相同、不同的内容）.
     * @param params 手机号(多个号码以,分隔)
     * @param template 模板
     * @return SmsRstate
     */
    public static SmsRstate sendOeySmsBath(List<SmsAlidayuParam<SendOeyParam>> params, String sign, String template) throws  Exception{
      SmsRstate smsRstate = new SmsRstate();
      List<SmsAlidayuRstate> sucstates = Lists.newArrayList();
      List<SmsAlidayuRstate> falstates = Lists.newArrayList();
      for (SmsAlidayuParam<? extends ISendParam> smsAlidayu : params) {
        SMSState smsState = null;
        if (StringUtil.isEmpty(sign)) {
          smsState = sendSmsTemplate(smsAlidayu.getTels(), template, smsAlidayu.getParam());
        }else{
          smsState = sendSmsTemplate(smsAlidayu.getTels(), sign, template, smsAlidayu.getParam());
        }
        if ((smsState).equals(SMSState.SUCCESS)) {
          sucstates.add(new SmsAlidayuRstate(smsAlidayu.getTels(), smsState));
        }else if ((smsState).equals(SMSState.FAIL101)) {
          falstates.add(new SmsAlidayuRstate(smsAlidayu.getTels(), smsState));
        }
      }
      smsRstate.setSucstates(sucstates);
      smsRstate.setFailstates(falstates);
      return SmsRstate.validate(smsRstate);
    }


    public static void main(String[] args) throws Exception {
        //【开创啦】您在开创啦请求的验证码是：
        System.out.println(SMSUtilAlidayu.sendSms("17362925676"));

        //SendParam parm1 = new SendParam("zy","双创团队");
        //System.out.println(SMSUtilAlidayu.sendSmsTemplate("13554344939", SMSUtilAlidayu.AILIDAYU_SMS_TemplateInvite, parm1)  );

        //SendParam parm2 = new SendParam("zy","双创团队");
        //System.out.println(SMSUtilAlidayu.sendSmsTemplate("13580366759,13733515910", SMSUtilAlidayu.AILIDAYU_SMS_TemplateApply,parm2));

//        List<SmsAlidayuParam<?>> smss = Lists.newArrayList();
////        smss.add(new SmsAlidayuParam("13580366759", new SendParam("章传胜,陈浩","双创团队")));
////        smss.add(new SmsAlidayuParam("13733515910", new SendParam("章传胜","双创团队-章传胜")));
//        smss.add(new SmsAlidayuParam("17362925676", new SendParam("陈浩","双创团队-陈浩")));
//        SmsRstate smsRstate = SMSUtilAlidayu.sendSmsBath(smss, SMSUtilAlidayu.AILIDAYU_SMS_TemplateApply);
//        System.out.println(smsRstate.getStatus());
//        System.out.println(smsRstate.getMsg());
//        System.out.println(smsRstate.getFailstates());
//        System.out.println(smsRstate.getSucstates());

        /*
        try {
            TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23784350", "0c8d207c93b7a0328d1d7b8dd7a78f04");
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setSmsType("normal");
            req.setSmsFreeSignName("开创啦");
            req.setSmsParamString("{\"name\":\"zhangsan\",\"team\":\"lishi\"}");
            req.setRecNum("13733515910");
//            req.setSmsTemplateCode("SMS_100895033");
            req.setSmsTemplateCode("SMS_100920047");
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
