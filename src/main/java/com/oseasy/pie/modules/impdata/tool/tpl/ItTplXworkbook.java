/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.tpl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 */
public class ItTplXworkbook extends IitAbsTpl implements IitTpl<XSSFWorkbook>{
    private XSSFWorkbook workbook;

    @Override
    public String getType() {
        return null;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public XSSFWorkbook getFile() {
        return workbook;
    }

    @Override
    public String getFtype() {
        return StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;
    }

    @Override
    public String getFileType() {
        return FileUtil.SUFFIX_EXCEL_XLSX;
    }
}