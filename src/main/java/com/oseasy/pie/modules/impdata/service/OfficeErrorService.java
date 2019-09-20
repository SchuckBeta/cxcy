package com.oseasy.pie.modules.impdata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.OfficeErrorDao;
import com.oseasy.pie.modules.impdata.entity.OfficeError;

/**
 * 机构导入错误信息表Service
 * @author 9527
 * @version 2017-05-24
 */
@Service
@Transactional(readOnly = true)
public class OfficeErrorService extends CrudService<OfficeErrorDao, OfficeError> {

	public OfficeError get(String id) {
		return super.get(id);
	}
	
	public List<OfficeError> findList(OfficeError officeError) {
		return super.findList(officeError);
	}
	
	public Page<OfficeError> findPage(Page<OfficeError> page, OfficeError officeError) {
		return super.findPage(page, officeError);
	}
	
	@Transactional(readOnly = false)
	public void save(OfficeError officeError) {
		super.save(officeError);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfficeError officeError) {
		super.delete(officeError);
	}
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	@Transactional(readOnly = false)
	public void insert(OfficeError officeError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			officeError.setUpdateBy(user);
			officeError.setCreateBy(user);
		}
		officeError.setUpdateDate(new Date());
		officeError.setCreateDate(officeError.getUpdateDate());
		dao.insert(officeError);
	}
}