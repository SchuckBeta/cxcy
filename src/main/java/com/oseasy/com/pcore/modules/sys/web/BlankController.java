package com.oseasy.com.pcore.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.config.CorePages;

/**
 * Created by zhangzheng on 2017/4/8.
 */
@Controller
@RequestMapping(value = "${adminPath}/blank")
public class BlankController {
    @RequestMapping(value = "")
    public String blank(Model model) {
        model.addAttribute("msg","该模块还在建设中");
        return CorePages.BLANK_AHOME.getIdxUrl();
    }

    @RequestMapping(value = "body")
    public String blankBody(Model model) {
        model.addAttribute("msg","该模块还在建设中");
        return CorePages.BLANK_ABODY.getIdxUrl();
    }
}
