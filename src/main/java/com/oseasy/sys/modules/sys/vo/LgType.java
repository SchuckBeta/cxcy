package com.oseasy.sys.modules.sys.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/1 0001.
 * 系统全局登录类型.
 */
public enum LgType {
    STUDENT(true, "10", "学生", "学生", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NSCHOOL}, new CoreSval.Rtype[]{CoreSval.Rtype.STUDENT}),
    EXPORT(true, "20", "专家", "专家", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NPROVINCE, Sval.EmPn.NSCHOOL}, new CoreSval.Rtype[]{CoreSval.Rtype.EXPORT}),
    MANAGER(true, "30", "管理员", "管理员", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NPROVINCE, Sval.EmPn.NSCHOOL}, new CoreSval.Rtype[]{
        CoreSval.Rtype.SUPER_SC,
        CoreSval.Rtype.ADMIN_SYS_SC,
        CoreSval.Rtype.ADMIN_YW_SC,
        CoreSval.Rtype.ADMIN_PN,
        CoreSval.Rtype.ADMIN_SN,
        CoreSval.Rtype.MINISTER
    });

    private boolean enable;//是否开启
    private String key;//标识
    private String name;//
    private String remark;//
    private Sval.EmPn[] pns;//适用平台类型
    private CoreSval.Rtype[] rtypes;//适用角色类型

    private LgType(boolean enable, String key, String name, String remark, Sval.EmPn[] pns, CoreSval.Rtype[] rtypes) {
        this.enable = enable;
        this.key = key;
        this.pns = pns;
        this.rtypes = rtypes;
        this.name = name;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key 标识
     * @return LgType
     */
    public static LgType getByKey(String key) {
        if ((key != null)) {
            LgType[] entitys = LgType.values();
            for (LgType entity : entitys) {
                if ((entity.getKey()).equals(key)) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param pn 平台标识
     * @return LgType
     */
    public static LgType getByPns(Sval.EmPn pn) {
        List<LgType> rtypes = Lists.newArrayList();
        if ((pn != null)) {
            LgType[] entitys = LgType.values();
            for (LgType entity : entitys) {
                if ((Arrays.asList(entity.getPns())).contains(pn)) {
                    return entity;
                }
            }
        }
        return null;
    }

    public static List<LgType> getByPns(String pnPrefix) {
        Sval.EmPn pn = Sval.EmPn.getByPrefix(pnPrefix);
        List<LgType> rtypes = Lists.newArrayList();
        if (pn == null) {
            return rtypes;
        }

        if ((pn != null)) {
            LgType[] entitys = LgType.values();
            for (LgType entity : entitys) {
                if ((Arrays.asList(entity.getPns())).contains(pn)) {
                    rtypes.add(entity);
                }
            }
        }
        return rtypes;
    }

    /**
     * 获取主题 .
     * @return List
     */
    public static List<LgType> getAll() {
        List<LgType> enty = Lists.newArrayList();
        LgType[] entitys = LgType.values();
        for (LgType entity : entitys) {
            if(entity.isEnable()){
                enty.add(entity);
            }
        }
        return enty;
    }

    /**
     * 获取平台标识.
     * @param lgtype LgType
     * @return List
     */
    public static List<String> pnsToList(LgType lgtype) {
        List<String> pnsv = Lists.newArrayList();
        for (Sval.EmPn pn: lgtype.getPns()) {
            pnsv.add(pn.getPrefix());
        }
        return pnsv;
    }

    /**
     * 获取平台角色类型.
     * @param lgtype LgType
     * @return List
     */
    public static List<String> rtypesToList(LgType lgtype) {
        List<String> rtyps = Lists.newArrayList();
        for (CoreSval.Rtype cur: lgtype.getRtypes()) {
            rtyps.add(cur.getKey());
        }
        return rtyps;
    }

    /**
     * 检查是否为前台登录.
     * @param lgtype LgType
     * @return boolean
     */
    public static boolean frontFilter(LgType lgtype){
        List<LgType> lgTypes = Lists.newArrayList();
        lgTypes.add(LgType.STUDENT);
        if(lgTypes.contains(lgtype)){
            return true;
        }
        return false;
    }
    public static Map<String, String> frontParam(Usvo usvo){
        Map<String, String> rmap = Maps.newHashMap();
        rmap.put("loginType", "2");
        rmap.put("password", usvo.getPassword());
        rmap.put("username", usvo.getLoginName());
        rmap.put("validateCode", usvo.getAllVcode());
        return rmap;
    }

    /**
     * 检查是否为后台登录.
     * @param lgtype LgType
     * @return boolean
     */
    public static boolean adminFilter(LgType lgtype){
        List<LgType> lgTypes = Lists.newArrayList();
        lgTypes.add(LgType.EXPORT);
        lgTypes.add(LgType.MANAGER);
        if(lgTypes.contains(lgtype)){
            return true;
        }
        return false;
    }
    public static Map<String, String> adminParam(Usvo usvo){
        Map<String, String> rmap = Maps.newHashMap();
        rmap.put("password", usvo.getPassword());
        rmap.put("username", usvo.getLoginName());
        rmap.put("validateCode", usvo.getAllVcode());
        return rmap;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Sval.EmPn[] getPns() {
        return pns;
    }

    public String getRemark() {
        return remark;
    }

    public CoreSval.Rtype[] getRtypes() {
        return rtypes;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\"key\":\"" + this.key + "\"");
        buffer.append(",\"name\":" + "\"" + this.name +"\"");
        buffer.append(",\"enable\":" + "\"" + this.enable +"\"");

        if(this.pns != null){
            buffer.append(",\"pns\":" + Arrays.asList(this.pns).toString());
        }else{
            buffer.append(",\"pns\":{}");
        }

        if(this.rtypes != null){
            buffer.append(",\"rtypes\":" + Arrays.asList(this.rtypes).toString());
        }else{
            buffer.append(",\"rtypes\":{}");
        }

        if(StringUtil.isNotEmpty(this.remark)){
            buffer.append(",\"remark\":\"" + this.remark + "\"}");
        }else{
            buffer.append(",\"remark\":\"\"}");
        }
        return buffer.toString();
    }
}
