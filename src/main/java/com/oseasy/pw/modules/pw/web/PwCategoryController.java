package com.oseasy.pw.modules.pw.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwCategory;
import com.oseasy.pw.modules.pw.service.PwCategoryService;
import com.oseasy.pw.modules.pw.utils.SpaceUtils;
import com.oseasy.pw.modules.pw.vo.Msg;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 资源类别Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwCategory")
public class PwCategoryController extends BaseController {

    @Autowired
    private PwCategoryService pwCategoryService;

    @ModelAttribute
    public PwCategory get(@RequestParam(required = false) String id) {
        PwCategory entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwCategoryService.get(id);
        }
        if (entity == null) {
            entity = new PwCategory();
        }
        return entity;
    }

    @RequiresPermissions("pw:pwCategory:view")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwCategoryList";
    }

    @RequestMapping(value = {"listpage", ""})
    @ResponseBody
    public ApiResult listpage(PwCategory pwCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            List<PwCategory> list = pwCategoryService.findList(pwCategory);
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }



    @RequiresPermissions("pw:pwCategory:view")
    @RequestMapping(value = "form")
    public String form(PwCategory pwCategory, Model model, HttpServletRequest request) {
        if (pwCategory.getParent() != null && StringUtil.isNotBlank(pwCategory.getParent().getId())) {
            pwCategory.setParent(pwCategoryService.get(pwCategory.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtil.isBlank(pwCategory.getId())) {
                PwCategory pwCategoryChild = new PwCategory();
                pwCategoryChild.setParent(new PwCategory(pwCategory.getParent().getId()));
                List<PwCategory> list = pwCategoryService.findList(pwCategory);
                if (list.size() > 0) {
                    pwCategory.setSort(list.get(list.size() - 1).getSort());
                    if (pwCategory.getSort() != null) {
                        pwCategory.setSort(pwCategory.getSort() + 30);
                    }
                }
            }
        }
        if (pwCategory.getSort() == null) {
            pwCategory.setSort(30);
        }
        //树没有给父ID时默认给root节点
        if (pwCategory.getParent() == null || StringUtils.isBlank(pwCategory.getParent().getId())) {
            PwCategory category = pwCategoryService.get(CoreIds.NCE_SYS_TREE_ROOT.getId());//该方法没有join
            pwCategory.setParent(category);
        }
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        //查看修改时，如果是第二级节点(资产大类)，并且有子节点，不允许修改父节点
        model.addAttribute("canEditParent", true);
        if (StringUtils.isNotBlank(pwCategory.getId())) {
            if (CoreIds.NCE_SYS_TREE_ROOT.getId().equals(pwCategory.getParent().getId())) {
                PwCategory p = new PwCategory(pwCategory.getId());
                PwCategory category = new PwCategory();
                category.setParent(p);
                model.addAttribute("canEditParent", pwCategoryService.findList(category).isEmpty());
            }
        }
        model.addAttribute("pwCategory", pwCategory);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwCategoryForm";
    }

    @RequiresPermissions("pw:pwCategory:view")
    @RequestMapping(value = "details")
    public String details(PwCategory pwCategory, Model model) {
        if (pwCategory.getParent() != null && StringUtil.isNotBlank(pwCategory.getParent().getId())) {
            pwCategory.setParent(pwCategoryService.get(pwCategory.getParent().getId()));
        }
        model.addAttribute("pwCategory", pwCategory);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwCategoryDetails";
    }


    @RequiresPermissions("pw:pwCategory:edit")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult save(@RequestBody PwCategory pwCategory, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, pwCategory)) {
            return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(),ApiConst.getErrMsg(ApiConst.PARAM_ERROR.getCode()));
        }
        try {
            pwCategoryService.save(pwCategory);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "asySave", method = RequestMethod.POST)
    public String save(@RequestBody PwCategory pwCategory) {
        Msg msg;
        try {
            pwCategoryService.save(pwCategory);
            msg = new Msg(true, pwCategory.getId());
        } catch (Exception e) {
            msg = new Msg(e.getMessage());
        }
        return msg.toJson();
    }

    @RequiresPermissions("pw:pwCategory:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public ApiResult delete(PwCategory pwCategory, RedirectAttributes redirectAttributes) {
        try {
            pwCategoryService.delete(pwCategory);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,e.getMessage());
        }
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) Boolean isParent, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<PwCategory> list = pwCategoryService.findList(new PwCategory());
        for (int i = 0; i < list.size(); i++) {
            PwCategory e = list.get(i);
            if (StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                if (isParent == null) {
                    map.put("isParent", false);
                } else {
                    map.put("isParent", isParent);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }


    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "pwCategoryTree")
    public List<Map<String, Object>> pwCategoryTree(@RequestParam(required = false) String extId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<String> parentIds = new ArrayList<String>();
        parentIds.add(CoreIds.NCE_SYS_TREE_PROOT.getId());
        parentIds.add(CoreIds.NCE_SYS_TREE_ROOT.getId());
        List<PwCategory> list = pwCategoryService.findListByParentIds(parentIds);
        for (int i = 0; i < list.size(); i++) {
            PwCategory e = list.get(i);
            if (StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                map.put("isParent", false);
                map.put("prefix", e.getPwFassetsnoRule() != null ? e.getPwFassetsnoRule().getPrefix() : null);
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 根据固定资产的类别ID获取直接子类别
     *
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/childrenCategory", method = RequestMethod.GET, produces = "application/json")
    public List<PwCategory> children(@RequestParam(required = false) String categoryId) {
        return SpaceUtils.findChildrenCategorys(categoryId);
    }


}