package com.oseasy.pie.modules.impdata.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.dao.ActYwDao;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.iep.tool.impl.IeDmap;
import com.oseasy.pie.modules.impdata.dao.BackUserErrorDao;
import com.oseasy.pie.modules.impdata.dao.GcontestErrorDao;
import com.oseasy.pie.modules.impdata.dao.ImpInfoDao;
import com.oseasy.pie.modules.impdata.dao.ImpInfoErrmsgDao;
import com.oseasy.pie.modules.impdata.dao.OfficeErrorDao;
import com.oseasy.pie.modules.impdata.dao.PmgMemsErrorDao;
import com.oseasy.pie.modules.impdata.dao.PmgTeasErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProMdApprovalErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProMdCloseErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProMdMidErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProModelErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProModelGcontestErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProjectErrorDao;
import com.oseasy.pie.modules.impdata.dao.ProjectHsErrorDao;
import com.oseasy.pie.modules.impdata.dao.StudentErrorDao;
import com.oseasy.pie.modules.impdata.dao.TeacherErrorDao;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.tool.IitCheckService;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;

/**
 * 导入数据信息表Service
 *
 * @author 9527
 * @version 2017-05-16
 */
@Service
public class ImpInfoService extends CrudService<ImpInfoDao, ImpInfo> implements IitCheckService{
	@Autowired
	private ActYwDao actYwDao;
	@Autowired
	private ImpInfoErrmsgDao impInfoErrmsgDao;
	@Autowired
	private StudentErrorDao studentErrorDao;
	@Autowired
	private BackUserErrorDao backUserErrorDao;
	@Autowired
	private OfficeErrorDao officeErrorDao;
	@Autowired
	private TeacherErrorDao teacherErrorDao;
	@Autowired
	private ProjectErrorDao projectErrorDao;
	@Autowired
	private ProjectHsErrorDao projectHsErrorDao;
	@Autowired
	private ProMdCloseErrorDao proMdCloseErrorDao;
	@Autowired
	private ProMdApprovalErrorDao proMdApprovalErrorDao;
	@Autowired
	private ProMdMidErrorDao proMdMidErrorDao;
	@Autowired
	private GcontestErrorDao gcontestErrorDao;
	@Autowired
	private ProModelErrorDao proModelErrorDao;
	@Autowired
	private ProModelGcontestErrorDao proModelGcontestErrorDao;
	@Autowired
	private PmgMemsErrorDao pmgMemsErrorDao;
	@Autowired
	private PmgTeasErrorDao pmgTeasErrorDao;
//	@Autowired
//	private ProModelMdGcService proModelMdGcService;
	public final static Logger logger = Logger.getLogger(ImpInfoService.class);
	public Page<ImpInfo> getDrCardList(Page<ImpInfo> page, ImpInfo impInfo) {
	    if(impInfo == null){
	        impInfo = new ImpInfo();
	    }
	    impInfo.setImpTpye((ExpType.DrCard.getIdx()));
		impInfo.setPage(page);
		page.setList(dao.getDrCardList(impInfo));
		return page;
	}
	public Page<ImpInfo> getProModelList(Page<ImpInfo> page, ImpInfo impInfo) {
	    ActYw ay=actYwDao.get(impInfo.getActywid());
	    if(ay!=null){
	        impInfo.setActywid(ay.getId());
	        impInfo.setProtype(ay.getProProject().getProType());
	        impInfo.setProsubtype(ay.getProProject().getType());
	    }
	    impInfo.setPage(page);
	    page.setList(dao.getProModelList(impInfo));
	    return page;
	}
	public Page<Map<String, String>> getMdList(Page<Map<String, String>> page, Map<String, Object> param) {
		if (page.getPageNo() <= 0) {
			page.setPageNo(1);
		}
		int count = dao.getMdListCount(param);
		param.put("offset", (page.getPageNo() - 1) * page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String, String>> list = null;
		if (count > 0) {
			list = dao.getMdList(param);
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}
	public Page<Map<String, String>> getList(Page<Map<String, String>> page, Map<String, Object> param) {
		if (page.getPageNo() <= 0) {
			page.setPageNo(1);
		}
		int count = dao.getListCount(param);
		param.put("offset", (page.getPageNo() - 1) * page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String, String>> list = null;
		if (count > 0) {
			list = dao.getList(param);
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}
	public ImpInfo getImpInfo(String id) {
		ImpInfo ii=(ImpInfo)CacheUtils.get(CacheUtils.IMPDATA_CACHE, id);
		if (ii==null) {
			ii=super.get(id);
		}
		return ii;
	}
	public ImpInfo get(String id) {
		return super.get(id);
	}

	public List<ImpInfo> findList(ImpInfo impInfo) {
		return super.findList(impInfo);
	}

	public Page<ImpInfo> findPage(Page<ImpInfo> page, ImpInfo impInfo) {
		return super.findPage(page, impInfo);
	}
	@Transactional(readOnly = false)
	public void save(ImpInfo impInfo) {
		super.save(impInfo);
	}

	@Transactional(readOnly = false)
	public void delete(ImpInfo impInfo) {
		ExpType expType = ExpType.getByIdx(impInfo.getImpTpye());
        if(expType == null){
            return;
        }

		super.delete(impInfo);
		if ((ExpType.Stu).equals(expType)) {
			studentErrorDao.deleteByImpId(impInfo.getId());
		}if ((ExpType.ConStu).equals(expType)) {
			studentErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.Tea).equals(expType)) {
			teacherErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.BackUser).equals(expType)) {
			backUserErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.Org).equals(expType)) {
			officeErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.DcProjectClose).equals(expType)) {
			projectErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.MdExpapproval).equals(expType)) {
			proMdApprovalErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.MdExpmid).equals(expType)) {
			proMdMidErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.MdExpclose).equals(expType)) {
			proMdCloseErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.PmProjectHsmid).equals(expType)) {
			projectHsErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.HlwGcontest).equals(expType)) {
			gcontestErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.PmProject).equals(expType)) {
			proModelErrorDao.deleteByImpId(impInfo.getId());
		}else if ((ExpType.PmGcontest).equals(expType)) {
			proModelGcontestErrorDao.deleteByImpId(impInfo.getId());
			pmgMemsErrorDao.deleteByImpId(impInfo.getId());
			pmgTeasErrorDao.deleteByImpId(impInfo.getId());
		}
		impInfoErrmsgDao.deleteByImpId(impInfo.getId());
	}

	/********************************************************************
     * 统一的导入导出页面.
	 * @param impInfo 导入信息页面
	 * @return List
	 */
    public List<ImpInfo> findListByIep(ImpInfo impInfo) {
        return dao.findListByIep(impInfo);
    }

    /**
     * 删除信息.
     * @param impInfo
     * @return
     */
    @Transactional(readOnly = false)
    public boolean deleteByIep(ImpInfo impInfo) {
        if(impInfo == null){
            return false;
        }
        super.delete(impInfo);
        impInfoErrmsgDao.deleteByImpId(impInfo.getId());
        if ((TplType.MR.getKey()).equals(impInfo.getIepType())) {
            proModelErrorDao.deleteByImpId(impInfo.getId());
        }else if ((TplType.GJ.getKey()).equals(impInfo.getIepType())) {
            proModelErrorDao.deleteByImpId(impInfo.getId());
            proModelGcontestErrorDao.deleteByImpId(impInfo.getId());
        }
        return true;
    }

    /**
     * 获取下载成功数据信息.
     * @param impInfo
     */
    public IeDmap downSdataByIep(ImpInfo impInfo) {
        IeDmap dmap = new IeDmap(impInfo);
        //dmap.setEinfos(impInfoErrmsgDao.getListByImpId(impInfo.getId()));
        if ((TplType.MR.getKey()).equals(impInfo.getIepType())) {
            dmap.setEdinfos(proModelErrorDao.getListByImpId(impInfo.getId()));
        }else if ((TplType.GJ.getKey()).equals(impInfo.getIepType())) {
//            List<ProModelMdGc> pmgcs = proModelMdGcService.findList(new ProModelMdGc());
//            List<Map<String, String>> edinfos = Lists.newArrayList();
//            for (ProModelMdGc pmgc : pmgcs) {
//                Map<String, String> cmap = Maps.newHashMap();
//                cmap.put("teachers", pmgc.getTeachers());
//                //TODO CHENHAO
//                edinfos.add(cmap);
//            }
//            dmap.setEdinfos(edinfos);
            dmap.setEdinfos(proModelGcontestErrorDao.getMapByImpId(impInfo.getId()));
        }
        return dmap;
    }

    /**
     * 获取下载错误信息.
     * @param impInfo
     */
    public IeDmap downEdataByIep(ImpInfo impInfo) {
        IeDmap dmap = new IeDmap(impInfo);
        dmap.setEinfos(impInfoErrmsgDao.getListByImpId(impInfo.getId()));
        if ((TplType.MR.getKey()).equals(impInfo.getIepType())) {
            dmap.setEdinfos(proModelErrorDao.getListByImpId(impInfo.getId()));
        }else if ((TplType.GJ.getKey()).equals(impInfo.getIepType())) {
            dmap.setEdinfos(proModelGcontestErrorDao.getMapByImpId(impInfo.getId()));
        }
        return dmap;
    }
}