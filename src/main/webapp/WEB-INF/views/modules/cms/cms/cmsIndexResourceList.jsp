<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
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
</head>


<body>
<div class="container-fluid">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>内容管理</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="cmsIndexResource" action="${ctx}/cms/cmsIndexResource/" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">资源名称</label>
                <div class="controls">
                    <form:input path="resName" htmlEscape="false" maxlength="64" class="input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">栏目</label>
                <div class="controls">
                    <sys:treeselect id="category" name="cmsIndexRegion.category.id"
                                    value="${cmsIndexResource.cmsIndexRegion.category.id}"
                                    labelName="cmsIndexRegion.category.name"
                                    labelValue="${cmsIndexResource.cmsIndexRegion.category.name}" title="栏目"
                                    url="/cms/category/treeData" extId="${cmsIndexRegion.category.id}"
                                    cssClass="required" allowClear="true" cssStyle="width:134px;"/>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button class="btn btn-primary" type="submit">查询</button>
            <a class="btn btn-primary" href="${ctx}/cms/cmsIndexResource/form">添加</a>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <th width="25%">资源名称</th>
            <th>栏目</th>
            <th>区域</th>
            <th>模式</th>
            <th>状态</th>
            <!-- <th>排序</th> -->
            <th>更新时间</th>
            <shiro:hasPermission name="cms:cmsIndexResource:edit">
                <th width="120">操作</th>
            </shiro:hasPermission>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="cmsIndexResource">
            <tr>
                <td><a href="${ctx}/cms/cmsIndexResource/form?id=${cmsIndexResource.id}" class="prj-name">
                        ${cmsIndexResource.resName}
                </a></td>
                <td><a href="${ctx}/cms/category/form?id=${cmsIndexResource.cmsIndexRegion.category.id}">
                    <c:if test="${(not empty cmsIndexResource.cmsIndexRegion.category.parent.parent.parent) && (cmsIndexResource.cmsIndexRegion.category.parent.parent.parent.id ne '1')}">
                        ${cmsIndexResource.cmsIndexRegion.category.parent.parent.parent.name}/
                    </c:if>
                    <c:if test="${(not empty cmsIndexResource.cmsIndexRegion.category.parent.parent)  && (cmsIndexResource.cmsIndexRegion.category.parent.parent.id ne '1')}">
                        ${cmsIndexResource.cmsIndexRegion.category.parent.parent.name}/
                    </c:if>
                    <c:if test="${(not empty cmsIndexResource.cmsIndexRegion.category.parent)  && (cmsIndexResource.cmsIndexRegion.category.parent.id ne '1')}">
                        ${cmsIndexResource.cmsIndexRegion.category.parent.name}/
                    </c:if>
                        ${cmsIndexResource.cmsIndexRegion.category.name}
                </a></td>
                <td><a href="${ctx}/cms/cmsIndexRegion/form?id=${cmsIndexResource.cmsIndexRegion.id}" class="prj-name">
                        ${cmsIndexResource.cmsIndexRegion.regionName}
                </a></td>
                <td>
                        ${fns:getDictLabel(cmsIndexResource.resModel, 'region_model', '')}
                </td>
                <td>
                        ${fns:getDictLabel(cmsIndexResource.resState, 'resstate_flag', '')}
                </td>
                    <%-- <td>
                        ${cmsIndexResource.resSort}
                    </td> --%>
                <td>
                    <fmt:formatDate value="${cmsIndexResource.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <shiro:hasPermission name="cms:cmsIndexResource:edit">
                    <td>
                        <a href="${ctx}/cms/cmsIndexResource/form?id=${cmsIndexResource.id}"
                           class="btn btn-small btn-primary">修改</a>
                        <a href="${ctx}/cms/cmsIndexResource/delete?id=${cmsIndexResource.id}"
                           class="btn btn-default btn-small"
                           onclick="return confirmx('确认要删除该首页资源管理吗？', this.href)">删除</a>
                    </td>
                </shiro:hasPermission>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>

</body>
</html>