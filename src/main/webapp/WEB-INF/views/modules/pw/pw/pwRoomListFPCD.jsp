<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
</head>
<body>
<form:form id="searchForm" modelAttribute="pwRoom" cssStyle="display: none" action="${ctx}/pw/pwRoom/listFPCD?pwSpace.id=${pwRoom.pwSpace.id}" method="post" >
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
<%--     <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
    <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
    <input type="hidden" name="pwSpace.id" value="${pwRoom.pwSpace.id}"> --%>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable"  class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort table-room">
    <thead>
    <tr>
        <th>房间名称</th>
        <%--<th>别名</th>--%>
        <th data-name="type">房间类型</th>
        <th data-name="num">容纳人数</th>
        <th data-name="isAssign">可否分配</th>
        <th>多团队</th>
        <th>地址</th>
        <%--<th>负责人</th>--%>
        <th>联系电话</th>
        <!-- <th>是否满员</th> -->
        <shiro:hasPermission name="pw:pwRoom:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="pwRoom">
        <tr>
            <td>
                <a href="${ctx}/pw/pwRoom/view?id=${pwRoom.id}">${pwRoom.name}</a>
            </td>
            <%--<td>${pwRoom.alias}</td>--%>
            <td>${fns:getDictLabel(pwRoom.type, 'pw_room_type', '')}</td>
            <td>${pwRoom.num}</td>
            <td>${fns:getDictLabel(pwRoom.isAssign, 'yes_no', '')}</td>
            <td>${fns:getDictLabel(pwRoom.isAllowm, 'yes_no', '')}</td>
           	<td>
				<c:if test="${(not empty pwRoom.pwSpace.parent) && (pwRoom.pwSpace.parent.id ne root)}">
					<c:if test="${(not empty pwRoom.pwSpace.parent.parent) && (pwRoom.pwSpace.parent.parent.id ne root)}">
						<c:if test="${(not empty pwRoom.pwSpace.parent.parent.parent) && (pwRoom.pwSpace.parent.parent.parent.id ne root)}">
							<c:if test="${(not empty pwRoom.pwSpace.parent.parent.parent.parent) && (pwRoom.pwSpace.parent.parent.parent.parent.id ne root)}">
							${pwRoom.pwSpace.parent.parent.parent.parent.name}/
							</c:if>${pwRoom.pwSpace.parent.parent.parent.name}/
						</c:if>${pwRoom.pwSpace.parent.parent.name}/
					</c:if>${pwRoom.pwSpace.parent.name}/
				</c:if>${pwRoom.pwSpace.name}
            </td>
            <%--<td>${pwRoom.person}</td>--%>
            <td>${pwRoom.mobile}</td>
            <%-- <td>${fns:getDictLabel(pwRoom.isFull, 'true_false', '')}</td> --%>
            <shiro:hasPermission name="pw:pwRoom:edit">
                <td>
                    <a class="btn btn-small btn-primary" href="${ctx}/pw/pwRoom/formSetCD?id=${pwRoom.id}">分配场地</a>
                    <%-- <c:if test="${pwRoom.isAllowm eq 1}">
                        <a class="btn btn-small btn-primary"
                           href="${ctx}/pw/pwRoom/formIsUsable?id=${pwRoom.id}&isUsable=1"
                           data-toggle="confirm"
                           data-msg="确认开放${pwRoom.name}预约吗？"
                           data-id="${pwRoom.id}">开放</a>
                    </c:if>
                    <c:if test="${pwRoom.isUsable eq 1}">
                    	<a class="btn btn-small btn-primary"
                           href="${ctx}/pw/pwRoom/formIsUsable?id=${pwRoom.id}&isUsable=0"
                           data-toggle="confirm" data-msg="确认关闭${pwRoom.name}预约吗？"
                           data-id="${pwRoom.id}">关闭</a>
                    </c:if> --%>
                    <%-- <a class="btn btn-small btn-default"
                       href="${ctx}/pw/pwRoom/delete?id=${pwRoom.id}"
                       data-toggle="confirm" onclick="return confirmx('确认取消${pwRoom.name}分配吗？', this.href)"
                       data-msg="确认取消${pwRoom.name}分配吗？"
                       data-id="${pwRoom.id}" >取消</a> --%>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
${page.footer}
<script>
    $(function () {
        var $parentDoc = $(parent.document);
        var $sidebar = $parentDoc.find('.sidebar')
        var $layoutHandlerBar = $parentDoc.find('.layout-handler-bar')
        if(!$layoutHandlerBar.hasClass('bar-close')){
            $sidebar.show()
        }
        $layoutHandlerBar.show()
        $parentDoc.find('.room-list-content').css('margin-left','');
    })
</script>
</body>
</html>