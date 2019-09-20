<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>项目通告管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/backtable.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css">
	<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${ctxJs}/common.js"></script>
	<style>
		.ui-dialog .ui-dialog-buttonpane button{
			font-size: 12px;
		}
		.btns {
			float: right !important;
		}
		.proCss {
			width: 150px;
		}
		tr {
			text-align: center;
		}
		td, th {
			text-align: center !important;
		}
		th {
			background: #f4e6d4 !important;
		}
		.breadcrumb {
			padding: 0px 0px !important;
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
		.ui-dialog .ui-dialog-titlebar-close{
			background-image: none;
		}
	</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#ps").val($("#pageSize").val());
		$('#sendMsgWrap tr').on('click','td .sendMsg',function(){
			var pid=$(this).parent().siblings('.first-line').find('input').val();
			var type=$(this).siblings('input').val();
			showModalMessage(0, '确定要发布吗？', {
				确定 : function() {
					$.ajax({
						url:"${ctx}/project/projectAnnounce/publish",
						type:"post",
						data:{"type":type},
						success:function(data){
							if(data>0){
								showModalMessage(0,"该类型项目已发布！");
								return false;
							}else{
								window.location.href="${ctx}/oa/oaNotify/formBroadcast?protype='1'&sId="+pid;
							}
						}
					})
					$(this).dialog("close");
				},
				取消 : function() {
					$(this).dialog("close");
					return false;
				}

			});

		})
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>

</head>
<body>
	<div class="mybreadcrumbs">
		<span>项目通告管理</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/project/projectAnnounce/">项目列表</a></li>
			<shiro:hasPermission name="project:projectAnnounce:edit">
				<li><a href="${ctx}/project/projectAnnounce/form">创建项目</a></li>
			</shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="projectAnnounce"
			action="${ctx}/project/projectAnnounce/" method="post"
			class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		</form:form>
		<sys:message content="${message}" />
		<table id="contentTable"
			class="table table-hover table-bordered table-condensed">
			<thead>
				<tr>
					<th class="proCss">项目名称</th>
					<th class="proCss">项目类型</th>
					<th class="proCss">项目申报</th>
					<th class="proCss">立项审核有效期</th>
					<th class="proCss">中期检查有效期</th>
					<th class="proCss">结项审核有效期</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="sendMsgWrap">
				<c:forEach items="${page.list}" var="projectAnnounce">
					<tr>
						<td class="first-line">${projectAnnounce.name }<input type="hidden"  value="${projectAnnounce.id }"></td>
						<td>
							${fns:getDictLabel(projectAnnounce.proType, 'project_style', '')}
						</td>
						<td style="width: 12%">
							<fmt:formatDate value="${projectAnnounce.beginDate}" pattern="yyyy-MM-dd" />
								<span>至</span>
							<fmt:formatDate value="${projectAnnounce.endDate}" pattern="yyyy-MM-dd" />
						</td>
						<td style="width: 12%">
							<fmt:formatDate value="${projectAnnounce.pIniStart}" pattern="yyyy-MM-dd" />

								<span>至</span>
							 <fmt:formatDate value="${projectAnnounce.pIniEnd}" pattern="yyyy-MM-dd" /></td>
						<td style="width: 12%">
							<fmt:formatDate value="${projectAnnounce.midStartDate}" pattern="yyyy-MM-dd" />
								<span>至</span>
							<fmt:formatDate value="${projectAnnounce.midEndDate}" 	pattern="yyyy-MM-dd" /></td>
						<td style="width: 12%">
							<fmt:formatDate value="${projectAnnounce.finalStartDate}" pattern="yyyy-MM-dd" />
								<span>至</span>
							<fmt:formatDate value="${projectAnnounce.finalEndDate}" pattern="yyyy-MM-dd" /></td>
						<td style="white-space: nowrap">
							<c:choose>
								<c:when test="${projectAnnounce.status=='0'}">
									发布
								</c:when>
								<c:when test="${projectAnnounce.status=='1'}">
									发布
								</c:when>
								<c:otherwise>
									未发布
								</c:otherwise>
							</c:choose>
						</td>

						<td style="white-space: nowrap">
							<c:choose>
								<c:when test="${projectAnnounce.status!='0' && projectAnnounce.status!='1' }">
									<a href="${ctx}/project/projectAnnounce/formEdit?id=${projectAnnounce.id}" class="btn">修改</a>
								</c:when>
								<c:otherwise>
									<a href="${ctx}/project/projectAnnounce/formEdit?id=${projectAnnounce.id}" class="btn">修改</a>
									<a href="${ctx}/project/projectAnnounce/form?id=${projectAnnounce.id}&operationType=1" class="btn">查看</a>
								</c:otherwise>
							</c:choose>
							<a href="${ctx}/project/projectAnnounce/delete?id=${projectAnnounce.id}" onclick="return confirmx('确认要删除该项目通告吗？', this.href)" class="btn">删除</a>
							<%--<c:if test="${projectAnnounce.status!='0' && projectAnnounce.status!='1' }">--%>
									<%--<a class="btn sendMsg">消息发布</a>--%>
							<%--</c:if>--%>
							<input type="hidden" id="type" value="${projectAnnounce.proType }"/>
						</td>

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