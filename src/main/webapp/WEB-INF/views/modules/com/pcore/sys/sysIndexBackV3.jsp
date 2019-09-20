<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<!--公用重置样式文件-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>
		<!--头部样式文件公共部分-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header2.css"/>
		<!--manage页面样式文件-->
		<link rel="stylesheet" type="text/css" href="${ctxCss}/manage.css"/>
		<!--引用装饰器-->
		<title>${backgroundTitle}</title>
		<meta name="decorator" content="backPath_header"/>
	</head>
	<body>
		<!--------下部------->
		<div class="down">
			<!--------左边------->
			<div class="left">
					<c:set var="menuList" value="${fns:getMenuList()}"/>
					<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
						<c:if test="${menu.id eq param.parentId}">
						<img class="borderimg" src="${ctxImg}/manageborderimg_04.png" alt="" />
						<div class="title">
							<img src="${ctxImg}/manageLeftTitleIcon.png" alt="" />
							<big>${menu.name}</big>
						</div>
						<img class="borderimg" src="${ctxImg}/manageborderimg_04.png" alt="" />
						</c:if>
					</c:forEach>
				
				<ul>
					<c:set var="firstMenu" value="true"/>
					<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
						<c:if test="${menu.parent.id eq (not empty param.parentId ? param.parentId:1)&&menu.isShow eq '1'}">
							<c:forEach items="${menuList}" var="menu2">
								<c:if test="${menu2.parent.id eq menu.id&&menu2.isShow eq '1'}">
									<a href="${fn:indexOf(menu2.href, '://') eq -1 ? ctx : ''}${not empty menu2.href ? menu2.href : '/404'}" target="${not empty menu2.target ? menu2.target : 'mainFrame'}" >
												<li>${menu2.name}<img src="${ctxImg}/manageLeftArrow.png" alt="" /></li>
									</a>
									<img class="borderimg" src="${ctxImg}/manageborderimg_04.png" alt="" />
									<c:forEach items="${menuList}" var="menu3">
										<c:if test="${menu3.parent.id eq menu2.id&&menu3.isShow eq '1'}">
											<a href="${fn:indexOf(menu3.href, '://') eq -1 ? ctx : ''}${not empty menu3.href ? menu3.href : '/404'}" target="${not empty menu3.target ? menu3.target : 'mainFrame'}" >
												<li>${menu3.name}<img src="${ctxImg}/manageLeftArrow.png" alt="" /></li>
											</a>
											<img class="borderimg" src="${ctxImg}/manageborderimg_04.png" alt="" />
										</c:if>
									</c:forEach>
									<c:set var="firstMenu" value="false"/>
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
				</ul>
			</div>
			<!--------右边------->
			<div class="right">
				<iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="100%"></iframe>
			</div>
		</div>
	<!--js：1.11.3-->
	<script src="${ctxJs}/manage.js" type="text/javascript" charset="utf-8"></script>
	<!----------页面js初始化----------->
	<script type="text/javascript">
		$(function(){
			var oScreenHieght=document.documentElement.clientHeight||document.body.clientHeight;
			$('.down').height(oScreenHieght-93);
			$('.right').height(oScreenHieght-93);
		})
	</script>
	</body>
</html>
