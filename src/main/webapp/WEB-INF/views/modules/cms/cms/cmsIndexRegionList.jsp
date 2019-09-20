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
            <%--<span>区域管理</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="cmsIndexRegion" action="${ctx}/cms/cmsIndexRegion/" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">站点栏目</label>
                <div class="controls">
                    <sys:treeselect id="category" name="category.id"
                                    value="${cmsIndexRegion.category.id}" labelName="category.name"
                                    labelValue="${cmsIndexRegion.category.name}" title="栏目"
                                    url="/cms/category/treeData" extId="${category.id}"
                                    cssClass="required" allowClear="true" cssStyle="width:135px;"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">区域名</label>
                <div class="controls">
                    <form:input path="regionName" htmlEscape="false" maxlength="64" class="input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">区域模式</label>
                <div class="controls">
                    <form:select path="regionModel" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getDictList('region_model')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">区域类型</label>
                <div class="controls">
                    <form:select path="regionType" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getDictList('regiontype_flag')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button class="btn btn-primary" type="submit">查询</button>
            <a class="btn btn-primary" href="${ctx}/cms/cmsIndexRegion/form">添加</a>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <th width="30%">栏目</th>
            <th>区域名</th>
            <!-- <th>区域编号</th> -->
            <th>区域类型</th>
            <th>区域状态</th>
            <th>区域排序</th>
            <shiro:hasPermission name="cms:cmsIndexRegion:edit">
                <th width="120">操作</th>
            </shiro:hasPermission>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="cmsIndexRegion">
            <tr>
                <td><a href="${ctx}/cms/category/form?id=${cmsIndexRegion.category.id}">
                    <c:if test="${(not empty cmsIndexRegion.category.parent.parent.parent) && (cmsIndexRegion.category.parent.parent.parent.id ne '1')}">
                        ${cmsIndexRegion.category.parent.parent.parent.name}/
                    </c:if>
                    <c:if test="${(not empty cmsIndexRegion.category.parent.parent)  && (cmsIndexRegion.category.parent.parent.id ne '1')}">
                        ${cmsIndexRegion.category.parent.parent.name}/
                    </c:if>
                    <c:if test="${(not empty cmsIndexRegion.category.parent)  && (cmsIndexRegion.category.parent.id ne '1')}">
                        ${cmsIndexRegion.category.parent.name}/
                    </c:if>
                        ${cmsIndexRegion.category.name}
                </a></td>
                <td><a href="${ctx}/cms/category/form?id=${cmsIndexRegion.category.id}">
                        ${cmsIndexRegion.regionName}
                </a></td>
                    <%-- <td>
                    ${cmsIndexRegion.regionId}
                </td> --%>
                <td>
                        ${fns:getDictLabel(cmsIndexRegion.regionType, 'regiontype_flag', '')}
                </td>
                <td>
                        ${fns:getDictLabel(cmsIndexRegion.regionState, 'regionstate_flag', '')}
                </td>
                <td>
                        ${cmsIndexRegion.regionSort}
                </td>
                <shiro:hasPermission name="cms:cmsIndexRegion:edit">
                    <td style="white-space:nowrap">
                        <a href="${ctx}/cms/cmsIndexRegion/form?id=${cmsIndexRegion.id}" class="btn btn-primary btn-small">修改</a>
                        <a href="${ctx}/cms/cmsIndexRegion/delete?id=${cmsIndexRegion.id}" class="btn btn-default btn-small"
                           onclick="return confirmx('确认要删除该区域吗？', this.href)">删除</a>
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