package com.oseasy.pw.modules.pw.entity;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻申报关联Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwEnterRel extends DataEntity<PwEnterRel> {
  public static final String PER_ID = "id";
  public static final String PER_EID = "eid";
  public static final String PER_RID = "rid";
  public static final String PER_AYID = "ayid";

  private static final long serialVersionUID = 1L;
  @Transient
  private PwEnterRoom eroom;    // 房间
  private PwEnterDetail pwEnterDetail;    // 申报编号
	private ActYwApply actYwApply;		// 流程申报编号

	public PwEnterRel() {
		super();
	}

	public PwEnterRel(String id){
		super(id);
	}

	public PwEnterDetail getPwEnterDetail() {
    return pwEnterDetail;
  }

  public void setPwEnterDetail(PwEnterDetail pwEnterDetail) {
    this.pwEnterDetail = pwEnterDetail;
  }

  public PwEnterRoom getEroom() {
    return eroom;
  }

  public void setEroom(PwEnterRoom eroom) {
    this.eroom = eroom;
  }

  public ActYwApply getActYwApply() {
    return actYwApply;
  }

  public void setActYwApply(ActYwApply actYwApply) {
    this.actYwApply = actYwApply;
  }

  public static List<String> getIds(List<PwEnterRel> pwEnterRels, String type) {
    List<String> ids = Lists.newArrayList();
    if((pwEnterRels == null)){
      return ids;
    }

//    for (PwEnterRel pweRel : pwEnterRels) {
//      if(PER_ID.equals(type)){
//        ids.add(pweRel.getId());
//      }else if((PER_EID).equals(type) && (pweRel.getPwEnterDetail() != null) && (pweRel.getPwEnterDetail().getPwEnter() != null)){
//        ids.add(pweRel.getPwEnterDetail().getPwEnter().getId());
//      }else if((PER_AYID).equals(type) && (pweRel.getActYwApply() != null)){
//        ids.add(pweRel.getActYwApply().getId());
//      }
//    }
    return ids;
  }

  public static List<PwEnterRel> addPwRoom(List<PwEnterRel> pwEnterRels, List<PwEnterRoom> pwEnterRooms) {
    for (PwEnterRel pweRel : pwEnterRels) {
//      if((pweRel == null) || (pweRel.getPwEnterDetail() == null) || (pweRel.getPwEnterDetail().getPwEnter() == null)){
//        continue;
//      }

//      if(StringUtil.isEmpty(pweRel.getPwEnterDetail().getPwEnter().getId())){
//        continue;
//      }

      for (PwEnterRoom pwEnterRoom : pwEnterRooms) {
        if((pwEnterRoom.getPwEnter() == null)){
          continue;
        }

        if(StringUtil.isEmpty(pwEnterRoom.getPwEnter().getId())){
          continue;
        }

//        if((pweRel.getPwEnterDetail().getPwEnter().getId()).equals(pwEnterRoom.getPwEnter().getId())){
//          pweRel.setEroom(new PwEnterRoom(pweRel.getPwEnterDetail().getPwEnter(), pwEnterRoom.getPwRoom()));
//        }
      }
    }
    return pwEnterRels;
  }
}