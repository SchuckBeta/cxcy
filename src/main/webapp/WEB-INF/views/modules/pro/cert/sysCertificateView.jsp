<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
</head>
<body>
	<div class="mybreadcrumbs">
		<span>系统查看</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysCertificate/">系统证书列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysCertificate/view?id=${sysCertificate.id}">系统证书查看</a></li>
		</ul><br/>
		<div>
			证书编号:${sysCertificate.no}<br/>
			证书名称:${sysCertificate.name}<br/>
			证书类型:${sysCertificate.type}<br/>
			所属机构:${sysCertificate.office.name}<br/>
			使用中:${sysCertificate.hasUse}<br/>
			<c:if test="${fn:length(sysCertificate.sysCertRels) > 0}">
				<table style="padding-left: 15px;" border="1" bordercolor="#000000">
					<thead>
						<tr>
							<td style="width: 300px;">图片</td>
							<td style="width: 300px;">资源名称</td>
							<td style="width: 300px;">资源ID</td>
							<td style="width: 80px;">资源类型</td>
							<td style="width: 80px;">宽度</td>
							<td style="width: 80px;">高度</td>
							<td style="width: 80px;">X坐标</td>
							<td style="width: 80px;">Y坐标</td>
							<td style="width: 80px;">透明度</td>
							<td style="width: 80px;">角度</td>
							<td style="width: 80px;">是否平铺</td>
							<td style="width: 80px;">是否显示</td>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="sysCertRel" items="${sysCertificate.sysCertRels }">
						<tr>
							<td><img src="${fns:ftpImgUrl(sysCertRel.sysCertRes.file.url) }" height="50px"/></td>
							<td>${sysCertRel.sysCertRes.name}</td>
							<td>${sysCertRel.sysCertRes.id}</td>
							<td>${sysCertRel.sysCertRes.type}</td>
							<td>${sysCertRel.sysCertRes.width}</td>
							<td>${sysCertRel.sysCertRes.height}</td>
							<td>${sysCertRel.sysCertRes.xlt}</td>
							<td>${sysCertRel.sysCertRes.ylt}</td>
							<td>${sysCertRel.sysCertRes.opacity}</td>
							<td>${sysCertRel.sysCertRes.rate}</td>
							<td>${sysCertRel.sysCertRes.hasLoop}</td>
							<td>${sysCertRel.sysCertRes.isShow}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</c:if>

			<c:if test="${fn:length(sysCertificate.sysCertRels) <= 0}">
				<div style="padding-left: 15px;"><a href="${ctx}/sys/sysCertificate/design?id=${sysCertificate.id}">没有资源文件，点我去添加</a></div>
			</c:if>
		</div>
	</div>
</body>
</html>