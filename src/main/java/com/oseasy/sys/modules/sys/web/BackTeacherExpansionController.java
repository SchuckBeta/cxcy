package com.oseasy.sys.modules.sys.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导师信息表Controller
 *
 * @author l
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/backTeacherExpansion")
public class BackTeacherExpansionController extends BaseController {

    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;

    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private TeamUserHistoryService teamUserHistoryService;

    @Autowired
    private TeacherKeywordService teacherKeywordService;
    @Autowired
    private CoreService coreService;

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
    @RequestMapping(value = {"list", ""})
    public String list(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response, Model model) {
//        if (backTeacherExpansion == null) {
//            backTeacherExpansion = new BackTeacherExpansion();
//        }
//        backTeacherExpansion.setTenantId((String) TenantConfig.getCacheTenant());
//        Page<BackTeacherExpansion> page = backTeacherExpansionService.findPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);
//        if (backTeacherExpansion != null && backTeacherExpansion.getUser() != null
//                && backTeacherExpansion.getUser().getOffice() != null
//                && StringUtil.isNotBlank(backTeacherExpansion.getUser().getOffice().getId())) {
//            model.addAttribute("xueyuanId", backTeacherExpansion.getUser().getOffice().getId());
//        }
//
//        model.addAttribute("page", page);



        String tenantId = TenantConfig.getCacheTenant();
        if(tenantId.equals(CoreIds.NPR_SYS_TENANT.getId())){
            return SysSval.path.vms(SysSval.SysEmskey.SYS.k()) + "provinceTeacherList";
        }
        return SysSval.path.vms(SysEmskey.SYS.k()) + "backTeacherExpansionList";

    }


    @RequestMapping(value = "getTeacherList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getTeacherList(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (backTeacherExpansion == null) {
            backTeacherExpansion = new BackTeacherExpansion();
        }
        backTeacherExpansion.setTenantId(TenantConfig.getCacheTenant());
        backTeacherExpansion.setRoleType(CoreSval.Rtype.TEACHER.getKey());
        Page<BackTeacherExpansion> page = backTeacherExpansionService.findPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);
        if (backTeacherExpansion != null && backTeacherExpansion.getUser() != null
                && backTeacherExpansion.getUser().getOffice() != null
                && StringUtil.isNotBlank(backTeacherExpansion.getUser().getOffice().getId())) {
            hashMap.put("xueyuanId", backTeacherExpansion.getUser().getOffice().getId());
        }
        JedisUtils.storage("","");
        hashMap.put("page", page);
        return ApiResult.success(hashMap);
    }

    @RequestMapping(value = "expertForm")
    public String expertForm(BackTeacherExpansion backTeacherExpansion, Model model, HttpServletRequest request) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        String operateType = request.getParameter("operateType");
//        hashMap.put("operateType", operateType);
//        BackTeacherExpansion olbackTeacherExpansion=null;
//        if(backTeacherExpansion.getUser()!=null){
//            olbackTeacherExpansion=backTeacherExpansionService.getByUserId(backTeacherExpansion.getUser().getId());
//        }
//        //添加专家角色
//
//        if(olbackTeacherExpansion!=null && olbackTeacherExpansion.getUser()!=null){
//            getBactExpertRoleList(olbackTeacherExpansion);
//            hashMap.put("backTeacherExpansion", olbackTeacherExpansion);
//        }else {
//            if(backTeacherExpansion!=null && backTeacherExpansion.getUser()!=null){
//                getBactExpertRoleList(backTeacherExpansion);
//                User user=UserUtils.get(backTeacherExpansion.getUser().getId());
//                backTeacherExpansion.setUser(user);
//            }
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
            model.addAttribute("userAsExpertId", user.getId());
        }
        return SysSval.path.vms(SysEmskey.SYS.k()) + "backExpertForm";
    }

    public void getBactExpertRoleList(BackTeacherExpansion backTeacherExpansion) {
        List<Role> roleList = coreService.findListByUserId(backTeacherExpansion.getUser().getId());
        List<String> roleIdList= Lists.newArrayList();
        if(StringUtil.checkNotEmpty(roleList)){
			for(Role role:roleList){
				if(role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
						||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
						||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())
                        ||role.getRtype().equals(CoreSval.Rtype.EXPORT.getKey())){
					roleIdList.add(role.getId());
				}
			}
            backTeacherExpansion.getUser().setRoleList(roleList);
            backTeacherExpansion.setRoleIdList(roleIdList);
		}
    }

    @RequestMapping(value = "expertView")
    public String expertView(BackTeacherExpansion backTeacherExpansion, Model model, HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String operateType = request.getParameter("operateType");
        hashMap.put("operateType", operateType);
        BackTeacherExpansion olbackTeacherExpansion=null;
        if(backTeacherExpansion.getUser()!=null){
            olbackTeacherExpansion=backTeacherExpansionService.getByUserId(backTeacherExpansion.getUser().getId());
        }
        //添加专家角色
        if(olbackTeacherExpansion!=null && olbackTeacherExpansion.getUser()!=null){
            getBactExpertRoleList(olbackTeacherExpansion);
            hashMap.put("backTeacherExpansion", olbackTeacherExpansion);
        }else {
            getBactExpertRoleList(backTeacherExpansion);
            User user=UserUtils.get(backTeacherExpansion.getUser().getId());
            backTeacherExpansion.setUser(user);
            hashMap.put("backTeacherExpansion", backTeacherExpansion);
        }
        if (olbackTeacherExpansion.getId() != null) {
            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(olbackTeacherExpansion.getId());
            if (tes.size() > 0) {
                hashMap.put("tes", tes);
            }
        }
        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        hashMap.put("allDomains", dictList);
        model.addAttribute("teacherData", hashMap);
        return SysSval.path.vms(SysEmskey.SYS.k()) + "backExpertView";
    }

    //修改头像信息
   	@ResponseBody
   	@RequestMapping(value = "/ajaxUpdatePhoto", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
   	public ApiTstatus<User> ajaxUpdatePhoto(@RequestParam(required = false) String userId, @RequestParam(required = true) String photo) {
   		if(StringUtil.isEmpty(userId)){
   			userId = CoreUtils.getUser().getId();
   		}
   		if(StringUtil.isEmpty(photo)){
   			userId = UserUtils.getUser().getId();
   		}
   		User user = userService.findUserById(userId);

   		//替换头像地址
   		if(StringUtil.isNotEmpty(photo)) {
            String newUrl = null;
            newUrl = VsftpUtils.moveFile(photo);
            if (StringUtil.isNotEmpty(newUrl)) {
                user.setPhoto(newUrl);
            }
        }
   		//user.setPhoto(photo);
   		userService.updateUser(user);

   		if (user!=null) {
   			CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + user.getId());
   			CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
   		}
   		return new ApiTstatus<User>(true, "修改头像成功", user);
   	}

    @RequestMapping(value = "getExpertRoleList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getExpertRoleList(HttpServletRequest request) {
        try {
            Map<String,Object> map= Maps.newHashMap();
            map.put(SysIds.EXPERT_COLLEGE_EXPERT.getId(),SysIds.EXPERT_COLLEGE_EXPERT.getRemark());
            map.put(SysIds.EXPERT_SCHOOL_EXPERT.getId(),SysIds.EXPERT_SCHOOL_EXPERT.getRemark());
            map.put(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId(),SysIds.EXPERT_OUTSCHOOL_EXPERT.getRemark());
            return ApiResult.success(map);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    @RequestMapping(value = "saveExpertOrTea", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveExpert(@RequestBody BackTeacherExpansion backTeacherExpansion, HttpServletRequest request) {
        try {
            String msg="";
            if (StringUtil.isNotBlank(backTeacherExpansion.getId())) {
                User user = backTeacherExpansion.getUser();
                if (StringUtil.isNotEmpty(user.getId()) && teamUserHistoryService.getBuildingCountByUserId(user.getId()) > 0) {//修改时有正在进行的项目大赛
                    User old = UserUtils.get(user.getId());
                    if (old != null && StringUtil.isNotEmpty(old.getId())) {
                        String checkRole = SysUserUtils.checkRoleChange(user, old);
                        if (checkRole != null) {//用户类型变化了
                             msg="保存失败，该用户有正在进行的项目或大赛，" + checkRole;
                            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, msg);
                        } else if (SysUserUtils.checkHasRole(old, RoleBizTypeEnum.DS)) {//导师类型
                            BackTeacherExpansion bte = backTeacherExpansionService.getByUserId(old.getId());
                            if (bte != null && bte.getTeachertype() != null && !bte.getTeachertype().equals(backTeacherExpansion.getTeachertype())) {//导师类型的用户导师来源发生变化
                                 msg="保存失败，该用户有正在进行的项目或大赛，不能修改导师来源";
                                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, msg);
                            }
                        }
                    }
                }

                //首页置顶
                if (backTeacherExpansion.getTopShow() != null && backTeacherExpansion.getTopShow().equals("1")) {
                    BackTeacherExpansion backTeacherExpansionNew = backTeacherExpansionService.findTeacherByTopShow(backTeacherExpansion.getTeachertype());
                    if (backTeacherExpansionNew != null) {
                        backTeacherExpansionNew.setTopShow("0");
                        backTeacherExpansionService.updateAll(backTeacherExpansionNew);

                    }
                }

                if(backTeacherExpansion.getRoleType().equals(BackTeacherExpansion.isTeacher)){  //保存导师
                    backTeacherExpansionService.updateAll(backTeacherExpansion);
                    msg= "保存导师成功";
                }else{//保存专家
                    backTeacherExpansionService.updateExpertAll(backTeacherExpansion);
                    msg= "保存专家成功";
                }
            } else {
                //判断置顶
                if (backTeacherExpansion.getTopShow() != null && backTeacherExpansion.getTopShow().equals("1")) {
                    BackTeacherExpansion backTeacherExpansionNew = backTeacherExpansionService.findTeacherByTopShow(backTeacherExpansion.getTeachertype());
                    if (backTeacherExpansionNew != null) {
                        backTeacherExpansionNew.setTopShow("0");
                        backTeacherExpansionService.updateAll(backTeacherExpansionNew);
                    }
                }
                if(backTeacherExpansion.getUser().getOffice()!=null){
                    String companyId = officeService.selelctParentId(backTeacherExpansion.getUser().getOffice().getId());
                    backTeacherExpansion.getUser().setCompany(new Office());
                    backTeacherExpansion.getUser().getCompany().setId(companyId);//设置学校id
                }
                if(backTeacherExpansion.getRoleType().equals(BackTeacherExpansion.isTeacher)){//保存导师
                    if(StringUtil.isNotEmpty(backTeacherExpansion.getUser().getId())){
                        backTeacherExpansionService.updateAll(backTeacherExpansion);
                        msg= "保存导师成功";
                    }else {
                        backTeacherExpansionService.saveAll(backTeacherExpansion);
                        msg = "添加用户成功！初始密码设置为：123456";
                    }
                }else{//保存专家
                    if(StringUtil.isNotEmpty(backTeacherExpansion.getUser().getId())){
                        backTeacherExpansionService.updateExpertAll(backTeacherExpansion);
                        msg= "保存专家成功";
                    }else {
                        backTeacherExpansionService.saveExpertAll(backTeacherExpansion);
                        msg= "添加用户成功！初始密码设置为：123456";
                    }

                }
            }
            if(StringUtil.checkNotEmpty(backTeacherExpansion.getKeywords())){
                backTeacherExpansionService.updateKeyword(backTeacherExpansion);
            }
            return ApiResult.success(backTeacherExpansion,msg);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }


    @RequestMapping(value = "sendMsgToExpert", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult sendMsgToExpert(String ids, HttpServletRequest request) {
        try {
            List<String> userList= Arrays.asList(ids.split(","));
            oaNotifyService.sendOaNotifyByTypeAndUser(UserUtils.getUser(), userList, "专家通知", "您有项目需要去审核",
                   OaNotify.Type_Enum.TYPE31.getValue(), "");
            return ApiResult.success();
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    /**
     * 检查loginName 登录名不能与其他人的登录名相同，不能与其他人的no相同
     * @return
     */
    @RequestMapping(value = "checkLoginName", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Boolean checkLoginName(@RequestBody User user) {
        return userService.getByLoginNameOrNo(user.getLoginName(), user.getId())== null;
    }

    @RequiresPermissions("sys:backTeacherExpansion:edit")
    @RequestMapping(value = "delete")
    public String delete(BackTeacherExpansion backTeacherExpansion, RedirectAttributes redirectAttributes) {
        TeamUserRelation teamUserRelation = new TeamUserRelation();
        teamUserRelation.setUser(backTeacherExpansion.getUser());
        teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
        if (teamUserRelation != null) {
            addMessage(redirectAttributes, "该导师已加入团队，不能删除!");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sys/backTeacherExpansion/?repage";
        }
        backTeacherExpansionService.delete(backTeacherExpansion);
        User user = UserUtils.get(backTeacherExpansion.getUser().getId());
        userService.delete(user);
        addMessage(redirectAttributes, "删除导师信息成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sys/backTeacherExpansion/?repage";
    }

    @RequestMapping(value = "deleteTeacher", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult deleteTeacher(BackTeacherExpansion backTeacherExpansion) {
        TeamUserRelation teamUserRelation = new TeamUserRelation();
        teamUserRelation.setUser(backTeacherExpansion.getUser());
        teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
        if (teamUserRelation != null) {
          return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"该导师已加入团队，不能删除!");
        }
        backTeacherExpansionService.delete(backTeacherExpansion);
        User user = UserUtils.get(backTeacherExpansion.getUser().getId());
        userService.delete(user);
        return ApiResult.success(user);
    }

    //批量删除 返回成功删除数，失败删除数
    @RequestMapping(value = "deleteBatch", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult deleteBatch(String ids) {
        String[] idStr = ids.split(",");
        int successCount = 0;
        int failCount = 0;
        HashMap<String, List<String>> hashMap= new HashMap<>();
        List<String> failIds = new ArrayList<>();
        List<String> successIds = new ArrayList<>();
        for (int i = 0; i < idStr.length; i++) {
            BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.get(idStr[i]);
            TeamUserRelation teamUserRelation = new TeamUserRelation();
            teamUserRelation.setUser(backTeacherExpansion.getUser());
            teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
            if (teamUserRelation != null) {
                failCount++;
                failIds.add(idStr[i]);
            } else {
                successCount++;
                backTeacherExpansionService.delete(backTeacherExpansion);
                User user = UserUtils.get(backTeacherExpansion.getUser().getId());
                userService.delete(user);
                successIds.add(idStr[i]);
            }
        }
        hashMap.put("failIds", failIds);
        hashMap.put("successIds", successIds);
        ApiResult.success(hashMap);
        String message;
        if (failCount == 0) {
            message = "成功删除" + successCount + "个导师。";
        } else {
            message = "成功删除" + successCount + "个导师。" + failCount + "个导师已加入团队，不能删除!";
        }
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_SUCCESS);
        result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
        result.setData(hashMap);
        result.setMsg(message);
        return result;
    }

    @RequestMapping(value="getTeaExDataBySId/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getTeaExDataBySId(@PathVariable String id){
        try {
            if(StringUtil.isEmpty(id)){
                logger.error("导师扩展表ID不存在");
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":导师扩展表ID不存在");
            }
            BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.get(id);
            if(backTeacherExpansion == null){
                logger.error("该导师不存在");
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":导师不存在");
            }
            List<TeacherKeyword> keywords = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
            List<String> stringList = Lists.newArrayList();
            for(TeacherKeyword keyword: keywords){
                stringList.add(keyword.getKeyword());
            }
            backTeacherExpansion.setKeywords(stringList);
            return ApiResult.success(backTeacherExpansion);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequestMapping(value="getStuExDataByUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getStuExDataByUserId(@PathVariable String userId){
        try {
            if(StringUtil.isEmpty(userId)){
                logger.error("用户ID不存在");
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":用户ID不存在");
            }
            BackTeacherExpansion backTeacherExpansion = SysUserUtils.getTeacherByUserId(userId);
            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
            if (StringUtil.checkNotEmpty(tes)) {
                List<String> keywords = Lists.newArrayList();
                for(TeacherKeyword teacherKeyword : tes){
                    keywords.add(teacherKeyword.getKeyword());
                }
                backTeacherExpansion.setKeywords(keywords);
            }
            if(backTeacherExpansion == null){
                backTeacherExpansion = new BackTeacherExpansion();
                User user=UserUtils.get(userId);
                backTeacherExpansion.setUser(user);
                //logger.error("导师不存在");
                //return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":导师不存在");
            }
            return ApiResult.success(backTeacherExpansion);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}