<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>技能学分认定管理</title>
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
		<span>技能学分认定</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sco/scoAffirmSkill/">技能学分认定列表</a></li>
			<shiro:hasPermission name="sco:scoAffirmSkill:edit"><li><a href="${ctx}/sco/scoAffirmSkill/form">技能学分认定添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="scoAffirmSkill" action="${ctx}/sco/scoAffirmSkill/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li><label>认定项目名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="128" class="input-medium"/>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-bordered table-condensed">
			<thead>
				<tr>
					<th>认定项目名称</th>
					<th>更新时间</th>
					<th>备注信息</th>
					<shiro:hasPermission name="sco:scoAffirmSkill:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="scoAffirmSkill">
				<tr>
					<td><a href="${ctx}/sco/scoAffirmSkill/form?id=${scoAffirmSkill.id}">
						${scoAffirmSkill.name}
					</a></td>
					<td>
						<fmt:formatDate value="${scoAffirmSkill.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${scoAffirmSkill.remarks}
					</td>
					<shiro:hasPermission name="sco:scoAffirmSkill:edit"><td>
	    				<a href="${ctx}/sco/scoAffirmSkill/form?id=${scoAffirmSkill.id}">修改</a>
						<a href="${ctx}/sco/scoAffirmSkill/delete?id=${scoAffirmSkill.id}" onclick="return confirmx('确认要删除该技能学分认定吗？', this.href)">删除</a>
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