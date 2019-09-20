/**
 *
 */
package com.oseasy.pw.modules.pw.service;

import java.util.List;

import com.oseasy.com.pcore.modules.sys.dao.DictDao;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 字典Service
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class PwDictService {
	private static Logger logger = LoggerFactory.getLogger(PwDictService.class);

    @Autowired
    DictDao dictDao;
    @Autowired
    PwRoomService pwRoomService;

	@Transactional(readOnly = false)
    public JSONObject delDictType(String id) {
        JSONObject js=new JSONObject();
        js.put("ret", "1");
        js.put("msg", "删除成功");
        if (StringUtil.isEmpty(id)) {
            js.put("ret", "0");
            js.put("msg", "参数错误，没有id");
            return js;
        }
        Dict d=dictDao.get(id);
        if (d==null||"1".equals(d.getDelFlag())) {
            js.put("ret", "0");
            js.put("msg", "该字典类型已被删除");
            return js;
        }
        if ("1".equals(d.getIsSys())) {
            js.put("ret", "0");
            js.put("msg", "该字典类型属于系统数据，不能被删除");
            return js;
        }
        PwRoom pwRoom = new PwRoom();
        pwRoom.setType(dictDao.get(id).getValue());
        List<PwRoom> list = pwRoomService.findList(pwRoom);
        if(null != list && list.size() > 0 ){
            js.put("ret", "0");
            js.put("msg", "该字典类型已经被使用，不能被删除");
            return js;
        }
        try {
            dictDao.delDictType(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            js.put("ret", "0");
            js.put("msg", "系统异常");
            return js;
        }
        CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
        return js;
    }
}
