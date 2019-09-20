<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>--%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
<body>
<div class="container-fluid">
    <%--<div class="edit-bar clearfix">--%>
    <%--<div class="edit-bar-left">--%>
    <%--<span>${menuName}</span>--%>
    <%--<i class="line weight-line"></i>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>

    <form:form id="searchForm" modelAttribute="proModel" action="${actionUrl}" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
        <input id="gnodeId" name="gnodeId" type="hidden" value="${gnodeId}"/>


        <div class="search-btn-box">
            <form:input class="input-xlarge" path="queryStr" htmlEscape="false" maxlength="100" autocomplete="off"
                        placeholder="项目编号/项目名称/负责人模糊搜索"/>
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
            <%--<button type="button" class="btn btn-primary" onclick="projectExport()">导出</button>--%>
        </div>

    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th width="140" data-name="a.competition_number"><a class="btn-sort" href="javascript:void(0);">编号<i
                    class="icon-sort"></i></a></th>
            <th width="25%" data-name="a.p_name"><a class="btn-sort" href="javascript:void(0);">作品名称<i
                    class="icon-sort"></i></a></th>

            <th data-name="u.name"><a class="btn-sort" href="javascript:void(0);">负责人<i
                    class="icon-sort"></i></a></th>
            <th data-name="o6.name"><a class="btn-sort" href="javascript:void(0);">学院<i
                    class="icon-sort"></i></a></th>
            <th>组人数</th>
            <th>指导老师</th>
            <th>报名日期</th>
            <th>${menuName}结果</th>
            <c:if test="${isScore!=null && isScore=='1'}">
            <th>评分</th>
            </c:if>
            <th>年份</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="item">
            <c:set var="map" value="${fns:getActByPromodelId(item.id)}"/>
            <tr>
                <td>${item.competitionNumber}</td>
                <td>
                    <a href="${ctx}/promodel/proModel/viewForm?id=${item.id}&taskName=${map.taskName}">${item.pName}</a>
                </td>

                <td>${fns:getUserById(item.declareId).name}</td>
                <td>${item.deuser.office.name}</td>
                <td>${item.team.memberNum}</td>
                <td>
                        ${item.team.uName}
                </td>
                <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd"/></td>
                <td>
                        ${item.finalResult}
                </td>
                <c:if test="${item.gScore!=null && item.gScore!=''}">
                    <td>${item.gScore}</td>
                </c:if>
                <td>${item.year}</td>
                <td>
                    <a href="${ctx}/actyw/actYwGnode/designView?groupId=${item.actYw.groupId}&proInsId=${item.procInsId}"
                       target="_blank">
                        <c:choose>
                            <c:when test="${item.state eq '1'}">
                                项目已结项
                            </c:when>
                            <c:otherwise>待${map.taskName}</c:otherwise>
                        </c:choose>
                    </a>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${item.state =='1'}">
                            <button type="button" class="btn btn-primary btn-small" disabled>审核</button>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${map.status =='todo'}">
                                    <a class="btn btn-small btn-primary"
                                       href="${ctx}/act/task/auditform?gnodeId=${map.gnodeId}&proModelId=${item.id}
                              &pathUrl=${actionUrl}&taskName=${map.taskName}">审核</a>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="btn btn-primary btn-small" disabled>审核</button>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
<script>

    $(document).ready(function () {
        window.parent.sideNavModule.changeUnreadTag('${actywId}');
        $("#ps").val($("#pageSize").val());
    });

    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }

    function projectExport() {
        var actywId = '${actywId}';
        var proCategory = $("#proCategory").val();
        var officeId = $("#officeId").val();
        var queryStr = $("#queryStr").val();
        location.href = "${ctx}/exp/expData/" + actywId + "?proCategory=" + proCategory
            + "&officeId=" + officeId + "&queryStr=" + queryStr + "&gnodeId=" + '${gnodeId}';

    }


</script>
</body>
</html>