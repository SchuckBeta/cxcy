/**
 *
 */
package com.oseasy.sys.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import com.oseasy.sys.common.utils.IdUtils;

import net.sf.json.JSONObject;

/**
 * 字典Controller

 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class SysDictController extends BaseController {
    @Autowired
    private DictService dictService;

    /*新增字典类型*/

    @RequestMapping(value = "addDictType")
    @ResponseBody
    public JSONObject addDictType(HttpServletRequest request, HttpServletResponse response) {
        String name=request.getParameter("name");
        return dictService.addDictType(name, IdUtils.getDictNumberByDb());
    }

    /*新增字典*/

    @RequestMapping(value = "addDict")
    @ResponseBody
    public JSONObject addDict(HttpServletRequest request, HttpServletResponse response) {
        String typeid=request.getParameter("typeid");
        String name=request.getParameter("name");
        String sort=request.getParameter("sort");
        return dictService.addDict(typeid,name,sort, IdUtils.getDictNumberByDb());
    }
}
