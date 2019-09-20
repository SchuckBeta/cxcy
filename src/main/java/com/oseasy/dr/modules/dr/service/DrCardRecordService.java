package com.oseasy.dr.modules.dr.service;

import java.util.Date;
import java.util.List;

import com.oseasy.dr.common.config.DrIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.dr.modules.dr.dao.DrCardRecordDao;
import com.oseasy.dr.modules.dr.dao.DrEquipmentDao;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.entity.DrCardRule;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.entity.DrCardreGtime;
import com.oseasy.dr.modules.dr.entity.DrInoutRecord;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.vo.DrCardRecordParam;
import com.oseasy.dr.modules.dr.vo.DrCardRecordShowVo;
import com.oseasy.dr.modules.dr.vo.DrCardRecordWarnVo;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 门禁卡记录Service.
 * @author chenh
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = true)
public class DrCardRecordService extends CrudService<DrCardRecordDao, DrCardRecord> {

	@Autowired
	DrEquipmentDao drEquipmentDao;
	@Autowired
	DrCardRuleService drCardRuleService;
	@Autowired
	DrInoutRecordService drInoutRecordService;
	@Autowired
	DrCardreGtimeService drCardreGtimeService;

	public DrCardRecord get(String id) {
		return super.get(id);
	}

	public List<DrCardRecord> findList(DrCardRecord drCardRecord) {
		return super.findList(drCardRecord);
	}

	public List<DrCardRecordShowVo> findRecordList(DrCardRecordShowVo drCardRecord) {
	    return dao.findRecordList(drCardRecord);
	}

	public Page<DrCardRecordShowVo> findPage(Page<DrCardRecordShowVo> page, DrCardRecordShowVo drCardRecordShowVo) {
		drCardRecordShowVo.setPage(page);
		page.setList(dao.findRecordList(drCardRecordShowVo));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(DrCardRecord drCardRecord) {
		super.save(drCardRecord);
	}

	@Transactional(readOnly = false)
	public void delete(DrCardRecord drCardRecord) {
		super.delete(drCardRecord);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardRecord drCardRecord) {
  	  dao.deleteWL(drCardRecord);
  	}

    @Transactional(readOnly = false)
    public void updateByPl(List<DrCardRecord> entitys) {
        dao.updateByPl(entitys);
    }

    @Transactional(readOnly = false)
    public void updateUidByPl(List<DrCardRecord> entitys) {
        dao.updateUidByPl(entitys);
    }

    /**
     * 批量保存.
     * @param entitys 节点数据
     */
    @Transactional(readOnly = false)
    public Boolean batchSave(List<DrCardRecord> entitys,String equipmentSn,int tindex) {
        if(StringUtil.checkEmpty(entitys)){
            return false;
        }
		try {
			int i = dao.savePl(entitys);
			if(i>0){
				drEquipmentDao.updateTindexByNo(equipmentSn,tindex);
				//刷新缓存
				CacheUtils.remove(DrUtils.CACHE_DR_EQUIPMENT_MAP);
			}
			return true;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return false;
		}
    }

	public Page<DrCardRecordWarnVo> findWarnPage(Page<DrCardRecordWarnVo> page, DrCardRecordWarnVo drCardRecordWarnVo) {
	    DrCardreGtime drCardreGtime = new DrCardreGtime();
	    drCardreGtime.setStatus(Const.YES);
	    if(StringUtil.isNotEmpty(drCardRecordWarnVo.getGid())){
//	        drCardreGtime.setGroup(new DrCardreGroup(drCardRecordWarnVo.getGid()));
	        drCardreGtime.setGroup(new DrCardreGroup(DrIds.DR_CARD_GID.getId()));
	    }
	    drCardRecordWarnVo.setDrGtimes(drCardreGtimeService.findList(drCardreGtime));
		drCardRecordWarnVo.setPage(page);
		DrCardRule drCardRuleIn=drCardRuleService.getDrCardRule();
		if(drCardRuleIn != null){
			if((Const.YES).equals(drCardRuleIn.getIsEnter())){
				//查询预警n天未进入的卡号
				List<String> inIds=dao.findInList(drCardRuleIn.getEnterTime());
				drCardRecordWarnVo.setIsEnter(drCardRuleIn.getIsEnter());
				drCardRecordWarnVo.setEnterDay(drCardRuleIn.getEnterTime());
			}
			if((Const.YES).equals(drCardRuleIn.getIsOut())){
				//查询预警当天begintime至endtime时间段未出的卡号
				Date date=new Date();
				int year=date.getYear();
				int mouth=date.getMonth();
				int day=date.getDate();
				if(StringUtil.isEmpty(drCardRuleIn.getBeginTime())){
					drCardRuleIn.setBeginTime(DateUtil.FMT_HMS000);
				}
				if(StringUtil.isEmpty(drCardRuleIn.getEndTime())){
					drCardRuleIn.setBeginTime(DateUtil.FMT_HMS999);
				}
                Date begindate=new Date(year, mouth, day, Integer.parseInt(drCardRuleIn.getBeginTime().substring(0,2)),0,0);
                Date enddate=new Date(year, mouth, day, Integer.parseInt(drCardRuleIn.getEndTime().substring(0,2)),0,0);
				//List<String> outIds=dao.findOutList(begindate,enddate);
				drCardRecordWarnVo.setIsOut(drCardRuleIn.getIsOut());
				drCardRecordWarnVo.setOutBeginDate(begindate);
				drCardRecordWarnVo.setOutEndDate(enddate);
			}
			if((Const.YES).equals(drCardRecordWarnVo.getIsEnter()) || (Const.YES).equals(drCardRecordWarnVo.getIsOut())){
	            page.setList(dao.getListVo(drCardRecordWarnVo));
	        }
		}
		return page;
//		Page page=new Page();
//		DrCardRule drCardRuleIn=drCardRuleService.getDrCardRule();
//		List <DrCardRecordWarnVo> drCardRecordInList=new ArrayList<DrCardRecordWarnVo>();
//		List <DrCardRecordWarnVo> drCardRecordOutList=new ArrayList<DrCardRecordWarnVo>();
//		if(drCardRuleIn!=null){
//			if(drCardRuleIn.getIsEnter().equals("1")){
//				//查询预警n天未进入的卡号
//				List<String> inIds=dao.findInList(drCardRuleIn.getEnterTime());
//			}
//			if(drCardRuleIn.getIsOut().equals("1")){
//				//查询预警当天begintime至endtime时间段未出的卡号
//				Date date=new Date();
//				int year=date.getYear();
//				int mouth=date.getMonth();
//				int day=date.getDay();
//				Date begindate=new Date(year,mouth,day,Integer.parseInt(drCardRuleIn.getBeginTime().substring(0,2)),0,0);
//				Date enddate=new Date(year,mouth,day,Integer.parseInt(drCardRuleIn.getEndTime().substring(0,2)),0,0);
//				List<String> outIds=dao.findOutList(begindate,enddate);
//			}
//		}
//		return page;
	}

	private List<String> findInList(String day) {
		return dao.findInList(day);
	}
	private List<String> findOutList(Date beginTime,Date endTime) {
		return dao.findOutList(beginTime,endTime);
	}

	public Date getLastEnterTime(String cardId) {
		Date lastEnterTime=dao.getLastEnterTime(cardId);
		return lastEnterTime;
	}

	public String getEnterAllTime(String cardId) {
		List<DrInoutRecord> drInoutRecordList=drInoutRecordService.getAllDrByCardId(cardId);
		String allTimeString="";
		if(StringUtil.checkNotEmpty(drInoutRecordList)){
			Long allTime=0L;
			for(DrInoutRecord drInoutRecord:drInoutRecordList){
				if(drInoutRecord.getEnterTime()!=null && drInoutRecord.getExitTime()!=null){
					Long time=drInoutRecord.getExitTime().getTime()-drInoutRecord.getEnterTime().getTime();
					allTime=allTime+time;
				}
			}
			if(allTime>0){
				allTimeString=DateUtil.formatDateTime3(allTime);
			}
		}
		return allTimeString;
	}

    /**
     * 同步记录数据,默认最近3天数据.
     * @param drCrParam 参数
     * @return Boolean
     */
    @Transactional(readOnly = false)
    public boolean ajaxSynch(DrCardRecordParam drCrParam) {
        if(drCrParam == null){
            drCrParam = new DrCardRecordParam();
            drCrParam.setMinPcTime(DateUtil.getCurDateYMD000(DateUtil.addDays(new Date(), -3)));
            drCrParam.setMaxPcTime(DateUtil.getCurDateYMD999(new Date()));
        }
        List<DrCardRecord> drCardRecordAlls = dao.findAllSynRecord(drCrParam.getMinPcTime(), drCrParam.getMaxPcTime());
        if(StringUtil.checkNotEmpty(drCardRecordAlls)){
        	int cc = drCardRecordAlls.size() / 20;
        	if((drCardRecordAlls.size() < 500)){
            	System.out.println("size = "+500);
        		runThread(drCardRecordAlls);
        		return true;
        	}

        	cc = drCardRecordAlls.size() / 20;
        	System.out.println("size = "+cc);
            //根据ftp连接数分线程执行
            for (int i = 0; i < 20; i++) {
                List<DrCardRecord> drCardRecords = null;
            	if (i == 20 - 1) {//最后一个链接处理剩余全部文件
            		drCardRecords = drCardRecordAlls.subList(i * cc, drCardRecordAlls.size());
            	}else{
            		drCardRecords = drCardRecordAlls.subList(i * cc, ((i + 1) * cc));
            	}
            	if (drCardRecords != null && drCardRecords.size() > 0) {
            		runThread(drCardRecords);
            	}
            }
            logger.info("打卡记录数据同步处理数量"+drCardRecordAlls.size() + "条，同步条件为 cr.ept_sn IS NOT NULL AND cr.card_no IS NOT NULL AND cr.card_id IS NULL");
        }
        return true;
    }

    @Transactional(readOnly = false)
	private void runThread(List<DrCardRecord> drCardRecords) {
		ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
		    @Override
		    public void run() {
		        try {
		            dao.updateByPl(drCardRecords);
		        } catch (Exception e) {
		            logger.error("更新数据出错", e);
		        }
		    }
		});
	}

    public static void main(String[] args) {
    	DrCardRecordParam cardRecordParam = new DrCardRecordParam();
        cardRecordParam.setMinPcTime(DateUtil.getCurDateYMD000(DateUtil.addDays(new Date(), -1)));
        cardRecordParam.setMaxPcTime(DateUtil.getCurDateYMD999(new Date()));
        System.out.println(DateUtil.formatDate(cardRecordParam.getMinPcTime(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG));
        System.out.println(DateUtil.formatDate(cardRecordParam.getMaxPcTime(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG));
	}
}