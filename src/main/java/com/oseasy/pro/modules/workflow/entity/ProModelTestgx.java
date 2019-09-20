package com.oseasy.pro.modules.workflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.IWorkDaoety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;

import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * ProModelTestgxEntity.
 * @author zy
 * @version 2017-09-18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProModelTestgx extends WorkFetyPm<ProModelTestgx> implements IWorkRes, IWorkDaoety{

	private static final long serialVersionUID = 1L;
	public static final String NO_PASS = "0";
	private ProModel proModel;		// model_id
	private String modelId;		// model_id

	public ProModelTestgx() {
			super();
		}

	public ProModel getProModel() {
		return proModel;
	}

	public void setProModel(ProModel proModel) {
		this.proModel = proModel;
	}

	@Length(min=0, max=64, message="model_id长度必须介于 0 和 64 之间")
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}


    /* (non-Javadoc)
     * @see com.oseasy.pro.modules.workflow.IWorkRes#getOfficeName()
     */
    @Override
    public String getOfficeName() {
        if(this.getProModel() == null){
            return "";
        }
        return this.getProModel().getOfficeName();
    }
}