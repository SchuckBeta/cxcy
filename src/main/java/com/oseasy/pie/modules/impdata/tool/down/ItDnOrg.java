/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.down;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.pie.modules.impdata.tool.IitDownTpl;

/**
 * 下载机构模板.
 * @author chenhao
 *
 */
public class ItDnOrg implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnOrg.class);

    @Override
    public void setHead(XSSFSheet sheet) {
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：导入学院只需要填写学院，导入专业则需要填写该专业所属学院"));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {}
}
