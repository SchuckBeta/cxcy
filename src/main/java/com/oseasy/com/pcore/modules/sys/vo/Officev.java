package com.oseasy.com.pcore.modules.sys.vo;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/5/6 0006.
 */
public class Officev extends Office {
    private String nid;

    public Officev() {
        super();
    }
    public Officev(String id) {
        super(id);
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public static Officev gen(Office office) {
        Officev mv = new Officev();
        BeanUtils.copyProperties(office, mv);
//        BeanUtils.copyProperties(office, mv, new String[]{"currentUser", "page"});
        return mv;
    }

    public static List<Officev> gen(List<Office> offices) {
        List<Officev> mvs = Lists.newArrayList();
        for (Office office :offices){
            mvs.add(Officev.gen(office));
        }
        return mvs;
    }



    public static void main(String[] args) {
        String tid = "999";
        List<Officev> mvs = Lists.newArrayList();
        Officev top = new Officev();
        top.setParent(new Office("-"));
        top.setId("0");
        top.setParentIds("-");
        mvs.add(top);

        Officev m1 = new Officev();
        m1.setParent(top);
        m1.setId("1");
        m1.setParentIds("0,");
        mvs.add(m1);

        Officev m11= new Officev();
        m11.setParent(m1);
        m11.setId("11");
        m11.setParentIds("0,1,");
        mvs.add(m11);

        Officev m111= new Officev();
        m111.setParent(m11);
        m111.setId("111");
        m111.setParentIds("0,1,11,");
        mvs.add(m111);

        Officev m1111= new Officev();
        m1111.setParent(m111);
        m1111.setId("1111");
        m1111.setParentIds("0,1,11,111,");
        mvs.add(m1111);

        Officev m12= new Officev();
        m12.setParent(m1);
        m12.setId("12");
        m12.setParentIds("0,1,");
        mvs.add(m12);

        Officev m121= new Officev();
        m121.setParent(m12);
        m121.setId("121");
        m121.setParentIds("0,1,12,");
        mvs.add(m121);

        Officev m1211= new Officev();
        m1211.setParent(m121);
        m1211.setId("1211");
        m1211.setParentIds("0,1,12,121,");
        mvs.add(m1211);



        Officev m2 = new Officev();
        m2.setParent(top);
        m2.setId("2");
        m2.setParentIds("0,");
        mvs.add(m2);

        Officev m21= new Officev();
        m21.setParent(m2);
        m21.setId("21");
        m21.setParentIds("0,2,");
        mvs.add(m21);

        Officev m211= new Officev();
        m211.setParent(m21);
        m211.setId("211");
        m211.setParentIds("0,2,21,");
        mvs.add(m211);

        Officev m2111= new Officev();
        m2111.setParent(m211);
        m2111.setId("2111");
        m2111.setParentIds("0,2,21,211,");
        mvs.add(m2111);

        Officev m22= new Officev();
        m22.setParent(m2);
        m22.setId("22");
        m22.setParentIds("0,2,");
        mvs.add(m22);

        Officev m221= new Officev();
        m221.setParent(m22);
        m221.setId("221");
        m221.setParentIds("0,2,22,");
        mvs.add(m221);

        Officev m2211= new Officev();
        m2211.setParent(m221);
        m2211.setId("2211");
        m2211.setParentIds("0,2,22,221,");
        mvs.add(m2211);


        System.out.println("\n============================================================");
        System.out.println("处理前的结果：");
        printf(mvs, 0);

        List<Officev> nofficevs = Lists.newArrayList();
        List<Officev> officevs = Lists.newArrayList();
        sortList(tid, nofficevs, officevs, mvs, new Officev("0"));
        System.out.println("\n============================================================");
        System.out.println("sortList结果：");
        printf(nofficevs, 1);

        System.out.println("\n============================================================");
        System.out.println("updateParent结果：");
        updateParent(nofficevs, officevs);
        printf(nofficevs, 2);
    }

    public static void sortList(String tid, List<Officev> nlist, List<Officev> list, List<Officev> sourcelist, Officev parent) {
        for (int i=0; i<sourcelist.size(); i++) {
            Officev e = sourcelist.get(i);
            if ((e.getParent() != null) && (e.getParent().getId() != null) && (e.getParent().getId().equals(parent.getId()))) {
                list.add(e);
                nlist.add(genOfficev(tid, e, Officev.gen(e.getParent())));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    Officev child = sourcelist.get(j);
                    if ((child.getParent() != null) && (child.getParent().getId() != null) && (child.getParent().getId().equals(e.getId()))) {
                        sortList(tid, nlist, list, sourcelist, e);
                        break;
                    }
                }
            }
        }
    }

    public static List<Officev> updateParent(List<Officev> ms, List<Officev> mvs) {
        for (Officev mv: mvs) {
            for (Officev m : ms) {
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

    public static Officev genOfficev(String tid, Officev sysOffice, Officev parent) {
        Officev cur = new Officev();
        cur.setId(IdGen.uuid());
        cur.setParentIds(sysOffice.getParentIds());
        sysOffice.setNid(cur.getId());
        cur.setParent(parent);
        cur.setTenantId(tid);
        return cur;
    }

    public static void printf(List<Officev> mvs, int type) {
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
        for (Officev mv: mvs) {
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
