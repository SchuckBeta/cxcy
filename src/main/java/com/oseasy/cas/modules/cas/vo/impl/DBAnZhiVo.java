/**
 * .
 */

package com.oseasy.cas.modules.cas.vo.impl;

import java.util.Date;

import com.oseasy.cas.modules.cas.vo.ICasb;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;

import net.sf.json.JSONArray;

/**
 * 用户基本信息.
 * @author chenhao
 *
 */
public class DBAnZhiVo implements ICasb {
    private String no; // 工号 学号
    private String name; // 姓名
    private String email; // 邮箱
    private String phone; // 电话
    private String mobile; // 手机
    private String userType;// 用户类型 1学生 2导师
    private String degree;// 学位； 1：学士2：硕士3：博士
    private String education;// 学历:  1、专科，2、本科，3、研究生，0000000149、其他，0000000148、博士，0000000150、硕士
    private String sex; // 性别：0 女 1 男
    private Date birthday;// 生日:yyyy-MM-dd
    private String professional;// 专业
    private String idType;// 证件：1、身份证,2、护照
    private String national;// 民族
    private String political;// 政治面貌
    private String country;// 国家地区
    private String idNumber;// 身份证
    private String qq; // qq
    private String postCode;// 邮编
    private String age;//年龄

    public String getNo() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public static StudentExpansion dealStudentExpansion(StudentExpansion studentExpansion, DBAnZhiVo dbvo) {
        return studentExpansion;
    }

    public static BackTeacherExpansion dealBackTeacherExpansion(BackTeacherExpansion backTeacherExpansion, DBAnZhiVo dbvo) {
        return backTeacherExpansion;
    }

    public static User dealDbvo(User user, DBAnZhiVo dbvo) {
        if(user == null){
            return null;
        }

        user.setNo(dbvo.getNo());
        user.setName(dbvo.getName());
        user.setEmail(dbvo.getEmail());
        user.setPhone(dbvo.getPhone());
        user.setMobile(dbvo.getMobile());
        user.setUserType(dbvo.getUserType());
        user.setDegree(dbvo.getDegree());
        user.setEducation(dbvo.getEducation());
        user.setSex(dbvo.getSex());
        user.setBirthday(dbvo.getBirthday());
        user.setIdType(dbvo.getIdType());
        user.setNational(dbvo.getNational());
        user.setPolitical(dbvo.getPolitical());
        user.setCountry(dbvo.getCountry());
        user.setIdNumber(dbvo.getIdNumber());
        user.setQq(dbvo.getQq());
        user.setSource("1");
        user.setPostCode(dbvo.getPostCode());
        user.setAge(dbvo.getAge());
        return user;
    }

    public static void main(String[] args) {
        String[] ss = new String[]{"出生地代码","籍贯代码","国家(地区)代码","民族代码","身份证件类型代码","港澳台侨代码","政治面貌代码","所在院系（单位）代码","所在科室(系)代码","教职工类别代码","最高学历代码","毕业日期","专业技术职务代码","党政职务"};
        System.out.println(JSONArray.fromObject(ss).size());

//        System.out.println(JsonNetUtils.toJsonString(new DBAnZhiVo()));
//        String ss = "[{\"birthday\":null,\"qq\":\"111\",\"country\":\"\",\"no\":\"\",\"education\":\"\",\"idType\":\"\",\"sex\":\"\",\"degree\":\"\",\"mobile\":\"\",\"political\":\"\",\"idNumber\":\"\",\"professional\":\"\",\"phone\":\"\",\"name\":\"\",\"national\":\"\",\"postCode\":\"\",\"userType\":\"\",\"age\":\"\",\"email\":\"\"}]";
//        List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(ss, DBAnZhiVo.class);
//        System.out.println(dbAnZhiVos.toString());
    }
}
