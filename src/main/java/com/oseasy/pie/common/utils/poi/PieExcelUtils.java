package com.oseasy.pie.common.utils.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.pie.modules.iep.tool.IeAbsTpl;
import com.oseasy.util.common.utils.StringUtil;

public class PieExcelUtils {
    /**
     * 设置下载Excel的文件头.
     * @param response
     * @param absTpl
     * @return FileInputStream
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static FileInputStream setExcelHeader(HttpServletResponse response, IeAbsTpl absTpl)
            throws UnsupportedEncodingException, FileNotFoundException {
        FileInputStream fs;
        String headStr = "attachment; filename=\"" + new String(absTpl.getFname().getBytes(), "ISO-8859-1") + "\"";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", headStr);
        fs = new FileInputStream(new File(absTpl.getRootPath() + File.separator + absTpl.getTplName()));
        return fs;
    }

    /**
     * 单元格添加下拉菜单(不限制菜单可选项个数)
     * [注意：此方法会添加隐藏的sheet，可调用getDataSheetInDropMenuBook方法获取用户输入数据的未隐藏的sheet]
     * [待添加下拉菜单的单元格 -> 以下简称：目标单元格]
     *
     * @param
     *            workbook
     * @param
     *            tarSheet 目标单元格所在的sheet
     * @param
     *            menuItems 下拉菜单可选项数组
     * @param
     *            firstRow 第一个目标单元格所在的行号(从0开始)
     * @param
     *            lastRow 最后一个目标单元格所在的行(从0开始)
     * @param
     *            column 待添加下拉菜单的单元格所在的列(从0开始)
     */
    public static XSSFWorkbook addDropDownList(XSSFWorkbook workbook, XSSFSheet tarSheet, ExcelDvo edvo)
            throws Exception {
        if (null == workbook) {
            throw new Exception("workbook为null");
        }
        if (null == tarSheet) {
            throw new Exception("待添加菜单的sheet为null");
        }
        if (StringUtil.checkEmpty(edvo.getDivos())) {
            throw new Exception("待添加菜单项为null");
        }

        for (int i = 0; i < edvo.getDivos().size(); i++) {
            ExcelDivo divo = edvo.getDivos().get(i);
            String[] items = divo.getItems();
            if ((items == null) || (items.length <= 0)) {
                continue;
            }

            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(tarSheet);
            DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(divo.getItems());
            CellRangeAddressList addressList = new CellRangeAddressList(divo.getFirstRow(), divo.getLastRow(),
                    divo.getEndCol(), divo.getEndCol());
            DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
            // 设置出错提示信息
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            // 添加菜单(将单元格与"名称"建立关联)
            tarSheet.addValidationData(validation);
        }
        return workbook;
    }

    /**
     * TODO 需要调整. dvo.getDivos().add(new Builder()
     * .key(ItDnStudent.DICT_TECHNOLOGY_FIELD)
     * .items(DictUtils.convertArrays(ItDnStudent.DICT_TECHNOLOGY_FIELD))
     * .firstRow(IitDownTpl.START_ROW_NUM) .lastRow(IitDownTpl.MAX_ROW_NUM)
     * .startCol(1) .vtype(999) .vmin("0") .vmax("3000") .voptype(1) .build());
     * 单元格添加下拉菜单(不限制菜单可选项个数)
     * [注意：此方法会添加隐藏的sheet，可调用getDataSheetInDropMenuBook方法获取用户输入数据的未隐藏的sheet]
     * [待添加下拉菜单的单元格 -> 以下简称：目标单元格]
     *
     * @param
     *            workbook
     * @param
     *            tarSheet 目标单元格所在的sheet
     * @param
     *            menuItems 下拉菜单可选项数组
     * @param
     *            firstRow 第一个目标单元格所在的行号(从0开始)
     * @param
     *            lastRow 最后一个目标单元格所在的行(从0开始)
     * @param
     *            column 待添加下拉菜单的单元格所在的列(从0开始)
     */
    public static XSSFWorkbook addDropDownListBySheet(XSSFWorkbook workbook, XSSFSheet tarSheet, ExcelDvo edvo)
            throws Exception {
        if (null == workbook) {
            throw new Exception("workbook为null");
        }
        if (null == tarSheet) {
            throw new Exception("待添加菜单的sheet为null");
        }
        if (StringUtil.checkEmpty(edvo.getDivos())) {
            throw new Exception("待添加菜单项为null");
        }

        // XSSFSheet hiddenSheet = workbook.createSheet(edvo.getSheetName());
        // edvo.setSheetIdx(workbook.getSheetIndex(hiddenSheet));
        // //用于存储 下拉菜单数据
        // //存储下拉菜单项的sheet页不显示
        // Map<Integer, Boolean> edvMap = edvo.getHideSheets();
        // Iterator<Integer> it = edvMap.keySet().iterator();
        // while(it.hasNext()){
        // Integer idx = it.next();
        // workbook.setSheetHidden(idx, edvMap.get(idx));
        // }

        XSSFRow row = null;
        XSSFCell cell = null;

        for (int i = 0; i < edvo.getDivos().size(); i++) {
            ExcelDivo divo = edvo.getDivos().get(i);
            String[] items = divo.getItems();
            if ((items == null) || (items.length <= 0)) {
                continue;
            }

            // //隐藏sheet中添加菜单数据
            // for (int j = 0; j < divo.getItems().length; j++) {
            // row = hiddenSheet.createRow(j);
            // //隐藏表的数据列必须和添加下拉菜单的列序号相同，否则不能显示下拉菜单
            // cell = row.createCell(divo.getEndCol());
            // cell.setCellValue(items[j]);
            // }

            // XSSFName namedCell = workbook.createName();
            // //创建"名称"标签，用于链接
            // namedCell.setNameName(divo.getKey());
            // namedCell.setRefersToFormula("'" + edvo.getSheetName() +
            // "'!$A$1:$A$" + items.length);
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(tarSheet);
            DataValidationConstraint dvConstraint = null;
            // if(divo.getVtype() ==
            // DataValidationConstraint.ValidationType.TEXT_LENGTH) {
            // dvConstraint =
            // dvHelper.createFormulaListConstraint(divo.getKey());
            //// dvConstraint =
            // dvHelper.createTextLengthConstraint(divo.getVoptype(),
            // divo.getVmin(), divo.getVmax());
            // } else if(divo.getVtype() ==
            // DataValidationConstraint.ValidationType.DECIMAL) {
            // dvConstraint =
            // dvHelper.createDecimalConstraint(divo.getVoptype(),
            // divo.getVmin(), divo.getVmax());
            // } else if(divo.getVtype() ==
            // DataValidationConstraint.ValidationType.INTEGER) {
            // dvConstraint =
            // dvHelper.createIntegerConstraint(divo.getVoptype(),
            // divo.getVmin(), divo.getVmax());
            // } else if(divo.getVtype() ==
            // DataValidationConstraint.ValidationType.DATE) {
            // dvConstraint = dvHelper.createDateConstraint(divo.getVoptype(),
            // divo.getVmin(), divo.getVmax(), divo.getVformat());
            // } else if(divo.getVtype() ==
            // DataValidationConstraint.ValidationType.TIME) {
            // dvConstraint = dvHelper.createTimeConstraint(divo.getVoptype(),
            // divo.getVmin(), divo.getVmax());
            // } else {
            // dvConstraint =
            // dvHelper.createFormulaListConstraint(divo.getKey());
            // }

            dvConstraint = dvHelper.createExplicitListConstraint(divo.getItems());
            CellRangeAddressList addressList = new CellRangeAddressList(divo.getFirstRow(), divo.getLastRow(),
                    divo.getEndCol(), divo.getEndCol());
            DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
            // 设置出错提示信息
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            // setDataValidationErrorMessage(validation, errorTitle, errorMsg);
            // 添加菜单(将单元格与"名称"建立关联)
            tarSheet.addValidationData(validation);
        }
        return workbook;
    }

}
