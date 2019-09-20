/**
 *
 */
package com.oseasy.auy.common.utils;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.interactive.dao.SysLikesDao;
import com.oseasy.pro.modules.interactive.entity.SysLikes;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户工具类
 */
public class AuyCmsUserUtils {
    public static SysLikesDao sysLikesDao = SpringContextHolder.getBean(SysLikesDao.class);

    /**检查当前用户对foreignId是否点过赞
     * @param foreignId
     * @return
     */
    public static boolean checkIsLikeForUserInfo(String foreignId) {
    	User user=UserUtils.getUser();
    	if (StringUtil.isEmpty(user.getId())) {
    		return true;
    	}
    	if (user.getId().equals(foreignId)) {
    		return true;
    	}
    	SysLikes sc=new SysLikes();
    	sc.setUserId(user.getId());
    	sc.setForeignId(foreignId);
    	if (sysLikesDao.getExistsLike(sc)>0) {
    		return true;
    	}else{
    		return false;
    	}
    }

}
