package com.oseasy.act.modules.actyw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwGroleDao;
import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 节点角色Service.
 * @author chenh
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwGroleService extends CrudService<ActYwGroleDao, ActYwGrole> {
    @Autowired
    UserDao userDao;

	public ActYwGrole get(String id) {
		ActYwGrole actYwGrole = super.get(id);
		return actYwGrole;
	}

	public List<ActYwGrole> findList(ActYwGrole actYwGrole) {
		return super.findList(actYwGrole);
	}

	public Page<ActYwGrole> findPage(Page<ActYwGrole> page, ActYwGrole actYwGrole) {
		return super.findPage(page, actYwGrole);
	}

    public List<ActYwGrole> checkUseByRole(List<String> roleIds) {
        return dao.checkUseByRole(roleIds);
    }

	@Transactional(readOnly = false)
	public void save(ActYwGrole actYwGrole) {
		super.save(actYwGrole);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwGrole actYwGrole) {
		super.delete(actYwGrole);
	}

    /**
     * 批量保存.
     * @param groles
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwGrole> groles) {
        dao.savePl(groles);
    }

    /**
     * 根据流程批量删除.
     * @param groupId 流程ID
     */
    @Transactional(readOnly = false)
    public void deletePlwlByGroup(String groupId) {
        dao.deletePlwlByGroup(groupId);
    }

    /**
     * 根据流程批量删除.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     */
    @Transactional(readOnly = false)
    public void deletePlwl(String groupId, String gnodeId) {
        dao.deletePlwl(groupId, gnodeId);
    }

    public List<Map<String,String>> findListByRoleNames(List<ActYwGrole> roles) {
        List<Map<String,String>> userRoleList=new ArrayList<Map<String,String>>();
        Office office= CoreUtils.getOffice(CoreIds.NCE_SYS_OFFICE_TOP.getId());
        for(ActYwGrole actYwGrole:roles) {
            Role role = actYwGrole.getRole();
            List<User> in=userDao.findListByRoleName(role.getEnname());
            if(StringUtil.checkNotEmpty(in)){
                for(int i=0;i<in.size();i++){
                    Map<String,String> map =new HashMap<String,String>();
                    User index=in.get(i);
                    map.put("id",index.getId());
                    map.put("name",index.getName());
                    map.put("roleId",role.getId());
                    map.put("roleName",role.getName());
                    if(StringUtil.isNotEmpty(index.getOfficeId()) && StringUtil.isNotEmpty(index.getOffice().getId())){
                        map.put("collegeId", index.getOffice().getId());
                    }else{
                        map.put("collegeId",office.getId());
                    }

                    if(StringUtil.isNotEmpty(index.getOfficeId())&& StringUtil.isNotEmpty(index.getOffice().getName())){
                        map.put("collegeName",index.getOffice().getName());
                    }else{
                        map.put("collegeName",office.getName());
                    }

                    userRoleList.add(map);
                }
            }

        }
        return userRoleList;
    }

    //根据节点角色获得页面人员信息
    public JSONObject findUserJsByRoles(List<ActYwGrole> roles) {
        JSONObject js=new JSONObject();
        JSONArray jsRoles=new JSONArray();
        JSONArray jsColleges=new JSONArray();
        for(ActYwGrole actYwGrole:roles){
            Role role=actYwGrole.getRole();
            JSONObject jsRole=new JSONObject();
            jsRole.put("id",role.getId());
            jsRole.put("name",role.getName());

            //根据学院分组
            List<User> userList = userDao.findListByRoleName(role.getEnname());
            List<Map<String,String>> collegeList =new ArrayList<Map<String,String>>();
            int initNum=0;
            for(User user:userList){
                Map<String,String> college=new HashMap<String,String>();
                //判断是否存在相同的学院
                boolean isHavaCollegeName=false;
                if(user.getOffice()!=null && user.getOffice().getName()!=null){
                    for(int i=0;i<collegeList.size();i++){
                        Map<String,String> incollege=collegeList.get(i);
                        if(incollege.get("name").equals(user.getOffice().getName())){
                            isHavaCollegeName=true;
                            break;
                        }
                    }
                    if(!isHavaCollegeName){
                        college.put("id",user.getOffice().getId());
                        college.put("name",user.getOffice().getName());
                        collegeList.add(college);
                    }
                }else{
                    if(initNum<1){
                        college.put("id","000");
                        college.put("name","校级");
                        collegeList.add(college);
                        initNum++;
                    }
                }
            }
            for(int coll=0;coll<collegeList.size();coll++){
                JSONObject collegejs=new JSONObject();
                collegejs.put("id",(String)collegeList.get(coll).get("id"));
                collegejs.put("name",(String)collegeList.get(coll).get("name"));
                JSONArray userListJs=new JSONArray();
                for(User user:userList){
                    JSONObject userjs=new JSONObject();
                    if(user.getOffice()!=null && user.getOffice().getName()!=null){
                        if(user.getOffice().getName().equals(collegejs.get("name"))){
                            userjs.put("id",user.getId());
                            userjs.put("name",user.getName());
                            userListJs.add(userjs);
                        }
                    }else{
                        if("000".equals(collegejs.get("id"))){
                            userjs.put("id",user.getId());
                            userjs.put("name",user.getName());
                            userListJs.add(userjs);
                        }
                    }
                }
                collegejs.put("assignUsers",userListJs);
                jsColleges.add(collegejs);
            }
            jsRole.put("colleges",jsColleges);
            jsRoles.add(jsRole);
        }
        js.put("roles",jsRoles);
        js.put("colleges",jsColleges);
        return js;
    }
}