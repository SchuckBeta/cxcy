package com.oseasy.com.rediserver.common.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.oseasy.com.pcore.common.websocket.WebSockectUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;
@Service
public class RedisMessageListener implements MessageListener {  
  
    @SuppressWarnings("rawtypes")
	@Autowired  
    private RedisTemplate redisTemplate;
	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();//请使用valueSerializer  
        //请参考配置文件，本例中key，value的序列化方式均为string。  
        //其中key必须为stringSerializer。和redisTemplate.convertAndSend对应  
        String itemValue = (String)redisTemplate.getValueSerializer().deserialize(body);  
        JSONObject info=JSONObject.fromObject(itemValue);
        String userids=info.getString("userid");
        String msg=info.getString("msg");
        if (StringUtil.isNotEmpty(userids)&&StringUtil.isNotEmpty(msg)) {
        	for(String userid:userids.split(",")) {
        		WebSockectUtil.sendMsg(userid, msg);
        	}
        }
	} 
}
