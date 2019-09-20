package com.oseasy.pw.modules.pw.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifyTypeStatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SysService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.common.config.RoomUseStatus;
import com.oseasy.pw.modules.pw.dao.PwAppointmentDao;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.entity.PwAppointmentRule;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.exception.AppointmentException;
import com.oseasy.pw.modules.pw.vo.PwAppCalendarParam;
import com.oseasy.pw.modules.pw.vo.PwAppMouthVo;
import com.oseasy.pw.modules.pw.vo.PwAppointmentStatus;
import com.oseasy.pw.modules.pw.vo.PwAppointmentVo;
import com.oseasy.pw.modules.pw.vo.PwSpaceType;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.util.common.utils.DateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 预约Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwAppointmentService extends CrudService<PwAppointmentDao, PwAppointment> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String roomType="pw_room_type";

    @Autowired
    private PwRoomService pwRoomService;

    @Autowired
    private PwAppointmentRuleService pwAppointmentRuleService;

    @Autowired
    private OaNotifyService oaNotifyService;

    @Autowired
    private SysService sysService;


	@Transactional(readOnly = false)
	public Long getCountToAudit() {
		PwAppointment p=new PwAppointment();
		p.setStatus(PwAppointmentStatus.WAIT_AUDIT.getValue());
		Long c=dao.getCount(p);
		return c==null?0L:c;
	}
    public PwAppointment get(String id) {
        return super.get(id);
    }

    public List<PwAppointment> findListByCalendarParam(PwAppCalendarParam pwAppcParam) {
        return dao.findListByCalendarParam(pwAppcParam);
    }

    public List<PwAppointment> findRoomOrderDetailList(PwAppointment pwAppointment){
	    return dao.findRoomOrderDetailList(pwAppointment);
    }

    public List<PwAppointment> findRoomOrderDetailList(PwAppointment pwAppointment,String querySql,Integer qureyStatus){
        pwAppointment.setQuerySql(querySql);
        List<PwAppointment> list = dao.findRoomOrderDetailList(pwAppointment);
        return list;
    }

    public Page<PwAppointment> roomOrderDetailList(Page<PwAppointment> page, PwAppointment pwAppointment,String queryType) {
        pwAppointment.setPage(page);
        String querySql ;
        List<PwAppointment> list = Lists.newArrayList();
        if(PwRoomService.NOWDATA.equals(queryType)){
            //使用中、待使用
            querySql = " and ( SELECT count(p.id) from pw_appointment p\n" +
                    "LEFT JOIN pw_room r1 ON r1.id = p.rid and r1.del_flag=0\n" +
                    "where p.`status`=1 and NOW() >= p.start_date and p.end_date >= now()\n" +
                    "and p.id = a.id\n" +
                    " ) >0\n" +
                    "or ( SELECT count(p.id) from pw_appointment p\n" +
                    "LEFT JOIN pw_room r1 ON r1.id = p.rid and r1.del_flag=0\n" +
                    "where p.`status`=1 and p.start_date > now()\n" +
                    "and p.id = a.id\n" +
                    " ) >0 ";
            List<PwAppointment> usingRoomList = findRoomOrderDetailList(pwAppointment,querySql,PwRoomService.STATUS1);
            for(PwAppointment pa :usingRoomList){
                if(pa.getIsUsing() > 0){
                    pa.setQuerystatus(RoomUseStatus.USED.getStatus());
                }
                if(pa.getIsToUse() > 0){
                    pa.setQuerystatus(RoomUseStatus.TO_USE.getStatus());
                }
            }
            page.setList(usingRoomList);
        }else if((PwRoomService.HISTORYDATA.equals(queryType))){
            //已使用、取消预约
            querySql = " and ( SELECT count(p.id) from pw_appointment p\n" +
                    "LEFT JOIN pw_room r1 ON r1.id = p.rid and r1.del_flag=0\n" +
                    "where p.`status`=1 and now() > p.end_date\n" +
                    "and p.id = a.id\n" +
                    " ) > 0 \n" +
                    "or ( SELECT count(p.id) from pw_appointment p\n" +
                    "LEFT JOIN pw_room r1 ON r1.id = p.rid and r1.del_flag=0\n" +
                    "where p.`status`=3 \n" +
                    "and p.id = a.id\n" +
                    " ) >0 ";
            List<PwAppointment> usedRoomList = findRoomOrderDetailList(pwAppointment,querySql,PwRoomService.STATUS1);
            for(PwAppointment pa :usedRoomList){
                if(pa.getIsUsed() > 0){
                    pa.setQuerystatus(RoomUseStatus.END_USE.getStatus());
                }
                if(pa.getIsNoUse() > 0){
                    pa.setQuerystatus(RoomUseStatus.CANCEL_ORDER.getStatus());
                }
            }
            page.setList(usedRoomList);
        }
        return page;
    }


    public List<PwAppointment> findList(PwAppointment pwAppointment) {
        return super.findList(pwAppointment);
    }

    public Page<PwAppointment> findPage(Page<PwAppointment> page, PwAppointment pwAppointment) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        try {
            Date rangeDateFrom;
            Date rangeDateTo;
            int timeFrom = 0;
            int timeTo = 0;
            if (StringUtils.isNotBlank(pwAppointment.getDateFrom())) {
                if (StringUtils.isNotBlank(pwAppointment.getTimeFrom())) {
                    //TODO 使用DateUtil工具类常量代替
                    sdf.applyPattern("yyyy-MM-dd HH:mm");
                    rangeDateFrom = sdf.parse(pwAppointment.getDateFrom() + " " + pwAppointment.getTimeFrom());
                } else {
                    //TODO
                    sdf.applyPattern("yyyy-MM-dd");
                    rangeDateFrom = DateUtil.getDayFromBegin(sdf.parse(pwAppointment.getDateFrom()), 0);
                }
                pwAppointment.setRangeDateFrom(rangeDateFrom);
            } else {
                if (StringUtils.isNotBlank(pwAppointment.getTimeFrom())) {
                    timeFrom = Integer.valueOf(pwAppointment.getTimeFrom().replace(":", ""));
                }
                pwAppointment.setStartTime(timeFrom);
            }
            if (StringUtils.isNotBlank(pwAppointment.getDateTo())) {
                if (StringUtils.isNotBlank(pwAppointment.getTimeTo())) {
                    //TODO
                    sdf.applyPattern("yyyy-MM-dd HH:mm");
                    rangeDateTo = sdf.parse(pwAppointment.getDateTo() + " " + pwAppointment.getTimeTo());
                } else {
                    sdf.applyPattern("yyyy-MM-dd");
                    rangeDateTo = DateUtil.getDayToEnd(sdf.parse(pwAppointment.getDateTo()), 0);
                }
                pwAppointment.setRangeDateTo(rangeDateTo);
            } else {
                if (StringUtils.isNotBlank(pwAppointment.getTimeTo())) {
                    timeTo = Integer.valueOf(pwAppointment.getTimeTo().replace(":", ""));
                    if (timeTo == 0) {//处理日期控件不能选择24点的问题
                        timeTo = 2400;
                    }
                }
                pwAppointment.setEndTime(timeTo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return page;
        }
        return super.findPage(page, pwAppointment);
    }

    public void validate(PwAppointment pwAppointment, User user) {
        if (pwAppointment.getPwRoom() == null || StringUtils.isBlank(pwAppointment.getPwRoom().getId())
                || pwRoomService.get(pwAppointment.getPwRoom().getId()) == null) {
            logger.warn("未找到房间信息");
            throw new AppointmentException("未找到房间信息");
        }
        Date startDate = pwAppointment.getStartDate();
        Date endDate = pwAppointment.getEndDate();
        if (startDate == null || endDate == null) {
            logger.warn("未设置完整的开始和结束时间");
            throw new AppointmentException("未设置完整的开始和结束时间");
        }
        if (startDate.after(endDate)) {
            logger.warn("开始时间应该早于结束时间");
            throw new AppointmentException("开始时间应该早于结束时间");
        }
        if (startDate.getTime() < new Date().getTime()) {
            logger.warn("不能预约过去的时间");
            throw new AppointmentException("不能预约过去的时间");
        }
        PwAppointmentRule pwAppointmentRule = pwAppointmentRuleService.getPwAppointmentRule();
        if (pwAppointmentRule == null) {
            logger.warn("未设置预约规则");
            throw new AppointmentException("未设置预约规则");
        }
        Integer delay = Integer.valueOf(pwAppointmentRule.getAfterDays());
        Date dayToEnd = DateUtil.getDayToEnd(delay);
        if (!isAdmin(user) && endDate.after(dayToEnd)) {
            logger.warn(String.format("只能预约%s天内的房间", delay));
            throw new AppointmentException(String.format("只能预约%s天内的房间", delay));
        }
        PwAppointmentVo vo = new PwAppointmentVo();
        List<String> rooms = new ArrayList<>(1);
        rooms.add(pwAppointment.getPwRoom().getId());
        vo.setRoomIds(rooms);
        List<String> status = new ArrayList<>(2);
        status.add(PwAppointmentStatus.WAIT_AUDIT.getValue());
        status.add(PwAppointmentStatus.PASS.getValue());
        status.add(PwAppointmentStatus.LOCKED.getValue());
        vo.setStatus(status);
        vo.setStartDate(DateUtil.getDayFromBegin(startDate, 0));
        vo.setEndDate(DateUtil.getDayToEnd(endDate, 0));
        List<PwAppointment> list = this.findListByPwAppointmentVo(vo);
        if (!list.isEmpty()) {
            for (PwAppointment appointment : list) {
                if (!(appointment.getStartDate().getTime() < startDate.getTime() && appointment.getEndDate().getTime() <= startDate.getTime())
                        && !(appointment.getStartDate().getTime() >= endDate.getTime() && appointment.getEndDate().getTime() > endDate.getTime())) {
                    logger.warn("当前时间段不能预约");
                    throw new AppointmentException("当前时间段不能预约");
                }

            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(PwAppointment pwAppointment) {
        PwAppointment newPwAppointment = this.get(pwAppointment.getId());
        if (newPwAppointment == null) {
            logger.warn("未找到指定的预约信息");
            throw new AppointmentException("未找到指定的预约信息");
        }
        super.delete(newPwAppointment);
    }


    @Transactional(readOnly = false)
    public void deleteByRoomIds(List<String> roomIds) {
        dao.deleteByRoomIds(roomIds);
    }

    public Page<PwAppointment> findMyPwAppointmentList(Page<PwAppointment> page, PwAppointment pwAppointment) {
        pwAppointment.setPage(page);
        page.setList(dao.findMyPwAppointmentList(pwAppointment));
        return page;
    }

    public List<PwAppointment> findViewMonth(PwAppointment pwAppointment) {
        return dao.findViewMonth(pwAppointment);
    }

    /**
     * 根据条件查询预约记录
     *
     * @param pwAppointmentVo
     * @return
     */
    public List<PwAppointment> findListByPwAppointmentVo(PwAppointmentVo pwAppointmentVo) {
        return dao.findListByPwAppointmentVo(pwAppointmentVo);
    }

    /**
     * 根据信息查询预约、房间、预约规则
     *
     * @param pwAppointmentVo
     * @return
     */
    public Map<String, Object> mouthSearch(PwAppointmentVo pwAppointmentVo) {
        Map<String, Object> map = new HashMap<>();
        List<PwRoom> rooms = pwRoomService.findListByPwAppointmentVo(pwAppointmentVo);
        PwAppointmentRule pwAppointmentRule = pwAppointmentRuleService.getPwAppointmentRule();
        setRoomPath(rooms);
        JSONObject jsonData = findViewMonthList(pwAppointmentVo);
        map.put("list", jsonData.get("dataList"));
        map.put("rooms", rooms);
        map.put("appRule", pwAppointmentRule);
        map.put("now", DateUtil.formatDate(sysService.getDbCurDateYmdHms()));
        return map;
    }

    private void setRoomPath(List<PwRoom> rooms) {
        if (!rooms.isEmpty()) {
            for (PwRoom room : rooms) {
                PwSpace pwSpace = room.getPwSpace();
                StringBuffer sb = new StringBuffer(room.getName());
                while (pwSpace != null && StringUtils.isNotBlank(pwSpace.getName())) {
                    if (StringUtils.isBlank(pwSpace.getType()) || PwSpaceType.SCHOOL.getValue().equals(pwSpace.getType())) {
                        break;
                    }
                    sb.insert(0, pwSpace.getName());
                    pwSpace = pwSpace.getParent();
                }
                sb.insert(0, "【" + DictUtils.getDictLabel(room.getType(),roomType,"") + "】");
                room.setPath(sb.toString());
            }
        }
    }


    /**
     * 根据信息查询预约、房间、预约规则
     *
     * @param pwAppointmentVo
     * @return
     */
    public Map<String, Object> weekAndDaySearch(PwAppointmentVo pwAppointmentVo) {
        Map<String, Object> map = new HashMap<>();
        List<PwAppointment> list = this.findListByPwAppointmentVo(pwAppointmentVo);
        List<PwRoom> rooms = pwRoomService.findListByPwAppointmentVo(pwAppointmentVo);
        setRoomPath(rooms);
        PwAppointmentRule pwAppointmentRule = pwAppointmentRuleService.getPwAppointmentRule();
        map.put("list", list);
        map.put("rooms", rooms);
        map.put("appRule", pwAppointmentRule);
        map.put("now", DateUtil.formatDate(sysService.getDbCurDateYmdHms()));
        return map;
    }

    /**
     * 取消预约
     *
     * @param pwAppointment
     * @return
     */
    @Transactional(readOnly = false)
    public String cancel(PwAppointment pwAppointment) {
        PwAppointment newPwAppointment = get(pwAppointment.getId());
        if (newPwAppointment == null) {
            logger.warn("未找到预约信息");
            throw new AppointmentException("未找到预约信息");
        }
        User user = UserUtils.getUser();
        if (!user.getId().equals(newPwAppointment.getUser().getId()) && !isAdmin(user)) {
            logger.warn("当前用户没有权限取消该预约");
            throw new AppointmentException("当前用户没有权限取消该预约");
        }
        this.changeStatus(pwAppointment.getId(), PwAppointmentStatus.CANCELED);
        if (!user.getId().equals(newPwAppointment.getUser().getId()) && !isAdmin(newPwAppointment.getUser())) {//不是本人取消预约,也不是管理员的预约，发站内信
            this.sendOaNotify(newPwAppointment, user);
        }
        return newPwAppointment.getId();
    }

    /**
     * 更新预约的状态
     *
     * @param pwAppointmentId
     * @param status
     */
    @Transactional(readOnly = false)
    public void changeStatus(String pwAppointmentId, PwAppointmentStatus status) {
        PwAppointment newPwAppointment = get(pwAppointmentId);
        if (newPwAppointment == null) {
            logger.warn("未找到预约信息");
            throw new AppointmentException("未找到预约信息");
        }
        if (newPwAppointment.getStatus().equals(status.getValue())) {
            logger.warn(String.format("已经是%s状态", status.getName()));
            throw new AppointmentException(String.format("已经是%s状态", status.getName()));
        }
        newPwAppointment.setStatus(status.getValue());
        super.save(newPwAppointment);
    }


    public JSONObject findViewMonthList(PwAppointmentVo pwAppointmentVo) {
        List<PwAppMouthVo> wAppMouthVoList = dao.findViewMonthList(pwAppointmentVo);
        JSONObject jsonDataList = new JSONObject();
        JSONArray yeardata = new JSONArray();
        for (PwAppMouthVo vo : wAppMouthVoList) {
            JSONObject jsonData = new JSONObject();
            String title = "您有" + vo.getNum() + "条预约" +
                    DictUtils.getDictLabel(vo.getStatus(), "pw_appointment_status", "");
            jsonData.put("title", title);
            jsonData.put("start", vo.getMday());
            jsonData.put("link", "/pw/pwAppointment/getListByState?mDay=" + vo.getMday() + "&state=" + vo.getStatus());

            jsonData.put("state", vo.getStatus());
            yeardata.add(jsonData);
        }
        jsonDataList.put("dataList", yeardata);
        return jsonDataList;
    }

    /**
     * 发送站内信.
     *
     * @param pwAppointment
     * @param user
     */
    @Transactional(readOnly = false)
    public void sendOaNotify(PwAppointment pwAppointment, User user) {
        OaNotifyRecord oaNotifyRecord = new OaNotifyRecord();
        OaNotify oaNotify = new OaNotify();
        oaNotify.setTitle("预约通知");
        oaNotify.setContent(String.format("您预约%s到%s的%s已被管理员取消", DateUtil.formatToDate(pwAppointment.getStartDate(), "yyyy-MM-dd HH:mm"), DateUtil.formatToDate(pwAppointment.getEndDate(), "yyyy-MM-dd HH:mm"), pwAppointment.getPwRoom().getName()));
        oaNotify.setType(OaNotify.Type_Enum.TYPE15.getValue());
        oaNotify.setsId(pwAppointment.getId());
        oaNotify.setCreateBy(user);
        oaNotify.setCreateDate(new Date());
        oaNotify.setUpdateBy(user);
        oaNotify.setUpdateDate(oaNotify.getCreateDate());
        oaNotify.setEffectiveDate(oaNotify.getCreateDate());
        oaNotify.setStatus(OaNotifyTypeStatus.DEPLOY.getKey());
        oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());

        List<OaNotifyRecord> recList = new ArrayList<>();
        oaNotifyRecord.setId(IdGen.uuid());
        oaNotifyRecord.setOaNotify(oaNotify);
        oaNotifyRecord.setUser(pwAppointment.getUser());
        oaNotifyRecord.setReadFlag(Const.NO);
        oaNotifyRecord.setOperateFlag(Const.NO);
        recList.add(oaNotifyRecord);

        oaNotify.setOaNotifyRecordList(recList);
        oaNotifyService.save(oaNotify);
    }

    /**
     * 用户是否是管理员
     * 服务于预约视图
     * 目前的逻辑是只要能看到菜单同时用户类型不是学生或是导师
     *
     * @param user
     * @return
     */
    public boolean isAdmin(User user) {
        return !(SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)
                || SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS));
    }




}