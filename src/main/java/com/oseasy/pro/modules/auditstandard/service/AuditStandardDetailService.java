package com.oseasy.pro.modules.auditstandard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.auditstandard.dao.AuditStandardDetailDao;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardDetail;
import com.oseasy.pro.modules.project.vo.ProjectStandardDetailVo;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 评审标准详情Service.
 * @author 9527
 * @version 2017-07-28
 */
@Service
@Transactional(readOnly = true)
public class AuditStandardDetailService extends CrudService<AuditStandardDetailDao, AuditStandardDetail> {
	public List<AuditStandardDetail> findByFid(String fid) {
		return dao.findByFid(fid);
	}
	public void delByFid(String fid) {
		dao.delByFid(fid);
	}
	public AuditStandardDetail get(String id) {
		return super.get(id);
	}

	public List<AuditStandardDetail> findList(AuditStandardDetail auditStandardDetail) {
		return super.findList(auditStandardDetail);
	}

	public Page<AuditStandardDetail> findPage(Page<AuditStandardDetail> page, AuditStandardDetail auditStandardDetail) {
		return super.findPage(page, auditStandardDetail);
	}

	@Transactional(readOnly = false)
	public void save(AuditStandardDetail auditStandardDetail) {
		super.save(auditStandardDetail);
	}

	public List<ProjectStandardDetailVo> findStandardDetailByNode(String node,String flow) {
		return dao.findStandardDetailByNode(node,flow);
	}

	@Transactional(readOnly = false)
	public void delete(AuditStandardDetail auditStandardDetail) {
		super.delete(auditStandardDetail);
	}

}