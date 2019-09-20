/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowAuparam;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowAutype;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.IOrby;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.IAuser;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程申请实体接口.
 * @author chenhao
 */
public interface IApply extends IOrby{
    /**
     * 自定义流程申请对象KEY.
     */
    public final static String I_APPLY = "iapply";
    public final static String I_ACTYW = "iactyw";
    public static final String I_GNODE = "ignode";
    public final static String I_GROUP = "igroup";
    public final static String I_CONFIG = "iconfig";
    public final static String I_AURL = "iaurl";//审核相关的Url
    public final static String I_PAGE = "ipage";
    public final static String I_LURL = "ilurl";//列表URL路径

    /**
     * 接收页面传递参数.
     * 流程ID必填.
     * @param apply 申请
     * @param request 请求
     * @param fautype 请求类型
     * @return IApply
     */
    public static IApply ia(IApply apply, HttpServletRequest request, FlowAutype fautype){
        if((FlowAutype.FLOW).equals(fautype)){
            apply.iactYw().id(request.getParameter(IActYw.IACTYW_ID));
            apply.iid(request.getParameter(CoreJkey.JK_ID));
        }else if((FlowAutype.APPLY).equals(fautype)){
            apply.iactYw().id(request.getParameter(IActYw.IACTYW_ID));
            apply.iid(request.getParameter(CoreJkey.JK_ID));
        }else if((FlowAutype.FLOWTYPE).equals(fautype)){
            apply.iactYw().id(request.getParameter(IActYw.IACTYW_ID));
            apply.iactYw().group().flowType(FlowType.getByKey(request.getParameter(IGroup.IFLOW_TYPE)));
            apply.iactYw().config().ptype(request.getParameter(IConfig.I_PTYPE));
        }else if((FlowAutype.AFLOW).equals(fautype)){
            apply.iactYw().id(request.getParameter(IActYw.IACTYW_ID));
            apply.iid(request.getParameter(CoreJkey.JK_ID));
        }else if((FlowAutype.GNODE).equals(fautype)){
            apply.iactYw().id(request.getParameter(IActYw.IACTYW_ID));
            apply.iid(request.getParameter(CoreJkey.JK_ID));
            apply.ignode().id(request.getParameter(IGnode.IGNODE_ID));
        }
        return apply;
    }

    public static IApply iall(IApply apply, HttpServletRequest request){
        apply = IApply.ia(apply, request, FlowAutype.FLOW);
        //其它拓展查询条件，非私有的
        //TODO CHENHAO
        //apply = IApply.ia(apply, request);
        //其它拓展查询条件，私有的
        //TODO CHENHAO
        //apply = IApply.ia(apply, request);
        return apply;
    }

    public static IAengine iobj(IAengine engine) {
        //根据匹配传页面需要参数，数据
        if (engine.apply().ignode() != null) {
            engine.apply().ignode(engine.gnser().get(engine.apply().getIgnodeId()));
        }
        if (engine.apply().iactYw() != null) {
            engine.apply().iactYw(engine.ywser().get(engine.apply().getIactYwId()));
        }
        if (engine.apply().iactYw().group() != null) {
            engine.apply().iactYw().group(engine.gser().get(engine.apply().iactYw().group().id()));
        }
        if ((engine.apply().iactYw().config() != null) && StringUtil.isNotEmpty(engine.apply().iactYw().config().id())) {
            engine.apply().iactYw().config(engine.cser().get(engine.apply().iactYw().config().id()));
        }
        return engine;
    }

    /**
     * 传递参数到页面.
     */
    public static Model imodel(IAengine engine, Model model, HttpServletRequest request, HttpServletResponse response){
        return imodel(engine, model, request, response, Arrays.asList(new FlowAuparam[]{FlowAuparam.DEFAULT}));
    }
    public static Model imodel(IAengine engine, Model model, HttpServletRequest request, HttpServletResponse response, List<FlowAuparam> fauparams){
        iobj(engine);

        if((fauparams).contains(FlowAuparam.ALL)){
            model.addAttribute(CoreJkey.JK_ISSUP, UserUtils.isSuper());
            model.addAttribute(CoreJkey.JK_ISADMIN, UserUtils.isAdm());
            model.addAttribute(CoreJkey.JK_ISADMSYS, UserUtils.isAdminSys());
            model.addAttribute(CoreJkey.JK_ISADMOFFICE, UserUtils.isNscAdminOffice());

            model.addAttribute(IApply.I_APPLY, engine.appser().gen(request));
            model.addAttribute(IApply.I_ACTYW, engine.apply().iactYw());
            model.addAttribute(IApply.I_GNODE, engine.apply().ignode());
            model.addAttribute(IApply.I_GROUP, engine.apply().iactYw().group());
            model.addAttribute(IApply.I_CONFIG, engine.apply().iactYw().config());
            model.addAttribute(IApply.I_AURL, engine.apply().getIaurl());

            model.addAttribute(IOrby.ORBY_ORS_KEY, engine.apply().ors());

            //列表表单ID
            String lformId = request.getParameter(IForm.ILFORM_ID);
            if(StringUtil.isNotEmpty(lformId)){
                model.addAttribute(IForm.ILFORM_ID, StringUtil.isEmpty(lformId) ? "" : lformId);
                //列表URL
                model.addAttribute(IApply.I_LURL, IAurl.genLurl(engine.apply().getIaurl(), lformId, engine.apply().getIactYwId(), engine.apply().getIgnodeId()));
            }
        }else if((fauparams).contains(FlowAuparam.DEFAULT)){
            model.addAttribute(CoreJkey.JK_ISSUP, UserUtils.isSuper());
            model.addAttribute(CoreJkey.JK_ISADMIN, UserUtils.isAdm());
            model.addAttribute(CoreJkey.JK_ISADMSYS, UserUtils.isAdminSys());
            model.addAttribute(CoreJkey.JK_ISADMOFFICE, UserUtils.isNscAdminOffice());

            model.addAttribute(IApply.I_APPLY, engine.appser().gen(request));
            model.addAttribute(IApply.I_ACTYW, engine.apply().iactYw());
            model.addAttribute(IApply.I_GNODE, engine.apply().ignode());
            model.addAttribute(IApply.I_GROUP, engine.apply().iactYw().group());
            model.addAttribute(IApply.I_CONFIG, engine.apply().iactYw().config());
            model.addAttribute(IApply.I_AURL, engine.apply().getIaurl());

            //列表表单ID
            String lformId = request.getParameter(IForm.ILFORM_ID);
            if(StringUtil.isNotEmpty(lformId)){
                model.addAttribute(IForm.ILFORM_ID, StringUtil.isEmpty(lformId) ? "" : lformId);
                //列表URL
                model.addAttribute(IApply.I_LURL, IAurl.genLurl(engine.apply().getIaurl(), lformId, engine.apply().getIactYwId(), engine.apply().getIgnodeId()));
            }
        }else if((fauparams).contains(FlowAuparam.LIST_AUDIT)){
            model.addAttribute(IApply.I_APPLY, engine.appser().gen(request));
            model.addAttribute(IApply.I_ACTYW, engine.apply().iactYw());
            model.addAttribute(IApply.I_GNODE, engine.apply().ignode());
            model.addAttribute(IApply.I_GROUP, engine.apply().iactYw().group());
            model.addAttribute(IApply.I_CONFIG, engine.apply().iactYw().config());
            model.addAttribute(IApply.I_AURL, engine.apply().getIaurl());

            model.addAttribute(IOrby.ORBY_ORS_KEY, engine.apply().ors());

            //列表表单ID
            String lformId = request.getParameter(IForm.ILFORM_ID);
            if(StringUtil.isNotEmpty(lformId)){
                model.addAttribute(IForm.ILFORM_ID, StringUtil.isEmpty(lformId) ? "" : lformId);
                //列表URL
                model.addAttribute(IApply.I_LURL, IAurl.genLurl(engine.apply().getIaurl(), lformId, engine.apply().getIactYwId(), engine.apply().getIgnodeId()));
            }
        }else{
            if((fauparams).contains(FlowAuparam.ADM_R)){
                model.addAttribute(CoreJkey.JK_ISADMIN, UserUtils.isAdm());
            }
            if((fauparams).contains(FlowAuparam.ADM_RSUP)){
                model.addAttribute(CoreJkey.JK_ISSUP, UserUtils.isSuper());
            }
            if((fauparams).contains(FlowAuparam.ADM_RSYS)){
                model.addAttribute(CoreJkey.JK_ISADMSYS, UserUtils.isAdminSys());
            }
            if((fauparams).contains(FlowAuparam.ADM_ROFFICE)){
                model.addAttribute(CoreJkey.JK_ISADMOFFICE, UserUtils.isNscAdminOffice());
            }

            if((fauparams).contains(FlowAuparam.APPLY)){
                model.addAttribute(IApply.I_APPLY, engine.appser().gen(request));
            }
            if((fauparams).contains(FlowAuparam.ACTYW)){
                model.addAttribute(IApply.I_ACTYW, engine.apply().iactYw());
            }
            if((fauparams).contains(FlowAuparam.GNODE)){
                model.addAttribute(IApply.I_GNODE, engine.apply().ignode());
            }
            if((fauparams).contains(FlowAuparam.GROUP)){
                model.addAttribute(IApply.I_GROUP, engine.apply().iactYw().group());
            }
            if((fauparams).contains(FlowAuparam.CONFIG)){
                model.addAttribute(IApply.I_CONFIG, engine.apply().iactYw().config());
            }
            if((fauparams).contains(FlowAuparam.AURL)){
                model.addAttribute(IApply.I_AURL, engine.apply().getIaurl());
            }
            if((fauparams).contains(FlowAuparam.ORBY)){
                model.addAttribute(IOrby.ORBY_ORS_KEY, engine.apply().ors());
            }
            if((fauparams).contains(FlowAuparam.FORM_ID)){
                //列表表单ID
                String lformId = request.getParameter(IForm.ILFORM_ID);
                if(StringUtil.isNotEmpty(lformId)){
                    model.addAttribute(IForm.ILFORM_ID, StringUtil.isEmpty(lformId) ? "" : lformId);
                    //列表URL
                    model.addAttribute(IApply.I_LURL, IAurl.genLurl(engine.apply().getIaurl(), lformId, engine.apply().getIactYwId(), engine.apply().getIgnodeId()));
                }
            }
        }
        return model;
    }

    /**
     * 申请页传递参数.
     * @param model 模型
     * @param request 请求
     * @return Model
     */
    public static Model iappmodel(Model model, HttpServletRequest request){
        model.addAttribute(IActYw.IACTYW_ID, request.getParameter(IActYw.IACTYW_ID));
        model.addAttribute(IGroup.IFLOW_TYPE, request.getParameter(IGroup.IFLOW_TYPE));
        model.addAttribute(IConfig.I_PTYPE, request.getParameter(IConfig.I_PTYPE));
        return model;
    }

    /**********************************************************************************
     * 获取流程配置实体.
     * @return IActYw
     */
    IActYw iactYw();

    /**
     * 获取流程配置实体ID.
     * @return String
     */
    String getIactYwId();

    /**
     * 设置流程配置实体.
     * @return IActYw
     */
    IActYw iactYw(IActYw iactYw);

    /**
     * 获取流程节点实体.
     * @return IGnode
     */
    IGnode ignode();

    /**
     * 设置流程节点实体.
     * @return IGnode
     */
    IGnode ignode(IGnode ignode);

    /**
     * 获取流程节点实体ID.
     * @return String
     */
    public String getIgnodeId();

    /**
     * 获取流程节实例ID.
     * @return String
     */
    String getIprocInsId();

    /**
     * 设置流程节实例ID.
     * @return String
     */
    String iprocInsId(String ipiid);

    /**
     * 获取流程节实例变量.
     * @return Map
     */
    public Map<String,Object> ivars();

    /**********************************************************************************
     * 获取流程申请人实体ID.
     * @return
     */
    IAuser iauser();

    /**
     * 设置流程申请人实体ID.
     * @return String
     */
    String iauserId();

    /**
     * 设置流程申请人实体.
     * @return IAuser
     */
    IAuser iauser(IAuser iauser);

    /**********************************************************************************
     * 获取流程审核对象.
     * @return IAsup
     */
    IAsup iasup();

    /**
     * 设置流程审核对象.
     * @return IAsup
     */
    IAsup iasup(IAsup iasup);

    /**
     * 获取流程申请拓展属性对象.
     * @return IAprop
     */
    IAprop iaprop();

    /**
     * 设置流程申请拓展属性对象.
     * @return IAprop
     */
    IAprop iaprop(IAprop iaprop);

    /**
     * 获取流程审核Act对象.
     * @return IAmap
     */
    IAmap getIamap();

    /**
     * 设置流程审核Act对象.
     * @return IAmap
     */
    IAmap iamap(IAmap iamap);

    /**
     * 设置流程审核Url对象.
     * @return IAmap
     */
    IAurl getIaurl();

    /**
     * 获取流程申请ID.
     * @return String
     */
    String getIid();
    /**
     * 设置流程申请ID.
     * @return String
     */
    String iid(String iid);

    /**
     * 获取流程申请名称.
     * @return String
     */
    String getIname();
}
