/**
 * .
 */

package com.oseasy.pro.modules.tpl.vo;

import java.io.Serializable;

/**
 * word上传需要参数.
 * @author chenhao
 *
 */
public class Wtparam implements Serializable{
    private String wprefix;
    private String wtypes;

    public Wtparam() {
        super();
    }
    public Wtparam(String wprefix, String wtypes) {
        super();
        this.wprefix = wprefix;
        this.wtypes = wtypes;
    }
    public String getWprefix() {
        return wprefix;
    }
    public void setWprefix(String wprefix) {
        this.wprefix = wprefix;
    }
    public String getWtypes() {
        return wtypes;
    }
    public void setWtypes(String wtypes) {
        this.wtypes = wtypes;
    }
}
