<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程历史任务管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#ps").val($("#pageSize").val());
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
		<span>流程历史任务</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/query/actYwHiTaskinst/">流程历史任务列表</a></li>
			<shiro:hasPermission name="query:actYwHiTaskinst:edit"><li><a href="${ctx}/query/actYwHiTaskinst/form">流程历史任务添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="actYwHiTaskinst" action="${ctx}/query/actYwHiTaskinst/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li><label>项目</label>
					<form:input path="projectDeclare.name" class="input-medium" />
				</li>
				<li><label>流程实例</label>
					<form:input path="procInstId" class="input-medium" />
				</li>
				<li><label>流程模型</label>
					<form:input path="procDefId" class="input-medium" />
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-bordered table-condensed">
			<thead>
				<tr>
					<th>项目</th>
					<th>项目状态</th>
					<th>流程实例</th>
					<th>流程模型</th>
					<th>task_def_key_</th>
					<th>execution_id_</th>
					<th>name_</th>
					<th>parent_task_id_</th>
					<th>description_</th>
					<th>owner_</th>
					<th>assignee_</th>
					<th>start_time_</th>
					<th>claim_time_</th>
					<th>end_time_</th>
					<th>duration_</th>
					<th>delete_reason_</th>
					<th>priority_</th>
					<th>due_date_</th>
					<th>form_key_</th>
					<th>category_</th>
					<th>tenant_id_</th>
					<shiro:hasPermission name="query:actYwHiTaskinst:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="actYwHiTaskinst">
				<tr>
					<td>${actYwHiTaskinst.projectDeclare.name }</td>
					<td>${actYwHiTaskinst.projectDeclare.status }</td>
					<td>
						${actYwHiTaskinst.procInstId}
					</td>
					<td><a href="${ctx}/query/actYwHiTaskinst/form?id=${actYwHiTaskinst.id}">
						${actYwHiTaskinst.procDefId}
					</a></td>
					<td>
						${actYwHiTaskinst.taskDefKey}
					</td>
					<td>
						${actYwHiTaskinst.executionId}
					</td>
					<td>
						${actYwHiTaskinst.name}
					</td>
					<td>
						${actYwHiTaskinst.parentTaskId}
					</td>
					<td>
						${actYwHiTaskinst.description}
					</td>
					<td>
						${actYwHiTaskinst.owner}
					</td>
					<td>
						${actYwHiTaskinst.assignee}
					</td>
					<td>
						<fmt:formatDate value="${actYwHiTaskinst.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${actYwHiTaskinst.claimTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${actYwHiTaskinst.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${actYwHiTaskinst.duration}
					</td>
					<td>
						${actYwHiTaskinst.deleteReason}
					</td>
					<td>
						${actYwHiTaskinst.priority}
					</td>
					<td>
						<fmt:formatDate value="${actYwHiTaskinst.dueDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${actYwHiTaskinst.formKey}
					</td>
					<td>
						${actYwHiTaskinst.category}
					</td>
					<td>
						${actYwHiTaskinst.tenantId}
					</td>
					<shiro:hasPermission name="query:actYwHiTaskinst:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwHiTaskinst/form?id=${actYwHiTaskinst.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwHiTaskinst/delete?id=${actYwHiTaskinst.id}" onclick="return confirmx('确认要删除该流程历史任务吗？', this.href)">删除</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		${page.footer}
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>