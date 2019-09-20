package com.oseasy.pw.modules.pw.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping(value = "${frontPath}/pw/pwAppointment")
public class FrontAuyPwAppointmentController extends BaseController {
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
}