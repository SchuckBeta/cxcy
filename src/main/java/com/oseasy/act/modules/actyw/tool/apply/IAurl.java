/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.io.Serializable;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程审核相关的Url地址.
 * @author chenhao
 */
public class IAurl implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String base;//根路径
    protected String start;//申请页面Url
    protected String timeline;//时间轴页面Url
    protected String welcome;//欢迎页面Url
    protected String view;//查看Url

    protected String lurl;//审核列表Url
    protected String lajax;//审核列表数据Url
    protected String ldel;//审核列表删除Url
    protected String aurl;//审核Url
    protected String asave;//审核保存Url


    protected String qurl;//查询列表Url
    protected String qajax;//查询列表数据Url

    protected String gzurl;//流程跟踪Url

    public IAurl() {
        super();
        this.base = "/cmss";
        this.start = this.base + "/start";
        this.welcome = ActSval.ASD_INDX;

        this.lurl = this.base + "/lform/";
        this.lajax = this.base + "/lform/ajaxList";
        this.ldel = this.base + "/lform/ajaxDelpl";
        this.aurl = this.base + "/aform";
        this.asave = this.base + "/asave";
        this.view = this.base + "/vform";
        this.qurl = this.base + "/qform";
        this.qajax = this.base + "/qform/ajaxList";

        this.gzurl = this.base + "/act/task/processMap";
    }

    /**
     * 生成流程审核节点列表菜单地址（新版）.
     * @param actYwId 流程ID
     * @param gnid 节点ID
     * @param formId 表单ID
     * @return String
     */
    public static String genLurl(IAurl iaurl, String formId, String actYwId, String gnid) {
        String base = iaurl.getLurl() + formId;
        if(StringUtil.isEmpty(actYwId) || StringUtil.isEmpty(gnid)){
            return base;
        }
        return base
                + StringUtil.WENH + IActYw.IACTYW_ID + StringUtil.DENGH + actYwId
                + StringUtil.AD + IGnode.IGNODE_ID + StringUtil.DENGH + gnid;
    }

    /**
     * 生成流程查询列表菜单地址（新版）.
     * @param actYwId 流程ID
     * @return String
     */
    public static String genQurl(IAurl iaurl, String actYwId) {
        return iaurl.getQurl() + StringUtil.WENH + IActYw.IACTYW_ID + StringUtil.DENGH + actYwId;
    }

    /**
     * 生成流程欢迎菜单地址（新版）.
     * @param actYwId 流程ID
     * @return String
     */
    public static String genWelcome(IAurl iaurl, String actYwId) {
        return iaurl.getWelcome() + StringUtil.WENH + IActYw.IACTYW_ID + StringUtil.DENGH + actYwId;
    }

    public String getLdel() {
        return ldel;
    }

    public void setLdel(String ldel) {
        this.ldel = ldel;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getLurl() {
        return lurl;
    }
    public void setLurl(String lurl) {
        this.lurl = lurl;
    }
    public String getLajax() {
        return lajax;
    }
    public void setLajax(String lajax) {
        this.lajax = lajax;
    }
    public String getAurl() {
        return aurl;
    }
    public void setAurl(String aurl) {
        this.aurl = aurl;
    }
    public String getAsave() {
        return asave;
    }
    public void setAsave(String asave) {
        this.asave = asave;
    }
    public String getView() {
        return view;
    }
    public void setView(String view) {
        this.view = view;
    }
    public String getQurl() {
        return qurl;
    }
    public void setQurl(String qurl) {
        this.qurl = qurl;
    }
    public String getQajax() {
        return qajax;
    }
    public void setQajax(String qajax) {
        this.qajax = qajax;
    }
    public String getGzurl() {
        return gzurl;
    }
    public void setGzurl(String gzurl) {
        this.gzurl = gzurl;
    }
}