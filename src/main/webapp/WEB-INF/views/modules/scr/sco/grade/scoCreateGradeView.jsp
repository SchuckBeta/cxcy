<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@ include file="/WEB-INF/views/include/backcyjd.jsp"%>

</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>查看学分详情</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <caption>${projectName}</caption>
        <thead>
        <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>职责</th>
            <th>任务权重</th>
            <th>学分配比</th>
            <th>认定学分</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach  items="${scoProjectVoList}" var="scoProjectVo">
        <tr>
            <td>${scoProjectVo.user.no}</td>
            <td>${scoProjectVo.user.name}</td>
            <td>
                <c:choose>
                    <c:when test="${scoProjectVo.user.id eq scoProjectVo.projectDeclare.leader}">
                    项目负责人
                    </c:when>
                    <c:otherwise>
                    项目成员
                    </c:otherwise>
                </c:choose>
            </td>
            <td>${scoProjectVo.weightVal}</td>
            <td>
                <c:if test="${scoProjectVo.percent<1}">
                    <fmt:formatNumber type="percent" minFractionDigits="0" value="${scoProjectVo.percent}" />
                </c:if>
            </td>
            <td>
                <c:if test="${scoProjectVo.percent<1}">
                     ${fns:saveNum(scoProjectVo.scoAffirm.scoreVal*scoProjectVo.percent,1)}
                </c:if>
            </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="text-center">
        <button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
    </div>
</div>


</body>
</html>