/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 用户模糊查询接口.
 * @author chenhao
 *
 */
public class UserQuery extends User{
    private String qryStr;

    public String getQryStr() {
        return qryStr;
    }

    public void setQryStr(String qryStr) {
        this.qryStr = qryStr;
    }
}
