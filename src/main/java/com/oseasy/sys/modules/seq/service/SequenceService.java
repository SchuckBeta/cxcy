package com.oseasy.sys.modules.seq.service;

import java.util.List;

import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.sys.modules.seq.dao.SequenceDao;
import com.oseasy.sys.modules.seq.entity.Sequence;

/**
 * 序列表Service.
 * @author zy
 * @version 2018-10-08
 */
@Service
@Transactional(readOnly = true)
public class SequenceService extends CrudService<SequenceDao, Sequence> {

	private String startNum="100000";

	private String increment="1";

	private String teamSeq="teamSeq";

	private String projectSeq="projectSeq";

	private String dictSeq="dictSeq";

	private String gcontestSeq="gcontestSeq";

	public Sequence get(String id) {
		return super.get(id);
	}

	public List<Sequence> findList(Sequence entity) {
		return super.findList(entity);
	}

	public Page<Sequence> findPage(Page<Sequence> page, Sequence entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(Sequence entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<Sequence> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<Sequence> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(Sequence entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(Sequence entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(Sequence entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(Sequence entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	@Transactional(readOnly = false)
	public String nextSequence(String name) {
		return dao.nextSequence(name);
	}
	@Transactional(readOnly = false)
	public String getTeamNextSequence() {
		String number=nextSequence(teamSeq);
		if(Const.NO.equals(number)){
			Sequence sequence =new Sequence();
			sequence.setName(teamSeq);
			//递增值
			sequence.setIncrement(increment);
			//团队起始值
			sequence.setCurrentValue(startNum);
			number=startNum;
			save(sequence);
		}
		StringBuffer res = new StringBuffer("");
		res.append(DateUtil.getDate("yyyyMMddHHmmss")).append(number);
		return res.toString();
	}

	@Transactional(readOnly = false)
	public String getProjectNextSequence() {
		String number=nextSequence(projectSeq);
		if(Const.NO.equals(number)){
			Sequence sequence =new Sequence();
			sequence.setName(projectSeq);
			//递增值
			sequence.setIncrement(increment);
			//团队起始值
			sequence.setCurrentValue(startNum);
			number=startNum;
			save(sequence);
		}
		StringBuffer res = new StringBuffer("");
		res.append(DateUtil.getDate("yyyyMMddHHmmss")).append(number);
		return res.toString();
	}

	@Transactional(readOnly = false)
	public String getGcontestNextSequence() {
		String number=nextSequence(gcontestSeq);
		if(Const.NO.equals(number)){
			Sequence sequence =new Sequence();
			sequence.setName(gcontestSeq);
			//递增值
			sequence.setIncrement(increment);
			//团队起始值
			sequence.setCurrentValue(startNum);
			number=startNum;
			save(sequence);
		}
		StringBuffer res = new StringBuffer("");
		res.append(DateUtil.getDate("yyyyMMddHHmmss")).append(number);
		return res.toString();
	}

	@Transactional(readOnly = false)
	public String getDictNextSequence() {
		String number=nextSequence(dictSeq);
		if(Const.NO.equals(number)){
			Sequence sequence =new Sequence();
			sequence.setName(dictSeq);
			//递增值
			sequence.setIncrement(increment);
			//团队起始值
			sequence.setCurrentValue(startNum);
			number=startNum;
			save(sequence);
		}
		StringBuffer res = new StringBuffer("");
		res.append(DateUtil.getDate("yyyyMMddHHmmss")).append(number);
		return res.toString();
	}
}