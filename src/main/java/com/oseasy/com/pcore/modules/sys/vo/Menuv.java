package com.oseasy.com.pcore.modules.sys.vo;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.authorize.enums.TenantMenu;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/5/6 0006.
 */
public class Menuv extends Menu {
    private String nid;

    public Menuv() {
        super();
    }
    public Menuv(String id) {
        super(id);
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public static Menuv gen(Menu menu) {
        Menuv mv = new Menuv();
        BeanUtils.copyProperties(menu, mv);
        //BeanUtils.copyProperties(menu, mv, new String[]{"currentUser", "page"});
        return mv;
    }

    public static List<Menuv> gen(List<Menu> menus) {
        List<Menuv> mvs = Lists.newArrayList();
        for (Menu menu :menus){
            mvs.add(Menuv.gen(menu));
        }
        return mvs;
    }



    public static void main(String[] args) {
        String tid = "999";
        List<Menuv> mvs = Lists.newArrayList();
        Menuv top = new Menuv();
        top.setParent(new Menu("-"));
        top.setId("0");
        top.setParentIds("-");
        mvs.add(top);

        Menuv m1 = new Menuv();
        m1.setParent(top);
        m1.setId("1");
        m1.setParentIds("0,");
        mvs.add(m1);

        Menuv m11= new Menuv();
        m11.setParent(m1);
        m11.setId("11");
        m11.setParentIds("0,1,");
        mvs.add(m11);

        Menuv m111= new Menuv();
        m111.setParent(m11);
        m111.setId("111");
        m111.setParentIds("0,1,11,");
        mvs.add(m111);

        Menuv m1111= new Menuv();
        m1111.setParent(m111);
        m1111.setId("1111");
        m1111.setParentIds("0,1,11,111,");
        mvs.add(m1111);

        Menuv m12= new Menuv();
        m12.setParent(m1);
        m12.setId("12");
        m12.setParentIds("0,1,");
        mvs.add(m12);

        Menuv m121= new Menuv();
        m121.setParent(m12);
        m121.setId("121");
        m121.setParentIds("0,1,12,");
        mvs.add(m121);

        Menuv m1211= new Menuv();
        m1211.setParent(m121);
        m1211.setId("1211");
        m1211.setParentIds("0,1,12,121,");
        mvs.add(m1211);



        Menuv m2 = new Menuv();
        m2.setParent(top);
        m2.setId("2");
        m2.setParentIds("0,");
        mvs.add(m2);

        Menuv m21= new Menuv();
        m21.setParent(m2);
        m21.setId("21");
        m21.setParentIds("0,2,");
        mvs.add(m21);

        Menuv m211= new Menuv();
        m211.setParent(m21);
        m211.setId("211");
        m211.setParentIds("0,2,21,");
        mvs.add(m211);

        Menuv m2111= new Menuv();
        m2111.setParent(m211);
        m2111.setId("2111");
        m2111.setParentIds("0,2,21,211,");
        mvs.add(m2111);

        Menuv m22= new Menuv();
        m22.setParent(m2);
        m22.setId("22");
        m22.setParentIds("0,2,");
        mvs.add(m22);

        Menuv m221= new Menuv();
        m221.setParent(m22);
        m221.setId("221");
        m221.setParentIds("0,2,22,");
        mvs.add(m221);

        Menuv m2211= new Menuv();
        m2211.setParent(m221);
        m2211.setId("2211");
        m2211.setParentIds("0,2,22,221,");
        mvs.add(m2211);


        System.out.println("\n============================================================");
        System.out.println("处理前的结果：");
        printf(mvs, 0);

        List<Menuv> nmenuvs = Lists.newArrayList();
        List<Menuv> menuvs = Lists.newArrayList();
        sortList(tid, nmenuvs, menuvs, mvs, new Menuv("0"));
        System.out.println("\n============================================================");
        System.out.println("sortList结果：");
        printf(nmenuvs, 1);

        System.out.println("\n============================================================");
        System.out.println("updateParent结果：");
        updateParent(nmenuvs, menuvs);
        printf(nmenuvs, 2);
    }

    public static void sortList(String tid, List<Menuv> nlist, List<Menuv> list, List<Menuv> sourcelist, Menuv parent) {
        for (int i=0; i<sourcelist.size(); i++) {
            Menuv e = sourcelist.get(i);
            if ((e.getParent() != null) && (e.getParent().getId() != null) && (e.getParent().getId().equals(parent.getId()))) {
                list.add(e);
                nlist.add(genMenuv(tid, e, Menuv.gen(e.getParent())));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    Menuv child = sourcelist.get(j);
                    if ((child.getParent() != null) && (child.getParent().getId() != null) && (child.getParent().getId().equals(e.getId()))) {
                        sortList(tid, nlist, list, sourcelist, e);
                        break;
                    }
                }
            }
        }
    }

    public static List<Menuv> updateParent(List<Menuv> ms, List<Menuv> mvs) {
        for (Menuv mv: mvs) {
            for (Menuv m : ms) {
                if((m.getParentIds()).contains(StringUtil.DOTH + mv.getId() + StringUtil.DOTH)){
                    m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + mv.getId() + StringUtil.DOTH), (StringUtil.DOTH + mv.getNid() + StringUtil.DOTH)));
                }

                if((m.getParent().getId()).equals(mv.getId())){
                    m.getParent().setId(mv.getNid());
                }
            }
        }
        return ms;
    }

    public static Menuv genMenuv(String tid, Menuv sysMenu, Menuv parent) {
        Menuv cur = new Menuv();
        cur.setId(IdGen.uuid());
        cur.setParentIds(sysMenu.getParentIds());
        sysMenu.setNid(cur.getId());
        cur.setParent(parent);
        cur.setTenantId(tid);
        return cur;
    }

    /**
     * 根据Rtype 过滤菜单。
     * @param filtypes 过滤类型
     * @param menus 菜单
     * @return List
     */
    public static List<Menu> filters(List<Integer> filtypes, List<Menu> menus){
        List<Menu> cmenus = Lists.newArrayList();
        if(StringUtil.checkEmpty(filtypes)){
            return cmenus;
        }

        for (Menu cur : menus) {
            if((cur.getLtype() == null)){
                continue;
            }

            if((filtypes).contains(cur.getLtype())){
                cmenus.add(cur);
            }
        }
        return cmenus;
    }

    private static void printf(List<Menuv> mvs, int type) {
        String tl = "---";
        String tf = "\t";
        int tcols = 8;
        int tpcols = 8;
        if(type == 0){
            tl = "---";
            tcols = 8;
            tpcols = 8;
        }else if(type == 0){
            tl = "----";
            tcols = 12;
            tpcols = 18;
        }else if(type == 2){
            tl = "----";
            tcols = 12;
            tpcols = 20;
        }
        StringBuffer buf;
        for (Menuv mv: mvs) {
            buf = new StringBuffer();
            if((mv.getId()).equals("0")){
                buf.append(mv.getId());
                for (int i = 0; i < tcols; i++) {
                    buf.append(tf);
                }
            }else if(((mv.getId()).length() == 1) || ((mv.getId()).length() == 2) || ((mv.getId()).length() == 3)){
                for (int i = 0; i < (mv.getId()).length(); i++) {
                    buf.append(tl);
                }

                buf.append(mv.getId());
                for (int i = 0; i < tcols - (mv.getId()).length(); i++) {
                    buf.append(tf);
                }
            }else if((mv.getId()).length() >= 4){
                for (int i = 0; i < 4; i++) {
                    buf.append(tl);
                }

                buf.append(mv.getId());
                for (int i = 0; i < tcols - 4; i++) {
                    buf.append(tf);
                }
            }
            buf.append(mv.getParentIds());
            if(((mv.getParentIds()).length() < 4)){
                for (int i = 0; i < tpcols + 1; i++) {
                    buf.append(tf);
                }
            }else if(((mv.getParentIds()).length() == 7) || ((mv.getParentIds()).length() == 4)){
                for (int i = 0; i < tpcols; i++) {
                    buf.append(tf);
                }
            }else if(((mv.getParentIds()).length() >= 7)){
                for (int i = 0; i < tpcols - 1; i++) {
                    buf.append(tf);
                }
            }
            buf.append(mv.getParent().getId());
            System.out.println(buf.toString());
        }
    }
}
