<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>场地分配</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <sys:message content="${message}"/>
    <div class="row-room">
        <div class="sidebar sidebar-floor">
            <div id="floorTree" class="ztree" data-iframe="#roomIframe" data-iframe-url="${ctx}/pw/pwRoom/listFPCD?pwSpace.id=pwSpaceId" data-url="${ctx}/pw/pwSpace/treeData"></div>
        </div>
        <div class="layout-handler-bar"></div>
        <div class="room-list-content">
            <iframe id="roomIframe" src="${ctx}/pw/pwRoom/listFPCD?pwSpace.id=${pwRoom.pwSpace.id}"></iframe>
        </div>
    </div>
</div>

</body>
</html>