<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>大赛导入</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/impdata/impProModelGcontestList.js"></script>
    <script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>大赛导入</span>
            <i class="line weight-line"></i>
        </div>
        <sys:message content="${message}"/>
    </div>
    <form:form id="searchForm" action="${ctx}/impdata/promodelgcontestlist" method="post"
               class="form-horizontal clearfix form-search-block">
        <input type="hidden" id="referrer" name="referrer" value="${referrer}">
        <input type="hidden" id="isTrue" name="isTrue" value="${isTrue}">
        <input type="hidden" id="tplType" name="tplType" value="${tplType}">
        <input type="hidden" id="gnodeId" name="gnodeId" value="${gnodeId}">
        <input type="hidden" id="actywId" name="actywId" value="${actywId}">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="text-right mgb-20">
       	 	<button id="loadTemp" class="btn btn-primary" type="button"
                   onclick="downTemplate()">下载模板</button>
            <button id="import" class="btn btn-primary" type="button">选择文件</button>
            <button class="btn btn-default" onclick="goback();" type="button">返回</button>
            <input type="file" style="display: none" id="fileToUpload" name="fileName" multiple="multiple"
                   accept=".xls,.xlsx,.zip"/>
        </div>
    </form:form>
    <%--<div class="table-responsive">--%>
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover table-center table-orange">
            <thead>
            <tr>
            	<th>文件名称</th>
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
                <c:if test="${data.isComplete!=1}">
                    <tr uncomplete="${data.id}">
                </c:if>
                <c:if test="${data.isComplete==1}">
                    <tr>
                </c:if>
                <td>${data.filename}</td>
                <td><fmt:formatDate value="${data.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>${data.total}</td>
                <td>${data.success}</td>
                <td>${data.fail}</td>
                <td>
                    <c:if test="${data.isComplete==0}">导入中</c:if>
                    <c:if test="${data.isComplete==1 and empty data.errmsg}">导入完毕</c:if>
                    <c:if test="${data.isComplete==1 and not empty data.errmsg}">导入失败</c:if>
                </td>
                <td>${data.errmsg}</td>
                <shiro:hasPermission name="sys:user:import">
                    <td>
                        <c:if test="${data.isComplete!=1}">
                            <a style="display: none" href="${ctx}/impdata/expProModelGcontestData?id=${data.id}"
                               class="btn isComplete btn-primary btn-small">下载错误数据</a>
                        </c:if>
                        <c:if test="${data.isComplete==1}">
                        	<c:if test="${data.fail!='0'}">
                            <a href="${ctx}/impdata/expProModelGcontestData?id=${data.id}"
                               class="btn btn-primary btn-small">下载错误数据</a>
                               </c:if>
                        </c:if>
                            <a href="${ctx}/impdata/delete?actywId=${actywId}&gnodeId=${gnodeId}&id=${data.id}&tplType=${tplType}&referrer=${encodereferrer}" class="btn btn-default btn-small"
                               onclick="return confirmx('确认要删除吗？', this.href)">删除</a>
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