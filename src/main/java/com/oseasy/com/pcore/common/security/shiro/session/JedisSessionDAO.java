/**
 * 
 */
package com.oseasy.com.pcore.common.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.Servlets;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义授权会话管理类

 * @version 2014-7-20
 */
public class JedisSessionDAO extends AbstractSessionDAO implements SessionDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SessionIdName="initiate.session.id_"+CoreSval.getConfig("filter.frontOrBackground");
	public static final String FrontsessionKeyFix=CoreSval.getConfig("redis.keyPrefix")+"_session_f_";
	public static final String AdminsessionKeyFix=CoreSval.getConfig("redis.keyPrefix")+"_session_a_";
	//private String getSessionKeyPrefix() = "shiro_session_";

	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {  
            return;
        }
		
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不更新SESSION
			if (Servlets.isStaticFile(uri)) {
				return;
			}
			// 如果是视图文件，则不更新SESSION
			if (StringUtil.startsWith(uri, CoreSval.getConfig("web.view.prefix"))
					&& StringUtil.endsWith(uri, CoreSval.getConfig("web.view.suffix"))) {
				return;
			}
			// 手动控制不更新SESSION
			if (Const.NO.equals(request.getParameter("updateSession"))) {
				return;
			}
		}
		
		try {
			
			
			// 获取登录者编号
			PrincipalCollection pc = (PrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			String principalId = pc != null ? pc.getPrimaryPrincipal().toString() : StringUtil.EMPTY;
			
			JedisUtils.hset(getSessionKeyPrefix(), session.getId().toString(), principalId + "|" + session.getTimeout() + "|" + session.getLastAccessTime().getTime());
			JedisUtils.set(JedisUtils.getBytesKey(getSessionKeyPrefix() + session.getId()), JedisUtils.toBytes(session));
			
			// 设置超期时间
			int timeoutSeconds = (int)(session.getTimeout() / 1000);
			JedisUtils.expire((getSessionKeyPrefix() + session.getId()), timeoutSeconds);

			logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			logger.error("update {} {}", session.getId(), request != null ? request.getRequestURI() : "", e);
		}
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}
		
		try {
			
			JedisUtils.hdel(JedisUtils.getBytesKey(getSessionKeyPrefix()), JedisUtils.getBytesKey(session.getId().toString()));
			JedisUtils.del(JedisUtils.getBytesKey(getSessionKeyPrefix() + session.getId()));

			logger.debug("delete {} ", session.getId());
		} catch (Exception e) {
			logger.error("delete {} ", session.getId(), e);
		}
	}
	
	@Override
	public Collection<Session> getActiveSessions() {
		return getActiveSessions(true);
	}
	
	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return getActiveSessions(includeLeave, null, null);
	}
	
	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		/*修改代码，前后台合并*/
		Set<Session> fsessions = getActiveSessions(includeLeave, principal, filterSession, FrontsessionKeyFix);
		Set<Session> asessions = getActiveSessions(includeLeave, principal, filterSession, AdminsessionKeyFix);
		fsessions.addAll(asessions);
		return fsessions;
		/*修改代码，前后台合并*/
	}
	/*新加代码，前后台合并*/
	private Set<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession,String sessionKeyFix) {
		Set<Session> sessions = Sets.newHashSet();
		
		try {
			Map<String, String> map = JedisUtils.hgetAll(sessionKeyFix);
			for (Map.Entry<String, String> e : map.entrySet()) {
				if (StringUtil.isNotBlank(e.getKey()) && StringUtil.isNotBlank(e.getValue())) {
					
					String[] ss = StringUtil.split(e.getValue(), "|");
					if (ss != null && ss.length == 3) {
						SimpleSession session = new SimpleSession();
						session.setId(e.getKey());
						session.setAttribute("principalId", ss[0]);
						session.setTimeout(Long.valueOf(ss[1]));
						session.setLastAccessTime(new Date(Long.valueOf(ss[2])));
						try{
							// 验证SESSION
							session.validate();
							
							boolean isActiveSession = false;
							// 不包括离线并符合最后访问时间小于等于3分钟条件。
							if (includeLeave || DateUtil.pastMinutes(session.getLastAccessTime()) <= 3) {
								isActiveSession = true;
							}
							// 符合登陆者条件。
							if (principal != null) {
								PrincipalCollection pc = (PrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
								if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtil.EMPTY)) {
									isActiveSession = true;
								}
							}
							// 过滤掉的SESSION
							if (filterSession != null && filterSession.getId().equals(session.getId())) {
								isActiveSession = false;
							}
							if (isActiveSession) {
								sessions.add(session);
							}
							
						}
						// SESSION验证失败
						catch (Exception e2) {
							JedisUtils.hdel(sessionKeyFix, e.getKey());
						}
					}
					// 存储的SESSION不符合规则
					else{
						JedisUtils.hdel(sessionKeyFix, e.getKey());
					}
				}
				// 存储的SESSION无Value
				else if (StringUtil.isNotBlank(e.getKey())) {
					JedisUtils.hdel(sessionKeyFix, e.getKey());
				}
			}
			logger.info("getActiveSessions size: {} ", sessions.size());
		} catch (Exception e) {
			logger.error("getActiveSessions", e);
		}
		return sessions;
	}
	@Override
	protected Serializable doCreate(Session session) {
		HttpServletRequest request = Servlets.getRequest();
		String oldsessionId=null;
		if (request != null) {
			/*新加代码，前后台合并*/
			Cookie[] cs=request.getCookies();
			if(cs!=null){
				for(Cookie c:cs){
					if(c!=null&&SessionIdName.equals(c.getName())){
						oldsessionId=c.getValue();
						break;
					}
				}
			}
			/*新加代码，前后台合并*/
			String uri = request.getServletPath();
			// 如果是静态文件，则不创建SESSION
			if (Servlets.isStaticFile(uri)) {
		        return null;
			}
		}
		Serializable sessionId = oldsessionId==null?this.generateSessionId(session):oldsessionId;
		this.assignSessionId(session, sessionId);
		this.update(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {

		Session s = null;
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不获取SESSION
			if (Servlets.isStaticFile(uri)) {
				return null;
			}
			s = (Session)request.getAttribute("session_"+sessionId);
		}
		if (s != null) {
			return s;
		}

		Session session = null;
		try {
//			if (jedis.exists(getSessionKeyPrefix() + sessionId)) {
				session = (Session)JedisUtils.toObject(JedisUtils.get(
						JedisUtils.getBytesKey(getSessionKeyPrefix() + sessionId)));
//			}
			logger.debug("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			logger.error("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "", e);
		}
		
		if (request != null && session != null) {
			request.setAttribute("session_"+sessionId, session);
		}
		
		return session;
	}
	
	@Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
    	try{
        	return super.readSession(sessionId);
    	}catch (UnknownSessionException e) {
			return null;
		}
    }

//	public String getSessionKeyPrefix() {
//		return getSessionKeyPrefix();
//	}
//
//	public void setSessionKeyPrefix(String getSessionKeyPrefix()) {
//		this.getSessionKeyPrefix() = getSessionKeyPrefix();
//	}
	
	private String getSessionKeyPrefix(){
		//先从Request中根据url区分前后台
		HttpServletRequest re= Servlets.getRequest();
		if(re!=null){
			String url = Servlets.getRequest().getRequestURI();
			return getSessionKeyPrefixByUrl(url);
		}
		//无Request时 （线程调用) 从Subject中取
		//Subject有时取不到 不知道为什么 可能Subject还未实例化的时候取就报错
		try {
			Subject subject = SecurityUtils.getSubject();
			WebDelegatingSubject ws=(WebDelegatingSubject)subject;
			return ws.getSessionKeyFix();
		} catch (Exception e) {
			return null;
		}
		
	}
	public static String getSessionKeyPrefixByUrl(String url){
		if(StringUtil.isEmpty(url)){
			return null;
		}
		if(url.endsWith("/a")||url.indexOf("/a/") > -1
				||(url.indexOf("/f/") == -1&&url.indexOf("/act/") > -1)//工作流链接
				){
			return AdminsessionKeyFix;
		}
		if(url.endsWith("/f")||url.indexOf("/f/") > -1){
			return FrontsessionKeyFix;
		}
		return null;
	}
}
