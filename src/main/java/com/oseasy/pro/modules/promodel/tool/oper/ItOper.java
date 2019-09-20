/**
 * .
 */

package com.oseasy.pro.modules.promodel.tool.oper;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.pro.modules.promodel.tool.IitOper;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导入功能Vo,参数gnodeId和first.
 * @author chenhao
 *
 */
public class ItOper implements IitOper{
    private Boolean isFirstTask;//判断导入节点是否为第一个节点，默认不是
    private ActYwGnode first;//第一个节点
    private Boolean hasFile;//是否有附件
    private String actywId;//导入的actywId
    private ItReqParam reqParam;//请求参数

    private Boolean isImpHisData;//导入历史数据
    private Boolean isImpFirstData;//导入审核数据
    private Boolean isImpFileData;//导入附件数据
    public ItOper() {
        super();
    }

    public ItOper(HttpServletRequest request) {
        this.actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
        this.hasFile = (Const.YES).equals(request.getParameter(ItReqParam.HAS_FILE))?true:false;
        this.reqParam = new ItReqParam(request, actywId);
        String tplType = request.getParameter(TplType.TPL_TYPE);
        this.reqParam.setTplType(StringUtil.isEmpty(tplType) ? TplType.MR.getKey() : tplType);
    }

    public ItOper(ActYwGnode first, String gnodeId) {
        super();
        this.first = first;
    }
    public ItOper(ActYwGnode first, String gnodeId, Boolean isImpFileData) {
        super();
//        this.isImpFileData = (isImpFileData == null) ? false : isImpFileData;
        this.isImpFileData = true;
        this.first = first;
        this.reqParam = new ItReqParam();
    }
    public ItOper(ActYwGnode first, String gnodeId, Boolean isImpFileData, boolean isNew) {
        super();
//        this.isImpFileData = (isImpFileData == null) ? false : isImpFileData;
        this.isImpFileData = true;
        this.first = first;
        this.reqParam = new ItReqParam();
    }

    /**
     * 检查当前导入支持哪些功能.
     * 如果(节点ID为空)或(节点ID不为空，且如果不是第一个节点)，则只支持导入历史数据，
     * 如果节点ID不为空，且是第一个节点，则支持数据和附件导入，
     * @param impVo
     * @return ImpVo
     */
    public static ItOper check(ItOper impVo){
        if((impVo.getReqParam() != null) && StringUtil.isNotEmpty(impVo.getReqParam().getGnodeId())){
            if((impVo.getFirst() != null) && ((impVo.getReqParam() != null) && (impVo.getReqParam().getGnodeId()).equals(impVo.getFirst().getId()) || ((impVo.getReqParam() != null) && (impVo.getReqParam().getGnodeId()).equals(impVo.getFirst().getParentId())))){
                impVo.setIsFirstTask(true);
            }else{
                impVo.setIsFirstTask(false);
            }
        }

        impVo.isImpHisData = true;
        impVo.isImpFirstData = (impVo.getIsFirstTask() == null) ? false : impVo.getIsFirstTask();
//        impVo.isImpFileData = ((impVo.getIsImpFirstData() == null) || (impVo.getIsImpFirstData() == false)) ? false : true;
        impVo.isImpFileData = true;
        return impVo;
    }

    public void setIsImpHisData(Boolean isImpHisData) {
        this.isImpHisData = isImpHisData;
    }
    public void setIsImpFirstData(Boolean isImpFirstData) {
        this.isImpFirstData = isImpFirstData;
    }
    public void setIsImpFileData(Boolean isImpFileData) {
        this.isImpFileData = isImpFileData;
    }
    public ActYwGnode getFirst() {
        return first;
    }

    public Boolean getHasFile() {
        return hasFile;
    }

    public void setHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
    }

    public String getActywId() {
        return actywId;
    }

    public void setActywId(String actywId) {
        this.actywId = actywId;
    }

    public ItReqParam getReqParam() {
        return reqParam;
    }

    public void setReqParam(ItReqParam reqParam) {
        this.reqParam = reqParam;
    }

    public void setFirst(ActYwGnode first) {
        this.first = first;
    }

    public Boolean getIsFirstTask() {
        if(this.isFirstTask == null){
            this.isFirstTask = false;
        }
        return isFirstTask;
    }

    public void setIsFirstTask(Boolean isFirstTask) {
        this.isFirstTask = isFirstTask;
    }

    public Boolean getIsImpHisData() {
        if(this.isImpHisData == null){
            this.isImpHisData = false;
        }
        return isImpHisData;
    }
    public Boolean getIsImpFirstData() {
        if(this.isImpFirstData == null){
            this.isImpFirstData = false;
        }
        return isImpFirstData;
    }
    public Boolean getIsImpFileData() {
        if(this.isImpFileData == null){
            this.isImpFileData = false;
        }
        return isImpFileData;
    }
}
