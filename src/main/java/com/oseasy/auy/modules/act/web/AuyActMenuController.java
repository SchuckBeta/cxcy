/**
 *
 */
package com.oseasy.auy.modules.act.web;

import java.util.List;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.auy.modules.menu.service.AuySysSystemService;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.utils.ProjectUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 菜单Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class AuyActMenuController extends BaseController {
    @Autowired
    private CoreService coreService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private AuySysSystemService proSysSystemService;

    @RequiresPermissions("user")
    @RequestMapping(value = "treePlusByLtype")
    public String treePlus(Integer lver, Integer ltype, Model model,String href) {
        Menu m = new Menu();
        m.setHref(href);
        m.setLver(lver);
        m.setLtype(ltype);
        Menu entity = CoreUtils.getRootByHref(m);
        if(entity != null){
            return treePlus(model, entity.getId(), CoreSval.getAdminPath() + href);
        }
        model.addAttribute("msg","无权限访问该页面");
        return CorePages.ERROR_MSG.getIdxUrl();
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "treePlus")
    public String treePlus(Model model, String parentId, String href) {
//      if (!authorizeService.checkMenu(parentId)) {
//          return "redirect:/a/authorize";
//      }
        List<Menu> list = Lists.newArrayList();
        List<Menu> sourcelist = CoreUtils.getAllMenuList();
        Menu.sortList(list, sourcelist, parentId, true);
        //没有下级菜单
        if (StringUtil.checkEmpty(list)) {
            model.addAttribute("msg","无权限访问该页面");
            return CorePages.ERROR_MSG.getIdxUrl();
        }
        Menu firstMenu = systemService.getMenu(parentId);
        List<Menu> secondMenus=Lists.newArrayList();
        List<Menu> threeMenus=Lists.newArrayList();

        for (Menu menu:list) {
            if (menu.getParent().getId().equals(parentId)) {
                if (StringUtil.equals("1",menu.getIsShow())) {
                    secondMenus.add(menu);
                }
            }else{
                threeMenus.add(menu);
            }
        }
        for (Menu menu2:secondMenus) {
            List<Menu> children=Lists.newArrayList();
            for (Menu menu3:threeMenus) {
                if (menu3.getParent().getId().equals(menu2.getId())) {
                    if (StringUtil.equals("1",menu3.getIsShow())) {
                        children.add(menu3);
                    }

                }
            }
            menu2.setChildren(children);
        }
        for (Menu menu : secondMenus) {
            menu.setTodoCount(proSysSystemService.menuTodoCount(menu, actTaskService));
            menu.setTodoCount(ProjectUtils.assessCount(menu));
        }
        // 自定义流程菜单待办任务数
        for (Menu menu : threeMenus) {
//          String hreff = menu.getHref();
//          if (StringUtils.isNotBlank(hreff) && hreff.contains("?actywId=") && hreff.contains("&gnodeId=")) {
//              int index1 = hreff.indexOf("?actywId=");
//              int index2 = hreff.indexOf("&gnodeId=");
//              String actywId = hreff.substring(index1 + 9, index2);
//              String gnodeId = hreff.substring(index2 + 9);
//              int count = actTaskService.todoCount(actywId, gnodeId);
//              menu.setTodoCount(count);
//          }

            menu.setTodoCount(proSysSystemService.menuTodoCount(menu, actTaskService));
            menu.setTodoCount(ProjectUtils.assessCount(menu));
        }

        if (StringUtil.isNotEmpty(firstMenu.getHref()) && StringUtil.isNotEmpty((firstMenu.getHref()).replaceAll(" ", ""))) {
          model.addAttribute("hasHome", true);
        }else{
          model.addAttribute("hasHome", false);
        }
        model.addAttribute("firstMenu", firstMenu);
        model.addAttribute("secondMenus",secondMenus);

        model.addAttribute("href",href); //addBy张正，根据href跳转。如果href不为空，跳转到指定href
        //return CorePages.IDX_BACK_V4.getIdxUrl();
        return ICorePn.curPt().framePage(Sval.EmPt.TM_ADMIN);
    }

    @RequestMapping(value="getMenuTodoNum/{menuId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getMenuTodoNum(@PathVariable String menuId){
      try {
          List<Menu> menuList = Lists.newArrayList();
          List<Menu> list = Lists.newArrayList();
          List<Menu> sourcelist = CoreUtils.getAllMenuList();
          Menu.sortList(list, sourcelist, menuId, true);
          List<Menu> secondMenus=Lists.newArrayList();
          List<Menu> threeMenus=Lists.newArrayList();

          for (Menu menu:list) {
              if (menu.getParent().getId().equals(menuId)) {
                  if (StringUtil.equals("1",menu.getIsShow())) {
                      secondMenus.add(menu);
                  }
              }else{
                  threeMenus.add(menu);
              }
          }
          for (Menu menu : secondMenus) {
              menu.setTodoCount(proSysSystemService.menuTodoCount(menu, actTaskService));
              menu.setTodoCount(ProjectUtils.assessCount(menu));
              menuList.add(menu);
          }
          // 自定义流程菜单待办任务数
          for (Menu menu : threeMenus) {
              menu.setTodoCount(proSysSystemService.menuTodoCount(menu, actTaskService));
              menu.setTodoCount(ProjectUtils.assessCount(menu));
              menuList.add(menu);
          }
          return ApiResult.success(menuList);
      }catch (Exception e){
          logger.error(e.getMessage());
          return ApiResult.failed(ApiConst.CODE_GATELIST_ERROR, ApiConst.getErrMsg(ApiConst.CODE_GATELIST_ERROR)+":"+e.getMessage());
      }
    }

    /**
     * 保存所有节点与流程状态
     */
    @ResponseBody
    @RequestMapping(value = "ajaxMenuCount")
    public ApiTstatus<Long> ajaxMenuCount(@RequestParam(required=false) String id) {
        if(StringUtil.isEmpty(id)){
            return new ApiTstatus<Long>(false, "菜单不能为空！");
        }
        List<Role> roles = UserUtils.getUser().getRoleList();
        if(StringUtil.checkEmpty(roles)){
            return new ApiTstatus<Long>(false, "菜单不能为空！");
        }

        List<Menu> sourcelist = coreService.findRoleMenuByParentIdsLike(roles, id);
        long total = 0;
        for (Menu m : sourcelist) {
            total += proSysSystemService.menuTodoCount(m, actTaskService);
        }

        if(total != 0){
            return new ApiTstatus<Long>(true, "代办任务有" + total + "条！", total);
        }
        return new ApiTstatus<Long>(false, "代办任务有0条！");
    }
}
