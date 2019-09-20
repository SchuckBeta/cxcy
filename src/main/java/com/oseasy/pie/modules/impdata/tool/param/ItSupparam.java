/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.param;

import com.oseasy.pie.modules.impdata.tool.IitParam;
import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;

/**
 * 通用导入参数定义.
 * @author chenhao
 *
 */
public class ItSupparam implements IitParam<ItOper>{
    private ItOper oper;//支持操作类型
    private String ftype;//业务功能类型
    private IitTpl<?> tpl;//模板对象

    @Override
    public ItOper itOper() {
        return getOper();
    }

    public ItOper getOper() {
        return oper;
    }

    public void setOper(ItOper oper) {
        this.oper = oper;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public IitTpl<?> getTpl() {
        return tpl;
    }

    public void setTpl(IitTpl<?> tpl) {
        this.tpl = tpl;
    }
}
