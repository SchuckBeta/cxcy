<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>评论点赞</title>
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
				<span>评论点赞</span> <i class="line weight-line"></i>
			</div>
		</div>
		<form:form id="searchForm" modelAttribute="cmsConmmentLikes" action="${ctx}/cms/cmsConmmentLikes/" method="post" class="form-horizontal clearfix form-search-block">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<div class="col-control-group">
						<div class="control-group">
							<label class="control-label">点赞人</label>
							<div class="controls">
							<sys:treeselect id="uid" name="uid" value="${cmsConmmentLikes.uid}" labelName="" labelValue="${cmsConmmentLikes.}"
								title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
						</div>
					</div>
						<div class="control-group">
							<label class="control-label">栏目编号</label>
							<div class="controls">
							<sys:treeselect id="pid" name="pid" value="${cmsConmmentLikes.pid}" labelName="" labelValue="${cmsConmmentLikes.}"
								title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
						</div>
					</div>
			</div>
			<div class="search-btn-box">
				<button type="submit" class="btn btn-primary">查询</button>
				<shiro:hasPermission name="dr:drEquipment:edit">
					<shiro:hasPermission name="cms:cmsConmmentLikes:edit"><a  class="btn btn-primary" href="${ctx}/cms/cmsConmmentLikes/form">评论点赞添加</a></shiro:hasPermission>
				</shiro:hasPermission>

			</div>
		</form:form>
		<sys:message content="${message}"/>
			<table id="contentTable" class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
				<thead>
					<tr>
						<th>点赞人</th>
						<th>栏目编号</th>
						<shiro:hasPermission name="cms:cmsConmmentLikes:edit"><th>操作</th></shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="cmsConmmentLikes">
					<tr>
						<td><a href="${ctx}/cms/cmsConmmentLikes/form?id=${cmsConmmentLikes.id}">
							${cmsConmmentLikes.}
						</a></td>
						<td>
							${cmsConmmentLikes.}
						</td>
						<shiro:hasPermission name="cms:cmsConmmentLikes:edit"><td>
							<a class="btn btn-small btn-primary" href="${ctx}/cms/cmsConmmentLikes/form?id=${cmsConmmentLikes.id}">修改</a>
							<a class="btn btn-small btn-default" href="${ctx}/cms/cmsConmmentLikes/delete?id=${cmsConmmentLikes.id}" onclick="return confirmx('确认要删除该评论点赞吗？', this.href)">删除</a>
						</td></shiro:hasPermission>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			${page.footer}
	</div>
</body>
</html>