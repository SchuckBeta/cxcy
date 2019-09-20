package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.authorize.enums.TenantMenu;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.Menuv;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmMenu extends SupSytm<SytmvMenu> {
    private final static Logger logger = LoggerFactory.getLogger(SytmRole.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    public SytmMenu(SytmvMenu sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "菜单";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }

        if((this.sytmvo.getSytmvTenant() == null)){
            this.status.setMsg(name() + this.sytmvo.getSytmvTenant().getName() + "不能为空！");
        }

        this.sytmvo.setName(name());
        return true;
    }

    @Override
    public boolean run() {
        if(!before()){
            return false;
        }

        System.out.println(name() + "处理中...");
        SytmvTenant sytmvTenant = this.sytmvo.getSytmvTenant();
        SytmvRole sytmvRole = this.sytmvo.getSytmvRole();

        Menu proot = new Menu();
        proot.setParent(new Menu(CoreIds.NCE_SYS_TREE_PROOT.getId()));
        proot.setTenantId(sytmvTenant.getSysTenantTpl().getTenantId());
        Menu root =  menuDao.getRoot(proot);
//        Menu root =  menuDao.get(CoreIds.NCE_SYS_TREE_ROOT.getId());
        Menu menu = new Menu();
        menu.setParent(root);
        SytmvTenant.setTenant(sytmvTenant, menu);
//        List<String> rids = Lists.newArrayList();
        List<Menu> menus = menuDao.findListByTenant(menu);
        if(StringUtil.checkEmpty(menus)){
            menus = Lists.newArrayList();
        }
        menus.add(root);
        //this.sytmvo.setIsReset(true);
        if(this.sytmvo.isReset()){
            Menu pmenu = new Menu();
//            pmenu.setParent(new Menu(CoreIds.NCE_SYS_TREE_ROOT.getId()));
            pmenu.setTenantId(sytmvTenant.getTenantId());
            menuDao.deleteWLByTenant(pmenu);
//            menuDao.deleteRoeMenuWLByTenant();
        }

        List<Role> curRs = sytmvRole.getRoles();
        List<Menu> curNms = Lists.newArrayList();
        List<Menuv> curMenmus = Lists.newArrayList();
        SytmMenu.sortList(sytmvTenant.getTenantId(), curNms, curMenmus, Menuv.gen(menus), CoreIds.NCE_SYS_TREE_PROOT.getId());
        for (Menu cur: updateParent(curNms, curMenmus)) {
            cur.setTenantId(sytmvTenant.getTenantId());
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            menuDao.insert(cur);
            this.sytmvo.getMenus().add(cur);
        }

        if(StringUtil.checkNotEmpty(this.sytmvo.getMenus())){
            for (Role curr: curRs) {
                if((CoreSval.Rtype.ADMIN_SN.getKey()).equals(curr.getRtype())){
                    curr.setMenuList(this.sytmvo.getMenus());
                }else{
                    curr.getMenuList().addAll(Menuv.filters(TenantMenu.filters(curr.getRtype()), this.sytmvo.getMenus()));
                }

                if(StringUtil.checkNotEmpty(curr.getMenuList())){
                    roleDao.insertRoleMenu(curr);
                }
            }
//            menuDao.insertPL(this.sytmvo.getMenus());
//            menuDao.insertPLRoleMenu(this.sytmvo.getMenus());
        }

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }

    public static Menu genMenu(String tid, Menuv sysMenu, Menuv parent) {
        Menu cur = new Menu();
        BeanUtils.copyProperties(sysMenu, cur);
        cur.setId(IdGen.uuid());
        sysMenu.setNid(cur.getId());
        cur.setRid(sysMenu.getId());
        cur.setParent(parent);
        cur.setTenantId(tid);
        cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setCreateDate(DateUtil.newDate());
        cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setUpdateDate(DateUtil.newDate());
        return cur;
    }

    public static void sortList(String tid, List<Menu> nlist, List<Menuv> list, List<Menuv> sourcelist, String parentId) {
        for (int i=0; i<sourcelist.size(); i++) {
            Menuv e = sourcelist.get(i);

            if ((e.getParent() != null) && (e.getParent().getId() != null) && (e.getParent().getId().equals(parentId))) {
                list.add(e);
                nlist.add(genMenu(tid, e, Menuv.gen(e.getParent())));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    Menuv child = sourcelist.get(j);
                    if ((child.getParent() != null) && (child.getParent().getId() != null) && (child.getParent().getId().equals(e.getId()))) {
                        sortList(tid, nlist, list, sourcelist, e.getId());
                        break;
                    }
                }
            }
        }
    }

    public static List<Menu> updateParent(List<Menu> ms, List<Menuv> mvs) {
        for (Menuv mv: mvs) {
            for (Menu m : ms) {
                if((m.getParentIds()).contains(StringUtil.DOTH + mv.getId() + StringUtil.DOTH)){
                    m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + mv.getId() + StringUtil.DOTH), (StringUtil.DOTH + mv.getNid() + StringUtil.DOTH)));
                }

                if(m.getParent() == null){
                    continue;
                }

                if((m.getParent().getId()).equals(mv.getId())){
                    m.getParent().setId(mv.getNid());
                }
            }
        }
        return ms;
    }
}
