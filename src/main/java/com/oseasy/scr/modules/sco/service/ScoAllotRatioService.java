package com.oseasy.scr.modules.sco.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.scr.modules.sco.dao.ScoAllotRatioDao;
import com.oseasy.scr.modules.sco.entity.ScoAllotRatio;
import com.oseasy.scr.modules.sco.vo.ScoRatioVo;

/**
 * 学分分配比例Service.
 * @author 9527
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoAllotRatioService extends CrudService<ScoAllotRatioDao, ScoAllotRatio> {
	public int checkNumber(String id,String number,String confid) {
		return dao.checkNumber(id,number,confid);
	}
	public List<ScoAllotRatio> findAll(String confId) {
		return dao.findAll(confId);
	}
	public ScoAllotRatio get(String id) {
		return super.get(id);
	}

	public List<ScoAllotRatio> findList(ScoAllotRatio scoAllotRatio) {
		return super.findList(scoAllotRatio);
	}

	public Page<ScoAllotRatio> findPage(Page<ScoAllotRatio> page, ScoAllotRatio scoAllotRatio) {
		return super.findPage(page, scoAllotRatio);
	}

	@Transactional(readOnly = false)
	public void save(ScoAllotRatio scoAllotRatio) {
		super.save(scoAllotRatio);
	}

	@Transactional(readOnly = false)
	public void delete(ScoAllotRatio scoAllotRatio) {
		super.delete(scoAllotRatio);
	}

	public ScoRatioVo findRatio(ScoRatioVo scoRatioVo) {
		return dao.findRatio(scoRatioVo);
	}

}