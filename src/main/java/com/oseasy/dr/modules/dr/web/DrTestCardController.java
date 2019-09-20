package com.oseasy.dr.modules.dr.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.service.DRDeviceService;
import com.oseasy.dr.modules.dr.vo.DrAuth;
import com.oseasy.dr.modules.dr.vo.DrCstatus;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.util.common.utils.DateUtil;

@Controller
@RequestMapping(value = "${adminPath}/door")
public class DrTestCardController extends BaseController  {

//    @Autowired
//    private OaNotifyService oaNotifyService;

    @Autowired
    private DRDeviceService deviceService;


    @RequestMapping(value = {"/testdoor"})
    public String testdoor(Model model) {
//        Map<String, DrEquipment> drEquipmentMap = CardFactory.drEquipmentMap;
//        model.addAttribute("drEquipmentMap", drEquipmentMap);
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoorbak";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteCard", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String deleteCard(String id) {
        Long cardNo = 13746859l;
        String equipmentId = "1";
        deviceService.deleteCard(cardNo,equipmentId);
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoorbak";
    }

    @ResponseBody
    @RequestMapping(value = "/getCard", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getCard() {
        String cardNo = "13746859";
        //有特性方法时，使用case操作
        //mock data
        DrCard card = new DrCard();
        card.setNo(cardNo);
        String equipmentId = "1";
        deviceService.getCard(card,equipmentId);
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoorbak";
    }


    @ResponseBody
    @RequestMapping(value = "/uploadCard", method = RequestMethod.POST)
    public String uploadCard(HttpServletRequest request, HttpServletResponse response, Model model) {
       //MOCK data;
        DrCard card = new DrCard();
        card.setNo("13746859");
        card.setPassword("99999");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date date = sdf.parse("2022-12-16 16:00");
            card.setExpiry(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        card.setOpenTimes(65535);
        card.setStatus(DrCstatus.DC_NORMAL.getKey());
        card.setHolidayUse(true);
        card.setPrivilege(DrAuth.DA_NONE.getKey());
        card.setDrNo(DrConfig.DET_DR_NO);
        String equipmentId ="630466e4df714eb0ad7952e65f2aa079";
        //mock data end;
//        deviceService.uploadCard(card, "equipmentId" );

        deviceService.uploadCard(card, "equipmentId" );
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoorbak";
    }




    @ResponseBody
    @RequestMapping(value = "/getRecordsByIndex", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getRecordsByIndex() {
        System.out.println("getRecordsByIndex.....");
        deviceService.getRecordsByIndex();
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoor";
    }


    @ResponseBody
    @RequestMapping(value = "/getNewRecords", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getNewRecords() {
        System.out.println("getNewRecords.....");
        deviceService.getNewRecords();
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoor";
    }

    @ResponseBody
    @RequestMapping(value = "/disposeDrCardRecord", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String disposeDrCardRecord() {
        Date day = new Date();
        DrUtils.disposeDrCardRecord(DateUtil.formatDate(day, "yyyy-MM-dd"));
        return DrSval.path.vms(DrEmskey.DR.k()) + "testdoor";
    }

    @ResponseBody
    @RequestMapping(value = "/testSeqPage", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String testSeqPage(HttpServletRequest request, HttpServletResponse response) {
        return SysSval.path.vms(SysEmskey.SEQ.k()) + "testSeqPage";
    }

}
