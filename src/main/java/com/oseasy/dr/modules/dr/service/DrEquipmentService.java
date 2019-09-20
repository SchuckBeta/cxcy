package com.oseasy.dr.modules.dr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrEquipmentDao;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.manager.CardConstants;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.impl.DrCloseEntranceGuardsService;
import com.oseasy.dr.modules.dr.manager.impl.DrEptConnectService;
import com.oseasy.dr.modules.dr.manager.impl.DrHoldOpenEntranceGuardsService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrKey;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 门禁设备Service.
 * @author chenh
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = true)
public class DrEquipmentService extends CrudService<DrEquipmentDao, DrEquipment> {
	@Autowired
	DrEptConnectService drEptConnectService;

	@Autowired
	private DrEquipmentRspaceService drEquipmentRspaceService;

	@Autowired
	private DrCloseEntranceGuardsService drCloseEntranceGuardsService;

	@Autowired
	private DrHoldOpenEntranceGuardsService drHoldOpenEntranceGuardsService;

	//门编号List
	private List<Integer> doors = new ArrayList<Integer>(){{
		add(1);
		add(2);
		add(3);
		add(4);
	}};

	public DrEquipment get(String id) {
		return super.get(id);
	}

	/**
	 * 根据编号更新索引值.
	 *
	 * @param no
	 */
	@Transactional(readOnly = false)
	public void getTindexByNo(String no) {
		dao.getTindexByNo(no);
	}

	public List<DrEquipment> findList(DrEquipment drEquipment) {
		return super.findList(drEquipment);
	}

	public Page<DrEquipment> findPage(Page<DrEquipment> page, DrEquipment drEquipment) {
		return super.findPage(page, drEquipment);
	}

	@Transactional(readOnly = false)
	public void save(DrEquipment drEquipment) {
		super.save(drEquipment);
	}

	/**
	 * 根据编号更新索引值.
	 *
	 * @param no
	 */
	@Transactional(readOnly = false)
	public void updateTindexByNo(String no, int tindex) {
		dao.updateTindexByNo(no, tindex);
	}

	@Transactional(readOnly = false)
	public void updateDelFlag(DrEquipment drEquipment) {
		dao.updateDelFlag(drEquipment);
	}

	@Transactional(readOnly = false)
	public void delete(DrEquipment drEquipment) {
	    super.delete(drEquipment);
	}

	@Transactional(readOnly = false)
	public void deleteAll(DrEquipment drEquipment) {
	    drEquipment.setRemoveAll(true);
	    if(drEquipment.getRemoveAll()){
            drEquipmentRspaceService.deleteByEtpId(drEquipment.getId());
        }
	    super.delete(drEquipment);
	}

	@Transactional(readOnly = false)
	public void deleteWL(DrEquipment drEquipment) {
		dao.deleteWL(drEquipment);
	}

	/**
	 * 检查设备是否存在.
	 *
	 * @param equipmentId
	 * @return ActYwRstatus
	 */
	public ApiTstatus<DrEquipment> check(String equipmentId) {
		DrEquipment drEquipment = dao.get(equipmentId);
		if ((drEquipment == null) || StringUtil.isEmpty(drEquipment.getDrNo())) {
			return new ApiTstatus<DrEquipment>(false, "设备不存在", new DrEquipment(equipmentId));
		}
		return new ApiTstatus<DrEquipment>(true, "成功", drEquipment);
	}

	public JSONObject saveDrEquipment(DrEquipment drEquipment) {
		JSONObject js = new JSONObject();

		Boolean isHasName = checkName(drEquipment);
		if (isHasName) {
			js.put("ret", "0");
			js.put("msg", "已存在相同名字设备");
			return js;
		} else {
			Boolean isIpAndPort = checkIpAndPort(drEquipment);
			if (isIpAndPort) {
				js.put("ret", "0");
				js.put("msg", "已存在相同ip与端口");
				return js;
			} else {
				super.save(drEquipment);
				js.put("ret", "1");
				js.put("msg", "保存成功");
				return js;
			}
		}

		//return js;
	}

	private Boolean checkIpAndPort(DrEquipment drEquipment) {
		Boolean isIpAndPort = false;
		List<DrEquipment> list = dao.getListByDrEquipmentIpPort(drEquipment);
		if (StringUtil.checkNotEmpty(list)) {
			isIpAndPort = true;
		}
		return isIpAndPort;
	}

	private Boolean checkName(DrEquipment drEquipment) {
		Boolean isHasName = false;
		List<DrEquipment> list = dao.getListByDrEquipmentNo(drEquipment);
		if (StringUtil.checkNotEmpty(list)) {
			isHasName = true;
		}
		return isHasName;
	}

	public JSONObject ajaxCheck(DrEquipment drEquipment) {
		JSONObject js = new JSONObject();
		Boolean isHasName = checkName(drEquipment);
		if (isHasName) {
			js.put("ret", "0");
			js.put("msg", "已存在相同名字设备");
			return js;
		} else {
			Boolean isIpAndPort = checkIpAndPort(drEquipment);
			if (isIpAndPort) {
				js.put("ret", "0");
				js.put("msg", "已存在相同ip与端口");
				return js;
			} else {
				js.put("ret", "1");
				return js;
			}
		}
	}

	public List<Map<String, String>> getAllList(DrEquipment DrEquipment) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		DrEquipment drEquipment = dao.get(DrEquipment.getId());
		List<String> drno = drEquipment.getDrNoList();
		for (int j = 0; j < drno.size(); j++) {
			String doorPort = drno.get(j);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", drEquipment.getId());
			map.put("no", drEquipment.getNo());
			map.put("name", drEquipment.getName());
			map.put("ip", drEquipment.getIp());
			map.put("doorPort", doorPort);
	        if(StringUtil.isNotEmpty(doorPort)){
	        	DrKey drKey = DrKey.getByKeyStr(doorPort);
	        	map.put("doorName", (drKey == null)? "" : drKey.getName());
	        }
			list.add(map);
		}
		return list;
	}

	@Transactional(readOnly = false)
	public ApiTstatus<DrEquipment> ajaxCheckConnect(DrEquipment equipment) {
		String prefix = "测试连接";
		//检查参数
		if ((equipment == null) || StringUtil.isEmpty(equipment.getId())) {
			return new ApiTstatus<DrEquipment>(false, prefix + "失败,设备ID参数不存在");
		}

		equipment.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
		save(equipment);

		DrCardParam param = new DrCardParam(null, equipment.getId());
		ApiTstatus<DrCardRparam> rstatus = drEptConnectService.runner(param);
		if (!rstatus.getStatus()) {
			equipment.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
			save(equipment);
			return new ApiTstatus<DrEquipment>(false, prefix + "失败", equipment);
		}
		return new ApiTstatus<DrEquipment>(true, prefix + "成功", equipment);
	}


	@Transactional
	public void updateBatchEquipmentStatusByIds(Integer status, List<String> ids) {
		dao.updateBatchEquipmentStatusByIds(status, ids);
	}

	/**
	 * 打开所有门
	 */
	@Transactional
	public void openEntranceGuards() {

		//获取所有门禁设备
		List<DrEquipment> drEquipments = dao.findAllList();
		List<String> ids = new ArrayList<>();

		if (!CollectionUtils.isEmpty(drEquipments)) {
			drEquipments.forEach(drEquipment -> {
				ids.add(drEquipment.getId());
				drHoldOpenEntranceGuardsService.runner(new DrCardParam(drEquipment.getId(), doors, CardConstants.commandType.DEVICE_COMMAND.getType()));
			});
			dao.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_DEALING.getKey(), ids);
		}
	}

	/**
	 * 关闭所有门
	 */
	@Transactional
	public void closeEntranceGuards() {

		//获取所有门禁设备
		List<DrEquipment> drEquipments = dao.findAllList();

		List<String> ids = new ArrayList<>();
		if (!CollectionUtils.isEmpty(drEquipments)) {

			drEquipments.forEach(drEquipment -> {
				ids.add(drEquipment.getId());
				DrCardParam param = new DrCardParam(drEquipment.getId(), doors, CardConstants.commandType.DEVICE_COMMAND.getType());
				drCloseEntranceGuardsService.runner(param);
			});
			dao.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_DEALING.getKey(), ids);
		}
	}


	/**
	 * 获取当前门禁下的所有门编号
	 * @param drEquipment 设备对象
	 * @return  当前门禁下的所有门编号
	 */
	private List<Integer> getEntranceGuardDoors(DrEquipment drEquipment) {
		List<Integer> doors = new ArrayList<>();
		drEquipmentRspaceService.findList(new DrEquipmentRspace(drEquipment)).forEach(drEquipmentRspace -> {
			try {
				if(StringUtil.isNotEmpty(drEquipmentRspace.getDrNo())){
					doors.add(DrKey.getByKeyStr(drEquipmentRspace.getDrNo()).getIndex());
				}
			} catch (NumberFormatException e) {
				throw new NumberFormatException("门编号应为数字，数据错误！");
			}

		});
		return doors;
	}
}