/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.tpl;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class ItTplXsheet extends IitAbsTpl implements IitTpl<XSSFSheet>{
    private XSSFSheet sheet;

    public ItTplXsheet() {
        super();
    }

    public ItTplXsheet(XSSFSheet sheet) {
        super();
        this.sheet = sheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public XSSFSheet getFile() {
        return sheet;
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
