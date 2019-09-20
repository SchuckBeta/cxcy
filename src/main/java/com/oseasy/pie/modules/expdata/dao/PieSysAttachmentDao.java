package com.oseasy.pie.modules.expdata.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.expdata.entity.ExpInfo;

/**
 * 附件信息表DAO接口
 * @author zy
 * @version 2017-03-23
 */
@MyBatisDao
public interface PieSysAttachmentDao extends CrudDao<SysAttachment> {
    public List<SysAttachment> findByExpInfo(@Param("eis")List<ExpInfo> eis);
}