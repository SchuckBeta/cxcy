package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpaceRoom;
import com.oseasy.pw.modules.pw.vo.PwAppointmentVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间DAO接口.
 *
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwRoomDao extends CrudDao<PwRoom> {

    /**
     * 级联查询所有房间.
     *
     * @param pwRoom
     * @return List
     */
    @FindListByTenant
    public List<PwRoom> findListByJL(PwRoom pwRoom);

    @Override
    @FindListByTenant
    public List<PwRoom> findList(PwRoom entity);


    @Override
    @InsertByTenant
    public  int insert(PwRoom entity);

    /**
     * 级联查询所有已被分配房间.
     *
     * @param pwRoom
     * @return List
     */
    @FindListByTenant
    public List<PwRoom> findListByJLKfpCD(PwRoom pwRoom);

    /**
     * 删除
     *
     * @param roomIds
     * @return
     */
    public int deleteByRoomIds(@Param("roomIds") List<String> roomIds);

    /**
     * 查询房间和其上述楼层和楼栋
     *
     * @return
     */
    @FindListByTenant
    List<PwSpaceRoom> findSpaceAndRoom();

    @FindListByTenant
    List<PwRoom> findListByPwAppointmentVo(PwAppointmentVo pwAppointmentVo);

    /**
     * 验证房间名是否存在.
     * @param name 房间名
     * @param sid 楼层
     * @return List
     */
    public List<PwRoom> verifyNameBySpace(@Param("name") String name, @Param("sid") String sid);


    /**
     * 批量删除房间
     */
    public void deleteRoom(@Param("entitys")List<PwRoom> entitys);

    public void updateRoom(PwRoom pwRoom);

    /**
     * 批量修改房间剩余工位数
     */
    public void updatePLRemaindernum(@Param("entitys") List<PwRoom> entitys);

    /**
     * 预约房间查询
     */
    public List<PwRoom> findOrderRoomPage(PwRoom pwRoom);

    /**
     * 分配房间查询
     */
    @FindListByTenant
    public List<PwRoom> findAllotRoomPage(PwRoom pwRoom);
    /**
     * 其他房间查询
     */
    @FindListByTenant
    public List<PwRoom> findOtherRoomPage(PwRoom pwRoom);
    @FindListByTenant
    public List<PwRoom> findRoomUseList(PwRoom pwRoom);

}