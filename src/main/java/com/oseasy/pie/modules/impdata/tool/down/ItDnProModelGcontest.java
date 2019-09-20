/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.down;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.common.utils.poi.ExcelDivo.Builder;
import com.oseasy.pie.common.utils.poi.ExcelDvo;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.sys.common.config.SysSval;

/**
 * 下载自定义大赛模板.
 * @author chenhao
 *
 */
public class ItDnProModelGcontest implements IitDownTpl{

    public static Logger logger = Logger.getLogger(ItDnProModelGcontest.class);

    private Boolean isDefault;//没有业务节点，使用默认模板和头
    private ActYw actYw;

    public ItDnProModelGcontest() {
        super();
        this.isDefault = true;
    }

    public ItDnProModelGcontest(ActYw actYw) {
        super();
        this.actYw = actYw;
    }

    @Override
    public void setHead(XSSFSheet sheet) {
        if(getActYw() != null){
            isDefault = false;
        }

        //如果是默认，执行默认
        if(isDefault){
            sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。项目年份举例：2016。\r\n"));
            return;
        }

        FormTheme ftheme =  getActYw().getFtheme();
        FlowProjectType fpType =  getActYw().getFptype();
        if((ftheme == null) || (fpType == null)){
            logger.warn("模板类型未定义，请检查参数！");
            return;
        }

        if((FlowProjectType.PMT_DASAI).equals(fpType)){
            if((FormTheme.F_MD).equals(ftheme) || (FormTheme.F_MD).equals(ftheme)){
                sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。项目年份举例：2016。\r\n"));
                //tplName = "promodel_gcontest_data_template.xlsx";
            }else{
                sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。项目年份举例：2016。\r\n"));
                //tplName = "promodel_gcontest_data_template.xlsx";
            }
        }else{
            logger.info("当前下载的模板类型未定义！");
        }
    }

    @Override
    public void setBody(XSSFWorkbook workbook, XSSFSheet sheet) {
        if(getActYw() != null){
            isDefault = false;
        }

        ExcelDvo dvo = new ExcelDvo();
        dvo.setSheetName(IdGen.uuid());
        //如果是默认，执行默认
        if(isDefault){
            dvo.getDivos().add(new Builder()
                    .items(DictUtils.convertArrays(ProSval.DICT_COMPETITION_NET_TYPE))
                    .startCol(3)
                    .build());
            dvo.getDivos().add(new Builder()
                    .items(DictUtils.convertArrays(ProSval.DICT_GCONTEST_LEVEL))
                    .startCol(4)
                    .build());
            dvo.getDivos().add(new Builder()
                    .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                    .startCol(12)
                    .build());
            dvo.getDivos().add(new Builder()
                    .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                    .startCol(21)
                    .build());
        }else{
            FormTheme ftheme =  getActYw().getFtheme();
            FlowProjectType fpType =  getActYw().getFptype();
            if((ftheme == null) || (fpType == null)){
                logger.warn("模板类型未定义，请检查参数！");
                return;
            }

            if((FlowProjectType.PMT_DASAI).equals(fpType)){
                if((FormTheme.F_MD).equals(ftheme) || (FormTheme.F_MD).equals(ftheme)){
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(ProSval.DICT_COMPETITION_NET_TYPE))
                            .startCol(3)
                            .build());
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(ProSval.DICT_GCONTEST_LEVEL))
                            .startCol(4)
                            .build());
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                            .startCol(12)
                            .build());
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                            .startCol(21)
                            .build());
                }else{
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(ProSval.DICT_COMPETITION_NET_TYPE))
                            .startCol(3)
                            .build());
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(ProSval.DICT_GCONTEST_LEVEL))
                            .startCol(4)
                            .build());
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                            .startCol(12)
                            .build());
                    dvo.getDivos().add(new Builder()
                            .items(DictUtils.convertArrays(SysSval.DICT_ENDUCATION_LEVEL))
                            .startCol(21)
                            .build());
                }
            }else{
                logger.info("当前下载的模板类型未定义！");
            }
        }

        try {
            PieExcelUtils.addDropDownList(workbook, sheet, dvo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActYw getActYw() {
        return actYw;
    }

    public void setActYw(ActYw actYw) {
        this.actYw = actYw;
    }
}
