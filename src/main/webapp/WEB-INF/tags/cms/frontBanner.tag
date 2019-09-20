<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true" description="栏目区域对象"%>
<c:if test="${region.id eq '7191db760b47448483f15983cc6d2d11'}">
<!--banner部分-->
<%-- <div class="bannerBox">
	<div id="slideBox" class="slideBox">
		<div class="bd">
			<ul>
				<c:forEach var="resource" items="${region.childResourceList}" begin="1" end="1">
					<li>
					<p>${resource.title }</p> <img src="${ctxImg}/index4-banner.png"> <a
					class="btn1" href="http://qbview.url.cn/getResourceInfo?appid=31&url=http%3A%2F%2Fgdjyc.gov.hnedu.cn%2Fc%2F2017-05-15%2F871941.shtml%3Ffrom%3Dtimeline%26isappinstalled%3D1%26nsukey%3D6SjwyPfiLT5hUeT4Kpp%252FX1GcJ%252FZZD%252FPVOObbSOMEkC%252F%252BwexIhb%252Fw76dvdW8cI9gDHXGxupzvCwyhoXLLdxmwwNT%252F7fB8FKrym%252BR63MXaqDJ4OWARvXFhW6lfd%252BENMRR3zrfPcZFdq4sHJ5CoZf6Sy5iFWkklBLzaKQL9uHGZSuWzSrXTfA8JaAeaDtbY4YqW&version=10000&doview=1&ua=Mozilla%2F5.0+(iPhone%3B+CPU+iPhone+OS+10_3_1+like+Mac+OS+X)+AppleWebKit%2F603.1.30+(KHTML%2C+like+Gecko)+Mobile%2F14E304+MicroMessenger%2F6.5.7+NetType%2F4G+Language%2Fzh_CN&keeplink=0&reformat=0&from=timeline&isappinstalled=0" target="_blank">${resource.botton1Name }</a> <a class="btn2"
					href="${ctxFront}/project/projectDeclare/form">${resource.botton2Name }</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div> --%>

<div class="bannerBox">
	<div id="slideBox" class="slideBox">
		<div class="bd">
			<ul>
				<c:forEach var="resource" items="${region.childResourceList}">
					<li>
					<p>${resource.title }</p>
						<img src="${fns:ftpImgUrl(resource.resUrl1)}">
						<%--<a class="btn1" href="${ctxFront}/${resource.jumpUrl1 }">${resource.botton1Name }</a>--%>
						<a class="btn2" href="${ctxFront}/${resource.jumpUrl2 }">${resource.botton2Name }</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		<!-- 圆点 分页 开始 -->
		<div class="hd">
			　<ul></ul>
		</div>
		<!-- 圆点 分页 结束 -->
		<a class="prev" href="javascript:void(0)"></a>
		<a class="next" href="javascript:void(0)"></a>
	</div>
</div>
</c:if>
