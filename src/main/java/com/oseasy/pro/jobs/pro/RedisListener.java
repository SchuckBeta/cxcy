package com.oseasy.pro.jobs.pro;

import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author: QM
 * @date: 2019/4/8 17:55
 * @description: 订阅
 */
public class RedisListener implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisListener.class);
	@Resource
	private ProModelService proModelService;
	@Resource
	private RedisTemplate redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();
		LOGGER.info("step3订阅收到消息:");
		ProModel proModel = (ProModel)redisTemplate.getValueSerializer().deserialize(body);
		proModelService.save(proModel);
		LOGGER.info("RedisListener:step3订阅收到消息插入成功");
	}
}
