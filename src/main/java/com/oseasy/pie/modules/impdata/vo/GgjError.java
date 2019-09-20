/**
 * .
 */

package com.oseasy.pie.modules.impdata.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pie.modules.iep.ext.Ierror;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGJError;
import com.oseasy.pro.modules.promodel.vo.GgjBusInfo;
import com.oseasy.pro.modules.promodel.vo.GgjStudent;
import com.oseasy.pro.modules.promodel.vo.GgjTeacher;

/**
 * .
 * @author chenhao
 */
public class GgjError implements Ierror<ProModelError>{
    ProModelGJError verr = new ProModelGJError();// 用于保存处理之后的信息，以免再次查找数据库.
    ProModelGJError err = new ProModelGJError();

    List<GgjStudent> stuErrs = Lists.newArrayList();
    List<GgjTeacher> teaErrs = Lists.newArrayList();
    List<GgjBusInfo> infoErrs = Lists.newArrayList();

    @Override
    public ProModelGJError getErr() {
        return this.err;
    }

    public List<GgjBusInfo> getInfoErrs() {
        return infoErrs;
    }

    public void setInfoErrs(List<GgjBusInfo> infoErrs) {
        this.infoErrs = infoErrs;
    }

    @Override
    public ProModelGJError getVerr() {
        return this.verr;
    }

    public List<GgjStudent> getStuErrs() {
        return stuErrs;
    }

    public void setStuErrs(List<GgjStudent> stuErrs) {
        this.stuErrs = stuErrs;
    }

    public List<GgjTeacher> getTeaErrs() {
        return teaErrs;
    }

    public void setTeaErrs(List<GgjTeacher> teaErrs) {
        this.teaErrs = teaErrs;
    }

    public void setVerr(ProModelGJError verr) {
        this.verr = verr;
    }

    public void setErr(ProModelGJError err) {
        this.err = err;
    }
}
