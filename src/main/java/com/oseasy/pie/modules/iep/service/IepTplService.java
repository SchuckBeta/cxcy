package com.oseasy.pie.modules.iep.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.pie.modules.iep.dao.IepTplDao;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.vo.TplLevel;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 模板导入导出Service.
 * @author chenhao
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class IepTplService extends TreeService<IepTplDao, IepTpl> {

	public IepTpl get(String id) {
		return super.get(id);
	}

	public List<IepTpl> findList(IepTpl entity) {
		if (StringUtil.isNotBlank(entity.getParentIds())){
			entity.setParentIds(","+entity.getParentIds()+",");
		}
		return super.findList(entity);
	}

	public List<IepTpl> findByParentIdsLike(IepTpl entity) {
	    return dao.findByParentIdsLike(entity);
	}

	public List<IepTpl> findTreeById(IepTpl entity) {
	    if(StringUtil.isEmpty(entity.getParentIds())){
	        entity = dao.get(entity);
	    }

	    if(entity == null){
	        return null;
	    }

	    if(StringUtil.isNotEmpty(entity.getParentIds())){
	        entity.setParentIds(entity.getParentIds() + entity.getId() + StringUtil.DOTH);
        }
	    return dao.findTreeById(entity);
	}

	@Transactional(readOnly = false)
	public void save(IepTpl entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<IepTpl> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<IepTpl> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(IepTpl entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(IepTpl entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(IepTpl entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(IepTpl entity) {
  	  dao.deleteWLPL(entity);
  	}

  	 /**
     * 更新Step属性.
     * @param entitys
     */
    @Transactional(readOnly = false)
    public boolean updateSteps(IepTpl entity) {
        entity = get(entity);
        IepTpl centity = null;
        if(StringUtil.isNotEmpty(entity.getType())){
            if((TplLevel.FUN.getKey() == entity.getLevel())){
                centity = get(entity);
            }else if((TplLevel.OPER.getKey() == entity.getLevel())){
                if((entity.getParent() != null) && StringUtil.isNotEmpty(entity.getParent().getId())){
                    return false;
                }
                centity = get(entity.getParent());
            }
        }

        if(centity == null){
            return false;
        }

        List<IepTpl> entitys = findListBySteps(centity);
        if(StringUtil.checkGtOne(entitys)){
            for (IepTpl cur : entitys) {
                //cur.setCurr(0);
                if((centity.getStep() + 1) >= cur.getStep()){
                    cur.setCurr(1);
                }
            }
            updatePL(entitys);
        }
        return true;
    }

    /**
     * 查找当前组的step节点.
     * @param entity IepTpl
     * @return List
     */
    public List<IepTpl> findListBySteps(IepTpl entity) {
        if(StringUtil.isEmpty(entity.getParentIds())){
            entity = dao.get(entity);
        }
        entity.setLevel(TplLevel.FUN.getKey());
        return dao.findListBySteps(entity);
    }
}