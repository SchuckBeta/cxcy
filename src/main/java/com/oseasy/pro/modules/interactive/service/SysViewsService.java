package com.oseasy.pro.modules.interactive.service;

import java.util.*;

import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.course.dao.CourseDao;
import com.oseasy.pro.modules.interactive.dao.SysViewsDao;
import com.oseasy.pro.modules.interactive.entity.SysViews;

import com.oseasy.util.common.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * 浏览表Service.
 * @author 9527
 * @version 2017-06-30
 */
@Service
@Transactional(readOnly = true)
public class SysViewsService extends CrudService<SysViewsDao, SysViews> {
//	@Autowired
//	private ExcellentShowDao excellentShowDao;
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private UserDao userDao;
	public List<Map<String,String>> getBrowse(String uid) {
		return dao.getBrowse(uid);
	}
	public List<Map<String,String>> getVisitors(String uid) {
		return dao.getVisitors(uid);
	}
	/**
	 * 评导师、学生队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleUserInfoViews() {
		List<SysViews> list=new ArrayList<SysViews>();//需要保存的list
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新浏览数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysViews sc=(SysViews)CacheUtils.rpop(CacheUtils.USER_VIEWS_QUEUE);
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
				sc=(SysViews)CacheUtils.rpop(CacheUtils.USER_VIEWS_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			dao.insertBatch(list);
			userDao.updateViews(map);
			for(String userid:map.keySet()) {
				CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ +userid);
			}
		}
		return count;
	}
	/**
	 * 名师讲堂浏览量队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleCourseViews() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新浏览量数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysViews sv=(SysViews)CacheUtils.rpop(CacheUtils.COURSE_VIEWS_QUEUE);
		while(count<tatol&&sv!=null) {
			count++;//增加了一条数据
			up=map.get(sv.getForeignId());
			if (up==null) {
				map.put(sv.getForeignId(), 1);
			}else{
				map.put(sv.getForeignId(), up+1);
			}
			if (count<tatol) {
				sv=(SysViews)CacheUtils.rpop(CacheUtils.COURSE_VIEWS_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			courseDao.updateViewsPlus(map);
		}
		return count;
	}
	/**
	 * 优秀展示浏览量队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleExcellentViews() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新浏览量数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysViews sv=(SysViews)CacheUtils.rpop(CacheUtils.EXCELLENT_VIEWS_QUEUE);
		while(count<tatol&&sv!=null) {
			count++;//增加了一条数据
			up=map.get(sv.getForeignId());
			if (up==null) {
				map.put(sv.getForeignId(), 1);
			}else{
				map.put(sv.getForeignId(), up+1);
			}
			if (count<tatol) {
				sv=(SysViews)CacheUtils.rpop(CacheUtils.EXCELLENT_VIEWS_QUEUE);
			}
		}
//		if (count>0) {//有数据需要处理
//			excellentShowDao.updateViews(map);
//		}
		return count;
	}
	public SysViews get(String id) {
		return super.get(id);
	}

	public List<SysViews> findList(SysViews sysViews) {
		return super.findList(sysViews);
	}

	public Page<SysViews> findPage(Page<SysViews> page, SysViews sysViews) {
		return super.findPage(page, sysViews);
	}

	@Transactional(readOnly = false)
	public void save(SysViews sysViews) {
		super.save(sysViews);
	}

	@Transactional(readOnly = false)
	public void delete(SysViews sysViews) {
		super.delete(sysViews);
	}

	public Boolean getIsLike(String userId, String foreignId) {
		SysViews sysViews=new SysViews();
		sysViews.setUserId(userId);
		sysViews.setForeignId(foreignId);
		List<SysViews> list=findList(sysViews);
		return (list.size()>0)?true:false;
	}
	public static void updateViews(String foreignId, HttpServletRequest request, String queue) {
		User user= UserUtils.getUser();
		SysViews sc=new SysViews();
		sc.setId(IdGen.uuid());
		sc.setCreateDate(new Date());
		sc.setUserId(user.getId());
		sc.setForeignId(foreignId);
		sc.setDelFlag("0");
		sc.setIp(UrlUtil.getRemoteIp(request));
		CacheUtils.lpush(queue, sc);
	}
}