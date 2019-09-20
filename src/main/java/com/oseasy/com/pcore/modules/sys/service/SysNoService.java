package com.oseasy.com.pcore.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.SysNoDao;
import com.oseasy.com.pcore.modules.sys.entity.SysNo;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统全局编号Service.
 * @author chenhao
 * @version 2017-07-17
 */
@Service
@Transactional(readOnly = true)
public class SysNoService extends CrudService<SysNoDao, SysNo> {
  @Autowired
  private SysNoDao sysNoDao;

	public SysNo get(String id) {
		return super.get(id);
	}

	public SysNo getByKeyss(SysNoType key) {
	  SysNo sysNo = sysNoDao.getByKeyss(key.getKey());
    if (sysNo == null) {
      logger.warn("SysNoType 定义的编号类型在数据库不存在！");
    }
	  return sysNo;
	}

	public List<SysNo> findList(SysNo sysNo) {
		return super.findList(sysNo);
	}

	public Page<SysNo> findPage(Page<SysNo> page, SysNo sysNo) {
		return super.findPage(page, sysNo);
	}

	@Transactional(readOnly = false)
	public void save(SysNo sysNo) {
	  if (sysNo.getIsNewRecord() && (sysNo.getSysmaxVal() == null)) {
	    sysNo.setSysmaxVal(new Long(0));
	  }
		super.save(sysNo);
	}

	@Transactional(readOnly = false)
	public void delete(SysNo sysNo) {
		super.delete(sysNo);
	}

  /**
   * 根据ID获取全局序号最大值(不加1)
   * @param entity 全局编号
   * @return
   */
  @Transactional(readOnly = false)
  public synchronized SysNo getSysMaxNo(SysNo entity) {
    return getSysMaxNo(false, entity);
  }

  /**
   * 根据ID获取全局序号最大值(加1)
   * @param entity 全局编号
   * @return
   */
  @Transactional(readOnly = false)
  public synchronized SysNo getSysMaxNoAddOne(SysNo entity) {
    return getSysMaxNo(true, entity);
  }

  /**
   * 根据ID获取全局序号最大值
   * @param isAddOne 是否加1
   * @param entity 全局编号
   * @return
   */
  @Transactional(readOnly = false)
  public synchronized SysNo getSysMaxNo(Boolean isAddOne, SysNo entity) {
    if ((entity == null) || (StringUtil.isEmpty(entity.getId()))) {
      return null;
    }

    entity = get(entity.getId());
    if (entity == null) {
      return null;
    }

    if (isAddOne) {
      entity.setSysmaxVal(entity.getSysmaxVal() + 1);
      save(entity);
    }
    return entity;
  }

  /**
   * 根据SysNoType生成编号.
   * @param sysNoType 编号类型
   * @return String
   */
  @Transactional(readOnly = false)
  public SysNo genByKeyss(SysNoType sysNoType) {
    SysNo entity = sysNoDao.getByKeyss(sysNoType.getKey());
    if (entity != null) {
      entity.setSysmaxVal(entity.getSysmaxVal() + 1);
      sysNoDao.update(entity);
      return entity;
    }
    logger.warn("SNOKeyss 流程标识数据不存在，请初始化到数据库："+sysNoType.getKey());
    return null;
  }
}