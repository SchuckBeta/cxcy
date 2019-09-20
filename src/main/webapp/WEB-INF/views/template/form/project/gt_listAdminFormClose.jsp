<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%--<%@include file="/WEB-INF/views/include/backCommon.jsp" %>--%>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
        	getExpInfo();
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
            <span>结项审核</span>
            <i class="line weight-line"></i>
        </div>
    </div>
</div>
<div class="content_panel" style="width: 97%;margin:0 auto;">
    <form:form id="searchForm" modelAttribute="act" action="${actionUrl}" method="post"
               class="form-inline form-content-box">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="gnodeId" name="gnodeId" type="hidden" value="${gnodeId}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>

    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-font14">
            <thead>
            <tr>
                <th width="15%">项目编号</th>
                <th width="37%">项目名称</th>
                <th width="10%">申报人</th>
                <th width="12%">项目类型</th>
                <th width="13%">状态</th>
                <th width="12%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="act">
               <%-- <c:set var="proModelMd" value="${fns:getProModelMdById(act.vars.map.id)}" />--%>
                <tr>
                    <td>${act.vars.map.number}</td>
                    <td style="word-break: break-all">${act.vars.map.name}</td>
                    <td>${fns:getUserById(act.vars.map.declareId).name}</td>
                    <td>
                            ${fns:getDictLabel(act.vars.map.proCategory, "project_type", "")}
                    </td>
                    <td>
                        <c:set var="proModel" value="${pj:getProModelById(act.vars.map.id)}" />
                        <c:set var="name" value="${pj:getProModelAuditNameById(proModel.procInsId)}" />
                        <a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${proModel.procInsId}&grade=${proModel.state}"
                           class="check_btn btn-pray btn-lx-primary" target="_blank">
                            <c:choose>
                                <c:when test="${not empty name}">
                                    <c:choose>
                                        <c:when test="${proModel.state == '1'}">
                                            ${name}不通过
                                        </c:when>
                                        <c:otherwise>
                                            待${name}
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    已结项
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </td>
                    <td>
                        <c:if test="${act.status=='todo'}">
                            <c:set var="proModel" value="${pj:getProModelById(act.vars.map.id)}" />
                            <c:if test="${proModel.state!='1'}">
                                <a class="btn btn-small btn-primary"
                                   href="${ctx}/act/task/auditform?taskId=${act.task.id}&taskName=${fns:urlEncode(act.task.name)}
                               &taskDefKey=${act.task.taskDefinitionKey}&procInsId=${act.task.processInstanceId}
                               &procDefId=${act.task.processDefinitionId}&status=${act.status}
                               &path=${path}&pathUrl=${actionUrl}">审核</a>
                            </c:if>
                        </c:if>
                        <a class="btn btn-small btn-primary" href="${ctx}/promodel/proModel/viewForm?id=${proModel.id}">查看</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    ${page.footer}

</div>
</body>
</html>