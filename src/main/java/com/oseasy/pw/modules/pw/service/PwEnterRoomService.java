package com.oseasy.pw.modules.pw.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.modules.pw.dao.PwApplyRecordDao;
import com.oseasy.pw.modules.pw.dao.PwEnterDetailDao;
import com.oseasy.pw.modules.pw.dao.PwEnterRoomDao;
import com.oseasy.pw.modules.pw.dao.PwEnterRoomRecordDao;
import com.oseasy.pw.modules.pw.dao.PwRoomDao;
import com.oseasy.pw.modules.pw.entity.PwApplyRecord;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwEnterRoomRecord;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.vo.PwEnterAuditEnum;
import com.oseasy.pw.modules.pw.vo.PwEnterBgremarks;
import com.oseasy.pw.modules.pw.vo.PwErecordType;
import com.oseasy.pw.modules.pw.vo.PwEroom;
import com.oseasy.pw.modules.pw.vo.PwEroomMap;
import com.oseasy.pw.modules.pw.vo.PwEroomParam;
import com.oseasy.pw.modules.pw.vo.PwEroomStatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻场地分配Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwEnterRoomService extends CrudService<PwEnterRoomDao, PwEnterRoom> {
    @Autowired
    PwEnterService pwEnterService;
    @Autowired
    PwEnterDetailDao pwEnterDetailDao;
    @Autowired
    PwRoomDao pwRoomDao;
    @Autowired
    private PwApplyRecordDao pwApplyRecordDao;
    @Autowired
    private PwEnterRoomRecordDao pwEnterRoomRecordDao;
    @Autowired
    OaNotifyService oaNotifyService;
    public PwEnterRoom get(String id) {
        return super.get(id);
    }

    public List<PwEnterRoom> findList(PwEnterRoom pwEnterRoom) {
        return super.findList(pwEnterRoom);
    }

    public Page<PwEnterRoom> findPage(Page<PwEnterRoom> page, PwEnterRoom pwEnterRoom) {
        return super.findPage(page, pwEnterRoom);
    }

    @Transactional(readOnly = false)
    public void save(PwEnterRoom pwEnterRoom) {
        super.save(pwEnterRoom);
    }

    /**
     * 批量分配场地.
     * @param pwEnterRoom
     */
    @Transactional(readOnly = false)
    public Boolean saveEnter(PwEroomParam param) {
        if ((StringUtil.isEmpty(param.getId())) || (StringUtil.checkEmpty(param.getErooms()))) {
            return false;
        }

        PwEroomMap map;
        PwEnter pwEnter = pwEnterService.get(param.getId());
        if ((pwEnter != null)) {
            /**
             * 1、保存分配信息.
             */
            map = PwEnterRoom.genByIds(param);
            dao.insertPl(map.getErooms());

            /**
             * 2、更新房间剩余工位信息.
             */
            PwRoom ppwRoom = new PwRoom();
            ppwRoom.setIds(map.getIds());
            List<PwRoom> pwRooms = pwRoomDao.findList(ppwRoom);
            for (PwRoom curPwRoom : pwRooms) {
                for (PwEroom curPwEroom : param.getErooms()) {
                    if((curPwRoom.getId()).equals(curPwEroom.getId())){
                        curPwRoom.setRemaindernum((curPwRoom.getRemaindernum() > curPwEroom.getNum()) ? (curPwRoom.getRemaindernum() - curPwEroom.getNum()) : 0);
                    }
                }
            }
            pwRoomDao.updatePLRemaindernum(pwRooms);

            /**
             * 3、更新入驻状态.
             */
            pwEnter.setRestatus(PwEroomStatus.PER_YFP.getKey());
            pwEnterService.update(pwEnter);

            User apply_User = UserUtils.getUser();
            User rec_User = new User();
            rec_User.setId(pwEnter.getApplicant().getId());

            oaNotifyService.sendOaNotifyByType(apply_User, rec_User,  OaNotify.Type_Enum.TYPE30.getName(),
                    "你的房间已经被管理员分配", OaNotify.Type_Enum.TYPE30.getValue(), pwEnter.getId());
            pwEnterRoomRecordDao.insertPL(PwEnterRoomRecord.converts(pwEnter.getId(), map.getErooms(), saveErRecord(pwEnter), PwErecordType.ASS.getKey()));
            return true;
        }
        return false;
    }

    /**
     * 场地修改，保存场地记录.
     * @param pwEnter
     */
    @Transactional(readOnly = false)
    private PwApplyRecord saveErRecord(PwEnter pwEnter) {
        PwApplyRecord pwApplyRecord=new PwApplyRecord();
        pwApplyRecord.setIsNewRecord(true);
        pwApplyRecord.setId(IdGen.uuid());
        pwApplyRecord.setEid(pwEnter.getId());
        pwApplyRecord.setType(PwEnterBgremarks.R110.getKey());
        pwApplyRecord.setStatus(PwEnterAuditEnum.SHBTG.getValue());
        pwApplyRecord.setDeclareId(UserUtils.getUser().getId());
        pwApplyRecord.setDeclareTime(new Date());
        pwApplyRecord.setCreateBy(UserUtils.getUser());
        pwApplyRecord.setCreateDate(new Date());
        pwApplyRecord.setUpdateBy(UserUtils.getUser());
        pwApplyRecord.setUpdateDate(new Date());
        pwApplyRecord.setDelFlag(Const.NO);
        pwApplyRecordDao.insert(pwApplyRecord);
        return pwApplyRecord;
    }

    /**
     * 批量取消分配场地.
     * @param pwEnterRoom
     */
    @Transactional(readOnly = false)
    public Boolean cancelEnter(PwEroomParam param) {
        return cancelEnter(param, false);
    }
    public Boolean cancelEnter(PwEroomParam param, Boolean isExit) {
        if ((StringUtil.isEmpty(param.getId())) || (StringUtil.checkEmpty(param.getErooms()))) {
            return false;
        }

        PwEnter pwEnter = pwEnterService.getRoom(new PwEnter(param.getId()));
        if ((pwEnter != null) && StringUtil.checkNotEmpty(pwEnter.getErooms())) {
            List<String> rids = Lists.newArrayList();
            List<PwRoom> qxRooms = Lists.newArrayList();
            List<PwRoom> yfpRooms = Lists.newArrayList();
            List<PwEnterRoom> qxErooms = Lists.newArrayList();
            for (PwEroom room : param.getErooms()) {
                if(StringUtil.isNotEmpty(room.getId())){
                    rids.add(room.getId());
                }

                for (PwEnterRoom eroom : pwEnter.getErooms()) {
                    PwRoom curPwRoom = eroom.getPwRoom();
                    if((room.getId()).equals(curPwRoom.getId())){
                        curPwRoom.setRemaindernum(((curPwRoom.getRemaindernum() + eroom.getNum()) >= curPwRoom.getNum()) ? (curPwRoom.getNum()) : (curPwRoom.getRemaindernum() + eroom.getNum()));
                        qxRooms.add(curPwRoom);
                        qxErooms.add(eroom);
                    }else{
                        yfpRooms.add(curPwRoom);
                    }
                }
            }

            if(StringUtil.isEmpty(param.getId()) || StringUtil.checkEmpty(rids) || StringUtil.checkEmpty(qxRooms) || StringUtil.checkEmpty(qxErooms)){
                return false;
            }

            //删除PwEnterRoom 关联
            dao.deletePLWLByErids(param.getId(), rids);

            //更新房间PwRoom剩余工位数
            pwRoomDao.updatePLRemaindernum(qxRooms);

            //更新入驻的Restatus（检查是否还有已分配的房间确定状态）
            if(StringUtil.checkNotEmpty(yfpRooms)){
                pwEnter.setRestatus(PwEroomStatus.PER_YFP.getKey());
            }else{
                pwEnter.setRestatus(PwEroomStatus.PER_DFP.getKey());
            }
            pwEnterService.update(pwEnter);
            pwEnterRoomRecordDao.insertPL(PwEnterRoomRecord.converts(pwEnter.getId(), qxErooms, saveErRecord(pwEnter), PwErecordType.CANCEL.getKey(), isExit));
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public Boolean deletePLWLByErid(PwEnterRoom pwEnterRoom) {
        if (pwEnterRoom == null) {
            return false;
        }

        if ((pwEnterRoom.getPwEnter() == null) || StringUtil.isEmpty(pwEnterRoom.getPwEnter().getId())) {
            return false;
        }

        if ((pwEnterRoom.getPwRoom() == null) || StringUtil.isEmpty(pwEnterRoom.getPwRoom().getId())) {
            return false;
        }

        PwEnter pwEnter = pwEnterService.get(pwEnterRoom.getPwEnter());
        if (pwEnter != null) {
            dao.deletePLWLByErid(pwEnterRoom);

            pwEnter.setRestatus(PwEroomStatus.PER_DFP.getKey());
            pwEnter.setRestatus(PwEroomStatus.PER_YFP.getKey());
            pwEnterService.update(pwEnter);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public ApiResult checkDeleteByRid(PwEnterRoom pwEnterRoom) {
        if ((pwEnterRoom == null) || (pwEnterRoom.getPwRoom() == null) || StringUtil.isEmpty(pwEnterRoom.getPwRoom().getId())) {
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "房间取消分配失败，参数不对！");
        }
        List<PwEnterRoom> pwEnterRooms = dao.findList(pwEnterRoom);
        if ((pwEnterRooms == null) || (pwEnterRooms.size() <= 0)) {
            return ApiResult.success(ApiConst.CODE_REQUEST_SUCCESS, "房间可以取消分配！");
        } else {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, "房间被使用无法取消分配");
        }
    }

    @Transactional(readOnly = false)
    public Boolean deletePLWLByRid(PwEnterRoom pwEnterRoom) {
        dao.deletePLWLByRid(pwEnterRoom);
        return true;
    }

    @Transactional(readOnly = false)
    public void delete(PwEnterRoom pwEnterRoom) {
        super.delete(pwEnterRoom);
    }

    @Transactional(readOnly = false)
    public void deleteWL(PwEnterRoom pwEnterRoom) {
        dao.deleteWL(pwEnterRoom);
    }

    @Transactional(readOnly = false)
    public void deletePLWL(List<String> ids) {
        dao.deletePLWL(ids);
    }

    public List<PwEnterRoom> findListByinIds(List<String> ids) {
        return dao.findListByinIds(ids);
    }
}