/**
 * .
 */

package com.oseasy.sys.modules.sys.vo;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 用户信息.
 * @author chenhao
 *
 */
public class UserInfoVo implements Serializable{
    protected static Logger logger = LoggerFactory.getLogger(UserInfoVo.class);
    public static final String UI_BASE = "base";
    public static final String UI_EDUCATION = "education";
    private static final String UVO_ENTERDATE = "enterdate";
    private static final String UVO_BIRTHDAY = "birthday";
    private static final long serialVersionUID = 1L;
    private UserBase base;
    private UserEducation education;

    public UserInfoVo() {
        super();
    }
    public UserInfoVo(UserBase base, UserEducation education) {
        super();
        this.base = base;
        this.education = education;
    }
    public UserBase getBase() {
        return base;
    }
    public void setBase(UserBase base) {
        this.base = base;
    }
    public UserEducation getEducation() {
        return education;
    }
    public void setEducation(UserEducation education) {
        this.education = education;
    }

    /**
     * 转换UserUInfoVo到StudentExpansion.
     */
    public static StudentExpansion convert(StudentExpansion sexpansion, UserInfoVo userInfoVo) {
        if(userInfoVo == null){
            return null;
        }

        if(sexpansion == null){
            sexpansion = new StudentExpansion();
        }
        if(userInfoVo.getBase() != null){
            UserBase userBase = userInfoVo.getBase();
            if(sexpansion.getUser() == null){
                sexpansion.setUser(UserUtils.getUser());
            }
            User user = sexpansion.getUser();
            user.setId(userBase.getId());
            user.setLoginName(userBase.getLoginName());
            if(StringUtil.isEmpty(user.getNo())){
                user.setNo(user.getLoginName());
            }
            user.setNo(user.getNo());
            user.setName(userBase.getName());
            user.setSex(userBase.getSex());
            user.setIdType(userBase.getIdType());
            user.setIdNumber(userBase.getIdNumber());
            user.setBirthday(userBase.getBirthday());
            user.setEmail(userBase.getEmail());
            user.setMobile(userBase.getMobile());
            user.setQq(userBase.getQq());
            user.setCountry(userBase.getCountry());
            user.setNational(userBase.getNational());
            user.setPolitical(userBase.getPolitical());
            user.setIntroduction(userBase.getIntroduction());
            user.setResidence(userBase.getResidence());

            sexpansion.setUser(user);
            sexpansion.setAddress(userBase.getAddress());
        }

        if(userInfoVo.getEducation() != null){
            UserEducation userEducation = userInfoVo.getEducation();
            if(sexpansion.getUser() != null){
                User user = sexpansion.getUser();
                user.setDomainIdList(userEducation.getDomainIdList());
                user.setEducation(userEducation.getEducation());
                user.setDegree(userEducation.getDegree());
                user.setNo(userEducation.getNo());
                user.setProfessional(userEducation.getProfessional());
                if(StringUtil.isNotEmpty(userEducation.getOfficeId())){
                    user.setOffice(new Office(userEducation.getOfficeId()));
                }
                sexpansion.setUser(user);
            }
            sexpansion.setId(userEducation.getId());
            sexpansion.setEnterdate(userEducation.getEnterdate());
            sexpansion.setCycle(userEducation.getCycle());
            sexpansion.setGraduation(userEducation.getGraduation());
            sexpansion.setCurrState(userEducation.getCurrState());
            sexpansion.setInstudy(userEducation.getInstudy());
            sexpansion.setTClass(userEducation.getTclass());
            sexpansion.setInstudy(userEducation.getInstudy());
            sexpansion.setTemporaryDate(userEducation.getTemporaryDate());
        }
        return sexpansion;
    }

    /**
     * 处理提交的日期属性.
     * @param gps JSONObject
     * @param userInfoVo UserInfoVo
     * @return UserInfoVo
     */
    public static UserInfoVo dealDate(JSONObject gps, UserInfoVo userInfoVo) {
        if((userInfoVo != null)){
            String birthday = null;
            String enterdate = null;
            try {
                if((userInfoVo.getBase() != null)){
                    JSONObject base = gps.getJSONObject(UserInfoVo.UI_BASE);
                    Object obb = base.get(UserInfoVo.UVO_BIRTHDAY);
                    if(obb != null){
                        birthday = base.getString(UserInfoVo.UVO_BIRTHDAY);
                    }
                    if(StringUtil.isNotEmpty(birthday)){
                        userInfoVo.getBase().setBirthday(DateUtil.parseDate(birthday, DateUtil.FMT_YYYYMM_ZG));
                    }
                    logger.info(UserInfoVo.UVO_BIRTHDAY + " = "+ userInfoVo.getBase().getBirthday());
                }
                if((userInfoVo.getEducation() != null)){
                    JSONObject education = gps.getJSONObject(UserInfoVo.UI_EDUCATION);
                    Object obb = education.get(UserInfoVo.UVO_ENTERDATE);
                    if(obb != null){
                        enterdate = education.getString(UserInfoVo.UVO_ENTERDATE);
                    }
                    if(StringUtil.isNotEmpty(enterdate)){
                        userInfoVo.getEducation().setEnterdate(DateUtil.parseDate(enterdate, DateUtil.FMT_YYYYMM_ZG));
                    }
                    logger.info(UserInfoVo.UVO_ENTERDATE + " = "+ userInfoVo.getEducation().getEnterdate());
                }

            } catch (Exception e) {
                logger.info(UserInfoVo.UVO_BIRTHDAY + " = "+ birthday);
                logger.info(UserInfoVo.UVO_ENTERDATE + " = "+ enterdate);
            }
        }
        return userInfoVo;
    }
}
