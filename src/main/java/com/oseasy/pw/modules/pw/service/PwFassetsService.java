package com.oseasy.pw.modules.pw.service;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.modules.pw.dao.PwFassetsDao;
import com.oseasy.pw.modules.pw.entity.PwFassets;
import com.oseasy.pw.modules.pw.entity.PwFassetsUhistory;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.exception.FassetsException;
import com.oseasy.pw.modules.pw.utils.FassetsUtils;
import com.oseasy.pw.modules.pw.vo.PwFassetsAssign;
import com.oseasy.pw.modules.pw.vo.PwFassetsBatch;
import com.oseasy.pw.modules.pw.vo.PwFassetsStatus;
import com.oseasy.util.common.utils.StringUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 固定资产Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwFassetsService extends CrudService<PwFassetsDao, PwFassets> {

    @Autowired
    private PwRoomService pwRoomService;

    @Autowired
    private PwFassetsUhistoryService pwFassetsUhistoryService;

    @Autowired
    private PwFassetsDao pwFassetsDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public PwFassets get(String id) {
        return super.get(id);
    }

    public List<PwFassets> findList(PwFassets pwFassets) {
        return super.findList(pwFassets);
    }

    public Page<PwFassets> findPage(Page<PwFassets> page, PwFassets pwFassets) {
        return super.findPage(page, pwFassets);
    }

    /**
     * 已分配的资产.
     *
     * @param pwFassets 实体
     * @return List
     */
    public List<PwFassets> findListByYfp(PwFassets pwFassets) {
        if ((pwFassets.getPwRoom() == null) || StringUtil.isEmpty(pwFassets.getPwRoom().getId())) {
            return Lists.newArrayList();
        }
        pwFassets.setStatus(PwFassetsStatus.USING.getValue());
        return super.findList(pwFassets);
    }

    /**
     * 已分配的资产.
     *
     * @param page      分页
     * @param pwFassets 实体
     * @return Page
     */
    public Page<PwFassets> findPageByYfp(Page<PwFassets> page, PwFassets pwFassets) {
        pwFassets.setPage(page);
        page.setList(findListByYfp(pwFassets));
        return page;
    }

    public List<PwFassets> findListByRoom(PwFassets pwFassets) {
        if ((pwFassets.getPwRoom() == null) || StringUtil.isEmpty(pwFassets.getPwRoom().getId())) {
            return Lists.newArrayList();
        }
        return dao.findListByRoom(pwFassets);
    }

    public Page<PwFassets> findPageByRoom(Page<PwFassets> page, PwFassets pwFassets) {
        pwFassets.setPage(page);
        page.setList(findListByRoom(pwFassets));
        return page;
    }

    public List<PwFassets> findListByNoRoom(PwFassets pwFassets) {
        if ((pwFassets.getPwRoom() != null) && StringUtil.isNotEmpty(pwFassets.getPwRoom().getId())) {
            return Lists.newArrayList();
        }
        return dao.findListByNoRoom(pwFassets);
    }

    public Page<PwFassets> findPageByNoRoom(Page<PwFassets> page, PwFassets pwFassets) {
        pwFassets.setPage(page);
        page.setList(findListByNoRoom(pwFassets));
        return page;
    }

    //TODO
    @Transactional(readOnly = false)
    public void saveSet(PwFassets pwFassets) {
        PwFassets newPwFassets = get(pwFassets.getId());
        if (newPwFassets != null) {
            newPwFassets.setPwRoom(pwFassets.getPwRoom());
            newPwFassets.setStatus(PwFassetsStatus.USING.getValue());
            save(newPwFassets);
        }
    }

    /**
     * 取消分配
     *
     * @param pwFassetsList
     */
    @Transactional(readOnly = false)
    public void unAssign(List<PwFassets> pwFassetsList) {
        for(PwFassets pwFassets :pwFassetsList ){
            PwFassets newPwFassets = get(pwFassets.getId());
            if (newPwFassets == null) {
                logger.warn("指定的资产不存在");
                throw new FassetsException("指定的资产不存在");
            }
            if (!newPwFassets.getStatus().equals(PwFassetsStatus.USING.getValue())) {
                logger.warn("当前资产未在使用中,无须取消分配");
                throw new FassetsException("当前资产未在使用中,无须取消分配");
            }

            PwFassetsUhistory pwFassetsUhistory = new PwFassetsUhistory();
            pwFassetsUhistory.setPwRoom(newPwFassets.getPwRoom());
            pwFassetsUhistory.setPwFassets(newPwFassets);
            pwFassetsUhistory.setRespName(newPwFassets.getRespName());
            pwFassetsUhistory.setRespPhone(newPwFassets.getRespPhone());
            pwFassetsUhistory.setRespMobile(newPwFassets.getRespMobile());
            pwFassetsUhistory.setStartDate(newPwFassets.getStartDate());
            pwFassetsUhistory.setEndDate(new Date());
            pwFassetsUhistoryService.save(pwFassetsUhistory);


            //状态设置为闲置
            newPwFassets.setStatus(PwFassetsStatus.UNUSED.getValue());
            //清空责任人信息
            newPwFassets.getPwRoom().setId(null);
            newPwFassets.setRespName(null);
            newPwFassets.setRespPhone(null);
            newPwFassets.setRespMobile(null);
            newPwFassets.setStartDate(null);

            this.save(newPwFassets);
        }


    }

    /**
     * 分配
     *
     * @param pwFassets
     */
    @Transactional(readOnly = false)
    public void assign(PwFassets pwFassets) {
        PwFassets newPwFassets = get(pwFassets.getId());
        //检查必要信息
        if (newPwFassets == null) {
            logger.warn("没有指定资产");
            throw new FassetsException("没有指定资产");
        }
        if (!newPwFassets.getStatus().equals(PwFassetsStatus.UNUSED.getValue())
                || StringUtils.isNotBlank(newPwFassets.getPwRoom().getId())) {
            logger.warn("当前资产不是闲置状态");
            throw new FassetsException("当前资产不是闲置状态");
        }
        if (pwFassets.getPwRoom() == null || pwRoomService.get(pwFassets.getPwRoom().getId()) == null) {
            logger.warn("未找到要绑定的房间的信息");
            throw new FassetsException("未找到要绑定的房间的信息");
        }
        //设置相关的信息
        newPwFassets.setRespName(pwFassets.getRespName());
        newPwFassets.setRespPhone(pwFassets.getRespPhone());
        newPwFassets.setMobile(pwFassets.getMobile());
        newPwFassets.setStartDate(new Date());
        //状态设置为使用中
        newPwFassets.setStatus(PwFassetsStatus.USING.getValue());
        //设置房间信息
        newPwFassets.setPwRoom(new PwRoom(pwFassets.getPwRoom().getId()));
        this.save(newPwFassets);
    }


    @Transactional(readOnly = false)
    public int batchAssign(PwFassetsAssign pwFassetsAssign) {
        if (StringUtils.isBlank(pwFassetsAssign.getFassetsIds())) {
            logger.warn("没有指定资产");
            throw new FassetsException("没有指定资产");
        }
        if (StringUtils.isBlank(pwFassetsAssign.getRoomId()) || pwRoomService.get(pwFassetsAssign.getRoomId()) == null) {
            logger.warn("未找到要绑定的房间的信息");
            throw new FassetsException("未找到要绑定的房间的信息");
        }
        return batchAssign(new PwFassets(new PwRoom(pwFassetsAssign.getRoomId()), pwFassetsAssign.getRespName(), pwFassetsAssign.getRespPhone(), pwFassetsAssign.getRespMobile()), findListByIds(Arrays.asList(StringUtil.split(pwFassetsAssign.getFassetsIds(), StringUtil.DOTH))));
    }

    /**
     * 批量分配资产.
     *
     * @param pwFassets   资产参数
     * @param qpwFassetss 资产待修改列表
     * @return
     */
    @Transactional(readOnly = false)
    public int batchAssign(PwFassets pwFassets, List<PwFassets> qpwFassetss) {
        if (StringUtil.checkEmpty(qpwFassetss)) {
            logger.warn("未找到资产数据");
            throw new FassetsException("未找到资产数据");
        }

        List<PwFassets> pwFassetss = Lists.newArrayList();
        for (PwFassets newPwFassets : qpwFassetss) {
            //检查必要信息
            if (!(PwFassetsStatus.UNUSED.getValue()).equals(newPwFassets.getStatus())
                    || (newPwFassets.getPwRoom() != null && StringUtils.isNotBlank(newPwFassets.getPwRoom().getId()))) {
                logger.warn(String.format("编号为【%s】的资产不是闲置状态", newPwFassets.getName()));
                throw new FassetsException(String.format("编号为【%s】的资产不是闲置状态", newPwFassets.getName()));
            }

            //设置相关的信息
            newPwFassets.setRespName(pwFassets.getRespName());
            newPwFassets.setRespPhone(pwFassets.getRespPhone());
            newPwFassets.setRespMobile(pwFassets.getRespMobile());
            newPwFassets.setStartDate(new Date());
            //状态设置为使用中
            newPwFassets.setStatus(PwFassetsStatus.USING.getValue());
            //设置房间信息
            newPwFassets.setPwRoom(pwFassets.getPwRoom());
            pwFassetss.add(newPwFassets);
        }
        return updateByPL(pwFassetss);
    }

    @Transactional(readOnly = false)
    public void save(PwFassets pwFassets) {
        if (StringUtils.isBlank(pwFassets.getId())) {
            pwFassets.setId(IdGen.uuid());
            pwFassets.setIsNewRecord(true);
        }
        if (StringUtils.isBlank(pwFassets.getName())) {
            pwFassets.setName(genFasNo(pwFassets));
        }
        /**
         * 已分配房间，修改状态为已使用
         */
        if (pwFassets.getPwRoom() != null && StringUtils.isNotBlank(pwFassets.getPwRoom().getId())) {
            pwFassets.setStatus(PwFassetsStatus.USING.getValue());
        } else if (StringUtil.isBlank(pwFassets.getStatus())) {
            pwFassets.setStatus(PwFassetsStatus.UNUSED.getValue());
        }
        super.save(pwFassets);
    }

    private String genFasNo(PwFassets pwFassets) {
        /**
         * 最多尝试3次生成编号,乐观锁
         */
        String fasNo = FassetsUtils.genFasNo(pwFassets.getPwCategory());
        int count = 1;
        while (count < 3 && StringUtils.isBlank(fasNo)) {
            fasNo = FassetsUtils.genFasNo(pwFassets.getPwCategory());
            count++;
        }
        if (StringUtils.isBlank(fasNo)) {
            logger.warn("生成编号失败，请稍候再试");
            throw new FassetsException("生成编号失败，请稍候再试");
        }
        return fasNo;
    }

    @Transactional(readOnly = false)
    public void batchSave(PwFassetsBatch pwFassetsModel) {
        if (pwFassetsModel.getAmount() > 0) {
            PwFassets pwFassets = pwFassetsModel.getPwFassets();
            if (pwFassets == null) {
                logger.warn("资产信息为空");
                throw new FassetsException("资产信息为空");
            }
            if (StringUtils.isNotBlank(pwFassetsModel.getRoomId())) {
                PwRoom pwRoom = pwRoomService.get(pwFassetsModel.getRoomId());
                if (pwRoom == null) {
                    logger.warn("指定的房间不存在");
                    throw new FassetsException("指定的房间不存在");
                }
                pwFassets.setPwRoom(pwRoom);
                pwFassets.setStatus(PwFassetsStatus.USING.getValue());
            }
            pwFassets.setRemarks(pwFassetsModel.getRemarks());
            if (StringUtils.isBlank(pwFassets.getStatus())) {
                pwFassets.setStatus(PwFassetsStatus.UNUSED.getValue());
            }
            User user = UserUtils.getUser();
            if (StringUtils.isNotBlank(user.getId())) {
                pwFassets.setCreateBy(user);
                pwFassets.setUpdateBy(user);
            }
            pwFassets.setIsNewRecord(true);
            List<PwFassets> list = new ArrayList<>(pwFassetsModel.getAmount());
            for (int i = 0; i < pwFassetsModel.getAmount(); i++) {
                PwFassets fassets = new PwFassets();
                try {
                    BeanUtils.copyProperties(fassets, pwFassets);
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                    throw new FassetsException("数据异常");
                }
                fassets.setId(IdGen.uuid());//ID
                fassets.setName(genFasNo(pwFassets));//编号
                fassets.setCreateDate(new Date());
                fassets.setUpdateDate(new Date());
                list.add(fassets);
            }
            if (!list.isEmpty()) {
                pwFassetsDao.batchInsert(list);
            }
        }
    }

    /**
     * 批量更新数据.
     *
     * @param entitys
     * @return
     */
    @Transactional(readOnly = false)
    public int updateByPL(String rid, String status, List<PwFassets> entitys) {
        if (StringUtil.isEmpty(rid)) {
            return 0;
        }
        for (PwFassets pwf : entitys) {
            if (pwf.getPwRoom() == null) {
                pwf.setPwRoom(new PwRoom(rid));
            } else {
                pwf.getPwRoom().setId(rid);
            }
            pwf.setStatus(status);
        }
        return updateByPL(entitys);
    }
    @Transactional(readOnly = false)
    public int updateByFassetsPL(String status, List<PwFassets> entitys) {
        for (PwFassets pwf : entitys) {
            pwf.setStatus(status);
        }
        return updateByPL(entitys);
    }

    @Transactional(readOnly = false)
    public int updateByPL(List<PwFassets> entitys) {
        if ((entitys == null) || (entitys.size() <= 0)) {
            return 0;
        }
        return dao.updateByPL(entitys);
    }


    @Transactional(readOnly = false)
    public void delete(PwFassets pwFassets) {
        PwFassets newFassets = this.get(pwFassets);
        if (newFassets == null) {
            logger.warn("指定的资产不存在");
            throw new FassetsException("指定的资产不存在");
        }
        /**
         * 删除历史使用记录
         */
        List<String> fassetsIds = new ArrayList<>(1);
        fassetsIds.add(pwFassets.getId());
        pwFassetsUhistoryService.deleteByFassetsIds(fassetsIds);
        super.delete(pwFassets);
    }

    public List<PwFassets> findListByIds(List<String> ids) {
        return dao.findListByIds(ids);
    }


    @Transactional(readOnly = false)
    public void changeBroken(PwFassets pwFassets) {
        PwFassets newPwFassets = this.get(pwFassets.getId());
        if (newPwFassets == null) {
            logger.warn("指定的资产不存在");
            throw new FassetsException("指定的资产不存在");
        }
        if (!newPwFassets.getStatus().equals(PwFassetsStatus.UNUSED.getValue())
                || (newPwFassets.getPwRoom() != null && StringUtils.isNotBlank(newPwFassets.getPwRoom().getId()))) {
            logger.warn("资产不是闲置状态");
            throw new FassetsException("资产不是闲置状态");
        }
        newPwFassets.setStatus(PwFassetsStatus.BROKEN.getValue());
        this.save(newPwFassets);
    }

    @Transactional(readOnly = false)
    public void changeUnused(PwFassets pwFassets) {
        PwFassets newPwFassets = this.get(pwFassets.getId());
        if (newPwFassets == null) {
            logger.warn("指定的资产不存在");
            throw new FassetsException("指定的资产不存在");
        }
        if (!newPwFassets.getStatus().equals(PwFassetsStatus.BROKEN.getValue())) {
            logger.warn("资产不是损坏状态");
            throw new FassetsException("资产不是损坏状态");
        }
        newPwFassets.setStatus(PwFassetsStatus.UNUSED.getValue());
        this.save(newPwFassets);
    }

    /**
     * 释放房间关联的所有资产，同时清理历史使用明细信息
     *
     * @param roomIds
     */
    @Transactional(readOnly = false)
    public void clearByRoomIds(List<String> roomIds) {
        if (!roomIds.isEmpty()) {
            pwFassetsDao.updateByRoomIds(roomIds);
            pwFassetsUhistoryService.deleteByRoomIds(roomIds);
        }
    }


}