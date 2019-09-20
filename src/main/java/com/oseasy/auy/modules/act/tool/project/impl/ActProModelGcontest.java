package com.oseasy.auy.modules.act.tool.project.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.oseasy.act.common.config.ActTypes;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.auy.common.config.AuyIds;
import com.oseasy.auy.modules.act.service.AuyActYwService;
import com.oseasy.cms.modules.cms.service.SiteService;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.project.ActMenuRemarks;
import com.oseasy.act.modules.actyw.tool.project.ActProParamVo;
import com.oseasy.act.modules.actyw.tool.project.IActProDeal;
import com.oseasy.act.modules.actyw.utils.ActYwUtils;
import com.oseasy.auy.modules.cms.entity.AuyProProject;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2017/7/29 0029.
 */

public class ActProModelGcontest implements IActProDeal {
	//@Autowired
	// CategoryService categoryService;
	CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
	CoreService coreService = SpringContextHolder.getBean(CoreService.class);
	//@Autowired
	//ActYwGnodeService actYwGnodeService;
	ActYwGnodeService actYwGnodeService = SpringContextHolder.getBean(ActYwGnodeService.class);
	SiteService siteService = SpringContextHolder.getBean(SiteService.class);
	AuyActYwService auyActYwService= SpringContextHolder.getBean(AuyActYwService.class);
	@Override
	@Transactional(readOnly = false)
	public Boolean dealMenu(ActProParamVo actProParamVo) {
//	    AuyProProject proProject = (AuyProProject)actProParamVo.getProProject();
	    ProProject proProject = actProParamVo.getProProject();
		ActYw actYw = actProParamVo.getActYw();
		List<Menu> topmenus = Lists.newArrayList();
		if (proProject != null) {
//			Menu  menu= coreService.getMenu(proProject.getMenuRid());
			Menu  menu= auyActYwService.getMenuRid(actYw);
			topmenus = coreService.gentopMenus(topmenus, menu);
//			Menu  menu= coreService.getMenu(AuyIds.SITE_MENU_NPGCONTEST_ROOT.getId());

			if ((actYw.getGroupId() != null) && (menu != null)) {
				ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
				List<ActYwGnode> sourcelist = actYwGnodeService.findListBygMenu(actYwGnode);
				Menu menuForm = new Menu();
				menuForm.setParent(menu);
				menuForm.setName(proProject.getProjectName());
				menuForm.setIsShow(Const.SHOW);
				menuForm.setHref(ActSval.ASD_INDEX + actYw.getId());
				//menuForm.setHref("form/" + proProject.getProjectMark() + "/" + sourcelist.get(i).getFormId() + "?id=" + actYw.getId());
				menuForm.setSort(10);
				menuForm.setRemarks(ActMenuRemarks.DASAI.getName());
				coreService.saveMenu(menuForm);

				proProject.setMenu(menuForm);
				proProject.setMenuRid(menuForm.getId());
				actYw.setProProject(proProject);
				if ((sourcelist != null) && (sourcelist.size() > 0)) {
					for (int i = 0; i < sourcelist.size(); i++) {
						ActYwGnode actYwGnodeIndex = sourcelist.get(i);
						if ((actYwGnodeIndex != null) && StringUtil.checkNotEmpty(actYwGnodeIndex.getGforms())) {
							//前台节点不生成菜单
							boolean isFront=false;
							if(actYwGnodeIndex.getType().equals(GnodeType.GT_PROCESS.getId())){
//                                isFront = ActYwGform.checkFront(isFront, actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),actYwGnodeIndex.getId()));
                            }else{
                                isFront = ActYwGform.checkFront(isFront, actYwGnodeIndex);
                            }
							if(isFront){
								continue;
							}

                            String formId = ActYwGform.checkAdminList(actYwGnodeIndex);
							Menu menuNextForm = new Menu();
							menuNextForm.setParent(menuForm);
							menuNextForm.setName(actYwGnodeIndex.getName());
							menuNextForm.setIsShow(Const.SHOW);
							menuNextForm.setSort(10 + i);
							menuNextForm.setHref("/cms/form/" + proProject.getProjectMark() + "/" + formId +
									"?actywId=" + actYw.getId() +
									"&gnodeId=" + actYwGnodeIndex.getId()
							);
							menuNextForm.setRemarks(ActMenuRemarks.DASAI.getName());
							coreService.saveMenu(menuNextForm);
							List<ActYwGnode> actYwGnodes = new ArrayList<ActYwGnode>();
//							List<ActYwGnode> actYwGnodes = actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),sourcelist.get(i).getId());
							if(GnodeType.GT_PROCESS.getId().equals(actYwGnodeIndex.getType())){
								actYwGnodes = actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),sourcelist.get(i).getId());
							}else{
								actYwGnodes.add(actYwGnodeIndex);
							}

							for (int j = 0; j < actYwGnodes.size(); j++) {
								//给相应角色赋权访问
							    ActYwGnode curGnode = actYwGnodes.get(j);
                                if(curGnode != null){
									if(curGnode.getIsDelegate()){
										//如果当前节点为委派节点 专家可以看到当前节点
										Role role = coreService.getRole(coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey()));
										if (role != null) {
											List<Menu> roleMenuList = role.getMenuList();
											if (!roleMenuList.contains(menuForm)) {
												roleMenuList.add(menuForm);
											}
											if (!roleMenuList.contains(menuNextForm)) {
												roleMenuList.add(menuNextForm);
											}
											role.setMenuList(roleMenuList);
											coreService.saveRole(role);
												coreService.insertRoleMenu(role, topmenus);
										}
									}
                                    for (Role curRole : curGnode.getRoles()) {
                                        if ((curRole != null) && StringUtil.isNotEmpty(curRole.getId())) {
                                            Role role = coreService.getRole(curRole.getId());
                                            if (role != null) {
        										List<Menu> roleMenuList = role.getMenuList();
        										if (!roleMenuList.contains(menuForm)) {
        											roleMenuList.add(menuForm);
        										}
        										if (!roleMenuList.contains(menuNextForm)) {
        											roleMenuList.add(menuNextForm);
        										}
        										role.setMenuList(roleMenuList);
        										coreService.saveRole(role);
												coreService.insertRoleMenu(role, topmenus);
        									}
                                        }
                                    }
									Role sAdminRole = coreService.getAdminRole();
									if (sAdminRole != null) {
										sAdminRole = coreService.getRole(sAdminRole.getId());
										List<Menu> roleMenuList = sAdminRole.getMenuList();
										if (!roleMenuList.contains(menuForm)) {
											roleMenuList.add(menuForm);
										}
										if (!roleMenuList.contains(menuNextForm)) {
											roleMenuList.add(menuNextForm);
										}
										sAdminRole.setMenuList(roleMenuList);
										coreService.saveRole(sAdminRole);
										coreService.insertRoleMenu(sAdminRole, topmenus);
									}
								}
							}
						}
					}
				}
				//添加查询表单
				Menu menuQueryForm = new Menu();
				menuQueryForm.setParent(menuForm);
				menuQueryForm.setName("大赛查询列表");
				menuQueryForm.setIsShow(Const.SHOW);
				menuQueryForm.setSort(30);
				menuQueryForm.setHref("/cms/form/queryMenuList/?actywId=" + actYw.getId());
				menuQueryForm.setRemarks(ActMenuRemarks.DASAI.getName());
				coreService.saveMenu(menuQueryForm);
//				Role sAdminRole = coreService.getAdminRole();
//				if (sAdminRole != null) {
//					sAdminRole = coreService.getRole(sAdminRole.getId());
//					List<Menu> roleMenuList = sAdminRole.getMenuList();
//					if (!roleMenuList.contains(menuForm)) {
//						roleMenuList.add(menuForm);
//					}
//					if (!roleMenuList.contains(menuQueryForm)) {
//						roleMenuList.add(menuQueryForm);
//					}
//					sAdminRole.setMenuList(roleMenuList);
//					systemService.saveRole(sAdminRole);
//				}
				Set<String> roleIds = actYwGnodeService.getRolesByGroup(actYw.getGroupId());//项目流程所有节点角色IDs
//				if(!roleIds.contains(CoreIds.NSC_SYS_ROLE_ADMIN.getId())){
//					roleIds.add(CoreIds.NSC_SYS_ROLE_ADMIN.getId());//学校管理员
//				}

				//添加学校管理员id
				coreService.addAdminUser(roleIds);
				for (String roleId : roleIds) {
					Role role = coreService.getRole(roleId);
					if (role == null) {
						continue;
					}
					List<Menu> menuList = role.getMenuList();
					if(!menuList.contains(menuForm)){
						menuList.add(menuForm);
					}
					if(!menuList.contains(menuQueryForm)){
						menuList.add(menuQueryForm);
					}
					role.setMenuList(menuList);
					coreService.saveRole(role);
					coreService.insertRoleMenu(role, topmenus);
				}
				//addTaskAssignMenu(menuForm, actYw);
			}
		}
		return true;
	}

	@Override
    public Boolean dealDelMenu(ActProParamVo actProParamVo) {
        return true;
    }

	private void addTaskAssignMenu(Menu menuForm,ActYw actYw){
		List<ActYwGnode> list=ActYwUtils.getAssignNodes(actYw.getId());
		if(list!=null&&list.size()>0){
			Menu menuQueryForm = new Menu();
			menuQueryForm.setParent(menuForm);
			menuQueryForm.setName("专家任务指派");
			menuQueryForm.setIsShow(Const.SHOW);
			menuQueryForm.setSort(40);
			menuQueryForm.setHref("/cms/form/taskAssignList/?actywId=" + actYw.getId());
			menuQueryForm.setRemarks(ActMenuRemarks.DASAI.getName());
			coreService.saveMenu(menuQueryForm);
			//给学校管理员分配菜单
			Role role = coreService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
			if (role == null) {
				return;
			}
			List<Menu> menuList = role.getMenuList();
			if(!menuList.contains(menuForm)){
				menuList.add(menuForm);
			}
			if(!menuList.contains(menuQueryForm)){
				menuList.add(menuQueryForm);
			}
			role.setMenuList(menuList);
			coreService.saveRole(role);
		}
	}
	@Override
	@Transactional(readOnly = false)
	public Boolean dealCategory(ActProParamVo actProParamVo) {
	    ProProject proProject = actProParamVo.getProProject();
		ActYw actYw =actProParamVo.getActYw();
		//修改栏目为可见
		//Category category = categoryService.get(CmsIds.SITE_CATEGORYS_GCONTEST_ROOT.getId());
		//String siteId=(String)CoreUtils.getCache(Site.SITE_ID);
		Site site = siteService.getAutoSite();
		if((site == null) || StringUtil.isEmpty(site.getId())){
			return false;
		}

		//Category category =categoryService.getByIdAndType(site.getId(),"0000000284");
		String type = null;
		String tenantId = TenantConfig.getCacheTenant();
		String flowType = actYw.getGroup().getFlowType();
		if (tenantId.equals(CoreIds.NPR_SYS_TENANT.getId())){
			if (flowType.equals(ActTypes.DASAI)){
				type = ActTypes.SITE_MENU_NPGCONTEST_ROOT.getId();
			}else{
				type = ActTypes.SITE_MENU_NPPROJECT_ROOT.getId();
			}
		}else{
			if (flowType.equals(ActTypes.DASAI)){
				type = ActTypes.SITE_MENU_GCONTEST_ROOT.getId();
			}else{
				type = ActTypes.SITE_MENU_PROJECT_ROOT.getId();
			}
		}
		Category category =categoryService.getByIdAndType(site.getId(), type);
		if(category!=null) {
			//默认添加申报表单
			Category categoryapp = new Category();
			categoryapp.setParent(category);
			categoryapp.setParentId(category.getId());
			categoryapp.setSite(site);
			categoryapp.setName(proProject.getProjectName());
			categoryapp.setName(proProject.getProjectName());
			categoryapp.setDescription(proProject.getContent());
			categoryapp.setIsShow(1);
			categoryapp.setIsAudit(Const.NO);
			categoryapp.setIsSys(1);
			categoryapp.setHref("/cms/form/" + proProject.getProjectMark() + "/applyForm?actywId=" + actYw.getId());
			categoryapp.setSort(10);
			categoryapp.setRemarks(ActMenuRemarks.DASAI.getName());
			if (category.getLtype() != null){
				categoryapp.setLtype(category.getLtype());
			}
			categoryService.save(categoryapp);
//			Category categoryNew=categoryService.findByName(categoryapp.getName());
			//Category categoryNew=categoryService.findByName(categoryapp.getName());
//			proProject.setCategory(categoryNew);
//			proProject.setCategoryRid(categoryNew.getId());
			proProject.setCategoryRid(categoryapp.getId());
			actYw.setProProject(proProject);
		}
		return true;
	}

    @Override
    public Boolean dealDelCategory(ActProParamVo actProParamVo) {
        return true;
    }

  @Override
  public Boolean dealTime(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean dealIcon(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean dealActYw(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean dealDeploy(ActProParamVo actProParamVo) {
    return true;
  }

  @Override
  public Boolean requireMenu() {
    return true;
  }

  @Override
  public Boolean requireCategory() {
    return true;
  }

  @Override
  public Boolean requireTime() {
    return true;
  }

  @Override
  public Boolean requireIcon() {
    return true;
  }

  @Override
  public Boolean requireActYw() {
    return true;
  }

  @Override
  public Boolean requireDeploy() {
    return true;
  }

}
