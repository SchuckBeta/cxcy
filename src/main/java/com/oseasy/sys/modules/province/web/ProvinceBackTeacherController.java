package com.oseasy.sys.modules.province.web;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProBackTeacherExpansionService;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by PW on 2019/5/5.
 */

@Controller
@RequestMapping(value = "${adminPath}/sys/backTeacherExpansion")
public class ProvinceBackTeacherController extends BaseController {
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private CoreService coreService;
    @Autowired
    private TeacherKeywordService teacherKeywordService;
    @Autowired
    private ProBackTeacherExpansionService proBackTeacherExpansionService;
    @Autowired
    private ProActTaskService proActTaskService;


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

    //跳转导师列表
    @RequestMapping(value = "toProvinceTeacherList")
    public String toProvinceTeacherList() {
        return SysSval.path.vms(SysSval.SysEmskey.SYS.k()) + "provinceTeacherList";
    }

    //导师列表
    @RequestMapping(value = "provinceTeacherList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult provinceTeacherList(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            /*HashMap<String, Object> hashMap = new HashMap<>();
            if (backTeacherExpansion == null) {
                backTeacherExpansion = new BackTeacherExpansion();
            }
            backTeacherExpansion.setTenantId(TenantConfig.getCacheTenant());
            Page<BackTeacherExpansion> page = backTeacherExpansionService.findPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);
            if (backTeacherExpansion != null && backTeacherExpansion.getUser() != null
                    && backTeacherExpansion.getUser().getOffice() != null
                    && StringUtil.isNotBlank(backTeacherExpansion.getUser().getOffice().getId())) {
                hashMap.put("xueyuanId", backTeacherExpansion.getUser().getOffice().getId());
            }
            JedisUtils.storage("", "");
            hashMap.put("page", page);*/
            Page<BackTeacherExpansion> page =  proBackTeacherExpansionService.findExpertPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);

            return ApiResult.success(page);
            //return ApiResult.success(hashMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }
    }


    //查看导师详情
    @RequestMapping(value = "provinceTeacherView")
    public String provinceTeacherView(BackTeacherExpansion backTeacherExpansion, Model model, HttpServletRequest request) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        String operateType = request.getParameter("operateType");
//        hashMap.put("operateType", operateType);
//        BackTeacherExpansion olbackTeacherExpansion=null;
//        if(backTeacherExpansion.getUser()!=null){
//            olbackTeacherExpansion=backTeacherExpansionService.getByUserId(backTeacherExpansion.getUser().getId());
//        }
//        //添加专家角色
//        if(olbackTeacherExpansion!=null && olbackTeacherExpansion.getUser()!=null){
//            getBactExpertRoleList(olbackTeacherExpansion);
//            hashMap.put("backTeacherExpansion", olbackTeacherExpansion);
//        }else {
//            getBactExpertRoleList(backTeacherExpansion);
//            User user= UserUtils.get(backTeacherExpansion.getUser().getId());
//            backTeacherExpansion.setUser(user);
//            hashMap.put("backTeacherExpansion", backTeacherExpansion);
//        }
//        if (backTeacherExpansion.getId() != null) {
//            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
//            if (tes.size() > 0) {
//                hashMap.put("tes", tes);
//            }
//        }
//        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
//        hashMap.put("allDomains", dictList);
//        model.addAttribute("teacherData", hashMap);
        User user = backTeacherExpansion.getUser();
        if(user != null){
            model.addAttribute("userAsTeacherId", user.getId());
        }
        return SysSval.path.vms(SysSval.SysEmskey.SYS.k()) + "provinceTeacherView";
    }
    public void getBactExpertRoleList(BackTeacherExpansion backTeacherExpansion) {
        List<Role> roleList = coreService.findListByUserId(backTeacherExpansion.getUser().getId());
        List<String> roleIdList= Lists.newArrayList();
        if(StringUtil.checkNotEmpty(roleList)){
            for(Role role:roleList){
                if(role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
                        ||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
                        ||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())){
                    roleIdList.add(role.getId());
                }
            }
            backTeacherExpansion.getUser().setRoleList(roleList);
            backTeacherExpansion.setRoleIdList(roleIdList);
        }
    }
    //跳转编辑页面
    @RequestMapping(value = "provinceTeacherForm")
    public String provinceTeacherForm(BackTeacherExpansion backTeacherExpansion, Model model, HttpServletRequest request) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        String operateType = request.getParameter("operateType");
//        hashMap.put("operateType", operateType);
//        BackTeacherExpansion olbackTeacherExpansion=null;
//        if(backTeacherExpansion.getUser()!=null){
//            olbackTeacherExpansion=backTeacherExpansionService.getByUserId(backTeacherExpansion.getUser().getId());
//        }else{
//            backTeacherExpansion.setUser(new User());
//        }
//        //添加专家角色
//        if(olbackTeacherExpansion!=null && olbackTeacherExpansion.getUser()!=null){
//            getBactExpertRoleList(olbackTeacherExpansion);
//            hashMap.put("backTeacherExpansion", olbackTeacherExpansion);
//        }else {
//            getBactExpertRoleList(backTeacherExpansion);
//            User user= UserUtils.get(backTeacherExpansion.getUser().getId());
//            backTeacherExpansion.setUser(user);
//            hashMap.put("backTeacherExpansion", backTeacherExpansion);
//        }
//        if (backTeacherExpansion.getId() != null) {
//            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
//            if (tes.size() > 0) {
//                hashMap.put("tes", tes);
//            }
//        }
//        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
//        hashMap.put("allDomains", dictList);
//        model.addAttribute("teacherData", hashMap);
        User user = backTeacherExpansion.getUser();
        if(user != null){
            model.addAttribute("userAsTeacherId", user.getId());
        }
        return SysSval.path.vms(SysSval.SysEmskey.SYS.k()) + "provinceTeacherForm";
    }






}
