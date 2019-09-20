package com.oseasy.pro.jobs.pro;

import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: QM
 * @date: 2019/4/20 14:52
 * @description:
 */
public class ProModelListenerTwo implements MessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProModelListenerTwo.class);
	@Resource
	private ProModelService proModelService;
	@Resource
	private RedisTemplate redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();
		LOGGER.info("step2订阅收到消息:");
		ProModel proModel = (ProModel)redisTemplate.getValueSerializer().deserialize(body);
		proModelService.save(proModel);
		LOGGER.info("第二步插入消息");
	}
}
