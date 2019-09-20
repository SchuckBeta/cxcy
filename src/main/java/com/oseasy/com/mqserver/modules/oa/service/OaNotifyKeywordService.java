package com.oseasy.com.mqserver.modules.oa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyKeywordDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyKeyword;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 通知通告关键词Service.
 * @author 9527
 * @version 2017-07-11
 */
@Service
@Transactional(readOnly = true)
public class OaNotifyKeywordService extends CrudService<OaNotifyKeywordDao, OaNotifyKeyword> {
	@Autowired
	private OaNotifyKeywordDao oaNotifyKeywordDao;

	public List<String> findListByEsid(String esid) {
		return oaNotifyKeywordDao.findListByEsid(esid);
	}
	public OaNotifyKeyword get(String id) {
		return super.get(id);
	}

	public List<OaNotifyKeyword> findList(OaNotifyKeyword oaNotifyKeyword) {
		return super.findList(oaNotifyKeyword);
	}

	public Page<OaNotifyKeyword> findPage(Page<OaNotifyKeyword> page, OaNotifyKeyword oaNotifyKeyword) {
		return super.findPage(page, oaNotifyKeyword);
	}

	@Transactional(readOnly = false)
	public void save(OaNotifyKeyword oaNotifyKeyword) {
		super.save(oaNotifyKeyword);
	}

	@Transactional(readOnly = false)
	public void delete(OaNotifyKeyword oaNotifyKeyword) {
		super.delete(oaNotifyKeyword);
	}

}