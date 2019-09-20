/**
 * .
 */

package com.oseasy.pie.modules.impdata.entity;

import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromElement;

/**
 * .
 * @author chenhao
 *
 */
public class ProModelGJError extends ProModelError{
    private static final long serialVersionUID = 1L;
    private int lsrow;//起始行
    private String province;//省（区、市）
    private String universityName;//高校名称
    private String lyear;        // 入学时间
    private String gyear;        // 毕业时间

    private String pstype;       // 项目赛道
    private String pdomain;       // 项目所属区域

    public String getLyear() {
        return lyear;
    }
    public void setLyear(String lyear) {
        this.lyear = lyear;
    }
    public int getLsrow() {
        return lsrow;
    }
    public void setLsrow(int lsrow) {
        this.lsrow = lsrow;
    }

    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getUniversityName() {
        return universityName;
    }
    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
    public String getGyear() {
        return gyear;
    }
    public void setGyear(String gyear) {
        this.gyear = gyear;
    }
    public String getPstype() {
        return pstype;
    }
    public void setPstype(String pstype) {
        this.pstype = pstype;
    }
    public String getPdomain() {
        return pdomain;
    }
    public void setPdomain(String pdomain) {
        this.pdomain = pdomain;
    }

    public static ProModelGcontestError toGcontestError(ProModelGJError gferror) {
        ProModelGcontestError gcerror = new ProModelGcontestError();
        gcerror.setId(gferror.getId());
        gcerror.setIsNewRecord(gferror.getIsNewRecord());
        gcerror.setImpId(gferror.getImpId());
        gcerror.setName(gferror.getName());
        gcerror.setYear(gferror.getYear());
        gcerror.setStage(gferror.getStage());
        gcerror.setType(gferror.getType());
        gcerror.setGroups(gferror.getLevel());
        gcerror.setTrack(gferror.getPstype());
        gcerror.setIntroduction(gferror.getIntroduction());
        gcerror.setHasfile(gferror.getHasfile());
        gcerror.setLeader(gferror.getLeader());
        gcerror.setNo(gferror.getNo());
        gcerror.setProfes(gferror.getProfes());
        gcerror.setEnter(gferror.getLyear());
        gcerror.setOut(gferror.getGyear());
        gcerror.setLyear(gferror.getLyear());
        gcerror.setGyear(gferror.getGyear());
        gcerror.setLschool(gferror.getUniversityName());
        gcerror.setProvince(gferror.getProvince());
        gcerror.setDomain(gferror.getPdomain());
//        gcerror.setXueli();
//        gcerror.setIdnum();
        gcerror.setMobile(gferror.getMobile());
        gcerror.setEmail(gferror.getEmail());
        gcerror.setValidName(gferror.isValidName());
        gcerror.setTeachers(gferror.getTeachers());
        gcerror.setMembers(gferror.getMembers());
        gcerror.setBusinfos(gferror.getBusinfos());
//        private List<PmgMemsError> pmes=new ArrayList<PmgMemsError>();//组成员
//        private List<PmgTeasError> ptes=new ArrayList<PmgTeasError>();//导师
        return gcerror;
    }
}
