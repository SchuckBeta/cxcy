/**
 *
 */
package com.oseasy.com.pcore.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;

import static com.oseasy.com.pcore.modules.sys.utils.CoreUtils.CACHE_OFFICE_LIST;

/**
 * 机构Service

 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {
	@Autowired
	OfficeDao officeDao;
	@Autowired
	CoreService coreService;

	public List<Office> findAll() {
		return CoreUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return CoreUtils.getOfficeAllList();
		}else{
			return CoreUtils.getOfficeList();
		}
	}

	public List<Office> findListFront(Boolean isAll) {
		return CoreUtils.getOfficeListFront();
	}

	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		if (office != null) {
			office.setParentIds(office.getParentIds()+"%");
			return dao.findByParentIdsLike(office);
		}
		return  new ArrayList<Office>();
	}

	@Transactional(readOnly = false)
	public void save(Office office) {
//	    if(office.getIsNewRecord()){
//	        office.setId(IdGen.uuid());
//	    }
		super.save(office);

		JedisUtils.hashDel(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_OFFICE_LIST);
		//JedisUtils.setObject(CoreSval.ck.cks(CoreSval.CoreEmskey.OFFICE, tenant)+office.getId(),office);
		//JedisUtils.delObject(CACHE_OFFICE_LIST+StringUtil.LINE_D+tenant);
//		CoreUtils.removeCache(CoreUtils.CACHE_OFFICE_LIST);
//		CoreUtils.removeCache("officeListFront");
//		OfficeUtils.clearCache();
	}

	@Transactional(readOnly = false)
	public void saveById(Office office) {
		if(office.getId()!=null && "1".equals(office.getId())){
			dao.updateSpace(office);
		}
		save(office);
		JedisUtils.hashDel(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_OFFICE_ALL_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		JedisUtils.hashDel(RedisEnum.OFFICE.getValue()+StringUtil.MAOH+TenantConfig.getCacheTenant(),CoreUtils.CACHE_OFFICE_ALL_LIST);
	}

	@Transactional(readOnly = false)
	public void update(Office office) {
		officeDao.update(office);
		String tenant= TenantConfig.getCacheTenant();
		JedisUtils.delObject(CoreSval.ck.cks(CoreSval.CoreEmskey.OFFICE, tenant)+office.getId());
		JedisUtils.delObject(CACHE_OFFICE_LIST+StringUtil.LINE_D+tenant);

//		CoreUtils.removeCache(CACHE_OFFICE_LIST);
//		CoreUtils.removeCache("officeListFront");
//		OfficeUtils.clearCache();
	}

	public String selelctParentId(String id) {
		return officeDao.selelctParentId(id);
	}

	public Boolean checkByName(String parentId, String name) {
	    return (officeDao.checkNameByParent(parentId, name) == 0) ? false : true;
	}

	public Boolean checkByNameAndId(String id,String parentId, String name) {
		return (officeDao.checkByNameAndId(id,parentId, name) == 0) ? false : true;
	}

	public List<Office> findColleges() {
		return officeDao.findColleges();
	}

	public List<Office> findProfessionals(String parentId) {
		return officeDao.findProfessionals(parentId);
	}

	public List<Office>  findProfessionByParentIdsLike(Office office) {
		return officeDao.findProfessionByParentIdsLike(office);
	}

	public  List<Office> findProfessionByParentIds(String officeIds) {
		return officeDao.findProfessionByParentIds(officeIds);
	}

	public boolean checkIsHasUser(String id) {
		boolean ishas=false;
		List<User> list = coreService.findUserByOfficeId(id);
		if(StringUtil.checkNotEmpty(list)){
			ishas=true;
		}
		return ishas;
	}

	public String selectOfficeIdByParentId(String parentId){

		return officeDao.selectOfficeIdByParentId(parentId);
	}

	/**
	 * 查找租户下的顶级机构(条件：parentId=0).
	 * @return Office
     */
	public Office getRoot(){
		return getRoot(null);
	}
	/**
	 * 查找租户下的顶级机构(条件：parentId=0 AND rid).
	 * @return Office
	 */
	public Office getRoot(String rid){
		Office entity = new Office();
		entity.setParent(new Office(CoreIds.NCE_SYS_TREE_PROOT.getId()));
		if(StringUtil.isNotEmpty(rid)){
			entity.setRid(rid);
		}
		return officeDao.getRoot(entity);
	}
}
