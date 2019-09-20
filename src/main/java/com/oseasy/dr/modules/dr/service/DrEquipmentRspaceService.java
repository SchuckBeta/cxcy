package com.oseasy.dr.modules.dr.service;

import java.util.*;

import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrEquipmentRspaceDao;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.manager.CardConstants;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.impl.DrCloseEntranceGuardsService;
import com.oseasy.dr.modules.dr.manager.impl.DrHoldOpenEntranceGuardsService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrKey;

import org.springframework.util.CollectionUtils;

/**
 * 门禁设备场地Service.
 * @author chenh
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = true)
public class DrEquipmentRspaceService extends CrudService<DrEquipmentRspaceDao, DrEquipmentRspace> {

	@Autowired
	private DrCloseEntranceGuardsService drCloseEntranceGuardsService;

	@Autowired
	private DrHoldOpenEntranceGuardsService drHoldOpenEntranceGuardsService;

	@Autowired
	private DrEquipmentService drEquipmentService;


	public DrEquipmentRspace get(String id) {
		return super.get(id);
	}

	public DrEquipmentRspace getByRoom(String id) {
	    return dao.getByRoom(id);
	}

	public List<DrEquipmentRspace> findList(DrEquipmentRspace drEquipmentRspace) {
		return super.findList(drEquipmentRspace);
	}

	public List<DrEquipmentRspace> findListByRoom(DrEquipmentRspace drEquipmentRspace) {
	    return dao.findListByRoom(drEquipmentRspace);
	}

	public Page<DrEquipmentRspace> findPage(Page<DrEquipmentRspace> page, DrEquipmentRspace drEquipmentRspace) {
		return super.findPage(page, drEquipmentRspace);
	}

	public Page<DrEquipmentRspace> findPageByRoom(Page<DrEquipmentRspace> page, DrEquipmentRspace drEquipmentRspace) {
	    drEquipmentRspace.setPage(page);
        page.setList(dao.findListByRoom(drEquipmentRspace));
        return page;
	}

	@Transactional(readOnly = false)
	public void save(DrEquipmentRspace drEquipmentRspace) {
		super.save(drEquipmentRspace);
	}

	@Transactional(readOnly = false)
	public void delete(DrEquipmentRspace drEquipmentRspace) {
		super.delete(drEquipmentRspace);
	}

	@Transactional(readOnly = false)
	public void deleteByEtpId(String etpId) {
	    dao.deleteByDrEquipmentId(etpId);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrEquipmentRspace drEquipmentRspace) {
  	  dao.deleteWL(drEquipmentRspace);
  	}

	@Transactional(readOnly = false)
	public JSONObject saveDoorRelation(JSONArray jsData) {
		//删除掉原来的设备，保存新的
		if(jsData.size()>0){
			String drId=(String) jsData.getJSONObject(0).get("drEquipmentId");
			dao.deleteByDrEquipmentId(drId);
			for(int i=0;i<jsData.size();i++){
				JSONObject jsonObject=jsData.getJSONObject(i);
				String drEquipmentId=(String) jsonObject.get("drEquipmentId");
				String rspaceId= (String) jsonObject.get("rspaceId");
				String type= (String) jsonObject.get("type");
				String doorOtherName="";
				String doorPort= (String) jsonObject.get("doorPort");
				if(jsonObject.get("doorOtherName")!=null){
					doorOtherName=(String)jsonObject.get("doorOtherName");
				}else{
					if(StringUtil.isNotEmpty(doorPort)){
						doorOtherName=DrKey.getByKeyStr(doorPort).getName();
			        }
				}
				DrEquipmentRspace drEquipmentRspace =new DrEquipmentRspace();
				drEquipmentRspace.setEpment(new DrEquipment(drEquipmentId));
				drEquipmentRspace.setRspType(type);
				drEquipmentRspace.setRspace(rspaceId);
				drEquipmentRspace.setName(doorOtherName);
				drEquipmentRspace.setDrNo(doorPort);
				save(drEquipmentRspace);
			}
		}
		JSONObject js=new JSONObject();
		js.put("ret","1");
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject deleDoorRelation(String id) {
		delete(new DrEquipmentRspace(id));
		JSONObject js=new JSONObject();
		js.put("ret","1");
		return js;
	}

	public List<Map<String,String>>  getAllRelationListByDrEquipment(String  drEquipmentid) {
		List<DrEquipmentRspace> rdRspaceList=dao.getAllRelationListByDrEquipment(drEquipmentid);
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int j=0;j<rdRspaceList.size();j++){
			DrEquipmentRspace drEquipmentRspace=rdRspaceList.get(j);
			Map<String,String> map=new HashMap<String,String>();
			map.put("drEquipmentId",drEquipmentRspace.getEpment().getId());
			map.put("doorOtherName",StringUtil.isEmpty(drEquipmentRspace.getName())? drEquipmentRspace.getDoorName() : drEquipmentRspace.getName());
			map.put("roomId",drEquipmentRspace.getRspace());
			map.put("doorPort",drEquipmentRspace.getDrNo());
	        if(StringUtil.isNotEmpty(drEquipmentRspace.getDrNo())){
	        	DrKey drKey = DrKey.getByKeyStr(drEquipmentRspace.getDrNo());
	        	map.put("doorName", (drKey == null)? "" : drKey.getName());
	        }
			list.add(map);
		}
		return list;
	}

	@Transactional
	public void updateDoorStatusByEquipmentIdAndDoorNos(Integer dealStatus, String equipmentId, List<String> doorNos) {
		dao.updateDoorStatusByEquipmentIdAndDoorNos(dealStatus, equipmentId, doorNos);
	}

	/**
	 * 校验设备及门编号的合法性，并返回门编号处理后的list
	 * @param epmentid 设备ID(epment表主键)
	 * @param doorNo 门编号
	 * @return 返回门编号处理后的list
	 */
	public List<Integer> checkEntranceGuardDoorCtr(String epmentid, String doorNo) {
		if (StringUtil.isEmpty(epmentid)) {
			throw new RuntimeException("传入设备编号错误！");
		}

		if (StringUtil.isEmpty(doorNo)) {
			throw new RuntimeException("传入设备门编号错误！");
		}

		DrEquipmentRspace drEquipmentRspaceQuery = new DrEquipmentRspace();
		drEquipmentRspaceQuery.setDrNo(doorNo);
		drEquipmentRspaceQuery.setEpment(new DrEquipment(epmentid));
		List<DrEquipmentRspace> drEquipmentRspaceList = dao.findListByRoom(drEquipmentRspaceQuery);
		if (CollectionUtils.isEmpty(drEquipmentRspaceList)) {
			throw new RuntimeException("系统中未查询到当前门数据！");
		}
		List<Integer> doors = new ArrayList<>();
		try {
			if(StringUtil.isNotEmpty(doorNo)){
				doors.add(DrKey.getByKeyStr(doorNo).getIndex());
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("门编号数据类型错误，请联系管理员！");
		}
		return doors;
	}

	/**
	 *开门操作
	 * @param epmentid 设备ID(epment表主键)
	 * @param doorNo 门编号
	 * @return
	 */
	@Transactional
	public ApiTstatus<DrCardRparam> openEntranceGuardDoor(String epmentid, String doorNo) {
		List<Integer> doors = checkEntranceGuardDoorCtr(epmentid, doorNo);
		DrCardParam param = new DrCardParam(epmentid, doors, CardConstants.commandType.DOOR_COMMAND.getType());
		List<String> doorStrList = new ArrayList<>();
		doors.forEach(door-> doorStrList.add((door - 1) + ""));
		dao.updateDoorStatusByEquipmentIdAndDoorNos(DrCdealStatus.DCD_DEALING.getKey(), epmentid, doorStrList);
//		drEquipmentService.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_DEALING.getKey(), Arrays.asList(epmentid));
		return drHoldOpenEntranceGuardsService.runner(param);
	}

	/**
	 *关门操作
	 * @param epmentid 设备ID(epment表主键)
	 * @param doorNo 门编号
	 * @return
	 */
	@Transactional
	public ApiTstatus<DrCardRparam> closeEntranceGuardDoor(String epmentid, String doorNo) {
		List<Integer> doors = checkEntranceGuardDoorCtr(epmentid, doorNo);
		DrCardParam param = new DrCardParam(epmentid, doors, CardConstants.commandType.DOOR_COMMAND.getType());
		drEquipmentService.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_DEALING.getKey(), Arrays.asList(epmentid));
		return drCloseEntranceGuardsService.runner(param);
	}

    /**
     * 批量更新数据处理状态
     * @param ids
     *            授权ID
     * @param dealStatus
     *            状态
     * @return
     */
    @Transactional(readOnly = false)
    public int updateDealStatusByPl(List<String> ids, Integer dealStatus) {
        return dao.updateDealStatusByPl(ids, dealStatus);
    }
}