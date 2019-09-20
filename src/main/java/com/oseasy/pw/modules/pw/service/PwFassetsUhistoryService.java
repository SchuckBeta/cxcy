package com.oseasy.pw.modules.pw.service;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwFassetsUhistoryDao;
import com.oseasy.pw.modules.pw.entity.PwFassetsUhistory;
import com.oseasy.util.common.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 固定资产使用记录Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwFassetsUhistoryService extends CrudService<PwFassetsUhistoryDao, PwFassetsUhistory> {

    @Autowired
    private PwFassetsUhistoryDao pwFassetsUhistoryDao;

    public PwFassetsUhistory get(String id) {
        return super.get(id);
    }

    public List<PwFassetsUhistory> findList(PwFassetsUhistory pwFassetsUhistory) {
        return super.findList(pwFassetsUhistory);
    }

    public Page<PwFassetsUhistory> findPage(Page<PwFassetsUhistory> page, PwFassetsUhistory pwFassetsUhistory) {
        Page<PwFassetsUhistory> pageList = super.findPage(page, pwFassetsUhistory);
        String keys = pwFassetsUhistory.getKeys();
        if(StringUtil.isNotEmpty(keys)){
            List<PwFassetsUhistory> list = pageList.getList();
            List<PwFassetsUhistory> listTemp = Lists.newArrayList();
            for(PwFassetsUhistory pwf : list){
                if(pwf.getPwFassets().getName().contains(keys) || pwf.getPwRoom().getName().contains(keys)
                        || pwf.getRespName().contains(keys)){
                    listTemp.add(pwf);
                }
            }
            pageList.setList(listTemp);
            pageList.setCount(listTemp.size());
        }

        return pageList;
    }

    @Transactional(readOnly = false)
    public void save(PwFassetsUhistory pwFassetsUhistory) {
        super.save(pwFassetsUhistory);
    }

    @Transactional(readOnly = false)
    public void delete(PwFassetsUhistory pwFassetsUhistory) {
        super.delete(pwFassetsUhistory);
    }

    @Transactional(readOnly = false)
    public void deleteByFassetsIds(List<String> fassetsIds) {
        pwFassetsUhistoryDao.deleteByFassetsIds(fassetsIds);
    }

    @Transactional(readOnly = false)
    public void deleteByRoomIds(List<String> roomIds) {
        pwFassetsUhistoryDao.deleteByRoomIds(roomIds);
    }


}