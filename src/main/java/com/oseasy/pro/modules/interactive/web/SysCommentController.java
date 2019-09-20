package com.oseasy.pro.modules.interactive.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.interactive.dao.SysLikesDao;
import com.oseasy.pro.modules.interactive.entity.SysLikes;
import com.oseasy.pro.modules.interactive.service.SysCommentService;

import com.oseasy.util.common.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 评论表Controller.
 * @author 9527
 * @version 2017-06-30
 * @deprecated
 */
@Controller
@RequestMapping(value = "${frontPath}/interactive/sysComment")
public class SysCommentController extends BaseController {

	@Autowired
	private SysCommentService sysCommentService;
	@Autowired
	private SysLikesDao sysLikesDao;


	@RequestMapping(value = "save")
	@ResponseBody
	public JSONObject save(@RequestBody JSONObject param,HttpServletRequest request) {
		return sysCommentService.save(param,request);
	}
	@RequestMapping(value = "getCommentData")
	@ResponseBody
	public Map<String,Object> getCommentData(HttpServletRequest request) {
		return sysCommentService.getCommentData(request);
	}

	@RequestMapping(value="checkLiked", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String,Object> checkLiked(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		String foreignId=request.getParameter("foreignId");
		if (StringUtil.isNotEmpty(foreignId)) {
			User user = UserUtils.getUser();
			String token = request.getParameter("token");
			String userid = user.getId();
			if (StringUtil.isEmpty(userid)) {
				userid = "-9999";
			}
			String ip = UrlUtil.getRemoteIp(request);
			if ("unknown".equals(ip) && "-9999".equals(userid) && StringUtil.isEmpty(token)) {//什么信息都取不到不能点赞
				map.put("isExistsLike", "1");
			} else {
				SysLikes sc = new SysLikes();
				sc.setUserId(userid);
				sc.setForeignId(foreignId);
				sc.setToken(token);
				sc.setIp(ip);
				if (sysLikesDao.getExistsLike(sc) > 0) {
					map.put("isExistsLike", "1");
				} else {
					map.put("isExistsLike", "0");
				}
			}
			return map;
		}
		return null;
	}


	@RequestMapping(value = "getNextPage")
	@ResponseBody
	public List<Map<String,String>> getNextPage(HttpServletRequest request) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("foreignId", request.getParameter("foreignId"));
		param.put("pageNo", request.getParameter("pageNo"));
		String queryType=request.getParameter("queryType");
		User user=UserUtils.getUser();
		String token=request.getParameter("token");
		String userid=user.getId();
		if (StringUtil.isEmpty(userid)) {
			userid="-9999";
		}
		String ip= UrlUtil.getRemoteIp(request);
		param.put("userId", userid);
		param.put("token",token );
		param.put("ip", ip);
		if ("2".equals(queryType)) {
			if (StringUtil.isEmpty(UserUtils.getUser().getId())) {
				return null;
			}
			param.put("userid", UserUtils.getUser().getId());
		}
		return sysCommentService.getPageList(param);
	}
}