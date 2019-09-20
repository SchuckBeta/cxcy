package com.oseasy.act.modules.actyw.tool.project;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.pro.entity.ProProject;

/**
 * Created by Administrator on 2017/7/29 0029.
 */
public class ActProParamVo {

	private FlowType flowType;
	private ActYw actYw;
	private IApply apply;
	private ProProject proProject;

	public ActProParamVo() {
        super();
    }

    public ActProParamVo(ActYw actYw) {
        super();
        this.actYw = actYw;
    }

    public ActProParamVo(FlowType flowType) {
        super();
        this.flowType = flowType;
    }

    public ProProject getProProject() {
		return proProject;
	}

	public void setProProject(ProProject proProject) {
		this.proProject = proProject;
	}

	public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType(FlowType flowType) {
        this.flowType = flowType;
    }

    public ActYw getActYw() {
		return actYw;
	}

	public void setActYw(ActYw actYw) {
		this.actYw = actYw;
	}

    public IApply getApply() {
        return apply;
    }

    public void setApply(IApply apply) {
        this.apply = apply;
    }

    public static ActProParamVo delFlowType(ActProParamVo paramVo) {
        if((paramVo == null) || (paramVo.getActYw() == null) || (paramVo.getActYw().getGroup() == null)){
            return paramVo;
        }

        if ((paramVo.getActYw().getGroup() != null) && (paramVo.getActYw().getGroup().getFlowType() != null)) {
            paramVo.setFlowType(FlowType.getByKey(paramVo.getActYw().getGroup().getFlowType()));
        }

        return paramVo;
    }
}
