package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.Menuv;
import com.oseasy.com.pcore.modules.sys.vo.Officev;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.PinyinUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 初始化顶级机构下的子机构.
 */
public class SytmOfficeSub extends SupSytm<SytmvOfficeSub> {
    private final static Logger logger = LoggerFactory.getLogger(SytmOfficeSub.class);
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

    public SytmOfficeSub(SytmvOfficeSub sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "机构";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setStatus(false);
            this.status.setMsg(name() + "不能为Null！");
            logger.error(this.status.getMsg());
        }

        if((this.sytmvo.getSytmvOffice() == null) && StringUtil.isEmpty(this.sytmvo.getSytmvOffice().getId())){
            this.status.setStatus(false);
            this.status.setMsg(name() + this.sytmvo.getSytmvOffice().getName() + "ID不能为空！");
            logger.error(this.status.getMsg());
        }

        this.sytmvo.setName(name());
        return true;
    }

    @Override
    public boolean run() {
        if(!before()){
            return false;
        }
        SytmvOffice sytmvOffice = this.sytmvo.getSytmvOffice();
        System.out.println("" +sytmvOffice.getOffice().getName());
        Office office = new Office(sytmvOffice.getOfficeTpl());
        SytmvTenant.setTenant(sytmvOffice, office);
        List<Office> offices = officeDao.findByParentIds(office);

        if(this.sytmvo.isReset()){
            office.setTenantId(sytmvOffice.getOffice().getTenantId());
            officeDao.deleteWLByParentIds(office);
        }

        List<Office> curOffices = Lists.newArrayList();
        List<Officev> curOfficevs = Lists.newArrayList();
        SytmOfficeSub.sortList(sytmvOffice.getOffice().getTenantId(), curOffices, curOfficevs, Officev.gen(offices), sytmvOffice.getOfficeTpl());

        curOffices.add(sytmvOffice.getOffice());
        Officev ov = Officev.gen(sytmvOffice.getOffice());
        ov.setNid(sytmvOffice.getOffice().getId());
        curOfficevs.add(ov);
        for (Office sysOffice: updateParent(curOffices, curOfficevs, sytmvOffice.getOffice())) {
            Soffice sentity = new Soffice(genProp(sysOffice));
            Office cur = new Office();
            BeanUtils.copyProperties(sysOffice, cur);
            if(!updateProp(sentity, cur)){
                cur.setName(sysOffice.getParent().getName()+"_"+cur.getName());
            }
            if((sytmvOffice.getOffice() != null) && StringUtil.isNotEmpty(sytmvOffice.getOffice().getCode())){
                cur.setCode(sytmvOffice.getOffice().getCode() + StringUtil.genNumStr(4));
            }else{
                cur.setCode(StringUtil.genNumStr(4));
            }

            cur.setRegval(null);
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            officeDao.insert(cur);
            this.sytmvo.getSubs().add(cur);
        }

        if(StringUtil.checkNotEmpty(this.sytmvo.getSubs())){
//            officeDao.insertPL(this.sytmvo.getSubs());
        }
        System.out.println(name() + "处理完成...");

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }

    public static Office genOffice(String tid, Officev sysOffice, Officev parent) {
        sysOffice.setParent(parent);
        Office cur = new Office();
        BeanUtils.copyProperties(sysOffice, cur);
        cur.setId(IdGen.uuid());
        sysOffice.setNid(cur.getId());
        cur.setRid(sysOffice.getId());
        cur.setParent(parent);
        cur.setTenantId(tid);
        cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setCreateDate(DateUtil.newDate());
        cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setUpdateDate(DateUtil.newDate());
        return cur;
    }

    public static void sortList(String tid, List<Office> nlist, List<Officev> list, List<Officev> sourcelist, Office parent) {
        for (int i=0; i<sourcelist.size(); i++) {
            Officev e = sourcelist.get(i);

            if ((e.getParent() != null) && (e.getParent().getId() != null) && (e.getParent().getId().equals(parent.getId()))) {
                e.setParent(parent);
                list.add(e);
                nlist.add(genOffice(tid, e, Officev.gen(e.getParent())));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    Officev child = sourcelist.get(j);
                    if ((child.getParent() != null) && (child.getParent().getId() != null) && (child.getParent().getId().equals(e.getId()))) {
                        child.setParent(e);
                        sortList(tid, nlist, list, sourcelist, e);
                        break;
                    }
                }
            }
        }
    }

    public static List<Office> updateParent(List<Office> ms, List<Officev> mvs, Office root) {
        for (Office m : ms) {
            if((m.getParentId()).equals(root.getRid()) || (m.getParent().getId()).equals(root.getRid())){
                m.getParent().setId(root.getId());
                m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + root.getRid() + StringUtil.DOTH), (StringUtil.DOTH + root.getId() + StringUtil.DOTH)));
            }

            if((m.getParentIds()).contains(StringUtil.DOTH + root.getRid() + StringUtil.DOTH)){
                m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + root.getRid() + StringUtil.DOTH), (StringUtil.DOTH + root.getId() + StringUtil.DOTH)));
            }

            if(StringUtil.isNotEmpty(root.getCode())){
                m.setCode(root.getCode() + StringUtil.genNumStr(4));
            }else{
                m.setCode(StringUtil.genNumStr(4));
            }

            for (Officev mv: mvs) {
                if((m.getParentIds()).contains(StringUtil.DOTH + mv.getId() + StringUtil.DOTH)){
                    m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + mv.getId() + StringUtil.DOTH), (StringUtil.DOTH + mv.getNid() + StringUtil.DOTH)));
                }

                if(m.getParent() == null){
                    continue;
                }

                if((m.getParentId()).equals(root.getRid()) || (m.getParent().getId()).equals(mv.getId())){
                    m.getParent().setId(mv.getNid());
                }
            }
        }
        ms.remove(root);
        return ms;
    }

    /**
     * 生成属性值.
     * 格式：[专业#PREF%s][学院#PREF%s]
     * @param entity Office 正则参数{名称}
     * @return String 格式：{名称|编码}
     */
    public static String[] genProp(Office entity){
        if(StringUtil.isEmpty(entity.getRegval())){
            return null;
        }

        return StringUtil.genRegex(entity.getRegval(), new String[]{entity.getParent().getCode()});
    }

    public static boolean updateProp(Soffice sentity, Office entity){
        if((sentity != null) && StringUtil.isNotEmpty(sentity.getName())  && StringUtil.isNotEmpty(sentity.getCode())){
            entity.setName(sentity.getName());
            entity.setCode(sentity.getCode());
            return true;
        }
        return false;
    }

    /**
     * 格式：{名称|登录名|密码}
     */
    class Soffice{
        private String name;
        private String code;

        public Soffice(String[] params) {
            if((params != null) && (params.length == 2)){
                this.name = params[0];
                this.code = params[1];
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
