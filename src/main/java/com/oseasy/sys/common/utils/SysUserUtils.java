/**
 *
 */
package com.oseasy.sys.common.utils;

import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.dao.StudentExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户工具类
 */
public class SysUserUtils {
    public static StudentExpansionDao studentExpansionDao = SpringContextHolder.getBean(StudentExpansionDao.class);
    public static BackTeacherExpansionDao backTeacherExpansionDao = SpringContextHolder.getBean(BackTeacherExpansionDao.class);

    public static StudentExpansion getStudentByUserId(String uid) {
    	return studentExpansionDao.getByUserId(uid);
    }

    public static BackTeacherExpansion getTeacherByUserId(String uid) {
    	return backTeacherExpansionDao.getByUserId(uid);
    }

    //返回true 说明未完善
    	public static boolean checkFrontInfoPerfect(User user) {
    		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {//学生
    			StudentExpansion stu=getStudentByUserId(user.getId());
    			if (StringUtil.isEmpty(user.getLoginName())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(user.getSex())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(user.getName())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(user.getIdType())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(user.getIdNumber())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(user.getMobile())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(user.getIntroduction())) {
    				return true;
    			}
    //			if (StringUtil.isEmpty(user.getResidence())) {
    //				return true;
    //			}
    			if (StringUtil.isEmpty(user.getEmail())) {
    				return true;
    			}
    			if (user.getOffice()==null||StringUtil.isEmpty(user.getOffice().getId())) {
    				return true;
    			}
    			if (StringUtil.isEmpty(stu.getCurrState())) {
    				return true;
    			}
    			if ("1".equals(stu.getCurrState())) {
    				if (StringUtil.isEmpty(stu.getInstudy())) {
    					return true;
    				}
    				if (StringUtil.isEmpty(user.getNo())) {
    					return true;
    				}
    			}
    			if ("2".equals(stu.getCurrState())) {
    				if (StringUtil.isEmpty(user.getEducation())) {
    					return true;
    				}
    				if (StringUtil.isEmpty(user.getDegree())) {
    					return true;
    				}
    				if (stu.getGraduation()==null) {
    					return true;
    				}
    			}
    			if ("3".equals(stu.getCurrState())) {
    				if (stu.getTemporaryDate()==null) {
    					return true;
    				}
    			}
    			if (stu.getEnterdate()==null) {
    				return true;
    			}
    			if (StringUtil.isEmpty(stu.getCycle())) {
    				return true;
    			}
    		}
    		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {//导师
    			BackTeacherExpansion bt=getTeacherByUserId(user.getId());
    			if ((bt != null)) {//不是企业导师
    				if (!(TeacherType.TY_QY.getKey()).equals(bt.getTeachertype())) {//不是企业导师
    					if (StringUtil.isEmpty(user.getLoginName())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getSex())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getNo())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getName())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(bt.getTeachertype())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(bt.getServiceIntention())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getIdType())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getIdNumber())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(bt.getEducationType())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getEducation())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getEmail())) {
    						return true;
    					}
    					if (StringUtil.isEmpty(user.getMobile())) {
    						return true;
    					}
    				}
    			}
    		}
    		return false;
    	}

        //返回true 说明未完善
        	public static boolean checkInfoPerfect(User user) {
        		if (user==null||StringUtil.isEmpty(user.getId())) {
        			return false;
        		}
        		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {//学生
        			StudentExpansion stu=getStudentByUserId(user.getId());
        			if (StringUtil.isEmpty(user.getLoginName())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(user.getSex())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(user.getName())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(user.getIdType())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(user.getIdNumber())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(user.getMobile())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(user.getIntroduction())) {
        				return true;
        			}
        //			if (StringUtil.isEmpty(user.getResidence())) {
        //				return true;
        //			}
        			if (StringUtil.isEmpty(user.getEmail())) {
        				return true;
        			}
        			if (user.getOffice()==null||StringUtil.isEmpty(user.getOffice().getId())) {
        				return true;
        			}
        			if (StringUtil.isEmpty(stu.getCurrState())) {
        				return true;
        			}
        			if ("1".equals(stu.getCurrState())) {
        				if (StringUtil.isEmpty(stu.getInstudy())) {
        					return true;
        				}
        				if (StringUtil.isEmpty(user.getNo())) {
        					return true;
        				}
        			}
        			if ("2".equals(stu.getCurrState())) {
        				if (StringUtil.isEmpty(user.getEducation())) {
        					return true;
        				}
        				if (StringUtil.isEmpty(user.getDegree())) {
        					return true;
        				}
        				if (stu.getGraduation()==null) {
        					return true;
        				}
        			}
        			if ("3".equals(stu.getCurrState())) {
        				if (stu.getTemporaryDate()==null) {
        					return true;
        				}
        			}
        			if (stu.getEnterdate()==null) {
        				return true;
        			}
        			if (StringUtil.isEmpty(stu.getCycle())) {
        				return true;
        			}
        		}
        		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {//导师
        			BackTeacherExpansion bt=getTeacherByUserId(user.getId());
        			if ((bt != null)) {//不是企业导师
            		    if (!(TeacherType.TY_QY.getKey()).equals(bt.getTeachertype())) {//不是企业导师
            				if (StringUtil.isEmpty(user.getLoginName())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getSex())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getNo())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getName())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(bt.getTeachertype())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(bt.getServiceIntention())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getIdType())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getIdNumber())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(bt.getEducationType())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getEducation())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getEmail())) {
            					return true;
            				}
            				if (StringUtil.isEmpty(user.getMobile())) {
            					return true;
            				}
            		    }
        			}
        		}
        		return false;
        	}

            /**检查用户是否有后台登录的角色
            	 * @param user user对象中有role list
            //	 * @param rolebiztype
            	 * @return
            	 */
            	public static boolean checkHasAdminRole(User user) {
            		if(user==null||StringUtil.isEmpty(user.getId())){
            			return false;
            		}else{
            			user =UserUtils.get(user.getId());
            		}
            		List<Role> rs=user.getRoleList();
            		if(rs==null||rs.size()==0){
                        throw new RuntimeException("该用户没有角色 User:[" + user.getId() + "]");
            		}
            		for(Role r:rs){
            			if(StringUtil.isEmpty(r.getBizType())){
            				r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            			}
            			if(!RoleBizTypeEnum.XS.getValue().equals(r.getBizType())&&!RoleBizTypeEnum.DS.getValue().equals(r.getBizType())){
            				return true;
            			}
            		}
            		return false;
            	}

            /**检查用户角色变更是否可行 不可变更则有返回信息
             * @param nuser now
             * @param ouser old
             * @return
             */
            public static String checkRoleChange(User nuser,User ouser) {
            	if(nuser==null||ouser==null){
            		return null;
            	}
            	if(StringUtil.isNotEmpty(nuser.getId()) && StringUtil.checkEmpty(nuser.getRoleList())){
            	    nuser.setRoleList(UserUtils.roleDao.findListByUserId(nuser.getId()));
                }
            	if(StringUtil.checkEmpty(nuser.getRoleList())){
            		return "该用户没有角色 User:[" + nuser.getId() + "]";
            	}
            	List<Role> ors=ouser.getRoleList();
            	if(ors==null||ors.size()==0){
            		return null;
            	}
            	boolean hasStu=false;//是否有学生角色
            	boolean hasTea=false;//是否有导师角色
            	for(Role r: nuser.getRoleList()){
            		if(StringUtil.isEmpty(r.getBizType())){
            			r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            		}
            		if(RoleBizTypeEnum.XS.getValue().equals(r.getBizType())){
            			hasStu=true;
            			break;
            		}
            		if(RoleBizTypeEnum.DS.getValue().equals(r.getBizType())){
            			hasTea=true;
            			break;
            		}
            	}
            	if(hasStu){
            		for(Role r:ors){
            			if(StringUtil.isEmpty(r.getBizType())){
            				r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            			}
            			if(RoleBizTypeEnum.DS.getValue().equals(r.getBizType())){
            				return "不能从导师角色变更为学生角色";
            			}
            		}
            	}
            	if(hasTea){
            		for(Role r:ors){
            			if(StringUtil.isEmpty(r.getBizType())){
            				r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            			}
            			if(RoleBizTypeEnum.XS.getValue().equals(r.getBizType())){
            				return "不能从学生角色变更为导师角色";
            			}
            		}
            	}
            	return null;
            }

            /*检查用户角色组成 有返回信息则验证不通过*/
            public static String checkRoleList(List<Role> rs) {
            	if(rs==null||rs.size()==0){
            		return null;
            	}
            	boolean hasStu=false;//是否有学生角色
            	boolean hasTea=false;//是否有导师角色
            	for(Role r:rs){
            		if(StringUtil.isEmpty(r.getBizType())){
            			r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            		}
            		if(RoleBizTypeEnum.XS.getValue().equals(r.getBizType())){
            			hasStu=true;
            		}
            		if(RoleBizTypeEnum.DS.getValue().equals(r.getBizType())){
            			hasTea=true;
            		}
            		if(hasStu&&hasTea){
            			return "用户不能同时有学生和导师的角色";
            		}
            	}
            	return null;
            }

            /*检查用户角色组成 有返回信息则验证不通过*/
            /**检查用户是否有某个角色
             * @param user user对象中有role list
             * @param rolebiztype
             * @return
             */
			public static boolean checkHasRole(User user,RoleBizTypeEnum rolebiztype) {
				if(rolebiztype == null){
					return false;
				}
				return CoreUtils.checkHasRole(user, rolebiztype.getValue());
			}

			public static boolean checkHasRole(User user, CoreSval.Rtype rtype) {
				if(rtype == null){
					return false;
				}
				return CoreUtils.checkHasRole(user, rtype);
			}

}
