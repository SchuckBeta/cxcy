<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <title>${fns:getConfig('frontTitle')}</title>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
            $('#content').css('minHeight', function () {
                return $(window).height() - 100 - 308
            })
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();

        }
    </script>
</head>

<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li class="active">创业基地</li>
        <li class="active">预约查询</li>
    </ol>
    <div class="cate-table-module">
        <h4 class="title">我的预约列表</h4>
        <div class="table-block">
            <table class="table table-bordered table-condensed table-coffee table-nowrap table-center">
                <thead>
                <tr>
                    <th>房间名称</th>
                    <th>房间类型</th>
                    <th>预约日期</th>
                    <th>预约时间段</th>
                    <th>参与人数（人）</th>
                    <th>状态</th>
                    <th>操作</th>
                    <%--<th>备注</th>--%>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="pwAppointment">
                    <tr>
                        <td>
                            <a href="${ctxFront}/pw/pwAppointment/details?id=${pwAppointment.id}">${pwAppointment.pwRoom.name}</a>
                        </td>
                        <td>
                                ${fns:getDictLabel(pwAppointment.pwRoom.type,"pw_room_type" , "")}
                        </td>
                        <td>
                            <fmt:formatDate value="${pwAppointment.startDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${pwAppointment.startDate}" pattern="HH:mm"/>-<fmt:formatDate
                                value="${pwAppointment.endDate}" pattern="HH:mm"/>
                        </td>
                        <td>
                                ${pwAppointment.personNum}
                        </td>
                        <td>
                                ${fns:getDictLabel(pwAppointment.status,"pw_appointment_status" , "")}
                            <c:if test="${pwAppointment.status == '0' and pwAppointment.expired}">
                                （已过期）
                            </c:if>
                        </td>
                        <td>
                                <%--<shiro:hasPermission name="pw:pwAppointment:edit">--%>
                            <c:choose>
                                <c:when test="${(pwAppointment.status == '0' or pwAppointment.status == '1') and not pwAppointment.expired}">
                                    <a class="btn btn-primary btn-sm"
                                       href="${ctxFront}/pw/pwAppointment/myCancel?id=${pwAppointment.id}">取消预约</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-default btn-sm btn-gray" href="#" disabled="">取消预约</a>
                                </c:otherwise>
                            </c:choose>
                                <%--</shiro:hasPermission>--%>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${page.footer}
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="pwAppointment" action="${ctxFront}/pw/pwAppointment/myList" method="post"
               cssClass="hide">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    </form:form>
</div>
</body>
</html>