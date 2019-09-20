<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>大赛通告表管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/backtable.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css">
    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="${ctxJs}/common.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
            $('#sendMsgWrap tr').on('click', 'td .sendMsg', function () {
                var sId = $(this).parent().siblings('.first-line').find('input').val();
                var type = $(this).siblings('input').val();
                showModalMessage(0, '确定要发布吗？', {
                    确定: function () {
                        $.ajax({
                            url: "${ctx}/gcontest/gContestAnnounce/publish",
                            type: "post",
                            data: {"type": type},
                            success: function (data) {
                                if (data > 0) {
                                    showModalMessage(0,"该类型项目已发布！");
                                    return false;
                                } else {
                                    window.location.href = "${ctx}/oa/oaNotify/formBroadcast?sId=" + sId;
                                }
                            }
                        })
                        $(this).dialog("close");
                    },
                    取消: function () {
                        $(this).dialog("close");
                        return false;
                    }
                });
            })
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
    <style>
        th {
            background: #f4e6d4 !important;
        }

        table td .btn {
            padding: 3px 11px;
            background: #e5e5e5;
            color: #666;
        }

        table td .btn:hover {
            background: #e9432d;
            color: #fff;
        }

        .ui-dialog .ui-button {
            padding: 0;
        }

        .ui-dialog .ui-dialog-titlebar-close {
            background: none;
        }
    </style>
</head>
<body>
<div class="mybreadcrumbs"><span>大赛通告表管理</span></div>
<div class="content_panel">
    <ul class="nav nav-tabs" style="margin-top: 10px;">
        <li class="active"><a href="${ctx}/gcontest/gContestAnnounce/">大赛列表</a></li>
        <li><a href="${ctx}/gcontest/gContestAnnounce/form">创建大赛</a></li>
    </ul>

    <form:form id="searchForm" modelAttribute="gContestAnnounce"
               action="${ctx}/gcontest/gContestAnnounce/" method="post"
               class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-hover table-bordered table-condensed">
        <thead>
        <tr>
            <th>大赛名称</th>
            <th>大赛类型</th>
            <th>大赛报名有效期</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="sendMsgWrap">
        <c:forEach items="${page.list}" var="gContestAnnounce">
            <tr>
                <td class="first-line">${gContestAnnounce.gName }<input type="hidden" value="${gContestAnnounce.id }"/>
                </td>
                <td>${fns:getDictLabel(gContestAnnounce.type, 'gContestAnnounce_type', '')}</td>
                <td>
                    <fmt:formatDate value="${gContestAnnounce.applyStart}" pattern="yyyy-MM-dd"/>至
                    <fmt:formatDate value="${gContestAnnounce.applyEnd}" pattern="yyyy-MM-dd"/>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${gContestAnnounce.status=='0'}">
                            发布
                        </c:when>
                        <c:when test="${gContestAnnounce.status=='1'}">
                            发布
                        </c:when>
                        <c:otherwise>
                            未发布
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${gContestAnnounce.status!='0' && gContestAnnounce.status!='1' }">
                            <a href="${ctx}/gcontest/gContestAnnounce/formEdit?id=${gContestAnnounce.id}"
                               class="btn">修改</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${ctx}/gcontest/gContestAnnounce/formEdit?id=${gContestAnnounce.id}"
                               class="btn">修改</a>
                            <a href="${ctx}/gcontest/gContestAnnounce/form?id=${gContestAnnounce.id}&operationType=1"
                               class="btn">查看</a>
                        </c:otherwise>
                    </c:choose>
                    <a href="${ctx}/gcontest/gContestAnnounce/delete?id=${gContestAnnounce.id}"
                       onclick="return confirmx('确认要删除该大赛通告表吗？', this.href)" class="btn">删除</a>
                    <c:if test="${gContestAnnounce.status!='1' && gContestAnnounce.status!='0' }">

                        <a class="btn sendMsg">消息发布</a>
                    </c:if>
                    <input value="${gContestAnnounce.type }" id="gType" type="hidden"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    ${page.footer}
</div>

<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
</body>
</html>