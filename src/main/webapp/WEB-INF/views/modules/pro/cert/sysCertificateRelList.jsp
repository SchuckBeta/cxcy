<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书资源关联管理</title>
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
		<span>系统证书资源关联</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/sysCertificateRel/">系统证书资源关联列表</a></li>
			<shiro:hasPermission name="sys:sysCertificateRel:edit"><li><a href="${ctx}/sys/sysCertificateRel/form">系统证书资源关联添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="sysCertificateRel" action="${ctx}/sys/sysCertificateRel/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>证书：</label>
					<sys:treeselect id="sysCert" name="sysCert.id" value="${sysCertificateRel.sysCert.id}" labelName="sysCert.name" labelValue="${sysCertificateRel.sysCert.name}"
						title="证书" url="/sys/sysCertificate/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<li><label>证书资源：</label>
					<sys:treeselect id="sysCertRes" name="sysCertRes.id" value="${sysCertificateRel.sysCertRes.id}" labelName="sysCertRes.name" labelValue="${sysCertificateRel.sysCertRes.name}"
						title="证书资源" url="/sys/sysCertificateRes/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<li><label>类型：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${wrelType}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>资源类型：</label>
					<form:select path="resType" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${wresType}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>证书</th>
					<th>资源</th>
					<th>类型</th>
					<th>资源类型</th>
					<th>属性</th>
					<th>类</th>
					<th>最后更新时间</th>
					<th>说明</th>
					<shiro:hasPermission name="sys:sysCertificateRel:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysCertificateRel">
				<tr>
					<td><a href="${ctx}/sys/sysCertificateRel/form?id=${sysCertificateRel.id}">
						${sysCertificateRel.sysCert.name}
					</a></td>
					<td>
						${sysCertificateRel.sysCertRes.name}
					</td>
					<td>
						<c:forEach var="wreltype" items="${wrelType }">
							<c:if test="${sysCertificateRel.type eq wreltype.key}">${wreltype.name}</c:if>
						</c:forEach>
					</td>
					<td>
						<c:forEach var="wresType" items="${wresType }">
							<c:if test="${sysCertificateRel.resType eq wresType.key}">${wresType.name}</c:if>
						</c:forEach>
					</td>
					<td>
						${sysCertificateRel.resClazz}
					</td>
					<td>
						${sysCertificateRel.resClazzProp}
					</td>
					<td>
						<fmt:formatDate value="${sysCertificateRel.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${sysCertificateRel.remarks}
					</td>
					<shiro:hasPermission name="sys:sysCertificateRel:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateRel/form?id=${sysCertificateRel.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateRel/delete?id=${sysCertificateRel.id}" onclick="return confirmx('确认要删除该系统证书资源关联吗？', this.href)">删除</a>
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