package com.oseasy.pro.modules.promodel.utils;

import java.util.HashMap;
import java.util.Map;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.gcontest.service.GContestService;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangzheng on 2017/3/18.
 */
public class GcontestUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(GcontestUtils.class);
    private static GContestService gContestService = SpringContextHolder.getBean(GContestService.class);



    //获得专家审核的待办任务个数
    public static long collegeExportCount() {
        Map<String,Object> param =new HashMap<String,Object>();
        User user = UserUtils.getUser();
        param.put("auditId", user.getId());
        if (user.getOffice() == null){
			LOGGER.warn("未找到房间信息");
			throw new NullPointerException("该专家无机构信息");
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) {
            param.put("auditState", "1");
            param.put("collegeId",user.getOffice().getId());
        }else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)) {
            param.put("auditState", "2");
            param.put("collegeId",user.getOffice().getId());
        }else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXZJ)) {
            param.put("auditState", "3");
        }else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) {
            param.put("auditState", "4");
        }else{
            return 0;
        }
        int todoCount=gContestService.todoCount(param);
        int hasdoCount=gContestService.hasdoCount(param);
        return (todoCount-hasdoCount);
    }


    //获得网评审核待办任务个数
    public static long  schoolActAuditList() {
        Map<String,Object> param =new HashMap<String,Object>();
        User user = UserUtils.getUser();
        param.put("auditState", "5");
        int todoCount=gContestService.todoCount(param);
        return todoCount;
    }

    //获得学校路演审核待办任务个数
    public static long  schoolEndAuditList() {
        Map<String,Object> param =new HashMap<String,Object>();
        User user = UserUtils.getUser();
        param.put("auditState", "6");
        int todoCount=gContestService.todoCount(param);
        return todoCount;
    }

}
