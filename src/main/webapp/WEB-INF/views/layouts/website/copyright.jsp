<%@ page contentType="text/html;charset=UTF-8" %>
<div class="footer-box-copyright">
    <p class="copyright">
        <c:choose>
            <c:when test="${fnc:getAutoSite().copyright != null && fnc:getAutoSite().copyright != ''}">
            <span class="white-space-pre-static">${fnc:getAutoSite().copyright}</span><br>
                <c:if test="${fnc:getAutoSite().copyright.indexOf('噢易') == -1}">技术支持:武汉噢易云计算股份有限公司</c:if>
                <a href="http://${fns:getSysFrontIp()}">进入门户网站</a>
            </c:when>
            <c:otherwise>
                Copyright © 2015-2019 :武汉噢易云计算股份有限公司 <br>
                <a href="http://${fns:getSysFrontIp()}">进入门户网站</a>
            </c:otherwise>
        </c:choose>
    </p>
</div>
<style>
    .white-space-pre-static{
        white-space: pre;
        word-wrap: break-word;
    }
</style>