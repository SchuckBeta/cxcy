package com.oseasy.act.modules.pro.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGtime;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.act.modules.actyw.tool.apply.IAcservice;
import com.oseasy.act.modules.pro.dao.ProProjectDao;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创建项目Service.
 * @author zhangyao
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class ProProjectService extends CrudService<ProProjectDao, ProProject> implements IAcservice{
	@Autowired
	private SystemService systemService;
	@Autowired
	ActYwGtimeService actYwGtimeService;


	public ProProject get(String id) {
		return get(new ProProject(id));
	}
	public List<String> getByProType(String key) {
		return dao.getByProType(key);
	}

	public ProProject get(ProProject proProject) {
        Menu menu = new Menu();
        menu.setTenantId(TenantConfig.getCacheTenant());
        proProject.setMenu(menu);
		return super.get(proProject);
	}

	public List<ProProject> findList(ProProject proProject) {
		return super.findList(proProject);
	}

	public Page<ProProject> findPage(Page<ProProject> page, ProProject proProject) {
		return super.findPage(page, proProject);
	}

  	@Transactional(readOnly = false)
  	public void save(ProProject proProject) {
		if (StringUtil.isEmpty(proProject.getProjectMark())) {
		  	proProject.setProjectMark(SysNodeTool.genByKeyss(SysNoType.NO_PROJECT));
		}

    try {
      proProject.setStartDate(DateUtil.getStartDate(proProject.getStartDate()));
      proProject.setEndDate(DateUtil.getEndDate(proProject.getEndDate()));
    } catch (ParseException e) {
      logger.error(e.getMessage());
    }
		super.save(proProject);
	}

	@Transactional(readOnly = false)
	public void delete(ProProject proProject) {
		super.delete(proProject);
	}
	public ProProject getProProjectByName(String name) {
		return dao.getProProjectByName(name);
	}

	@Transactional(readOnly = false)
	public void changeProProjectModel(ActYw actYw, HttpServletRequest request)  {
		ProProject proProject = actYw.getProProject();

		//根据流程生成子菜单
		if (actYw.getGroupId() != null) {


			String[] gNodeId = request.getParameterValues("nodeId");
			String[] beginDate = request.getParameterValues("beginDate");
			String[] endDate = request.getParameterValues("endDate");
			if (beginDate != null && beginDate.length > 0 && endDate != null && endDate.length > 0) {
				for (int i = 0; i < beginDate.length; i++) {
					String status = request.getParameter("status" + i);
					ActYwGtime actYwGtime = new ActYwGtime();
					actYwGtime.setGrounpId(actYw.getGroupId());
					actYwGtime.setProjectId(actYw.getRelId());
					actYwGtime.setGnodeId(gNodeId[i]);
					actYwGtime.setStatus(status);
					actYwGtime.setBeginDate(DateUtil.parseDate(beginDate[i]));
					actYwGtime.setEndDate(DateUtil.parseDate(endDate[i]));
					actYwGtimeService.save(actYwGtime);
				}
			}
	    save(proProject);
		}
	}

	//创建菜单
	@Transactional(readOnly = false)
	public void createMenu(ProProject proProject) {
		Menu menu=new Menu();
		menu.setParent(systemService.getMenu(Menu.getRootId()));
		menu.setName(proProject.getProjectName());
		menu.setIsShow(Const.SHOW);
		menu.setRemarks(proProject.getContent());
		menu.setSort(10);
		menu.setImgUrl(proProject.getImgUrl());
		systemService.saveMenu(menu);
		proProject.setMenu(menu);
	}

	public ProProject getWithId(String relId) {
		return dao.getWithId(relId);
	}
}