<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${backgroundTitle}</title>
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
		<span>账单</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/pw/pwBill/">账单列表</a></li>
			<shiro:hasPermission name="pw:pwBill:edit"><li><a href="${ctx}/pw/pwBill/form">账单添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="pwBill" action="${ctx}/pw/pwBill/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>账单名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>账单类型：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('pw_bill_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>状态：</label>
					<form:select path="status" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('pw_bill_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>团队编号：</label>
					<form:input path="teamId" htmlEscape="false" maxlength="64" class="input-medium"/>
				</li>
				<li><label>账单周期起：</label>
					<input name="cfromDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${pwBill.cfromDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</li>
				<li><label>账单周期止：</label>
					<input name="ctoDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${pwBill.ctoDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</li>
				<li><label>是否结清：</label>
					<form:select path="settled" class="input-medium">
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
					<th>账单名称</th>
					<th>总金额</th>
					<th>账单类型</th>
					<th>状态</th>
					<th>团队编号</th>
					<th>账单周期起</th>
					<th>账单周期止</th>
					<th>是否结清</th>
					<th>最后更新时间</th>
					<th>备注</th>
					<shiro:hasPermission name="pw:pwBill:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="pwBill">
				<tr>
					<td><a href="${ctx}/pw/pwBill/form?id=${pwBill.id}">
						${pwBill.name}
					</a></td>
					<td>
						${pwBill.totalAmount}
					</td>
					<td>
						${fns:getDictLabel(pwBill.type, 'pw_bill_type', '')}
					</td>
					<td>
						${fns:getDictLabel(pwBill.status, 'pw_bill_status', '')}
					</td>
					<td>
						${pwBill.teamId}
					</td>
					<td>
						<fmt:formatDate value="${pwBill.cfromDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${pwBill.ctoDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${fns:getDictLabel(pwBill.settled, 'yes_no', '')}
					</td>
					<td>
						<fmt:formatDate value="${pwBill.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${pwBill.remarks}
					</td>
					<shiro:hasPermission name="pw:pwBill:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwBill/form?id=${pwBill.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwBill/delete?id=${pwBill.id}" onclick="return confirmx('确认要删除该账单吗？', this.href)">删除</a>
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