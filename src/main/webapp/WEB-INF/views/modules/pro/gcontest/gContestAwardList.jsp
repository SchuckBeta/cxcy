<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>大赛获奖表管理</title>
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
		<li class="active"><a href="${ctx}/gcontest/gContestAward/">大赛获奖表列表</a></li>
		<shiro:hasPermission name="gcontest:gContestAward:edit"><li><a href="${ctx}/gcontest/gContestAward/form">大赛获奖表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="gContestAward" action="${ctx}/gcontest/gContestAward/" method="post" class="breadcrumb form-search">
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
				<shiro:hasPermission name="gcontest:gContestAward:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gContestAward">
			<tr>
				<td><a href="${ctx}/gcontest/gContestAward/form?id=${gContestAward.id}">
					<fmt:formatDate value="${gContestAward.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<shiro:hasPermission name="gcontest:gContestAward:edit"><td>
    				<a href="${ctx}/gcontest/gContestAward/form?id=${gContestAward.id}">修改</a>
					<a href="${ctx}/gcontest/gContestAward/delete?id=${gContestAward.id}" onclick="return confirmx('确认要删除该大赛获奖表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>