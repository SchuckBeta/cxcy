package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwGuserDao;
import com.oseasy.act.modules.actyw.entity.ActYwGuser;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 节点用户Service.
 * @author chenh
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwGuserService extends CrudService<ActYwGuserDao, ActYwGuser> {


	public ActYwGuser get(String id) {
		ActYwGuser actYwGuser = super.get(id);
		return actYwGuser;
	}

	public List<ActYwGuser> findList(ActYwGuser actYwGuser) {
		return super.findList(actYwGuser);
	}

	public Page<ActYwGuser> findPage(Page<ActYwGuser> page, ActYwGuser actYwGuser) {
		return super.findPage(page, actYwGuser);
	}

	@Transactional(readOnly = false)
	public void save(ActYwGuser actYwGuser) {
		super.save(actYwGuser);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwGuser actYwGuser) {
		super.delete(actYwGuser);
	}

    /**
     * 批量保存用户.
     * @param gusers
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwGuser> gusers) {
        dao.savePl(gusers);
    }

    /**
     * 根据流程批量删除.
     * @param groupId 流程ID
     */
    @Transactional(readOnly = false)
    public void deletePlwlByGroup(String groupId) {
        dao.deletePlwlByGroup(groupId);
    }

    /**
     * 根据流程批量删除.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     */
    @Transactional(readOnly = false)
    public void deletePlwl(String groupId, String gnodeId) {
        dao.deletePlwl(groupId, gnodeId);
    }

}