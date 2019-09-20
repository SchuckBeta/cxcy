package com.oseasy.scr.modules.sco.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.scr.modules.sco.dao.ScoAffirmConfDao;
import com.oseasy.scr.modules.sco.dao.ScoAffirmCriterionCouseDao;
import com.oseasy.scr.modules.sco.dao.ScoAffirmCriterionDao;
import com.oseasy.scr.modules.sco.dao.ScoAllotRatioDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirmConf;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分认定配置Service.
 * @author 9527
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoAffirmConfService extends CrudService<ScoAffirmConfDao, ScoAffirmConf> {
	@Autowired
	private ScoAffirmCriterionDao scoAffirmCriterionDao;
	@Autowired
	private ScoAffirmCriterionCouseDao scoAffirmCriterionCouseDao;
	@Autowired
	private ScoAllotRatioDao scoAllotRatioDao;
	public List<Dict> getDictForScoAffirm(Set<String> ids,String type) {
		return dao.getDictForScoAffirm(ids, type);
	}
	public Set<String> getTypeSetData(String item) {
		if (StringUtil.isEmpty(item)) {
			return null;
		}
		List<Map<String, String>> list=dao.getTypeSetData(item);
		if (list!=null&&list.size()>0) {
			Set<String> set =new HashSet<String>();
			for(Map<String, String> m:list) {
				if (m!=null&&StringUtil.isNotEmpty(m.get("type"))) {
					for(String s:m.get("type").split(",")) {
						set.add(s);
					}
				}
			}
			return set;
		}else{
			return null;
		}
	}
	public Set<String> getLevelSetData(String item,String category) {
		if (StringUtil.isEmpty(item)||StringUtil.isEmpty(category)) {
			return null;
		}
		List<Map<String, String>> list=dao.getSetData(item, category);
		if (list!=null&&list.size()>0&&!list.isEmpty()) {
			Set<String> set =new HashSet<String>();
			for(Map<String, String> m:list) {
				if (m!=null&&StringUtil.isNotEmpty(m.get("level"))) {
					for(String s:m.get("level").split(",")) {
						set.add(s);
					}
				}
			}
			return set;
		}else{
			return null;
		}
	}
	public Set<String> getFinalResSetData(String item,String category) {
		if (StringUtil.isEmpty(item)||StringUtil.isEmpty(category)) {
			return null;
		}
		List<Map<String, String>> list=dao.getSetData(item, category);
		if (list!=null&&list.size()>0) {
			Set<String> set =new HashSet<String>();
			for(Map<String, String> m:list) {
				if (m!=null&&StringUtil.isNotEmpty(m.get("final_status"))) {
					for(String s:m.get("final_status").split(",")) {
						set.add(s);
					}
				}
			}
			return set;
		}else{
			return null;
		}
	}
	public Set<String> getSetData(String item,String category) {
		if (StringUtil.isEmpty(item)||StringUtil.isEmpty(category)) {
			return null;
		}
		List<Map<String, String>> list=dao.getSetData(item, category);
		if (list!=null&&list.size()>0) {
			Set<String> set =new HashSet<String>();
			for(Map<String, String> m:list) {
				if (m!=null&&StringUtil.isNotEmpty(m.get("pro_category"))) {
					for(String s:m.get("pro_category").split(",")) {
						set.add(s);
					}
				}
			}
			return set;
		}else{
			return null;
		}
	}		
	@Transactional(readOnly = false)
	public void updateProc(String id,String proc) {
		dao.updateProc(id, proc);
	}
	public int check(String id,String type,String item,String category,String subdivision) {
		return dao.check(id, type, item, category, subdivision);
	}
	public ScoAffirmConf getByItem(String item) {
		List<ScoAffirmConf> ScoAffirmConfList=dao.getByItem(item);
		if (ScoAffirmConfList.size()>0) {
			return ScoAffirmConfList.get(0);
		}
		return null;
	}

	public ScoAffirmConf get(String id) {
			return super.get(id);
		}

	public List<ScoAffirmConf> findList(ScoAffirmConf scoAffirmConf) {
		return super.findList(scoAffirmConf);
	}

	public Page<ScoAffirmConf> findPage(Page<ScoAffirmConf> page, ScoAffirmConf scoAffirmConf) {
		return super.findPage(page, scoAffirmConf);
	}
	public List<ScoAffirmConf> findAll() {
		return dao.findAll();
	}
	@Transactional(readOnly = false)
	public void save(ScoAffirmConf scoAffirmConf) {
		super.save(scoAffirmConf);
	}

	@Transactional(readOnly = false)
	public void delete(ScoAffirmConf scoAffirmConf) {
		super.delete(scoAffirmConf);
		scoAffirmCriterionCouseDao.delByFid(scoAffirmConf.getId());
		scoAffirmCriterionDao.delByConfid(scoAffirmConf.getId());
		scoAllotRatioDao.delByFid(scoAffirmConf.getId());
	}

}