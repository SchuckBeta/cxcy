package com.oseasy.act.modules.actyw.service;


import java.util.Date;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.dao.ActYwApplyDao;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.act.modules.actyw.exception.ProTimeException;
import com.oseasy.act.modules.actyw.vo.ActYwApplyVo;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程申请Service.
 *
 * @author zy
 * @version 2017-12-05
 */
@Service
@Transactional(readOnly = true)
public class ActYwApplyService extends CrudService<ActYwApplyDao, ActYwApply> {
	@Autowired
	ActTaskService actTaskService;
	@Autowired
	ActYwService actYwService;
	@Autowired
	SystemService systemService;
	@Autowired
	UserService userService;
	@Autowired
	TaskService taskService;
	@Autowired
	ActYwAuditInfoService actYwAuditInfoService;
	@Autowired
	IdentityService identityService;
	@Autowired
	ActDao actDao;
	@Autowired
	private SysAttachmentService sysAttachmentService;

	public ActYwApply get(String id) {
		return super.get(id);
	}

	public List<ActYwApply> findList(ActYwApply actYwApply) {
		return super.findList(actYwApply);
	}

	public Page<ActYwApply> findPage(Page<ActYwApply> page, ActYwApply actYwApply) {
		return super.findPage(page, actYwApply);
	}

	@Transactional(readOnly = false)
	public void save(ActYwApply actYwApply) {
		super.save(actYwApply);
	}


	@Transactional(readOnly = false)
	public void updateProcInsId(ActYwApplyVo actYwApply) {
		dao.updateProcInsId(actYwApply);
	}


	@Transactional(readOnly = false)
	public void saveApply(ActYwApply actYwApply) {
		if (actYwApply.getIsNewRecord()) {
			if ((actYwApply.getActYw() == null) || StringUtil.isEmpty(actYwApply.getActYw().getId())) {
				throw new RuntimeException("申报信息不完整");
			}
			if (StringUtil.isEmpty(actYwApply.getType())) {
				throw new RuntimeException("申报业务类型未指定");
			}
			if (StringUtil.isEmpty(actYwApply.getRelId())) {
				throw new RuntimeException("申报业务编号未指定");
			}
			if ((actYwApply.getApplyUser() == null) || StringUtil.isEmpty(actYwApply.getApplyUser().getId())) {
				actYwApply.setApplyUser(UserUtils.getUser());
			}
			if (StringUtil.isEmpty(actYwApply.getNo())) {
				actYwApply.setNo(SysNodeTool.genByKeyss(SysNoType.NO_YW_APPLY));
			}
			actYwApply.setProcInsId(null);
		}
		save(actYwApply);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwApply actYwApply) {
		super.delete(actYwApply);
	}

	//检查当前时间是否在时间范围内 true 不在有效期内
	public boolean checkValidDate(Date s, Date e) {
		if (s == null || e == null) {
		  throw new ProTimeException();
		}
		if (s.after(new Date())) {
			return true;
		}
		if (e.before(new Date())) {
			return true;
		}
		return false;
	}

	//判断是否需要重新保存附件,true 需要保存
	private boolean checkFile(String pid, AttachMentEntity a) {
		SysAttachment s = new SysAttachment();
		s.setUid(pid);
		List<SysAttachment> list = sysAttachmentService.getFiles(s);
		if (list == null || list.size() == 0 || list.size() > 1) {
			return true;
		} else {
			if (a != null && a.getFielFtpUrl() != null && a.getFielFtpUrl().size() == 1 && a.getFielFtpUrl().get(0).equals(list.get(0).getUrl())) {
				return false;
			} else {
				return true;
			}
		}

	}
}