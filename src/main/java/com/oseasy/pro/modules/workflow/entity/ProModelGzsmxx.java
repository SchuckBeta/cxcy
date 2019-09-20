package com.oseasy.pro.modules.workflow.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.IWorkDaoety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;

/**
 * ProModelGzxmxxEntity.
 * @author zy
 * @version 2017-09-18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProModelGzsmxx extends WorkFetyPm<ProModelGzsmxx> implements IWorkRes, IWorkDaoety{

	private static final long serialVersionUID = 1L;
	public static final String NO_PASS = "0";
	private ProModel proModel;		// model_id
	private String modelId;		// model_id
	private String region;      //领域
	private String regionGroup;  //领域分组

	private String regionName;
	private String regionGroupName;

	public ProModelGzsmxx() {
		super();
	}

	public ProModelGzsmxx(List<String> ids) {
        super(ids);
    }

    public ProModelGzsmxx(List<String> ids, String actYwId) {
        super(ids, actYwId);
    }

    public ProModelGzsmxx(String id) {
		super(id);
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

	@Length(min=0, max=64, message="领域字段长度必须介于 0 和 64 之间")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Length(min=0, max=64, message="领域分组字段长度必须介于 0 和 64 之间")
	public String getRegionGroup() {
		return regionGroup;
	}

	public void setRegionGroup(String regionGroup) {
		this.regionGroup = regionGroup;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionGroupName() {
		return regionGroupName;
	}

	public void setRegionGroupName(String regionGroupName) {
		this.regionGroupName = regionGroupName;
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