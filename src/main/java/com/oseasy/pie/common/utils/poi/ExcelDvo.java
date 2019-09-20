/**
 * .
 */

package com.oseasy.pie.common.utils.poi;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Excel.
 * @author chenhao
 */
public class ExcelDvo {
    private String sheetName;
    private Integer sheetIdx;
    private XSSFWorkbook workbook;
    private List<ExcelDivo> divos;
    private Map<Integer, Boolean> hideSheets;

    public ExcelDvo() {
        super();
        this.divos = Lists.newArrayList();
        this.hideSheets = Maps.newHashMap();
    }

    public ExcelDvo(String sheetName, List<ExcelDivo> divos, Map<Integer, Boolean> hideSheets) {
        super();
        this.sheetName = sheetName;
        this.divos = divos;
        this.hideSheets = hideSheets;
    }

    /**
     * 显示数据隐藏sheet.
     * @param isHide
     * @return Boolean
     */
    public Boolean hide(Boolean isHide) {
        if(this.workbook == null){
            return false;
        }
        if(isHide == null){
            isHide = true;
        }
        workbook.setSheetHidden(this.getSheetIdx(), isHide);
        return true;
    }

    public Integer getSheetIdx() {
        return sheetIdx;
    }

    public void setSheetIdx(Integer sheetIdx) {
        this.sheetIdx = sheetIdx;
    }

    public String getSheetName() {
        return sheetName;
    }
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
    public List<ExcelDivo> getDivos() {
        return divos;
    }
    public void setDivos(List<ExcelDivo> divos) {
        this.divos = divos;
    }
    public Map<Integer, Boolean> getHideSheets() {
        return hideSheets;
    }
    public void setHideSheets(Map<Integer, Boolean> hideSheets) {
        this.hideSheets = hideSheets;
    }
}
