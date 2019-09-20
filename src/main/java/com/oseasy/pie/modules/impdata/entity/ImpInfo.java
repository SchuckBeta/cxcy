package com.oseasy.pie.modules.impdata.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.impdata.tool.IitCheckInfo;

/**
 * 导入数据信息表Entity
 * @author 9527
 * @version 2017-05-16
 */
public class ImpInfo extends DataEntity<ImpInfo> implements IitCheckInfo{

	private static final long serialVersionUID = 1L;
	private String actywid;
	private String iepType;//大类型
	private String protype;//大类型
	private String prosubtype;//小类型
	private String impTpye;		// 导入数据的类型
	private String total;		// 总数
	private String success;		// 成功数
	private String fail;		// 失败数
	private String isComplete;		// 是否结束：0-未结束，1-结束
	private String msg;//存储文件名称等信息
	private String filename;//文件名称
	private String errmsg;//文件级别错误信息

	@JsonIgnore
    private List<String> iepTypes;//大类型
	public ImpInfo() {
		super();
	}

	public String getActywid() {
		return actywid;
	}

	public void setActywid(String actywid) {
		this.actywid = actywid;
	}

	public String getProtype() {
		return protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	public String getProsubtype() {
		return prosubtype;
	}

	public void setProsubtype(String prosubtype) {
		this.prosubtype = prosubtype;
	}

	@JsonIgnore
	public List<String> getIepTypes() {
        return iepTypes;
    }

    public void setIepTypes(List<String> iepTypes) {
        this.iepTypes = iepTypes;
    }

    public ImpInfo(String id) {
		super(id);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Length(min=0, max=64, message="导入数据的类型长度必须介于 0 和 64 之间")
	public String getImpTpye() {
		return impTpye;
	}

	public void setImpTpye(String impTpye) {
		this.impTpye = impTpye;
	}

	@Length(min=0, max=20, message="总数长度必须介于 0 和 20 之间")
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Length(min=0, max=20, message="成功数长度必须介于 0 和 20 之间")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@Length(min=0, max=20, message="失败数长度必须介于 0 和 20 之间")
	public String getFail() {
		return fail;
	}

	public void setFail(String fail) {
		this.fail = fail;
	}

	@Length(min=0, max=2, message="是否结束：0-未结束，1-结束长度必须介于 0 和 2 之间")
	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getIepType() {
        return iepType;
    }

    public void setIepType(String iepType) {
        this.iepType = iepType;
    }

    /**
     * 根据自定义流程生成ImpInfo对象.
     */
	public static ImpInfo genImpInfo(ActYw ay, String fileName, String impTpye) {
        ImpInfo ii = new ImpInfo();
        ii.setActywid(ay.getId());
        ii.setProtype(ay.getProProject().getProType());
        ii.setProsubtype(ay.getProProject().getType());
        ii.setImpTpye(impTpye);
        ii.setTotal("0");
        ii.setFail("0");
        ii.setSuccess("0");
        ii.setIsComplete(Const.NO);
        ii.setFilename(fileName);
        return ii;
    }
	/**
	 * 根据自定义流程生成ImpInfo对象.
	 */
	public static ImpInfo genImpInfo(IepTpl iepTpl, ActYw ay, String fileName) {
	    ImpInfo ii = new ImpInfo();
	    ii.setIepType(iepTpl.getType());
	    ii.setImpTpye(iepTpl.getId());
	    ii.setActywid(ay.getId());
	    ii.setTotal("0");
	    ii.setFail("0");
	    ii.setSuccess("0");
	    ii.setIsComplete(Const.NO);
	    ii.setFilename(fileName);
	    return ii;
	}

	/**
	 * 非自定义流程生成ImpInfo对象.
	 */
	public static ImpInfo genImpInfo(String fileName, String impTpye) {
	    ImpInfo ii = new ImpInfo();
	    ii.setImpTpye(impTpye);
	    ii.setTotal("0");
	    ii.setFail("0");
	    ii.setSuccess("0");
	    ii.setIsComplete(Const.NO);
	    ii.setFilename(fileName);
	    return ii;
	}

	/**
	 * 非自定义流程生成ImpInfo对象.
	 */
	public static ImpInfo genImpInfo(String fileName, String impTpye, String total) {
	    ImpInfo ii = new ImpInfo();
	    ii.setImpTpye(impTpye);
	    ii.setTotal("0");
	    ii.setFail("0");
	    ii.setSuccess("0");
	    ii.setIsComplete(Const.NO);
	    ii.setFilename(fileName);
	    return ii;
	}
}