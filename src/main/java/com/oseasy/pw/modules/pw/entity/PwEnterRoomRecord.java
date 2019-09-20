package com.oseasy.pw.modules.pw.entity;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;


/**
 * 场地分配记录Entity.
 * @author chenh
 * @version 2018-12-10
 */
public class PwEnterRoomRecord extends DataExtEntity<PwEnterRoomRecord> {

	private static final long serialVersionUID = 1L;
	private String eid;		// 入驻编号
	private String rid;		// 房间编号
	private PwApplyRecord record; // 记录编号
	private String type;		// 操作类型
	private Boolean isExit;		// 是否退孵操作
	private Integer num;		// 入驻工位数

	public PwEnterRoomRecord() {
		super();
	}
	public PwEnterRoomRecord(PwEnterRoom pwEroom) {
	    super();
	    if(pwEroom.getPwEnter() != null){
	        this.eid = pwEroom.getPwEnter().getId();
	    }
	    if(pwEroom.getPwRoom() != null){
	        this.rid = pwEroom.getPwRoom().getId();
	    }
	    this.num = pwEroom.getNum();
	}

	public PwEnterRoomRecord(String id){
		super(id);
	}

	@Length(min=1, max=64, message="入驻编号长度必须介于 1 和 64 之间")
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public Boolean getIsExit() {
        return isExit;
    }
    public void setIsExit(Boolean isExit) {
        this.isExit = isExit;
    }
    @Length(min=1, max=64, message="房间编号长度必须介于 1 和 64 之间")
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public PwApplyRecord getRecord() {
        return record;
    }
    public void setRecord(PwApplyRecord record) {
        this.record = record;
    }

    @Length(min=0, max=11, message="操作类型长度必须介于 0 和 11 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=11, message="入驻工位数长度必须介于 0 和 11 之间")
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	/**
     * 入驻转入驻记录.
     */
	public static List<PwEnterRoomRecord> converts(String eid, List<PwEnterRoom> pwEnterRooms, PwApplyRecord pwArecord, String type) {
	    return converts(eid, pwEnterRooms, pwArecord, type, false);
	}
    public static List<PwEnterRoomRecord> converts(String eid, List<PwEnterRoom> pwEnterRooms, PwApplyRecord pwArecord, String type, Boolean isExit) {
        if(isExit == null){
            isExit = false;
        }
	    List<PwEnterRoomRecord> peroomRecords = Lists.newArrayList();
	    for (PwEnterRoom pwEnterRoom : pwEnterRooms) {
	        PwEnterRoomRecord record = new PwEnterRoomRecord(pwEnterRoom);
	        record.setIsNewRecord(true);
	        record.setEid(eid);
	        record.setId(IdGen.uuid());
	        record.setType(type);
	        record.setIsExit(isExit);
	        record.setRecord(pwArecord);
	        record.setCreateBy(UserUtils.getUser());
	        record.setCreateDate(new Date());
	        record.setUpdateBy(UserUtils.getUser());
	        record.setUpdateDate(new Date());
	        record.setDelFlag(Const.NO);
	        peroomRecords.add(record);
        }
	    return peroomRecords;
    }


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}