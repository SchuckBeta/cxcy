/**
 *
 */
package com.oseasy.com.pcore.modules.sys.service;

import java.util.*;

import javax.annotation.Resource;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.util.common.utils.UrlUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.security.Digests;
import com.oseasy.com.pcore.common.security.shiro.session.SessionDAO;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.common.web.Servlets;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.security.SystemAuthorizingRealm;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.LogUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.Encodes;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.util.StringUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
@Transactional(readOnly = true)
public class CoreService extends BaseService implements InitializingBean {
    public static final String USER_PSW_DEFAULT = "123456";
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
    /**
     * 是需要同步Activiti数据，如果从未同步过，则同步数据。
     */
    public static boolean isSynActivitiIndetity = true;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private SessionDAO sessionDao;
    @Resource
    private SystemAuthorizingRealm systemAuthorizingRealm;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private OfficeService officeService;

    /*************************************************************
     * 公共处理的方法.
     *************************************************************/
	public Integer getRoleUserCount(String roleid){
		return roleDao.getRoleUserCount(roleid);
	}

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Encodes.decodeHex(password.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }

    public SessionDAO getSessionDao() {
        return sessionDao;
    }

    /**has rolelist
     * 获取用户
     * @param id
     * @return
     */
    public User getUser(String id) {
        return CoreUtils.get(id);
    }


    @Transactional(readOnly = false)
    public void saveRole(Role role) {
        saveRole(role, true);
    }

    /**
     * 保存角色授权.
     * @param role
     * @param isndelrm 是否删除角色菜单
     */
    @Transactional(readOnly = false)
    public void saveRole(Role role, boolean isndelrm) {
        String officeId = officeService.selectOfficeIdByParentId("0");
        Office off = new Office();
        off.setId(officeId);
        role.setOffice(off);

        Role dbrole = roleDao.get(role);
        if((dbrole != null) && dbrole.getRinit()){
            role.setRtype(dbrole.getRtype());
            role.setRid(dbrole.getRid());
            role.setRinit(dbrole.getRinit());
        }
        if (StringUtil.isBlank(role.getId())) {
            role.preInsert();
            roleDao.insert(role);
            // 同步到Activiti
            saveActivitiGroup(role);
        } else {
            role.preUpdate();
            roleDao.update(role);
        }
        if(isndelrm){
            // 更新角色与菜单关联
            roleDao.deleteRoleMenu(role);
        }
        List<Menu> menus = Lists.newArrayList();
        //String[] meusList = role.getMenuIds()
        if (role.getMenuList().size() > 0) {
            roleDao.insertRoleMenu(role);
        }
        // 更新角色与部门关联
        roleDao.deleteRoleOffice(role);
        if (role.getOfficeList().size() > 0) {
            roleDao.insertRoleOffice(role);
        }
        // 同步到Activiti
        saveActivitiGroup(role);
        // 清除用户角色缓存
//        removeCache(CoreUtils.CACHE_ROLE_LIST);
        JedisUtils.hashDel(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST);
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
    }

    /**
     * 保存角色授权.
     * @param role Role
     * @param menus List<Menu>
     * @return Role
     */
    @Transactional(readOnly = false)
    public Role insertRoleMenu(Role role, List<Menu> menus){
        if (StringUtil.checkEmpty(menus)) {
            return role;
        }

        Role crole = new Role();
        BeanUtils.copyProperties(role, crole);
        crole.setMenuList(menus);
        if (StringUtil.checkNotEmpty(crole.getMenuList())) {
            roleDao.deleteRoleMenus(crole);
            roleDao.insertRoleMenu(crole);
        }
        return role;
    }

    @Transactional(readOnly = false)
    public void deleteRole(Role role) {
        roleDao.delete(role);
        // 同步到Activiti
        deleteActivitiGroup(role);
        // 清除用户角色缓存
        JedisUtils.hashDel(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST);
//        removeCache(CoreUtils.CACHE_ROLE_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant());
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
    }

    /**has rolelist
     * 根据登录名获取用户
     *
     * @param str
     * @return
     */
    public User getUserByLoginName(String str) {
//        String[] ss = str.split("_");
//        String loginName="";
//        String url = "";
//        if(ss != null){
//            loginName = ss[0];
//            url = ss[1];
//        }
        User user = new User();
        user.setLoginName(str);
//        user.setDomianURL(url);
        return getUserByLoginName(user);
    }
    public User getUserByLoginName(User puser) {
        User user=userDao.getByLoginName(puser);
        if(user!=null&&StringUtil.isNotEmpty(user.getId())){
            user.setRoleList(roleDao.findList(new Role(user)));
        }
        return user;
    }

    public User getUserByLoginNameNt(String str) {
        User user = new User();
        user.setLoginName(str);
        return getUserByLoginNameNt(user);
    }
    public User getUserByLoginNameNt(User puser) {
        User user=userDao.getByLoginNameNt(puser);
        if(user!=null&&StringUtil.isNotEmpty(user.getId())){
            user.setRoleList(roleDao.findListNtenant(new Role(user)));
        }
        return user;
    }

    public List<String> findListByRoleId(String roleId) {
        List<User> users= userDao.findListByRoleId(roleId);
        List<String> list=new ArrayList<String>();
        for (User user:users) {
            list.add(user.getId());
        }
        return list;
    }

    /**has rolelist
     * @param loginNameOrNo
     * @return
     */
    public User getUserByLoginNameOrNo(String loginNameOrNo) {
    	User user=userDao.getByLoginNameOrNo(loginNameOrNo, null);
    	if(user!=null&&StringUtil.isNotEmpty(user.getId())){
    		user.setRoleList(roleDao.findList(new Role(user)));
    	}
    	return user;
    }

    public User getUserByLoginNameAndNo(String loginNameOrNo, String no) {
        return userDao.getByLoginNameAndNo(loginNameOrNo, no);
    }

    public User getUserByNo(String no) {
        return userDao.getByNo(no);
    }

    /**has rolelist
     * @param mobile
     * @return
     */
    public User getUserByMobile(String mobile) {
    	User user=CoreUtils.getByMobile(mobile);
    	if(user!=null&&StringUtil.isNotEmpty(user.getId())){
    		user.setRoleList(roleDao.findList(new Role(user)));
    	}
        return user;
    }

    public User getUserByMobile(String mobile, String id) {
        return CoreUtils.getByMobile(mobile, id);
    }

    /**
     * 会查询导师表.
     */
    public Page<User> findUser(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        page.setList(userDao.findListByg(user));
        return page;
    }

    /**
     * 只查询User表.
     */
    public Page<User> findPage(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        page.setList(userDao.findList(user));
        return page;
    }

    public Page<User> findUserByExpert(Page<User> page, User user) {
        user.setPage(page);
       // 执行分页查询
        page.setList(userDao.findUserByExpert(user));
        if (page != null) {
            List<User> userList = page.getList();
            if (userList != null && userList.size() > 0) {
                for (User usertmp : userList) {
                    List<Role> roleList =findListByUserId(usertmp.getId());
                    usertmp.setRoleList(roleList);
                }
            }
        }
        return page;
    }

    public String getTeacherTypeByUserId(String userId) {
        return userDao.getTeacherTypeByUserId(userId);
    }

    public Page<User> findListTree(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        page.setList(userDao.findListTree(user));
        return page;
    }


    /**
     * 无分页查询人员列表
     *
     * @param user
     * @return
     */
    public List<User> findUser(User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        return userDao.findListByg(user);
    }

    /**
     * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findUserByOfficeId(String officeId) {
        List<User> list = (List<User>) CacheUtils.get(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
        if (list == null) {
            User user = new User();
            user.setOffice(new Office(officeId));
            list = userDao.findUserByOfficeId(user);
            CacheUtils.put(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
        }
        return list;
    }

    /**
     * 通过专业ID获取用户列表，仅返回用户id和name（树查询用户时用）
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findUserByProfessionId(String professionalId) {
        List<User> list = (List<User>) CacheUtils.get(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + professionalId);
        if (list == null) {
            User user = new User();
            user.setProfessional(professionalId);
            list = userDao.findUserByProfessionId(user);
            CacheUtils.put(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + professionalId, list);
        }
        return list;
    }

    @Transactional(readOnly = false)
    public void updateUserInfo(User user) {
        user.preUpdate();
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
        userDao.updateUserInfo(user);
        // 清除用户缓存
        CoreUtils.removeUserAll(user);
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
    }


    @Transactional(readOnly = false)
    public void updatePasswordById(String id, String loginName, String newPassword) {
        User user = new User(id);
        user.setPassword(CoreUtils.entryptPassword(newPassword));
        userDao.updatePasswordById(user);
        // 清除用户缓存
        user.setLoginName(loginName);
        CoreUtils.updatePsw(CoreUtils.getSession(), newPassword);
        CoreUtils.clearCache(user);
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
    }

    //-- Role Service --//

    @Transactional(readOnly = false)
    public void updateUserLoginInfo(User user) {
        // 保存上次登录信息
        user.setOldLoginIp(user.getLoginIp());
        user.setOldLoginDate(user.getLoginDate());
        // 更新本次登录信息
        user.setLoginIp(UrlUtil.getRemoteAddr(Servlets.getRequest()));
        user.setLoginDate(new Date());
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
        userDao.updateLoginInfo(user);
    }

    /**
     * 获得活动会话
     *
     * @return
     */
    public Collection<Session> getActiveSessions() {
        return sessionDao.getActiveSessions(false);
    }

    public Role getRole(String id) {
        return roleDao.get(id);
    }

    public Role getRole(Role role) {
        Role cr = roleDao.getByRtype(role);
        return getRole(cr.getId());
    }

    public Role getByRtype(Role role) {
        return roleDao.getByRtype(role);
    }

    public Role getRoleByRid(String rid) {
        Role prole = new Role();
        prole.setRid(rid);
        return roleDao.getByRid(prole);
    }

    /**
     * 获取当前租户类型开户的初始角色（rinit = true）.
     * @return Role
     */
    public Role getByRtype(String rType){
        Role role=new Role();
        role.setRtype(rType);
        return roleDao.getByRtype(role);
    }

    /**
     * 获取省租户类型开户的初始角色（rinit = true）
     * @param rType
     * @return
     */
    public Role getByRtypeOfProvince(String rType,String tenantId){
        Role role=new Role();
        role.setRtype(rType);
        role.setTenantId(tenantId);
        return roleDao.getByRtypeOfProvince(role);
    }

    /**
     * 获取当前租户所有该类型角色（包含初始和用户自己新建的）.
     * @param role 角色
     * @return List
     */
    public List<Role> findListByRtype(Role role){
        return roleDao.findListByRinit(role);
    }

    /**
     * 获取当前租户所有的的初始角色（rinit = true）.
     * @param role 角色
     * @return List
     */
    public List<Role> findListByRinit(Role role){
        return roleDao.findListByRinit(role);
    }

    public Role getNamebyId(String id) {
        return roleDao.getNamebyId(id);
    }

    public Role getRoleByName(String name) {
        Role r = new Role();
        r.setName(name);
        return roleDao.getByName(r);
    }

    public Role getRoleByEnname(String enname) {
        Role r = new Role();
        r.setEnname(enname);
        return roleDao.getByEnname(r);
    }

    public Boolean checkRoleName(Role role){
        Integer isUnique = roleDao.checkRoleName(role);
        return isUnique < 1;
    }

    public Boolean checkRoleEnName(Role role){
        Integer isUnique = roleDao.checkRoleEnName(role);
        return isUnique < 1;
    }

    /**
     * 根据UID查询角色是否存在.
     * @return Boolean
     */
    public Boolean checkRoleByUid(String uid, String rid) {
        return roleDao.checkRoleByUid(uid, rid) >= 1;
    }

    public Boolean checkAllByUid(String uid, String rid) {
        Boolean hasStudent = checkStudentByUid(uid);
        Boolean hasTeacher = checkTeacherByUid(uid);
        Boolean hasRole = checkRoleByUid(uid, rid);
        return true;
    }

    /**
     * 根据学号和登录名查询是否存在.
     * @return Boolean
     */
    public Boolean checkUserByNoAndLoginName(User user) {
        return userDao.checkUserByNoAndLoginName(user) >= 1;
    }
    public Boolean checkTeacherByUid(String uid) {
        return userDao.checkTeacherByUid(uid) >= 1;
    }
    public Boolean checkStudentByUid(String uid) {
        return userDao.checkStudentByUid(uid) >= 1;
    }
    /**
     * 根据学号和登录名查询是否存在.
     * @return Boolean
     */
    public List<User> getUserByNoAndLoginName(User user) {
        return userDao.getUserByNoAndLoginName(user);
    }

    public List<Role> findRole(Role role) {
        return roleDao.findList(role);
    }

    public List<Role> findAllRole() {
        return CoreUtils.getRoleList();
    }

    public Menu getMenu(String id) {
        return menuDao.getById(new Menu(id));
    }

    /**
     * 获取顶级菜单.
     * @param topmenus List
     * @param menu Menu
     * @return List
     */
    public List<Menu> gentopMenus(List<Menu> topmenus, Menu menu){
        if((menu == null) || StringUtil.isEmpty(menu.getParentIds())){
            return topmenus;
        }

        List<String> ids = StringUtil.splitStr(menu.getParentIds(), StringUtil.DOTH);
        if(StringUtil.checkEmpty(ids)){
            return topmenus;
        }

        List<Menu> entitys = menuDao.findListByIds(new Menu(ids));
        if(StringUtil.checkNotEmpty(entitys)){
            topmenus.addAll(entitys);
            topmenus.add(menu);
        }
        return topmenus;
    }

    /**
     * 根据当前租户获取根菜单.
     * @return Menu
     */
    public Menu getRoot(Menu entity) {
        if(entity == null){
            entity = new Menu();
        }
        entity.setParent(new Menu(CoreIds.NCE_SYS_TREE_PROOT.getId()));
        return menuDao.getRoot(entity);
    }

    public Menu getMenuById(String id) {
        return menuDao.findById(id);
    }

    public Menu getMenuByName(String name) {
        return menuDao.getMenuByName(name);
    }

    public List<Menu> findAllMenu() {
        return CoreUtils.getMenuList();
    }

    @Transactional(readOnly = false)
    public void saveMenu(Menu menu) {

        // 获取父节点实体
        if (menu.getParent()!= null){
            menu.setParent(menuDao.findById(menu.getParent().getId()));
            if (menu.getParent().getLtype() != null && menu.getParent().getLtype() != 0){
                menu.setLtype(menu.getParent().getLtype());
            }
        }
        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = menu.getParentIds();

        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
        if(menu.getLver() == null){
            menu.setLver(StringUtil.splitLength(StringUtil.DOTH, menu.getParentIds()));
        }
        // 保存或更新实体
        if (StringUtil.isBlank(menu.getId())) {
            menu.preInsert();
            menuDao.insert(menu);
        } else {
            menu.preUpdate();
            menuDao.update(menu);
        }

        // 更新子节点 parentIds
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        List<Menu> list = menuDao.findByParentIdsLike(m);
        for (Menu e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            e.setLver(StringUtil.splitLength(StringUtil.DOTH, menu.getParentIds()));
            menuDao.updateParentIds(e);
        }
        // 清除用户菜单缓存
//        removeCache(CACHE_MENU_LIST);
        JedisUtils.hashDelByKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
//        removeCache(CACHE_MENU_LIST+StringUtil.LINE_D+ TenantConfig.getCacheTenant());
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    @Transactional(readOnly = false)
    public void updateMenuSort(Menu menu) {
        menuDao.updateSort(menu);
        // 清除用户菜单缓存
//        JedisUtils.hashDel(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_MENU_LIST);
        JedisUtils.hashDelByKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());

//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
        // 清除日志相关缓存
        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }

    ///////////////// Synchronized to the Activiti //////////////////

    // 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

    @Transactional(readOnly = false)
    public void deleteMenu(Menu menu) {
        menuDao.delete(menu);
        // 清除用户菜单缓存
//        removeCache(CACHE_MENU_LIST);
//        removeCache(CACHE_MENU_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
        // 清除日志相关缓存
//        JedisUtils.hashDelByKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
//        CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
    }


    /**
     * 根据菜单ID查找所有子菜单.
     * @return List
     */
    public List<Menu> findAllMenuByParent(Menu menu) {
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        return menuDao.findByParentIdsLike(m);
    }

    public List<Menu> findAllMenuByParent(String id) {
        return findAllMenuByParent(new Menu(id));
    }


    public List<Menu> findRoleMenuByParentIdsLike(List<Role> roles, Menu menu) {
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        return menuDao.findRoleMenuByParentIdsLike(StringUtil.listIdToList(roles), m);
    }

    public List<Menu> findRoleMenuByParentIdsLike(List<Role> roles, String mid) {
        return findRoleMenuByParentIdsLike(roles, new Menu(mid));
    }

    public Menu getMenuByHref(String href) {
        return menuDao.getMenuByHref(href);
    }

    /**
     * 单租户下检验登录名已存在。
     * 返回true表示不存在，false表示已存在.
     * id 为Null时表示注册时校验，id不为Null时表示修改登录名校验
     * @param loginName 登录名
     * @param id 用户ID
     * @return
     */
    public boolean checkLoginNameUniqueByTenant(String loginName, String id) {
        Integer num = userDao.checkLoginNameUniqueByTenant(loginName, id);
        return  StringUtils.isEmpty(num) || num == 0;
    }

    /**
     * 全局检验登录名已存在。
     * 返回true表示不存在，false表示已存在.
     * id 为Null时表示注册时校验，id不为Null时表示修改登录名校验
     * @param loginName 登录名
     * @param id 用户ID
     * @return
     */
    public boolean checkLoginNameUnique(String loginName, String id) {
        Integer num = userDao.checkLoginNameUnique(loginName, id);
        return  StringUtils.isEmpty(num) || num == 0;
    }

    /**
     * 全局检验登录名已存在且只有一个。
     * 返回true表示不存在，false表示已存在.
     * id 为Null时表示注册时校验，id不为Null时表示修改登录名校验
     * @param loginName 登录名
     * @param id 用户ID
     * @return
     */
    public boolean checkLoginNameOnlyOne(String loginName, String id) {
        Integer num = userDao.checkLoginNameUnique(loginName, id);
        return (num != null) && (num == 1);
    }

    /**
     * 单租户下检验学号已存在。
     * 返回true表示不存在，false表示已存在.
     * id 为Null时表示注册时校验，id不为Null时表示修改学号校验
     * @param no 学号
     * @param id 用户ID
     * @return
     */
    public boolean checkNoUniqueByTenant(String no, String id) {
        Integer num = userDao.checkNoUniqueByTenant(no, id);
        return  StringUtils.isEmpty(num) || num == 0;
    }

    /**
     * 全局检验学号已存在。
     * 返回true表示不存在，false表示已存在.
     * id 为Null时表示注册时校验，id不为Null时表示修改学号校验
     * @param no 学号
     * @param id 用户ID
     * @return
     */
    public boolean checkNoUnique(String no, String id) {
        Integer num = userDao.checkNoUnique(no, id);
        return  StringUtils.isEmpty(num) || num == 0;
    }

    public Integer checkUserNoUnique(String no, String userId) {
        return userDao.checkUserNoUnique(no, userId);
    }

    /**
     * 全局检验手机号已存在。
     * 返回true表示不存在，false表示已存在.
     * id 为Null时表示注册时校验，id不为Null时表示修改手机号校验
     * @param mobile 手机号
     * @param id 用户ID
     * @return
     */
    public boolean checkMobileUnique(String mobile, String id) {
        Integer num = userDao.checkMobileUnique(mobile, id);
        return  StringUtils.isEmpty(num) || num == 0;
    }


    ///////////////// Synchronized to the Activiti end //////////////////


    public List<Role> findListByUserId(String userId) {
        return roleDao.findListByUserId(userId);
    }

    public List<Role> findListByUserIds(List<String> ids) {
        return roleDao.findListByUserIds(ids);
    }

    /**
     * 批量添加用户角色.
     *
     * @param rid  角色ID
     * @param uids 用户IDS
     */
    @Transactional(readOnly = false)
    public ApiTstatus<List<String>> insertPLUserRole(String rid, List<String> uids) {
        if (StringUtil.isNotEmpty(rid)) {
            Role role = new Role(rid);
            List<String> repairedIds = Lists.newArrayList();
            for (String id : uids) {
                User user = new User(id);
                user.getRoleList().add(role);
                insertUserRole(user);
                repairedIds.add(id);
            }
            return new ApiTstatus<List<String>>(true, "修复成功，角色ID为:[" + rid + "],共修复 " + repairedIds.size() + "条", repairedIds);
        }
        return new ApiTstatus<List<String>>(false, "修复失败,角色ID为空!");
    }


    /**
     * 添加用户角色.
     *
     * @param user
     */
    @Transactional(readOnly = false)
    public void insertUserRole(User user) {
        userDao.insertUserRole(user);
    }

    @Transactional(readOnly = false)
    public void deleteUser(User user) {
        userDao.delete(user);
        // 同步到Activiti
        deleteActivitiUser(user);
        // 清除用户缓存
        CoreUtils.clearCache(user);
//      // 清除权限缓存
//      systemRealm.clearAllCachedAuthorizationInfo();
    }

    @Transactional(readOnly = false)
    public void updateUserLoginName(User user){
        user = User.dealLoginName(user);
        user = User.dealOffice(user);
        userDao.updateUserLoginName(user.getId(), user.getLoginName());
        // 清除用户缓存
        CoreUtils.removeUserAll(user);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CoreSval.isSynActivitiIndetity()) {
            return;
        }
        if (isSynActivitiIndetity) {
            isSynActivitiIndetity = false;
            // 同步角色数据
            List<Group> groupList = identityService.createGroupQuery().list();
            if (groupList.size() == 0) {
                Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
                while (roles.hasNext()) {
                    Role role = roles.next();
                    saveActivitiGroup(role);
                }
            }
            // 同步用户数据
            List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
            if (userList.size() == 0) {
                Iterator<User> users = userDao.findAllList(new User()).iterator();
                while (users.hasNext()) {
                    saveActivitiUser(users.next());
                }
            }
        }
    }

    private void saveActivitiGroup(Role role) {
        if (!CoreSval.isSynActivitiIndetity()) {
            return;
        }
        String groupId = role.getEnname();

        // 如果修改了英文名，则删除原Activiti角色
        if (StringUtil.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())) {
            identityService.deleteGroup(role.getOldEnname());
        }

        Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
        if (group == null) {
            group = identityService.newGroup(groupId);
        }
        group.setName(role.getName());
        group.setType(role.getRoleType());
        identityService.saveGroup(group);

        // 删除用户与用户组关系
        List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
        for (org.activiti.engine.identity.User activitiUser : activitiUserList) {
            identityService.deleteMembership(activitiUser.getId(), groupId);
        }

        // 创建用户与用户组关系
        List<User> userList = findUser(new User(new Role(role.getId())));
        for (User e : userList) {
            String userId = e.getLoginName();//ObjectUtils.toString(user.getId());
            // 如果该用户不存在，则创建一个
            org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
            if (activitiUser == null) {
                activitiUser = identityService.newUser(userId);
                activitiUser.setFirstName(e.getName());
                activitiUser.setLastName(StringUtil.EMPTY);
                activitiUser.setEmail(e.getEmail());
                activitiUser.setPassword(StringUtil.EMPTY);
                identityService.saveUser(activitiUser);
            }
            identityService.createMembership(userId, groupId);
        }
    }

    public void deleteActivitiGroup(Role role) {
        if (!CoreSval.isSynActivitiIndetity()) {
            return;
        }
        if (role != null) {
            String groupId = role.getEnname();
            identityService.deleteGroup(groupId);
        }
    }

    public void saveActivitiUser(User user) {
        if (!CoreSval.isSynActivitiIndetity()) {
            return;
        }
        String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
        org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
        if (activitiUser == null) {
            activitiUser = identityService.newUser(userId);
        }
        activitiUser.setFirstName(user.getName());
        activitiUser.setLastName(StringUtil.EMPTY);
        activitiUser.setEmail(user.getEmail());
        activitiUser.setPassword(StringUtil.EMPTY);
        identityService.saveUser(activitiUser);

        // 删除用户与用户组关系
        List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
        for (Group group : activitiGroups) {
            identityService.deleteMembership(userId, group.getId());
        }
        // 创建用户与用户组关系
        for (Role role : user.getRoleList()) {
            String groupId = role.getEnname();
            // 如果该用户组不存在，则创建一个
            Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            if (group == null) {
                group = identityService.newGroup(groupId);
                group.setName(role.getName());
                group.setType(role.getRoleType());
                identityService.saveGroup(group);
            }
            identityService.createMembership(userId, role.getEnname());
        }
    }

    private void deleteActivitiUser(User user) {
        if (!CoreSval.isSynActivitiIndetity()) {
            return;
        }
        if (user != null) {
            String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
            identityService.deleteUser(userId);
        }
    }



    /**
     * 获取Key加载信息
     */
    public static boolean printKeyLoadMessage() {
        /*StringBuilder sb = new StringBuilder();
        sb.append("\r\n======================================================================\r\n");
        sb.append("\r\n    欢迎使用 "+CoreSval.getConfig("productName")+"  - Powered By http://initiate.com\r\n");
        sb.append("\r\n======================================================================\r\n");
        System.out.println(sb.toString());*/
        return true;
    }

    public void addAdminUser(Set<String> roleIds) {
        if((Sval.EmPn.NCENTER.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            if(!roleIds.contains(CoreIds.NCE_SYS_ROLE_ADMIN.getId())){
                roleIds.add(CoreIds.NCE_SYS_ROLE_ADMIN.getId());//运营管理员
            }
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            if(!roleIds.contains(CoreIds.NPR_SYS_ROLE_ADMIN.getId())){
                roleIds.add(CoreIds.NPR_SYS_ROLE_ADMIN.getId());//省管理员
            }
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            if(!roleIds.contains(CoreIds.NSC_SYS_ROLE_ADMIN.getId())){
                roleIds.add(CoreIds.NSC_SYS_ROLE_ADMIN.getId());//学校管理员
            }
        }
    }

    public Role getAdminRoleTpl() {
        String roleId="";
        if((Sval.EmPn.NCENTER.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            roleId=CoreIds.NCE_SYS_ROLE_ADMIN.getId();
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            roleId=CoreIds.NPR_SYS_ROLE_ADMIN.getId();
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            roleId=CoreIds.NSC_SYS_ROLE_ADMIN.getId();
        }
        return roleDao.get(roleId);
    }

    public Role getAdminRole() {
        Role role = null;
        if((Sval.EmPn.NCENTER.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            role = getByRtype(CoreSval.Rtype.ADMIN_SYS_SC.getKey());
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            role = getByRtype(CoreSval.Rtype.ADMIN_PN.getKey());
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(CoreSval.getTenantCurrpn())){
            role = getByRtype(CoreSval.Rtype.ADMIN_SN.getKey());
        }
        return role;
    }
    /*************************************************************
     * 公共未处理的方法.
     *************************************************************/
}
