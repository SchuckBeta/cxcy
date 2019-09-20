package com.oseasy.pw.modules.pw.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpaceRoom;
import com.oseasy.pw.modules.pw.service.PwRoomService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "${frontPath}/pw/pwRoom")
public class FrontPwRoomController extends BaseController {

    @Autowired
    private PwRoomService pwRoomService;

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
                }
                //房间
                Map<String, Object> map2 = Maps.newHashMap();
                map2.put("id", pr.getRoomId());
                map2.put("pId", pr.getPwSpace().getId());
                map2.put("name", pr.getRoomName());
                map2.put("type", "room");
                map2.put("respName", pr.getRespName());
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


    @ResponseBody
    @RequestMapping(value = "jsonList")
    public List<PwRoom> list(PwRoom pwRoom) {
        return pwRoomService.findList(pwRoom);
    }
}
