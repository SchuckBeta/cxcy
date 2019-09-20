<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true" description="栏目区域对象" %>
<%@ attribute name="user" type="com.oseasy.com.pcore.modules.sys.entity.User" required="true" description="用户" %>
<c:if test="${region.id eq 'b6130eed9a404d34aa75925b549e840b'}">
    <c:forEach var="resource" items="${region.childResourceList}">
        <div class="notice-module">
            <div class="container w1240">
                <div id="myCarousel" class="carousel-notice carousel slide">
                    <input id="userId" type="hidden" value="${user }"/>
                    <!-- 轮播（Carousel）项目 -->
                    <div class="carousel-head">
                        <img class="icon-notice" src="${ctxImg}/index-notify2.png" title="${resource.title }">
                        <span style="color:#e04527;font-weight: bold;">通知公告：</span>
                    </div>
                    <div class="carousel-inner" id="wrap"></div>
                    <a class="in-notice-list" href="${ctxFront}/frontNotice/noticeList">更多<i class="icon  icon-double-angle-right"></i></a>
                </div>
            </div>
        </div>
    </c:forEach>
</c:if>
