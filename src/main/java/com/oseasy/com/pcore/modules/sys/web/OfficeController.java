/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.AreaService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.OfficeTree;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 机构Controller
 *
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;
    @ModelAttribute("office")
    public Office get(@RequestParam(required = false) String id) {
        if (StringUtil.isNotBlank(id)) {
            return officeService.get(id);
        } else {
            return new Office();
        }
    }

    @RequestMapping(value = "findProfessionals")
    @ResponseBody
    public List<Office> findProfessionals(String parentId) {
        return officeService.findProfessionals(parentId);
    }


    @RequiresPermissions("sys:office:view")
    @RequestMapping(value = {""})
    public String index(Office office, Model model) {
        model.addAttribute("filter", officeService.findAll());
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "officeIndex";
    }

    @RequiresPermissions("sys:office:view")
    @RequestMapping(value = {"list"})
    public String list(Office office, Model model) {
//        if (StringUtil.isEmpty(office.getParentIds())) {
//            office.setParentIds(SysIds.NCE_SYS_TREE_PROOT.getId() + StringUtil.DOTH);
//        }
//        model.addAttribute("list", officeService.findList(office));
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "officeList";
    }

    @RequestMapping(value="getOfficeList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getOfficeList(){
        try {
            return ApiResult.success(CoreUtils.getOfficeList());
        }catch (Exception e){
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    @RequiresPermissions("sys:office:view")
    @RequestMapping(value = "form")
    public String form(Office office, Model model) {
        User user = UserUtils.getUser();
        if (office.getParent() == null || office.getParent().getId() == null) {
            office.setParent(user.getOffice());
        }
        office.setParent(officeService.get(office.getParent().getId()));

        // 自动获取排序号
        if (StringUtil.isBlank(office.getId()) && office.getParent() != null) {
            int size = 0;
            List<Office> list = officeService.findAll();
            for (int i = 0; i < list.size(); i++) {
                Office e = list.get(i);
                if (e.getParent() != null && e.getParent().getId() != null
                        && e.getParent().getId().equals(office.getParent().getId())) {
                    size++;
                }
            }
            office.setCode((office.getParent().getCode() == null ? "" : office.getParent().getCode()) + StringUtil.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
        }
        if (office.getArea() == null) {
            office.setArea(user.getOffice().getArea());
        }else {
            Area area = areaService.get(office.getCityCode());
            office.setArea(area);
        }
        model.addAttribute("office", office);
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "officeForm";
    }

    @RequestMapping(value = "checkOfficeName", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Boolean checkOfficeName(Office office) {
        if (StringUtil.isNotEmpty(office.getId())) {
            return !officeService.checkByNameAndId(office.getId(), office.getParentId(), office.getName());
        } else {
            return !officeService.checkByName(office.getParentId(), office.getName());
        }
    }


    @RequiresPermissions("sys:office:edit")
    @RequestMapping(value = "save")
    public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
        if (CoreSval.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return CoreSval.REDIRECT + adminPath + "/sys/office/";
        }
        if (!beanValidator(model, office)) {
            return form(office, model);
        }
        if (!StringUtil.isNotEmptys(office.getParentId(), office.getName())) {
            addMessage(redirectAttributes, "父机构和机构名称不能为空！");
            model.addAttribute("office", office);
            return CoreSval.REDIRECT + adminPath + "/sys/office/form?parent.id=" + office.getParentId();
        }

		if(StringUtil.isNotEmpty(office.getId())){
			if (officeService.checkByNameAndId(office.getId(),office.getParentId(), office.getName())) {
				addMessage(redirectAttributes, "机构名称已存在，不能重复提交！");
				model.addAttribute("office", office);
				return CoreSval.REDIRECT + adminPath + "/sys/office/form?parent.id=" + office.getParentId();
			}
		}else{
			if (officeService.checkByName(office.getParentId(), office.getName())) {
				addMessage(redirectAttributes, "机构名称已存在，不能重复提交！");
				model.addAttribute("office", office);
				return CoreSval.REDIRECT + adminPath + "/sys/office/form?parent.id=" + office.getParentId();
			}
		}
        if (office.getParent() != null && StringUtil.isNotBlank(office.getParent().getId())) {
            Office parentOffice = officeService.get(office.getParent().getId());
            if (parentOffice != null) {
                String parentGrade = parentOffice.getGrade();
                if (StringUtil.isNotBlank(parentGrade)) {
                    office.setGrade(String.valueOf(Integer.valueOf(parentGrade) + 1));
                    office.setType((Integer.valueOf(office.getGrade()) + 2) + "");  //机构类型
                }
            }
        }
        officeService.saveById(office);
        //officeService.save(office);

        if (office.getChildDeptList() != null) {
            Office childOffice = null;
            for (String id : office.getChildDeptList()) {
                childOffice = new Office();
                childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
                childOffice.setParent(office);
                childOffice.setArea(office.getArea());
                childOffice.setType("2");
                childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1));
                childOffice.setUseable(Const.YES);
                officeService.save(childOffice);
            }
        }

        addMessage(redirectAttributes, "保存机构" + office.getName() + "成功");
        return CoreSval.REDIRECT + adminPath + "/sys/office/";
    }

    @RequiresPermissions("sys:office:edit")
    @RequestMapping(value = "delete")
    public String delete(Office office, RedirectAttributes redirectAttributes) {
        if (CoreSval.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return CoreSval.REDIRECT + adminPath + "/sys/office/list";
        }
        officeService.delete(office);
        addMessage(redirectAttributes, "删除机构成功");
        return CoreSval.REDIRECT + adminPath + "/sys/office/list?id=" + office.getParentId() + "&parentIds=" + office.getParentIds();
    }

    @RequestMapping(value = "delOffice", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult delOffice(@RequestBody Office office) {
        try {
            if (CoreSval.isDemoMode()) {
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":演示模式，不允许操作");
            }
            boolean isHas=officeService.checkIsHasUser(office.getId());
            if (isHas) {
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, "该机构有人员存在");
            }
            officeService.delete(office);

            return ApiResult.success();
        } catch (Exception e) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "treeDataForUserSel")
    public List<Map<String, Object>> treeDataForUserSel(@RequestParam(required = false) String extId, @RequestParam(required = false) String type,
                                                        @RequestParam(required = false) Long grade, @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Office> list = officeService.findListFront(isAll);
        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            if ((StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
                    && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
                    ) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                map.put(Office.GRADE, e.getGrade());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 获取机构JSON数据。
     *
     * @param extId    排除的ID
     * @param type     类型（1：公司；2：部门/小组/其它：3：用户）
     * @param grade    显示级别
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String type,
                                              @RequestParam(required = false) Long grade, @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Office> list = new ArrayList<Office>();
        if (type != null) {
            if (type.equals("2")) {
                list = officeService.findColleges();
            } else {
                list = officeService.findList(isAll);
            }
        } else {
            list = officeService.findList(isAll);
        }

        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            if ((StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
                    && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 获取团建发布机构JSON数据。
     *
     * @param extId    排除的ID
     * @param type     类型（1：公司；2：部门/小组/其它：3：用户）
     * @param grade    显示级别
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "relTreeData")
    public List<Map<String, Object>> relTreeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String type,
                                                 @RequestParam(required = false) Long grade, @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Office> list = officeService.findList(isAll);
        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            if ((StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
                    && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
                    && Const.YES.equals(e.getUseable())) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                mapList.add(map);
            }
        }

        JSONObject json = JSONObject.fromObject(mapList);

        logger.info("json=" + json);
        return mapList;
    }


    @RequestMapping(value="getOrganizationList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Office> getOrganizationList(){
        return CoreUtils.getOfficeList();
    }

    @RequestMapping(value="getOfficeTree", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Office> getOfficeTree(){
        return OfficeTree.buildTree(CoreUtils.getOfficeList());
    }


}
