package com.oseasy.cms.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.cms.modules.cms.dao.CmsConmmentDao;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.DateUtil;

/**
 * 评论Service.
 * @author chenh
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class CmsConmmentService extends CrudService<CmsConmmentDao, CmsConmment> {
	@Autowired
	private OaNotifyService oaNotifyService;

	public CmsConmment get(String id) {
		return super.get(id);
	}

	public List<CmsConmment> findList(CmsConmment cmsConmment) {
		return super.findList(cmsConmment);
	}

	public Page<CmsConmment> findPage(Page<CmsConmment> page, CmsConmment cmsConmment) {
		return super.findPage(page, cmsConmment);
	}

	@Transactional(readOnly = false)
	public void save(CmsConmment cmsConmment) {
	    if(cmsConmment.getIsNewRecord()){
	        if(cmsConmment.getUser() == null){
	            cmsConmment.setUser(UserUtils.getUser());
	        }

	        if(cmsConmment.getLikes() == null){
	            cmsConmment.setLikes(0);
	        }
	        if(cmsConmment.getIsRecommend() == null){
	            cmsConmment.setIsRecommend(Const.NO);
	        }
	        if(cmsConmment.getAuditstatus() == null){
	            cmsConmment.setAuditstatus(Const.NO);
	        }
	    }
		super.save(cmsConmment);
	}

    @Transactional(readOnly = false)
    public void updatePL(List<CmsConmment> entitys) {
        dao.updatePL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePLRecommend(List<CmsConmment> entitys) {
        dao.updatePLRecommend(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePLAudit(List<CmsConmment> entitys) {
        dao.updatePLAudit(entitys);
    }

	@Transactional(readOnly = false)
	public void updatePLAuditByCntIds(List<CmsConmment> entitys) {
	    List<CmsConmment> curentitys = Lists.newArrayList();
        for (CmsConmment cmsConmment : entitys) {
            if(cmsConmment.getAuUser() == null){
                cmsConmment.setAuUser(UserUtils.getUser());
            }
            if(cmsConmment.getAuditDate() == null){
                cmsConmment.setAuditDate(DateUtil.newDate());
            }
            curentitys.add(cmsConmment);
//			//评论审核发送消息
//			CmsConmment oldCmsConmment=get(cmsConmment.getId());
//			if(oldCmsConmment.getUser()!=null){
//				if(cmsConmment.getAuditstatus().equals(ApiConst.AUDITSTATUS_SUCCESS_STR)){
//					  oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), oldCmsConmment.getUser(), "评论管理",
//						  "你的评论被管理员审核通过了", OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
//				}else if (cmsConmment.getAuditstatus().equals(ApiConst.AUDITSTATUS_FAIL_STR)){
//					  oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), oldCmsConmment.getUser(), "评论管理",
//						  "你的评论被管理员审核拒绝了",OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
//				}
//			}
        }
		dao.updatePLAuditByCntIds(entitys);
	}

	@Transactional(readOnly = false)
	public void delete(CmsConmment cmsConmment) {
		super.delete(cmsConmment);
	}

	@Transactional(readOnly = false)
	public void deletePL(CmsConmment cmsConmment) {
	    dao.deletePL(cmsConmment);
	}

	@Transactional(readOnly = false)
	public void deletePLByCntIds(List<String> ids) {
	    dao.deletePLByCntIds(ids);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsConmment cmsConmment) {
  	  dao.deleteWL(cmsConmment);
  	}

	@Transactional(readOnly = false)
  	public void deleteWLPL(CmsConmment cmsConmment) {
  	    dao.deleteWLPL(cmsConmment);
  	}

	@Transactional(readOnly = false)
  	public void deleteWLPLByCntIds(List<String> ids) {
  	    dao.deleteWLPLByCntIds(ids);
  	}

	public Integer getArticleCommentCounts(CmsConmment cmsConmment){
		return dao.getArticleCommentCounts(cmsConmment);
	}

}