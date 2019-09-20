package com.oseasy.cms.modules.cms.service;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.cms.modules.cms.dao.CmsProvinceProjectDao;
import com.oseasy.cms.modules.cms.entity.CmsProvinceProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by PW on 2019/6/19.
 * @author liangjie
 */
@Service
@Transactional(readOnly = true)
public class CmsProvinceProjectService extends CrudService<CmsProvinceProjectDao,CmsProvinceProject> {
    @Autowired
    private CmsProvinceProjectDao cmsProvinceProjectDao;

    @Override
    public CmsProvinceProject get(String id){
        return super.get(id);
    }

    @Override
    public List<CmsProvinceProject> findList(CmsProvinceProject cmsProvinceProject){
        return super.findList(cmsProvinceProject);
    }

    @Override
    public Page<CmsProvinceProject> findPage(Page<CmsProvinceProject> page, CmsProvinceProject cmsProvinceProject){
        return super.findPage(page, cmsProvinceProject);
    }

}
