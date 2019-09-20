package com.oseasy.auy.modules.pcore.manager;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.manager.SytFacadeAct;
import com.oseasy.cms.modules.cms.manager.SytFacadeCms;
import com.oseasy.com.pcore.modules.syt.manager.ISytFacade;
import com.oseasy.com.pcore.modules.syt.manager.SytFacadeCore;
import com.oseasy.scr.modules.scr.manager.SytFacadeScr;
import com.oseasy.util.common.utils.PinyinUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 开户流程工具类.
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class OpenUtil {
    private final static Logger logger = LoggerFactory.getLogger(OpenUtil.class);

    /**
     * 初始化租户-运营中心.
     */
    @Transactional(readOnly = false)
    public static boolean openNce(String id) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeCore(id));
        for (ISytFacade facade : facades) {
            facade.initNce();
        }
        return true;
    }

    /**
     * 初始化租户-省.
     */
    @Transactional(readOnly = false)
    public static boolean openNpr(String id) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeCore(id));
        for (ISytFacade facade : facades) {
            facade.initNpr();
        }
        return true;
    }

    /**
     * 初始化租户-学校.
     */
    public static boolean openNsc(String id) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeCore(id));
        facades.add(new SytFacadeCms(id));
        facades.add(new SytFacadeAct(id));
        //facades.add(new SytFacadeScr(id));
        for (ISytFacade facade : facades) {
            facade.initNsc();
        }
        return true;
    }


    /**
     * 重置租户模板-运营中心.
     */
    @Transactional(readOnly = false)
    public static boolean resetTNce(String id) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeCore(id));
        for (ISytFacade facade : facades) {
            facade.resetTNce();
        }
        return true;
    }

    /**
     * 重置租户模板-省.
     */
    @Transactional(readOnly = false)
    public static boolean resetTNpr(String id) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeCore(id));
        for (ISytFacade facade : facades) {
            facade.resetTNpr();
        }
        return true;
    }

    /**
     * 重置租户模板-学校.
     */
    @Transactional(readOnly = false)
    public static boolean resetTNsc(String id) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeCore(id));
        facades.add(new SytFacadeCms(id));
        facades.add(new SytFacadeAct(id));
        //facades.add(new SytFacadeScr(id));
        for (ISytFacade facade : facades) {
            facade.resetTNsc();
        }
        return true;
    }


    /**
     * 推送-运营流程.
     * @param targetId 目标租户ID
     * @param id 当前租户ID
     * @param groupId 推送流程ID
     * @return boolean
     */
    @Transactional(readOnly = false)
    public static boolean pushNce(String targetId, String id, String groupId) {
        return true;
    }

    /**
     * 推送-省流程.
     * @param targetId 目标租户ID
     * @param id 当前租户ID
     * @param groupId 推送流程ID
     * @return boolean
     */
    @Transactional(readOnly = false)
    public static boolean pushNpr(String targetId, String id, String groupId) {
        return true;
    }

    /**
     * 推送-校流程.
     * @param targetId 目标租户ID
     * @param id 当前租户ID
     * @param groupId 推送流程ID
     * @return boolean
     */
    @Transactional(readOnly = false)
    public static boolean pushNsc(String targetId, String id, String groupId, String scid) {
        List<ISytFacade> facades = Lists.newArrayList();
        facades.add(new SytFacadeAct(id));
        for (ISytFacade facade : facades) {
            facade.pushNsc(targetId, groupId, scid);
        }
        return true;
    }

    /**
     * 转换参数.
     * @return boolean
     */
    public static String[] convert(String[] srcs, Map<String, String> param) {
        String[] target = new String[srcs.length];
//        for (int i = 0; i < srcs.length; i++) {
//            target[i] = srcs[i].replace(param.[i]);
//        }
        return target;
    }
}
