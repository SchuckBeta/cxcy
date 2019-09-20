/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.project;

/**
 * 菜单表标记.
 * @author chenhao
 */
public enum ActMenuRemarks {
    XM("act项目流程"),
    DASAI("act大赛流程"),
    APPOINTMENT("act预约流程"),
    ENTER("act入驻流程"),
    SCORE("act学分流程"),
    ACT("act流程");
    private String name;

    private ActMenuRemarks(String name) {
      this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
