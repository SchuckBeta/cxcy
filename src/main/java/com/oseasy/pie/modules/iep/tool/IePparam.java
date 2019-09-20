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
public interface IePparam {
    public String getOperType();//获取操作类型
    public void setOperType(String operType);
    public String getIepId();//获取模板ID
    public void setIepId(String iepId);
    public String getReferrer();//获取处理完后的重定向
    public void setReferrer(String referrer);

    public String getkey();//获取业务ID的请求Key
    public String getId();//获取业务ID
    public void setId(String id);

    public ImpInfo getIi();//获取信息对象
    public void setIi(ImpInfo ii);
}
