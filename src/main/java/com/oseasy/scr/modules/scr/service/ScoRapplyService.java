package com.oseasy.scr.modules.scr.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ProcessDefUtils;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.apply.IAservice;
import com.oseasy.act.modules.actyw.tool.apply.IAstatus;
import com.oseasy.act.modules.actyw.tool.apply.IAsup;
import com.oseasy.act.modules.actyw.tool.apply.IConfig;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.act.modules.actyw.tool.apply.IGroup;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeTaskType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.act.modules.actyw.vo.ActYwRuntimeException;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.modules.scr.dao.ScoRapplyDao;
import com.oseasy.scr.modules.scr.dao.ScoRsumDao;
import com.oseasy.scr.modules.scr.dao.ScoRuleDao;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyMember;
import com.oseasy.scr.modules.scr.entity.ScoRapplyPb;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.scr.modules.scr.entity.ScoRapplySum;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;
import com.oseasy.scr.modules.scr.entity.ScoRset;
import com.oseasy.scr.modules.scr.entity.ScoRsum;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.tool.apply.ScrAengine;
import com.oseasy.scr.modules.scr.vo.ScoAuditVo;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;
import com.oseasy.scr.modules.scr.vo.ScoRuleType;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.Encodes;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分申请Service.
 * @author chenh
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class ScoRapplyService extends CrudService<ScoRapplyDao, ScoRapply> implements IAservice<ScrAengine>{
	@Autowired
	private SysAttachmentService sysAttachmentService;
	@Autowired
	private ScoRuleService scoRuleService;
    @Autowired
    private ScoRsumService scoRsumService;
	@Autowired
	private ScoRapplyPbService scoRapplyPbService;
	@Autowired
	private ScoRapplyRecordService scoRapplyRecordService;
	@Autowired
	private ScoRapplyMemberService scoRapplyMemberService;
    @Autowired
    private ScoRsetService scoRsetService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    IdentityService identityService;
    @Autowired
    TaskService taskService;
    @Autowired
    ActTaskService actTaskService;
//    @Autowired
//    ProActService proActService;
	@Autowired
	ScrProModelService scrProModelService;
    @Autowired
    private ScoRuleDao scoRuleDao;
    @Autowired
    private ScoRsumDao scoRsumDao;
    @Autowired
    SystemService systemService;
    @Autowired
    UserService userService;
    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    private ScoRuleDetailService scoRuleDetailService;

    public ScoRapply get(String id) {
	    return formatNum(super.get(id));
	}

    /**
     * 获取最高分的学分申请对象.
     * 1、需要返回需要最终比较的申请对象.
     * 2、需要将当前AppId相关的汇总学分记录填充到rscoRsums列表中.
     * @param rscoRsums 相关联的学分汇总信息
     * @param scoaVo 前台请求参数
     * @return ScoRapply
     */
	@Transactional
	public ScoRapply getGtRapply(ScoRapply rapply, List<ScoRsum> rscoRsums, ScoAuditVo scoaVo) {
	    ScoRapply pscoRapply = new ScoRapply();
	    pscoRapply.setAppIds(scoaVo.getAppId());
	    List<ScoRapply> scoRapplys = findListByg(pscoRapply);

	    /**
	     * 查找当前申请的汇总数据.
	     */
        ScoRapply curRapply = get(scoaVo.getAppId());
        scoRapplys.add(curRapply);
	    if(StringUtil.checkTwo(scoRapplys)){
	        ScoRsum pcscoRsum = new ScoRsum();
	        pcscoRsum.getAppIds().add(scoaVo.getAppId());
	        List<ScoRsum> cscoRsumss = scoRsumService.findListByAppIds(pcscoRsum);
	        if(StringUtil.checkNotEmpty(cscoRsumss)){
	            rscoRsums.addAll(cscoRsumss);
	        }

	        curRapply.setIsFirst(Const.YES);
            return curRapply;
        }


	    ScoRsum pscoRsum = new ScoRsum();
	    pscoRsum.getAppIds().add(scoaVo.getAppId());
	    pscoRsum.setAppIds(StringUtil.sqlInByListIdss(scoRapplys));
	    List<ScoRsum> scoRsumss = scoRsumService.findListByAppIds(pscoRsum);
	    for (ScoRapply crapply : scoRapplys) {
            if(curRapply == null){
                curRapply = crapply;
                continue;
            }

            /**
             * 排除掉当前申请记录.
             */
            if((rapply.getId()).equals(crapply.getId())){
                continue;
            }

            if(curRapply.getTcval() < crapply.getTcval()){
                curRapply = crapply;
            }
        }
	    curRapply.setIsFirst(Const.NO);

        if((curRapply != null) && StringUtil.checkNotEmpty(scoRsumss)){
            rscoRsums.addAll(scoRsumss);
        }
        return curRapply;
    }

	public ApiResult findRsumByUid(ScoRapply scoRapply){
        ScoRsum entity = new ScoRsum();
        entity.setRdetail(scoRapply.getRdetail());
        ScoRapplyMember scoRapplyMember = new ScoRapplyMember();
        scoRapply = get(scoRapply.getId());
        scoRapplyMember.setApply(scoRapply);
        //找寻申请中的成员列表
        List<ScoRapplyMember> scoRapplyMemberList = scoRapplyMemberService.findList(scoRapplyMember);
        List<ScoRapplySum> scoRapplySumList = Lists.newArrayList();


        ScoRule scoRule = new ScoRule();
        scoRule.setType("2");
        List<ScoRule> list = Lists.newArrayList();
        ScoRule r = new ScoRule();
        r.setName("创新创业课程");
        list.add(r);
        list.addAll(scoRuleDao.scoRuleSingleList(scoRule));
        entity.setEntitys(list);

        if(scoRapplyMemberList != null && scoRapplyMemberList.size() > 0){
            for(ScoRapplyMember srm : scoRapplyMemberList){
                ScoRapplySum scoRapplySum = new ScoRapplySum();
                entity.setUser(srm.getUser());
                scoRapplySum.setUserId(srm.getUser().getId());
                List<Map<String,Double>> map = scoRsumDao.findScoSumList(entity);
                if(map != null && map.size() >0){
                    scoRapplySum.setSumVal(map.get(0).get("总分"));
                }else{
                    scoRapplySum.setSumVal(0);
                }
                entity.setApply(srm.getApply());
                ScoRsum temp= scoRsumDao.findRdetailSum(entity);
                if(temp != null){
                    scoRapplySum.setrDetailVal(temp.getVal() == 0.0 ? 0:temp.getVal());
                }else{
                    scoRapplySum.setrDetailVal(0);
                }
                scoRapplySumList.add(scoRapplySum);
            }
        }else{
            ScoRapplySum scoRapplySum = new ScoRapplySum();
            scoRapplySum.setUserId(scoRapply.getUser().getId());
            ScoRsum temp= scoRsumDao.findRdetailSum(entity);
            if(temp != null){
                scoRapplySum.setrDetailVal(temp.getVal() == 0.0 ? 0:temp.getVal());
            }else{
                scoRapplySum.setrDetailVal(0);
            }
            List<Map<String,Double>> map = scoRsumDao.findScoSumList(entity);
            if(map != null && map.size() >0){
                scoRapplySum.setSumVal(map.get(0).get("总分"));
            }else{
                scoRapplySum.setSumVal(0);
            }
            scoRapplySumList.add(scoRapplySum);
        }
        return ApiResult.success(scoRapplySumList);
    }
    @Transactional(readOnly = false)
    public ApiResult saveFronSaveScoRapplyForm(ScoRapply scoRapply, HttpServletRequest request){
        try{
            scoRapply.setIsTemp(Const.YES);
            scoRapply.setTop(new ScoRule(CoreSval.getScoreSkillId()));
            scoRapply.setStatus(ScoRstatus.SRS_DSH.getKey());
            scoRapply.setApplyDate(new Date());
            scoRapply.setAutBy(UserUtils.getUser());
            String id = scoRapply.getId();
            if(StringUtil.isEmpty(id)){
                id = IdGen.uuid();
                scoRapply.setId(id);
                scoRapply.setIsNewRecord(true);
            }
            scoRapply=start(scoRapply, request);
            if(!scoRapply.iaprop().istart()){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,"流程ID或类型参数不能为空！");
            }
            save(scoRapply);
            //其他类别学分，若为团队保存组成员信息
            if( !scoRapply.getRule().getId().equals(CoreSval.getScoreSkillId()) && scoRapply.getScoRapplyMemberList() != null
                    && scoRapply.getScoRapplyMemberList().size() > 0 ){
                //删除之前团队
                ScoRapplyMember temp = new ScoRapplyMember();
                temp.setApply(scoRapply);
                scoRapplyMemberService.deleteRapplyMembers(temp);
                for(ScoRapplyMember scoRapplyMember : scoRapply.getScoRapplyMemberList()){
                    scoRapplyMember.setApply(scoRapply);
                    scoRapplyMemberService.save(scoRapplyMember);
                }
            }
            //保存附件
            saveCreditResultApply(scoRapply);
            //添加记录
            ScoRapplyRecord scr = new ScoRapplyRecord(scoRapply, scoRapply.getUser(),scoRapply.getAutBy(),ScoRstatus.SRS_DSH.getKey());
            scoRapplyRecordService.save(scr);
            return ApiResult.success();
        } catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }


	public ScoRapply ajaxFindScoDetail(ScoRapply entity) {
		ScoRapply scoRapply = dao.ajaxFindScoDetail(entity);
		formatNum(scoRapply);
		//获取附件
		SysAttachment sysAttachment=new SysAttachment();
		sysAttachment.setUid(entity.getId());
		List<SysAttachment> sysAttachmentList=sysAttachmentService.getFiles(sysAttachment);
		scoRapply.setSysAttachmentList(sysAttachmentList);
		//获取组成员
		ScoRapplyMember scoRapplyMember = new ScoRapplyMember();
		scoRapplyMember.setApply(new ScoRapply(entity.getId()));
		List<ScoRapplyMember> scoRapplyMemberList = scoRapplyMemberService.findScoMemberList(scoRapplyMember);
		scoRapply.setScoRapplyMemberList(scoRapplyMemberList);
		return scoRapply;
	}

	public ScoRapply formatNum(ScoRapply scoRapply) {
	   return formatNum(scoRapply, getKpoint());
	}
    public ScoRapply formatNum(ScoRapply scoRapply, Integer kpoint) {
        if((scoRapply != null) && (scoRapply.getTcval() != null)){
            if(kpoint == null){
                return scoRapply;
            }
            BigDecimal bd = new BigDecimal(scoRapply.getTcval());
            scoRapply.setTcval(bd.setScale(kpoint, BigDecimal.ROUND_DOWN).doubleValue());
        }
        return scoRapply;
    }

	public ScoRapply ajaxFindCourseDetail(ScoRapply entity) {
		ScoRapply scoRapply = dao.ajaxFindCourseDetail(entity);
		//查找附件
		SysAttachment sysAttachment = new SysAttachment();
		sysAttachment.setUid(entity.getId());
		List<SysAttachment> attachmentList = sysAttachmentService.findList(sysAttachment);
		scoRapply.setSysAttachmentList(attachmentList);
		return scoRapply;
	}
    @Transactional(readOnly = false)
    public void deleteScoCredit(ScoQuery scoQuery){
        ScoRapply scoRapply = new ScoRapply(scoQuery.getId());
        //删除申请
        delete(scoRapply);
        //删除配比
        scoRapplyPbService.deleteWLByApplyId(scoQuery);
        //删除组成员信息
        ScoRapplyMember scoRapplyMember = new ScoRapplyMember();
        scoRapplyMember.setApply(scoRapply);
        scoRapplyMemberService.deleteRapplyMembers(scoRapplyMember);
        //删除总分
        scoRsumService.deleteByAppId(scoQuery);
    }

	public List<ScoRapplyRecord> scoRapplyRecordList(ScoRapply entity){
		//获取审核结果
		ScoRapplyRecord scoRapplyRecord = new ScoRapplyRecord();
		scoRapplyRecord.setApply(new ScoRapply(entity.getId()));
		scoRapplyRecord.setQueryStr(ScoRstatus.SRS_PASS.getKey()+StringUtil.DOTH+ScoRstatus.SRS_NOPASS.getKey());
		List<ScoRapplyRecord> scoRapplyRecordList = scoRapplyRecordService.findList(scoRapplyRecord);
		return  scoRapplyRecordList;
	}

	public Page<ScoQuery> findScoCreditQueryPage(Page<ScoQuery> page,ScoQuery entity){
		entity.setPage(page);
		page.setList(findScoRuleNameList(dao.findScoCreditQueryList(entity)));
		return page;
	}
    public List<ScoRapply> scoRuleInRapply(ScoRule entity){
	    return dao.scoRuleInRapply(entity);
    }
	/**
	 * 查询当前申报的相关申报.
	 */
	public List<ScoRapply> findList(ScoRapply entity) {
		return super.findList(entity);
	}

	/**
	 * 检查是否存在该项目.
	 * @param id
	 * @return boolean
	 */
	public boolean checkHasProject(String id) {
	    ScoRapply rapply = get(id);
	    if(rapply == null){
	        return false;
	    }
	    rapply.setUids(Arrays.asList(new String[]{rapply.getUser().getId()}));
	    Integer count =  dao.checkHasProject(rapply);
	    return (count > 0);
	}

    public List<ScoRapply> findProjects(ScoRapply entity) {
	    entity = dao.ajaxFindScoDetail(entity);
	    if(entity == null){
	        return null;
	    }

//        ScoRapplyMember scoRapplyMember = new ScoRapplyMember();
//        scoRapplyMember.setApply(new ScoRapply(entity.getId()));
//        List<ScoRapplyMember> scoRapplyMemberList = scoRapplyMemberService.findScoMemberList(scoRapplyMember);
//        entity.setScoRapplyMemberList(scoRapplyMemberList);

        ScoRapply pentity = new ScoRapply();
        pentity.setStatus(ScoRstatus.SRS_PASS.getKey());
        List<String> uids = Lists.newArrayList();
        uids.add(entity.getUser().getId());
        pentity.setUids(uids);
        if((ScoRuleType.MAX_SCORE.getKey()).equals(entity.getRdetail().getMaxOrSum())){
            pentity.setLrule(entity.getLrule());
            pentity.setAppIds(entity.getAppIds());
        }else if((ScoRuleType.SUM_SCORE.getKey()).equals(entity.getRdetail().getMaxOrSum())){
            pentity.setRdetail(entity.getRdetail());
        }

        List<ScoRapply> srapplys = dao.findProjects(pentity);
        List<ScoRapply> filters = Lists.newArrayList();
        for (ScoRapply srapply : srapplys) {
            for (ScoRapply cur : srapplys) {
                if((cur.getAppIds()).contains(srapply.getId())){
                    filters.add(cur);
                    srapply.getApps().add(cur);
                }
            }
        }

        srapplys.removeAll(filters);
	    return dao.findProjects(pentity);
	}

	public Page<ScoRapply> findPage(Page<ScoRapply> page, ScoRapply entity) {
		return super.findPage(page, entity);
	}
	public List<ScoRapply> findListByg(ScoRapply entity) {
	    return dao.findListByg(entity);
	}

	public Page<ScoRapply> findPageByg(Page<ScoRapply> page, ScoRapply entity) {
	    entity.setPage(page);
        page.setList(findListByg(entity));
        return page;
	}
	public Page<ScoQuery> findCreditPage(Page<ScoQuery> page, ScoQuery entity) {
		entity.setPage(page);
		page.setList(findScoRuleNameList(dao.findCreditList(entity)));
		return page;
	}

	public List<ScoQuery> findScoRuleNameList(List<ScoQuery> scoQueryList){
        List<ScoRset> list = scoRsetService.findList(new ScoRset());
		for(ScoQuery scoQuery : scoQueryList){
		    if(StringUtil.checkNotEmpty(list)){
                //String price_CNY ="5";
                String keepNpoint = list.get(0).getKeepNpoint();
                String score = scoQuery.getCredit() == null ? Const.NO : scoQuery.getCredit();
                Double cny = Double.parseDouble(score);//转换成Double
                DecimalFormat df = toDouByRset(keepNpoint);
                scoQuery.setCredit(df.format(cny));
                if(scoQuery.getStatus().equals(ScoRstatus.SRS_DSH.getKey())){
                    scoQuery.setCredit("");
                }
            }
			if(scoQuery.getRid() != null && StringUtil.isNotEmpty(scoQuery.getRid())){
				ScoRule scoRule = scoRuleService.findScoRule(new ScoRule(scoQuery.getRid()));
				String[] scoRuleIds  = scoRule.getParentIds().split(StringUtil.DOTH);
				List<ScoRule> scoRuleList = Lists.newArrayList();
                scoRuleList.add(scoRule);
				for(int i=3;i<scoRuleIds.length;i++){
					scoRuleList.add(scoRuleService.findScoRule(new ScoRule(scoRuleIds[i])));
				}
				scoQuery.setScoRuleList(scoRuleList);
			}
		}
		return  scoQueryList;
	}


    /**
     * 根据规则返回指定格式.
     * @param keepNpoint
     * @return DecimalFormat
     */
	public static DecimalFormat toDouByRset(String keepNpoint) {
        DecimalFormat df = new DecimalFormat("0.00");
        if(keepNpoint.equals(ScoRset.VAL_1)){
            df = new DecimalFormat("0.0");
        }else if(keepNpoint.equals(ScoRset.VAL_3)){
            df = new DecimalFormat("0.000");
        }
        return df;
    }

	@Transactional(readOnly = false)
	public void save(ScoRapply entity) {
		if(entity.getIsNewRecord()){
		    if(StringUtil.isEmpty(entity.getIsTemp())){
		        entity.setIsTemp(Const.NO);
		    }
		    if(entity.getAutDate() == null){
                entity.setAutDate(entity.getApplyDate());
            }
		    if(entity.getIsFirst() == null){
		        entity.setIsFirst(Const.NO);
		    }
	    }
		super.save(entity);
        //向后台管理员发送申请消息
        List<String> roles = Lists.newArrayList();
        Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
        if(role!=null){
            roles = userService.findListByRoleId(role.getId());
            if(roles!=null && roles.size()>0){
                User user=UserUtils.getUser();
                oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "创新创业学分申请", user.getName()+"申请"+
                        scoRuleDetailService.get(entity.getRdetail().getId()).getName()+"的学分", OaNotify.Type_Enum.TYPE21.getValue(), entity.getId());
            }
        }
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRapply> entitys) {
        dao.insertPL(entitys);
    }

	@Transactional(readOnly = false)
	public void saveCreditResultApply(ScoRapply scoRapply){
		sysAttachmentService.deleteByUid(scoRapply.getId());
		//保存成果物附件
		sysAttachmentService.saveBySysAttachmentVo(scoRapply.getSysAttachmentList(), scoRapply.getId(), FileTypeEnum.S16, FileStepEnum.S1601);
	}

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRapply> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRapply entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRapply entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRapply entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRapply entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

    /**
     * 学分审核.
     */
    @Transactional(readOnly = false)
    public Boolean curActYw() {
        //返回结果状态
        return true;
    }

    /**
     * 学分审核.
     * @param scoaVo
     */
    @Transactional(readOnly = false)
    public Boolean audit(ScoAuditVo scoaVo) {
        //获取申请对象.
        ScoRapply scoRapply = get(scoaVo.getId());
        //更改申请状态.
        if((PassNot.PASS.getKey()).equals(scoaVo.getAtype())){
            scoRapply.setStatus(ScoRstatus.SRS_PASS.getKey());
        }else if((PassNot.NOT.getKey()).equals(scoaVo.getAtype())){
            scoRapply.setStatus(ScoRstatus.SRS_NOPASS.getKey());
        }else{
            scoRapply.setStatus(ScoRstatus.SRS_DSH.getKey());
        }

        //计算最终得分情况
        scoaVo = ScoAuditVo.countScore(scoaVo, scoRapply);
        //保存、更新数据
        scoRapply.setAutDate(DateUtil.newDate());
        scoRapply.setAutBy(UserUtils.getUser());
        //save(scoRapply);
        //启动流程
        IAsup asup = new IAsup();
        asup.iscore(scoaVo.getScore());
        asup.igrade(scoaVo.getAtype());
        asup.iremarks(scoaVo.getRemarks());
        scoRapply.iasup(asup);
        scoRapply.setIgnodeId(scoaVo.getIgnodeId());
        scoRapply = auditByGateWay(scoRapply);
        scoRapply.setTval(scoaVo.getScore());
        if((PassNot.PASS.getKey()).equals(scoaVo.getAtype())){
            scoRapply.setTcval(scoaVo.getTcval());
        }
        scoRapply.setAppIds(scoaVo.getAppIds());
//        scoRapply.setAppIds(StringUtil.listToStr(scoaVo.getAppIds()));
        save(scoRapply);

        scoRapplyRecordService.save(ScoRapplyRecord.convert(scoRapply, scoaVo.getRemarks()));
        scoRsumService.deleteWLByAppId(new ScoRsum(scoRapply));
        if((PassNot.PASS.getKey()).equals(scoaVo.getAtype())){
            /**
             * 是否需要处理相同项目(只支持单个项目).
             * 规则：相同项目，取最高分，最高分相等，不处理
             */
            boolean needDpro = false;
            boolean needDealPro = false;
            List<ScoRsum> odscoRsums = Lists.newArrayList();
            if(StringUtil.isNotEmpty(scoaVo.getAppId())){
                ScoRapply srapply = getGtRapply(scoRapply, odscoRsums, scoaVo);
                //判断总分数是否大于旧的
                if(srapply != null){
                    if((Const.YES).equals(srapply.getIsFirst())){
                        save(srapply);
                    }

                    if((scoRapply.getTcval() > srapply.getTcval())){
                        /**
                         * 如果当前申请大于历史申请，需要处理历史数据标记为isPro=1,新增的总分isPro=0;
                         * 如果当前申请小于历史申请，需要处理历史数据标记为isPro=0,新增的总分isPro=1;
                         */
                        needDpro = false;
                        needDealPro = true;
                    }else if((scoRapply.getTcval() == srapply.getTcval())){
                        /**
                         * 如果当前申请等于历史申请,且申请ID一致是，需要处理历史数据标记为isPro=1,新增的总分isPro=0;
                         */
                        if((scoRapply.getId()).equals(srapply.getId())){
                            needDpro = false;
                            needDealPro = true;
                        }else{
                            needDpro = true;
                            needDealPro = false;
                        }
                    }else{
                        needDpro = true;
                        needDealPro = false;
                    }
                }

                //总分表无数据不做处理
                if(StringUtil.checkEmpty(odscoRsums)){
                    needDealPro = false;
                }

                if(needDealPro){
                    for (ScoRsum scoRsum : odscoRsums) {
                        scoRsum.setIsSpro(Const.YES);
                    }
                    scoRsumService.updatePL(odscoRsums);
                }
            }

            scoRsumService.deleteWLByAppId(scoRapply.getId());
            if(StringUtil.checkNotEmpty(scoaVo.getMembers())){
                scoRapplyPbService.deleteWLByApplyId(scoRapply.getId());
                List<ScoRsum> scoRsums = ScoRsum.convert(scoaVo, scoRapply, needDpro);
                scoRsumService.insertPL(scoRsums);
                scoRapplyPbService.insertPL(ScoRapplyPb.convert(scoRsums, scoaVo, scoRapply));
            }else{
                scoRsumService.save(ScoRsum.convert(scoRapply, scoRapply.getUser(), scoaVo.getScore(), needDpro));
            }
        }else{
            scoRapplyPbService.deleteWLByApplyId(scoRapply.getId());
            if(StringUtil.checkNotEmpty(scoaVo.getMembers())){
                scoRapplyPbService.insertPL(ScoRapplyPb.convert(scoaVo, scoRapply));
            }
        }
        //返回结果状态
        return true;
    }

    /**************************************************************************************
     * 自定义工作流相关方法.
     **************************************************************************************/
    /**
     * 启动流程：request必填.
     * @param rapply ScoRapply
     * @param request
     * @return ScoRapply
     */
    @Transactional(readOnly = false)
    public ScoRapply start(IApply rapply, HttpServletRequest request) {
        String id = request.getParameter(IActYw.IACTYW_ID);
        String flowType = request.getParameter(IGroup.IFLOW_TYPE);
        String ptype = request.getParameter(IConfig.I_PTYPE);
        if(StringUtil.isNotEmpty(id)){
            rapply.iactYw(actYwService.get(rapply.iactYw().id()));
            return start(rapply);
        }else{
            return start(rapply, flowType, ptype);
        }
    }

    /**
     * 启动流程：flowType和ptype必填.
     * @param rapply ScoRapply
     * @param flowType 流程类型
     * @param ptype 项目类型
     * @return ScoRapply
     */
    @Transactional(readOnly = false)
    public ScoRapply start(IApply rapply, String flowType, String ptype) {
        FlowType ftype = null;
        if(StringUtil.isEmpty(rapply.iactYw().id())){
            if((StringUtil.isEmpty(flowType) || StringUtil.isEmpty(ptype))){
                rapply.iaprop().istart(false);
            }else{
                ftype = FlowType.getByKey(flowType);
                rapply.iactYw().config().ptype(ptype);
            }

            if(ftype != null){
                rapply.iactYw().group().flowType(ftype);
                rapply.iactYw(actYwService.findCurrsByflowTypeAndPType(rapply.iactYw()));
            }
        }else{
            rapply.iactYw(actYwService.get(rapply.iactYw().id()));
        }
        return start(rapply);
    }

    /**
     * 启动流程：actYwId必填.
     * @param rapply ScoRapply
     * @return ScoRapply
     */
    @Transactional(readOnly = false)
    private ScoRapply start(IApply rapply) {
        if((rapply.iactYw() == null) || StringUtil.isEmpty(rapply.getIactYwId())){
            throw new ActYwRuntimeException("流程配置故障（审核流程不存在），请联系管理员!");
        }
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(rapply.iactYw()));
        if((actYwNextGnode == null)){
            throw new ActYwRuntimeException("流程配置故障（流程未发布或未生效），请联系管理员!");
        }
        String nodeRoleId = null;
        List<String> roles = new ArrayList<String>();
        if(actYwNextGnode.getIsAssign()){
            roles.clear();
            roles.add(ActYwTool.FLOW_ASSIGN_USER);
        }else {
            List<Role> roleList = actYwNextGnode.getRoles();
            if(StringUtil.checkEmpty(roleList)){
               throw new ActYwRuntimeException("找不到流程下一步审核人员，请联系管理员!");
            }
            roles = scrProModelService.getUsersByRoleList(rapply.getIid(), roleList);
            nodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
        }

        if (StringUtil.checkEmpty(roles)) {
            throw new ActYwRuntimeException("找不到流程下一步审核人员，请联系管理员!");
        }
        identityService.setAuthenticatedUserId(UserUtils.getUser().getId());
        //加Tenant_id
         ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(ActYw.getPkey(rapply.iactYw()), ActYw.genBusinessKey(rapply.iactYw(), rapply.getIid()), initVar(rapply, actYwNextGnode, roles, nodeRoleId), TenantConfig.getCacheTenant());
        // 流程id返写业务表
        if (procIns == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程未启动），请联系管理员!");
        }
        rapply.iprocInsId(procIns.getId());
        rapply.iaprop().istart(true);
        return (ScoRapply) rapply;
    }

    @Transactional(readOnly = false)
    public ScoRapply auditByGateWay(IApply rapply) throws GroupErrorException {
        //走审核节点
        Map<String, Object> vars = Maps.newHashMap();
        ActYw actYw = actYwService.get(rapply.getIactYwId());
        // 根据当前节点gnodeId和网关判断条件得到下一步审核角色
        if ((actYw != null) && StringUtil.isNotEmpty(rapply.getIgnodeId())) {
            String key = ActYw.getPkey(actYw);
            // 得到当前节点
            ActYwGnode actYwGnode = actYwGnodeService.getBygGclazz(rapply.getIgnodeId());
            //是否是值传递节点
            Boolean isListen = false;
            Boolean isGate = scrProModelService.getNextIsGate(rapply.getIgnodeId());
            ActYwGnode nextGnode = null;
            ActYwGnode nextGate = null;
            String nextGnodeRoleId = null;
            if (isGate) {
                nextGate = scrProModelService.getNextNextGate(rapply.getIgnodeId());
                isListen = nextGate.hasListeners();
            } else {
                nextGate = scrProModelService.getNextNextGnode(rapply.getIgnodeId());
                if (nextGnode != null && (GnodeType.GT_PROCESS_TASK.getId().equals(nextGnode.getType()) || GnodeType.GT_ROOT_TASK.getId().equals(nextGnode.getType()))) {
                    List<Role> roleList= nextGnode.getRoles();
                    if (roleList == null) {
                        throw new GroupErrorException("审核节点角色配置错误。");
                    }
                    //多角色配置人员
                    nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
                }
            }

            String taskId = null;
            try{
                // 当前节点为签收节点 先签收任务
                if (actYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
                    //得到当前节点的审核人
                    List<Role> roleList= actYwGnode.getRoles();
                    //任务已经被签收就向其他待签收者发消息 该任务已经被签收
                    List<String> noneUserList= scrProModelService.getUsersByRoleList(rapply.iauserId(), roleList);
                    if(noneUserList!=null &&noneUserList.size()>0){
                        noneUserList.remove(UserUtils.getUser().getId());
                        if(noneUserList.size()>0){
                            //审核完成发消息
							scrProModelService.sendOaNotifyByTypeAndUser(UserUtils.getUser(), noneUserList, "任务审核", rapply.getIname() + "审核已经被其他人签收" , OaNotify.Type_Enum.TYPE14.getValue(), rapply.getIid());
                        }
                    }
                    taskId = actTaskService.getTaskidByProcInsId(rapply.getIprocInsId());
                    taskService.claim(taskId, UserUtils.getUserId());
                } else {
                    // 铜陵学院代码 因为老师和学生角色未分开 前台提交统一按照项目负责人查找taskId
                    if (actYwGnode.getGforms() != null && FormClientType.FST_FRONT.getKey().equals(actYwGnode.getGforms().get(0).getForm().getClientType())) {
                        taskId = actTaskService.getTaskidByProcInsId(rapply.getIprocInsId(), rapply.iauserId());//UserUtils.getUser().getId()
                    } else {
                        taskId = actTaskService.getTaskidByProcInsId(rapply.getIprocInsId(), UserUtils.getUserId());//UserUtils.getUser().getId()
                    }
                }
            }catch (Exception e){
                throw new GroupErrorException("该任务不存在");
            }

            if(StringUtil.isEmpty(taskId)){
                throw new GroupErrorException("该任务不存在");
            }
            Task task = actTaskService.getTask(taskId);
            //是否是同一审核节点  每个节点批次号批次号不一样
            String gnodeVesion = (String) taskService.getVariable(taskId, "gnodeVesion");
            //判断是否改变审核节点 1是改变了审核节点 0是没有改变审核节点
            String isChangeNode = (String) taskService.getVariable(taskId, "isChangeNode");
            //判断审核前后台
            if (!(FormClientType.FST_FRONT.getKey()).equals(actYwGnode.getGforms().get(0).getForm().getClientType())) {
                // 根据批次号 保存后台审核结果
                if (gnodeVesion != null) {
					scrProModelService.saveActYwAuditInfo(rapply, actYwGnode, gnodeVesion);
                } else {
					scrProModelService.saveActYwAuditInfo(rapply, actYwGnode);
                }
            } else {
                // 保存前台审核结果
                if (gnodeVesion != null) {
					scrProModelService.saveFrontActYwAuditInfo(rapply, actYwGnode, gnodeVesion);
                } else {
					scrProModelService.saveFrontActYwAuditInfo(rapply, actYwGnode);
                }
            }
            //工作流传递携带参数
            vars = rapply.ivars();
            if(StringUtil.isNotEmpty(isChangeNode) && "1".equals(isChangeNode)){
                vars.put("isChangeNode","0");
            }
            //项目类型 名称
            String typeName = actYw.projectTname();
            if (actYwGnode != null) {
                // 节点之间有连接线
                // 根据实例id得到下一个节点
                // 判断下一个节点是否为网关
                boolean isEndNode = actTaskService.isMultiLast(key, task.getTaskDefinitionKey(), rapply.getIprocInsId());
                //最后一个审核人 更换批次号
                if (isEndNode) {
                    vars.put("gnodeVesion", IdGen.uuid());
                }
                //是否是学分审核
                Boolean isScoreAudit = false;
                String auditGrade = null;
                // 判断是网关
                if (isGate) {
                    // 判断是否为值传递
                    if (isListen) {
                        Object object=runtimeService.getVariable(rapply.getIprocInsId(),"globel");
                        if(object!=null && rapply.iasup().igrade()==null){
                            auditGrade = (String)object;
                        }
                    }
                    // 根据网关节点得到网关后面连接线
                    List<IAstatus> actYwStatusList = scrProModelService.getActYwStatus(rapply.getIgnodeId());
                    if (actYwStatusList != null && actYwStatusList.size() > 0) {
                        // 判断审核结果 判断网关类型  为评分还是审核 1是审核2是评分
                        for (IAstatus actYwStatus : actYwStatusList) {
                            if ((RegType.RT_GE.getId()).equals(actYwStatus.getIregType())) {
                                isScoreAudit = true;
                                break;
                            }
                        }
                        IAstatus actYwStatusNext = null;
                        if (isScoreAudit) {
                            // 判断是否走到最后一步
                            boolean isLast = actTaskService.isMultiLast(key, task.getTaskDefinitionKey(), rapply.getIprocInsId());
                            if (isLast) { // 如果当前任务环节完成了
                                ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
                                actYwAuditInfo.setPromodelId(rapply.getIid());
                                actYwAuditInfo.setGnodeId(rapply.getIgnodeId());
                                if (gnodeVesion != null) {
                                    actYwAuditInfo.setGnodeVesion(gnodeVesion);
                                }
                                rapply.iasup().iscore(scrProModelService.getAuditAvgInfo(actYwAuditInfo));
                                // 判断 走的那种节点状态
                                if (auditGrade != null) {
                                    actYwStatusNext = scrProModelService.getGateActYwStatusByGrade(auditGrade, actYwStatusList);
                                } else {
                                    actYwStatusNext = scrProModelService.getGateActYwStatus(rapply, actYwStatusList);
                                }
                                if (actYwStatusNext != null) {
                                    if (auditGrade != null) {
                                        nextGnode = scrProModelService.getNextGnodeByGrade(auditGrade, rapply.getIgnodeId());
                                    } else {
                                        nextGnode = scrProModelService.getNextGnode(rapply, rapply.getIgnodeId());
                                    }
                                    if ((nextGnode != null) && !((GnodeType.GT_PROCESS_END.getId()).equals(nextGnode.getType())) && !((GnodeType.GT_ROOT_END.getId()).equals(nextGnode.getType()))) {
                                        //nextGnodeRoleId = nextGnode.getGroles().get(0).getRole().getId();
                                        List<Role> roleList= nextGnode.getRoles();
                                        if (StringUtil.checkEmpty(roleList)) {
                                           throw new GroupErrorException("审核节点角色配置错误。");
                                        }
                                        //多角色配置人员
                                        nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
                                        vars = scrProModelService.addRoleIn(nextGnode, nextGnodeRoleId, vars, rapply);
                                        //向下一步审核人发送审核消息
                                        if(StringUtil.isNotEmpty(nextGnodeRoleId)){
											scrProModelService.sendMsgToNextGnodeUser(nextGnode, vars, nextGnodeRoleId, rapply);
                                        }
                                    }
                                    //如果是值传递节点  将审核的节点值保存到数据库
                                    if (isListen && StringUtil.isEmpty(auditGrade)) {

                                        runtimeService.setVariable(rapply.getIprocInsId(),"globel",actYwStatusNext.getIstatus());
//										scrProModelService.setListenGrade(nextGate, rapply.getIid(), actYwStatusNext.getIstatus(), ClazzThemeListener.CMR_A.getKey());
                                    }
                                    vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getIstatus());
                                    try {
                                        taskService.complete(taskId, vars);
                                    } catch (Exception e) {
                                        logger.error("审核节点配置错误。");
                                        throw new GroupErrorException("审核节点配置错误。");
                                    }
                                    //审核完成发消息
									scrProModelService.sendOaNotifyByType(UserUtils.getUser(), new User(rapply.iauserId()), "学校管理员审核",
                                        typeName + " " + rapply.getIname() + "项目，" + actYwGnode.getName()
                                        + actYwStatusNext.getIstate(), OaNotify.Type_Enum.TYPE14.getValue(), rapply.getIid());
                                }
                            } else {
                                try {
                                    taskService.complete(taskId, vars);
                                } catch (Exception e) {
                                    logger.error("审核节点配置错误。");
                                    throw new GroupErrorException("审核节点配置错误。");
                                }
                            }
                        } else {
                            if (auditGrade != null) {
                                actYwStatusNext = scrProModelService.getGateActYwStatusByGrade(auditGrade, actYwStatusList);
                                nextGnode = scrProModelService.getNextGnodeByGrade(auditGrade, rapply.getIgnodeId());
                            } else {
                                actYwStatusNext = scrProModelService.getGateActYwStatus(rapply, actYwStatusList);
                                nextGnode = scrProModelService.getNextGnode(rapply, rapply.getIgnodeId());
                            }
                            if (nextGnode != null
                                    && !GnodeType.GT_PROCESS_END.getId().equals(nextGnode.getType())
                                    && !GnodeType.GT_ROOT_END.getId().equals(nextGnode.getType())) {
                                //nextGnodeRoleId = nextGnode.getGroles().get(0).getRole().getId();
                                List<Role> roleList= nextGnode.getRoles();
                                if (roleList == null) {
                                  throw new GroupErrorException("审核节点角色配置错误。");
                                }
                                //多角色配置人员
                                nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
                                vars = scrProModelService.addRoleIn(nextGnode, nextGnodeRoleId, vars, rapply);
                                //向下一步审核人发送审核消息
                                if(StringUtil.isNotEmpty(nextGnodeRoleId)){
									scrProModelService.sendMsgToNextGnodeUser(nextGnode,vars,nextGnodeRoleId,rapply);
                                }
                                vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getIstatus());
                                taskService.complete(taskId, vars);
                            } else {
                                vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getIstatus());
                                try {
                                    taskService.complete(taskId, vars);
                                } catch (Exception e) {
                                    logger.error("审核节点配置错误。");
                                    throw new GroupErrorException("审核节点配置错误。");
                                }
                            }
                            if (isListen && StringUtil.isEmpty(auditGrade)) {

                                runtimeService.setVariable(rapply.getIprocInsId(),"globel",actYwStatusNext.getIstatus());
//								scrProModelService.setListenGrade(nextGate, rapply.getIid(), actYwStatusNext.getIstatus(), ClazzThemeListener.CMR_A.getKey());
                            }
							scrProModelService.sendOaNotifyByType(UserUtils.getUser(), new User(rapply.iauserId()), "学校管理员审核",
                                    typeName + " " + rapply.getIname() + "学分，" + actYwGnode.getName()
                                    + actYwStatusNext.getIstate(),OaNotify.Type_Enum.TYPE14.getValue(), rapply.getIid());
                        }
                    }
                } else {
                    if (nextGnodeRoleId == null) {
                        try {
                            taskService.complete(taskId, vars);
                        } catch (Exception e) {
                            logger.error("审核节点配置错误。");
                            throw new GroupErrorException("审核节点配置错误。");
                        }
                    } else {
                        // 没有网关
                        vars = scrProModelService.addRoleIn(nextGnode, nextGnodeRoleId, vars, rapply);
                        try {
                            //向下一步审核人发送审核消息
                            if(StringUtil.isNotEmpty(nextGnodeRoleId)){
								scrProModelService.sendMsgToNextGnodeUser(nextGnode, vars, nextGnodeRoleId, rapply);
                            }
                            taskService.complete(taskId, vars);
                        } catch (Exception e) {
                            logger.error("审核节点配置错误。");
                            throw new GroupErrorException("审核节点配置错误。");
                        }
                    }
                }
            }
        }
       return (ScoRapply) rapply;
    }

    /**
     * 初始化流程变量.
     * @param rapply ScoRapply
     * @param nextGnode ActYwGnode
     * @param roles List
     * @param nodeRoleId String
     * @return Map
     */
    private Map<String, Object> initVar(IApply rapply, ActYwGnode nextGnode, List<String> roles, String nodeRoleId) {
        Map<String, Object> vars = rapply.ivars();
        if (nextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(nextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, roles);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + ActYwTool.FLOW_ROLE_POSTFIX_S, roles);
        }
        return vars;
    }

    public static void main(String[] args) {
        System.out.println(Encodes.escapeHtml("/cms/form/SNPT190100127/88888888882?actywId=8b5305bf11234b6a96262b05112c2546&gnodeId=4f05f29628174ce390ba44493bfdec0f"));
    }

    @Override
    @Transactional(readOnly = false)
    public void idelPl(List<String> ids) {
        dao.deletePL(new ScoRapply(ids));
    }

    @Override
    public List<ScoRapply> ifindTodoByIds(List<String> ids) {
        if (StringUtil.checkEmpty(ids)) {
            return Lists.newArrayList();
        }

        ScoRapply apply = new ScoRapply();
        apply.setIds(ids);
        apply.setIsTemp(Const.NO);
        apply.setStatus(ScoRstatus.SRS_DSH.getKey());
        return dao.findListBygIds(apply);
    }

    @Override
    public Page<ScoRapply> ifindQPage(ScrAengine engine) {
        //TODO
        return null;
    }
    @Override
    public Page<ScoRapply> ifindPage(ScrAengine engine) {
        ScoRapply apply = (ScoRapply) engine.apply();
        List<ActYwGnode> actYwGnodes = scrProModelService.getSubGnodeList(apply.getIgnodeId(), apply.iactYw().group().id());
        List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<String> recordIds = scrProModelService.recordIds(new Act(ActYw.getPkey(apply.iactYw())), gnodeIdList, apply.getIactYwId());
        Page<ScoRapply> page = new Page<ScoRapply>(engine.request(), engine.response());
        apply.setIds(recordIds);
        apply.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(Lists.newArrayList());
            return page;
        }

        if (StringUtil.isEmpty(page.getOrderBy())) {
            //page.setOrderBy(ScoRapply.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE);
            //page.setOrderByType(Page.ORDER_DESC);
        }
        apply.setIsTemp(Const.NO);
        //apply.setStatus(ScoRstatus.SRS_DSH.getKey());

        String actYwId = engine.request().getParameter(IActYw.IACTYW_ID);
        if(StringUtil.isNotEmpty(actYwId)){
            apply.setActYw(new ActYw(actYwId));
        }

        SimpleDateFormat format =   new SimpleDateFormat(DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
        String createDate = engine.request().getParameter("createDate");
        if(StringUtil.isNotEmpty(createDate)){
            apply.setCreateDate(DateUtil.formatToDate(format, createDate));
        }

        String createQDate = engine.request().getParameter("createQDate");
        if(StringUtil.isNotEmpty(createQDate)){
            apply.setCreateQDate(DateUtil.formatToDate(format, createQDate));
        }

        String keys = engine.request().getParameter("keys");
        if(StringUtil.isNotEmpty(keys)){
            apply.setKeys(keys);
        }

        String status = engine.request().getParameter("status");
        if(StringUtil.isNotEmpty(status)){
            apply.setStatus(status);
        }

        String rule_id = engine.request().getParameter("rule.id");
        if(StringUtil.isNotEmpty(rule_id)){
            apply.setRule(new ScoRule(rule_id));
        }

        String user_office_id = engine.request().getParameter("user.office.id");
        if(StringUtil.isNotEmpty(user_office_id)){
            User puser = new User();
            puser.setOffice(new Office(user_office_id));
            apply.setUser(puser);
        }

        Integer keepNpoint = getKpoint();
        List<ScoRapply> applys = dao.findListBygIds(apply);
        //List<ScoRapply> applysCount = dao.findListBygIdsNews(apply); //将最新操作的数据放在第一页第一条显示  打开此代码
        //List<ScoRapply> applys = dao.findListBygIdsNew(apply);//将最新操作的数据放在第一页第一条显示  打开此代码 取代findListBygIds(apply)方法
        //page.setCount(applysCount.size());//将最新操作的数据放在第一页第一条显示  打开此代码
        for (ScoRapply scoApply : applys) {
            scoApply = (ScoRapply) initIamap(engine, actYwGnodes.get(0), scoApply);
            if((scoApply.getTcval() == null) || (keepNpoint == null)){
                continue;
            }
            scoApply = formatNum(scoApply);
        }

        page.setList(applys);
        return page;
    }

    public Integer getKpoint() {
        List<ScoRset> list = scoRsetService.findList(new ScoRset());
        if(StringUtil.checkNotEmpty(list) && (list.get(0) != null) && StringUtil.isNumeric(list.get(0).getKeepNpoint())){
            System.out.println(Integer.parseInt(list.get(0).getKeepNpoint()));
            return Integer.parseInt(list.get(0).getKeepNpoint());
        }
        return null;
    }

    @Override
    public IApply initIamap(ScrAengine engine, IGnode subGnode, IApply scoApply) {
        /**
         * 添加审核方法参数.
         */
        scoApply.iamap(ProcessDefUtils.initIamap(engine, scoApply));

        /**
         * 判断当前结点是否为评分节点.
         */
        ActYwGnode lastNode = null;
        ActYwAuditInfo actYwAuditInfoIn = new ActYwAuditInfo();
        actYwAuditInfoIn.setPromodelId(scoApply.getIid());
        actYwAuditInfoIn.setGnodeId(subGnode.id());
        ActYwAuditInfo plastActYwAuditInfoIn = scrProModelService.getGnodeByNextGnode(actYwAuditInfoIn);
        if(plastActYwAuditInfoIn != null){
            lastNode = scrProModelService.getByg(plastActYwAuditInfoIn.getGnodeId());
            List<ActYwGform> forms = lastNode.getGforms();
            for(ActYwGform actYwGform : forms){
                if((RegType.RT_GE.getId()).equals(actYwGform.getForm().getSgtype())){
                    scoApply.getIamap().setIregType(RegType.RT_GE.getId());
                    scoApply.getIamap().setIscore(true);
                    break;
                }
            }
        }

        //如果上一个节点为评分节点 算出平均分
        if(scoApply.getIamap().getIscore()){
            actYwAuditInfoIn = new ActYwAuditInfo();
            actYwAuditInfoIn.setPromodelId(scoApply.getIid());
            actYwAuditInfoIn.setGnodeId(lastNode.getId());
            ActYwAuditInfo lastActYwAuditInfoIn = scrProModelService.getLastAudit(actYwAuditInfoIn);

            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setPromodelId(scoApply.getIid());
            actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
            //判断是否存在同批次号数据
            if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
                actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
            }
            scoApply.iasup().iscore(scrProModelService.getAuditAvgInfo(actYwAuditInfo));
        }


        // 项目结果
        if (StringUtils.isNotBlank(scoApply.iaprop().iendgnid()) && (scoApply.iaprop().iend())) {// 流程已结束
//                    getFinalResult(tlxyProModel);

//                  ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
//                  actYwAuditInfo.setGnodeId(tlxyProModel.getEndGnodeId());
//                  actYwAuditInfo.setPromodelId(tlxyProModel.getId());
//                  if(StringUtil.isNotEmpty(tlxyProModel.getEndGnodeVesion())){
//                      actYwAuditInfo.setGnodeVesion(tlxyProModel.getEndGnodeVesion());
//                  }
//                  ActYwGnode endNode = actYwGnodeService.get(tlxyProModel.getEndGnodeId());
//                  tlxyProModel.setFinalResult((endNode != null ? endNode.getName() : "") + proModelService.getStateByAuditInfo(actYwAuditInfoService.findList(actYwAuditInfo)));
        }
        return scoApply;
    }

    @Override
    public IApply gen(HttpServletRequest request) {
        ScoRapply rapply = (ScoRapply) IApply.iall(new ScoRapply(), request);
//        String year = request.getParameter("year");
//        String officeId = request.getParameter("officeId");
//        rapply.setIsAll(request.getParameter("isAll"));
//        if(!(CoreSval.YES).equals(proModel.getIsAll())){
//            rapply.setYear(year);
//            if (StringUtil.isNotBlank(officeId)) {
//                User user = new User();
//                user.setOffice(new Office(officeId));
//                rapply.iauser(user);
//            }
//            rapply.setKeys(request.getParameter("queryStr"));
//        }
        return rapply;
    }


	public List<ScoRapply> findScoProjects(String userId) {
		return dao.findScoProjects(userId);
	}

    public List<ScoRapplyValid> findDetailRapplyCertList(ScoRapplyValid scoRapplyValid){return dao.findDetailRapplyCertList(scoRapplyValid);}

    public List<ScoRapplyValid> findDetailNameList(ScoRapplyValid scoRapplyValid){return dao.findDetailNameList(scoRapplyValid);}

    public List<ScoRapply> findRuleDetailIsApply(ScoRule scoRule){ return  dao.findRuleDetailIsApply(scoRule);}
}