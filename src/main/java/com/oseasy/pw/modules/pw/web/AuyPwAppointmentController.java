package com.oseasy.pw.modules.pw.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.auy.modules.pw.service.AuyPwAppointmentService;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.vo.Msg;

/**
 * 预约Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwAppointment")
public class AuyPwAppointmentController extends BaseController {
    @Autowired
    private AuyPwAppointmentService auyPwAppointmentService;
    /**
     * 学生预约和管理锁定
     *
     * @param pwAppointment
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "asySave", method = RequestMethod.POST)
    public String save(PwAppointment pwAppointment) {
        Msg msg;
        try {
            String id = auyPwAppointmentService.add(pwAppointment);
            msg = new Msg(true, id);
        } catch (Exception e) {
            msg = new Msg(e.getMessage());
        }
        return msg.toJson();
    }

    /**
     * 人工审核预约
     *
     * @param id
     * @param grade              0：拒绝  1：通过
     * @param redirectAttributes
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "manualAudit/{id}/{grade}")
    public String manualAudit(@PathVariable String id, @PathVariable String grade, RedirectAttributes redirectAttributes) {
        Msg msg;
        try {
            auyPwAppointmentService.manualAudit(id, grade);
            msg = new Msg(true, "");
        } catch (Exception e) {
            msg = new Msg(e.getMessage());
        }
        return msg.toJson();
    }
}