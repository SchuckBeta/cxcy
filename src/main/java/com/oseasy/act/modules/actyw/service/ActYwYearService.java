package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwYearDao;
import com.oseasy.act.modules.actyw.entity.ActYwYear;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程年份表Service.
 * @author zy
 * @version 2018-03-21
 */
@Service
@Transactional(readOnly = true)
public class ActYwYearService extends CrudService<ActYwYearDao, ActYwYear> {

	public ActYwYear get(String id) {
		return super.get(id);
	}

	public List<ActYwYear> findList(ActYwYear actYwYear) {
		return super.findList(actYwYear);
	}

	public Page<ActYwYear> findPage(Page<ActYwYear> page, ActYwYear actYwYear) {
		return super.findPage(page, actYwYear);
	}

	@Transactional(readOnly = false)
	public void save(ActYwYear actYwYear) {
		super.save(actYwYear);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwYear actYwYear) {
		super.delete(actYwYear);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwYear actYwYear) {
  	  dao.deleteWL(actYwYear);
  	}

	public String getProByActywIdAndYear(String actywId, String year) {
		ActYwYear ayy= dao.getByActywIdAndYear(actywId,year);
		if(ayy!=null){
			return ayy.getId();
		}else{
			return null;
		}
	}

	public ActYwYear getByActywIdAndYear(String actywId, String year) {
		return dao.getByActywIdAndYear(actywId,year);
	}
	public List<ActYwYear> findListByActywId(String actywId) {
		List<ActYwYear> list= dao.findListByActywId(actywId);
		return list;
	}

	public boolean isOverActywId(ActYwYear actYwYear) {
		boolean isOver=false;
		List<ActYwYear> overList=dao.findOverYear(actYwYear);
		if(StringUtil.checkNotEmpty(overList)){
			isOver=true;
		}
		return isOver;
	}

	public ActYwYear isOverActYear(ActYwYear actYwYear) {
		List<ActYwYear> overList=dao.findOverYear(actYwYear);
		if(StringUtil.checkNotEmpty(overList)){
			return overList.get(0);
		}
		return null;
	}

	public int checkYearTimeByActYw(String actywId) {
		return  dao.checkYearTimeByActYw(actywId);
	}

	public int checkYearByActYw(String actywId) {
		return  dao.checkYearByActYw(actywId);
	}
}