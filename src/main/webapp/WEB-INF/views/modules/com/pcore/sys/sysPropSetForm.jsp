<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/fullcalendar/moment.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#isWarmCheck").on("change", function () {
                //点击之后如果变成打钩时触发
                if ($(this).attr("checked") == "checked") {
                    $("#isWarm").val("1");
                }
                //点击之后变为不是打钩时触发
                else{
                    $("#isWarm").val("0");
                }
            });

            $("#isHatbackCheck").on("change", function () {
                //点击之后如果变成打钩时触发
                if ($(this).attr("checked") == "checked") {
                    $("#isHatback").val("1");
                }
                //点击之后变为不是打钩时触发
                else{
                    $("#isHatback").val("0");
                }
            });

            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else if (element.attr('name') === 'afterDays' || element.attr('name') === 'autoTime') {
                        error.appendTo(element.parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<di>${sysPropAA}1111</di>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>${sysProp.title }1111</span>
            <i class="line weight-line"></i>
        </div>
       	<span>${sysProp.micaPanic }</span>
        <form id="inputForm" modelAttribute="sysProp" action="${ctx}/sys/sysProp/savePropItem"class="form-horizontal form-horizontal-rule" cssStyle="padding-left: 90px;">
        <sys:message content="${message}"/>
		<c:if test="${not empty sysProp.items }">
			<c:forEach var="pitem" items="${sysProp.items }">

		   	        <input type="hidden" name="items.id" value="${pitem.id }">
		            <div class="control-group" style="position: relative;padding-top: 8px;">
		                <div class="controls-checkbox"
		                     style="padding: 25px 40px 10px 15px; white-space: nowrap; border-radius: 5px; border: 1px solid #ddd;display: inline-block">
		                    <div class="control-group"  style="width: 634px;">
		                        <div class="controls"  style="margin-left: 20px;">
		                            <input type="hidden" id="isWarm" name="${pitem.isOpen }" value="${pitem.openval}">
		                            <label class="checkbox inline" style="vertical-align: top;">
		                                <%--<input id="isWarmCheck" <c:if test="${pitem.isOpen eq'1'}"> checked="checked" </c:if>--%>
                                               <%--style="vertical-align: top" type="checkbox">--%>
		           						<c:forEach var="varMap" items="${pitem.varMaps }">

											${varMap.preHtml}
                                            <c:if test="${(not empty varMap.var)}">
                                            <input name="${varMap.var}" value="${varMap.val}" class="required input-mini number digits"/>
                                            </c:if>

                                        </c:forEach>
	           						</label>
		                        </div>
		                    </div>
		                </div>
						<span style="position: absolute;left: 15px; top: 0; background-color: #fff; padding: 0 8px;">
                            <input id="isWarmCheck" <c:if test="${pitem.isOpen eq'1'}"> checked="checked" </c:if>
                                                                           style="vertical-align: top" type="checkbox">
						${pitem.name }</span>
		            </div>

				</c:forEach>
			</c:if>
            <div class="form-actions" style="padding-left: 390px;margin-left: -90px;">
                <input class="btn btn-primary" type="submit" value="保 存"/>
            </div>
        </form>
    </div>
</div>

<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
</body>
</html>