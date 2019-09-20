<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title }导入</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/md/impdataList.js"></script>
    <script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span><c:if test="${type=='6' }">立项</c:if><c:if test="${type=='7' }">中期检查</c:if><c:if
                    test="${type=='8' }">结项审核</c:if>导入</span>
            <i class="line weight-line"></i>
        </div>
        <sys:message content="${message}"/>
    </div>
    <form:form id="searchForm" action="${ctx}/impdata/mdlist" method="post"
               class="form-horizontal clearfix form-search-block" cssStyle="overflow: hidden">
        <input type="hidden" id="referrer" name="referrer" value="${referrer}">
        <input type="hidden" id="type" name="type" value="${type}">
        <input type="hidden" id="actywId" name="actywId" value="${actywId}">
        <input type="hidden" id="gnodeId" name="gnodeId" value="${gnodeId}">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="text-right mgb-20">
            <button type="button" id="import" class="btn btn-primary">选择文件</button>
            <button type="button" class="btn btn-default" onclick="goback();">返回</button>
            <input type="file" style="display: none" id="fileToUpload" name="fileName" multiple="multiple"
                   accept=".xls,.xlsx"/>
        </div>
    </form:form>
    <%--<div class="table-responsive">--%>
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover table-center table-orange table-sort table-nowrap">
            <thead>
            <tr>
            	<th width="28%">文件名称</th>
                <th>开始导入时间</th>
                <th>数据总数</th>
                <th>导入成功数</th>
                <th>导入失败数</th>
                <th>状态</th>
                <th>错误信息</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="dataTb">
            <c:forEach items="${page.list}" var="data">
                <c:if test="${data.is_complete!=1}">
                    <tr uncomplete="${data.id}">
                </c:if>
                <c:if test="${data.is_complete==1}">
                    <tr>
                </c:if>
                <td>${data.filename}</td>
                <td>${data.create_date}</td>
                <td>${data.total}</td>
                <td>${data.success}</td>
                <td>${data.fail}</td>
                <td>
                    <c:if test="${data.is_complete==0}">导入中</c:if>
                    <c:if test="${data.is_complete==1 and empty data.errmsg}">导入完毕</c:if>
                    <c:if test="${data.is_complete==1 and not empty data.errmsg}">导入失败</c:if>
                </td>
                <td>${data.errmsg}</td>
                <shiro:hasPermission name="sys:user:import">
                    <td style="white-space: nowrap">
                        <c:if test="${data.is_complete!=1}">
                            <a style="display: none" href="${ctx}/impdata/expData?id=${data.id}&type=${data.imp_tpye}&actywId=${actywId}&gnodeId=${gnodeId}"
                               class="btn isComplete_down btn-primary btn-small">下载错误数据</a>
                            <a style="display: none" href="${ctx}/impdata/delete?id=${data.id}&referrer=${encodereferrer}&actywId=${actywId}&gnodeId=${gnodeId}"
                               class="btn isComplete btn-default btn-small"
                               onclick="return confirmx('确认要删除吗？', this.href)">删除</a>
                        </c:if>
                        <c:if test="${data.is_complete==1}">
                        	<c:if test="${data.fail!='0'}">
                            <a href="${ctx}/impdata/expData?id=${data.id}&type=${data.imp_tpye}&actywId=${actywId}&gnodeId=${gnodeId}"
                               class="btn btn-primary  btn-small">下载错误数据</a>
                               </c:if>
                            <a href="${ctx}/impdata/delete?id=${data.id}&referrer=${encodereferrer}&actywId=${actywId}&gnodeId=${gnodeId}" class="btn btn-default btn-small"
                               onclick="return confirmx('确认要删除吗？', this.href)">删除</a>
                        </c:if>
                    </td>
                </shiro:hasPermission>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        ${page.footer}
    <%--</div>--%>
</div>
<div id="dialog-message" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
<script>
    $(document).ready(function () {
        $("#ps").val($("#pageSize").val());
    });

    function page(n, s) {
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }
</script>
</body>
</html>