package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwEnterDetailDao;
import com.oseasy.pw.modules.pw.entity.PwEnterDetail;
import com.oseasy.pw.modules.pw.vo.PwEnterType;

/**
 * 入驻申报详情Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwEnterDetailService extends CrudService<PwEnterDetailDao, PwEnterDetail> {
    @Autowired
    PwCompanyService pwCompanyService;

    public PwEnterDetail get(String id) {
        return super.get(id);
    }

    public List<PwEnterDetail> findList(PwEnterDetail pwEnterDetail) {
        return super.findList(pwEnterDetail);
    }

    public Page<PwEnterDetail> findPage(Page<PwEnterDetail> page, PwEnterDetail pwEnterDetail) {
        return super.findPage(page, pwEnterDetail);
    }

    public List<PwEnterDetail> findListByTeam(PwEnterDetail pwEnterDetail) {
        return dao.findListByTeam(pwEnterDetail);
    }

    public Page<PwEnterDetail> findPageByTeam(Page<PwEnterDetail> page, PwEnterDetail entity) {
        entity.setPage(page);
        page.setList(findListByTeam(entity));
        return page;
    }

    public List<PwEnterDetail> findListByQy(PwEnterDetail pwEnterDetail) {
        return dao.findListByQy(pwEnterDetail);
    }

    public Page<PwEnterDetail> findPageByQy(Page<PwEnterDetail> page, PwEnterDetail entity) {
        entity.setPage(page);
        page.setList(findListByQy(entity));
        return page;
    }

    public List<PwEnterDetail> findListByXm(PwEnterDetail pwEnterDetail) {
        return dao.findListByXm(pwEnterDetail);
    }

    public Page<PwEnterDetail> findPageByXm(Page<PwEnterDetail> page, PwEnterDetail entity) {
        entity.setPage(page);
        page.setList(findListByXm(entity));
        return page;
    }

    public List<PwEnterDetail> findListByAll(PwEnterDetail pwEnterDetail) {
        return dao.findListByAll(pwEnterDetail);
    }

    public Page<PwEnterDetail> findPageByAll(Page<PwEnterDetail> page, PwEnterDetail entity) {
        entity.setPage(page);
        page.setList(findListByAll(entity));
        return page;
    }


    @Transactional(readOnly = false)
    public void save(PwEnterDetail pwEnterDetail) {
        super.save(pwEnterDetail);
    }

    @Transactional(readOnly = false)
    public PwEnterDetail saveRT(PwEnterDetail pwEnterDetail) {
        save(pwEnterDetail);
        return pwEnterDetail;
    }

    /**
     * 企业入驻信心保存.
     *
     * @param pwEnterDetail
     */
    @Transactional(readOnly = false)
    public PwEnterDetail saveCompany(PwEnterDetail pwEnterDetail) {
        if (pwEnterDetail.getIsNewRecord()) {
//            if ((pwEnterDetail.getPwEnter() != null) && (pwEnterDetail.getPwCompany() != null)) {
//                PwCompany newPwCompany = pwEnterDetail.getPwCompany();
//                pwCompanyService.save(newPwCompany);
//                pwEnterDetail.setPwCompany(newPwCompany);
//                pwEnterDetail.setRid(newPwCompany.getId());
//                save(pwEnterDetail);
//                return pwEnterDetail;
//            }
        } else {
            save(pwEnterDetail);
//            pwEnterDetail.getPwCompany().setIsNewRecord(false);
//            pwCompanyService.save(pwEnterDetail.getPwCompany());
            return pwEnterDetail;
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void delete(PwEnterDetail pwEnterDetail) {
        super.delete(pwEnterDetail);
    }

    @Transactional(readOnly = false)
    public void deleteByEid(String eid) {
        dao.deleteByEid(eid);
    }

    /**
     * 物理删除数据.
     * @param perDetail
     */
    @Transactional(readOnly = false)
    public void deleteWL(PwEnterDetail perDetail) {
        dao.deleteWL(perDetail);
    }

    public PwEnterDetail getByPwEnterIdAndType(String id, String type) {
        return dao.getByPwEnterIdAndType(id,type);
    }

    public List<PwEnterDetail> getByPwEnterXmIdAndType(String id, String type) {
        return dao.getByPwEnterXmIdAndType(id,type);
    }
    @Transactional(readOnly = false)
    //批量保存详情表
    public void saveAll(List<PwEnterDetail> detailList) {
        dao.saveAll(detailList);
    }

    public List<PwEnterDetail> checkPwEnterTeam(String pwEnterId, String teamId) {
        return dao.checkPwEnterTeam(pwEnterId,teamId, PwEnterType.PET_TEAM.getKey());
    }

    public void deleteByEidAndType(String eid, String type) {
        dao.deleteByEidAndType(eid,type);
    }

    public int checkPwEnterByTeamId(String teamId) {
        return dao.checkPwEnterByTeamId(teamId);
    }
}