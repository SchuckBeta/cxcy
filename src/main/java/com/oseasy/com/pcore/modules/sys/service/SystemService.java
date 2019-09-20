/**
 *
 */
package com.oseasy.com.pcore.modules.sys.service;

import java.util.List;

import javax.annotation.Resource;

import com.oseasy.com.pcore.modules.authorize.vo.IAuthCheck;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.security.SystemAuthorizingRealm;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.LogUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Resource
    private SystemAuthorizingRealm systemAuthorizingRealm;
    @Autowired
    private CoreService coreService;

    /*************************************************************
     * 公共处理的方法.
     *************************************************************/
//    @Override
//    public Integer checkRltype(String id) {
//        //TODO LM
//        return "1";
//    }
//    @Override
//    public boolean checked(Integer id) {
//        Integer ltype = checkRltype(id);
//        //TODO LM
//        return (ltype).equals("1");
//    }

    /**has rolelist
     * 获取用户
     *
     * @param id
     * @return
     */
    public User getUser(String id) {
        return CoreUtils.get(id);
    }

    public void afterPropertiesSet() throws Exception {
        coreService.afterPropertiesSet();
    }

    public Page<User> findUser(Page<User> page, User user) {
        List<String> queryDict=Lists.newArrayList();
        //查询领域。
        List<Dict> dictList=DictUtils.getDictList("technology_field");
        if(StringUtil.isNotEmpty(user.getQueryStr())){
            for(Dict dict:dictList){
                if(dict.getLabel().contains(user.getQueryStr())){
                    queryDict.add(dict.getValue());
                }
            }
        }
        if(StringUtil.checkNotEmpty(queryDict)){
            user.setDictList(queryDict);
        }
        return coreService.findUser(page, user);
    }

    public Page<User> findPage(Page<User> page, User user) {
        List<String> queryDict=Lists.newArrayList();
        //查询领域。
        List<Dict> dictList=DictUtils.getDictList("technology_field");
        if(StringUtil.isNotEmpty(user.getQueryStr())){
            for(Dict dict:dictList){
                if(dict.getLabel().contains(user.getQueryStr())){
                    queryDict.add(dict.getValue());
                }
            }
        }
        if(StringUtil.checkNotEmpty(queryDict)){
            user.setDictList(queryDict);
        }
        return coreService.findPage(page, user);
    }

    public Page<User> findUserByExpert(Page<User> page, User user) {
        return coreService.findUserByExpert(page, user);
    }

    public String getTeacherTypeByUserId(String userId) {
        return userDao.getTeacherTypeByUserId(userId);
    }

    public Page<User> findListTree(Page<User> page, User user) {
        return coreService.findListTree(page, user);
    }

    /**
     * 通过专业ID获取用户列表，仅返回用户id和name（树查询用户时用）
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findUserByProfessionId(String professionalId) {
        return coreService.findUserByProfessionId(professionalId);
    }

    @Transactional(readOnly = false)
    public int insert(User user) {
        if((user != null)){
            user = User.dealLoginNameAnNo(user);
        }
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
        return userDao.insert(user);
    }

    public Role getRole(String id) {
        return roleDao.get(id);
    }

    public Role getNamebyId(String id) {
        return coreService.getNamebyId(id);
    }

    public Role getRoleByName(String name) {
        return coreService.getRoleByName(name);
    }
    public Role getRoleByEnname(String enname) {
        return coreService.getRoleByEnname(enname);
    }

    public List<Role> findRole(Role role) {
        return coreService.findRole(role);
    }

    public List<Role> findAllRole() {
        return coreService.findAllRole();
    }

    //-- Menu Service --//
    public Menu getMenu(String id) {
        return coreService.getMenu(id);
    }

    public Menu getMenuById(String id) {
        return coreService.getMenuById(id);
    }

    public Menu getMenuByName(String name) {
        return coreService.getMenuByName(name);
    }

    public List<Menu> findAllMenu() {
        return coreService.findAllMenu();
    }

    @Transactional(readOnly = false)
    public void saveMenu(Menu menu) {
        coreService.saveMenu(menu);
    }

    /*************************************************************
     * 公共未处理的方法.
     *************************************************************/
    /**
     * 修改用户.
     * @param nuser 用户参数
     * @param ouser 操作用户
     * @return User
     */
    public User updateUserByLoginName(User nuser, User ouser) {
        if(nuser != null){
            if(StringUtil.isNotEmpty(nuser.getName())){
                ouser.setName(nuser.getName());
            }
            if(StringUtil.isNotEmpty(nuser.getMobile())){
                ouser.setMobile(nuser.getMobile());
            }
            if(StringUtil.isNotEmpty(nuser.getEmail())){
                ouser.setEmail(nuser.getEmail());
            }
            if(nuser.getBirthday() != null){
                ouser.setBirthday(nuser.getBirthday());
            }
            if(StringUtil.isNotEmpty(nuser.getSex())){
                ouser.setSex(nuser.getSex());
            }
            if(StringUtil.isNotEmpty(nuser.getDegree())){
                ouser.setDegree(nuser.getDegree());
            }
            if(StringUtil.isNotEmpty(nuser.getEducation())){
                ouser.setEducation(nuser.getEducation());
            }
            if(nuser.getOffice() != null){
                ouser.setOffice(nuser.getOffice());
            }
            if(StringUtil.isNotEmpty(nuser.getProfessional())){
                ouser.setProfessional(nuser.getProfessional());
            }
        }

        userDao.update(ouser);
        CoreUtils.clearCache(ouser);
        return ouser;
    }

    @Transactional(readOnly = false)
    public void changeMenuIsShow(Menu menu) {
        menuDao.updateIsShow(menu);
        CoreUtils.removeCache(CoreUtils.CACHE_MENU_LIST);
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    public Boolean checkMenuName(Menu menu){
        Integer isUnique = menuDao.checkMenuName(menu);
        return isUnique < 1;
    }

    public Boolean checkRoleName(Role role){
       return coreService.checkRoleName(role);
    }

    public Boolean checkRoleEnName(Role role){
        return coreService.checkRoleEnName(role);
    }

    public List<Menu> getMenuByRoleId(String roleId) {
        return menuDao.findByRoleId(roleId);
    }

    public Menu getByRid(Menu entity){
        return menuDao.getByRid(entity);
    }


    public Menu getByLtype(Menu entity){
        return menuDao.getByLtype(entity);
    }


    public Integer getParentLtypeById(String id){
        return menuDao.getParentLtypeById(id);
    }
}
