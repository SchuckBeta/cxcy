<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>
    <!--头部导航公用样式-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/common.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=q1111">
    <!--focus样式表-->

    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>

    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <!--文本溢出-->
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <script src="${ctxJs}/frontCyjd/frontCommon.js?v=21" type="text/javascript"></script>
    <script>
        var ftpHttp = '${fns:ftpHttpUrl()}';
        var webMaxUpFileSize = "${fns:getMaxUpFileSize()}";
    </script>
    <script type="text/javascript">

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            //$("#searchForm").attr("action","${ctxFront}/sys/user/userListTreePublish?userType=${userType}");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<div>
    <table id="contentTable"
           class="table table-bordered table-condensed table-orange table-nowrap table-hover" style="font-size: 12px;">
        <thead>
        <tr>
            <th><input type="checkbox" id="check_all" data-flag="false"></th>
            <th>专业</th>
        <tbody>
        <c:forEach items="${professionList}" var="office">
            <tr>
                <td class="checkone"><input type="checkbox" value="${office.id}" name="boxTd"></td>
                <td>${office.name}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%--${page.footer}--%>
<script src="${ctxJs}/userTreeList/userTreeList.js"></script>
</body>
</html>