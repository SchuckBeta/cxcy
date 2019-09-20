/**
 *
 */
package com.oseasy.pro.modules.promodel.web;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;

/**
 * 流程个人任务相关Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ProActTaskController extends BaseController {
	@Autowired
	private ProActTaskService proActTaskService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;

    //获取跟踪信息
    @RequestMapping(value = "getNodeByProInsId")
    public String getNodeByProInsId(String proInstId, Model model) throws Exception {
        //String gnodeId=ProcessMapUtil.getNodeByProInsId(repositoryService, actTaskService, runtimeService, proInstId);
        ActYwGnode actYwGnode= proActTaskService.getNodeByProInsId(proInstId);
        return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskMap";
    }
}
