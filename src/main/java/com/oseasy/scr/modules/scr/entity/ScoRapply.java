package com.oseasy.scr.modules.scr.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IAmap;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.apply.IAprop;
import com.oseasy.act.modules.actyw.tool.apply.IAsup;
import com.oseasy.act.modules.actyw.tool.apply.IAurl;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.IAuser;
import com.oseasy.scr.modules.scr.vo.ScoRaurl;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分申请Entity.
 * @author chenh
 * @version 2018-12-21
 */
public class ScoRapply extends DataExtEntity<ScoRapply> implements IApply, IidEntity{

	private static final long serialVersionUID = 1L;
    public static final String TABLEA = "a.";


    private User user;      // 申请人ID
	private StudentExpansion studentExpansion; //学分信息
	private ScoRule top;		// 申请标准（学分类型、一级）ID
	private ScoRule rule;		// 申请标准（学分类别、二级、表名）ID-冗余便于审核通过时生成查询统计
	private ScoRule lrule;		// 申请标准ID（标准子类别、最后一级）
	private ScoRuleDetail rdetail;		// 申请标准详情ID（标准具体描述规则ID）
	private String procInsId;		// 流程实例ID
	private String name;		// 名称
	private String status;		// 状态:0、待审核 1、通过;2、未通过
	private String isFirst;		// 是否临时数据:0、否；1、是
	private String isTemp;		// 是否临时数据:0、否；1、是
	private boolean iend;		// 是否结束:false、否(默认)；true、是
	private String condVal;		// 值
	private Date certDate;		// 证书获得日期
	private Date applyDate;		// 申请日期
	private Date autDate;		// 审核日期
	private User autBy;		// 审核人
	private ScoRapplyCert scoRapplyCert; //证书
	private List<ScoRapplyMember> scoRapplyMemberList;//申请成员list
	private List<SysAttachment> sysAttachmentList; //附件
	private List<ScoRapplyRecord> scoRapplyRecordList; // 审核记录

	private Double tval;       // 值
	private Double tcval;       // 值
	private String appIds;       //相同项目IDS(第一次申请的ID)

	private Date certQDate;      // 证书获得日期（查询使用）
    private Date applyQDate;     // 申请日期（查询使用）
    private Date autQDate;       // 审核日期（查询使用）
    protected Date createQDate;  // 创建日期（查询使用）
    private String keys; // 关键字（查询使用）
    @JsonIgnore
    @Transient
    private List<String> uids;       // 查询Ids
    @Transient
    private List<ScoRapply> apps;       //相同项目IDS(第一次申请的ID)

    private ActYw actYw; //流程
    @JsonIgnore
    private ActYwGnode gnode; //流程审核节点
    private String ignodeId; //流程审核节点
    private IAsup iasup; //流程审核节点传递参数
    private IAmap iamap; //流程审核节点Act属性
    private IAprop iaprop; //流程审核节点Act属性

    public HashMap<String,Object> getVars() {
        HashMap<String,Object> vars=new HashMap<String, Object>();
        vars.put(CoreJkey.JK_ID, this.id);  //id
        vars.put(ActYwGroup.ACT_YW_ID, (this.actYw != null) ? this.actYw.getId(): "");             //项目评级
        vars.put("uid", (this.user != null) ? this.user.getId(): "");     //大赛申报人id
        return vars;
    }

    public void setIamap(IAmap iamap) {
        this.iamap = iamap;
    }

    public ActYw getActYw() {
        return actYw;
    }

    public void setActYw(ActYw actYw) {
        this.actYw = actYw;
    }

    public List<ScoRapply> getApps() {
        if(this.apps == null){
            this.apps = Lists.newArrayList();
        }
        return apps;
    }

    public void setApps(List<ScoRapply> apps) {
        this.apps = apps;
    }

    public List<String> getUids() {
        return uids;
    }

    public boolean isIend() {
        return iend;
    }

    public void setIend(boolean iend) {
        this.iend = iend;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    @JsonIgnore
    @Transient
    public ActYwGnode getGnode() {
        return gnode;
    }

    @JsonIgnore
    @Transient
    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }

    public StudentExpansion getStudentExpansion() {
		return studentExpansion;
	}

	public void setStudentExpansion(StudentExpansion studentExpansion) {
		this.studentExpansion = studentExpansion;
	}

	public List<ScoRapplyRecord> getScoRapplyRecordList() {
		return scoRapplyRecordList;
	}

	public void setScoRapplyRecordList(List<ScoRapplyRecord> scoRapplyRecordList) {
		this.scoRapplyRecordList = scoRapplyRecordList;
	}

	public List<SysAttachment> getSysAttachmentList() {
		return sysAttachmentList;
	}

	public void setSysAttachmentList(List<SysAttachment> sysAttachmentList) {
		this.sysAttachmentList = sysAttachmentList;
	}

	public ScoRapplyCert getScoRapplyCert() {
		return scoRapplyCert;
	}

	public void setScoRapplyCert(ScoRapplyCert scoRapplyCert) {
		this.scoRapplyCert = scoRapplyCert;
	}

	public List<ScoRapplyMember> getScoRapplyMemberList() {
		return scoRapplyMemberList;
	}

	public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getAppIds() {
        return appIds;
    }

    public void setAppIds(String appIds) {
        this.appIds = appIds;
    }

    public void setScoRapplyMemberList(List<ScoRapplyMember> scoRapplyMemberList) {
		this.scoRapplyMemberList = scoRapplyMemberList;
	}

	public ScoRapply() {
		super();
        this.iactYw(new ActYw());
        this.ignode(new ActYwGnode());
        this.iactYw().group(new ActYwGroup());
        this.iactYw().config(new ProProject());
	}

    public ScoRapply(String id){
		super(id);
	}
	public ScoRapply(List<String> ids){
	    this.ids = ids;
	}

	@Length(min=0, max=64, message="流程实例ID长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	@Length(min=0, max=64, message="名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=1, message="状态:0、待审核 1、通过;2、未通过长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=1, max=1, message="是否临时数据:0、否；1、是长度必须介于 1 和 1 之间")
	public String getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(String isTemp) {
		this.isTemp = isTemp;
	}

	public Double getTcval() {
        return tcval;
    }

    public void setTcval(Double tcval) {
        this.tcval = tcval;
    }

    public Double getTval() {
        return tval;
    }

    public void setTval(Double tval) {
        this.tval = tval;
    }

    @Length(min=1, max=64, message="值长度必须介于 1 和 64 之间")
	public String getCondVal() {
		return condVal;
	}

	public void setCondVal(String condVal) {
		this.condVal = condVal;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCertDate() {
		return certDate;
	}

	public void setCertDate(Date certDate) {
		this.certDate = certDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAutDate() {
		return autDate;
	}

	public void setAutDate(Date autDate) {
		this.autDate = autDate;
	}

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ScoRule getTop() {
        return top;
    }

    public void setTop(ScoRule top) {
        this.top = top;
    }

    public ScoRule getRule() {
        return rule;
    }

    public void setRule(ScoRule rule) {
        this.rule = rule;
    }

    public ScoRule getLrule() {
        return lrule;
    }

    public void setLrule(ScoRule lrule) {
        this.lrule = lrule;
    }

    public ScoRuleDetail getRdetail() {
        return rdetail;
    }

    public void setRdetail(ScoRuleDetail rdetail) {
        this.rdetail = rdetail;
    }

    public User getAutBy() {
        return autBy;
    }

    public void setAutBy(User autBy) {
        this.autBy = autBy;
    }

    @Transient
    public Date getCertQDate() {
        return certQDate;
    }

    @Transient
    public void setCertQDate(Date certQDate) {
        this.certQDate = certQDate;
    }

    @Transient
    public Date getApplyQDate() {
        return applyQDate;
    }

    @Transient
    public void setApplyQDate(Date applyQDate) {
        this.applyQDate = applyQDate;
    }

    @Transient
    public Date getCreateQDate() {
        return createQDate;
    }

    public void setCreateQDate(Date createQDate) {
        this.createQDate = createQDate;
    }

    @Transient
    public Date getAutQDate() {
        return autQDate;
    }

    @Transient
    public void setAutQDate(Date autQDate) {
        this.autQDate = autQDate;
    }

    @Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}



    /**********************************************************************************
     * IApply流程接口实现方法
     **********************************************************************************/
    @Override
    public String getIactYwId() {
        if(this.actYw != null){
            return this.actYw.getId();
        }
        return null;
    }

    @Override
    public IAsup iasup() {
        if(this.iasup == null){
            this.iasup = new IAsup();
        }
        return this.iasup;
    }

    @Override
    public IAsup iasup(IAsup iasup) {
        this.iasup = iasup;
        return this.iasup;
    }

    @Override
    public IAprop iaprop() {
        if(this.iaprop == null){
            this.iaprop = new IAprop();
        }
        return this.iaprop;
    }

    @Override
    public IAprop iaprop(IAprop iaprop) {
        if(this.iaprop == null){
            this.iaprop = new IAprop();
        }
        return this.iaprop;
    }

    @Override
    public IAmap getIamap() {
        if(this.iamap == null){
            this.iamap = new IAmap();
        }
        return this.iamap;
    }

    @Override
    public IAmap iamap(IAmap iamap) {
        this.iamap = iamap;
        return this.iamap;
    }

    @JsonIgnore
    @Override
    public IActYw iactYw() {
        if(this.actYw == null){
            this.actYw = new ActYw();
        }
        return this.actYw;
    }

    @JsonIgnore
    @Override
    public ActYw iactYw(IActYw actYw) {
        this.actYw = (ActYw) actYw;
        return this.actYw;
    }

    @JsonIgnore
    @Override
    public User iauser() {
        return this.user;
    }

    @JsonIgnore
    @Override
    public String iauserId() {
        if(this.user != null){
            return this.user.getId();
        }
        return null;
    }

    @JsonIgnore
    @Override
    public User iauser(IAuser auser) {
        this.user = (User) auser;
        return this.user;
    }

    @Override
    public IAurl getIaurl() {
        return new ScoRaurl();
    }

    @Override
    public String getIid() {
        return this.id;
    }

    @JsonIgnore
    @Override
    public String iid(String iid) {
        this.id = iid;
        return this.id;
    }

    @Override
    public String getIname() {
        return this.name;
    }

    @JsonIgnore
    @Override
    public IGnode ignode() {
        return this.gnode;
    }

    @JsonIgnore
    @Override
    public ActYwGnode ignode(IGnode gnode) {
        this.gnode = (ActYwGnode) gnode;
        return this.gnode;
    }

    public void setIgnodeId(String ignodeId) {
        this.ignodeId = ignodeId;
    }

    public String getIgnodeId() {
        if((this.gnode != null) && StringUtil.isEmpty(this.ignodeId)){
            this.ignodeId = this.gnode.getId();
        }
        return this.ignodeId;
    }

    @Override
    public String getIprocInsId() {
        return this.procInsId;
    }

    @JsonIgnore
    @Override
    public String iprocInsId(String piid) {
        this.procInsId = piid;
        return this.procInsId;
    }

    @JsonIgnore
    @Override
    public Map<String, Object> ivars() {
        return getVars();
    }

    @Override
    public Map<String, String> ors() {
        Map<String, String> orby = Maps.newHashMap();
        orby.put("user.no", "u.no");
        orby.put("top.id", "a.rtid");
        orby.put("rule.name", "a.rpid");
        orby.put("lrule.name", "a.rid");
        orby.put("rdetail.name", "a.rdid");
        orby.put("applyDate", "a.apply_date");
        orby.put("status", "a.status");
        return orby;
    }
}