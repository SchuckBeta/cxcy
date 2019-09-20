/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.down;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.common.utils.poi.ExcelDvo;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.common.utils.poi.ExcelDivo.Builder;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;

/**
 * 下载华师项目模板.
 * @author chenhao
 *
 */
public class ItDnProjectHs implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnProjectHs.class);

    @Override
    public void setHead(XSSFSheet sheet) {
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。年级示例：2016。\r\n团队成员及学号示例：韩浩2015211240,马翔宇2014211510,于欣彤2015211927,游漫 2016211182;以英文输入法逗号分隔\r\n指导教师姓名、工号、职称多个输入以中文顿号分隔;示例：李名峰、王涛"));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(FlowPcategoryType.PCT_XM.getKey()))
                .startCol(3)
                .build());
        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
