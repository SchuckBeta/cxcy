<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true" description="栏目区域对象"%>
		<c:if test="${region.id eq '50a693eb248f4d1192f8ede7f3d44dd2'}">
			<c:set var="res1" value="${region.childResourceList[0]}" />
			<c:set var="res2" value="${region.childResourceList[1]}" />
			<c:set var="res3" value="${region.childResourceList[2]}" />
			<!--大赛热点-->
			<div class="hotspotBox">
				<c:if test="${not empty res1.resUrl1}">
				<!--标题-->
				<a href="${ctxFront }/${res1.jumpUrl1 }"><img class="title" src="${fns:ftpImgUrl(res1.resUrl1)}" /></a>
				</c:if>
				<c:if test="${empty res1}">大赛热点的标题必填</c:if>

				<!--大赛热点内容部分-->
				<div class="content">
					<!--视频播放-->
					<div class="leftVideoBox">
						<video id="media" height="413" preload="metadata" controls="controls" poster="${fns:ftpImgUrl(res2.resUrl1)}">
							<source src="${res2.resUrl2 }" type="video/mp4"></source>
							当前浏览器不支持 video直接播放，请使用高版本的浏览器查看。
						</video>
						<!--视频标题-->
						<p>${res2.content }</p>
						<!--播放按钮-->
						<img class="start" src="${ctxImg}/start.png" />
					</div>

					<!--轮播-->
					<div class="rightScrollBox">
						<div id="slideBox2" class="slideBox2">
							<div class="bd">
								<c:if test="${not empty res3}">
								<ul>
									<c:if test="${not empty res3.resUrl1 }"><li><img src="${fns:ftpImgUrl(res3.resUrl1)}"></li></c:if>
									<c:if test="${not empty res3.resUrl1 }"><li><img src="${fns:ftpImgUrl(res3.resUrl2)}"></li></c:if>
									<c:if test="${not empty res3.resUrl1 }"><li><img src="${fns:ftpImgUrl(res3.resUrl3)}"></li></c:if>
								</ul>
								</c:if>
							</div>
							<a class="prev" href="javascript:void(0)"></a> <a class="next" href="javascript:void(0)"></a>
						</div>
					</div>
				</div>
			</div>
		</c:if>
