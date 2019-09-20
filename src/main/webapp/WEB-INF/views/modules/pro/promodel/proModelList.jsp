<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>proModel管理</title>
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
		<span>proModel</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/promodel/proModel/">proModel列表</a></li>
			<shiro:hasPermission name="promodel:proModel:edit"><li><a href="${ctx}/promodel/proModel/form">proModel添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/" method="post" class="breadcrumb form-search">
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
					<shiro:hasPermission name="promodel:proModel:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="proModel">
				<tr>
					<td><a href="${ctx}/promodel/proModel/form?id=${proModel.id}">
						<fmt:formatDate value="${proModel.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</a></td>
					<shiro:hasPermission name="promodel:proModel:edit"><td>
	    				<a href="${ctx}/promodel/proModel/form?id=${proModel.id}">修改</a>
						<a href="${ctx}/promodel/proModel/delete?id=${proModel.id}" onclick="return confirmx('确认要删除该proModel吗？', this.href)">删除</a>
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