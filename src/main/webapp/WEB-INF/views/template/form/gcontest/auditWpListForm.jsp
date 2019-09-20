<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backtable.jsp" %>
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

        function claim(taskId) {
            $.get('${ctx}/act/task/claim', {taskId: taskId}, function (data) {
                /*	top.$.jBox.tip('签收完成');*/
                location.reload();
            });
        }

    </script>
</head>
<body>

<div class="mybreadcrumbs"><span>${menuName}</span></div>
<div class="content_panel">
    <form:form id="searchForm" modelAttribute="act" action="${actionUrl}" method="post"
               class="form-inline form-content-box">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    </form:form>
    <table id="contentTable" class="table  table-bordered table-condensed self_table">
        <thead>
        <tr>
            <th width="160px">大赛编号</th>
            <th>参赛项目名称</th>
            <th>学院</th>
            <th>申报人</th>

            <th>大赛级别</th>
            <th>大赛类别</th>
            <th>融资情况</th>
            <th>组成员</th>
            <th>指导老师</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="act">

            <tr>
                <td>${act.vars.map.number}</td>
                <td>${act.vars.map.name}</td>
                <td>${fns:getUserById(act.vars.map.declareId).office.name}</td>
                <td>${fns:getUserById(act.vars.map.declareId).name}</td>
                <td>
                        ${fns:getDictLabel(act.vars.map.level, "competition_format", "")}
                </td>
                <td>
                        ${fns:getDictLabel(act.vars.map.proCategory, "competition_net_type", "")}
                </td>
                <td>
                        ${fns:getDictLabel(act.vars.map.financingStat, "financing_stat", "")}
                </td>
                <td>
                    <c:if test="${act.vars.map.teamId!=null&&act.vars.map.teamId!=''}">
                      ${fns:getTeamStudentName(act.vars.map.teamId)}
                    </c:if>
                </td>
                <td>
                    <c:if test="${act.vars.map.teamId!=null&&act.vars.map.teamId!=''}">
                      ${fns:getTeamTeacherName(act.vars.map.teamId)}
                    </c:if>
                </td>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${act.status=='todo'||act.status=='claim'}">
                        <c:set var="proModel" value="${pj:getProModelById(act.vars.map.id)}" />
                          <c:choose>
                              <c:when test="${proModel.state=='1'}">
                                  <a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${proModel.procInsId}&grade=1" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                        ${act.taskName}不通过
                                  </a>
                              </c:when>
                              <c:otherwise>
                                  <a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">待${act.taskName}</a>
                              </c:otherwise>
                          </c:choose>
                        </c:when>
                        <c:otherwise>已${act.taskName}</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${act.status=='claim'}">
                        <a class="check_btn btn-pray btn-lx-primary" href="javascript:void(0)"
                           onclick="claim('${act.task.id}');">签收</a>
                    </c:if>
                    <c:if test="${act.status=='todo'}">
                        <c:if test="${proModel.state!='1'}">
                        <a class="check_btn btn-pray btn-lx-primary"
                           href="${ctx}/act/task/auditform?taskId=${act.task.id}&taskName=${fns:urlEncode(act.task.name)}
						   &taskDefKey=${act.task.taskDefinitionKey}&procInsId=${act.task.processInstanceId}
						   &procDefId=${act.task.processDefinitionId}&status=${act.status}
						   &path=${path}&pathUrl=${actionUrl}">审核</a>
                        <%--<a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${act.task.processInstanceId}" class="check_btn btn-pray btn-lx-primary" target="_blank">查看</a>
                         --%>   <a href="${ctx}/promodel/proModel/viewForm?id=${act.vars.map.id}">查看</a>
                        </c:if>
                    </c:if>
                    <c:if test="${act.status=='finish'}">
                       	<%--<a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${act.histTask.processInstanceId}" class="check_btn btn-pray btn-lx-primary" target="_blank">查看</a>--%>
                        <a href="${ctx}/promodel/proModel/viewForm?id=${act.vars.map.id}">查看</a>
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