package com.oseasy.pw.modules.pw.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.pw.common.config.RoomUseStatus;
import com.oseasy.pw.modules.pw.dao.PwEnterDao;
import com.oseasy.pw.modules.pw.dao.PwRoomDao;
import com.oseasy.pw.modules.pw.dao.PwSpaceDao;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.entity.PwSpaceRoom;
import com.oseasy.pw.modules.pw.vo.PwAppointmentVo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 房间Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwRoomService extends CrudService<PwRoomDao, PwRoom> {

    @Autowired
    PwSpaceDao pwSpaceDao;
    @Autowired
    private PwRoomDao pwRoomDao;
    @Autowired
    private PwFassetsService pwFassetsService;
    @Autowired
    private PwAppointmentService pwAppointmentService;
    @Autowired
    private PwEnterRoomService pwEnterRoomService;
    @Autowired
    private PwEnterDao pwEnterDao;
    @Autowired
    private PwEnterService pwEnterService;
    //房间类型字典id值
    public static final String ROOMTYPEDICT="c99890ec11f148bfba5dcc2415c5b831";
    //房间默认色值
    public static final String ROOMDEFAULTCOLOR="e9432d";
    public static final Integer STATUS3 = 3;
    public static final Integer STATUS2 = 2;
    /**
     * 使用中和待使用
     */
    public static final Integer STATUS1 = 1;
    public static final String HISTORYDATA = "2";
    /**
     * 当前和历史
     */
    public static final String NOWDATA = "1";

    public PwRoom get(String id) {
        return super.get(id);
    }

    public List<PwRoom> findList(PwRoom pwRoom) {
        return super.findList(pwRoom);
    }

    public Page<PwRoom> findPage(Page<PwRoom> page, PwRoom pwRoom) {
        return super.findPage(page, pwRoom);
    }

    private void pwRoomSetIds(PwRoom pwRoom){
        if ((pwRoom.getPwSpace() != null) && StringUtil.isNotEmpty(pwRoom.getPwSpace().getId())) {
            PwSpace pwSpace = pwSpaceDao.get(pwRoom.getPwSpace());
            PwSpace ppwSpace = new PwSpace();
            ppwSpace.setParentIds(pwSpace.getParentIds() + pwSpace.getId() + StringUtil.DOTH);
            List<PwSpace> pwSpaces = pwSpaceDao.findList(ppwSpace);
            List<String> pids = Lists.newArrayList();
            pids.add(pwSpace.getId());
            for (PwSpace pspace : pwSpaces) {
                pids.add(pspace.getId());
            }
            pwRoom.setIds(pids);
        }
    }
    private void queryByKeyRoom(Page<PwRoom> page, PwRoom pwRoom,List<PwRoom> list){
        List<PwRoom> pwRoomList = Lists.newArrayList();
        if(null != pwRoom.getQuerystatus()){
            for(PwRoom ps : list){
                if(pwRoom.getQuerystatus().equals(ps.getQuerystatus())){
                    pwRoomList.add(ps);
                }
            }
        }

        if(null == pwRoom.getQuerystatus()){

            page.setList(list);
        }else{
            page.setCount(pwRoomList.size());
            page.setList(pwRoomList);
        }
    }
    private void queryByKey(Page<PwRoom> page, PwRoom pwRoom,List<PwRoom> list){
        List<PwRoom> pwRoomList = Lists.newArrayList();
        if(null != pwRoom.getQuerystatus()){
            for(PwRoom ps : list){
                if(pwRoom.getQuerystatus().equals(ps.getQuerystatus())){
                    pwRoomList.add(ps);
                }
            }
        }
        if(StringUtil.isNotEmpty(pwRoom.getKeys()) && null != pwRoom.getKeys()){
            if(pwRoomList.size()>0){
                List<PwRoom> pwRoomList1 = Lists.newArrayList();
                for(PwRoom pr : pwRoomList){
                    if(pr.getName().contains(pwRoom.getKeys()) || pr.getPwSpace().getName().contains(pwRoom.getKeys())
                            || pr.getPwSpace().getParent().getName().contains(pwRoom.getKeys())
                            || pr.getPwSpace().getParent().getParent().getName().contains(pwRoom.getKeys())
                            || CoreUtils.getOffice(CoreIds.NCE_SYS_TREE_ROOT.getId()).getName().contains(pwRoom.getKeys())){
                        pwRoomList1.add(pr);
                    }
                }
                pwRoomList = pwRoomList1;
            }else{
                for(PwRoom pr : list){
                    if(pr.getName().contains(pwRoom.getKeys()) || pr.getPwSpace().getName().contains(pwRoom.getKeys())
                            || pr.getPwSpace().getParent().getName().contains(pwRoom.getKeys())
                            || pr.getPwSpace().getParent().getParent().getName().contains(pwRoom.getKeys())
                            || CoreUtils.getOffice(CoreIds.NCE_SYS_TREE_ROOT.getId()).getName().contains(pwRoom.getKeys())){
                        pwRoomList.add(pr);
                    }
                }
            }
        }
        if(null == pwRoom.getQuerystatus() && null ==pwRoom.getKeys()){

            page.setList(list);
        }else{
            page.setCount(pwRoomList.size());
            page.setList(pwRoomList);
        }
    }

    public List<PwRoom> findListByJL(PwRoom pwRoom) {
        pwRoomSetIds(pwRoom);
        return dao.findListByJL(pwRoom);
    }

    public Page<PwRoom> findPageByJL(Page<PwRoom> page, PwRoom pwRoom) {
        pwRoom.setPage(page);
        List<PwRoom> list =  isEdit(findListByJL(pwRoom));
        queryByKeyRoom(page, pwRoom, list);
        return page;
    }

    public List<PwRoom> isEdit(List<PwRoom> list){
        for(PwRoom proom : list) {
            PwEnterRoom pwEnterRoom = new PwEnterRoom(new PwRoom(proom.getId()));
            PwAppointment pa = new PwAppointment();
            pa.setPwRoom(proom);
            //已分配的不能编辑
            List<PwEnterRoom> pwEnterRoomList = pwEnterRoomService.findList(pwEnterRoom);
            if(pwEnterRoomList.size()>0){
                for(PwEnterRoom pwEnterRoo : pwEnterRoomList){
                    if (pwEnterRoom.getPwRoom().getId().equals(proom.getId())) {
                        proom.setIsEdit(Const.NO);
                    }else{
                        proom.setIsEdit(Const.YES);
                    }
                }
            }else{
                proom.setIsEdit(Const.YES);
            }
            //已预约的不能编辑
            List<PwAppointment> pwAppointmentList = pwAppointmentService.findRoomOrderDetailList(pa, "and a.`status`=1", Const.Z1);
            if(pwAppointmentList.size()>0){
                for(PwAppointment pat: pwAppointmentList) {
                    if(pat.getPwRoom().getId().equals(proom.getId())){
                        proom.setIsEdit(Const.NO);
                    } else if(proom.getIsEdit().equals(Const.YES)) {
                        proom.setIsEdit(Const.YES);
                    }
                }
            }else{
                proom.setIsEdit(Const.YES);
            }
        }
        return list;
    }


    public Page<PwRoom> findOrderRoomPage(Page<PwRoom> page, PwRoom pwRoom){
        pwRoom.setPage(page);
        pwRoomSetIds(pwRoom);
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.ORDERUSINGROOM.getStatus())){
            pwRoom.setOrderusing(2);
        }
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.ORDERNOTUSEROOM.getStatus())){
            pwRoom.setOrdernotuse(2);
        }
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.NOTORDERROOM.getStatus())){
            pwRoom.setOrderusing(1);
            pwRoom.setOrdernotuse(1);
        }
        //预约房间列表
        List<PwRoom> list =pwRoomDao.findOrderRoomPage(pwRoom);
        for(PwRoom room : list){
            if(room.getOrderusing() > 0 ){
                room.setQuerystatus(RoomUseStatus.ORDERUSINGROOM.getStatus());
            }else if(room.getOrdernotuse() > 0 ){
                room.setQuerystatus(RoomUseStatus.ORDERNOTUSEROOM.getStatus());
            }else{
                room.setQuerystatus(RoomUseStatus.NOTORDERROOM.getStatus());
            }
        }
        page.setList(list);
        return page;
    }

    public Page<PwRoom> findAllotRoomPage(Page<PwRoom> page, PwRoom pwRoom){
        pwRoomSetIds(pwRoom);
        List<PwRoom> list = pwRoomDao.findAllotRoomPage(pwRoom);
        pwRoom.setPage(page);
        pwRoom.setIsAssign(Const.YES);
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.ALLOTEDFULLROOM.getStatus())){
            pwRoom.setFullroom(2);
        }
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.ALLOTEDNOTFULLROOM.getStatus())){
            pwRoom.setNotfullroom(2);
        }
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.NOTALLOTROOM.getStatus())){
            pwRoom.setFullroom(1);
            pwRoom.setNotfullroom(1);
        }
        List<PwRoom> pwList = pwRoomDao.findListByJL(pwRoom);
        for(PwRoom prooms : pwList){
            if(prooms.getFullroom() > 0 ){
                prooms.setQuerystatus(RoomUseStatus.ALLOTEDFULLROOM.getStatus());
            }else if(prooms.getNotfullroom() > 0 ){
                prooms.setQuerystatus(RoomUseStatus.ALLOTEDNOTFULLROOM.getStatus());
            }else{
                prooms.setQuerystatus(RoomUseStatus.NOTALLOTROOM.getStatus());
            }
            prooms.setEnterteamcount(0);
            for(PwRoom pr : list){
                if(prooms.getId().equals(pr.getId())){
                    prooms.setEnterteamcount(pr.getEnterteamcount());
                }
            }
        }
        page.setList(pwList);
        return page;
    }
    public Page<PwRoom> findOtherRoomPage(Page<PwRoom> page, PwRoom pwRoom){
        pwRoom.setPage(page);
        pwRoomSetIds(pwRoom);
        List<PwRoom> list = pwRoomDao.findOtherRoomPage(pwRoom);
        queryByKey(page, pwRoom, list);
        return page;
    }
    public Page<PwRoom> findRoomUsePage(Page<PwRoom> page, PwRoom pwRoom){
        pwRoom.setPage(page);
        pwRoomSetIds(pwRoom);
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.ORDEREDROOM.getStatus())){
            pwRoom.setIsYY(2);
        }
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.ALLOTEDROOM.getStatus())){
            pwRoom.setIsFP(2);
        }
        if(null != pwRoom.getQuerystatus() && pwRoom.getQuerystatus().equals(RoomUseStatus.OTHERSTATUS.getStatus())){
            pwRoom.setIsFP(1);
            pwRoom.setIsYY(1);
        }
        List<PwRoom> list = pwRoomDao.findRoomUseList(pwRoom);
        for(PwRoom room :list){
            if(room.getIsYY() > 0 ){
                room.setQuerystatus(RoomUseStatus.ORDEREDROOM.getStatus());
            }else if(room.getIsFP() > 0 ){
                room.setQuerystatus(RoomUseStatus.ALLOTEDROOM.getStatus());
            }else{
                room.setQuerystatus(RoomUseStatus.OTHERSTATUS.getStatus());
            }
        }
        page.setList(list);
        return page;
    }

    public List<PwRoom> queryByRoomList(PwRoom pwRoom){
        pwRoomSetIds(pwRoom);
        return queryRoomList(pwRoomDao.findListByJL(pwRoom), pwRoom);
    }

    public List<PwRoom> queryRoomList(List<PwRoom> entitys,PwRoom pwRoom){
        List<PwRoom> pwList = getQueryStatus(entitys);
        List<PwRoom> pwRoomList2 = Lists.newArrayList();
        List<PwRoom> pwList2 = getQueryStatus(pwRoomDao.findListByJL(new PwRoom()));
        if(null != pwRoom.getQuerystatus()){
            for(PwRoom ps : pwList2){
                if(pwRoom.getQuerystatus().equals(ps.getQuerystatus())){
                    pwRoomList2.add(ps);
                }
            }
        }
        if(StringUtil.isNotEmpty(pwRoom.getKeys())){
            for(PwRoom pr : pwList2){
                if(pr.getName().contains(pwRoom.getKeys()) || pr.getPwSpace().getName().contains(pwRoom.getKeys())
                        || pr.getPwSpace().getParent().getName().contains(pwRoom.getKeys())
                        || pr.getPwSpace().getParent().getParent().getName().contains(pwRoom.getKeys())
                        || CoreUtils.getOffice(CoreIds.NCE_SYS_TREE_ROOT.getId()).getName().contains(pwRoom.getKeys())){
                    pwRoomList2.add(pr);
                }
            }
        }
        if(pwRoomList2.size()>0){
            pwList = pwRoomList2;
        }
        return pwList;
    }

    public List<PwRoom> getQueryStatus(List<PwRoom> pwList){
        //预约成功的列表
        PwAppointment pwa = new PwAppointment();
        pwa.setStatus(Const.YES);
        List<PwAppointment> pwaList = pwAppointmentService.findList(pwa);
        Date date = new Date();
        for(PwRoom prooms : pwList){
            if( !(prooms.getRemaindernum().equals(prooms.getNum())) && prooms.getIsAssign().equals(Const.YES)){
                prooms.setQuerystatus(RoomUseStatus.ALLOTEDROOM.getStatus());
            }
            for(PwAppointment pa : pwaList){
                if(prooms.getId().equals(pa.getPwRoom().getId()) && pa.getStartDate().getTime() <= date.getTime()
                        && pa.getEndDate().getTime() >= date.getTime() ){ //使用中
                    prooms.setQuerystatus(RoomUseStatus.ORDEREDROOM.getStatus());
                }
            }
            if(null == prooms.getQuerystatus()){
                prooms.setQuerystatus(RoomUseStatus.OTHERSTATUS.getStatus());
            }
        }
        return pwList;
    }

    /**
     * 查询可分配场地.
     *
     * @param pwRoom
     * @return
     */
    public List<PwRoom> findListByJLKfpCD(PwRoom pwRoom) {
        if ((pwRoom.getPwSpace() != null) && StringUtil.isNotEmpty(pwRoom.getPwSpace().getId())) {
            PwSpace pwSpace = pwSpaceDao.get(pwRoom.getPwSpace());
            if(pwSpace == null){
              return Lists.newArrayList();
            }
            PwSpace ppwSpace = new PwSpace();
            ppwSpace.setParentIds(pwSpace.getParentIds() + pwSpace.getId() + StringUtil.DOTH);
            List<PwSpace> pwSpaces = pwSpaceDao.findList(ppwSpace);
            List<String> pids = Lists.newArrayList();
            pids.add(pwSpace.getId());
            for (PwSpace pspace : pwSpaces) {
                pids.add(pspace.getId());
            }
            pwRoom.setIds(pids);
        }
        return dao.findListByJLKfpCD(pwRoom);
    }

    public Page<PwRoom> findPageByJLKfpCD(Page<PwRoom> page, PwRoom pwRoom) {
        pwRoom.setPage(page);
        page.setList(findListByJLKfpCD(pwRoom));
        return page;
    }

    @Transactional(readOnly = false)
    public ApiResult savePR(PwRoom pwRoom) {
        if (StringUtil.isEmpty(pwRoom.getIsUsable())) {
            pwRoom.setIsUsable(Const.NO);
        }
        if (StringUtil.isEmpty(pwRoom.getIsAssign())) {
            pwRoom.setIsAssign(Const.NO);
        }
       if(pwRoom.getNumtype().equals(PwRoom.Type_Enum.TYPE_VALUE1)){
            pwRoom.setIsAllowm(Const.NO);
        }else if(pwRoom.getNumtype().equals(PwRoom.Type_Enum.TYPE_VALUE2)){
            pwRoom.setIsAllowm(Const.YES);
        }
        if(null == pwRoom.getRemaindernum()){
            pwRoom.setRemaindernum(pwRoom.getNum());
        }
        if (!pwRoom.getIsNewRecord()) {
            if ((Const.NO).equals(pwRoom.getIsAssign())) {//不可分配删除预约记录
                ApiResult apiResult = pwEnterRoomService.checkDeleteByRid(new PwEnterRoom(pwRoom));
                if(!ApiConst.CODE_REQUEST_SUCCESS.equals(apiResult.getCode())){
                    return apiResult;
                }
            }
            if ((Const.NO).equals(pwRoom.getIsUsable())) {//不可预约删除预约记录
                pwAppointmentService.deleteByRoomIds(Arrays.asList(new String[]{pwRoom.getId()}));
            }
        }
        if ((Const.YES).equals(pwRoom.getIsAssign()) && (Const.YES).equals(pwRoom.getIsUsable())) {
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,"定期租用和临时预约不能同时开启");
        }
        save(pwRoom);
        return ApiResult.success(ApiConst.CODE_REQUEST_SUCCESS, "操作成功！");
    }

    @Transactional(readOnly = false)
    public void save(PwRoom pwRoom) {
        super.save(pwRoom);
    }

    @Transactional(readOnly = false)
    public void deleteAndClear(PwRoom pwRoom) {
        if (StringUtils.isBlank(pwRoom.getId())) {
            throw new RuntimeException("指定的房间未找到");
        }
        PwEnterRoom pwEnterRoom = new PwEnterRoom(new PwRoom(pwRoom.getId()));
        if(!pwEnterRoomService.findList(pwEnterRoom).isEmpty()){
            throw new RuntimeException("该房间有入驻信息，无法删除");
        }


        pwFassetsService.clearByRoomIds(Arrays.asList(new String[]{pwRoom.getId()}));
        pwAppointmentService.deleteByRoomIds(Arrays.asList(new String(pwRoom.getId())));
        delete(pwRoom);
    }

    @Transactional(readOnly = false)
    public int deleteRoomList(String ids){
        String[] idsList = ids.split(StringUtil.DOTH);
        List<PwRoom> pwRoomList = new ArrayList<>();
        for(int i=0;i<idsList.length;i++){
            PwRoom pwRoom = new PwRoom();
            pwRoom.setId(idsList[i]);
            PwEnterRoom pwEnterRoom = new PwEnterRoom(new PwRoom(pwRoom.getId()));
            if(!pwEnterRoomService.findList(pwEnterRoom).isEmpty()){
                //该房间有入驻信息，无法删除
                return ApiConst.CODE_MORE_ERROR;
            }
            PwAppointment pa = new PwAppointment();
            pa.setPwRoom(new PwRoom(pwRoom.getId()));
            //已预约的不能删除
            List<PwAppointment> pwAppointmentList = pwAppointmentService.findRoomOrderDetailList(pa, "and a.`status`=1 and a.start_date >=now()", Const.Z1);
            if(pwAppointmentList.size()>0){
                return ApiConst.CODE_MORE_ERROR;
            }
            pwRoom.setDelFlag(Const.YES);
            pwRoomList.add(pwRoom);
        }
        pwFassetsService.clearByRoomIds(Arrays.asList(idsList));
        pwAppointmentService.deleteByRoomIds(Arrays.asList(idsList));
        pwRoomDao.deleteRoom(pwRoomList);
        return ApiConst.CODE_REQUEST_SUCCESS;
    }

    @Transactional(readOnly = false)
    public void updateRoom(PwRoom pwRoom){
        pwRoom.preUpdate();
        pwRoomDao.updateRoom(pwRoom);
    }

    @Transactional(readOnly = false)
    public void delete(PwRoom pwRoom) {
        super.delete(pwRoom);
    }

    public List<PwSpaceRoom> findSpaceAndRoom() {
        return pwRoomDao.findSpaceAndRoom();
    }

    public List<PwRoom> findListByPwAppointmentVo(PwAppointmentVo pwAppointmentVo) {
        return pwRoomDao.findListByPwAppointmentVo(pwAppointmentVo);
    }

    @Transactional(readOnly = false)
    public int deleteByRoomIds(List<String> roomIds) {
        return pwRoomDao.deleteByRoomIds(roomIds);
    }

    /**
     * 验证房间名重复.
     * @param name 房间名称
     * @param sid 楼层
     * @return ActYwRstatus
     */
    public ApiResult verifyName(String name, String sid) {
      if(StringUtil.isEmpty(name) || StringUtil.isEmpty(sid)){
          return ApiResult.failed(ApiConst.CODE_NULL_ERROR, "房间名和楼层为空");
      }
      List<PwRoom> pwRooms = dao.verifyNameBySpace(StringUtil.trim(name), sid);
      if((pwRooms == null) || (pwRooms.size() <= 0)){
          return ApiResult.success(ApiConst.CODE_REQUEST_SUCCESS, "房间名不存在");
      }
      return ApiResult.failed(ApiConst.CODE_MORE_ERROR, "房间名已存在");
    }

}