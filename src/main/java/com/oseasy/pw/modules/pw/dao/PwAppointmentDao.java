package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.vo.PwAppCalendarParam;
import com.oseasy.pw.modules.pw.vo.PwAppMouthVo;
import com.oseasy.pw.modules.pw.vo.PwAppointmentVo;

/**
 * 预约DAO接口.
 * 
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwAppointmentDao extends CrudDao<PwAppointment> {
	public Long getCount(PwAppointment p);

	/**
	 * 根据日历条件查询预约申请.
	 * 
	 * @param pwAppcParam
	 * @return
	 */
	public List<PwAppointment> findListByCalendarParam(PwAppCalendarParam pwAppcParam);

	List<PwAppointment> findMyPwAppointmentList(PwAppointment pwAppointment);

	List<PwAppointment> findViewMonth(PwAppointment pwAppointment);

	List<PwAppointment> findListByPwAppointmentVo(PwAppointmentVo pwAppointmentVo);

	List<PwAppMouthVo> findViewMonthList(PwAppointmentVo pwAppointmentVo);

	List<PwAppointment> findRoomOrderDetailList(PwAppointment pwAppointment);

	int deleteByRoomIds(@Param("roomIds") List<String> roomIds);

	@Override
	@FindListByTenant
	public List<PwAppointment> findList(PwAppointment entity);


	@Override
	@InsertByTenant
	public int insert(PwAppointment entity);
}