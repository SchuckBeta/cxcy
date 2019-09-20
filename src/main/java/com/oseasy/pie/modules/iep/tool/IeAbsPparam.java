/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

import com.oseasy.pie.modules.impdata.entity.ImpInfo;

/**
 * 请求参数.
 * @author chenhao
 *
 */
public abstract class IeAbsPparam implements IePparam{
    private String iepId;//模板ID
    private String id;//业务ID,与IeYw、IeYwser对应
    private String referrer;//业务处理完成重定向地址
    private String operType;//操作类型 TplOperType
    private ImpInfo ii;
    @Override
    public ImpInfo getIi() {
        if(ii == null){
            ii = new ImpInfo();
        }
        return ii;
    }

    public String getIepId() {
        return iepId;
    }

    public void setIepId(String iepId) {
        this.iepId = iepId;
    }

    @Override
    public String getId() {
        return this.id;
    }
    public void setIi(ImpInfo ii) {
        this.ii = ii;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String getReferrer() {
        return referrer;
    }
    @Override
    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
    @Override
    public void setOperType(String operType) {
        this.operType = operType;
    }
    @Override
    public String getOperType() {
        return operType;
    }
}
