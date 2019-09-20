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

import net.sf.json.JSONArray;

/**
 * .
 * @author chenhao
 *
 */
public class GgjStudent implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String MEMBERS = "members";
    private String cname;//匹配列名
    private Integer srow;//开始行
    private String name;//姓名
    private String no;//学号
    private String area;// 地区
    private String school;//高校
    private String professional;// 专业
    private String mobile;//手机号
    private String email;//邮箱
    private String year;//入学年份
    private String gyear;//毕业年份

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getProfessional() {
        return professional;
    }
    public void setProfessional(String professional) {
        this.professional = professional;
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
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getGyear() {
        return gyear;
    }
    public void setGyear(String gyear) {
        this.gyear = gyear;
    }

    /**
     * 生成成员数据.
     * [姓名, 学号, 省市, 高校, 专业, 手机, 邮箱, 入学年份, 毕业年份]
     */
    public static String toMembers(List<GgjStudent> students) {
        ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_FH.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_FH);

        StringBuffer buffer = new StringBuffer();
        for (GgjStudent cur : students) {
            buffer.append(oper.getReqParam().getPostfix());
            buffer.append(cur.getName());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getNo());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getArea());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getSchool());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getProfessional());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getMobile());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getEmail());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getYear());
            buffer.append(oper.getReqParam().getPrefix());
            buffer.append(cur.getGyear());
        }
        if(StringUtil.isEmpty(buffer.toString())){
            return null;
        }
        return buffer.toString().replaceFirst(oper.getReqParam().getPostfix(), StringUtil.EMPTY);
    }

    public static List<GgjStudent> mtoGgjStudents(String members) {
        ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_FH.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_FH);

        List<GgjStudent> ggjSts = Lists.newArrayList();
        List<String[]> sts = StringUtil.splitByPF(members, oper.getReqParam().getPrefix(), oper.getReqParam().getPostfix());
        for (String[] cur : sts) {
            if((cur == null) || (cur.length != 9)){
                continue;
            }
            GgjStudent curst = new GgjStudent();
            curst.setName(cur[0]);
            curst.setNo(cur[1]);
            curst.setArea(cur[2]);
            curst.setSchool(cur[3]);
            curst.setProfessional(cur[4]);
            curst.setMobile(cur[5]);
            curst.setEmail(cur[6]);
            curst.setYear(cur[7]);
            curst.setGyear(cur[8]);
            ggjSts.add(curst);
        }
        return ggjSts;
    }


public static void main(String[] args) {
    ItOper oper =  new ItOper();
    oper.setReqParam(new ItReqParam());
    oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
    oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
    oper.getReqParam().setPostfix(SpiltPost.SPOST_FH.getRemark());
    oper.getReqParam().setSpost(SpiltPost.SPOST_FH);

    List<GgjStudent> students = Lists.newArrayList();
    for (int i = 0; i < 3; i++) {
        GgjStudent stu = new GgjStudent();
        stu.setName("张三"+i);
        stu.setArea("湖北");
        stu.setEmail("316@qq.com"+i);
        stu.setYear("2016"+i);
        stu.setGyear("2019"+i);
        stu.setMobile("13580366759"+i);
        stu.setNo("No13580366759"+i);
        stu.setProfessional("计算机科学"+i);
        stu.setSchool("湖北大学"+i);
        students.add(stu);
    }
    System.out.println("------");
    System.out.println(oper.getReqParam().getPrefix());
    System.out.println(oper.getReqParam().getPostfix());
    System.out.println(JSONArray.fromObject(StringUtil.splitByPF(GgjStudent.toMembers(students), oper.getReqParam().getPrefix(), oper.getReqParam().getPostfix())).toString());
    List<GgjStudent> ggjs = mtoGgjStudents(GgjStudent.toMembers(students));
    System.out.println(JSONArray.fromObject(ggjs).toString());
    System.out.println("------");
}
}
