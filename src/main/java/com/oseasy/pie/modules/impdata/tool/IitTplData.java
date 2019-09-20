/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.tool.data.TplCkCol;
import com.oseasy.pie.modules.impdata.tool.data.TplCkRows;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;

/**
 * 导入导出模板数据处理接口.
 * @author chenhao
 */
public interface IitTplData {
    /**
     * 头行.
     */
    public int headRow();
    /**
     * 数据起始行.
     */
    public int dataStartRow();

    /**
     * 检查模板.
     */
    public void checkTpl(ItOper impVo, XSSFSheet datasheet, HttpServletRequest request);

    /**
     * 导入模板数据处理（校验模板，启动多线程）.
     */
    public void impData(ActYw ay, MultipartFile imgFile, HttpServletRequest request, ItOper impVo);
    /**
     * 导入模板数据处理(文件处理).
     */
    public void impDataFile(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) throws Exception;

    /**
     * 导出错误数据.
     * @param ay 流程
     * @param request 请求
     * @param impVo 参数
     */
    public void expDataError(String id, ActYw ay, HttpServletRequest request, ItOper impVo);


    public static void genCheckExcept(XSSFSheet sheet, List<TplCkRows> tckRows) throws POIXMLException{
        if ((sheet == null)) {
            throw new POIXMLException("模板错误,请检查文档格式。");
        }

        String terror = "模板错误,请下载最新模板。";
        String tecolrror = "模板错误,请下载最新模板。";
//        String terror = "模板错误,请下载最新模板,模板不一致。";
//        String tecolrror = "模板错误,请下载最新模板,第({0})行({1})列头不等于({2})-({3})。";
        for (TplCkRows tckRow : tckRows) {
            List<TplCkCol> curCols = tckRow.getCols();
            String sheeRC = null;
            for (TplCkCol tckCol : curCols) {
                if ((sheet.getRow(tckRow.getCur()) == null) || (sheet.getRow(tckRow.getCur()).getCell(tckCol.getIdx()) == null)) {
                    throw new POIXMLException(terror);
                }

                sheeRC = ExcelUtils.getStringByCell(sheet.getRow(tckRow.getCur()).getCell(tckCol.getIdx()), sheet);
                if (!(sheeRC).equals(tckCol.getName())) {
                    throw new POIXMLException(MessageFormat.format(tecolrror, tckRow.getCur(), tckCol.getIdx(), tckCol.getName(), sheeRC));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            List<TplCkRows> tckRows = Lists.newArrayList();
            TplCkRows crow = new TplCkRows(0);
            crow.getCols().add(new TplCkCol(0, "项目名称"));
            crow.getCols().add(new TplCkCol(1, "项目XX"));
            tckRows.add(crow);
            TplCkRows crow1 = new TplCkRows(1);
            crow1.getCols().add(new TplCkCol(1, "项称"));
            crow1.getCols().add(new TplCkCol(2, "项目BB"));
            tckRows.add(crow1);
            genCheckExcept(null, tckRows);
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
    }
}
