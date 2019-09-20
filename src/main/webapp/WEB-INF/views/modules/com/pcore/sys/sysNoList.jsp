<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统全局编号管理</title>
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
		<span>系统全局编号</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/sysNo/">系统全局编号列表</a></li>
			<li><a href="${ctx}/sys/sysNoOffice/">系统机构编号列表</a></li>
			<shiro:hasPermission name="sys:sysNo:edit"><li><a href="${ctx}/sys/sysNo/form">系统全局编号添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="sysNo" action="${ctx}/sys/sysNo/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li><label>名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>编号标识：</label>
					<form:input path="keyss" htmlEscape="false" maxlength="11" class="input-medium"/>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table  table-bordered table-condensed">
			<thead>
				<tr>
					<th>编号处理类</th>
					<th>标识</th>
					<th>处理类</th>
					<th>前缀</th>
					<th>全局编号最大值</th>
					<th>编号位数</th>
					<th>后缀</th>
					<th>格式</th>
					<th>最后更新时间</th>
					<th>编号功能说明</th>
					<%-- <shiro:hasPermission name="sys:sysNo:edit"><th>操作</th></shiro:hasPermission> --%>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysNo">
				<tr>
					<td><a href="${ctx}/sys/sysNo/form?id=${sysNo.id}">
						${sysNo.name}
					</a></td>
					<td>
						${sysNo.keyss}
					</td>
					<td>
						${sysNo.clazz}
					</td>
					<td>
						${sysNo.prefix}
					</td>
					<td>
						${sysNo.sysmaxVal}
					</td>
					<td>
						${sysNo.maxByte}
					</td>
					<td>
						${sysNo.postfix}
					</td>
					<td>
						${sysNo.format}
					</td>
					<td>
						<fmt:formatDate value="${sysNo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${sysNo.remarks}
					</td>
					<%-- <shiro:hasPermission name="sys:sysNo:edit"><td>
	    				<a href="${ctx}/sys/sysNo/form?id=${sysNo.id}">修改</a>
						<a href="${ctx}/sys/sysNo/delete?id=${sysNo.id}" onclick="return confirmx('确认要删除该系统全局编号吗？', this.href)">删除</a>
					</td></shiro:hasPermission> --%>
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