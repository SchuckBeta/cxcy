package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 业务指派表Entity.
 * @author zy
 * @version 2018-04-03
 */
public class ActYwGassign extends DataEntity<ActYwGassign> {

	private static final long serialVersionUID = 1L;
	private String ywId;		// 项目流程编号
	private String gnodeId;		// 流程节点编号
	private String promodelId;		// 项目id
	private String assignUserId;		// 指派人
	private String revUserId;		// 被指派人
	private String isOver;		// 是否完成 0：未完成 1：完成
	private String type;		//任务类型：0：委派 1：指派 EarAtype

    private User assignUser;        // 指派人
    private User revUser;       // 被指派人

	public String getIsOver() {
		return isOver;
	}

	public void setIsOver(String isOver) {
		this.isOver = isOver;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ActYwGassign() {
		super();
	}

	public ActYwGassign(String id){
		super(id);
	}

	public User getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(User assignUser) {
        this.assignUser = assignUser;
    }

    public User getRevUser() {
        return revUser;
    }

    public void setRevUser(User revUser) {
        this.revUser = revUser;
    }

    @Length(min=1, max=64, message="项目流程编号长度必须介于 1 和 64 之间")
	public String getYwId() {
		return ywId;
	}

	public void setYwId(String ywId) {
		this.ywId = ywId;
	}

	@Length(min=0, max=64, message="流程节点编号长度必须介于 0 和 64 之间")
	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public String getPromodelId() {
		return promodelId;
	}

	public void setPromodelId(String promodelId) {
		this.promodelId = promodelId;
	}

	@Length(min=0, max=64, message="指派人长度必须介于 0 和 64 之间")
	public String getAssignUserId() {
		return assignUserId;
	}

	public void setAssignUserId(String assignUserId) {
		this.assignUserId = assignUserId;
	}

	@Length(min=0, max=64, message="被指派人长度必须介于 0 和 64 之间")
	public String getRevUserId() {
		return revUserId;
	}

	public void setRevUserId(String revUserId) {
		this.revUserId = revUserId;
	}

	/**
	 * 初始化Type.
	 * @param gassign
	 * @param curGnode 当前节点
	 * @return ActYwGassign
	 */
	public static ActYwGassign initType(ActYwGnode curGnode) {
	    return initType(new ActYwGassign(), curGnode);
	}
    public static ActYwGassign initType(ActYwGassign gassign, ActYwGnode curGnode) {
        if((curGnode == null)){
            return gassign;
        }
        if(curGnode.getIsAssign()){
            gassign.setType(EarAtype.ZP.getKey());
        }else if(curGnode.getIsDelegate()){
            gassign.setType(EarAtype.WP.getKey());
        }
        return gassign;
    }

}