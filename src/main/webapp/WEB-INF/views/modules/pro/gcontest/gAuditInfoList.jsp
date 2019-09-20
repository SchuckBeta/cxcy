<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>大赛信息管理</title>
	<meta name="decorator" content="default"/>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/gcontest/gAuditInfo/">大赛信息列表</a></li>
		<shiro:hasPermission name="gcontest:gAuditInfo:edit"><li><a href="${ctx}/gcontest/gAuditInfo/form">大赛信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="gAuditInfo" action="${ctx}/gcontest/gAuditInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table  table-bordered table-condensed">
		<thead>
			<tr>
				<th>update_date</th>
				<shiro:hasPermission name="gcontest:gAuditInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gAuditInfo">
			<tr>
				<td><a href="${ctx}/gcontest/gAuditInfo/form?id=${gAuditInfo.id}">
					<fmt:formatDate value="${gAuditInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<shiro:hasPermission name="gcontest:gAuditInfo:edit"><td>
    				<a href="${ctx}/gcontest/gAuditInfo/form?id=${gAuditInfo.id}">修改</a>
					<a href="${ctx}/gcontest/gAuditInfo/delete?id=${gAuditInfo.id}" onclick="return confirmx('确认要删除该大赛信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>