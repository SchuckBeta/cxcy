package com.oseasy.act.modules.actyw.vo;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/5/7 0007.
 */
public class ActYwGnodev extends ActYwGnode {
    private String nid;

    public ActYwGnodev() {
    }

    public ActYwGnodev(String id) {
        super(id);
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public static ActYwGnodev gen(ActYwGnode gnode) {
        ActYwGnodev mv = new ActYwGnodev();
//        BeanUtils.copyProperties(gnode, mv);
        BeanUtils.copyProperties(gnode, mv, new String[]{"currentUser", "page"});
        return mv;
    }

    public static List<ActYwGnodev> gen(List<ActYwGnode> gnodevs) {
        List<ActYwGnodev> mvs = Lists.newArrayList();
        for (ActYwGnode gnodev :gnodevs){
            mvs.add(ActYwGnodev.gen(gnodev));
        }
        return mvs;
    }

    public static void main(String[] args) {
        String tid = "999";
        List<ActYwGnodev> mvs = Lists.newArrayList();
        ActYwGnodev top = new ActYwGnodev();
        top.setParent(new ActYwGnodev("-"));
        top.setId("0");
        top.setParentIds("-");
        mvs.add(top);

        ActYwGnodev m1 = new ActYwGnodev();
        m1.setParent(top);
        m1.setId("1");
        m1.setParentIds("0,");
        mvs.add(m1);

        ActYwGnodev m11= new ActYwGnodev();
        m11.setParent(m1);
        m11.setId("11");
        m11.setParentIds("0,1,");
        mvs.add(m11);

        ActYwGnodev m111= new ActYwGnodev();
        m111.setParent(m11);
        m111.setId("111");
        m111.setParentIds("0,1,11,");
        mvs.add(m111);

        ActYwGnodev m1111= new ActYwGnodev();
        m1111.setParent(m111);
        m1111.setId("1111");
        m1111.setParentIds("0,1,11,111,");
        mvs.add(m1111);

        ActYwGnodev m12= new ActYwGnodev();
        m12.setParent(m1);
        m12.setId("12");
        m12.setParentIds("0,1,");
        mvs.add(m12);

        ActYwGnodev m121= new ActYwGnodev();
        m121.setParent(m12);
        m121.setId("121");
        m121.setParentIds("0,1,12,");
        mvs.add(m121);

        ActYwGnodev m1211= new ActYwGnodev();
        m1211.setParent(m121);
        m1211.setId("1211");
        m1211.setParentIds("0,1,12,121,");
        mvs.add(m1211);



        ActYwGnodev m2 = new ActYwGnodev();
        m2.setParent(top);
        m2.setId("2");
        m2.setParentIds("0,");
        mvs.add(m2);

        ActYwGnodev m21= new ActYwGnodev();
        m21.setParent(m2);
        m21.setId("21");
        m21.setParentIds("0,2,");
        mvs.add(m21);

        ActYwGnodev m211= new ActYwGnodev();
        m211.setParent(m21);
        m211.setId("211");
        m211.setParentIds("0,2,21,");
        mvs.add(m211);

        ActYwGnodev m2111= new ActYwGnodev();
        m2111.setParent(m211);
        m2111.setId("2111");
        m2111.setParentIds("0,2,21,211,");
        mvs.add(m2111);

        ActYwGnodev m22= new ActYwGnodev();
        m22.setParent(m2);
        m22.setId("22");
        m22.setParentIds("0,2,");
        mvs.add(m22);

        ActYwGnodev m221= new ActYwGnodev();
        m221.setParent(m22);
        m221.setId("221");
        m221.setParentIds("0,2,22,");
        mvs.add(m221);

        ActYwGnodev m2211= new ActYwGnodev();
        m2211.setParent(m221);
        m2211.setId("2211");
        m2211.setParentIds("0,2,22,221,");
        mvs.add(m2211);


        System.out.println("\n============================================================");
        System.out.println("处理前的结果：");
        printf(mvs, 0);

        List<ActYwGnodev> nactYwGnodevs = Lists.newArrayList();
        List<ActYwGnodev> actYwGnodes = Lists.newArrayList();
        sortList(tid, nactYwGnodevs, actYwGnodes, mvs, top.getId());
        System.out.println("\n============================================================");
        System.out.println("sortList结果：");
        printf(nactYwGnodevs, 1);

        System.out.println("\n============================================================");
        System.out.println("updateParent结果：");
        updateParent(nactYwGnodevs, actYwGnodes);
        printf(nactYwGnodevs, 2);
    }

    public static void sortList(String tid, List<ActYwGnodev> nlist, List<ActYwGnodev> list, List<ActYwGnodev> sourcelist, String parentId) {
        for (int i=0; i<sourcelist.size(); i++) {
            ActYwGnodev e = sourcelist.get(i);
            if ((e.getParent() != null) && (e.getParent().getId() != null) && (e.getParent().getId().equals(parentId))) {
                list.add(e);
                nlist.add(genActYwGnodev(tid, e, ActYwGnodev.gen(e.getParent())));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    ActYwGnodev child = sourcelist.get(j);
                    if ((child.getParent() != null) && (child.getParent().getId() != null) && (child.getParent().getId().equals(e.getId()))) {
                        sortList(tid, nlist, list, sourcelist, e.getId());
                        break;
                    }
                }
            }
        }
    }

    public static List<ActYwGnodev> updateParent(List<ActYwGnodev> ms, List<ActYwGnodev> mvs) {
        for (ActYwGnodev mv: mvs) {
            for (ActYwGnode m : ms) {
                if((m.getParentIds()).contains(StringUtil.DOTH + mv.getId() + StringUtil.DOTH)){
                    m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + mv.getId() + StringUtil.DOTH), (StringUtil.DOTH + mv.getNid() + StringUtil.DOTH)));
                }

                if((m.getParent().getId()).equals(mv.getId())){
                    m.getParent().setId(mv.getNid());
                }

                if((m.getPreId()).equals(mv.getId())){
                    m.setPreId(mv.getNid());
                }
            }
        }
        return ms;
    }

    public static ActYwGnodev genActYwGnodev(String tid, ActYwGnodev sysActYwGnodev, ActYwGnodev parent) {
        ActYwGnodev cur = new ActYwGnodev();
        cur.setId(IdGen.uuid());
        cur.setParentIds(sysActYwGnodev.getParentIds());
        sysActYwGnodev.setNid(cur.getId());
        cur.setParent(parent);
        cur.setTenantId(tid);
        return cur;
    }

    private static void printf(List<ActYwGnodev> mvs, int type) {
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
        for (ActYwGnodev mv: mvs) {
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
