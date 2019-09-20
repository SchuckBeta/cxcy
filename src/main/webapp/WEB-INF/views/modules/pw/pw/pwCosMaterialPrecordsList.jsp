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
		<span>耗材购买记录</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/pw/pwCosMaterialPrecords/">耗材购买记录列表</a></li>
			<shiro:hasPermission name="pw:pwCosMaterialPrecords:edit"><li><a href="${ctx}/pw/pwCosMaterialPrecords/form">耗材购买记录添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="pwCosMaterialPrecords" action="${ctx}/pw/pwCosMaterialPrecords/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>耗材编号：</label>
				</li>
				<li><label>购买人姓名：</label>
					<form:input path="prname" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>电话：</label>
					<form:input path="phone" htmlEscape="false" maxlength="200" class="input-medium"/>
				</li>
				<li><label>手机：</label>
					<form:input path="mobile" htmlEscape="false" maxlength="200" class="input-medium"/>
				</li>
				<li><label>购买日期：</label>
					<input name="time" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${pwCosMaterialPrecords.time}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>耗材编号</th>
					<th>购买人姓名</th>
					<th>电话</th>
					<th>手机</th>
					<th>购买日期</th>
					<th>购买数量</th>
					<th>单价</th>
					<th>总价</th>
					<th>最后更新时间</th>
					<th>备注</th>
					<shiro:hasPermission name="pw:pwCosMaterialPrecords:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="pwCosMaterialPrecords">
				<tr>
					<td><a href="${ctx}/pw/pwCosMaterialPrecords/form?id=${pwCosMaterialPrecords.id}">
						${pwCosMaterialPrecords.cmid}
					</a></td>
					<td>
						${pwCosMaterialPrecords.prname}
					</td>
					<td>
						${pwCosMaterialPrecords.phone}
					</td>
					<td>
						${pwCosMaterialPrecords.mobile}
					</td>
					<td>
						<fmt:formatDate value="${pwCosMaterialPrecords.time}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${pwCosMaterialPrecords.num}
					</td>
					<td>
						${pwCosMaterialPrecords.price}
					</td>
					<td>
						${pwCosMaterialPrecords.totalPrice}
					</td>
					<td>
						<fmt:formatDate value="${pwCosMaterialPrecords.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${pwCosMaterialPrecords.remarks}
					</td>
					<shiro:hasPermission name="pw:pwCosMaterialPrecords:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwCosMaterialPrecords/form?id=${pwCosMaterialPrecords.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwCosMaterialPrecords/delete?id=${pwCosMaterialPrecords.id}" onclick="return confirmx('确认要删除该耗材购买记录吗？', this.href)">删除</a>
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