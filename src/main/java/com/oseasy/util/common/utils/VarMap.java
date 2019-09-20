/**
 * .
 */

package com.oseasy.util.common.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 反射对象属性类.
 * @author chenhao
 *
 */
public class VarMap implements Serializable{
    private static final long serialVersionUID = 1L;
    private String key;//序列（排序）
    private String var;//变量
    private Object val;//变量值
    private String preHtml;//前置HTML
    @JsonIgnore
    private Class<?> vaclazz;//变量值对象
    @JsonIgnore
    private Method vamethod;//变量值方法

    public VarMap() {
        super();
    }
    public VarMap(String key, String var) {
        super();
        this.key = key;
        this.var = var;
    }
    public VarMap(String key, String var, String preHtml) {
        super();
        this.key = key;
        this.var = var;
        this.preHtml = preHtml;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getVar() {
        return var;
    }
    public void setVar(String var) {
        this.var = var;
    }
    public Object getVal() {
        return val;
    }
    public void setVal(Object val) {
        this.val = val;
    }
    public String getPreHtml() {
        return preHtml;
    }
    public void setPreHtml(String preHtml) {
        this.preHtml = preHtml;
    }
    public Class<?> getVaclazz() {
        return vaclazz;
    }

    public void setVaclazz(Class<?> vaclazz) {
        this.vaclazz = vaclazz;
    }

    public Method getVamethod() {
        return vamethod;
    }

    public void setVamethod(Method vamethod) {
        this.vamethod = vamethod;
    }

    /**
     * 字符串提取变量.
     * @param vmaps 结果Map
     * @param srcStr 源字符串
     * @param spStar 开始分隔符
     * @param spEnd 结束分隔符
     * @return List
     */
    public static List<VarMap> splitVar(List<VarMap> vmaps, String srcStr, String spStar, String spEnd) {
        int idxEnd = srcStr.indexOf(spEnd);
        int idxStart = srcStr.indexOf(spStar);
        if((idxStart != -1) && (idxEnd != -1)){
            VarMap vmap = new VarMap(vmaps.size() + StringUtil.EMPTY, (srcStr).substring(idxStart + 2, idxEnd));
            vmap.setPreHtml((srcStr).substring(0, idxStart));
            vmaps.add(vmap);
            return splitVar(vmaps, (srcStr).substring(idxEnd + 1), spStar, spEnd);
        }else{
            VarMap vmap = new VarMap();
            vmap.setPreHtml(srcStr);
            vmaps.add(vmap);
        }
        return vmaps;
    }

    /**
     * 字符串提取变量.
     * @param srcStr 源字符串
     * @param spStar 开始分隔符
     * @param spEnd 结束分隔符
     * @return List
     */
    public static List<VarMap> splitVar(String srcStr, String spStar, String spEnd) {
        return splitVar(new ArrayList<VarMap>(), srcStr, spStar, spEnd);
    }

    /**
     * 渲染变量字符串.
     * @param vmaps
     * @return String
     */
    public static String renderVar(List<VarMap> vmaps){
        StringBuffer buffer = new StringBuffer();
        for (VarMap varMap : vmaps) {
            buffer.append(varMap.getPreHtml());
            if(StringUtil.isEmpty(varMap.getVar())){
                continue;
            }

            if(varMap.getVal() == null){
                continue;
            }
            buffer.append(varMap.getVal());
        }
        return buffer.toString();
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"var\":\"" + this.var + "\",\"val\":\"" + this.val + "\"}";
    }

    public static void main(String[] args) {
        String srcStr = "AAAA${ss},dshds${ssv}sasdad,${sss}jksfjska${vv}ssSlsls${bvbv}ppp";
        List<VarMap> varmaps = splitVar(srcStr, StringUtil.JSP_VAL_PREFIX, StringUtil.JSP_VAL_POSTFIX);
        for (VarMap varMap : varmaps) {
            System.out.println(varMap.getKey()+"|"+varMap.getVar());
            System.out.println(">>>"+varMap.getPreHtml());
        }

        //JsonAliUtils.writeBean(SvalPw.ENTER_EXPIRE_LOGFILE + StringUtil.LINE + DateUtil.formatDate(new Date(), DateUtil.FMT_YYYY_MM_DD_HH) + StringUtil.LOG,
        //    JSONObject.fromObject(new PwEnterEvo()));
    }
}
