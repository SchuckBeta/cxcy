<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统机构编号管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {

		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<div class="mybreadcrumbs">
		<span>系统机构编号</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysNo/">系统全局编号列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysNoOffice/">系统机构编号列表</a></li>
			<shiro:hasPermission name="sys:sysNo:edit"><li><a href="${ctx}/sys/sysNo/form">系统全局编号添加</a></li></shiro:hasPermission>
			<%-- <shiro:hasPermission name="sys:sysNoOffice:edit"><li><a href="${ctx}/sys/sysNoOffice/form">系统机构编号添加</a></li></shiro:hasPermission> --%>
		</ul>
		<form:form id="searchForm" modelAttribute="sysNoOffice" action="${ctx}/sys/sysNoOffice/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<%-- <li><label>机构ID：</label>
					<form:input path="sysNo.id" htmlEscape="false" maxlength="64" class="input-medium"/>
				</li> --%>
				<li><label>机构ID：</label>
					<sys:treeselect id="office" name="office.id" value="${sysNoOffice.office.id}" labelName="office.name" labelValue="${sysNoOffice.office.name}"
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<%-- <li><label>编号最大值：</label>
					<form:input path="maxVal" htmlEscape="false" maxlength="11" class="input-medium"/>
				</li> --%>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-bordered table-condensed">
			<thead>
				<tr>
					<th>机构ID</th>
					<th>机构ID</th>
					<th>编号最大值</th>
					<th>update_date</th>
					<th>create_by</th>
					<shiro:hasPermission name="sys:sysNoOffice:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysNoOffice">
				<tr>
					<td><a href="${ctx}/sys/sysNoOffice/form?id=${sysNoOffice.id}">
						${sysNoOffice.sysNo.id}
					</a></td>
					<td>
						${sysNoOffice.office.name}
					</td>
					<td>
						${sysNoOffice.maxVal}
					</td>
					<td>
						<fmt:formatDate value="${sysNoOffice.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${sysNoOffice.remarks}
					</td>
					<shiro:hasPermission name="sys:sysNoOffice:edit"><td>
	    				<a href="${ctx}/sys/sysNoOffice/form?id=${sysNoOffice.id}">修改</a>
						<a href="${ctx}/sys/sysNoOffice/delete?id=${sysNoOffice.id}" onclick="return confirmx('确认要删除该系统机构编号吗？', this.href)">删除</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>