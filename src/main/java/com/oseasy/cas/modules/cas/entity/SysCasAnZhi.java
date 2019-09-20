/**
 * .
 */

package com.oseasy.cas.modules.cas.entity;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.cas.modules.cas.service.ISysCas;
import com.oseasy.cas.modules.cas.vo.IdType;
import com.oseasy.cas.modules.cas.vo.impl.DBAnZhiVo;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * .
 * @author chenhao
 *
 */
public class SysCasAnZhi extends DataEntity<SysCasAnZhi> implements IidEntity, ISysCas{
    public final static Logger logger = Logger.getLogger(SysCasAnZhi.class);
    private static final long serialVersionUID = 1L;
    private String id;
    private String ruid;
    private String rutype;//用户类型
    private String rname;
    private String rcname;
    private String rou;//OU
    private String rcontainerId;//容器ID
    private String rjson;//关联用户信息
    private Integer time;    // 处理次数
    private Boolean enable;    // 是否处理

    private Integer checkRet;    //检查结果

//    拓展字段
    private String idNumber;//身份证件号：（身份证）
    private String sex;//性别：0 女 1 男
    private Date birthday;//出生日期:yyyy-MM-dd
    private String rarray;//拓展属性：["出生地代码","籍贯代码","国家(地区)代码","民族代码","身份证件类型代码","港澳台侨代码","政治面貌代码","所在院系（单位）代码","所在科室(系)代码","教职工类别代码","最高学历代码","毕业日期","专业技术职务代码","党政职务"]
    private List<String> rarrays;//拓展属性：["出生地代码","籍贯代码","国家(地区)代码","民族代码","身份证件类型代码","港澳台侨代码","政治面貌代码","所在院系（单位）代码","所在科室(系)代码","教职工类别代码","最高学历代码","毕业日期","专业技术职务代码","党政职务"]

    public SysCasAnZhi() {
        super();
    }

    public SysCasAnZhi(String id) {
        super();
        this.id = id;
    }

    public SysCasAnZhi(SysCasUser sysCasAnZhi) {
        super();
        this.ruid = sysCasAnZhi.getRuid();
        this.rutype = sysCasAnZhi.getRutype();
        this.rname = sysCasAnZhi.getRname();
        this.rcname = sysCasAnZhi.getRcname();
        this.rou = sysCasAnZhi.getRou();
        this.rcontainerId = sysCasAnZhi.getRcontainerId();
        this.time = 0;
        this.enable = sysCasAnZhi.getEnable();
        this.checkRet = sysCasAnZhi.getCheckRet();
    }

    public SysCasAnZhi(String ruid, String rutype, String rname, String rcname, String rou, String rcontainerId,
            String rjson, Boolean enable, Integer checkRet) {
        super();
        this.ruid = ruid;
        this.rutype = rutype;
        this.rname = rname;
        this.rcname = rcname;
        this.rou = rou;
        this.rcontainerId = rcontainerId;
        this.rjson = rjson;
        this.time = 0;
        this.enable = enable;
        this.checkRet = checkRet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuid() {
        return ruid;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public String getRutype() {
        return rutype;
    }

    public void setRutype(String rutype) {
        this.rutype = rutype;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRcname() {
        return rcname;
    }

    public void setRcname(String rcname) {
        this.rcname = rcname;
    }

    public String getRou() {
        return rou;
    }

    public void setRou(String rou) {
        this.rou = rou;
    }

    public String getRcontainerId() {
        return rcontainerId;
    }

    public void setRcontainerId(String rcontainerId) {
        this.rcontainerId = rcontainerId;
    }

    public String getRjson() {
        if(StringUtil.isEmpty(this.rjson)){
            this.rjson = StringUtil.KUOHZLR;
        }
        return rjson;
    }

    public void setRjson(String rjson) {
        this.rjson = rjson;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getCheckRet() {
        return checkRet;
    }

    public void setCheckRet(Integer checkRet) {
        this.checkRet = checkRet;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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

    public String getRarray() {
        return rarray;
    }

    public void setRarray(String rarray) {
        this.rarray = rarray;
    }

    @SuppressWarnings("deprecation")
    public List<String> getRarrays() {
        if(StringUtil.isNotEmpty(this.rarray)){
            this.rarrays = JSONArray.toList(JSONArray.fromObject(this.rarray));
        }
        return rarrays;
    }

    /**
     * 创建SysCasUser.
     * @param sysCasAnZhi
     * @return
     */
    public static SysCasUser newSysCasUser(SysCasAnZhi sysCasAnZhi) {

        SysCasUser sysCasUser = new SysCasUser();
        sysCasUser.setRuid(sysCasAnZhi.getRuid());
        List<DBAnZhiVo> casbs = null;
        if(StringUtil.isNotEmpty(sysCasAnZhi.getRjson())){
            try {
                casbs = JsonAliUtils.toBean(sysCasAnZhi.getRjson(), DBAnZhiVo.class);

            } catch (Exception e) {
                logger.info("用户详细数据(rjson)处理失败");
                sysCasUser.setIsDeal(false);
                return sysCasUser;
            }
        }
        sysCasUser.setIsDeal(true);
        sysCasUser.setCheckUtype(true);
        sysCasUser.setId(IdGen.uuid());
        sysCasUser.setRuid(sysCasAnZhi.getRuid());
        sysCasUser.setRutype(sysCasAnZhi.getRutype());
        sysCasUser.setRname(sysCasAnZhi.getRname());
        sysCasUser.setRcname(sysCasAnZhi.getRcname());
        sysCasUser.setRou(sysCasAnZhi.getRou());
        sysCasUser.setRcontainerId(sysCasAnZhi.getRcontainerId());
        sysCasUser.setLastLoginDate(new Date());
        sysCasUser.setTime(0);
        sysCasUser.setCreateDate(new Date());
        sysCasUser.setCreateBy(CoreUtils.getUser());


        if(sysCasUser.getCreateBy() == null){
            sysCasUser.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        }
        if(StringUtil.checkNotEmpty(casbs)){
            String rjson = null;
            DBAnZhiVo dbazVo = casbs.get(0);
            dbazVo.setBirthday(sysCasAnZhi.getBirthday());
            dbazVo.setSex(sysCasAnZhi.getSex());
            dbazVo.setIdNumber(sysCasAnZhi.getIdNumber());
            dbazVo.setIdType(IdType.SFZ.getKey());
            dbazVo.setUserType(sysCasAnZhi.getRutype());
            dbazVo.setNo(sysCasAnZhi.getRuid());
            dbazVo.setName(sysCasAnZhi.getRname());
            if(StringUtil.checkNotEmpty(sysCasAnZhi.getRarrays())){
                //拓展属性：["出生地代码","籍贯代码","国家(地区)代码","民族代码","身份证件类型代码","港澳台侨代码","政治面貌代码","所在院系（单位）代码","所在科室(系)代码","教职工类别代码","最高学历代码","毕业日期","专业技术职务代码","党政职务"]
                dbazVo.setCountry(sysCasAnZhi.getRarrays().get(2));
                dbazVo.setNational(sysCasAnZhi.getRarrays().get(3));
//                dbazVo.setEmail(sysCasAnZhi.getRarrays().get(0));
//                dbazVo.setPhone(sysCasAnZhi.getRarrays().get(0));
//                dbazVo.setMobile(sysCasAnZhi.getRarrays().get(0));
//                dbazVo.setDegree(sysCasAnZhi.getRarrays().get(0));
//                dbazVo.setQq(sysCasAnZhi.getRarrays().get(0));
//                dbazVo.setPostCode(sysCasAnZhi.getRarrays().get(0));
//                dbazVo.setAge(sysCasAnZhi.getRarrays().get(0));
                dbazVo.setPolitical(sysCasAnZhi.getRarrays().get(6));
                dbazVo.setProfessional(sysCasAnZhi.getRarrays().get(8));
                dbazVo.setEducation(sysCasAnZhi.getRarrays().get(10));
            }
            rjson = JSONObject.fromObject(dbazVo).toString();
            sysCasUser.setRjson((rjson == null) ? StringUtil.KUOHZLR : rjson);
            sysCasUser.setCasbs(casbs);
        }
        return sysCasUser;
    }
}
