/**
 * .
 */

package com.oseasy.pro.modules.promodel.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.workflow.impl.SpiltPost;
import com.oseasy.pro.modules.workflow.impl.SpiltPref;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class GgjTeacher implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String TEACHERS = "teachers";
    private String cname;//匹配列名
    private Integer srow;//开始行
    private String name;//姓名
    private String no;//工号
    private String area;// 地区
    private String school;//高校
    private String subject;// 部门
    private String technicalTitle;// 职称
    private String mobile;//手机号
    private String email;//邮箱
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public Integer getSrow() {
        return srow;
    }
    public void setSrow(Integer srow) {
        this.srow = srow;
    }
    public String getNo() {
        if((this.no == null) && (this.mobile != null)){
            this.no = this.mobile;
        }
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTechnicalTitle() {
        return technicalTitle;
    }
    public void setTechnicalTitle(String technicalTitle) {
        this.technicalTitle = technicalTitle;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 生成导师数据.
     * [姓名, 学号, 省市, 高校, 部门, 手机, 邮箱, 职称]
     */
    public static String toTeachers(List<GgjTeacher> teachers) {
        ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_DUN.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_DUN);

        StringBuffer buffer = new StringBuffer();
        for (GgjTeacher cur : teachers) {
            buffer.append(oper.getReqParam().getPostfix());
            buffer.append(cur.getName());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getNo());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getArea());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getSchool());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getSubject());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getMobile());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getEmail());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getTechnicalTitle());
        }

        if(StringUtil.isEmpty(buffer.toString())){
            return null;
        }
        return buffer.toString().replaceFirst(oper.getReqParam().getPostfix(), StringUtil.EMPTY);
    }

    public static List<GgjTeacher> mtoGgjTeachers(String teachers) {
        ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_DUN.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_DUN);

        List<GgjTeacher> ggjts = Lists.newArrayList();
        List<String[]> sts = StringUtil.splitByPF(teachers, oper.getReqParam().getPrefix(), oper.getReqParam().getPostfix());
        for (String[] cur : sts) {
            if((cur == null) || (cur.length != 8)){
                continue;
            }
            GgjTeacher curst = new GgjTeacher();
            curst.setName(cur[0]);
            curst.setNo(cur[1]);
            curst.setArea(cur[2]);
            curst.setSchool(cur[3]);
            curst.setSubject(cur[4]);
            curst.setMobile(cur[5]);
            curst.setEmail(cur[6]);
            curst.setTechnicalTitle(cur[7]);
            ggjts.add(curst);
        }
        return ggjts;
    }
}
