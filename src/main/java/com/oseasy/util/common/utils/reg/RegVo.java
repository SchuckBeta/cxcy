/**
 * .
 */

package com.oseasy.util.common.utils.reg;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 规则处理类.
 * @author chenhao
 *
 */
public class RegVo {
    private RegType type;
    private String key;
    private String exp;
    private String x;
    private String y;
    private Double dx;
    private Double dy;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Double getDx() {
        if((this.dx == null) && StringUtil.isNumeric(this.x)){
            this.dx = Double.parseDouble(this.x);
        }
        return dx;
    }

    public Double getDy() {
        if((this.dy == null) && StringUtil.isNumeric(this.y)){
            this.dy = Double.parseDouble(this.y);
        }
        return dy;
    }

    public RegVo(String key) {
        super();
        this.key = key;
    }

    public RegType getType() {
        return type;
    }

    public void setType(RegType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RegVo [key=" + key + ", exp=" + exp + ", x=" + x + ", y=" + y + "]";
    }
}