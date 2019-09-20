package com.oseasy.dr.modules.dr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.dr.modules.dr.entity.DrCard;

/**
 * 门禁卡DAO接口.
 * @author chenh
 * @version 2018-03-30
 */
@MyBatisDao
public interface DrCardDao extends CrudDao<DrCard> {
	public DrCard getByNoWithoutId(@Param("cardno")String cardno,@Param("cardid")String cardid);
	/**
     * 根据No获取单条数据.
	 * @param no
	 * @return
	 */
	public DrCard getByNo(String no);

	/**
     * 获取单条数据
     * @param entity
     * @return
     */
    public DrCard getByg(String id);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    public List<DrCard> findListByg(DrCard entity);


    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<DrCard> findAllListByg(DrCard entity);

    /**
     * 查找所有入驻用户.
     * @return List 列表
     */
    public List<User> findAllUserList(User user);

    /**
     * 查找所有入驻未开卡用户.
     * @return List 列表
     */
    public List<User> findUserListNoCard(User user);

    /**
     * 查找所有未开卡用户.
     * @return List 列表
     */
    public List<User> findAllUserListNoCard(User user);

    /**
     * 查找所有入驻用户卡.
     * @return List 列表
     */
    public List<DrCard> findUserListBy(DrCard entity);

    /**
     * 查找所有入驻用户卡.
     * @return List 列表
     */
    public List<DrCard> findUserListByg(DrCard entity);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrCard drCard);

  /**
   * 批量更新卡用户数据
   * @param entity
   * @return
   */
  public int updateUserByPl(@Param("entitys") List<DrCard> entitys);

  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int savePl(@Param("list") List<DrCard> entitys);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deletePlwl(@Param("ids") List<String> ids);

  /**
   * 批量更新数据状态
   * @param cids 卡号
   * @param status 状态
   * @return
   */
  public int updateStatusByPl(@Param("cids") List<String> cids, @Param("status") Integer status);
  /**
   * 批量更新数据处理状态
   * @param cids 卡号
   * @param status 状态
   * @return
   */
  public int updateDealStatusByPl(@Param("cids") List<String> cids, @Param("dealStatus") Integer dealStatus);
  /**
   * 批量更新更新处理中的数据处理状态
   * @param cids 卡号
   * @param status 状态
   * @return
   */
  public int updateDealingStatusByPl(@Param("cids") List<String> cids, @Param("dealStatus") Integer dealStatus);
/**
 * 根据姓名、学号、手机号获取卡.
 * @param param
 * @return
 */
public List<DrCard> findListCardByNameOrNOorMobile(String param);
}