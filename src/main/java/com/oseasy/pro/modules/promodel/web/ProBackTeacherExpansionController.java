package com.oseasy.pro.modules.promodel.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.service.ProBackTeacherExpansionService;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导师信息表Controller
 *
 * @author l
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/backTeacherExpansion")
public class ProBackTeacherExpansionController extends BaseController {
    @Autowired
    private CoreService coreService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private TeacherKeywordService teacherKeywordService;
    @Autowired
    private TeamUserHistoryService teamUserHistoryService;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private ProBackTeacherExpansionService proBackTeacherExpansionService;

    @ModelAttribute
    public BackTeacherExpansion get(@RequestParam(required = false) String id, Model model) {
        BackTeacherExpansion entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = backTeacherExpansionService.get(id);
        }
        if (entity == null) {
            entity = new BackTeacherExpansion();
        }
        List<Office> offices = officeService.findAll();
        model.addAttribute("offices", offices);
        return entity;
    }

    @RequiresPermissions("sys:backTeacherExpansion:view")
    @RequestMapping(value = "form")
    public String form(BackTeacherExpansion backTeacherExpansion, Model model, HttpServletRequest request) {
//        String operateType = request.getParameter("operateType");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("operateType", operateType);
//        hashMap.put("backTeacherExpansion", backTeacherExpansion);
//        if (backTeacherExpansion.getId() != null) {
//            List<Role> roleList = coreService.findListByUserId(backTeacherExpansion.getUser().getId());
//            List<String> roleIdList= Lists.newArrayList();
//            List<Role> expertRoleList = Lists.newArrayList();
//            if(StringUtil.checkNotEmpty(roleList)){
//                for(Role role:roleList){
//                    if(role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
//                            ||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
//                            ||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())){
//                        expertRoleList.add(role);
//                        roleIdList.add(role.getId());
//                    }
//                }
//                backTeacherExpansion.getUser().setRoleList(expertRoleList);
//                backTeacherExpansion.setRoleIdList(roleIdList);
//            }
//            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
//            if (tes.size() > 0) {
//                hashMap.put("tes", tes);
//            }
//        }
//        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
//        hashMap.put("allDomains", dictList);
//        if (backTeacherExpansion.getUser() != null) {
//            List<ProjectExpVo> projectExpVo = proBackTeacherExpansionService.findProjectByTeacherId(backTeacherExpansion.getUser().getId());//查询项目经历
//            List<GContestUndergo> gContest = proBackTeacherExpansionService.findGContestByTeacherId(backTeacherExpansion.getUser().getId()); //查询大赛经历
//            hashMap.put("projectExpVo", projectExpVo);
//            hashMap.put("gContestExpVo", gContest);
//            hashMap.put("cuser", backTeacherExpansion.getUser().getId());
//        } else {
//            if (StringUtil.isNotEmpty(backTeacherExpansion.getId())) {
//                return CorePages.ERROR_404.getIdxUrl();
//            }
//        }
//        model.addAttribute("teacherData", hashMap);
        User user = backTeacherExpansion.getUser();
        if(user != null){
            model.addAttribute("userAsTeacherId", user.getId());
        }
        return SysSval.path.vms(SysEmskey.SYS.k()) + "backTeacherForm";
    }


    @RequestMapping(value = "teacherView")
    public String teacherView(BackTeacherExpansion backTeacherExpansion, Model model, HttpServletRequest request) {
        String operateType = request.getParameter("operateType");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("operateType", operateType);
        hashMap.put("backTeacherExpansion", backTeacherExpansion);
        if (backTeacherExpansion.getId() != null) {
            List<Role> roleList = coreService.findListByUserId(backTeacherExpansion.getUser().getId());
            List<String> roleIdList= Lists.newArrayList();
            List<Role> expertRoleList = Lists.newArrayList();
            if(StringUtil.checkNotEmpty(roleList)){
                for(Role role:roleList){
                    if(role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
                            ||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
                            ||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())){
                        expertRoleList.add(role);
                        roleIdList.add(role.getId());
                    }
                }
                backTeacherExpansion.getUser().setRoleList(expertRoleList);
                backTeacherExpansion.setRoleIdList(roleIdList);
            }
            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
            if (tes.size() > 0) {
                hashMap.put("tes", tes);
            }
        }
        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        hashMap.put("allDomains", dictList);
        if (backTeacherExpansion.getUser() != null) {
            List<ProjectExpVo> projectExpVo = proBackTeacherExpansionService.findProjectByTeacherId(backTeacherExpansion.getUser().getId());//查询项目经历
            List<GContestUndergo> gContest = proBackTeacherExpansionService.findGContestByTeacherId(backTeacherExpansion.getUser().getId()); //查询大赛经历
            hashMap.put("projectExpVo", projectExpVo);
            hashMap.put("gContestExpVo", gContest);
            hashMap.put("cuser", backTeacherExpansion.getUser().getId());
        } else {
            if (StringUtil.isNotEmpty(backTeacherExpansion.getId())) {
                return CorePages.ERROR_404.getIdxUrl();
            }
        }
        model.addAttribute("teacherData", hashMap);
        return SysSval.path.vms(SysEmskey.SYS.k()) + "backTeacherView";
    }

    @RequiresPermissions("sys:backTeacherExpansion:edit")
    @RequestMapping(value = "save")
    public String save(BackTeacherExpansion backTeacherExpansion, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, backTeacherExpansion)) {
            return form(backTeacherExpansion, model, request);
        }

        if (StringUtil.isNotBlank(backTeacherExpansion.getId())) {
            User user = backTeacherExpansion.getUser();
            if (StringUtil.isNotEmpty(user.getId()) && teamUserHistoryService.getBuildingCountByUserId(user.getId()) > 0) {//修改时有正在进行的项目大赛
                User old = UserUtils.get(user.getId());
                if (old != null && StringUtil.isNotEmpty(old.getId())) {
                    String checkRole = SysUserUtils.checkRoleChange(user, old);
                    if (checkRole != null) {//用户类型变化了
                        addMessage(model, "保存失败，该用户有正在进行的项目或大赛，" + checkRole);
                        return form(backTeacherExpansion, model, request);
                    } else if (SysUserUtils.checkHasRole(old, RoleBizTypeEnum.DS)) {//导师类型
                        BackTeacherExpansion bte = backTeacherExpansionService.getByUserId(old.getId());
                        if (bte != null && bte.getTeachertype() != null && !bte.getTeachertype().equals(backTeacherExpansion.getTeachertype())) {//导师类型的用户导师来源发生变化
                            addMessage(model, "保存失败，该用户有正在进行的项目或大赛，不能修改导师来源");
                            return form(backTeacherExpansion, model, request);
                        }
                    }
                }
            }
            if (backTeacherExpansion.getTopShow()!=null && backTeacherExpansion.getTopShow().equals("1")) {
                BackTeacherExpansion backTeacherExpansionNew = backTeacherExpansionService.findTeacherByTopShow(backTeacherExpansion.getTeachertype());
                if (backTeacherExpansionNew != null) {
                    backTeacherExpansionNew.setTopShow("0");
                    backTeacherExpansionService.updateAll(backTeacherExpansionNew);
                }
            }
            backTeacherExpansionService.updateAll(backTeacherExpansion);
        } else {
//            User exitUser = userService.getByMobile(backTeacherExpansion.getUser());
//            if (exitUser != null) {
//                List<Dict> dictList = DictUtils.getDictList(ItDnStudent.DICT_TECHNOLOGY_FIELD);
//                model.addAttribute("allDomains", dictList);
//                model.addAttribute("loginNameMessage", "手机号已经存在!");
//                model.addAttribute("operateType", "1");
//                return SysSval.path.vms(SysEmskey.SYS.k()) + "backTeacherForm";
//            }
            if (backTeacherExpansion.getTopShow()!=null && backTeacherExpansion.getTopShow().equals("1")) {
                BackTeacherExpansion backTeacherExpansionNew = backTeacherExpansionService.findTeacherByTopShow(backTeacherExpansion.getTeachertype());
                if (backTeacherExpansionNew != null) {
                    backTeacherExpansionNew.setTopShow("0");
                    backTeacherExpansionService.updateAll(backTeacherExpansionNew);
                }
            }
            String companyId = officeService.selelctParentId(backTeacherExpansion.getUser().getOffice().getId());
            backTeacherExpansion.getUser().setCompany(new Office());
            backTeacherExpansion.getUser().getCompany().setId(companyId);//设置学校id
//            backTeacherExpansion.getUser().setArea(backTeacherExpansion.getUser().getArea());
            backTeacherExpansion.setRoleType(BackTeacherExpansion.isTeacher);
            backTeacherExpansionService.saveAll(backTeacherExpansion);
        }
        backTeacherExpansionService.updateKeyword(backTeacherExpansion);
        addMessage(redirectAttributes, "保存导师信息成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sys/backTeacherExpansion/?repage";
    }

    @RequestMapping(value = "changeExpertOrTea", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult changeExpertOrTea(@RequestBody BackTeacherExpansion backTeacherExpansion, HttpServletRequest request) {
        try {
            String userId=backTeacherExpansion.getUser().getId();
            BackTeacherExpansion oldBackTeacherExpansion=backTeacherExpansionService.getByUserId(userId);
            User user =UserUtils.get(userId);
            if(oldBackTeacherExpansion!=null){
                backTeacherExpansion.setId(oldBackTeacherExpansion.getId());
            }
            //删除了导师身份 需要做判断。是否有项目进行
            if (!(backTeacherExpansion.getRoleType().contains(BackTeacherExpansion.isTeacher))
                    && teamUserHistoryService.getBuildingCountByUserId(user.getId()) > 0) {//修改时有正在进行的项目大赛
                String msg="变换失败，该用户有正在进行的项目或大赛";
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, msg);
            }
            //删除了专家身份 需要做判断。是否有任务进行
            if (!backTeacherExpansion.getRoleType().contains(BackTeacherExpansion.isExpert)) {//修改时有正在进行的项目大赛
                int todoNum = actTaskService.recordUserId(user.getId());
                if (todoNum > 0) {
                    return ApiResult.failed(ApiConst.STATUS_FAIL,ApiConst.getErrMsg(ApiConst.STATUS_FAIL)+":该用户还有流程任务，不能变更!");
                }
            }
            backTeacherExpansion.setUser(user);
            backTeacherExpansionService.updateExpertChange(backTeacherExpansion);
            return ApiResult.success(backTeacherExpansion);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    @RequestMapping(value = "PlChangeExpertOrTea", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult PlChangeExpertOrTea(@RequestBody BackTeacherExpansion backTeacherExpansion, HttpServletRequest request) {
        try {
            String msg = proBackTeacherExpansionService.getResMsg(backTeacherExpansion);
            return ApiResult.success(backTeacherExpansion,msg);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    //批量删除 返回成功删除数，失败删除数
    @RequestMapping(value = "deleteBatchByUser", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult deleteBatchByUser(String ids,String type) {
        return proBackTeacherExpansionService.getDelResult(ids, type);
    }
}