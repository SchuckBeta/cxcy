package com.oseasy.pw.modules.pw.service;

import java.util.ArrayList;
import java.util.List;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.pw.modules.pw.dao.PwProjectDao;
import com.oseasy.pw.modules.pw.entity.PwProject;

/**
 * pwService.
 * @author zy
 * @version 2018-11-20
 */
@Service
@Transactional(readOnly = true)
public class PwProjectService extends CrudService<PwProjectDao, PwProject> {
	@Autowired
	SysAttachmentService sysAttachmentService;

	public PwProject get(String id) {
		return super.get(id);
	}

	public List<PwProject> findList(PwProject entity) {
		return super.findList(entity);
	}

	public Page<PwProject> findPage(Page<PwProject> page, PwProject entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(PwProject entity) {
		if(entity.getIsNewRecord()){
		    if(StringUtil.isEmpty(entity.getStype())){
		        entity.setStype("0");
		    }
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<PwProject> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<PwProject> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(PwProject entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(PwProject entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(PwProject entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(PwProject entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	public List<PwProject> getPwProjectListByEid(String eid) {
		List<PwProject> pwProjectList=dao.getPwProjectListByEid(eid);
		if(StringUtil.checkNotEmpty(pwProjectList)){
			//添加附件
			for(PwProject pwProject:pwProjectList){
				SysAttachment sa=new SysAttachment();
				sa.setUid(pwProject.getId());
				sa.setType(FileTypeEnum.S11);
				List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
				if(StringUtil.checkNotEmpty(fileListMap)){
					pwProject.setFileInfo(fileListMap);
				}
			}
		}
		return pwProjectList;
	}

	public List<PwProject> findListByPwProject(PwProject pwProject) {
		return dao.findListByPwProject(pwProject);
	}
}