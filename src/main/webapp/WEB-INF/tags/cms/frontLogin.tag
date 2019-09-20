<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<%@ attribute name="user" type="com.oseasy.com.pcore.modules.sys.entity.User" required="true" description="用户" %>
<!--用户名-->
<input type="hidden" id="84bcd363e9b24e25b8aaad7bed3d1dcf_userid" value="${user.id}">
<%--<c:if test="${not empty loginPage}">--%>
    <%--<div class="user-actions">--%>
        <%--<a href="${ctxFront}/toLogin">登录</a> / <a href="${ctxFront}/toRegister">注册</a>--%>
    <%--</div>--%>
<%--</c:if>--%>

<c:if test="${user == null || user.id == null}">
    <div class="user-actions">
        <a href="${ctxFront}/toLogin">登录</a> <c:if test="${fns: getCasHasLogin()}">| <a href="${ctxFront}/toRegister">注册</a></c:if>
        <input type="hidden" name="fromFront" value="1"/>
    </div>
</c:if>
<c:if test="${user.id!=null}">
    <div id="headerUserBlock" class="user-info-notice">
        <a class="user-notice-num" href="${ctxFront}/oa/oaNotify/indexMyNoticeList"><i
                class="el-icon-bell"></i>消息(<span id="84bcd363e9b24e25b8aaad7bed3d1dcf_unread"></span>)</a>
        <div class="user-info-profile">
            <c:set var="userPic" value="${fns:ftpImgUrl(user.photo)}"></c:set>
            <c:set var="studentUrl"
                   value="${ctxFront}/sys/frontStudentExpansion/findUserInfoById?id=${user.id}"></c:set>
            <c:set var="teacherUrl" value="${ctxFront}/sys/frontTeacherExpansion/findUserInfoById?userId=${user.id}"></c:set>
            <form id="userForm" action="${ctxFront}/logout" method="post">
                <a href="${user.userType eq 1 ? studentUrl : teacherUrl}">
                    <img class="user-small-pic"
                         src="${user.photo!=null && user.photo!='' ? userPic : '/img/u4110.png'}"><i
                        class="el-icon-caret-right"></i> </a>
                <div class="user-info">
                    <i class="el-icon-caret-top"></i>
                    <div class="user-info-inner">

                        <div class="user-info-pic">
                            <a href="${user.userType eq 1 ? studentUrl : teacherUrl}"><img
                                    src="${user.photo!=null && user.photo!='' ? userPic : '/img/u4110.png'}"></a>
                        </div>
                        <div class="user-name-office">
                            <a href="${user.userType eq 1 ? studentUrl : teacherUrl}" class="user-name">
                                <c:choose>
                                    <c:when test="${not empty user.name}">
                                        <c:set var="cuser" value="${user}" />
                                        ${cuser.name}
                                    </c:when>
                                    <c:when test="${not empty user.mobile}">
                                        ${user.mobile}
                                    </c:when>
                                    <c:otherwise>
                                        ${user.loginName}
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <span class="office-name">${user.office.name}</span>
                        </div>
                        <div class="user-login-action text-right">
                            <c:if test="${fns: getCasHasLogin()}">
                                <a class="change-user-password"
                                   href="${ctxFront}/sys/frontStudentExpansion/frontUserPassword">修改密码</a>
                            </c:if>
                            <a class="btn-login-out" href="javascript:$('#userForm').submit();">退出登录</a>
                        </div>
                        <input type="hidden" name="fromFront" value="1"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</c:if>
