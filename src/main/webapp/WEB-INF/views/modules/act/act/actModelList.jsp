<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>模型管理</title>
	<%@include file="/WEB-INF/views/include/backCommon.jsp" %>
	<%--<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>--%>
	<%--<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>--%>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			$("#ps").val($("#pageSize").val());
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", buttons:{"关闭":true}, submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/model/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search" onsubmit="loading('正在分类，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="id" value="" />
			<select id="categoryBoxCategory" name="category">
				<option value="">无分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" style="position: absolute; top: 75%;left: 55%;" value="   保    存   "/>　　
		</form>
	</script>
	<style>
		html,body{
			font-size: 14px;
			font-family: "微软雅黑";
		}
		.process-wrap{
			margin: 27px 27px 0 27px;
		}

		.m-form-search{
			border-left: 1px solid #ddd; border-right: 1px solid #ddd;  margin-bottom: 0px; padding: 15px 15px;  margin-top: 0px; border-radius: 0px;
		}

		.table-nowrap td{
			max-width: 110px !important;
		}
		.breadcrumb{
			background-color: #fff;
		}
		.form-search{
			position: relative;
		}
		.btn-primary{
			background: #e9432d;
			position: absolute;
			right:0;
			top:8px;
			color:#fff;
		}
		.btn-primary:hover{
			background: #e9432d;
			color:#fff;
		}
		table tr th{
			color:#656565;
			background: #f4e6d4 !important;
		}
		table td .btn{
			padding:3px 11px ;
			background: #e5e5e5;
			color:#666;
		}
		table td .btn:hover{
			background:#e9432d ;
			color:#fff;
		}
	</style>
</head>
<body>
<div class="process-wrap" style="margin-bottom: 60px;">
	<div class="edit-bar clearfix">
		<div class="edit-bar-left">
			<span>流程模型</span>
			<i class="line weight-line"></i>
		</div>
	</div>
	<ul class="nav nav-tabs" style="margin-bottom: 0">
		<li class="active"><a href="${ctx}/act/model/">模型管理</a></li>
		<li><a href="${ctx}/act/model/create">新建模型</a></li>
		<%-- <li><a href="${ctx}/actyw/model/form">新建模型2</a></li> --%>
	</ul>
	<form id="searchForm" action="${ctx}/act/model/" method="post" class="breadcrumb form-search m-form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<select id="category" name="category" class="input-medium">
			<option value="">全部分类</option>
			<c:forEach items="${fns:getDictList('act_category')}" var="dict">
				<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
			</c:forEach>
		</select>
		&nbsp;<input id="btnSubmit" style="margin-right: 15px;" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<sys:message content="${message}"/>
	<table class="table table-bordered table-condensed table-nowrap">
		<thead>
			<tr>
				<th>流程分类</th>
				<th>模型名称</th>
				<th style="width:300px;">流程标识/模型ID</th>
				<!-- <th style="width: 200px;">模型标识</th>
				<th style="width: 200px;">模型ID</th> -->
				<th>版本号</th>
				<th>创建时间</th>
				<th>最后更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="model">
				<tr>
					<td style="width: 50px;"><a href="javascript:updateCategory('${model.id}', '${model.category}')" title="设置分类">${fns:getDictLabel(model.category,'act_category','无分类')}</a></td>
					<td>${model.name}</td>
					<td title="${model.key}">${model.key} ${model.id}</td>
					<%-- <td>${model.key}</td>
					<td>${model.id}</td> --%>
					<td style="width: 50px;"><b title='流程版本号'>V: ${model.version}</b></td>
					<td style="width: 50px;"><fmt:formatDate value="${model.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td style="width: 50px;"><fmt:formatDate value="${model.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td style="text-align: center;">
						<a href="${pageContext.request.contextPath}/act/process-editor/modeler.jsp?modelId=${model.id}" target="_blank" class="btn">编辑</a>
						<a href="${ctx}/act/model/deploy?id=${model.id}&isUpdateYw=true" onclick="return confirmx('确认要部署该模型吗？', this.href)" class="btn">部署</a><br>
						<a href="${ctx}/act/model/export?id=${model.id}" target="_blank" class="btn" style="margin-top: 5px;">导出</a>
	                    <%--<a href="${ctx}/act/model/delete?id=${model.id}" onclick="return confirmx('确认要删除该模型吗？', this.href)" class="btn">删除</a>--%>
						<a href="${ctx}/act/model/delete?id=${model.id}" class="btn" style="margin-top: 5px;">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	${page.footer}
	</div>
</body>
</html>
