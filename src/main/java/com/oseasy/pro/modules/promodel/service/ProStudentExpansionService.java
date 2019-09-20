package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.entity.ActYwYear;
import com.oseasy.act.modules.actyw.service.ActYwYearService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.dao.ProStudentExpansionDao;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.entity.ProStudentExpansion;
import com.oseasy.sys.modules.sys.dao.StudentExpansionDao;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;

/**
 * 学生信息表Service
 * @author zy
 * @version 2017-03-27
 */
@Service
@Transactional(readOnly = true)
public class ProStudentExpansionService extends CrudService<ProStudentExpansionDao, StudentExpansion> {
    @Autowired
    private StudentExpansionDao studentExpansionDao;
    @Autowired
    private ActYwYearService actYwYearService;
    @Autowired
    private ProjectDeclareService projectDeclareService;

    public StudentExpansion getByUserId(String userId) {
        return studentExpansionDao.getByUserId(userId);
    }

    public List<GContestUndergo> findContestByUserId(String userId) {
        return dao.findGContestByStudentId(userId);
    }

    public List<ProjectExpVo> findProjectByStudentId(String id) {
        List<ProjectExpVo> projectList=dao.findProjectByStudentId(id);
        for(int i=0;i<projectList.size();i++){
            ActYwYear actYwYear = actYwYearService.getByActywIdAndYear(projectList.get(i).getActywId()
            ,projectList.get(i).getYear());
            if(actYwYear!=null){
                projectList.get(i).setStartDate(actYwYear.getStartDate());
                projectList.get(i).setEndDate(actYwYear.getEndDate());
            }
        }
        return projectList;
    }

    public List<GContestUndergo> findGContestByStudentId(String id) {
        List<GContestUndergo> gcontestList= dao.findGContestByStudentId(id);
        for(int i=0;i<gcontestList.size();i++){
            ActYwYear actYwYear=actYwYearService.getByActywIdAndYear(gcontestList.get(i).getActywId()
            ,gcontestList.get(i).getYear());
            if(actYwYear!=null){
                gcontestList.get(i).setStartDate(actYwYear.getStartDate());
                gcontestList.get(i).setEndDate(actYwYear.getEndDate());
            }
        }
        return gcontestList;
    }


    public Page<ProStudentExpansion> findStudentPage(Page<ProStudentExpansion> page, Map<String, Object> param) {
        if (page.getPageNo()<=0) {
            page.setPageNo(1);
        }
        int count = studentExpansionDao.getStuExListCount(param);
        param.put("offset", (page.getPageNo()-1)*page.getPageSize());
        param.put("pageSize", page.getPageSize());
        List<Map<String,String>> listStudentExpansion=null;
        List<ProStudentExpansion> listSt=new ArrayList<ProStudentExpansion>();
        if (count>0) {
            listStudentExpansion = studentExpansionDao.getStuExList(param);
            for(Map<String,String> map:listStudentExpansion) {
                ProStudentExpansion newse = new ProStudentExpansion(get(String.valueOf(map.get("id"))));
                List<ProjectExpVo> projectList = projectDeclareService.getExpsByUserId(newse.getUser().getId());//查询项目经历
                List<GContestUndergo> gContestList = dao.findGContestByStudentId(newse.getUser().getId());

                if (gContestList.size()>0) {
                     newse.setgContestList(gContestList);
                }
                if (projectList.size()>0) {
                    newse.setProjectList(projectList);
                }
                listSt.add(newse);
            }
        }
        page.setCount(count);
        page.setList(listSt);
        page.initialize();
        return page;
    }
}