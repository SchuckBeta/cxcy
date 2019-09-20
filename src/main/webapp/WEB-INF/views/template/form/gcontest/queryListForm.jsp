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

        function claim(taskId) {
            $.get('${ctx}/act/task/claim', {taskId: taskId}, function (data) {
                /*	top.$.jBox.tip('签收完成');*/
                location.reload();
            });
        }

    </script>
</head>
<body>
<div class="container-fluid container-fluid-oe">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>${menuName}</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    </div>
<div class="content_panel">
    <form:form id="searchForm" modelAttribute="proModel" action="${ctx}/cms/form/queryMenuList" method="post"
               class="form-top-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
        <div class="search-form-wrap form-inline">
            <button class="btn btn-back-oe btn-primaryBack-oe"  type="submit" >查询</button>
        </div>
        <div class="condition-main form-horizontal">
            <div class="condition-row">
                <div class="condition-item">
                    <div class="control-group">
                        <label  class="control-label">大赛名称</label>
                            <div class="controls" style="margin-left: 107px;">
                            <form:input type="text" cssClass="form-control input-medium" path="pName"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
    <table id="contentTable" class="table table-bordered table-condensed table-sort table-hover table-center table-theme-default">
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
            <%--<th>操作</th>--%>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proModel">

            <tr>
                <td>${proModel.competitionNumber}</td>
                <td><a href="${ctx}/promodel/proModel/viewForm?id=${proModel.id}">${proModel.pName}</a></td>
                <td>${fns:getUserById(proModel.declareId).office.name}</td>
                <td>${fns:getUserById(proModel.declareId).name}</td>

                <td>
                        ${fns:getDictLabel(proModel.level, "competition_format", "")}
                </td>
                <td>
                        ${fns:getDictLabel(proModel.proCategory, "competition_net_type", "")}
                </td>
                <td>
                        ${fns:getDictLabel(proModel.financingStat, "financing_stat", "")}
                </td>
                <td>
                    <c:if test="${proModel.teamId!=null&&proModel.teamId!=''}">
                      ${fns:getTeamStudentName(proModel.teamId)}
                    </c:if>
                </td>
                <td>
                    <c:if test="${proModel.teamId!=null&&proModel.teamId!=''}">
                      ${fns:getTeamTeacherName(proModel.teamId)}
                    </c:if>
                </td>

                <td>
                    <c:choose>
                            <c:when test="${proModel.state!=null &&  proModel.state=='1'}">
                                <c:choose >
                                   <c:when test="${proModel.grade!=null && proModel.grade=='0'}">
                                       ${pj:getProModelAuditNameById(proModel.procInsId)}不通过
                                   </c:when>
                                   <c:otherwise>
                                       ${pj:getProModelAuditNameById(proModel.procInsId)}通过
                                   </c:otherwise>
                               </c:choose>
                            </c:when>
                            <c:otherwise>
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                ${pj:getProModelAuditNameById(proModel.procInsId)}
                                </a>
                            </c:otherwise>
                        </c:choose>

                </td>

                <%--<td>--%>
                    <%--<a href="">查看</a>--%>
                   <%--&lt;%&ndash; <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">查看</a>&ndash;%&gt;--%>
                <%--</td>--%>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    ${page.footer}

</div>
</body>
</html>