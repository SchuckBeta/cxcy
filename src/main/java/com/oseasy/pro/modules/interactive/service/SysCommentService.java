package com.oseasy.pro.modules.interactive.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.pro.modules.course.dao.CourseDao;
import com.oseasy.pro.modules.interactive.dao.SysCommentDao;
import com.oseasy.pro.modules.interactive.dao.SysLikesDao;
import com.oseasy.pro.modules.interactive.entity.SysComment;
import com.oseasy.pro.modules.interactive.entity.SysLikes;

import com.oseasy.util.common.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;


import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 评论表Service.
 * @author 9527
 * @version 2017-06-30
 */
@Service
@Transactional(readOnly = true)
public class SysCommentService extends CrudService<SysCommentDao, SysComment> {
	private final static int pageSize=50;
	@Autowired
	private SysLikesDao sysLikesDao;
	@Autowired
	private SysCommentDao sysCommentDao;

	@Autowired
	private CourseDao courseDao;

	public Map<String,Object> getCommentData(HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();
		String foreignId=request.getParameter("foreignId");
		if (StringUtil.isNotEmpty(foreignId)) {
			User user=UserUtils.getUser();
			String token=request.getParameter("token");
			String userid=user.getId();
			if (StringUtil.isEmpty(userid)) {
				userid="-9999";
			}
			String ip= UrlUtil.getRemoteIp(request);
			if ("unknown".equals(ip)&&"-9999".equals(userid)&&StringUtil.isEmpty(token)) {//什么信息都取不到不能点赞
				map.put("isExistsLike", "1");
			}else{
				SysLikes sc=new SysLikes();
				sc.setUserId(userid);
				sc.setForeignId(foreignId);
				sc.setToken(token);
				sc.setIp(ip);
				if (sysLikesDao.getExistsLike(sc)>0) {
					map.put("isExistsLike", "1");
				}else{
					map.put("isExistsLike", "0");
				}
			}
			if ("-9999".equals(userid)) {
				map.put("myComments", "0");
			}else{
				map.put("myComments", sysCommentDao.getMyComments(userid, foreignId));
			}
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("foreignId", foreignId);
			param.put("pageNo", request.getParameter("pageNo"));
			param.put("userId", userid);
			param.put("token",token );
			param.put("ip", ip);
			map.put("list", getPageList(param));
			map.put("pageSize", pageSize);
		}else{
			return null;
		}
		return map;
	}
	public List<Map<String,String>>  getPageList(Map<String,Object> param) {
		int pageNo=Integer.parseInt(param.get("pageNo").toString());
		if (pageNo<=0) {
			pageNo=1;
		}
		param.put("offset", (pageNo-1)*pageSize);
		param.put("pageSize", pageSize);
		List<Map<String,String>> list=sysCommentDao.getPageList(param);//取评论数据
		if (list!=null&&list.size()>0) {
			List<String> foreignIds=new ArrayList<String>();//批量查询是否点过赞的id参数
			for(Map<String, String> map:list) {//处理图片链接
				foreignIds.add(map.get("id"));
				if (StringUtil.isEmpty(map.get("photo"))) {
					map.put("photo", "/img/u4110.png");
				}else{
					map.put("photo", FtpUtil.ftpImgUrl(map.get("photo")));
				}
			}
			param.put("foreignIds", foreignIds);
			List<Map<String, Object>> existsLikeList=sysLikesDao.getAllExistsLike(param);//批量查询是否点过赞
			Map<String, String> existsLikeMap=new HashMap<String, String>();
			for(Map<String, Object> map:existsLikeList) {//将list转为map方便下面一步处理
				existsLikeMap.put(map.get("foreign_id").toString(), map.get("cc").toString());
			}
			for(Map<String, String> map:list) {
				map.put("existsLikes", existsLikeMap.get(map.get("id")));//将是否点过赞的数据塞入需要返回的结果集
			}
		}
		return list;
	}
	/**
	 * 名师讲堂评论队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleCourseComment() {
		List<SysComment> list=new ArrayList<SysComment>();//需要保存的list
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新评论数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysComment sc=(SysComment)CacheUtils.rpop(CacheUtils.COURSE_COMMENT_QUEUE);
		while(count<tatol&&sc!=null) {
			count++;//增加了一条数据
			up=map.get(sc.getForeignId());
			if (up==null) {
				map.put(sc.getForeignId(), 1);
			}else{
				map.put(sc.getForeignId(), up+1);
			}
			list.add(sc);
			if (count<tatol) {
				sc=(SysComment)CacheUtils.rpop(CacheUtils.COURSE_COMMENT_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			sysCommentDao.insertBatch(list);
			courseDao.updateComments(map);
		}
		return count;
	}
	/**
	 * 优秀展示评论队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleExcellentComment() {
		List<SysComment> list=new ArrayList<SysComment>();//需要保存的list
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新评论数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysComment sc=(SysComment)CacheUtils.rpop(CacheUtils.EXCELLENT_COMMENT_QUEUE);
		while(count<tatol&&sc!=null) {
			count++;//增加了一条数据
			up=map.get(sc.getForeignId());
			if (up==null) {
				map.put(sc.getForeignId(), 1);
			}else{
				map.put(sc.getForeignId(), up+1);
			}
			list.add(sc);
			if (count<tatol) {
				sc=(SysComment)CacheUtils.rpop(CacheUtils.EXCELLENT_COMMENT_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			sysCommentDao.insertBatch(list);
		}
		return count;
	}

	public JSONObject save(JSONObject param,HttpServletRequest request) {
		JSONObject js= new JSONObject();
		js.put("ret", "1");
		js.put("msg", "评论成功");
		User user=UserUtils.getUser();
		if (user==null||StringUtil.isEmpty(user.getId())) {
			js.put("ret", "0");
			js.put("msg", "请先登录再进行评论");
			return js;
		}
		String foreignId=param.getString("foreignId");
		String foreignType=param.getString("foreignType");
		String content=param.getString("content");
		try {
			content=URLDecoder.decode(content, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.isEmpty(content)) {
			js.put("ret", "0");
			js.put("msg", "请填写评论内容");
			return js;
		}
		if (content.length()>500) {
			js.put("ret", "0");
			js.put("msg", "最多500个字");
			return js;
		}
		if (StringUtil.isEmpty(foreignId)||StringUtil.isEmpty(foreignType)) {
			js.put("ret", "0");
			js.put("msg", "发生了意外~");
			return js;
		}
		SysComment sc=new SysComment();
		sc.setId(IdGen.uuid());
		sc.setCreateDate(new Date());
		sc.setAuditState("1");
		sc.setUserId(user.getId());
		sc.setForeignId(foreignId);
		sc.setDelFlag("0");
		sc.setContent(content);
		sc.setIp(UrlUtil.getRemoteIp(request));
		sc.setLikes("0");
		if ("1".equals(foreignType)) {//优秀展示
			CacheUtils.lpush(CacheUtils.EXCELLENT_COMMENT_QUEUE, sc);
		}else if ("2".equals(foreignType)) {//名师讲堂
			CacheUtils.lpush(CacheUtils.COURSE_COMMENT_QUEUE, sc);
		}
		return js;
	}

	public SysComment get(String id) {
		return super.get(id);
	}

	public List<SysComment> findList(SysComment sysComment) {
		return super.findList(sysComment);
	}

	public Page<SysComment> findPage(Page<SysComment> page, SysComment sysComment) {
		return super.findPage(page, sysComment);
	}

	@Transactional(readOnly = false)
	public void save(SysComment sysComment) {
		super.save(sysComment);
	}

	@Transactional(readOnly = false)
	public void delete(SysComment sysComment) {
		super.delete(sysComment);
	}

}