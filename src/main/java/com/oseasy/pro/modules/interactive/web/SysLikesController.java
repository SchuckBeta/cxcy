package com.oseasy.pro.modules.interactive.web;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.interactive.entity.SysLikes;
import com.oseasy.pro.modules.interactive.service.SysLikesService;
import com.oseasy.util.common.utils.StringUtil;

import com.oseasy.util.common.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import java.util.Date;

/**
 * 点赞表Controller.
 * @author 9527
 * @version 2017-06-30
 * @deprecated
 */
@Controller
@RequestMapping(value = "${frontPath}/interactive/sysLikes")
public class SysLikesController extends BaseController {

	@Autowired
	private SysLikesService sysLikesService;

	@RequestMapping(value = "save")
	@ResponseBody
	public JSONObject save(@RequestBody JSONObject param,HttpServletRequest request) {
		return sysLikesService.save(param,request);
	}
	@RequestMapping(value = "saveForUserInfo")
	@ResponseBody
	public JSONObject saveForUserInfo(@RequestBody JSONObject param,HttpServletRequest request) {
		return sysLikesService.saveForUserInfo(param,request);
	}

	@RequestMapping(value="doLike", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult doLike(String doUserId, HttpServletRequest request){
		User user= UserUtils.getUser();
		String userid=user.getId();
		if (StringUtil.isEmpty(userid)) {
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"点赞失败，请重新登录");
		}
		String ip= UrlUtil.getRemoteIp(request);
		if (StringUtil.isEmpty(doUserId)) {
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"发生了意外");
		}
		SysLikes sc=new SysLikes();
		sc.setId(IdGen.uuid());
		sc.setCreateDate(new Date());
		sc.setUserId(userid);
		sc.setForeignId(doUserId);
		sc.setToken("unknown");
		sc.setDelFlag("0");
		sc.setIp(ip);
		CacheUtils.lpush(CacheUtils.USER_LIKES_QUEUE, sc);
		return ApiResult.success();
	}

	@RequestMapping(value = "uuid")
	@ResponseBody
	public String uuid() {
		return IdGen.uuid();
	}

}