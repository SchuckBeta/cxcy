<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的大赛--审核中</title>
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
		<li class="active"><a href="${ctx}/oa/dasai/auditedList">审核中</a></li>
		<li><a href="${ctx}/oa/dasai/auditedList">审核通过</a></li>
	</ul><br/>

	<form:form id="searchForm" modelAttribute="oaDasai" action="${ctx}/dasai/oaDasai/auditingList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>项目名称</th>
				<th>报名时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaDasai">
			<tr>
				<td>
					${oaDasai.projectName}
				</td>
				<td>
					<fmt:formatDate value="${oaDasai.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<a href="${ctx}/dasai/oaDasai/form?id=${oaDasai.procInsId}">详情</a>
					<a target="_blank" href="${ctx}/act/task/processMap?procDefId=${oaDasai.act.procDefId}&proInstId=${oaDasai.procInsId}">跟踪</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>