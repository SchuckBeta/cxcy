package com.oseasy.auy.modules.menu.web.front;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontStudentExpansion")
public class FrontAuyCmsStudentExpansionController extends BaseController {
    @Autowired
    StudentExpansionService studentExpansionService;
    @Autowired
    SysViewsService sysViewsService;

    @RequestMapping(value = "ajaxGetViewAndLike")
    @ResponseBody
    public JSONObject ajaxGetViewAndLike(String id) {
        JSONObject js= new JSONObject();
        StudentExpansion studentExpansion = studentExpansionService.get(id);
        if (StringUtil.isEmpty(studentExpansion.getUser().getViews())) {
            studentExpansion.getUser().setViews("0");
        }
        if (StringUtil.isEmpty(studentExpansion.getUser().getLikes())) {
            studentExpansion.getUser().setLikes("0");
        }
        js.put("likes",studentExpansion.getUser().getLikes());
        js.put("views",studentExpansion.getUser().getViews());
        List<Map<String,String>> visitors=sysViewsService.getVisitors(studentExpansion.getUser().getId());
        DateFormat dateFormat = new SimpleDateFormat(DateUtil.FMT_YYYYMMDD_ZG);

        for (Map<String, String> visitor : visitors) {
            visitor.put("create_date", dateFormat.format(visitor.get("create_date")));
        }
        js.put("visitors",visitors);
        return js;
    }
}