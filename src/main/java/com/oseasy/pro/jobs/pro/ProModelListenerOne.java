package com.oseasy.pro.jobs.pro;

import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: QM
 * @date: 2019/4/20 14:47
 * @description: 监听第一步
 */
public class ProModelListenerOne implements MessageListener {

	private static ProModelService proModelService = SpringContextHolder.getBean(ProModelService.class);
	private static final Logger LOGGER = LoggerFactory.getLogger(ProModelListenerOne.class);
//	@Autowired
//	private ProModelService proModelService;
	@Resource
	private RedisTemplate redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();
		LOGGER.info("step1订阅收到消息:");
		ProModel proModel = (ProModel)redisTemplate.getValueSerializer().deserialize(body);
		if (proModel.getIsNewRecord()){
			LOGGER.info("新项目插入:");
			proModel.setIsNewRecord(false);
//			proModelService.savePro(proModel);
			proModelService.saveTopicProModel(proModel);
		}
		else{
			LOGGER.info("未提交项目update:");
			proModelService.save(proModel);
		}
		LOGGER.info("第一步插入消息");
	}
}
