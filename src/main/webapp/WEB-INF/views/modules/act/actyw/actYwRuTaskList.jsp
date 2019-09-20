<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程运行任务管理</title>
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
		<span>流程运行任务</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/query/actYwRuTask/">流程运行任务列表</a></li>
			<shiro:hasPermission name="query:actYwRuTask:edit"><li><a href="${ctx}/query/actYwRuTask/form">流程运行任务添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="actYwRuTask" action="${ctx}/query/actYwRuTask/" method="post" class="breadcrumb form-search">
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
					<th>rev_</th>
					<th>execution_id_</th>
					<th>name_</th>
					<th>parent_task_id_</th>
					<th>description_</th>
					<th>task_def_key_</th>
					<th>owner_</th>
					<th>assignee_</th>
					<th>delegation_</th>
					<th>priority_</th>
					<th>create_time_</th>
					<th>due_date_</th>
					<th>category_</th>
					<th>suspension_state_</th>
					<th>tenant_id_</th>
					<th>form_key_</th>
					<shiro:hasPermission name="query:actYwRuTask:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="actYwRuTask">
				<tr>
					<td>${actYwRuTask.projectDeclare.name }</td>
					<td>${actYwRuTask.projectDeclare.status }</td>
					<td>
						${actYwRuTask.procInstId}
					</td>
					<td>
						${actYwRuTask.procDefId}
					</td>
					<td><a href="${ctx}/query/actYwRuTask/form?id=${actYwRuTask.id}">
						${actYwRuTask.rev}
					</a></td>
					<td>
						${actYwRuTask.executionId}
					</td>
					<td>
						${actYwRuTask.name}
					</td>
					<td>
						${actYwRuTask.parentTaskId}
					</td>
					<td>
						${actYwRuTask.description}
					</td>
					<td>
						${actYwRuTask.taskDefKey}
					</td>
					<td>
						${actYwRuTask.owner}
					</td>
					<td>
						${actYwRuTask.assignee}
					</td>
					<td>
						${actYwRuTask.delegation}
					</td>
					<td>
						${actYwRuTask.priority}
					</td>
					<td>
						<fmt:formatDate value="${actYwRuTask.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${actYwRuTask.dueDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${actYwRuTask.category}
					</td>
					<td>
						${actYwRuTask.suspensionState}
					</td>
					<td>
						${actYwRuTask.tenantId}
					</td>
					<td>
						${actYwRuTask.formKey}
					</td>
					<shiro:hasPermission name="query:actYwRuTask:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwRuTask/form?id=${actYwRuTask.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwRuTask/delete?id=${actYwRuTask.id}" onclick="return confirmx('确认要删除该流程运行任务吗？', this.href)">删除</a>
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