/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.util.common.utils.StringUtil;

import Net.PC15.FC8800.Command.Data.CardDetail;

/**
 * 控制器门标识.
 * @author chenhao
 */
public enum DrKey {
    DK_1(0, 1, "0", "1", "1门"),
    DK_2(1, 2, "1", "2", "2门"),
    DK_3(2, 3, "2", "3", "3门"),
    DK_4(3, 4, "3", "4", "4门");

    public static final String DR_KEYS = "drKeys";

    private int key;
    private int index;
    private String keyStr;
    private String idxStr;
    private String name;

    private DrKey(int key, int index, String keyStr, String idxStr, String name) {
        this.key = key;
        this.index = index;
        this.keyStr = keyStr;
        this.idxStr = idxStr;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return DrKey
     */
    public static DrKey getByKey(int key) {
        switch (key) {
        case 0:
            return DK_1;
        case 1:
            return DK_2;
        case 2:
            return DK_3;
        case 3:
            return DK_4;
        default:
            return null;
        }
    }
    public static DrKey getByKeyStr(String keyStr) {
    	switch (keyStr) {
    	case "0":
    		return DK_1;
    	case "1":
    		return DK_2;
    	case "2":
    		return DK_3;
    	case "3":
    		return DK_4;
    	default:
    		return null;
    	}
    }
    public static DrKey getByIdxStr(String idxStr) {
    	switch (idxStr) {
    	case "1":
    		return DK_1;
    	case "2":
    		return DK_2;
    	case "3":
    		return DK_3;
    	case "4":
    		return DK_4;
    	default:
    		return null;
    	}
    }

    /**
     * 更新cardDetail特权信息.
     * @param drCard 卡信息
     * @param cardDetail 卡详情信息
     */
    public static String updateDrCard(DrCard drCard, CardDetail cardDetail) {
        DrKey[] entitys = DrKey.values();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < entitys.length; i++) {
            DrKey entity = entitys[i];
            boolean istrue = cardDetail.GetDoor(entity.getKey());
            if(istrue){
                buffer.append(entity.getKey());
            }
            if(i != 0){
                buffer.append(StringUtil.DOTH);
            }
        }
        return buffer.toString();
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIdxStr() {
		return idxStr;
	}

	public void setIdxStr(String idxStr) {
		this.idxStr = idxStr;
	}

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置门禁授权信息
     * @param drnos
     * @return Boolean
     */
    public static Boolean[] genDrStatus(List<String> drnos) {
    	return genDrStatus(null, drnos);
    }
	public static Boolean[] genDrStatus(Boolean[] drStatus, List<String> drnos) {
    	if(drStatus == null){
    		drStatus = new Boolean[]{false, false, false, false};
    	}
    	for (String drno : drnos) {
    		if((DrKey.DK_1.keyStr).equals(drno)){
    			drStatus[0] = true;
    		}else if((DrKey.DK_2.keyStr).equals(drno)){
    			drStatus[1] = true;
    		}else if((DrKey.DK_3.keyStr).equals(drno)){
    			drStatus[2] = true;
    		}else if((DrKey.DK_4.keyStr).equals(drno)){
    			drStatus[3] = true;
    		}else{
    			System.out.println("drno 未定义！");
    		}
		}
    	return drStatus;
	}

    public static List<Object> getDrKeys(){
        List<Object> list = Lists.newArrayList();
        for (DrKey drKey: DrKey.values()){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("key", drKey.getKey());
            hashMap.put("index", drKey.getIndex());
            hashMap.put("name", drKey.getName());
            list.add(hashMap);
        }
        return list;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"index\":\"" + this.index + "\",\"name\":\"" + this.name + "\"}";
    }
}
