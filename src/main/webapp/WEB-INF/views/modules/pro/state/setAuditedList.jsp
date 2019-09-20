<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>国创项目学院立项已审核</title>
        <script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <%@include file="/WEB-INF/views/include/backtable.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/topSearchForm.css">
    <script src="${ctxJs}/gcProject/download.js"></script>
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
<div class="mybreadcrumbs"><span>立项审核</span></div>
<div class="content_panel">
    <ul class="nav nav-tabs" style="margin-top: 10px;">
        <li><a href="${ctx}/state/setAuditList/">待审核</a></li>
        <li class="active"><a href="${ctx}/state/setAuditedList">已审核</a></li>
    </ul>

    <form:form id="searchForm" modelAttribute="act" action="${ctx}/state/setAuditedList" method="post"
               class="form-inline form-content-box">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="row">
            <div class="right-form pull-right">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
            </div>
            <div class="left-form">
                <div class="form-group">
                    <label for="map['number']">项目编号</label>
                    <form:input type="text" path="map['number']"/>
                </div>
                <div class="form-group">
                    <label for="map['name']">项目名称</label>
                    <form:input type="text" path="map['name']"/>
                </div>
                <div class="form-group">
                    <label for="map['type']">项目类别</label>
                    <form:select path="map['type']" class="required">
                        <form:option value="" label="所有项目类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
                <div class="form-group">
                    <label for="map['leader']">负责人</label>
                    <form:input  type="text" path="map['leader']"/>
                </div>
            </div>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table  table-bordered table-condensed self_table">
        <thead>
        <tr>
            <th width="160px">项目编号</th>
            <th>项目名称</th>
            <th class="white-nowrap">项目类别</th>
            <th class="white-nowrap">负责人</th>
            <th>项目组成员</th>
            <th class="white-nowrap">指导老师</th>
            <th>项目级别</th>
            <th class="white-nowrap">状态</th>
            <th class="white-nowrap">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="act">
            <c:set var="task" value="${act.histTask}"/>
            <c:set var="vars" value="${act.vars}"/>
            <c:set var="procDef" value="${act.procDef}"/>
            <c:set var="status" value="${act.status}"/>
            <tr>
                <td>
                        ${act.vars.map.number}
                </td>
                <td>
                        ${act.vars.map.name}
                </td>
                <td>
                        ${fns:getDictLabel(act.vars.map.type, "project_type", "")}
                </td>
                <td>
                        ${act.vars.map.leader}
                </td>
                <td>
                        ${act.vars.map.teamList}
                </td>
                <td>
                        ${act.vars.map.teacher}
                </td>
                <td>
                        ${fns:getDictLabel(pj:getById(act.vars.map.id).level, "project_degree", pj:getById(act.vars.map.id).level)}
                </td>
                <td>
                        ${act.taskName}
                </td>
                <td>
                    <a class="check_btn btn-pray"
                       href="${ctx}/state/infoView?id=${act.vars.map.id}&taskDefinitionKey=${task.taskDefinitionKey}">查看</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        ${page.footer}
</div>


</body>
</html>