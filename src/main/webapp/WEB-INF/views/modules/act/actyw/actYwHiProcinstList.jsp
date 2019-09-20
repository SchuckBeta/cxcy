<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程历史实例管理</title>
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
		<span>流程历史实例</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/query/actYwHiProcinst/">流程历史实例列表</a></li>
			<shiro:hasPermission name="query:actYwHiProcinst:edit"><li><a href="${ctx}/query/actYwHiProcinst/form">流程历史实例添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="actYwHiProcinst" action="${ctx}/query/actYwHiProcinst/" method="post" class="breadcrumb form-search">
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
					<th>business_key_</th>
					<th>start_time_</th>
					<th>end_time_</th>
					<th>duration_</th>
					<th>start_user_id_</th>
					<th>start_act_id_</th>
					<th>end_act_id_</th>
					<th>super_process_instance_id_</th>
					<th>delete_reason_</th>
					<th>tenant_id_</th>
					<shiro:hasPermission name="query:actYwHiProcinst:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="actYwHiProcinst">
				<tr>
					<td>${actYwHiProcinst.projectDeclare.name }</td>
					<td>${actYwHiProcinst.projectDeclare.status }</td>
					<td><a href="${ctx}/query/actYwHiProcinst/form?id=${actYwHiProcinst.id}">
						${actYwHiProcinst.procInstId}
					</a></td>
					<td>
						${actYwHiProcinst.procDefId}
					</td>
					<td>
						${actYwHiProcinst.businessKey}
					</td>
					<td>
						<fmt:formatDate value="${actYwHiProcinst.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${actYwHiProcinst.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${actYwHiProcinst.duration}
					</td>
					<td>
						${actYwHiProcinst.startUserId}
					</td>
					<td>
						${actYwHiProcinst.startActId}
					</td>
					<td>
						${actYwHiProcinst.endActId}
					</td>
					<td>
						${actYwHiProcinst.superProcessInstanceId}
					</td>
					<td>
						${actYwHiProcinst.deleteReason}
					</td>
					<td>
						${actYwHiProcinst.tenantId}
					</td>
					<shiro:hasPermission name="query:actYwHiProcinst:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwHiProcinst/form?id=${actYwHiProcinst.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/query/actYwHiProcinst/delete?id=${actYwHiProcinst.id}" onclick="return confirmx('确认要删除该流程历史实例吗？', this.href)">删除</a>
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