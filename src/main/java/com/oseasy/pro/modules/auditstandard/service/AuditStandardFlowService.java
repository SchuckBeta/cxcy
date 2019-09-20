package com.oseasy.pro.modules.auditstandard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.auditstandard.dao.AuditStandardFlowDao;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardFlow;
import com.oseasy.pro.modules.auditstandard.vo.AsdYwGnode;
import com.oseasy.pro.modules.auditstandard.vo.AuditStandardVo;

/**
 * 评审标准、流程关系表Service.
 * @author 9527
 * @version 2017-07-28
 */
@Service
@Transactional(readOnly = true)
public class AuditStandardFlowService extends CrudService<AuditStandardFlowDao, AuditStandardFlow> {
	public List<AsdYwGnode> getGnodeListByYwid(String id) {
		return dao.getGnodeListByYwid(id);
	}
	public int findByCdn(String flow,String node) {
		return dao.findByCdn( flow, node);
	}
	public AuditStandardFlow get(String id) {
		return super.get(id);
	}

	public List<AuditStandardFlow> findList(AuditStandardFlow auditStandardFlow) {
		return super.findList(auditStandardFlow);
	}

	public Page<AuditStandardFlow> findPage(Page<AuditStandardFlow> page, AuditStandardFlow auditStandardFlow) {
		return super.findPage(page, auditStandardFlow);
	}

	@Transactional(readOnly = false)
	public void save(AuditStandardFlow auditStandardFlow) {
		super.save(auditStandardFlow);
	}
	@Transactional(readOnly = false)
	public void saveChild(AuditStandardVo vo) {
		dao.saveChild(vo);
	}
	@Transactional(readOnly = false)
	public void delete(AuditStandardFlow auditStandardFlow) {
		super.delete(auditStandardFlow);
	}

	public AuditStandardFlow getByDef(String defName) {
		AuditStandardFlow auditStandardFlow=dao.getByDef(defName);
		return auditStandardFlow;
	}
}