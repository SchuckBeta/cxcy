/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.engine;

/**
 * 错误数据列与索引关系Vo.
 * @author chenhao
 *
 */
public class ItIdxVo {
    private Integer idx;
    private String key;

    public ItIdxVo(Integer idx, String key) {
        super();
        this.idx = idx;
        this.key = key;
    }
    public Integer getIdx() {
        return idx;
    }
    public void setIdx(Integer idx) {
        this.idx = idx;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
