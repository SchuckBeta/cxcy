/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.param;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pro.modules.workflow.enums.ExpType;

/**
 * 自定义流程导入参数定义.
 * @author chenhao
 *
 */
public class ItParam extends ItSupparam{
    private ActYw actYw;
    private ImpInfo ii;

    public ActYw getActYw() {
        return actYw;
    }

    public ImpInfo getIi() {
        return ii;
    }

    public void setIi(ImpInfo ii) {
        this.ii = ii;
    }

    public void setActYw(ActYw actYw) {
        this.actYw = actYw;
    }

    public String getImpType(){
        return ExpType.PmProject.getIdx();
    }
}
