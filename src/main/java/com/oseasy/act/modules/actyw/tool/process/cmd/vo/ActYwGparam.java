/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.vo;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRunner;
import com.oseasy.act.modules.actyw.tool.process.cmd.Gpnode;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.util.common.utils.DateUtil;

import net.sf.json.JSONObject;

/**
 * .
 * @author chenhao
 *
 */
public class ActYwGparam {
    Boolean temp;
    String groupId;
    JSONObject uiJson;//界面UI字符，用于预览和显示
    String uiHtml;//界面UI字符，用于预览和显示

    List<Gpnode> list;
    public List<Gpnode> getList() {
        return list;
    }
    public void setList(List<Gpnode> list) {
        this.list = list;
    }

    public Boolean getTemp() {
        return temp;
    }
    public void setTemp(Boolean temp) {
        this.temp = temp;
    }
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public JSONObject getUiJson() {
        return uiJson;
    }
    public void setUiJson(JSONObject uiJson) {
        this.uiJson = uiJson;
    }
    public String getUiHtml() {
        return uiHtml;
    }
    public void setUiHtml(String uiHtml) {
        this.uiHtml = uiHtml;
    }
    /**
     * 转换参数为节点对象.
     */
    public static List<ActYwGnode> convert(ActYwRunner runner, ActYwGroup group, ActYwGparam gparam) {
        List<ActYwGnode> gnodes = Lists.newArrayList();
        for (Gpnode gpe : gparam.getList()) {
            gnodes.add(Gpnode.gen(runner, gpe, group));
        }
        return gnodes;
    }

    public static void main(String[] args) {
        ActYwGparam param = new ActYwGparam();
        List<Gpnode> gps = Lists.newArrayList();
        gps.add(new Gpnode());
        param.setList(gps);
        System.out.println(JSONObject.fromObject(param));
    }
}
