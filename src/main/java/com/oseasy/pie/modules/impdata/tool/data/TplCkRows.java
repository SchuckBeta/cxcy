/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.data;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 模板校验实体.
 * @author chenhao
 */
public class TplCkRows{
    private Integer cur;
    private List<TplCkCol> cols;

    public TplCkRows(int cur) {
        super();
        this.cur = cur;
        this.cols = Lists.newArrayList();
    }
    public TplCkRows(Integer cur, List<TplCkCol> cols) {
        super();
        this.cur = cur;
        this.cols = cols;
    }
    public Integer getCur() {
        return cur;
    }
    public void setCur(Integer cur) {
        this.cur = cur;
    }
    public List<TplCkCol> getCols() {
        return cols;
    }
    public void setCols(List<TplCkCol> cols) {
        this.cols = cols;
    }
}
