<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>门禁设备场地管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<style>
		.table-thead-bg thead tr{
			background-color: #f4e6d4;
		}
		.table th{
			background: none;
		}
		#searchForm{
			height: auto;
			padding: 15px 0 0;
			overflow: hidden;
		}
		.ul-form input[type="text"]{
			height: 20px;
		}
		.ul-form select{
			height: 30px;
			width: 174px;
			max-width: 174px;
		}
		.form-search .ul-form li{
			margin-bottom: 15px;
		}
		.form-search .ul-form li.btns{
			float: right;
		}
		.table{
			margin-bottom: 20px;
		}
	</style>
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
		<span>门禁设备场地</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/dr/drEquipmentRspace/">门禁设备场地列表</a></li>
			<shiro:hasPermission name="dr:drEquipmentRspace:edit"><li><a href="${ctx}/dr/drEquipmentRspace/form">门禁设备场地添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="drEquipmentRspace" action="${ctx}/dr/drEquipmentRspace/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>类型 ： 房间 楼栋  基地：</label>
					<form:input path="rspType" htmlEscape="false" maxlength="1" class="input-medium"/>
				</li>
				<li><label>设备号：</label>
					<form:select path="rspace.id" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>设备ID</th>
					<th>类型 ： 房间 楼栋  基地</th>
					<th>设备号</th>
					<shiro:hasPermission name="dr:drEquipmentRspace:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="drEquipmentRspace">
				<tr>
					<td><a href="${ctx}/dr/drEquipmentRspace/form?id=${drEquipmentRspace.id}">
						${fns:getDictLabel(drEquipmentRspace.epment.id, '', '')}
					</a></td>
					<td>
						${drEquipmentRspace.rspType}
					</td>
					<td>
						${fns:getDictLabel(drEquipmentRspace.rspace.id, '', '')}
					</td>
					<shiro:hasPermission name="dr:drEquipmentRspace:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/dr/drEquipmentRspace/form?id=${drEquipmentRspace.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/dr/drEquipmentRspace/delete?id=${drEquipmentRspace.id}" onclick="return confirmx('确认要删除该门禁设备场地吗？', this.href)">删除</a>
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