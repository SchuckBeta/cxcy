<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>

</head>
<body>
<div class="container-fluid container-fluid-view">
    <div class="edit-bar edit-bar-sm clearfix">
        <div class="edit-bar-left">
            <span>房间信息</span> <i class="line"></i>
        </div>
    </div>
    <div class="panel-body collapse in">
        <div class="panel-inner">
            <div class="row-fluid row-info-fluid" style="height: auto">
                <div class="span6">
                    <p><span class="item-label">名称：</span><i></i>${pwRoom.name}</p>
                    <p><span class="item-label">房间类型：</span>会议室</p>
                    <p><span class="item-label">容纳人数：</span>${pwRoom.num }人</p>
                    <p><span class="item-label">负责人：</span>${pwRoom.person }</p>
                </div>
                <div class="span6">
                    <c:if test="${not empty pwRoom.alias}"><p><span class="item-label">别名：</span>${pwRoom.alias}</p></c:if>
                    <p><span class="item-label">允许多团队入驻：</span>${fns:getDictLabel(pwRoom.isAllowm, 'yes_no', '')}</p>
                    <p><span class="item-label">基地/楼栋/楼层：</span>
	                    <c:if test="${(not empty pwRoom.pwSpace.parent) && (pwRoom.pwSpace.parent.id ne root)}">
							<c:if test="${(not empty pwRoom.pwSpace.parent.parent) && (pwRoom.pwSpace.parent.parent.id ne root)}">
								<c:if test="${(not empty pwRoom.pwSpace.parent.parent.parent) && (pwRoom.pwSpace.parent.parent.parent.id ne root)}">
									<c:if test="${(not empty pwRoom.pwSpace.parent.parent.parent.parent) && (pwRoom.pwSpace.parent.parent.parent.parent.id ne root)}">
									${pwRoom.pwSpace.parent.parent.parent.parent.name}/
									</c:if>${pwRoom.pwSpace.parent.parent.parent.name}/
								</c:if>${pwRoom.pwSpace.parent.parent.name}/
							</c:if>${pwRoom.pwSpace.parent.name}/
						</c:if>${pwRoom.pwSpace.name}
                    </p>
                    <p><span class="item-label">手机：</span>${pwRoom.mobile}</p>
                </div>
            </div>
        </div>
    </div>
    <div class="edit-bar edit-bar-sm clearfix">
        <div class="edit-bar-left">
            <span>房间分配信息</span> <i class="line"></i>
        </div>
    </div>
    <div class="panel-body collapse in">
        <div class="panel-inner">
            <table id="assignedTable"
                   class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-assigned">
                <thead>
                <tr>
                    <th>入驻编号</th>
                    <th>入驻类型</th>
                    <th>负责人</th>
                    <th>名称（团队/企业/项目）</th>
                    <th>入驻有效期</th>
                </tr>
                </thead>
                <tbody id="assignedTbody">
	                <c:forEach var="pwEnter" items="${pwEnters }">
	                	<tr data-index="0">
		                    <td>${pwEnter.no }</td>
		                    <td>
		                    	<c:if test="${not empty pwEnter.eteam}">${fns:getDictLabel(pwEnter.eteam.type, 'pw_enter_type', '')}/</c:if>
				                <c:if test="${not empty pwEnter.eproject}">${fns:getDictLabel(pwEnter.eproject.type, 'pw_enter_type', '')}/</c:if>
				                <c:if test="${not empty pwEnter.ecompany}">${fns:getDictLabel(pwEnter.ecompany.type, 'pw_enter_type', '')}</c:if>
		                    </td>
		                    <td>${pwEnter.applicant.name}</td>
		                    <td><span class="team-name">${pwEnter.eteam.team.name } / ${pwEnter.eproject.project.name } / ${pwEnter.ecompany.pwCompany.name }</span></td>
		                    <td><fmt:formatDate value="${pwEnter.endDate}" pattern="yyyy-MM-dd"/></td>
		                </tr>
	                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="text-center">
    <button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
</div>
<script>
    $(function () {
        var $parentDoc = $(parent.document);
        $parentDoc.find('.sidebar, .layout-handler-bar').hide()
        $parentDoc.find('.room-list-content').css('margin-left', '0');
    })
</script>
</body>
</html>