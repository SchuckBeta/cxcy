<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%request.setAttribute("user", UserUtils.getUser());%>
<div id="appHeader" class="app-header background">
    <div class="app-header-title-container">
        <%--<h2 class="site-title">湖北省大学生创新创业云平台</h2>--%>
        <%--<p class="site-title-second">—管理端</p>--%>
    </div>
    <div class="user-profile">
        <span class="up-item"><i class="iconfont icon-xiaoxi"></i>消息<b class="notification_count"></b> </span>
        <span class="up-item"><i class="iconfont icon-yonghu"></i>${user.name}</span>
        <span class="up-item"><a href="${ctx}/sysMenuIndex"><i class="iconfont icon-fanhui"></i>返回首页</a></span>
        <span class="up-item"><a href="${ctx}/logout"><i class="iconfont icon-tuichu1"></i>退出登录</a></span>
    </div>
</div>




