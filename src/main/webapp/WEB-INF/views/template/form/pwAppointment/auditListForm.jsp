<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

    </script>
</head>
<body>
<div class="container-fluid container-fluid-oe">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>预约列表</span>
            <i class="line weight-line"></i>
        </div>
    </div>
</div>
<div class="content_panel">
    <form:form id="searchForm" modelAttribute="act" action="${actionUrl}" method="post"
               class="form-inline form-content-box">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    </form:form>
    <table id="contentTable" class="table table-bordered table-condensed table-hover  table-sort table-theme-default table-center">
        <thead>
        <tr>
            <th width="160px">序号</th>
            <th>预约人</th>
            <th>预约房间</th>
            <th>预约类型</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="act">
            <tr>
                <td>${act.vars.map.number}</td>
                <td>${fns:getUserById(act.vars.map.declareId).name}</td>
                <td>
                        ${fns:getDictLabel(act.vars.map.type, "competition_net_type", "")}
                </td>
                <td>
                        ${fns:getDictLabel(act.vars.map.financingStat, "financing_stat", "")}
                </td>
                <td>
                    <c:if test="${act.status=='todo'||act.status=='claim'}">
                    	<a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${act.task.processInstanceId}" class="check_btn btn-pray btn-lx-primary" target="_blank">待${act.taskName}</a>
                    </c:if>
                    <c:if test="${act.status=='finish'}">${act.taskName}</c:if>
                </td>
                <td>

                    <c:if test="${act.status=='finish'}">
                    	<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${act.task.processInstanceId}" target="_blank">查看</a>
                        <%-- ${ctx}/promodel/proModel/view?id=${act.vars.map.id}&taskDefinitionKey=${act.task.taskDefinitionKey}">查看</a>--%>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    ${page.footer}

</div>
</body>
</html>