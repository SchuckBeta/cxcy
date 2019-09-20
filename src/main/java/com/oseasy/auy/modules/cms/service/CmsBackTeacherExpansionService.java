package com.oseasy.auy.modules.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导师信息表Service
 * @author chenhao
 * @version 2017-03-31
 */
@Service
@Transactional(readOnly = true)
public class CmsBackTeacherExpansionService extends BackTeacherExpansionService {
    @Autowired
    private SysViewsService sysViewsService;

    public Page<BackTeacherExpansion> findTeacherPage(Page<BackTeacherExpansion> page, BackTeacherExpansion backTeacherExpansion) {
        backTeacherExpansion.setPage(page);
        List<BackTeacherExpansion> siteTeacherList= new ArrayList<BackTeacherExpansion> ();
        if("3".equals(backTeacherExpansion.getTeachertype())){
            backTeacherExpansion.setTeachertype(null);
            backTeacherExpansion.setSiteShow(Const.YES);
            siteTeacherList=findExpertList(backTeacherExpansion);
        }else{
            siteTeacherList=findTeacherList(backTeacherExpansion);
        }
        List<BackTeacherExpansion> siteTeacherListnew =new ArrayList<BackTeacherExpansion> ();
        if(siteTeacherList != null && siteTeacherList.size() > 0){
            for(int i=0;i<siteTeacherList.size();i++) {
                if (siteTeacherList.get(i).getId()!=null) {
                    BackTeacherExpansion backTeacherExpansionIndex=siteTeacherList.get(i);
                    Boolean isLike=sysViewsService.getIsLike(UserUtils.getUser().getId(),backTeacherExpansionIndex.getUser().getId());
                    if(isLike){
                        backTeacherExpansionIndex.setIsLike("1");
                    }
                    if (StringUtil.isEmpty(backTeacherExpansionIndex.getUser().getViews())) {
                        backTeacherExpansionIndex.getUser().setViews("0");
                    }
                    if (StringUtil.isEmpty(backTeacherExpansionIndex.getUser().getLikes())) {
                        backTeacherExpansionIndex.getUser().setLikes("0");
                    }
                    List <String> tes=teacherKeywordService.getStringKeywordByTeacherid(backTeacherExpansionIndex.getId());
                    if (tes.size()>0) {
                        backTeacherExpansionIndex.setKeywords(tes);
                    }
                }
                siteTeacherListnew.add(siteTeacherList.get(i));
            }
        }
        page.setList(siteTeacherListnew);
        return page;
    }

    public Page<BackTeacherExpansion> findProvinceTeacherPage(Page<BackTeacherExpansion> page, BackTeacherExpansion backTeacherExpansion) {
        backTeacherExpansion.setPage(page);
        List<BackTeacherExpansion> siteTeacherList= new ArrayList<BackTeacherExpansion> ();
        siteTeacherList=findProvinceTeacherList(backTeacherExpansion);
        List<BackTeacherExpansion> siteTeacherListnew =new ArrayList<BackTeacherExpansion> ();
        if(siteTeacherList != null && siteTeacherList.size() > 0){
            for(int i=0;i<siteTeacherList.size();i++) {
                if (siteTeacherList.get(i).getId()!=null) {
                    BackTeacherExpansion backTeacherExpansionIndex=siteTeacherList.get(i);
                    Boolean isLike=sysViewsService.getIsLike(UserUtils.getUser().getId(),backTeacherExpansionIndex.getUser().getId());
                    if(isLike){
                        backTeacherExpansionIndex.setIsLike("1");
                    }
                    if (StringUtil.isEmpty(backTeacherExpansionIndex.getUser().getViews())) {
                        backTeacherExpansionIndex.getUser().setViews("0");
                    }
                    if (StringUtil.isEmpty(backTeacherExpansionIndex.getUser().getLikes())) {
                        backTeacherExpansionIndex.getUser().setLikes("0");
                    }
                    List <String> tes=teacherKeywordService.getStringKeywordByTeacherid(backTeacherExpansionIndex.getId());
                    if (tes.size()>0) {
                        backTeacherExpansionIndex.setKeywords(tes);
                    }
                }
                siteTeacherListnew.add(siteTeacherList.get(i));
            }
        }
        page.setList(siteTeacherListnew);
        return page;
    }

}