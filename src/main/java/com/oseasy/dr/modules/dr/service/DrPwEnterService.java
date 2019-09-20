package com.oseasy.dr.modules.dr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrPwEnterDao;
import com.oseasy.dr.modules.dr.vo.PwSpace;
import com.oseasy.dr.modules.dr.vo.PwSpaceDoor;
import com.oseasy.dr.modules.dr.vo.PwSpaceGitem;
import com.oseasy.pw.modules.pw.entity.PwEnter;

/**
 * 入驻申报Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class DrPwEnterService extends CrudService<DrPwEnterDao, PwEnter> {

    /**获取卡授权信息
     * @param cardid
     * @return
     */
    public List<PwSpace> getPwSpaceInfo(String cardid){
        List<PwSpace> pw=dao.getPwSpace();
        if(pw==null||pw.size()==0){
            return pw;
        }
        List<PwSpaceDoor> pd=dao.getPwSpaceDoor(cardid);
        if(pd!=null&&pd.size()>0){
            Map<String,List<PwSpaceDoor>> map=new HashMap<String,List<PwSpaceDoor>>();
            for(PwSpaceDoor p:pd){
                List<PwSpaceDoor> tem=map.get(p.getSpaceId());
                if(tem==null){
                    tem= new ArrayList<PwSpaceDoor>();
                    map.put(p.getSpaceId(), tem);
                }
                tem.add(p);
            }
            for(PwSpace p:pw){
                p.setDoors(map.get(p.getSid()));
            }
        }
        return pw;
    }

    /**
     * 获取预警规则信息.
     * @param gid
     * @return List
     */
    public List<PwSpaceGitem> getPwSpaceGitem(String gid){
        return dao.getPwSpaceGitem(gid);
    }

    /**
     * 获取楼层.
     * @return List
     */
    public List<PwSpace> getPwSpaceList(){
        return dao.getPwSpace();
    }
}
