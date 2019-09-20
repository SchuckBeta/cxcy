package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.DictDao;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.*;
import com.oseasy.com.pcore.modules.sys.vo.Dictv;
import com.oseasy.com.pcore.modules.sys.vo.Menuv;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 初始化数据字典.
 */
public class SytmDict extends SupSytm<SytmvDict> {
    private final static Logger logger = LoggerFactory.getLogger(SytmDict.class);
    private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
    public SytmDict(SytmvDict sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "数据字典";
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
        Dict dict = new Dict();
        dict.getSqlMap().put("dsf","drop table if exists sys_dict_"+sytmvTenant.getTenantId());
        dictDao.runBySqlString(dict);
        dict.getSqlMap().put("dsf","CREATE TABLE sys_dict_"+sytmvTenant.getTenantId()+" SELECT * FROM sys_dict");
        dictDao.runBySqlString(dict);
        /*SytmvTenant.setTenant(sytmvTenant, dict);
        List<Dict> dicts = dictDao.findList(dict);
        if(this.sytmvo.isReset()){
            Dict pDict = new Dict();
            pDict.setTenantId(sytmvTenant.getTenantId());
            dictDao.delDictByTenantId(pDict);
        }
        List<Dictv> nmenuvs = Lists.newArrayList();
        List<Dictv> menuvs = Lists.newArrayList();
        sortList(sytmvTenant.getTenantId(), nmenuvs, menuvs, Dictv.gen(dicts), new Dictv(CoreIds.NCE_SYS_TREE_PROOT.getId()));
        List<Dictv> list = updateParent(nmenuvs, menuvs);
        for (Dict cur: list) {
            cur.setTenantId(sytmvTenant.getTenantId());
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            dictDao.insert(cur);
            this.sytmvo.getMenus().add(cur);
            //this.sytmvo.getMenus().add(cur);
        }*/
        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }

    public static Dictv genMenuv(String tid, Dictv sysMenu, Dictv parent) {
        Dictv cur = new Dictv();
        BeanUtils.copyProperties(parent, cur);
        cur.setId(IdGen.uuid());
        sysMenu.setNid(cur.getId());
        cur.setParentId(parent.getId());
        cur.setTenantId(tid);
        cur.setUpdateBy(new User(CoreIds.NCE_SYS_TREE_ROOT.getId()));
        cur.setUpdateDate(new Date());
        return cur;
    }

    public static void sortList(String tid, List<Dictv> nlist, List<Dictv> list, List<Dictv> sourcelist, Dictv parent) {
        for (int i=0; i<sourcelist.size(); i++) {
            Dictv e = sourcelist.get(i);
            if ((e.getParentId() != null)  && (e.getParentId().equals(parent.getId()))) {
                list.add(e);
                Dictv d = new Dictv();
                BeanUtils.copyProperties(e, d);
                d.setId(e.getParentId());
                nlist.add(genMenuv(tid, e, Dictv.gen(d)));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    Dictv child = sourcelist.get(j);
                    if ((child.getParentId() != null)  && (child.getParentId().equals(e.getId()))) {
                        sortList(tid, nlist, list, sourcelist, e);
                        break;
                    }
                }
            }
        }
    }

    public static List<Dictv> updateParent(List<Dictv> ms, List<Dictv> mvs) {
        for (Dictv mv: mvs) {
            for (Dictv m : ms) {
                if((m.getParentId()).equals(mv.getId())){
                    m.setParentId(mv.getNid());
                }
            }
        }
        return ms;
    }
}
