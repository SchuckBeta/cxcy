/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.data;

/**
 * 模板校验列实体.
 * @author chenhao
 */
public class TplCkCol{
    private Integer idx;
    private String name;

    public TplCkCol() {
        super();
    }
    public TplCkCol(Integer idx, String name) {
        super();
        this.idx = idx;
        this.name = name;
    }
    public Integer getIdx() {
        return idx;
    }
    public void setIdx(Integer idx) {
        this.idx = idx;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
