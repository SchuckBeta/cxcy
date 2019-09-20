package com.oseasy.dr.jobs.dr;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.modules.sys.service.SysService;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.service.DrEquipmentRspaceService;
import com.oseasy.dr.modules.dr.service.DrEquipmentService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("drCardCvoExpireJob")
public class DrCardCvoExpireJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(DrCardCvoExpireJob.class);

    @Autowired
    private SysService sysService;
    @Autowired
    private DrEquipmentService drEquipmentService;
    @Autowired
    private DrEquipmentRspaceService drEquipmentRspaceService;

    public void doWork() {
        try {
            /**
             * 处理开卡、挂失、退卡、重新开卡操作处理中过期数据.
             */
            List<String> cids = DrUtils.checkCvos(sysService.getDbCurLong());
            if (StringUtil.checkNotEmpty(cids)) {
                logger.error("处理门禁卡事件过期，数量：" + cids.size() + "->卡ID分别为：" + JsonMapper.toJsonString(cids));
            }

            /**
             * 处理设备开门、关门、测试连接操作处理中过期数据.
             */
            DrEquipment drEquipment = new DrEquipment();
            drEquipment.setDealStatus(DrCdealStatus.DCD_DEALING.getKey());
            drEquipment.setDelFlag(Const.NO);
            List<DrEquipment> drEquipments = drEquipmentService.findList(drEquipment);
            if (StringUtil.checkNotEmpty(drEquipments)) {
                //处理设备表和设备房间表授权信息
                List<String> drEptids = StringUtil.listIdToList(drEquipments);
                drEquipmentService.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_FAIL.getKey(), drEptids);
                logger.error("处理设备操作过期，数量：" + drEptids.size() + "->设备ID分别为：" + JsonMapper.toJsonString(drEptids));
            }

            /**
             * 处理设备授权操作处理中过期数据.
             */
            DrEquipmentRspace drEquipmentRspace = new DrEquipmentRspace();
            drEquipmentRspace.setDealStatus(DrCdealStatus.DCD_DEALING.getKey() + "");
            List<DrEquipmentRspace> drEquipmentRspaces = drEquipmentRspaceService.findList(drEquipmentRspace);
            if (StringUtil.checkNotEmpty(drEquipmentRspaces)) {
                //处理设备表和设备房间表授权信息
                List<String> drEptRids = StringUtil.listIdToList(drEquipmentRspaces);
                drEquipmentRspaceService.updateDealStatusByPl(drEptRids, DrCdealStatus.DCD_FAIL.getKey());
                logger.error("处理设备授权操作过期，数量：" + drEptRids.size() + "->设备ID分别为：" + JsonMapper.toJsonString(drEptRids));
            }
        } catch (Exception e) {
            logger.error("处理门禁卡事件出错:" + ExceptionUtil.getStackTrace(e));
        }
    }


}
