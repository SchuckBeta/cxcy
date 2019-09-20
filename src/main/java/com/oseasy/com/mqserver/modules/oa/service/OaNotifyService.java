/**
 *
 */
package com.oseasy.com.mqserver.modules.oa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyDao;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyKeywordDao;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyRecordDao;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyReoffilterDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyKeyword;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyReoffilter;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotype;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通知通告Service

 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OaNotifyService extends CrudService<OaNotifyDao, OaNotify> {
	@Autowired
	private OaNotifyKeywordDao oaNotifyKeywordDao;
	@Autowired
	private OaNotifyRecordDao oaNotifyRecordDao;
	@Autowired
	private OaNotifyReoffilterDao oaNotifyReoffilterDao;
	@Autowired
	private UserService userService;
	@Autowired
	private SysTenantService sysTenantService;
	@Autowired
	private CoreService coreService;

//	@Autowired
//	private TeamDao teamDao;
	@Autowired
	private SysAttachmentService sysAttachmentService;

	public OaNotify get(String id) {
		OaNotify entity = dao.get(id);
		return entity;
	}
	public Integer getUnreadCount(String uid) {
		return dao.getUnreadCount(uid);
	}
	public Integer getUnreadCountByUser(User user) {
	    if((user == null) || StringUtil.isEmpty(user.getId()) || StringUtil.isEmpty(user.getProfessional())){
	        return 0;
	    }
	    return dao.getUnreadCountByUser(user);
	}
	public List<Map<String,Object>> getMore(String type,String id,List<String> keys) {
		return dao.getMore(type,id,keys);
	}
	@Transactional(readOnly = false)
	/**
	 * 发送通告
	 * @param apply_User 发送人
	 * @param rec_User 接受人
	 * @param title 标题
	 * @param content 内容
	 * @param type 类型 OaNotify.Type_Enum
	 * @param sid 关联大赛或者项目的id
	 * @return
	 */
	public int sendOaNotifyByType(User apply_User,User rec_User,String title,String content, String type, String sid) {
		try {
			OaNotify oaNotify=new OaNotify();
			oaNotify.setTitle(title);
			oaNotify.setContent(content);
			oaNotify.setType(type);
			oaNotify.setsId(sid);
			oaNotify.setCreateBy(apply_User);
			oaNotify.setCreateDate(new Date());
			oaNotify.setUpdateBy(apply_User);
			oaNotify.setUpdateDate(new Date());
			oaNotify.setEffectiveDate(new Date());
			oaNotify.setStatus("1");
			oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());
			OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
			List<OaNotifyRecord> recList=new ArrayList<OaNotifyRecord>();
			oaNotifyRecord.setId(IdGen.uuid());
			oaNotifyRecord.setOaNotify(oaNotify);
			oaNotifyRecord.setUser(rec_User);
			oaNotifyRecord.setReadFlag("0");
			oaNotifyRecord.setOperateFlag("0");
			recList.add(oaNotifyRecord);
			oaNotify.setOaNotifyRecordList(recList);
			save(oaNotify);
			if (recList.size() > 0) {
				oaNotifyRecordDao.insertAll(recList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		return 1;
	}
	public int sendOaNotifyByTypes(User apply_User,User rec_User,String title,String content, String type, String sid) {
		try {
			OaNotify oaNotify=new OaNotify();
			oaNotify.setTitle(title);
			oaNotify.setContent(content);
			oaNotify.setOtype(Const.NO);
			oaNotify.setType(type);
			oaNotify.setsId(sid);
			oaNotify.setCreateBy(apply_User);
			oaNotify.setCreateDate(new Date());
			oaNotify.setUpdateBy(apply_User);
			oaNotify.setUpdateDate(new Date());
			oaNotify.setEffectiveDate(new Date());
			oaNotify.setStatus("1");
			oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());
			OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
			List<OaNotifyRecord> recList=new ArrayList<OaNotifyRecord>();
			oaNotifyRecord.setId(IdGen.uuid());
			oaNotifyRecord.setOaNotify(oaNotify);
			oaNotifyRecord.setUser(rec_User);
			oaNotifyRecord.setReadFlag("0");
			oaNotifyRecord.setOperateFlag("0");
			recList.add(oaNotifyRecord);
			oaNotify.setOaNotifyRecordList(recList);
			save(oaNotify);
			if (recList.size() > 0) {
				oaNotifyRecordDao.insertAll(recList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		return 1;
	}
	/**
	 * 发送通告
	 * @param apply_User 发送人

	 * @param title 标题
	 * @param content 内容
	 * @param type 类型 OaNotify.Type_Enum
	 * @param sid 关联大赛或者项目的id
	 * @return
	 */
    @Transactional(readOnly = false)
	public int sendOaNotifyByTypeAndUser(User apply_User,List<String> userList,String title,String content, String type, String sid) {
		try {
			OaNotify oaNotify=new OaNotify();
			oaNotify.setTitle(title);
			oaNotify.setContent(content);
			oaNotify.setType(type);
			oaNotify.setsId(sid);
			oaNotify.setCreateBy(apply_User);
			oaNotify.setCreateDate(new Date());
			oaNotify.setUpdateBy(apply_User);
			oaNotify.setUpdateDate(new Date());
			oaNotify.setEffectiveDate(new Date());
			oaNotify.setStatus("1");
			oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());
			List<OaNotifyRecord> recList=new ArrayList<OaNotifyRecord>();
			for(String userId:userList){
				OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
				oaNotifyRecord.setId(IdGen.uuid());
				oaNotifyRecord.setOaNotify(oaNotify);
				User rec_User = UserUtils.get(userId);
				oaNotifyRecord.setUser(rec_User);
				oaNotifyRecord.setReadFlag("0");
				oaNotifyRecord.setOperateFlag("0");
				recList.add(oaNotifyRecord);
			}
			oaNotify.setOaNotifyRecordList(recList);
			this.save(oaNotify);
			if (recList.size() > 0) {
				oaNotifyRecordDao.insertAll(recList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;

		}
		return 1;
	}


	/**
	 * 获取通知发送记录
	 * @param oaNotify
	 * @return
	 */
	public OaNotify getRecordList(OaNotify oaNotify) {
		oaNotify.setOaNotifyRecordList(oaNotifyRecordDao.findList(new OaNotifyRecord(oaNotify)));
		return oaNotify;
	}

	/**
	 * 检查是否为机构通知.
	 * @param oaNotify
	 * @return
	 */
	public Boolean checkIsOfficeNotify(OaNotify oaNotify) {
        Boolean isOffice = false;
        if(StringUtil.checkNotEmpty(oaNotify.getOaNotifyRecordList())){
            for (OaNotifyRecord record : oaNotify.getOaNotifyRecordList()) {
                if((record.getUser() == null) && (record.getOffice() != null)){
                    isOffice = true;
                    break;
                }
            }
        }
        return isOffice;
    }

	public OaNotify getRecordListByUser(OaNotify oaNotify) {
	    OaNotifyRecord onRecord = new OaNotifyRecord(oaNotify);
	    onRecord.setUser(UserUtils.getUser());
	    oaNotify.setOaNotifyRecordList(oaNotifyRecordDao.findList(onRecord));
	    return oaNotify;
	}
	public String getReadFlag(String oid,User user) {
		return oaNotifyRecordDao.getReadFlag(oid, user);
	}
	public Page<OaNotify> find(Page<OaNotify> page, OaNotify oaNotify) {
		oaNotify.setPage(page);
		page.setList(dao.findList(oaNotify));
		return page;
	}

	public Page<OaNotify> findAllRecord(Page<OaNotify> page, OaNotify oaNotify) {
	    if((oaNotify == null) || (oaNotify.getCurrentUser() == null) || (StringUtil.isEmpty(oaNotify.getCurrentUser().getId()) && StringUtil.isEmpty(oaNotify.getCurrentUser().getProfessional()))){
	        return page;
	    }
	    oaNotify.setPage(page);
		if(oaNotify.getCurrentUser().getProfessional() == null){
			oaNotify.getCurrentUser().setProfessional("");
		}
		if(oaNotify.getCurrentUser()!=null && StringUtil.isNotEmpty(oaNotify.getCurrentUser().getId())){
			page.setList(dao.findAllRecord(oaNotify));
		}else{
			page.setList(null);
		}
//	    page.setList(dao.findAllRecord(oaNotify));
	    return page;
	}

	public Page<OaNotify> findSend(Page<OaNotify> page, OaNotify oaNotify) {
		oaNotify.setPage(page);
		page.setList(dao.findSendList(oaNotify));
		return page;
	}

	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify) {
		return dao.findCount(oaNotify);
	}

	public OaNotifyRecord getTeamOaNotify(OaNotify oaNotify) {
		OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
		OaNotifyRecord tempOaNotifyRecord = oaNotifyRecordDao.getTeamOaNotify(oaNotifyRecord);
		return tempOaNotifyRecord;
	}

	/**
	 * 更新阅读状态
	 */
	@Transactional(readOnly = false)
	public void updateReadFlag(OaNotify oaNotify) {
		OaNotifyRecord tempOaNotifyRecord = null;
		if(StringUtil.isEmpty(oaNotify.getOtype())){
	        oaNotify = get(oaNotify);
	    }
	    if((OaNotype.OA_NOMALL.getKey()).equals(oaNotify.getOtype()) || null == oaNotify.getOtype() || StringUtil.isEmpty(oaNotify.getOtype())){
	        OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
			if(oaNotify.getType().equals("7")){
				tempOaNotifyRecord= oaNotifyRecordDao.getTeamOaNotify(oaNotifyRecord);
			}
	        oaNotifyRecord.setUser(oaNotify.getCurrentUser());
	        oaNotifyRecord.setReadDate(new Date());
	        oaNotifyRecord.setReadFlag("1");
			OaNotifyRecord temp = oaNotifyRecordDao.getMine(oaNotifyRecord);
	        if(temp != null){
				oaNotifyRecordDao.updateReadFlag(oaNotifyRecord);
			}else{
				if(tempOaNotifyRecord != null){
					oaNotifyRecord.setId(IdGen.uuid());
						oaNotifyRecord.setDelFlag(tempOaNotifyRecord.getDelFlag());
					oaNotifyRecordDao.insert(oaNotifyRecord);
				}
			}
	    }else if((OaNotype.OA_OFFICE.getKey()).equals(oaNotify.getOtype())){
	        OaNotifyReoffilter poaNotifyReoffilter = new OaNotifyReoffilter(oaNotify);
	        poaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
	        OaNotifyReoffilter oaNotifyReoffilter = oaNotifyReoffilterDao.getByNidAndUser(poaNotifyReoffilter);
	        if(oaNotifyReoffilter == null){
	            oaNotifyReoffilter = new OaNotifyReoffilter(oaNotify);
	            oaNotifyReoffilter.setId(IdGen.uuid());
	            oaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
	            oaNotifyReoffilter.setReadDate(new Date());
	            oaNotifyReoffilter.setReadFlag("1");
	            oaNotifyReoffilter.setOperateFlag("0");
	            oaNotifyReoffilterDao.insert(oaNotifyReoffilter);
	        }else{
	            oaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
	            oaNotifyReoffilter.setReadDate(new Date());
	            oaNotifyReoffilter.setReadFlag("1");
	            oaNotifyReoffilterDao.updateReadFlag(oaNotifyReoffilter);
	        }
	    }
	}
	/**
	 * 更新操作状态
	 */
	@Transactional(readOnly = false)
	public void updateOperateFlag(OaNotify oaNotify) {
	    if(StringUtil.isEmpty(oaNotify.getOtype())){
            oaNotify = get(oaNotify);
        }
        if((OaNotype.OA_NOMALL.getKey()).equals(oaNotify.getOtype())){
            OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
            oaNotifyRecord.setUser(oaNotifyRecord.getCurrentUser());
            oaNotifyRecord.setOffice(new Office(oaNotifyRecord.getCurrentUser().getProfessional()));
            oaNotifyRecord.setOperateFlag("1");
            oaNotifyRecordDao.updateOperateFlag(oaNotifyRecord);
        }else if((OaNotype.OA_OFFICE.getKey()).equals(oaNotify.getOtype())){
            OaNotifyReoffilter poaNotifyReoffilter = new OaNotifyReoffilter(oaNotify);
            poaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
            OaNotifyReoffilter oaNotifyReoffilter = oaNotifyReoffilterDao.getByNidAndUser(poaNotifyReoffilter);
            if(oaNotifyReoffilter == null){
                oaNotifyReoffilter = new OaNotifyReoffilter(oaNotify);
                oaNotifyReoffilter.setId(IdGen.uuid());
                oaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
                oaNotifyReoffilter.setReadDate(new Date());
                oaNotifyReoffilter.setReadFlag("1");
                oaNotifyReoffilter.setOperateFlag("1");
                oaNotifyReoffilterDao.insert(oaNotifyReoffilter);
            }else{
                oaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
                oaNotifyReoffilter.setOperateFlag("1");
                oaNotifyReoffilterDao.updateOperateFlag(oaNotifyReoffilter);
            }
        }
	}
	/**
	 * 更新阅读、操作状态
	 */
	@Transactional(readOnly = false)
	public void updateReadOperateFlag(OaNotify oaNotify) {
	    if(StringUtil.isEmpty(oaNotify.getOtype())){
            oaNotify = get(oaNotify);
        }
        if((OaNotype.OA_NOMALL.getKey()).equals(oaNotify.getOtype())){
            OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
            oaNotifyRecord.setUser(oaNotifyRecord.getCurrentUser());
            oaNotifyRecord.setReadDate(new Date());
            oaNotifyRecord.setReadFlag("1");
            oaNotifyRecord.setOperateFlag("1");
            oaNotifyRecordDao.updateReadOperateFlag(oaNotifyRecord);
        }else if((OaNotype.OA_OFFICE.getKey()).equals(oaNotify.getOtype())){
            OaNotifyReoffilter poaNotifyReoffilter = new OaNotifyReoffilter(oaNotify);
            poaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
            OaNotifyReoffilter oaNotifyReoffilter = oaNotifyReoffilterDao.getByNidAndUser(poaNotifyReoffilter);
            if(oaNotifyReoffilter == null){
                oaNotifyReoffilter = new OaNotifyReoffilter(oaNotify);
                oaNotifyReoffilter.setId(IdGen.uuid());
                oaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
                oaNotifyReoffilter.setReadDate(new Date());
                oaNotifyReoffilter.setReadFlag("1");
                oaNotifyReoffilter.setOperateFlag("1");
                oaNotifyReoffilterDao.insert(oaNotifyReoffilter);
            }else{
                oaNotifyReoffilter.setUser(oaNotify.getCurrentUser());
                oaNotifyReoffilter.setReadDate(new Date());
                oaNotifyReoffilter.setReadFlag("1");
                oaNotifyReoffilter.setOperateFlag("1");
                oaNotifyReoffilterDao.updateReadOperateFlag(oaNotifyReoffilter);
            }
        }
	}

	/**
	 * 保存广播通知
	 */
	@Transactional(readOnly = false)
	public void saveBroadcast(OaNotify oaNotify) {
		super.save(oaNotify);
		// 更新发送接受人记录
		if ("2".equals(oaNotify.getSendType())) {
			oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
			if (oaNotify.getOaNotifyRecordList().size() > 0) {
				oaNotifyRecordDao.insertAll(oaNotify.getOaNotifyRecordList());
			}
		}
	}
	private boolean saveoaNotifyContent(OaNotify es) {
		Map<String,String> map=sysAttachmentService.moveAndSaveTempFile(es.getContent(), es.getId(), FileTypeEnum.S8, FileStepEnum.S801);
		if ("1".equals(map.get("ret"))) {
			es.setContent(map.get("content"));
			return true;
		}
		//未检测附件
		return true;
	}
	@Transactional(readOnly = false)
	public void saveCollegeBroadcast(OaNotify oaNotify) {

			if (oaNotify.getViews()==null) {
				oaNotify.setViews("0");
			}
			if (StringUtil.isNotEmpty(oaNotify.getContent())) {
				oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
			}
		if(StringUtil.isNotEmpty(oaNotify.getSchOorPro()) && oaNotify.getSchOorPro().equals("1")){
			//如果选择了校平台发布消息
			SysTenant sysTenant = new SysTenant();
			List<String> lists = CoreIds.filterTenantByNsAdmin();
			sysTenant.setFilterIds(lists);
			sysTenant.setStatus("1");
			List<SysTenant> sysTenantList = sysTenantService.findList(sysTenant);
			for(SysTenant tenant : sysTenantList){
				oaNotify.setTenantId(tenant.getTenantId());
				oaNotify.setIsNewRecord(true);
				oaNotify.setId(IdGen.uuid());
				saveOaNotifyNew(oaNotify);
			}

		}else{
			saveOaNotifyNew(oaNotify);
		}

	}

	private void saveOaNotifyNew(OaNotify oaNotify) {
		super.save(oaNotify);
		//处理内容里的临时url---start(需要在entity保存之后，需要id)
		//反转义
		oaNotify.setContent(StringEscapeUtils.unescapeHtml4(oaNotify.getContent()));
		//处理之前替换回占位字符串
		oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		boolean ret=saveoaNotifyContent(oaNotify);
		if (ret) {
            oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_HTTPURL, FtpUtil.FTP_MARKER));
            //转义
            oaNotify.setContent(StringEscapeUtils.escapeHtml4(oaNotify.getContent()));
            dao.update(oaNotify);//更新
        }
		//处理内容里的临时url---end
		//处理关键字
		if ("4".equals(oaNotify.getType())||"8".equals(oaNotify.getType())||"9".equals(oaNotify.getType())) {
            if (StringUtil.isNotEmpty(oaNotify.getId())) {
                oaNotifyKeywordDao.delByEsid(oaNotify.getId());
            }
            if (oaNotify.getKeywords()!=null) {
                for(String ek:oaNotify.getKeywords()) {
                    OaNotifyKeyword ekk=new OaNotifyKeyword();
                    ekk.setKeyword(ek);
                    ekk.setNotifyId(oaNotify.getId());
                    ekk.preInsert();
                    oaNotifyKeywordDao.insert(ekk);
                }
            }
        }
		// 更新发送接收人记录
		if ("2".equals(oaNotify.getSendType())) {
            // 更新发送接受人记录
            oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
            String officeIds=oaNotify.getOaNotifyRecordIds();
            List<OaNotifyRecord> oaNotifyRecordList = Lists.newArrayList();
            for (String id : StringUtil.split(officeIds, ",")) {
                List<User> list = coreService.findUserByOfficeId(id);
                for(User user:list) {
                    OaNotifyRecord entity = new OaNotifyRecord();
                    entity.setId(IdGen.uuid());
                    entity.setOaNotify(oaNotify);
                    entity.setUser(user);
                    entity.setReadFlag("0");
                    oaNotifyRecordList.add(entity);
                }
            }
            if (oaNotifyRecordList.size() > 0) {
                oaNotifyRecordDao.insertAll(oaNotifyRecordList);
            }
        }
	}

	public Page<OaNotify> findLoginPage(Page<OaNotify> page, OaNotify oaNotify) {
		oaNotify.setPage(page);
		page.setList(dao.findLoginList(oaNotify));
		return page;
	}

	@Transactional(readOnly = true)
	public List<OaNotify> loginList(Integer number) {
		return dao.loginList(number);
	}

	public OaNotifyRecord getMine(OaNotifyRecord oaNotifyRecord) {
		return oaNotifyRecordDao.getMine(oaNotifyRecord);

	}

	public List<OaNotify> unReadOaNotifyList(OaNotify oaNotify) {
		if(oaNotify.getCurrentUser()!=null && StringUtil.isNotEmpty(oaNotify.getCurrentUser().getId())){
			return dao.unReadOaNotifyList(oaNotify);
		}else{
			return null;
		}
	}

	/** 检验是否邀请过
	 * @param userId
	 * @param teamId
	 * @return
	 */
	public Integer findNotifyCount(String userId,String teamId) {
		return dao.findNotifyCount( userId,teamId);
	}

	public OaNotify findOaNotifyByTeamID(String userId,String sId) {
		return dao.findOaNotifyByTeamID(userId, sId);

	}

	@Transactional(readOnly = false)
	public void deleteRec(OaNotify oaNotify) {
		User currUser = UserUtils.getUser();
		OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
		oaNotifyRecord.setOaNotify(oaNotify);
		oaNotifyRecord.setUser(currUser);
		OaNotifyRecord temp =oaNotifyRecordDao.getMine(oaNotifyRecord);
		if(null != temp){
			//oaNotifyRecord =oaNotifyRecordDao.getMine(oaNotifyRecord);
			oaNotifyRecordDao.delete(temp);
		}else{
			oaNotifyRecord.setId(IdGen.uuid());
			oaNotifyRecord.setDelFlag(Const.YES);
			oaNotifyRecord.setReadFlag("0");
			oaNotifyRecordDao.insert(oaNotifyRecord);
		}

	}
	@Transactional(readOnly = false)
	public void deleteSend(OaNotify oaNotify) {
		OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
		oaNotifyRecord.setOaNotify(oaNotify);
		User recUser = userService.findUserById(oaNotify.getUserId());
		oaNotifyRecord.setUser(recUser);
		if(oaNotify.getType().equals("7") && oaNotify.getIsReCall().equals(Const.YES)){
			oaNotifyRecordDao.deleteAllTeamOaNotify(oaNotify.getId());
		}else if(oaNotify.getType().equals("7") && oaNotify.getIsReCall().equals(Const.NO)){
			oaNotifyRecord = oaNotifyRecordDao.getTeamOaNotify(oaNotifyRecord);
			oaNotifyRecordDao.deleteTeamOaNotify(oaNotifyRecord);
		}else{
			oaNotifyRecord=oaNotifyRecordDao.getMine(oaNotifyRecord);
			oaNotifyRecordDao.delete(oaNotifyRecord);
		}
	}
	@Transactional(readOnly = false)
	public void deleteTeamOaNotify(OaNotify oaNotify) {
		OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
		oaNotifyRecord.setOaNotify(oaNotify);
		oaNotifyRecord = oaNotifyRecordDao.getTeamOaNotify(oaNotifyRecord);
		oaNotifyRecordDao.deleteTeamOaNotify(oaNotifyRecord);
	}

}