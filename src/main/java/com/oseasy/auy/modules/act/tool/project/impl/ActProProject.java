package com.oseasy.auy.modules.act.tool.project.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.com.pcore.common.config.CoreSval;
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
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.auy.modules.cms.entity.AuyProProject;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2017/7/29 0029.
 */

public class ActProProject  implements IActProDeal {
	//@Autowired
	// CategoryService categoryService;

    CategoryService categoryService = (CategoryService) SpringContextHolder.getBean(CategoryService.class);
	//@Autowired
	//SystemService systemService;
    CoreService coreService = (CoreService) SpringContextHolder.getBean(CoreService.class);
	//@Autowired
	//ActYwGnodeService actYwGnodeService;
	ActYwGnodeService actYwGnodeService = (ActYwGnodeService) SpringContextHolder.getBean(ActYwGnodeService.class);

	@Override
	@Transactional(readOnly = false)
	public Boolean dealMenu(ActProParamVo actProParamVo) {
		ProProject proProject =actProParamVo.getProProject();
		ActYw actYw =actProParamVo.getActYw();
		if (proProject != null) {
			Menu  menu = new Menu();
			menu.setParent(coreService.getMenu(Menu.getRootId()));
			menu.setName(proProject.getProjectName());
			menu.setImgUrl(proProject.getImgUrl());
			menu.setIsShow(Const.SHOW);
			menu.setRemarks(proProject.getContent());
			menu.setHref(ActSval.ASD_INDEX + actYw.getId());
			menu.setSort(10);
			menu.setRemarks(ActMenuRemarks.XM.getName());
			coreService.saveMenu(menu);
			proProject.setMenu(menu);
			proProject.setMenuRid(menu.getId());
			actYw.setProProject(proProject);
			if (actYw.getGroupId() != null) {
				ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
				List<ActYwGnode> sourcelist = actYwGnodeService.findListBygMenu(actYwGnode);
				Menu menuForm = new Menu();
				menuForm.setParent(menu);
				menuForm.setName(menu.getName());
				menuForm.setIsShow(Const.SHOW);
				//menuForm.setHref("form/" + proProject.getProjectMark() + "/" + sourcelist.get(i).getFormId() + "?id=" + actYw.getId());
				menuForm.setSort(10);
				menuForm.setRemarks(ActMenuRemarks.XM.getName());
				coreService.saveMenu(menuForm);

				if (sourcelist.size() > 0) {
					for (int i = 0; i < sourcelist.size(); i++) {
						ActYwGnode actYwGnodeIndex = sourcelist.get(i);
						if (actYwGnodeIndex != null) {
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

							/*menuNextForm.setHref("/cms/form/" + proProject.getProjectMark() + "/" + actYwGnodeIndex.getForm().getId() +
									"?actywId=" + actYw.getId() +
									"&gnodeId=" +  sourcelist.get(i).getId()
							);*/
							menuNextForm.setRemarks(ActMenuRemarks.ACT.getName());
							coreService.saveMenu(menuNextForm);
//							ActYwGnode actYwGnodePar = new ActYwGnode();
//							actYwGnodePar.setParentIds(sourcelist.get(i).getId());
							//List<ActYwGnode> actYwGnodes = actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),sourcelist.get(i).getId());
							List<ActYwGnode> actYwGnodes = new ArrayList<ActYwGnode>();
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
									}
                                }

							}
						}
					}
				}
				//添加查询表单
				Menu menuQueryForm = new Menu();
				menuQueryForm.setParent(menuForm);
				menuQueryForm.setName("项目查询列表");
				menuQueryForm.setIsShow(Const.SHOW);
				menuQueryForm.setSort(30);
				menuQueryForm.setHref("/cms/form/queryMenuList/?actywId=" + actYw.getId());
				menuQueryForm.setRemarks(ActMenuRemarks.XM.getName());
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
//					coreService.saveRole(sAdminRole);
//				}


				//给项目流程中所有涉及的角色和学校管理员分配查询菜单
				Set<String> roleIds = actYwGnodeService.getRolesByGroup(actYw.getGroupId());//项目流程所有节点角色IDs
				if(!roleIds.contains(CoreIds.NSC_SYS_ROLE_ADMIN.getId())){
					roleIds.add(CoreIds.NSC_SYS_ROLE_ADMIN.getId());//学校管理员
				}
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
				}
			}
		}

		return true;
	}

	@Override
    public Boolean dealDelMenu(ActProParamVo actProParamVo) {
        return true;
    }

	@Override
	@Transactional(readOnly = false)
	public Boolean dealCategory(ActProParamVo actProParamVo) {
//	    AuyProProject proProject = (AuyProProject)actProParamVo.getProProject();
	    ProProject proProject = actProParamVo.getProProject();
		ActYw actYw =actProParamVo.getActYw();
		//修改栏目为可见
		Category  category = new Category();
		Category parent = categoryService.get(CmsIds.SITE_CATEGORYS_TOP_ROOT.getId());
		category.setParent(parent);
		category.setSite(parent.getSite());
//		category.setOffice(parent.getOffice());
		category.setName(proProject.getProjectName());
		category.setDescription(proProject.getContent());
//		category.setInMenu(CoreSval.SHOW);
//		category.setInList(CoreSval.SHOW);
		category.setIsSys(1);
		category.setIsAudit(Const.NO);
		category.setSort(10);
		category.setRemarks(ActMenuRemarks.XM.getName());
		categoryService.save(category);

		//默认添加申报表单
		Category categoryapp=new Category();
		categoryapp.setParent(category);
		categoryapp.setSite(category.getSite());
//		categoryapp.setOffice(category.getOffice());
		categoryapp.setName(proProject.getProjectName());
		categoryapp.setDescription(proProject.getContent());
//		categoryapp.setInMenu(CoreSval.SHOW);
//		categoryapp.setInList(CoreSval.SHOW);
		categoryapp.setIsAudit(Const.NO);
		categoryapp.setIsSys(1);
		categoryapp.setHref("/cms/form/"+proProject.getProjectMark()+"/applyForm?actywId="+actYw.getId());
		categoryapp.setSort(10);
		categoryapp.setRemarks(ActMenuRemarks.XM.getName());
		categoryService.save(categoryapp);
		//Category categoryNew=categoryService.findByName(categoryapp.getName());
//		proProject.setCategory(categoryNew);
		proProject.setCategoryRid(categoryapp.getId());
		actYw.setProProject(proProject);
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
