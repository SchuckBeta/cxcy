<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书资源管理</title>
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
		<span>系统证书资源</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/sysCertificateRes/">系统证书资源列表</a></li>
			<shiro:hasPermission name="sys:sysCertificateRes:edit"><li><a href="${ctx}/sys/sysCertificateRes/form">系统证书资源添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="sysCertificateRes" action="${ctx}/sys/sysCertificateRes/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>资源类型：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${wresFtype}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>资源名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>是否显示：</label>
					<form:select path="isShow" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>资源类型</th>
					<th>资源名称</th>
					<th>宽度</th>
					<th>高度</th>
					<th>X坐标</th>
					<th>Y坐标</th>
					<th>透明度</th>
					<th>角度</th>
					<th>是否平铺</th>
					<th>是否显示</th>
					<th>最后更新时间</th>
					<th>说明</th>
					<shiro:hasPermission name="sys:sysCertificateRes:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysCertificateRes">
				<tr>
					<td><a href="${ctx}/sys/sysCertificateRes/form?id=${sysCertificateRes.id}">
						${sysCertificateRes.name}
					</a></td>
					<td>
						<c:forEach var="wrftype" items="${wresFtype }">
							<c:if test="${sysCertificateRes.type eq wrftype.key}">${wrftype.name}</c:if>
						</c:forEach>
					</td>
					<td>
						${sysCertificateRes.width}
					</td>
					<td>
						${sysCertificateRes.height}
					</td>
					<td>
						${sysCertificateRes.xlt}
					</td>
					<td>
						${sysCertificateRes.ylt}
					</td>
					<td>
						${sysCertificateRes.opacity}
					</td>
					<td>
						${sysCertificateRes.rate}
					</td>
					<td>
						${fns:getDictLabel(sysCertificateRes.hasLoop, 'yes_no', '')}
					</td>
					<td>
						${fns:getDictLabel(sysCertificateRes.isShow, 'yes_no', '')}
					</td>
					<td>
						<fmt:formatDate value="${sysCertificateRes.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${sysCertificateRes.remarks}
					</td>
					<shiro:hasPermission name="sys:sysCertificateRes:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateRes/form?id=${sysCertificateRes.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateRes/delete?id=${sysCertificateRes.id}" onclick="return confirmx('确认要删除该系统证书资源吗？', this.href)">删除</a>
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