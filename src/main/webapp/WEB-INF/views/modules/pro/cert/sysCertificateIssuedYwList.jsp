<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>证书业务记录管理</title>
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
		<span>证书业务记录</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysCertificateIssued/list">证书记录列表</a></li>
			<li><a href="${ctx}/sys/sysCertificateIssued/acceptList">证书获取记录列表</a></li>
			<li><a href="${ctx}/sys/sysCertificateIssued/issuedList">证书颁布记录列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysCertificateIssued/ywList">证书业务记录列表</a></li>
			<%-- <shiro:hasPermission name="sys:sysCertificateIssued:edit"><li><a href="${ctx}/sys/sysCertificateIssued/form">证书业务记录添加</a></li></shiro:hasPermission> --%>
		</ul>
		<form:form id="searchForm" modelAttribute="sysCertificateIssued" action="${ctx}/sys/sysCertificateIssued/ywList" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>证书：</label>
					<sys:treeselect id="sysCertRel" name="sysCertRel.id" value="${sysCertificateIssued.sysCertRel.id}" labelName="sysCertRel.sysCert.name" labelValue="${sysCertificateIssued.sysCertRel.sysCert.name}"
						title="证书" url="/sys/sysCertificate/tree?hasUse=true" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<li><label>执行人：</label>
					<sys:treeselect id="userIssuedBy" name="issuedBy.id" value="${sysCertificateIssued.issuedBy.id}" labelName="issuedBy.name" labelValue="${sysCertificateIssued.issuedBy.name}"
					title="执行用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<li><label>被执行人：</label>
					<sys:treeselect id="userAcceptBy" name="acceptBy.id" value="${sysCertificateIssued.acceptBy.id}" labelName="acceptBy.name" labelValue="${sysCertificateIssued.acceptBy.name}"
					title="执行用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<li><label>项目流程：</label>
					<sys:treeselect id="actYw" name="actYw.id" value="${sysCertificateIssued.actYw.id}" labelName="actYw.proProject.projectName" labelValue="${sysCertificateIssued.actYw.proProject.projectName}"
						title="项目流程" url="/actyw/actYw/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</li>
				<li><label>授予状态：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${sysCertIsstypes}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>业务</th>
					<th>被执行人</th>
					<th>证书</th>
					<th>授予状态</th>
					<th>执行人</th>
					<th>执行原因</th>
					<th>最后更新时间</th>
					<th>说明</th>
					<%-- <shiro:hasPermission name="sys:sysCertificateIssued:edit"><th>操作</th></shiro:hasPermission> --%>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysCertificateIssued">
				<tr>
					<%-- <td><a href="${ctx}/sys/sysCertificateIssued/form?id=${sysCertificateIssued.id}">
						${sysCertificateIssued.actYw.proProject.projectName}-${sysCertificateIssued.proModel.pName}
					</a></td> --%>
					<td>${sysCertificateIssued.actYw.proProject.projectName}-${sysCertificateIssued.proModel.pName}</td>
					<td>
						${sysCertificateIssued.acceptBy.name}
					</td>
					<td>
						${sysCertificateIssued.sysCertRel.sysCert.name}
					</td>
					<td>
						<c:forEach var="sysCertIsstype" items="${sysCertIsstypes }">
							<c:if test="${sysCertIsstype.key eq sysCertificateIssued.type}">
								${sysCertIsstype.name}
							</c:if>
						</c:forEach>
					</td>
					<td>
						${sysCertificateIssued.issuedBy.name}
					</td>
					<td>
						${sysCertificateIssued.reason}
					</td>
					<td>
						<fmt:formatDate value="${sysCertificateIssued.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${sysCertificateIssued.remarks}
					</td>
					<%-- <shiro:hasPermission name="sys:sysCertificateIssued:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateIssued/form?id=${sysCertificateIssued.id}">查看</a>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateIssued/form?id=${sysCertificateIssued.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificateIssued/delete?id=${sysCertificateIssued.id}" onclick="return confirmx('确认要删除该证书业务记录吗？', this.href)">删除</a>
					</td></shiro:hasPermission> --%>
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