<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/common/tablepage.css"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
            $("#btnExport").click(function () {
                top.$.jBox.confirm("确认要导出用户数据吗？", "系统提示", function (v, h, f) {
                    if (v == "ok") {
                        $("#searchForm").attr("action", "${ctx}/sys/user/export");
                        $("#searchForm").submit();
                    }
                }, {buttonsFocus: 1});
                top.$('.jbox-body .jbox-icon').css('top', '55px');
            });
            $("#btnImport").click(function () {
                $.jBox($("#importBox").html(), {
                    title: "导入数据", buttons: {"关闭": true},
                    bottomText: "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
                });
            });
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            var grade = $('#findGrade', parent.document).html();
            var id = $('#findId', parent.document).html();

            var teacherType = $('#teacherType', parent.document).html();
            var userName = $('#username', parent.document).val();
            var currState = $("#currState", parent.document).val();
            var arrs = new Array();
            $("input[name='curJoinStr']:checkbox", parent.document).each(function () {
                if ($(this).attr("checked")) {
                    arrs.push($(this).val());
                }
            });
            if (grade == '1') {
                $('#searchForm').attr(
                        "action",
                        "${ctxFront}/sys/user/userListTree?userType=${userType}&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
            } else if (grade == '2') {
                $('#searchForm').attr(
                        "action",
                        "${ctxFront}/sys/user/userListTree?office.id=" + id
                        + "&userType=" + ${userType} +"&grade=2&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
            } else if (grade == '3') {
                $('#searchForm').attr(
                        "action",
                        "${ctxFront}/sys/user/userListTree?professionId=" + id
                        + "&userType=" + ${userType} +"&grade=3&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
            } else {
                $('#searchForm').attr(
                        "action",
                        "${ctxFront}/sys/user/userListTree?userType=${userType}&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
            }
            $("#searchForm").submit();
            return false;
        }
    </script>
    <style>
        .table thead tr th{
            white-space: nowrap;
            text-align: center;
            vertical-align: middle;
        }
        .table tbody tr td{
            text-align: center;
            white-space: nowrap;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<form:form id="searchForm" style="display:none" modelAttribute="user" action="${ctxFront}/sys/user/userListTree"
           method="post" class="breadcrumb form-search ">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
    <ul class="ul-form">
            <%-- <li><label>归属公司：</label><sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
                title="公司" url="/sys/office/treeData?type=1" cssClass="input-small" allowClear="true"/></li> --%>
            <%-- <li><label>姓&nbsp;&nbsp;&nbsp;名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
        <li><label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/></li> --%>
        <!-- <li class="clearfix"></li> -->
            <%-- <li><label>归属部门：</label><sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
                title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/></li> --%>
            <%--  <li><label>关键字</label><form:input path="keyWords"  htmlEscape="false" placeholder="请输入姓名、技术领域、专业等关键字" maxlength="50" class="input-medium"/></li> --%>
        <li style="margin-left:-36px"><label>专业</label> <form:select path="professional"
                                                                     class="input-medium">
            <form:option value="" label="所有专业"/>
            <form:options items="${fns:getDictList('0000000111')}"
                          itemLabel="label" itemValue="value" htmlEscape="false"/>
        </form:select></li>
        <li style="margin-left:-36px"><label>技术领域</label> <form:select path="domain"
                                                                       class="input-medium">
            <form:option value="" label="所有技术领域"/>
            <form:options items="${fns:getDictList('technology_field')}"
                          itemLabel="label" itemValue="value" htmlEscape="false"/>
        </form:select></li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"
                                onclick="return page();"/>
            <!-- <input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
            <input id="btnImport" class="btn btn-primary" type="button" value="导入"/> --></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<table id="contentTable" class="table  table-bordered table-condensed table-theme-default">
    <thead>
    <tr>
        <th><input type="checkbox" id="check_all" data-flag="false"></th>
        <th>姓名</th>
        <c:if test="${user.userType=='1'}">
        <th>学号</th>
        </c:if>
        <c:if test="${user.userType=='2'}">
        <th>工号</th>
        </c:if>
        <c:if test="${user.userType=='1'}">
        <th>现状</th>
        <th>当前在研</th>
        </c:if>
        <c:if test="${user.userType=='2'}">
        <th>当前指导</th>
        </c:if>
        <th>技术领域</th>
        <c:if test="${user.userType=='2'}">
        <th>导师来源</th>
        </c:if>
    <tbody>
    <c:forEach items="${page.list}" var="user">
        <tr>
            <td class="checkone"><input type="checkbox" value="${user.id}" name="boxTd"></td>
            <td>${user.name}</td>
            <td>${user.no}</td>
            <c:if test="${user.userType=='1'}">
                <td>${user.currStateStr}</td>
                <td>${user.curJoin}</td>
            </c:if>
            <c:if test="${user.userType=='2'}">
                <td>${user.curJoin}</td>
            </c:if>
            <td>${user.domainlt}</td>
            <c:if test="${user.userType=='2'}">
                <td>
                    <c:if test="${user.userType=='1'}">
                        ${fns:getDictLabel(user.userType, 'sys_user_type', '')}
                    </c:if>
                    <c:if test="${user.userType=='2' && user.teacherType=='1'}">
                        校内导师
                    </c:if>
                    <c:if test="${user.userType=='2' && user.teacherType=='2'}">
                        企业导师
                    </c:if>
                </td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>
${page.footer}
<script src="${ctxJs}/userTreeList/userTreeList.js"></script>
</body>
</html>