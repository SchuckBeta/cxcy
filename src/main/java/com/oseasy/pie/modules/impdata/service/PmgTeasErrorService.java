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
import com.oseasy.pie.modules.impdata.dao.PmgTeasErrorDao;
import com.oseasy.pie.modules.impdata.entity.PmgTeasError;

/**
 * 自定义大赛导师信息错误数据Service.
 * @author 自定义大赛导师信息错误数据
 * @version 2018-05-14
 */
@Service
@Transactional(readOnly = true)
public class PmgTeasErrorService extends CrudService<PmgTeasErrorDao, PmgTeasError> {
	public List<PmgTeasError> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	@Transactional(readOnly = false)
	public void insert(PmgTeasError projectError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			projectError.setUpdateBy(user);
			projectError.setCreateBy(user);
		}
		projectError.setUpdateDate(new Date());
		projectError.setCreateDate(projectError.getUpdateDate());
		dao.insert(projectError);
	}
	public PmgTeasError get(String id) {
		return super.get(id);
	}

	public List<PmgTeasError> findList(PmgTeasError pmgTeasError) {
		return super.findList(pmgTeasError);
	}

	public Page<PmgTeasError> findPage(Page<PmgTeasError> page, PmgTeasError pmgTeasError) {
		return super.findPage(page, pmgTeasError);
	}

	@Transactional(readOnly = false)
	public void save(PmgTeasError pmgTeasError) {
		super.save(pmgTeasError);
	}

	@Transactional(readOnly = false)
	public void delete(PmgTeasError pmgTeasError) {
		super.delete(pmgTeasError);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(PmgTeasError pmgTeasError) {
  	  dao.deleteWL(pmgTeasError);
  	}
}