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
 * .
 * @author chenhao
 *
 */
public class ItDnTeacher implements IitDownTpl{
    public static Logger logger = Logger.getLogger(ItDnTeacher.class);

    @Override
    public void setHead(XSSFSheet sheet) {
        StringBuffer headDesc = new StringBuffer("填写数据说明：红色名称为必填信息。日期格式举例2017-05-19；擅长技术领域、服务意向为多选信息，若有多个则用英文输入法逗号分隔;\r\n");
        List<Dict> list=DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        List<String> temlist = new ArrayList<String>();
        for(Dict d:list) {
            temlist.add(d.getLabel());
        }
        headDesc.append("擅长技术领域可选值有:"+StringUtil.join(temlist.toArray(), ","));
        list=DictUtils.getDictList("master_help");
        temlist = new ArrayList<String>();
        for(Dict d:list) {
            temlist.add(d.getLabel());
        }
        headDesc.append("\r\n服务意向可选值有:"+StringUtil.join(temlist.toArray(), ","));
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString(headDesc.toString()));
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_MASTER_TYPE))
                .startCol(1)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ID_TYPE))
                .startCol(9)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_TECHNOLOGY_FIELD))
                .startCol(11)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_TYPE))
                .startCol(12)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_DEGREE))
                .startCol(13)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                .startCol(14)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_PROFESSIONAL_TYPE))
                .startCol(20)
                .build());
        dvo.getDivos().add(new Builder()
                .items(DictUtils.convertArrays(SysSval.DICT_MASTER_HELP))
                .startCol(23)
                .build());
        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}