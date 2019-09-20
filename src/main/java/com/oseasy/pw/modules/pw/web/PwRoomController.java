package com.oseasy.pw.modules.pw.web;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.common.config.RoomUseStatus;
import com.oseasy.pw.modules.pw.dao.PwRoomDao;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwRoomEnumDto;
import com.oseasy.pw.modules.pw.entity.PwSpaceRoom;
import com.oseasy.pw.modules.pw.entity.RoomUseStatusDto;
import com.oseasy.pw.modules.pw.service.PwAppointmentService;
import com.oseasy.pw.modules.pw.service.PwEnterRoomService;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.service.PwRoomService;
import com.oseasy.pw.modules.pw.vo.PwEnterAtype;
import com.oseasy.pw.modules.pw.vo.PwEnterStatus;
import com.oseasy.pw.modules.pw.vo.PwEroomStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 房间Controller.
 *
 * @author chenh
 * @version 2017-11-26
 * update by liangjie
 * update time : 2018-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwRoom")
public class PwRoomController extends BaseController {

    @Autowired
    private PwRoomService pwRoomService;
    @Autowired
    private PwEnterService pwEnterService;
    @Autowired
    private PwEnterRoomService pwEnterRoomService;
    @Autowired
    private PwAppointmentService pwAppointmentService;
    @Autowired
    private PwRoomDao pwRoomDao;
    @ModelAttribute
    public PwRoom get(@RequestParam(required = false) String id) {
        PwRoom entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwRoomService.get(id);
        }
        if (entity == null) {
            entity = new PwRoom();
        }
        return entity;
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"tree"})
    public String tree() {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomTree";
    }

    /**
     *  查询房间列表
     * @param pwRoom
     * @param request
     * @param response
     * @param model
     * @return ApiResult
     * Add : liangjie 2018-11-19
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult list(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Page<PwRoom> page = pwRoomService.findPageByJL(new Page<PwRoom>(request, response), pwRoom);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     *  删除、批量删除房间
     * @param ids 房间id（多个id用逗号分隔）
     * @return ApiResult
     * Add : liangjie 2018-11-19
     */
    @RequestMapping(value = "deleteRoom")
    @ResponseBody
    public ApiResult deleteRoom(String ids) {
        try {
            int deleteFlag = pwRoomService.deleteRoomList(ids);
            if (deleteFlag == ApiConst.CODE_MORE_ERROR) {
                return ApiResult.failed(ApiConst.CODE_MORE_ERROR, "该房间有入驻信息，无法删除");
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 修改is_allowm、is_usable、is_assign
     * @param pwRoom
     * @return ApiResult
     * Add : liangjie 2018-11-19
     */
    @RequestMapping(value = "updateRoom", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult updateRoom(@RequestBody PwRoom pwRoom, Model model, HttpServletRequest request, HttpServletResponse response) {
        try{
            PwRoom pwRoomParam = new PwRoom();
            pwRoomParam.setId(pwRoom.getId());
            pwRoomParam = pwRoomService.get(pwRoomParam.getId());
            //不可分配删除预约记录
            if ((Const.NO).equals(pwRoomParam.getIsAssign()) && null!=pwRoom.getIsAssign()) {
                ApiResult apiResult = pwEnterRoomService.checkDeleteByRid(new PwEnterRoom(pwRoomParam));
                if(!ApiConst.CODE_REQUEST_SUCCESS.equals(apiResult.getCode())){
                    return apiResult;
                }
            }
            //不可预约删除预约记录
            if ((Const.NO).equals(pwRoomParam.getIsUsable()) && null!=pwRoom.getIsAssign()) {
                pwAppointmentService.deleteByRoomIds(Arrays.asList(new String[]{pwRoomParam.getId()}));
            }
            //定期租用和临时预约不能同时开启
            if ((Const.YES).equals(pwRoomParam.getIsAssign()) && (Const.YES).equals(pwRoom.getIsUsable()) ||
                    (Const.YES).equals(pwRoomParam.getIsUsable()) && (Const.YES).equals(pwRoom.getIsAssign())) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,"定期租用和临时预约不能同时开启");
            }
            pwRoomService.updateRoom(pwRoom);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     *  添加、修改房间
     * @param pwRoom
     * @param model
     * @return ApiResult
     * Add : liangjie 2018-11-20
     */

    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult save(@RequestBody PwRoom pwRoom, Model model){
        if (!beanValidator(model, pwRoom)) {
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE));
        }
        return pwRoomService.savePR(pwRoom);
    }

    /**
     * 房间重名验证.
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ajaxVerifyName")
    public ApiResult ajaxVerifyName(String name, String sid) {
        return pwRoomService.verifyName(name, sid);
    }


    /**
     * 房间容纳类型
     * @return
     */
    @RequestMapping(value = "pwRoomType")
    @ResponseBody
    public ApiResult pwRoomType(){
        List<PwRoomEnumDto> pwRoomEnumDtos = Lists.newArrayList();
        for(PwRoom.Type_Enum entity :PwRoom.Type_Enum.values()){
            PwRoomEnumDto pwRoomEnumDto = new PwRoomEnumDto();
            pwRoomEnumDto.setLabel(entity.getName());
            pwRoomEnumDto.setValue(entity.getValue());
            pwRoomEnumDtos.add(pwRoomEnumDto);
        }
        return ApiResult.success(pwRoomEnumDtos);
    }

    /**
     * 房间使用状态
     * @return
     */
    @RequestMapping(value = "roomUseStatusList")
    @ResponseBody
    public ApiResult roomUseStatusList(){
        List<RoomUseStatusDto> roomUseStatusDtoList = Lists.newArrayList();
        for(RoomUseStatus roomUseStatus : RoomUseStatus.values()){
            RoomUseStatusDto roomUseStatusDto = new RoomUseStatusDto();
            roomUseStatusDto.setStatus(roomUseStatus.getStatus());
            roomUseStatusDto.setStatusname(roomUseStatus.getStatusname());
            roomUseStatusDtoList.add(roomUseStatusDto);
        }
        return ApiResult.success(roomUseStatusDtoList);
    }

    //预约房间查询
    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"orderRoomList"})
    public String orderRoomList(){
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomOrderList";
    }

    //预约房间详情tab页1
    @RequestMapping(value = {"orderRoomDetailTab1"})
    public String orderRoomDetailTab1(String rid,Model model){
        model.addAttribute("rid",rid);
        return PwSval.path.vms(PwEmskey.PW.k()) + "orderRoomDetailTab1";
    }
    //预约房间详情tab页2
    @RequestMapping(value = {"orderRoomDetailTab2"})
    public String orderRoomDetailTab2(String rid,Model model){
        model.addAttribute("rid",rid);
        return PwSval.path.vms(PwEmskey.PW.k()) + "orderRoomDetailTab2";
    }
    //分配房间查询
    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"allotRoomList"})
    public String allotRoomList(){
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomAllotList";
    }
    //分配房间详情tab1
    @RequestMapping(value = {"allotRoomDetailTab1"})
    public String allotRoomDetailTab1(String rid,Model model){
        model.addAttribute("rid",rid);
        return PwSval.path.vms(PwEmskey.PW.k()) + "allotRoomDetailTab1";
    }
    //分配房间详情tab2
    @RequestMapping(value = {"allotRoomDetailTab2"})
    public String allotRoomDetailTab2(String rid,Model model){
        model.addAttribute("rid",rid);
        return PwSval.path.vms(PwEmskey.PW.k()) + "allotRoomDetailTab2";
    }
    //房间使用记录
    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"hUseRoomList"})
    public String hUseRoomList(){
        return PwSval.path.vms(PwEmskey.PW.k()) + "hUseRoomList";
    }
    //其他房间查询
    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"otherRoomList"})
    public String otherRoomList(){
        return PwSval.path.vms(PwEmskey.PW.k()) + "otherRoomList";
    }
    //其他房间详情
    @RequestMapping(value = {"otherRoomDetailList"})
    public String otherRoomDetailList(){
        return PwSval.path.vms(PwEmskey.PW.k()) + "otherRoomDetailList";
    }

    /**
     *  预约房间查询
     * @param pwRoom
     * @param request
     * @param response
     * @return ApiResult
     * Add : liangjie 2018-11-21
     */
    @RequestMapping(value = "roomOrderList")
    @ResponseBody
    public ApiResult roomOrderList(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response){
        try{
            Page<PwRoom> page = pwRoomService.findOrderRoomPage(new Page<PwRoom>(request,response),pwRoom);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
    /**
     * 房间使用状态枚举接口
     */
    @RequestMapping(value = "roomUseType")
    @ResponseBody
    public ApiResult roomUseType(Integer num){
        List<RoomUseStatusDto> pwRoomEnumDtos = Lists.newArrayList();
        Date date = new Date();
        PwAppointment pwa = new PwAppointment();
        pwa.setStatus(Const.YES);
        List<PwAppointment> pwaList = pwAppointmentService.findList(pwa);
        switch (num){
            //房间使用记录
            case 1:
                List<PwRoom> pwList = pwRoomDao.findListByJL(new PwRoom());
                int ordernum=0;
                int allotnum=0;
                int nouse=0;
                for(PwRoom prooms : pwList){
                    if( !(prooms.getRemaindernum().equals(prooms.getNum())) && prooms.getIsAssign().equals(Const.YES)){
                        allotnum = allotnum +1;
                    }
                    for(PwAppointment pa : pwaList){
                        if(prooms.getId().equals(pa.getPwRoom().getId()) && pa.getStartDate().getTime() <= date.getTime()
                                && pa.getEndDate().getTime() >= date.getTime() ){ //使用中
                            ordernum = ordernum + 1;
                        }
                    }
                }
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.ORDEREDROOM.getStatus(),RoomUseStatus.ORDEREDROOM.getStatusname(),ordernum));
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.ALLOTEDROOM.getStatus(),RoomUseStatus.ALLOTEDROOM.getStatusname(),allotnum));
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.OTHERSTATUS.getStatus(),RoomUseStatus.OTHERSTATUS.getStatusname(),pwList.size() - allotnum - ordernum));
                break;
            //预约房间
            case 2:
                //预约成功的列表
                int orderusenum = 0;
                int ordernotusenum = 0;
                //预约房间列表
                List<PwRoom> list =pwRoomDao.findOrderRoomPage(new PwRoom());
                for(PwRoom pw : list){
                    for(PwAppointment pa : pwaList){
                        if(pw.getId().equals(pa.getPwRoom().getId()) && pa.getStartDate().getTime() >= date.getTime()){ //待使用
                            //pw.setQuerystatus(RoomUseStatus.ORDERNOTUSEROOM.getStatus());
                            if(null == pw.getQuerystatus()){
                                pw.setQuerystatus(RoomUseStatus.ORDERNOTUSEROOM.getStatus());
                            }else if(null != pw.getQuerystatus()){
                                continue;
                            }
                        }else if(pw.getId().equals(pa.getPwRoom().getId()) && pa.getStartDate().getTime() <= date.getTime()
                                && pa.getEndDate().getTime() >= date.getTime() ){ //使用中
                            pw.setQuerystatus(RoomUseStatus.ORDERUSINGROOM.getStatus());

                        }
                    }
                    if(null == pw.getQuerystatus()){
                        pw.setQuerystatus(RoomUseStatus.NOTORDERROOM.getStatus());
                    }
                }
                for(PwRoom pwr : list){
                    if(pwr.getQuerystatus().equals(RoomUseStatus.ORDERUSINGROOM.getStatus())){
                        orderusenum = orderusenum + 1;
                    }else if(pwr.getQuerystatus().equals(RoomUseStatus.ORDERNOTUSEROOM.getStatus())){
                        ordernotusenum = ordernotusenum + 1;
                    }
                }
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.ORDERUSINGROOM.getStatus(),RoomUseStatus.ORDERUSINGROOM.getStatusname(),orderusenum));
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.ORDERNOTUSEROOM.getStatus(),RoomUseStatus.ORDERNOTUSEROOM.getStatusname(),ordernotusenum));
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.NOTORDERROOM.getStatus(),RoomUseStatus.NOTORDERROOM.getStatusname(),list.size()-orderusenum-ordernotusenum));
                break;
            //分配房间
            case 3:
                int fullRoomNum = 0;
                int notFullRoomNum = 0;
                PwRoom pw = new PwRoom();
                pw.setIsAssign(Const.YES);
                List<PwRoom> roomList = pwRoomDao.findListByJL(pw);
                for(PwRoom proom : roomList){
                    if(proom.getRemaindernum()==0){
                        //已分配（满员）
                        fullRoomNum = fullRoomNum + 1;
                    }else if(proom.getRemaindernum()>0 && proom.getRemaindernum() < proom.getNum()){
                        //已分配（未满员）
                        notFullRoomNum = notFullRoomNum + 1;
                    }
                }
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.ALLOTEDFULLROOM.getStatus(),RoomUseStatus.ALLOTEDFULLROOM.getStatusname(),fullRoomNum));
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.ALLOTEDNOTFULLROOM.getStatus(),RoomUseStatus.ALLOTEDNOTFULLROOM.getStatusname(),notFullRoomNum));
                pwRoomEnumDtos.add(new RoomUseStatusDto(RoomUseStatus.NOTALLOTROOM.getStatus(),RoomUseStatus.NOTALLOTROOM.getStatusname(),roomList.size() - fullRoomNum - notFullRoomNum));
                break;
        }
        return ApiResult.success(pwRoomEnumDtos);
    }

    /**
     * 分配房间查询接口
     * @param pwRoom
     * @param request
     * @param response
     * @return ApiResult
     */
    @RequestMapping(value = "roomAllotList")
    @ResponseBody
    public ApiResult roomAllotList(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response){
        try{
            Page<PwRoom> page = pwRoomService.findAllotRoomPage(new Page<PwRoom>(request,response),pwRoom);
            return ApiResult.success(page);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    /**
     * 房间使用记录接口
     * @param pwRoom
     * @param request
     * @param response
     * @return ApiResult
     */
    @RequestMapping(value = "roomUseList")
    @ResponseBody
    public ApiResult roomUseList(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response){
        try{
            Page<PwRoom> page = pwRoomService.findRoomUsePage(new Page<PwRoom>(request,response),pwRoom);
            return ApiResult.success(page);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    /**
     * 查看预约详情
     * @param rid 房间id
     * @param queryType 查询类型：1-当前记录，2-历史记录
     * @return ApiResult
     */
    @RequestMapping(value = "roomOrderDetailList")
    @ResponseBody
    public ApiResult roomNowOrderList(String rid,String queryType, PwAppointment pwAppointment, HttpServletRequest request, HttpServletResponse response){
        try{
            PwRoom pw = pwRoomService.get(rid);
            pwAppointment.setPwRoom(pw);
            Page<PwAppointment> page = pwAppointmentService.roomOrderDetailList(new Page<PwAppointment>(request,response),pwAppointment,queryType);
            return ApiResult.success(page);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    /**
     * 查看分配详情
     * @param queryType 查询类型：1-当前记录，2-历史记录
     * @return ApiResult
     */
    @RequestMapping(value = "roomAllotDetailList")
    @ResponseBody
    public ApiResult roomAllotDetailList(String queryType, PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response){
        try {
            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            pwEnter.setPstatus(PwEnterStatus.getCGKeyByQuery());
            pwEnter.setPrestatus(PwEroomStatus.getKeyByDFP());
            Page<PwEnter> page = pwEnterService.findAllotPageByGroup(new Page<PwEnter>(request, response), pwEnter, queryType);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 其他房间查询
     */
    @RequestMapping(value="roomOtherList")
    @ResponseBody
    public ApiResult roomOtherList(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response){
        try{
            Page<PwRoom> page = pwRoomService.findOtherRoomPage(new Page<PwRoom>(request,response),pwRoom);
            return ApiResult.success(page);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"treeFPCD"})
    public String treeFP(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomTreeFPCD";
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = {"listFPCD"})
    public String listFPCD(PwRoom pwRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PwRoom> page = pwRoomService.findPageByJLKfpCD(new Page<PwRoom>(request, response), pwRoom);
        model.addAttribute("page", page);
        model.addAttribute("root", CoreIds.NCE_SYS_TREE_ROOT.getId());
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomListFPCD";
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = "form")
    public String form(PwRoom pwRoom, Model model, RedirectAttributes redirectAttributes) {
        pwRoom = pwRoomService.get(pwRoom.getId());
        if ((pwRoom == null)) {
            pwRoom = new PwRoom();
            pwRoom.setColor(PwRoomService.ROOMDEFAULTCOLOR);
        }
        model.addAttribute("typeid", PwRoomService.ROOMTYPEDICT);
        model.addAttribute("pwRoom", pwRoom);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomForm";
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = "formSetZC")
    public String formSetZC(PwRoom pwRoom, Model model) {
        model.addAttribute("pwRoom", pwRoom);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomFormSetZC";
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = "formSetCD")
    public String formSetCD(PwRoom pwRoom, Model model) {
        model.addAttribute("pwRoom", pwRoom);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomFormSetCD";
    }

    @RequiresPermissions("pw:pwRoom:view")
    @RequestMapping(value = "view")
    public String view(PwRoom pwRoom, Model model) {
        List<PwEnter> pwEnters = Lists.newArrayList();
        if ((pwRoom != null) && StringUtil.isNotEmpty(pwRoom.getId())) {
            PwEnter pwEnter = new PwEnter();
            pwEnter.setPstatus(PwEnterStatus.getKeyByYFP());
            pwEnters = pwEnterService.findListByGroup(pwEnter);
        }
        model.addAttribute("pwRoom", pwRoom);
        model.addAttribute("pwEnters", pwEnters);
        model.addAttribute("root", CoreIds.NCE_SYS_TREE_ROOT.getId());
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwRoomView";
    }

    @RequiresPermissions("pw:pwRoom:view")
    @ResponseBody
    @RequestMapping(value = "roomEnters")
    public Map<String, Object> getEntersByRoom(PwRoom pwRoom) {
        Map<String, Object> map = new HashMap<>();
        List<PwEnter> pwEnters = Lists.newArrayList();
        if ((pwRoom != null) && StringUtil.isNotEmpty(pwRoom.getId())) {
            pwRoom = pwRoomService.get(pwRoom.getId());
            PwEnter pwEnter = new PwEnter();
            pwEnter.setPstatus(PwEnterStatus.getKeyByYFP());
            pwEnters = pwEnterService.findListByGroup(pwEnter);
        }
        map.put("enters", pwEnters);
        map.put("room", pwRoom);
        return map;
    }


    @RequiresPermissions("pw:pwRoom:view")
    @ResponseBody
    @RequestMapping(value = "roomTreeData")
    public List<Map<String, Object>> roomTreeData(PwRoom pwRoom, @RequestParam(required = false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<PwSpaceRoom> list = pwRoomService.findSpaceAndRoom();
        Set<String> idSet = new HashSet<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            PwSpaceRoom pr = list.get(i);
            if (pr.getPwSpace() == null) {
                continue;
            }
            if (StringUtils.isNotBlank(pr.getRoomId())) {//房间
                if (!idSet.contains(pr.getPwSpace().getId())) {
                    //楼层
                    Map<String, Object> map1 = Maps.newHashMap();
                    map1.put("id", pr.getPwSpace().getId());
                    map1.put("pId", pr.getPwSpace().getParent().getId());
                    map1.put("name", pr.getPwSpace().getName());
                    map1.put("type", pr.getPwSpace().getType());
                    map1.put("isParent", true);
                    mapList.add(map1);
                    idSet.add(pr.getPwSpace().getId());
                }
                if ("1".equals(pr.getRoomDelFlag())) {
                    continue;
                }
                if (pwRoom != null) {
                    if (StringUtils.isNotBlank(pwRoom.getIsUsable()) && !pwRoom.getIsUsable().equals(pr.getUsable())) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(pwRoom.getIsAssign()) && !pwRoom.getIsAssign().equals(pr.getPwRoom().getIsAssign())) {
                        continue;
                    }
                }
                //房间
                Map<String, Object> map2 = Maps.newHashMap();
                map2.put("id", pr.getRoomId());
                map2.put("pId", pr.getPwSpace().getId());
                map2.put("name", pr.getRoomName());
                map2.put("type", "room");
                map2.put("respName", pr.getRespName());
                map2.put("mobile", pr.getPwRoom().getMobile());
                map2.put("num", pr.getPwRoom().getNum());
                map2.put("isParent", false);
                mapList.add(map2);
            } else {//非房间（学校、校区、基地、楼栋）
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", pr.getPwSpace().getId());
                map.put("pId", pr.getPwSpace().getParent().getId());
                map.put("name", pr.getPwSpace().getName());
                map.put("type", pr.getPwSpace().getType());
                map.put("isParent", true);
                mapList.add(map);
            }
        }
        return mapList;
    }

    @RequiresPermissions("pw:pwRoom:view")
    @ResponseBody
    @RequestMapping(value = "jsonList")
    public List<PwRoom> list(PwRoom pwRoom) {
        return pwRoomService.findList(pwRoom);
    }


}