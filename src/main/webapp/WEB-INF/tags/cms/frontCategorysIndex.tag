<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!--导航部分-->
<div class="nav-container" style="margin-left: 344px; margin-right: auto">
    <ul class="nav-menu">
        <c:forEach var="pitem" items="${fns:getCategorysIndex()}" varStatus="idx">
                <c:if test="${idx.index < 7 }">
                    <%--<c:if test="${fns: hasDSSH(pitem.href)}">--%>
                    <li class="nav-menu-item">
                        <a href="${empty pitem.href ? 'javascript:void(0);' : fns: addCtxFront(ctxFront, pitem.href, pitem.id)}"
                           target="${pitem.isNewtab eq '1' ? '_blank' : '_self'}">
                                ${pitem.name}
                        </a>
                        <c:if test="${not empty pitem.childList}">
                            <ul class="nav-second-menu">
                                <c:forEach var="sitem" items="${pitem.childList}" varStatus="idxs">
                                    <li class="nav-second-menu-item">
                                        <c:if test="${fns: hasDSSH(sitem.href)}">
                                            <a href="${empty sitem.href ? 'javascript:void(0);' :  fns: addCtxFront(ctxFront, sitem.href, sitem.id)}"
                                               target="${sitem.isNewtab eq '1' ? '_blank' : '_self'}">
                                                    ${sitem.name}
                                                <c:if test="${not empty sitem.childList}">
                                                    <i class="el-icon-d-arrow-right"></i>
                                                </c:if>
                                            </a>
                                        </c:if>
                                        <c:if test="${not empty sitem.childList}">
                                            <ul class="nav-three-menu">
                                                <c:forEach var="titem" items="${sitem.childList}" varStatus="idxt">
                                                    <li class="nav-three-menu-item">
                                                        <a href="${empty titem.href ? 'javascript:void(0);' : fns: addCtxFront(ctxFront, titem.href, titem.id)}"
                                                           target="${titem.isNewtab eq '1' ? '_blank' : '_self'}">
                                                                ${titem.name}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                    <%--</c:if>--%>
                </c:if>
        </c:forEach>
    </ul>
</div>
