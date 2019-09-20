<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true" description="栏目区域对象"%>
<c:if test="${region.id eq '7191db760b47448483f15983cc6d2d11'}">
<!--banner部分-->
<div class="bannerBox">
	<div id="slideBox" class="slideBox">
		<div class="bd">
			<ul>
				<c:forEach var="resource" items="${region.childResourceList}">
					<li>
					<p>${resource.title }</p> <img src="${resource.resUrl1 }"> <a
					class="btn1" href="${resource.jumpUrl1 }">${resource.botton1Name }</a> <a class="btn2"
					href="${resource.jumpUrl2 }">${resource.botton2Name }</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
</c:if>