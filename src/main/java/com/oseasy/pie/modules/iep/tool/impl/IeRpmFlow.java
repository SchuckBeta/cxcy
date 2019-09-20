/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import com.oseasy.act.modules.actyw.tool.IeYw;
import com.oseasy.act.modules.actyw.tool.IeYwser;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.IeAbsPparam;
import com.oseasy.pie.modules.iep.tool.IePparam;
import com.oseasy.pie.modules.impdata.tool.IitParam;
import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class IeRpmFlow extends IeAbsPparam implements IitParam<ItOper>{
    private String actywId;//IActYw
    private String gnodeId;//节点ID
    private ItOper oper;//支持操作类型
    private String ftype;//业务功能类型
    private IitTpl<?> tpl;//模板对象

    public String getActywId() {
        return actywId;
    }

    public void setActywId(String actywId) {
        this.actywId = actywId;
    }

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

    public String getGnodeId() {
        return gnodeId;
    }

    public void setGnodeId(String gnodeId) {
        this.gnodeId = gnodeId;
    }

    /**
     * 检查业务对象处理是否正确.
     * @param iepTpl 模板配置对象
     * @param IeAbsYw 业务对象
     * @return Rtstatus
     */
    public static ApiTstatus<Object> checkIeYw(IepTpl iepTpl, IePparam pparam, IeYwser ywser) {
        if((pparam == null)){
            return new ApiTstatus<Object>(false, "导入失败，业务项目配置不能为空");
        }

        IeRpmFlow param = (IeRpmFlow)pparam;
        if(StringUtil.isEmpty(param.getActywId())){
            return new ApiTstatus<Object>(false, "导入失败，业务项目配置ID不能为空");
        }

        IeYw ieyw = ywser.get(param.getActywId());
        if(ieyw == null){
            return new ApiTstatus<Object>(false, "导入失败，未找到业务项目配置信息");
        }
        return new ApiTstatus<Object>(true, "成功", ieyw);
    }

    /**
     * 生成参数.
     */
    public static String genParam(IeRpmFlow param) {
       return "&referrer="+ param.getReferrer() +"&actywId="+ param.getActywId() + "&gnodeId="+ param.getGnodeId();
    }

    @Override
    public String getkey() {
        return IActYw.IACTYW_ID;
    }
}
