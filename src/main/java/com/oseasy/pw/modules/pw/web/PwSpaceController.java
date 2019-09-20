package com.oseasy.pw.modules.pw.web;

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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.service.PwEnterRoomService;
import com.oseasy.pw.modules.pw.service.PwSpaceService;
import com.oseasy.pw.modules.pw.vo.PwSpaceMap;
import com.oseasy.pw.modules.pw.vo.PwSpaceType;
import com.oseasy.pw.modules.pw.vo.PwSparam;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 设施Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwSpace")
public class PwSpaceController extends BaseController {

    @Autowired
    private PwSpaceService pwSpaceService;
    @Autowired
    private PwEnterRoomService pwEnterRoomService;

    @ModelAttribute
    public PwSpace get(@RequestParam(required = false) String id) {
        PwSpace entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwSpaceService.get(id);
        }
        if (entity == null) {
            entity = new PwSpace();
        }
        return entity;
    }

    @RequiresPermissions("pw:pwSpace:view")
    @RequestMapping(value = {"list", ""})
    public String list(PwSpace pwSpace, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<PwSpace> list = pwSpaceService.findList(pwSpace);
        model.addAttribute("list", list);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceList";
    }


    @RequiresPermissions("pw:pwSpace:view")
    @RequestMapping(value = "form")
    public String form(PwSpace pwSpace, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        if (pwSpace.getParent() != null && StringUtil.isNotBlank(pwSpace.getParent().getId())) {
            pwSpace.setParent(pwSpaceService.get(pwSpace.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtil.isBlank(pwSpace.getId())) {
                PwSpace pwSpaceChild = new PwSpace();
                pwSpaceChild.setParent(new PwSpace(pwSpace.getParent().getId()));
                List<PwSpace> list = pwSpaceService.findList(pwSpace);
                if (list.size() > 0) {
                    pwSpace.setSort(list.get(list.size() - 1).getSort());
                    if (pwSpace.getSort() != null) {
                        pwSpace.setSort(pwSpace.getSort() + 30);
                    }
                }
            }
            /**
             * 获取所有父的信息
             */
            PwSpace space = pwSpace;
            while (space.getParent() != null) {
                space = space.getParent();
                if (space == null || CoreIds.NCE_SYS_TREE_ROOT.getId().equals(space.getId())) {
                    break;
                }
                if (StringUtils.isBlank(space.getType()) || StringUtils.isBlank(space.getName())) {
                    space = pwSpaceService.get(space.getId());
                }
                searchToTop(model, space);
            }
        }
        if (pwSpace.getSort() == null) {
            pwSpace.setSort(30);
        }
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        if (PwSpaceType.CAMPUS.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceCampusForm";
        } else if (PwSpaceType.BASE.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceBaseForm";
        } else if (PwSpaceType.BUILDING.getValue().equals(pwSpace.getType())) {
            /**
             * 检查楼栋数是否可编辑
             * 有楼层不可编辑
             */
            if (StringUtils.isBlank(pwSpace.getId())) {
                model.addAttribute("bEmpty", "y");
            } else {
                PwSpace s = new PwSpace();
                PwSpace p = new PwSpace(pwSpace.getId());
                s.setParent(p);
                String empty = pwSpaceService.findList(s).isEmpty() ? "y" : "n";
                model.addAttribute("bEmpty", empty);
            }
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceBuildingForm";
        } else if (PwSpaceType.FLOOR.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceFloorForm";
        } else {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSchoolForm";
        }
    }


    @RequiresPermissions("pw:pwSpace:view")
    @RequestMapping(value = "details")
    public String details(PwSpace pwSpace, Model model, RedirectAttributes redirectAttributes) {
        if (pwSpace.getParent() != null && StringUtil.isNotBlank(pwSpace.getParent().getId())) {
            pwSpace.setParent(pwSpaceService.get(pwSpace.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtil.isBlank(pwSpace.getId())) {
                PwSpace pwSpaceChild = new PwSpace();
                pwSpaceChild.setParent(new PwSpace(pwSpace.getParent().getId()));
                List<PwSpace> list = pwSpaceService.findList(pwSpace);
                if (list.size() > 0) {
                    pwSpace.setSort(list.get(list.size() - 1).getSort());
                    if (pwSpace.getSort() != null) {
                        pwSpace.setSort(pwSpace.getSort() + 30);
                    }
                }
            }
            /**
             * 获取所有父的信息
             */
            PwSpace space = pwSpace;
            while (space.getParent() != null) {
                space = space.getParent();
                if (space == null || CoreIds.NCE_SYS_TREE_ROOT.getId().equals(space.getId())) {
                    break;
                }
                if (StringUtils.isBlank(space.getType()) || StringUtils.isBlank(space.getName())) {
                    space = pwSpaceService.get(space.getId());
                }
                searchToTop(model, space);
            }
        }
        if (pwSpace.getSort() == null) {
            pwSpace.setSort(30);
        }
        if (PwSpaceType.CAMPUS.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceCampusDetails";
        } else if (PwSpaceType.BASE.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceBaseDetails";
        } else if (PwSpaceType.BUILDING.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceBuildingDetails";
        } else if (PwSpaceType.FLOOR.getValue().equals(pwSpace.getType())) {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSpaceFloorDetails";
        } else {
            return PwSval.path.vms(PwEmskey.PW.k()) + "pwSchoolDetails";
        }
    }

    private void searchToTop(Model model, PwSpace pwSpace) {
        if (PwSpaceType.SCHOOL.getValue().equals(pwSpace.getType())) {
            model.addAttribute("school", CoreUtils.getOffice(CoreIds.NCE_SYS_TREE_ROOT.getId()).getName());
        } else if (PwSpaceType.CAMPUS.getValue().equals(pwSpace.getType())) {
            model.addAttribute("campus", pwSpace.getName());
        } else if (PwSpaceType.BASE.getValue().equals(pwSpace.getType())) {
            model.addAttribute("base", pwSpace.getName());
        } else if (PwSpaceType.BUILDING.getValue().equals(pwSpace.getType())) {
            model.addAttribute("building", pwSpace.getName());
        } else {
            model.addAttribute("school", pwSpace.getName());
        }
    }

    @RequiresPermissions("pw:pwSpace:edit")
    @RequestMapping(value = "save")
    public String save(PwSpace pwSpace, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            if (!beanValidator(model, pwSpace)) {
                return form(pwSpace, model, redirectAttributes,request);
            }
            pwSpaceService.save(pwSpace);
            addMessage(redirectAttributes, "保存设施成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwSpace/?repage";
    }

    @RequiresPermissions("pw:pwSpace:edit")
    @RequestMapping(value = "delete")
    public String delete(PwSpace pwSpace, RedirectAttributes redirectAttributes) {
        try {
            pwSpaceService.delete(pwSpace);
            addMessage(redirectAttributes, "删除设施成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwSpace/?repage";
    }

    /**
     * 场地树
     * @param extId
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<PwSpace> list = pwSpaceService.findList(new PwSpace());
        for (int i = 0; i < list.size(); i++) {
            PwSpace e = list.get(i);
            if (StringUtil.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                map.put("type", e.getType());
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 不带分页的场地列表（异步）
     * @param pwSpace
     * @return
     */
    @RequiresPermissions("pw:pwSpace:view")
    @ResponseBody
    @RequestMapping(value = "jsonList")
    public List<PwSpace> list(PwSpace pwSpace) {
        return pwSpaceService.findList(pwSpace);
    }


    /**
     * 根据场地id查询所有的子（直接和间接子）
     * @param id
     * @return
     */
    @RequiresPermissions("pw:pwSpace:view")
    @ResponseBody
    @RequestMapping(value = "children/{id}")
    public List<PwSpace> findChildren(@PathVariable String id) {
        return pwSpaceService.findChildren(id);
    }

    /**
     * 查询所有的场地（校区、基地、楼栋、层）并分类返回
     * @return
     */
    @RequiresPermissions("pw:pwSpace:view")
    @ResponseBody
    @RequestMapping(value = "allSpaces")
    public PwSpaceMap allSpaces() {
        return pwSpaceService.allSpaces();
    }

    @ResponseBody
    @RequestMapping(value="getPwSpaceList", method = RequestMethod.GET, produces = "application/json")
    public List<PwSpace> getPwSpaceList(){
        return pwSpaceService.findList(new PwSpace());
    }

    /**
     * 未分配房间列表.
     * @param pwSparam
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxPwSpaceRooms", method = RequestMethod.POST, produces = "application/json")
    public ApiResult ajaxPwSpaceRooms(@RequestBody PwSparam pwSparam){
        List<PwSpace> rspaces = Lists.newArrayList();
        List<PwSpace> pwSpaces = pwSpaceService.findListRooms(PwSparam.convertPwspace(pwSparam));
        for (PwSpace curps : pwSpaces) {
            /*判断房间是否符合条件，不符合条件移除.*/
            Boolean isRoom = true;
            List<PwRoom> curRooms = Lists.newArrayList();
            for (PwRoom curRoom : curps.getRooms()) {
                Boolean needAdd = true;
                //判断是否独立使用房间
                if(pwSparam.getIsAlone()){
                    //判断房间容纳人数=剩余容纳人数（未使用）
                    if((curRoom.getRemaindernum() != curRoom.getNum())){
                        isRoom = false;
                        continue;
                    }
                }
              //判断房间期望容纳人数<剩余容纳人数
                if(((pwSparam.getNum() != null) && curRoom.getRemaindernum() != null ) && (curRoom.getRemaindernum() < pwSparam.getNum())){
                    isRoom = false;
                    continue;
                }
                if(needAdd && isRoom){
                    curRooms.add(curRoom);
                }
            }
            curps.setRooms(curRooms);
            rspaces.add(curps);
        }
        return ApiResult.success(rspaces);
    }

    /**
     * 已分配房间列表.
     * @param pwSparam
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxPwSpaceErooms", method = RequestMethod.POST, produces = "application/json")
    public ApiResult ajaxPwSpaceErooms(@RequestBody PwSparam pwSparam){
        if(StringUtil.isEmpty(pwSparam.getEid())){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+": 入驻标识不能为空！");
        }
        List<PwEnterRoom> pwErooms = pwEnterRoomService.findList(PwSparam.convertPeroom(pwSparam));
        if(StringUtil.checkEmpty(pwErooms)){
            return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
        }
        return ApiResult.success(pwErooms);
    }
}