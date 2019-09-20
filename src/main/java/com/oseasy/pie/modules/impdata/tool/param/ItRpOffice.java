/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.param;

import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.pie.modules.impdata.tool.IitCheckRparam;

/**
 * .
 * @author chenhao
 *
 */
public class ItRpOffice implements IitCheckRparam{
    private Office school;//学校
    private Office office;//学院
    private Office org;//专业
    private Office cur;//当前保存数据
    public Office getSchool() {
        return school;
    }
    public void setSchool(Office school) {
        this.school = school;
    }
    public Office getOffice() {
        return office;
    }
    public void setOffice(Office office) {
        this.office = office;
    }
    public Office getOrg() {
        return org;
    }
    public void setOrg(Office org) {
        this.org = org;
    }
    public Office getCur() {
        return cur;
    }
    public void setCur(Office cur) {
        this.cur = cur;
    }
}
