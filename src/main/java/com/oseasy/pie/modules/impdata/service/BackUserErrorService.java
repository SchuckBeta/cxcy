package com.oseasy.pie.modules.impdata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.BackUserErrorDao;
import com.oseasy.pie.modules.impdata.entity.BackUserError;

/**
 * 后台用户导入Service
 * @author 9527
 * @version 2017-05-23
 */
@Service
@Transactional(readOnly = true)
public class BackUserErrorService extends CrudService<BackUserErrorDao, BackUserError> {
	@Autowired
	private UserDao userDao;
	@Autowired
	private SystemService systemService;
	public BackUserError get(String id) {
		return super.get(id);
	}

	public List<BackUserError> findList(BackUserError backUserError) {
		return super.findList(backUserError);
	}

	public Page<BackUserError> findPage(Page<BackUserError> page, BackUserError backUserError) {
		return super.findPage(page, backUserError);
	}

	@Transactional(readOnly = false)
	public void save(BackUserError backUserError) {
		super.save(backUserError);
	}

	@Transactional(readOnly = false)
	public void delete(BackUserError backUserError) {
		super.delete(backUserError);
	}
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	@Transactional(readOnly = false)
	public void insert(BackUserError backUserError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			backUserError.setUpdateBy(user);
			backUserError.setCreateBy(user);
		}
		backUserError.setUpdateDate(new Date());
		backUserError.setCreateDate(backUserError.getUpdateDate());
		dao.insert(backUserError);
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveBackUser(User nuser) {
		User user = UserUtils.getUser();
		nuser.setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
		nuser.setId(IdGen.uuid());
		if (StringUtils.isNotBlank(user.getId())) {
			nuser.setUpdateBy(user);
			nuser.setCreateBy(user);
		}
		nuser.setSource("1");
		nuser.setUpdateDate(new Date());
		nuser.setCreateDate(nuser.getUpdateDate());
		systemService.insert(nuser);
		userDao.insertUserRole(nuser);
	}
}