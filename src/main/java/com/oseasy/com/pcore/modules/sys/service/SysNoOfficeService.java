package com.oseasy.com.pcore.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.SysNoDao;
import com.oseasy.com.pcore.modules.sys.dao.SysNoOfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.SysNo;
import com.oseasy.com.pcore.modules.sys.entity.SysNoOffice;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;

/**
 * 系统机构编号Service.
 * @author chenhao
 * @version 2017-07-17
 */
@Service
@Transactional(readOnly = true)
public class SysNoOfficeService extends CrudService<SysNoOfficeDao, SysNoOffice> {
  @Autowired
  private SysNoOfficeDao sysNoOfficeDao;

	public SysNoOffice get(String id) {
		return super.get(id);
	}

	public List<SysNoOffice> findList(SysNoOffice sysNoOffice) {
		return super.findList(sysNoOffice);
	}

	public Page<SysNoOffice> findPage(Page<SysNoOffice> page, SysNoOffice sysNoOffice) {
		return super.findPage(page, sysNoOffice);
	}

	@Transactional(readOnly = false)
	public void save(SysNoOffice sysNoOffice) {
	  if (sysNoOffice.getIsNewRecord() && (sysNoOffice.getMaxVal() == null)) {
      sysNoOffice.setMaxVal(new Long(0));
    }
		super.save(sysNoOffice);
	}

	@Transactional(readOnly = false)
	public void delete(SysNoOffice sysNoOffice) {
		super.delete(sysNoOffice);
	}

  /**
   * 获取机构序号最大值(不加1)
   * @param entity 机构编号
   * @param officeId
   * @return
   */
  @Transactional(readOnly = false)
  public synchronized SysNoOffice getOfficeMaxNo(SysNoOffice entity) {
    return getOfficeMaxNo(false, entity);
  }

  /**
   * 获取机构序号最大值(加1)
   * @param entity 机构编号
   * @return
   */
  @Transactional(readOnly = false)
  public synchronized SysNoOffice getOfficeMaxNoAddOne(SysNoOffice entity) {
    return getOfficeMaxNo(true, entity);
  }

  /**
   * 获取机构序号最大值
   * @param isAddOne 是否加1
   * @param entity 机构编号
   * @return
   */
  @Transactional(readOnly = false)
  public synchronized SysNoOffice getOfficeMaxNo(Boolean isAddOne, SysNoOffice entity) {
    if ((entity == null) || (StringUtil.isEmpty(entity.getId()))) {
      return null;
    }

    entity = get(entity.getId());
    if (entity == null) {
      return null;
    }

    if (isAddOne) {
      entity.setMaxVal(entity.getMaxVal() + 1);
      save(entity);
    }
    return entity;
  }

  /**
   * 根据SysNoType生成编号.
   * SysNo
   *
   * @param sysNoType 编号类型
   * @return String
   */
  @Transactional(readOnly = false)
  public SysNoOffice genByKeyss(SysNoType sysNoType, String OfficeId) {
    SysNoOffice sysNoOffice = sysNoOfficeDao.getByKeyss(sysNoType.getKey(), OfficeId);
    if ((sysNoOffice != null) && (sysNoOffice.getSysNo() != null)) {
      SysNoOffice entity = sysNoOffice;
      SysNo sysNo = entity.getSysNo();
      sysNo.setFormat(SysNodeTool.genBySysMaxNo(sysNo));
      sysNo.setSysmaxVal(sysNo.getSysmaxVal() + 1);
      sysNoOfficeDao.update(entity);
      return sysNoOffice;
    }
    logger.warn("SNOKeyss 流程标识数据不存在，请初始化到数据库："+sysNoType.getKey());
    return null;
  }
}