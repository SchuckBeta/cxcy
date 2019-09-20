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
 * 下载项目结项模板.
 * @author chenhao
 *
 */
public class ItDnProjectClose implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnProjectClose.class);

    @Override
    public void setHead(XSSFSheet sheet) {
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。立项年份举例：2016。\r\n项目其他成员信息：姓名/学号,姓名/学号；举例：刘家胜/2014210163,郭力/2014210132;以英文输入法逗号分隔"));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(FlowPcategoryType.PCT_XM.getKey()))
                .startCol(6)
                .build());
        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
