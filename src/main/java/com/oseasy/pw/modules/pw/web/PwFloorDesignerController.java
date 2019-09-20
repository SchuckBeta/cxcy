package com.oseasy.pw.modules.pw.web;

import java.util.HashMap;
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

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwDesignerCanvas;
import com.oseasy.pw.modules.pw.entity.PwFloorDesigner;
import com.oseasy.pw.modules.pw.entity.PwFloorRooms;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.service.PwDesignerCanvasService;
import com.oseasy.pw.modules.pw.service.PwFloorDesignerService;
import com.oseasy.pw.modules.pw.service.PwSpaceService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * 楼层设计Controller.
 *
 * @author 章传胜
 * @version 2017-11-28
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwFloorDesigner")
public class PwFloorDesignerController extends BaseController {

    @Autowired
    private PwDesignerCanvasService pwDesignerCanvasService;
    @Autowired
    private PwFloorDesignerService pwFloorDesignerService;
    @Autowired
    private PwSpaceService pwSpaceService;

    @ModelAttribute
    public PwFloorDesigner get(@RequestParam(required = false) String id) {
        PwFloorDesigner entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwFloorDesignerService.get(id);
        }
        if (entity == null) {
            entity = new PwFloorDesigner();
        }
        return entity;
    }


    @RequestMapping(value = {"list"}, method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String list(@RequestParam(required = true) String floorId, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("floorId", floorId);
        PwSpace pwSpace = pwSpaceService.get(floorId);
//        PwFloorDesigner pwFloorDesigner = pwFloorDesignerService.get(floorId);
        if((pwSpace != null) && StringUtil.isNotEmpty(pwSpace.getName())){
            model.addAttribute("floorName", pwSpace.getName());
        }
        PwDesignerCanvas pwDesignerCanvas = pwDesignerCanvasService.getPwDesignerCanvasByFloorId(floorId);
        if((pwDesignerCanvas != null) && StringUtil.isNotEmpty(pwDesignerCanvas.getId())){
            model.addAttribute("pdCanvasId", pwDesignerCanvas.getId());
        }
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFloorPlan";
    }


    /**
     * 根据楼层ID获取该楼层房间以及房间内元素信息。
     */
    @RequestMapping(value = {"floorDesDates"}, method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public PwFloorRooms floorDesDates(@RequestParam(required = false) String floorId, HttpServletRequest request, HttpServletResponse response, Model model) {
        PwFloorDesigner pwFloorDesigner = new PwFloorDesigner();
        pwFloorDesigner.setFloorId(floorId);
        List<PwFloorDesigner> list = pwFloorDesignerService.findList(pwFloorDesigner);
        PwFloorRooms pwFloorRooms = new PwFloorRooms();
        pwFloorRooms.setRooms(list);
        pwFloorRooms.setFloorId(floorId);
        return pwFloorRooms;
    }

    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public JSONObject save(@RequestBody JSONObject param) {
        JSONObject result = new JSONObject();
        try {
            Map classMap = new HashMap();
            classMap.put("rooms",PwFloorDesigner.class);
            PwFloorRooms pwFloorRooms = (PwFloorRooms) JSONObject.toBean(param, PwFloorRooms.class,classMap);
            List<PwFloorDesigner> list = pwFloorRooms.getRooms();
            int res = pwFloorDesignerService.insertAll(list);
            result.put("status", "true");
            result.put("msg", "请求成功");
        } catch (Exception e) {
            result.put("status", "false");
            result.put("msg", ExceptionUtil.getStackTrace(e));
            logger.error(ExceptionUtil.getStackTrace(e));
        }
        return result;
    }


//    @ResponseBody
//    @RequestMapping(value = "saveFloor", method = RequestMethod.POST)
//    public JSONObject saveFloor(HttpServletRequest request, MultipartFile file){
//        JSONObject res = new JSONObject();
////        String pic = request.getParameter("file");
//        String filename = file.getName();
//        return  res;
//    }



    @RequestMapping(value = {"pwFloorPlan"})
    public String show() {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFloorTree";
    }


}