package com.oseasy.pw.modules.pw.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.modules.pw.vo.PwEroom;
import com.oseasy.pw.modules.pw.vo.PwEroomMap;
import com.oseasy.pw.modules.pw.vo.PwEroomParam;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻场地分配Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwEnterRoom extends DataEntity<PwEnterRoom> {

	private static final long serialVersionUID = 1L;
	private PwEnter pwEnter;		// 入驻编号
	private PwRoom pwRoom;		// 房间编号
	@Transient
	private Integer cnum;		//数量
	private Integer num;		//入驻工位数

	public PwEnterRoom() {
    super();
    this.num = 0;
  }

	public PwEnterRoom(PwRoom pwRoom) {
    super();
    this.pwRoom = pwRoom;
    this.num = 0;
    this.createBy = CoreUtils.getUser();
    this.createDate = new Date();
    this.updateBy = CoreUtils.getUser();
    this.updateDate = new Date();
    this.delFlag = Const.NO;
  }

  public PwEnterRoom(String rid, String eid) {
	  super();
	  this.pwEnter = new PwEnter(eid);
	  this.pwRoom = new PwRoom(rid);
      this.num = 0;
      this.createBy = CoreUtils.getUser();
      this.createDate = new Date();
      this.updateBy = CoreUtils.getUser();
      this.updateDate = new Date();
      this.delFlag = Const.NO;
	}
  public PwEnterRoom(String id, String rid, String eid) {
      super();
      this.id = id;
      this.pwEnter = new PwEnter(eid);
      this.pwRoom = new PwRoom(rid);
      this.num = 0;
      this.createBy = CoreUtils.getUser();
      this.createDate = new Date();
      this.updateBy = CoreUtils.getUser();
      this.updateDate = new Date();
      this.delFlag = Const.NO;
  }

  public PwEnterRoom(String id, String rid, String eid, Integer num) {
      super();
      this.id = id;
      this.pwEnter = new PwEnter(eid);
      this.pwRoom = new PwRoom(rid);
      this.num = num;
      this.createBy = CoreUtils.getUser();
      this.createDate = new Date();
      this.updateBy = CoreUtils.getUser();
      this.updateDate = new Date();
      this.delFlag = Const.NO;
  }

	public PwEnterRoom(PwEnter pwEnter) {
	  super();
	  this.pwEnter = pwEnter;
	}

	public PwEnterRoom(PwEnter pwEnter, PwRoom pwRoom) {
	  super();
	  this.pwEnter = pwEnter;
	  this.pwRoom = pwRoom;
	}

	public PwEnterRoom(String id){
		super(id);
	}

  public PwEnter getPwEnter() {
    return pwEnter;
  }

  public void setPwEnter(PwEnter pwEnter) {
    this.pwEnter = pwEnter;
  }

  public PwRoom getPwRoom() {
    return pwRoom;
  }

  public void setPwRoom(PwRoom pwRoom) {
    this.pwRoom = pwRoom;
  }

  public Integer getCnum() {
    return cnum;
  }

  public Integer getNum() {
    return num;
}

public void setNum(Integer num) {
    this.num = num;
}

public void setCnum(Integer cnum) {
    this.cnum = cnum;
  }

  public static List<String> getIds(List<PwEnterRoom> pwEnterRooms) {
    return getIds(pwEnterRooms, PwEnterRel.PER_ID);
  }


  public static List<String> getIds(List<PwEnterRoom> pwEnterRooms, String type) {
    List<String> ids = Lists.newArrayList();
    if((pwEnterRooms == null)){
      return ids;
    }

    for (PwEnterRoom pwer : pwEnterRooms) {
      if((PwEnterRel.PER_ID).equals(type)){
        ids.add(pwer.getId());
      }else if((PwEnterRel.PER_EID).equals(type) && (pwer.getPwEnter() != null) && StringUtil.isNotEmpty(pwer.getPwEnter().getId())){
        ids.add(pwer.getPwEnter().getId());
      }else if((PwEnterRel.PER_RID).equals(type) && (pwer.getPwRoom() != null) && StringUtil.isNotEmpty(pwer.getPwRoom().getId())){
        ids.add(pwer.getPwRoom().getId());
      }
    }
    return ids;
  }

  public static PwEroomMap genByIds(PwEnterRoom entity) {
      if (((entity.getPwEnter() == null) || StringUtil.isNotEmpty(entity.getPwEnter().getId())) || ((entity.getPwRoom() == null) && StringUtil.checkEmpty(entity.getPwRoom().getIds()))) {
          return null;
      }
      PwEroomMap map = new PwEroomMap();
      List<PwEnterRoom> erooms = Lists.newArrayList();
      for (String rid : entity.getPwRoom().getIds()) {
          String curId = IdGen.uuid();
          map.getIds().add(curId);
          erooms.add(new PwEnterRoom(curId, rid, entity.getPwEnter().getId()));
      }
      map.setErooms(erooms);
      return map;
  }

  public static PwEroomMap genByIds(PwEroomParam entity) {
      if ((StringUtil.isEmpty(entity.getId())) || (StringUtil.checkEmpty(entity.getErooms()))) {
          return null;
      }
      PwEroomMap map = new PwEroomMap();
      List<PwEnterRoom> erooms = Lists.newArrayList();
      for (PwEroom eroom : entity.getErooms()) {
          String curId = IdGen.uuid();
          map.getIds().add(curId);
          erooms.add(new PwEnterRoom(curId, eroom.getId(), entity.getId(), eroom.getNum()));
      }
      map.setErooms(erooms);
      return map;
  }
}