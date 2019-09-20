/**
 *
 */
package com.oseasy.auy.modules.menu.web;

import com.oseasy.auy.modules.menu.service.AuyActTaskService;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;

/**
 * 菜单Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class AuyMenuController extends BaseController {
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private AuyActTaskService auyActTaskService;

    @RequestMapping(value = "/totoReflash/{actywId}", method = RequestMethod.GET)
    @ResponseBody
    public String totoReflash(@PathVariable String actywId) {
        try{
            return proActTaskService.flashTodoCount(actywId);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            JSONObject response = new JSONObject();
            response.put("msg", "查询异常");
            response.put("result", null);
            return response.toString();
        }


    }

    @ResponseBody
    @RequestMapping(value = "/reflashTodoCount/{actywId}", method = RequestMethod.GET)
    public int reflashTodoCount(@PathVariable String actywId, String gnodeId, Model model) {
//        return proActTaskService.todoCount(actywId, gnodeId);
        return auyActTaskService.todoCount(actywId, gnodeId);
    }
}
