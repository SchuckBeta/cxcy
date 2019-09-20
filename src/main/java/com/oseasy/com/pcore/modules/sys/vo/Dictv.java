package com.oseasy.com.pcore.modules.sys.vo;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/5/6 0006.
 */
public class Dictv extends Dict {
    private String nid;

    public Dictv() {
        super();
        this.currentUser = new User();
    }
    public Dictv(String id) {
        super(id);
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public static Dictv gen(Dict menu) {
        Dictv mv = new Dictv();
        //BeanUtils.copyProperties(menu, mv);
        BeanUtils.copyProperties(menu, mv, new String[]{"currentUser", "page"});
        return mv;
    }

    public static List<Dictv> gen(List<Dict> menus) {
        List<Dictv> mvs = Lists.newArrayList();
        for (Dict menu :menus){
            mvs.add(Dictv.gen(menu));
        }
        return mvs;
    }



    public static void main(String[] args) {
        String tid = "999";
        List<Dictv> mvs = Lists.newArrayList();
        Dictv top = new Dictv();
        top.setParentId("-");
        top.setId("0");
        mvs.add(top);
        Dictv m1 = new Dictv();
        m1.setParentId(top.getId());
        m1.setId("1");
        m1.setValue("aaaa");
        mvs.add(m1);
        Dictv m11= new Dictv();
        m11.setParentId(m1.getId());
        m11.setId("11");
        mvs.add(m11);
        Dictv m12= new Dictv();
        m12.setParentId(m1.getId());
        m12.setId("12");
        mvs.add(m12);
        Dictv m2 = new Dictv();
        m2.setParentId(top.getId());
        m2.setId("2");
        mvs.add(m2);
        Dictv m21= new Dictv();
        m21.setParentId(m2.getId());
        m21.setId("21");
        mvs.add(m21);
        Dictv m22= new Dictv();
        m22.setParentId(m2.getId());
        m22.setId("22");
        mvs.add(m22);
        System.out.println("\n============================================================");
        System.out.println("处理前的结果：");
        printf(mvs, 0);
        List<Dictv> nmenuvs = Lists.newArrayList();
        List<Dictv> menuvs = Lists.newArrayList();
        sortList(tid, nmenuvs, menuvs, mvs, new Dictv("0"));
        System.out.println("\n============================================================");
        System.out.println("sortList结果：");
        printf(nmenuvs, 1);
        System.out.println("\n============================================================");
       System.out.println("updateParent结果：");
        updateParent(nmenuvs, menuvs);
        printf(nmenuvs, 2);
    }

    public static void sortList(String tid, List<Dictv> nlist, List<Dictv> list, List<Dictv> sourcelist, Dictv parent) {
        for (int i=0; i<sourcelist.size(); i++) {
            Dictv e = sourcelist.get(i);
            if ((e.getParentId() != null)  && (e.getParentId().equals(parent.getId()))) {
                list.add(e);
                Dictv d = new Dictv();
                BeanUtils.copyProperties(e, d, new String[]{"currentUser", "page"});
                d.setId(e.getParentId());

                nlist.add(genMenuv(tid, e, d));

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

    public static Dictv genMenuv(String tid, Dictv sysMenu, Dictv parent) {
        Dictv cur = new Dictv();
        BeanUtils.copyProperties(parent, cur, new String[]{"currentUser", "page"});
        cur.setId(IdGen.uuid());
        sysMenu.setNid(cur.getId());
        cur.setParentId(parent.getId());
        cur.setTenantId(tid);
        return cur;
    }

    private static void printf(List<Dictv> mvs, int type) {
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
        for (Dictv mv: mvs) {
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

            buf.append(mv.getParentId());
            System.out.println(buf.toString());

        }
    }
}
