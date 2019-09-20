/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.oseasy.pie.modules.impdata.tool.IitCheckEety;
import com.oseasy.pie.modules.impdata.tool.IitCheckEservice;
import com.oseasy.pie.modules.impdata.tool.IitCheckInfo;
import com.oseasy.pie.modules.impdata.tool.IitCheckParam;
import com.oseasy.pie.modules.impdata.tool.IitCheckRparam;

/**
 * 导入参数校验的参数对象.
 * @author chenhao
 */
public abstract class AbsItCparam<X extends XSSFSheet, IES extends IitCheckEservice, I extends IitCheckInfo, IE extends IitCheckEety> implements IitCheckParam {
    public final static Logger logger = Logger.getLogger(AbsItCparam.class);
    protected X xs; //导入的模板对象
    protected IES ies; //错误服务
    protected I info; //信息
    protected IE ie; //错误信息
    protected int rows;//当前行数
    protected int tag;//校验的结果状态(行级别)
    protected String val;//检验的属性值
    protected int idx;//检验的列索引
    protected int idxRow;//检验的Row索引
    protected String idxSheet;//检验的Sheet索引
    protected int tags;//校验的结果状态(Sheet级别汇总)
    protected String colName;//检验的属性列名
    protected IitCheckRparam rparam;//检查返回参数

    public AbsItCparam() {
        super();
    }

    public AbsItCparam(X xs, IES ies, I info, IE ie) {
        super();
        this.xs = xs;
        this.ies = ies;
        this.info = info;
        this.ie = ie;
    }

    public AbsItCparam(X xs, IES ies, I info, IE ie, int rows, int tag, int idx, String val) {
        super();
        this.xs = xs;
        this.ies = ies;
        this.info = info;
        this.ie = ie;
        this.rows = rows;
        this.tag = tag;
        this.idx = idx;
        this.val = val;
    }
    public AbsItCparam(X xs, IES ies, I info, IE ie, int rows, int tag, int idx, String val, IitCheckRparam rparam) {
        super();
        this.xs = xs;
        this.ies = ies;
        this.info = info;
        this.ie = ie;
        this.rows = rows;
        this.tag = tag;
        this.idx = idx;
        this.rparam = rparam;
    }

    public String getIdxSheet() {
        return idxSheet;
    }

    public void setIdxSheet(String idxSheet) {
        this.idxSheet = idxSheet;
    }

    public int getIdxRow() {
        return idxRow;
    }

    public void setIdxRow(int idxRow) {
        this.idxRow = idxRow;
    }

    public X getXs() {
        return xs;
    }

    public void setXs(X xs) {
        this.xs = xs;
    }

    public I getInfo() {
        return info;
    }
    public void setInfo(I info) {
        this.info = info;
    }
    public IES getIes() {
        return ies;
    }
    public void setIes(IES ies) {
        this.ies = ies;
    }
    public IE getIe() {
        return ie;
    }
    public void setIe(IE ie) {
        this.ie = ie;
    }
    public int getTag() {
        return tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public int getIdx() {
        return idx;
    }
    public void setIdx(int idx) {
        this.idx = idx;
    }
    public String getVal() {
        return val;
    }
    public void setVal(String val) {
        this.val = val;
    }

    public String getColName() {
        return colName;
    }

    public int getTags() {
        return tags;
    }

    public void setTags(int tags) {
        this.tags = tags;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public IitCheckRparam getRparam() {
        return rparam;
    }

    public void setRparam(IitCheckRparam rparam) {
        this.rparam = rparam;
    }
}
