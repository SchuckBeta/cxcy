package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwGformDao;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 节点角色Service.
 * @author chenh
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwGformService extends CrudService<ActYwGformDao, ActYwGform> {


	public ActYwGform get(String id) {
		ActYwGform actYwGform = super.get(id);
		return actYwGform;
	}

	public List<ActYwGform> findList(ActYwGform actYwGform) {
		return super.findList(actYwGform);
	}

	public Page<ActYwGform> findPage(Page<ActYwGform> page, ActYwGform actYwGform) {
		return super.findPage(page, actYwGform);
	}

	@Transactional(readOnly = false)
	public void save(ActYwGform actYwGform) {
		super.save(actYwGform);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwGform actYwGform) {
		super.delete(actYwGform);
	}

    /**
     * 批量保存.
     * @param gforms
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwGform> gforms) {
        dao.savePl(gforms);
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