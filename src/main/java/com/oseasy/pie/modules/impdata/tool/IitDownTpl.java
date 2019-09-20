/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导入模板引擎.
 * @author chenhao
 *
 */
public interface IitDownTpl{
    public final static Integer START_ROW_NUM = 0;
    public final static Integer MAX_ROW_NUM = 50000;
    /**
     * 设置模板头.
     */
    public void setHead(XSSFSheet sheet);

    /**
     * 设置模板体.
     */
    public void setBody(XSSFWorkbook wb, XSSFSheet sheet);
}
