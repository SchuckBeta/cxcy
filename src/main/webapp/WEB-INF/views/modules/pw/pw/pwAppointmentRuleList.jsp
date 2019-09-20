<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>预约设置</span>
            <i class="line weight-line"></i>
        </div>
    </div>
</div>
<div class="mybreadcrumbs">
    <span>预约规则</span>
</div>
<div class="content_panel">
    <ul class="nav nav-tabs">
        <li class="active"><a href="${ctx}/pw/pwAppointmentRule/">预约规则列表</a></li>
        <shiro:hasPermission name="pw:pwAppointmentRule:edit">
            <li><a href="${ctx}/pw/pwAppointmentRule/form">预约规则添加</a></li>
        </shiro:hasPermission>
    </ul>
    <form:form id="searchForm" modelAttribute="pwAppointmentRule" action="${ctx}/pw/pwAppointmentRule/" method="post"
               class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <ul class="ul-form">
            <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
            <li><label>是否自动审核：</label>
                <form:radiobuttons path="isAuto" items="${fns:getDictList('yes_no')}" itemLabel="label"
                                   itemValue="value" htmlEscape="false"/>
            </li>
        </ul>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
        <thead>
        <tr>
            <th>是否自动审核</th>
            <th>预约自动审核时间（分钟）</th>
            <th>最后更新时间</th>
            <th>备注</th>
            <shiro:hasPermission name="pw:pwAppointmentRule:edit">
                <th>操作</th>
            </shiro:hasPermission>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="pwAppointmentRule">
            <tr>
                <td><a href="${ctx}/pw/pwAppointmentRule/form?id=${pwAppointmentRule.id}">
                        ${fns:getDictLabel(pwAppointmentRule.isAuto, 'yes_no', '')}
                </a></td>
                <td>
                        ${pwAppointmentRule.minute}
                </td>
                <td>
                    <fmt:formatDate value="${pwAppointmentRule.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                        ${pwAppointmentRule.remarks}
                </td>
                <shiro:hasPermission name="pw:pwAppointmentRule:edit">
                    <td>
                        <a class="check_btn btn-pray btn-lx-primary"
                           href="${ctx}/pw/pwAppointmentRule/form?id=${pwAppointmentRule.id}">修改</a>
                        <a class="check_btn btn-pray btn-lx-primary"
                           href="${ctx}/pw/pwAppointmentRule/delete?id=${pwAppointmentRule.id}"
                           onclick="return confirmx('确认要删除该预约规则吗？', this.href)">删除</a>
                    </td>
                </shiro:hasPermission>
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