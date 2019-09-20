package com.oseasy.pie.modules.impdata.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.PmgMemsErrorDao;
import com.oseasy.pie.modules.impdata.entity.PmgMemsError;

/**
 * 自定义大赛成员信息错误数据Service.
 * @author 自定义大赛成员信息错误数据
 * @version 2018-05-14
 */
@Service
@Transactional(readOnly = true)
public class PmgMemsErrorService extends CrudService<PmgMemsErrorDao, PmgMemsError> {
	public List<PmgMemsError> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	@Transactional(readOnly = false)
	public void insert(PmgMemsError projectError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			projectError.setUpdateBy(user);
			projectError.setCreateBy(user);
		}
		projectError.setUpdateDate(new Date());
		projectError.setCreateDate(projectError.getUpdateDate());
		dao.insert(projectError);
	}
	public PmgMemsError get(String id) {
		return super.get(id);
	}

	public List<PmgMemsError> findList(PmgMemsError pmgMemsError) {
		return super.findList(pmgMemsError);
	}

	public Page<PmgMemsError> findPage(Page<PmgMemsError> page, PmgMemsError pmgMemsError) {
		return super.findPage(page, pmgMemsError);
	}

	@Transactional(readOnly = false)
	public void save(PmgMemsError pmgMemsError) {
		super.save(pmgMemsError);
	}

	@Transactional(readOnly = false)
	public void delete(PmgMemsError pmgMemsError) {
		super.delete(pmgMemsError);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(PmgMemsError pmgMemsError) {
  	  dao.deleteWL(pmgMemsError);
  	}
}