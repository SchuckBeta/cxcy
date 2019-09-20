package com.oseasy.pw.modules.pw.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.service.PwAppointmentService;
import com.oseasy.pw.modules.pw.vo.Msg;
import com.oseasy.pw.modules.pw.vo.PwAppCalendarParam;
import com.oseasy.pw.modules.pw.vo.PwAppCalendarVo;
import com.oseasy.pw.modules.pw.vo.PwAppointmentStatus;
import com.oseasy.pw.modules.pw.vo.PwAppointmentVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 预约Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${frontPath}/pw/pwAppointment")
public class FrontPwAppointmentController extends BaseController {
    @Autowired
    private PwAppointmentService pwAppointmentService;

    @ModelAttribute
    public PwAppointment get(@RequestParam(required = false) String id) {
        PwAppointment entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwAppointmentService.get(id);
        }
        if (entity == null) {
            entity = new PwAppointment();
        }
        return entity;
    }

    @RequestMapping(value = {"list", ""})
    public String list(PwAppointment pwAppointment, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PwAppointment> page = pwAppointmentService.findPage(new Page<PwAppointment>(request, response), pwAppointment);
        model.addAttribute("page", page);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentList";
    }


    @RequestMapping(value = "myList")
    public String myList(PwAppointment pwAppointment, HttpServletRequest request, HttpServletResponse response, Model model) {
        pwAppointment.setUser(UserUtils.getUser());
        Page<PwAppointment> page = pwAppointmentService.findMyPwAppointmentList(new Page<PwAppointment>(request, response), pwAppointment);
        model.addAttribute("page", page);
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwAppointment/myPwAppointmentList";
    }

    @RequestMapping(value = "form")
    public String form(PwAppointment pwAppointment, Model model) {
        model.addAttribute("pwAppointment", pwAppointment);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentForm";
    }

    @RequestMapping(value = "details")
    public String details(PwAppointment pwAppointment, Model model) {
        model.addAttribute("pwAppointment", pwAppointment);
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwAppointment/pwAppointmentDetails";
    }

    @ResponseBody
    @RequestMapping(value = "getListByState")
    public List<PwAppointment> getListByState(HttpServletRequest request, Model model) {
        String mDay = request.getParameter("mDay");
        String state = request.getParameter("state");
        String startString = mDay + " 00:00:00";
        String endString = mDay + " 23:59:59";
        List<PwAppointment> list = null;
        try {
            Date startDay = DateUtil.parseDate(startString, "yyyy-MM-dd HH:mm:ss");
            Date endDay = DateUtil.parseDate(endString, "yyyy-MM-dd HH:mm:ss");
            PwAppointmentVo pwAppointmentVo = new PwAppointmentVo();
            pwAppointmentVo.setUser(UserUtils.getUser());
            pwAppointmentVo.setStartDate(startDay);
            pwAppointmentVo.setEndDate(endDay);
            List<String> states = new ArrayList<String>();
            states.add(state);
            pwAppointmentVo.setStatus(states);
            list = pwAppointmentService.findListByPwAppointmentVo(pwAppointmentVo);
        } catch (ParseException e) {
            logger.error("找不到该状态预约");
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "save")
    public String save(PwAppointment pwAppointment, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, pwAppointment)) {
            return form(pwAppointment, model);
        }
        pwAppointmentService.save(pwAppointment);
        addMessage(redirectAttributes, "保存预约成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwAppointment/?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(PwAppointment pwAppointment, RedirectAttributes redirectAttributes) {
        pwAppointmentService.delete(pwAppointment);
        addMessage(redirectAttributes, "删除预约成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwAppointment/?repage";
    }


    @ResponseBody
    @RequestMapping(value = "mouthSearch")
    public Map viewMouthSearch(PwAppointmentVo pwAppointmentVo) {
        beforeMouthSearch(pwAppointmentVo);
        pwAppointmentVo.setUser(UserUtils.getUser());
        Map<String, Object> map = pwAppointmentService.mouthSearch(pwAppointmentVo);
        return map;
    }

    @RequestMapping(value = "viewMonth")
    public String viewMonth(PwAppointmentVo pwAppointmentVo, Model model) {
        //得到当前日期
        beforeMouthSearch(pwAppointmentVo);
        pwAppointmentVo.setUser(UserUtils.getUser());
        Map<String, Object> map = pwAppointmentService.mouthSearch(pwAppointmentVo);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        model.addAttribute("now", map.get("now"));
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwAppointment/myPwAppointmentViewMonth";
    }


    @RequestMapping(value = "viewWeek")
    public String viewWeek(PwAppointmentVo pwAppointmentVo, Model model) {
        beforeWeekSearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        model.addAttribute("isAdmin", false);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        model.addAttribute("now", map.get("now"));
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwAppointment/myPwAppointmentViewWeek";
    }

    /**
     * 月视图的默认查询时间段
     *
     * @param pwAppointmentVo
     */
    private void beforeMouthSearch(PwAppointmentVo pwAppointmentVo) {
        if (pwAppointmentVo.getStartDate() == null) {
            pwAppointmentVo.setStartDate(DateUtil.getDayFromBegin(-180));
        }
        if (pwAppointmentVo.getEndDate() == null) {
            pwAppointmentVo.setEndDate(DateUtil.getDayToEnd(180));
        }
        if (pwAppointmentVo.getRoomTypes() != null && pwAppointmentVo.getRoomTypes().isEmpty()) {
            pwAppointmentVo.setRoomTypes(null);
        }
        setDefaultStatus(pwAppointmentVo);
    }

    /**
     * 周视图的默认查询时间段
     *
     * @param pwAppointmentVo
     */
    private void beforeWeekSearch(PwAppointmentVo pwAppointmentVo) {
        if (pwAppointmentVo.getStartDate() == null) {
            pwAppointmentVo.setStartDate(DateUtil.getDayFromBegin(-180));
        }
        if (pwAppointmentVo.getEndDate() == null) {
            pwAppointmentVo.setEndDate(DateUtil.getDayToEnd(180));
        }
        if (pwAppointmentVo.getRoomTypes() != null && pwAppointmentVo.getRoomTypes().isEmpty()) {
            pwAppointmentVo.setRoomTypes(null);
        }
        setDefaultStatus(pwAppointmentVo);
    }

    /**
     * //默认查询待审核和通过的预约记录
     *
     * @param pwAppointmentVo
     */
    private void setDefaultStatus(PwAppointmentVo pwAppointmentVo) {
        if (pwAppointmentVo.getStatus() == null || pwAppointmentVo.getStatus().isEmpty()) {
            List<String> status = pwAppointmentVo.getStatus();
            if (status == null) {
                status = new ArrayList<>();
            }
            status.add(PwAppointmentStatus.WAIT_AUDIT.getValue());
            status.add(PwAppointmentStatus.PASS.getValue());
            status.add(PwAppointmentStatus.LOCKED.getValue());
            pwAppointmentVo.setStatus(status);
        }
    }


    @ResponseBody
    @RequestMapping(value = "weekSearch")
    public Map viewWeekSearch(PwAppointmentVo pwAppointmentVo) {
        beforeWeekSearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        map.put("isAdmin", false);
        return map;
    }

    @RequestMapping(value = "viewDay")
    public String viewDay(PwAppointmentVo pwAppointmentVo, Model model) {
        beforeDaySearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        model.addAttribute("isAdmin", false);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        model.addAttribute("now", map.get("now"));
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwAppointment/myPwAppointmentViewDay";
    }

    @RequestMapping(value = "mouthViewDay")
    public String mouthViewDay(PwAppointmentVo pwAppointmentVo, Model model, HttpServletRequest request) {
        beforeDaySearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        model.addAttribute("isAdmin", false);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        String newDay = request.getParameter("newDay");
        if (newDay != null) {
            model.addAttribute("now", newDay);
        } else {
            model.addAttribute("now", map.get("now"));
        }

        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentViewDay";
    }

    /**
     * 日视图的默认查询时间段
     *
     * @param pwAppointmentVo
     */
    private void beforeDaySearch(PwAppointmentVo pwAppointmentVo) {
        if (pwAppointmentVo.getStartDate() == null) {
            pwAppointmentVo.setStartDate(DateUtil.getDayFromBegin(-30));
        }
        if (pwAppointmentVo.getEndDate() == null) {
            pwAppointmentVo.setEndDate(DateUtil.getDayFromBegin(30));
        }
        if (pwAppointmentVo.getRoomTypes() != null && pwAppointmentVo.getRoomTypes().isEmpty()) {
            pwAppointmentVo.setRoomTypes(null);
        }
        setDefaultStatus(pwAppointmentVo);
    }

    @ResponseBody
    @RequestMapping(value = "daySearch")
    public Map viewDaySearch(PwAppointmentVo pwAppointmentVo) {
        beforeDaySearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        map.put("isAdmin", false);
        return map;
    }

    /**
     * 取消预约
     *
     * @param pwAppointment
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "asyCancel")
    public String cancel(PwAppointment pwAppointment) {
        Msg msg;
        try {
            String id = pwAppointmentService.cancel(pwAppointment);
            msg = new Msg(true, id);
        } catch (Exception e) {
            msg = new Msg(e.getMessage());
        }
        return msg.toJson();
    }


    /**
     * 取消预约
     *
     * @param pwAppointment
     * @return
     */
    @RequestMapping(value = "myCancel")
    public String cancel(PwAppointment pwAppointment, RedirectAttributes redirectAttributes) {
        try {
            pwAppointmentService.cancel(pwAppointment);
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/pw/pwAppointment/myList?repage";
    }

    /**
     * 获取预约申请记录.
     *
     * @param response
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/treeData", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<PwAppCalendarVo>> treeData(@RequestBody PwAppCalendarParam pwAppcParam, HttpServletResponse response) {
        List<PwAppointment> list = pwAppointmentService.findListByCalendarParam(pwAppcParam);
        List<PwAppCalendarVo> acVo = PwAppCalendarVo.convert(list);

        if ((acVo == null) || (acVo.size() <= 0)) {
            return new ApiTstatus<List<PwAppCalendarVo>>(false, "请求失败或数据为空！");
        }
        return new ApiTstatus<List<PwAppCalendarVo>>(true, "请求成功", acVo);
    }
}