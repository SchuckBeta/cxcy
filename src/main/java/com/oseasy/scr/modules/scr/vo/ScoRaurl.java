/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import com.oseasy.act.modules.actyw.tool.apply.IAurl;

/**
 * 学分流程审核Url地址.
 * @author chenhao
 */
public class ScoRaurl extends IAurl{
    private static final long serialVersionUID = 1L;

    public ScoRaurl() {
        super();
        //this.base = "/cmss";
        //this.lurl = this.base + "/lform/{iform}";
        //this.lajax = this.base + "/lform/ajaxList";
        //this.aurl = this.base + "/aform";
        this.asave = "/scr/scoRapply/ajaxAudit";
        this.view = "/scr/scoRapply/view";
        this.qurl = "/scr/scoCreditQuery/list";
        //this.qajax = this.base + "/qform/ajaxList";
        //this.gzurl = this.base + "/act/task/processMap";
    }
}
