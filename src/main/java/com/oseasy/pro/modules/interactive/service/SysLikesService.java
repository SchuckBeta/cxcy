package com.oseasy.pro.modules.interactive.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.pro.modules.course.dao.CourseDao;
import com.oseasy.pro.modules.interactive.dao.SysCommentDao;
import com.oseasy.pro.modules.interactive.dao.SysLikesDao;
import com.oseasy.pro.modules.interactive.entity.SysLikes;

import com.oseasy.util.common.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;

import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 点赞表Service.
 * @author 9527
 * @version 2017-06-30
 */
@Service
@Transactional(readOnly = true)
public class SysLikesService extends CrudService<SysLikesDao, SysLikes> {
	@Autowired
	private SysLikesDao sysLikesDao;
	@Autowired
	private SysCommentDao sysCommentDao;

	@Autowired
	private CourseDao courseDao;
	@Autowired
	private UserDao userDao;

	/**
	 * 导师、学生详情赞队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleUserInfoLikes() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新点赞数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysLikes sc=(SysLikes)CacheUtils.rpop(CacheUtils.USER_LIKES_QUEUE);
		while(count<tatol&&sc!=null) {
			if (sysLikesDao.getExistsLike(sc)==0) {
				count++;//增加了一条数据
				sysLikesDao.insert(sc);
				up=map.get(sc.getForeignId());
				if (up==null) {
					map.put(sc.getForeignId(), 1);
				}else{
					map.put(sc.getForeignId(), up+1);
				}
			}
			if (count<tatol) {
				sc=(SysLikes)CacheUtils.rpop(CacheUtils.USER_LIKES_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			userDao.updateLikes(map);
			for(String userid:map.keySet()) {
				CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ +userid);
			}
		}
		return count;
	}
	/**
	 * 评论的点赞队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleCommentLikes() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新点赞数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysLikes sc=(SysLikes)CacheUtils.rpop(CacheUtils.COMMENT_LIKES_QUEUE);
		while(count<tatol&&sc!=null) {
			if (sysLikesDao.getExistsLike(sc)==0) {
				count++;//增加了一条数据
				sysLikesDao.insert(sc);
				up=map.get(sc.getForeignId());
				if (up==null) {
					map.put(sc.getForeignId(), 1);
				}else{
					map.put(sc.getForeignId(), up+1);
				}
			}
			if (count<tatol) {
				sc=(SysLikes)CacheUtils.rpop(CacheUtils.COMMENT_LIKES_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			sysCommentDao.updateLikes(map);
		}
		return count;
	}
	/**
	 * 优秀展示点赞队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleExcellentLikes() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新点赞数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysLikes sc=(SysLikes)CacheUtils.rpop(CacheUtils.EXCELLENT_LIKES_QUEUE);
		while(count<tatol&&sc!=null) {
			if (sysLikesDao.getExistsLike(sc)==0) {
				count++;//增加了一条数据
				sysLikesDao.insert(sc);
				up=map.get(sc.getForeignId());
				if (up==null) {
					map.put(sc.getForeignId(), 1);
				}else{
					map.put(sc.getForeignId(), up+1);
				}
			}
			if (count<tatol) {
				sc=(SysLikes)CacheUtils.rpop(CacheUtils.EXCELLENT_LIKES_QUEUE);
			}
		}
//		if (count>0) {//有数据需要处理
//			excellentShowDao.updateLikes(map);
//		}
		return count;
	}
	/**
	 * 名师讲堂点赞队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleCourseLikes() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新点赞数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysLikes sc=(SysLikes)CacheUtils.rpop(CacheUtils.COURSE_LIKES_QUEUE);
		while(count<tatol&&sc!=null) {
			if (sysLikesDao.getExistsLike(sc)==0) {
				count++;//增加了一条数据
				sysLikesDao.insert(sc);
				up=map.get(sc.getForeignId());
				if (up==null) {
					map.put(sc.getForeignId(), 1);
				}else{
					map.put(sc.getForeignId(), up+1);
				}
			}
			if (count<tatol) {
				sc=(SysLikes)CacheUtils.rpop(CacheUtils.COURSE_LIKES_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			courseDao.updateLikes(map);
		}
		return count;
	}




	public JSONObject saveForUserInfo(JSONObject param,HttpServletRequest request) {
		JSONObject js= new JSONObject();
		js.put("ret", "1");
		js.put("msg", "点赞成功");
		User user=UserUtils.getUser();
		String foreignId=param.getString("foreignId");
		String userid=user.getId();
		if (StringUtil.isEmpty(userid)) {
			js.put("ret", "0");
			js.put("msg", "点赞失败，请重新登录");
			return js;
		}
		if (userid.equals(foreignId)) {
			js.put("ret", "0");
			js.put("msg", "点赞失败，不能给自己点赞");
			return js;
		}
		String ip= UrlUtil.getRemoteIp(request);
		if (StringUtil.isEmpty(foreignId)) {
			js.put("ret", "0");
			js.put("msg", "发生了意外~");
			return js;
		}
		SysLikes sc=new SysLikes();
		sc.setId(IdGen.uuid());
		sc.setCreateDate(new Date());
		sc.setUserId(userid);
		sc.setForeignId(foreignId);
		sc.setToken("unknown");
		sc.setDelFlag("0");
		sc.setIp(ip);
		CacheUtils.lpush(CacheUtils.USER_LIKES_QUEUE, sc);
		return js;
	}
	public JSONObject save(JSONObject param,HttpServletRequest request) {
		JSONObject js= new JSONObject();
		js.put("ret", "1");
		js.put("msg", "点赞成功");
		User user=UserUtils.getUser();
		String foreignId=param.getString("foreignId");
		String foreignType=param.getString("foreignType");
		String token=param.getString("token");
		String userid=user.getId();
		if (StringUtil.isEmpty(userid)) {
			userid="-9999";
		}
		String ip=UrlUtil.getRemoteIp(request);
		if ("unknown".equals(ip)&&"-9999".equals(userid)&&StringUtil.isEmpty(token)) {
			js.put("ret", "0");
			js.put("msg", "发生了意外~");
			return js;
		}
		if (StringUtil.isEmpty(foreignId)||StringUtil.isEmpty(foreignType)) {
			js.put("ret", "0");
			js.put("msg", "发生了意外~");
			return js;
		}
		SysLikes sc=new SysLikes();
		sc.setId(IdGen.uuid());
		sc.setCreateDate(new Date());
		sc.setUserId(userid);
		sc.setForeignId(foreignId);
		sc.setToken(token);
		sc.setDelFlag("0");
		sc.setIp(ip);
		if ("0".equals(foreignType)) {//评论
			CacheUtils.lpush(CacheUtils.COMMENT_LIKES_QUEUE, sc);
		}else if ("1".equals(foreignType)) {//优秀展示
			CacheUtils.lpush(CacheUtils.EXCELLENT_LIKES_QUEUE, sc);
		}else if ("2".equals(foreignType)) {//名师讲堂
			CacheUtils.lpush(CacheUtils.COURSE_LIKES_QUEUE, sc);
		}
		return js;
	}
	public SysLikes get(String id) {
		return super.get(id);
	}

	public List<SysLikes> findList(SysLikes sysLikes) {
		return super.findList(sysLikes);
	}

	public Page<SysLikes> findPage(Page<SysLikes> page, SysLikes sysLikes) {
		return super.findPage(page, sysLikes);
	}

	@Transactional(readOnly = false)
	public void save(SysLikes sysLikes) {
		super.save(sysLikes);
	}

	@Transactional(readOnly = false)
	public void delete(SysLikes sysLikes) {
		super.delete(sysLikes);
	}

    public int getExistsLike(SysLikes sc) {
        return dao.getExistsLike(sc);
    }
}