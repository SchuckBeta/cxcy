/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 多卡多设备参数.
 * @author chenhao
 */
public class DrCardEparam implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final String DR_CARD_EXPIRY = "expiry";
    protected static Logger logger = LoggerFactory.getLogger(DrCardEparam.class);
    private List<DrCardEquipment> list;//

    public List<DrCardEquipment> getList() {
        return list;
    }

    public void setList(List<DrCardEquipment> list) {
        this.list = list;
    }


    /**
     * 处理有效期为指定格式.
     * @param gps JSONObject
     * @param gparam DrCardEparam
     * @return DrCardEparam
     */
    public static DrCardEparam dealDate(JSONObject gps, DrCardEparam gparam) {
        if((gparam != null)){
            String expiry = null;
            try {
                if(StringUtil.checkNotEmpty(gparam.getList())){
                    JSONArray list = gps.getJSONArray(CoreJkey.JK_LIST);
                    for (int i = 0; i < list.size() ; i++) {
                        JSONObject card = list.getJSONObject(i);
                        Object curexpiry = card.get(DR_CARD_EXPIRY);
                        if(curexpiry != null){
                            expiry = card.getString(DR_CARD_EXPIRY);
                        }
                        if(StringUtil.isNotEmpty(expiry)){
                            gparam.getList().get(i).setExpiry(DateUtil.getCurDateYMD999(DateUtil.parseDate(expiry, DateUtil.FMT_YYYYMM_ZG)));
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("expiry = "+ expiry);
            }
        }
        return gparam;
    }
}
