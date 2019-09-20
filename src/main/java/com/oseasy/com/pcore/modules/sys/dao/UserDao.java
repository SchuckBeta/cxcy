/**
 *
 */
package com.oseasy.com.pcore.modules.sys.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;

/**
 * 用户DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public User getNtenant(String id);

	@Override
	@FindListByTenant
	public List<User> findList(User entity);

	/**
	 * 查询机构所有用户.
	 * @param entity User
	 * @return List
     */
	@FindListByTenant
	public List<User> findListByOffice(User entity);

	/**
	 * 查询机构所顶级用户.
	 * @param entity User
	 * @return List
	 */
	@FindListByTenant
	public List<User> findListByOfftop(User entity);

	/**
	 * 查询机构所子级用户.
	 * @param entity User
	 * @return List
	 */
	@FindListByTenant
	public List<User> findListByOffsub(User entity);

	public List<UserVo> findListByVo(UserVo vo);
	@FindListByTenant
	public List<UserVo> getTeaInfo(@Param("idsArr")String[] idsArr);
	@FindListByTenant
	public List<UserVo> getStudentInfo(@Param("idsArr")String[] idsArr);
	public void updateLikes(@Param("param") Map<String,Integer> param);
	//批量更新浏览量
    public void updateViews(@Param("param") Map<String,Integer> param);
	@FindListByTenant
	public User getByMobile(User user);
	public User getByMobileForAll(User user);

	public User getByMobileWithId(User user);
	public void updateMobile(User user);

	public  String getTeacherTypeByUserId(@Param("userId") String userId);
	/**
	 * 根据登录名称查询用户
	 * @return
	 */
	@FindListByTenant
	public User getByLoginName(User user);

	public User getByLoginNameNt(User user);

	/**
	 * 根据登录名或者学号查询用户
	 * @param loginNameOrNo 录名或者学号
	 * @return User
     */
	@FindListByTenant
	public User getByLoginNameOrNo(@Param("loginNameOrNo")String  loginNameOrNo,@Param("id")String  id);

	public User getByLoginNameOrNoForAll(@Param("loginNameOrNo")String  loginNameOrNo,@Param("id")String  id);
	@FindListByTenant
	public User getByLoginNameAndNo(@Param("loginName")String  loginName,@Param("no")String no);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@FindListByTenant
	public List<User> findUserByOfficeId(User user);

	/**
	 * 通过professionalId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@FindListByTenant
	public List<User> findUserByProfessionId(User user);
	@FindListByTenant
	public List<User> findUserByProfessionIdAndRoleType(User user);

	/**
	 * 会查询关联拓展表表.
	 * @param user 用户
	 * @return List
	 */
	@FindListByTenant
    public List<User> findListByg(User user);

	/**
	 * 查询全部用户数目
	 * @return
	 */
	@FindListByTenant
	public long findAllCount(User user);

	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);

	/**
	 * 更新用户照片
	 * @param user
	 * @return
	 */
	public int updateUserPhoto(User user);
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);


	public int deleteUserRoleByTypes(@Param("rtypes")List<String> rtypes,@Param("userId")String id,@Param("tenantId")String tenantId);

	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);

	/**
	 * 插入专家、导师、学生
	 * @param user
	 * @return
	 */
	public int insertUserStu(User user);
	public int insertUserTea(User user);

	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	@FindListByTenant
	public List<User> findListByRoleName(String enname);
	public List<User> getCollegeSecs(String id);
	public List<User> getCollegeExperts(String id);
	public List<User> getSchoolSecs();
	public List<User> getSchoolExperts();
	@FindListByTenant
	public List<User> findByType(User user);
	@FindListByTenant
	public List<User> findByRoleBizType(User user);

	@Override
	@InsertByTenant
	public int insert(User user);


	public void updateUserByPhone(User user);

	public User findUserByLoginName(String loginName);

	public User getUserByName(String name);

	@FindListByTenant
	public List<User> findListTree(User user);



	/**
	 * 查询学生.
   * @param user 用户
   * @return List
	 */
	public List<User> findListTreeByStudent(User user);
	/**
	 * 查询学生.
	 * @param user 用户
	 * @return List
	 */
	@FindListByTenant
	public List<User> findListTreeByStudentNoDomain(User user);

  /**
   * 查询导师.
   * @param user 用户
   * @return List
   */
  	@FindListByTenant
	public List<User> findListTreeByTeacher(User user);

  /**
   * 查询用户（基本信息）.
   * @param user 用户
   * @return List
   */
  	@FindListByTenant
	public List<User> findListTreeByUser(User user);
	@FindListByTenant
	public List<User> getStuByCdn(@Param("no") String no,@Param("name") String name);
	@FindListByTenant
	public List<User> getTeaByCdn(@Param("no") String no,@Param("name") String name);
	@FindListByTenant
	public User getByNo(@Param("no")String no);
	@FindListByTenant
	List<User> findListByRoleId(String roleId);


	List<User> findProvListByRoleId(@Param("roleId")String roleId,@Param("tenantId")String tenantId);

	List<User> findListByRoleNameAndOffice(@Param("enname") String enname, @Param("userId") String userId);

	public List<UserVo> getUserByPorIdAndTeamId(@Param("proId")String proId, @Param("teamId") String teamId, @Param("userType") String userType);

	/**
	 * 查询所有需要修复的学生.
	 */
  	public List<String> findUserByRepair();

	List<User> findListByRoleIdAndOffice(@Param("roleId")String roleId, @Param("userId")String userId);

    public Integer checkUserByNoAndLoginName(User user);
    public List<User> getUserByNoAndLoginName(User user);
    public int checkTeacherByUid(String uid);
    public int checkStudentByUid(String uid);


	Integer checkUserNoUnique(@Param("no") String no, @Param("userId") String userId);

	public List<User> findListByRoleTypeAndOffice(@Param("officeid")String officeid,@Param("roletype")String roletype);

	List<User> findUserByExpert(User user);

	List<User> findUserListByRoleId(@Param("roleId")String roleId);

	Date getSysDate();

	void deleteByUserRole(@Param("userId")String userId,@Param("roleId") String roleId);

	void updateUserLoginName(@Param("id")String id, @Param("loginName")String loginName);

	User getUserByMobileAndName(User user);

	String findFistTeacherByDeclareId(String declareId);

	/**
	 * 删除所有用户
	 * @param entity
	 * @return
	 */
	public void deleteWLByOffice(@Param("entity")User entity);

	/**
	 * 删除所有顶集用户
	 * @param entity
	 * @return
	 */
	public void deleteWLByOfftop(@Param("entity")User entity);

	/**
	 * 删除所有子集用户
	 * @param entity User
	 * @return
	 */
	public void deleteWLByOffsub(@Param("entity")User entity);

	@FindListByTenant
	Integer checkLoginNameUniqueByTenant(@Param("loginName") String loginName, @Param("id") String id);
	Integer checkLoginNameUnique(@Param("loginName") String loginName, @Param("id") String id);

	@FindListByTenant
	Integer checkNoUniqueByTenant(@Param("no") String no, @Param("id") String id);
	Integer checkNoUnique(@Param("no") String no, @Param("id") String id);

	Integer checkMobileUnique(@Param("mobile") String mobile, @Param("id") String id);
}
