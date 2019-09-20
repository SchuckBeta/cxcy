/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.down;

import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.common.utils.poi.ExcelDvo;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.common.utils.poi.ExcelDivo.Builder;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.util.common.utils.StringUtil;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载用户模板.
 * @author chenhao
 *
 */
public class ItDnConStu implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnConStu.class);

    @Override
    public void setHead(XSSFSheet sheet) {
        String headDesc = "填写数据说明：红色名称为必填信息。日期格式举例2017-05-19；擅长技术领域和角色为多选信息，若有多个则用英文输入法逗号分隔;\r\n";
        List<Dict> list=DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        List<String> temlist = new ArrayList<String>();

//        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString(headDesc
//                +"擅长技术领域可选值有:"+StringUtil.join(temlist.toArray(), ",")+"\r\n"
//                +"角色可选值有:"+StringUtil.join(getRoles(), ",")+"\r\n"));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());

        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
