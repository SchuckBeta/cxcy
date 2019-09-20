package com.oseasy.pw.modules.pw.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.service.PwSpaceService;
import com.oseasy.util.common.utils.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/pw/pwSpace")
public class FrontPwSpaceController extends BaseController {

    @Autowired
    private PwSpaceService pwSpaceService;

    @ResponseBody
    @RequestMapping(value = "jsonList")
    public List<PwSpace> list(PwSpace pwSpace) {
        return pwSpaceService.findList(pwSpace);
    }

    @ResponseBody
    @RequestMapping(value = "children/{id}")
    public List<PwSpace> findChildren(@PathVariable String id) {
        return pwSpaceService.findChildren(id);
    }

    /**
     * 场地树
     * @param extId
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<PwSpace> list = pwSpaceService.findList(new PwSpace());
        for (int i = 0; i < list.size(); i++) {
            PwSpace e = list.get(i);
            if (StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                map.put("type", e.getType());
                mapList.add(map);
            }
        }
        return mapList;
    }
}
