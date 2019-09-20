package com.oseasy.pw.modules.pw.web;

import java.util.Arrays;
import java.util.List;

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

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwCategory;
import com.oseasy.pw.modules.pw.entity.PwFassets;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.service.PwFassetsService;
import com.oseasy.pw.modules.pw.service.PwRoomService;
import com.oseasy.pw.modules.pw.vo.PwFassetsAssign;
import com.oseasy.pw.modules.pw.vo.PwFassetsBatch;
import com.oseasy.pw.modules.pw.vo.PwFassetsStatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 固定资产Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwFassets")
public class PwFassetsController extends BaseController {

    @Autowired
    private PwFassetsService pwFassetsService;
    @Autowired
    private PwRoomService pwRoomService;

    @ModelAttribute
    public PwFassets get(@RequestParam(required = false) String id) {
        PwFassets entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwFassetsService.get(id);
        }
        if (entity == null) {
            entity = new PwFassets();
        }
        return entity;
    }

    @RequiresPermissions("pw:pwFassets:view")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsList";
    }

    @RequestMapping(value = {"listpage", ""})
    @ResponseBody
    public ApiResult listpage(PwFassets pwFassets, HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Page<PwFassets> page = pwFassetsService.findPage(new Page<PwFassets>(request, response), pwFassets);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = {"listYfp"})
    @ResponseBody
    public ApiResult listYfp(PwFassets pwFassets, HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Page<PwFassets> page = pwFassetsService.findPageByYfp(new Page<PwFassets>(request, response), pwFassets);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequiresPermissions("pw:pwFassets:view")
    @RequestMapping(value = "form")
    public String form(PwFassets pwFassets, Model model, HttpServletRequest request) {
        model.addAttribute("pwFassets", pwFassets);
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsForm";
    }

    @RequiresPermissions("pw:pwFassets:view")
    @RequestMapping(value = "details")
    public String details(PwFassets pwFassets, Model model) {
        model.addAttribute("pwFassets", pwFassets);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsDetails";
    }

    @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult save(@RequestBody PwFassets pwFassets, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, pwFassets)) {
            return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(),ApiConst.getErrMsg(ApiConst.PARAM_ERROR.getCode()));
        }
        try {
            pwFassetsService.save(pwFassets);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequiresPermissions("pw:pwFassets:view")
    @RequestMapping(value = "batchForm")
    public String batchForm(PwFassetsBatch pwFassetsModel, Model model, HttpServletRequest request) {
        model.addAttribute("pwFassetsModel", pwFassetsModel);
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsBatchAddForm";
    }

    /**
     * 批量添加资产
     * @param pwFassetsModel
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "batchSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult batchSave(@RequestBody PwFassetsBatch pwFassetsModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, pwFassetsModel)) {
            return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(),ApiConst.getErrMsg(ApiConst.PARAM_ERROR.getCode()));
        }
        try {
            pwFassetsService.batchSave(pwFassetsModel);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public ApiResult delete(PwFassets pwFassets, RedirectAttributes redirectAttributes) {
        try {
            pwFassetsService.delete(pwFassets);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 给房间分配固定资产.
     *
     * @param pwFassets 实体
     * @param model     模型
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "set")
    public String set(PwFassets pwFassets, Model model, RedirectAttributes redirectAttributes) {
        if (StringUtil.isEmpty(pwFassets.getId())) {
            addMessage(redirectAttributes, "分配固定资产失败，资产ID不能为空！");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/listYfp/?repage";
        }

        if ((pwFassets.getPwRoom() == null) || StringUtil.isEmpty(pwFassets.getPwRoom().getId())) {
            addMessage(redirectAttributes, "分配固定资产失败，房间ID不能为空！");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/listYfp/?repage";
        }

        try {
            pwFassetsService.assign(pwFassets);
            addMessage(redirectAttributes, "分配固定资产成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/listYfp/?repage";
    }

    /**
     * 取消分配资产.
     *
     * @param pwFassets 实体
     * @param model     模型
     * @return ActYwRstatus
     */
    /*@RequestMapping(value = "cancel")
    public String cancel(PwFassets pwFassets, Model model, RedirectAttributes redirectAttributes) {
        if (StringUtil.isEmpty(pwFassets.getId())) {
            addMessage(redirectAttributes, "删除失败，数据不存在");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/listYfp/?repage&pwRoom.id=" + pwFassets.getPwRoom().getId();
        }

        try {
            pwFassetsService.unAssign(pwFassets);
            addMessage(redirectAttributes, "取消分配成功");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/listYfp/?repage&pwRoom.id=" + pwFassets.getPwRoom().getId();
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/listYfp/?repage";
    }*/

    /**
     * 分配固定资产.
     *
     * @param pwFassets 实体
     * @param model     模型
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxSet")
    public ApiTstatus<PwFassets> ajaxSet(PwFassets pwFassets, Model model, HttpServletResponse response) {
        if (StringUtil.isEmpty(pwFassets.getId())) {
            return new ApiTstatus<PwFassets>(false, "分配失败，资产数据不存在");
        }

        if ((pwFassets.getPwRoom() == null) || StringUtil.isEmpty(pwFassets.getPwRoom().getId())) {
            return new ApiTstatus<PwFassets>(false, "分配失败，房间数据不存在");
        }

        try {
            pwFassetsService.assign(pwFassets);
        } catch (Exception e) {
            return new ApiTstatus<PwFassets>(false, e.getMessage());
        }
        return new ApiTstatus<PwFassets>(true, "分配成功", pwFassets);
    }

    /**
     * 批量分配固定资产.
     *
     * @param pwFassets 实体
     * @param model     模型
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxSetPL")
    public ApiTstatus<PwFassets> ajaxSetPL(PwFassets pwFassets, Model model, HttpServletResponse response) {
        if (StringUtil.isEmpty(pwFassets.getId())) {
            return new ApiTstatus<PwFassets>(false, "分配失败，资产数据不存在");
        }

        if ((pwFassets.getPwRoom() == null) || StringUtil.isEmpty(pwFassets.getPwRoom().getId())) {
            return new ApiTstatus<PwFassets>(false, "分配失败，房间数据不存在");
        }

        PwRoom pwRoom = pwRoomService.get(pwFassets.getPwRoom().getId());
        if (pwRoom == null) {
            return new ApiTstatus<PwFassets>(true, "分配失败，场地不存在", pwFassets);
        } else {
            pwFassets.setRespName(pwRoom.getPerson());
            pwFassets.setRespPhone(pwRoom.getPhone());
            pwFassets.setRespMobile(pwRoom.getMobile());
        }
        try {
            pwFassetsService.batchAssign(pwFassets, pwFassetsService.findListByIds(Arrays.asList(StringUtil.split(pwFassets.getId(), StringUtil.DOTH))));
        }catch (Exception e){
            return  new ApiTstatus<PwFassets>(false, e.getMessage());
        }
        return new ApiTstatus<PwFassets>(true, "分配成功", pwFassets);
    }

    /**
     * 批量取消分配固定资产.
     *
     * @param pwFassets 实体
     * @param model     模型
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxCancelPL")
    public ApiTstatus<PwFassets> ajaxCancelPL(PwFassets pwFassets, Model model, HttpServletResponse response) {
        if (StringUtil.isEmpty(pwFassets.getId())) {
            return new ApiTstatus<PwFassets>(false, "取消失败，资产数据不存在");
        }

        if ((pwFassets.getPwRoom() == null) || StringUtil.isEmpty(pwFassets.getPwRoom().getId())) {
            return new ApiTstatus<PwFassets>(false, "取消失败，房间数据不存在");
        }

        List<PwFassets> pwFassetss = pwFassetsService.findListByIds(Arrays.asList(StringUtil.split(pwFassets.getId(), StringUtil.DOTH)));
        pwFassetsService.updateByPL(pwFassets.getPwRoom().getId(), PwFassetsStatus.UNUSED.getValue(), pwFassetss);
        return new ApiTstatus<PwFassets>(true, "取消成功", pwFassets);
    }

    /**
     * 批量取消分配资产接口
     * @param ids 资产id（多个id用逗号分隔）
     * Add : liangjie 2018-11-20
     */
    @RequestMapping(value = "cancelFassets")
    @ResponseBody
    public ApiResult cancelFassets(String ids){
        try {
            List<PwFassets> pwFassetsList = pwFassetsService.findListByIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
             pwFassetsService.unAssign(pwFassetsList);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 根据房间获取资产.
     *
     * @param rid      类别ID
     * @param response
     * @return ActYwRstatus
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeDataRoom/{rid}")
    public ApiTstatus<List<PwFassets>> treeDataRoom(@PathVariable String rid, HttpServletResponse response) {
        List<PwFassets> list = pwFassetsService.findListByRoom(new PwFassets(new PwRoom(rid)));
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<PwFassets>>(false, "请求失败或数据为空！");
        }
        return new ApiTstatus<List<PwFassets>>(true, "请求成功", list);
    }

    /**
     * 根据类别获取资产.
     *
     * @param cid      类别ID
     * @param response
     * @return ActYwRstatus
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData/{cid}")
    public ApiTstatus<List<PwFassets>> treeData(@PathVariable String cid, HttpServletResponse response) {
        List<PwFassets> list = pwFassetsService.findListByNoRoom(new PwFassets(new PwCategory(cid)));
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<PwFassets>>(false, "请求失败或数据为空！");
        }
        return new ApiTstatus<List<PwFassets>>(true, "请求成功", list);
    }

    /**
     * 根据类别获取资产.
     *
     * @param response
     * @return ActYwRstatus
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeDataAll")
    public ApiTstatus<List<PwFassets>> treeData(HttpServletResponse response) {
        List<PwFassets> list = pwFassetsService.findListByNoRoom(new PwFassets());
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<PwFassets>>(false, "请求失败或数据为空！");
        }
        return new ApiTstatus<List<PwFassets>>(true, "请求成功", list);
    }


    /**
     * 取消分配资产
     * @param model
     * @param redirectAttributes
     * @return
     */
   /* @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "unassign")
    public String unAssign(PwFassets pwFassets, Model model, RedirectAttributes redirectAttributes) {
        try {
            pwFassetsService.unAssign(pwFassets);
            addMessage(redirectAttributes, "取消分配成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, e.getMessage());
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/?repage";
    }*/

    @RequiresPermissions("pw:pwFassets:view")
    @RequestMapping(value = "assignForm")
    public String assignForm(PwFassetsAssign pwFassetsAssign, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (StringUtils.isBlank(pwFassetsAssign.getFassetsIds())) {
            addMessage(redirectAttributes, "未选择资产");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassets/?repage";
        }
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        String[] split = pwFassetsAssign.getFassetsIds().split(",");
        List<PwFassets> list = pwFassetsService.findListByIds(Arrays.asList(split));
        model.addAttribute("list", list);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsAssignForm";
    }

    /**
     * 分配资产
     * @param pwFassetsAssign
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "assign")
    @ResponseBody
    public ApiResult assign(@RequestBody PwFassetsAssign pwFassetsAssign, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            pwFassetsService.batchAssign(pwFassetsAssign);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }

    }

    /**
     * 变更状态为损坏
     * @param pwFassets
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "changeBroken")
    @ResponseBody
    public ApiResult changeBroken(PwFassets pwFassets, Model model, RedirectAttributes redirectAttributes) {
        try {
            pwFassetsService.changeBroken(pwFassets);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 变更状态为闲置
     * @param pwFassets
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("pw:pwFassets:edit")
    @RequestMapping(value = "changeUnused")
    @ResponseBody
    public ApiResult changeUnused(PwFassets pwFassets, Model model, RedirectAttributes redirectAttributes) {
        try {
            pwFassetsService.changeUnused(pwFassets);
            return ApiResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}