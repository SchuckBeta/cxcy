<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@ include file="/WEB-INF/views/include/backcyjd.jsp" %>
</head>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>创新学分分配比例</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form class="form-inline text-right">
        <input id="confId" name="confId" type="hidden" value="${confId}"/>
        <input type="hidden" id="secondName" name="secondName"value="${secondName}"/>
        <button type="button" class="btn btn-primary"
                onclick="javascript:location.href='${ctx}/sco/scoAllotRatio/form?confId=${confId}'">创建学分配比
        </button>
        <button type="button" class="btn btn-default" onclick="javascript:location.href='${ctx}/sco/scoAffirmConf'">返回
        </button>
    </form>
    <sys:message content="${message}"/>
    <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <th>组人数</th>
            <th>学分分配比例</th>
            <th>备注</th>
            <th width="120">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="allot">
            <tr>
                <td>${allot.number }</td>
                <td>${allot.ratio }</td>
                <td>
                        ${allot.remarks }
                </td>
                <td>
                    <button class="btn btn-primary btn-small" type="button"
                            onclick="javascript:location.href='${ctx}/sco/scoAllotRatio/form?id=${allot.id}&secondName=编辑'">编辑
                    </button>
                    <button class="btn btn-handle-table btn-default btn-small" type="button"
                            onclick="javascript:return confirmx('确认要删除吗？', function(){location.href='${ctx}/sco/scoAllotRatio/delete?id=${allot.id}&affirmConfId=${confId}'})">
                        删除
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>