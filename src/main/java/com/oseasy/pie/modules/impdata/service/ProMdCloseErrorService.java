package com.oseasy.pie.modules.impdata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.ProMdCloseErrorDao;
import com.oseasy.pie.modules.impdata.entity.ProMdCloseError;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.proprojectmd.dao.ProModelMdDao;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 民大项目结项导入错误数据Service.
 * @author 9527
 * @version 2017-10-20
 */
@Service
@Transactional(readOnly = true)
public class ProMdCloseErrorService extends CrudService<ProMdCloseErrorDao, ProMdCloseError> {
	@Autowired
	private ProModelDao proModelDao;
	@Autowired
	private ProModelMdDao proModelMdDao;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private ProActTaskService proActTaskService;
	@Autowired
	private TeamUserHistoryService teamUserHistoryService;
	public List<Map<String, String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	@Transactional(readOnly = false)
	public void saveProMdClose(String exc,String result,ProModel pm) {
	    saveProMdClose(exc, result, pm, null);
	}
    @Transactional(readOnly = false)
    public void saveProMdClose(String exc,String result,ProModel pm, ItOper impVo) {
		//团队完成记录
		teamUserHistoryService.updateFinishAsClose(pm.getId());
		if ("0".equals(result)) {//未通过
			actTaskService.suspendProcess(pm.getProcInsId());
			proModelDao.updateResult("2", pm.getId());
		}else if ("1".equals(result)) {//通过
		    proActTaskService.runNextProcess(pm);
			if (StringUtil.isNotEmpty(exc)) {
				proModelDao.updateResult(exc, pm.getId());
			}else{
				proModelDao.updateResult("0", pm.getId());
			}
		}
		/**
		 * 结项不论同步还是不通过，置状态为1.
		 */
        proModelDao.updateState("1",pm.getId());
	    if((impVo != null) && StringUtil.isNotEmpty(impVo.getReqParam().getGnodeId()) && StringUtil.isNotEmpty(pm.getId())){
	        ProModel proModel = proModelDao.get(pm.getId());
	        proModel.setEndGnodeId(impVo.getReqParam().getGnodeId());
	        proModelDao.update(proModel);
	    }
		proModelMdDao.updateCloseResult(result, pm.getId());
	}
	@Transactional(readOnly = false)
	public void insert(ProMdCloseError studentError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			studentError.setUpdateBy(user);
			studentError.setCreateBy(user);
		}
		studentError.setUpdateDate(new Date());
		studentError.setCreateDate(studentError.getUpdateDate());
		dao.insert(studentError);
	}
	public ProMdCloseError get(String id) {
		return super.get(id);
	}

	public List<ProMdCloseError> findList(ProMdCloseError proMdCloseError) {
		return super.findList(proMdCloseError);
	}

	public Page<ProMdCloseError> findPage(Page<ProMdCloseError> page, ProMdCloseError proMdCloseError) {
		return super.findPage(page, proMdCloseError);
	}

	@Transactional(readOnly = false)
	public void save(ProMdCloseError proMdCloseError) {
		super.save(proMdCloseError);
	}

	@Transactional(readOnly = false)
	public void delete(ProMdCloseError proMdCloseError) {
		super.delete(proMdCloseError);
	}

}