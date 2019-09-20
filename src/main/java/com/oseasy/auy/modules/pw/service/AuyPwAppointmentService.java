package com.oseasy.auy.modules.pw.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.act.modules.actyw.exception.ApplyException;
import com.oseasy.act.modules.actyw.service.ActYwApplyService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.vo.ActYwApplyVo;
import com.oseasy.auy.modules.act.service.AuyActYwApplyService;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.tool.process.vo.FlowYwId;
import com.oseasy.pw.modules.pw.dao.PwAppointmentDao;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.entity.PwAppointmentRule;
import com.oseasy.pw.modules.pw.exception.AppointmentException;
import com.oseasy.pw.modules.pw.service.PwAppointmentRuleService;
import com.oseasy.pw.modules.pw.service.PwAppointmentService;
import com.oseasy.pw.modules.pw.vo.PwAppointmentStatus;

/**
 * 预约Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class AuyPwAppointmentService extends CrudService<PwAppointmentDao, PwAppointment> {
    @Autowired
    private ActYwApplyService actYwApplyService;
    @Autowired
    private AuyActYwApplyService auyActYwApplyService;

    @Autowired
    private PwAppointmentService pwAppointmentService;
    @Autowired
    private PwAppointmentRuleService pwAppointmentRuleService;

    /**
     * 保存预约的申报信息.
     *
     * @param pwAppointment 预约对象
     * @return ActYwApply
     */
    @Transactional(readOnly = false)
    public ActYwApply saveApply(PwAppointment pwAppointment) {
        ActYwApply actYwApply = new ActYwApply();
        actYwApply.setActYw(new ActYw(FlowYwId.FY_APPOINTMENT.getId()));
        actYwApply.setType(FlowType.FWT_APPOINTMENT.getKey());
        actYwApply.setRelId(pwAppointment.getId());
        actYwApplyService.saveApply(actYwApply);
        ActYwApplyVo actYwApplyVo = new ActYwApplyVo(actYwApply.getId(), actYwApply.getActYw(), actYwApply.getApplyUser());
        PwAppointmentRule pwAppointmentRule = pwAppointmentRuleService.getPwAppointmentRule();
        ApiTstatus<?> actYwRstatus;
        if ("1".equals(pwAppointmentRule.getIsAuto())) {
            actYwRstatus = auyActYwApplyService.timeSubmit(actYwApplyVo);
        } else {
            actYwRstatus = auyActYwApplyService.submit(actYwApplyVo);
        }
        if (!actYwRstatus.getStatus()) {
            throw new ApplyException(actYwRstatus.getMsg(), null);
        }
        return actYwApply;
    }


    /**
     * 添加预约
     *
     * @param pwAppointment
     * @return
     */
    @Transactional(readOnly = false)
    public String add(PwAppointment pwAppointment) {
        User user = UserUtils.getUser();
        pwAppointmentService.validate(pwAppointment, user);
        pwAppointment.setUser(user);
        pwAppointment.setId(IdGen.uuid());
        pwAppointment.setIsNewRecord(true);
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        pwAppointment.setStartTime(Integer.valueOf(sdf.format(pwAppointment.getStartDate())));
        int endTime = Integer.valueOf(sdf.format(pwAppointment.getEndDate()));
        pwAppointment.setEndTime(endTime == 0 ? 2400 : endTime);
        if (!pwAppointmentService.isAdmin(user)) {
            pwAppointment.setOpType("0");
            pwAppointment.setStatus(PwAppointmentStatus.WAIT_AUDIT.getValue());//待审核
            super.save(pwAppointment);
            this.saveApply(pwAppointment);//保存预约信息到流程
        } else {
            pwAppointment.setOpType("1");
            pwAppointment.setStatus(PwAppointmentStatus.LOCKED.getValue());
//            pwAppointment.setSubject("锁定");
            super.save(pwAppointment);
        }
        return pwAppointment.getId();
    }

    /**
     * 人工审核
     *
     * @param id
     * @param grade
     */
    @Transactional(readOnly = false)
    public void manualAudit(String id, String grade) {
        PwAppointment newPwAppointment = get(id);
        if (newPwAppointment == null) {
            logger.warn("未找到预约信息");
            throw new AppointmentException("未找到预约信息");
        }
        if (!newPwAppointment.getStatus().equals(PwAppointmentStatus.WAIT_AUDIT.getValue())) {
            logger.warn("预约已经审核过");
            throw new AppointmentException("预约已经审核过");
        }
        ActYwApply actYwApply = new ActYwApply();
        actYwApply.setRelId(id);
        List<ActYwApply> applyList = actYwApplyService.findList(actYwApply);
        if (applyList.isEmpty()) {
            logger.warn("未找到预约相关的流程信息");
            throw new AppointmentException("未找到预约相关的流程信息");
        }
        auyActYwApplyService.pwAppointAudit(applyList.get(0), grade);
    }
}