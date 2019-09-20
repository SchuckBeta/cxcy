/**
 * .
 */

package com.oseasy.dr.modules.dr.manager;


import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.dr.modules.dr.dao.DrCardDao;
import com.oseasy.dr.modules.dr.dao.DrCardErspaceDao;
import com.oseasy.dr.modules.dr.dao.DrCardRecordDao;
import com.oseasy.dr.modules.dr.dao.DrCardreGroupDao;
import com.oseasy.dr.modules.dr.dao.DrEquipmentDao;
import com.oseasy.dr.modules.dr.dao.DrEquipmentRspaceDao;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrCardreGitem;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.service.DrInoutRecordService;
import com.oseasy.dr.modules.dr.vo.DrCardRecordVo;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class DrUtils{
    public final static Logger logger = Logger.getLogger(DrUtils.class);
    private static DrCardDao drCardDao = SpringContextHolder.getBean(DrCardDao.class);
    private static DrCardreGroupDao drCardreGroupDao = SpringContextHolder.getBean(DrCardreGroupDao.class);
    private static DrCardErspaceDao drCardErspaceDao = SpringContextHolder.getBean(DrCardErspaceDao.class);
	private static DrInoutRecordService drInoutRecordService = SpringContextHolder.getBean(DrInoutRecordService.class);
    private static DrCardRecordDao drCardRecordDao = SpringContextHolder.getBean(DrCardRecordDao.class);
    private static DrEquipmentDao dreDao = SpringContextHolder.getBean(DrEquipmentDao.class);
    public static final String CACHE_DR_EQUIPMENT_MAP = "dreMap";
    private static DrEquipmentRspaceDao dreRspaceDao = SpringContextHolder.getBean(DrEquipmentRspaceDao.class);
    public static final String CACHE_DR_EQUIPMENT_RSPACE_LIST = "dreRspaceList";
    public static final String CACHE_DR_EQUIPMENT_RSPACE_IDMAP = "dreRspaceByIdMap";
    public static final String CACHE_DR_EQUIPMENT_RSPACE_NOMAP = "dreRspaceByNoMap";
    private static DrCardErspaceDao dreCardRspaceDao = SpringContextHolder.getBean(DrCardErspaceDao.class);
    public static final String CACHE_DR_CARD_ERSPACE_IDMAP = "dreCardRspaceByIdMap";
    public static final String CACHE_DR_CARD_ERSPACE_NOMAP = "dreCardRspaceByNoMap";

    public static final String CACHE_DRCVO = "drCvoMap";
    public static final String CACHE_DR_EXPORT = "drExport";

    public static IdrVo getIdrVo(String uuid) {
        @SuppressWarnings("unchecked")
        Map<String, IdrVo> cardMap = (Map<String, IdrVo>)CacheUtils.get(CACHE_DR_EXPORT);
        if (cardMap == null || cardMap.size() == 0) {
            return null;
        }
        return cardMap.get(uuid);
    }

    public static Map<String, IdrVo> putIdrVo(String uuid, IdrVo vo) {
        @SuppressWarnings("unchecked")
        Map<String, IdrVo> cardMap = (Map<String, IdrVo>)CacheUtils.get(CACHE_DR_EXPORT);
        if (cardMap == null || cardMap.size() == 0) {
            cardMap = Maps.newHashMap();
        }
        if (StringUtil.isNotEmpty(uuid)) {
            cardMap.put(uuid, vo);
        }
        CacheUtils.put(CACHE_DR_EXPORT, cardMap);
        return cardMap;
    }

    /**
     * 从缓存中移除IdrVo.
     * @param cacheName
     * @param key
     */
    public static Map<String, IdrVo> removeIdrVo(String cacheName, String key) {
        Map<String, IdrVo> cardMap = (Map<String, IdrVo>)CacheUtils.get(cacheName);
        cardMap.remove(key);
        CacheUtils.put(cacheName, cardMap);
        return cardMap;
    }


    public static DrCvo getCard(Long version) {
	  	@SuppressWarnings("unchecked")
	  	Map<String, DrCvo> cardMap = (Map<String, DrCvo>)CacheUtils.get(CACHE_DRCVO);
	  	if (cardMap == null || cardMap.size() == 0) {
	  		return null;
  		}
  		DrCvo cvo = cardMap.get(version+"");
  		if(cvo == null){
  			logger.info("getCard->当前缓存标识为："+CACHE_DRCVO+"，当前版本为："+version);
  			logger.info(JsonMapper.toJsonString(cardMap));
  		}
  		return cvo;
  	}

    public static Map<String, DrCvo> putCard(DrCvo drCvo) {
		@SuppressWarnings("unchecked")
		Map<String, DrCvo> cardMap = (Map<String, DrCvo>)CacheUtils.get(CACHE_DRCVO);
		if (cardMap == null || cardMap.size() == 0) {
			cardMap = Maps.newHashMap();
		}
		if (drCvo.getVersion() != null) {
			cardMap.put(drCvo.getVersion()+"", drCvo);
		}
		CacheUtils.put(CACHE_DRCVO, cardMap);
		return cardMap;
	}

    /**
     * 检查是否最后一次，如果是，更新数据到数据库（请求响应时调用）.
     * @param version 版本标识
     * @param card 当前卡
     * @param cardErspaces 当前授权.
     * @return Boolean
     */
    public static synchronized Boolean checkCvo(Long version, DrCard cad, String eptId, List<DrCardErspace> cardErspaces) {
    	DrCvo cvo = getCard(version);
    	if(cvo == null){
    		logger.error("设备["+eptId+"没有获取到授权信息]，取不到缓存对象");
    		return false;
    	}

    	/**
    	 * 设备不存在设备列表中不做任何处理.
    	 */
		List<String> eptIds = cvo.getEtpIds();
		if(!(eptIds).contains(eptId)){
	        return cvo.getHasFinish();
		}

		//没有授权信息则不执行任何操作
    	if(StringUtil.checkEmpty(cardErspaces)){
    		logger.error("设备["+eptId+"没有获取到授权信息]");
    	}

		/**
		 * 设备已经处理过不再重复处理.
		 */
		Map<String, List<DrCardErspace>> maps = cvo.getEtpCerspaces();
		//1、更新卡授权数据
		maps.put(eptId, cardErspaces);
		cvo.setEtpCerspaces(maps);

		//2、更新卡数据
		DrCard card = cvo.getCard();
		if((card != null) && (!(card.getId()).equals(cad.getId()))){
    		logger.error("设备["+eptId+"更新卡数据失败,卡参数未传递到响应]");
	        return cvo.getHasFinish();
		}

		if(card == null){
			card = cad;
		}
		cvo.setCard(card);

        DrUtils.putCard(cvo);
        return cvo.getHasFinish();
	}

    /**
     * 检查是否过期，如果是，更新数据到数据库.
     * @return Boolean
     */
    public static synchronized List<String> checkCvos(Long localCurTime) {
	  	@SuppressWarnings("unchecked")
		Map<String, DrCvo> cardMap = (Map<String, DrCvo>)CacheUtils.get(CACHE_DRCVO);
	  	if (cardMap == null || cardMap.size() == 0) {
	  		return null;
  		}

        logger.error("门禁卡缓存总数量："+cardMap.size());
	  	List<String> failCids = Lists.newArrayList();
	  	Iterator<Entry<String, DrCvo>> it = cardMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	DrCvo entry = (DrCvo) it.next().getValue();
	    	if((entry == null) || (entry.getCard() == null)){
	    		continue;
	    	}

            if(entry.getHasFinish()){
                DrUtils.removeDrCvo(DrUtils.CACHE_DRCVO, entry.getVersion()+"");
            }

	    	if(entry.checkHasExpire(localCurTime) && (!entry.getHasFinish())){
	    		failCids.add(entry.getCard().getId());
	    		DrUtils.removeDrCvo(DrUtils.CACHE_DRCVO, entry.getVersion()+"");
	    	}
	    }

	    if(StringUtil.checkNotEmpty(failCids)){
    		//drCardDao.updateDealStatusByPl(failCids, DrCdealStatus.DCD_FAIL.getKey());
    		drCardDao.updateDealingStatusByPl(failCids, DrCdealStatus.DCD_FAIL.getKey());
    		drCardErspaceDao.updateStatusByCid(failCids, DrCdealStatus.DCD_FAIL.getKey());
	    }
	    return failCids;
    }

    /**
     * 从缓存中移除DrCvo.
     * @param cacheName
     * @param key
     */
    public static Map<String, DrCvo> removeDrCvo(String cacheName, String key) {
        Map<String, DrCvo> cardMap = (Map<String, DrCvo>)CacheUtils.get(cacheName);
        cardMap.remove(key);
		logger.info("removeDrCvo->当前缓存标识为："+CACHE_DRCVO+"，当前Key为："+key);
        CacheUtils.put(cacheName, cardMap);
        return cardMap;
    }

    /***************************************************
     * 获取DrEquipment设备.
     * @param id
     * @return DrEquipment
     */
    public static DrEquipment getEt(String id) {
        return dreDao.get(id);
    }

    /**
     * 根据类型获取所有设备.
     * @param type 设备ID
     * @return List
     */
    public static List<DrEquipment> getAllEt(Integer type) {
        if(type == null){
            type = DrConfig.DET_TYPE;
        }

        @SuppressWarnings("unchecked")
        Map<String, List<DrEquipment>> dreMap = (Map<String, List<DrEquipment>>)CacheUtils.get(CACHE_DR_EQUIPMENT_MAP);
        if ((dreMap == null) || (dreMap.size()==0)) {
            dreMap = Maps.newHashMap();
            DrEquipment drept = new DrEquipment();
            drept.setDelFlags(Arrays.asList(new String[]{"0"}));
            for (DrEquipment dre : dreDao.findAllList(drept)) {
                List<DrEquipment> dreList = dreMap.get(dre.getType()+"");
                if (dreList != null) {
                    dreList.add(dre);
                }else{
                    dreMap.put(type+"", Lists.newArrayList(dre));
                }
            }
            CacheUtils.put(CACHE_DR_EQUIPMENT_MAP, dreMap);
        }

        List<DrEquipment> dreList = dreMap.get(type+"");
        if (dreList == null) {
            dreList = Lists.newArrayList();
        }
        return dreList;
    }
    public static List<DrEquipment> getAllEt() {
        return getAllEt(null);
    }

    /**
     * 获取所有设备和房间.
     * @return List
     */
    public static List<DrEquipmentRspace> getAllEtrs() {
        @SuppressWarnings("unchecked")
        List<DrEquipmentRspace> drerspaceList = (List<DrEquipmentRspace>)CacheUtils.get(CACHE_DR_EQUIPMENT_RSPACE_LIST);
        if (StringUtil.checkEmpty(drerspaceList)) {
            drerspaceList = dreRspaceDao.findAllList(new DrEquipmentRspace());
            CacheUtils.put(CACHE_DR_EQUIPMENT_RSPACE_LIST, drerspaceList);
        }
        if (drerspaceList == null) {
            drerspaceList = Lists.newArrayList();
        }
        return drerspaceList;
    }

    /**
     * 根据设备ID或门号获取所有设备和房间.
     * @param epmentId 设备ID
     * @param drno 门编号DrKey
     * @return List
     */
    public static DrEquipmentRspace getEtrspaceById(String epmentId, String drno) {
        @SuppressWarnings("unchecked")
        Map<String, List<DrEquipmentRspace>> drerspaceMap = (Map<String, List<DrEquipmentRspace>>)CacheUtils.get(CACHE_DR_EQUIPMENT_RSPACE_IDMAP);
        if ((drerspaceMap == null) || (drerspaceMap.size() == 0)) {
            drerspaceMap = new HashMap<String, List<DrEquipmentRspace>>();
            List<DrEquipmentRspace> dreRspaceList = drerspaceMap.get(epmentId + StringUtil.LINE_M + drno);
            if (dreRspaceList == null) {
                dreRspaceList = dreRspaceDao.findList(new DrEquipmentRspace(epmentId, drno));
                drerspaceMap.put(epmentId + StringUtil.LINE_M + drno, dreRspaceList);
            }
            CacheUtils.put(CACHE_DR_EQUIPMENT_RSPACE_IDMAP, drerspaceMap);
        }

        List<DrEquipmentRspace> dreRspaceList = drerspaceMap.get(epmentId + StringUtil.LINE_M + drno);
        if ((dreRspaceList == null) || (dreRspaceList.size() != 1)) {
            return null;
        }
        return dreRspaceList.get(0);
    }

    /**
     * 根据根据设备ID或门号获取设备信息.
     * @param etrspace
     * @return
     */
    public static DrEquipmentRspace getEtrspaceById(DrEquipmentRspace etrspace) {
        if((etrspace.getEpment() == null) || StringUtil.isEmpty(etrspace.getEpment().getId()) || StringUtil.isEmpty(etrspace.getDrNo())){
            return null;
        }
        return getEtrspaceById(etrspace.getEpment().getId(), etrspace.getDrNo());
    }


    /**
     * 根据设备No或门号获取所有设备和房间.
     * @param epmentNo 申报编号DrKey
     * @param drno 门编号DrKey
     * @return List
     */
    public static DrEquipmentRspace getEtrspaceByNo(String epmentNo, String drno) {
        @SuppressWarnings("unchecked")
        Map<String, List<DrEquipmentRspace>> drerspaceMap = (Map<String, List<DrEquipmentRspace>>)CacheUtils.get(CACHE_DR_EQUIPMENT_RSPACE_NOMAP);
        if ((drerspaceMap == null) || (drerspaceMap.size()==0)) {
            drerspaceMap = Maps.newHashMap();
            DrEquipment dre = new DrEquipment();
            dre.setNo(epmentNo);
            List<DrEquipmentRspace>  dreRspaceList = dreRspaceDao.findList(new DrEquipmentRspace(dre, drno));
            if (StringUtil.checkNotEmpty(dreRspaceList) ){
                drerspaceMap.put(epmentNo + StringUtil.LINE_M + drno, dreRspaceList);
                CacheUtils.put(CACHE_DR_EQUIPMENT_RSPACE_NOMAP, drerspaceMap);
                return dreRspaceList.get(0);
            }
        }

        List<DrEquipmentRspace> dreRspaceList = drerspaceMap.get(epmentNo + StringUtil.LINE_M + drno);
        if (StringUtil.checkNotEmpty(dreRspaceList) ){
            return dreRspaceList.get(0);
        }
       return null;
    }

    /**
     * 根据设备No或门号获取设备信息.
     * @param etrspace
     * @return
     */
    public static DrEquipmentRspace getEtrspaceByNo(DrEquipmentRspace etrspace) {
        if((etrspace.getEpment() == null) || StringUtil.isEmpty(etrspace.getEpment().getNo()) || StringUtil.isEmpty(etrspace.getDrNo())){
            return null;
        }
        return getEtrspaceByNo(etrspace.getEpment().getId(), etrspace.getDrNo());
    }

    /**
     * 根据设备ID或门号获取所有卡和设备和房间.
     * @param drno 门编号DrKey
     * @return List
     */
    public static List<DrCardErspace> getCardEtrspaceById(String epmentId, String drno) {
        @SuppressWarnings("unchecked")
        Map<String, List<DrCardErspace>> drerspaceMap = (Map<String, List<DrCardErspace>>)CacheUtils.get(CACHE_DR_CARD_ERSPACE_IDMAP);
        if ((drerspaceMap == null) || (drerspaceMap.size() == 0)) {
            drerspaceMap = Maps.newHashMap();
            List<DrCardErspace>  dreRspaceList = dreCardRspaceDao.findListByg(new DrCardErspace(new DrEquipmentRspace(epmentId, drno)));
            if (StringUtil.checkNotEmpty(dreRspaceList) ){
                drerspaceMap.put(epmentId + StringUtil.LINE_M + drno, dreRspaceList);
                CacheUtils.put(CACHE_DR_CARD_ERSPACE_IDMAP, drerspaceMap);
                return dreRspaceList;
            }
        }

        return drerspaceMap.get(epmentId + StringUtil.LINE_M + drno);
    }

    /**
     * 根据设备ID或门号获取设备信息.
     * @return
     */
    public static List<DrCardErspace> getCardEtrspaceById(DrCardErspace cardEtrspace) {
        if((cardEtrspace.getErspace() == null) || (cardEtrspace.getErspace().getEpment() == null) || StringUtil.isEmpty(cardEtrspace.getErspace().getEpment().getId()) || StringUtil.isEmpty(cardEtrspace.getErspace().getDrNo())){
            return null;
        }
        return getCardEtrspaceById(cardEtrspace.getErspace().getEpment().getId(), cardEtrspace.getErspace().getDrNo());
    }

    /**
     * 根据设备No或门号获取所有卡和设备和房间.
     * @param epmentNo 申报编号DrKey
     * @param drno 门编号DrKey
     * @return List
     */
    public static List<DrCardErspace> getCardEtrspaceByNo(String epmentNo, String drno) {
        @SuppressWarnings("unchecked")
        Map<String, List<DrCardErspace>> drerspaceMap = (Map<String, List<DrCardErspace>>)CacheUtils.get(CACHE_DR_CARD_ERSPACE_NOMAP);
        if ((drerspaceMap == null) || (drerspaceMap.size() == 0)) {
            drerspaceMap = Maps.newHashMap();
            DrEquipment dre = new DrEquipment();
            dre.setNo(epmentNo);
            List<DrCardErspace>  dreRspaceList = dreCardRspaceDao.findListByg(new DrCardErspace(new DrEquipmentRspace(dre, drno)));
            if (StringUtil.checkNotEmpty(dreRspaceList) ){
                drerspaceMap.put(epmentNo + StringUtil.LINE_M + drno, dreRspaceList);
                CacheUtils.put(CACHE_DR_CARD_ERSPACE_NOMAP, drerspaceMap);
                return dreRspaceList;
            }
        }

        return drerspaceMap.get(epmentNo + StringUtil.LINE_M + drno);
    }

    /**
     * 根据设备No或门号获取设备信息.
     * @param etrspace
     * @return
     */
    public static List<DrCardErspace> getCardEtrspaceByNo(DrCardErspace cardEtrspace) {
        if((cardEtrspace.getErspace() == null) || (cardEtrspace.getErspace().getEpment() == null) || StringUtil.isEmpty(cardEtrspace.getErspace().getEpment().getNo()) || StringUtil.isEmpty(cardEtrspace.getErspace().getDrNo())){
            return null;
        }
        return getCardEtrspaceByNo(cardEtrspace.getErspace().getEpment().getId(), cardEtrspace.getErspace().getDrNo());
    }


    /**
     * 获取配置 未出基地预警小时数
     * @return 未出基地预警小时数
     */
    public static int getMaxHour(){
    	return 24;
    }

    /**
     * 将DrCardRecord数据处理到出入记录表
     */
    public static Boolean disposeDrCardRecord(String date){
        //Old 旧版本
//    	List<DrCardRecordVo> list = drCardRecordDao.getDataToDispose(date);
//    	if(list==null||list.size()==0){
//    		return;
//    	}
//    	int maxHour=getMaxHour();
//		drInoutRecordService.disposeDrCardRecord(list, maxHour);
        return disposeDrCardRecordByGids(date);
    }

    /**
     * 将DrCardRecord数据处理到出入记录表
     */
    public static Boolean disposeDrCardRecordByGids(String date){
        DrCardreGroup drCreGroup = new DrCardreGroup();
        drCreGroup.setIsShow(true);
        return setDrCrecordVoGid(date, drCardreGroupDao.findListByg(drCreGroup));
    }

    /**
     * 将DrCardRecord数据处理到出入记录表
     */
    public static Boolean disposeDrCardRecordByGids(String date, String gids){
        return setDrCrecordVoByGids(date, Arrays.asList(StringUtil.split(gids, StringUtil.DOTH)));
//        DrCardreGroup drCreGroup = new DrCardreGroup();
//        drCreGroup.setIds(Arrays.asList(StringUtil.split(gids, StringUtil.DOTH)));
//        List<DrCardreGroup> drCardreGroups = drCardreGroupDao.findListByg(drCreGroup);
//        return setDrCrecordVoGid(date, drCardreGroups);
    }

    /**
     * 设置记录的Gid参数.
     * @param date 处理记录时间
     * @param drCardreGroups 匹配的Group
     * @return Boolean
     */
    private static Boolean setDrCrecordVoByGids(String date, List<String> gids) {
        //List<DrCardRecordVo> drCardRvos = drCardRecordDao.getDataToDispose(date, getRspaceIdsByGroups(drCardreGroups));
        List<DrCardRecordVo> drCardRvos = drCardRecordDao.getDataToDispose(date, gids, null);
        if(StringUtil.checkEmpty(drCardRvos)){
            return false;
        }

        List<DrCardRecordVo> targetdrCardRvos = Lists.newArrayList();
        try {
            for (DrCardRecordVo drcRvo : drCardRvos) {
                if(StringUtil.isEmpty(drcRvo.getGid())){
                    continue;
                }

                DrCardRecordVo distDrCardRecordVo = new DrCardRecordVo();
                BeanUtils.copyProperties(distDrCardRecordVo, drcRvo);
                targetdrCardRvos.add(distDrCardRecordVo);
            }

            System.out.println("targetdrCardRvos = "+targetdrCardRvos.size());
            int maxHour=getMaxHour();
            drInoutRecordService.disposeDrCardRecord(targetdrCardRvos, maxHour);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 设置记录的Gid参数.
     * @param date 处理记录时间
     * @param drCardreGroups 匹配的Group
     * @return Boolean
     */
    private static Boolean setDrCrecordVoGid(String date, List<DrCardreGroup> drCardreGroups) {
        //List<DrCardRecordVo> drCardRvos = drCardRecordDao.getDataToDispose(date, getRspaceIdsByGroups(drCardreGroups));
        List<DrCardRecordVo> drCardRvos = drCardRecordDao.getDataToDispose(date, getGidsByGroups(drCardreGroups), null);
        if(StringUtil.checkEmpty(drCardRvos)){
            return false;
        }

        List<DrCardRecordVo> targetdrCardRvos = Lists.newArrayList();
        try {
            for (DrCardRecordVo drcRvo : drCardRvos) {
                if(StringUtil.isEmpty(drcRvo.getGid())){
                    logger.warn(drcRvo.getId()+"->gid不能为空！");
                    continue;
                }
                if(StringUtil.isEmpty(drcRvo.getCardId())){
                    logger.warn(drcRvo.getId()+"->cardId不能为空！");
                    continue;
                }
                if(StringUtil.isEmpty(drcRvo.getEptId())){
                    logger.warn(drcRvo.getId()+"->eptId不能为空！");
                    continue;
                }
                if(StringUtil.isEmpty(drcRvo.getRspaceId())){
                    logger.warn(drcRvo.getId()+"->rspaceId不能为空！");
                    continue;
                }
                if(drcRvo.getPcTime() == null){
                    continue;
                }

                DrCardRecordVo distDrCardRecordVo = new DrCardRecordVo();
                BeanUtils.copyProperties(distDrCardRecordVo, drcRvo);
                targetdrCardRvos.add(distDrCardRecordVo);
            }

            System.out.println("targetdrCardRvos = "+targetdrCardRvos.size());
            int maxHour=getMaxHour();
            drInoutRecordService.disposeDrCardRecord(targetdrCardRvos, maxHour);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;

//        List<DrCardRecordVo> targetdrCardRvos = Lists.newArrayList();
//        try {
//            for (DrCardreGroup curDrCreGroup : drCardreGroups) {
//                List<DrCardreGitem> curDcGitems = curDrCreGroup.getDrCreGitems();
//                for (DrCardreGitem curDcGitem : curDcGitems) {
//                    if((curDcGitem.getErspace() == null) || StringUtil.isEmpty(curDcGitem.getErspace().getRspace())){
//                        continue;
//                    }
//
//                    for (DrCardRecordVo drcRvo : drCardRvos) {
//                        if(StringUtil.isEmpty(drcRvo.getRspaceId())){
//                            continue;
//                        }
//
//                        if((drcRvo.getRspaceId()).equals(curDcGitem.getErspace().getRspace())){
//                            DrCardRecordVo distDrCardRecordVo = new DrCardRecordVo();
//                            BeanUtils.copyProperties(distDrCardRecordVo, drcRvo);
//                            distDrCardRecordVo.setGid(curDrCreGroup.getId());
//                            targetdrCardRvos.add(distDrCardRecordVo);
//                        }
//                    }
//                }
//            }
//
//            System.out.println("targetdrCardRvos = "+targetdrCardRvos.size());
//            int maxHour=getMaxHour();
//            drInoutRecordService.disposeDrCardRecord(targetdrCardRvos, maxHour);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
    }

    /**
     * 根据预警规则组获取场地Ids.
     * @param drCardreGroups
     * @return List
     */
    public static List<String> getRspaceIdsByGroups(List<DrCardreGroup> drCardreGroups) {
        List<String> rspaceIds = Lists.newArrayList();
        for (DrCardreGroup drCardreGroup : drCardreGroups) {
            for (DrCardreGitem gitem : drCardreGroup.getDrCreGitems()) {
                if(StringUtil.isEmpty(gitem.getErspace().getRspace())){
                    continue;
                }
                if((rspaceIds).contains(gitem.getErspace().getRspace())){
                    continue;
                }
                rspaceIds.add(gitem.getErspace().getRspace());
            }
        }
        return rspaceIds;
    }

    /**
     * 根据预警规则组获取gids.
     * @param drCardreGroups
     * @return List
     */
    public static List<String> getGidsByGroups(List<DrCardreGroup> drCardreGroups) {
        List<String> gids = Lists.newArrayList();
        for (DrCardreGroup drCardreGroup : drCardreGroups) {
            if((gids).contains(drCardreGroup.getId())){
                continue;
            }
            gids.add(drCardreGroup.getId());
        }
        return gids;
    }
}
