/**
 * .
 */

package com.oseasy.com.pcore.modules.sys.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.persistence.TreeLver;
import com.oseasy.com.pcore.common.persistence.UserEntity;
import com.oseasy.com.pcore.modules.authorize.enums.TenantMenu;
import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.vo.Rvo;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.com.pcore.modules.syt.dao.SysTenantDao;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.util.common.utils.rsa.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.security.Digests;
import com.oseasy.com.pcore.common.security.shiro.session.JedisSessionDAO;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.web.Servlets;
import com.oseasy.com.pcore.modules.authorize.service.AuthorizeService;
import com.oseasy.com.pcore.modules.sys.dao.AreaDao;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.com.pcore.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.Encodes;
import com.oseasy.util.common.utils.StringUtil;

import static com.oseasy.com.pcore.common.config.CoreSval.isChangeTcvo;

/**
 * .
 * @author chenhao
 *
 */
public class CoreUtils {
    public static final int SALT_SIZE = 8;
    public static final int HASH_INTERATIONS = 1024;
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final String USER_PSW_DEFAULT = "123456";
    public static final String SYS_USER_MODIFY_PWD = "/sys/user/modifyPwd";
    public static final String OA_NOTIFY_MSG_LIST = "/oa/oaNotify/msgRecList";

    private static AuthorizeService authorizeService = SpringContextHolder.getBean(AuthorizeService.class);
    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
    private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
    private static SysTenantDao sysTenantDao = SpringContextHolder.getBean(SysTenantDao.class);

    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    public static final String CACHE_AUTH_INFO = "authInfo";
    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
    public static final String CACHE_OFFICE = "office";
    public static final String CACHE_TOKEN = "token";
    public static final String CACHE_TENANT = "tenant";//当前操作生效的租户


    /*************************************************************
     * 公共处理的方法.
     *************************************************************/
    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String plainPassword) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
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
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, CoreUtils.HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }

    /**
     * 获取当前用户 (has rolelist)
     * @return 取不到返回 new User()
     */
    public static User getUser() {
        Principal principal = getPrincipal();
        if (principal!=null) {
            User user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new User();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new User();
    }

    public static Integer getUserRtype(User user) {
        Integer maxAuth = Integer.parseInt(CoreSval.Rtype.OTHER.getKey());
        if((user != null) && StringUtil.checkNotEmpty(user.getRoleList())){
            Integer cur = null;
            for (Role role: user.getRoleList()) {
                cur = Integer.parseInt(role.getRtype());
                if(maxAuth > cur){
                    maxAuth = cur;
                }
            }
        }
        return maxAuth;
    }

    public static User getUserTenant() {
        Principal principal = getPrincipal();
        if (principal!=null) {
            User user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new User();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new User();
    }
    /**
     * 根据域名获取当前租户
     * @return SysTenant
     */
    public static SysTenant getTenant(String www) {
        return sysTenantDao.getByDomainName(www);
    }

    /**
     * 根据id获取当前租户
     * @return SysTenant
     */
    public static SysTenant getTenantById(String id) {
        return sysTenantDao.getByTenant(id);
    }

    /**
     * 根据id获取当前用户权限Rvo.
     * @return UserRvo
     */
    public static Map<String, List<String>> getUrvo() {
        Rvo urvo = Rvo.check(UserUtils.getUser());
        if(((CoreSval.Const.YES).equals(urvo.getIsSuper()))){
            return urvo.getSuperFilters();
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsAdmin()))){
            return urvo.getAdminFilters();
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsAdmyw()))){
            return urvo.getAdmywFilters();
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsNprAdmin()))){
            return urvo.getNprAdminFilters();
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsNscAdmin()))){
            return urvo.getNscAdminFilters();
        }
        return Maps.newHashMap();
    }

    /**has rolelist
     * 根据ID获取用户
     * @param id
     * @return 取不到返回null
     */
    public static User get(String id) {
        User user = (User)JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(id),id);

//                CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user ==  null) {
            user = userDao.getNtenant(id);
            if (user == null) {
                return null;
            }
            Role role = new Role(user);
            //role.setTenantId(TenantConfig.getCache(TenantCvtype.WWW));
            user.setRoleList(roleDao.findListNtenant(new Role(user)));
            JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(id),id,user);
            JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(id),user.getLoginName(),user);
//            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
//            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }
    /**
     * 获取所有角色列表
     * @return
     */
    public static List<Role> getAllRoleList() {
        @SuppressWarnings("unchecked")
        List<Role> roleList = (List<Role>)
                JedisUtils.hashGet(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CACHE_ROLE_LIST);
//                getCache(CACHE_ROLE_LIST);
        if (roleList == null) {
            roleList = roleDao.findAllList(new Role());
            JedisUtils.hashSetKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CACHE_ROLE_LIST,roleList);
//            putCache(CACHE_ROLE_LIST, roleList);
        }
        return roleList;
    }

    public static String getRoleBizType(String roleid){
        if(StringUtil.isEmpty(roleid)){
            return null;
        }
        List<Role> rs=getAllRoleList();
        if(rs!=null&&rs.size()>0){
            for(Role r:rs){
                if(roleid.equals(r.getId())){
                    return r.getBizType();
                }
            }
        }
        return null;
    }

    public static boolean checkHasRoleBizType(User user,String rolebiztype) {
        if(user==null||StringUtil.isEmpty(user.getId())||StringUtil.isEmpty(rolebiztype)){
            return false;
        }
        List<Role> rs=user.getRoleList();
        if(rs==null||rs.size()==0){
            throw new RuntimeException("该用户没有角色 User:[" + user.getId() + "] ->RoleBizTypeEnum:[" + rolebiztype + "]");
        }
        for(Role r:rs){
            if(StringUtil.isEmpty(r.getBizType())){
                r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            }
            if(rolebiztype.equals(r.getBizType())){
                return true;
            }
        }
        return false;
    }

    /**检查用户是否有某个角色
     * @param user user对象中有role list
     * @param rbtype
     * @return
     */
    public static boolean checkHasRole(User user,String rbtype) {
        if(user==null||StringUtil.isEmpty(user.getId())|| StringUtil.isEmpty(rbtype)){
            return false;
        }else{
            if(StringUtil.checkEmpty(user.getRoleList())){

                user =get(user.getId());
            }
        }
        List<Role> rs=user.getRoleList();
        if(StringUtil.checkEmpty(rs)){
            throw new RuntimeException("该用户没有指定角色 User:[" + user.getId() + "] ->rbtype:[" + rbtype + "]");
        }
        for(Role r:rs){
            if(r == null){
                continue;
            }
            if(StringUtil.isEmpty(r.getBizType())){
                r.setBizType(getRoleBizType(r.getId()));
            }
            if((rbtype).equals(r.getBizType())){
                return true;
            }
        }
        return false;
    }

    /**检查用户是否有某个角色
     * @param user user对象中有role list
     * @param rbtype
     * @return
     */
    public static boolean checkHasRole(User user,CoreSval.Rtype rtype) {
        if(user==null||StringUtil.isEmpty(user.getId())|| (rtype == null)){
            return false;
        }else{
            if(StringUtil.checkEmpty(user.getRoleList())){

                user =get(user.getId());
            }
        }
        List<Role> rs=user.getRoleList();
        if(StringUtil.checkEmpty(rs)){
            throw new RuntimeException("该用户没有指定角色 User:[" + user.getId() + "] ->rtype:[" + rtype.getKey() + "]");
        }
        for(Role r:rs){
            if(r == null){
                continue;
            }
            if(StringUtil.isEmpty(r.getBizType())){
                r.setBizType(getRoleBizType(r.getId()));
            }
            if((rtype.getKey()).equals(r.getRtype())){
                return true;
            }
        }
        return false;
    }

    /**根据编号判断授权信息
     * @param num MenuPlusEnum 枚举值序号从0开始
     * @return
     */
    public static boolean checkMenuByNum(Integer num) {
//        return authorizeService.checkMenuByNum(num);
        return authorizeService.checkMenuByNumOfTenant(num);
    }

    public static boolean checkCategory(String id) {
//        return authorizeService.checkCategory(id);
        return authorizeService.checkCategoryOfTenant(id);
    }

    public static String hiddenMobile(String mobile) {
        return StringUtil.hiddenStr(mobile, 3, 4, 4);
    }

    public static boolean checkChildMenu(String id) {
//        return authorizeService.checkChildMenu(id);

        /**
         * 检查当前用户是否存在.
         */
        User curUser = UserUtils.getUser();
        if((curUser == null) || StringUtil.checkEmpty(curUser.getRoleIdList())){
            return false;
        }

        /**
         * 检查当前用户是否为超级管理员角色.
         */
        if((curUser.getRoleIdList()).contains(CoreIds.NCE_SYS_ROLE_SUPER.getId())){
            return true;
        }
        if (!authorizeService.checkChildMenuOfTenant(id)){
            return false;
        }
        /**
         * 运营系统管理员可查看授权的菜单.
         */
        if(curUser.getRoleIdList().contains(CoreIds.NCE_SYS_ROLE_ADMIN.getId()) ||
                curUser.getRoleIdList().contains(CoreIds.NCE_SYS_ROLE_ADMYW.getId())){
            return true;
        }
        return true;
    }

    public static boolean checkMenu(String id) {

        /**
         * 检查当前用户是否存在.
         */
        User curUser = UserUtils.getUser();
        if((curUser == null) || StringUtil.checkEmpty(curUser.getRoleIdList())){
            return false;
        }

        /**
         * 检查当前用户是否为超级管理员角色.
         */
        if((curUser.getRoleIdList()).contains(CoreIds.NCE_SYS_ROLE_SUPER.getId())){
            return true;
        }

        /**
         * 检查是否有证书.
         */
//        if(!authorizeService.checkMenu(id)){
        if(!authorizeService.checkMenuOfTenant(id)){
            return false;
        }



        /**
         * 非超级管理员角色检查菜单授权.
         */
        Menu menu = menuDao.get(id);
        if((menu == null) || StringUtil.isEmpty(menu.getId())){
            return false;
        }

        /**
         * 运营系统管理员可查看授权的菜单.
         */
        if(curUser.getRoleIdList().contains(CoreIds.NCE_SYS_ROLE_ADMIN.getId()) ||
                curUser.getRoleIdList().contains(CoreIds.NCE_SYS_ROLE_ADMYW.getId())){
            return true;
        }

        Menu pmenu = new Menu();
        pmenu.setParentIds(menu.getParentIds() + menu.getId());
        List<Menu> filters = menuDao.findRoleMenuByParentIdsLike(curUser.getRoleIdList(), pmenu);
        if(StringUtil.checkEmpty(filters)){
            return false;
        }

        if((filters.size() == 1) && filters.contains(menu)){
            return false;
        }
        return true;
    }

    /*************************************************************
     * 公共未处理的方法.
     *************************************************************/




    /**
     * 根据登录名获取用户
     * @param loginName
     * @return 取不到返回null
     */
    public static User getByLoginName(String loginName) {
        User user =  (User)JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),loginName);
//                (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
        if (user == null) {
            user = userDao.getByLoginName(new User(null, loginName));
            if (user == null) {
                return null;
            }
            user.setRoleList(roleDao.findList(new Role(user)));
//            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
//            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
            JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),user.getLoginName(),user);
            JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),user.getId(),user);
        }
        return user;
    }

    public static User getByLoginNameOrNo(String loginNameOrNo) {
        User user =
                //(User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginNameOrNo);
        (User)JedisUtils.hashGet(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),loginNameOrNo);

        if (user == null) {
            user = userDao.getByLoginNameOrNo(loginNameOrNo,null);
            if (user == null) {
                return null;
            }
            JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),user.getLoginName(),user);
            JedisUtils.hashSetKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),user.getId(),user);
//            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
//            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }


    public static boolean isExistNo(String no) {
        User    user = userDao.getByLoginNameOrNo(no,null);
        if (user == null) {
            return false;
        }
        return true;
    }
    /**
     * 根据手机号获取用户
     * @param mobile
     * @return 取不到返回null
     */
    public static boolean isExistMobile(String mobile) {
        User p=new User();
        p.setMobile(mobile);
        User    user = userDao.getByMobile(p);
        if (user == null) {
            return false;
        }
        return true;
    }
    public static User getByMobile(String mobile) {
        User p=new User();
        p.setMobile(mobile);
        User    user = userDao.getByMobile(p);
        if (user == null) {
            return null;
        }
        user.setRoleList(roleDao.findList(new Role(user)));
        return user;
    }
    public static User getByMobile(String mobile,String id) {
        User p = new User();
        p.setMobile(mobile);
        p.setId(id);
        User user = userDao.getByMobileWithId(p);
        if (user == null) {
            return null;
        }
        user.setRoleList(roleDao.findList(new Role(user)));
        return user;
    }

    public static Office getOffice(String ofid) {
        if (StringUtil.isNotEmpty(ofid)) {
            Office office = (Office)JedisUtils.hashGet(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),ofid);
//                    CacheUtils.get(CACHE_OFFICE,ofid);
                if (office == null) {
                office=officeDao.get(ofid);
                if (office!=null){
                    JedisUtils.hashSetKey(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),ofid,office);
//                    CacheUtils.put(CACHE_OFFICE,ofid,office);
                }
            }
            return office;
        }
        return null;
      }


    /**
     * 获取当前用户角色列表
     * @return
     */
    public static List<Role> getRoleList() {
        @SuppressWarnings("unchecked")
        List<Role> roleList = (List<Role>)JedisUtils.hashGet(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST);
//                getCache(CACHE_ROLE_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant());
        if (roleList == null) {
            User user = getUser();
            if (user.getAdmin()) {
                roleList = roleDao.findAllList(new Role());
            }else{
                Role role = new Role();
                //role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
                roleList = roleDao.findList(role);
            }
            JedisUtils.hashSetKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST,roleList);
//            JedisUtils.hashDel(RedisEnum.ROLE.getName()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_ROLE_LIST);
//            putCache(CACHE_ROLE_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant(), roleList);
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     * @return
     */
    public static List<Menu> getMenuList() {
        @SuppressWarnings("unchecked")
        List<Menu> menuList = (List<Menu>)JedisUtils.hashGet(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_MENU_LIST);
//                getCache(CACHE_MENU_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant());
        //menuList = null;
        if (menuList == null) {
            User user = getUser();
            //判断当前用户的租户id 和切换的id 是否一致
            if(!isChangeTcvo()){
                menuList = menuDao.findAllList(new Menu());
            }else{
                if (UserEntity.isSuper(user)) {
                    menuList = menuDao.findAllList(new Menu());
                }else{
                    Menu m = new Menu();
                    m.setUserId(user.getId());
                    menuList = menuDao.findByUserId(m);
                }
            }
            JedisUtils.hashSetKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_MENU_LIST,menuList);

//            putCache(CACHE_MENU_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant(), menuList);
        }
        return menuList;
    }

    public static SimpleAuthorizationInfo getSimpleAuthorizationInfo(SimpleAuthorizationInfo info) {
        List<Menu> list = CoreUtils.getMenuList();
        for (Menu menu : list) {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                // 添加基于Permission的权限信息
                for (String permission : StringUtils.split(menu.getPermission(),",")) {
                    info.addStringPermission(permission);
                }
            }
        }
        return info;
    }

    /**
     * 获取当前所有用户授权菜单
     * @return
     */
    public static List<Menu> getAllMenuList() {
        Menu m = new Menu();
        m.setTenantId(TenantConfig.getCacheTenant());
        User user = getUser();
        List<Menu> menuList;
        if (UserEntity.isSuper(user)) {
            menuList = menuDao.findAllList(m);
        }else if (UserEntity.isAdmin(user) || UserEntity.isAdmyw(user)) {
            menuList = menuDao.findAllList(m);
        }else{
            m.setUserId(user.getId());
            menuList= menuDao.findAllListIndex(m);
        }
        menuList = TenantMenu.yymenufilters(menuList);
        return menuList;
    }

    /**
     * 获取当前所有用户授权菜单
     * @return
     */
    public static Menu getRoot() {
        Menu m = new Menu();
        m.setParent(new Menu(CoreIds.NCE_SYS_TREE_PROOT.getId()));
        m.setTenantId(TenantConfig.getCacheTenant());
        return menuDao.getRoot(m);
    }

    /**
     * 根据Href查找菜单.
     * @param m Menu
     * @return List
     */
    public static List<Menu> findListByHref(Menu m) {
        return menuDao.findListByHref(m);
    }

    /**
     * 根据Href获取当前用户菜单的L2级菜单.
     * href、lver必填
     * @param m Menu
     * @return List
     */
    public static List<Menu> getRootsByHref(Menu m) {
        if(StringUtil.isEmpty(m.getHref()) || (m.getLver() == null)){
            return null;
        }

        List<Menu> menus = findListByHref(m);
        List<String> ids = Lists.newArrayList();
        for (Menu cur:menus) {
            ids.addAll(Arrays.asList(StringUtil.split(cur.getParentIds(), StringUtil.DOTH)));
        }
        if(StringUtil.checkEmpty(ids)){
            return Lists.newArrayList();
        }
        m.setIds(ids);
        m.setLver(TreeLver.L2.getKey());
        return menuDao.findLlistByLver(m);
    }

    /**
     * 根据Href获取当前用户菜单的菜单.
     * href、lver必填
     * @return Menu
     */
    public static Menu getRootByHref(Menu m) {
        List<Menu> menus = getRootsByHref(m);
        if(StringUtil.checkOne(menus)){
            return menus.get(0);
        }
        return null;
    }

    /**
     * 获取当前用户的个人信息页面父级菜单.
     * @return Menu
     */
    public static Menu getPwd() {
        Menu m = new Menu();
        m.setHref(SYS_USER_MODIFY_PWD);
        m.setLtype(TenantMenu.S1.getId());
        m.setLver(TreeLver.L2.getKey());
        return getRootByHref(m);
    }

    /**
     * 获取当前用户的消息通知页面父级菜单.
     * @return Menu
     */
    public static Menu getMsg(String href) {
        Menu m = new Menu();
        m.setHref(OA_NOTIFY_MSG_LIST);
        m.setLtype(TenantMenu.S1.getId());
        m.setLver(TreeLver.L2.getKey());
        return getRootByHref(m);
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static boolean logout(Subject subject) {
        CoreUtils.removeAll();
        TenantConfig.clearCache(TenantCvtype.CHANGE);
        subject.logout();
        System.out.println("----------------------logout start");
        System.out.println("logout = "+TenantConfig.getConfig().toString());
        System.out.println("----------------------logout end");
        return true;
    }

    public static boolean logout(Subject subject, ServletRequest request, ServletResponse response) {
        CoreUtils.removeAll();
        TenantConfig.clearCache((HttpServletRequest)request, response);
        subject.logout();
        return true;
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try{
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal)subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        }catch (UnavailableSecurityManagerException e) {

        }catch (InvalidSessionException e) {

        }
        return null;
    }

    public static Session getSession() {
        try{
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        }catch (InvalidSessionException e) {

        }
        return null;
    }

    /**
     * 登录成功后处理Session.
     * @param user
     * @return Session
     */
    public static Session dealSession(User user) {
        //TODO 登录时设置Session值
        return getSession();
    }
    public static Session dealPassword(String password) {
        Session session = getSession();
        if (session == null) {
            return session;
        }

        System.out.println("Password ~~~~~~~~~~~~~~~~~~~~~~~~~Start");
        if(StringUtil.isNotEmpty(password)){
            System.out.println("Password = " + password);
            System.out.println("Password MD5Util = " + MD5Util.string2MD5(password));
            updatePsw(session, password);
        }else{
            updatePsw(session, CoreUtils.USER_PSW_DEFAULT);
        }
        System.out.println("Password ~~~~~~~~~~~~~~~~~~~~~~~~~End");
        return session;
    }

    public static void updatePsw(Session session, String password) {
        session.setAttribute(User.PSW_MW, password);
        session.setAttribute(User.PSW_MD5, MD5Util.string2MD5(password));
    }

    // ============== User Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
//      Object obj = getCacheMap().get(key);
        Object obj = getSession().getAttribute(key);
        return obj==null?defaultValue:obj;
    }

    public static void putCache(String key, Object value) {
//      getCacheMap().put(key, value);
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
//      getCacheMap().remove(key);
        getSession().removeAttribute(key);
    }

    /**
     * 跳转登录页面.
     * @return String
     */
    public static String toLogin() {
      return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/toLogin";
    }

    public static boolean isAdmin(User user){
        String roleIds = StringUtil.listIdToStr(user.getRoleList());
        return roleIds != null && (roleIds).contains(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
    }

    public static boolean isAdminOrSuperAdmin(User user){
        List<Role> roles =roleDao.findList(new Role(user));
        boolean isAdmin=false;
        if(StringUtil.checkNotEmpty(roles)){
            for(Role role:roles){
                if(role != null &&
                        (CoreIds.NSC_SYS_ROLE_ADMIN.getId().equals(role.getId())
                        || CoreIds.NCE_SYS_ROLE_SUPER.getId().equals(role.getId())
                        || CoreIds.NCE_SYS_ROLE_ADMIN.getId().equals(role.getId())
                        )){
                    isAdmin=true;
                    break;
                }
            }
        }
        return isAdmin;
    }

    /**检查用户是否有后台登录的角色
     * @param user user对象中有role list
//   * @param rolebiztype
     * @return
     */
    public static boolean checkHasAdminRole(User user) {
        if(user==null||StringUtil.isEmpty(user.getId())){
            return false;
        }else{
            user =get(user.getId());
        }
        List<Role> rs=user.getRoleList();
        if(StringUtil.checkEmpty(rs)){
            throw new RuntimeException("该用户没有角色 User:[" + user.getId() + "]");
        }
        for(Role r:rs){
            if(StringUtil.isEmpty(r.getBizType())){
                r.setBizType(getRoleBizType(r.getId()));
            }
            if(!RoleBizTypeEnum.XS.getValue().equals(r.getBizType())&&!RoleBizTypeEnum.DS.getValue().equals(r.getBizType())){
                return true;
            }
        }
        return false;
    }
    public static boolean checkHasAdminRole(User user, List<String> bizTypes) {
        if(user==null||StringUtil.isEmpty(user.getId())){
            return false;
        }else{
            user =get(user.getId());
        }
        List<Role> rs=user.getRoleList();
        if(rs==null||rs.size()==0){
            throw new RuntimeException("该用户没有角色 User:[" + user.getId() + "]");
        }
        for(Role r:rs){
            if(StringUtil.isEmpty(r.getBizType())){
                r.setBizType(getRoleBizType(r.getId()));
            }
            if(!(bizTypes).contains(r.getBizType())){
            //if(!RoleBizTypeEnum.XS.getValue().equals(r.getBizType())&&!RoleBizTypeEnum.DS.getValue().equals(r.getBizType())){
                return true;
            }
        }
        return false;
    }


    /**
     * 获取当前用户有权限访问的部门
     * @return
     */
    public static List<Office> getOfficeList() {
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>) JedisUtils.hashGet(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CACHE_OFFICE_ALL_LIST);

        if (officeList == null) {
            User user = getUser();
            Office qoffice = new Office();

            List<String> qfilter = Lists.newArrayList();
            String cur = CoreSval.getTenantCurrpn();
            Office entity = new Office();
            entity.setParent(new Office(CoreIds.NCE_SYS_TREE_PROOT.getId()));
            Office office = officeDao.getRoot(entity);
            if((office != null) && StringUtil.isNotEmpty(office.getId())){
                if((Sval.EmPn.NCENTER.getPrefix()).equals(cur)){
                    if(!(User.isSuper(user) || User.isAdmin(user))){
//                        qfilter.add(office.getId());
                    }
                }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(cur)){
                    if(!User.isNprAdmin(user)){
                        qfilter.add(office.getId());
                    }
                }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(cur)){
                    if(!User.isNscAdmin(user)){
                        qfilter.add(office.getId());
                    }
                }
            }

            qoffice.setQfilters(qfilter);
            qoffice.setTenantId(TenantConfig.getCacheTenant());
            if (User.isSuper(user) || User.isNprAdmin(user) || User.isNscAdmin(user)) {
//                officeList = officeDao.findAllList(qoffice);
                officeList = officeDao.findAllListByTenant(qoffice);
            }else{
//                officeList = officeDao.findList(qoffice);
                officeList = officeDao.findListByTenant(qoffice);
            }
            JedisUtils.hashSetKey(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CACHE_OFFICE_ALL_LIST,officeList);
//            JedisUtils.setObject(CACHE_OFFICE_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant(),officeList);
        }
        return officeList;
    }

    public static String getOfficeListJson() {
        return JsonMapper.toJsonString(CoreUtils.getOfficeList());
    }

    /**
     * 获取当前用户有权限访问的部门
     * @return
     */
    public static List<Office> getOfficeAllList() {
        @SuppressWarnings("unchecked")
//        List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);

        List<Office> officeList = (List<Office>)
                                JedisUtils.hashGet(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CACHE_OFFICE_ALL_LIST);
//                      JedisUtils.getObject(CACHE_OFFICE_LIST+StringUtil.LINE_D+TenantConfig.getCacheTenant());
        if (officeList == null) {
            officeList = officeDao.findAllList(new Office());
        }
        return officeList;
    }

    public static List<Office> getOfficeListFront() {
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>)
                JedisUtils.hashGet(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),"officeListFront");
//                getCache("officeListFront");
        if (officeList == null) {
            officeList = officeDao.findAllList(new Office());
            JedisUtils.hashSetKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),"officeListFront",officeList);
//            putCache("officeListFront", officeList);
        }
        return officeList;
    }

    /**
     * 获取当前用户授权的区域
     * @return
     */
    public static List<Area> getAreaList() {
        @SuppressWarnings("unchecked")
        List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
        if (areaList == null) {
            areaList = areaDao.findAllList(new Area());
            putCache(CACHE_AREA_LIST, areaList);
        }
        return areaList;
    }

    /**
     * 是否是验证码登录
     * @param isFail 计数加1
     * @param clean 计数清零
     * @return
     */
    public static boolean isValidateCodeLogin(boolean isFail, boolean clean) {
        Integer loginFailNum=null;
        String sessionId=null;
        HttpServletRequest request = Servlets.getRequest();
        if(request!=null){
            Cookie[] cs=request.getCookies();
            if(cs!=null){
                for(Cookie c:cs){
                    if(c!=null&&JedisSessionDAO.SessionIdName.equals(c.getName())){
                        sessionId=c.getValue();
                        break;
                    }
                }
            }
        }
        if(sessionId!=null){
            loginFailNum =(Integer)CacheUtils.get("loginFailMap", sessionId);
        }
        if (loginFailNum==null) {
            loginFailNum = 0;
        }
        if (isFail) {
            loginFailNum++;
            CacheUtils.put("loginFailMap", sessionId, loginFailNum);
        }
        if (clean) {
            CacheUtils.remove("loginFailMap", sessionId);
        }
        return loginFailNum >= 3;
    }

    public static String getVersion(){
        return CoreSval.getConfig("version");
    }

    /**
     * 获取租户ID,
     * 租户为空则弹框选择租户.
     * 1、若为空，然后取User缓存Key中的租户ID,
     * 2、若为空，最后取域名端口缓存Key中的租户ID.
     * @return String
     */
    public static TenantConfig getTconfig(){
        return TenantConfig.getConfig();
    }

    public static String getRealIp(){
        String sysTenantId = TenantConfig.getCacheTenant();
        SysTenant sysTenant = sysTenantDao.getByTenant(sysTenantId);
        if(null != sysTenant){
            return sysTenant.getDomainName();
        }else{
            return "";
        }
    }

    public static boolean getTenantIsOpen(){
        return CoreSval.getTenantIsopen();
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.ACTYW.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.PROMODEL.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.DICT.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant());
        CoreUtils.clearCache(getUserTenant());
    }

    /**
     * 清除指定用户缓存
     * @param user
     */
    public static void clearCache(User user) {
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
//        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
//        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
//        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
//        if (user.getOffice() != null && user.getOffice().getId() != null) {
//            CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
//        }
    }

    /**
     * 清理用户的所有缓存.
     * @param user
     */
    public static void removeUserAll(User user) {
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant(), user.getId());
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant(), user.getLoginName());
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant(), CoreUtils.USER_CACHE_ID_ + user.getId());
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant(), CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant(), UserUtils.CACHE_OANOTIFY_LIST);
        JedisUtils.hashDelByKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
    }

    public static void removeAll() {
        JedisUtils.hashDelByKey(RedisEnum.USER.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.ACTYW.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.PROMODEL.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.DICT.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
    }

    public static void removeOpenAll() {
        JedisUtils.hashDelByKey(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.ROLE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.ACTYW.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.PROMODEL.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant());
        JedisUtils.hashDelByKey(RedisEnum.DICT.getValue()+ StringUtil.MAOH+ TenantConfig.getCacheTenant());
    }

}

