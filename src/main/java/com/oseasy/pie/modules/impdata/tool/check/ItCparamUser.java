/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.entity.TeacherExpansion;

/**
 * 导入参数校验的参数对象.
 * @author chenhao
 */
public class ItCparamUser extends AbsItCparam<XSSFSheet, ImpInfoErrmsgService, ImpInfo, ImpInfoErrmsg> {
    public final static Logger logger = Logger.getLogger(ItCparamUser.class);
    protected User user;//用户
    protected StudentExpansion student;//学生
    protected TeacherExpansion teacher;//导师
    public ItCparamUser() {
        super();
    }

    public ItCparamUser(User user) {
        super();
        this.user = user;
    }
    public ItCparamUser(User user, StudentExpansion student) {
        super();
        this.user = user;
        this.student = student;
    }
    public ItCparamUser(User user, TeacherExpansion teacher) {
        super();
        this.teacher = teacher;
    }

    public ItCparamUser(XSSFSheet xs, ImpInfoErrmsgService ies, ImpInfo info, ImpInfoErrmsg ie, User user) {
        super(xs, ies, info, ie);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudentExpansion getStudent() {
        return student;
    }

    public void setStudent(StudentExpansion student) {
        this.student = student;
    }

    public TeacherExpansion getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherExpansion teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean check() {
        if((this.xs == null) || (this.ies == null) || (this.info == null) || (this.ie == null)){
            logger.warn("当前校验初始化参数存在空对象！");
            return false;
        }

        if((this.xs instanceof XSSFSheet) && (this.ies instanceof ImpInfoErrmsgService) && (this.info instanceof ImpInfo) && (this.ie instanceof ImpInfoErrmsg)){
            return true;
        }
        logger.warn("当前校验初始化参数存在类型不匹配对象！");
        return false;
    }
}
