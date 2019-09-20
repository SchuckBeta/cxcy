package com.oseasy.pro.modules.auditstandard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.auditstandard.dao.AuditStandardDao;
import com.oseasy.pro.modules.auditstandard.dao.AuditStandardFlowDao;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandard;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardDetail;
import com.oseasy.pro.modules.auditstandard.vo.AuditStandardVo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 评审标准Service.
 * @author 9527
 * @version 2017-07-28
 */
@Service
@Transactional(readOnly = true)
public class AuditStandardService extends CrudService<AuditStandardDao, AuditStandard> {
	@Autowired
	private AuditStandardDetailService auditStandardDetailService;
	@Autowired
	private AuditStandardFlowDao auditStandardFlowDao;
	
	
	public int checkName(String id,String name) {
		return dao.checkName(id,name);
	}
	public AuditStandard get(String id) {
		return super.get(id);
	}

	public List<AuditStandard> findList(AuditStandard auditStandard) {
		return super.findList(auditStandard);
	}
	public Page<AuditStandardVo> findPageVo(Page<AuditStandardVo> page, AuditStandardVo auditStandard) {
		auditStandard.setPage(page);
		page.setList(dao.findListVo(auditStandard));
		if (page.getList()!=null&&page.getList().size()>0) {
			Map<String,List<Integer>> indexMap=new HashMap<String,List<Integer>>();
			List<Map<String,String>> param=new ArrayList<Map<String,String>>();
			for(int i=0;i<page.getList().size();i++) {
				AuditStandardVo vo=page.getList().get(i);
				Map<String,String> map=new HashMap<String,String>();
				map.put("flow", vo.getFlow());
				map.put("node", vo.getNode());
				param.add(map);
				if (indexMap.get(vo.getFlow()+vo.getNode())==null) {
					List<Integer> temlist=new ArrayList<Integer>();
					temlist.add(i);
					indexMap.put(vo.getFlow()+vo.getNode(), temlist);
				}else{
					indexMap.get(vo.getFlow()+vo.getNode()).add(i);
				}
			}
			addChildNodes(page, dao.getSelfNodes(param), indexMap);
			addChildNodes(page, dao.getChildNodes(param), indexMap);
		}
		return page;
	}
	private void  addChildNodes(Page<AuditStandardVo> page,List<Map<String, String>> ret,Map<String,List<Integer>> indexMap){
		if (ret!=null&&ret.size()>0) {
			for(Map<String, String> nodeMap:ret) {
				String flow=nodeMap.get("flow");
				String node=nodeMap.get("node");
				List<Integer> temLi=indexMap.get(flow+node);
				for(Integer in:temLi) {
					AuditStandardVo vo=page.getList().get(in);
					List<Map<String,String>> c=vo.getChilds();
					if (c==null) {
						c=new ArrayList<Map<String,String>>();
						vo.setChilds(c);
					}
					Map<String,String> tem=new HashMap<String,String>();
					tem.put("id", nodeMap.get("id"));
					tem.put("name", nodeMap.get("name"));
					if (vo.getIsEscoreNodes()!=null&&vo.getIsEscoreNodes().contains(nodeMap.get("id"))) {
						tem.put("sel", "1");
					}else{
						tem.put("sel", "0");
					}
					c.add(tem);
				}
			}
		}
	}
	public Page<AuditStandard> findPage(Page<AuditStandard> page, AuditStandard auditStandard) {
		return super.findPage(page, auditStandard);
	}

	@Transactional(readOnly = false)
	public void save(AuditStandard auditStandard) {
		super.save(auditStandard);
		auditStandardDetailService.delByFid(auditStandard.getId());
		for(int i=0;i<auditStandard.getCheckElement().size();i++) {
			AuditStandardDetail asd=new AuditStandardDetail();
			asd.setManageId(auditStandard.getId());
			asd.setCheckElement(auditStandard.getCheckElement().get(i));
			asd.setCheckPoint(auditStandard.getCheckPoint().get(i));
			asd.setViewScore(auditStandard.getViewScore().get(i));
			asd.setSort(i+"");
			auditStandardDetailService.save(asd);
		}
	}

	@Transactional(readOnly = false)
	public void delete(AuditStandard auditStandard) {
		super.delete(auditStandard);
	}
	@Transactional(readOnly = false)
	public void delete(AuditStandardVo vo) {
		if (StringUtil.isNotEmpty(vo.getFlow())&&StringUtil.isNotEmpty(vo.getNode())) {
			auditStandardFlowDao.delByCdn(vo.getId(), vo.getFlow(), vo.getNode());
		}else{
			AuditStandard a=new AuditStandard();
			a.setId(vo.getId());
			dao.delete(a);
			auditStandardDetailService.delByFid(a.getId());
			auditStandardFlowDao.delByPid(vo.getId());
		}
	}
}