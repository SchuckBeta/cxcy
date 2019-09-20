/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.down;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.common.utils.poi.ExcelDivo.Builder;
import com.oseasy.pie.common.utils.poi.ExcelDvo;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 下载学生模板.
 * @author chenhao
 *
 */
public class ItDnStudent implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnStudent.class);

    @Override
    public void setHead(XSSFSheet sheet) {
        String headDesc = "填写数据说明：红色名称为必填信息。日期格式举例2017-05-19；擅长技术领域为多选信息，若有多个则用英文输入法逗号分隔;\r\n";
        List<Dict> list=DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        List<String> temlist = new ArrayList<String>();
        for(Dict d:list) {
            temlist.add(d.getLabel());
        }
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString(headDesc+"擅长技术领域可选值有:"+StringUtil.join(temlist.toArray(), ",")));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ID_TYPE))
                .startCol(7)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_TECHNOLOGY_FIELD))
                .startCol(10)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_DEGREE))
                .startCol(11)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                .startCol(12)
                .build());
        dvo.getDivos().add(new Builder()
//                .items(DictUtils.convertArrays(DICT_ENDUCATION_LEVEL))
                .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_DEGREE))
                .startCol(26)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_CURRENT_SATE))
                .startCol(27)
                .build());
        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
