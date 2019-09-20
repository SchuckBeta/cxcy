package com.oseasy.pro.modules.excellent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.excellent.dao.ExcellentKeywordDao;
import com.oseasy.pro.modules.excellent.entity.ExcellentKeyword;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 优秀展示关键词Service.
 * @author 9527
 * @version 2017-06-23
 */
@Service
@Transactional(readOnly = true)
public class ExcellentKeywordService extends CrudService<ExcellentKeywordDao, ExcellentKeyword> {
	@Autowired
	private ExcellentKeywordDao excellentKeywordDao;
	
	public List<String> findListByEsid(String esid) {
		return excellentKeywordDao.findListByEsid(esid);
	}

	public ExcellentKeyword get(String id) {
		return super.get(id);
	}

	public List<ExcellentKeyword> findList(ExcellentKeyword excellentKeyword) {
		return super.findList(excellentKeyword);
	}

	public Page<ExcellentKeyword> findPage(Page<ExcellentKeyword> page, ExcellentKeyword excellentKeyword) {
		return super.findPage(page, excellentKeyword);
	}

	@Transactional(readOnly = false)
	public void save(ExcellentKeyword excellentKeyword) {
		super.save(excellentKeyword);
	}

	@Transactional(readOnly = false)
	public void delete(ExcellentKeyword excellentKeyword) {
		super.delete(excellentKeyword);
	}

}