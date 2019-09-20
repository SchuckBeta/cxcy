package com.oseasy.pro.modules.gcontest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.gcontest.dao.GcontestHotsKeywordDao;
import com.oseasy.pro.modules.gcontest.entity.GcontestHotsKeyword;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 大赛热点关键字Service.
 * @author 9527
 * @version 2017-07-12
 */
@Service
@Transactional(readOnly = true)
public class GcontestHotsKeywordService extends CrudService<GcontestHotsKeywordDao, GcontestHotsKeyword> {
	@Autowired
	private GcontestHotsKeywordDao gcontestHotsKeywordDao;
	public List<String> findListByEsid(String esid) {
		return gcontestHotsKeywordDao.findListByEsid(esid);
	}
	public GcontestHotsKeyword get(String id) {
		return super.get(id);
	}

	public List<GcontestHotsKeyword> findList(GcontestHotsKeyword gcontestHotsKeyword) {
		return super.findList(gcontestHotsKeyword);
	}

	public Page<GcontestHotsKeyword> findPage(Page<GcontestHotsKeyword> page, GcontestHotsKeyword gcontestHotsKeyword) {
		return super.findPage(page, gcontestHotsKeyword);
	}

	@Transactional(readOnly = false)
	public void save(GcontestHotsKeyword gcontestHotsKeyword) {
		super.save(gcontestHotsKeyword);
	}

	@Transactional(readOnly = false)
	public void delete(GcontestHotsKeyword gcontestHotsKeyword) {
		super.delete(gcontestHotsKeyword);
	}

}