<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <%@ include file="/WEB-INF/views/include/backtable.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/credit-module.css">
    <style type="text/css">
        .table{
            margin-top: 20px;
        }
        .table thead tr{
            background-color: #f4e6d4;
        }
        .accordion-heading, .table th{
            background: none;
        }

        div.jbox .jbox-button-panel{
            background-color: transparent;
        }
        div.jbox .jbox-title-panel{
            background-color: #eee;
        }
        .jbox-button-panel .jbox-button{
            background-color: #e9432d;
            color: #fff;
            background-image: none;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <table id="tableFormReview" class="table table-bordered table-hover table-theme-default table-form-review">
        <thead>
        <tr>
            <th width="270">检查要点</th>
            <th>审核元素</th>
            <th width="150">分值</th>
        </tr>
        </thead>
        <tbody id="datatb">
        <c:if test="${not empty auditStandard.id }">
            <c:forEach items="${details}" var="de">
                <tr>
                    <td>
                        <div class="form-control-box">
                                ${de.checkPoint }
                        </div>
                    </td>
                    <td>
                        <div class="form-control-box">
                                ${de.checkElement }
                        </div>
                    </td>
                    <td>
                        <div class="form-control-box">
                                ${de.viewScore }
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
        <tfoot>
        <tr>
            <td>总分</td>
            <td colspan="3" id="totalScoreV">${auditStandard.totalScore}</td>
        </tr>
        </tfoot>
    </table>
</div>
</body>
</html>