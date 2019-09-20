package com.oseasy.pro.modules.auditstandard.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandard;
import com.oseasy.pro.modules.auditstandard.vo.AuditStandardVo;

/**
 * 评审标准DAO接口.
 * @author 9527
 * @version 2017-07-28
 */
@MyBatisDao
public interface AuditStandardDao extends CrudDao<AuditStandard> {
    public int checkName(@Param("id") String id,@Param("name")String name);
    public List<AuditStandardVo> findListVo(AuditStandardVo vo);
    public List<Map<String,String>> getChildNodes(@Param("param") List<Map<String,String>> param);
    public List<Map<String,String>> getSelfNodes(@Param("param") List<Map<String,String>> param);
    
}