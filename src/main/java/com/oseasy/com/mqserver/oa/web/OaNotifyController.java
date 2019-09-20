/**
 *
 */
package com.oseasy.com.mqserver.oa.web;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
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
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.mqserver.common.config.MqsrSval;
import com.oseasy.com.mqserver.common.config.MqsrSval.MqsrEmskey;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyKeywordService;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 通知通告Controller
TODO
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaNotify")
public class OaNotifyController extends BaseController {
	@Autowired
	private OaNotifyKeywordService oaNotifyKeywordService;
	@Autowired
	private OaNotifyService oaNotifyService;

	@ModelAttribute
	public OaNotify get(@RequestParam(required=false) String id) {
		OaNotify entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = oaNotifyService.get(id);
		}
		if (entity == null) {
			entity = new OaNotify();
		}
		return entity;
	}
	/**
	 * 查看我的通知
	 */
	@RequestMapping(value = "viewMsg")
	@ResponseBody
	public OaNotify viewMsg(String oaNotifyId, Model model) {
		OaNotify oaNotify = new OaNotify();
		oaNotify.setId(oaNotifyId);
		oaNotifyService.updateReadFlag(oaNotify);
		oaNotify = oaNotifyService.get(oaNotify.getId());
		if (oaNotify == null) {
		  return null;
		}
		OaNotify  oaNotifyTmp = oaNotifyService.getRecordList(oaNotify);
		if (oaNotifyTmp == null) {
      		return null;
    	}
		if ((oaNotify == null) || (oaNotifyTmp == null)) {
		  return oaNotify;
		}
		oaNotify.setOaNotifyRecordList(oaNotifyTmp.getOaNotifyRecordList());
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (oaNotify.getEffectiveDate()!=null) {
			String effDate = sFormat.format(oaNotify.getEffectiveDate());
			oaNotify.setPublishDate(effDate);
		}
		if (oaNotify.getContent()!=null) {
			oaNotify.setContent(StringUtil.replaceEscapeHtml(oaNotify.getContent()));
		}
		if (oaNotify!=null&&StringUtil.isNotEmpty(oaNotify.getContent())) {
			oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}
		return oaNotify;
	}
	@RequestMapping(value = "sendTestMsg")
	@ResponseBody
	public JSONObject sendTestMsg(String uid) {
		JSONObject js=new JSONObject();
		try {
			OaNotify on=new OaNotify();
			on.setContent("这是一条测试消息内容");
			on.setTitle("这是一条测试消息标题");
			on.setSendType(OaNotifySendType.DIRECRIONAL.getVal());
			on.setStatus("1");
			on.setEffectiveDate(new Date());
			on.setType(OaNotify.Type_Enum.TYPE1.getValue());
			on.setOaNotifyRecordIds(uid);
			oaNotifyService.save(on);
			js.put("ret", "1");
			js.put("msg", "发送成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			js.put("ret", "0");
			js.put("msg", "发送失败");
		}
		return js;
	}
	@RequestMapping(value = {"getUnreadCount"})
	@ResponseBody
	public JSONObject getUnreadCount() {
		String uid=UserUtils.getUser().getId();
		JSONObject js=new JSONObject();
		js.put("ret", "1");
		if (StringUtil.isEmpty(uid)) {
			js.put("ret", "0");
			js.put("count", 0);
			return js;
		}
		Integer c=oaNotifyService.getUnreadCountByUser(UserUtils.getUser());
		if (c==null) {
			c=0;
		}
		js.put("count",c);
		return js;
	}
	@RequestMapping(value = "deleteRec")
	public String deleteRec(OaNotify oaNotify, RedirectAttributes redirectAttributes) {
		oaNotifyService.deleteRec(oaNotify);
		addMessage(redirectAttributes, "删除成功");
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + CoreUtils.OA_NOTIFY_MSG_LIST + "/?repage";
	}

	@RequestMapping(value = "deleteRecMsg", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult deleteRecMsg(@RequestBody List<OaNotify> list){
		for(OaNotify oaNotify: list){
			oaNotifyService.deleteRec(oaNotify);
		}
		return ApiResult.success();
	}


	@RequestMapping(value = "deleteSend")
	public String deleteSend(OaNotify oaNotify, RedirectAttributes redirectAttributes) {
		oaNotifyService.deleteSend(oaNotify);
		addMessage(redirectAttributes, "删除成功");
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/msgSendList/?repage";
	}

	@RequestMapping(value = "deleteSendMsg", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult deleteSendMsg(@RequestBody List<OaNotify> list){
		for(OaNotify oaNotify: list){
			oaNotify=oaNotifyService.get(oaNotify.getId());
			oaNotifyService.deleteSend(oaNotify);
		}
		return ApiResult.success();
	}

	/**
	 * 批量删除接收消息
	 * @param ids OaNotify的ids，以逗号分隔
	 */
	@RequestMapping(value = "deleteRevBatch")
	@ResponseBody
	public boolean deleteRevBatch(String ids) {
		String[] idStr=ids.split(",");
		for (int i=0;i<idStr.length;i++) {
			OaNotify oaNotify=new OaNotify();
			oaNotify.setId(idStr[i]);
			oaNotifyService.deleteRec(oaNotify);
		}
		return true;
	}

	/**
	 * 批量删除发送消息
	 * @param ids OaNotify的ids，以逗号分隔
	 */
	@RequestMapping(value = "deleteSendBatch")
	@ResponseBody
	public boolean deleteSendBatch(String ids) {
		String[] idStr=ids.split(",");
		for (int i=0;i<idStr.length;i++) {
			OaNotify oaNotify=new OaNotify();
			oaNotify.setId(idStr[i]);
			oaNotifyService.deleteSend(oaNotify);
		}
		return true;
	}

	@RequestMapping(value = "getReadFlag")
	@ResponseBody
	public String getReadFlag(String oaNotifyId) {
		String flag="0";
		User u =UserUtils.getUser();
        if (u != null) {
            return oaNotifyService.getReadFlag(oaNotifyId, u);
		}
		return flag;
	}
	@RequestMapping(value="resetNotifyShow")
	@ResponseBody
	public String resetNotifyShow(HttpServletRequest request) {
		if (request.getSession().getAttribute("notifyShow")!=null) {//登录成功后重置是否弹出消息
			request.getSession().removeAttribute("notifyShow");
		}
		return "1";
	}
	@RequestMapping(value="closeButton")
	@ResponseBody
	public String closeButton(HttpServletRequest request) {
		String oaNotifyId = request.getParameter("send_id");
		OaNotify oaNotify = oaNotifyService.get(oaNotifyId);
		oaNotifyService.updateReadFlag(oaNotify);
		return "1";
	}
	@RequestMapping(value = {"msgRecList"})
	public String msgRecList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		User currUser = UserUtils.getUser();
		if (currUser!=null&&currUser.getId()!=null) {
			oaNotify.setUserId(String.valueOf(currUser.getId()));
		}else{
			oaNotify.setType("error");
		}

		oaNotify.setIsSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		model.addAttribute("page", page);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "adminMsgRec";
	}

	@RequestMapping(value = "getMsgRecList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getMsgRecList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response){
		User currUser = UserUtils.getUser();
		if (currUser!=null&&currUser.getId()!=null) {
			oaNotify.setUserId(String.valueOf(currUser.getId()));
		}else{
			oaNotify.setType("error");
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":用户不存在，请重新登录");
		}
		oaNotify.setIsSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		List<OaNotify> list = page.getList();
		for (OaNotify oaNotifyItem : list) {
			String id = oaNotifyItem.getCreateBy().getId();
			User user = UserUtils.get(id);
			oaNotifyItem.setCreateUser(user);
		}
		return ApiResult.success(page);
	}

	/**
	 * 消息类型字典.
	 * @return ApiResult
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxOaNotifyTypes", method = RequestMethod.GET)
	public ApiResult ajaxOaNotifyTypes(@RequestParam Boolean isAll) {
		try {
			if(isAll == null){
				isAll = false;
			}
			if(isAll){
				return ApiResult.success(Arrays.asList(OaNotify.Type_Enum.values()).toString());
			}else{
				return ApiResult.success(OaNotify.Type_Enum.getAll());
			}
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}
	@RequestMapping(value = {"msgSendList"})
	public String msgSendList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		User currUser = UserUtils.getUser();
		if (currUser!=null&&currUser.getId()!=null) {
			oaNotify.setUserId(String.valueOf(currUser.getId()));
		}else{
			oaNotify.setType("error");
		}

		Page<OaNotify> page = oaNotifyService.findSend(new Page<OaNotify>(request, response), oaNotify);
		model.addAttribute("page", page);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "adminMsgSend";
	}

	@RequestMapping(value = "getMsgSendList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getMsgSendList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response){
		User currUser = UserUtils.getUser();
		if (currUser!=null&&currUser.getId()!=null) {
			oaNotify.setUserId(String.valueOf(currUser.getId()));
		}else{
			oaNotify.setType("error");
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":用户不存在，请重新登录");
		}

		Page<OaNotify> page = oaNotifyService.findSend(new Page<OaNotify>(request, response), oaNotify);
		List<OaNotify> list = page.getList();
		for (OaNotify oaNotifyItem : list) {
			String id = oaNotifyItem.getCreateBy().getId();
			User user = UserUtils.get(id);
			oaNotifyItem.setCreateUser(user);
		}
		return ApiResult.success(page);
	}


	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		User currUser = UserUtils.getUser();
		if (currUser!=null&&currUser.getId()!=null&&!"1".equals(currUser.getId())) {
			oaNotify.setUserId(String.valueOf(currUser.getId()));
		}
		oaNotify.setIsSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		model.addAttribute("page", page);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyList";
	}



	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = {"broadcastList"})
	public String broadcastList() {
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyListBroadcast";
	}

	@RequestMapping(value="getOaNotifyList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getOaNotifyList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			oaNotify.setSendType(OaNotifySendType.DIS_DIRECRIONAL.getVal());
			Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
			List<OaNotify> oaNotifies = page.getList();
			for(OaNotify oaNotifyItem: oaNotifies){
				User user = UserUtils.get(oaNotifyItem.getCreateBy().getId());
				oaNotifyItem.setCreateUser(user);
			}
			page.setList(oaNotifies);
			return  ApiResult.success(page);
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = {"assignList"})
	public String assignList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		model.addAttribute("page", page);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyListAssign";
	}

	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = "formAssign")
	public String formAssign(OaNotify oaNotify, Model model) {
		if (StringUtil.isNotBlank(oaNotify.getId())) {
			oaNotify = oaNotifyService.getRecordList(oaNotify);
		}
		model.addAttribute("oaNotify", oaNotify);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyFormAssign";
	}

	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = "formBroadcast")
	public String formBroadcast(OaNotify oaNotify, Model model,HttpServletRequest request) {
		if (StringUtil.isNotBlank(oaNotify.getId())) {
			oaNotify = oaNotifyService.getRecordList(oaNotify);
		}

		if (StringUtil.isNotBlank(oaNotify.getId())) {
			oaNotify=oaNotifyService.getRecordList(oaNotify);
			List<String> officeIdList= Lists.newArrayList();
			List<String> officeNameList = Lists.newArrayList();
			for(OaNotifyRecord onr:oaNotify.getOaNotifyRecordList()) {
				if (!officeIdList.contains(onr.getUser().getOffice().getId())) {
					officeIdList.add(onr.getUser().getOffice().getId());
				}
				if (!officeNameList.contains(onr.getUser().getOffice().getName())) {
					officeNameList.add(onr.getUser().getOffice().getName());
				}
			}
			model.addAttribute("officeIdList", StringUtil.join(officeIdList, ","));
			model.addAttribute("officeNameList",  StringUtil.join(officeNameList, ","));
		}

		model.addAttribute("oaNotify", oaNotify);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyFormBroadcast";
	}

    /**
     * 发布.
     * @param model              模型
     * @param request            请求
     * @param redirectAttributes 重定向
     * @return String
     */
    @RequiresPermissions("oa:oaNotify:edit")
    @RequestMapping(value = "ajaxDeploy")
    public String ajaxDeploy(OaNotify oaNotify, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (StringUtil.isNotEmpty(oaNotify.getId()) && (oaNotify.getStatus() != null)) {
            OaNotify curOaNotify = oaNotifyService.get(oaNotify.getId());
            curOaNotify.setStatus(oaNotify.getStatus());
            oaNotifyService.save(curOaNotify);
            addMessage(redirectAttributes, "通知'" + oaNotify.getTitle() + "'更新成功");
        }else{
            addMessage(redirectAttributes, "通知更新失败");
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/broadcastList?repage";
    }


	@RequestMapping(value="updatePublish", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult updatePublish(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			if (StringUtil.isNotEmpty(oaNotify.getId()) && (oaNotify.getStatus() != null)) {
				OaNotify curOaNotify = oaNotifyService.get(oaNotify.getId());
				curOaNotify.setStatus(oaNotify.getStatus());
				oaNotifyService.save(curOaNotify);
				return ApiResult.success();
			}else {
				return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":通知更新失败");
			}
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequiresPermissions("oa:oaNotify:edit")
	@RequestMapping(value = "delete")
	public String delete(OaNotify oaNotify, RedirectAttributes redirectAttributes) {
		oaNotifyService.delete(oaNotify);
		addMessage(redirectAttributes, "删除通告成功");
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/broadcastList?repage";
	}

	@RequestMapping(value="delOaNotify", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult delOaNotify(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			oaNotifyService.delete(oaNotify);
			return ApiResult.success();
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 * 我的通知列表
	 */
	@RequestMapping(value = "self")
	public String selfList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaNotify.setIsSelf(true);
		Page<OaNotify> page = oaNotifyService.findAllRecord(new Page<OaNotify>(request, response), oaNotify);
		model.addAttribute("page", page);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyList";
	}



	/**
	 * 我的通知列表-数据
	 */
	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = "selfData")
	@ResponseBody
	public Page<OaNotify> listData(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaNotify.setIsSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		return page;
	}

	/**
	 * 查看我的通知
	 */
	@RequestMapping(value = "view")
	public String view(OaNotify oaNotify, Model model) {
		if (StringUtil.isNotBlank(oaNotify.getId())) {
			oaNotifyService.updateReadFlag(oaNotify);
			oaNotify = oaNotifyService.getRecordListByUser(oaNotify);
			if (oaNotify!=null&&StringUtil.isNotEmpty(oaNotify.getContent())) {
				oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
			}
			model.addAttribute("oaNotify", oaNotify);
			if (oaNotify!=null) {
				if ("1".equals(oaNotify.getSendType())) {
					return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyFormBroadcast";
				}else if ("2".equals(oaNotify.getSendType())) {
					return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyFormAssign";
				}
			}else{
			  return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/self?repage";
			}
		}
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/self?repage";
	}

	/**
	 * 查看我的通知-数据
	 */
	@RequestMapping(value = "viewData")
	@ResponseBody
	public OaNotify viewData(OaNotify oaNotify) {
		if (StringUtil.isNotBlank(oaNotify.getId())) {
			oaNotifyService.updateReadFlag(oaNotify);
			return oaNotify;
		}
		return null;
	}

	/**
	 * 查看我的通知-发送记录
	 */
	@RequestMapping(value = "viewRecordData")
	@ResponseBody
	public OaNotify viewRecordData(OaNotify oaNotify) {
		if (StringUtil.isNotBlank(oaNotify.getId())) {
			oaNotify = oaNotifyService.getRecordList(oaNotify);
			return oaNotify;
		}
		return null;
	}

	/**
	 * 获取我的通知数目
	 */
	@RequestMapping(value = "self/count")
	@ResponseBody
	public String selfCount(OaNotify oaNotify) {
		oaNotify.setIsSelf(true);
		oaNotify.setReadFlag("0");
		return String.valueOf(oaNotifyService.findCount(oaNotify));
	}
	/**
	 * 获取我的通知数目
	 */
	@RequestMapping(value = "validateName")
	@ResponseBody
	public String validateName(String title,String oldTitle) {
		if (StringUtil.equals(title,oldTitle)) {
			return "0";
		}
		OaNotify oaNotify = new OaNotify();
		oaNotify.setTitle(title);
		return String.valueOf(oaNotifyService.findCount(oaNotify));
	}

	@RequestMapping(value = "checkTitle")
	@ResponseBody
	public boolean checkTitle(String title,String oldTitle) {
		if (StringUtil.equals(title,oldTitle)) {
			return true;
		}

		OaNotify oaNotify = new OaNotify();
		oaNotify.setTitle(title);
		if ( oaNotifyService.findCount(oaNotify)>0) {
			return false;
		}
		return true;
	}
	//通告添加
	@RequestMapping(value = "allNoticeForm")
	public String allNoticeForm(OaNotify oaNotify) {
	    if (oaNotify != null) {
	  	  //处理关键字
	  		if (StringUtil.isNotEmpty(oaNotify.getId())) {
	  			if ("4".equals(oaNotify.getType())||"8".equals(oaNotify.getType())||"9".equals(oaNotify.getType())) {
	  					oaNotify.setKeywords(oaNotifyKeywordService.findListByEsid(oaNotify.getId()));
	  			}
	  		}
	  		if (oaNotify!=null&&StringUtil.isNotEmpty(oaNotify.getContent())) {
	  			oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
	  		}
	    }
	    return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "allNoticeForm";
	}
	@RequestMapping(value = "saveAllNotice")
	public String saveAllNotice(OaNotify oaNotify, RedirectAttributes redirectAttributes) {
		oaNotifyService.saveCollegeBroadcast(oaNotify);
		addMessage(redirectAttributes, "保存通告" + oaNotify.getTitle() + "成功");
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/broadcastList";
	}

	@RequestMapping(value="saveNotify", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveNotify(@RequestBody OaNotify oaNotify){
		try {
			oaNotifyService.saveCollegeBroadcast(oaNotify);
			return ApiResult.success();
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//通知下发
	@RequestMapping(value = "toSendOaNotifyList")
	public String toSendOaNotifyList() {
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "toSendOaNotifyList";
	}


	@RequestMapping(value = "toSendOaNotifyForm")
	public String toSendOaNotifyForm(OaNotify oaNotify, Model model) {
		model.addAttribute("oaNotify", oaNotify);
		return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "toSendOaNotifyForm";
	}
}