/**
 * .
 */

package com.oseasy.pie.common.utils.poi;

import org.apache.poi.ss.usermodel.DataValidationConstraint;

import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.util.common.utils.DateUtil;

public class ExcelDivo {
    private String key;//列标识
    private Integer index;//索引
    private String[] items;//数据
    private Integer firstRow;//其开始行
    private Integer lastRow;//结束行
    private Integer startCol;//开始列
    private Integer endCol;//结束列
    private int vtype;//数据类型
    private int voptype;//操作符
    private String vmin;//最小长度
    private String vmax;//最大长度
    private String vformat;//日期格式

    public ExcelDivo() {
        super();
        this.vmin = "0";
        this.vmax = "3000";
        this.firstRow = IitDownTpl.START_ROW_NUM;
        this.lastRow = IitDownTpl.MAX_ROW_NUM;
        this.vformat = DateUtil.FMT_YYYY_MM_DD_HHmmss;
        this.vtype = DataValidationConstraint.ValidationType.TEXT_LENGTH;
    }

    public ExcelDivo(String key, String[] items, Integer firstRow, Integer lastRow, Integer startCol) {
        super();
        this.key = key;
        this.items = items;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.startCol = startCol;
        this.endCol = startCol;
        this.vtype = DataValidationConstraint.ValidationType.TEXT_LENGTH;
    }

    public ExcelDivo(ExcelDivo evo) {
        super();
        this.key = evo.key;
        this.index = evo.index;
        this.items = evo.items;
        this.firstRow = evo.firstRow;
        this.lastRow = evo.lastRow;
        this.startCol = evo.startCol;
        this.endCol = evo.endCol;
        this.vtype = DataValidationConstraint.ValidationType.TEXT_LENGTH;
    }

    public int getVoptype() {
        return voptype;
    }
    public int getVtype() {
        return vtype;
    }

    public Integer getIndex() {
        return index;
    }
    public String getKey() {
        return key;
    }
    public String[] getItems() {
        return items;
    }
    public Integer getFirstRow() {
        return firstRow;
    }
    public Integer getLastRow() {
        return lastRow;
    }
    public Integer getStartCol() {
        return startCol;
    }
    public Integer getEndCol() {
        if((this.endCol == null) && (this.startCol != null)){
            this.endCol = this.startCol;
        }
        return endCol;
    }

    public String getVmin() {
        return vmin;
    }

    public String getVmax() {
        return vmax;
    }
    public String getVformat() {
        return vformat;
    }

    //新增Builder静态类并赋默认值
    public static class Builder {
        private ExcelDivo evo;

        public Builder() {
            super();
            evo = new ExcelDivo();
        }

        public Builder index(Integer index) {
            evo.index = index;
            return this;
        }
        public Builder key(String key) {
            evo.key = key;
            return this;
        }
        public Builder items(String[] items) {
            evo.items = items;
            return this;
        }
        public Builder firstRow(Integer firstRow) {
            evo.firstRow = firstRow;
            return this;
        }
        public Builder lastRow(Integer lastRow) {
            evo.lastRow = lastRow;
            return this;
        }
        public Builder startCol(Integer startCol) {
            evo.startCol = startCol;
            return this;
        }
        public Builder endCol(Integer endCol) {
            evo.endCol = endCol;
            return this;
        }
        public Builder voptype(int voptype) {
            evo.voptype = voptype;
            return this;
        }
        public Builder vtype(int vtype) {
            evo.vtype = vtype;
            return this;
        }
        public Builder vmin(String vmin) {
            evo.vmin = vmin;
            return this;
        }
        public Builder vmax(String vmax) {
            evo.vmax = vmax;
            return this;
        }
        public Builder vformat(String vformat) {
            evo.vformat = vformat;
            return this;
        }

        //新建一个build方法，创建一个父类对象，传递给apply方法为这个空对象赋构建出来的参数值，返回这个构建对象即可。
        public ExcelDivo build(){
            return new ExcelDivo(this.evo);
        }
    }
}
