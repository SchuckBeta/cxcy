package com.oseasy.pro.modules.promodel.web.front;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectAuditTaskVo;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProBackTeacherExpansionService;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 导师信息表Controller
 * @author chenhao
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontTeacherExpansion")
public class FrontProTeacherExpansionController extends BaseController {
    @Autowired
    private SysViewsService sysViewsService;
    @Autowired
    private TeacherKeywordService teacherKeywordService;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private ProBackTeacherExpansionService proBackTeacherExpansionService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private UserService userService;
    @Autowired
    ProjectDeclareService projectDeclareService;
    @Autowired
    private TeamUserHistoryService teamUserHistoryService;

    @ModelAttribute
    public BackTeacherExpansion get(@RequestParam(required=false) String id, Model model) {
        BackTeacherExpansion entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = backTeacherExpansionService.get(id);
        }
        if (entity == null) {
            entity=backTeacherExpansionService.getByUserId(id);
        }
        if(entity == null){
            entity = new BackTeacherExpansion();
        }

        List<Office> offices = officeService.findList(true);
        model.addAttribute("offices", offices);
        return entity;
    }

    @RequestMapping(value = "view")
    public String view(BackTeacherExpansion backTeacherExpansion, Model model,HttpServletRequest request) {

        model.addAttribute("cuser", backTeacherExpansion.getUser().getId());
        if (StringUtil.isEmpty(backTeacherExpansion.getUser().getViews())) {
            backTeacherExpansion.getUser().setViews("0");
        }
        if (StringUtil.isEmpty(backTeacherExpansion.getUser().getLikes())) {
            backTeacherExpansion.getUser().setLikes("0");
        }
        List <String> tes=teacherKeywordService.getStringKeywordByTeacherid(backTeacherExpansion.getId());
        if (tes.size()>0) {
            backTeacherExpansion.setKeywords(tes);
        }

        String teaId = backTeacherExpansion.getUser().getId();
        String userId = UserUtils.getUser().getId();
        String mobile=backTeacherExpansion.getUser().getMobile();
        String email=backTeacherExpansion.getUser().getEmail();
        if (!teamService.findTeamByUserId(userId,teaId) && mobile!=null) {
            mobile=mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        if(!teamService.findTeamByUserId(userId,teaId) && email!=null){
            email=email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4") ;
        }
        model.addAttribute("mobile", mobile);
        model.addAttribute("email", email);
        /*记录浏览量*/
        User user= UserUtils.getUser();
        if (user!=null&&StringUtil.isNotEmpty(user.getId())&&!user.getId().equals(backTeacherExpansion.getUser().getId())) {
            SysViewsService.updateViews(backTeacherExpansion.getUser().getId(), request,CacheUtils.USER_VIEWS_QUEUE);
        }
        /*记录浏览量*/
        /*查询谁看过它*/
        model.addAttribute("visitors", sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
        String isNewJSP=request.getParameter("isNewJSP");
        if(StringUtil.isNotEmpty(isNewJSP) && Const.YES.equals(isNewJSP)){
            return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontTeacherExpansionViewNew";
        }else{
            return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontTeacherExpansionView";
        }
    }

    @RequestMapping(value = "ajaxAuditTask")
    @ResponseBody
    public ApiResult ajaxAuditTask(BackTeacherExpansion backTeacherExpansion){
        try{
            List<ProjectAuditTaskVo> projectAuditTaskVoList = proActTaskService.getExpertAuditProAndYear(backTeacherExpansion.getUser().getId());
            if(StringUtil.checkNotEmpty(projectAuditTaskVoList)){
                //根据年份倒序排序
                Collections.sort(projectAuditTaskVoList, new Comparator<ProjectAuditTaskVo>(){
                    public int compare(ProjectAuditTaskVo o1, ProjectAuditTaskVo o2) {
                        return Integer.valueOf(o2.getYear())-Integer.valueOf(o1.getYear());
                    }});
            }
            return ApiResult.success(projectAuditTaskVoList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }

    }

    @RequestMapping(value = "ajaxGetViewAndLike")
    @ResponseBody
    public JSONObject ajaxGetViewAndLike(String id) {
        JSONObject js= new JSONObject();
        BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.get(id);
        if(null == backTeacherExpansion){
            backTeacherExpansion = new BackTeacherExpansion();
        }
        if(null == backTeacherExpansion.getUser()){
            User user = new User();
            backTeacherExpansion.setUser(user);
        }
        if(null == backTeacherExpansion || null == backTeacherExpansion.getUser()){

            backTeacherExpansion.getUser().setViews("0");
            backTeacherExpansion.getUser().setLikes("0");
        }else{
            if(StringUtil.isEmpty(backTeacherExpansion.getUser().getViews())){
                backTeacherExpansion.getUser().setViews("0");
            }else if(StringUtil.isEmpty(backTeacherExpansion.getUser().getLikes())){
                backTeacherExpansion.getUser().setLikes("0");
            }
        }
        /*if (StringUtil.isEmpty(backTeacherExpansion.getUser().getViews())) {

            backTeacherExpansion.getUser().setViews("0");
        }
        if (StringUtil.isEmpty(backTeacherExpansion.getUser().getLikes())) {
            backTeacherExpansion.getUser().setLikes("0");
        }*/
        js.put("likes",backTeacherExpansion.getUser().getLikes());
        js.put("views",backTeacherExpansion.getUser().getViews());
        List<Map<String,String>> visitors=sysViewsService.getVisitors(backTeacherExpansion.getUser().getId());
        DateFormat dateFormat = new SimpleDateFormat(DateUtil.FMT_YYYYMMDD_ZG);

        for (Map<String, String> visitor : visitors) {
            visitor.put("create_date", dateFormat.format(visitor.get("create_date")));
        }
        js.put("visitors",visitors);
        return js;
    }

    @RequestMapping(value = "form")
    public String form(BackTeacherExpansion backTeacherExpansion,String custRedict,String okurl,String backurl,Model model,HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if(backTeacherExpansion == null){
            return CorePages.ERROR_404.getIdxUrl();
        }
        if((backTeacherExpansion.getUser() == null) || StringUtil.isEmpty(backTeacherExpansion.getUser().getId())){
            backTeacherExpansion.setUser(UserUtils.getUser());
        }
        if (!UserUtils.getUser().getId().equals(backTeacherExpansion.getUser().getId())) {
            return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/view?id="+backTeacherExpansion.getId();
        }
        if (StringUtil.isEmpty(custRedict)) {
            custRedict=(String)model.asMap().get("custRedict");
            okurl=(String)model.asMap().get("okurl");
            backurl=(String)model.asMap().get("backurl");
        }
        if ("1".equals(custRedict)) {
            if (StringUtil.isEmpty(okurl)) {
                String reqreferer=request.getHeader("referer");
                if (reqreferer.contains("/infoPerfect")) {
                    okurl="/f";
                }else{
                    okurl=reqreferer;
                }
                backurl=reqreferer;
            }

        }
        hashMap.put("custRedict", custRedict);
        hashMap.put("okurl", okurl);
        hashMap.put("backurl", backurl);
        model.addAttribute("custRedict",custRedict);
        model.addAttribute("okurl",okurl);
        model.addAttribute("backurl",backurl);
        if (backTeacherExpansion.getId() != null && backTeacherExpansion.getUser() != null) {
            backTeacherExpansion = backTeacherExpansionService.getByUserId(backTeacherExpansion.getUser().getId());
            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
            if (tes.size() > 0) {
                model.addAttribute("tes",tes);
                hashMap.put("tes", tes);
            }
        }
        List<BackTeacherExpansion> teacherAwardInfo = backTeacherExpansionService.findTeacherAward(backTeacherExpansion.getUser().getId());
        if (teacherAwardInfo!=null) {
             model.addAttribute("teacherAwardInfo", teacherAwardInfo);
        }
        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
                model.addAttribute("allDomains", dictList);
        hashMap.put("allDomains", dictList);
        /*查询谁看过它*/
        model.addAttribute("visitors", sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
        hashMap.put("visitors",  sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
        /*查询我看过谁*/
        model.addAttribute("browse", sysViewsService.getBrowse(backTeacherExpansion.getUser().getId()));
        hashMap.put("browse",   sysViewsService.getBrowse(backTeacherExpansion.getUser().getId()));
        //导师参评信息
        List<ProjectExpVo> projectExpVo = proBackTeacherExpansionService.findProjectByTeacherId(backTeacherExpansion.getUser().getId());//查询项目经历
        List<GContestUndergo> gContest = proBackTeacherExpansionService.findGContestByTeacherId(backTeacherExpansion.getUser().getId()); //查询大赛经历
        model.addAttribute("projectExpVo", projectExpVo);
        model.addAttribute("gContestExpVo", gContest);
        model.addAttribute("cuser", backTeacherExpansion.getUser().getId());


        hashMap.put("projectExpVo", projectExpVo);
        hashMap.put("gContestExpVo", gContest);
        hashMap.put("cuser", dictList);
        hashMap.put("backTeacherExpansion", backTeacherExpansion);
        model.addAttribute("teacherData", hashMap);
        return SysSval.path.vms(SysEmskey.SYS.k()) + "front/editTeacher";
    }

    @RequestMapping(value = "teacherForm")
    public String teacherForm(String custRedict,String okurl,String backurl,Model model,HttpServletRequest request) {
        String userId=request.getParameter("userId");
        BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.getByUserId(userId);
        if(backTeacherExpansion == null){
            return CorePages.ERROR_404.getIdxUrl();
        }
        if((backTeacherExpansion.getUser() == null) || StringUtil.isEmpty(backTeacherExpansion.getUser().getId())){
            backTeacherExpansion.setUser(UserUtils.getUser());
        }
        if (!UserUtils.getUser().getId().equals(backTeacherExpansion.getUser().getId())) {
            return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/view?id="+backTeacherExpansion.getId();
        }
        if (StringUtil.isEmpty(custRedict)) {
            custRedict=(String)model.asMap().get("custRedict");
            okurl=(String)model.asMap().get("okurl");
            backurl=(String)model.asMap().get("backurl");
        }
        if ("1".equals(custRedict)) {
            if (StringUtil.isEmpty(okurl)) {
                String reqreferer=request.getHeader("referer");
                if (reqreferer.contains("/infoPerfect")) {
                    okurl="/f";
                }else{
                    okurl=reqreferer;
                }
                backurl=reqreferer;
            }

        }
        model.addAttribute("custRedict",custRedict);
        model.addAttribute("okurl",okurl);
        model.addAttribute("backurl",backurl);
        List<BackTeacherExpansion> teacherAwardInfo = backTeacherExpansionService.findTeacherAward(backTeacherExpansion.getUser().getId());
        if (teacherAwardInfo!=null) {
             model.addAttribute("teacherAwardInfo", teacherAwardInfo);
        }
        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
                model.addAttribute("allDomains", dictList);
        /*查询谁看过它*/
        model.addAttribute("visitors", sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
        /*查询我看过谁*/
        model.addAttribute("browse", sysViewsService.getBrowse(backTeacherExpansion.getUser().getId()));
        //导师参评信息
        List<ProjectExpVo> projectExpVo = proBackTeacherExpansionService.findProjectByTeacherId(backTeacherExpansion.getUser().getId());//查询项目经历
        List<GContestUndergo> gContest = proBackTeacherExpansionService.findGContestByTeacherId(backTeacherExpansion.getUser().getId()); //查询大赛经历
        model.addAttribute("projectExpVo", projectExpVo);
        model.addAttribute("gContestExpVo", gContest);
        model.addAttribute("cuser", backTeacherExpansion.getUser().getId());
        model.addAttribute("backTeacherExpansion", backTeacherExpansion);
        return SysSval.path.vms(SysEmskey.SYS.k()) + "front/editTeacher";
    }


    @RequestMapping(value = "findUserInfoById")
    public String  findUserInfoById(Model model,HttpServletRequest request,String custRedict,String okurl,String backurl){
        String userId=request.getParameter("userId");
        BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.getByUserId(userId);
        HashMap<String, Object> hashMap = new HashMap<>();
        if(backTeacherExpansion == null){
            return CorePages.ERROR_404.getIdxUrl();
        }
        if((backTeacherExpansion.getUser() == null) || StringUtil.isEmpty(backTeacherExpansion.getUser().getId())){
            backTeacherExpansion.setUser(UserUtils.getUser());
        }
        if (!UserUtils.getUser().getId().equals(backTeacherExpansion.getUser().getId())) {
            return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/view?id="+backTeacherExpansion.getId();
        }
        if (StringUtil.isEmpty(custRedict)) {
            custRedict=(String)model.asMap().get("custRedict");
            okurl=(String)model.asMap().get("okurl");
            backurl=(String)model.asMap().get("backurl");
        }
        if ("1".equals(custRedict)) {
            if (StringUtil.isEmpty(okurl)) {
                String reqreferer=request.getHeader("referer");
                if (reqreferer.contains("/infoPerfect")) {
                    okurl="/f";
                }else{
                    okurl=reqreferer;
                }
                backurl=reqreferer;
            }

        }
        hashMap.put("custRedict", custRedict);
        hashMap.put("okurl", okurl);
        hashMap.put("backurl", backurl);
        model.addAttribute("custRedict",custRedict);
        model.addAttribute("okurl",okurl);
        model.addAttribute("backurl",backurl);
        if (backTeacherExpansion.getId() != null && backTeacherExpansion.getUser() != null) {
            backTeacherExpansion = backTeacherExpansionService.getByUserId(backTeacherExpansion.getUser().getId());
            List<TeacherKeyword> tes = teacherKeywordService.getKeywordByTeacherid(backTeacherExpansion.getId());
            if (tes.size() > 0) {
                model.addAttribute("tes",tes);
                hashMap.put("tes", tes);
            }
        }
        List<BackTeacherExpansion> teacherAwardInfo = backTeacherExpansionService.findTeacherAward(backTeacherExpansion.getUser().getId());
        if (teacherAwardInfo!=null) {
             model.addAttribute("teacherAwardInfo", teacherAwardInfo);
        }
        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
                model.addAttribute("allDomains", dictList);
        hashMap.put("allDomains", dictList);
        /*查询谁看过它*/
        model.addAttribute("visitors", sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
        hashMap.put("visitors",  sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
        /*查询我看过谁*/
        model.addAttribute("browse", sysViewsService.getBrowse(backTeacherExpansion.getUser().getId()));
        hashMap.put("browse",   sysViewsService.getBrowse(backTeacherExpansion.getUser().getId()));
        //导师参评信息
        List<ProjectExpVo> projectExpVo = proBackTeacherExpansionService.findProjectByTeacherId(backTeacherExpansion.getUser().getId());//查询项目经历
        List<GContestUndergo> gContest = proBackTeacherExpansionService.findGContestByTeacherId(backTeacherExpansion.getUser().getId()); //查询大赛经历
        model.addAttribute("projectExpVo", projectExpVo);
        model.addAttribute("gContestExpVo", gContest);
        model.addAttribute("cuser", backTeacherExpansion.getUser().getId());
        hashMap.put("projectExpVo", projectExpVo);
        hashMap.put("gContestExpVo", gContest);
        hashMap.put("cuser", dictList);
        hashMap.put("backTeacherExpansion", backTeacherExpansion);
        model.addAttribute("teacherData", hashMap);
        return SysSval.path.vms(SysEmskey.SYS.k()) + "front/editTeacher";
    }


    @RequestMapping(value = "save")
    public String save(BackTeacherExpansion backTeacherExpansion, Model model,String custRedict,String okurl,String backurl,RedirectAttributes redirectAttributes,HttpServletRequest request) {
        if (!beanValidator(model, backTeacherExpansion)) {
            return form(backTeacherExpansion, custRedict, okurl, backurl, model, request);
        }
        redirectAttributes.addFlashAttribute("custRedict", custRedict);
        redirectAttributes.addFlashAttribute("okurl", okurl);
        redirectAttributes.addFlashAttribute("backurl", backurl);
        String[] str=request.getParameterValues("user.domainIdList");
        User user=backTeacherExpansion.getUser();
        if(str==null){
            user.setDomainIdList(null);
            user.setDomain(null);
        }
        if (StringUtil.isNotBlank(backTeacherExpansion.getId())) {
            if (StringUtil.isNotEmpty(user.getId())&&teamUserHistoryService.getBuildingCountByUserId(user.getId())>0) {//修改时有正在进行的项目大赛
//              User old=UserUtils.get(user.getId());
//              if (old!=null&&StringUtil.isNotEmpty(old.getId())) {
//                  String checkRole=UserUtils.checkRoleChange(user, old);
//                  if (checkRole!=null) {//用户类型变化了
//                      addMessage(model, "保存失败，该用户有正在进行的项目或大赛，"+checkRole);
//                      return form(backTeacherExpansion, custRedict, okurl, backurl, model, request);
//                  }else if (UserUtils.checkHasRole(old, RoleBizTypeEnum.DS)) {//导师类型
//                      BackTeacherExpansion bte = proBackTeacherExpansionService.getByUserId(old.getId());
//                      if (bte!=null&&bte.getTeachertype()!=null&&!bte.getTeachertype().equals(backTeacherExpansion.getTeachertype())) {//导师类型的用户导师来源发生变化
//                          addMessage(model, "保存失败，该用户有正在进行的项目或大赛，不能修改导师来源");
//                          return form(backTeacherExpansion, custRedict, okurl, backurl, model, request);
//                      }
//                  }
//              }
                BackTeacherExpansion old = backTeacherExpansionService.getByUserId(user.getId());
                if(!backTeacherExpansion.getTeachertype().equals(old.getTeachertype())){
                    addMessage(model, "保存失败，该用户有正在进行的项目或大赛，不能修改导师来源");
                    return form(backTeacherExpansion, custRedict, okurl, backurl, model, request);
                }
            }
            backTeacherExpansionService.updateAll(backTeacherExpansion);
        }else {
            User exitUser = userService.getByMobile(backTeacherExpansion.getUser());
            if (exitUser != null) {
                List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
                model.addAttribute("allDomains", dictList);
                model.addAttribute("loginNameMessage","手机号已经存在!");
                model.addAttribute("operateType", "1");
                return SysSval.path.vms(SysEmskey.SYS.k()) + "backTeacherForm";
            }
            String companyId = officeService.selelctParentId(backTeacherExpansion.getUser().getOffice().getId());
            backTeacherExpansion.getUser().setCompany(new Office());
            backTeacherExpansion.getUser().getCompany().setId(companyId);//设置学校id
            backTeacherExpansionService.saveAll(backTeacherExpansion);
        }
        backTeacherExpansionService.updateKeyword(backTeacherExpansion);
        addMessage(redirectAttributes, "保存导师信息成功");
        redirectAttributes.addFlashAttribute("custRedict", custRedict);
        redirectAttributes.addFlashAttribute("okurl", okurl);
        redirectAttributes.addFlashAttribute("backurl", backurl);
        return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/form?id="+backTeacherExpansion.getId();
    }
}