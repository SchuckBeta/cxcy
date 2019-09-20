<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<%@ attribute name="region" type="com.oseasy.cms.modules.cms.entity.CmsIndexRegion" required="true"
              description="栏目区域对象" %>
<%@ attribute name="oaNotifys" type="java.util.List" required="true" description="通知列表" %>
<%@ attribute name="type" type="java.lang.String" required="true" description="通知类型" %>
<%-- <%@ attribute name="detailUrl" type="java.lang.String" required="true" description="通知详情地址" %>
<%@ attribute name="moreUrl" type="java.lang.String" required="true" description="更多通知地址" %> --%>

<c:forEach var="resource" items="${region.childResourceList}">
    <div class="card sc-dyn-news">
        <div class="title"><a href="${ctxFront}/page-SCtognzhi#${type}">${resource.title}</a></div>
        <div class="photo">
            <a href="${ctxFront}/page-SCtognzhi#${type}"><img src="${fns:ftpImgUrl(resource.resUrl1)}"/></a>
        </div>
        <ul class="news-list">
            <c:forEach var="o" items="${oaNotifys}">
                <li><span class="date"><fmt:formatDate value="${o.updateDate }" pattern="yyyy-MM-dd"/></span><a
                        href="${ctxFront}/oa/oaNotify/viewDynamic?id=${o.id}" class="link"
                        title="${o.title }">${fns:abbr(o.title,50)}</a></li>
                <%-- <li><span class="date"><fmt:formatDate value="${o.effectiveDate }" pattern="yyyy-MM-dd" /></span><a href="${ctxFront}/oa/oaNotify/viewList?id=${o.id}" class="link" title="${o.title }">${fns:abbr(o.title,50)}</a></li> --%>
            </c:forEach>

        </ul>
    </div>
</c:forEach>