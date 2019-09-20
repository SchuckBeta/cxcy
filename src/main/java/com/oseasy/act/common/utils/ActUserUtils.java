/**
 *
 */
package com.oseasy.act.common.utils;

import java.util.List;

import com.oseasy.act.modules.actyw.dao.ActYwSgtypeDao;
import com.oseasy.act.modules.actyw.entity.ActYwSgtype;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;

/**
 * 用户工具类
 */
public class ActUserUtils {
    public static ActYwSgtypeDao actYwSgtypeDao=SpringContextHolder.getBean(ActYwSgtypeDao.class);

    public static List<ActYwSgtype> getActywStatusList(){
        return actYwSgtypeDao.findList(new ActYwSgtype());
    }

}
