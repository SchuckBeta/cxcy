package com.oseasy.pw.modules.pw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.pw.modules.pw.dao.PwFloorDesignerDao;
import com.oseasy.pw.modules.pw.entity.PwFloorDesigner;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 楼层设计Service.
 *
 * @author 章传胜
 * @version 2017-11-28
 */
@Service
@Transactional(readOnly = true)
public class PwFloorDesignerService extends CrudService<PwFloorDesignerDao, PwFloorDesigner> {

    @Autowired
    private PwFloorDesignerDao pwFloorDesignerDao;

    public PwFloorDesigner get(String id) {
        return super.get(id);
    }
    public PwFloorDesigner getByFloorid(String floorid) {
        return dao.getByFloorid(floorid);
    }

    public List<PwFloorDesigner> findList(PwFloorDesigner pwFloorDesigner) {
        return super.findList(pwFloorDesigner);
    }

    public Page<PwFloorDesigner> findPage(Page<PwFloorDesigner> page, PwFloorDesigner pwFloorDesigner) {
        return super.findPage(page, pwFloorDesigner);
    }

    @Transactional(readOnly = false)
    public void save(PwFloorDesigner pwFloorDesigner) {
        super.save(pwFloorDesigner);
    }

    @Transactional(readOnly = false)
    public void delete(PwFloorDesigner pwFloorDesigner) {
        super.delete(pwFloorDesigner);
    }

    @Transactional(readOnly = false)
    public int insertAll(List<PwFloorDesigner> pwFloorDesigners) {
        List<String> ids = new ArrayList<String>();
        List<PwFloorDesigner> list = new ArrayList<PwFloorDesigner>();
        if (pwFloorDesigners != null && pwFloorDesigners.size() > 0) {
            for (PwFloorDesigner pfd : pwFloorDesigners) {
                if (!StringUtil.isEmpty(pfd.getId())) {
                    ids.add(pfd.getId().trim());
                }
                pfd.setId(IdGen.uuid());
                pfd.setDelFlag("0");
                pfd.setIsclickable(pfd.getIsclickable() != null ? Integer.valueOf(pfd.getIsclickable()) : 0);
                list.add(pfd);
            }
        }
//       String[] sts =  ids.toArray(new String[ids.size()]);
        if (ids.size() > 0) {
            pwFloorDesignerDao.deleteAll(ids);
        }
        return pwFloorDesignerDao.insertAll(list);
    }

    public void deleteAll(List<String> ids) {
        pwFloorDesignerDao.deleteAll(ids);
    }

}