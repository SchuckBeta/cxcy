<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="fnc" uri="/WEB-INF/tlds/fnc.tld" %>
<%@ taglib prefix="pj" uri="/WEB-INF/tlds/pj.tld" %>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags/sys" %>
<%@ taglib prefix="act" tagdir="/WEB-INF/tags/act" %>
<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/cms" %>
<%@ taglib prefix="flow" tagdir="/WEB-INF/tags/flow" %>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="ctxFront" value="${pageContext.request.contextPath}${fns:getFrontPath()}"/>
<c:set var="ctxJs" value="${pageContext.request.contextPath}/js"/>
<c:set var="ctxCss" value="${pageContext.request.contextPath}/css"/>
<c:set var="ctxImg" value="${pageContext.request.contextPath}/img"/>
<c:set var="ctxFonts" value="${pageContext.request.contextPath}/fonts"/>
<c:set var="ctxOther" value="${pageContext.request.contextPath}/other"/>
<c:set var="ctxImages" value="${pageContext.request.contextPath}/images"/>
<c:set var="ctxCommon" value="${pageContext.request.contextPath}/common"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<c:set var="frontTitle" value="${fns:getFrontTitle()}"/>
<c:set var="backgroundTitle" value="${fns:getBackgroundTitle()}"/>
<c:set var="ftpHttp" value="${fns:ftpHttpUrl()}"/>
<%-- <c:set var="drDealTimerMax" value="10500"/> --%>
<c:set var="drDealTimerMax" value="5100"/>
<c:set var="drDealBtnTimerMax" value="42000"/>
