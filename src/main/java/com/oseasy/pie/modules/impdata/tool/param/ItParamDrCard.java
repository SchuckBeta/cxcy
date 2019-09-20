/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.param;

import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pro.modules.workflow.enums.ExpType;

/**
 * 自定义流程导入参数定义.
 * @author chenhao
 *
 */
public class ItParamDrCard extends ItSupparam{
    private ImpInfo ii;

    public ImpInfo getIi() {
        return ii;
    }

    public void setIi(ImpInfo ii) {
        this.ii = ii;
    }

    public String getImpType(){
        return ExpType.DrCard.getIdx();
    }
}
