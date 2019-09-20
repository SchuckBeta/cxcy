/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowAutype;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 */
public abstract class IAengine {
    private final static Logger logger = LoggerFactory.getLogger(IAengine.class);
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Boolean isinit;
    private IApply apply;
    protected IAservice<?> appser;
    private IAywservice ywser;
    private IAgservice gser;
    private IAgnservice gnser;
    private IAcservice cser;

    public IAengine() {
        super();
        if(this.appser == null){
            initser();
        }
        this.isinit = i();
    }
    public IAengine(IApply apply, HttpServletRequest request, FlowAutype fautype) {
        super();
        if(this.appser == null){
            initser();
        }
        this.request = request;
        this.apply = IApply.ia(apply, this.request, fautype);
        this.isinit = i();
    }

    /**
     * 初始化引擎服务.
     */
    public boolean i() {
        /**
         * 初始化服务.
         */
//        if(this.appser == null){
//            this.appser = SpringContextHolder.getBean(ScoRapplyService.class);
//        }
        if(this.ywser == null){
            this.ywser = SpringContextHolder.getBean(ActYwService.class);
        }
        if(this.gser == null){
            this.gser = SpringContextHolder.getBean(ActYwGroupService.class);
        }
        if(this.gnser == null){
            this.gnser = SpringContextHolder.getBean(ActYwGnodeService.class);
        }
        if(this.cser == null){
            this.cser = SpringContextHolder.getBean(ProProjectService.class);
        }


        /**
         * 处理参数.
         */
        if ((this.ywser == null) || (this.apply() == null) || (StringUtil.isEmpty(this.apply().getIactYwId()))) {
            return false;
        }

        IActYw iactYw = this.ywser.get(this.apply().getIactYwId());
        if(iactYw == null){
            return false;
        }
        if((FlowType.FWT_SCORE).equals(iactYw.flowType())){
//            this.appser = SpringContextHolder.getBean(ScoRapplyService.class);
        }else if((FlowType.FWT_DASAI).equals(iactYw.flowType())){
//            this.appser = SpringContextHolder.getBean(ProModelService.class);
//            this.apply = new ProModel();
        }else if((FlowType.FWT_XM).equals(iactYw.flowType())){
//            this.appser = SpringContextHolder.getBean(ProModelService.class);
//            this.apply = new ProModel();
        }else if((FlowType.FWT_ENTER).equals(iactYw.flowType())){
            //this.appser = SpringContextHolder.getBean(PwEnterService.class);
            //this.apply = new PwEnter();
        }else{
            logger.warn("流程类型未定义");
        }
        return true;
    }

    public abstract IAengine initser();

    public Boolean isinit() {
        return isinit;
    }

    public HttpServletRequest request() {
        return request;
    }
    public void request(HttpServletRequest request) {
        this.request = request;
    }
    public HttpServletResponse response() {
        return response;
    }
    public void response(HttpServletResponse response) {
        this.response = response;
    }

    public IApply apply() {
        return apply;
    }

    public void apply(IApply apply) {
        this.apply = apply;
    }

    public IAservice appser() {
        return appser;
    }

    public void appser(IAservice appser) {
        this.appser = appser;
    }

    public IAywservice ywser() {
        return ywser;
    }
    public void ywser(IAywservice ywser) {
        this.ywser = ywser;
    }
    public IAgnservice gnser() {
        return gnser;
    }
    public void gnser(IAgnservice gnser) {
        this.gnser = gnser;
    }
    public IAgservice gser() {
        return gser;
    }
    public void gser(IAgservice gser) {
        this.gser = gser;
    }
    public IAcservice cser() {
        return cser;
    }
    public void cser(IAcservice cser) {
        this.cser = cser;
    }
}
