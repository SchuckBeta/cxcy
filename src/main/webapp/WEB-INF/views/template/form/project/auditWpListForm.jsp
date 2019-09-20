<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>
<head>
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/Gctable.css">--%>
    <script src="${ctxStatic}/common/initiate.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
            $('#content').css('min-height', function () {
                return $(window).height() - $('.header').height() - $('.footerBox').height()
            });
            $('.pagination_num').removeClass('row');
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
<form:form id="searchForm" modelAttribute="projectDeclare"
           action="${ctx}/project/projectDeclare/list" method="post">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden"
           value="${page.pageSize}"/>
</form:form>
<div class="container-fluid container-fluid-oe">
    <div class="text-right mgb15">
        <a href="curProject" class="btn btn-default-oe">当前项目</a>
        <a href="list" style="margin-top: 0" class="btn btn-active">我的项目列表</a>
    </div>
    <div class="project-list-row">
        <h4 class="front-table-title">我的项目列表</h4>
        <div class="front-table-wrap">
            <%--<div class="table-responsive-big">--%>
                <table class="table table-bordered table-front-default-thead">
                    <thead>
                    <th width="180px">项目编号</th>
                    <th>项目名称</th>
                    <th width="80px">项目类型</th>
                    <th>项目负责人</th>
                    <th>项目成员</th>
                    <th>项目导师</th>
                    <th>申报时间</th>
                    <th width="80px">项目状态</th>
                    <th>立项评级</th>
                    <th width="80px">项目结果</th>
                    <th>操作</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${page.list}" var="projectDeclare">
                        <tr>
                            <td>${projectDeclare.number }</td>
                            <td>${projectDeclare.project_name }</td>
                            <td>${projectDeclare.type }</td>
                            <td>${projectDeclare.leader }</td>
                            <td>${projectDeclare.snames }</td>
                            <td>${projectDeclare.tnames }</td>
                            <td>${projectDeclare.apply_time }</td>
                            <td>${projectDeclare.status }</td>
                            <td>${projectDeclare.level }</td>
                            <td>${projectDeclare.final_result }</td>
                            <td>
                                <a class="btn btn-primary-oe btn-sm"
                                   href="${ctx}/project/projectDeclare/viewForm?id=${projectDeclare.id}">查看</a>
                                <c:if test="${projectDeclare.create_by==user.id}">
                                    <c:if test="${projectDeclare.status_code==null||projectDeclare.status_code==0}">
                                        <a class="btn btn-primary-oe btn-sm"
                                           href="${ctx}/project/projectDeclare/form?id=${projectDeclare.id}">修改</a>
                                        <a class="btn btn-default-oe btn-sm"
                                           href="javascript:void(0);"
                                           onclick="delPro('${projectDeclare.id}')">删除</a>
                                    </c:if>
                                </c:if>
                                <c:if test="${projectDeclare.proc_ins_id!=null&&projectDeclare.proc_ins_id!=''}">
                                    <a class="btn btn-primary-oe btn-sm"
                                       href="${ctx}/act/task/processMapByType?proInsId=${projectDeclare.proc_ins_id}&type=gc&status=${projectDeclare.status_code}"
                                       target="_blank">跟踪</a>
                                </c:if>
                                <a class="btn btn-primary-oe btn-sm"
                                   href="${ctx}/excellent/projectShowForm?projectId=${projectDeclare.id}">项目展示</a>
                                    <%--<c:if test="${projectDeclare.hasConfig == 'true' }">
                                        <a  class="buttom btn" href="${ctxFront}/project/projectDeclare/scoreConfig?projectId=${projectDeclare.id}" >学分配比</a>
                                    </c:if>--%>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            <%--</div>--%>
            ${page.footer}
        </div>
    </div>
</div>
<script src="${ctxJs}/Projectlist.js"></script>
</body>
</html>