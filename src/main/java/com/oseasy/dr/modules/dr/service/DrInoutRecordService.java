package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrCardRecordDao;
import com.oseasy.dr.modules.dr.dao.DrInoutRecordDao;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.entity.DrInoutRecord;
import com.oseasy.dr.modules.dr.vo.DrCardRecordVo;
import com.oseasy.dr.modules.dr.vo.DrInoutRecordVo;
import com.oseasy.dr.modules.dr.vo.GitemEstatus;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁卡出入记录Service.
 * @author 奔波儿灞
 * @version 2018-04-08
 */
@Service
@Transactional(readOnly = true)
public class DrInoutRecordService extends CrudService<DrInoutRecordDao, DrInoutRecord> {
	@Autowired
	private DrCardRecordDao drCardRecordDao;

	@Transactional(readOnly = false)
	public Boolean disposeDrCardRecord(DrCardRecordVo vo,int maxHour) {
		if((vo.getPcTime() == null) || StringUtil.isEmpty(vo.getGid()) || StringUtil.isEmpty(vo.getCardId()) || StringUtil.isEmpty(vo.getEptId()) || StringUtil.isEmpty(vo.getRspaceId())){
		    return false;
		}
		if((GitemEstatus.GES_ENTER.getKey()).equals(vo.getIsEnter())){//1进门打卡
			DrInoutRecord dir = new DrInoutRecord();
			dir.setGroup(new DrCardreGroup(vo.getGid()));
			dir.setUid(vo.getUid());
			dir.setCardId(vo.getCardId());
			dir.setErspaceId(vo.getErspaceId());
			dir.setEnterTime(vo.getPcTime());
			dir.setPcTime(vo.getPcTime());
			dir.setCardNo(vo.getCardNo());
			dir.setEptId(vo.getEptId());
			dir.setRspType(vo.getRspType());
			dir.setRspaceId(vo.getRspaceId());
			dir.setDrNo(vo.getDrNo());
			dir.setName(vo.getName());
			dir.setDelFlag(Const.NO);
			save(dir);
		}else{//0出门打卡
			DrInoutRecord dir=new DrInoutRecord();
            dir.setGroup(new DrCardreGroup(vo.getGid()));
			dir.setUid(vo.getUid());
			dir.setCardId(vo.getCardId());
			dir.setErspaceId(vo.getErspaceId());
			dir.setExitTime(vo.getPcTime());
			dir.setPcTime(vo.getPcTime());
			dir.setCardNo(vo.getCardNo());
			dir.setEptId(vo.getEptId());
			dir.setRspType(vo.getRspType());
			dir.setRspaceId(vo.getRspaceId());
			dir.setDrNo(vo.getDrNo());
			dir.setName(vo.getName());
			dir.setDelFlag(Const.NO);
			save(dir);
		}
        return true;
	}

	/**
	 * 夏添处理的逻辑，需要计算进入时长.
	 * @param vo
	 * @param maxHour
	 * @return Boolean
	 */
	@Transactional(readOnly = false)
	public Boolean disposeDrCardRecordByXt(DrCardRecordVo vo,int maxHour) {
	    if((vo.getPcTime() == null) || StringUtil.isEmpty(vo.getGid()) || StringUtil.isEmpty(vo.getCardId()) || StringUtil.isEmpty(vo.getEptId()) || StringUtil.isEmpty(vo.getRspaceId())){
	        System.out.println("------------------------->>>>参数异常");
	        return false;
	    }
	    if(Const.YES.equals(vo.getIsEnter())){//进门打卡
	        DrInoutRecord dir = new DrInoutRecord();
	        dir.setGroup(new DrCardreGroup(vo.getGid()));
	        dir.setUid(vo.getUid());
	        dir.setCardId(vo.getCardId());
	        dir.setErspaceId(vo.getErspaceId());
	        dir.setEnterTime(vo.getPcTime());
	        dir.setPcTime(vo.getPcTime());
	        dir.setCardNo(vo.getCardNo());
	        dir.setEptId(vo.getEptId());
	        dir.setRspType(vo.getRspType());
	        dir.setRspaceId(vo.getRspaceId());
	        dir.setDrNo(vo.getDrNo());
	        dir.setName(vo.getName());
	        dir.setDelFlag(Const.NO);
	        save(dir);
	    }else{//出门打卡
	        //时间排序，获取当前卡第一条出记录后的最后一条进记录（查询条件、cardId和rspaceId）
	        DrInoutRecord lastenter=dao.getLastEnterData(vo);
	        //判断最后进记录后的第一条出记录的时间是否超过预警时间，没有则添加到进记录后设置退出时间
	        if(lastenter!=null&&lastenter.getEnterTime()!=null&&lastenter.getExitTime()==null &&DateUtil.addHours(lastenter.getEnterTime(), maxHour).after(vo.getPcTime())){
	            lastenter.setExitTime(vo.getPcTime());
	            lastenter.setGroup(new DrCardreGroup(vo.getGid()));
	            save(lastenter);
	        }else{
	            DrInoutRecord dir=new DrInoutRecord();
	            dir.setGroup(new DrCardreGroup(vo.getGid()));
	            dir.setUid(vo.getUid());
	            dir.setCardId(vo.getCardId());
	            dir.setErspaceId(vo.getErspaceId());
	            dir.setExitTime(vo.getPcTime());
	            dir.setPcTime(vo.getPcTime());
	            dir.setCardNo(vo.getCardNo());
	            dir.setEptId(vo.getEptId());
	            dir.setRspType(vo.getRspType());
	            dir.setRspaceId(vo.getRspaceId());
	            dir.setDrNo(vo.getDrNo());
	            dir.setName(vo.getName());
	            dir.setDelFlag(Const.NO);
	            save(dir);
	        }
	        //处理如果最外层的闸机没有出卡记录，以检查对应房间是否存在出卡记录，存在则以房间出卡记录作为当次打卡的出卡记录计算退出时间。
	        updateChildEmptyOutTime(vo.getRspaceId(), maxHour*60*60L, DateUtil.formatDate(vo.getPcTime(), "yyyy-MM-dd HH:mm:ss"));
	    }
	    return true;
	}
	/**根据场地id更新子节点没有出卡时间的记录
	 * @param spaceOrRoomId 场地或房间id
	 * @param maxTime 预警时间，秒,超过这个时间就不匹配出和入
	 * @param date 出卡时间
	 */
	private void updateChildEmptyOutTime(String spaceOrRoomId,Long maxTime,String date){
		dao.updateChildEmptyOutTime(spaceOrRoomId, maxTime, date);
	}
	@Transactional(readOnly = false)
	public void disposeDrCardRecord(List<DrCardRecordVo> sources, int maxHour) {
	    List<DrCardRecordVo> targets = Lists.newArrayList();
		if(StringUtil.checkEmpty(sources)){
		    return;
		}

		for(DrCardRecordVo vo: sources){
		    if(disposeDrCardRecord(vo, maxHour)){
		        targets.add(vo);
		    }
		}
		if(StringUtil.checkNotEmpty(targets)){
            drCardRecordDao.updateDisposeDone(targets);
		}
	}
	public DrInoutRecord get(String id) {
		return super.get(id);
	}

	public List<DrInoutRecord> findList(DrInoutRecord drInoutRecord) {
		return super.findList(drInoutRecord);
	}

	public List<DrInoutRecordVo> findListVo(DrInoutRecordVo vo) {
	    return dao.getListVo(vo);
	}

	public Page<DrInoutRecordVo> findPage(Page<DrInoutRecordVo> page, DrInoutRecordVo vo) {
		vo.setPage(page);
		page.setList(dao.getListVo(vo));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(DrInoutRecord drInoutRecord) {
		super.save(drInoutRecord);
	}

    @Transactional(readOnly = false)
    public void updateByPl(List<DrInoutRecord> entitys) {
        dao.updateByPl(entitys);
    }

    @Transactional(readOnly = false)
    public void updateUidByPl(List<DrInoutRecord> entitys) {
        dao.updateUidByPl(entitys);
    }

	@Transactional(readOnly = false)
	public void delete(DrInoutRecord drInoutRecord) {
		super.delete(drInoutRecord);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrInoutRecord drInoutRecord) {
  	  dao.deleteWL(drInoutRecord);
  	}

	public List<DrInoutRecord> getAllDrByCardId(String cardId) {
		return dao.getAllDrByCardId(cardId);
	}
}