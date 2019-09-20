/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import com.oseasy.pro.modules.promodel.vo.ExpRparam;

/**
 * .
 * @author chenhao
 *
 */
public class PwEnterRparam implements ExpRparam{
    private String type;//类型

    public PwEnterRparam(String type) {
        super();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
