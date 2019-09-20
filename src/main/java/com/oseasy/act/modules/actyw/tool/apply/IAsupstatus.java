/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;

/**
 * 审核状态默认实体.
 * @author chenhao
 */
public class IAsupstatus implements IAstatus{
    private String key;
    private String remark;

    @Override
    public String getIstatus() {
        return PassNot.getByKey(this.key).getKey();
    }

    @Override
    public String getIregType() {
        return RegType.RT_EQ.getId();
    }

    @Override
    public String getIkey() {
        return key;
    }

    @Override
    public String getIremark() {
        return remark;
    }

    public IAsupstatus setKey(String key) {
        this.key = key;
        return this;
    }

    public IAsupstatus setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    @Override
    public String getIalias() {
        return this.remark;
    }

    @Override
    public String getIstate() {
        return this.remark;
    }

    /**
     * 枚举转换.
     */
    public static IAsupstatus convert(PassNot passNot) {
        return new IAsupstatus().setKey(passNot.getKey()).setRemark(passNot.getRemark());
    }
}
