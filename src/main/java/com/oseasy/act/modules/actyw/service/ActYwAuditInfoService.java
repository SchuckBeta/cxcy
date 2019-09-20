package com.oseasy.act.modules.actyw.service;

import com.oseasy.act.modules.actyw.dao.ActYwAuditInfoDao;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.FloatUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 自定义审核信息Service.
 * @author zy
 * @version 2017-11-01
 */
@Service
@Transactional(readOnly = true)
public class ActYwAuditInfoService extends CrudService<ActYwAuditInfoDao, ActYwAuditInfo> {

	public ActYwAuditInfo get(String id) {
		return super.get(id);
	}

	public List<ActYwAuditInfo> findList(ActYwAuditInfo actYwAuditInfo) {
		return super.findList(actYwAuditInfo);
	}
	public List<ActYwAuditInfo> findNoBackList(ActYwAuditInfo actYwAuditInfo) {
			return dao.findNoBackList(actYwAuditInfo);
		}
	public List<String> findSubStringList(String promodelId) {
		return dao.findSubStringList(promodelId);
	}

	public Page<ActYwAuditInfo> findPage(Page<ActYwAuditInfo> page, ActYwAuditInfo actYwAuditInfo) {
		return super.findPage(page, actYwAuditInfo);
	}

	public double getAuditAvgInfo(ActYwAuditInfo infoSerch) {
			List<ActYwAuditInfo> infos=dao.findList(infoSerch);
			double total=0;
			double average=0;
			int number=0;
			if (infos==null||infos.size()==0) {
				average=infoSerch.getScore();
			}else{
				for (ActYwAuditInfo info:infos) {
					total=total+info.getScore();
					number++;
				}
				average= FloatUtils.division(total, number);
			}
			return average;
		}

	public double getAuditAvgInfoByList(List<ActYwAuditInfo> infos) {
	    double total=0;
		double average=0;
		int number=0;
		for (ActYwAuditInfo info:infos) {
			total = total + info.getScore();
			number++;
		}
		average= FloatUtils.division(total,number);
		return average;
	}

	@Transactional(readOnly = false)
	public void save(ActYwAuditInfo actYwAuditInfo) {
		super.save(actYwAuditInfo);
	}

	@Transactional(readOnly = false)
	public void updateIsBack(String id) {
		dao.updateIsBack(id);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwAuditInfo actYwAuditInfo) {
		super.delete(actYwAuditInfo);
	}

	public ActYwAuditInfo getLastAudit(ActYwAuditInfo actYwAuditInfo) {
		return dao.getLastAudit(actYwAuditInfo);
	}

	public ActYwAuditInfo getLastAuditByPromodel(ActYwAuditInfo actYwAuditInfo) {
		return dao.getLastAuditByPromodel(actYwAuditInfo);
	}

	public List<ActYwAuditInfo> getProModelChangeGnode(ActYwAuditInfo actYwAuditInfo) {
		return dao.getProModelChangeGnode(actYwAuditInfo);
	}

	public ActYwAuditInfo getGnodeByNextGnode(ActYwAuditInfo actYwAuditInfoIn) {
		return dao.getGnodeByNextGnode(actYwAuditInfoIn);
	}

	public ActYwAuditInfo getInAudit(ActYwAuditInfo actYwAuditInfo) {
		return dao.getInAudit(actYwAuditInfo);
	}

	public List<ActYwAuditInfo> getLastAuditListByPromodel(ActYwAuditInfo actYwAuditInfo) {
		return dao.getLastAuditListByPromodel(actYwAuditInfo);
	}

	@Transactional(readOnly = false)
	public void deleteByProIdAndGnodeId(ActYwAuditInfo actYwAuditInfo) {
		dao.deleteByProIdAndGnodeId(actYwAuditInfo);
	}

	public List<ActYwAuditInfo> getProModelAuditInfo(String modelId){
		return dao.getActYwAuditRecord(modelId);
	}

	public ActYwAuditInfo getFirstAuditByPromodel(ActYwAuditInfo actYwAuditInfo) {
		return dao.getFirstAuditByPromodel(actYwAuditInfo);
	}
}