package com.oseasy.pro.modules.workflow.entity;

import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProModelMdGcHistory;
import com.oseasy.pro.modules.promodel.vo.GgjBusInfo;
import com.oseasy.pro.modules.promodel.vo.GgjStudent;
import com.oseasy.pro.modules.promodel.vo.GgjTeacher;
import com.oseasy.pro.modules.workflow.IWorkDaoety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;
import com.oseasy.util.common.utils.StringUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * 互联网+大赛模板Entity.
 * @author zy
 * @version 2018-06-05
 */
public class ProvinceProModelGc extends WorkFetyPm<ProvinceProModelGc> implements IWorkRes, IWorkDaoety{

	private static final long serialVersionUID = 1L;
	private ProModel proModel;		// model_id
	private String modelId;		// model_id
	private String type;		    // 赛制 （0000000154校赛,0000000159省赛0000000162国赛）
	private String result;		    // 当前审核结果
    private String members;     // 团队成员及学号
    private String teachers;        // 指导教师
    private String businfos;        // 工商信息
	private List<ProModelMdGcHistory> proModelMdGcHistoryList;		    // 获奖集
	private ProModelMdGcHistory proModelMdGcHistory;                    // 获奖结果

	@Transient
    private List<GgjStudent> gmembers;     // 团队成员及学号
	@Transient
    private List<GgjTeacher> gteachers;     // 指导教师
	@Transient
    private List<GgjBusInfo> gbusinfos;     // 工商信息

	private String actywId;

	private String proNumber;

	public String getProNumber() {
		return proNumber;
	}

	public void setProNumber(String proNumber) {
		this.proNumber = proNumber;
	}

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public ProvinceProModelGc() {
		super();
	}

	public ProvinceProModelGc(String id){
		super(id);
	}

	public ProvinceProModelGc(List<String> ids) {
        super(ids);
    }
    public List<GgjStudent> getGmembers() {
        return gmembers;
    }

    public void setGmembers(List<GgjStudent> gmembers) {
        this.gmembers = gmembers;
    }

    public List<GgjTeacher> getGteachers() {
        return gteachers;
    }

    public void setGteachers(List<GgjTeacher> gteachers) {
        this.gteachers = gteachers;
    }

    public List<GgjBusInfo> getGbusinfos() {
        return gbusinfos;
    }

    public void setGbusinfos(List<GgjBusInfo> gbusinfos) {
        this.gbusinfos = gbusinfos;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
        if(StringUtil.isNotEmpty(this.members)){
            this.gmembers = GgjStudent.mtoGgjStudents(this.members);
        }
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
        if(StringUtil.isNotEmpty(this.teachers)){
            this.gteachers = GgjTeacher.mtoGgjTeachers(this.teachers);
        }
    }

    public String getBusinfos() {
        return businfos;
    }

    public void setBusinfos(String businfos) {
        this.businfos = businfos;
        if(StringUtil.isNotEmpty(this.businfos)){
            this.gbusinfos = GgjBusInfo.mtoGgjBusInfo(this.businfos);
        }
    }

    public ProvinceProModelGc(List<String> ids, String actYwId) {
        super(ids, actYwId);
    }
    public List<ProModelMdGcHistory> getProModelMdGcHistoryList() {
		return proModelMdGcHistoryList;
	}

	public void setProModelMdGcHistoryList(List<ProModelMdGcHistory> proModelMdGcHistoryList) {
		this.proModelMdGcHistoryList = proModelMdGcHistoryList;
	}

	public ProModelMdGcHistory getProModelMdGcHistory() {
		return proModelMdGcHistory;
	}

	public void setProModelMdGcHistory(ProModelMdGcHistory proModelMdGcHistory) {
		this.proModelMdGcHistory = proModelMdGcHistory;
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

	@Length(min=0, max=64, message="赛制 （1校赛,2省赛3国赛）长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=255, message="审核结果长度必须介于 0 和 255 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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