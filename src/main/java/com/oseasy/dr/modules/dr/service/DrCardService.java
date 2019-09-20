package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SysService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.dr.modules.dr.dao.DrCardDao;
import com.oseasy.dr.modules.dr.dao.DrEquipmentDao;
import com.oseasy.dr.modules.dr.dao.DrEquipmentRspaceDao;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrEmentNo;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.dr.modules.dr.manager.DrCvo;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.manager.impl.DrCactivitService;
import com.oseasy.dr.modules.dr.manager.impl.DrCbackListService;
import com.oseasy.dr.modules.dr.manager.impl.DrCbackService;
import com.oseasy.dr.modules.dr.manager.impl.DrClossService;
import com.oseasy.dr.modules.dr.manager.impl.DrCpublishService;
import com.oseasy.dr.modules.dr.vo.DrAuth;
import com.oseasy.dr.modules.dr.vo.DrCardEquipment;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrCstatus;
import com.oseasy.dr.modules.dr.vo.DrKey;
import com.oseasy.dr.modules.dr.vo.UserQuery;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁卡Service.
 * @author chenh
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = true)
public class DrCardService extends CrudService<DrCardDao, DrCard> {
    @Autowired
    DRDeviceService drDeviceService;
    @Autowired
    DrCardErspaceService drCardErspaceService;
    @Autowired
    DrEquipmentDao drEquipmentDao;
    @Autowired
    DrEquipmentRspaceDao drEquipmentRspaceDao;

    public DrCard getByNoWithoutId(String cardno,String cardid){
    	return dao.getByNoWithoutId(cardno, cardid);
    }

    public DrCard getByNo(String no) {
        return dao.getByNo(no);
    }

	public DrCard get(String id) {
		return super.get(id);
	}
	public DrCard getByg(String id) {
	    return dao.getByg(id);
	}

	public List<DrCard> findList(DrCard drCard) {
		return super.findList(drCard);
	}
    public List<DrCard> findListByg(DrCard entity) {
        return dao.findListByg(entity);
    }
    public List<DrCard> findAllListByg(DrCard entity) {
        return dao.findAllListByg(entity);
    }

	public Page<DrCard> findPage(Page<DrCard> page, DrCard entity) {
		return super.findPage(page, entity);
	}
	public Page<DrCard> findPageByg(Page<DrCard> page, DrCard entity) {
	    entity.setPage(page);
        page.setList(dao.findList(entity));
        return page;
	}

	@Transactional(readOnly = false)
	public void save(DrCard drCard) {
        if(drCard.getPrivilege() == null){
            drCard.setPrivilege(DrAuth.DA_NONE.getKey());
        }
        if(drCard.getStatus() == null){
            drCard.setStatus(DrCstatus.DC_NORMAL.getKey());
        }
        if(drCard.getExpiry() == null){
            drCard.setExpiry(DrConfig.getExpiry().getTime());
        }
        if(StringUtil.isEmpty(drCard.getPassword())){
            drCard.setPassword(DrConfig.DET_PSW);
        }
        if(drCard.getOpenTimes() == null){
            drCard.setOpenTimes(DrConfig.DET_CARD_EXPIRE_MAX);
        }
        if(drCard.getHolidayUse() == null){
            drCard.setHolidayUse(true);
        }
        if(StringUtil.isEmpty(drCard.getWarnning())){
            drCard.setWarnning(Const.NO);
        }
        if(StringUtil.isEmpty(drCard.getIsCancel())){
            drCard.setIsCancel(Const.YES);
        }
        if(drCard.getIsNewRecord()){
            if(StringUtil.isEmpty(drCard.getId())){
                drCard.setId(IdGen.uuid());
            }
        }
        if((drCard.getUser() != null) && StringUtil.isNotEmpty(drCard.getUser().getId())){
            User curUser = UserUtils.get(drCard.getUser().getId());
            if(curUser != null){
                if(StringUtil.isNotEmpty(curUser.getNo())){
                    drCard.setTmpNo(curUser.getNo());
                }
                if(StringUtil.isNotEmpty(curUser.getName())){
                    drCard.setTmpName(curUser.getName());
                }
                if(StringUtil.isNotEmpty(curUser.getMobile())){
                    drCard.setTmpTel(curUser.getMobile());
                }
                if(StringUtil.isNotEmpty(curUser.getSex())){
                    drCard.setTmpSex(curUser.getSex());
                }
                if(StringUtil.isNotEmpty(curUser.getOfficeName())){
                    drCard.setTmpOfficeXy(curUser.getOfficeName());
                }
                if(StringUtil.isNotEmpty(curUser.getProfessional())){
                    drCard.setTmpOfficeZy(curUser.getProfessional());
                }
                if(StringUtil.isNotEmpty(drCard.getTmpOfficeXy()) && StringUtil.isNotEmpty(drCard.getTmpOfficeZy())){
                    drCard.setTmpOffice(drCard.getTmpOfficeXy() + StringUtil.LINE + drCard.getTmpOfficeZy());
                }
            }
        }
		super.save(drCard);
	}

	@Transactional(readOnly = false)
	public void delete(DrCard drCard) {
		super.delete(drCard);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCard drCard) {
  	  dao.deleteWL(drCard);
  	}


    /**
     * 查找所有入驻用户.
     * @return List 列表
     */
    public List<User> findAllUserList(User user){
        return dao.findAllUserList(user);
    }

    /**
     * 查找所有入驻没有卡用户.
     * @return List 列表
     */
    public List<User> findUserListNoCard(UserQuery user){
        return dao.findUserListNoCard(user);
    }
    /**
     * 查找所有没有卡用户.
     * @return List 列表
     */
    public List<User> findAllUserListNoCard(UserQuery user){
        return dao.findAllUserListNoCard(user);
    }

    /**
     * 查找所有入驻用户.
     * @return List 列表
     */
    public List<DrCard> findUserListBy(DrCard entity){
        return dao.findUserListBy(entity);
    }

    public Page<DrCard> findUserPageBy(Page<DrCard> page, DrCard entity) {
        entity.setPage(page);
        page.setList(findUserListBy(entity));
        return page;
    }

    /**
     * 查找所有入驻用户.
     * @return List 列表
     */
    public List<DrCard> findUserListByg(DrCard entity){
        return dao.findUserListByg(entity);
    }

    public Page<DrCard> findUserPageByg(Page<DrCard> page, DrCard entity) {
        entity.setPage(page);
        page.setList(findUserListByg(entity));
        return page;
    }

	@Transactional(readOnly = false)
	public void savePl(List<DrCard> entitys) {
	    dao.savePl(entitys);
	}

	@Transactional(readOnly = false)
	public void updateUserByPl(List<DrCard> entitys) {
	    dao.updateUserByPl(entitys);
	}

  	@Transactional(readOnly = false)
  	public void deletePlwl(List<String> ids) {
  	    dao.deletePlwl(ids);
  	}

    @Autowired
    SysService sysService;
    @Autowired
    DrCbackService drCbackService;
    @Autowired
    DrClossService drClossService;
    @Autowired
    DrCactivitService drCactivitService;
    @Autowired
    DrCbackListService DrCbackListService;
    @Autowired
    DrCpublishService drCpublishService;


    /**
     * 重置当前卡的状态.
     * @param id
     */
    @Transactional(readOnly = false)
    public void dealReset(String id) {
        DrCard drCard = get(id);
        if((DrCdealStatus.DCD_DEALING.getKey()).equals(drCard.getDealStatus())){
            drCard.setDealStatus(DrCdealStatus.DCD_FAIL.getKey());
        }
        save(drCard);
        /**
         * 设置卡设备状态为处理中.
         */
        DrCardErspace pentity = new DrCardErspace();
        pentity.setCard(new DrCard(drCard.getId()));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(pentity);
        if(StringUtil.checkNotEmpty(drCardErspaces)){
            for (DrCardErspace drcerspace : drCardErspaces) {
                drcerspace.setVersion(null);
                if((DrCdealStatus.DCD_DEALING.getKey()).equals(drcerspace.getStatus())){
                    drcerspace.setStatus(DrCdealStatus.DCD_FAIL.getKey());
                }
            }
            drCardErspaceService.updateByPl(drCardErspaces);
        }
    }
    /****************************************************************************
     * 硬件对接接口
     ****************************************************************************
     * 批量多卡多设备开卡.
     * @param drCards
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<List<DrCard>> ajaxCardPublishPl(List<DrCardEquipment> drCards) {
        if(StringUtil.checkEmpty(drCards)){
            return new ApiTstatus<List<DrCard>>(false, "卡信息不存在");
        }
        String KEY_STR = "开卡";
        ApiTstatus<List<DrCard>> rstatus = new ApiTstatus<List<DrCard>>(true, KEY_STR + "完成");
        List<DrCard> cards = Lists.newArrayList();

        for (DrCardEquipment drCardEquipment : drCards) {
            ApiTstatus<List<DrCard>> currstatus = ajaxCardPublishPl(drCardEquipment, KEY_STR);
            if(StringUtil.checkNotEmpty(currstatus.getDatas())){
                cards.addAll(currstatus.getDatas());
            }
        }
        rstatus.setDatas(cards);
        return rstatus;
    }

    /**
     *  单卡多设备开卡.
     * @param drCardEt 卡设备
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    private ApiTstatus<List<DrCard>> ajaxCardPublishPl(DrCardEquipment drCardEt, String prefix) {
        List<String> drEids = Lists.newArrayList();
        List<String> oldDrEids = Lists.newArrayList();
        List<DrCard> drCards = Lists.newArrayList();
        List<DrEmentNo> drEmentNos = Lists.newArrayList();

        //查询历史授权信息.
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(new DrCardErspace(new DrCard(drCardEt.getId())));
        //TODO 获取所有设备，处理所有设备授权
        for (DrCardErspace drcErspace : drCardErspaces) {
            if(drcErspace.getErspace() == null){
                continue;
            }

            if(drcErspace.getErspace().getEpment() == null){
                continue;
            }

            if(drcErspace.getCard() == null){
                continue;
            }

            if(StringUtil.isEmpty(drcErspace.getCard().getNo())){
                continue;
            }

            if(StringUtil.isEmpty(drcErspace.getErspace().getEpment().getId())){
                continue;
            }

            if((oldDrEids).contains(drcErspace.getErspace().getEpment().getId())){
                continue;
            }
            oldDrEids.add(drcErspace.getErspace().getEpment().getId());
            CardFactory.getCardManager(drCpublishService).deleteCard(Long.parseLong(drcErspace.getCard().getNo()), drcErspace.getErspace().getEpment().getId());
        }
        //删除卡所有授权信息
        drCardErspaceService.deletePlwlByCard(drCardEt.getId());

        //设置缓存版本号
        DrCvo cvo = new DrCvo();
        cvo.setVersion(sysService.getDbCurRlong());
        cvo.setCurTime(sysService.getDbCurLong());
        cvo.getEtpIds().addAll(DrCard.getDrEids(drCardEt.getDrEmentNos()));

        //检查完成实现开卡功能
        if(StringUtil.isNotEmpty(drCardEt.getId())){
            drCardEt.setIsNewRecord(false);
        }
        drCardEt.setDealVersion(cvo.getVersion());
        drCardEt.setStatus(DrCdealStatus.DCD_NORMAL.getKey());
        drCardEt.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
        save(drCardEt);
        cvo.setCard(drCardEt);
        DrUtils.putCard(cvo);

        int i = 0;
        for (DrEmentNo dreNo : drCardEt.getDrEmentNos()) {
            if((drEids).contains(dreNo.getEtId())){
                continue;
            }else{
                drEids.add(dreNo.getEtId());
            }

            ApiTstatus<DrCard> rstatus;
            if(i == (drCardEt.getDrEmentNos().size() - 1)){
        		rstatus = ajaxCardPublish(true, drCardEt, dreNo.getEtId());
        	}else{
        		rstatus = ajaxCardPublish(false, drCardEt, dreNo.getEtId());
        	}

            if(!rstatus.getStatus()){
                drEmentNos.add(dreNo);
            }
            drCards.add(rstatus.getDatas());
        }

        if(StringUtil.checkNotEmpty(drEmentNos)){
            return new ApiTstatus<List<DrCard>>(true, prefix + "完成,失败(" + drEmentNos.size() + "/" + drCardEt.getDrEmentNos().size()+")条", drCards);
        }
        return new ApiTstatus<List<DrCard>>(true, prefix + "成功", drCards);
    }

    /**
     *  单卡单设备开卡.
     * @param drCard 卡
     * @param equipmentId 设备ID
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    private ApiTstatus<DrCard> ajaxCardPublish(Boolean isRelease, DrCardEquipment drCard, String equipmentId) {
        String prefix = "开卡";
        //检查参数
        if(StringUtil.isEmpty(equipmentId)){
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }

        //检查设备是否存在
        DrEquipment drEquipment = drEquipmentDao.get(equipmentId);
        if((drEquipment == null)){
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }

        List<String> drEnoIds = DrCard.getDrEnos(drCard.getDrEmentNos(), equipmentId);
        drCard.setDrNoStatus(DrKey.genDrStatus(drEnoIds));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+equipmentId);
        System.out.println(drCard.getDrNoStatus()[0]+","+drCard.getDrNoStatus()[1]+","+drCard.getDrNoStatus()[2]+","+drCard.getDrNoStatus()[3]);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        DrCardParam param = new DrCardParam(drCard, equipmentId, drCard.getDealVersion(), sysService.getDbCurRlong(), isRelease);
        //检查设备是否存在，是否与场地、房间做关联
        DrEquipmentRspace pDrEquipmentRspace = new DrEquipmentRspace();
        pDrEquipmentRspace.setEpment(new DrEquipment(equipmentId));
        pDrEquipmentRspace.setDrNos(drEnoIds);
        List<DrEquipmentRspace> drEquipmentRspaces = drEquipmentRspaceDao.findList(pDrEquipmentRspace);
        if(StringUtil.checkEmpty(drEquipmentRspaces)){
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备和设施数据未初始化");
        }

        // 更新卡设备状态
        List<DrCardErspace> drCerspaces = Lists.newArrayList();
        for (DrEquipmentRspace erspace: drEquipmentRspaces) {
            for (DrEmentNo drEment: drCard.getDrEmentNos()) {
                if(StringUtil.isEmpty(drEment.getDrNo())){
                    continue;
                }

                if(!(equipmentId).equals(drEment.getEtId())){
                    continue;
                }

                if((drEment.getDrNo()).equals(erspace.getDrNo())){
                    DrCardErspace dcerspace = new DrCardErspace(IdGen.uuid());
                    dcerspace.setCard(new DrCard(drCard.getId()));
                    dcerspace.setErspace(erspace);
                    dcerspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
                    dcerspace.setVersion(param.getCeVersion());
                    dcerspace.setIsNewRecord(true);
                    drCerspaces.add(dcerspace);
                }

            }
        }

        if(StringUtil.checkNotEmpty(drCerspaces)){
            drCardErspaceService.savePl(drCerspaces);
        }


        DrCardErspace curPdrCardErspace = new DrCardErspace();
        curPdrCardErspace.setCard(new DrCard(drCard.getId()));
        curPdrCardErspace.setErspace(new DrEquipmentRspace(new DrEquipment(equipmentId)));
        List<DrCardErspace> curPdrCardErspaces = drCardErspaceService.findListByg(curPdrCardErspace);
        drCard.setErspaces(curPdrCardErspaces);
        ApiTstatus<DrCardRparam> rstatus = drCpublishService.runner(param);
        //当第一次开卡时，如果推送数据出错，数据还原
        if(!rstatus.getStatus() && drCard.getIsNewRecord()){
            //检查是否存在记录，如果有就删除（每一张卡每台台设备只能有一条记录）
            DrCardErspace pentity = new DrCardErspace();
            pentity.setCard(new DrCard(drCard.getId()));
            pentity.setErspace(new DrEquipmentRspace(equipmentId, null));
            List<DrCardErspace> drCerspaces2 = drCardErspaceService.findListByg(pentity);
            if(StringUtil.checkNotEmpty(drCerspaces2)){
                drCardErspaceService.deletePlwl(StringUtil.listIdToList(drCerspaces2));
            }

            //检查完成实现开卡功能
            delete(drCard);
            return new ApiTstatus<DrCard>(false, prefix + "失败，请确认设备连接是否正常", drCard);
        }
        //TODO 消息
        //drDeviceService.senMessage(prefix + "成功");
        return new ApiTstatus<DrCard>(true, prefix + "成功", drCard);
    }

    /****************************************************************************
     * 批量多卡多设备激活.
     * @param drCard
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardActivatePl(List<DrCardEquipment> drCards) {
        if(StringUtil.checkEmpty(drCards)){
            return new ApiTstatus<DrCard>(false, "卡信息不存在");
        }
        String KEY_STR = "激活";
        ApiTstatus<DrCard> rstatus = new ApiTstatus<DrCard>(true, KEY_STR + "成功");
        for (DrCardEquipment drCardEquipment : drCards) {
            ApiTstatus<DrCard> curRstatu = ajaxCardActivatePl(drCardEquipment, KEY_STR);
            if(!curRstatu.getStatus()){
                rstatus.setStatus(false);
                rstatus.setDatas(drCardEquipment);
                rstatus.setMsg(KEY_STR + "完成，存在失败记录！");
            }
        }
        return rstatus;
    }

    /**
     *  单卡多设备激活.
     * @param drCardEt 卡设备
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    private ApiTstatus<DrCard> ajaxCardActivatePl(DrCardEquipment drCardEt, String prefix) {
        List<String> drEids = Lists.newArrayList();
        List<DrEmentNo> drEmentNos = Lists.newArrayList();

        //设置缓存版本号
        DrCvo cvo = new DrCvo();
        cvo.setVersion(sysService.getDbCurRlong());
        cvo.setCurTime(sysService.getDbCurLong());
        cvo.getEtpIds().addAll(DrCard.getDrEids(drCardEt.getDrEmentNos()));

        //检查完成实现开卡功能
        if(StringUtil.isNotEmpty(drCardEt.getId())){
            drCardEt.setIsNewRecord(false);
        }
        //检查完成实现激活功能
        drCardEt.setDealVersion(cvo.getVersion());
        drCardEt.setStatus(DrCstatus.DC_NORMAL.getKey());
        drCardEt.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
        save(drCardEt);
        cvo.setCard(drCardEt);
        DrUtils.putCard(cvo);

        for (DrEmentNo dreNo : drCardEt.getDrEmentNos()) {
            if((drEids).contains(dreNo.getEtId())){
                continue;
            }else{
                drEids.add(dreNo.getEtId());
            }
            ApiTstatus<DrCard> rstatus = ajaxCardActivate(drCardEt, dreNo.getEtId());
            if(!rstatus.getStatus()){
                drEmentNos.add(dreNo);
            }
        }

        if(StringUtil.checkNotEmpty(drEmentNos)){
            return new ApiTstatus<DrCard>(true, prefix + "完成,失败(" + drEmentNos.size() + "/" + drCardEt.getDrEmentNos().size()+")条");
        }
        return new ApiTstatus<DrCard>(true, prefix + "成功");
    }

    /**
     * 激活.
     * @param drCard
     * @return
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardActivate(DrCard drCard, String equipmentId) {
        String prefix = "激活";
        //检查参数
        if((drCard == null) || StringUtil.isEmpty(drCard.getId())){
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡参数不存在");
        }

        //检查设备是否存在
        drCard = dao.get(drCard.getId());
        if((drCard == null)){
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡不存在");
        }

        if(StringUtil.isEmpty(equipmentId)){
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }

        //检查设备是否存在
        DrEquipment drEquipment = drEquipmentDao.get(equipmentId);
        if((drEquipment == null)){
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }

        List<DrEmentNo> drEmentNos = drCardErspaceService.findDrEmentNosByCid(drCard.getId());
        drCard.setDrNoStatus(DrKey.genDrStatus(DrCard.getDrEnos(drEmentNos, equipmentId)));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+equipmentId);
        System.out.println(drCard.getDrNoStatus()[0]+","+drCard.getDrNoStatus()[1]+","+drCard.getDrNoStatus()[2]+","+drCard.getDrNoStatus()[3]);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 保存表数据，然后调用硬件接口,避免由于写数据慢导致回调失败。
         */
        DrCardParam param = new DrCardParam(drCard, equipmentId, drCard.getDealVersion(), sysService.getDbCurRlong());

        /**
         * 设置卡设备状态为处理中.
         */
        DrCardErspace pentity = new DrCardErspace();
        pentity.setCard(new DrCard(drCard.getId()));
        pentity.setErspace(new DrEquipmentRspace(equipmentId, null));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(pentity);

        for (DrCardErspace drcerspace : drCardErspaces) {
            drcerspace.setVersion(param.getCeVersion());
            drcerspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
        }
        drCardErspaceService.updateByPl(drCardErspaces);

        ApiTstatus<DrCardRparam> rstatus = drCactivitService.runner(param);
        if(!rstatus.getStatus()){
            //检查完成实现开卡功能
            drCard.setDealVersion(null);
            drCard.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
            save(drCard);

            /**
             * 设置卡设备状态为处理中.
             */
            DrCardErspace pentity2 = new DrCardErspace();
            pentity2.setCard(new DrCard(drCard.getId()));
            pentity2.setErspace(new DrEquipmentRspace(equipmentId, null));
            List<DrCardErspace> drCardErspaces2 = drCardErspaceService.findListByg(pentity);
            for (DrCardErspace drcerspace : drCardErspaces2) {
                drcerspace.setVersion(null);
                drcerspace.setStatus(DrCdealStatus.DCD_NORMAL.getKey());
            }
            drCardErspaceService.updateByPl(drCardErspaces2);

            return new ApiTstatus<DrCard>(false, prefix + "失败", drCard);
        }
        //TODO 消息
        //drDeviceService.senMessage(prefix + "成功");
        return new ApiTstatus<DrCard>(true, prefix + "成功", drCard);
    }


    /****************************************************************************
     * 批量多卡挂失.
     * @param drCard
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardLossPl(List<DrCardEquipment> drCards) {
        if(StringUtil.checkEmpty(drCards)){
            return new ApiTstatus<DrCard>(false, "卡信息不存在");
        }
        String KEY_STR = "挂失";
        ApiTstatus<DrCard> rstatus = new ApiTstatus<DrCard>(true, KEY_STR + "成功");
        for (DrCardEquipment drCardEquipment : drCards) {
            ApiTstatus<DrCard> curRstatu = ajaxCardLossPl(drCardEquipment, KEY_STR);
            if(!curRstatu.getStatus()){
                rstatus.setStatus(false);
                rstatus.setDatas(drCardEquipment);
                rstatus.setMsg(KEY_STR + "失败！");
            }
        }
        return rstatus;
    }

    /**
     * 单卡多设备挂失.
     *
     * @param drCardEt 卡设备
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    private ApiTstatus<DrCard> ajaxCardLossPl(DrCardEquipment drCardEt, String prefix) {
        List<String> drEids = Lists.newArrayList();
        List<DrEmentNo> drEmentNos = Lists.newArrayList();

        //设置缓存版本号
        DrCvo cvo = new DrCvo();
        cvo.setVersion(sysService.getDbCurRlong());
        cvo.setCurTime(sysService.getDbCurLong());
        cvo.getEtpIds().addAll(DrCard.getDrEids(drCardEt.getDrEmentNos()));

        //检查完成实现开卡功能
        if(StringUtil.isNotEmpty(drCardEt.getId())){
            drCardEt.setIsNewRecord(false);
        }
        //检查完成实现激活功能
        drCardEt.setDealVersion(cvo.getVersion());
        drCardEt.setStatus(DrCstatus.DC_LOSS.getKey());
        drCardEt.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
        save(drCardEt);
        cvo.setCard(drCardEt);
        DrUtils.putCard(cvo);

        for (DrEmentNo dreNo : drCardEt.getDrEmentNos()) {
            if ((drEids).contains(dreNo.getEtId())) {
                continue;
            } else {
                drEids.add(dreNo.getEtId());
            }
            ApiTstatus<DrCard> rstatus = ajaxCardLoss(drCardEt, dreNo.getEtId());
            if (!rstatus.getStatus()) {
                drEmentNos.add(dreNo);
            }
        }

        if (StringUtil.checkNotEmpty(drEmentNos)) {
            return new ApiTstatus<DrCard>(true, prefix + "完成,失败(" + drEmentNos.size() + "/" + drCardEt.getDrEmentNos().size() + ")条");
        }
        return new ApiTstatus<DrCard>(true, prefix + "成功");
    }

    /**
     * 挂失.
     * @param drCard
     * @return
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardLoss(DrCard drCard, String equipmentId) {
        String prefix = "挂失";
        //检查参数
        if ((drCard == null) || StringUtil.isEmpty(drCard.getId())) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡参数不存在");
        }

        //检查设备是否存在
        drCard = dao.get(drCard.getId());
        if ((drCard == null)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡不存在");
        }

        if (StringUtil.isEmpty(equipmentId)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }

        //检查设备是否存在
        DrEquipment drEquipment = drEquipmentDao.get(equipmentId);
        if ((drEquipment == null)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }


        List<DrEmentNo> drEmentNos = drCardErspaceService.findDrEmentNosByCid(drCard.getId());
        drCard.setDrNoStatus(DrKey.genDrStatus(DrCard.getDrEnos(drEmentNos, equipmentId)));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+equipmentId);
        System.out.println(drCard.getDrNoStatus()[0]+","+drCard.getDrNoStatus()[1]+","+drCard.getDrNoStatus()[2]+","+drCard.getDrNoStatus()[3]);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 保存表数据，然后调用硬件接口,避免由于写数据慢导致回调失败。
         */
        DrCardParam param = new DrCardParam(drCard, equipmentId, drCard.getDealVersion(), sysService.getDbCurRlong());

        /**
         * 设置卡设备状态为处理中.
         */
        DrCardErspace pentity = new DrCardErspace();
        pentity.setCard(new DrCard(drCard.getId()));
        pentity.setErspace(new DrEquipmentRspace(equipmentId, null));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(pentity);
        for (DrCardErspace drcerspace : drCardErspaces) {
            drcerspace.setVersion(param.getCeVersion());
            drcerspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
        }
        drCardErspaceService.updateByPl(drCardErspaces);

        ApiTstatus<DrCardRparam> rstatus = drClossService.runner(param);
        if (!rstatus.getStatus()) {
            //检查完成实现开卡功能
            drCard.setDealVersion(null);
            drCard.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
            save(drCard);

            /**
             * 设置卡设备状态为处理中.
             */
            DrCardErspace pentity2 = new DrCardErspace();
            pentity2.setCard(new DrCard(drCard.getId()));
            pentity2.setErspace(new DrEquipmentRspace(equipmentId, null));
            List<DrCardErspace> drCardErspaces2 = drCardErspaceService.findListByg(pentity2);
            for (DrCardErspace drcerspace : drCardErspaces2) {
                drcerspace.setVersion(null);
                drcerspace.setStatus(DrCdealStatus.DCD_NORMAL.getKey());
            }
            drCardErspaceService.updateByPl(drCardErspaces2);
            return new ApiTstatus<DrCard>(false, prefix + "失败", drCard);
        }
        //TODO 消息
        //drDeviceService.senMessage(prefix + "成功");
        return new ApiTstatus<DrCard>(true, prefix + "成功", drCard);
    }

    /****************************************************************************
     * 批量多卡多设备黑名单.
     * @param drCard
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardBackListPl(List<DrCardEquipment> drCards) {
        if (StringUtil.checkEmpty(drCards)) {
            return new ApiTstatus<DrCard>(false, "卡信息不存在");
        }
        String KEY_STR = "加入黑名单";
        ApiTstatus<DrCard> rstatus = new ApiTstatus<DrCard>(true, KEY_STR + "成功");
        for (DrCardEquipment drCardEquipment : drCards) {
            ApiTstatus<DrCard> curRstatu = ajaxCardBackListPl(drCardEquipment, KEY_STR);
            if (!curRstatu.getStatus()) {
                rstatus.setStatus(false);
                rstatus.setDatas(drCardEquipment);
                rstatus.setMsg(KEY_STR + "失败！");
            }
        }
        return rstatus;
    }

    /**
     * 单卡多设备黑名单.
     *
     * @param drCardEt 卡设备
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    private ApiTstatus<DrCard> ajaxCardBackListPl(DrCardEquipment drCardEt, String prefix) {
        List<String> drEids = Lists.newArrayList();
        List<DrEmentNo> drEmentNos = Lists.newArrayList();

        //设置缓存版本号
        DrCvo cvo = new DrCvo();
        cvo.setVersion(sysService.getDbCurRlong());
        cvo.setCurTime(sysService.getDbCurLong());
        cvo.getEtpIds().addAll(DrCard.getDrEids(drCardEt.getDrEmentNos()));

        //检查完成实现开卡功能
        if(StringUtil.isNotEmpty(drCardEt.getId())){
            drCardEt.setIsNewRecord(false);
        }
        //检查完成实现激活功能
        drCardEt.setDealVersion(cvo.getVersion());
        drCardEt.setStatus(DrCstatus.DC_BLACK_LIST.getKey());
        drCardEt.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
        save(drCardEt);
        cvo.setCard(drCardEt);
        DrUtils.putCard(cvo);

        for (DrEmentNo dreNo : drCardEt.getDrEmentNos()) {
            if ((drEids).contains(dreNo.getEtId())) {
                continue;
            } else {
                drEids.add(dreNo.getEtId());
            }
            ApiTstatus<DrCard> rstatus = ajaxCardBackList(drCardEt, dreNo.getEtId());
            if (!rstatus.getStatus()) {
                drEmentNos.add(dreNo);
            }
        }

        if (StringUtil.checkNotEmpty(drEmentNos)) {
            return new ApiTstatus<DrCard>(true, prefix + "完成,失败(" + drEmentNos.size() + "/" + drCardEt.getDrEmentNos().size() + ")条");
        }
        return new ApiTstatus<DrCard>(true, prefix + "成功");
    }

    /**
     * 黑名单.
     *
     * @param drCard
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardBackList(DrCard drCard, String equipmentId) {
        String prefix = "加入黑名单";
        //检查参数
        if ((drCard == null) || StringUtil.isEmpty(drCard.getId())) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡参数不存在");
        }

        //检查设备是否存在
        drCard = dao.get(drCard.getId());
        if ((drCard == null)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡不存在");
        }

        if (StringUtil.isEmpty(equipmentId)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }

        //检查设备是否存在
        DrEquipment drEquipment = drEquipmentDao.get(equipmentId);
        if ((drEquipment == null)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,设备不存在");
        }


        List<DrEmentNo> drEmentNos = drCardErspaceService.findDrEmentNosByCid(drCard.getId());
        drCard.setDrNoStatus(DrKey.genDrStatus(DrCard.getDrEnos(drEmentNos, equipmentId)));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+equipmentId);
        System.out.println(drCard.getDrNoStatus()[0]+","+drCard.getDrNoStatus()[1]+","+drCard.getDrNoStatus()[2]+","+drCard.getDrNoStatus()[3]);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 保存表数据，然后调用硬件接口,避免由于写数据慢导致回调失败。
         */
        DrCardParam param = new DrCardParam(drCard, equipmentId, drCard.getDealVersion(), sysService.getDbCurRlong());

        /**
         * 设置卡设备状态为处理中.
         */
        DrCardErspace pentity = new DrCardErspace();
        pentity.setCard(new DrCard(drCard.getId()));
        pentity.setErspace(new DrEquipmentRspace(equipmentId, null));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(pentity);
        for (DrCardErspace drcerspace : drCardErspaces) {
            drcerspace.setVersion(param.getCeVersion());
            drcerspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
        }
        drCardErspaceService.updateByPl(drCardErspaces);

        ApiTstatus<DrCardRparam> rstatus = drClossService.runner(param);
        if (!rstatus.getStatus()) {
            /**
             * 设置卡状态为处理中和版本号.
             */
            drCard.setDealVersion(null);
            drCard.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
            save(drCard);

            /**
             * 设置卡设备状态为处理中.
             */
            DrCardErspace pentity2 = new DrCardErspace();
            pentity2.setCard(new DrCard(drCard.getId()));
            pentity2.setErspace(new DrEquipmentRspace(equipmentId, null));
            List<DrCardErspace> drCardErspaces2 = drCardErspaceService.findListByg(pentity2);
            for (DrCardErspace drcerspace : drCardErspaces2) {
                drcerspace.setVersion(null);
                drcerspace.setStatus(DrCdealStatus.DCD_NORMAL.getKey());
            }
            drCardErspaceService.updateByPl(drCardErspaces2);
            return new ApiTstatus<DrCard>(false, prefix + "失败", drCard);
        }
        //TODO 消息
        //drDeviceService.senMessage(prefix + "成功");
        return new ApiTstatus<DrCard>(true, prefix + "成功", drCard);
    }

    /****************************************************************************
     * 批量多卡多设备退卡.
     * @param drCard
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardBackPl(List<DrCardEquipment> drCards) {
        if (StringUtil.checkEmpty(drCards)) {
            return new ApiTstatus<DrCard>(false, "卡信息不存在");
        }
        String KEY_STR = "退卡";
        ApiTstatus<DrCard> rstatus = new ApiTstatus<DrCard>(true, KEY_STR + "成功");
        for (DrCardEquipment card : drCards) {
            ApiTstatus<DrCard> curRstatu = ajaxCardBackPl(card, KEY_STR);
            if (!curRstatu.getStatus()) {
                rstatus.setStatus(false);
                rstatus.setDatas(card);
                rstatus.setMsg(KEY_STR + "失败！");
            }
        }
        return rstatus;
    }

    /**
     * 单卡多设备退卡.
     *
     * @param drCardEt 卡设备
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    private ApiTstatus<DrCard> ajaxCardBackPl(DrCardEquipment drCardEt, String prefix) {
        List<String> drEids = Lists.newArrayList();
        List<DrEmentNo> drEmentNos = Lists.newArrayList();

        //设置缓存版本号
        DrCvo cvo = new DrCvo();
        cvo.setVersion(sysService.getDbCurRlong());
        cvo.setCurTime(sysService.getDbCurLong());
        cvo.getEtpIds().addAll(DrCard.getDrEids(drCardEt.getDrEmentNos()));

        //检查完成实现开卡功能
        if(StringUtil.isNotEmpty(drCardEt.getId())){
            drCardEt.setIsNewRecord(false);
        }
        //检查完成实现激活功能
        drCardEt.setDealVersion(cvo.getVersion());
        drCardEt.setStatus(DrCstatus.DC_BLACK.getKey());
        drCardEt.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
        save(drCardEt);
        cvo.setCard(drCardEt);
        DrUtils.putCard(cvo);

        for (DrEmentNo dreNo : drCardEt.getDrEmentNos()) {
            if ((drEids).contains(dreNo.getEtId())) {
                continue;
            } else {
                drEids.add(dreNo.getEtId());
            }
            ApiTstatus<DrCard> rstatus = ajaxCardBack(drCardEt, dreNo.getEtId());
            if (!rstatus.getStatus()) {
                drEmentNos.add(dreNo);
            }
        }

        if (StringUtil.checkNotEmpty(drEmentNos)) {
            return new ApiTstatus<DrCard>(true, prefix + "完成,失败(" + drEmentNos.size() + "/" + drCardEt.getDrEmentNos().size() + ")条");
        }
        return new ApiTstatus<DrCard>(true, prefix + "成功");
    }

    /**
     * 单卡单设备退卡.
     * @param drCard
     * @return
     */
    @Transactional(readOnly = false)
    public ApiTstatus<DrCard> ajaxCardBack(DrCard drCard, String equipmentId) {
        String prefix = "退卡";
        //检查参数
        if ((drCard == null) || StringUtil.isEmpty(drCard.getId())) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡参数不存在");
        }

        //检查设备是否存在
        drCard = dao.get(drCard.getId());
        if ((drCard == null)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,卡不存在");
        }

        DrCardErspace entity = new DrCardErspace();
        entity.setCard(new DrCard(drCard.getId()));
        entity.setErspace(new DrEquipmentRspace(equipmentId, null));
        List<DrCardErspace> drCerspaces = drCardErspaceService.findListByg(entity);
        if (StringUtil.checkEmpty(drCerspaces)) {
            return new ApiTstatus<DrCard>(false, prefix + "失败,查询不到设备");
        }

        List<DrEmentNo> drEmentNos = drCardErspaceService.findDrEmentNosByCid(drCard.getId());
        drCard.setDrNoStatus(DrKey.genDrStatus(DrCard.getDrEnos(drEmentNos, equipmentId)));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+equipmentId);
        System.out.println(drCard.getDrNoStatus()[0]+","+drCard.getDrNoStatus()[1]+","+drCard.getDrNoStatus()[2]+","+drCard.getDrNoStatus()[3]);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 保存表数据，然后调用硬件接口,避免由于写数据慢导致回调失败。
         */
        DrCardParam param = new DrCardParam(drCard, equipmentId, drCard.getDealVersion(), sysService.getDbCurRlong());

        /**
         * 设置卡设备状态为处理中.
         */
        DrCardErspace pentity = new DrCardErspace();
        pentity.setCard(new DrCard(drCard.getId()));
        pentity.setErspace(new DrEquipmentRspace(equipmentId, null));
        List<DrCardErspace> drCardErspaces = drCardErspaceService.findListByg(pentity);
        for (DrCardErspace drcerspace : drCardErspaces) {
            drcerspace.setVersion(param.getCeVersion());
            drcerspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
        }
        drCardErspaceService.updateByPl(drCardErspaces);

        ApiTstatus<DrCardRparam> rstatus = drCbackService.runner(param);
        if (!rstatus.getStatus()) {
            //检查完成实现退卡功能
            drCard.setDealVersion(null);
            drCard.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
            save(drCard);

            /**
             * 设置卡设备状态为处理中.
             */
            DrCardErspace pentity2 = new DrCardErspace();
            pentity2.setCard(new DrCard(drCard.getId()));
            pentity2.setErspace(new DrEquipmentRspace(equipmentId, null));
            List<DrCardErspace> drCardErspaces2 = drCardErspaceService.findListByg(pentity2);
            for (DrCardErspace drcerspace : drCardErspaces2) {
                drcerspace.setVersion(null);
                drcerspace.setStatus(DrCdealStatus.DCD_NORMAL.getKey());
            }
            drCardErspaceService.updateByPl(drCardErspaces2);

            //TODO 消息
            //drDeviceService.senMessage(prefix + "成功");
            return new ApiTstatus<DrCard>(true, prefix + "成功", drCard);
        }
        return new ApiTstatus<DrCard>(false, prefix + "失败", drCard);
    }

    /****************************************************************************
     * 批量多卡多设备重新开卡.
     * @param drCard
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<List<DrCard>> ajaxCardRepublishPl(List<DrCardEquipment> drCards) {
        if (StringUtil.checkEmpty(drCards)) {
            return new ApiTstatus<List<DrCard>>(false, "卡信息不存在");
        }
        return ajaxCardPublishPl(drCards);
    }

    /**
     * 根据姓名、学号、手机号获取卡.
     * @return
     */
    public List<DrCard> findListCardByNameOrNOorMobile(String param) {
        return dao.findListCardByNameOrNOorMobile(param);
    }
}