<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>teacherKeyword管理</title>
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
		<span>teacherKeyword</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/teacherkeyword/teacherKeyword/">teacherKeyword列表</a></li>
			<shiro:hasPermission name="sys:teacherkeyword:teacherKeyword:edit"><li><a href="${ctx}/sys/teacherkeyword/teacherKeyword/form">teacherKeyword添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="teacherKeyword" action="${ctx}/sys/teacherkeyword/teacherKeyword/" method="post" class="breadcrumb form-search">
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
					<th>更新时间</th>
					<shiro:hasPermission name="sys:teacherkeyword:teacherKeyword:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="teacherKeyword">
				<tr>
					<td><a href="${ctx}/sys/teacherkeyword/teacherKeyword/form?id=${teacherKeyword.id}">
						<fmt:formatDate value="${teacherKeyword.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</a></td>
					<shiro:hasPermission name="sys:teacherkeyword:teacherKeyword:edit"><td>
	    				<a href="${ctx}/sys/teacherkeyword/teacherKeyword/form?id=${teacherKeyword.id}">修改</a>
						<a href="${ctx}/sys/teacherkeyword/teacherKeyword/delete?id=${teacherKeyword.id}" onclick="return confirmx('确认要删除该teacherKeyword吗？', this.href)">删除</a>
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