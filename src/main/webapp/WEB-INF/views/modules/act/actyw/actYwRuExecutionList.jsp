<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程运行实例管理</title>
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
		<span>流程运行实例</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/query/actYwRuExecution/">流程运行实例列表</a></li>
			<shiro:hasPermission name="query:actYwRuExecution:edit"><li><a href="${ctx}/query/actYwRuExecution/form">流程运行实例添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="actYwRuExecution" action="${ctx}/query/actYwRuExecution/" method="post" class="breadcrumb form-search">
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
				<li><label>运行中</label>
					<form:select path="isActive" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:option value="1" label="是"/>
						<form:option value="0" label="否"/>
					</form:select>
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
					<th>parent_id_</th>
					<th>rev_</th>
					<th>business_key_</th>
					<th>parent_id_</th>
					<th>super_exec_</th>
					<th>act_id_</th>
					<th>is_active_</th>
					<th>is_concurrent_</th>
					<th>is_scope_</th>
					<th>is_event_scope_</th>
					<th>suspension_state_</th>
					<th>cached_ent_state_</th>
					<th>tenant_id_</th>
					<th>name_</th>
					<th>lock_time_</th>
					<shiro:hasPermission name="query:actYwRuExecution:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="actYwRuExecution">
				<tr>
					<td>${actYwRuExecution.projectDeclare.name }</td>
					<td>${actYwRuExecution.projectDeclare.status }</td>
					<td>
						${actYwRuExecution.procInstId}
					</td>
					<td>
						${actYwRuExecution.procDefId}
					</td>
					<td><a href="${ctx}/query/actYwRuExecution/form?id=${actYwRuExecution.parentId}">
						${actYwRuExecution.parent.id}
					</a></td>
					<td><a href="${ctx}/query/actYwRuExecution/form?id=${actYwRuExecution.id}">
						${actYwRuExecution.rev}
					</a></td>
					<td>
						${actYwRuExecution.businessKey}
					</td>
					<td>
						${actYwRuExecution.superExec}
					</td>
					<td>
						${actYwRuExecution.actId}
					</td>
					<td>
						${actYwRuExecution.isActive}
					</td>
					<td>
						${actYwRuExecution.isConcurrent}
					</td>
					<td>
						${actYwRuExecution.isScope}
					</td>
					<td>
						${actYwRuExecution.isEventScope}
					</td>
					<td>
						${actYwRuExecution.suspensionState}
					</td>
					<td>
						${actYwRuExecution.cachedEntState}
					</td>
					<td>
						${actYwRuExecution.tenantId}
					</td>
					<td>
						${actYwRuExecution.name}
					</td>
					<td>
						<fmt:formatDate value="${actYwRuExecution.lockTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<shiro:hasPermission name="query:actYwRuExecution:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwRuExecution/form?id=${actYwRuExecution.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwRuExecution/delete?id=${actYwRuExecution.id}" onclick="return confirmx('确认要删除该流程运行实例吗？', this.href)">删除</a>
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