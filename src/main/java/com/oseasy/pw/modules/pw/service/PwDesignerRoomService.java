package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwDesignerRoomDao;
import com.oseasy.pw.modules.pw.entity.PwDesignerRoom;

/**
 * 房间设计表Service.
 * @author zy
 * @version 2017-12-18
 */
@Service
@Transactional(readOnly = true)
public class PwDesignerRoomService extends CrudService<PwDesignerRoomDao, PwDesignerRoom> {

	public PwDesignerRoom get(String id) {
		return super.get(id);
	}

	public List<PwDesignerRoom> findList(PwDesignerRoom pwDesignerRoom) {
		return super.findList(pwDesignerRoom);
	}


	public List<PwDesignerRoom> findListByCid(String cid) {
		return dao.findListByCid(cid);
	}

	public Page<PwDesignerRoom> findPage(Page<PwDesignerRoom> page, PwDesignerRoom pwDesignerRoom) {
		return super.findPage(page, pwDesignerRoom);
	}

	@Transactional(readOnly = false)
	public void save(PwDesignerRoom pwDesignerRoom) {
		super.save(pwDesignerRoom);
	}

	@Transactional(readOnly = false)
	public void delete(PwDesignerRoom pwDesignerRoom) {
		super.delete(pwDesignerRoom);
	}

	public void deleteAllByCid(String CId) {
		dao.deleteAllByCid(CId);
	}
}