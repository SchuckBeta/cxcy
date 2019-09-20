/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.down;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.common.utils.poi.ExcelDivo.Builder;
import com.oseasy.pie.common.utils.poi.ExcelDvo;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.pro.common.config.ProSval;

/**
 * 下载互联网+大赛大赛模板.
 * @author chenhao
 *
 */
public class ItDnHlwGcontest implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnHlwGcontest.class);

    public ItDnHlwGcontest() {
        super();
    }

    @Override
    public void setHead(XSSFSheet sheet) {
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。\r\n申报人/学号示例：韩浩/2015211240\r\n校内导师/工号和企业导师/工号多个输入以中文顿号分隔;示例：韩浩/2015211240、韩浩/2015211240"));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(ProSval.DICT_COMPETITION_NET_TYPE))
                .startCol(1)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(ProSval.DICT_GCONTEST_LEVEL))
                .startCol(2)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(ProSval.DICT_COMPETITION_COLLEGE_PRISE))
                .startCol(10)
                .build());
        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
