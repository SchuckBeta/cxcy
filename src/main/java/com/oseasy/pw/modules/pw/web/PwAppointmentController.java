package com.oseasy.pw.modules.pw.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.exception.AppointmentException;
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
@RequestMapping(value = "${adminPath}/pw/pwAppointment")
public class PwAppointmentController extends BaseController {
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

	@RequestMapping(value = "getCountToAudit")
	@ResponseBody
	public Long getCountToAudit() {
		return pwAppointmentService.getCountToAudit();
	}
    @RequestMapping(value = {"list", ""})
    public String list() {

        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentList";
    }

    @RequestMapping(value = {"listpage", ""})
    @ResponseBody
    public ApiResult listpage(PwAppointment pwAppointment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwAppointment> page = pwAppointmentService.findPage(new Page<PwAppointment>(request, response), pwAppointment);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = {"review", ""})
    public String reviewList() {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentReviewList";
    }

    @RequestMapping(value = {"reviewList", ""})
    @ResponseBody
    public ApiResult reviewListPage(PwAppointment pwAppointment, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<String> multiStatus = new ArrayList<>(3);
            if (StringUtils.isBlank(pwAppointment.getStatus())) {
                multiStatus.add(PwAppointmentStatus.WAIT_AUDIT.getValue());
                multiStatus.add(PwAppointmentStatus.PASS.getValue());
                multiStatus.add(PwAppointmentStatus.LOCKED.getValue());
            } else {
                multiStatus.add(pwAppointment.getStatus());
            }
            pwAppointment.setMultiStatus(multiStatus);
            pwAppointment.setRangeDateFrom(new Date());//只查询有效的预约，过期的不显示
            Page<PwAppointment> page = pwAppointmentService.findPage(new Page<PwAppointment>(request, response), pwAppointment);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }

    }


    @RequestMapping(value = "form")
    public String form(PwAppointment pwAppointment, Model model) {
        model.addAttribute("pwAppointment", pwAppointment);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentForm";
    }

    @ResponseBody
    @RequestMapping(value = "details")
    public PwAppointment details(PwAppointment pwAppointment) {
        return pwAppointmentService.get(pwAppointment.getId());
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

    @RequiresPermissions("pw:pwAppointment:view")
    @RequestMapping(value = "viewMonth")
    public String viewMonth(PwAppointmentVo pwAppointmentVo, Model model) {
        //得到当前日期
        //根据配置得出当前提前预约天数
//        PwAppointmentRule pwAppointmentRuleIn = pwAppointmentRuleService.getPwAppointmentRule();
//        String afterday = pwAppointmentRuleIn.getAfterDays();
        //根据配置得出当前预约终止时间

        beforeMouthSearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.mouthSearch(pwAppointmentVo);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        model.addAttribute("now", map.get("now"));

//        JSONObject jsonData=new JSONObject();
//        jsonData=pwAppointmentService.findViewMonthList(pwAppointmentVo);
//        jsonData.put("nowDate",nowString);
//        model.addAttribute("jsonData", jsonData);
//        model.addAttribute("pwAppMouthVo", pwAppMouthVo);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentViewMonth";
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
            //TODO
            Date startDay = DateUtil.parseDate(startString, "yyyy-MM-dd HH:mm:ss");
            Date endDay = DateUtil.parseDate(endString, "yyyy-MM-dd HH:mm:ss");
            PwAppointmentVo pwAppointmentVo = new PwAppointmentVo();
            pwAppointmentVo.setStartDate(startDay);
            pwAppointmentVo.setEndDate(endDay);
            List<String> states = new ArrayList<String>();
            states.add(state);
            pwAppointmentVo.setStatus(states);
            list = pwAppointmentService.findListByPwAppointmentVo(pwAppointmentVo);
        } catch (ParseException e) {
            throw new AppointmentException("时间解析异常");
        }
        return list;
    }


    @RequestMapping(value = "mouthViewDay")
    public String mouthViewDay(PwAppointmentVo pwAppointmentVo, Model model, HttpServletRequest request) {
        beforeDaySearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        User currentUser = UserUtils.getUser();
        model.addAttribute("isAdmin", pwAppointmentService.isAdmin(currentUser));
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

    @RequestMapping(value = "delete")
    public String delete(PwAppointment pwAppointment, RedirectAttributes redirectAttributes) {
        try {
            pwAppointmentService.delete(pwAppointment);
            addMessage(redirectAttributes, "删除预约成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwAppointment/?repage";
    }


    @RequiresPermissions("pw:pwAppointment:view")
    @RequestMapping(value = "viewWeek")
    public String viewWeek(PwAppointmentVo pwAppointmentVo, Model model) {
        beforeWeekSearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        User currentUser = UserUtils.getUser();
        model.addAttribute("isAdmin", pwAppointmentService.isAdmin(currentUser));
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        model.addAttribute("now", map.get("now"));
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentViewWeek";
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
    @RequestMapping(value = "mouthSearch")
    public Map<String,Object> viewMouthSearch(PwAppointmentVo pwAppointmentVo) {
        beforeMouthSearch(pwAppointmentVo);
        return pwAppointmentService.mouthSearch(pwAppointmentVo);
    }


    @ResponseBody
    @RequestMapping(value = "weekSearch")
    public Map<String,Object> viewWeekSearch(PwAppointmentVo pwAppointmentVo) {
        beforeWeekSearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        User currentUser = UserUtils.getUser();
        map.put("isAdmin", pwAppointmentService.isAdmin(currentUser));
        return map;
    }

    @RequiresPermissions("pw:pwAppointment:view")
    @RequestMapping(value = "viewDay")
    public String viewDay(PwAppointmentVo pwAppointmentVo, Model model) {
        beforeDaySearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        User currentUser = UserUtils.getUser();
        model.addAttribute("isAdmin", pwAppointmentService.isAdmin(currentUser));
        model.addAttribute("list", map.get("list"));
        model.addAttribute("rooms", map.get("rooms"));
        model.addAttribute("appRule", map.get("appRule"));
        model.addAttribute("now", map.get("now"));
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentViewDay";
    }

    /**
     * 日视图的默认查询时间段
     *
     * @param pwAppointmentVo
     */
    private void beforeDaySearch(PwAppointmentVo pwAppointmentVo) {
        if (StringUtils.isNotBlank(pwAppointmentVo.getSearchDay())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date searchDay = sdf.parse(pwAppointmentVo.getSearchDay());
                pwAppointmentVo.setStartDate(DateUtil.getDayFromBegin(searchDay, -30));
                pwAppointmentVo.setEndDate(DateUtil.getDayToEnd(searchDay, 30));
            } catch (ParseException e) {
                throw new AppointmentException("时间解析异常");
            }
        } else {
            if (pwAppointmentVo.getStartDate() == null) {
                pwAppointmentVo.setStartDate(DateUtil.getDayFromBegin(-30));
            }
            if (pwAppointmentVo.getEndDate() == null) {
                pwAppointmentVo.setEndDate(DateUtil.getDayToEnd(30));
            }
        }
        if (pwAppointmentVo.getRoomTypes() != null && pwAppointmentVo.getRoomTypes().isEmpty()) {
            pwAppointmentVo.setRoomTypes(null);
        }
        setDefaultStatus(pwAppointmentVo);
    }

    @RequiresPermissions("pw:pwAppointment:view")
    @ResponseBody
    @RequestMapping(value = "daySearch")
    public Map<String,Object> viewDaySearch(PwAppointmentVo pwAppointmentVo) {
        beforeDaySearch(pwAppointmentVo);
        Map<String, Object> map = pwAppointmentService.weekAndDaySearch(pwAppointmentVo);
        User currentUser = UserUtils.getUser();
        map.put("isAdmin", pwAppointmentService.isAdmin(currentUser));
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

    @RequestMapping(value = "cancel")
    @ResponseBody
    public ApiResult cancel(PwAppointment pwAppointment, RedirectAttributes redirectAttributes) {
        try {
            pwAppointmentService.cancel(pwAppointment);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
        //return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwAppointment/review?repage";
    }


    /**
     * 获取预约申请记录.
     *
     * @param pwAppcParam 申报条件
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