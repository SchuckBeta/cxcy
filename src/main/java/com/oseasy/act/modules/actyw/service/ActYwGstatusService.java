package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwGstatusDao;
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 节点状态中间表Service.
 * @author zy
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwGstatusService extends CrudService<ActYwGstatusDao, ActYwGstatus> {

	public ActYwGstatus get(String id) {
		return super.get(id);
	}

	public List<ActYwGstatus> findList(ActYwGstatus actYwGstatus) {
		return super.findList(actYwGstatus);
	}

	public Page<ActYwGstatus> findPage(Page<ActYwGstatus> page, ActYwGstatus actYwGstatus) {
		return super.findPage(page, actYwGstatus);
	}



	@Transactional(readOnly = false)
		public void save(ActYwGstatus actYwGstatus) {
			super.save(actYwGstatus);
		}

	//批量保存节点数据
	@Transactional(readOnly = false)
	public boolean saveAll(List<ActYwGstatus> actYwGstatusList) {
		boolean res=true;
		try{
			dao.saveAll(actYwGstatusList);
		}catch (Exception e){
			res=false;
		}
		return res;
	}

	//根据流程和节点删除 节点状态
	@Transactional(readOnly = false)
	public boolean deleteByGroupIdAndGnodeId(String groupId,String gnodeId) {
		boolean res=true;
		try{
			dao.deleteByGroupIdAndGnodeId(groupId,gnodeId);
		}catch (Exception e){
			res=false;
		}
		return res;
	}

	@Transactional(readOnly = false)
	public void delete(ActYwGstatus actYwGstatus) {
		super.delete(actYwGstatus);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwGstatus actYwGstatus) {
  	  dao.deleteWL(actYwGstatus);
  	}

    /**
     * 批量保存.
     * @param gstatus
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwGstatus> gstatus) {
        dao.savePl(gstatus);
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