package com.oseasy.dr.modules.dr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.vo.PwSpace;
import com.oseasy.dr.modules.dr.vo.PwSpaceDoor;
import com.oseasy.dr.modules.dr.vo.PwSpaceGitem;
import com.oseasy.pw.modules.pw.entity.PwEnter;

/**
 * 入驻申报DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface DrPwEnterDao extends CrudDao<PwEnter> {
    public List<PwSpace> getPwSpace();
    public List<PwSpaceDoor> getPwSpaceDoor(String cardid);
    public List<PwSpaceGitem> getPwSpaceGitem(String gid);
}