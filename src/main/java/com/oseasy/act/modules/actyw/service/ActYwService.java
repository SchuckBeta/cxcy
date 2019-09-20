package com.oseasy.act.modules.actyw.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.act.common.config.ActSval;
//import com.oseasy.annotation.Domain;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.tool.process.vo.*;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActModelService;
import com.oseasy.act.modules.act.vo.ActRstatus;
import com.oseasy.act.modules.actyw.dao.ActYwDao;
import com.oseasy.act.modules.actyw.dao.ActYwGroupDao;
import com.oseasy.act.modules.actyw.dao.ActYwGtimeDao;
import com.oseasy.act.modules.actyw.tool.IeYwser;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IAywservice;
import com.oseasy.act.modules.actyw.tool.process.ActYwResult;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 项目流程关联Service.
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Service
@Transactional(readOnly = true)
public class ActYwService extends CrudService<ActYwDao, ActYw> implements IAywservice, IeYwser{
	protected static final Logger logger = Logger.getLogger(ActYwService.class);
	@Autowired
	ActYwGroupDao actYwGroupDao;
	@Autowired
	ActYwGtimeDao actYwGtimeDao;
	@Autowired
	private ActYwGtimeService actYwGtimeService;
    @Autowired
    ActYwGnodeService actYwGnodeService;
	@Autowired
    ActYwYearService actYwYearService;
	@Autowired
	ActModelService actModelService;
	@Autowired
	ActYwPscrelService actYwPscrelService;
	@Autowired
	ActYwScstepService actYwScstepService;
	@Autowired
	ActYwGroupRelationService actYwGroupRelationService;
	@Autowired
	SysAttachmentService sysAttachmentService;
	@Autowired
	private ActYwDao actYwDao;
	@Autowired
	private ActYwStepService actYwStepService;
	public ActYw get(String id) {
		ActYw actYw=null;
		String tenant= TenantConfig.getCacheTenant();
		String key = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, tenant)+id;
		Object obj=JedisUtils.getObject(key);
		obj = null;
		if(obj!=null){
			actYw=(ActYw)obj;
		}else{
			actYw =super.get(id);
			if (actYw != null){
				if (actYw.getIsNewRecord()){
					actYw.setIsNewRecord(false);
				}
				JedisUtils.setObject(key,actYw);
			}
		}
		return actYw;
	}


	public ActYw getById(String id) {
		return actYwDao.getById(id);
	}

	public List<ActYw> getByGroupId(ActYw entity) {
		return actYwDao.getByGroupId(entity);
	}

	public List<ActYw> getByKeyss(String keyss) {
		return dao.getByKeyss(keyss);
	}

	@Transactional(readOnly = false)
	public void updateIsShowAxisPL(List<ActYw> actYws, Boolean isShowAxis) {
		dao.updateIsShowAxisPL(actYws, isShowAxis);
	}

	/**
	 * 验证项目流程标识是否存在.
	 *
	 * @param keyss Key
	 * @param isNew 是否新增
	 * @return Boolean
	 */
	public Boolean validKeyss(String keyss, Boolean isNew) {
		List<ActYw> actYws = getByKeyss(keyss);
		if ((actYws == null) || (actYws.size() <= 0)) {
			return true;
		}

		int size = actYws.size();
		if (!isNew && (size == 1)) {
			return true;
		}
		return false;
	}

	public List<ActYw> findList(ActYw actYw) {
		return super.findList(actYw);
	}

	/**
	 * 根据条件查询已部署的流程.
	 *
	 * @param actYw 项目流程
	 * @return List
	 */
	public List<ActYw> findListByDeploy(ActYw actYw) {
		actYw.setIsDeploy(true);
		return dao.findListByDeploy(actYw);
	}

	/**
	 * 根据流程类型条件查询已部署的流程.
	 *
	 * @return List
	 */
	public List<ActYw> findListByDeploy(FlowType flowType) {
		if (flowType == null) {
			return null;
		}
		ActYw pactYw = new ActYw();
		ActYwGroup pactYwGroup = new ActYwGroup();
		pactYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_1);
		pactYwGroup.setDelFlag(Const.NO);
		pactYwGroup.setFlowType(flowType.getKey());
		pactYw.setGroup(pactYwGroup);
		pactYw.setDelFlag(Const.NO);
		return findListByDeploy(pactYw);
	}

	/**
	 * 根据流程类型条件查询已部署的流程. 如果类型为空，返回所有已部署流程.
	 *
	 * @param ftype 排除的ID
	 * @return
	 */
	public List<ActYw> findListByDeploy(String ftype) {
		List<ActYw> actYws = Lists.newArrayList();
		if (StringUtil.isNotEmpty(ftype)) {
			FlowType flowType = FlowType.getByKey(ftype);
			if (flowType != null) {
				actYws = findListByDeploy(flowType);
			} else {
				logger.warn("类型参数未定义!");
			}
		} else {
			actYws = findListByDeploy(new ActYw());
		}
		return actYws;
	}

    /**
     * 根据条件查询流程.
     * @param flowType 流程类型
     * @return List
     */
  public List<ActYw> findCurrsByflowType(String flowType){
      return dao.findCurrsByflowType(flowType);
  }

  /**
   * 根据条件查询流程.
   * @param flowType 流程类型
   * @return List
   */
    public List<ActYw> findCurrsByflowTypeAndPType(String flowType, String ptype){
        return dao.findCurrsByflowTypeAndPtype(flowType, ptype);
    }

    public ActYw findCurrsByflowTypeAndPType(IActYw iactYw){
        if((iactYw == null) || (iactYw.flowType() == null) || StringUtil.isEmpty(iactYw.ptype())){
            return null;
        }

        List<ActYw> actYws = findCurrsByflowTypeAndPType(iactYw.flowType().getKey(), iactYw.ptype());
        if(StringUtil.checkNotEmpty(actYws)){
            return actYws.get(0);
        }
        return null;
    }

    /**
     * 检查是否当前流程类型下的项目流程已存在.
     * @return Boolean
     */
    public Boolean checkHasCurr(String id, String ptype){
        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(ptype)){
            return false;
        }
        ActYwGroup actYwGroup = actYwGroupDao.get(id);
        if(actYwGroup == null){
            return false;
        }

        List<ActYw> actYws = findCurrsByflowTypeAndPType(actYwGroup.flowType().getKey(), ptype);
        if(StringUtil.checkNotEmpty(actYws)){
            return true;
        }
        return false;
    }

    /**
     * 检查是否当前流程类型下的项目流程已存在,存在则更新为不存在.
     * @param id 流程
     * @return Boolean
     */
    @Transactional(readOnly = false)
    public ActYw updateCurr(String id){
        return updateCurr(get(id));
    }

    /**
     * 检查是否当前流程类型下的项目流程已存在,存在则更新为不存在.
     * @param iactYw 流程
     * @return Boolean
     */
    @Transactional(readOnly = false)
    public ActYw updateCurr(IActYw iactYw){
        ActYw actYw = (ActYw) iactYw;
        if((actYw == null) || (actYw.flowType() == null)){
            return actYw;
        }

        if((FlowType.FWT_XM).equals(actYw.flowType()) || (FlowType.FWT_DASAI).equals(actYw.flowType())){
            if(StringUtil.isEmpty(actYw.ptype())){
                return actYw;
            }

            List<ActYw> actYws = findCurrsByflowTypeAndPType(actYw.flowType().getKey(), actYw.ptype());
            if(StringUtil.checkNotEmpty(actYws)){
                dao.updateIsCurr(actYws, Const.NO);
            }
        }else{
            List<ActYw> actYws = findCurrsByflowType(actYw.flowType().getKey());
            if(StringUtil.checkNotEmpty(actYws)){
                dao.updateIsCurr(actYws, Const.NO);
            }
        }
        actYw.setIsCurr(Const.YES);
        return actYw;
    }

	  /**
	   * 根据条件查询当前流程.
	   * @param groupId 流程ID
	   * @return List
	   */
	  public List<ActYw> findCurrsByGroup(String groupId){
	      return dao.findCurrsByGroup(groupId);
	  }


	public Page<ActYw> fistPageByYear(Page<ActYw> page, ActYw actYw) {
		actYw.setPage(page);
		page.setList(dao.findListByYear(actYw));
		return page;
	}

	public Page<ActYw> findPage(Page<ActYw> page, ActYw actYw) {
		return super.findPage(page, actYw);
	}

	@Transactional(readOnly = false)
	public void save(ActYw actYw) {
		if ((actYw.getIsNewRecord())) {
			if (actYw.getIsDeploy() == null) {
				actYw.setIsDeploy(false);
			}

			if (actYw.getIsPreRelease() == null) {
			    actYw.setIsPreRelease(true);
			}

			if (actYw.getIsShowAxis() == null) {
				actYw.setIsShowAxis(false);
			}

			if(ActYw.needNum(actYw)){
			    actYw.setIsNrule(Const.YES);
			}else{
                actYw.setIsNrule(Const.NO);
			}

			if ((actYw.getKeyType() == null)) {
			    actYw.setKeyType(FormTheme.F_MR.getKey());
			}

	        /**
	         * 处理isCurr属性生成规则.
	         */
            actYw = updateCurr(actYw);
		}

		if((actYw.getGroup() != null) && (actYw.getGroup().getTheme() != null)){
            actYw.setKeyType(FormTheme.getById(actYw.getGroup().getTheme()).getKey());
		}
		if(!actYw.getIsDeploy()){
			actYw.setIsPreRelease(false);
		}
		super.save(actYw);
		String tenant= actYw.getTenantId();
		String key = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, tenant)+actYw.getId();
		JedisUtils.delObject(key);
		if (actYw.getIsNewRecord()){
			actYw.setIsNewRecord(false);
		}
		JedisUtils.setObject(key,actYw);

	}

	public ActYw saveNc(ActYw actYw) {
		List<ActYw> actywList=getByGroupId(actYw);
		if(StringUtil.checkNotEmpty(actywList)){
			actYw=actywList.get(0);
		}else {
			if ((actYw.getIsNewRecord())) {
				if (actYw.getIsDeploy() == null) {
					actYw.setIsDeploy(false);
				}

				if (actYw.getIsPreRelease() == null) {
					actYw.setIsPreRelease(true);
				}

				if (actYw.getIsShowAxis() == null) {
					actYw.setIsShowAxis(false);
				}

				if (ActYw.needNum(actYw)) {
					actYw.setIsNrule(Const.YES);
				} else {
					actYw.setIsNrule(Const.NO);
				}

				if ((actYw.getKeyType() == null)) {
					actYw.setKeyType(FormTheme.F_MR.getKey());
				}

				/**
				 * 处理isCurr属性生成规则.
				 */
				actYw = updateCurr(actYw);
			}

			if ((actYw.getGroup() != null) && (actYw.getGroup().getTheme() != null)) {
				actYw.setKeyType(FormTheme.getById(actYw.getGroup().getTheme()).getKey());
			}
			//运营中心模板
			actYw.setTenantId("10");
			actYw.preInsert();
			dao.insertNc(actYw);
			String tenant = actYw.getTenantId();
			String key = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, tenant) + actYw.getId();
			JedisUtils.delObject(key);
			if (actYw.getIsNewRecord()) {
				actYw.setIsNewRecord(false);
			}
			JedisUtils.setObject(key, actYw);
		}
		return actYw;
	}

	@Transactional(readOnly = false)
	public ApiStatus addGtime(ActYw actYw, HttpServletRequest request) {
		if (!(actYw.getShowTime()).equals("1")) {
			return new ApiStatus(true, "不显示时间！");
		}

		if (StringUtil.isEmpty(actYw.getGroupId())) {
			return new ApiStatus(false, "流程ID不能为空！");
		}

		String[] gNodeId = request.getParameterValues("nodeId");
		String[] beginDate = request.getParameterValues("beginDate");
		String[] endDate = request.getParameterValues("endDate");
		if ((beginDate == null) || (beginDate.length <= 0)) {
			return new ApiStatus(false, "开始时间不能为空！");
		}

		if ((endDate == null) || (endDate.length <= 0)) {
			return new ApiStatus(false, "结束时间不能为空！");
		}

		if ((gNodeId == null) || (gNodeId.length <= 0)) {
			return new ApiStatus(false, "节点ID不能为空！");
		}

		ActYwGtime actYwGtimeOld = new ActYwGtime();
		// actYwGtimeOld.setGrounpId(actYw.getGroupId());
		actYwGtimeOld.setProjectId(actYw.getRelId());
		actYwGtimeService.deleteByGroupId(actYwGtimeOld);

		ApiStatus rstatus = new ApiStatus();
		for (int i = 0; i < beginDate.length; i++) {
			String status = request.getParameter("status" + i);
			String rate = request.getParameter("rate" + i);
			String rateStatus = request.getParameter("rateStatus" + i);
            String hasTpl = request.getParameter("hasTpl" + i);
            String excelTplPath = request.getParameter("excelTplPath" + i);
            String excelTplClazz = request.getParameter("excelTplClazz" + i);

			ActYwGtime actYwGtime = new ActYwGtime();
			actYwGtime.setGrounpId(actYw.getGroupId());
			actYwGtime.setProjectId(actYw.getRelId());

			if (StringUtil.isEmpty(status)) {
				actYwGtime.setStatus(Const.HIDE);
			} else {
				actYwGtime.setStatus(status);
			}

			if (StringUtil.isEmpty(rate)) {
				actYwGtime.setRate(0.0f);
			} else {
				actYwGtime.setRate(Float.parseFloat(rate));
			}

			if (StringUtil.isEmpty(rateStatus)) {
				actYwGtime.setRateStatus(Const.HIDE);
			} else {
				actYwGtime.setRateStatus(rateStatus);
			}

            if (StringUtil.isEmpty(hasTpl)) {
                actYwGtime.setHasTpl(false);
            } else {
                actYwGtime.setHasTpl((hasTpl).equals(Const.YES)?true:false);
            }

            if (StringUtil.isNotEmpty(excelTplPath)) {
                actYwGtime.setExcelTplPath(excelTplPath);
            }
            if (StringUtil.isNotEmpty(excelTplClazz)) {
                actYwGtime.setExcelTplClazz(excelTplClazz);
            }

			if (StringUtil.isNotEmpty(beginDate[i])) {
				actYwGtime.setBeginDate(DateUtil.parseDate(beginDate[i]));
			}

			if (StringUtil.isNotEmpty(endDate[i])) {
				actYwGtime.setEndDate(DateUtil.parseDate(endDate[i]));
			}

			if (StringUtil.isNotEmpty(gNodeId[i])) {
				actYwGtime.setGnodeId(gNodeId[i]);
			}
			actYwGtimeService.save(actYwGtime);
		}
		return rstatus;
	}

	@Transactional(readOnly = false)
	public ApiStatus addGtimeNewYear(String year,ActYw actYw, HttpServletRequest request) {
		if ((actYw.getShowTime()).equals(Const.HIDE)) {
			return new ApiStatus(true, "不显示时间！");
		}

		if (StringUtil.isEmpty(actYw.getGroupId())) {
			return new ApiStatus(false, "流程ID不能为空！");
		}

		String[] gNodeId = request.getParameterValues("nodeId");
		if ((gNodeId == null) || (gNodeId.length <= 0)) {
			return new ApiStatus(false, "节点ID不能为空！");
		}
		//申报时间开始结束
		String nodeStartDate = request.getParameter("nodeStartDate");
		String nodeEndDate = request.getParameter("nodeEndDate");
		//项目时间开始结束
		String startYearDate = request.getParameter("startYearDate");
		String endYearDate = request.getParameter("endYearDate");
		ActYwYear actYwYear=new ActYwYear();
		actYwYear.setYear(year);
		if(nodeStartDate!=null){
			actYwYear.setNodeStartDate(DateUtil.parseDate(nodeStartDate));
		}
		if(nodeEndDate!=null){
			actYwYear.setNodeEndDate(DateUtil.parseDate(nodeEndDate));
		}
		if(startYearDate!=null){
			actYwYear.setStartDate(DateUtil.parseDate(startYearDate));
		}
		if(endYearDate!=null){
			actYwYear.setEndDate(DateUtil.parseDate(endYearDate));
		}
//		actYwYear.setActywId(actYw.getId());
//		boolean isOver=actYwYearService.isOverActywId(actYwYear);
//		if(isOver){
//			return new ApiStatus(false, "申报时间不能重复！");
//		}
		actYwYear.setActywId(actYw.getId());
		ActYwYear overYear=actYwYearService.isOverActYear(actYwYear);
		if(overYear!=null){
			return new ApiStatus(false, "申报时间与"+overYear.getYear()+"年重复！");
		}
		actYwYearService.save(actYwYear);

		String cacheKey = ActSval.ck.cks(ActSval.ActEmskey.ACTYWYEAR, TenantConfig.getCacheTenant())+actYwYear.getActywId();
		if (!JedisUtils.hasKey(cacheKey)){
			JedisUtils.setObject(cacheKey,actYwYear);
		}
		ApiStatus rstatus = new ApiStatus();
		for (int i = 0; i < gNodeId.length; i++) {
			String beginDate = request.getParameter("beginDate" + i);
			String endDate = request.getParameter("endDate" + i);
			String status = request.getParameter("status" + i);
			String rate = request.getParameter("rate" + i);
			String rateStatus = request.getParameter("rateStatus" + i);
            String hasTpl = request.getParameter("hasTpl" + i);
            String excelTplPath = request.getParameter("excelTplPath" + i);
            String excelTplClazz = request.getParameter("excelTplClazz" + i);

			ActYwGtime actYwGtime = new ActYwGtime();
			actYwGtime.setGrounpId(actYw.getGroupId());
			actYwGtime.setProjectId(actYw.getRelId());
			actYwGtime.setYearId(actYwYear.getId());

			if (StringUtil.isEmpty(status)) {
				actYwGtime.setStatus(Const.HIDE);
			} else {
				actYwGtime.setStatus(status);
			}

			if (StringUtil.isEmpty(rate)) {
				actYwGtime.setRate(0.0f);
			} else {
				actYwGtime.setRate(Float.parseFloat(rate));
			}

			if (StringUtil.isEmpty(rateStatus)) {
				actYwGtime.setRateStatus(Const.HIDE);
			} else {
				actYwGtime.setRateStatus(rateStatus);
			}


            if (StringUtil.isEmpty(hasTpl)) {
                actYwGtime.setHasTpl(false);
            } else {
                actYwGtime.setHasTpl((hasTpl).equals(Const.YES)?true:false);
            }

            if (StringUtil.isNotEmpty(excelTplPath)) {
                actYwGtime.setExcelTplPath(excelTplPath);
            }
            if (StringUtil.isNotEmpty(excelTplClazz)) {
                actYwGtime.setExcelTplClazz(excelTplClazz);
            }

			if (StringUtil.isNotEmpty(beginDate)) {
				actYwGtime.setBeginDate(DateUtil.parseDate(beginDate));
			}

			if (StringUtil.isNotEmpty(endDate)) {
				actYwGtime.setEndDate(DateUtil.parseDate(endDate));
			}

			if (StringUtil.isNotEmpty(gNodeId[i])) {
				actYwGtime.setGnodeId(gNodeId[i]);
			}
			actYwGtimeService.save(actYwGtime);
		}
		return rstatus;
	}

	@Transactional(readOnly = false)
	public ApiStatus addJsonGtime(String year,ActYw actYw) {
		if ((actYw.getShowTime()).equals(Const.HIDE)) {
			return new ApiStatus(true, "不显示时间！");
		}
		if (StringUtil.isEmpty(actYw.getGroupId())) {
			return new ApiStatus(false, "流程ID不能为空！");
		}
		ActYwYear actYwYear=new ActYwYear();
		if(actYw.getProProject().getNodeStartDate()!=null){
			actYwYear.setNodeStartDate(actYw.getProProject().getNodeStartDate());
		}
		if(actYw.getProProject().getNodeEndDate()!=null){
			actYwYear.setNodeEndDate(actYw.getProProject().getNodeEndDate());
		}
		if(actYw.getProProject().getStartDate()!=null){
			actYwYear.setStartDate(actYw.getProProject().getStartDate());
		}
		if(actYw.getProProject().getEndDate()!=null){
			actYwYear.setEndDate(actYw.getProProject().getEndDate());
		}
		actYwYear.setYear(year);
		actYwYear.setActywId(actYw.getId());
		ActYwYear overYear=actYwYearService.isOverActYear(actYwYear);
		if(overYear!=null){
			return new ApiStatus(false, "申报时间与"+overYear.getYear()+"年重复！");
		}
		actYwYearService.save(actYwYear);
		for(ActYwGtime actYwGtime:actYw.getActYwGtimeList()){
			actYwGtime.setGrounpId(actYw.getGroupId());
			actYwGtime.setProjectId(actYw.getRelId());
			actYwGtime.setYearId(actYwYear.getId());
			actYwGtimeService.save(actYwGtime);
		}
		return new ApiStatus();
	}

	@Transactional(readOnly = false)
	public ApiStatus addGtimeOld(String year,ActYw actYw, HttpServletRequest request) {
		if ((actYw.getShowTime()).equals(Const.HIDE)) {
			return new ApiStatus(true, "不显示时间！");
		}

		if (StringUtil.isEmpty(actYw.getGroupId())) {
			return new ApiStatus(false, "流程ID不能为空！");
		}

		String[] gNodeId = request.getParameterValues("nodeId");
		if ((gNodeId == null) || (gNodeId.length <= 0)) {
			return new ApiStatus(false, "节点ID不能为空！");
		}


		String yearId=actYwYearService.getProByActywIdAndYear(actYw.getId(),year);
		String nodeStartDate = request.getParameter("nodeStartDate");
		String nodeEndDate = request.getParameter("nodeEndDate");

		//项目时间开始结束
		String startYearDate = request.getParameter("startYearDate");
		String endYearDate = request.getParameter("endYearDate");
		//修改申报时间
		ActYwYear actYwYear=actYwYearService.get(yearId);
		if(nodeStartDate!=null){
			actYwYear.setNodeStartDate(DateUtil.parseDate(nodeStartDate));
		}
		if(nodeEndDate!=null){
			actYwYear.setNodeEndDate(DateUtil.parseDate(nodeEndDate));
		}
		if(startYearDate!=null){
			actYwYear.setStartDate(DateUtil.parseDate(startYearDate));
		}
		if(endYearDate!=null){
			actYwYear.setEndDate(DateUtil.parseDate(endYearDate));
		}

		//boolean isOver=actYwYearService.isOverActywId(actYwYear);
		ActYwYear overYear=actYwYearService.isOverActYear(actYwYear);
		if(overYear!=null){
			return new ApiStatus(false, "申报时间与"+overYear.getYear()+"年重复！");
		}
		actYwYearService.save(actYwYear);
		actYwGtimeService.deleteByYear(actYw.getRelId(),yearId);
		//修改节点时间
		ApiStatus rstatus = new ApiStatus();
		for (int i = 0; i < gNodeId.length; i++) {
			String beginDate = request.getParameter("beginDate" + i);
			String endDate = request.getParameter("endDate" + i);
			String status = request.getParameter("status" + i);
			String rate = request.getParameter("rate" + i);
			String rateStatus = request.getParameter("rateStatus" + i);
			String hasTpl = request.getParameter("hasTpl" + i);
			String excelTplPath = request.getParameter("excelTplPath" + i);
			String excelTplClazz = request.getParameter("excelTplClazz" + i);

			ActYwGtime actYwGtime = new ActYwGtime();
			actYwGtime.setGrounpId(actYw.getGroupId());
			actYwGtime.setProjectId(actYw.getRelId());
			actYwGtime.setYearId(yearId);
			if (StringUtil.isEmpty(status)) {
				actYwGtime.setStatus(Const.HIDE);
			} else {
				actYwGtime.setStatus(status);
			}

			if (StringUtil.isEmpty(rate)) {
				actYwGtime.setRate(0.0f);
			} else {
				actYwGtime.setRate(Float.parseFloat(rate));
			}

			if (StringUtil.isEmpty(rateStatus)) {
				actYwGtime.setRateStatus(Const.HIDE);
			} else {
				actYwGtime.setRateStatus(rateStatus);
			}

			if (StringUtil.isEmpty(hasTpl)) {
			    actYwGtime.setHasTpl(false);
			} else {
			    actYwGtime.setHasTpl((hasTpl).equals(Const.YES)?true:false);
			}

			if (StringUtil.isNotEmpty(excelTplPath)) {
			    actYwGtime.setExcelTplPath(excelTplPath);
			}
			if (StringUtil.isNotEmpty(excelTplClazz)) {
			    actYwGtime.setExcelTplClazz(excelTplClazz);
			}

			if (StringUtil.isNotEmpty(beginDate)) {
				actYwGtime.setBeginDate(DateUtil.parseDate(beginDate));
			}

			if (StringUtil.isNotEmpty(endDate)) {
				actYwGtime.setEndDate(DateUtil.parseDate(endDate));
			}

			if (StringUtil.isNotEmpty(gNodeId[i])) {
				actYwGtime.setGnodeId(gNodeId[i]);
			}
			actYwGtimeService.save(actYwGtime);
		}
		return rstatus;
	}

	@Transactional(readOnly = false)
	public void delete(ActYw actYw) {
		super.delete(actYw);
	}

	/**
	 * 发布项目流程. 以流程标识和项目标识生成流程模板标识和版本（防止多项目共用一个流程是出现菜单、栏目重合）
	 *
	 * @param actYw 项目流程
	 * @return Boolean
	 * @author chenhao
	 */
	@Transactional(readOnly = false)
	public Boolean deploy(ActYw actYw, RepositoryService repositoryService) {
		return deploy(actYw, repositoryService, null);
	}

	/**
	 * 发布并部署项目流程. 以流程标识和项目标识生成流程模板标识和版本（防止多项目共用一个流程是出现菜单、栏目重合）
	 *
	 * @param actYw             项目流程
	 * @param repositoryService 流程服务
	 * @param isUpdateYw        标识是否更新到业务表
	 * @return Boolean
	 * @author chenhao
	 */
	@Transactional(readOnly = false)
	public Boolean deploy(ActYw actYw, RepositoryService repositoryService, Boolean isUpdateYw) {
		try {
			if ((actYw == null) || (!actYw.getIsNewRecord()) && StringUtil.isEmpty(actYw.getId())) {
				return false;
			}

			ActYw actYwNew = get(actYw.getId());
			if (((actYwNew == null) || (StringUtil.isEmpty(actYwNew.getId())))
					&& actYw.getIsNewRecord()) {
				save(actYw);
				actYwNew = get(actYw.getId());
			}

			if (actYwNew == null) {
				return false;
			}

			if ((actYwNew.getGroup() == null) && StringUtil.isNotEmpty(actYwNew.getGroupId())) {
				actYwNew.setGroup(actYwGroupDao.get(actYwNew.getGroupId()));
			}
			actYw.setGroup(actYwNew.getGroup());
			ActYwGroup actYwGroup = actYw.getGroup();
			if (actYwGroup == null) {
				return false;
			}

			ProProject proProject = actYw.getProProject();
			if (proProject == null) {
				return false;
			}

			if (StringUtil.isEmpty(actYwGroup.getKeyss()) || StringUtil.isEmpty(proProject.getProjectMark())) {
				return false;
			}

			String modelKey = ActYw.getPkey(actYw);
			if (modelKey == null) {
				return false;
			}
			RtModel rtModel = new RtModel(FlowType.getByKey(actYwGroup.getFlowType()).getName() + IActYw.KEY_SEPTOR + actYwGroup.getName(), modelKey, actYwGroup.getRemarks(), actYwGroup.getFlowType(), null, null);
			org.activiti.engine.repository.Model modelData = ActYwTool.genModelData(rtModel, repositoryService);
			repositoryService.saveModel(modelData);

			Model repModel = repositoryService.getModel(modelData.getId());
//			try {
//              String json = JsonAliUtils.readJson("stencilset-xm3.json");
//              rtModel.setJsonXml(json);
//          } catch (Exception e) {
//              e.printStackTrace();
//          }

            List<ActYwGnode> actYwGnodes = actYwGnodeService.findListBygGroup(new ActYwGnode(actYwGroup));
			rtModel.setJsonXml(ActYwResult.dealJsonException(JSONObject.fromObject(ActYwResult.init(rtModel, actYwGroup, ActYwTool.initProps(actYwGnodes))).toString()));
			logger.info(rtModel.getJsonXml());
			repositoryService.addModelEditorSource(repModel.getId(), rtModel.getJsonXml().getBytes(RtSvl.RtModelVal.UTF_8));

			/**
			 * 如果部署服务不为空，执行部署！
			 */
			if (isUpdateYw != null) {
				ActRstatus result = actModelService.deploy(modelData.getId());
				/**
				 * 流程发布，流程ID回填到业务表.
				 */
				if (isUpdateYw!=null && isUpdateYw) {
					actYw.setFlowId(result.getId());
					actYw.setDeploymentId(result.getDeploymentId());
					save(actYw);
				}
			}
			return true;
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持编码格式", e);
			return false;
		}
	}

	//根据流程ID找已发布项目
	public List<ActYw> findActYwListByGroupId(String groupId) {
		return dao.findActYwListByGroupId(groupId);
	}

	//根据项目配置ID找已发布项目
	public List<ActYw> findActYwListByProProject(String proType,String type,String tenantId) {
		return dao.findActYwListByProProject(proType,type,tenantId);
	}

	//根据项目配置ID找未发布项目

	public List<ActYw> findActYwListByRelIdAndState(String proType,String type,String tenantId) {
		return dao.findActYwListByRelIdAndState(proType,type,tenantId);
	}

	public List<ActYw> findAllActYwListByGroupId(String groupId) {
		return dao.findAllActYwListByGroupId(groupId);
	}

	public List<ActYw> findActYwListByProType(String proType) {
		return dao.findActYwListByProType(proType);
	}

	@Transactional(readOnly = false)
	public void saveRelation(ActYw actYw, List<String> schoolList) {
		List<ActYwPscrel> list=new ArrayList<ActYwPscrel>();
		List<ActYwScstep> scList=new ArrayList<ActYwScstep>();
		for(String tenantId:schoolList){
			ActYwPscrel actYwPscrel=new ActYwPscrel();
			actYwPscrel.setId(IdGen.uuid());
			actYwPscrel.setProvinceActywId(actYw.getId());
			actYwPscrel.setSchoolTenantId(tenantId);
			actYwPscrel.setIspushed("0");
			actYwPscrel.setCreateDate(new Date());
			actYwPscrel.setUpdateDate(new Date());
			actYwPscrel.setCreateBy(UserUtils.getUser());
			list.add(actYwPscrel);
			ActYwScstep actYwScstep=new ActYwScstep();
			actYwScstep.setId(IdGen.uuid());
			actYwScstep.setProvinceActywId(actYw.getId());
			actYwScstep.setSchoolTenantId(tenantId);
			scList.add(actYwScstep);
		}
		actYwPscrelService.saveAll(list);
		actYwScstepService.saveAll(scList);

		ActYwStep actYwStep = actYwStepService.getActYwStepByGroupId(actYw.getGroupId());
		actYwStep.setStep(ActYwStep.StepEnmu.STEP7.getValue());
		actYwStepService.save(actYwStep);
	}

	public List<ActYw> getActywByModel(String proType) {
		return dao.getActywByModel(proType);
	}

	public ActYwGroup getModelActYwByProv(String actYwGroupId) {
		String modelActYwGroupId=actYwGroupRelationService.getModelActYwGroupIdByProv(actYwGroupId);
		ActYwGroup actYwGroup=actYwGroupDao.get(modelActYwGroupId);
		return actYwGroup;
	}

	public Map<String,Object> getStep(String id) {
		Map<String, Object> map=new HashedMap<>();
		ActYwScstep actYwScstep=actYwScstepService.get(id);
		map.put("actYwScstep",actYwScstep);
		//得到当前校平台的项目id
		ActYw actyw=dao.getSchoolActYwByActYwId(actYwScstep.getProvinceActywId(),TenantConfig.getCacheTenant());

		if (actyw!=null) {
			if (StringUtil.isNotEmpty(actyw.getRelId()) && StringUtil.isNotEmpty(actyw.getGroupId())) {
				ActYwGtime actYwGtimeOld = new ActYwGtime();
				actYwGtimeOld.setGrounpId(actyw.getGroupId());
				actYwGtimeOld.setProjectId(actyw.getRelId());
				List<ActYwGtime> gtimes = actYwGtimeService.findList(actYwGtimeOld);
				if(StringUtil.checkEmpty(gtimes)){
					gtimes = ActYwGnode.convertToGtimes(
							actYwGnodeService.findListByMenu(new ActYwGnode(new ActYwGroup(actyw.getGroupId())))
					);
				}
				map.put("actYwGtimeList", gtimes);
			}
		}
		//得到申报附件
		SysAttachment sysAttachment=new SysAttachment();
		sysAttachment.setUid(actYwScstep.getProvinceActywId());
		List<SysAttachment> sysList=sysAttachmentService.getFiles(sysAttachment);
		map.put("sysList",sysList);

		ActYwGroup actYwGroup=actYwGroupDao.get(actyw.getGroupId());
		map.put("actYwGroup",actYwGroup);

		List<ActYwGnode> gnodeList = new ArrayList<ActYwGnode>();
		if (actyw!=null) {
			ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actyw.getGroupId()));
			gnodeList = actYwGnodeService.findListBygMenu(actYwGnode);
			map.put("gnodeList",gnodeList);
		}
		return map;
	}

	/**********************************************************************************
     * IActYw流程接口实现方法
     **********************************************************************************/

}