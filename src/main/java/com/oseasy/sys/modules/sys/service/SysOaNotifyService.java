/**
 *
 */
package com.oseasy.sys.modules.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyRecordDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifySent;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotype;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.websocket.WebSockectUtil;
import com.oseasy.com.pcore.common.websocket.WsMsg;
import com.oseasy.com.pcore.common.websocket.WsMsgBtn;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.TeamConf;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通知通告Service

 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class SysOaNotifyService extends OaNotifyService {
	@Autowired
	private OaNotifyRecordDao oaNotifyRecordDao;
	@Autowired
	private UserService userService;
    @Autowired
    private TeamDao teamDao;

	@Transactional(readOnly = false)
	public void save(OaNotify oaNotify,List<String> userList) {
	    if(StringUtil.isEmpty(oaNotify.getOtype())){
	        oaNotify.setOtype(OaNotype.OA_NOMALL.getKey());
	    }
		super.save(oaNotify);
		// 更新发送接受人记录
		oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
		if (oaNotify.getOaNotifyRecordList().size() > 0) {
			oaNotifyRecordDao.insertAll(oaNotify.getOaNotifyRecordList());
		}
		TeamConf te=SysConfigUtil.getSysConfigVo().getTeamConf();
		/*向浏览器发送消息******************/
		WsMsg wsMsg=new WsMsg();
		wsMsg.setContent(oaNotify.getContent());
		wsMsg.setNotifyId(oaNotify.getId());
		wsMsg.setTeamId(oaNotify.getsId());
		if (OaNotify.Type_Enum.TYPE13.getValue().equals(oaNotify.getType())
				||OaNotify.Type_Enum.TYPE10.getValue().equals(oaNotify.getType())
				||OaNotify.Type_Enum.TYPE11.getValue().equals(oaNotify.getType())
				||OaNotify.Type_Enum.TYPE14.getValue().equals(oaNotify.getType())) {
		}else if (OaNotify.Type_Enum.TYPE6.getValue().equals(oaNotify.getType())) {
			if(te!=null && Const.YES.equals(te.getInvitationOnOff())){
				WsMsgBtn b1=new WsMsgBtn();
				b1.setName("接受");
				b1.setClick("notifyModule.acceptP(this,'"+oaNotify.getId()+"')");
				wsMsg.addBtn(b1);
				WsMsgBtn b2=new WsMsgBtn();
				b2.setName("拒绝");
				b2.setClick("notifyModule.refuseP(this,'"+oaNotify.getId()+"')");
				wsMsg.addBtn(b2);
			}

			WsMsgBtn b3=new WsMsgBtn();
			b3.setName("查看详情");
			b3.setClick("notifyModule.openViewP(this,'"+oaNotify.getsId()+"','"+oaNotify.getId()+"')");
			wsMsg.addBtn(b3);
		}else if (OaNotify.Type_Enum.TYPE7.getValue().equals(oaNotify.getType())) {
			WsMsgBtn b1=new WsMsgBtn();
			b1.setName("查看详情");
			b1.setClick("notifyModule.openViewP(this,'"+oaNotify.getsId()+"','"+oaNotify.getId()+"')");
			wsMsg.addBtn(b1);
		}else if (OaNotify.Type_Enum.TYPE5.getValue().equals(oaNotify.getType())) {
			if(te!=null && Const.YES.equals(te.getJoinOnOff())) {
				WsMsgBtn b1 = new WsMsgBtn();
				b1.setName("接受");
				b1.setClick("notifyModule.acceptP(this,'" + oaNotify.getId() + "')");
				wsMsg.addBtn(b1);
				WsMsgBtn b2 = new WsMsgBtn();
				b2.setName("拒绝");
				b2.setClick("notifyModule.refuseP(this,'" + oaNotify.getId() + "')");
				wsMsg.addBtn(b2);
			}
		}
		for(String onr:userList) {
			WebSockectUtil.pushToRedis(onr, wsMsg);
		}
		/*向浏览器发送消息******************/
	}

	@Transactional(readOnly = false)
	public void saveOffice(OaNotify oaNotify) {
        oaNotify.setOtype(OaNotype.OA_OFFICE.getKey());
        if(oaNotify.getEndDate() == null){
            oaNotify.setEndDate(DateUtil.addMonth(new Date(), 2));
        }
        super.save(oaNotify);
        // 更新发送接受人记录
        oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
        if (oaNotify.getOaNotifyRecordList().size() > 0) {
            oaNotifyRecordDao.insertAllOffice(oaNotify.getOaNotifyRecordList());
        }
        /*向浏览器发送消息******************/
        WsMsg wsMsg=new WsMsg();
        wsMsg.setContent(oaNotify.getContent());
        wsMsg.setNotifyId(oaNotify.getId());
        wsMsg.setTeamId(oaNotify.getsId());
		TeamConf te=SysConfigUtil.getSysConfigVo().getTeamConf();
        if (OaNotify.Type_Enum.TYPE13.getValue().equals(oaNotify.getType())
                ||OaNotify.Type_Enum.TYPE10.getValue().equals(oaNotify.getType())
                ||OaNotify.Type_Enum.TYPE11.getValue().equals(oaNotify.getType())
                ||OaNotify.Type_Enum.TYPE14.getValue().equals(oaNotify.getType())) {
        }else if (OaNotify.Type_Enum.TYPE6.getValue().equals(oaNotify.getType())) {
            WsMsgBtn b1=new WsMsgBtn();
            b1.setName("接受");
            b1.setClick("notifyModule.acceptP(this,'"+oaNotify.getId()+"')");
            wsMsg.addBtn(b1);
            WsMsgBtn b2=new WsMsgBtn();
            b2.setName("拒绝");
            b2.setClick("notifyModule.refuseP(this,'"+oaNotify.getId()+"')");
            wsMsg.addBtn(b2);
            WsMsgBtn b3=new WsMsgBtn();
            b3.setName("查看详情");
            b3.setClick("notifyModule.openViewP(this,'"+oaNotify.getsId()+"','"+oaNotify.getId()+"')");
            wsMsg.addBtn(b3);
        }else if (OaNotify.Type_Enum.TYPE7.getValue().equals(oaNotify.getType())) {
            WsMsgBtn b1=new WsMsgBtn();
            b1.setName("查看详情");
            b1.setClick("notifyModule.openViewP(this,'"+oaNotify.getsId()+"','"+oaNotify.getId()+"')");
            wsMsg.addBtn(b1);
        }else if (OaNotify.Type_Enum.TYPE5.getValue().equals(oaNotify.getType())) {
			if(te!=null && Const.YES.equals(te.getJoinOnOff())) {
				WsMsgBtn b1 = new WsMsgBtn();
				b1.setName("接受");
				b1.setClick("notifyModule.acceptP(this,'" + oaNotify.getId() + "')");
				wsMsg.addBtn(b1);
				WsMsgBtn b2 = new WsMsgBtn();
				b2.setName("拒绝");
				b2.setClick("notifyModule.refuseP(this,'" + oaNotify.getId() + "')");
				wsMsg.addBtn(b2);
			}
        }
        for(OaNotifyRecord onr:oaNotify.getOaNotifyRecordList()) {
            WebSockectUtil.pushToRedisByOffice(onr.getOffice().getId(), wsMsg);
        }
        /*向浏览器发送消息******************/
	}

	@Transactional(readOnly = false)
    public void saveCollege(OaNotify oaNotify) {
        super.save(oaNotify);
        // 更新发送接受人记录
        oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
        String officeIds=oaNotify.getOaNotifyRecordIds();
        List<OaNotifyRecord> oaNotifyRecordList = Lists.newArrayList();
        for (String id : StringUtil.split(officeIds, ",")) {
            List<User> list = userService.findListByRoleTypeAndOffice(id, RoleBizTypeEnum.XS.getValue());
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

	@Transactional(readOnly = false)
	public List<OaNotifySent> unRead(OaNotify oaNotify) {
		User acceptUser = UserUtils.getUser();
		if(acceptUser == null){
			return Lists.newArrayList();
		}
		List<OaNotifySent> list1 =null;
		try {

		oaNotify.setIsSelf(true);
		oaNotify.setReadFlag("0");
		oaNotify.setCurrentUser(acceptUser);
		List<OaNotify> list = new ArrayList<OaNotify>();
	    list1 = new ArrayList<OaNotifySent>();
		list = unReadOaNotifyList(oaNotify);
		for (OaNotify oaNotify2 : list) {
			OaNotifySent oaNotifySent = new OaNotifySent();
			String userId = oaNotify2.getCreateBy().getId();
			User sentUser = userService.findUserById(userId);
			TeamUserRelation teamUserRelation = new TeamUserRelation();
			teamUserRelation.setCreateBy(sentUser);
			teamUserRelation.setUser(acceptUser);
			Team team = teamDao.get(oaNotify2.getsId());
			if (team != null) {
				oaNotifySent.setTeamName(team.getName());
			}
			oaNotifySent.setType(oaNotify2.getType());
			oaNotifySent.setNotifyId(oaNotify2.getId());

			if ((sentUser != null) && StringUtil.isNotEmpty(sentUser.getName())) {
				oaNotifySent.setSentName(sentUser.getName());
			}
			oaNotifySent.setTeamId(oaNotify2.getsId());
			oaNotifySent.setContent(oaNotify2.getContent());
			list1.add(oaNotifySent);
		}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return list1;
	}
}