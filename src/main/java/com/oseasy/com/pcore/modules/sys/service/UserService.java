package com.oseasy.com.pcore.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by zhangzheng on 2017/2/23.
 */
@Service
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private CoreService coreService;

    public List<UserVo> getTeaInfo(String[] idsArr) {
    	return userDao.getTeaInfo(idsArr);
    }

    public List<UserVo> getStudentInfo(String[] idsArr) {
    	return userDao.getStudentInfo(idsArr);
    }
    public Page<UserVo> findPageByVo(Page<UserVo> page, UserVo userVo) {
    	userVo.setPage(page);
		page.setList(userDao.findListByVo(userVo));
		return page;
	}
    @Transactional(readOnly = false)
    public int insertUserRole(User user) {
        return userDao.insertUserRole(user);

    }

    public List<User> getTeaByCdn(String no,String name) {
        return userDao.getTeaByCdn( no, name);
    }
    /**has RoleList
     * @param no
     * @return
     */
    public User getByNo(String no) {
    	User user=userDao.getByNo(no);
    	if(user!=null&&StringUtil.isNotEmpty(user.getId())){
    		user.setRoleList(roleDao.findList(new Role(user)));
    	}
        return user;
    }
    public List<User> getStuByCdn(String no,String name) {
        return userDao.getStuByCdn( no, name);
    }

    public List<User> findListByRoleName(String enname) {
        return userDao.findListByRoleName(enname);
    }

    public List<User> findUserListByRoleId(String roleId) {
        return userDao.findUserListByRoleId(roleId);
    }

    public List<User> findListByRoleName(String enname,String userId) {
        return userDao.findListByRoleNameAndOffice(enname,userId);
    }

    public List<User> findListByUserId(String userid) {
        return userDao.findListByRoleName(userid);
    }

    public List<User> findByType(User user) {
        return userDao.findByType(user);
    }
    public List<User> findByRoleBizType(User user) {
        return userDao.findByRoleBizType(user);
    }
    public User findUserById(String id) {
        return userDao.get(id);
    }

    public List<String> getRolesByName(String roleName) {
        List<User> users = findListByRoleName(roleName);
        List<String> roles = new ArrayList<String>();
        for (User user : users) {
            roles.add(user.getId());
        }
        return roles;
    }

    public List<String> getRolesByName(String roleName,String userId) {
        List<User> users=findListByRoleName(roleName,userId);
        List<String> roles=new ArrayList<String>();
        for (User user:users) {
            roles.add(user.getId());
        }
        return roles;
    }

    //根据学生id找到对应学院的教学秘书
    public List<String> getCollegeSecs(String userid) {
        List<User> users= userDao.getCollegeSecs(userid);
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    //根据学生id找到院级专家
    public  List<String> getCollegeExperts(String userid) {
        List<User> users= userDao.getCollegeExperts(userid);
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    //找到院级专家
    public List<User> getCollegeExpertUsers(String userid) {
        List<User> users= userDao.getCollegeExperts(userid);
        return users;
    }

    //根据学生id找到对应学院的教学秘书
    public User getCollegeSecUsers(String userid) {
        List<User> users= userDao.getCollegeSecs(userid);
        if (users.size()>0) {
        	return users.get(0);
        }
        return null;
    }

    //找到学校管理员
    public User getSchoolSecUsers() {
        List<User> users= userDao.getSchoolSecs();
        if (users.size()>0) {
        	return users.get(0);
        }
        return null;
    }

    //找到院级专家
    public List<User> getSchoolExpertUsers() {
        List<User> users= userDao.getSchoolExperts();
        return users;
    }


    //找到学校管理员
    public  List<String> getSchoolSecs() {
        List<User> users= userDao.getSchoolSecs();
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    public User getSchoolSecUser() {
        List<User> users= userDao.getSchoolSecs();
        if (users.size()>0) {
            return users.get(0);
        }
        return null;
    }

    //找到校级专家
    public  List<String> getSchoolExperts() {
        List<User> users= userDao.getSchoolExperts();
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    //根据手机号查找用户
    public User getByMobile(User User) {
		return userDao.getByMobileForAll(User);
    }
    //根据手机号和姓名查找用户
    public User getUserByMobileAndName(String name, String mobile) {
        User pu = new User();
        pu.setName(name);
        pu.setMobile(mobile);
        return getUserByMobileAndName(pu);
    }
    public User getUserByMobileAndName(User User) {
        return userDao.getUserByMobileAndName(User);
    }

    //根据手机号查找用户排除自己
    public User getByMobileWithId(User User) {
		return userDao.getByMobileWithId(User);

    }
    @Transactional(readOnly = false)
    public void updateMobile(User user) {
        userDao.updateMobile(user);
        CoreUtils.removeUserAll(user);
    }

    @Transactional(readOnly = false)
    public int saveUser(User user) {
        if(user != null){
            if(user.getCreateDate() == null){
                user.setCreateDate(DateUtil.newDate());
            }
            if(user.getUpdateDate() == null){
                user.setUpdateDate(DateUtil.newDate());
            }
            if(user.getCreateBy() == null){
                user.setCreateBy(CoreUtils.getUser());
            }
            if(user.getUpdateBy() == null){
                user.setUpdateBy(CoreUtils.getUser());
            }
            user = User.dealLoginNameAnNo(user);
        }
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
		return userDao.insert(user);
    }

    @Transactional(readOnly = false)
    public void updateUser(User user) {
        if(user != null){
            user.setUpdateDate(DateUtil.newDate());
            user.setUpdateBy(CoreUtils.getUser());
            user = User.dealLoginNameAnNo(user);
        }
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
    	userDao.update(user);
    	CoreUtils.removeUserAll(user);
    }

    @Transactional(readOnly = false)
    public void updateUserPhoto(User user) {
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
        userDao.updateUserPhoto(user);
        CoreUtils.removeUserAll(user);
    }

    @Transactional(readOnly = false)
    public void delete(User user) {
        userDao.delete(user);
        CoreUtils.removeUserAll(user);
    }

	public User findUserByLoginName(String loginName) {
		return userDao.findUserByLoginName(loginName);
	}

	public User getUserByName(String name) {
		return userDao.getUserByName(name);
	}

    public User getByLoginNameOrNo(String loginNameOrNo,String id) {
        return userDao.getByLoginNameOrNoForAll(loginNameOrNo,id);
    }

    public List<String> findListByRoleId(String roleId) {
        List<User> users= userDao.findListByRoleId(roleId);
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    public List<String> findProvListByRoleId(String roleId,String tenantId) {
        List<User> users= userDao.findProvListByRoleId(roleId,tenantId);
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    public List<User> findByRoleId(String roleId) {
        return  userDao.findListByRoleId(roleId);
    }
    public List<String> findUserByRepair() {
      return userDao.findUserByRepair();
    }

    public List<String> findListByRoleIdAndOffice(String roleId, String userId) {
        List<User> users= userDao.findListByRoleIdAndOffice(roleId,userId);
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }
    public List<User> findListByRoleTypeAndOffice(String officeid, String roletype) {
        return userDao.findListByRoleTypeAndOffice(officeid, roletype);
    }

    public List<User> findUserListByRoleId(String roleId, String userId) {
        List<User> users= userDao.findListByRoleIdAndOffice(roleId,userId);
        return users;
    }

    public List<UserVo> getUserByPorIdAndTeamId(String proId, String teamId, String userType) {
        return userDao.getUserByPorIdAndTeamId(proId, teamId, userType);
    }

    public Boolean checkUserNoUnique(User user){
        Integer integer = coreService.checkUserNoUnique(user.getNo(), user.getId());
        return integer < 1;
    }
    @Transactional(readOnly = false)
    public void deleteByUserRole(String id, String roleId) {
        userDao.deleteByUserRole(id,roleId);
    }
    @Transactional(readOnly = false)
    public void insert(User user) {
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
        userDao.insert(user);
    }
    @Transactional(readOnly = false)
    public void deleteUserRole(User user) {
        userDao.deleteUserRole(user);
    }

    @Transactional(readOnly = false)
    public void deleteUserRole(List<String> rtypes, String id,String tenantId) {
        userDao.deleteUserRoleByTypes(rtypes,id,tenantId);
    }

}
