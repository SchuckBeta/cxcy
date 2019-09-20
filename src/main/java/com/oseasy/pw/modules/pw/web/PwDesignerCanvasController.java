package com.oseasy.pw.modules.pw.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwDesignerCanvas;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.service.PwDesignerCanvasService;
import com.oseasy.pw.modules.pw.service.PwSpaceService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 画布表Controller.
 *
 * @author zy
 * @version 2017-12-18
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwDesignerCanvas")
public class PwDesignerCanvasController extends BaseController {

    @Autowired
    private PwDesignerCanvasService pwDesignerCanvasService;

    @Autowired
    private PwSpaceService pwSpaceService;

    @ModelAttribute
    public PwDesignerCanvas get(@RequestParam(required = false) String id) {
        PwDesignerCanvas entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwDesignerCanvasService.get(id);
        }
        if (entity == null) {
            entity = new PwDesignerCanvas();
        }
        return entity;
    }

    @RequiresPermissions("pw:pwDesignerCanvas:view")
    @RequestMapping(value = {"list", ""})
    public String list(PwDesignerCanvas pwDesignerCanvas, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PwDesignerCanvas> page = pwDesignerCanvasService.findPage(new Page<PwDesignerCanvas>(request, response), pwDesignerCanvas);
        model.addAttribute("page", page);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwDesignerCanvasList";
    }

    @RequiresPermissions("pw:pwDesignerCanvas:view")
    @RequestMapping(value = "form")
    public String form(PwDesignerCanvas pwDesignerCanvas, Model model) {
        model.addAttribute("pwDesignerCanvas", pwDesignerCanvas);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwDesignerCanvasForm";
    }

    @RequiresPermissions("pw:pwDesignerCanvas:view")
    @RequestMapping(value = "index")
    public String index(PwDesignerCanvas pwDesignerCanvas, Model model) {
        model.addAttribute("pwDesignerCanvas", pwDesignerCanvas);
        Map<String, Object> map = pwSpaceService.basesAndBuildings();
        model.addAttribute("bases", map.get("bases"));
        model.addAttribute("buildings", map.get("buildings"));
        model.addAttribute("floorList", map.get("floorList"));
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwDesignerCanvasIndex";
    }

    @RequiresPermissions("pw:pwDesignerCanvas:view")
    @RequestMapping(value = "view")
    public String view(PwDesignerCanvas pwDesignerCanvas, Model model) {
        model.addAttribute("pwDesignerCanvas", pwDesignerCanvas);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwDesignerCanvasView";
    }

    @RequiresPermissions("pw:pwDesignerCanvas:edit")
    @RequestMapping(value = "save")
    public String save(PwDesignerCanvas pwDesignerCanvas, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, pwDesignerCanvas)) {
            return form(pwDesignerCanvas, model);
        }
        pwDesignerCanvasService.save(pwDesignerCanvas);
        addMessage(redirectAttributes, "保存画布表成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwDesignerCanvas/?repage";
    }

    @RequiresPermissions("pw:pwDesignerCanvas:edit")
    @RequestMapping(value = "delete")
    public String delete(PwDesignerCanvas pwDesignerCanvas, RedirectAttributes redirectAttributes) {
        pwDesignerCanvasService.delete(pwDesignerCanvas);
        addMessage(redirectAttributes, "删除画布表成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwDesignerCanvas/?repage";
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<PwDesignerCanvas> ajaxSave(@RequestBody PwDesignerCanvas pwDesignerCanvas, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, pwDesignerCanvas)) {
            return new ApiTstatus<PwDesignerCanvas>(false , "参数异常");
        }
        pwDesignerCanvasService.save(pwDesignerCanvas);
        return new ApiTstatus<PwDesignerCanvas>(true , "保存成功", pwDesignerCanvas);
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxGet/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<PwDesignerCanvas> ajaxGet(@PathVariable("id") String id, Model model) {
        PwDesignerCanvas pwDesignerCanvas = pwDesignerCanvasService.get(id);
        if (pwDesignerCanvas == null) {
            return new ApiTstatus<PwDesignerCanvas>(false , "获取设计不存在");
        }
        return new ApiTstatus<PwDesignerCanvas>(true , "获取成功", pwDesignerCanvas);
    }

    @ResponseBody
    @RequestMapping(value = "/saveAll", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public JSONObject saveAll(@RequestBody JSONObject json) {
        logger.info("saveall......");
        JSONObject jsonData = json;
        if (jsonData != null) {
            try {
                boolean res = pwDesignerCanvasService.saveAll(jsonData);
                //保存成功
                if (res) {
                    jsonData.put("ret", "1");
                    return jsonData;
                }
            } catch (Exception e) {
                logger.error("saveall保存失败");
                logger.error(e.getMessage());
            }
            jsonData.put("ret", "0");//保存失败
        }
        return jsonData;
    }

    @ResponseBody
    @RequestMapping(value = "getAll")
    public String getAll(HttpServletRequest request) {
        String floorId = request.getParameter("floorId");
        JSONObject jsonData = pwDesignerCanvasService.getAll(floorId);
        return jsonData.toString();
    }

    @RequiresPermissions("pw:pwDesignerCanvas:view")
    @ResponseBody
    @RequestMapping(value = "parentAndChildren")
    public Map<String, Object> parentAndChildren(PwSpace pwSpace, Model model) {
        return pwSpaceService.parentAndChildren(pwSpace.getId());
    }

}