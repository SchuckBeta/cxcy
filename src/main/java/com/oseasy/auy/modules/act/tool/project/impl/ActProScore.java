package com.oseasy.auy.modules.act.tool.project.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.tool.apply.IAurl;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.project.ActMenuRemarks;
import com.oseasy.act.modules.actyw.tool.project.ActProParamVo;
import com.oseasy.act.modules.actyw.tool.project.IActProDeal;
import com.oseasy.act.modules.actyw.utils.ActYwUtils;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.auy.common.config.AuyIds;
import com.oseasy.auy.modules.cms.entity.AuyProProject;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.vo.CategoryType;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.authorize.enums.TenantCategory;
import com.oseasy.com.pcore.modules.authorize.enums.TenantMenu;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2017/7/29 0029.
 */
public class ActProScore implements IActProDeal{
    CoreService coreService = (CoreService) SpringContextHolder.getBean(CoreService.class);
    CategoryService categoryService = (CategoryService) SpringContextHolder.getBean(CategoryService.class);
    ActYwGnodeService actYwGnodeService = (ActYwGnodeService) SpringContextHolder.getBean(ActYwGnodeService.class);

    private void addTaskAssignMenu(Menu menuForm,ActYw actYw){
        List<ActYwGnode> list=ActYwUtils.getAssignNodes(actYw.getId());
        if(list!=null&&list.size()>0){
            Menu menuQueryForm = new Menu();
            menuQueryForm.setParent(menuForm);
            menuQueryForm.setName("专家任务指派");
            menuQueryForm.setIsShow(Const.SHOW);
            menuQueryForm.setSort(40);
            menuQueryForm.setLtype(TenantCategory.S5.getId());
            menuQueryForm.setLver(menuForm.getLver()+1);
            menuQueryForm.setHref("/cms/form/taskAssignList/?actywId=" + actYw.getId());
            menuQueryForm.setRemarks(ActMenuRemarks.SCORE.getName());
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
	public Boolean dealMenu(ActProParamVo actProParamVo) {
	    ProProject proProject =actProParamVo.getProProject();
        ActYw actYw =actProParamVo.getActYw();
        IAurl iaurl = actProParamVo.getApply().getIaurl();
        if (proProject != null) {
            Menu  menu= coreService.getMenu(AuyIds.SITE_MENU_SCORE_ROOT.getId());
            if (actYw.getGroupId() != null) {
                ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
                List<ActYwGnode> sourcelist = actYwGnodeService.findListBygMenu(actYwGnode);
                        //findListByMenu(actYwGnode);
                Menu menuForm = new Menu();
                menuForm.setParent(menu);
                menuForm.setName(proProject.getProjectName());
                menuForm.setIsShow(Const.SHOW);
                menuForm.setHref(IAurl.genWelcome(iaurl, actYw.getId()));
                //menuForm.setHref("form/" + proProject.getProjectMark() + "/" + sourcelist.get(i).getFormId() + "?id=" + actYw.getId());
                menuForm.setSort(10);
                menuForm.setRemarks(ActMenuRemarks.SCORE.getName());
                coreService.saveMenu(menuForm);

                proProject.setMenu(menuForm);
                proProject.setMenuRid(menuForm.getId());
                actYw.setProProject(proProject);
                if ((sourcelist != null) && (sourcelist.size() > 0)) {
                    for (int i = 0; i < sourcelist.size(); i++) {
                        ActYwGnode actYwGnodeIndex = sourcelist.get(i);
                        if (actYwGnodeIndex != null) {
                            //前台节点不生成菜单
                            boolean isFront=false;
                            if(actYwGnodeIndex.getType().equals(GnodeType.GT_PROCESS.getId())){
                                isFront = ActYwGform.checkFront(isFront, actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),actYwGnodeIndex.getId()));
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
                            menuNextForm.setLtype(TenantMenu.S5.getId());
                            menuNextForm.setLver(menuForm.getLver()+1);
                            menuNextForm.setHref(IAurl.genLurl(iaurl, formId, actYw.getId(), actYwGnodeIndex.getId()));
                            menuNextForm.setRemarks(ActMenuRemarks.SCORE.getName());
                            coreService.saveMenu(menuNextForm);
                            List<ActYwGnode> actYwGnodes = new ArrayList<ActYwGnode>();
                            if(GnodeType.GT_PROCESS.getId().equals(actYwGnodeIndex.getType())){
                                actYwGnodes = actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),sourcelist.get(i).getId());
                            }else{
                                actYwGnodes.add(actYwGnodeIndex);
                            }
                            //actYwGnodeService.findListBygYwGprocess(actYw.getGroupId(),sourcelist.get(i).getId());
                            for (int j = 0; j < actYwGnodes.size(); j++) {
                                //给相应角色赋权访问
                                ActYwGnode curGnode = actYwGnodes.get(j);
                                if(curGnode != null){
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
                menuQueryForm.setName(proProject.getProjectName() + "查询");
                menuQueryForm.setIsShow(Const.SHOW);
                menuQueryForm.setSort(30);
                menuQueryForm.setLtype(TenantMenu.S5.getId());
                menuQueryForm.setLver(menuForm.getLver()+1);
                menuQueryForm.setHref(IAurl.genQurl(iaurl, actYw.getId()));
                menuQueryForm.setRemarks(ActMenuRemarks.SCORE.getName());
                coreService.saveMenu(menuQueryForm);

//                Role sAdminRole = coreService.getAdminRole();
//                if (sAdminRole != null) {
//                    sAdminRole = coreService.getRole(sAdminRole.getId());
//                  List<Menu> roleMenuList = sAdminRole.getMenuList();
//                  if (!roleMenuList.contains(menuForm)) {
//                      roleMenuList.add(menuForm);
//                  }
//                  if (!roleMenuList.contains(menuQueryForm)) {
//                      roleMenuList.add(menuQueryForm);
//                  }
//                  sAdminRole.setMenuList(roleMenuList);
//                  systemService.saveRole(sAdminRole);
//              }
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
                //addTaskAssignMenu(menuForm, actYw);
            }
        }

		return true;
	}

    @Override
    public Boolean dealDelMenu(ActProParamVo actProParamVo) {
        ProProject proProject = actProParamVo.getActYw().getProProject();
        Menu menu = coreService.getMenu(proProject.getMenuRid());
        // 删除菜单
        if (menu != null) {
            coreService.deleteMenu(menu);
        }
        return true;
    }

	@Override
	public Boolean dealCategory(ActProParamVo actProParamVo) {
//	    AuyProProject proProject = (AuyProProject)actProParamVo.getProProject();
	    ProProject proProject = actProParamVo.getProProject();
        ActYw actYw =actProParamVo.getActYw();
        //修改栏目为可见
        String siteId=(String)CoreUtils.getCache(Site.SITE_ID);
        Category category =categoryService.getByIdAndType(siteId, CategoryType.CT_SCORE.getKey());
        if(category!=null){
            //默认添加申报表单
            Category categoryapp=new Category();
            categoryapp.setParent(category);
            categoryapp.setParentId(category.getId());
            categoryapp.setSite(category.getSite());
    //      categoryapp.setOffice(category.getOffice());
            categoryapp.setName(proProject.getProjectName());
            categoryapp.setDescription(proProject.getContent());
    //      categoryapp.setInMenu(CoreSval.SHOW);
    //      categoryapp.setInList(CoreSval.SHOW);
            categoryapp.setIsAudit(Const.NO);
            categoryapp.setIsShow(1);
            categoryapp.setModule(category.getModule());
            categoryapp.setLtype(TenantCategory.S5.getId());
    //      categoryapp.setHref("javascript:onProjectApply('/cms/form/"+proProject.getProjectMark()+"/applyForm','"+actYw.getId()+"');");
            categoryapp.setHref("/cms/form/"+proProject.getProjectMark()+"/applyForm?actywId="+actYw.getId());
            categoryapp.setSort(10);
            categoryapp.setRemarks(ActMenuRemarks.SCORE.getName());
            categoryService.save(categoryapp);
            //Category categoryNew=categoryService.findByName(categoryapp.getName());
//            proProject.setCategory(categoryNew);
            proProject.setCategoryRid(categoryapp.getId());
            actYw.setProProject(proProject);
        }
		return true;
	}

    @Override
    public Boolean dealDelCategory(ActProParamVo actProParamVo) {
        String siteId=(String)CoreUtils.getCache(Site.SITE_ID);
        Category category =categoryService.getByIdAndType(siteId, CategoryType.CT_SCORE.getKey());

        // 删除栏目
        if (category != null) {
            categoryService.delete(category);
        }
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
    return false;
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
