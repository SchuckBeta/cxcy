package com.oseasy.pw.modules.pw.web;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.SysProp;
import com.oseasy.com.pcore.modules.sys.service.SysPropService;
import com.oseasy.com.pcore.modules.sys.vo.CorePropType;
import com.oseasy.pw.modules.pw.service.PwRenewalRuleService;
import com.oseasy.pw.modules.pw.vo.PwPropType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统功能Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysProp")
public class PwPropController extends BaseController {
    @Autowired
    private SysPropService sysPropService;
    @Autowired
    private PwRenewalRuleService pwRenewalRuleService;

    @RequiresPermissions("sys:sysProp:view")
    @RequestMapping(value = "setProp")
    public String setProp(SysProp sysProp, Model model) {
        if(StringUtil.isNotEmpty(sysProp.getId())){
            sysProp = sysPropService.get(sysProp.getId());
            Map<String, Object> maps = PwPropType.getMaps(pwRenewalRuleService);
//          maps.put(SysPropType.SPT_ENTER.getKey(), JsonMapper.fromJsonString("", sysProp.getItems().get(0).getParams()));
            //maps.put(SysPropType.SPT_DOOR.getKey(), sysProp);


            model.addAttribute("sysPropAA", sysProp);
            model.addAttribute("sysProp", SysProp.render(maps, sysProp));
        }
        model.addAttribute(CorePropType.SYS_PROP_TYPES, CorePropType.values());
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysPropSetForm";
    }
}