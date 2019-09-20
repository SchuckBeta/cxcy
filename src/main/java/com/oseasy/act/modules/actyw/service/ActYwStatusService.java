package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.act.modules.actyw.dao.ActYwStatusDao;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 节点状态表Service.
 * @author zy
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwStatusService extends CrudService<ActYwStatusDao, ActYwStatus> {
	@Autowired
	ActYwStatusDao actYwStatusDao;

	public ActYwStatus get(String id) {
		return super.get(id);
	}

	public List<ActYwStatus> findList(ActYwStatus actYwStatus) {
		return super.findList(actYwStatus);
	}

	public Page<ActYwStatus> findPage(Page<ActYwStatus> page, ActYwStatus actYwStatus) {
		return super.findPage(page, actYwStatus);
	}



	@Transactional(readOnly = false)
	public void save(ActYwStatus actYwStatus) {
		super.save(actYwStatus);
	}

	@Transactional(readOnly = false)
	public boolean saveStatus(ActYwStatus actYwStatus) {
		boolean res=true;
		try {
			super.save(actYwStatus);
		}
		catch(Exception e){
			logger.error("保存状态出错");
			res=false;
		}
		return res;


	}


	@Transactional(readOnly = false)
	public boolean deleteStatusWithGnodeId(String statusId, String gnodeId) {
		boolean res= false;
		List<ActYwStatus> list= getInuseStateById(statusId,gnodeId);
		if(list!=null &&list.size()>0){
			res=true;
		}else{
			ActYwStatus actYwStatus=new ActYwStatus(statusId);
			super.delete(actYwStatus);
		}
		return res;
	}

	@Transactional(readOnly = false)
	public boolean deleteStatus(String statusId) {
		boolean res= false;
		List<ActYwStatus> list= getInuseState(statusId);
		if(list!=null &&list.size()>0){
			res=true;
		}else{
			ActYwStatus actYwStatus=new ActYwStatus(statusId);
			super.delete(actYwStatus);
		}
		return res;
	}
	//根据节点找到所有节点状态
	public List<ActYwStatus>  getAllStateByGnodeId(String gnodeId) {
		return dao.getAllStateByGnodeId(gnodeId);
	}

	//根据id找到使用节点状态
	public List<ActYwStatus>  getInuseStateById(String statusId,String gnodeId) {
		return dao.getInuseStateById(statusId,gnodeId);
	}


	//根据id找到使用节点状态
	public List<ActYwStatus>  getInuseState(String statusId) {
		return dao.getInuseState(statusId);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwStatus actYwStatus) {
  	  dao.deleteWL(actYwStatus);
  	}

	@Transactional(readOnly = false)
  	public void deleteWLBySgtypeId(String gtypeId) {
  	  	dao.deleteWLBySgtypeId(gtypeId);
  	}

	public boolean checkState(ActYwStatus actYwStatus) {
		boolean result=true;
		List<ActYwStatus> list=dao.checkState(actYwStatus);
		if(list!=null &&list.size()>0){
			result=false;
		}
		return result;
	}

	public int getStatusValue(String  gType) {
		ActYwStatus actYwStatus=new ActYwStatus();
		actYwStatus.setGtype(gType);
		List<ActYwStatus> list= actYwStatusDao.findList(actYwStatus);
		if(StringUtil.checkNotEmpty(list)){
			ActYwStatus lastActYwStatus =list.get(0);
			int last =Integer.parseInt(lastActYwStatus.getStatus());
			return last+1;
		}
		return 3;
	}
}