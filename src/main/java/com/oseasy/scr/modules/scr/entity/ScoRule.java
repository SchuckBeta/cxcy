package com.oseasy.scr.modules.scr.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.TreeExtEntity;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.scr.modules.scr.vo.IScoObj;

/**
 * 学分规则Entity.
 * @author chenh
 * @version 2018-12-21
 */
public class ScoRule extends TreeExtEntity<ScoRule> implements IScoObj {


	private static final long serialVersionUID = 1L;
    public static final String TABLEA = "a.";
    public static final int TYPE = 0;

	private ScoRule parent;		// 父级编号
	private String parentIds;		// 所有父级编号
	private String name;		// 名称
	private String type;		// 级别：1：类型 ;2：类别（级别）;3、子类别（子级别）
	private String ptype;		// 认定形式：1：个人 2：团队
	private Boolean isPb;		// 是否设置配比：0：否 1：是
	private String isShow;		// 是否显示：0、不显示；1、显示
	private ScoRuleDetail rdetail;     // 申请标准详情ID（标准具体描述规则ID）
	private List children;
	private String level; //层级

	private ScoRuleDetailMould scoRuleDetailMould; //学分规则标准模板

	public ScoRuleDetailMould getScoRuleDetailMould() {
		return scoRuleDetailMould;
	}

	public void setScoRuleDetailMould(ScoRuleDetailMould scoRuleDetailMould) {
		this.scoRuleDetailMould = scoRuleDetailMould;
	}

	public ScoRule() {
		super();
	}
	public ScoRule(String id){
		super(id);
	}

	public ScoRule(String tenantId,String delFlag){
		this.tenantId=tenantId;
		this.delFlag=delFlag;
	}

	public ScoRule(String id,ScoRule parent,String parentIds,String name,String type,String ptype,String tenantId,String delFlag){
		super(id);
		this.parent=parent;
		this.parentIds=parentIds;
		this.name=name;
		this.type=type;
		this.ptype=ptype;
		this.tenantId=tenantId;
		this.delFlag=delFlag;
	}

	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public ScoRule getParent() {
		return parent;
	}

	public void setParent(ScoRule parent) {
		this.parent = parent;
	}

	@Length(min=1, max=2000, message="所有父级编号长度必须介于 1 和 2000 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=100, message="名称长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=1, message="级别：1：类型 ;2：类别（级别）;3、子类别（子级别）长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=1, message="认定形式：1：个人 2：团队长度必须介于 0 和 1 之间")
	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}


	public Boolean getIsPb() {
	    if(this.isPb == null){
	        this.isPb = false;
	    }
		return isPb;
	}

	public void setIsPb(Boolean isPb) {
		this.isPb = isPb;
	}


	@Length(min=0, max=1, message="是否显示：0、不显示；1、显示长度必须介于 0 和 1 之间")
	public String getIsShow() {
	    if(isShow == null){
            this.isShow = Const.SHOW;
        }
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

    public ScoRuleDetail getRdetail() {
        return rdetail;
    }

    public void setRdetail(ScoRuleDetail rdetail) {
        this.rdetail = rdetail;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : CoreIds.NCE_SYS_TREE_PROOT.getId();
    }

	public List getChildren() {
		return children;
	}

	@JsonIgnore
	public List<ScoRule> genCrules() {
		return (List<ScoRule>) children;
	}

	@JsonIgnore
	public List<ScoRuleDetail> genCruleDetails() {
		return (List<ScoRuleDetail>) children;
	}



	public void setChildren(List children) {
		this.children = children;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@JsonIgnore
    public static void sortList(List<Menu> list, List<Menu> sourcelist, String parentId, boolean cascade) {
        for (int i=0; i<sourcelist.size(); i++) {
            Menu e = sourcelist.get(i);
            if (e.getParent()!=null && e.getParent().getId()!=null
                    && e.getParent().getId().equals(parentId)) {
                list.add(e);
                if (cascade) {
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (int j=0; j<sourcelist.size(); j++) {
                        Menu child = sourcelist.get(j);
                        if (child.getParent()!=null && child.getParent().getId()!=null
                                && child.getParent().getId().equals(e.getId())) {
                            sortList(list, sourcelist, e.getId(), true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    public static String getRootId() {
    return CoreIds.NCE_SYS_TREE_ROOT.getId();
    }

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}