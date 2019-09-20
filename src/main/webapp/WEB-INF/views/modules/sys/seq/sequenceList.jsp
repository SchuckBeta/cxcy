<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>序列表</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>

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
	<div class="container-fluid">
		<div class="edit-bar clearfix">
			<div class="edit-bar-left">
				<span>序列表</span> <i class="line weight-line"></i>
			</div>
		</div>
		<form:form id="searchForm" modelAttribute="sequence" action="${ctx}/seq/sequence/" method="post" class="form-horizontal clearfix form-search-block">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<div class="col-control-group">
				<div>
					<div class="control-group">
						<label class="control-label">序列名称</label>
						<div class="controls">
						<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
						</div>
					</div>
				</div>
			</div>
			<div class="search-btn-box">
				<button type="submit" class="btn btn-primary">查询</button>
				<a  class="btn btn-primary" href="${ctx}/seq/sequence/form">序列表添加</a>
			</div>
		</form:form>
		<sys:message content="${message}"/>
			<table id="contentTable" class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
				<thead>
					<tr>
						<th>序列名称</th>
						<th>序列值</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="sequence">
					<tr>
						<td><a href="${ctx}/seq/sequence/form?id=${sequence.id}">
							${sequence.name}
						</a></td>
						<td>
							${sequence.currentValue}
						</td>
						<td>
							<a class="btn btn-small btn-primary" href="${ctx}/seq/sequence/getNextSeq?name=${sequence.name}">next</a>
							<a class="btn btn-small btn-primary" href="${ctx}/seq/sequence/form?id=${sequence.id}">修改</a>
							<a class="btn btn-small btn-default" href="${ctx}/seq/sequence/delete?id=${sequence.id}" onclick="return confirmx('确认要删除该序列表吗？', this.href)">删除</a>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			${page.footer}
	</div>
</body>
</html>