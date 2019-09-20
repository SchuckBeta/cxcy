<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>
<head>
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
            $('#content').css('min-height', function () {
                return $(window).height() - $('.header').height() - $('.footerBox').height()
            });
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


<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0;">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/html-sctzsj">双创大赛</a></li>
        <li class="active">我的大赛</li>
    </ol>
    <div class="cate-table-module">
        <div class="text-right mgb15">
            <%--<a href="${ctx}/gcontest/gContest/viewList" class="btn btn-default btn-sm">当前大赛</a>--%>
            <a href="${ctx}/gcontest/gContest/" class="btn btn-primary btn-sm">大赛列表</a>
        </div>
        <h4 class="title">我的大赛列表</h4>
        <div class="table-block">
            <table id="matchTable"
                   class="table table-bordered table-condensed table-coffee table-nowrap table-center">
                <thead>
                <tr>
                    <%--<th>序号</th>--%>
                    <th>大赛编号</th>
                    <th>大赛项目名称</th>
                    <th>大赛类型</th>
                    <th>大赛类别</th>
                    <th>负责人</th>
                    <%--<th>组成员</th>--%>
                    <th>指导老师</th>
                    <th>参赛组别</th>
                    <th>当前赛制</th>
                    <th>大赛结果</th>
                    <th>荣获奖项</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="gContest" varStatus="state">
                    <tr>
                            <%--<td>${state.count}</td>--%>
                        <td>${gContest.competitionNumber}
	                        <%--<c:choose>--%>
                                <%--<c:when test="${gContest.ftb==0}">--%>
                                    <%--<a class="project-name"--%>
                                       <%--href="${ctx}/gcontest/gContest/viewPro?id=${gContest.id}">${gContest.competitionNumber}</a>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--<a class="project-name"--%>
                                       <%--href="${ctx}/gcontest/gContest/viewPro?id=${gContest.id}">${gContest.competitionNumber}</a>--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${gContest.ftb==0}">
                                    <a class="project-name"
                                       href="${ctx}/gcontest/gContest/viewPro?id=${gContest.id}">${gContest.pName}</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="project-name"
                                       href="${ctx}/promodel/proModel/viewForm?id=${gContest.id}">${gContest.pName}</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${gContest.proTypeStr }</td>
                        <td>
                                ${fns:getDictLabel(gContest.type, "competition_net_type", "")}
                        </td>
                        <td>${gContest.declareName}</td>
                            <%--<td>${gContest.snames }</td>--%>
                        <td>${gContest.tnames }</td>
                        <td>
                            ${fns:getDictLabel(gContest.level, "gcontest_level", "")}
                            <%--<c:choose>--%>
                                <%--<c:when test="${gContest.ftb==0}">--%>
                                    <%--${fns:getDictLabel(gContest.level, "gcontest_level", "")}--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>

                        </td>
                        <td>

                            <c:choose>
                                <c:when test="${gContest.ftb==0}">
                                    ${gContest.currentSystem}
                                </c:when>

                            </c:choose>


                        </td>
                        <td>
                                ${gContest.auditState}

                        </td>
                        <td>
                            <c:if test="${gContest.schoolendResult!=null}">
                                ${fns:getDictLabel(gContest.schoolendResult, "competition_college_prise", "")}
                            </c:if>
                        </td>
                        <td>

                            <c:if test="${gContest.declareId==user.id}">
                                <c:if test="${empty gContest.proc_ins_id || gContest.auditCode ==0}">
                                    <c:if test="${gContest.ftb eq '0'}">
                                        <a class="btn btn-primary btn-sm"
                                        href="${ctx}/gcontest/gContest/form?id=${gContest.id}">继续完善</a>
                                    </c:if>
                                    <c:if test="${gContest.ftb ne '0' && gContest.subStatus eq '0' }">
                                        <a class="btn btn-primary btn-sm"
                                        href="${ctx}/promodel/proModel/form?id=${gContest.id}">继续完善 </a>
                                    </c:if>
                                    <a data-name="${gContest.pName}" class="btn btn-default btn-sm btn-delete-match" href="${ctx}/gcontest/gContest/delete?id=${gContest.id}&ftb=${gContest.ftb}">删除</a>
                                </c:if>
                            </c:if>

                            <c:if test="${not empty gContest.proc_ins_id}">
                                <c:choose>
	                                <c:when test="${gContest.ftb ne 0}">
                                        <c:choose>
                                            <c:when test="${gContest.state !=null && gContest.state eq '1'}">
                                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${gContest.groupId}&proInsId=${gContest.proc_ins_id}&grade=1"
                                                   class="btn btn-primary btn-sm" target="_blank">跟踪</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${gContest.groupId}&proInsId=${gContest.proc_ins_id}"
                                                   class="btn btn-primary btn-sm" target="_blank">跟踪</a>
                                            </c:otherwise>
                                        </c:choose>
                                        <%-- <a href="${ctx}/actyw/actYwGnode/designView?groupId=${gContest.groupId}&proInsId=${gContest.proc_ins_id}"
                                            class="btn btn-primary-oe btn-sm" target="_blank">跟踪</a>--%>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${ctx}/actyw/actYwGnode/designView/${gContest.auditCode}?groupId=${gContest.groupId}"
                                           class="btn btn-primary btn-sm" target="_blank">跟踪</a>
                                    </c:otherwise>
                                </c:choose>

                                <%--<a href="${ctx}/act/task/processMapByType?proInsId=${gContest.proc_ins_id}&type=ds&status=${gContest.auditCode}" target="_blank" class="btn btn-primary-oe btn-sm">跟踪</a>--%>
                                <%--a href="${ctx}/actyw/actYwGnode/designView/${gContest.auditCode}?groupId=${gContest.groupId}" class="btn btn-primary-oe btn-sm" target="_blank">跟踪</a>--%>
                            </c:if>
                            <%--<a class="btn btn-primary btn-sm"--%>
                               <%--href="${ctx}/excellent/gcontestShowForm?gcontestId=${gContest.id}">展示</a>--%>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <%--</div>--%>
            <form:form id="searchForm" modelAttribute="proModel" action="${actionUrl}" method="post"
                       class="form-horizontal clearfix form-search-block">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
            ${page.footer}
            </form:form>
        </div>
    </div>
</div>

<div id="dialogCyjd" class="dialog-cyjd"></div>
<script>
    $(function () {
        $('#matchTable').on('click', 'a.btn-delete-match', function (e) {
            e.preventDefault();
            var name = $(this).data('name');
            var $this = $(this);
            var href = $this.attr('href');
            dialogCyjd.createDialog(0, '是否删除' + name + '大赛？', {
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        $(this).dialog('close')
                        location.href = href;
                    }
                }, {
                    text: '取消',
                    'class': 'btn btn-sm btn-default',
                    click: function () {
                        $(this).dialog('close')
                    }
                }]
            })
        })
    })
</script>
</body>
</html>