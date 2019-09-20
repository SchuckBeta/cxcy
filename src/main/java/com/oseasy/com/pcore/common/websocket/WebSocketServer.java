package com.oseasy.com.pcore.common.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.util.common.utils.StringUtil;  
/** 
 * 注解的值将被用于监听用户连接的终端访问URL地址 
 */  
@ServerEndpoint(value="/websocket")  
public class WebSocketServer {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static int total=0;
	public static Map<String,String> sessionid_userid=new HashMap<String,String>();
	public static Map<String,Map<String,Session>> userid_session=new HashMap<String,Map<String,Session>>();
    /** 
     * 当服务器接收到客户端发送的消息时所调用的方法 
     */  
    @OnMessage  
    public void onMessage(String message,Session session) throws IOException, InterruptedException {  
    }  
    /** 
     * 当一个新用户连接时所调用的方法 
     */  
    @OnOpen  
    public void onOpen(Session session,EndpointConfig con) {
    	List<String> li=session.getRequestParameterMap().get("userid");
    	if (li!=null&&li.size()>0) {
    		String userid=li.get(0);
    		if (StringUtil.isNotEmpty(userid)) {
    			sessionid_userid.put(session.getId(), userid);
    			Map<String,Session> map= userid_session.get(userid);
    			if (map==null) {
    				map=new HashMap<String,Session>();
    				userid_session.put(userid, map);
    			}
    			map.put(session.getId(), session);
    			total++;
    			logger.info("webSocket total session:"+total);
    		}
    	}
    }  
    /** 当一个用户断开连接时所调用的方法*/  
    @OnClose  
    public void onClose(Session session,CloseReason r) {
    	celarSession(session);
    }
    @OnError
    public void OnError(Session session,Throwable e) {
    	celarSession(session);
    }
    private void celarSession(Session session) {
    	String userid=sessionid_userid.get(session.getId());
    	if (StringUtil.isNotEmpty(userid)) {
    		sessionid_userid.remove(session.getId());
    		Map<String,Session> map= userid_session.get(userid);
    		if (map!=null) {
    			Session s=map.remove(session.getId());
    			if (s!=null) {
    				s=null;
	    			total--;
	    	    	logger.info("webSocket total session:"+total);
    			}
    		}
    	}
    }
}  