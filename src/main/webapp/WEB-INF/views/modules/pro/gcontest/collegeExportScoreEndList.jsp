<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>大赛学院专家打分</title>
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

    </script>
    <style>
        .tabel_head {
            position: relative;
        }

        .tabel_head .tlt {
            position: absolute;
            left: 10px;
            top: 6px;
            color: white;
        }
    </style>
</head>
<body>
<div class="mybreadcrumbs"><span>参赛项目网评</span></div>
<ul class="nav nav-tabs" style="margin-top: 10px;">
    <li><a href="${ctx}/gcontest/gContest/collegeExportScore/">待审核</a></li>
    <li class="active"><a href="${ctx}/gcontest/gContest/collegeExportScoreEnd/">已审核</a></li>
</ul>

<form:form id="searchForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/collegeExportScoreEnd"
           method="post" class="breadcrumb form-search clearfix">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="input-item"><label>大赛类型</label>
        <form:select path="type" class="input-xlarge required">
            <form:option value="" label="所有大赛类型"/>
            <form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select></div>
    <%-- 学院:
    <sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue="${role.office.name}"
    title="学院" url="/sys/office/treeData" />--%>
    <div class="input-item"><label class="char6">参赛项目名称</label><form:input path="pName"/></div>
    <div class="pull-right">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
    </div>
</form:form>
<table id="contentTable" class="table table-hover table-bordered table-condensed self_table">
    <thead>
    <tr>
        <th width="160px">大赛编号</th>
        <th>参赛项目名称</th>
        <th>学院</th>
        <th>申报人</th>
        <th>组成员</th>
        <th>组别</th>
        <th>大赛类型</th>
        <th>融资情况</th>
        <th>指导老师</th>
        <c:if test="${state!=null && state=='2'}">
            <th>学院专家评分</th>
            <th>学院排名</th>
        </c:if>
        <c:if test="${state!=null && state=='3'}">
            <th>学院得分</th>
            <th>学院排名</th>
        </c:if>
        <c:if test="${state!=null && state=='4'}">
            <th>学院专家评分</th>
        </c:if>
        <c:if test="${state!=null && state=='5'}">
            <th>学院排名</th>
            <th>学院排名</th>
            <th>学院网评得分</th>
        </c:if>
        <th>状态</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="gContest">
        <tr>
            <td>${gContest.competitionNumber}</td>
            <td>${gContest.pName}</td>
            <td>
                    ${gContest.collegeName}
            </td>
            <td>
                    ${gContest.declareName}
            </td>
            <td>
                    ${gContest.snames}
            </td>
            <td>
                    ${fns:getDictLabel(gContest.level, "gcontest_level", "")}
            </td>
            <td>
                    ${fns:getDictLabel(gContest.type, "competition_net_type", "")}
            </td>
            <td>
                    ${fns:getDictLabel(gContest.financingStat, "financing_stat", "")}
            </td>
            <td>
                    ${gContest.tnames}
            </td>
            <c:if test="${state!=null && state=='2'}">
                <td>
                        ${gContest.gScore}
                </td>
                <td>
                    <c:if test="${gContest.sort!=null && gContest.sort!=''}">
                        ${gContest.sort}
                    </c:if>
                </td>
            </c:if>
            <c:if test="${state!=null && state=='3'}">
                <td>
                        ${gContest.gScore}
                </td>
                <td>
                    <c:if test="${gContest.sort!=null && gContest.sort!=''}">
                        ${gContest.sort}
                    </c:if>
                </td>
            </c:if>
            <c:if test="${state!=null && state=='4'}">
                <td>
                        ${gContest.gScore}
                </td>
            </c:if>
            <c:if test="${state!=null && state=='5'}">
                <td>
                        ${gContest.collegeSort}
                </td>
                <td>
                        ${gContest.schoolSort}
                </td>
                <td>
                        ${gContest.gScore}
                </td>
            </c:if>
            <td>
                <c:choose>
                    <c:when test="${gContest.auditCode!=null && gContest.auditCode!='0' && gContest.auditCode!='7' && gContest.auditCode!='8'&& gContest.auditCode!='9'}">
                        <%-- <a target="_blank" href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}">${gContest.auditState}</a> --%>
                    	<a href="${ctx}/actyw/actYwGnode/designView/${gContest.auditCode}?groupId=${gContest.groupId}" class="btn btn-primary-oe btn-sm" target="_blank">${gContest.auditState}</a>
                    </c:when>
                    <c:otherwise>
                        ${gContest.auditState}
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a class="check_btn btn-pray"
                   href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${state}"/>
                查看
                </a>
                    <%-- <a class="check_btn btn-pray" target="_blank"
                     href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}">查看</a> --%>
            </td>
        </tr>

    </c:forEach>
    </tbody>
</table>
${page.footer}
</body>
</html>