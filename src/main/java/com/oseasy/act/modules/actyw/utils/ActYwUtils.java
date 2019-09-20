package com.oseasy.act.modules.actyw.utils;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.util.common.utils.StringUtil;

public class ActYwUtils {
	private static ActYwService actYwService = SpringContextHolder.getBean(ActYwService.class);
	private static ActYwGnodeService actYwGnodeService = SpringContextHolder.getBean(ActYwGnodeService.class);
	public static List<ActYw> getActListData(String ftype) {
	    return actYwService.findListByDeploy(ftype);
	}

	public static boolean isFirstMenu(String actywid,String gnodeid){
		if(StringUtil.isEmpty(gnodeid)){
			return false;
		}
		ActYw actYw =actYwService.get(actywid);
		if(actYw==null){
			return false;
		}
		if (actYw.getGroupId() == null) {
			return false;
		}
		ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
		List<ActYwGnode> sourcelist = actYwGnodeService.findListBygMenu(actYwGnode);
		if(sourcelist==null||sourcelist.size()==0){
			return false;
		}
		if(gnodeid.equals(sourcelist.get(0).getId())){
			return true;
		}
		return false;
	}
    public static List<ActYwGnode> getAssignNodes(String ywId) {
    	return actYwGnodeService.getAssignNodes(ywId);
    }
	public static String getFlowName(String key) {
	  FlowType flowType = FlowType.getByKey(key);
	  if (flowType == null) {
	    return "";
	  }
	  return flowType.getName();
	}

	public static String getFlowSname(String key) {
	  FlowType flowType = FlowType.getByKey(key);
	  if (flowType == null) {
	    return "";
	  }
	  return flowType.getSname();
	}
}
