package com.oseasy.com.pcore.common.websocket;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

public class WebSockectUtil {
    private static final String SEND_WS_MSG = "sendWsMsg";
    private static final String SEND_OFFICE_WS_MSG = "sendOfficeWsMsg";
    @SuppressWarnings("rawtypes")
	private static RedisTemplate redisTemplate= SpringContextHolder.getBean(RedisTemplate.class);
	/**推送消息
	 * @param session
	 * @param msg
	 */
	public static void sendMsg(Session session,String msg) {
		session.getAsyncRemote().sendText(msg);
	}
	/**根据userid推送消息
	 * @param userid
	 * @param msg
	 */
	public static void sendMsg(String userid,String msg) {
		if (StringUtil.isNotEmpty(userid)&&StringUtil.isNotEmpty(msg)) {
			Map<String,Session> map=WebSocketServer.userid_session.get(userid);
			if (map!=null) {
				for(String k:map.keySet()) {
					sendMsg(map.get(k), msg);
				}
			}
		}
	}
	/**向渠道sendWsMsg发布消息
	 * @param userid
	 * @param msg
	 */
	public static void pushToRedis(String userid,WsMsg wbmsg) {
		JSONObject msg=JSONObject.fromObject(wbmsg);
		JSONObject js=new JSONObject();
		js.put("userid", userid);
		js.put("msg", msg);
		try {
			redisTemplate.convertAndSend(SEND_WS_MSG, js.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**向渠道sendWsMsg发布消息
	 * @param userid
	 * @param msg
	 */
	public static void pushToRedis(List<String> userids,WsMsg wbmsg) {
		JSONObject msg=JSONObject.fromObject(wbmsg);
		if (userids!=null&&userids.size()>0) {
			JSONObject js=new JSONObject();
			js.put("userid", StringUtils.join(userids.toArray(), ","));
			js.put("msg", msg);
			try {
				redisTemplate.convertAndSend(SEND_WS_MSG, js.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**向渠道sendOfficeWsMsg发布消息
	 * @param officeId
	 * @param msg
	 */
	public static void pushToRedisByOffice(String officeId, WsMsg wbmsg) {
	    JSONObject msg=JSONObject.fromObject(wbmsg);
	    JSONObject js=new JSONObject();
	    js.put("officeId", officeId);
	    js.put("msg", msg);
	    try {
	        redisTemplate.convertAndSend(SEND_OFFICE_WS_MSG, js.toString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**向渠道sendOfficeWsMsg发布消息
	 * @param userid
	 * @param msg
	 */
	public static void pushToRedisByOffice(List<String> officeIds,WsMsg wbmsg) {
	    JSONObject msg=JSONObject.fromObject(wbmsg);
	    if (officeIds!=null&&officeIds.size()>0) {
	        JSONObject js=new JSONObject();
	        js.put("officeIds", StringUtils.join(officeIds.toArray(), ","));
	        js.put("msg", msg);
	        try {
	            redisTemplate.convertAndSend(SEND_OFFICE_WS_MSG, js.toString());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
}
