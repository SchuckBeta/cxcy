package com.oseasy.com.pcore.common.utils.poi;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

public class ExcelUtils {
    // 创建xlsx
    public static void writeDataExcelx(String filePath, String[] dataExcel, String title, String[] unitTitle,
            String[] itemTitle) {
        OutputStream ostream = null;
        try {
            ostream = new FileOutputStream(filePath);
            XSSFWorkbook excel = new XSSFWorkbook();
            XSSFSheet sheet0 = excel.createSheet(title);
            // excel中第一行 poi中行、列开始都是以0开始计数
            // 合并第一行 显示总标题 占据第一行的第一列到第15列
            sheet0.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            XSSFRow row = sheet0.createRow(0);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(title);
            // 设置样式
            XSSFCellStyle cellStyle = excel.createCellStyle();
            // 居中对齐
            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            // 字体
            XSSFFont font = excel.createFont();
            font.setFontHeightInPoints((short) 16);
            font.setColor(new XSSFColor(Color.RED));
            cellStyle.setFont(font);
            // 设置第一行的样式
            cell.setCellStyle(cellStyle);

            // 显示第二行 表头 各项标题
            row = sheet0.createRow(1);
            for (int i = 0; i < itemTitle.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(itemTitle[i]);
                cell.setCellStyle(cellStyle);
            }

            // 从第三行开始显示 各大公司的数据
            int start = 2;
            for (String unit : unitTitle) {
                row = sheet0.createRow(start);
                // 第一列显示单位名称
                cell = row.createCell(0);
                cell.setCellValue(unit);
                // 添加合计行
                if ("合计".equals(unit)) {
                    for (int i = 0; i < dataExcel.length; i++) {
                        cell = row.createCell(i + 1);
                        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        char charaColumn = (char) ('b' + i);
                        String formula = "sum(" + charaColumn + 2 + ":" + charaColumn + start + ")";
                        cell.setCellFormula(formula);
                    }
                } else { // 添加数据行
                    for (int i = 0; i < dataExcel.length; i++) {
                        cell = row.createCell(i + 1);
                        cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.valueOf(dataExcel[i]));
                    }
                }
                start++;
            }
            excel.write(ostream);
            System.out.println("创建excel成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ostream != null){
                try {
                    ostream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 读取xlsx的数据
    public static String readDataExcelx(String filePath) {
        String content = "";
        try {
            OPCPackage pkg = OPCPackage.open(filePath);
            XSSFWorkbook excel = new XSSFWorkbook(pkg);
            // 获取第一个sheet
            XSSFSheet sheet0 = excel.getSheetAt(0);
            for (Iterator rowIterator = sheet0.iterator(); rowIterator.hasNext();) {
                XSSFRow row = (XSSFRow) rowIterator.next();
                for (Iterator iterator = row.cellIterator(); iterator.hasNext();) {
                    XSSFCell cell = (XSSFCell) iterator.next();
                    // 根据单元的的类型 读取相应的结果
                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
                        content += cell.getStringCellValue() + "\t";
                    else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
                        content += cell.getNumericCellValue() + "\t";
                    else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA)
                        content += cell.getCellFormula() + "\t";
                }
                content += "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 获取Excel表格列行数.
     * @param sheet 表格
     * @param tag 列明
     * @return int
     */
    public static int getTotalRow(XSSFSheet sheet, String tag) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            String s = sheet.getRow(i).getCell(0).getStringCellValue();
            if (StringUtil.isNotEmpty(s) && s.contains(tag)) {
                return i;
            }
        }
        return sheet.getLastRowNum();
    }

    /**
     * 获取Excel单元格值.
     * @param cell 单元格
     * @param sheet 表
     * @return
     */
    public static String getStringByCell(XSSFCell cell, XSSFSheet sheet) {
        String ret = null;
        if (cell == null) {
            return ret;
        } else {
            switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:// 数字
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 用于转化为日期格式
                    DateFormat formater = new SimpleDateFormat(DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
                    ret = formater.format(cell.getDateCellValue());
                } else {
                    // 用于格式化数字，只保留数字的整数部分
                    // DecimalFormat df = new DecimalFormat("########");
                    // ret = df.format(cell.getNumericCellValue());
                    ret = cell.getRawValue();
                }
                break;
            case XSSFCell.CELL_TYPE_STRING:// 字符串
                ret = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_FORMULA:// 公式
                FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
                ret = evaluator.evaluate(cell).getStringValue();
                if (StringUtil.isEmpty(ret)) {
                    ret = String.valueOf(evaluator.evaluate(cell).getNumberValue());
                }
                break;
            default:
                ret = cell.getStringCellValue();
                break;
            }
            return ret;
        }
    }

    /**
     * 判断单元格是否为合并.
     * @param sheet 判断单元格是否合并
     * @param row 行
     * @param column 列
     * @return MergedResult
     */
    public static MergedResult isMergedRegion(XSSFSheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return new MergedResult(true, firstRow, lastRow, firstColumn, lastColumn);
                }
            }
        }
        return new MergedResult(false, 0, 0, 0, 0);
    }

    /**
     * 创建行.
     * @param sheet0
     * @param start
     * @param count
     */
    public static void creatRow(XSSFSheet sheet0, int start, int count) {
        for (int i = 0; i < count; i++) {
            sheet0.createRow(start + i);
        }
    }

    /**
     * 多行单元格合并.
     * @param sheet0
     * @param start
     * @param count
     */
    public static void mergedRowCell(XSSFSheet sheet0, int start, int count) {
        if (count < 2) {
            return;
        }
        // 合并单元格
        for (int i = 0; i < 11; i++) {
            CellRangeAddress cra = new CellRangeAddress(start, start + count - 1, i, i); // 起始行,
                                                                                         // 终止行,
                                                                                         // 起始列,
                                                                                         // 终止列
            sheet0.addMergedRegion(cra);
        }
        CellRangeAddress cra = new CellRangeAddress(start, start + count - 1, 21, 21); // 起始行,
                                                                                       // 终止行,
                                                                                       // 起始列,
                                                                                       // 终止列
        sheet0.addMergedRegion(cra);
        CellRangeAddress cra2 = new CellRangeAddress(start, start + count - 1, 22, 22); // 起始行,
                                                                                        // 终止行,
                                                                                        // 起始列,
                                                                                        // 终止列
        sheet0.addMergedRegion(cra2);
    }

    /**
     *
     * @Title: SetDataValidation @Description: 下拉列表元素很多的情况
     * (255以上的下拉) @param strFormula @param firstRow
     * 起始行 @param endRow 终止行 @param firstCol 起始列 @param
     * endCol 终止列 @return @return HSSFDataValidation @throws
     */
    public static HSSFDataValidation setDataValidation(String strFormula, int firstRow, int endRow, int firstCol,
            int endCol) {
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);

        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }

    /**
     * @Title: setDataValidation @Description:
     * 下拉列表元素不多的情况(255以内的下拉) @param sheet @param
     * textList @param firstRow @param endRow @param
     * firstCol @param endCol @return @return
     * DataValidation @throws
     */
    public static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow,
            int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        // DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);

        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol,
                (short) endCol);

        // 数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        // DataValidation data_validation = new DataValidation(regions,
        // constraint);

        return data_validation;
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
    public static void addDropDownList(HSSFWorkbook workbook, HSSFSheet tarSheet, String[] menuItems, int firstRow,
            int lastRow, int column) throws Exception {
        if (null == workbook) {
            throw new Exception("workbook为null");
        }
        if (null == tarSheet) {
            throw new Exception("待添加菜单的sheet为null");
        }

        // 必须以字母开头，最长为31位
        String hiddenSheetName = "a" + IdGen.uuid().toString().replace("-", "").substring(1, 31);
        // excel中的"名称"，用于标记隐藏sheet中的用作菜单下拉项的所有单元格
        String formulaId = "form" + IdGen.uuid().toString().replace("-", "");
        HSSFSheet hiddenSheet = workbook.createSheet(hiddenSheetName);
        // 用于存储 下拉菜单数据
        // 存储下拉菜单项的sheet页不显示
        workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
        HSSFRow row = null;
        HSSFCell cell = null;
        // 隐藏sheet中添加菜单数据
        for (int i = 0; i < menuItems.length; i++) {
            row = hiddenSheet.createRow(i);
            // 隐藏表的数据列必须和添加下拉菜单的列序号相同，否则不能显示下拉菜单
            cell = row.createCell(column);
            cell.setCellValue(menuItems[i]);
        }
        HSSFName namedCell = workbook.createName();
        // 创建"名称"标签，用于链接
        namedCell.setNameName(formulaId);
        namedCell.setRefersToFormula(hiddenSheetName + "!$B$1:$B$" + menuItems.length);
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(tarSheet);
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(formulaId);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, column, column);
        HSSFDataValidation validation = (HSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
        // 添加菜单(将单元格与"名称"建立关联)
        tarSheet.addValidationData(validation);
    }
}
