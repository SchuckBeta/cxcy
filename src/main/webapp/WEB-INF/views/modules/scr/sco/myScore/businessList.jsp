<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title>
	<meta charset="UTF-8">
	<meta name="decorator" content="cyjd-site-default"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/sco/frontCreditModule.css?v=1">
	<script type="text/javascript">
		$(function () {
			$("#ps").val($("#pageSize").val());
			$('#content').css('minHeight',$(window).height() - $('.header').height() - $('.footerBox').height())
			$('.pagination_num').removeClass('row')
		})

		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
		}


	</script>
	<style>

		.breadcrumb > li + li:before {
		  content: "\003e";
		  padding: 0 5px 0 3px;
		  color: #ccc; }

		.breadcrumb .icon-home:before {
		  content: "\f015";
		  margin-right: 7px; }
	</style>
</head>
<body>
<div class="container">
	<ol class="breadcrumb" style="margin-top: 60px">
		 <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
		 <li class="active">学分认定</li>
		 <li class="active">创业学分认定</li>
	 </ol>
	<div class="credit-title-bar clearfix">
		<h4 class="title">创业学分认定</h4>
	</div>
	<form id="searchForm" action="${ctxFront}/myscore/innovationList" autocomplete="off">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form>
	<div class="row-form-table">
		<table class="table table-bordered table-hover table-vertical-middle table-theme-default table-text-center">
			<thead>
			<tr>
				<th class="none-wrap">项目编号</th>
				<th>项目名称</th>
				<th class="none-wrap">负责人</th>
				<th>组成员</th>
				<th>项目类型</th>
				<th class="none-wrap">项目类别</th>
				<th class="none-wrap">学分类型</th>
				<th class="none-wrap">项目级别</th>
				<th class="none-wrap">项目结果</th>
				<th>学分认定标准</th>
				<th class="none-wrap">认定日期</th>
				<th class="none-wrap">认定学分</th>
				<th class="none-wrap">学分占比</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach  items="${page.list}" var="scoProjectVo">
				<tr>
					<td class="none-wrap">${scoProjectVo.projectDeclare.number}</td>
					<td class="text-left">${scoProjectVo.projectDeclare.name}</td>
					<td class="none-wrap"><span class="primary-color"> ${fns:getUserById(scoProjectVo.projectDeclare.leader).name}</span></td>
					<td>${scoProjectVo.teamUsers}</td>
					<td>${fns:getDictLabel(scoProjectVo.pType,"project_style" , "")}</td>
					<td class="none-wrap">${scoProjectVo.projectDeclare.typeString}</td>
					<td class="none-wrap">创业学分</td>
					<td class="none-wrap">${scoProjectVo.projectDeclare.levelString}</td>
					<td class="none-wrap">${scoProjectVo.projectDeclare.finalResultString}</td>
					<td class="none-wrap">
							${fns:deleteZero(scoProjectVo.scoAffirm.scoreStandard)}
					</td>
					<td class="none-wrap">
						<fmt:formatDate value="${scoProjectVo.scoAffirm.affirmDate}" pattern="yyyy-MM-dd" />
					</td>
					<td class="none-wrap">${fns:deleteZero(scoProjectVo.score)}</td>
					<td class="none-wrap">
						<c:if test="${scoProjectVo.percent<1}">
							<fmt:formatNumber type="percent" minFractionDigits="0" value="${scoProjectVo.percent}" />
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		${page.footer}
	</div>
</div>
</body>
</html>