/**
 *
 */
package com.oseasy.pro.modules.promodel.utils;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;

/**
 * 内容管理工具类


 */
public class ProModelUtils {
  	private static ProModelDao proModelDao = SpringContextHolder.getBean(ProModelDao.class);
	private static ProProjectService proProjectService = SpringContextHolder.getBean(ProProjectService.class);
	private static ProActTaskService proActTaskService = SpringContextHolder.getBean(ProActTaskService.class);
  	public static ProModel getProModelById(String id) {
    return proModelDao.get(id);
  }


	/**根据模板类型获取大赛结果html代码*/
	public static String getGrateSelect(String proProjectId) {
		ProProject proProject=proProjectService.get(proProjectId);
		String html="";
		if (proProject!=null) {
			String finalStatus=proProject.getFinalStatus();
			if (finalStatus!=null) {
				String[] finalStatuss=finalStatus.split(",");
				if (finalStatuss.length>0) {
					for(int i=0;i<finalStatuss.length;i++) {
						html +="<option value='"+finalStatuss[i]+"'>"+
								DictUtils.getDictLabel(finalStatuss[i],"competition_college_prise","")
								+"</option>";
					}
				}
			}
		}

		return html;
	}

	/**根据模板类型获取大赛级别html代码*/
	public static String getLevelSelect(String proProjectId) {
		ProProject proProject=proProjectService.get(proProjectId);
		String html="";
		if (proProject!=null) {
			String level=proProject.getLevel();
			if (level!=null) {
				String[] levels=level.split(",");
				if (levels.length>0) {
					for(int i=0;i<levels.length;i++) {
						html +="<option value='"+levels[i]+"'>"+
								DictUtils.getDictLabel(levels[i],"gcontest_level","")
								+"</option>";
					}
				}
			}
		}
		return html;
	}

	/**根据模板类型获取大赛类型html代码*/
	public static String getTypeSelect(String proProjectId) {
		ProProject proProject=proProjectService.get(proProjectId);
		String html="";
		if (proProject!=null) {
			String type=proProject.getType();
			if (type!=null) {
				String[] types=type.split(",");
				if (types.length>0) {
					for(int i=0;i<types.length;i++) {
						html +="<option value='"+types[i]+"'>"+
								DictUtils.getDictLabel(types[i],"competition_type","")
								+"</option>";
					}
				}
			}
		}
		return html;
	}
	public static String getProModelAuditNameById (String proid) {
		ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(proid);
		if (actYwGnode!=null) {
			return actYwGnode.getName();
		}
		return "";
	}

	/**
	 * 获取promodel
	 * @param tenantId tenantId
	 * @param proModelId proModelId
	 * @return ProModel
	 */
	public static ProModel getProModelOfCache(String tenantId,String proModelId){
		String key = ProSval.ck.cks(ProSval.ProEmskey.PROMODEL, tenantId, proModelId);
		ProModel proModel = null;
		if (JedisUtils.hasKey(key)){
			Object obj = JedisUtils.getObject(key);
			if (obj != null){
				return (ProModel)obj;
			}
			proModel = proModelDao.get(proModelId);
			if (proModel != null){
				JedisUtils.storage(key,proModel);
			}
			return proModel;
		}
		return null;
	}


	/**
	 *缓存
	 * @param proModel proModel
	 * @param tenantId tenantId
	 */
	public static void storageProModel(ProModel proModel,String tenantId){
		String key = ProSval.ck.cks(ProSval.ProEmskey.PROMODEL, tenantId, proModel.getId());
		JedisUtils.storage(key,proModel);
	}

}