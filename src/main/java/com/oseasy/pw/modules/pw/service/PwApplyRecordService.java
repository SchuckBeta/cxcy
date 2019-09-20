package com.oseasy.pw.modules.pw.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.vo.ActYwApplyVo;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.modules.pw.dao.PwApplyRecordDao;
import com.oseasy.pw.modules.pw.entity.PwApplyRecord;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.vo.PwAppointmentStatus;
import com.oseasy.pw.modules.pw.vo.PwEnterAuditEnum;
import com.oseasy.pw.modules.pw.vo.PwEnterBgremarks;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * pwApplyRecordService.
 * @author zy
 * @version 2018-11-20
 */
@Service
@Transactional(readOnly = true)
public class PwApplyRecordService extends CrudService<PwApplyRecordDao, PwApplyRecord> {
    @Autowired
    PwEnterService pwEnterService;
	@Autowired
	OaNotifyService oaNotifyService;

	public PwApplyRecord get(String id) {
		return super.get(id);
	}

	public List<PwApplyRecord> findList(PwApplyRecord entity) {
		return super.findList(entity);
	}

	public Page<PwApplyRecord> findPage(Page<PwApplyRecord> page, PwApplyRecord entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(PwApplyRecord entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<PwApplyRecord> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<PwApplyRecord> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(PwApplyRecord entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(PwApplyRecord entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(PwApplyRecord entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(PwApplyRecord entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	@Transactional(readOnly = false)
	public void saveChangeRecord(PwEnter pwEnter) {
		PwApplyRecord pwApplyRecord=new PwApplyRecord();
		pwApplyRecord.setEid(pwEnter.getId());
		pwApplyRecord.setParentId(pwEnter.getParentId());
		String changeString="";
		if(pwEnter.isSpaceIsChange()){
			changeString=changeString+ PwEnterBgremarks.R5.getKey()+StringUtil.DOTH;
			pwApplyRecord.setTerm(pwEnter.getExpectTerm());
		}
		if(pwEnter.isCompanyIsChange()){
			changeString=changeString+ PwEnterBgremarks.R6.getKey()+StringUtil.DOTH;;
		}
		if(pwEnter.isProjectIsChange()){
			changeString=changeString+ PwEnterBgremarks.R7.getKey()+StringUtil.DOTH;;
		}
		if(pwEnter.isTeamIsChange()){
			changeString=changeString+ PwEnterBgremarks.R8.getKey()+StringUtil.DOTH;;
		}
		//去掉最后一位
		changeString=StringUtil.removeLastDotH(changeString);
		pwApplyRecord.setType(changeString);
		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
		pwApplyRecord.setDeclareId(UserUtils.getUser().getId());
		pwApplyRecord.setDeclareTime(new Date());
		save(pwApplyRecord);
	}

	@Transactional(readOnly = false)
	public void saveRecord(PwEnter pwEnter) {
		PwApplyRecord pwApplyRecord=new PwApplyRecord();
		pwApplyRecord.setEid(pwEnter.getId());
		if(PwEnterType.PET_TEAM.getKey().equals(pwEnter.getType())){
			pwApplyRecord.setType(PwEnterBgremarks.R1.getKey());
		}
		if(PwEnterType.PET_QY.getKey().equals(pwEnter.getType())){
			pwApplyRecord.setType(PwEnterBgremarks.R2.getKey());
		}
		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
		pwApplyRecord.setDeclareId(UserUtils.getUser().getId());
		pwApplyRecord.setDeclareTime(new Date());
		save(pwApplyRecord);
	}
	//获取申报信息
	public List<PwApplyRecord> getAppList(PwApplyRecord pwApplyRecord) {
//		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
		List<PwApplyRecord> recordList=dao.getListByPwApplyRecord(pwApplyRecord);
		setTypeStringList(recordList);
		return recordList;
	}

	//获取审核记录

	private void setTypeStringList(List<PwApplyRecord> recordList) {
		if(StringUtil.checkNotEmpty(recordList)){
			for(PwApplyRecord pwApplyRecordIndex:recordList){
				pwApplyRecordIndex.setTypeString(PwEnterBgremarks.getTypeStringByType(pwApplyRecordIndex.getType()));
			}
		}
	}

	public List<PwApplyRecord> getAuditList(PwApplyRecord pwApplyRecord) {
//		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
		List<PwApplyRecord> recordList=dao.getAuditList(pwApplyRecord);
		setTypeStringList(recordList);
		return recordList;
	}

    /**
     * 申请记录审核.
     * @param id 审核ID
     * @param type 审核类型
     * @param atype 审核结果类型
     * @param remarks 说明
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> auditRecord(String id, String type, String atype, String remarks) {
        if (StringUtil.isEmpty(id)) {
            return new ApiTstatus<PwEnter>(false, "入驻申请ID不能为空");
        }

        if (StringUtil.isEmpty(type)) {
            return new ApiTstatus<PwEnter>(false, "申请类型不能为空");
        }

        if (StringUtil.isEmpty(atype)) {
            return new ApiTstatus<PwEnter>(false, "申请审核结果类型不能为空");
        }

        if ((PassNot.NOT.getKey()).equals(atype) && StringUtil.isEmpty(remarks)) {
            return new ApiTstatus<PwEnter>(false, "入驻申请审核拒绝通过需要说明原因");
        }
        return saveAuditRecord(id, type, atype, remarks);
    }

    /**
     * @param id
     * @param type
     * @param atype
     * @param remarks
     * @return
     */
    private ApiTstatus<PwEnter> saveAuditRecord(String id, String type, String atype, String remarks) {
		if (StringUtil.isEmpty(id)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请ID不能为空");
		}
		if (StringUtil.isEmpty(type)) {
			return new ApiTstatus<PwEnter>(false, "申请类型不能为空");
		}

		if (StringUtil.isEmpty(atype)) {
			return new ApiTstatus<PwEnter>(false, "申请审核结果类型不能为空");
		}
		if ((PassNot.NOT.getKey()).equals(atype) && StringUtil.isEmpty(remarks)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请审核拒绝通过需要说明原因");
		}
		PwApplyRecord pwApplyRecord=getLastAuditByEid(id);
		//new PwApplyRecord();
		//pwApplyRecord.setEid(id);
		PwEnterBgremarks pwEnterBgremarks=PwEnterBgremarks.getByKey(type);
		pwApplyRecord.setType(pwEnterBgremarks.getKey());
		pwApplyRecord.setRemarks(remarks);
		pwApplyRecord.setStatus(PwEnterAuditEnum.getAuditValue(atype));
		pwApplyRecord.setAuditId(UserUtils.getUser().getId());
		pwApplyRecord.setAuditTime(new Date());
		save(pwApplyRecord);
		User apply_User = UserUtils.getUser();
		User rec_User = new User();
		rec_User.setId(pwApplyRecord.getDeclareId());
		PwAppointmentStatus status = (PassNot.PASS.getKey()).equals(atype) ? PwAppointmentStatus.PASS : PwAppointmentStatus.REJECT;

		oaNotifyService.sendOaNotifyByType(apply_User, rec_User,  OaNotify.Type_Enum.TYPE29.getName(),
				"你的申请" + status.getName()
				, OaNotify.Type_Enum.TYPE29.getValue(), id);
		if((ActYwApplyVo.GRADE_PASS).equals(atype)){
            PwEnter pwEnter = pwEnterService.get(id);
		    if((PwEnterBgremarks.R3.getKey()).equals(type)){
                pwEnter.setExpectTerm(pwApplyRecord.getTerm());
                pwEnterService.save(pwEnter);
		        pwEnterService.enterByXq(pwApplyRecord.getEid(), pwApplyRecord.getTerm(), false);
            }else if((PwEnterBgremarks.R4.getKey()).equals(type)){
                pwEnterService.enterByExit(pwApplyRecord.getEid(), false);
            }else{
                logger.info("匹配类型未定义");
            }
		}
		return new ApiTstatus<PwEnter>(true, "审核完成", null);
    }

	public PwApplyRecord getLastAuditByEid(String eid) {
		return dao.getLastAuditByEid(eid);
	}

	public PwApplyRecord getLastByEid(String eid) {
		return dao.getLastByEid(eid);
	}

	public Long findCountByType(String type) {
	    Long count = dao.findCountByType(type);
	    return (count == null) ? 0 : count;
	}

	@Transactional(readOnly = false)
	public void saveAdminChangeRecord(PwEnter pwEnter) {
		PwApplyRecord pwApplyRecord=new PwApplyRecord();
		pwApplyRecord.setEid(pwEnter.getId());
		PwEnterBgremarks pwEnterBgremarks=PwEnterBgremarks.getByKey(PwEnterBgremarks.R10.getKey());
		pwApplyRecord.setType(pwEnterBgremarks.getKey());
		//变更说明
		String changeString="";
		if(pwEnter.isSpaceIsChange()){
			changeString=changeString+ PwEnterBgremarks.R5.getName()+StringUtil.DOTH;
		}
		if(pwEnter.isCompanyIsChange()){
			changeString=changeString+ PwEnterBgremarks.R6.getName()+StringUtil.DOTH;;
		}
		if(pwEnter.isProjectIsChange()){
			changeString=changeString+ PwEnterBgremarks.R7.getName()+StringUtil.DOTH;;
		}
		if(pwEnter.isTeamIsChange()){
			changeString=changeString+ PwEnterBgremarks.R8.getName()+StringUtil.DOTH;;
		}
		//去掉最后一位
		changeString=StringUtil.removeLastDotH(changeString);
		pwApplyRecord.setBgremarks(changeString);
		pwApplyRecord.setAuditId(UserUtils.getUser().getId());
		pwApplyRecord.setAuditTime(new Date());
//		pwApplyRecord.setStatus(atype);
		save(pwApplyRecord);
	}

	/**
     * 根据入驻ID更新记录为失败状态 2.
     * @param id
     */
    @Transactional(readOnly = false)
    public void updateFailByEid(String id) {
        dao.updateFailByEid(id);
    }

	public List<PwApplyRecord> getFrontAuditList(PwApplyRecord pwApplyRecord) {
	//		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
		List<PwApplyRecord> recordList=dao.getFrontAuditList(pwApplyRecord);
		setTypeStringList(recordList);
		return recordList;
	}

	@Transactional(readOnly = false)
	public PwApplyRecord saveFailRecored(PwEnter pwEnter, String type) {
		PwApplyRecord pwApplyRecord=new PwApplyRecord();
		pwApplyRecord.setType(type);
		pwApplyRecord.setEid(pwEnter.getId());
		pwApplyRecord.setAuditId(UserUtils.getUser().getId());
		pwApplyRecord.setAuditTime(new Date());
		save(pwApplyRecord);
		return pwApplyRecord;
	}

	public List<PwApplyRecord> getBackAuditList(PwApplyRecord pwApplyRecord) {
		List<PwApplyRecord> recordList=dao.getBackAuditList(pwApplyRecord);
		setTypeStringList(recordList);
		return recordList;
	}

	public PwApplyRecord getChangeAppByEid(String eid) {
						return dao.getChangeAppByEid(eid);
	}
}