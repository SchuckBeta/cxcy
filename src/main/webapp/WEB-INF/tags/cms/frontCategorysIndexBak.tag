<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!--导航部分-->
<ul id="header_info" style="margin-bottom: 0px;">
    <c:forEach var="pitem" items="${fns:getCategorysIndex() }" varStatus="idx">
            <c:if test="${idx.index < 7 }">
                <li>
                    <c:if test="${empty pitem.href}">
                        <a href="javascript:void(0);" <%--target="${pitem.target}"--%>><span>${pitem.name }</span></a>
                    </c:if>
                    <c:if test="${not empty pitem.href}">
                        <a href="${ctxFront}/${pitem.href}" <%--target="${pitem.target}"--%>><span>${pitem.name }</span></a>
                    </c:if>
                    <c:if test="${fn:length(pitem.childList) > 0}">
                        <div class="secondlever">
                            <p>
                                <c:forEach var="item" items="${pitem.childList }">
                                    <c:if test="${empty item.href}">
                                        <a href="javascript:void(0);" <%--target="${item.target}"--%>>●${item.name }</a>
                                    </c:if>
                                    <c:if test="${not empty item.href}">
                                    	<c:if test="${fn:contains(item.href,'javascript:')}">
                                    		<a href="${item.href}" <%--target="${item.target}"--%>>●${item.name }</a>
                                    	</c:if>
                                        <c:if test="${not fn:contains(item.href,'javascript:')}">
                                        	<a href="${ctxFront}/${item.href}" <%--target="${item.target}"--%>>●${item.name }</a>
                                    	</c:if>
                                    </c:if>
                                </c:forEach>
                            </p>
                        </div>
                    </c:if>
                </li>
            </c:if>
    </c:forEach>
</ul>
