package com.oseasy.scr.modules.scr.service;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.TreeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.scr.modules.scr.dao.ScoRapplyCertDao;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyCert;

/**
 * 学分申请认定Service.
 * @author liangjie
 * @version 2019-01-08
 */
@Service
@Transactional(readOnly = true)
public class ScoRapplyCertService extends TreeService<ScoRapplyCertDao, ScoRapplyCert> {
	@Autowired
	private SysAttachmentService sysAttachmentService;

	public ScoRapplyCert get(String id) {
		return super.get(id);
	}

	public List<ScoRapplyCert> findList(ScoRapplyCert entity) {
		return super.findList(entity);
	}

	public List<ScoRapplyCert> findTreeList(ScoRapplyCert entity){
		List<ScoRapplyCert> list = dao.findAllList(entity);
		for(ScoRapplyCert scoRapplyCert : list){
			//获取附件
			SysAttachment sysAttachment=new SysAttachment();
			sysAttachment.setUid(scoRapplyCert.getId());
			List<SysAttachment> sysAttachmentList=sysAttachmentService.getFiles(sysAttachment);
			scoRapplyCert.setSysAttachmentList(sysAttachmentList);
		}
		return buildTree(list);


	}

	public List<ScoRapplyCert> buildTree(List<ScoRapplyCert> scoRapplyCertList){
		List<ScoRapplyCert> list = Lists.newArrayList();
		for(ScoRapplyCert scoRapplyCert : scoRapplyCertList){
			if(Const.NO.equals(scoRapplyCert.getParent().getId())){
				list.add(scoRapplyCert);
			}
			for(ScoRapplyCert childScoRapply : scoRapplyCertList){
				if(childScoRapply.getParent().getId().equals(scoRapplyCert.getId())){
					if(scoRapplyCert.getChildren() == null){
						scoRapplyCert.setChildren(new ArrayList<ScoRapplyCert>());
					}
					scoRapplyCert.getChildren().add(childScoRapply);
				}
			}
		}
		return list;
	}

	public Page<ScoRapplyCert> findPage(Page<ScoRapplyCert> page, ScoRapplyCert entity) {
		return super.findPage(page, entity);
	}

    public List<ScoRapplyCert> findCertByUserList(ScoRapply scoRapply){
	    return dao.findCertByUserList(scoRapply);
    }


	@Transactional(readOnly = false)
	public void saveScoRapplyCertSysAttachment(ScoRapplyCert scoRapplyCert){
		sysAttachmentService.deleteByUid(scoRapplyCert.getId());
		//保存成果物附件
		sysAttachmentService.saveBySysAttachmentVo(scoRapplyCert.getSysAttachmentList(), scoRapplyCert.getId(), FileTypeEnum.S17, FileStepEnum.S1701);
	}

	@Transactional(readOnly = false)
	public void save(ScoRapplyCert entity) {
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRapplyCert> entitys) {
        dao.insertPL(entitys);
    }

	@Transactional(readOnly = false)
	public void updateSort(ScoRapplyCert entity){
		dao.updateSort(entity);
	}

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRapplyCert> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRapplyCert entity) {
		super.delete(entity);
	}
	@Transactional(readOnly = false)
	public void deleteChildren(ScoRapplyCert entity){
		dao.deleteChildren(entity);
	}
	@Transactional(readOnly = false)
	public void deletePL(ScoRapplyCert entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRapplyCert entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRapplyCert entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

  	@Transactional(readOnly = false)
	public Integer checkScoRaCertName(ScoRapplyCert scoRapplyCert){
  		String id =  scoRapplyCert.getId();
		String name = scoRapplyCert.getName();
  		return dao.checkScoRaCertName(id, name);
  	}
}