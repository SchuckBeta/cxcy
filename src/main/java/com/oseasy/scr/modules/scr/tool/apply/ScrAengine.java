/**
 * .
 */

package com.oseasy.scr.modules.scr.tool.apply;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.act.modules.actyw.tool.apply.IAengine;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowAutype;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;

public class ScrAengine extends IAengine{
    public ScrAengine() {
        super();
    }

    public ScrAengine(IApply apply, HttpServletRequest request, FlowAutype fautype) {
        super(apply, request, fautype);
    }

    @Override
    public IAengine initser() {
        this.appser = SpringContextHolder.getBean(ScoRapplyService.class);
        return this;
    }
}
