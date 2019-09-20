<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>大赛评分列表</title>
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
	<form:form id="searchForm" modelAttribute="act" action="${ctx}/oa/dasai/markList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		项目名称:<form:input path="map['projectName']" />
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
				<th>报名人</th>
				<th>报名时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="act">
			<c:set var="task" value="${act.task}" />
			<c:set var="vars" value="${act.vars}" />
			<c:set var="procDef" value="${act.procDef}" />
			<c:set var="status" value="${act.status}" />
			<tr>
				<td>
					${act.vars.map.projectName}
				</td>
				<td>
				   ${act.vars.map.sumbitter}
				</td>
				<td>
					<fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				  <%--  ${task.createTime}--%>
				</td>
				<td>
    				<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">评分</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>