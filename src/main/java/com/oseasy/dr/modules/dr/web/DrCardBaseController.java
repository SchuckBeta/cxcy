package com.oseasy.dr.modules.dr.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.dr.modules.dr.service.DrBaseService;
import com.oseasy.dr.modules.dr.vo.DrAuth;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/doorBase")
public class DrCardBaseController extends BaseController  {

    @Autowired
    private DrBaseService drBaseService;

    @RequestMapping(value = {"/testdoor"})
    public String testdoor(Model model) {
        return "modules/test/testdoor";
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
        drBaseService.getCard(card,equipmentId);
        return "modules/test/testdoor";
    }
    @ResponseBody
    @RequestMapping(value = "/getRecond", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public JSONObject getRecond(HttpServletRequest request) {
        JSONObject js=new JSONObject();
        String no=request.getParameter("no");
        String end=(String)CacheUtils.get("connect",no);
        if(StringUtil.isEmpty(end)){
            end="读取数据为空";
        }
        js.put("msg",end);
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/getOpenCardRecond", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public JSONObject getOpenCardRecond(HttpServletRequest request) {
        JSONObject js=new JSONObject();
        String cardNo=request.getParameter("cardNo");
        String end=(String)CacheUtils.get("addCard",cardNo);
        if(StringUtil.isEmpty(end)){
            end="读取数据为空";
        }
        js.put("msg",end);
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/getReadCardNoRecond", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public JSONObject getReadCardNoRecond(HttpServletRequest request) {
        JSONObject js=new JSONObject();
        String readCardNo=request.getParameter("readCardNo");
        String end=(String)CacheUtils.get("readCard",readCardNo);
        if(StringUtil.isEmpty(end)){
            end="读取数据为空";
        }
        js.put("msg",end);
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/getReadNewRecord", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public JSONObject getReadRecond(HttpServletRequest request) {
        JSONObject js=new JSONObject();
        String end="";
        List<String> stringEnd=(List<String>)CacheUtils.get("readNewRecordList");
        if(StringUtil.checkEmpty(stringEnd)){
            end=(String)CacheUtils.get("readNewRecord");
        }else{
            for(String record:stringEnd){
                end=end+record+"\n";
            }
        }
        js.put("msg",end);
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/clearRedis", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public JSONObject clearRedis(HttpServletRequest request) {
        JSONObject js=new JSONObject();
        CacheUtils.removeAll("readCard");
        CacheUtils.removeAll("addCard");
        CacheUtils.removeAll("connect");

        js.put("msg","清理缓存成功");
        return js;
    }

    @ResponseBody
    @RequestMapping(value = "/connetEq", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String connetEq(DrEquipment erEquipment ,HttpServletRequest request) {

        String ip=request.getParameter("ip");
        String port=request.getParameter("port");
        String no=request.getParameter("no");
        String psw=request.getParameter("psw");
        erEquipment.setIp(ip);
        erEquipment.setPort(Integer.parseInt(port));
        erEquipment.setNo(no);
        erEquipment.setPsw(psw);

        drBaseService.isConnectEq(erEquipment);
        return "modules/test/testdoor";
    }


    @ResponseBody
    @RequestMapping(value = "/openCard", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String openCard(DrEquipment erEquipment ,HttpServletRequest request) {
        String cardNo=request.getParameter("cardNo");
        String cardPass=request.getParameter("cardPass");
        String openNum=request.getParameter("openNum");
        String openState=request.getParameter("openState");
        String openDoor=request.getParameter("openDoor");
        DrCard card = new DrCard();
        card.setNo(cardNo);
        card.setPassword(cardPass);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date date = sdf.parse("2022-12-16 16:00");
            card.setExpiry(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        card.setOpenTimes(Integer.parseInt(openNum));
        card.setStatusBytes(Integer.parseInt(openState));
        card.setHolidayUse(true);
        card.setPrivilege(DrAuth.DA_NONE.getKey());
        if(openDoor!=null){
            card.setDrNo(openDoor);
        }else {
            card.setDrNo(DrConfig.DET_DR_NO);
        }
        drBaseService.uploadCard(card,"eqId");
        return "modules/test/testdoor";
    }

    @ResponseBody
    @RequestMapping(value = "/readCardNo", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String readCardNo(DrEquipment erEquipment ,HttpServletRequest request) {
        String readCardNo=request.getParameter("readCardNo");
        DrCard card = new DrCard();
        card.setNo(readCardNo);
        String equipmentId = "1";
        drBaseService.getCard(card,equipmentId);
        //drBaseService.isConnectEq(erEquipment);
        return "modules/test/testdoor";
    }

    @ResponseBody
    @RequestMapping(value = "/readNewRecord", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String readNewRecord(DrEquipment erEquipment ,HttpServletRequest request) {
        String readNum=request.getParameter("readNum");

        String equipmentId = "1";
        drBaseService.readNewRecord(readNum);
        //drBaseService.isConnectEq(erEquipment);
        return "modules/test/testdoor";
    }

}
