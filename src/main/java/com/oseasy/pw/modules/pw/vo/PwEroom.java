/**
 * .
 */

package com.oseasy.pw.modules.pw.vo;

/**
 * .
 * @author chenhao
 *
 */
public class PwEroom {
    private String id;
    private Integer num;

    public PwEroom() {
        super();
    }

    public PwEroom(String id, Integer num) {
        super();
        this.id = id;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
