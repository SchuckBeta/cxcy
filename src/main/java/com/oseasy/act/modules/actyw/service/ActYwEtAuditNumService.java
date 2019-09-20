package com.oseasy.act.modules.actyw.service;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.LotteryUtil;
import com.oseasy.util.common.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwEtAuditNumDao;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAuditNum;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;

/**
 * 指派专家组的项目Service.
 * @author zy
 * @version 2019-01-23
 */
@Service
@Transactional(readOnly = true)
public class ActYwEtAuditNumService extends CrudService<ActYwEtAuditNumDao, ActYwEtAuditNum> {
	@Autowired
	private ActYwEtAuditNumDao actYwEtAuditNumDao;

	public ActYwEtAuditNum get(String id) {
		return super.get(id);
	}

	public List<ActYwEtAuditNum> findList(ActYwEtAuditNum entity) {
		return super.findList(entity);
	}

	public Page<ActYwEtAuditNum> findPage(Page<ActYwEtAuditNum> page, ActYwEtAuditNum entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ActYwEtAuditNum entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ActYwEtAuditNum> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ActYwEtAuditNum> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ActYwEtAuditNum entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ActYwEtAuditNum entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwEtAuditNum entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ActYwEtAuditNum entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	@Transactional(readOnly = false)
	public  ArrayList getArrayList(ActYwEtAssignRule actYwEtAssignRule, int gradeSpeEachTotal, int gradeCancelTotal, ArrayList takeSpecs, String specId) {
        ArrayList result= Lists.newArrayList();
        Boolean vali=true;
        int step=0;
        while(vali){
            if (step>=100){ //防止没有分配到专家导致死循环。
                vali=false;
                break;
            }
            step++;
			ArrayList newResult=  LotteryUtil.getListByShuffle(takeSpecs,gradeSpeEachTotal);//随机分配专家
            String mkey= LotteryUtil.setKey(newResult);
			ActYwEtAuditNum actYwEtAuditNum=new ActYwEtAuditNum();
			actYwEtAuditNum.setActywId(actYwEtAssignRule.getActywId());
			actYwEtAuditNum.setGnodeId(actYwEtAssignRule.getGnodeId());
			actYwEtAuditNum.setExperts(mkey);
            List<ActYwEtAuditNum> list = actYwEtAuditNumDao.getListByRule(actYwEtAuditNum);
					//map.get(mkey) != null ? map.get(mkey) : Lists.newArrayList();
            //gradeCancelTotal=-1 表示 未设置限制
            if (gradeCancelTotal==-1 || StringUtil.checkEmpty(list) || list.size() < gradeCancelTotal-1) {//分配的专家如果同时审核gradeCancelTotal个项目，作废，重新洗牌抽取
//                list.add(specId);//项目集合
//                map.put(mkey, list);
				//清除指派中专家项目计数
				ActYwEtAuditNum oldActYwEtAuditNum=new ActYwEtAuditNum();
				oldActYwEtAuditNum.setActywId(actYwEtAssignRule.getActywId());
				oldActYwEtAuditNum.setGnodeId(actYwEtAssignRule.getGnodeId());
				oldActYwEtAuditNum.setProId(specId);
				deleteByProId(oldActYwEtAuditNum);
				//添加新的记录
				actYwEtAuditNum.setProId(specId);
				actYwEtAuditNum.setId(IdGen.uuid());
				actYwEtAuditNumDao.insert(actYwEtAuditNum);
				result=newResult;
                System.out.printf("项目%s 分配的【有效】专家是 ,%s \n",specId,result.toString());
                //汇总抽取的结果，方便后面验证结果是否正确
                vali=false;
                break;
            }
        }//end while
        return result;
    }
	@Transactional(readOnly = false)
	public void deleteByProId(ActYwEtAuditNum actYwEtAuditNum) {
		dao.deleteByProId(actYwEtAuditNum);
//		ActYwEtAuditNum actYwEtAuditNumindex=dao.getByProId(actYwEtAuditNum);
//		if(actYwEtAuditNumindex!=null) {
//			dao.delete(actYwEtAuditNumindex);
//		}
	}
}