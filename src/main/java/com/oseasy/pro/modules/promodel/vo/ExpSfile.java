/**
 * .
 */

package com.oseasy.pro.modules.promodel.vo;

import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 导出Excel参数.
 * @author chenhao
 *
 */
public class ExpSfile {
    private ExportParams param;

    public ExportParams getParam() {
        return param;
    }

    public void setParam(ExportParams param) {
        this.param = param;
        if((param.getHeight() == 0)){
            this.param.setHeight((short) 7);
        }
    }
}
