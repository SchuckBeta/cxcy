package com.oseasy.pro.modules.promodel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.promodel.dao.ProModelMdGcHistoryDao;
import com.oseasy.pro.modules.promodel.entity.ProModelMdGcHistory;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 互联网+大赛记录表Service.
 * @author zy
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = true)
public class ProModelMdGcHistoryService extends CrudService<ProModelMdGcHistoryDao, ProModelMdGcHistory> {

	public ProModelMdGcHistory get(String id) {
		return super.get(id);
	}

	public List<ProModelMdGcHistory> findList(ProModelMdGcHistory proModelMdGcHistory) {
		return super.findList(proModelMdGcHistory);
	}

	public Page<ProModelMdGcHistory> findPage(Page<ProModelMdGcHistory> page, ProModelMdGcHistory proModelMdGcHistory) {
		return super.findPage(page, proModelMdGcHistory);
	}

	@Transactional(readOnly = false)
	public void save(ProModelMdGcHistory proModelMdGcHistory) {
		super.save(proModelMdGcHistory);
	}

	@Transactional(readOnly = false)
	public void delete(ProModelMdGcHistory proModelMdGcHistory) {
		super.delete(proModelMdGcHistory);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ProModelMdGcHistory proModelMdGcHistory) {
  	  dao.deleteWL(proModelMdGcHistory);
  	}

	public ProModelMdGcHistory getProModelMdGcHistory(ProModelMdGcHistory proModelMdGcHistory) {
		return dao.getProModelMdGcHistory(proModelMdGcHistory);
	}

	public List<ProModelMdGcHistory> getProModelMdGcHistoryList(ProModelMdGcHistory proModelMdGcHistory) {
		return dao.getProModelMdGcHistoryList(proModelMdGcHistory);
	}

	public void updateAward(String id, String result) {
		dao.updateAward(id,result);
	}
}