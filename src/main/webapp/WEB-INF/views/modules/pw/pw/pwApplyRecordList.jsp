<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>pwApplyRecord</title>
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
				<span>pwApplyRecord</span> <i class="line weight-line"></i>
			</div>
		</div>
		<form:form id="searchForm" modelAttribute="pwApplyRecord" action="${ctx}/pw/pwApplyRecord/" method="post" class="form-horizontal clearfix form-search-block">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<div class="col-control-group">
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
					</div>
			</div>
			<div class="search-btn-box">
				<button type="submit" class="btn btn-primary">查询</button>
				<shiro:hasPermission name="dr:drEquipment:edit">
					<shiro:hasPermission name="pw:pwApplyRecord:edit"><a  class="btn btn-primary" href="${ctx}/pw/pwApplyRecord/form">pwApplyRecord添加</a></shiro:hasPermission>
				</shiro:hasPermission>

			</div>
		</form:form>
		<sys:message content="${message}"/>
			<table id="contentTable" class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
				<thead>
					<tr>
						<th>审核时间</th>
						<th>审核意见</th>
						<shiro:hasPermission name="pw:pwApplyRecord:edit"><th>操作</th></shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="pwApplyRecord">
					<tr>
						<td><a href="${ctx}/pw/pwApplyRecord/form?id=${pwApplyRecord.id}">
							<fmt:formatDate value="${pwApplyRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</a></td>
						<td>
							${pwApplyRecord.remarks}
						</td>
						<shiro:hasPermission name="pw:pwApplyRecord:edit"><td>
							<a class="btn btn-small btn-primary" href="${ctx}/pw/pwApplyRecord/form?id=${pwApplyRecord.id}">修改</a>
							<a class="btn btn-small btn-default" href="${ctx}/pw/pwApplyRecord/delete?id=${pwApplyRecord.id}" onclick="return confirmx('确认要删除该pwApplyRecord吗？', this.href)">删除</a>
						</td></shiro:hasPermission>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			${page.footer}
	</div>
</body>
</html>