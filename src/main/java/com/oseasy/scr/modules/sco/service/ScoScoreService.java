package com.oseasy.scr.modules.sco.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.scr.modules.sco.dao.ScoScoreDao;
import com.oseasy.scr.modules.sco.entity.ScoScore;
import com.oseasy.scr.modules.sco.vo.ScoScoreVo;
import com.oseasy.scr.modules.sco.vo.ScoType;

/**
 * 学分汇总Service.
 * @author chenh
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoScoreService extends CrudService<ScoScoreDao, ScoScore> {

	public ScoScore get(String id) {
		return super.get(id);
	}

	public List<ScoScore> findList(ScoScore scoScore) {
		return super.findList(scoScore);
	}

	public Page<ScoScore> findPage(Page<ScoScore> page, ScoScore scoScore) {
		return super.findPage(page, scoScore);
	}

	public List<ScoScore> findListGbyUser(ScoScore scoScore) {
	  return dao.findListGbyUser(scoScore);
	}

	public Page<ScoScore> findPageListGbyUser(Page<ScoScore> page, ScoScore scoScore) {
	  scoScore.setPage(page);
    page.setList(findListGbyUser(scoScore));
    return page;
	}

	@Transactional(readOnly = false)
	public void save(ScoScore scoScore) {
		super.save(scoScore);
	}

	@Transactional(readOnly = false)
	public void delete(ScoScore scoScore) {
		super.delete(scoScore);
	}

  @Transactional(readOnly = false)
  public ScoScore add(ScoScoreVo scoScoreVo, ScoType scoType) {
    if (scoType == null) {
      return null;
    }

    super.save(scoScoreVo.calculate(scoType));
    return scoScoreVo.getScoScore();
  }
}