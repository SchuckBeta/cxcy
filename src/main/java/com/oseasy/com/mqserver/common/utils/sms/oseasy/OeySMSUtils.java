package com.oseasy.com.mqserver.common.utils.sms.oseasy;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;
import com.oseasy.com.mqserver.common.utils.sms.SMSState;
import com.oseasy.com.mqserver.common.utils.sms.SmsAlidayuParam;
import com.oseasy.com.mqserver.common.utils.sms.SmsAlidayuRstate;
import com.oseasy.com.mqserver.common.utils.sms.SmsRstate;
import com.oseasy.com.mqserver.common.utils.sms.impl.SendOeyParam;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 高校展短信批量发送工具类.
 * 数据文件在OeySMSConfig.AILIDAYU_SMS_OEY_EXCELFILE定义路径
 *
 * 注意事项：
 *  1、填写Excel模板时，需要设置英文字体写手机号（非英文字体可能无法正确识别）
 *  2、填写Excel列如果为空请以中线（-）代替，注意：如果该列作为模板变量，需要必填，否则短信显示不正确
 *  3、执行工具类时，请配置属性文件：
 *      #安全码：
 *      ailidayu.sms.oey.secret=0c8d207c93b7a0328d1d7b8dd7a78f04
 *      #账号标识：
 *      ailidayu.sms.oey.appkey=23784350
 *      #接口地址：
 *      ailidayu.sms.oey.url=http://gw.api.taobao.com/router/rest
 *      #使用模板：
 *      ailidayu.sms.oey.template.invite=SMS_133155822
 *      #短信签名
 *      ailidayu.sms.oey.sign=\u5662\u6613\u4E91
 *      #年份
 *      ailidayu.sms.oey.pyear=2018
 *      #时间
 *      ailidayu.sms.oey.ptime=\u6625\u5B63
 *      #地点
 *      ailidayu.sms.oey.paddr=\u6B66\u6C49
 *      #展位号
 *      ailidayu.sms.oey.paddrNo=B5J58
 *      #展位名
 *      ailidayu.sms.oey.pposName=\u5662\u6613\u4E91
 *      #数据文件
 *      ailidayu.sms.oey.excelfile=
 * @author chenhao
 */
public class OeySMSUtils {
  private static final String REGEX_MOBILE = "1(3|5|7|8)[0-9]{9}";
  //private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
//  private static final String REGEX_MOBILE = "1\\d{10}";

  public static void main(String[] args) throws Exception {

    //System.out.println(("‭").matches(REGEX_MOBILE));

    System.out.println("============================================================================================");
    SmsRstate smsRstate = new SmsRstate();
    List<SmsAlidayuParam<SendOeyParam>> params = Lists.newArrayList();
    List<SmsAlidayuParam<SendOeyParam>> allParams = readDataExcelx("E:\\workspace-sts3\\ROOT\\src\\main\\webapp\\WEB-INF\\views\\template\\sms\\datas\\xxx.xlsx");
//    List<SmsAlidayuParam<SendOeyParam>> allParams = readDataExcelx(OeySMSConfig.AILIDAYU_SMS_OEY_EXCELFILE);
    System.out.println(allParams.size());
    for (SmsAlidayuParam<SendOeyParam> smsAParam : allParams) {
      if (StringUtil.isEmpty(smsAParam.getTels()) || (smsAParam.getParam() == null)) {
        smsRstate.getFailstates().add(new SmsAlidayuRstate(null, SMSState.FAIL201));
      }else{
        Boolean isMobile = true;
        for (String tel : (smsAParam.getTels()).split(StringUtil.DOTH)) {
          if (!(tel).matches(REGEX_MOBILE)) {
            isMobile = false;
          }
        }

        if (isMobile) {
          params.add(smsAParam);
        }else{
          smsRstate.getFailstates().add(new SmsAlidayuRstate(smsAParam.getTels(), SMSState.FAIL202));
        }
      }

      SendOeyParam oeyParam = smsAParam.getParam();
      System.out.println(oeyParam.getInviteNo() + ">>>" + smsAParam.getTels());
    }

//    SmsRstate srstate = SMSUtilAlidayu.sendOeySmsBath(params, OeySMSConfig.AILIDAYU_SMS_OEY_SIGN, OeySMSConfig.AILIDAYU_SMS_TemplateInvite);
//    if (smsRstate != null) {
//      smsRstate.getFailstates().addAll(srstate.getFailstates());
//      smsRstate.getSucstates().addAll(srstate.getSucstates());
//    }

    System.out.println(params.size());
    System.out.println("============================================================================================");
    System.out.println(SmsRstate.validate(smsRstate).setMsgDetail());
    System.out.println("============================================================================================");
  }

  /**
   * OEY 短信发送Excel数据读取工具，读取xlsx的数据.
   * @param filePath
   * @return List
   */
  public static List<SmsAlidayuParam<SendOeyParam>> readDataExcelx(String filePath) {
    List<SmsAlidayuParam<SendOeyParam>> smss = Lists.newArrayList();
    try {
      OPCPackage pkg = OPCPackage.open(filePath);
      XSSFWorkbook excel = new XSSFWorkbook(pkg);
      // 获取第一个sheet
      XSSFSheet sheet0 = excel.getSheetAt(0);
      int idrow = 0, idcol;
      DecimalFormat format = new DecimalFormat("#");
      for (Iterator<?> rowIterator = sheet0.iterator(); rowIterator.hasNext();) {
        XSSFRow row = (XSSFRow) rowIterator.next();
        if (idrow >= OeySMSConfig.EXCEL_ROW) {
          idcol = 0;
          String mobile = null;
          SendOeyParam param = null;
          for (Iterator<Cell> iterator = row.cellIterator(); iterator.hasNext();) {
            XSSFCell cell = (XSSFCell) iterator.next();
            if (cell == null) {
              continue;
            }

            if ((idcol == OeySMSConfig.EXCEL_COL_INO)) {
              if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                param = new SendOeyParam(StringUtil.trimToEmpty(cell.getRichStringCellValue().getString()));
              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                param = new SendOeyParam(format.format(cell.getNumericCellValue()));
              }
//              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
//              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
//              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {}
            }

            if ((idcol == OeySMSConfig.EXCEL_COL_MOBILE)) {
              if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                mobile = StringUtil.trimToEmpty(cell.getRichStringCellValue().getString());
//              mobile = cell.getStringCellValue();
              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                mobile = format.format(cell.getNumericCellValue());
              }
//              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
//              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
//              }else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {}
            }
            idcol++;
          }

          if ((param != null)) {
            smss.add(new SmsAlidayuParam<SendOeyParam>(mobile, param));
          }
        }
        idrow++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return smss;
  }
}
