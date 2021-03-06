<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
        <script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
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

<div class="container-fluid">
    <%--<div class="edit-bar edit-bar-tag clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>结项审核</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="projectDeclare" action="${ctx}/state/secCloseSearch" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目编号</label>
                <div class="controls">
                    <form:input type="text" path="number" cssClass="form-control input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目名称</label>
                <div class="controls">
                    <form:input type="text" path="name" cssClass="form-control input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="type" cssClass="form-control input-medium">
                        <form:option value="" label="所有项目类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目级别</label>
                <div class="controls">
                    <form:select path="level" cssClass="form-control input-medium">
                        <form:option value="" label="请选择"/>
                        <form:options items="${fns:getDictList('project_degree')}" itemLabel="label"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">负责人</label>
                <div class="controls">
                    <form:input type="text" path="leader" cssClass="form-control input-medium"/>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button class="btn btn-primary" type="submit">查询</button>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th width="151">项目编号</th>
            <th width="25%">项目名称</th>
            <th>项目类型</th>
            <th>负责人</th>
            <th>组人数</th>
            <th>指导老师</th>
            <th>项目级别</th>
            <th>年份</th>
            <th>项目审核状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proDeclare">
            <tr>
                <td>
                        ${proDeclare.state == "other"? proDeclare.number :proDeclare.number} <%--${act.vars.map.number}--%>
                </td>
                <td>
                    <c:if test="${proDeclare.state!='other'}">
                        <a
                           href="${ctx}/state/infoView?id=${proDeclare.id}&taskDefinitionKey=closeScore">${proDeclare.name}</a>
                    </c:if>
                    <c:if test="${proDeclare.state=='other'}">
                        <a
                           href="${ctx}/state/projectDetail?id=${proDeclare.id}"> ${proDeclare.name}</a>
                    </c:if>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.typeString :fns:getDictLabel(proDeclare.type, "project_type", "")} <%--${fns:getDictLabel(act.vars.map.type, "project_type", "")}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.leaderString :proDeclare.leaderString} <%--${act.vars.map.leader}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"?  pj:getTeamNum(proDeclare.snames) : proDeclare.snumber} <%--${act.vars.map.teamList}--%>

                        <%--${proDeclare.state == "other"? proDeclare.snames :proDeclare.snames} &lt;%&ndash;${act.vars.map.teamList}&ndash;%&gt;--%>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.tnames :proDeclare.tnames} <%-- ${act.vars.map.teacher}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.levelString :fns:getDictLabel(proDeclare.level, "project_degree", "")} <%-- ${fns:getDictLabel(act.vars.map.level, "project_degree", "")}--%>
                </td>
                <td>
                       ${proDeclare.state == "other"? proDeclare.year : proDeclare.year} <%--${fns:getDictLabel(act.vars.map.level, "project_degree", "")}--%>
                </td>
                <td>
                    <c:if test="${proDeclare.state!='other'}">
                        <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                        <%-- <a href="${ctx}/act/task/processMap2?proInsId=${act.histTask.processInstanceId}" target="_blank">${act.taskName}</a> --%>
                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                ${pj:getAuditStatus(projectDeclare.status_code)}
                        </a>
                    </c:if>
                    <c:if test="${proDeclare.state=='other'}">
                        <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                        <%-- <a href="${ctx}/act/task/processMap2?proInsId=${act.projectDeclare.procInsId}" target="_blank">待${act.taskName}</a> --%>
                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                ${pj:getAuditStatus(projectDeclare.status_code)}</a>
                    </c:if>
                </td>
                <td>
                    <c:if test="${proDeclare.state!='other'}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/state/infoView?id=${proDeclare.id}&taskDefinitionKey=closeScore">查看</a>
                    </c:if>
                    <c:if test="${proDeclare.state=='other'}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/state/projectDetail?id=${proDeclare.id}"> 查看</a>
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