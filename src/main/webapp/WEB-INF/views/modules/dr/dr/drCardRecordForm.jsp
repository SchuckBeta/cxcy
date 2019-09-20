<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
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
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>门禁卡记录</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="drCardRecord" action="${ctx}/dr/drCardRecord/save" method="post"
               class="form-horizontal">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <div class="control-group">
            <label class="control-label"><i>*</i>是否进门打卡：</label>
            <div class="controls">
                <form:select path="isEnter" class="required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                  htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">打卡类型 正常打打卡， 未注册打卡 ， 黑名单打卡：</label>
            <div class="controls">
                <form:select path="type" class="">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                  htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">打卡时间：</label>
            <div class="controls">
                <input name="pcTime" type="text" readonly="readonly" maxlength="20" class="Wdate "
                       value="<fmt:formatDate value="${drCardRecord.pcTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            </div>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="dr:drCardRecord:edit">
                <button class="btn btn-primary" type="submit">保存</button>
            </shiro:hasPermission>
            <button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
        </div>
    </form:form>
</div>

</body>
</html>