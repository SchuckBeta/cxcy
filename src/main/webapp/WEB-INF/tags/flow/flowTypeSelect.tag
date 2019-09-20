<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ attribute name="typeList" type="java.util.List" required="true" description="属性list" %>
<%@ attribute name="type" type="java.lang.String" required="true" description="属性类型" %>
<%@ attribute name="name" type="java.lang.String" required="true" description="属性名称" %>
<%@ attribute name="divClass" type="java.lang.String" required="true" description="div样式" %>
<%@ attribute name="formClass" type="java.lang.String" required="false" description="div样式" %>
<%-- <%@ attribute name="detailUrl" type="java.lang.String" required="true" description="通知详情地址" %>
<%@ attribute name="moreUrl" type="java.lang.String" required="true" description="更多通知地址" %> --%>
<c:if test="${typeList!=null}">
	<label class="control-label" for="${type}"><i class="icon-require">*</i>${name}：</label>
	<div class="${divClass}">
		<form:select path="${type}" class="form-control input-large required ${formClass}">
			<option value="" />请选择</option>
			<%--<c:forEach var="typeMap" items="${typeList}">
			<option value="${typeMap.key}">${typeMap.value}</option>
			</c:forEach>--%>

			<form:options items="${typeList}" itemLabel="label"
		    	itemValue="value" htmlEscape="false"/>
		</form:select>
	</div>
</c:if>