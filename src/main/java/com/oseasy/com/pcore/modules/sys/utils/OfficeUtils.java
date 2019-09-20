/**
 *
 */
package com.oseasy.com.pcore.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 工具类


 */
public class OfficeUtils {

	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static String schoolName="schoolName";
	public static Office getTop() {
		Office o = (Office)CacheUtils.get(CoreUtils.CACHE_OFFICE, CoreIds.NCE_SYS_OFFICE_TOP.getId());
		if (o==null) {
			o = officeDao.get(CoreIds.NCE_SYS_OFFICE_TOP.getId());
			if (o==null) {
				return o;
			}
			CacheUtils.put(CoreUtils.CACHE_OFFICE, CoreIds.NCE_SYS_OFFICE_TOP.getId(),o);
		}
		return o;
	}

	/**
	 * 获取租户的顶级机构.
	 * @param tenantId String
	 * @return Office
     */
	public static Office getRootByTenant(String tenantId) {
		if (StringUtil.isNotEmpty(tenantId)) {
			Office entity = new Office();
			entity.setParent(new Office(CoreIds.NCE_SYS_TREE_PROOT.getId()));
			return officeDao.getRoot(entity);
		}
		return null;
	}

	public static Office getOrgByName(String name) {
	    Office o = (Office)CacheUtils.get(CoreUtils.CACHE_OFFICE,name);
	    if (o==null) {
	        o=officeDao.getOrgByName(name);
	        if (o==null) {
	            return o;
	        }
	        CacheUtils.put(CoreUtils.CACHE_OFFICE, name,o);
	    }
	    return o;
	}
	public static Office getSchool() {
		Office o = (Office)CacheUtils.get(CoreUtils.CACHE_OFFICE,schoolName);
		if (o==null) {
			o = officeDao.getSchool();
			if (o==null) {
				return o;
			}
			CacheUtils.put(CoreUtils.CACHE_OFFICE, schoolName,o);
		}
		return o;
	}
	public static Office getOfficeByName(String officename) {
		Office o = (Office)CacheUtils.get(CoreUtils.CACHE_OFFICE,officename);
		if (o==null) {
			o=officeDao.getOfficeByName(officename);
			if (o==null) {
				return o;
			}
			CacheUtils.put(CoreUtils.CACHE_OFFICE, officename,o);
		}
		return o;
	}
	public static Office getProfessionalByName(String pname) {
		Office o=officeDao.getProfessionalByPName(pname);
		return o;
	}
	public static Office getProfessionalByName(String officename, String pname) {
		Office o = (Office)CacheUtils.get(CoreUtils.CACHE_OFFICE, officename+"_"+pname);
		if (o==null) {
			o=officeDao.getProfessionalByName(officename,pname);
			if (o==null) {
				return o;
			}
			CacheUtils.put(CoreUtils.CACHE_OFFICE, officename+"_"+pname,o);
		}
		return o;
	}
	public static void clearCache() {
		CacheUtils.removeAll(CoreUtils.CACHE_OFFICE);
	}
    public static List<Office> getOrganizationOther(List<Office> officeList, String grade){
        List<Office> organizationList = new ArrayList<>();
        for (Office office: officeList){
            if(office.getGrade().equals(grade)){
                organizationList.add(office);
            }
        }
        return organizationList;
    }
}
