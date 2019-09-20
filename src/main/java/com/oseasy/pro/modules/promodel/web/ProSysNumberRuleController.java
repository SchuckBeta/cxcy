package com.oseasy.pro.modules.promodel.web;

import java.util.List;

import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.service.ProSysNumberRuleService;
import com.oseasy.sys.common.utils.Tree;
import com.oseasy.util.common.utils.Msg;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 编号规则管理Controller.
 * @author 李志超
 * @version 2018-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysNumberRule")
public class ProSysNumberRuleController extends BaseController {
    @Autowired
    private ProSysNumberRuleService proSysNumberRuleService;
    @Autowired
    private SysNumberRuleService sysNumberRuleService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @ModelAttribute
    public SysNumberRule get(@RequestParam(required=false) String id) {
        SysNumberRule entity = null;
        if (StringUtil.isNotBlank(id)){
            entity = sysNumberRuleService.get(id);
        }
        if (entity == null){
            entity = new SysNumberRule();
        }
        return entity;
    }
    /**
     * 查询获取应用类型
     * @return
     */
    @RequestMapping("/getAppTypeTree")
    @ResponseBody
    public Msg getAppTypeTree() {
        List<Tree> treeList = proSysNumberRuleService.getAppTypeTreeList();
        return Msg.ok().put("data", treeList);
    }

    @RequestMapping(value = "getGnodeIdByActYw")
    @ResponseBody
    public Msg getGnodeIdByActYw(String actywId) {
        List<ActYwGnode> list= actYwGnodeService.getAuditNodes(actywId);
        if(StringUtil.checkNotEmpty(list)){
            return Msg.ok().put("data", list).put("msg", "当前应用可添加");
        }else{
            return Msg.error("当前编号规则项目级别");
        }
    }
}