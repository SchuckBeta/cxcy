/**
 *
 */
package com.oseasy.scr.modules.scr.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.scr.modules.sco.utils.ScoUtils;

/**
 * 字典Controller

 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class ScrDictController extends BaseController {
    @RequestMapping(value="getCurJoinProjects", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Dict> getCurJoinProjects(){
        return ScoUtils.getPublishDictList();
    }
}
